package com.dalessandro.ManagerSystem.specifications;

import com.dalessandro.ManagerSystem.dtos.UserFilterDto;
import com.dalessandro.ManagerSystem.models.UserModel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<UserModel> withFilters(UserFilterDto filter) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.name() != null && !filter.name().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + filter.name().toLowerCase().trim() + "%"
                ));
            }

            if (filter.email() != null && !filter.email().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + filter.email().toLowerCase().trim() + "%"
                ));
            }

            if (filter.phone() != null && !filter.phone().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("phone")),
                        "%" + filter.phone().toLowerCase().trim() + "%"
                ));
            }

            if (filter.nationality() != null && !filter.nationality().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nationality")),
                        "%" + filter.nationality().toLowerCase().trim() + "%"
                ));
            }

            if (filter.gender() != null && !filter.gender().isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("gender")),
                        filter.gender().toLowerCase().trim()
                ));
            }

            if (filter.birthDate() != null && !filter.birthDate().isBlank()) {
                LocalDate parsedDate = LocalDate.parse(filter.birthDate().trim());

                predicates.add(criteriaBuilder.greaterThan(
                        root.get("birthDate"),
                        parsedDate
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
