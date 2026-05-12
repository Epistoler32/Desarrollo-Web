package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object for the public product detail page (/productos/:id).
 * This is the page reachable from the /menu cards and used to verify
 * that adicionales are displayed correctly.
 */
public class ProductoDetallePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Selectors match producto-detalle-page.component.html
    private final By productoNombre = By.cssSelector("h3.detalle-nombre");
    private final By adicionalesSection = By.cssSelector("section.adicionales-section");
    private final By adicionalCards = By.cssSelector(".adicional-card .adicional-nombre");

    public ProductoDetallePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productoNombre));
    }

    public String getProductoNombre() {
        return driver.findElement(productoNombre).getText();
    }

    /** Returns the names of all adicionales shown in the detail page. */
    public List<String> getAdicionalNames() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(adicionalesSection));
        return driver.findElements(adicionalCards)
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    /** Returns how many adicional cards are displayed. */
    public int getAdicionalCount() {
        return driver.findElements(adicionalCards).size();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
