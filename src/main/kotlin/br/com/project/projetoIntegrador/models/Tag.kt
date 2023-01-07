package br.com.project.projetoIntegrador.models

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "TB_TABLE")
data class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TAG_ID", columnDefinition = "CHAR(36)")
    val id: UUID,

    @Column(name = "TAG_NAME", length = 25)
    var tagName: String?,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonBackReference(value = "user-tag-reference")
    var user: User,

    @OneToMany(mappedBy = "tag")
    @JsonManagedReference(value = "transaction-tag-reference")
    var transaction: ArrayList<FinancialTransaction>
) {

}