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

    public void registerTransaction(FinancialTransaction transaction) {
        transactionRepository.save(transaction);
    }

    public void deleteTransaction(UUID transactionId) {
        // delete from the user's too
        transactionRepository.deleteById(transactionId);
    }
}
