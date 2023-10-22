package com.datasoft.IgmMis.Model.DGInformation;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;

@Entity


@Getter
@Setter
@NoArgsConstructor
public class DgManifest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String Organization_Name;
    private String mlocode;
    private String Line_No;
    private String BL_No;

    private String Pack_Number;
    private String Pack_Description;
    private String Pack_Marks_Number;
    private String Description_of_Goods;
    private String Date_of_Entry_of_Goods;
    private String net_weight;
    private String weight;
    private Long manifestId;

    ArrayList<DgManifestReport> dgManifestReports;



    private String ConsigneeDesc;
    private String NotifyDesc;
    private String Bill_of_Entry_No;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date Bill_of_Entry_Date;
    private String No_of_Pack_Delivered;
    private String No_of_Pack_Discharged;
    private String Remarks;
    private String imco;
    private String un;
    private String notify_name;
    private String consignee_name;
    private String Address_1;



    public ArrayList<DgManifestReport> getDgManifestReports() {
        return dgManifestReports;
    }

    public void setDgManifestReports(ArrayList<DgManifestReport> dgManifestReports) {
        this.dgManifestReports = dgManifestReports;
    }




    public String getNotify_name() {
        return notify_name;
    }

    public void setNotify_name(String notify_name) {
        this.notify_name = notify_name;
    }

    public String getConsignee_name() {
        return consignee_name;
    }

    public void setConsignee_name(String consignee_name) {
        this.consignee_name = consignee_name;
    }

    public String getAddress_1() {
        return Address_1;
    }

    public void setAddress_1(String address_1) {
        Address_1 = address_1;
    }

    public Long getManifestId() {
        return manifestId;
    }

    public void setManifestId(Long manifestId) {
        this.manifestId = manifestId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganization_Name() {
        return Organization_Name;
    }

    public void setOrganization_Name(String organization_Name) {
        Organization_Name = organization_Name;
    }

    public String getMlocode() {
        return mlocode;
    }

    public void setMlocode(String mlocode) {
        this.mlocode = mlocode;
    }

    public String getLine_No() {
        return Line_No;
    }

    public void setLine_No(String line_No) {
        Line_No = line_No;
    }

    public String getBL_No() {
        return BL_No;
    }

    public void setBL_No(String BL_No) {
        this.BL_No = BL_No;
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

    public String getDate_of_Entry_of_Goods() {
        return Date_of_Entry_of_Goods;
    }

    public void setDate_of_Entry_of_Goods(String date_of_Entry_of_Goods) {
        Date_of_Entry_of_Goods = date_of_Entry_of_Goods;
    }







    public String getConsigneeDesc() {
        return ConsigneeDesc;
    }

    public void setConsigneeDesc(String consigneeDesc) {
        ConsigneeDesc = consigneeDesc;
    }

    public String getNotifyDesc() {
        return NotifyDesc;
    }

    public void setNotifyDesc(String notifyDesc) {
        NotifyDesc = notifyDesc;
    }

    public String getBill_of_Entry_No() {
        return Bill_of_Entry_No;
    }

    public void setBill_of_Entry_No(String bill_of_Entry_No) {
        Bill_of_Entry_No = bill_of_Entry_No;
    }

    public Date getBill_of_Entry_Date() {
        return Bill_of_Entry_Date;
    }

    public void setBill_of_Entry_Date(Date bill_of_Entry_Date) {
        Bill_of_Entry_Date = bill_of_Entry_Date;
    }

    public String getNo_of_Pack_Delivered() {
        return No_of_Pack_Delivered;
    }

    public void setNo_of_Pack_Delivered(String no_of_Pack_Delivered) {
        No_of_Pack_Delivered = no_of_Pack_Delivered;
    }

    public String getNo_of_Pack_Discharged() {
        return No_of_Pack_Discharged;
    }

    public void setNo_of_Pack_Discharged(String no_of_Pack_Discharged) {
        No_of_Pack_Discharged = no_of_Pack_Discharged;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getImco() {
        return imco;
    }

    public void setImco(String imco) {
        this.imco = imco;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public String getNet_weight() {
        return net_weight;
    }

    public void setNet_weight(String net_weight) {
        this.net_weight = net_weight;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
