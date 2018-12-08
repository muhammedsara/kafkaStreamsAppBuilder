package org.zero.kafkastreamsappbuilder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.zero.kafkastreamsappbuilder.exceptions.NoAppFoundException;
import org.zero.kafkastreamsappbuilder.jpa.AppRepository;
import org.zero.kafkastreamsappbuilder.models.AppModel;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class AppController {

    @Autowired
    public AppRepository appRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Map<String, Object> model) {
        List<AppModel> all = appRepository.findAll();
        model.put("apps", all);
        return "fragments/index";
    }

    @RequestMapping(value = "/app/create", method = RequestMethod.GET)
    public String newApp(Map<String, Object> model) {
        return "fragments/create_app";
    }

    @RequestMapping(value = "/app/edit/{id}", method = RequestMethod.GET)
    public String editApp(Map<String, Object> model,
                          @PathVariable Integer id) {
        Optional<AppModel> app = appRepository.findById(id);
        if (app.isPresent()) {
            model.put("app", app.get());
        } else {
            throw new NoAppFoundException();
        }
        return "fragments/create_app";
    }

}
