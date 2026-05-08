package com.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private final By searchBar = By.id("filter_name_desktop");
    private final By searchResults = By.className("single-product");
    private final By productLink = By.cssSelector("div.product-img a");
    private final By addToCartButton = By.cssSelector("button.btn-add-to-cart");
    private final By closeModalButton = By.cssSelector("button.btn-modal-close");
    private final By cartRows = By.cssSelector("div.row-cart-product");
    private final By emptyCartMessage = By.cssSelector("div.content");
    private final By subTotal = By.id("sub_total");
    private final By plusButton = By.cssSelector("button[data-type='plus']");
    private final By minusButton = By.cssSelector("button[data-type='minus']");
    private final By quantityInput = By.cssSelector("input.input-number");
    private final By removeButton = By.cssSelector("a.btn-cart-remove");
    private final By availabilityDropdown = By.id("availability");
    private final By filterButton = By.cssSelector("button.btn-filter");
    private final By outOfStockProduct = By.cssSelector("div.currently-unavailable");
    private final By modalText = By.cssSelector("div.modal-text");
    private final By cartCount = By.id("cart_total");
    private final By proceedToCheckout = By.cssSelector("a.btn.d-flex");


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

    public void goToCart(String baseUrl) {
        driver.get(baseUrl + "/checkout/cart");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public boolean isProductInCart(String productName) {
        String pageSource = driver.getPageSource();
        System.out.println("Product in cart check for: " + productName + " = " + pageSource.contains(productName));
        return pageSource.contains(productName);
    }

    public int getCartItemCount() {
        List<WebElement> rows = driver.findElements(cartRows);
        System.out.println("Cart item count: " + rows.size());
        return rows.size();
    }

    public String getQuantityOfFirstItem() {
        WebElement qty = wait.until(ExpectedConditions.visibilityOfElementLocated(quantityInput));
        return qty.getAttribute("value");
    }

    public String getSubTotal() {
        WebElement total = wait.until(ExpectedConditions.visibilityOfElementLocated(subTotal));
        return total.getText();
    }

    public void clickPlus() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(plusButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        System.out.println("Plus clicked");
    }

    public void clickMinus() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(minusButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        System.out.println("Minus clicked");
    }

    public void removeFirstItem() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(removeButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        System.out.println("Remove clicked");
    }

    public boolean isCartEmpty() {
        WebElement content = wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartMessage));
        boolean empty = content.getText().contains("Your shopping cart is empty");
        System.out.println("Cart empty: " + empty);
        return empty;
    }

    public void navigateToOutOfStockProduct(String productUrl) {
        driver.get(productUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(addToCartButton));
        System.out.println("Navigated to out of stock product page");
    }

    public String getModalText() {
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(modalText));
        String text = modal.getText();
        System.out.println("Modal text: " + text);
        return text;
    }

    public void clearCart(String baseUrl) throws InterruptedException {
        driver.get(baseUrl + "/checkout/cart");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        while (!isCartEmpty()) {
            removeFirstItem();
            Thread.sleep(2000);
            driver.get(baseUrl + "/checkout/cart");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        }
        System.out.println("Cart cleared");
    }

    public void refreshPage() {
        driver.navigate().refresh();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        System.out.println("Page refreshed");
    }

    public String getCartIconCount() {
        WebElement count = wait.until(ExpectedConditions.visibilityOfElementLocated(cartCount));
        String text = count.getText();
        System.out.println("Cart icon count: " + text);
        return text;
    }

    public boolean proceedToCheckout() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckout));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        wait.until(ExpectedConditions.urlContains("checkout"));
        System.out.println("Proceeded to checkout. URL: " + driver.getCurrentUrl());
        return driver.getCurrentUrl().contains("checkout");
    }
}