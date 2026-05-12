package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the client login page (/login).
 * Also exposes the staff portal links added at the bottom of the card.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Login form
    private final By correoInput = By.id("correo");
    private final By contrasenaInput = By.id("contrasena");
    private final By submitBtn = By.cssSelector("button[type='submit']");
    private final By errorAlert = By.cssSelector(".auth-alert.auth-error");

    // Staff portal links (added in previous session)
    private final By adminLoginLink = By.id("link-admin-login");
    private final By operadorLoginLink = By.id("link-operador-login");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
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

    public String getErrorText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorAlert)).getText();
    }

    /** Clicks "Acceso Administrador" portal link. */
    public void clickAdminLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(adminLoginLink)).click();
    }

    /** Clicks "Acceso Operador" portal link. */
    public void clickOperadorLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(operadorLoginLink)).click();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
