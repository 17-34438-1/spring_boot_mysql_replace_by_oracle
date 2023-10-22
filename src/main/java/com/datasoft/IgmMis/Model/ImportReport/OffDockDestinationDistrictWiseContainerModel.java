package com.datasoft.IgmMis.Model.ImportReport;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OffDockDestinationDistrictWiseContainerModel {
    @Id
    private String id;
    private Integer cont_size;
    private String cont_status;
    private Integer cont_height;
    private String test;
    private String mlocode;
    private String dist;

    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public Integer getCont_size() {
        return cont_size;
    }

    public void setCont_size(Integer cont_size) {
        this.cont_size = cont_size;
    }

    public String getCont_status() {
        return cont_status;
    }

    public void setCont_status(String cont_status) {
        this.cont_status = cont_status;
    }

    public Integer getCont_height() {
        return cont_height;
    }

    public void setCont_height(Integer cont_height) {
        this.cont_height = cont_height;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getMlocode() {
        return mlocode;
    }

    public void setMlocode(String mlocode) {
        this.mlocode = mlocode;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }


}
