package com.hybris.guestbook.selenium;

import com.hybris.guestbook.TestPropertiesHelper;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.RemoteWebDriver;


public class ChromeDriverBuilder {


    public RemoteWebDriver build() {


        System.setProperty("webdriver.chrome.driver", TestPropertiesHelper.getStringProperty("chrome.driver.path"));

        ChromeDriverService defaultService = ChromeDriverService.createDefaultService();

        return new ChromeDriver(defaultService);
    }

}
