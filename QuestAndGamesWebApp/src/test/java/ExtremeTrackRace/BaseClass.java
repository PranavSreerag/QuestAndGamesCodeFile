package ExtremeTrackRace;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

    public WebDriver driver;
    public WebDriverWait wait;
    public Actions act;
    public JavascriptExecutor js;

    // ⏱️ Timer variables
    public long startTime;
    public long endTime;
    public long gameDuration;

    @BeforeTest
    public void setup() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // ✅ Persistent session
        options.addArguments("user-data-dir=C:/selenium-profile");

        // ✅ Stability fixes
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        act = new Actions(driver);
        js = (JavascriptExecutor) driver;

        // ✅ Mobile screen
        driver.manage().window().setSize(new Dimension(390, 844));

        // Open app
        driver.get("https://quega-staging.ashontech.in/home");

        System.out.println("👉 First run: Login using TELEGRAM");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        // Open Games tab
        WebElement gamesBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(@aria-label,'Games')]")
        ));

        js.executeScript("arguments[0].click();", gamesBtn);
        System.out.println("✅ Games tab opened");

        // Wait for games lobby
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(.,'Escape Track')]")
        ));
    }

    // 🔥 Scroll helper
    public WebElement scrollToElement(By locator) {
        for (int i = 0; i < 10; i++) {
            try {
                WebElement el = driver.findElement(locator);
                if (el.isDisplayed()) {
                    return el;
                }
            } catch (Exception e) {}

            js.executeScript("window.scrollBy(0,400)");
            sleep(800);
        }

        throw new RuntimeException("❌ Element not found after scrolling");
    }

    // 🔥 Sleep helper
    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 JS click helper
    public void jsClick(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    // 🔥 Scroll element into center
    public void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    // 🔥 Parse score safely
    public int parseScore(String scoreText) {
        return (int) Double.parseDouble(
                scoreText.trim()
                         .replace("+", "")
                         .replace(",", "")
        );
    }

    // 🔥 Timer helpers
    public void startGameTimer() {
        startTime = System.currentTimeMillis();
    }

    public void stopGameTimer() {
        endTime = System.currentTimeMillis();
        gameDuration = endTime - startTime;
    }

    public void printGameDuration(String gameName) {
        System.out.println("⏱️ " + gameName + " Game Duration: " + gameDuration + " ms");
        System.out.println("⏱️ " + gameName + " Game Duration: " + (gameDuration / 1000.0) + " seconds");
    }
    
    
    public int parseTimeToMinutes(String timeText) {
        timeText = timeText.trim().toLowerCase();

        int hours = 0;
        int minutes = 0;

        // Match hours like "3h"
        java.util.regex.Matcher hourMatcher = java.util.regex.Pattern
                .compile("(\\d+)\\s*h")
                .matcher(timeText);

        if (hourMatcher.find()) {
            hours = Integer.parseInt(hourMatcher.group(1));
        }

        // Match minutes like "29m"
        java.util.regex.Matcher minuteMatcher = java.util.regex.Pattern
                .compile("(\\d+)\\s*m")
                .matcher(timeText);

        if (minuteMatcher.find()) {
            minutes = Integer.parseInt(minuteMatcher.group(1));
        }

        return (hours * 60) + minutes;
    }
    

    // 🔥 EXACT WORKING close-game method from your single code
    public void closeGameUsingYourWorkingLogic() {

        try {
            // same exact logic you had in single-file code
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));

            By closeBtnLocator = By.cssSelector("i.fa-times, .close-btn, [class*='close']");
            WebElement closeButton = shortWait.until(
                    ExpectedConditions.elementToBeClickable(closeBtnLocator)
            );

            closeButton.click();
            System.out.println("Close button clicked successfully.");

        } catch (Exception e) {
            System.out.println("Standard click failed, attempting JavaScript click...");

            try {
                WebElement closeButton = driver.findElement(
                        By.xpath("//i[contains(@class, 'fa-times')]/..")
                );

                js.executeScript("arguments[0].click();", closeButton);
                System.out.println("JavaScript click successful.");

            } catch (Exception jsEx) {
                System.out.println("Could not find or click the element: " + jsEx.getMessage());
                throw new RuntimeException("❌ Could not close DigiX game");
            }
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
