package com.hybris.web.controller;

import com.hybris.service.GuestService;
import com.hybris.service.dto.GuestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Controller
public class GuestController {


    private static final Logger LOG = LoggerFactory.getLogger(GuestController.class);
    private GuestService guestService;

    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @RequestMapping(value = "/guests", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    @ResponseStatus(OK)
    public List<GuestDto> getAllGuests(){
        return guestService.getAllGuests();
    }

    @RequestMapping(value = "guest/sign", method = RequestMethod.POST, consumes = {"application/json; charset=UTF-8"})
    @ResponseBody
    @ResponseStatus(OK)
    public GuestDto signGuestbook(@RequestBody GuestDto guest){
        return guestService.signGuest(guest);
    }
}
