package locators;

import org.openqa.selenium.By;

public class Locators {

    public static final By SEARCH_INPUT = By.xpath("//input[@data-testid='SearchBar-input']");
    public static final By SEARCH_INPUT_ALT = By.xpath("//input[contains(@placeholder,'Search')]");

    public static final By SEARCH_SUGGESTIONS_LIST = By.xpath("//ul[@data-testid='suggested-search-results']");
    public static final By SEARCH_SUGGESTION_ITEM = By.xpath("(//ul[@data-testid='suggested-search-results']//li)[1]");

    public static final By PAGE_HEADING = By.xpath("//h1[@data-testid='ConditionsSummary-loc']");
    public static final By PAGE_HEADING_ALT = By.xpath("//*[contains(@class,'LocationPageTitle')]");
}