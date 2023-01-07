package br.com.project.projetoIntegrador.utils

class InputUtils {
    companion object {
        fun isExceedingCompleteNameSize(completeName: String): Boolean {
            return completeName.length > 60
        }

        fun isExceedingUsernameSize(username: String): Boolean {
            return username.length > 100
        }

        fun isExceedingPasswordSize(password: String): Boolean {
            return password.length > 20
        }

        fun isExceedingTagNameSize(tagName: String): Boolean {
            return tagName.length > 25
        }

        fun validatePassword(password: String): Boolean {
            val regex: String = ("(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+{}|?>.<,:;~`'\"]).{8,20}\$")
                //("^(?=.*[a-z])(?=.*[A-Z])")
            //("(?=.*[@#$%^&+=])")
            //("(?=\\S+$).{8,20}$")
            return password.contains(regex)
        }
    }
}