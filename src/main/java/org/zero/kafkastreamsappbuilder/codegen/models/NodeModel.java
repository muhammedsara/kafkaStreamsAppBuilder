package org.zero.kafkastreamsappbuilder.codegen.models;

import org.zero.kafkastreamsappbuilder.models.OperatorModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeModel {

    private NodeModel parent;
    private OperatorModel operator;
    private Map<String, String> properties = new HashMap<>();
    private List<NodeModel> children = new ArrayList<>();
    private String variableName;


    public NodeModel(String variableName) {
        this.variableName = variableName;
    }

    public OperatorModel getOperator() {
        return operator;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setParent(NodeModel parent) {
        if (parent != null) {
            parent.children.add(this);
        }
        this.parent = parent;
    }

    public NodeModel getParent() {
        return this.parent;
    }

    public void setChildren(List<NodeModel> children) {
        this.children = children;
    }

    public List<NodeModel> getChildren() {
        return children;
    }

    public void addProperty(String propName, String value) {
        this.properties.put(propName, value);
    }

    public Map<String, String> getProperties() {
        return properties;
    }


    public void setOperator(OperatorModel operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return operator.getName() + " " + variableName;
    }
}
