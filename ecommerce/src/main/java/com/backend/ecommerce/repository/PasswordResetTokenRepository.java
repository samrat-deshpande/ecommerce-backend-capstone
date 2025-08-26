package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository interface for PasswordResetToken entity
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    
    /**
     * Find token by token value
     * @param token The token string
     * @return Optional containing the token entity
     */
    Optional<PasswordResetToken> findByToken(String token);
    
    /**
     * Find token by user ID
     * @param userId The user ID
     * @return Optional containing the token entity
     */
    Optional<PasswordResetToken> findByUserId(String userId);
    
    /**
     * Find valid token by user ID (not expired and not used)
     * @param userId The user ID
     * @return Optional containing the valid token entity
     */
    @Query("SELECT prt FROM PasswordResetToken prt WHERE prt.userId = :userId AND prt.expiryDate > :now AND prt.used = false")
    Optional<PasswordResetToken> findValidTokenByUserId(@Param("userId") String userId, @Param("now") LocalDateTime now);
    
    /**
     * Delete expired tokens
     * @param now Current timestamp
     */
    @Modifying
    @Query("DELETE FROM PasswordResetToken prt WHERE prt.expiryDate <= :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    /**
     * Mark token as used
     * @param tokenId The token ID
     */
    @Modifying
    @Query("UPDATE PasswordResetToken prt SET prt.used = true WHERE prt.id = :tokenId")
    void markTokenAsUsed(@Param("tokenId") String tokenId);
    
    /**
     * Delete all tokens for a user
     * @param userId The user ID
     */
    @Modifying
    @Query("DELETE FROM PasswordResetToken prt WHERE prt.userId = :userId")
    void deleteAllTokensForUser(@Param("userId") String userId);
    
    /**
     * Count active tokens for a user
     * @param userId The user ID
     * @return Number of active tokens
     */
    @Query("SELECT COUNT(prt) FROM PasswordResetToken prt WHERE prt.userId = :userId AND prt.expiryDate > :now AND prt.used = false")
    long countActiveTokensForUser(@Param("userId") String userId, @Param("now") LocalDateTime now);
}
