package com.datasoft.IgmMis.Model.ImportReport;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class OffDockDestinationOrgWiseContainerModel {
    @Id
    private String  id;
    private Integer cont_size;
    private String cont_status;
    private Integer cont_height;
    private String Organization_Name;
    private Integer off_dock_id;
    private String mlocode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getOrganization_Name() {
        return Organization_Name;
    }

    public void setOrganization_Name(String organization_Name) {
        Organization_Name = organization_Name;
    }

    public Integer getOff_dock_id() {
        return off_dock_id;
    }

    public void setOff_dock_id(Integer off_dock_id) {
        this.off_dock_id = off_dock_id;
    }

    public String getMlocode() {
        return mlocode;
    }

    public void setMlocode(String mlocode) {
        this.mlocode = mlocode;
    }
}
