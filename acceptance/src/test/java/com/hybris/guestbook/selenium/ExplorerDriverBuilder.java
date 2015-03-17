package com.hybris.guestbook.selenium;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.openqa.selenium.chrome.ChromeDriverService.createDefaultService;


public class ExplorerDriverBuilder {


    public RemoteWebDriver build() {

        ChromeDriverService defaultService = createDefaultService();

        return new ChromeDriver(defaultService);
    }

}
