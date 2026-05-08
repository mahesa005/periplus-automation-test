package com.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private final By emailField = By.name("email");
    private final By passwordField = By.id("ps");
    private final By loginButton = By.id("button-login");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-signin-text")));
        WebElement signInLink = driver.findElement(By.cssSelector("#nav-signin-text a"));
        signInLink.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page source contains email field: " + driver.getPageSource().contains("name=\"email\""));
    }

    public void enterEmail(String email) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        field.clear();
        field.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        field.clear();
        field.sendKeys(password);
    }

    public void clickLogin() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.urlContains("Your-Account"));
            return true;
        } catch (Exception e) {
            System.out.println("Login failed. Current URL: " + driver.getCurrentUrl());
            return false;
        }
    }
}