package br.com.project.utils;

import java.math.BigDecimal;

public class InputUtils {

    // Validar se a senha digitada pelo usuário é compatível com o padrão esperado
    public static boolean validatePassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        return password.matches(regex);
    }

    // Verificar se um valor é menor ou igual a zero
    public static boolean isGreaterThanZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    // Verificar se o tamanho do nome excede 60 caracteres
    public static boolean isExceedingCompleteNameSize(String completeName) {
        return completeName.length() > 60;
    }

    // Verificar se o tamanho do email excede 100 caracteres
    public static boolean isExceedingUsernameSize(String username) {
        return username.length() > 100;
    }

    // Verificar se o tamanho da senha excede 20 caracteres
    public static boolean isExceedingPasswordSize(String password) {
        return password.length() > 20;
    }

    // Verificar se o tamanho da tag excede 25 caracteres
    public static boolean isExceedingTagNameSize(String tagName) {
        return tagName.length() > 25;
    }
}
