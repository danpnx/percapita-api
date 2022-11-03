package br.com.project.service;

import br.com.project.models.FinancialTransaction;
import br.com.project.repositories.FinancialTransactionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;

    public FinancialTransactionService(FinancialTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Method to register new transaction
    public void registerTransaction(FinancialTransaction transaction) {
        transactionRepository.save(transaction);
    }

    // Method to delete a transaction
    public void deleteTransaction(UUID transactionId) {
        // delete from the user's too
        transactionRepository.deleteById(transactionId);
    }
}