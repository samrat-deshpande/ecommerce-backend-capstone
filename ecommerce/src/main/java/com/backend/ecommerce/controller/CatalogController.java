package com.backend.ecommerce.controller;

import com.backend.ecommerce.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for managing product catalog operations
 * Provides endpoints for browsing products, product details, search, and category management
 */
@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "*")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    /**
     * Get all products with pagination and filtering
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @param category Product category filter (optional)
     * @param search Search term filter (optional)
     * @param minPrice Minimum price filter (optional)
     * @param maxPrice Maximum price filter (optional)
     * @return Paginated list of products
     */
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        
        Map<String, Object> response = catalogService.getProducts(page, size, category, search, minPrice, maxPrice);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get product by ID with detailed information
     * @param productId Product ID
     * @return Product details
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable String productId) {
        var productOpt = catalogService.getProductById(productId);
        
        if (productOpt.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("product", productOpt.get());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Product not found");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all active categories
     * @return List of categories
     */
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        var categories = catalogService.getCategories();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("categories", categories);
        response.put("count", categories.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Search products by keyword
     * @param q Search query
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @return Search results
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Map<String, Object> response = catalogService.searchProducts(q, page, size);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get products by category
     * @param categoryId Category ID
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @return Products in the specified category
     */
    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Map<String, Object> response = catalogService.getProductsByCategory(categoryId, page, size);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get product inventory information
     * @param productId Product ID
     * @return Product inventory details
     */
    @GetMapping("/products/{productId}/inventory")
    public ResponseEntity<Map<String, Object>> getProductInventory(@PathVariable String productId) {
        Map<String, Object> response = catalogService.getProductInventory(productId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Create a new product (Admin only)
     * @param productData Product data
     * @return Response with creation status
     */
    @PostMapping("/products")
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Map<String, Object> productData) {
        Map<String, Object> response = catalogService.createProduct(productData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Update product (Admin only)
     * @param productId Product ID
     * @param productData Updated product data
     * @return Response with update status
     */
    @PutMapping("/products/{productId}")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable String productId,
            @RequestBody Map<String, Object> productData) {
        
        Map<String, Object> response = catalogService.updateProduct(productId, productData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Delete product (Admin only)
     * @param productId Product ID
     * @return Response with deletion status
     */
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable String productId) {
        Map<String, Object> response = catalogService.deleteProduct(productId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Update product inventory
     * @param productId Product ID
     * @param quantity Quantity to add/subtract
     * @return Response with update status
     */
    @PostMapping("/products/{productId}/inventory")
    public ResponseEntity<Map<String, Object>> updateInventory(
            @PathVariable String productId,
            @RequestBody Map<String, Integer> inventoryData) {
        
        Integer quantity = inventoryData.get("quantity");
        if (quantity == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Quantity is required");
            return ResponseEntity.badRequest().body(response);
        }
        
        Map<String, Object> response = catalogService.updateInventory(productId, quantity);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get products by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @return Products within price range
     */
    @GetMapping("/products/price-range")
    public ResponseEntity<Map<String, Object>> getProductsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        // Cast to ProductServiceImpl to access the additional methods
        if (catalogService instanceof com.backend.ecommerce.service.impl.ProductServiceImpl) {
            com.backend.ecommerce.service.impl.ProductServiceImpl productServiceImpl = 
                (com.backend.ecommerce.service.impl.ProductServiceImpl) catalogService;
            
            Map<String, Object> response = productServiceImpl.getProductsByPriceRange(minPrice, maxPrice, page, size);
            
            if ((Boolean) response.get("success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Price range filtering not available");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Get products with low stock (Admin only)
     * @param threshold Stock threshold (default: 10)
     * @return Products with low stock
     */
    @GetMapping("/products/low-stock")
    public ResponseEntity<Map<String, Object>> getProductsWithLowStock(
            @RequestParam(defaultValue = "10") int threshold) {
        
        // Cast to ProductServiceImpl to access the additional methods
        if (catalogService instanceof com.backend.ecommerce.service.impl.ProductServiceImpl) {
            com.backend.ecommerce.service.impl.ProductServiceImpl productServiceImpl = 
                (com.backend.ecommerce.service.impl.ProductServiceImpl) catalogService;
            
            Map<String, Object> response = productServiceImpl.getProductsWithLowStock(threshold);
            
            if ((Boolean) response.get("success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Low stock filtering not available");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Get recently added products
     * @param days Number of days to look back (default: 7)
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @return Recently added products
     */
    @GetMapping("/products/recent")
    public ResponseEntity<Map<String, Object>> getRecentlyAddedProducts(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        // Cast to ProductServiceImpl to access the additional methods
        if (catalogService instanceof com.backend.ecommerce.service.impl.ProductServiceImpl) {
            com.backend.ecommerce.service.impl.ProductServiceImpl productServiceImpl = 
                (com.backend.ecommerce.service.impl.ProductServiceImpl) catalogService;
            
            Map<String, Object> response = productServiceImpl.getRecentlyAddedProducts(days, page, size);
            
            if ((Boolean) response.get("success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Recent products filtering not available");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Get featured products (products with high ratings or popularity)
     * @param limit Number of products to return (default: 10)
     * @return Featured products
     */
    @GetMapping("/products/featured")
    public ResponseEntity<Map<String, Object>> getFeaturedProducts(
            @RequestParam(defaultValue = "10") int limit) {
        
        // TODO: Implement featured products logic
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Featured products endpoint - implementation pending");
        response.put("products", new Object[0]);
        response.put("limit", limit);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get product recommendations based on category or user preferences
     * @param productId Product ID for recommendations
     * @param limit Number of recommendations (default: 5)
     * @return Product recommendations
     */
    @GetMapping("/products/{productId}/recommendations")
    public ResponseEntity<Map<String, Object>> getProductRecommendations(
            @PathVariable String productId,
            @RequestParam(defaultValue = "5") int limit) {
        
        // TODO: Implement recommendation logic
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Product recommendations endpoint - implementation pending");
        response.put("productId", productId);
        response.put("recommendations", new Object[0]);
        response.put("limit", limit);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get catalog statistics (Admin only)
     * @return Catalog statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCatalogStats() {
        // TODO: Implement catalog statistics
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("stats", Map.of(
            "totalProducts", 0,
            "activeProducts", 0,
            "totalCategories", 0,
            "productsWithLowStock", 0,
            "averagePrice", 0.0
        ));
        
        return ResponseEntity.ok(response);
    }
}
