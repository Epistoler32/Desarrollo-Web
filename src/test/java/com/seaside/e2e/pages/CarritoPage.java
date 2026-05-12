package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object for the shopping cart page (/carrito).
 * Shows items that have been added during the order-creation flow
 * and persisted in localStorage via CarritoService.
 */
public class CarritoPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    private final By carritoSection = By.cssSelector("section.carrito-section");
    private final By itemNames = By.cssSelector("div.carrito-item h3.item-nombre");
    // Total is inside .carrito-resumen → .resumen-total → last <span>
    private final By resumenTotal = By.cssSelector("div.resumen-total span:last-child");
    private final By continuarBtn = By.cssSelector("button.btn-confirmar");
    private final By carritoVacio = By.cssSelector("div.carrito-vacio");

    public CarritoPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(carritoSection));
    }

    /** Returns true when the carrito has at least one item (not empty state). */
    public boolean hasItems() {
        return driver.findElements(carritoVacio).isEmpty();
    }

    /** Returns the displayed names of all items in the cart. */
    public List<String> getItemNames() {
        return driver.findElements(itemNames)
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Returns the total text as shown in the resumen panel, e.g. "$42,000".
     * The template renders: ${{ total | number:'1.0-0' }}
     */
    public String getTotalText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resumenTotal)).getText();
    }

    /**
     * Clicks "Continuar pedido →" to navigate back to /pedido/nuevo.
     * Uses JS click to avoid OS-focus dependency.
     */
    public void continuarPedido() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(continuarBtn));
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", btn);
    }
}
