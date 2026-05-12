package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the operator pedidos table (/pedidos).
 *
 * Exposes:
 * - Finding a specific pedido row by its ID
 * - Opening and confirming the "Actualizar Estado" modal
 * - Opening and confirming the "Asignar Domiciliario" modal
 *
 * The estado select uses [value]="e" (plain string binding) so setting
 * select.value + dispatching 'change' works correctly with Angular's ngModel.
 *
 * The domiciliario select uses [ngValue]="d.id" (object binding).
 * Setting selectedIndex + dispatching 'change' lets Angular's
 * SelectControlValueAccessor map back to the correct numeric id.
 */
public class OperadorPedidosPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    private final By tableRows = By.cssSelector("tr.pedido-row");
    // Modal: Actualizar Estado
    private final By modalEstadoTitle = By.xpath("//h3[contains(text(),'Actualizar Estado')]");
    private final By estadoSelect = By.cssSelector("select[name='estado']");
    private final By confirmarEstadoBtn = By.cssSelector("button.btn-confirmar");
    // Modal: Asignar Domiciliario
    private final By modalAsignarTitle = By.xpath("//h3[contains(text(),'Asignar Domiciliario')]");
    private final By domiciliarioSelect = By.cssSelector("select[name='domiciliario']");
    private final By confirmarAsignarBtn = By.cssSelector("button.btn-confirmar");

    public OperadorPedidosPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }

    public void waitForLoad() {
        // Wait for the pedidos table section to appear
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.tabla-pedidos")));
    }

    /**
     * Returns a By locator that directly targets the table row for the given
     * pedido ID. Using XPath ensures we never hold a stale WebElement reference
     * across Angular re-renders (each modal close triggers an HTTP reload that
     * rebuilds the table DOM).
     */
    private By rowLocator(long pedidoId) {
        return By.xpath(
                "//tr[contains(@class,'pedido-row') and " +
                        ".//td[normalize-space(text())='#" + pedidoId + "']]");
    }

    /**
     * Clicks the "Ver todos" filter button so that ENTREGADO / CANCELADO
     * pedidos (hidden by the default soloActivos=true filter) become visible,
     * then waits for the table to reload.
     */
    public void verTodos() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.btn-filtro")));
        js.executeScript("arguments[0].click();", btn);
        // Wait for the table to refresh (the count badge changes / rows reload)
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("table.tabla-pedidos")));
    }

    /** Returns the estado badge text for the given pedido. */
    public String getEstadoPedido(long pedidoId) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator(pedidoId)))
                .findElement(By.cssSelector("span.estado-badge"))
                .getText().trim();
    }

    // ── Estado modal ──────────────────────────────────────────────────────

    /** Clicks "📋 Estado" for the given pedido to open the estado modal. */
    public void abrirModalEstadoParaPedido(long pedidoId) {
        // Re-locate the row fresh on every call to avoid stale element references
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator(pedidoId)));
        WebElement btn = row.findElement(By.cssSelector("button.btn-estado"));
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", btn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalEstadoTitle));
    }

    /**
     * Selects the given estado value in the modal select.
     * The select uses [value]="e" (string binding), so setting .value directly
     * works.
     */
    public void seleccionarEstado(String estado) {
        WebElement sel = wait.until(ExpectedConditions.visibilityOfElementLocated(estadoSelect));
        js.executeScript(
                "var s=arguments[0],v=arguments[1];" +
                        "s.value=v;" +
                        "s.dispatchEvent(new Event('change',{bubbles:true,cancelable:true}));",
                sel, estado);
    }

    /** Waits for the confirm button to be enabled, then clicks it. */
    public void confirmarCambioEstado() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(confirmarEstadoBtn));
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", btn);
        // Wait for modal to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modalEstadoTitle));
    }

    // ── Asignar domiciliario modal ────────────────────────────────────────

    /** Clicks "🛵 Asignar" for the given pedido to open the asignar modal. */
    public void abrirModalAsignarParaPedido(long pedidoId) {
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator(pedidoId)));
        WebElement btn = row.findElement(By.cssSelector("button.btn-asignar"));
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", btn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalAsignarTitle));
    }

    /**
     * Selects the first available domiciliario (index 1, skipping the disabled
     * placeholder).
     * The select uses [ngValue]="d.id". Setting selectedIndex + dispatching
     * 'change'
     * allows Angular's SelectControlValueAccessor to update domiciliarioElegido.
     */
    public void seleccionarPrimerDomiciliario() {
        WebElement sel = wait.until(ExpectedConditions.visibilityOfElementLocated(domiciliarioSelect));
        js.executeScript(
                "var s=arguments[0];" +
                        "s.selectedIndex=1;" +
                        "s.dispatchEvent(new Event('change',{bubbles:true,cancelable:true}));",
                sel);
    }

    /**
     * Waits for the confirm button to be enabled, then confirms the domiciliario
     * assignment.
     */
    public void confirmarAsignacion() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(confirmarAsignarBtn));
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", btn);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modalAsignarTitle));
    }
}
