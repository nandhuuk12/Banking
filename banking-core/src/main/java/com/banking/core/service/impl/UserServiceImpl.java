package com.banking.core.service.impl;

import com.banking.core.entity.User;
import com.banking.core.enums.Status;
import com.banking.core.exception.BankingException;
import com.banking.core.exception.UserNotFoundException;
import com.banking.core.repository.UserRepository;
import com.banking.core.service.UserService;
import com.banking.core.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of UserService
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user, String rawPassword) {
        logger.info("Creating new user with email: {}", user.getEmail());
        
        // Validate user data
        validateUserForCreation(user);
        
        // Check if email or mobile already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BankingException("Email already exists: " + user.getEmail(), "DUPLICATE_EMAIL");
        }
        
        if (userRepository.existsByMobile(user.getMobile())) {
            throw new BankingException("Mobile number already exists: " + user.getMobile(), "DUPLICATE_MOBILE");
        }
        
        // Encode password
        user.setPasswordHash(PasswordUtil.encode(rawPassword));
        
        // Set default values
        if (user.getStatus() == null) {
            user.setStatus(Status.ACTIVE);
        }
        
        if (!StringUtils.hasText(user.getRole())) {
            user.setRole("ROLE_USER");
        }
        
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with ID: {}", savedUser.getId());
        
        return savedUser;
    }

    @Override
    public User updateUser(Long userId, User userDetails) {
        logger.info("Updating user with ID: {}", userId);
        
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        // Update allowed fields
        if (StringUtils.hasText(userDetails.getFirstName())) {
            existingUser.setFirstName(userDetails.getFirstName());
        }
        
        if (StringUtils.hasText(userDetails.getLastName())) {
            existingUser.setLastName(userDetails.getLastName());
        }
        
        if (StringUtils.hasText(userDetails.getMobile()) && !userDetails.getMobile().equals(existingUser.getMobile())) {
            if (userRepository.existsByMobile(userDetails.getMobile())) {
                throw new BankingException("Mobile number already exists: " + userDetails.getMobile(), "DUPLICATE_MOBILE");
            }
            existingUser.setMobile(userDetails.getMobile());
        }
        
        if (userDetails.getAddress() != null) {
            existingUser.setAddress(userDetails.getAddress());
        }
        
        User updatedUser = userRepository.save(existingUser);
        logger.info("User updated successfully: {}", updatedUser.getId());
        
        return updatedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByMobile(String mobile) {
        return userRepository.findByMobile(mobile);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByStatus(Status status) {
        return userRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> searchByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        logger.info("Changing password for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        // Verify current password
        if (!PasswordUtil.matches(currentPassword, user.getPasswordHash())) {
            throw new BankingException("Current password is incorrect", "INVALID_PASSWORD");
        }
        
        // Validate new password strength (basic validation)
        validatePasswordStrength(newPassword);
        
        user.setPasswordHash(PasswordUtil.encode(newPassword));
        userRepository.save(user);
        
        logger.info("Password changed successfully for user ID: {}", userId);
    }

    @Override
    public String resetPassword(String email) {
        logger.info("Resetting password for email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        
        // Generate temporary password
        String temporaryPassword = generateTemporaryPassword();
        user.setPasswordHash(PasswordUtil.encode(temporaryPassword));
        userRepository.save(user);
        
        logger.info("Password reset successfully for user ID: {}", user.getId());
        
        return temporaryPassword;
    }

    @Override
    public void activateUser(Long userId) {
        logger.info("Activating user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        
        logger.info("User activated successfully: {}", userId);
    }

    @Override
    public void deactivateUser(Long userId) {
        logger.info("Deactivating user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        user.setStatus(Status.INACTIVE);
        userRepository.save(user);
        
        logger.info("User deactivated successfully: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean mobileExists(String mobile) {
        return userRepository.existsByMobile(mobile);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateCredentials(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }
        
        User user = userOpt.get();
        return user.getStatus() == Status.ACTIVE && PasswordUtil.matches(password, user.getPasswordHash());
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalUserCount() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveUserCount() {
        return userRepository.countByStatus(Status.ACTIVE);
    }

    private void validateUserForCreation(User user) {
        if (user == null) {
            throw new BankingException("User cannot be null", "INVALID_INPUT");
        }
        
        if (!StringUtils.hasText(user.getEmail())) {
            throw new BankingException("Email is required", "INVALID_INPUT");
        }
        
        if (!StringUtils.hasText(user.getFirstName())) {
            throw new BankingException("First name is required", "INVALID_INPUT");
        }
        
        if (!StringUtils.hasText(user.getLastName())) {
            throw new BankingException("Last name is required", "INVALID_INPUT");
        }
        
        if (!StringUtils.hasText(user.getMobile())) {
            throw new BankingException("Mobile number is required", "INVALID_INPUT");
        }
        
        if (user.getDateOfBirth() == null) {
            throw new BankingException("Date of birth is required", "INVALID_INPUT");
        }
        
        if (user.getDateOfBirth().isAfter(LocalDate.now().minusYears(18))) {
            throw new BankingException("User must be at least 18 years old", "INVALID_AGE");
        }
    }
    
    private void validatePasswordStrength(String password) {
        if (!StringUtils.hasText(password) || password.length() < 8) {
            throw new BankingException("Password must be at least 8 characters long", "WEAK_PASSWORD");
        }
        
        // Add more password strength validations as needed
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        if (!hasUpper || !hasLower || !hasDigit) {
            throw new BankingException("Password must contain uppercase, lowercase and digit", "WEAK_PASSWORD");
        }
    }
    
    private String generateTemporaryPassword() {
        return "Temp" + UUID.randomUUID().toString().substring(0, 8).replace("-", "") + "@123";
    }
}
