package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Page Object for the Admin dashboard (/admin/dashboard). */
public class AdminDashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Selectors match admin-dashboard.component.html
    private final By dashTitle = By.cssSelector("h1.dash-title");
    private final By gestionMenuCard = By.cssSelector("a.dash-card[href='/productos']");

    public AdminDashboardPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(dashTitle));
    }

    public String getTitleText() {
        return driver.findElement(dashTitle).getText();
    }

    /** Clicks the "Gestión de Menú" card to navigate to /productos. */
    public void clickGestionMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(gestionMenuCard)).click();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
