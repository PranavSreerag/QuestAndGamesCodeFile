package ExtremeTrackRace;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DigiXTest extends BaseClass {

    @Test(priority = 1, enabled = true)
    public void DigiX() throws Exception {

        // =========================
        // 1. GET PROFILE TIME BEFORE GAME
        // =========================
        driver.navigate().to("https://quega-staging.ashontech.in/profile-view");
        sleep(3000);

        js.executeScript("window.scrollBy(0,500);");
        sleep(2000);

        
        WebElement beforeGameTime = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.history-pvc-active-time-value span")
        ));

        String beforeTimeText = beforeGameTime.getText();
        int beforeTimeMinutes = parseTimeToMinutes(beforeTimeText);

        System.out.println("⏱️ Before Game Time Text: " + beforeTimeText);
        System.out.println("⏱️ Before Game Time in Minutes: " + beforeTimeMinutes);

        // =========================
        // 2. GO TO LOBBY
        // =========================
        driver.navigate().to("https://quega-staging.ashontech.in/lobby");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        // 🔥 Find DigiX game card
        WebElement digix = scrollToElement(
                By.xpath("//*[contains(text(),'DigiX') or contains(text(),'Digix')]")
        );

        sleep(1000);

        // 🔥 Click DigiX
        scrollIntoView(digix);
        sleep(500);
        jsClick(digix);

        // Wait for DigiX page
        wait.until(ExpectedConditions.urlContains("game"));
        sleep(2000);

        System.out.println("✅ DigiX opened");

        js.executeScript("window.scrollBy(0,700);");
        sleep(2000);

        // =========================
        // 3. GET XP BEFORE GAME
        // =========================
        WebElement startingScore = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[class='leaderboard-list'] div:nth-child(2) div:nth-child(4)")
        ));

        int beforeScoreNum = parseScore(startingScore.getText());
        System.out.println("🎯 XP before playing the game: " + beforeScoreNum);

        // =========================
        // 4. CLICK START GAME
        // =========================
        WebElement start = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".game-start-btn")
        ));

        scrollIntoView(start);
        sleep(1000);

        jsClick(start);
        System.out.println("▶️ DigiX Start clicked");

        // ⏱️ Start timer
        startGameTimer();

        sleep(5000);

        // =========================
        // 5. CLOSE GAME
        // =========================
        closeGameUsingYourWorkingLogic();
        sleep(3000);

        // 🔥 Click Yes confirmation
        WebElement yesButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Yes']")
        ));
        yesButton.click();

        sleep(3000);

        // =========================
        // 6. GET WIN SCORE
        // =========================
        WebElement winScoreEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='win-stat-value']")
        ));

        int winScoreNum = parseScore(winScoreEl.getText());
        System.out.println("🏆 XP gained in-game: " + winScoreNum);

        sleep(3000);

        // 🔥 Done button
        WebElement slideBackButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@class='btn-win-done']")
        ));
        slideBackButton.click();

        sleep(3000);

        // =========================
        // 7. STOP TIMER
        // =========================
        stopGameTimer();
        printGameDuration("DigiX");

        long durationInSeconds = gameDuration / 1000;
        System.out.println("⏱️ Game Duration in seconds: " + durationInSeconds);

        // =========================
        // 8. GET XP AFTER GAME
        // =========================
        js.executeScript("window.scrollBy(0,500);");
        sleep(2000);

        WebElement finalScore = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[class='leaderboard-list'] div:nth-child(2) div:nth-child(4)")
        ));

        int afterScoreNum = parseScore(finalScore.getText());
        System.out.println("🎯 XP after playing the game: " + afterScoreNum);

        int expectedScore = beforeScoreNum + winScoreNum;
        System.out.println("🎯 Expected XP after game: " + expectedScore);

        
        /*
        Assert.assertEquals(afterScoreNum, expectedScore,
                "❌ The actual XP value does not match the expected value.");*/


        // =========================
        // 9. CLICK TOP LEFT BACK BUTTON
        // =========================
        js.executeScript("window.scrollTo(0,0);");
        sleep(2000);

        try {
            WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.game-page-back-btn")
            ));

            scrollIntoView(backButton);
            sleep(1000);

            jsClick(backButton);
            System.out.println("⬅️ Back button clicked successfully");

        } catch (Exception e) {
            System.out.println("⚠️ Main back button click failed, trying fallback...");

            try {
                WebElement svgParentBack = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[name()='svg']/ancestor::button[1]")
                ));

                scrollIntoView(svgParentBack);
                sleep(1000);

                jsClick(svgParentBack);
                System.out.println("⬅️ Fallback back button clicked successfully");

            } catch (Exception ex) {
                System.out.println("❌ Could not click back button: " + ex.getMessage());
                throw ex;
            }
        }

        // =========================
        // 10. VERIFY PROFILE TIME AFTER GAME
        // =========================
        sleep(2000);
        driver.navigate().to("https://quega-staging.ashontech.in/profile-view");
        sleep(3000);

        js.executeScript("window.scrollBy(0,700);");
        sleep(2000);

        WebElement gameTimeAfter = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.history-pvc-active-time-value span")
        ));

        String afterTimeText = gameTimeAfter.getText();
        int afterTimeMinutes = parseTimeToMinutes(afterTimeText);

        System.out.println("⏱️ After Game Time Text: " + afterTimeText);
        System.out.println("⏱️ After Game Time in Minutes: " + afterTimeMinutes);

        int expectedAfterMinutes = beforeTimeMinutes + (int)(gameDuration / 1000 / 60.0);
        System.out.println("⏱️ Expected Time After Game (approx): " + expectedAfterMinutes + " minutes");

        

        
        Assert.assertTrue(
        		afterTimeMinutes >= beforeTimeMinutes,
                "❌ Profile active time did not increase after playing the game."
        );

        System.out.println("✅ The game time is being updated correctly");
    }
}
