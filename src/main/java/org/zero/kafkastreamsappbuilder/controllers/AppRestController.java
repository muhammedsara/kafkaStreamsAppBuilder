package org.zero.kafkastreamsappbuilder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zero.kafkastreamsappbuilder.exceptions.NoAppFoundException;
import org.zero.kafkastreamsappbuilder.jpa.AppRepository;
import org.zero.kafkastreamsappbuilder.models.AppModel;

import java.util.Date;
import java.util.Optional;

@RestController
public class AppRestController {

    @Autowired
    private AppRepository appRepository;

    @PostMapping("rest/savenewapp")
    public void saveNewApp(@RequestParam("appName") String appName, @RequestParam("appJson") String appJson) {
        AppModel app = new AppModel();
        app.setAppName(appName);
        app.setCreateDate(new Date());
        app.setAppJson(appJson);
        appRepository.save(app);
    }

    @PostMapping("rest/updateapp")
    public void saveExistingApp(@RequestParam("appId") Integer appId, @RequestParam("appJson") String appJson) {
        Optional<AppModel> optional = appRepository.findById(appId);
        if (optional.isPresent()) {
            AppModel app = optional.get();
            app.setAppJson(appJson);
            appRepository.save(app);
        } else {
            throw new NoAppFoundException();
        }
    }
}
