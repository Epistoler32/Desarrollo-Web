package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object for the full pedido detail view (/pedidos/:id).
 *
 * This page is used both by operators and clients (MisPedidos → click →
 * /pedidos/:id).
 * It shows: order info card, client card, domiciliario card, and a table of
 * items
 * with their adicionales and the total.
 *
 * Distinct from ProductoDetallePage which is for /productos/:id (public product
 * view).
 */
public class PedidoDetalleFullPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // The page title is "Pedido #42"
    private final By pageTitle = By.cssSelector("h1.admin-title");
    // Estado badge in the info card
    private final By estadoBadge = By.cssSelector("span.estado-badge");
    // Item detail cards
    private final By itemDetalleCards = By.cssSelector("div.item-detalle-card");
    // Product name inside each item card
    private final By itemNombre = By.cssSelector("span.item-detalle-nombre");
    // Adicional rows (inside item cards)
    private final By adicionalNombre = By.cssSelector("span.adicional-detalle-nombre");
    // The total shown in the summary row (.price-tag--lg exists in info card and
    // items-total row)
    private final By totalTag = By.cssSelector("span.price-tag.price-tag--lg");

    public PedidoDetalleFullPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
        // Also wait for items to render
        wait.until(d -> !d.findElements(itemDetalleCards).isEmpty());
    }

    /** Returns the current estado badge text (e.g. "ENTREGADO"). */
    public String getEstado() {
        return driver.findElement(estadoBadge).getText().trim();
    }

    /** Returns the displayed name of every product item in the order. */
    public List<String> getProductNames() {
        return driver.findElements(itemNombre)
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();
    }

    /**
     * Returns all adicional names across all items in the order.
     * Each adicional row contains: ↳ {name} × {qty} +${subtotal}
     */
    public List<String> getAdicionalNames() {
        return driver.findElements(adicionalNombre)
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();
    }

    /**
     * Returns the order total text as shown in the page.
     * There are two .price-tag--lg elements (info card + items section), both
     * show the same value. Returns the text of the first one found.
     * Example: "$84,000" or "$84.000" depending on locale.
     */
    public String getTotalText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(totalTag)).getText().trim();
    }
}
