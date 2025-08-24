# Product Catalog API Documentation

This document describes the Product Catalog API endpoints for the Ecommerce Backend application.

## Base URL
```
http://localhost:8080/api/catalog
```

## Authentication
Most catalog browsing endpoints are public. The following endpoints require authentication (Admin only):
- `POST /products` - Create product
- `PUT /products/{productId}` - Update product
- `DELETE /products/{productId}` - Delete product
- `GET /products/low-stock` - Get low stock products
- `GET /stats` - Get catalog statistics

## Endpoints

### 1. Product Browsing

#### 1.1 Get All Products
**GET** `/products`

Retrieves all products with pagination and sorting.

**Query Parameters:**
- `page` (optional): Page number, default: 0
- `size` (optional): Page size, default: 20
- `sortBy` (optional): Sort field, default: "name"
- `sortDir` (optional): Sort direction ("asc" or "desc"), default: "asc"

**Response (Success - 200):**
```json
{
    "success": true,
    "products": [
        {
            "id": "prod-001",
            "name": "Smartphone X",
            "description": "Latest smartphone with advanced features",
            "price": 699.99,
            "category": {
                "id": "cat-001",
                "name": "Electronics"
            },
            "stockQuantity": 50,
            "images": ["image1.jpg", "image2.jpg"],
            "specifications": {
                "brand": "TechCorp",
                "model": "X-1000",
                "storage": "128GB"
            },
            "active": true,
            "createdAt": "2025-08-22T21:00:00Z",
            "updatedAt": "2025-08-22T21:00:00Z"
        }
    ],
    "currentPage": 0,
    "totalPages": 5,
    "totalElements": 100,
    "size": 20,
    "hasNext": true,
    "hasPrevious": false
}
```

#### 1.2 Get Products by Category
**GET** `/categories/{categoryId}/products`

Retrieves products within a specific category.

**Path Parameters:**
- `categoryId`: Category ID

**Query Parameters:**
- `page` (optional): Page number, default: 0
- `size` (optional): Page size, default: 20

**Response (Success - 200):**
```json
{
    "success": true,
    "products": [...],
    "categoryId": "cat-001",
    "count": 25
}
```

#### 1.3 Get Products by Price Range
**GET** `/products/price-range`

Retrieves products within a specific price range.

**Query Parameters:**
- `minPrice` (required): Minimum price
- `maxPrice` (required): Maximum price
- `page` (optional): Page number, default: 0
- `size` (optional): Page size, default: 20

**Response (Success - 200):**
```json
{
    "success": true,
    "products": [...],
    "minPrice": 100.0,
    "maxPrice": 500.0,
    "count": 15
}
```

#### 1.4 Get Recently Added Products
**GET** `/products/recent`

Retrieves recently added products.

**Query Parameters:**
- `days` (optional): Number of days to look back, default: 7
- `page` (optional): Page number, default: 0
- `size` (optional): Page size, default: 20

**Response (Success - 200):**
```json
{
    "success": true,
    "products": [...],
    "days": 7,
    "currentPage": 0,
    "totalPages": 2,
    "totalElements": 35
}
```

#### 1.5 Get Featured Products
**GET** `/products/featured`

Retrieves featured products (implementation pending).

**Query Parameters:**
- `limit` (optional): Number of products to return, default: 10

**Response (Success - 200):**
```json
{
    "success": true,
    "message": "Featured products endpoint - implementation pending",
    "products": [],
    "limit": 10
}
```

### 2. Product Details

#### 2.1 Get Product by ID
**GET** `/products/{productId}`

Retrieves detailed information about a specific product.

**Path Parameters:**
- `productId`: Product ID

**Response (Success - 200):**
```json
{
    "success": true,
    "product": {
        "id": "prod-001",
        "name": "Smartphone X",
        "description": "Latest smartphone with advanced features",
        "price": 699.99,
        "category": {
            "id": "cat-001",
            "name": "Electronics"
        },
        "stockQuantity": 50,
        "images": ["image1.jpg", "image2.jpg"],
        "specifications": {
            "brand": "TechCorp",
            "model": "X-1000",
            "storage": "128GB",
            "ram": "8GB",
            "screen": "6.1 inch OLED"
        },
        "active": true,
        "createdAt": "2025-08-22T21:00:00Z",
        "updatedAt": "2025-08-22T21:00:00Z"
    }
}
```

#### 2.2 Get Product Inventory
**GET** `/products/{productId}/inventory`

Retrieves inventory information for a specific product.

**Path Parameters:**
- `productId`: Product ID

**Response (Success - 200):**
```json
{
    "success": true,
    "inventory": {
        "productId": "prod-001",
        "productName": "Smartphone X",
        "stockQuantity": 50,
        "available": true,
        "lastUpdated": "2025-08-22T21:00:00Z"
    }
}
```

#### 2.3 Get Product Recommendations
**GET** `/products/{productId}/recommendations`

Retrieves product recommendations (implementation pending).

**Path Parameters:**
- `productId`: Product ID

**Query Parameters:**
- `limit` (optional): Number of recommendations, default: 5

**Response (Success - 200):**
```json
{
    "success": true,
    "message": "Product recommendations endpoint - implementation pending",
    "productId": "prod-001",
    "recommendations": [],
    "limit": 5
}
```

### 3. Product Search

#### 3.1 Search Products
**GET** `/search`

Searches for products using keywords.

**Query Parameters:**
- `q` (required): Search query
- `page` (optional): Page number, default: 0
- `size` (optional): Page size, default: 20

**Response (Success - 200):**
```json
{
    "success": true,
    "products": [...],
    "searchTerm": "smartphone",
    "count": 8
}
```

