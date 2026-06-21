package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import constants.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.WeatherHomePage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@ExtendWith(BaseTest.ScreenshotAndReportWatcher.class)
public class BaseTest {

    protected static WebDriver driver;
    protected WeatherHomePage homePage;

    protected static ExtentReports extent;
    protected static ExtentTest testLog;

    @BeforeAll
    public static void setupReport() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("Izvestaj_Testiranja.html");
        sparkReporter.config().setReportName("Automatizovani Izveštaj Vremenske Prognoze");
        sparkReporter.config().setDocumentTitle("QA Projekat Rezultati");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("QA Inženjer", "Studentska Grupa");
        extent.setSystemInfo("Browser", "Google Chrome");
    }

    @BeforeEach
    public void setUp() {
        testLog = extent.createTest("TC1 - Pretraga Beograda i verifikacija prognoze");

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        testLog.log(Status.INFO, "Pretraživač je uspešno pokrenut.");

        driver.get(Constants.WEATHER_URL);
        testLog.log(Status.INFO, "Otvorena stranica: " + Constants.WEATHER_URL);

        homePage = new WeatherHomePage(driver);
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static class ScreenshotAndReportWatcher implements TestWatcher {

        @Override
        public void testSuccessful(ExtensionContext context) {
            if (testLog != null) {
                testLog.log(Status.PASS, "Test je USPEŠNO prošao sve verifikacije!");
            }
            if (driver != null) {
                driver.quit();
            }
        }

        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            if (testLog != null) {
                testLog.log(Status.FAIL, "Test je PAO. Razlog: " + cause.getMessage());

                if (driver != null) {
                    try {
                        File screenshotFolder = new File("screenshots");
                        if (!screenshotFolder.exists()) {
                            screenshotFolder.mkdirs();
                        }

                        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String screenshotName = context.getRequiredTestMethod().getName() + "_" + timestamp + ".png";

                        String relativePath = "screenshots/" + screenshotName;

                        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                        File destFile = new File(screenshotFolder, screenshotName);
                        FileUtils.copyFile(srcFile, destFile);

                        testLog.addScreenCaptureFromPath(relativePath, "Ekran u trenutku pada");
                        System.out.println("Screenshot uspešno sačuvan: " + destFile.getAbsolutePath());

                    } catch (IOException e) {
                        System.out.println("Greška prilikom pravljenja screenshot-a: " + e.getMessage());
                    } finally {
                        driver.quit();
                    }
                }
            }
        }

        @Override
        public void testAborted(ExtensionContext context, Throwable cause) {
            if (driver != null) driver.quit();
        }

        @Override
        public void testDisabled(ExtensionContext context, Optional<String> reason) {}
    }
}