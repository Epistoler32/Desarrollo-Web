package com.seaside.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

/**
 * Base class for all Selenium e2e tests.
 *
 * Prerequisites:
 * 1. Spring Boot backend: mvn spring-boot:run (port 8080)
 * 2. Angular frontend: ng serve (port 4200)
 *
 * Run with: mvn test -Pe2e
 */
public abstract class BaseE2ETest {

    protected static final String BASE_URL = System.getProperty("app.url", "http://localhost:4200");

    protected static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeAll
    static void setupDriverBinary() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless=new"); // commented out to watch tests live
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1400,900");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    /** Navigate to a path relative to BASE_URL. */
    protected void navigateTo(String path) {
        driver.get(BASE_URL + path);
    }

    /** Open a new tab and switch to it. Returns the original tab handle. */
    protected String openNewTab() {
        String original = driver.getWindowHandle();
        ((JavascriptExecutor) driver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
        return original;
    }

    /** Switch to a tab by its saved window handle. */
    protected void switchToTab(String handle) {
        driver.switchTo().window(handle);
    }

    /** Switch to the last open tab. */
    protected void switchToLastTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    /** Refresh the current tab. */
    protected void refresh() {
        driver.navigate().refresh();
    }
}