### 4. Category Management

#### 4.1 Get All Categories
**GET** `/categories`

Retrieves all active categories.

**Response (Success - 200):**
```json
{
    "success": true,
    "categories": [
        {
            "id": "cat-001",
            "name": "Electronics",
            "description": "Electronic devices and accessories",
            "parentId": null,
            "productCount": 150,
            "active": true,
            "createdAt": "2025-08-22T21:00:00Z",
            "updatedAt": "2025-08-22T21:00:00Z"
        }
    ],
    "count": 10
}
```

#### 4.2 Get Category Hierarchy
**GET** `/categories/hierarchy`

Retrieves category hierarchy in tree structure.

**Response (Success - 200):**
```json
{
    "success": true,
    "hierarchy": {
        "cat-001": {
            "id": "cat-001",
            "name": "Electronics",
            "description": "Electronic devices",
            "productCount": 150,
            "active": true,
            "children": {
                "cat-002": {
                    "id": "cat-002",
                    "name": "Smartphones",
                    "description": "Mobile phones",
                    "productCount": 50,
                    "active": true
                }
            }
        }
    }
}
```

### 5. Admin Operations

#### 5.1 Create Product
**POST** `/products`

Creates a new product (Admin only).

**Request Body:**
```json
{
    "name": "New Product",
    "description": "Product description",
    "price": 99.99,
    "categoryId": "cat-001",
    "stockQuantity": 100,
    "images": ["image1.jpg", "image2.jpg"],
    "specifications": {
        "brand": "Brand Name",
        "model": "Model Number"
    }
}
```

**Response (Success - 201):**
```json
{
    "success": true,
    "message": "Product created successfully",
    "productId": "generated-uuid"
}
```

#### 5.2 Update Product
**PUT** `/products/{productId}`

Updates an existing product (Admin only).

**Path Parameters:**
- `productId`: Product ID

**Request Body:**
```json
{
    "name": "Updated Product Name",
    "price": 89.99,
    "stockQuantity": 75
}
```

**Response (Success - 200):**
```json
{
    "success": true,
    "message": "Product updated successfully"
}
```

#### 5.3 Delete Product
**DELETE** `/products/{productId}`

Soft deletes a product (Admin only).

**Path Parameters:**
- `productId`: Product ID

**Response (Success - 200):**
```json
{
    "success": true,
    "message": "Product deleted successfully"
}
```

#### 5.4 Get Low Stock Products
**GET** `/products/low-stock`

Retrieves products with stock below threshold (Admin only).

**Query Parameters:**
- `threshold` (optional): Stock threshold, default: 10

**Response (Success - 200):**
```json
{
    "success": true,
    "products": [...],
    "threshold": 10,
    "count": 5
}
```

#### 5.5 Get Catalog Statistics
**GET** `/stats`

Retrieves catalog statistics (Admin only).

**Response (Success - 200):**
```json
{
    "success": true,
    "stats": {
        "totalProducts": 1000,
        "activeProducts": 950,
        "totalCategories": 25,
        "productsWithLowStock": 15,
        "averagePrice": 149.99
    }
}
```

## Error Handling

All endpoints return consistent error responses with the following structure:

```json
{
    "success": false,
    "message": "Error description"
}
```

## HTTP Status Codes

- **200 OK**: Request successful
- **201 Created**: Resource created successfully
- **400 Bad Request**: Invalid request data
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Access denied
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

## Features

### 2.1. Browsing ✅
- **Pagination**: All product listing endpoints support pagination
- **Sorting**: Products can be sorted by various fields (name, price, date)
- **Category Filtering**: Browse products by category
- **Price Range Filtering**: Filter products by price range
- **Recently Added**: View recently added products

### 2.2. Product Details ✅
- **Complete Information**: Full product details including images and specifications
- **Inventory Status**: Real-time stock information
- **Category Information**: Product categorization details
- **Timestamps**: Creation and update timestamps

### 2.3. Search ✅
- **Keyword Search**: Full-text search across product names and descriptions
- **Search Results**: Paginated search results with relevance
- **Flexible Queries**: Support for various search terms

## Additional Features

### Advanced Filtering
- Price range filtering
- Stock level filtering
- Category-based filtering
- Recently added products

### Category Management
- Hierarchical category structure
- Parent-child relationships
- Product count tracking
- Category search functionality

### Admin Operations
- Product CRUD operations
- Category management
- Inventory monitoring
- Catalog statistics

## Development Notes

- The application uses H2 in-memory database for development
- H2 console is available at `/h2-console`
- Featured products and recommendations are placeholders (TODO: implement)
- Advanced search with relevance scoring is planned (TODO: implement)
- Product reviews and ratings are planned (TODO: implement)

## Testing the API

You can test the API using tools like:
- Postman
- cURL
- Insomnia
- Any HTTP client

### Example cURL Commands

**Get all products:**
```bash
curl -X GET "http://localhost:8080/api/catalog/products?page=0&size=10&sortBy=name&sortDir=asc"
```

**Search products:**
```bash
curl -X GET "http://localhost:8080/api/catalog/search?q=smartphone&page=0&size=10"
```

**Get products by category:**
```bash
curl -X GET "http://localhost:8080/api/catalog/categories/cat-001/products?page=0&size=20"
```

**Get product details:**
```bash
curl -X GET "http://localhost:8080/api/catalog/products/prod-001"
```

**Create a product (Admin only):**
```bash
curl -X POST http://localhost:8080/api/catalog/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "name": "Test Product",
    "description": "Test description",
    "price": 99.99,
    "categoryId": "cat-001",
    "stockQuantity": 100
  }'
```
