package org.zero.kafkastreamsappbuilder.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * This class represents an operation over a stream object.
 */
@Entity
@Table(name = "operator_model", schema = "kafkastremsappbuilderdb")
public class OperatorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "source_type_id")
    private TypeModel sourceType;
    @ManyToOne
    @JoinColumn(name = "return_type_id")
    private TypeModel returnType;
    @OneToMany(mappedBy = "operator")
    private Set<PropertyModel> properties;
    @Column(name = "template")
    private String template;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeModel getSourceType() {
        return sourceType;
    }

    public void setSourceType(TypeModel sourceType) {
        this.sourceType = sourceType;
    }

    public TypeModel getReturnType() {
        return returnType;
    }

    public void setReturnType(TypeModel returnType) {
        this.returnType = returnType;
    }

    public Set<PropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(Set<PropertyModel> properties) {
        this.properties = properties;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
