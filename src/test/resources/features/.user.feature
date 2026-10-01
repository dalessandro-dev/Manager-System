# language: pt
Funcionalidade: Gerenciamento de usuários
    Como cliente da API,
    Quero gerenciar os usuários do sistema
    Para manter a base de dados atualizada

    Cenário: Cadastrar um novo usuário com sucesso
        Dado que eu tenho os dados válidos de um novo usuário
        Quando eu enviar uma requisição POST para "/users"
        Então o status de resposta deve ser 201
        E o corpo da resposta deve conter os seguintes campos:
            | name        | Dalessandro      |
            | username    | dale             |
            | email       | dale@example.com |
            | cpf         | 99298830025      |
            | birthDate   | 2000-01-01       |
            | nationality | Brasileiro       |
            | gender      | M                |
            | phone       | 85999999999      |
        E o campo "createdAt" deve estar preenchido na resposta
        E o campo "updatedAt" deve estar preenchido na resposta

    Cenário: Cadastrar um novo usuário com sucesso (versão com erro)
        Dado que eu tenho os dados válidos de um novo usuário
        Quando eu enviar uma requisição POST para "/users"
        Então o status de resposta deve ser 201
        E o corpo da resposta deve conter os seguintes campos:
            | name        | Dalessandro  |
            | username    | dale         |
            | email       | dalemple.com |
            | cpf         | 99298830025  |
            | birthDate   | 2000-01-01   |
            | nationality | Brasileiro   |
            | gender      | M            |
            | phone       | 85999999999  |
        E o campo "createdAt" deve estar preenchido na resposta
        E o campo "updatedAt" deve estar preenchido na resposta

    Cenário: Falha ao cadastrar usuário com dados inválidos
        Dado que eu tenho os dados de um novo usuário com campos inválidos
        Quando eu enviar uma requisição POST para "/users"
        Então o status de resposta deve ser 400
        E o corpo da resposta deve conter os seguintes campos:
            | error   | Validation Failed              |
            | message | One or more fields are invalid |
        E o campo de erro "email" deve conter a mensagem "Email must be valid"

    Cenário: Falha ao cadastrar usuário com e-mail já existente
        Dado que já existe um usuário cadastrado com o e-mail "dale@example.com"
        E que eu tento cadastrar um novo usuário com o e-mail "dale@example.com"
        Quando eu enviar uma requisição POST para "/users"
        Então o status de resposta deve ser 409
        E o corpo da resposta deve conter os seguintes campos:
            | error   | Resource Conflict                       |
            | message | E-mail already in use: dale@example.com |

    Cenário: Falha ao cadastrar usuário com CPF já existente
        Dado que já existe um usuário cadastrado com o CPF "99298830025"
        E que eu tento cadastrar um novo usuário com o CPF "99298830025"
        Quando eu enviar uma requisição POST para "/users"
        Então o status de resposta deve ser 409
        E o corpo da resposta deve conter os seguintes campos:
            | error   | Resource Conflict               |
            | message | Cpf already in use: 99298830025 |

    Cenário: Deletar um usuário existente com sucesso
        Dado que existe um usuário cadastrado para deleção
        Quando eu enviar uma requisição DELETE para remover esse usuário
        Então o status de resposta deve ser 204
        E o usuário não deve mais existir no banco de dados

    Cenário: Falha ao deletar usuário que não existe
        Quando eu enviar uma requisição DELETE para "/users/999999"
        Então o status de resposta deve ser 404
        E o corpo da resposta deve conter os seguintes campos:
            | error   | Resource not found                         |
            | message | User with the value '999999' was not found |

    Cenário: Listar todos os usuários cadastrados
        Dado que existem os seguintes usuários cadastrados no sistema:
            | name        | username | email             | cpf         | phone       |
            | Ana Silva   | anasilva | ana@example.com   | 83043697022 | 85999991111 |
            | Bruno Souza | brunos   | bruno@example.com | 01904921043 | 85999992222 |
        Quando eu enviar uma requisição GET para "/users"
        Então o status de resposta deve ser 200
        E a lista de usuários deve conter 2 itens

    Cenário: Listar usuários filtrando por nome
        Dado que existem os seguintes usuários cadastrados no sistema:
            | name            | username | email              | cpf         | phone       |
            | Carlos Ferreira | cferr    | carlos@example.com | 01904921043 | 85999991111 |
            | Carlos Eduardo  | cedu     | cedu@example.com   | 83043697022 | 85999992222 |
            | Daniel Alves    | dalves   | daniel@example.com | 27983525095 | 85999990000 |
        Quando eu enviar uma requisição GET para "/users?name=Carlos"
        Então o status de resposta deve ser 200
        E a lista de usuários deve conter 2 itens
        E todos os usuários retornados devem conter o nome "Carlos"

    Cenário: Atualizar dados de um usuário com sucesso
        Dado que existe um usuário cadastrado para atualização
        Quando eu enviar uma requisição PUT para atualizar esse usuário com os dados:
            | name        | Novo Nome Atualizado |
            | nationality | Português            |
            | phone       | 85988889999          |
        Então o status de resposta deve ser 200
        E o corpo da resposta deve conter os seguintes campos:
            | name        | Novo Nome Atualizado |
            | nationality | Português            |
            | phone       | 85988889999          |

    Cenário: Falha ao atualizar usuário com username já existente
        Dado que existe um usuário cadastrado com o username "carlos_silva"
        E que existe um usuário cadastrado para atualização
        Quando eu enviar uma requisição PUT para atualizar esse usuário com os dados:
            | username | carlos_silva |
        Então o status de resposta deve ser 409
        E o corpo da resposta deve conter os seguintes campos:
            | error   | Resource Conflict                         |
            | message | Username already in use: carlos_silva     |