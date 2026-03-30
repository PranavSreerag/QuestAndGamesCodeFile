package ExtremeTrackRace;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

public class EscapeTrackTest extends BaseClass {

    @Test
    public void EscapeTrack() throws Exception {

        WebElement escapeTrack = scrollToElement(
                By.xpath("//div[contains(text(),'Escape Track')]")
        );

        jsClick(escapeTrack);

        wait.until(ExpectedConditions.urlContains("game"));

        System.out.println("✅ Escape Track opened");

        WebElement startBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".game-start-btn")
        ));

        startGameTimer();

        jsClick(startBtn);
        System.out.println("▶️ Escape Track Start clicked");

        WebElement canvas = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.tagName("canvas")
        ));

        act.moveToElement(canvas).click().perform();

        System.out.println("🎮 Escape Track started");

        stopGameTimer();
        printGameDuration("Escape Track");
    }
}
