package com.test.tests;

import com.test.base.BaseTest;
import com.test.pages.CartPage;
import com.test.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.test.utils.ConfigReader.*;

public class CartTest extends BaseTest {

    private LoginPage loginPage;
    private CartPage cartPage;

    // ==================== SETUP ====================

    @Test(priority = 1)
    public void testLogin() throws InterruptedException {
        loginPage = new LoginPage(driver, wait);
        cartPage = new CartPage(driver, wait);
        loginPage.navigateTo(BASE_URL);
        loginPage.enterEmail(EMAIL);
        loginPage.enterPassword(PASSWORD);
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login failed");
        cartPage.clearCart(BASE_URL);
        System.out.println("Login successful and cart cleared");
    }

    // ==================== CREATE ====================

    @Test(priority = 2, dependsOnMethods = "testLogin")
    public void testAddFirstItemToCart() {
        cartPage.searchProduct(BASE_URL, SEARCH_QUERY);
        cartPage.clickFirstProduct();
        cartPage.addToCart();
        cartPage.closeModal();
        cartPage.goToCart(BASE_URL);
        Assert.assertTrue(cartPage.isProductInCart("Ghostly Japan"), "First item not found in cart");
        System.out.println("First item added to cart successfully");
    }

    @Test(priority = 3, dependsOnMethods = "testAddFirstItemToCart")
    public void testAddSecondItemToCart() {
        cartPage.searchProduct(BASE_URL, SEARCH_QUERY_2);
        cartPage.clickFirstProduct();
        cartPage.addToCart();
        cartPage.closeModal();
        cartPage.goToCart(BASE_URL);
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items");
        System.out.println("Second item added to cart successfully");
    }

    // ==================== EDGE CASES ====================

    @Test(priority = 4, dependsOnMethods = "testAddSecondItemToCart")
    public void testCartPersistsAfterRefresh() {
        cartPage.goToCart(BASE_URL);
        cartPage.refreshPage();
        Assert.assertTrue(cartPage.isProductInCart("Ghostly Japan"), "Cart should persist after refresh");
        System.out.println("Cart persists after refresh");
    }

    @Test(priority = 5, dependsOnMethods = "testAddSecondItemToCart")
    public void testCartIconCountUpdates() {
        driver.get(BASE_URL);
        String count = cartPage.getCartIconCount();
        Assert.assertFalse(count.equals("0"), "Cart icon should show item count");
        System.out.println("Cart icon count verified: " + count);
    }

    @Test(priority = 6, dependsOnMethods = "testAddSecondItemToCart")
    public void testSubtotalUpdatesAfterQuantityChange() throws InterruptedException {
        cartPage.goToCart(BASE_URL);
        String totalBefore = cartPage.getSubTotal();
        cartPage.clickPlus();
        Thread.sleep(2000);
        cartPage.goToCart(BASE_URL);
        String totalAfter = cartPage.getSubTotal();
        Assert.assertNotEquals(totalBefore, totalAfter, "Subtotal should update after quantity change");
        System.out.println("Subtotal updated from " + totalBefore + " to " + totalAfter);
    }

    @Test(priority = 7, dependsOnMethods = "testAddSecondItemToCart")
    public void testProceedToCheckout() {
        cartPage.goToCart(BASE_URL);
        Assert.assertTrue(cartPage.proceedToCheckout(), "Should navigate to checkout page");
        System.out.println("Proceed to checkout works");
    }

    // ==================== UPDATE ====================

    @Test(priority = 8, dependsOnMethods = "testSubtotalUpdatesAfterQuantityChange")
    public void testIncreaseQuantity() throws InterruptedException {
        cartPage.goToCart(BASE_URL);
        String initialQty = cartPage.getQuantityOfFirstItem();
        String initialTotal = cartPage.getSubTotal();
        cartPage.clickPlus();
        Thread.sleep(2000);
        cartPage.goToCart(BASE_URL);
        String newQty = cartPage.getQuantityOfFirstItem();
        String newTotal = cartPage.getSubTotal();
        Assert.assertNotEquals(initialQty, newQty, "Quantity should have increased");
        Assert.assertNotEquals(initialTotal, newTotal, "Total should have increased");
        System.out.println("Quantity increased from " + initialQty + " to " + newQty);
    }

    @Test(priority = 9, dependsOnMethods = "testIncreaseQuantity")
    public void testDecreaseQuantity() throws InterruptedException {
        cartPage.goToCart(BASE_URL);
        String initialQty = cartPage.getQuantityOfFirstItem();
        cartPage.clickMinus();
        Thread.sleep(2000);
        cartPage.goToCart(BASE_URL);
        String newQty = cartPage.getQuantityOfFirstItem();
        Assert.assertNotEquals(initialQty, newQty, "Quantity should have decreased");
        System.out.println("Quantity decreased from " + initialQty + " to " + newQty);
    }

    @Test(priority = 10, dependsOnMethods = "testDecreaseQuantity")
    public void testDecreaseQuantityAtMinimum() throws InterruptedException {
        cartPage.goToCart(BASE_URL);
        String qtyBefore = cartPage.getQuantityOfFirstItem();
        if (!qtyBefore.equals("1")) {
            cartPage.clickMinus();
            Thread.sleep(1000);
            cartPage.goToCart(BASE_URL);
        }
        cartPage.clickMinus();
        Thread.sleep(1000);
        cartPage.goToCart(BASE_URL);
        String qtyAfter = cartPage.getQuantityOfFirstItem();
        Assert.assertEquals(qtyAfter, "1", "Quantity should not go below 1");
        System.out.println("Minimum quantity constraint verified: qty stays at " + qtyAfter);
    }

    // ==================== DELETE ====================

    @Test(priority = 11, dependsOnMethods = "testDecreaseQuantityAtMinimum")
    public void testRemoveOneItemFromCart() throws InterruptedException {
        cartPage.goToCart(BASE_URL);
        int itemsBefore = cartPage.getCartItemCount();
        cartPage.removeFirstItem();
        Thread.sleep(2000);
        cartPage.goToCart(BASE_URL);
        int itemsAfter = cartPage.getCartItemCount();
        Assert.assertEquals(itemsAfter, itemsBefore - 1, "Cart should have one less item");
        System.out.println("Item removed. Cart items: " + itemsBefore + " -> " + itemsAfter);
    }

    @Test(priority = 12, dependsOnMethods = "testRemoveOneItemFromCart")
    public void testRemoveLastItemShowsEmptyCart() throws InterruptedException {
        cartPage.goToCart(BASE_URL);
        cartPage.removeFirstItem();
        Thread.sleep(2000);
        cartPage.goToCart(BASE_URL);
        Assert.assertTrue(cartPage.isCartEmpty(), "Cart should show empty message");
        System.out.println("Empty cart message verified");
    }

    // ==================== OUT OF STOCK ====================

    @Test(priority = 13)
    public void testAddOutOfStockItem() throws InterruptedException {
        cartPage.navigateToOutOfStockProduct(OOS_PRODUCT_URL);
        cartPage.addToCart();
        Thread.sleep(2000);
        String modal = cartPage.getModalText();
        Assert.assertTrue(modal.contains(OOS_MODAL_TEXT), "Expected out of stock message not shown");
        cartPage.closeModal();
        System.out.println("Out of stock test passed");
    }
}