package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.controller.FinancialTransactionController
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
import java.text.SimpleDateFormat
import java.util.*

@Service
@Transactional
class FinancialTransactionService(
    @Autowired private val transactionRepository: FinancialTransactionRepository,
    @Autowired private val userService: UserService,
    @Autowired private val tagService: TagService
) {

    fun registerTransaction(transaction: FinancialTransaction, username: String, tagName: String) {
        if(transaction.transactionValue <= BigDecimal.ZERO) {
            throw InvalidInputException("Digite um valor maior que zero")
        }

        val user = userService.findByUsername(username)

        if(tagService.existsByTagNameAndUser(tagName, username)) {
            transaction.tag = tagService.getTagByTagNameAndUser(tagName, user.get())
        } else {
            transaction.tag = tagService.registerTagInFinancialTransaction(tagName, user.get())
        }

        transaction.user = user.get()
        transactionRepository.save(transaction)
    }

    fun deleteTransaction(transactionId: UUID, username: String) {
        val transaction = getTransaction(transactionId, username)
        transactionRepository.delete(transaction)
    }

    fun findAllByCategory(category: TransactionCategory, date: Date, username: String)
    : List<FinancialTransaction> {
        val user = userService.findByUsername(username)
        val list = transactionRepository
            .findAllByTransactionCategoryAndTransactionDateAndUser(category, date, user.get())

        if(list.isEmpty()) {
            throw ResourceNotFoundException("Você não possui nenhuma transação registrada com esta categoria")
        }

        addLinksInList(list)
        return list
    }

    fun getTransactionById(transactionId: UUID, username: String): FinancialTransaction {
        val transaction = getTransaction(transactionId, username)
        val date = SimpleDateFormat("dd/MM/yyyy").format(transaction.transactionDate)
        transaction.add(
            linkTo(
                methodOn(FinancialTransactionController::class.java).getAllTransaction(date)
            ).withRel("Transações financeiras")
        )
        transactionRepository.save(transaction)
        return transaction
    }

    fun getAllTransactions(username: String, date: Date): List<FinancialTransaction> {
        val user = userService.findByUsername(username)
        val list = transactionRepository.findAllByTransactionDateAndUser(date, user.get())

        if(list.isEmpty()) {
            throw ResourceNotFoundException("Você não possui nenhuma transação registrada neste mês")
        }

        addLinksInList(list)
        return list
    }

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

    fun editDate(date: Date, transactionId: UUID, username: String) {
        val transaction = getTransaction(transactionId, username)
        transaction.transactionDate = date
        transactionRepository.save(transaction)
    }

    fun editDescription(description: String?, transactionId: UUID, username: String) {
        val transaction = getTransaction(transactionId, username)
        transaction.transactionDescription = description ?: ""
        transactionRepository.save(transaction)
    }

    fun changeTag(transactionId: UUID, tagName: String?, username: String) {
        val transaction = getTransaction(transactionId, username)
        val user = userService.findByUsername(username)

        transaction.tag = user.get().tags.stream()
            .filter { tag -> tag.tagName == tagName }
            .findFirst().orElseThrow { InvalidInputException("Insira uma tag existente") }

        transactionRepository.save(transaction)
    }

    fun findByTag(tagName: String, username: String, date: Date): List<FinancialTransaction> {
        if(!tagService.existsByTagNameAndUser(tagName, username)) {
            throw DatabaseException("Essa tag não existe")
        }

        val user = userService.findByUsername(username)
        val tag = tagService.getTagByTagNameAndUser(tagName, user.get())
        val list = transactionRepository.findAllByTagAndTransactionDateAndUser(tag, date, user.get())

        if(list.isEmpty()) {
            throw ResourceNotFoundException("Você não possui nenhuma transação registrada com esta tag")
        }

        addLinksInList(list)
        return list
    }

    fun findByDateAndUser(date: Date, user: User): List<FinancialTransaction> {
        return transactionRepository.findAllByTransactionDateAndUser(date, user)
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
                        .editValue(BigDecimal.ZERO, transaction.transactionId.toString())
                ).withSelfRel()
            )

            transaction.add(
                linkTo(
                    methodOn(FinancialTransactionController::class.java)
                        .editCategory(null, transaction.transactionId.toString())
                ).withSelfRel()
            )

            transaction.add(
                linkTo(
                    methodOn(FinancialTransactionController::class.java)
                        .editDate(null, transaction.transactionId.toString())
                ).withSelfRel()
            )

            transaction.add(
                linkTo(
                    methodOn(FinancialTransactionController::class.java)
                        .editDescription(null, transaction.transactionId.toString())
                ).withSelfRel()
            )

            transaction.add(
                linkTo(
                    methodOn(FinancialTransactionController::class.java)
                        .editTag(transaction.transactionId.toString(), null)
                ).withSelfRel()
            )

            transactionRepository.save(transaction)
        }
    }
}