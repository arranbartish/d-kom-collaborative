package com.hybris.guestbook;

import com.hybris.guestbook.selenium.SeleniumFacade;
import com.hybris.service.Closure;
import com.hybris.service.ClosureService;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import java.util.Map;

import static com.hybris.guestbook.SpringWirer.getBean;
import static com.hybris.guestbook.SpringWirer.getBeansOfType;
import static com.hybris.guestbook.SpringWirer.wireSpring;

public class CucumberHooks {

    public static final Logger LOG = LoggerFactory.getLogger(CucumberHooks.class);

    @Before
    public void beforeAll() throws Exception{
        wireSpring();
        SeleniumFacade seleniumFacade = getBean(SeleniumFacade.class);
        seleniumFacade.startSelenium();
    }


    @After
    public void after() throws Exception {
        deleteAll();
    }

    public static void deleteAll() throws Exception{
        ClosureService closureService = getBean(ClosureService.class);
        Closure deleteAll = new Closure() {
            @Override
            public void execute() throws Exception {
                Map<String, CrudRepository> crudRepositories = getBeansOfType(CrudRepository.class);
                for (final CrudRepository crudRepository : crudRepositories.values()) {
                    crudRepository.deleteAll();
                }
            }
        };
        try {
            closureService.execute(deleteAll);
        } catch (Exception e) {
            LOG.debug("exception on delete", e);
        }
    }
}
