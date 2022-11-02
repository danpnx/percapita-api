package br.com.project.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Utilities {

    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

    // Validar se a senha digitada pelo usuário é compatível com o padrão esperado
    public static boolean validatePassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        return password.matches(regex);
    }

    // Gerar um token
    public static String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(UUID.randomUUID()).append(UUID.randomUUID()).toString();
    }

    // Verificar se um token está expirado
    public static boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }
}
