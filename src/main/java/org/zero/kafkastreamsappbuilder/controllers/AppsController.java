package org.zero.kafkastreamsappbuilder.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class AppsController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Map<String, Object> model){
        return "fragments/index";
    }

    @RequestMapping(value = "/app/create", method = RequestMethod.GET)
    public String newApp(Map<String, Object> model){
        return "fragments/create_app";
    }


}
