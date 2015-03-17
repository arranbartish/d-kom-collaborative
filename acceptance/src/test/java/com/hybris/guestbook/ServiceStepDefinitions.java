package com.hybris.guestbook;

import com.hybris.dao.GuestDao;
import com.hybris.dao.model.Guest;
import com.hybris.service.*;
import com.hybris.service.dto.GuestDto;
import com.hybris.service.exception.FailureException;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.collections.CollectionUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

import static com.hybris.guestbook.CucumberHooks.deleteAll;
import static com.hybris.guestbook.SpringWirer.getBean;
import static com.hybris.guestbook.SpringWirer.getBeansOfType;
import static com.hybris.service.dto.GuestDto.fromName;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;


public class ServiceStepDefinitions {
    private static final long EMPTY = 0L;

    @Given("^that I have access to the guest service$")
    public void I_have_access_to_the_guest_service() throws Throwable {
        GuestService guestService = getBean(GuestService.class);

        assertThat(guestService, is(notNullValue()));
    }

    @When("^(.*) (.*) is saved$")
    public void guest_is_saved(String firstname, String lastname) throws Throwable {
        getBean(GuestService.class).signGuest(fromName(firstname, lastname));
    }

    @Then("^(.*) (.*) can be loaded from the database$")
    public void guest_can_be_loaded_from_the_database(final String firstname, final String lastname) throws Throwable {
        ClosureService closureService = getBean(ClosureService.class);

        Closure findGuestClosure = new Closure() {
            @Override
            public void execute() throws Exception {
                GuestDao guestDao = getBean(GuestDao.class);
                List<Guest> guestList = guestDao.findByFirstnameAndLastname(firstname, lastname);
                assertThat( guestList, is( not( empty() ) ) );
            }
        };
        closureService.execute(findGuestClosure);

    }

    @Then("^(.*) (.*) will be in the guest book$")
    public void guest_will_be_in_the_guest_book(final String firstname, final String lastname) throws Throwable {
        final ClosureService closureService = getBean(ClosureService.class);

        final ValueReturningClosure<GuestDto> closure = new ValueReturningClosure<GuestDto>() {
            @Override
            public GuestDto execute() {
                GuestDao guestDao = getBean(GuestDao.class);
                List<Guest> guestList = guestDao.findByFirstnameAndLastname(firstname, lastname);
                if ( CollectionUtils.isNotEmpty(guestList) ) {
                    assertThat(guestList, hasSize(1));
                    return new GuestDto(guestList.get(0));
                } else {
                    return null;
                }
            }
        };

        ConditionChecker waitForUser = new AbstractConditionChecker(String.format("waitFor-%s-%s-toBeInDatabase",firstname,lastname)) {

            @Override
            public boolean isConditionPassed() {
                try {
                    GuestDto guest = closureService.execute(closure);
                    if(guest == null) {
                        return false;
                    }
                    assertThat(guest.getFirstname(), CoreMatchers.is(firstname));
                    assertThat(guest.getLastname(), CoreMatchers.is(lastname));
                } catch (Exception e) {
                    throw new FailureException("Unable to wait for user");
                }
                return true;
            }

            @Override
            public String getErrorMessage() {
                return String.format("Guest %s %s never made it into guestbook",firstname, lastname);
            }

            @Override
            public String getContext() {
                return "condition checker: " + getName();
            }
        };
        closureService.waitForCondition(waitForUser);
    }

    @Given("^(.*) (.*) has been saved$")
    public void guest_has_been_saved(String firstname, String lastname) throws Throwable {
        guest_is_saved(firstname, lastname);
    }

    @When("^I do a database clean$")
    public void I_do_a_database_clean() throws Throwable {
        deleteAll();
    }

    @Then("^the database will be empty$")
    public void the_database_will_be_empty() throws Throwable {
        ClosureService closureService = getBean(ClosureService.class);
        Map<String, CrudRepository> crudRepositories = getBeansOfType(CrudRepository.class);

        for (final Map.Entry<String, CrudRepository> repository : crudRepositories.entrySet()) {
            Closure checkEmpty = new Closure() {
                @Override
                public void execute() throws Exception {
                    long entityCount = repository.getValue().count();
                    String message = format("%s must be empty but is %s", repository.getKey(), entityCount);
                    assertThat(message, entityCount, is(EMPTY));
                }
            };
            closureService.execute(checkEmpty);
        }

    }


    @And("^(\\d+)(?:st|nd|rd|th) I sign (.*) (.*)$")
    public void sign_with_firstname_and_lastname(int row, String firstname, String lastname) throws Throwable {
        GuestService guestService = getBean(GuestService.class);
        guestService.signGuest(fromName(firstname, lastname));
        List<GuestDto> allGuests = guestService.getAllGuests();
        assertThat(allGuests.get(row-1).getFirstname(), CoreMatchers.is(firstname));
        assertThat(allGuests.get(row-1).getLastname(), CoreMatchers.is(lastname));
    }
}
