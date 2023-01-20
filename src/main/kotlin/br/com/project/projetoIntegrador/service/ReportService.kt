package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.enums.TransactionCategory
import br.com.project.projetoIntegrador.exceptions.ResourceNotFoundException
import br.com.project.projetoIntegrador.payload.ReportResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.text.SimpleDateFormat

@Service
class ReportService(
    @Autowired private val userService: UserService,
    @Autowired private val financialTransactionService: FinancialTransactionService
) {

    fun totalPayment(date: SimpleDateFormat, username: String): ReportResponse<BigDecimal> {
        val user = userService.findByUsername(username)
        val list = financialTransactionService.findByDateAndUser(date, user.get())

        if(list.isEmpty()) return ReportResponse(BigDecimal.ZERO)

        return ReportResponse(
            list.stream().filter { t -> t.transactionCategory == TransactionCategory.PAYMENT }
                .map { t -> t.transactionValue }
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        )
    }

    fun totalReceipt(date: SimpleDateFormat, username: String): ReportResponse<BigDecimal> {
        val user = userService.findByUsername(username)
        val list = financialTransactionService.findByDateAndUser(date, user.get())

        if(list.isEmpty()) return ReportResponse(BigDecimal.ZERO)

        return ReportResponse(
            list.stream().filter { t -> t.transactionCategory == TransactionCategory.RECEIPT }
                .map { t -> t.transactionValue }
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        )
    }

    fun accountBalance(date: SimpleDateFormat, username: String): ReportResponse<BigDecimal> {
        val totalPayment = totalPayment(date, username)
        val totalReceipt = totalReceipt(date, username)
        val balance = totalReceipt.responseValue.subtract(totalPayment.responseValue)
        return ReportResponse(balance)
    }

    fun reportChart(date: SimpleDateFormat, username: String)
    : ReportResponse<Map<String, BigDecimal>> {
        val user = userService.findByUsername(username)
        val tags = user.get().tags

        if (tags.isEmpty()) throw ResourceNotFoundException("Você ainda não possui nenhuma tag")

        val map: MutableMap<String, BigDecimal> = mutableMapOf()

        for(tag in tags) {
            if (
                tag.transaction.stream()
                    .filter{ transaction -> transaction.transactionDate == date }
                    .filter{ transaction ->
                        transaction.transactionCategory == TransactionCategory.PAYMENT
                    }
                    .toList().isEmpty()
            ) continue

            val total = tag.transaction.stream()
                .filter { t -> t.transactionDate == date }
                .filter { t -> t.transactionCategory == TransactionCategory.PAYMENT }
                .map { t -> t.transactionValue }
                .reduce(BigDecimal.ZERO, BigDecimal::add)

            map += Pair(tag.tagName, total)
        }

        return ReportResponse(map)
    }
}