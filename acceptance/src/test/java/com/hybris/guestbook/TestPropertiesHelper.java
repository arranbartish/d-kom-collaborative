package com.hybris.guestbook;

import com.google.common.primitives.Longs;
import com.hybris.service.exception.FailureException;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

import static com.hybris.guestbook.SpringWirer.getBean;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.StringUtils.defaultString;

public class TestPropertiesHelper {

    private static final Properties properties = new Properties();
    private static boolean propertiesAreRead = false;

    public static String frontendUrl(){
        return getStringProperty("frontend.url");
    }

    public static String activeWebDriver() {
        String key = "active.web.driver";
        String webDriver = getStringProperty(key);
        if (StringUtils.isEmpty(webDriver)) {
           throw new FailureException(String.format("%s is required and not defined", key ));
        }
        return webDriver;
    }

    public static boolean addFireFoxExtentions() {
        return getBooleanProperty("add.firefox.extensions");
    }

    public static boolean useExecutableDriverPath() {
        return getBooleanProperty("use.executable.driver.path");
    }

    public static String specificExecutablePath() {
        return getStringProperty("specific.executable.path");
    }

    public static String browserDownloadDirectory() {
        return getStringProperty("browser.download.directory");
    }

    public static boolean firefoxMimeTypeListToSaveToDiskWithoutAsking() {
        return getBooleanProperty("browser.download.directory");
    }

    public static boolean enableFirebugDebugTabs() {
        return getBooleanProperty("enable.firebug.debug.tabs");
    }

    public static long defaultTestWaitTime() {
        return getLongProperty("test.step.wait.time");
    }

    public static long getLongProperty(String key) {
        Long result = Longs.tryParse(getStringProperty(key));
        return result != null ? result : 0;
    }

    public static boolean getBooleanProperty(String key){
        return toBoolean(getProperty(key));
    }

    public static String getStringProperty(String key){
        return defaultString(getProperty(key), "");
    }


    private static String getProperty(String key){

        if (!propertiesAreRead) {
            Properties readProperties = getBean("testProperties");
            properties.putAll(readProperties);
            propertiesAreRead = true;
        }

        return properties.getProperty(key);
    }

    public static void putSharedProperty(String key, String value) {
        properties.put(key, value);
    }



}
