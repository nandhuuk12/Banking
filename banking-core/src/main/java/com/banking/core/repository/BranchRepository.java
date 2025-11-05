package com.banking.core.repository;

import com.banking.core.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Branch entity
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    /**
     * Find branch by IFSC code
     */
    Optional<Branch> findByIfsc(String ifsc);

    /**
     * Find branches by name
     */
    List<Branch> findByNameContainingIgnoreCase(String name);

    /**
     * Find branches by city
     */
    @Query("SELECT b FROM Branch b WHERE LOWER(b.address.city) = LOWER(:city)")
    List<Branch> findByCity(@Param("city") String city);

    /**
     * Find branches by state
     */
    @Query("SELECT b FROM Branch b WHERE LOWER(b.address.state) = LOWER(:state)")
    List<Branch> findByState(@Param("state") String state);

    /**
     * Find branches by postal code
     */
    @Query("SELECT b FROM Branch b WHERE b.address.postalCode = :postalCode")
    List<Branch> findByPostalCode(@Param("postalCode") String postalCode);

    /**
     * Check if IFSC code exists
     */
    boolean existsByIfsc(String ifsc);

    /**
     * Find branches by manager name
     */
    List<Branch> findByManagerNameContainingIgnoreCase(String managerName);

    /**
     * Find branches within geographical radius
     * Note: This is a simplified approach. In production, you might want to use PostGIS or similar
     */
    @Query("SELECT b FROM Branch b WHERE b.latitude IS NOT NULL AND b.longitude IS NOT NULL " +
           "AND ABS(b.latitude - :latitude) <= :latRange " +
           "AND ABS(b.longitude - :longitude) <= :lngRange")
    List<Branch> findBranchesNearby(@Param("latitude") BigDecimal latitude,
                                    @Param("longitude") BigDecimal longitude,
                                    @Param("latRange") BigDecimal latRange,
                                    @Param("lngRange") BigDecimal lngRange);

    /**
     * Find branches with coordinates
     */
    @Query("SELECT b FROM Branch b WHERE b.latitude IS NOT NULL AND b.longitude IS NOT NULL")
    List<Branch> findBranchesWithCoordinates();

    /**
     * Count branches by city
     */
    @Query("SELECT COUNT(b) FROM Branch b WHERE LOWER(b.address.city) = LOWER(:city)")
    long countByCity(@Param("city") String city);

    /**
     * Count branches by state
     */
    @Query("SELECT COUNT(b) FROM Branch b WHERE LOWER(b.address.state) = LOWER(:state)")
    long countByState(@Param("state") String state);

    /**
     * Get branch distribution by state
     */
    @Query("SELECT b.address.state, COUNT(b) FROM Branch b GROUP BY b.address.state")
    List<Object[]> getBranchDistributionByState();

    /**
     * Get branch distribution by city
     */
    @Query("SELECT b.address.city, COUNT(b) FROM Branch b GROUP BY b.address.city ORDER BY COUNT(b) DESC")
    List<Object[]> getBranchDistributionByCity();

    /**
     * Find branches with email
     */
    List<Branch> findByEmailIsNotNull();

    /**
     * Find branches with contact number
     */
    List<Branch> findByContactIsNotNull();
}
