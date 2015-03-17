package com.hybris.guestbook.selenium;

import com.google.common.collect.Maps;
import com.hybris.guestbook.TestPropertiesHelper;
import com.hybris.service.ClosureService;
import com.hybris.service.exception.FailureException;
import com.hybris.guestbook.page.GuestBookPage;
import com.hybris.service.AbstractConditionChecker;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hybris.guestbook.SpringWirer.getBean;
import static com.hybris.guestbook.TestPropertiesHelper.activeWebDriver;
import static com.hybris.guestbook.TestPropertiesHelper.frontendUrl;
import static com.hybris.guestbook.selenium.SeleniumFacade.WebDriverType.fromName;
import static java.lang.String.format;
import static org.apache.commons.collections.MapUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SeleniumFacade {
    private static final Logger LOG = getLogger(SeleniumFacade.class);

    private static final String MAIN_WINDOW_HANDLE_KEY = "MAIN_WINDOW_HANDLE_KEY";
    private static final String ACTIVE_DRIVER = "activeDriver";
    private static final int BROWSER_WINDOW_WIDTH = 1200;

    private Map<String, RemoteWebDriver> driverMap;

    public void startSelenium() throws Exception {
        try {
            getDriverMap();
            addDriverIfNotCreatedAndSaveWindowHandle();
            setImplicitWaitTimeMsInternalNoClosure(TestPropertiesHelper.defaultTestWaitTime());
        } catch(Exception e) {
            LOG.info("Failed to start selenium", e);
            throw e;
        }
    }

    private RemoteWebDriver getActiveDriver() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(TestPropertiesHelper.defaultTestWaitTime()));
        } catch (Exception e) {
            // don't care
            LOG.debug("Couldn't sleep", e);
        }
        getDriverMap();
        return this.driverMap.get(ACTIVE_DRIVER);
    }

    public void goToUrl(GuestBookPage page) {
        getActiveDriver().get(getUrl(page.getUri()));
    }

    private Map<String, RemoteWebDriver> getDriverMap() {
        if (driverMap == null) {
            driverMap = getBean("webDrivers");
        }
        return driverMap;
    }

    private String getUrl(String uri) {
        String url = frontendUrl();

        return format("%s/%s", removeEnd(url, "/"), removeStart(uri, "/"));
    }


    public String getPageId()
    {
        return getElementAttributeByXpath("//page-id", "id");
    }

    public void fillInTextFieldById(String id, String value) {
        getActiveDriver().findElement(By.id(id)).clear();
        getActiveDriver().findElement(By.id(id)).sendKeys(value);
    }

    public void clickElement(String Id) {
        waitForElementToBeVisibleById(Id);
        getActiveDriver().findElement(By.id(Id)).click();

    }

    public void waitForElementToBeVisibleById(final String id) {
        ClosureService closureService = getBean(ClosureService.class);
        closureService.waitForCondition(new AbstractConditionChecker(format("waitForElement-%s-ToBeVisibleById", id)) {

            @Override
            public boolean isConditionPassed() {
                WebElement webElement = safeFindElement(By.id(id));
                return webElement != null && webElement.isDisplayed();
            }

            @Override
            public String getErrorMessage() {
                return "Element did not appear";
            }

            @Override
            public String getContext() {
                return "elementId: " + id;
            }
        });
    }

    private WebElement safeFindElement(By by) {
        try {
            return getActiveDriver().findElement(by);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public String getGuestBookTableElementById(String id) {
        WebElement element = getActiveDriver().findElement(By.xpath(format("//span[@data-key='%s']", id)));
        return element.getText();
    }

    private String getElementAttributeByXpath(String xpath, String attributeName) {
        return getElementAttributeByIdentifier(By.xpath(xpath), attributeName);
    }

    private String getElementAttributeByIdentifier(By identifier, String attributeName) {
        String text;
        try {
            text = getActiveDriver().findElement(identifier).getAttribute(attributeName);
        } catch (Exception ex) {
            throw new FailureException("Failed to find element", ex);
        }
        return text;
    }

    enum WebDriverType {

        FIREFOX("firefox"),
        IE("ie"),
        CHROME("chrome");

        private final String name;
        private static Map<String, WebDriverType> enums;

        WebDriverType(String name) {
            this.name = name;
        }

        String getName(){
            return name;
        }

        static WebDriverType fromName(String name){
            if (isEmpty(enums)) {
                Map<String, WebDriverType> localEnums = Maps.newHashMap();
                for (WebDriverType webDriverType : WebDriverType.values()) {
                    localEnums.put(webDriverType.getName(), webDriverType);
                }
                enums = localEnums;
            }
            WebDriverType webDriverType = enums.get(name);
            if (webDriverType == null) {
                throw new FailureException(format("%s is not a supported driver [%s]", name, enums.keySet()));
            }
            return webDriverType;
        }
    }



    public RemoteWebDriver build() {
        RemoteWebDriver webDriver = null;

        WebDriverType activeDriver = fromName(activeWebDriver());
        switch (activeDriver) {
            case CHROME:
                webDriver = new ChromeDriverBuilder().build();
                break;
            case IE:
                webDriver = new ExplorerDriverBuilder().build();
                break;
            case FIREFOX:
                webDriver = new FirefoxDriverBuilder().build();
                break;
            default:
                throw new FailureException("Driver not specified");
        }

        return webDriver;
    }

    private void addDriverIfNotCreatedAndSaveWindowHandle() {

        RemoteWebDriver remoteWebDriver = driverMap.get(ACTIVE_DRIVER);
        if (remoteWebDriver == null) {
            remoteWebDriver = build();
            resizeBrowserWindow(remoteWebDriver);
            driverMap.put(ACTIVE_DRIVER, remoteWebDriver);
        }
        TestPropertiesHelper.putSharedProperty(MAIN_WINDOW_HANDLE_KEY, remoteWebDriver.getWindowHandle());
    }

    protected void resizeBrowserWindow(RemoteWebDriver webDriver) {

        WebDriver.Options webDriverManager = webDriver.manage();
        
        WebDriver.Window window = webDriverManager.window();
        window.setSize(new Dimension(BROWSER_WINDOW_WIDTH, window.getSize().getHeight()));
    }

    protected void setImplicitWaitTimeMsInternalNoClosure(final long ms) {
        getActiveDriver().manage().timeouts().implicitlyWait(ms, TimeUnit.MILLISECONDS);
    }
}
