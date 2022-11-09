# PerCapita API RESTful

API Restful da aplicação web PerCapita, seu assistente de controle financeiro pessoal.

### Métodos

As requisiçõs da API seguem os protocolos http:

| Método | Descrição                          |
| ------ | ---------------------------------- |
| GET    | Retorna dados a uma requisição.    |
| POST   | Insere novo registro.              |
| PUT    | Atualiza atributos de um registro. |
| DELETE | Remove um registro do sistema.     |

### Respostas

| Código | Descrição                                            |
| ------ | ---------------------------------------------------- |
| 200    | Requisição executada com sucesso.                    |
| 201    | Novo registro criado com sucesso.                    |
| 400    | Erro de validação ou registro não existe no sistema. |
| 401    | Erro de autenticação.                                |
| 403    | Acesso ao edpoint não autorizado.                    |
| 404    | Recurso não encontrado no banco de dados.            |

## Group Recursos

## Usuários [/user]

Retorna o perfil do usuário

### GET [/user/profile]

+ Request(application/json)

  + Headers

    `Authorization: Bearer token`

  + Body

    `{
        "username": "voldemort@hotmail.com",
        "password": "Voldemort#"
    }`

+ Response 200 (application/json)

  `{
      "userId": 4,
      "name": "lord voldemort",
      "username": "voldemort@hotmail.com",
      "password": "$2a$10$kqFyQ3mhNUmctLMKs9Oul.uqx1LgW3K4isDvL4b5pmLmN18VX6pCa",
      "tags": [],
      "transactions": [],
      "token": null,
      "tokenCreationDate": null,
      "enabled": true,
      "authorities": [],
      "accountNonExpired": true,
      "credentialsNonExpired": true,
      "accountNonLocked": true
  }`

### PUT [/user/edit-profileName]

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

​			`newName: Lord Voldemort (required, String)`

+ Response 201 (application/json) - Nome alterado com sucesso!

### PUT [/user/edit-Password]

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

​			`newPassword: Voldemort@ (required, String)`

+ Response 201 (application/json) - Senha alterada com sucesso!



## Financial Record [/record]

### POST [/record/add]

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Body

    `{
        "transactionValue": 50.00,
        "transactionCategory": "PAYMENT",
        "transactionDate": "22/10/2022",
        "transactionDescription": "pix"`

    `}`

  + Parâmetro

    tag







### Signup [GET]

Create a new account.

+ Response 200 (application/json)





