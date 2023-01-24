package br.com.project.projetoIntegrador.models

import jakarta.validation.Valid
import java.beans.ConstructorProperties

data class UserLogin
@ConstructorProperties("username" , "password")
constructor(val username: String, val password: String) {

}