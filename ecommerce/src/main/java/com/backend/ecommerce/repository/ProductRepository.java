package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for product data access operations
 * Extends JpaRepository to provide basic CRUD operations
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    /**
     * Find products by category
     * @param category Product category
     * @param pageable Pagination information
     * @return Page of products in the specified category
     */
    Page<Product> findByCategory(String category, Pageable pageable);
    
    /**
     * Find products by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param pageable Pagination information
     * @return Page of products within the price range
     */
    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
    
    /**
     * Find products with stock quantity greater than specified value
     * @param minStock Minimum stock quantity
     * @return List of products with sufficient stock
     */
    List<Product> findByStockQuantityGreaterThan(int minStock);
    
    /**
     * Search products by name or description containing the search term
     * @param searchTerm Search term
     * @param pageable Pagination information
     * @return Page of products matching the search criteria
     */
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:searchTerm% OR p.description LIKE %:searchTerm%")
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Find products by brand
     * @param brand Product brand
     * @param pageable Pagination information
     * @return Page of products from the specified brand
     */
    Page<Product> findByBrand(String brand, Pageable pageable);
    
    /**
     * Find products by multiple categories
     * @param categories List of category names
     * @param pageable Pagination information
     * @return Page of products in any of the specified categories
     */
    Page<Product> findByCategoryIn(List<String> categories, Pageable pageable);
    
    /**
     * Find products with discount
     * @param pageable Pagination information
     * @return Page of products that have discounts
     */
    @Query("SELECT p FROM Product p WHERE p.discountPercentage > 0")
    Page<Product> findDiscountedProducts(Pageable pageable);
    
    /**
     * Find products by rating
     * @param minRating Minimum rating value
     * @param pageable Pagination information
     * @return Page of products with rating greater than or equal to minRating
     */
    @Query("SELECT p FROM Product p WHERE p.averageRating >= :minRating")
    Page<Product> findByRatingGreaterThanEqual(@Param("minRating") Double minRating, Pageable pageable);
    
    /**
     * Count products by category
     * @param category Product category
     * @return Number of products in the specified category
     */
    long countByCategory(String category);
    
    /**
     * Find products created after a specific date
     * @param date Date to search from
     * @param pageable Pagination information
     * @return Page of products created after the specified date
     */
    Page<Product> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);
}
