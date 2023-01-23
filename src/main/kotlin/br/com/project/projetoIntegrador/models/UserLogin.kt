package br.com.project.projetoIntegrador.models

import jakarta.validation.Valid
import java.beans.ConstructorProperties

data class UserLogin
@ConstructorProperties( "email" , "password" )
constructor(val email: String, val password: String) {

}