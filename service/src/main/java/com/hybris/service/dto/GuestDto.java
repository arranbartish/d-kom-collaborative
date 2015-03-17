package com.hybris.service.dto;

import com.hybris.dao.model.Guest;
import com.hybris.util.ReflectiveBean;
import org.joda.time.DateTime;

import java.util.Date;

public class GuestDto extends ReflectiveBean {

    private String firstname;
    private String lastname;
    private DateTime created;

    private GuestDto() {
    }

    public GuestDto(Guest guest) {
        setFirstname(guest.getFirstname());
        setLastname(guest.getLastname());
        setCreated(guest.getCreated());
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public static GuestDto fromName(String firstname, String lastname) {
        GuestDto guestDto = new GuestDto();
        guestDto.setFirstname(firstname);
        guestDto.setLastname(lastname);
        return guestDto;
    }
}
