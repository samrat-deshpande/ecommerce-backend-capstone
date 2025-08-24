package com.backend.ecommerce.service.impl;

import com.backend.ecommerce.entity.Category;
import com.backend.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service implementation for category management operations
 * Provides business logic for category CRUD operations and queries
 */
@Service
public class CategoryServiceImpl {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Create a new category
     * @param categoryData Category data
     * @return Response with creation status
     */
    public Map<String, Object> createCategory(Map<String, String> categoryData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate required fields
            if (categoryData.get("name") == null || categoryData.get("name").trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Category name is required");
                return response;
            }
            
            // Check if category already exists
            if (categoryRepository.existsByName(categoryData.get("name").trim())) {
                response.put("success", false);
                response.put("message", "Category with this name already exists");
                return response;
            }
            
            // Create new category
            Category category = new Category();
            category.setName(categoryData.get("name").trim());
            category.setDescription(categoryData.get("description") != null ? 
                categoryData.get("description").trim() : "");
            category.setParentId(categoryData.get("parentId"));
            category.setActive(true);
            category.setProductCount(0);
            
            Category savedCategory = categoryRepository.save(category);
            
            response.put("success", true);
            response.put("message", "Category created successfully");
            response.put("categoryId", savedCategory.getId());
            response.put("categoryName", savedCategory.getName());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to create category: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * Update category
     * @param categoryId Category ID
     * @param categoryData Updated category data
     * @return Response with update status
     */
    public Map<String, Object> updateCategory(String categoryId, Map<String, String> categoryData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Category not found");
                return response;
            }
            
            Category category = categoryOpt.get();
            
            // Update fields if provided
            if (categoryData.get("name") != null && !categoryData.get("name").trim().isEmpty()) {
                // Check if new name conflicts with existing category
                if (!category.getName().equals(categoryData.get("name").trim()) && 
                    categoryRepository.existsByName(categoryData.get("name").trim())) {
                    response.put("success", false);
                    response.put("message", "Category with this name already exists");
                    return response;
                }
                category.setName(categoryData.get("name").trim());
            }
            
            if (categoryData.get("description") != null) {
                category.setDescription(categoryData.get("description").trim());
            }
            
            if (categoryData.get("parentId") != null) {
                category.setParentId(categoryData.get("parentId"));
            }
            
            if (categoryData.get("active") != null) {
                category.setActive(Boolean.parseBoolean(categoryData.get("active")));
            }
            
            categoryRepository.save(category);
            
            response.put("success", true);
            response.put("message", "Category updated successfully");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update category: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * Delete category (soft delete)
     * @param categoryId Category ID
     * @return Response with deletion status
     */
    public Map<String, Object> deleteCategory(String categoryId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Category not found");
                return response;
            }
            
            Category category = categoryOpt.get();
            
            // Check if category has subcategories
            List<Category> subcategories = categoryRepository.findByParentId(categoryId);
            if (!subcategories.isEmpty()) {
                response.put("success", false);
                response.put("message", "Cannot delete category with subcategories");
                return response;
            }
            
            // Soft delete - mark as inactive
            category.setActive(false);
            categoryRepository.save(category);
            
            response.put("success", true);
            response.put("message", "Category deleted successfully");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete category: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * Get category by ID
     * @param categoryId Category ID
     * @return Category details
     */
    public Optional<Map<String, Object>> getCategoryById(String categoryId) {
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                return Optional.empty();
            }
            
            Category category = categoryOpt.get();
            Map<String, Object> categoryDetails = new HashMap<>();
            categoryDetails.put("id", category.getId());
            categoryDetails.put("name", category.getName());
            categoryDetails.put("description", category.getDescription());
            categoryDetails.put("parentId", category.getParentId());
            categoryDetails.put("productCount", category.getProductCount());
            categoryDetails.put("active", category.isActive());
            categoryDetails.put("createdAt", category.getCreatedAt());
            categoryDetails.put("updatedAt", category.getUpdatedAt());
            
            return Optional.of(categoryDetails);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Get all active categories
     * @return List of active categories
     */
    public List<Category> getActiveCategories() {
        return categoryRepository.findByActive(true);
    }

    /**
     * Get root categories (categories without parent)
     * @return List of root categories
     */
    public List<Category> getRootCategories() {
        return categoryRepository.findByParentIdIsNull();
    }

    /**
     * Get subcategories of a parent category
     * @param parentId Parent category ID
     * @return List of subcategories
     */
    public List<Category> getSubcategories(String parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    /**
     * Search categories by name or description
     * @param searchTerm Search term
     * @return List of matching categories
     */
    public List<Category> searchCategories(String searchTerm) {
        return categoryRepository.searchCategories(searchTerm);
    }

    /**
     * Get categories with product count above threshold
     * @param minProductCount Minimum product count
     * @return List of categories with sufficient products
     */
    public List<Category> getCategoriesByProductCount(int minProductCount) {
        return categoryRepository.findByProductCountGreaterThanEqual(minProductCount);
    }

    /**
     * Get recently created categories
     * @param days Number of days to look back
     * @return List of recently created categories
     */
    public List<Category> getRecentlyCreatedCategories(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return categoryRepository.findByCreatedAtAfter(cutoffDate);
    }

    /**
     * Update product count for a category
     * @param categoryId Category ID
     * @param productCount New product count
     * @return Response with update status
     */
    public Map<String, Object> updateProductCount(String categoryId, int productCount) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Category not found");
                return response;
            }
            
            Category category = categoryOpt.get();
            category.setProductCount(productCount);
            categoryRepository.save(category);
            
            response.put("success", true);
            response.put("message", "Product count updated successfully");
            response.put("newProductCount", productCount);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update product count: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * Get category hierarchy (tree structure)
     * @return Category hierarchy
     */
    public Map<String, Object> getCategoryHierarchy() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Category> rootCategories = getRootCategories();
            Map<String, Object> hierarchy = new HashMap<>();
            
            for (Category root : rootCategories) {
                if (root.isActive()) {
                    Map<String, Object> rootNode = buildCategoryNode(root);
                    hierarchy.put(root.getId(), rootNode);
                }
            }
            
            response.put("success", true);
            response.put("hierarchy", hierarchy);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to build category hierarchy: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Build category node with subcategories
     * @param category Category to build node for
     * @return Category node with subcategories
     */
    private Map<String, Object> buildCategoryNode(Category category) {
        Map<String, Object> node = new HashMap<>();
        node.put("id", category.getId());
        node.put("name", category.getName());
        node.put("description", category.getDescription());
        node.put("productCount", category.getProductCount());
        node.put("active", category.isActive());
        
        // Get subcategories
        List<Category> subcategories = categoryRepository.findByParentId(category.getId());
        if (!subcategories.isEmpty()) {
            Map<String, Object> children = new HashMap<>();
            for (Category sub : subcategories) {
                if (sub.isActive()) {
                    children.put(sub.getId(), buildCategoryNode(sub));
                }
            }
            node.put("children", children);
        }
        
        return node;
    }
}
