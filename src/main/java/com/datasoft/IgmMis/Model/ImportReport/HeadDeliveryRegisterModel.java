package com.datasoft.IgmMis.Model.ImportReport;

import java.util.List;

public class HeadDeliveryRegisterModel {
    private String mfdch_value;
    private String mfdch_desc;
    private String Block_No;
    private String cf_name;
    private String cont_no;
    private Integer size;
    private String v_name;
    private String rot_no;
    private String bl_no;
    private String containerNoAndSize;
    private String stc;
    private String weight;
    private String reg_no;
    private String reg_date;
    private String beNo;
    private String beDate;
    private String truck_id;
    private float actual_delv_pack;
    private String Pack_Unit;
    private List truckInfo;
    private  List containerList;

    public String getMfdch_value() {
        return mfdch_value;
    }

    public void setMfdch_value(String mfdch_value) {
        this.mfdch_value = mfdch_value;
    }

    public String getMfdch_desc() {
        return mfdch_desc;
    }

    public void setMfdch_desc(String mfdch_desc) {
        this.mfdch_desc = mfdch_desc;
    }

    public String getBlock_No() {
        return Block_No;
    }

    public void setBlock_No(String block_No) {
        Block_No = block_No;
    }

    public String getCf_name() {
        return cf_name;
    }

    public void setCf_name(String cf_name) {
        this.cf_name = cf_name;
    }

    public String getCont_no() {
        return cont_no;
    }

    public void setCont_no(String cont_no) {
        this.cont_no = cont_no;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    public String getRot_no() {
        return rot_no;
    }

    public void setRot_no(String rot_no) {
        this.rot_no = rot_no;
    }

    public String getBl_no() {
        return bl_no;
    }

    public void setBl_no(String bl_no) {
        this.bl_no = bl_no;
    }

    public String getStc() {
        return stc;
    }

    public void setStc(String stc) {
        this.stc = stc;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getBeNo() {
        return beNo;
    }

    public void setBeNo(String beNo) {
        this.beNo = beNo;
    }

    public String getBeDate() {
        return beDate;
    }

    public void setBeDate(String beDate) {
        this.beDate = beDate;
    }

    public String getContainerNoAndSize() {
        return containerNoAndSize;
    }

    public void setContainerNoAndSize(String containerNoAndSize) {
        this.containerNoAndSize = containerNoAndSize;
    }

    public String getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.truck_id = truck_id;
    }

    public float getActual_delv_pack() {
        return actual_delv_pack;
    }

    public void setActual_delv_pack(float actual_delv_pack) {
        this.actual_delv_pack = actual_delv_pack;
    }

    public String getPack_Unit() {
        return Pack_Unit;
    }

    public void setPack_Unit(String pack_Unit) {
        Pack_Unit = pack_Unit;
    }

    public List getTruckInfo() {
        return truckInfo;
    }

    public void setTruckInfo(List truckInfo) {
        this.truckInfo = truckInfo;
    }

    public List getContainerList() {
        return containerList;
    }

    public void setContainerList(List containerList) {
        this.containerList = containerList;
    }
}
