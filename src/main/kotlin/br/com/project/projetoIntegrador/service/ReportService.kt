package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.enums.TransactionCategory
import br.com.project.projetoIntegrador.exceptions.ResourceNotFoundException
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.payload.ReportPayload
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class ReportService(
    @Autowired private val userService: UserService,
    @Autowired private val financialTransactionService: FinancialTransactionService
) {

    fun totalPayment(date: LocalDate, user: User): BigDecimal {
        val list = financialTransactionService.findByDateAndUser(date, user)

        if (list.isEmpty()) return BigDecimal.ZERO

        return list.stream().filter { t -> t.transactionCategory == TransactionCategory.PAYMENT }
            .map { t -> t.transactionValue }
            .reduce(BigDecimal.ZERO, BigDecimal::add)
    }

    fun totalReceipt(date: LocalDate, user: User): BigDecimal {
        val list = financialTransactionService.findByDateAndUser(date, user)

        if (list.isEmpty()) return BigDecimal.ZERO

        return list.stream().filter { t -> t.transactionCategory == TransactionCategory.RECEIPT }
            .map { t -> t.transactionValue }
            .reduce(BigDecimal.ZERO, BigDecimal::add)
    }

//    fun accountBalance(date: LocalDate, username: String): Map<String, BigDecimal> {
//        val totalPayment = totalPayment(date, username)
//        val totalReceipt = totalReceipt(date, username)
//        val balance = (totalReceipt["Recebimentos"] ?: BigDecimal.ZERO)
//            .subtract(totalPayment["Pagamentos"])
//        return mapOf("Saldo" to balance)
//    }

    fun reportChart(date: LocalDate, user: User): Map<String, BigDecimal> {
        val tags = user.tags

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

    fun getReport(username: String): ReportPayload? {
        val user = userService.getUser(username)
        val date = LocalDate.now()
        val totalPayment = totalPayment(date, user)
        val totalReceipt = totalReceipt(date, user)
        val accountBalance = totalReceipt.subtract(totalPayment)
        val chartData = reportChart(date, user)
        val map = mapOf<String, BigDecimal>(
            "Saldo" to accountBalance,
            "Receitas" to totalReceipt,
            "Despesas" to totalPayment
        )

        return ReportPayload(map, chartData)
    }
}