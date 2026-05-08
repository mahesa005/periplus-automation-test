package com.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private final By searchBar = By.id("filter_name_desktop");
    private final By searchResults = By.className("single-product");
    private final By productLink = By.cssSelector("div.product-img a");
    private final By addToCartButton = By.cssSelector("button.btn-add-to-cart");
    private final By closeModalButton = By.cssSelector("button.btn-modal-close");
    private final By cartIcon = By.id("show-your-cart");
    private final By cartItems = By.cssSelector(".shopping-list li");

    public CartPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void searchProduct(String baseUrl, String query) {
        driver.get(baseUrl);
        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBar));
        search.clear();
        search.sendKeys(query);
        search.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.presenceOfElementLocated(searchResults));
        System.out.println("Search results loaded for: " + query);
    }

    public void clickFirstProduct() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
        WebElement product = wait.until(ExpectedConditions.elementToBeClickable(productLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", product);
        System.out.println("Product clicked. Current URL: " + driver.getCurrentUrl());
    }

    public void addToCart() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        System.out.println("Add to cart clicked");
    }

    public void closeModal() {
        WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(closeModalButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeBtn);
        System.out.println("Modal closed");
    }

    public boolean isProductInCart(String productName) {
        driver.get("https://www.periplus.com/checkout/cart");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        String pageSource = driver.getPageSource();
        System.out.println("Product in cart check: " + pageSource.contains(productName));
        return pageSource.contains(productName);
    }
}