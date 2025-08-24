package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for category data access operations
 * Extends JpaRepository to provide basic CRUD operations
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    
    /**
     * Find category by name
     * @param name Category name
     * @return Optional containing category if found
     */
    Optional<Category> findByName(String name);
    
    /**
     * Check if category exists by name
     * @param name Category name
     * @return true if category exists, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Find categories by parent category
     * @param parentId Parent category ID
     * @return List of subcategories
     */
    List<Category> findByParentId(String parentId);
    
    /**
     * Find root categories (categories without parent)
     * @return List of root categories
     */
    List<Category> findByParentIdIsNull();
    
    /**
     * Find categories by active status
     * @param active Active status
     * @return List of categories with specified active status
     */
    List<Category> findByActive(boolean active);
    
    /**
     * Find categories containing the search term in name or description
     * @param searchTerm Search term
     * @return List of categories matching the search criteria
     */
    @Query("SELECT c FROM Category c WHERE c.name LIKE %:searchTerm% OR c.description LIKE %:searchTerm%")
    List<Category> searchCategories(@Param("searchTerm") String searchTerm);
    
    /**
     * Find categories with product count greater than specified value
     * @param minProductCount Minimum product count
     * @return List of categories with sufficient products
     */
    @Query("SELECT c FROM Category c WHERE c.productCount >= :minProductCount")
    List<Category> findByProductCountGreaterThanEqual(@Param("minProductCount") int minProductCount);
    
    /**
     * Count categories by parent category
     * @param parentId Parent category ID
     * @return Number of subcategories
     */
    long countByParentId(String parentId);
    
    /**
     * Find categories created after a specific date
     * @param date Date to search from
     * @return List of categories created after the specified date
     */
    List<Category> findByCreatedAtAfter(LocalDateTime date);
}
