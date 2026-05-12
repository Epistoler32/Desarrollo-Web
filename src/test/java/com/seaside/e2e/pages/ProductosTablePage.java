package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object for the Products CRUD table (/productos).
 * Also handles the adicionales modal that appears on that same page.
 */
public class ProductosTablePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Header / navigation — selectors match producto-table.component.html
    private final By pageTitle = By.cssSelector("h1.admin-title");
    private final By btnNuevo = By.cssSelector("a.btn-nuevo");

    // Each product row
    private final By productRows = By.cssSelector("tbody tr.product-row");

    // Adicionales modal
    private final By modalTitle = By.cssSelector("h3.modal-title");
    private final By adicionalItems = By.cssSelector(".adicional-checklist .adicional-check-item");
    private final By btnSaveModal = By.cssSelector("button.btn-save");
    private final By btnCancelModal = By.cssSelector("button.btn-cancel-modal");

    public ProductosTablePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
    }

    /** Waits until at least one product row is visible in the table. */
    public void waitForRows() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(productRows, 0));
    }

    /**
     * Clicks "+ Nuevo Producto" via JS to reliably trigger Angular router
     * navigation.
     */
    public void clickNuevoProducto() {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(btnNuevo));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    /**
     * Finds the row for the product with the given name and clicks its
     * "🍽 Acomp." (adicionales) button via JS to avoid OS-focus dependency.
     */
    public void openAdicionalesFor(String productName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productRows));
        List<WebElement> rows = driver.findElements(productRows);
        for (WebElement row : rows) {
            if (row.findElement(By.cssSelector("td.col-name")).getText().trim().equals(productName.trim())) {
                WebElement btn = row.findElement(By.cssSelector("button.btn-adicionales"));
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", btn);
                return;
            }
        }
        throw new RuntimeException("Product not found in table: " + productName);
    }

    /** Waits until the adicionales modal is visible. */
    public void waitForModal() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
    }

    /**
     * Sets the modal so that EXACTLY the given adicionales are selected.
     * First unchecks all currently-checked items, then checks the desired ones.
     * This avoids false counts when a product already has pre-assigned adicionales.
     */
    public void selectAdicionales(String... names) {
        java.util.Set<String> desired = new java.util.HashSet<>(java.util.Arrays.asList(names));
        List<WebElement> items = driver.findElements(adicionalItems);
        // First pass: uncheck everything that should not be selected
        for (WebElement item : items) {
            String itemName = item.findElement(By.cssSelector("span.check-name")).getText();
            WebElement checkbox = item.findElement(By.cssSelector("input[type='checkbox']"));
            if (checkbox.isSelected() && !desired.contains(itemName)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
            }
        }
        // Second pass: check everything that should be selected
        for (WebElement item : items) {
            String itemName = item.findElement(By.cssSelector("span.check-name")).getText();
            WebElement checkbox = item.findElement(By.cssSelector("input[type='checkbox']"));
            if (!checkbox.isSelected() && desired.contains(itemName)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
            }
        }
    }

    /**
     * Returns the names of all available adicionales displayed in the modal.
     * Useful for picking the first N without hardcoding names.
     */
    public List<String> getAllAdicionalNames() {
        waitForModal();
        return driver.findElements(By.cssSelector("span.check-name"))
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    /** Clicks "Guardar" in the adicionales modal via JS. */
    public void saveAdicionales() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnSaveModal));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", btn);
        // Wait for the modal to disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modalTitle));
    }

    public boolean isProductInTable(String productName) {
        // Rows must already be loaded before calling this
        List<WebElement> rows = driver.findElements(productRows);
        return rows.stream()
                .anyMatch(r -> r.findElement(By.cssSelector("td.col-name"))
                        .getText().trim().equals(productName.trim()));
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
