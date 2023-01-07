package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.enums.TransactionCategory
import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.models.FinancialTransaction
import br.com.project.projetoIntegrador.service.FinancialTransactionService
import br.com.project.projetoIntegrador.utils.ContextUtils
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/transaction")
class FinancialTransactionController @Autowired constructor(private val financialTransactionService: FinancialTransactionService) {

    @PostMapping("/register")
    fun registerTransaction(@RequestBody @Valid transaction: FinancialTransaction, @RequestParam tagName: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.registerTransaction(transaction, username, tagName)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/delete")
    fun deleteTransaction(@RequestParam transactionId: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.deleteTransaction(UUID.fromString(transactionId), username)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/by-category")
    fun getAllByCategory(@RequestParam category: String, @RequestParam date: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        try {
            val date: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            return ResponseEntity.ok(financialTransactionService.findAllByCategory(TransactionCategory.valueOf(category), date, username))
        } catch (e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }

    }

    @GetMapping("/by-id")
    fun getTransactionById(@RequestParam transactionId: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        return ResponseEntity.ok(financialTransactionService.getTransactionById(UUID.fromString(transactionId), username))
    }

    @GetMapping("/all")
    fun getAllTransaction(@RequestParam date: String): ResponseEntity<List<FinancialTransaction>> {
        val username: String = ContextUtils.getUsername()
        try {
            val date: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            return ResponseEntity.ok(financialTransactionService.getAllTransactions(username, date))
        } catch (e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }

    @PutMapping("/edit/value")
    fun editValue(@RequestParam value: BigDecimal, @RequestParam id: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.editValue(value, UUID.fromString(id), username)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/edit/category")
    fun editCategory(@RequestParam id: String?, @RequestParam category: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.editCategory(TransactionCategory.valueOf(category), UUID.fromString(id), username)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/edit/date")
    fun editDate(@RequestParam date: String?, @RequestParam id: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        try {
            val date: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            financialTransactionService.editDate(date, UUID.fromString(id), username)
            return ResponseEntity.ok().build()
        } catch (e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }

    @PutMapping("/edit/description")
    fun editDescription(@RequestParam description: String?, @RequestParam id: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        if(description == null || description.equals("")){
            financialTransactionService.editDescription(description, UUID.fromString(id), username)
            return ResponseEntity.ok().build()
        }

        financialTransactionService.editDescription(description, UUID.fromString(id), username)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/edit/tag")
    fun changeTag(@RequestParam tagName: String?, @RequestParam transactionId: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        financialTransactionService.changeTag(UUID.fromString(transactionId), tagName, username)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/by-tag")
    fun findByTag(@RequestParam tagName: String, date: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        try {
            val date: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            return ResponseEntity.ok(financialTransactionService.findByTag(tagName, username, date))
        } catch (e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }
}