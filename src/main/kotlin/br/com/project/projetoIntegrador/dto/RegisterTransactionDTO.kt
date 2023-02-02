package br.com.project.projetoIntegrador.dto

import br.com.project.projetoIntegrador.enums.TransactionCategory
import br.com.project.projetoIntegrador.models.FinancialTransaction
import br.com.project.projetoIntegrador.models.Tag
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.utils.getLocalDateTime
import java.math.BigDecimal

/**
 * @project projetoIntegrador
 * @author Daniel Augusto on 01/02/2023
 **/
data class RegisterTransactionDTO(
    val transactionValue: BigDecimal,
    val transactionCategory: String,
    val transactionDate: String,
    val transactionDescription: String,
) {
    fun convertToFinancialTransaction(user: User, tag: Tag): FinancialTransaction {
        return FinancialTransaction(
            transactionValue = this.transactionValue,
            transactionCategory = TransactionCategory.valueOf(this.transactionCategory),
            transactionDate = getLocalDateTime(this.transactionDate),
            transactionDescription = this.transactionDescription,
            user = user,
            tag = tag
        )
    }
}