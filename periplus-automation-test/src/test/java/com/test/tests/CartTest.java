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

    @Test(priority = 1)
    public void testLogin() {
        loginPage = new LoginPage(driver, wait);
        loginPage.navigateTo(BASE_URL);
        loginPage.enterEmail(EMAIL);
        loginPage.enterPassword(PASSWORD);
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login failed");
        System.out.println("Login successful");
    }

    @Test(priority = 2, dependsOnMethods = "testLogin")
    public void testSearchProduct() {
        cartPage = new CartPage(driver, wait);
        cartPage.searchProduct(BASE_URL, SEARCH_QUERY);
        System.out.println("Search successful");
    }

    @Test(priority = 3, dependsOnMethods = "testSearchProduct")
    public void testClickProduct() {
        cartPage.clickFirstProduct();
        System.out.println("Click product successful");
    }

    @Test(priority = 4, dependsOnMethods = "testClickProduct")
    public void testAddToCart() {
        cartPage.addToCart();
        System.out.println("Add to cart successful");
    }

    @Test(priority = 5, dependsOnMethods = "testAddToCart")
    public void testCloseModal() {
        cartPage.closeModal();
        System.out.println("Close modal successful");
    }

    @Test(priority = 6, dependsOnMethods = "testCloseModal")
    public void testVerifyCart() {
        boolean result = cartPage.isProductInCart("Ghostly Japan");
        Assert.assertTrue(result, "Product not found in cart");
        System.out.println("Cart verification successful");
    }
}