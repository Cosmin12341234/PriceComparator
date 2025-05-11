PRICE COMPARATOR
================

A sophisticated price comparison application that helps users track and optimize their shopping expenses across multiple stores.

PROJECT OVERVIEW
---------------

This project is a price comparison and shopping optimization tool that helps users make informed purchasing decisions by tracking prices across different stores and providing various features for cost optimization.

KEY FEATURES
-----------

1. Daily Shopping Basket Monitoring
   - Split shopping lists for optimal cost savings
   - Compare prices across multiple stores

2. Discount Tracking
   - View best current discounts across all tracked stores
   - Track newly added discounts (within last 24 hours)

3. Price History and Analysis
   - Dynamic price history graphs
   - Filterable by store, product category, or brand
   - Track price trends over time

4. Smart Shopping Features
   - Product substitutes and recommendations
   - Value per unit comparison (price per kg/liter)
   - Custom price alerts for target prices

USAGE
-----

API ENDPOINTS:

Data Import:
- POST /api/import/auto - Upload CSV files for products and discounts

Products:
- GET /api/products - Get all products
- GET /api/products/{productId}/substitutes - Get product substitutes and recommendations
- GET /api/price-history/product/{productId} - Get price history for a specific product

Shopping Baskets:
- POST /api/shopping-baskets - Create a new shopping basket
  Example body:
  {
    "name": "Cosmin's Cart",
    "productIds": ["P001", "P002", "P003", "P023", "P033", "P040", "P044"]
  }
- POST /api/shopping-baskets/{basketId}/products/{productId} - Add product to basket
- DELETE /api/shopping-baskets/{basketId}/products/{productId} - Remove product from basket
- GET /api/shopping-baskets/{basketId}/optimize - Get cost-optimized shopping suggestions

Discounts:
- GET /api/discounts/active - Get all active discounts
- GET /api/discounts/best - Get best current discounts
- GET /api/discounts/new - Get newly added discounts

Price Alerts:
- POST /api/price-alerts - Create a price alert
  Example body:
  {
    "productId": "P037",
    "targetPrice": "12"
  }

EXAMPLE REQUESTS
--------------

1. Creating a Shopping Basket:
   POST http://localhost:8080/api/shopping-baskets
   Content-Type: application/json
   
   {
       "name": "Cosmin's Cart",
       "productIds": ["P001", "P002", "P003", "P023", "P033", "P040", "P044"]
   }

2. Setting up a Price Alert:
   POST http://localhost:8080/api/price-alerts
   Content-Type: application/json
   
   {
       "productId": "P037",
       "targetPrice": "12"
   }

3. Getting Basket Optimization:
   GET http://localhost:8080/api/shopping-baskets/3/optimize

4. Viewing Product Price History:
   GET http://localhost:8080/api/price-history/product/P001