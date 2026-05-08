markdown# Periplus Shopping Cart Automation Test

An automated test suite for the Periplus online bookstore shopping cart functionality, built with Java, Selenium WebDriver, and TestNG following the Page Object Model (POM) design pattern.

## Tech Stack

- Java 17
- Selenium WebDriver 4.18.1
- TestNG 7.9.0
- Maven
- WebDriverManager 5.7.0

## Project Structure

```
src/test/java/com/test/
├── base/
│   └── BaseTest.java          # WebDriver setup and teardown
├── pages/
│   ├── LoginPage.java         # Login page elements and actions
│   └── CartPage.java          # Cart page elements and actions
├── tests/
│   └── CartTest.java          # Test cases
└── utils/
    └── ConfigReader.java      # Test data and constants
```

## Prerequisites

- Java 17 (Eclipse Temurin recommended)
- IntelliJ IDEA
- Google Chrome (latest)
- Maven (via WSL or system install)

## How to Run

1. Clone the repository
2. Open the project in IntelliJ IDEA
3. Wait for Maven to download dependencies
4. Navigate to `src/test/java/com/test/tests/CartTest.java`
5. Right click the class and select `Run 'CartTest'`

## Test Cases

| # | Test | Description |
|---|------|-------------|
| 1 | testLogin | Logs in and clears cart to ensure a clean state |
| 2 | testAddFirstItemToCart | Adds "In Ghostly Japan" to cart and verifies it appears |
| 3 | testAddSecondItemToCart | Adds "Harry Potter: Hedwig Plush Journal" and verifies cart has 2 items |
| 4 | testCartPersistsAfterRefresh | Refreshes the cart page and verifies items are still present |
| 5 | testCartIconCountUpdates | Verifies the cart icon in the header reflects the correct item count |
| 6 | testSubtotalUpdatesAfterQuantityChange | Increases quantity and verifies subtotal changes accordingly |
| 7 | testProceedToCheckout | Clicks the Checkout button and verifies navigation to checkout page |
| 8 | testIncreaseQuantity | Increases item quantity and verifies count and price update |
| 9 | testDecreaseQuantity | Decreases item quantity and verifies count and price update |
| 10 | testDecreaseQuantityAtMinimum | Verifies quantity cannot go below 1 |
| 11 | testRemoveOneItemFromCart | Removes one item and verifies cart item count decreases |
| 12 | testRemoveLastItemShowsEmptyCart | Removes last item and verifies empty cart message appears |
| 13 | testAddOutOfStockItem | Attempts to add an out of stock item and verifies the appropriate error message |

## Notes

- Test data is hardcoded in `ConfigReader.java`. If a product goes out of stock, update the relevant URL or search query in that file.
- Tests are sequential and depend on each other. Running individual tests out of order may cause failures.
- The test account credentials are stored in `ConfigReader.java`. Replace with your own Periplus account if needed.
- The out of stock test uses a specific product URL. If that product becomes available, update `OOS_PRODUCT_URL` in `ConfigReader.java`.