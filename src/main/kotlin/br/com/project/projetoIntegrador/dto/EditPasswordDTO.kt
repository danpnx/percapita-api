package br.com.project.projetoIntegrador.dto

/**
 * @project projetoIntegrador
 * @author Daniel Augusto on 10/02/2023
 **/
data class EditPasswordDTO(
    val actualPassword: String,
    val newPassword: String,
    val newName: String)