package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Page Object for the Admin login page (/admin/login). */
public class AdminLoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Selectors match admin-login.component.html
    private final By correoInput = By.id("correo");
    private final By contrasenaInput = By.id("contrasena");
    private final By submitBtn = By.cssSelector("button[type='submit']");
    private final By errorAlert = By.cssSelector(".auth-alert.auth-error");

    public AdminLoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(correoInput));
    }

    public void enterCorreo(String correo) {
        WebElement el = driver.findElement(correoInput);
        el.clear();
        el.sendKeys(correo);
    }

    public void enterContrasena(String contrasena) {
        WebElement el = driver.findElement(contrasenaInput);
        el.clear();
        el.sendKeys(contrasena);
    }

    public void submitLogin() {
        driver.findElement(submitBtn).click();
    }

    /** Fill and submit in one call. */
    public void loginAs(String correo, String contrasena) {
        waitForLoad();
        enterCorreo(correo);
        enterContrasena(contrasena);
        submitLogin();
    }

    public String getErrorText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorAlert)).getText();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
