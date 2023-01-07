package br.com.project.projetoIntegrador.models

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "TB_USER")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    val id: Long?,

    @Column(name = "USERNAME",  unique = true, length = 100)
    var username: String?,

    @Column(name = "PASSWORD",  length = 200)
    var password: String?,

    @Column(name = "COMPLETE_NAME", length = 60)
    var name: String?,

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value = "user-tag-reference")
    var tags: ArrayList<Tag>,

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value = "user-transaction-reference")
    var transaction: ArrayList<FinancialTransaction>,

    @Column(name = "TOKEN")
    var token: String,

    @Column(name = "CREATION_DATE_TOKEN")
    var tokenCreateTime: LocalDateTime) {

}