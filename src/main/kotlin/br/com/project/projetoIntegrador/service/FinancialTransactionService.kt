package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.enums.TransactionCategory
import br.com.project.projetoIntegrador.models.FinancialTransaction
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

@Service
class FinancialTransactionService {

    fun registerTransaction(transaction: FinancialTransaction, username: String, tagName: String) {

    }

    fun deleteTransaction(transactionId: UUID, username: String) {

    }

    fun findAllByCategory(category: TransactionCategory, date: SimpleDateFormat, username: String) {

    }

    fun getTransactionById(transactionId: UUID, username: String) {

    }

    fun getAllTransactions(username: String, date: SimpleDateFormat): List<FinancialTransaction> {

    }

    fun editValue(value: BigDecimal, transactionId: UUID, username: String) {

    }

    fun editCategory(category: TransactionCategory, transactionId: UUID, username: String) {

    }

    fun editDate(date: SimpleDateFormat, fromString: UUID, username: String) {

    }

    fun editDescription(description: String?, fromString: UUID, username: String) {

    }

    fun changeTag(tagId: UUID, tagName: String?, username: String) {

    }

    fun findByTag(tagName: String, username: String, date: SimpleDateFormat) {

    }
}