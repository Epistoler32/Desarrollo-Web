package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for the order-creation wizard (/pedido/nuevo).
 *
 * The wizard has two visible steps:
 * Paso 2 — product catalog + adicionales selection
 * Paso 3 — confirmation summary
 *
 * All click operations use JS to avoid OS-focus dependency.
 */
public class PedidoNuevoPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // ── Paso 2 selectors ──────────────────────────────────────────────────
    private final By productGrid = By.cssSelector("div.productos-grid");
    private final By productCards = By.cssSelector("div.paso2-catalogo div.producto-card");
    private final By itemCards = By.cssSelector("div.paso2-pedido div.item-card");
    private final By totalFila = By.cssSelector("div.total-fila strong");
    private final By continuarBtn = By.cssSelector(".paso-actions.paso-actions--right .btn-cta");

    // ── Paso 3 selectors ──────────────────────────────────────────────────
    private final By confirmarLayout = By.cssSelector("div.confirmar-layout");
    private final By resumenTotal = By.cssSelector("div.resumen-total strong");
    private final By confirmarBtn = By.cssSelector("button.btn-cta.btn-cta--full");

    // ── Success screen ────────────────────────────────────────────────────
    private final By exitoCard = By.cssSelector("div.exito-card");
    private final By detallePedidoLink = By.cssSelector("a.btn-cta.btn-cta--secondary");

    public PedidoNuevoPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }

    /** Waits until the paso-2 product grid is populated. */
    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productGrid));
        // Wait for at least one product card to appear
        wait.until(d -> !d.findElements(productCards).isEmpty());
    }

    /**
     * Clicks the product card at position {@code index} in the catalog grid.
     * Returns the product's displayed name.
     */
    public String addProductByIndex(int index) {
        List<WebElement> cards = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(productCards));
        WebElement card = cards.get(index);
        String nombre = card.findElement(By.cssSelector("h3.producto-card-nombre")).getText();
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", card);
        // Wait for the item to appear in the pedido sidebar
        wait.until(d -> d.findElements(itemCards).size() > index);
        return nombre;
    }

    /**
     * Expands the adicionales panel for the item at {@code itemIndex}
     * in the right-hand pedido list, waits for adicionales to load from API,
     * selects the first {@code count} available adicionales, and returns
     * their displayed names.
     */
    public List<String> expandAndSelectAdicionales(int itemIndex, int count) {
        List<WebElement> cards = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(itemCards));
        WebElement itemCard = cards.get(itemIndex);

        // Click the toggle button
        WebElement toggleBtn = itemCard.findElement(By.cssSelector("button.btn-adicionales"));
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", toggleBtn);

        // Wait for the panel to appear inside this item-card
        wait.until(d -> !itemCard.findElements(By.cssSelector("div.adicionales-panel")).isEmpty());

        // Wait for "Cargando…" to disappear
        wait.until(d -> itemCard.findElements(By.cssSelector("p.msg-cargando")).isEmpty()
                || !itemCard.findElement(By.cssSelector("p.msg-cargando")).isDisplayed());

        // Collect labels and select first N
        WebElement panel = itemCard.findElement(By.cssSelector("div.adicionales-panel"));
        List<WebElement> labels = wait.until(d -> {
            List<WebElement> found = panel.findElements(By.cssSelector("label.adicional-label"));
            return found.isEmpty() ? null : found;
        });

        List<String> selected = new ArrayList<>();
        for (int i = 0; i < Math.min(count, labels.size()); i++) {
            WebElement label = labels.get(i);
            String nombre = label.findElement(By.cssSelector("span.adicional-nombre")).getText();
            WebElement checkbox = label.findElement(By.cssSelector("input[type='checkbox']"));
            if (!checkbox.isSelected()) {
                js.executeScript("arguments[0].click();", checkbox);
            }
            selected.add(nombre);
        }
        return selected;
    }

    /**
     * Returns the total amount text shown at the bottom of paso 2 (e.g. "$42,000").
     */
    public String getTotalText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(totalFila)).getText();
    }

    /** Clicks "Continuar →" to advance from paso 2 to paso 3. */
    public void irPaso3() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(continuarBtn));
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", btn);
    }

    /** Waits until paso 3 (confirmation screen) is visible. */
    public void waitForPaso3() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmarLayout));
    }

    /** Returns the total text shown in the paso-3 summary (e.g. "$42,000"). */
    public String getTotalTextPaso3() {
        return driver.findElement(resumenTotal).getText();
    }

    /** Clicks "¡Confirmar pedido!" in paso 3. */
    public void confirmarPedido() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(confirmarBtn));
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", btn);
    }

    /** Waits for the success card to appear after order is confirmed. */
    public void waitForExito() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(exitoCard));
    }

    /**
     * Reads the pedido ID from the "Ver detalle del pedido" link href.
     * The link's routerLink is ['/pedidos', pedidoId].
     */
    public long getPedidoId() {
        WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(detallePedidoLink));
        String href = link.getAttribute("href");
        // href is like "http://localhost:4200/pedidos/42"
        String[] parts = href.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}
