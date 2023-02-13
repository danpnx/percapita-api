package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.dto.RegisterTransactionDTO
import br.com.project.projetoIntegrador.enums.TransactionCategory
import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.models.FinancialTransaction
import br.com.project.projetoIntegrador.payload.StandardMessage
import br.com.project.projetoIntegrador.service.FinancialTransactionService
import br.com.project.projetoIntegrador.utils.ContextUtils
import br.com.project.projetoIntegrador.utils.getLocalDate
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.text.ParseException
import java.util.*

@RestController
@RequestMapping("/transaction")
@Tag(name = "Financial Transaction", description = "Endpoints para manipulação de transações financeiras")
@SecurityRequirement(name = "Bearer Authentication")
class FinancialTransactionController @Autowired constructor(private val financialTransactionService: FinancialTransactionService) {

    @Operation(
        summary = "Cadastrar transação",
        description = "Endpoint para cadastro de uma transação financeira",
        tags = ["Financial Transaction"],
        responses = [
            ApiResponse(
                description = "CREATED",
                responseCode = "201",
                content = []
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @PostMapping("/register")
    fun registerTransaction(
        @RequestBody @Valid transaction: RegisterTransactionDTO,
        @RequestParam tagName: String
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.registerTransaction(transaction, username, tagName)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Operation(
        summary = "Deletar transação",
        description = "Endpoint para exclusão de uma transação financeira",
        tags = ["Financial Transaction"],
        responses = [
            ApiResponse(
                description = "NO CONTENT",
                responseCode = "204",
                content = []
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "FORBIDDEN",
                responseCode = "403",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @DeleteMapping("/delete/{transactionId}")
    fun deleteTransaction(
        @PathVariable("transactionId") transactionId: String?
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.deleteTransaction(UUID.fromString(transactionId), username)
        return ResponseEntity.noContent().build()
    }


    @Operation(
        summary = "Transações por categoria",
        description = "Endpoint para buscar transações por categoria no mês",
        tags = ["Financial Transaction"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(
                            schema = Schema(implementation = FinancialTransaction::class)
                        )
                    )
                ]
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/by-category")
    fun getAllByCategory(
        @RequestParam category: String,
        @RequestParam date: String
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        val dateFormatted = getLocalDate(date)
        try {
            return ResponseEntity.ok(financialTransactionService
                .findAllByCategory(
                    TransactionCategory.valueOf(category),
                    dateFormatted,
                    username
                )
            )
        } catch (e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }

    }

    @Operation(
        summary = "Buscar transação",
        description = "Endpoint para buscar dados de uma transação específica",
        tags = ["Financial Transaction"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = FinancialTransaction::class)
                    )
                ]
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "FORBIDDEN",
                responseCode = "403",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/{transactionId}")
    fun getTransactionById(@PathVariable("transactionId") transactionId: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        return ResponseEntity.ok(financialTransactionService
            .getTransactionById(UUID.fromString(transactionId), username)
        )
    }

    @Operation(
        summary = "Buscar transações",
        description = "Endpoint para buscar por todas as transações do usuário no mês",
        tags = ["Financial Transaction"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(
                            schema = Schema(implementation = FinancialTransaction::class)
                        )
                    )
                ]
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/all")
    fun getAllTransactions(): ResponseEntity<List<FinancialTransaction>> {
        val username: String = ContextUtils.getUsername()
        return ResponseEntity.ok(financialTransactionService.getAllTransactions(username))
    }

//    @GetMapping("/all")
//    fun getAllTransaction(@RequestParam date: String): ResponseEntity<List<FinancialTransaction>> {
//        val username: String = ContextUtils.getUsername()
//        val dateFormatted = getLocalDate(date)
//        try {
//            return ResponseEntity.ok(
//                financialTransactionService.getAllTransactions(username, dateFormatted)
//            )
//        } catch (e: ParseException) {
//            throw InvalidInputException("Não foi possível converter a data")
//        }
//    }

    @Operation(
        summary = "Alterar valor",
        description = "Endpoint para alterar valor de uma transação",
        tags = ["Financial Transaction"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = []
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "FORBIDDEN",
                responseCode = "403",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @PutMapping("/edit/{transactionId}/value")
    fun editValue(
        @PathVariable("transactionId") transactionId: String,
        @RequestParam value: BigDecimal
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.editValue(value, UUID.fromString(transactionId), username)
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Alterar categoria",
        description = "Endpoint para alterar categoria de uma transação",
        tags = ["Financial Transaction"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = []
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "FORBIDDEN",
                responseCode = "403",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @PutMapping("/edit/{transactionId}/category")
    fun editCategory(
        @PathVariable("transactionId") transactionId: String,
        @RequestParam category: String
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.editCategory(
            TransactionCategory.valueOf(category),
            UUID.fromString(transactionId),
            username
        )
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Alterar data",
        description = "Endpoint para alterar data de uma transação",
        tags = ["Financial Transaction"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = []
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "FORBIDDEN",
                responseCode = "403",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @PutMapping("/edit/{transactionId}/date")
    fun editDate(
        @PathVariable("transactionId") transactionId: String,
        @RequestParam date: String
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        val dateFormatted = getLocalDate(date)
        try {
            financialTransactionService.editDate(
                dateFormatted,
                UUID.fromString(transactionId),
                username
            )
            return ResponseEntity.ok().build()
        } catch (e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }

    @Operation(
        summary = "Alterar descrição",
        description = "Endpoint para alterar descrição de uma transação",
        tags = ["Financial Transaction"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = []
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "FORBIDDEN",
                responseCode = "403",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @PutMapping("/edit/{transactionId}/description")
    fun editDescription(
        @PathVariable("transactionId") transactionId: String,
        @RequestParam description: String
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.editDescription(
            description,
            UUID.fromString(transactionId),
            username
        )
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Alterar tag",
        description = "Endpoint para alterar tag de uma transação",
        tags = ["Financial Transaction"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = []
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "FORBIDDEN",
                responseCode = "403",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @PutMapping("/edit/{transactionId}/tag")
    fun editTag(
        @PathVariable("transactionId") transactionId: String,
        @RequestParam tagName: String
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.changeTag(UUID.fromString(transactionId), tagName, username)
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Transações por tag",
        description = "Endpoint para buscar transações por tag no mês",
        tags = ["Financial Transaction"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(
                            schema = Schema(implementation = FinancialTransaction::class)
                        )
                    )
                ]
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/by-tag")
    fun findByTag(@RequestParam tagName: String, date: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        val dateFormatted = getLocalDate(date)
        try {
            return ResponseEntity.ok(
                financialTransactionService.findByTag(tagName, username, dateFormatted)
            )
        } catch (e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }
}