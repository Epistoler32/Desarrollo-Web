package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object for the client's order history page (/perfil/pedidos).
 * Shows pedido cards ordered from newest to oldest.
 * Each card has: span.pedido-id ("#42"), span.pedido-estado, click →
 * /pedidos/:id
 */
public class MisPedidosPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    private final By pedidoCards = By.cssSelector("div.pedido-card");
    private final By cargandoMsg = By.cssSelector("p.estado-msg");

    public MisPedidosPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
    }

    /**
     * Waits until the pedido list has loaded (at least one card or the "no pedidos"
     * message).
     */
    public void waitForLoad() {
        // Wait for the loading message to disappear
        wait.until(d -> {
            List<WebElement> msgs = d.findElements(cargandoMsg);
            if (msgs.isEmpty())
                return true;
            return !msgs.get(0).getText().contains("Cargando");
        });
    }

    /**
     * Finds the pedido card matching the given ID.
     * The card contains span.pedido-id with text "#42".
     */
    private WebElement findCardForPedido(long pedidoId) {
        List<WebElement> cards = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(pedidoCards));
        for (WebElement card : cards) {
            String idText = card.findElement(By.cssSelector("span.pedido-id")).getText().trim();
            if (idText.equals("#" + pedidoId)) {
                return card;
            }
        }
        throw new RuntimeException("Pedido card not found: #" + pedidoId);
    }

    /** Returns the estado text for the given pedido (e.g. "EN_PREPARACION"). */
    public String getEstadoPedido(long pedidoId) {
        return findCardForPedido(pedidoId)
                .findElement(By.cssSelector("span.pedido-estado"))
                .getText().trim();
    }

    /** Clicks the pedido card to navigate to /pedidos/:id. */
    public void clickPedido(long pedidoId) {
        WebElement card = findCardForPedido(pedidoId);
        js.executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", card);
    }
}
