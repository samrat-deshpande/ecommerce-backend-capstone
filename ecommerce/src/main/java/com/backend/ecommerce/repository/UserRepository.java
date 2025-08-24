package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

/**
 * Repository interface for user data access operations
 * Extends JpaRepository to provide basic CRUD operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    /**
     * Find user by email address
     * @param email User's email address
     * @return Optional containing user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email
     * @param email User's email address
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by role
     * @param role User role
     * @return List of users with specified role
     */
    List<User> findByRole(String role);
    
    /**
     * Find users by first name or last name containing the search term
     * @param searchTerm Search term for name
     * @return List of users matching the search criteria
     */
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:searchTerm% OR u.lastName LIKE %:searchTerm%")
    List<User> findByNameContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Find users created after a specific date
     * @param date Date to search from
     * @return List of users created after the specified date
     */
    List<User> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Find active users
     * @return List of active users
     */
    List<User> findByActiveTrue();
    
    /**
     * Count users by role
     * @param role User role
     * @return Number of users with specified role
     */
    long countByRole(String role);
}
