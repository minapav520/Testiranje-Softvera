package pages;

import locators.Locators;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WeatherHomePage extends BasePage {

    private final By searchInput = Locators.SEARCH_INPUT;
    private final By searchInputAlt = Locators.SEARCH_INPUT_ALT;
    private final By suggestionsList = Locators.SEARCH_SUGGESTIONS_LIST;
    private final By suggestionItems = Locators.SEARCH_SUGGESTION_ITEM;

    public WeatherHomePage(WebDriver driver) {
        super(driver);
        handleCookieBanner();
    }

    public void handleCookieBanner() {
        try {
            By iframeLocator = By.id("sp_message_iframe_1233284");
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(4));
            shortWait.until(ExpectedConditions.presenceOfElementLocated(iframeLocator));

            driver.switchTo().frame(driver.findElement(iframeLocator));

            By acceptButton = By.xpath("//button[contains(@class, 'sp_choice_type_11') or @title='Accept']");
            shortWait.until(ExpectedConditions.elementToBeClickable(acceptButton)).click();

            driver.switchTo().defaultContent();
        } catch (Exception e) {
            driver.switchTo().defaultContent();
        }
    }

    private By resolveSearchInput() {
        if (elementExists(searchInput)) {
            return searchInput;
        }
        return searchInputAlt;
    }

    public WeatherHomePage enterSearchText(String city) {
        By inputLocator = resolveSearchInput();
        WebElement inputElement = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
        inputElement.click();

        inputElement.sendKeys(Keys.CONTROL + "a");
        inputElement.sendKeys(Keys.DELETE);
        inputElement.sendKeys(city);

        shortWait(1500);
        return this;
    }

    public boolean hasSuggestions() {
        try {
            WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            quickWait.until(ExpectedConditions.visibilityOfElementLocated(suggestionsList));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WeatherCityPage clickSuggestion() {
        By inputLocator = resolveSearchInput();
        WebElement inputElement = driver.findElement(inputLocator);

        try {
            if (hasSuggestions()) {
                WebElement firstSuggestion = wait.until(ExpectedConditions.elementToBeClickable(suggestionItems));
                firstSuggestion.click();
            } else {
                inputElement.sendKeys(Keys.ENTER);
            }
        } catch (Exception e) {
            inputElement.sendKeys(Keys.ENTER);
        }

        shortWait(2000);
        return new WeatherCityPage(driver);
    }

    public WeatherCityPage searchCity(String city) {
        enterSearchText(city);
        return clickSuggestion();
    }
}