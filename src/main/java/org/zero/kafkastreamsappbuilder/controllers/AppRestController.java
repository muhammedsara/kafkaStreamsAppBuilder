package org.zero.kafkastreamsappbuilder.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zero.kafkastreamsappbuilder.codegen.JavaSourceCodeGenerator;
import org.zero.kafkastreamsappbuilder.codegen.models.NodeModel;
import org.zero.kafkastreamsappbuilder.exceptions.NoAppFoundException;
import org.zero.kafkastreamsappbuilder.jpa.AppRepository;
import org.zero.kafkastreamsappbuilder.jpa.OperatorRepository;
import org.zero.kafkastreamsappbuilder.models.AppModel;

import java.util.*;

@RestController
public class AppRestController {

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private OperatorRepository operatorRepository;

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


    @PostMapping("/app/generateCode")
    public String generateCode(@RequestParam("appJson") String appJson) {

        JSONObject json = new JSONObject(appJson);

        Map<String, NodeModel> idNodesMap = new HashMap<>();

        // iterate through nodes
        for (Object obj : json.getJSONArray("nodes")) {

            if (obj instanceof JSONObject) {

                JSONObject node = (JSONObject) obj;

                int operatorId = node.getInt("operatorId");
                String id = node.getString("id");
                JSONObject props = node.getJSONObject("properties");

                NodeModel model = new NodeModel("node_"+id);
                model.setOperator(operatorRepository.findById(operatorId).get());

                for (String propName : props.keySet()) {
                    model.addProperty(propName, props.getString(propName));
                }
                idNodesMap.put(id, model);


            }

        }

        // iterate through edges
        for (Object obj : json.getJSONArray("edges")) {

            if (obj instanceof JSONObject) {

                JSONObject node = (JSONObject) obj;
                String sourceId = node.getString("source");
                String targetId = node.getString("target");

                idNodesMap.get(targetId).setParent(idNodesMap.get(sourceId));

            }

        }
        return JavaSourceCodeGenerator.getSourceCodeFromNodeMap(idNodesMap);
    }
}
