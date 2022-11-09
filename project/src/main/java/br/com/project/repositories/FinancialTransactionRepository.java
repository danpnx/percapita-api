package br.com.project.repositories;

import br.com.project.enums.TransactionCategory;
import br.com.project.models.FinancialRecord;
import br.com.project.models.Tag;
import br.com.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialRecord, UUID> {

    List<FinancialRecord> findAllByTransactionDateAndUser(Date date, User user);

    List<FinancialRecord> findAllByTransactionCategoryAndTransactionDateAndUser(TransactionCategory category, Date date, User user);

    List<FinancialRecord> findAllByTagAndTransactionDateAndUser(Tag tag, Date date, User user);
}