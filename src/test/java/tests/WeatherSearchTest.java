package tests;

import base.BaseTest;
import com.aventstack.extentreports.Status;
import constants.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.WeatherCityPage;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherSearchTest extends BaseTest {

    @Test
    @DisplayName("TC1 - Pretraga Beograda i verifikacija prognoze")
    public void pretragaBeogradaIVerifikacijaPrognozeTest() {

        WeatherCityPage cityPage = homePage.searchCity(Constants.BELGRADE_SEARCH);
        testLog.log(Status.INFO, "Izvršena pretraga lokacije: " + Constants.BELGRADE_SEARCH);

        String heading = cityPage.getHeadingText();
        testLog.log(Status.INFO, "Pročitani heading sa ekrana: " + heading);
        assertTrue(heading.toLowerCase().contains(Constants.BELGRADE_HEADING.toLowerCase()), "Heading greška!");
        testLog.log(Status.PASS, "Verifikacija naslova uspešna.");

        String temperature = cityPage.getCurrentTemperatureText();
        assertTrue(temperature.contains(Constants.CELSIUS_SYMBOL), "Temperatura greška!");
        assertTrue(cityPage.hasTodaySection(), "Today sekcija greška!");
        testLog.log(Status.PASS, "Verifikacija trenutnih parametara uspešna.");

        cityPage.clickTenDayForecast();
        assertTrue(cityPage.hasTenDayForecast(), "10-Day Forecast kontejner nije pronađen.");
        testLog.log(Status.PASS, "Verifikacija 10-Day Forecast sekcije uspešna.");

        assertTrue(cityPage.eachDayHasHighAndLow(), "Neki dan ne prikazuje max/min parametre.");
        testLog.log(Status.PASS, "Verifikacija min/max parametara prognoze po danima uspešna.");
    }
}