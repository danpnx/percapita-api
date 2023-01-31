package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.enums.TransactionCategory
import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.models.FinancialTransaction
import br.com.project.projetoIntegrador.service.FinancialTransactionService
import br.com.project.projetoIntegrador.utils.ContextUtils
import jakarta.validation.Valid
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.text.ParseException
import java.util.*

@RestController
@RequestMapping("/transaction")
class FinancialTransactionController @Autowired constructor(private val financialTransactionService: FinancialTransactionService) {

    @PostMapping("/register")
    fun registerTransaction(
        @RequestBody @Valid transaction: FinancialTransaction,
        @RequestParam tagName: String
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.registerTransaction(transaction, username, tagName)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/delete/{transactionId}")
    fun deleteTransaction(
        @PathVariable("transactionId") transactionId: String?
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.deleteTransaction(UUID.fromString(transactionId), username)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/by-category")
    fun getAllByCategory(
        @RequestParam category: String,
        @RequestParam date: String
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        try {
            val dateFormatted = DateUtils.parseDate(date, "dd/MM/yyyy")
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

    @GetMapping("/{transactionId}")
    fun getTransactionById(@PathVariable("transactionId") transactionId: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        return ResponseEntity.ok(financialTransactionService
            .getTransactionById(UUID.fromString(transactionId), username)
        )
    }

    @GetMapping("/all")
    fun getAllTransaction(@RequestParam date: String): ResponseEntity<List<FinancialTransaction>> {
        val username: String = ContextUtils.getUsername()
        try {
            val parsedDate = DateUtils.parseDate(date, "dd/MM/yyyy")
            return ResponseEntity.ok(
                financialTransactionService.getAllTransactions(username, parsedDate)
            )
        } catch (e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }

    @PutMapping("/edit/{transactionId}/value")
    fun editValue(
        @PathVariable("transactionId") transactionId: String,
        @RequestParam value: BigDecimal
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.editValue(value, UUID.fromString(transactionId), username)
        return ResponseEntity.ok().build()
    }

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

    @PutMapping("/edit/{transactionId}/date")
    fun editDate(
        @PathVariable("transactionId") transactionId: String,
        @RequestParam date: String
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        try {
            val parsedDate = DateUtils.parseDate(date, "dd/MM/yyyy")
            financialTransactionService.editDate(
                parsedDate,
                UUID.fromString(transactionId),
                username
            )
            return ResponseEntity.ok().build()
        } catch (e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }

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

    @PutMapping("/edit/{transactionId}/tag")
    fun editTag(
        @PathVariable("transactionId") transactionId: String,
        @RequestParam tagName: String
    ): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.changeTag(UUID.fromString(transactionId), tagName, username)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/by-tag")
    fun findByTag(@RequestParam tagName: String, date: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        try {
            val parsedDate = DateUtils.parseDate(date, "dd/MM/yyyy")
            return ResponseEntity.ok(
                financialTransactionService.findByTag(tagName, username, parsedDate)
            )
        } catch (e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }
}