package com.banking.core.service;

import com.banking.core.entity.User;
import com.banking.core.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User operations
 */
public interface UserService {

    /**
     * Create a new user
     */
    User createUser(User user, String rawPassword);

    /**
     * Update user information
     */
    User updateUser(Long userId, User userDetails);

    /**
     * Find user by ID
     */
    Optional<User> findById(Long userId);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by mobile
     */
    Optional<User> findByMobile(String mobile);

    /**
     * Find all users with pagination
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Find users by status
     */
    List<User> findByStatus(Status status);

    /**
     * Search users by name
     */
    List<User> searchByName(String name);

    /**
     * Change user password
     */
    void changePassword(Long userId, String currentPassword, String newPassword);

    /**
     * Reset user password
     */
    String resetPassword(String email);

    /**
     * Activate user account
     */
    void activateUser(Long userId);

    /**
     * Deactivate user account
     */
    void deactivateUser(Long userId);

    /**
     * Check if email exists
     */
    boolean emailExists(String email);

    /**
     * Check if mobile exists
     */
    boolean mobileExists(String mobile);

    /**
     * Validate user credentials
     */
    boolean validateCredentials(String email, String password);

    /**
     * Get user statistics
     */
    long getTotalUserCount();

    /**
     * Get active user count
     */
    long getActiveUserCount();
}
