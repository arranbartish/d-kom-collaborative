package com.hybris.service;

import com.google.common.base.Function;
import com.hybris.dao.GuestDao;
import com.hybris.dao.model.Guest;
import com.hybris.service.dto.GuestDto;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.transform;

@Service
@Transactional
public class DtoMappingGuestService implements GuestService {

    private GuestDao guestDao;

    private static final GuestToDto GUEST_TO_DTO_CONVERTER = new GuestToDto();
    private static final DtoToGuest DTO_TO_GUEST_CONVERTER = new DtoToGuest();


    @Autowired
    public DtoMappingGuestService(GuestDao guestDao) {
        this.guestDao = guestDao;
    }

    public List<GuestDto> getAllGuests() {

        return transform(guestDao.findAllByOrderByCreatedAsc(), GUEST_TO_DTO_CONVERTER);
    }

    @Override
    public GuestDto signGuest(GuestDto guest) {
        Guest newGuest = DTO_TO_GUEST_CONVERTER.apply(guest);
        newGuest.setCreated(DateTime.now());
        Guest savedGuest = guestDao.save(newGuest);
        return GUEST_TO_DTO_CONVERTER.apply(savedGuest);
    }


    private static class GuestToDto implements Function<Guest, GuestDto> {
        @Override
        public GuestDto apply(final Guest guest) {
            return new GuestDto(guest);
        }
    }

    private static class DtoToGuest implements Function<GuestDto, Guest> {
        @Override
        public Guest apply(final GuestDto guest) {
            Guest guestEntity = new Guest();
            guestEntity.setFirstname(guest.getFirstname());
            guestEntity.setLastname(guest.getLastname());
            guestEntity.setCreated(guest.getCreated());
            return guestEntity;
        }
    }
}
