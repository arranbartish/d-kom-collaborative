package com.hybris.guestbook.selenium;

import com.hybris.guestbook.TestPropertiesHelper;
import com.hybris.service.exception.FailureException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static com.google.common.primitives.Ints.checkedCast;
import static com.hybris.guestbook.TestPropertiesHelper.enableFirebugDebugTabs;

public class FirefoxDriverBuilder {

    private static final long FIREFOX_STARTUP_TIMEOUT = TimeUnit.MINUTES.toMillis(2);
    private static final long FIREFOX_MAX_SCRIPT_RUN_TIME = TimeUnit.SECONDS.toSeconds(30);

    public RemoteWebDriver build() {

        FirefoxProfile firefoxProfile = new FirefoxProfile();

        configureFirefoxMaxScriptRunTime(firefoxProfile);

        configureFirefoxToSaveFilesToDiskWithoutAsking(firefoxProfile);

        /* add Firefox extensions if needed */
        if (TestPropertiesHelper.addFireFoxExtentions()) {
            addFirefoxExtensions(firefoxProfile);
        }

        final FirefoxBinary firefoxBinary = TestPropertiesHelper.useExecutableDriverPath()?
                new FirefoxBinary(FileUtils.getFile(TestPropertiesHelper.specificExecutablePath())):
                new FirefoxBinary();


        firefoxBinary.setTimeout(FIREFOX_STARTUP_TIMEOUT);

        return new FirefoxDriver(firefoxBinary, firefoxProfile);
    }

    private void configureFirefoxMaxScriptRunTime(FirefoxProfile profile) {

        profile.setPreference("dom.max_script_run_time", checkedCast(FIREFOX_MAX_SCRIPT_RUN_TIME));
        profile.setPreference("dom.max_chrome_script_run_time", checkedCast(FIREFOX_MAX_SCRIPT_RUN_TIME));
    }

    /**
     * Download certain MIME types without asking so we can assert on those from FitNesse.
     * <p>
     * Original research: http://stackoverflow.com/a/7983487/68119
     */
    private void configureFirefoxToSaveFilesToDiskWithoutAsking(FirefoxProfile profile) {

        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", TestPropertiesHelper.browserDownloadDirectory());
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                TestPropertiesHelper.firefoxMimeTypeListToSaveToDiskWithoutAsking());
    }

    /**
     * Load extensions (currently FireBug and FireCookie) .
     * <p>
     * Original research: http://stackoverflow.com/questions/3421793/how-do-i-run-firebug-within-selenium-2
     */
    private void addFirefoxExtensions(FirefoxProfile profile) {
        try {
            File extDir = new ClassPathResource("selenium/firefox/extensions").getFile();

            // get all .xpi files from extensions directory recursively
            Collection<File> extFiles = FileUtils.listFiles(extDir, new String[]{"xpi"}, true);

            for (File extFile : extFiles) {
                profile.addExtension(extFile);
            }
        } catch (IOException e) {
            throw new FailureException("Unable to load Firefox extensions", e);
        }

        /*
         * Hack to prevent FireBug from opening its Release Notes tab breaking our tests. With this property set it
         * will only open that tab when FireBug version 301 is released. :-)
         */
        profile.setPreference("extensions.firebug.currentVersion", "300");

        if (enableFirebugDebugTabs()) {
            // Firebug properties from http://getfirebug.com/wiki/index.php/Firebug_Preferences
            profile.setPreference("extensions.firebug.script.enableSites", "true");
            profile.setPreference("extensions.firebug.console.enableSites", "true");
        }
    }

}
