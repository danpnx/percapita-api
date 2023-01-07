package br.com.project.projetoIntegrador.models

import jakarta.validation.Valid

class UserLogin(
    @Valid
    var username: String?,
    @Valid
    var password: String?,
    @Valid
    var name: String?) {
}