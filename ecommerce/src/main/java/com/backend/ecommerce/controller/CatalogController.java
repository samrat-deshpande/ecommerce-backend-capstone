package com.backend.ecommerce.controller;

import com.backend.ecommerce.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for managing product catalog operations
 * Provides endpoints for browsing, searching, and managing products
 */
@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "*")
@Tag(name = "Product Catalog", description = "APIs for browsing, searching, and managing products and categories")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    /**
     * Get products with pagination and filtering
     */
    @GetMapping("/products")
    @Operation(
        summary = "Get products with pagination and filtering",
        description = "Retrieves a paginated list of products with optional filtering by category, search term, price range, etc."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Products retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"products\": [...], \"totalPages\": 5, \"totalElements\": 100, \"currentPage\": 0}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> getProducts(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Category ID to filter by", example = "category-uuid")
            @RequestParam(required = false) String categoryId,
            @Parameter(description = "Search term for product name/description", example = "laptop")
            @RequestParam(required = false) String searchTerm,
            @Parameter(description = "Minimum price", example = "100.00")
            @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price", example = "1000.00")
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Map<String, Object> response = catalogService.getProducts(page, size, categoryId, searchTerm, minPrice != null ? minPrice.doubleValue() : null, maxPrice != null ? maxPrice.doubleValue() : null);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get product by ID
     */
    @GetMapping("/products/{productId}")
    @Operation(
        summary = "Get product by ID",
        description = "Retrieves detailed information about a specific product"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"product\": {\"id\": \"uuid\", \"name\": \"Product Name\", \"price\": 99.99}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found"
        )
    })
    public ResponseEntity<Map<String, Object>> getProductById(
            @Parameter(description = "Product ID", example = "product-uuid")
            @PathVariable String productId) {
        
        Optional<Map<String, Object>> productOptional = catalogService.getProductById(productId);
        
        if (productOptional.isPresent()) {
            Map<String, Object> response = productOptional.get();
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all categories
     */
    @GetMapping("/categories")
    @Operation(
        summary = "Get all categories",
        description = "Retrieves a list of all available product categories"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categories retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"categories\": [{\"id\": \"uuid\", \"name\": \"Electronics\"}]}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> getCategories() {
        List<Map<String, Object>> categories = catalogService.getCategories();
        Map<String, Object> response = Map.of("success", true, "categories", categories);
        return ResponseEntity.ok(response);
    }

    /**
     * Search products
     */
    @GetMapping("/search")
    @Operation(
        summary = "Search products",
        description = "Searches for products using keywords in name, description, or specifications"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"products\": [...], \"totalResults\": 25}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> searchProducts(
            @Parameter(description = "Search query", example = "wireless headphones")
            @RequestParam String query,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        
        Map<String, Object> response = catalogService.searchProducts(query, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get products by category
     */
    @GetMapping("/categories/{categoryId}/products")
    @Operation(
        summary = "Get products by category",
        description = "Retrieves all products belonging to a specific category"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Products retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"products\": [...], \"category\": {\"id\": \"uuid\", \"name\": \"Electronics\"}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Category not found"
        )
    })
    public ResponseEntity<Map<String, Object>> getProductsByCategory(
            @Parameter(description = "Category ID", example = "category-uuid")
            @PathVariable String categoryId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        
        Map<String, Object> response = catalogService.getProductsByCategory(categoryId, page, size);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get product inventory
     */
    @GetMapping("/products/{productId}/inventory")
    @Operation(
        summary = "Get product inventory",
        description = "Retrieves current inventory information for a specific product"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Inventory information retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"productId\": \"uuid\", \"stockQuantity\": 50, \"reservedQuantity\": 5}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found"
        )
    })
    public ResponseEntity<Map<String, Object>> getProductInventory(
            @Parameter(description = "Product ID", example = "product-uuid")
            @PathVariable String productId) {
        
        Map<String, Object> response = catalogService.getProductInventory(productId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create new product
     */
    @PostMapping("/products")
    @Operation(
        summary = "Create new product",
        description = "Creates a new product in the catalog (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Product created successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Product created successfully\", \"productId\": \"uuid\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": false, \"error\": \"Invalid input data\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Map<String, Object> productData) {
        Map<String, Object> response = catalogService.createProduct(productData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Update product
     */
    @PutMapping("/products/{productId}")
    @Operation(
        summary = "Update product",
        description = "Updates an existing product's information (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product updated successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Product updated successfully\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found"
        )
    })
    public ResponseEntity<Map<String, Object>> updateProduct(
            @Parameter(description = "Product ID", example = "product-uuid")
            @PathVariable String productId,
            @RequestBody Map<String, Object> productData) {
        
        Map<String, Object> response = catalogService.updateProduct(productId, productData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete product
     */
    @DeleteMapping("/products/{productId}")
    @Operation(
        summary = "Delete product",
        description = "Deletes a product from the catalog (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product deleted successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Product deleted successfully\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found"
        )
    })
    public ResponseEntity<Map<String, Object>> deleteProduct(
            @Parameter(description = "Product ID", example = "product-uuid")
            @PathVariable String productId) {
        
        Map<String, Object> response = catalogService.deleteProduct(productId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update product inventory
     */
    @PutMapping("/products/{productId}/inventory")
    @Operation(
        summary = "Update product inventory",
        description = "Updates the stock quantity for a specific product (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Inventory updated successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Inventory updated successfully\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found"
        )
    })
    public ResponseEntity<Map<String, Object>> updateInventory(
            @Parameter(description = "Product ID", example = "product-uuid")
            @PathVariable String productId,
            @Parameter(description = "New stock quantity", example = "100")
            @RequestParam int quantity) {
        
        Map<String, Object> response = catalogService.updateInventory(productId, quantity);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }










}
