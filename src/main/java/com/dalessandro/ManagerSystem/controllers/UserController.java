package com.dalessandro.ManagerSystem.controllers;

import com.dalessandro.ManagerSystem.dtos.*;
import com.dalessandro.ManagerSystem.models.UserModel;
import com.dalessandro.ManagerSystem.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/hello")
    public String handle() {
        return "Hello world";
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        UserResponseDto createdUser = userService.createUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<PageResponseDto<UserResponseDto>> getUsers(
            UserFilterDto filter,
            @PageableDefault(size = 10, sort = "name") Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getUsers(pageable, filter));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDto dto) {
        UserResponseDto updatedUser = userService.updateUser(id, dto);

        return ResponseEntity.ok(updatedUser);
    }
}
