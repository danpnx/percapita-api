# PerCapita API RESTful

###### v1.0

API Restful da aplicação web PerCapita, seu assistente de controle financeiro pessoal.

[Terms of Service](https://github.com/danpnx/projeto-integrador-dh)

[PerCapita - Website](https://github.com/danpnx/projeto-integrador-dh)

[Send email to PerCapita](mailto:saviachristine@gmail.com)

[Apache Licence 2.0](https://www.apache.org/licenses/LICENSE-2.0)



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
| 409    | Conflito, recurso ja está sendo utilizado.           |



## Group Recursos

## User [/user]

### GET [/user/profile] 

Retorna o perfil do usuário.

+ Request(application/json)

  + Headers

    `Authorization: Bearer token`

+ Response 200 (application/json) - OK

  ```
  {
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
  }
  ```



### PUT [/user/edit-profileName]

Atualiza o nome do usuário.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `newName: Lord Voldemort (required, String)`

+ Response 201 (application/json)

  ```
  Nome alterado com sucesso!
  ```

  

### PUT [/user/edit-Password]

Atualiza a senha do usuário.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

​			`newPassword: Voldemort@ (required, String)`

+ Response 201 (application/json) 

  ```
  Senha alterada com sucesso!
  ```

  

+ Response 400 (application/json) - Bad Request

```
{
    "timestamp": "07/11/2022 11:58:46",
    "status": 400,
    "error": "Campo inválido",
    "message": "A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial",
    "path": "/user/edit-Password"
}
```



## FinancialRecord [/record]

### GET [/record/all]

Retorna todos os registros financeiros por data.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `date: 25/10/2022 (required, String)`

+ Response 200 (application/json) - OK

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


+ Response 404 (application/json) - Not Found.

```
{
    "timestamp": "09/11/2022 12:04:58",
    "status": 404,
    "error": "Recurso não encontrado no banco de dados",
    "message": "Você não possui nenhuma transação registrada neste mês",
    "path": "/record/all"
}
```



### GET [/record/by-category]

Retorna todos os registros financeiros por categoria do registro.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `category: PAYMENT (required, String)`

    `date: 25/10/2022 (required, String)`

+ Response 200 (application/json) - OK

  

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


+ Response 404 (application/json) - Not Found

```
{
    "timestamp": "09/11/2022 12:06:16",
    "status": 404,
    "error": "Recurso não encontrado no banco de dados",
    "message": "Você não possui nenhuma transação registrada com esta categoria",
    "path": "/record/by-category"
}
```



### GET [/record/by-id]

Retorna todos os registros financeiros por id.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `id: 87aef2d5-d6af-445c-9b82-45aeaf4270aa (required, String)`

+ Response 200 (application/json) - OK

```
{
	"transactionId": "87aef2d5-d6af-445c-9b82-45aeaf4270aa",
	"transactionValue": 1000.00,
    "transactionCategory": "RECEIPT",
    "transactionDate": "22/10/2022",
    "transactionDescription": null,
    "_links": {...}
}
```



+ Response 403 (application/json) - Forbidden

```
{
    "timestamp": "09/11/2022 12:10:19",
    "status": 403,
    "error": "Usuário não autorizado a acessar endpoint",
    "message": "Essa transação não pertence ao usuário autenticado",
    "path": "/record/by-id"
}
```



### GET [/record/by-tag]

Retorna todos os registros financeiros por tag.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `tagName: salario (required, String)`

    `date: 22/10/2022 (required, String)`

+ Response 200 (application/json) - OK

```
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
```



+ Response 400 (application/json) - Bad Request

```
{
    "timestamp": "09/11/2022 12:11:27",
    "status": 400,
    "error": "Nenhum dado encontrado no banco",
    "message": "Essa tag não existe",
    "path": "/record/by-tag"
}
```



### GET [/record/by-year-month]

Retorna todos os registros financeiros do mes.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `date: 22/10/2022 (required, String)`

+ Response 200 (application/json)

```
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
```



+ Response 404 (application/json) - Not Found

```
{
    "timestamp": "2022-11-09T15:17:11.423+00:00",
    "status": 404,
    "error": "Not Found",
    "path": "/record/by-yea-month"
}
```



### POST [/record/add]

Cria um novo registro financeiro.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Body

  ```
  {
      "transactionValue": 50.00,
      "transactionCategory": "RECEIPT",
      "transactionDate": "22/10/2022",
      "transactionDescription": "pix"
  
  }
  ```

  + Parâmetro

    `tagName: pix Recebido (required, String)`

+ Response 201 (application/json) - Created



### PUT [/record/edit/category]

Atualiza a categoria de um registro financeiro existente.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `category: PAYMENT (required, String)`

    `id: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

+ Response 200 (application/json) - OK



### PUT [/record/edit/value]

Atualiza o valor de um registro financeiro existente.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `value: 45.00 (required, BigDecimal)`

    `id: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

+ Response 200 (application/json) - OK

  

### PUT [/record/edit/date]

Atualiza a data de um registro financeiro existente.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `date: 25/10/2022 (required, String)`

    `id: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

+ Response 200 (application/json) - OK
+ Response 400 (application/jason) - Bad Request

```
{
    "timestamp": "09/11/2022 12:33:18",
    "status": 400,
    "error": "Campo inválido",
    "message": "Não foi possível converter a data",
    "path": "/record/edit/date"
}
```



### PUT [/record/edit/description]

Atualiza a descrição de um registro financeiro existente.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `description: lanche mcdonalds (required, String)`

    `id: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

+ Response 200 (application/json) - OK



### PUT [/record/edit/tag]

Atualiza a tag de um registro financeiro existente.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `transactionId: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

    `tagName: mercado (required, String)`

+ Response 200 (application/json) - OK
+ Response 400 (application/json) - Bad Request

```
{
    "timestamp": "09/11/2022 12:29:51",
    "status": 400,
    "error": "Campo inválido",
    "message": "Insira uma tag existente",
    "path": "/record/edit/tag"
}
```



### DELETE [/record/delete]

Exclui um registro financeiro.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `transactionId: 7cfe5baa-9f72-4b2b-93a3-cef5126a0a0a (required, String)`

+ Response 204 (application/json) - No Content



## Tag [/tag]

### GET [/tag/all]

Retorna todas as tags criadas pelo usuário.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

+ Response 200 (application/json) - OK

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
### GET [/tag/by-id]

Retorna todas as tags criadas pelo usuário por id.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `tagId: 11cbc083-c0fb-4eb2-85a5-4474c399a0da (required, String)`

+ Response 200 (application/json) - OK

```
{
    	"tagId": "11cbc083-c0fb-4eb2-85a5-4474c399a0da",
    	"tagName": "salario",
    	"transactions": [...],
    	"_links": {...}
}
```

### POST [/tag/create]

Cria um nova tag.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `tagName: conta de luz (required, String)`

+ Response 201 (application/json) - Created

  ```
  Tag conta de luz criada!
  ```

+ Response 409 (application/json) - Conflict

  ```
  Já existe uma tag com este nome.
  ```

  

### PUT [/tag/edit]

Atualiza o nome de uma tag existente.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `tagId: conta de luz (required, String)`

    `newName: conta de luz (required, String)`

+ Response 200 (application/json) - OK

  ```
  Nome da tag atualizada.
  ```

  

### DELETE [/tag/delete]

Exclui uma tag.

+ Request (application/json)

  + Headers

    `Authorization: Bearer token`

  + Parâmetro

    `tagId: f25b32f3-f271-4b40-9164-c83f0d11afef (required, String)`

+ Response 204 (application/json) - No Content

  

## Signup [POST /signup]

Cria um novo cadastro de usuário.

+ Request

  + Body 

    	{
    	    "name": "Maria da Silva",
    	    "username": "maria@hotmail.com",
    	    "password": "Mariazinh@"
    	}
    

+ Response 200 (application/json) - OK 

  ```
  Conta criada com sucesso!
  ```

  

+ Response 409 (application/json) - Conflict

		{
			"timestamp": "09/11/2022 12:54:44",
			"status": 409,
			"error": "Não foi possível finalizar a ação",
			"message": "Email já está em uso",
			"path": "/signup"
		}
## Login [POST /login]

Autentica um usuário e autoriza o acesso aos endpoints.

+ Request

  + Body 

    	{
    	    "username": "maria@hotmail.com",
    	    "password": "Mariazinh@"
    	}

    

+ Response 200 (application/json) - OK 

+ Response 403 (application/json) - Forbidden

  

## ForgotPassword [POST /forgot-password]

Recupera a senha de um usuário existente.

+ Request

  + Parâmetro

    `email: maria@hotmail.com (required, String)`

+ Response 200 (application/json) - OK 

  ```
  Email enviado com sucesso!
  ```

  

+ Response 400 (application/json) - Bad Request

```
{
    "timestamp": "09/11/2022 13:05:23",
    "status": 400,
    "error": "Nenhum dado encontrado no banco",
    "message": "Usuário não encontrado",
    "path": "/forgot-password"
}
```



## ResetPassword [PUT /reset-password]

Recupera a senha de um usuário existente.

+ Request

  + Parâmetro

    `token: 065a7099-fdb4-4c41-af92-7fd363c07a31382b99aa-393b-4ec7-8d2d-b314a77212e0 (required, String)`

    `password: Mariazon@ (required, String)`

+ Response 200 (application/json) - OK 

  ```
  Senha alterada com sucesso!
  ```

  

+ Response 401 (application/json) - Unauthorized

  ```
  {
      "timestamp": "09/11/2022 13:12:29",
      "status": 401,
      "error": "Erro de validação de token de expiração",
      "message": "Token inválido",
      "path": "/reset-password"
  }
  ```

  

## Schemas

#### FinancialRecord

`{`

​	`transactionValue number`

​	`transactionCategory string Enum: [ RECEIPT, PAYMENT ]`

​	`transactionDate string($date-time)`

​	`transactionDescription string`

​	`user User{...}`

​	`tag Tag{...}`

​	`transaction_id string($uuid)`

​	`	links[...]`

`}`

#### GrantedAuthority

`{`

`	authority string`

`}`

#### Link

`{`

​	`rel string`

​	`href string`

​	`hreflang string`

​	`	media string`

​	`	title string`

​	`type string`

​	`deprecation string`

​	`profile string`

​	`name string`

`}`

#### Tag

`{`

​	`tagId string($uuid)`

​	`tagName string`

​	`user User{...}`

​	`transactions [FinancialTransaction{...}]`

`}`

#### User

`{`

​	`userId integer($int64)`

​	`name string`

​	`username string`

​	`password string`

​	`tags [Tag{...}]`

​	`transactions [FinancialTransaction{...}]`

​	`token string`

​	`tokenCreationDate string($date-time)`

​	`enabled boolean`

​	`authorities [GrantedAuthority{...}]`

​	`accountNonLocked boolean`

​	`credentialsNonExpired boolean`

​	`accountNonExpired boolean`

`}`