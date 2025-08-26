package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Repository test for ProductRepository using H2 database
 */
@SpringBootTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindAll() {
        // Should find all products from DataLoader
        List<Product> products = productRepository.findAll();
        assertEquals(10, products.size());
        
        // Verify specific products exist
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("iPhone 15 Pro")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("MacBook Pro 14\"")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Men's Casual T-Shirt")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("The Great Gatsby")));
    }

    @Test
    void testFindById() {
        // Test finding product by ID - get first product from database
        List<Product> allProducts = productRepository.findAll();
        assertFalse(allProducts.isEmpty());
        
        Product firstProduct = allProducts.get(0);
        Optional<Product> product = productRepository.findById(firstProduct.getId());
        assertTrue(product.isPresent());
        assertEquals(firstProduct.getName(), product.get().getName());
        assertEquals(firstProduct.getPrice(), product.get().getPrice());
        assertEquals(firstProduct.getStockQuantity(), product.get().getStockQuantity());
    }

    @Test
    void testFindByCategory() {
        // Test finding products by category
        List<Product> electronicsProducts = productRepository.findByCategory("Electronics");
        assertEquals(6, electronicsProducts.size()); // 6 electronics products in DataLoader
        
        List<Product> clothingProducts = productRepository.findByCategory("Clothing");
        assertEquals(2, clothingProducts.size()); // 2 clothing products in DataLoader
        
        List<Product> bookProducts = productRepository.findByCategory("Books");
        assertEquals(1, bookProducts.size()); // 1 book product in DataLoader
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        // Test searching products by name
        List<Product> phoneProducts = productRepository.findByNameContainingIgnoreCase("iPhone");
        assertEquals(1, phoneProducts.size()); // iPhone 15 Pro
        assertTrue(phoneProducts.stream().anyMatch(p -> p.getName().contains("iPhone")));
        
        List<Product> galaxyProducts = productRepository.findByNameContainingIgnoreCase("Galaxy");
        assertEquals(1, galaxyProducts.size()); // Samsung Galaxy S24
        assertTrue(galaxyProducts.stream().anyMatch(p -> p.getName().contains("Galaxy")));
        
        List<Product> bookProducts = productRepository.findByNameContainingIgnoreCase("Gatsby");
        assertEquals(1, bookProducts.size());
        assertEquals("The Great Gatsby", bookProducts.get(0).getName());
    }

    @Test
    void testFindByPriceBetween() {
        // Test finding products by price range
        List<Product> expensiveProducts = productRepository.findByPriceBetween(
            new BigDecimal("500.00"), 
            new BigDecimal("1000.00")
        );
        assertEquals(2, expensiveProducts.size()); // iPhone 15 Pro ($999.99), Samsung Galaxy S24 ($899.99)
        
        List<Product> cheapProducts = productRepository.findByPriceBetween(
            new BigDecimal("0.00"), 
            new BigDecimal("50.00")
        );
        assertEquals(2, cheapProducts.size()); // Men's Casual T-Shirt ($24.99) and The Great Gatsby ($12.99)
    }
}
