package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the client login page (/login).
 * Uses pure JS for form filling to avoid OS-focus dependency.
 */
public class ClienteLoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    private final By correoInput = By.id("correo");
    private final By contrasenaInput = By.id("contrasena");
    private final By submitBtn = By.cssSelector("form.auth-form button[type='submit']");
    private final By successAlert = By.cssSelector(".auth-alert.auth-success");

    public ClienteLoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(correoInput));
    }

    private void setField(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        js.executeScript(
                "var el=arguments[0],v=arguments[1];" +
                        "el.value=v;" +
                        "el.dispatchEvent(new Event('input',{bubbles:true}));" +
                        "el.dispatchEvent(new Event('change',{bubbles:true}));",
                el, value);
    }

    public void loginAs(String correo, String contrasena) {
        setField(correoInput, correo);
        setField(contrasenaInput, contrasena);
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
        js.executeScript("arguments[0].click();", btn);
    }
}
