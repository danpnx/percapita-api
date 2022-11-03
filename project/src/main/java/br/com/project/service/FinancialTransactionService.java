package br.com.project.service;

import br.com.project.enums.TransactionCategory;
import br.com.project.models.FinancialTransaction;
import br.com.project.models.User;
import br.com.project.repositories.FinancialTransactionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final UserService userService;

    public FinancialTransactionService(FinancialTransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    public void registerTransaction(FinancialTransaction transaction, String username) {
        User u = userService.findByUsername(username).get();
        transaction.setUser(u);
        transactionRepository.save(transaction);
    }

    public void deleteTransaction(UUID transactionId) {
        // delete from the user's too
        transactionRepository.deleteById(transactionId);
    }

    public List<FinancialTransaction> getAllTransactions(String username) {
        User u = userService.findByUsername(username).get();
        return transactionRepository.findByUser(u);
    }

    public void editValue(BigDecimal value, UUID id) {
        FinancialTransaction transaction = transactionRepository.findById(id).get();
        transaction.setTransactionValue(value);
        transactionRepository.save(transaction);
    }

    public void editCategory(TransactionCategory category, UUID id) {
        FinancialTransaction transaction = transactionRepository.findById(id).get();
        transaction.setTransactionCategory(category);
        transactionRepository.save(transaction);
    }

    public void editDate(Date date, UUID id) {
        FinancialTransaction transaction = transactionRepository.findById(id).get();
        transaction.setTransactionDate(date);
        transactionRepository.save(transaction);
    }

    public void editDescription(String description, UUID id) {
        FinancialTransaction transaction = transactionRepository.findById(id).get();
        transaction.setTransactionDescription(description);
        transactionRepository.save(transaction);
    }
}
