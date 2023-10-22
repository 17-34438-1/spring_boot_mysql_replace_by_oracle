package com.datasoft.IgmMis.Model.ImportReport;

public class OffDockWiseBockedContainerListModel {

    private String cont_id;
    private String offdoc_name;
    private String vsl_name;
    private String  rotation;
    private Integer size;
    private Integer height;
    private String  heightInString;
    private  String mlo_name;
    private String freight_kind;
    private String last_pos_name;
    private String time_in;
    private String time_out;
    private Integer totalDays;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getHeightInString() {
        return heightInString;
    }

    public void setHeightInString(String heightInString) {
        this.heightInString = heightInString;
    }

    public String getOffdoc_name() {
        return offdoc_name;
    }

    public void setOffdoc_name(String offdoc_name) {
        this.offdoc_name = offdoc_name;
    }

    public String getVsl_name() {
        return vsl_name;
    }

    public void setVsl_name(String vsl_name) {
        this.vsl_name = vsl_name;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }



    public String getMlo_name() {
        return mlo_name;
    }

    public void setMlo_name(String mlo_name) {
        this.mlo_name = mlo_name;
    }

    public String getFreight_kind() {
        return freight_kind;
    }

    public void setFreight_kind(String freight_kind) {
        this.freight_kind = freight_kind;
    }

    public String getLast_pos_name() {
        return last_pos_name;
    }

    public void setLast_pos_name(String last_pos_name) {
        this.last_pos_name = last_pos_name;
    }

    public String getTime_in() {
        return time_in;
    }

    public void setTime_in(String time_in) {
        this.time_in = time_in;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public String getCont_id() {
        return cont_id;
    }

    public void setCont_id(String cont_id) {
        this.cont_id = cont_id;
    }

}
