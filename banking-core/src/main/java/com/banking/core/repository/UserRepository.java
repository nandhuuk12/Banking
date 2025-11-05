package com.banking.core.repository;

import com.banking.core.entity.User;
import com.banking.core.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by mobile number
     */
    Optional<User> findByMobile(String mobile);

    /**
     * Find users by status
     */
    List<User> findByStatus(Status status);

    /**
     * Find users by status with pagination
     */
    Page<User> findByStatus(Status status, Pageable pageable);

    /**
     * Check if email already exists
     */
    boolean existsByEmail(String email);

    /**
     * Check if mobile number already exists
     */
    boolean existsByMobile(String mobile);

    /**
     * Search users by name (first name or last name)
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContaining(@Param("name") String name);

    /**
     * Find users by role
     */
    List<User> findByRole(String role);

    /**
     * Find users by city
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.address.city) = LOWER(:city)")
    List<User> findByCity(@Param("city") String city);

    /**
     * Count users by status
     */
    long countByStatus(Status status);

    /**
     * Find users created in the last N days
     */
    @Query("SELECT u FROM User u WHERE u.createdAt >= CURRENT_TIMESTAMP - :days DAY")
    List<User> findRecentUsers(@Param("days") int days);
}
