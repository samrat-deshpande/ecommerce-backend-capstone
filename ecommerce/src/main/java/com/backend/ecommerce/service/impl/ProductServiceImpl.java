package com.backend.ecommerce.service.impl;

import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.Category;
import com.backend.ecommerce.repository.ProductRepository;
import com.backend.ecommerce.repository.CategoryRepository;
import com.backend.ecommerce.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of CatalogService interface
 * Provides business logic for product catalog operations
 */
@Service
public class ProductServiceImpl implements CatalogService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Map<String, Object> getProducts(int page, int size, String category, 
                                         String search, Double minPrice, Double maxPrice) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage;
            
            // Apply filters based on parameters
            if (category != null && !category.trim().isEmpty()) {
                productPage = productRepository.findByCategory(category.trim(), pageable);
            } else if (search != null && !search.trim().isEmpty()) {
                productPage = productRepository.searchProducts(search.trim(), pageable);
            } else if (minPrice != null && maxPrice != null) {
                productPage = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
            } else {
                productPage = productRepository.findAll(pageable);
            }
            
            response.put("success", true);
            response.put("products", productPage.getContent());
            response.put("currentPage", productPage.getNumber());
            response.put("totalPages", productPage.getTotalPages());
            response.put("totalElements", productPage.getTotalElements());
            response.put("size", productPage.getSize());
            response.put("hasNext", productPage.hasNext());
            response.put("hasPrevious", productPage.hasPrevious());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve products: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public Optional<Map<String, Object>> getProductById(String productId) {
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                return Optional.empty();
            }
            
            Product product = productOpt.get();
            Map<String, Object> productDetails = new HashMap<>();
            productDetails.put("id", product.getId());
            productDetails.put("name", product.getName());
            productDetails.put("description", product.getDescription());
            productDetails.put("price", product.getPrice());
            productDetails.put("category", product.getCategory());
            productDetails.put("subCategory", product.getSubCategory());
            productDetails.put("brand", product.getBrand());
            productDetails.put("model", product.getModel());
            productDetails.put("sku", product.getSku());
            productDetails.put("stockQuantity", product.getStockQuantity());
            productDetails.put("imageUrl", product.getImageUrl());
            productDetails.put("additionalImages", product.getAdditionalImages());
            productDetails.put("averageRating", product.getAverageRating());
            productDetails.put("reviewCount", product.getReviewCount());
            productDetails.put("featured", product.isFeatured());
            productDetails.put("active", product.isActive());
            productDetails.put("createdAt", product.getCreatedAt());
            productDetails.put("updatedAt", product.getUpdatedAt());
            
            return Optional.of(productDetails);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Map<String, Object>> getCategories() {
        try {
            List<Category> categories = categoryRepository.findByActive(true);
            return categories.stream()
                .map(category -> {
                    Map<String, Object> categoryMap = new HashMap<>();
                    categoryMap.put("id", category.getId());
                    categoryMap.put("name", category.getName());
                    categoryMap.put("description", category.getDescription());
                    categoryMap.put("parentId", category.getParentId());
                    categoryMap.put("productCount", category.getProductCount());
                    categoryMap.put("active", category.isActive());
                    categoryMap.put("createdAt", category.getCreatedAt());
                    categoryMap.put("updatedAt", category.getUpdatedAt());
                    return categoryMap;
                })
                .toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public Map<String, Object> searchProducts(String query, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (query == null || query.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Search query is required");
                return response;
            }
            
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage = productRepository.searchProducts(query.trim(), pageable);
            
            response.put("success", true);
            response.put("products", productPage.getContent());
            response.put("query", query);
            response.put("currentPage", productPage.getNumber());
            response.put("totalPages", productPage.getTotalPages());
            response.put("totalElements", productPage.getTotalElements());
            response.put("count", productPage.getContent().size());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to search products: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public Map<String, Object> getProductsByCategory(String categoryId, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (categoryId == null || categoryId.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Category ID is required");
                return response;
            }
            
            // Get category name from categoryId
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Category not found");
                return response;
            }
            
            String categoryName = categoryOpt.get().getName();
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage = productRepository.findByCategory(categoryName, pageable);
            
            response.put("success", true);
            response.put("products", productPage.getContent());
            response.put("categoryId", categoryId);
            response.put("categoryName", categoryName);
            response.put("currentPage", productPage.getNumber());
            response.put("totalPages", productPage.getTotalPages());
            response.put("totalElements", productPage.getTotalElements());
            response.put("count", productPage.getContent().size());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve products by category: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public Map<String, Object> getProductInventory(String productId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Product not found");
                return response;
            }
            
            Product product = productOpt.get();
            Map<String, Object> inventory = new HashMap<>();
            inventory.put("productId", product.getId());
            inventory.put("productName", product.getName());
            inventory.put("stockQuantity", product.getStockQuantity());
            inventory.put("minStockLevel", product.getMinStockLevel());
            inventory.put("available", product.isInStock());
            inventory.put("lowStock", product.isLowStock());
            inventory.put("lastUpdated", product.getUpdatedAt());
            
            response.put("success", true);
            response.put("inventory", inventory);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve product inventory: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public Map<String, Object> createProduct(Map<String, Object> productData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate required fields
            if (productData.get("name") == null || productData.get("name").toString().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Product name is required");
                return response;
            }
            
            if (productData.get("price") == null) {
                response.put("success", false);
                response.put("message", "Product price is required");
                return response;
            }
            
            if (productData.get("category") == null) {
                response.put("success", false);
                response.put("message", "Product category is required");
                return response;
            }
            
            // Create new product
            Product product = new Product();
            product.setName(productData.get("name").toString().trim());
            product.setDescription(productData.get("description") != null ? 
                productData.get("description").toString() : "");
            product.setPrice(new BigDecimal(productData.get("price").toString()));
            product.setCategory(productData.get("category").toString().trim());
            product.setStockQuantity(productData.get("stockQuantity") != null ? 
                Integer.parseInt(productData.get("stockQuantity").toString()) : 0);
            product.setBrand(productData.get("brand") != null ? 
                productData.get("brand").toString() : null);
            product.setModel(productData.get("model") != null ? 
                productData.get("model").toString() : null);
            product.setSku(productData.get("sku") != null ? 
                productData.get("sku").toString() : null);
            product.setActive(true);
            
            Product savedProduct = productRepository.save(product);
            
            response.put("success", true);
            response.put("message", "Product created successfully");
            response.put("productId", savedProduct.getId());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to create product: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public Map<String, Object> updateProduct(String productId, Map<String, Object> productData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Product not found");
                return response;
            }
            
            Product product = productOpt.get();
            
            // Update fields if provided
            if (productData.get("name") != null && !productData.get("name").toString().trim().isEmpty()) {
                product.setName(productData.get("name").toString().trim());
            }
            
            if (productData.get("description") != null) {
                product.setDescription(productData.get("description").toString());
            }
            
            if (productData.get("price") != null) {
                product.setPrice(new BigDecimal(productData.get("price").toString()));
            }
            
            if (productData.get("category") != null) {
                product.setCategory(productData.get("category").toString());
            }
            
            if (productData.get("stockQuantity") != null) {
                product.setStockQuantity(Integer.parseInt(productData.get("stockQuantity").toString()));
            }
            
            if (productData.get("brand") != null) {
                product.setBrand(productData.get("brand").toString());
            }
            
            if (productData.get("model") != null) {
                product.setModel(productData.get("model").toString());
            }
            
            if (productData.get("active") != null) {
                product.setActive(Boolean.parseBoolean(productData.get("active").toString()));
            }
            
            productRepository.save(product);
            
            response.put("success", true);
            response.put("message", "Product updated successfully");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update product: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public Map<String, Object> deleteProduct(String productId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Product not found");
                return response;
            }
            
            Product product = productOpt.get();
            product.setActive(false);
            productRepository.save(product);
            
            response.put("success", true);
            response.put("message", "Product deleted successfully");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete product: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public Map<String, Object> updateInventory(String productId, int quantity) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Product not found");
                return response;
            }
            
            Product product = productOpt.get();
            int newQuantity = product.getStockQuantity() + quantity;
            
            if (newQuantity < 0) {
                response.put("success", false);
                response.put("message", "Insufficient stock for this operation");
                return response;
            }
            
            product.setStockQuantity(newQuantity);
            productRepository.save(product);
            
            response.put("success", true);
            response.put("message", "Inventory updated successfully");
            response.put("newStockQuantity", newQuantity);
            response.put("change", quantity);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update inventory: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Get products by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param page Page number
     * @param size Page size
     * @return Products within price range
     */
    public Map<String, Object> getProductsByPriceRange(double minPrice, double maxPrice, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
            
            response.put("success", true);
            response.put("products", productPage.getContent());
            response.put("minPrice", minPrice);
            response.put("maxPrice", maxPrice);
            response.put("currentPage", productPage.getNumber());
            response.put("totalPages", productPage.getTotalPages());
            response.put("totalElements", productPage.getTotalElements());
            response.put("count", productPage.getContent().size());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve products by price range: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Get products with low stock (below threshold)
     * @param threshold Stock threshold
     * @return Products with low stock
     */
    public Map<String, Object> getProductsWithLowStock(int threshold) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Product> products = productRepository.findByStockQuantityGreaterThan(threshold - 1);
            List<Product> lowStockProducts = products.stream()
                .filter(p -> p.getStockQuantity() <= threshold)
                .toList();
            
            response.put("success", true);
            response.put("products", lowStockProducts);
            response.put("threshold", threshold);
            response.put("count", lowStockProducts.size());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve products with low stock: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Get recently added products
     * @param days Number of days to look back
     * @param page Page number
     * @param size Page size
     * @return Recently added products
     */
    public Map<String, Object> getRecentlyAddedProducts(int days, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage = productRepository.findByCreatedAtAfter(cutoffDate, pageable);
            
            response.put("success", true);
            response.put("products", productPage.getContent());
            response.put("days", days);
            response.put("currentPage", productPage.getNumber());
            response.put("totalPages", productPage.getTotalPages());
            response.put("totalElements", productPage.getTotalElements());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve recently added products: " + e.getMessage());
        }
        
        return response;
    }
}
