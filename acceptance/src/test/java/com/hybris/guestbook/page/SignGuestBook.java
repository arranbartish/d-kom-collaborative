package com.hybris.guestbook.page;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SignGuestBook implements GuestBookPage {

    private static final String SIGN = "/";
    private static final String PAGE_ID = "guestbook-home";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String SIGN_GUESTBOOK = "signGuestbook";

    @Override
    public String getUri() {
        return SIGN;
    }

    @Override
    public String getPageId() {
        return PAGE_ID;
    }

    public String getFirstname() {
        return FIRSTNAME;
    }

    public String getLastname() {
        return LASTNAME;
    }

    public String getSignGuestbook() {
        return SIGN_GUESTBOOK;
    }

}
