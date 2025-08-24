package com.backend.ecommerce.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Category entity representing a product category in the ecommerce system
 * Supports hierarchical category structure with parent-child relationships
 */
@Entity
@Table(name = "categories")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "parent_id")
    private String parentId;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "icon_class")
    private String iconClass;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(name = "product_count")
    private Integer productCount = 0;
    
    @Column(name = "active")
    private boolean active = true;
    
    @Column(name = "featured")
    private boolean featured = false;
    
    @Column(name = "meta_title")
    private String metaTitle;
    
    @Column(name = "meta_description")
    private String metaDescription;
    
    @Column(name = "meta_keywords")
    private String metaKeywords;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Default constructor
    public Category() {}
    
    // Constructor with required fields
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Constructor with parent category
    public Category(String name, String description, String parentId) {
        this.name = name;
        this.description = description;
        this.parentId = parentId;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Integer getProductCount() {
        return productCount;
    }
    
    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean isFeatured() {
        return featured;
    }
    
    public void setFeatured(boolean featured) {
        this.featured = featured;
    }
    
    public String getMetaTitle() {
        return metaTitle;
    }
    
    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }
    
    public String getMetaDescription() {
        return metaDescription;
    }
    
    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }
    
    public String getMetaKeywords() {
        return metaKeywords;
    }
    
    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Check if this category is a root category (no parent)
     * @return true if no parent category, false otherwise
     */
    public boolean isRootCategory() {
        return parentId == null || parentId.isEmpty();
    }
    
    /**
     * Check if this category is a subcategory (has parent)
     * @return true if has parent category, false otherwise
     */
    public boolean isSubCategory() {
        return !isRootCategory();
    }
    
    /**
     * Check if category has products
     * @return true if product count is greater than 0
     */
    public boolean hasProducts() {
        return productCount > 0;
    }
    
    /**
     * Increment product count
     */
    public void incrementProductCount() {
        this.productCount++;
    }
    
    /**
     * Decrement product count
     */
    public void decrementProductCount() {
        if (this.productCount > 0) {
            this.productCount--;
        }
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", productCount=" + productCount +
                ", active=" + active +
                '}';
    }
}
