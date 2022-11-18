package br.com.project.service;

import br.com.project.enums.TransactionCategory;
import br.com.project.exceptions.ResourceNotFoundException;
import br.com.project.models.FinancialTransaction;
import br.com.project.payload.ReportResponse;
import br.com.project.models.Tag;
import br.com.project.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReportService {

    @Autowired
    private UserService userService;

    @Autowired
    private FinancialTransactionService transactionService;

    public ReportResponse<BigDecimal> totalPayment(Date date, String username) {
        User u = userService.findByUsername(username);

        List<FinancialTransaction> list = transactionService.findByDateAndUser(date, u);

        if(list.isEmpty()) {
            ReportResponse<BigDecimal> reportResponse = new ReportResponse<>();
            reportResponse.setResponseValue(BigDecimal.ZERO);
            return reportResponse;
        }

        ReportResponse<BigDecimal> reportResponse = new ReportResponse<>();
        reportResponse.setResponseValue(
                list.stream()
                        .filter(t -> t.getTransactionCategory().equals(TransactionCategory.PAYMENT))
                        .map(FinancialTransaction::getTransactionValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        return reportResponse;
    }

    public ReportResponse<BigDecimal> totalReceipt(Date date, String username) {
        User u = userService.findByUsername(username);

        List<FinancialTransaction> list = transactionService.findByDateAndUser(date, u);

        if(list.isEmpty()) {
            ReportResponse<BigDecimal> reportResponse = new ReportResponse<>();
            reportResponse.setResponseValue(BigDecimal.ZERO);
            return reportResponse;
        }

        ReportResponse<BigDecimal> reportResponse = new ReportResponse<>();
        reportResponse.setResponseValue(
                list.stream()
                        .filter(t -> t.getTransactionCategory().equals(TransactionCategory.RECEIPT))
                        .map(FinancialTransaction::getTransactionValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        return reportResponse;
    }

    public ReportResponse<BigDecimal> accountBalance(Date date, String username) {
        ReportResponse<BigDecimal> totalPayment = totalPayment(date, username);
        ReportResponse<BigDecimal> totalReceipt = totalReceipt(date, username);

        BigDecimal balance = totalReceipt.getResponseValue().subtract(totalPayment.getResponseValue());
        ReportResponse<BigDecimal> responseBalance = new ReportResponse<>();
        responseBalance.setResponseValue(balance);

        return responseBalance;
    }

    public ReportResponse<Map<String, BigDecimal>> reportChart(Date date, String username) {
        User u = userService.findByUsername(username);

        List<Tag> tags = u.getTags();

        if(tags.isEmpty()) {
            throw new ResourceNotFoundException("Você ainda não possui nenhuma tag");
        }

        Map<String, BigDecimal> map = new HashMap<>();

        for(Tag tag: tags) {
            // Verifica se a tag não possui nenhuma transação vinculada a ela, se for o caso
            if(tag.getTransactions().isEmpty()) {
                continue;
            }
            if(
                    tag.getTransactions().stream()
                            .filter(t -> t.getTransactionDate().equals(date))
                            .filter(t -> t.getTransactionCategory().equals(TransactionCategory.PAYMENT))
                            .toList()
                            .isEmpty()
            ) continue;

            map.put(
                    tag.getTagName(),
                    tag.getTransactions().stream()
                            .filter(t -> t.getTransactionDate().equals(date))
                            .filter(t -> t.getTransactionCategory().equals(TransactionCategory.PAYMENT))
                            .map(FinancialTransaction::getTransactionValue)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
        }

        ReportResponse<Map<String, BigDecimal>> reportResponse = new ReportResponse<>();
        reportResponse.setResponseValue(map);
        return reportResponse;
    }
}
