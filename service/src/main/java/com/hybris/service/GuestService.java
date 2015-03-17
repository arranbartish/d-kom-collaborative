package com.hybris.service;

import com.hybris.service.dto.GuestDto;
import java.util.List;

public interface GuestService {


    public List<GuestDto> getAllGuests();

    GuestDto signGuest(GuestDto guest);
}
