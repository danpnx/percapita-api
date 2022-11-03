package br.com.project.models;

import br.com.project.enums.TransactionCategory;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@TableGenerator(name = "TB_FINANCIAL_TRANSACTION")
public class FinancialTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TRANSACTION_ID", columnDefinition = "BINARY(16)")
    private UUID transactionId;

    @Column(name = "TRANSACTION_VALUE", nullable = false)
    private BigDecimal transactionValue;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TRANSACTION_CATEGORY", nullable = false)
    private TransactionCategory transactionCategory;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "TRANSACTION_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date transactionDate;

    @Column(name = "TRANSACTION_DESCRIPTION", length = 75)
    private String transactionDescription;

    public FinancialTransaction() {
    }

    public FinancialTransaction(UUID transaction_id, BigDecimal transactionValue,
                                TransactionCategory transactionCategory, Date transactionDate,
                                String transactionDescription) {
        this.transactionId = transaction_id;
        this.transactionValue = transactionValue;
        this.transactionCategory = transactionCategory;
        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
    }

    public UUID getTransaction_id() {
        return transactionId;
    }

    public void setTransaction_id(UUID transaction_id) {
        this.transactionId = transaction_id;
    }

    public BigDecimal getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(BigDecimal transactionValue) {
        this.transactionValue = transactionValue;
    }

    public TransactionCategory getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialTransaction that = (FinancialTransaction) o;
        return transactionId.equals(that.transactionId) &&
                transactionValue.equals(that.transactionValue) &&
                transactionCategory == that.transactionCategory &&
                transactionDate.equals(that.transactionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, transactionValue, transactionCategory, transactionDate);
    }
}