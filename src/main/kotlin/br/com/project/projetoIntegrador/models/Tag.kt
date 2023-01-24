package br.com.project.projetoIntegrador.models

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.springframework.hateoas.RepresentationModel
import java.util.UUID

@Entity
@Table(name = "TB_TAG")
data class Tag(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TAG_ID", unique = true, columnDefinition = "VARBINARY(36)")
    val id: UUID? = null,

    @Column(name = "TAG_NAME", length = 25, nullable = false)
    var tagName: String,

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    @JsonBackReference(value = "user-tag-reference")
    var user: User? = null,

    @OneToMany(mappedBy = "tag")
    @JsonManagedReference(value = "transaction-tag-reference")
    var transaction: MutableList<FinancialTransaction> = mutableListOf()
) : RepresentationModel<Tag>()