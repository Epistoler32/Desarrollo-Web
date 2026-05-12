package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the product creation page (/productos/nuevo).
 *
 * WHY pure JS?
 * - sendKeys requires the Chrome window to have OS-level keyboard focus,
 * which is not guaranteed when tests run alongside other windows.
 * - Setting element.value via JS + dispatching 'input' and 'change' events
 * works regardless of focus, and Zone.js (patched by Angular) intercepts
 * those events and runs change detection automatically.
 */
public class ProductoCrearPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    private final By nombreInput = By.id("nombre");
    private final By precioInput = By.id("precio");
    private final By categoriaSelect = By.id("categoriaId");
    private final By tiempoInput = By.id("tiempoMinutos");
    private final By descripcionInput = By.id("descripcion");
    private final By submitBtn = By.cssSelector("button.btn-submit");

    public ProductoCrearPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nombreInput));
    }

    /**
     * Sets any input/textarea value via JS and dispatches both 'input' and
     * 'change' events with bubbles:true. Zone.js patches dispatchEvent so
     * Angular's change detection fires and the NgModel / NgForm update.
     */
    private void setFieldValue(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        js.executeScript(
                "var el = arguments[0], v = arguments[1];" +
                        "el.value = v;" +
                        "el.dispatchEvent(new Event('input',  {bubbles:true, cancelable:true}));" +
                        "el.dispatchEvent(new Event('change', {bubbles:true, cancelable:true}));",
                el, value);
    }

    /**
     * Selects a <select> option by its visible text via JS and dispatches
     * 'change' so Angular's SelectControlValueAccessor updates the model.
     */
    private void setSelectValue(By locator, String visibleText) {
        WebElement sel = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        js.executeScript(
                "var sel = arguments[0], text = arguments[1];" +
                        "for (var i = 0; i < sel.options.length; i++) {" +
                        "  if (sel.options[i].text.trim() === text) { sel.selectedIndex = i; break; }" +
                        "}" +
                        "sel.dispatchEvent(new Event('change', {bubbles:true, cancelable:true}));",
                sel, visibleText);
    }

    public void enterNombre(String nombre) {
        setFieldValue(nombreInput, nombre);
    }

    public void enterPrecio(String precio) {
        setFieldValue(precioInput, precio);
    }

    public void selectCategoria(String visibleText) {
        setSelectValue(categoriaSelect, visibleText);
    }

    public void enterTiempo(String minutos) {
        setFieldValue(tiempoInput, minutos);
    }

    public void enterDescripcion(String desc) {
        setFieldValue(descripcionInput, desc);
    }

    /**
     * Waits until Angular enables the submit button (all required fields valid),
     * then clicks it via JS. js.click() on a type="submit" button fires the
     * browser's native click event, which triggers form submission and Angular's
     * (ngSubmit) handler — without requiring window focus.
     */
    public void guardar() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", btn);
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
