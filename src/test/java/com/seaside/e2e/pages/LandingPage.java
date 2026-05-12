package com.seaside.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Page Object for the Landing page (/). */
public class LandingPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By heroTitle = By.cssSelector("h1.hero-title");
    private final By menuCta = By.cssSelector("a.hero-cta");
    private final By loginLink = By.cssSelector("a.login-link[href='/login']");

    public LandingPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(heroTitle));
    }

    public String getHeroTitleText() {
        return driver.findElement(heroTitle).getText();
    }

    /** Clicks the "Ver Menú" CTA in the hero. */
    public void clickVerMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(menuCta)).click();
    }

    /** Clicks the "Log in" link in the navbar. */
    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
