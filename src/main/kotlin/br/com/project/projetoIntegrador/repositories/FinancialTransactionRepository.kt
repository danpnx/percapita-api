package br.com.project.projetoIntegrador.repositories

import br.com.project.projetoIntegrador.enums.TransactionCategory
import br.com.project.projetoIntegrador.models.FinancialTransaction
import br.com.project.projetoIntegrador.models.Tag
import br.com.project.projetoIntegrador.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FinancialTransactionRepository : JpaRepository<FinancialTransaction, Long > {

    fun findAllByTransactionDateAndUser(date: Date, user: User): List<FinancialTransaction>
    fun findAllByTransactionCategoryAndTransactionDateAndUser(category: TransactionCategory, date: Date, user: User): List<FinancialTransaction>
    fun findAllByTagAndTransactionDateAndUser(tag: Tag, date: Date, user: User): List<FinancialTransaction>
}