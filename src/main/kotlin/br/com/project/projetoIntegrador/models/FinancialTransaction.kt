package br.com.project.projetoIntegrador.models

import br.com.project.projetoIntegrador.enums.TransactionCategory
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "TB_FINANCIAL_TRANSACTION")
data class FinancialTransaction(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TRANSACTION_ID", columnDefinition = "CHAR(36)", unique = true)
    private val transactionId: UUID,

    @Column(name = "TRANSACTION_VALUE", nullable = false)
    private var transactionValue: BigDecimal,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TRANSACTION_CATEGORY", nullable = false)
    private var transactionCategory: TransactionCategory,

    @Temporal(value = TemporalType.DATE)
    @Column(name = "TRANSACTION_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "GMT-3")
    private var transactionDate: Date,

    @Column(name = "TRANSACTION_DESCRIPTION", length = 75)
    private var transactionDescription: String,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonBackReference(value = "user-transaction-reference")
    private var user: User,

    @ManyToOne
    @JoinColumn(name = "TAG_ID")
    @JsonBackReference(value = "transaction-tag-reference")
    private var tag: Tag)  {

}