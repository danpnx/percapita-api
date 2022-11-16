package br.com.project.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class ContextUtils {
    public static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
