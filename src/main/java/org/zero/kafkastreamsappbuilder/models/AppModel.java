package org.zero.kafkastreamsappbuilder.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * This class is a model for a kafka streams app.
 */
@Entity
@Table(name = "app_model", schema = "kafkastremsappbuilderdb")
public class AppModel {

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name="app_name")
    private String appName;

    @Column(name="create_date")
    private Date createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
