package com.hybris.dao;

import com.hybris.IntegrationServiceTest;
import com.hybris.dao.model.Guest;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class GuestDaoIntegrationTest extends IntegrationServiceTest {


    private static final String ARRAN = "Arran";
    private static final String BARTISH = "Bartish";

    @Autowired
    private GuestDao guestDao;

    private Guest guest;

    private DateTime then;

    @Before
    public void setup(){
        guest = new Guest();
        guest.setFirstname(ARRAN);
        guest.setLastname(BARTISH);
        guest.setCreated(then = DateTime.now());

        guestDao.save(guest);
    }

    @Test
    public void findAll_will_return_all_guests_as_pages() throws Exception {
        List<Guest> guests = guestDao.findAll();

        assertThat(guests, hasSize(1));
        assertThat(guests, contains(guest));
    }

    @Test
    public void findByFirstnameAndLastname_will_return_arran_bartish() throws Exception {
        List<Guest> arran = guestDao.findByFirstnameAndLastname(ARRAN, BARTISH);

        assertThat(arran, hasSize(1));
        assertThat(arran, contains(guest));
    }

    @Test
    public void created_Date_Maintains_Exact_Time() throws Exception {
        Thread.sleep(SECONDS.toMillis(1));
        List<Guest> arran = guestDao.findByFirstnameAndLastname(ARRAN, BARTISH);

        assertThat(arran.get(0).getCreated(), is(then));
    }
}