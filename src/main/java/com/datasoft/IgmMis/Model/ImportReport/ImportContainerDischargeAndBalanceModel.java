package com.datasoft.IgmMis.Model.ImportReport;

public class ImportContainerDischargeAndBalanceModel {
    String id;
    String fcy_time_in;
    String location;
    String sealno;
    String iso;
    String mlo;
    String freight_kind;
    float weight;
    String short_name;
    String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFcy_time_in() {
        return fcy_time_in;
    }

    public void setFcy_time_in(String fcy_time_in) {
        this.fcy_time_in = fcy_time_in;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSealno() {
        return sealno;
    }

    public void setSealno(String sealno) {
        this.sealno = sealno;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getMlo() {
        return mlo;
    }

    public void setMlo(String mlo) {
        this.mlo = mlo;
    }

    public String getFreight_kind() {
        return freight_kind;
    }

    public void setFreight_kind(String freight_kind) {
        this.freight_kind = freight_kind;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
