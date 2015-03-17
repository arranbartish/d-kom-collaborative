package com.hybris.guestbook.page;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ViewGuestBook implements GuestBookPage {

    private static final String VIEW = "/";
    private static final String PAGE_ID = "guestbook-home";

    private static final String FIRST_NAME_PREFIX="firstname-";
    private static final String LAST_NAME_PREFIX="lastname-";

    @Override
    public String getUri() {
        return VIEW;
    }

    @Override
    public String getPageId() {
        return PAGE_ID;
    }

    public String getFirstnameId(int index){
        return String.format("%s%s", FIRST_NAME_PREFIX, index);
    }

    public String getLastnameId(int index){
        return String.format("%s%s", LAST_NAME_PREFIX, index);
    }
}
