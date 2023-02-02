package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.enums.TransactionCategory
import br.com.project.projetoIntegrador.exceptions.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class ReportService(
    @Autowired private val userService: UserService,
    @Autowired private val financialTransactionService: FinancialTransactionService
) {

    fun totalPayment(date: LocalDate, username: String): Map<String, BigDecimal> {
        val user = userService.findByUsername(username)
        val list = financialTransactionService.findByDateAndUser(date, user.get())

        if(list.isEmpty()) return mapOf("Pagamentos" to BigDecimal.ZERO)

        val total = list.stream().filter { t -> t.transactionCategory == TransactionCategory.PAYMENT }
            .map { t -> t.transactionValue }
            .reduce(BigDecimal.ZERO, BigDecimal::add)

        return mapOf("Pagamentos" to total)
    }

    fun totalReceipt(date: LocalDate, username: String): Map<String, BigDecimal> {
        val user = userService.findByUsername(username)
        val list = financialTransactionService.findByDateAndUser(date, user.get())

        if(list.isEmpty()) return mapOf("Recebimentos" to BigDecimal.ZERO)

        val total = list.stream().filter { t -> t.transactionCategory == TransactionCategory.RECEIPT }
            .map { t -> t.transactionValue }
            .reduce(BigDecimal.ZERO, BigDecimal::add)

        return mapOf("Recebimentos" to total)
    }

    fun accountBalance(date: LocalDate, username: String): Map<String, BigDecimal> {
        val totalPayment = totalPayment(date, username)
        val totalReceipt = totalReceipt(date, username)
        val balance = (totalReceipt["Recebimentos"] ?: BigDecimal.ZERO)
            .subtract(totalPayment["Pagamentos"])
        return mapOf("Saldo" to balance)
    }

    fun reportChart(date: LocalDate, username: String)
    : Map<String, BigDecimal> {
        val user = userService.findByUsername(username)
        val tags = user.get().tags

        if (tags.isEmpty()) throw ResourceNotFoundException("Você ainda não possui nenhuma tag")

        val map: MutableMap<String, BigDecimal> = mutableMapOf()

        for(tag in tags) {
            if (
                tag.transactions.stream()
                    .filter{ transaction -> transaction.transactionDate.year == date.year &&
                                transaction.transactionDate.month == date.month
                    }
                    .filter{ transaction ->
                        transaction.transactionCategory == TransactionCategory.PAYMENT
                    }
                    .toList().isEmpty()
            ) continue

            val total = tag.transactions.stream()
                .filter { transaction -> transaction.transactionDate.year == date.year &&
                        transaction.transactionDate.month == date.month
                }
                .filter { t -> t.transactionCategory == TransactionCategory.PAYMENT }
                .map { t -> t.transactionValue }
                .reduce(BigDecimal.ZERO, BigDecimal::add)

            map[tag.tagName] = total
        }

        return map
    }
}