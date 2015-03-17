package com.hybris.service;

import com.hybris.dao.GuestDao;
import com.hybris.dao.model.Guest;
import com.hybris.service.dto.GuestDto;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.hybris.service.dto.GuestDto.fromName;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DtoMappingGuestServiceTest {

    private static final String ARRAN = "Arran";
    private static final String BARTISH = "Bartish";

    private static GuestDto ARRAN_BARTISH = fromName(ARRAN, BARTISH);

    @Mock
    private GuestDao guestDao;

    @InjectMocks
    private DtoMappingGuestService guestService;

    @Captor
    private ArgumentCaptor<Guest> guestCaptor;

    private Guest guest;

    @Before
    public void setup() {
        guest = new Guest();
        guest.setLastname(BARTISH);
        guest.setFirstname(ARRAN);

    }

    @Test
    public void getAllGuests_will_convert_all_guests_to_dtos(){
        when( guestDao.findAllByOrderByCreatedAsc() ).thenReturn( newArrayList(guest) );

        List<GuestDto> allGuests = guestService.getAllGuests();

        assertThat(allGuests, Matchers.hasSize(1));
        GuestDto guestDto = allGuests.get(0);
        assertThat(guestDto.getFirstname(), is(ARRAN));
        assertThat(guestDto.getLastname(), is(BARTISH));
    }

    @Test
    public void signGuest_will_store_the_guest(){
        Guest mockGuest = new Guest();
        mockGuest.setFirstname(ARRAN);
        mockGuest.setLastname(BARTISH);
        DateTime created = DateTime.now();
        mockGuest.setCreated(created);

        when(guestDao.save(any(Guest.class))).thenReturn(mockGuest);
        GuestDto guestDto = guestService.signGuest(ARRAN_BARTISH);

        verify(guestDao).save(guestCaptor.capture());
        Guest guest = guestCaptor.getValue();

        assertThat(guest.getFirstname(), is(ARRAN));
        assertThat(guest.getLastname(), is(BARTISH));
        assertThat(guestDto.getFirstname(), is(ARRAN));
        assertThat(guestDto.getLastname(), is(BARTISH));
        assertThat(guestDto.getCreated(), is(created));
    }
}