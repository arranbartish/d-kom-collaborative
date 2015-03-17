package com.hybris.guestbook;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format={"pretty",
        "html:target/test-report",
        "json:target/test-report.json",
        "junit:target/test-report.xml"})
public class CucumberIntegrationTest {

}
