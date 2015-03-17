package com.hybris.guestbook;

import com.hybris.guestbook.page.SignGuestBook;
import com.hybris.guestbook.page.ViewGuestBook;
import com.hybris.guestbook.selenium.SeleniumFacade;
import com.hybris.service.AbstractConditionChecker;
import com.hybris.service.ClosureService;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;


import static com.hybris.guestbook.SpringWirer.getBean;
import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class SelemiumStepDefinitions {

    private static final Logger LOG = getLogger(SelemiumStepDefinitions.class);

    private void gotoGuestBookPage() {
        ClosureService closureService = getBean(ClosureService.class);
        final SeleniumFacade facade = getBean(SeleniumFacade.class);
        final SignGuestBook signGuestBook = getBean(SignGuestBook.class);
        facade.goToUrl(signGuestBook);

        closureService.waitForCondition(new AbstractConditionChecker("wait for page to load") {
            @Override
            public boolean isConditionPassed() {
                try {
                    if(StringUtils.equalsIgnoreCase(facade.getPageId(), signGuestBook.getPageId())){
                        return true;
                    }
                } catch (Exception e){
                    LOG.debug("Could not find element for page id", e);
                }

                return false;
            }

            @Override
            public String getErrorMessage() {
                return "Could not find element for page id";
            }
        });


        assertThat(facade.getPageId(), is(signGuestBook.getPageId()));
    }

    @Given("^a guest can sign the guest book$")
    public void a_guest_can_sign_the_guest_book() throws Throwable {
        gotoGuestBookPage();
    }

    @When("^(.*) (.*) signs the guest book$")
    public void guest_signs_the_guest_book(String firstname, String lastname) throws Throwable {
        SeleniumFacade facade = getBean(SeleniumFacade.class);
        SignGuestBook signGuestBook = getBean(SignGuestBook.class);
        facade.fillInTextFieldById(signGuestBook.getFirstname(), firstname);
        facade.fillInTextFieldById(signGuestBook.getLastname(), lastname);
        facade.clickElement(signGuestBook.getSignGuestbook());
    }

    @When("^I view the guestbook$")
    public void I_view_the_guestbook() throws Throwable {
        gotoGuestBookPage();
    }

    @Then("^row (\\d+) will have (.*) (.*)$")
    public void I_will_save_a_guest(int row, String firstname, String lastname) throws Throwable {
        SeleniumFacade facade = getBean(SeleniumFacade.class);
        ViewGuestBook viewGuestBook = getBean(ViewGuestBook.class);
        String firstnameValue = facade.getGuestBookTableElementById(viewGuestBook.getFirstnameId(row-1));
        String lastnameValue = facade.getGuestBookTableElementById(viewGuestBook.getLastnameId(row-1));
        assertThat(firstnameValue, is(firstname));
        assertThat(lastnameValue, is(lastname));
    }
}
