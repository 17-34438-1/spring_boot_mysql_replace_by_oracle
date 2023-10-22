package com.datasoft.IgmMis.Model.IgmOperation;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

public class ConvertIgmModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private String id;
    private Integer ids;

    private List igm;
    private String xmlData;
    private String update1;
    private String update2;
    private String update3;
    private String update4;
    private String Vessel_Name;
    private String vessel;
    private String Import_Rotation_No;
    private String BL_No;
    private String igm_master_id;
    private String Rotation_no;
    private String igm_sub_detail_id;
    private String igm_detail_id;

    private String cont_number;
    private String cont_gross_weight;
    private String cont_status;
    private String cont_iso_type;
    private String commudity_code;
    private String off_dock_id;
    private String cont_seal_number;


    private String Pack_Number;
    private String Pack_Description;

    private  String Pack_Marks_Number;
    private String Description_of_Goods;

    private Integer weight;

    private  String weight_unit;
    private String IGM_id;
    private String mlocode;
    private String ConsigneeDesc;
    private String Submitee_Org_Id;
    private String type_of_igm;

    private String Voy_No;
    private String Port_of_Shipment;
    private String Port_Ship_ID;
    public String filename;

    private String Message;
    private String msg;

    private String Total_number_of_containers;
    private Integer Total_number_of_bols;
    private String rtnValue;

    public String getXmlData() {
        return xmlData;
    }

    public void setXmlData(String xmlData) {
        this.xmlData = xmlData;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getCont_number() {
        return cont_number;
    }

    public void setCont_number(String cont_number) {
        this.cont_number = cont_number;
    }

    public String getCont_gross_weight() {
        return cont_gross_weight;
    }

    public void setCont_gross_weight(String cont_gross_weight) {
        this.cont_gross_weight = cont_gross_weight;
    }

    public String getCont_status() {
        return cont_status;
    }

    public void setCont_status(String cont_status) {
        this.cont_status = cont_status;
    }

    public String getCont_iso_type() {
        return cont_iso_type;
    }

    public void setCont_iso_type(String cont_iso_type) {
        this.cont_iso_type = cont_iso_type;
    }

    public String getCommudity_code() {
        return commudity_code;
    }

    public void setCommudity_code(String commudity_code) {
        this.commudity_code = commudity_code;
    }

    public String getOff_dock_id() {
        return off_dock_id;
    }

    public void setOff_dock_id(String off_dock_id) {
        this.off_dock_id = off_dock_id;
    }

    public String getCont_seal_number() {
        return cont_seal_number;
    }

    public void setCont_seal_number(String cont_seal_number) {
        this.cont_seal_number = cont_seal_number;
    }

    public List getIgm() {
        return igm;
    }

    public void setIgm(List igm) {
        this.igm = igm;
    }

    public Integer getIds() {
        return ids;
    }

    public void setIds(Integer ids) {
        this.ids = ids;
    }

    public String getVoy_No() {
        return Voy_No;
    }

    public void setVoy_No(String voy_No) {
        Voy_No = voy_No;
    }

    public String getPort_of_Shipment() {
        return Port_of_Shipment;
    }

    public void setPort_of_Shipment(String port_of_Shipment) {
        Port_of_Shipment = port_of_Shipment;
    }

    public String getPort_Ship_ID() {
        return Port_Ship_ID;
    }

    public void setPort_Ship_ID(String port_Ship_ID) {
        Port_Ship_ID = port_Ship_ID;
    }



    public String getPack_Number() {
        return Pack_Number;
    }

    public void setPack_Number(String pack_Number) {
        Pack_Number = pack_Number;
    }

    public String getPack_Description() {
        return Pack_Description;
    }

    public void setPack_Description(String pack_Description) {
        Pack_Description = pack_Description;
    }

    public String getPack_Marks_Number() {
        return Pack_Marks_Number;
    }

    public void setPack_Marks_Number(String pack_Marks_Number) {
        Pack_Marks_Number = pack_Marks_Number;
    }

    public String getDescription_of_Goods() {
        return Description_of_Goods;
    }

    public void setDescription_of_Goods(String description_of_Goods) {
        Description_of_Goods = description_of_Goods;
    }



    public String getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public String getIGM_id() {
        return IGM_id;
    }

    public void setIGM_id(String IGM_id) {
        this.IGM_id = IGM_id;
    }

    public String getMlocode() {
        return mlocode;
    }

    public void setMlocode(String mlocode) {
        this.mlocode = mlocode;
    }

    public String getConsigneeDesc() {
        return ConsigneeDesc;
    }

    public void setConsigneeDesc(String consigneeDesc) {
        ConsigneeDesc = consigneeDesc;
    }

    public String getSubmitee_Org_Id() {
        return Submitee_Org_Id;
    }

    public void setSubmitee_Org_Id(String submitee_Org_Id) {
        Submitee_Org_Id = submitee_Org_Id;
    }

    public String getType_of_igm() {
        return type_of_igm;
    }

    public void setType_of_igm(String type_of_igm) {
        this.type_of_igm = type_of_igm;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getIgm_master_id() {
        return igm_master_id;
    }

    public void setIgm_master_id(String igm_master_id) {
        this.igm_master_id = igm_master_id;
    }

    public String getRotation_no() {
        return Rotation_no;
    }

    public void setRotation_no(String rotation_no) {
        Rotation_no = rotation_no;
    }

    public String getBL_No() {
        return BL_No;
    }

    public void setBL_No(String BL_No) {
        this.BL_No = BL_No;
    }

    public String getVessel_Name() {
        return Vessel_Name;
    }

    public void setVessel_Name(String vessel_Name) {
        Vessel_Name = vessel_Name;
    }

    public String getImport_Rotation_No() {
        return Import_Rotation_No;
    }

    public void setImport_Rotation_No(String import_Rotation_No) {
        Import_Rotation_No = import_Rotation_No;
    }

    public String getUpdate1() {
        return update1;
    }

    public void setUpdate1(String update1) {
        this.update1 = update1;
    }

    public String getUpdate2() {
        return update2;
    }

    public void setUpdate2(String update2) {
        this.update2 = update2;
    }

    public String getUpdate3() {
        return update3;
    }

    public void setUpdate3(String update3) {
        this.update3 = update3;
    }

    public String getUpdate4() {
        return update4;
    }

    public void setUpdate4(String update4) {
        this.update4 = update4;
    }

    public String getIgm_sub_detail_id() {
        return igm_sub_detail_id;
    }

    public void setIgm_sub_detail_id(String igm_sub_detail_id) {
        this.igm_sub_detail_id = igm_sub_detail_id;
    }



    public String getIgm_detail_id() {
        return igm_detail_id;
    }

    public void setIgm_detail_id(String igm_detail_id) {
        this.igm_detail_id = igm_detail_id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTotal_number_of_containers() {
        return Total_number_of_containers;
    }

    public void setTotal_number_of_containers(String total_number_of_containers) {
        Total_number_of_containers = total_number_of_containers;
    }

    public Integer getTotal_number_of_bols() {
        return Total_number_of_bols;
    }

    public void setTotal_number_of_bols(Integer total_number_of_bols) {
        Total_number_of_bols = total_number_of_bols;
    }

    public String getRtnValue() {
        return rtnValue;
    }

    public void setRtnValue(String rtnValue) {
        this.rtnValue = rtnValue;
    }
}
