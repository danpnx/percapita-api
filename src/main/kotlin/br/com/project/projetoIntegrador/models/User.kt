package br.com.project.projetoIntegrador.models

import com.fasterxml.jackson.annotation.JsonManagedReference
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "TB_USER")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    @Schema(hidden = true)
    val id: Long? = null,

    @Column(name = "USERNAME",  unique = true, length = 100, nullable = false)
    var username: String?,

    @Column(name = "PASSWORD",  length = 200)
    var password: String?,

    @Column(name = "COMPLETE_NAME", length = 60)
    var name: String?,

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value = "user-tag-reference")
    @Schema(hidden = true)
    var tags: MutableSet<Tag> = mutableSetOf(),

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value = "user-transaction-reference")
    @Schema(hidden = true)
    var transactions: MutableSet<FinancialTransaction> = mutableSetOf(),

    @Column(name = "TOKEN")
    @Schema(hidden = true)
    var token: String? = null,

    @Column(name = "CREATION_DATE_TOKEN")
    @Schema(hidden = true)
    var tokenCreateTime: LocalDateTime? = null
)
