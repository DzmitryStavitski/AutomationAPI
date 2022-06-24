package tests;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected Browser browser;

    @BeforeMethod
    public void setup() {
        browser = AqualityServices.getBrowser();
        browser.maximize();
    }

    @AfterMethod
    public void close() {
        browser.quit();
    }
}