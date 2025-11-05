package com.banking.core.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Utility class for password operations
 */
public final class PasswordUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    private PasswordUtil() {
        // Utility class
    }

    /**
     * Encode a raw password
     */
    public static String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Check if raw password matches encoded password
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Get the password encoder instance
     */
    public static PasswordEncoder getEncoder() {
        return passwordEncoder;
    }
}
