package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the operator login page (/operador/login).
 * Redirects to /pedidos on successful authentication.
 */
public class OperadorLoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    private final By usuarioInput = By.id("usuario");
    private final By contrasenaInput = By.id("contrasena");
    private final By submitBtn = By.cssSelector("button.auth-btn");
    private final By errorAlert = By.cssSelector(".auth-alert.auth-error");

    public OperadorLoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usuarioInput));
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

    public void loginAs(String usuario, String contrasena) {
        setField(usuarioInput, usuario);
        setField(contrasenaInput, contrasena);
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
        js.executeScript("arguments[0].click();", btn);
    }
}
