package com.hybris.web.controller;

import com.google.common.collect.Lists;
import com.hybris.service.GuestService;
import com.hybris.service.dto.GuestDto;
import com.hybris.web.converter.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;

import static com.google.common.collect.Lists.newArrayList;
import static com.hybris.service.dto.GuestDto.fromName;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class GuestControllerTest {

    @Mock
    private GuestService guestService;

    @InjectMocks
    private GuestController guestController;

    private ObjectMapper customObjectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(guestController).build();


        when(guestService.getAllGuests()).thenReturn(newArrayList(fromName("Arran", "Bartish")));
    }


    @Test
    public void guests_page_will_return_OK() throws Exception {
        mockMvc.perform(get("/guests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json("[{firstname: Arran, lastname: Bartish}]"));
    }

    @Test
    public void guest_sign_page_will_return_OK() throws Exception {
        GuestDto guestDto = fromName("Arran", "Bartish");
        GuestDto mockGuestDto = fromName("Arran", "Bartish");
        mockGuestDto.setCreated(DateTime.now());

        Mockito.when(guestService.signGuest(Mockito.any(GuestDto.class))).thenReturn(mockGuestDto);

        mockMvc.perform(post("/guest/sign")
                .contentType(APPLICATION_JSON_UTF8)
                .content(customObjectMapper.writeValueAsString(guestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json("{firstname: Arran, lastname: Bartish}"));

    }
}