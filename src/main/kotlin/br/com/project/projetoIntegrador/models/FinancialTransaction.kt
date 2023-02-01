package br.com.project.projetoIntegrador.models

import br.com.project.projetoIntegrador.enums.TransactionCategory
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "TB_FINANCIAL_TRANSACTION")
class FinancialTransaction(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
        name = "TRANSACTION_ID",
        columnDefinition = "VARBINARY(36)",
        unique = true,
        nullable = false
    )
    @Schema(hidden = true)
    val transactionId: UUID? = null,

    @Column(name = "TRANSACTION_VALUE", nullable = false)
    var transactionValue: BigDecimal,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TRANSACTION_CATEGORY", nullable = false)
    var transactionCategory: TransactionCategory? = null,

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "TRANSACTION_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT-3")
    var transactionDate: LocalDateTime,

    @Column(name = "TRANSACTION_DESCRIPTION", length = 75)
    var transactionDescription: String = "",

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    @JsonBackReference(value = "user-transaction-reference")
    @Schema(hidden = true)
    var user: User? = null,

    @ManyToOne
    @JoinColumn(name = "TAG_ID", nullable = false)
    @JsonBackReference(value = "transaction-tag-reference")
    @Schema(hidden = true)
    var tag: Tag? = null
    ) : RepresentationModel<FinancialTransaction>()