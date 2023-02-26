package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.controller.FinancialTransactionController
import br.com.project.projetoIntegrador.dto.RegisterTransactionDTO
import br.com.project.projetoIntegrador.enums.TransactionCategory
import br.com.project.projetoIntegrador.exceptions.AuthorizationException
import br.com.project.projetoIntegrador.exceptions.DatabaseException
import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.exceptions.ResourceNotFoundException
import br.com.project.projetoIntegrador.models.FinancialTransaction
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.repositories.FinancialTransactionRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Service
@Transactional
class FinancialTransactionService(
    @Autowired private val transactionRepository: FinancialTransactionRepository,
    @Autowired private val userService: UserService,
    @Autowired private val tagService: TagService
) {

    fun registerTransaction(transaction: RegisterTransactionDTO, username: String, tagName: String): FinancialTransaction { //adicionado o retorno de financialtransaction
        if(transaction.transactionValue <= BigDecimal.ZERO) {
            throw InvalidInputException("Digite um valor maior que zero")
        }

        val user = userService.getUser(username)
        val tag = if(tagService.existsByTagNameAndUser(tagName, username)) {
            tagService.getTagByTagNameAndUser(tagName, user)
        } else {
            tagService.registerTagInFinancialTransaction(tagName, user)
        }

        val financialTransaction = transaction.convertToFinancialTransaction(user, tag)
        transactionRepository.save(financialTransaction)
        return financialTransaction //adicionado o retorno do objeto
    }

    fun deleteTransaction(transactionId: UUID, username: String) {
        val transaction = getTransaction(transactionId, username)
        transactionRepository.delete(transaction)
    }

    fun findAllByCategory(category: TransactionCategory, date: LocalDate, username: String)
    : List<FinancialTransaction> {
        val user = userService.getUser(username)
        val list = transactionRepository
            .findAllByUserAndTransactionCategoryAndTransactionDate(user, category, date.monthValue, date.year)

        if(list.isEmpty()) {
            throw ResourceNotFoundException("Você não possui nenhuma transação registrada com esta categoria")
        }

        addLinksInList(list)
        return list
    }

    fun getTransactionById(transactionId: UUID, username: String): FinancialTransaction {
        val transaction = getTransaction(transactionId, username)
        val date = transaction.transactionDate
        val dateFormatted = LocalDate.of(date.year, date.monthValue, date.dayOfMonth)
        transaction.add(
            linkTo(
                methodOn(FinancialTransactionController::class.java).getAllTransactions()
            ).withRel("Transações financeiras")
        )
        transactionRepository.save(transaction)
        return transaction
    }

    fun getAllTransactions(username: String): List<FinancialTransaction> {
        val user = userService.getUser(username)
        val list = transactionRepository.findAllByUserOrderByTransactionDateDesc(user)
        if(list.isEmpty()) {
            throw ResourceNotFoundException("Você não possui nenhuma transação registrada neste mês")
        }
        return list
    }

//    fun getAllTransactions(username: String, date: LocalDate): List<FinancialTransaction> {
//        val user = userService.findByUsername(username)
//        val list = transactionRepository.findAllByUserAndTransactionDate(user.get(), date.monthValue, date.year)
//
//        if(list.isEmpty()) {
//            throw ResourceNotFoundException("Você não possui nenhuma transação registrada neste mês")
//        }
//
//        addLinksInList(list)
//        return list
//    }

    fun editValue(value: BigDecimal, transactionId: UUID, username: String) {
        if(value <= BigDecimal.ZERO) {
            throw InvalidInputException("Digite um valor maior que zero")
        }

        val transaction = getTransaction(transactionId, username)
        transaction.transactionValue = value
        transactionRepository.save(transaction)
    }

    fun editCategory(category: TransactionCategory, transactionId: UUID, username: String) {
        val transaction = getTransaction(transactionId, username)
        transaction.transactionCategory = category
        transactionRepository.save(transaction)
    }

    fun editDate(date: LocalDate, transactionId: UUID, username: String) {
        val transaction = getTransaction(transactionId, username)
        val newDate = LocalDateTime.of(
            date, LocalTime.of(
                transaction.transactionDate.hour,
                transaction.transactionDate.minute,
                transaction.transactionDate.second,
                transaction.transactionDate.nano
            )
        )

        transaction.transactionDate = newDate
        transactionRepository.save(transaction)
    }

    fun editDescription(description: String?, transactionId: UUID, username: String) {
        val transaction = getTransaction(transactionId, username)
        transaction.transactionDescription = description ?: ""
        transactionRepository.save(transaction)
    }

    fun changeTag(transactionId: UUID, tagName: String?, username: String) {
        val transaction = getTransaction(transactionId, username)
        val user = userService.getUser(username)

        transaction.tag = user.tags.stream()
            .filter { tag -> tag.tagName == tagName }
            .findFirst().orElseThrow { InvalidInputException("Insira uma tag existente") }

        transactionRepository.save(transaction)
    }

    fun findByTag(tagName: String, username: String, date: LocalDate): List<FinancialTransaction> {
        if(!tagService.existsByTagNameAndUser(tagName, username)) {
            throw DatabaseException("Essa tag não existe")
        }

        val user = userService.getUser(username)
        val tag = tagService.getTagByTagNameAndUser(tagName, user)
        val list = transactionRepository.findAllByTagAndUserAndTransactionDate(tag, user, date.monthValue, date.year)

        if(list.isEmpty()) {
            throw ResourceNotFoundException("Você não possui nenhuma transação registrada com esta tag")
        }

        addLinksInList(list)
        return list
    }

    fun findByDateAndUser(date: LocalDate, user: User): List<FinancialTransaction> {
        return transactionRepository.findAllByUserAndTransactionDate(user, date.monthValue, date.year)
    }

    fun getTransaction(id: UUID, username: String): FinancialTransaction {
        val transaction = transactionRepository.findById(id)
            .orElseThrow{ ResourceNotFoundException("O recurso buscado não existe") }

        if(transaction.user?.username != username) {
            throw AuthorizationException("Essa transação não pertence ao usuário autenticado")
        }

        return transaction
    }

    private fun addLinksInList(list: List<FinancialTransaction>) {
        for(transaction in list) {
            transaction.add(
                linkTo(
                    methodOn(FinancialTransactionController::class.java)
                        .getTransactionById(transaction.transactionId.toString())
                ).withSelfRel()
            )

            transaction.add(
                linkTo(
                    methodOn(FinancialTransactionController::class.java)
                        .deleteTransaction(transaction.transactionId.toString())
                ).withSelfRel()
            )

            transaction.add(
                linkTo(
                    methodOn(FinancialTransactionController::class.java)
                        .editValue(transaction.transactionId.toString(), BigDecimal.ZERO)
                ).withSelfRel()
            )

            transaction.add(
                linkTo(
                    methodOn(FinancialTransactionController::class.java)
                        .editCategory(transaction.transactionId.toString(), "")
                ).withSelfRel()
            )

            transaction.add(
                linkTo(
                    methodOn(FinancialTransactionController::class.java)
                        .editDate(transaction.transactionId.toString(), "")
                ).withSelfRel()
            )

            transaction.add(
                linkTo(
                    methodOn(FinancialTransactionController::class.java)
                        .editDescription(transaction.transactionId.toString(), "")
                ).withSelfRel()
            )

            transaction.add(
                linkTo(
                    methodOn(FinancialTransactionController::class.java)
                        .editTag(transaction.transactionId.toString(), "")
                ).withSelfRel()
            )

            transactionRepository.save(transaction)
        }
    }
}