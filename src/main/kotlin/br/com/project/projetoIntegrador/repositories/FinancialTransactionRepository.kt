package br.com.project.projetoIntegrador.repositories

import br.com.project.projetoIntegrador.enums.TransactionCategory
import br.com.project.projetoIntegrador.models.FinancialTransaction
import br.com.project.projetoIntegrador.models.Tag
import br.com.project.projetoIntegrador.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FinancialTransactionRepository : JpaRepository<FinancialTransaction, UUID> {

    fun findAllByUserOrderByTransactionDateDesc(user: User): List<FinancialTransaction>

    @Query(value = "select e from FinancialTransaction e where month(e.transactionDate)= ?2 and year(e.transactionDate)= ?3 and e.user= ?1 order by e.transactionDate desc")
    fun findAllByUserAndTransactionDate(user: User, month: Int, year: Int): List<FinancialTransaction>

    @Query(value = "select e from FinancialTransaction e where month(e.transactionDate)= ?3 and year(e.transactionDate)= ?4 and e.user= ?1 and e.transactionCategory= ?2 order by e.transactionDate desc")
    fun findAllByUserAndTransactionCategoryAndTransactionDate(user: User, category: TransactionCategory, month: Int, year: Int): List<FinancialTransaction>

    @Query(value = "select e from FinancialTransaction e where month(e.transactionDate)= ?3 and year(e.transactionDate)= ?4 and e.user= ?2 and e.tag= ?1 order by e.transactionDate desc")
    fun findAllByTagAndUserAndTransactionDate(tag: Tag, user: User, month: Int, year: Int): List<FinancialTransaction>
}