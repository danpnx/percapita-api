package br.com.project.repositories;

import br.com.project.enums.TransactionCategory;
import br.com.project.models.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, UUID> {
    List<FinancialTransaction> findFinancialTransactionByTransactionCategory(TransactionCategory category);
}