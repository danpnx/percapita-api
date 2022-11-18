package br.com.project.models;

import br.com.project.enums.TransactionCategory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.Type;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@TableGenerator(name = "TB_FINANCIAL_TRANSACTION")
public class FinancialTransaction extends RepresentationModel<FinancialTransaction> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(name = "TRANSACTION_ID", columnDefinition = "CHAR(36)", unique = true)
    @Schema(hidden = true)
    private UUID transactionId;

    @Column(name = "TRANSACTION_VALUE", nullable = false)
    private BigDecimal transactionValue;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TRANSACTION_CATEGORY", nullable = false)
    private TransactionCategory transactionCategory;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "TRANSACTION_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "GMT-3")
    private Date transactionDate;

    @Column(name = "TRANSACTION_DESCRIPTION", length = 75)
    private String transactionDescription;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonBackReference(value = "user-transaction-reference")
    @Schema(hidden = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "TAG_ID")
    @JsonBackReference(value = "transaction-tag-reference")
    @Schema(hidden = true)
    private Tag tag;

    public FinancialTransaction() {
    }

    public FinancialTransaction(BigDecimal transactionValue,
                                TransactionCategory transactionCategory, Date transactionDate,
                                String transactionDescription) {
        this.transactionId = UUID.randomUUID();
        this.transactionValue = transactionValue;
        this.transactionCategory = transactionCategory;
        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transaction_id) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
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