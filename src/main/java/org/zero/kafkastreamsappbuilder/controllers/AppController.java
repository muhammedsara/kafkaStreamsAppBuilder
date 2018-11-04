package org.zero.kafkastreamsappbuilder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.zero.kafkastreamsappbuilder.jpa.AppRepository;
import org.zero.kafkastreamsappbuilder.models.AppModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class AppController {

    @Autowired
    public AppRepository appRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Map<String, Object> model){
        List<AppModel> all = appRepository.findAll();
        model.put("apps", all);
        return "fragments/index";
    }

    @RequestMapping(value = "/app/create", method = RequestMethod.GET)
    public String newApp(Map<String, Object> model){
        return "fragments/create_app";
    }


}
