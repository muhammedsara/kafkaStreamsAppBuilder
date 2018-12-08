package org.zero.kafkastreamsappbuilder.codegen.models;

import org.zero.kafkastreamsappbuilder.models.OperatorModel;

import java.util.HashMap;
import java.util.Map;

public class NodeModel {

    private NodeModel parent;
    private OperatorModel operator;
    private Map<String,String> properties = new HashMap<>();

    public void setParent(NodeModel parent) {
        this.parent = parent;
    }

    public void addProperty(String propName, String value) {
        this.properties.put(propName,value);
    }

    public Map<String, String> getProperties() {
        return properties;
    }


    public void setOperator(OperatorModel operator) {
        this.operator = operator;
    }
}
