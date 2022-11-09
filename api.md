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
| 204    | Registro excluído.                                   |
| 400    | Erro de validação ou registro não existe no sistema. |
| 401    | Erro de autenticação.                                |
| 403    | Acesso ao edpoint não autorizado.                    |
| 404    | Recurso não encontrado no banco de dados.            |



## Group Recursos



## Usuários [/user]

### GET [/user/profile] 

Retorna o perfil do usuário.

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

Atualiza o nome do usuário.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `newName: Lord Voldemort (required, String)`

+ Response 201 (application/json) - Nome alterado com sucesso!

### PUT [/user/edit-Password]

Atualiza a senha do usuário.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

​			`newPassword: Voldemort@ (required, String)`

+ Response 201 (application/json) - Senha alterada com sucesso!



## Financial Record [/record]

### GET [/record/all]

Retorna todos os registros financeiros por data.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `date: 25/10/2022 (required, String)`

+ Response 200 (application/json)

	[
		{
	    "transactionId": "87aef2d5-d6af-445c-9b82-45aeaf4270aa",
	    "transactionValue": 1000.00,
	    "transactionCategory": "RECEIPT",
	    "transactionDate": "22/10/2022",
	    "transactionDescription": null,
	    "links": [...]
		}
	]
### GET [/record/by-category]

Retorna todos os registros financeiros por categoria do registro e data.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `category: PAYMENT (required, String)`

    `date: 25/10/2022 (required, String)`

+ Response 200 (application/json)

	[
		{
	    "transactionId": "6bcd43f2-980f-4aed-85bb-232033e70ba8",
	    "transactionValue": 65.00,
	    "transactionCategory": "PAYMENT",
	    "transactionDate": "25/10/2022",
	    "transactionDescription": "comida",
	    "links": [...]
		},
		{
	    "transactionId": "7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a",
	    "transactionValue": 45.00,
	    "transactionCategory": "PAYMENT",
	    "transactionDate": "25/10/2022",
	    "transactionDescription": "lanche mcdonalds",
	    "links": [...]
		}
	]
### GET [/record/by-id]

Retorna todos os registros financeiros por id.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `id: 87aef2d5-d6af-445c-9b82-45aeaf4270aa (required, String)`

+ Response 200 (application/json)

`	{
	    "transactionId": "87aef2d5-d6af-445c-9b82-45aeaf4270aa",
	    "transactionValue": 1000.00,
	    "transactionCategory": "RECEIPT",
	    "transactionDate": "22/10/2022",
	    "transactionDescription": null,
	    "_links": {...}
	    }
	}`

### GET [/record/by-tag]

Retorna todos os registros financeiros por tag em uma data específica.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `tagName: salario (required, String)`

    `date: 22/10/2022 (required, String)`

+ Response 200 (application/json)

​	`[
​	    {
​	        "transactionId": "87aef2d5-d6af-445c-9b82-45aeaf4270aa",
​	        "transactionValue": 1000.00,
​	        "transactionCategory": "RECEIPT",
​	        "transactionDate": "22/10/2022",
​	        "transactionDescription": null,
​	        "links": [...]
​	    }
​	]`

### GET [/record/by-year-month]

Retorna todos os registros financeiros do mes.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `date: 22/10/2022 (required, String)`

+ Response 200 (application/json)

  `[
      {
          "transactionId": "6bcd43f2-980f-4aed-85bb-232033e70ba8",
          "transactionValue": 65.00,
          "transactionCategory": "PAYMENT",
          "transactionDate": "25/10/2022",
          "transactionDescription": "comida",
          "links": [...]
      },
      {
          "transactionId": "7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a",
          "transactionValue": 45.00,
          "transactionCategory": "PAYMENT",
          "transactionDate": "25/10/2022",
          "transactionDescription": "lanche mcdonalds",
          "links": [...]
      }
  ]`

### POST [/record/add]

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Body

    `{
        "transactionValue": 50.00,
        "transactionCategory": "RECEIPT",
        "transactionDate": "22/10/2022",
        "transactionDescription": "pix"`

    `}`

  + Parâmetro

    `tagName: pix Recebido (required, String)`

+ Response 201 (application/json)

### PUT [/record/edit/category]

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `category: PAYMENT (required, String)`

    `id: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

+ Response 200 (application/json)

### PUT [/record/edit/value]

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `value: 45.00 (required, BigDecimal)`

    `id: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

+ Response 200 (application/json)

### PUT [/record/edit/date]

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `date: 25/10/2022 (required, String)`

    `id: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

+ Response 200 (application/json)

### PUT [/record/edit/description]

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `description: lanche mcdonalds (required, String)`

    `id: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

+ Response 200 (application/json)

### PUT [/record/edit/tag]

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `transactionId: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

    `tagName: mercado (required, String)`

+ Response 200 (application/json)

### DELETE [/record/delete]

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `transactionId: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

+ Response 204 (application/json)



## Tag [/tag]

### GET [/tag/all]

Retorna todas as tags criadas pelo usuário.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

+ Response 200 (application/json)

	[
		{
	    	"tagId": "11cbc083-c0fb-4eb2-85a5-4474c399a0da",
	    	"tagName": "salario",
	    	"transactions": [],
	    	"links": [...]
		},
		{
	    	"tagId": "38b033b0-e9bc-49fe-8634-cd420f78fc6b",
	    	"tagName": "internet",
	    	"transactions": [],
	    	"links": [...]
		},
		{
	    	"tagId": "78ec79d2-cb27-4b6f-b81e-514dbc2862ce",
	    	"tagName": "mercado",
	    	"transactions": [...],
	    	"links": [...]
		},
		{
	    	"tagId": "f25b32f3-f271-4b40-9164-c83f0d11afef",
	    	"tagName": "pix recebido",
	    	"transactions": [],
	    	"links": [...]
		},
		{
	    	"tagId": "fae49773-7d57-482a-9dbb-ab521d1500ae",
	    	"tagName": "aluguel",
	    	"transactions": [...],
	    	"links": [...]
		}
	]


### Signup [GET]

Create a new account.

+ Response 200 (application/json)





