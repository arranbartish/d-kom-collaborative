package com.hybris.web.controller;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class HealthController {

    @RequestMapping(value = "/health", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public Map<String, String> health(){
        Map<String, String> health = Maps.newHashMap();
        health.put("status", "OK");
        return health;
    }

}
