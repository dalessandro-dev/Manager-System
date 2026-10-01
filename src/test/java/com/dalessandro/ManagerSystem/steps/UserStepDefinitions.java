package com.dalessandro.ManagerSystem.steps;

import com.dalessandro.ManagerSystem.models.UserModel;
import com.dalessandro.ManagerSystem.models.enums.Gender;
import com.dalessandro.ManagerSystem.models.enums.Role;
import com.dalessandro.ManagerSystem.repositories.UserRepository;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String userPayloadJson;
    private ResultActions response;
    private UserModel userToUpdate;
    private Long deletedUserId;

    @Before
    public void setup() {
        userRepository.deleteAll();
    }

    // ASSERÇÕES E PASSOS GENÉRICOS (REUTILIZÁVEIS)

    @Entao("o status de resposta deve ser {int}")
    public void oStatusDeveSer(int statusCode) throws Exception {
        response.andExpect(status().is(statusCode));
    }

    @E("o corpo da resposta deve conter os seguintes campos:")
    public void oCorpoDaRespostaDeveConterOsSeguintesCampos(Map<String, String> expectedFields) throws Exception {
        for (Map.Entry<String, String> entry : expectedFields.entrySet()) {
            response.andExpect(jsonPath("$." + entry.getKey()).value(entry.getValue()));
        }
    }

    @E("o campo {string} deve estar preenchido na resposta")
    public void oCampoDeveEstarPreenchidoNaResposta(String field) throws Exception {
        response.andExpect(jsonPath("$." + field).value(notNullValue()));
    }

    @E("o campo de erro {string} deve conter a mensagem {string}")
    public void oCampoDeErroDeveConterAMensagem(String field, String errorMessage) throws Exception {
        response.andExpect(jsonPath("$.fieldErrors." + field).value(errorMessage));
    }

    // CENÁRIOS: CADASTRO (POST /users)

    @Dado("que eu tenho os dados válidos de um novo usuário")
    public void queEuTenhoOsDadosValidosDeUmNovoUsuario() {
        userPayloadJson = """
            {
                "name": "Dalessandro",
                "username": "dale",
                "email": "dale@example.com",
                "cpf": "99298830025",
                "birthDate": "2000-01-01",
                "password": "123456789",
                "nationality": "Brasileiro",
                "gender": "M",
                "phone": "85999999999"
            }
        """;
    }

    @Dado("que eu tenho os dados de um novo usuário com campos inválidos")
    public void queEuTenhoOsDadosDeUmNovoUsuarioComCamposInvalidos() {
        userPayloadJson = """
            {
                "name": "",
                "username": "d",
                "email": "email-invalido",
                "cpf": "123",
                "birthDate": "1885, 01,01",
                "password": "123",
                "nationality": "Brasileiro",
                "gender": "M",
                "phone": "85999999999"
            }
        """;
    }

    @Dado("que já existe um usuário cadastrado com o e-mail {string}")
    public void queJaExisteUmUsuarioCadastradoComOEmail(String email) {
        UserModel user = buildUser(
                "Usuário Existente", "existente_email", email, "11122233344",
                LocalDate.of(1990, 1, 1), Gender.M, Role.EMPLOYEE, "85988881111"
        );
        userRepository.save(user);
    }

    @E("que eu tento cadastrar um novo usuário com o e-mail {string}")
    public void queEuTentoCadastrarUmNovoUsuarioComOEmail(String email) {
        userPayloadJson = String.format("""
            {
                "name": "Outro Usuário",
                "username": "outro_usuario",
                "email": "%s",
                "cpf": "99298830025",
                "birthDate": "1995-05-05",
                "password": "12345678",
                "nationality": "Brasileiro",
                "gender": "M",
                "phone": "85977772222"
            }
        """, email);
    }

    @Dado("que já existe um usuário cadastrado com o CPF {string}")
    public void queJaExisteUmUsuarioCadastradoComOCpf(String cpf) {
        UserModel user = buildUser(
                "Usuário com CPF", "existente_cpf", "outro_email_cadastrado@example.com", cpf,
                LocalDate.of(1992, 3, 15), Gender.M, Role.EMPLOYEE, "85966663333"
        );
        userRepository.save(user);
    }

    @E("que eu tento cadastrar um novo usuário com o CPF {string}")
    public void queEuTentoCadastrarUmNovoUsuarioComOCpf(String cpf) {
        userPayloadJson = String.format("""
            {
                "name": "Tentativa Novo",
                "username": "tentativa_novo",
                "email": "tentativa_novo@example.com",
                "cpf": "%s",
                "password": "12345678",
                "birthDate": "2000-01-01",
                "nationality": "Brasileiro",
                "gender": "M",
                "phone": "85955554444"
            }
        """, cpf);
    }

    @Quando("eu enviar uma requisição POST para {string}")
    public void euEnviarUmaRequisicaoPOSTPara(String url) throws Exception {
        response = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userPayloadJson));
    }

    // CENÁRIOS: CONSULTA (GET /users)

    @Dado("que existem os seguintes usuários cadastrados no sistema:")
    public void queExistemOsSeguintesUsuariosCadastradosNoSistema(List<Map<String, String>> usersTable) {
        for (Map<String, String> row : usersTable) {
            UserModel user = buildUser(
                    row.get("name"), row.get("username"), row.get("email"), row.get("cpf"),
                    LocalDate.of(1995, 1, 1), Gender.M, Role.CLIENT, row.get("phone")
            );
            userRepository.save(user);
        }
    }

    @Quando("eu enviar uma requisição GET para {string}")
    public void euEnviarUmaRequisicaoGETPara(String url) throws Exception {
        response = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @E("a lista de usuários deve conter {int} itens")
    public void aListaDeUsuariosDeveConterItens(int expectedSize) throws Exception {
        response.andExpect(jsonPath("$.content", hasSize(expectedSize)));
    }

    @E("todos os usuários retornados devem conter o nome {string}")
    public void todosOsUsuariosRetornadosDevemConterONome(String nameFragment) throws Exception {
        response.andExpect(jsonPath("$.content[*].name", everyItem(containsString(nameFragment))));
    }

    // CENÁRIOS: ATUALIZAÇÃO (PUT /users/{id})

    @Dado("que existe um usuário cadastrado para atualização")
    public void queExisteUmUsuarioCadastradoParaAtualizacao() {
        UserModel user = buildUser(
                "Nome Original", "username_original", "original@example.com", "83043697022",
                LocalDate.of(1996, 6, 15), Gender.M, Role.CLIENT, "85977778888"
        );
        userToUpdate = userRepository.save(user);
    }

    @Dado("que existe um usuário cadastrado com o username {string}")
    public void queExisteUmUsuarioCadastradoComOUsername(String username) {
        UserModel user = buildUser(
                "Outro Usuário", username, "outro_username@example.com", "01904921043",
                LocalDate.of(1994, 4, 12), Gender.M, Role.CLIENT, "85966665551"
        );
        userRepository.save(user);
    }

    @Quando("eu enviar uma requisição PUT para atualizar esse usuário com os dados:")
    public void euEnviarUmaRequisicaoPUTParaAtualizarEsseUsuarioComOsDados(Map<String, String> updateFields) throws Exception {
        String updatePayloadJson = objectMapper.writeValueAsString(updateFields);

        response = mockMvc.perform(put("/users/" + userToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayloadJson));
    }

    // CENÁRIOS: DELEÇÃO (DELETE /users/{id})

    @Dado("que existe um usuário cadastrado para deleção")
    public void queExisteUmUsuarioCadastradoParaDelecao() {
        UserModel user = buildUser(
                "Usuário para Deletar", "para_deletar", "deletar@example.com", "12398745600",
                LocalDate.of(1998, 7, 10), Gender.M, Role.CLIENT, "85981112233"
        );
        UserModel savedUser = userRepository.save(user);
        deletedUserId = savedUser.getId();
    }

    @Quando("eu enviar uma requisição DELETE para remover esse usuário")
    public void euEnviarUmaRequisicaoDELETEParaRemoverEsseUsuario() throws Exception {
        response = mockMvc.perform(delete("/users/" + deletedUserId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Quando("eu enviar uma requisição DELETE para {string}")
    public void euEnviarUmaRequisicaoDELETEPara(String url) throws Exception {
        response = mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @E("o usuário não deve mais existir no banco de dados")
    public void oUsuarioNaoDeveMaisExistirNoBancoDeDados() {
        boolean exists = userRepository.existsById(deletedUserId);
        assertFalse(exists, "O usuário ainda existe no banco após a deleção");
    }

    private UserModel buildUser(String name, String username, String email, String cpf,
                                LocalDate birthDate, Gender gender, Role role, String phone) {
        UserModel user = new UserModel();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setCpf(cpf);
        user.setBirthDate(birthDate);
        user.setNationality("Brasileiro");
        user.setGender(gender);
        user.setRole(role);
        user.setPassword("123456789");
        user.setPhone(phone);
        return user;
    }
}