package com.backend.ecommerce.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for catalog management operations
 * Defines the contract for product catalog business logic
 */
public interface CatalogService {
    
    /**
     * Get products with pagination and filtering
     * @param page Page number
     * @param size Page size
     * @param category Product category filter
     * @param search Search term
     * @param minPrice Minimum price filter
     * @param maxPrice Maximum price filter
     * @return Map containing products and pagination info
     */
    Map<String, Object> getProducts(int page, int size, String category, 
                                   String search, Double minPrice, Double maxPrice);
    
    /**
     * Get product by ID
     * @param productId Product ID
     * @return Optional containing product details
     */
    Optional<Map<String, Object>> getProductById(String productId);
    
    /**
     * Get all product categories
     * @return List of category data
     */
    List<Map<String, Object>> getCategories();
    
    /**
     * Search products by text query
     * @param query Search query
     * @param page Page number
     * @param size Page size
     * @return Map containing search results and pagination
     */
    Map<String, Object> searchProducts(String query, int page, int size);
    
    /**
     * Get products by category
     * @param categoryId Category ID
     * @param page Page number
     * @param size Page size
     * @return Map containing products and pagination info
     */
    Map<String, Object> getProductsByCategory(String categoryId, int page, int size);
    
    /**
     * Get product inventory status
     * @param productId Product ID
     * @return Map containing inventory information
     */
    Map<String, Object> getProductInventory(String productId);
    
    /**
     * Create new product
     * @param productData Product data
     * @return Map containing creation result
     */
    Map<String, Object> createProduct(Map<String, Object> productData);
    
    /**
     * Update existing product
     * @param productId Product ID
     * @param productData Updated product data
     * @return Map containing update result
     */
    Map<String, Object> updateProduct(String productId, Map<String, Object> productData);
    
    /**
     * Delete product
     * @param productId Product ID
     * @return Map containing deletion result
     */
    Map<String, Object> deleteProduct(String productId);
    
    /**
     * Update product inventory
     * @param productId Product ID
     * @param quantity Quantity to add/subtract
     * @return Map containing update result
     */
    Map<String, Object> updateInventory(String productId, int quantity);
}
