package com.datasoft.IgmMis.Model.ImportReport;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BerthOperatorReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer Serial_No;
    private String imco;
    private String un;
    private String Pack_Number;
    private String Pack_Description;
    private String depu;

    //    @Lob
    private String Pack_Marks_Number;

    private String Import_Rotation_No;
    private String Line_No;
    private String BL_No;
    private String cont_number;
    private String cont_size;
    private String cont_weight;
    private String cont_seal_number;
    private String dg;
    private String cont_status;
    private String cont_height;
    private String cont_type;
    private String mlocode;
    private String Cont_gross_weight;
    private String Organization_Name;
    private String commudity_desc;

    private String final_amendment;
    private String response_details1;
    private String firstapprovaltime;
    private String response_details2;
    private String secondapprovaltime;
    private String response_details3;
    private String thirdapprovaltime;
    private String hold_application;
    private String rejected_application;
    private String hold_date;
    private String rejected_date;
    private String type;
    private String master_Line_No;

    private String CStatus;
    private String format;



    public Integer getSerial_No() {
        return Serial_No;
    }

    public void setSerial_No(Integer serial_No) {
        Serial_No = serial_No;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDepu() {
        return depu;
    }

    public void setDepu(String depu) {
        this.depu = depu;
    }

    public String getPack_Marks_Number() {
        return Pack_Marks_Number;
    }

    public void setPack_Marks_Number(String pack_Marks_Number) {
        Pack_Marks_Number = pack_Marks_Number;
    }

    public String getImport_Rotation_No() {
        return Import_Rotation_No;
    }

    public void setImport_Rotation_No(String import_Rotation_No) {
        Import_Rotation_No = import_Rotation_No;
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

    public String getCont_number() {
        return cont_number;
    }

    public void setCont_number(String cont_number) {
        this.cont_number = cont_number;
    }

    public String getCont_size() {
        return cont_size;
    }

    public void setCont_size(String cont_size) {
        this.cont_size = cont_size;
    }

    public String getCont_weight() {
        return cont_weight;
    }

    public void setCont_weight(String cont_weight) {
        this.cont_weight = cont_weight;
    }

    public String getCont_seal_number() {
        return cont_seal_number;
    }

    public void setCont_seal_number(String cont_seal_number) {
        this.cont_seal_number = cont_seal_number;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public String getCont_status() {
        return cont_status;
    }

    public void setCont_status(String cont_status) {
        this.cont_status = cont_status;
    }

    public String getCont_height() {
        return cont_height;
    }

    public void setCont_height(String cont_height) {
        this.cont_height = cont_height;
    }

    public String getCont_type() {
        return cont_type;
    }

    public void setCont_type(String cont_type) {
        this.cont_type = cont_type;
    }

    public String getMlocode() {
        return mlocode;
    }

    public void setMlocode(String mlocode) {
        this.mlocode = mlocode;
    }

    public String getCont_gross_weight() {
        return Cont_gross_weight;
    }

    public void setCont_gross_weight(String cont_gross_weight) {
        Cont_gross_weight = cont_gross_weight;
    }

    public String getOrganization_Name() {
        return Organization_Name;
    }

    public void setOrganization_Name(String organization_Name) {
        Organization_Name = organization_Name;
    }

    public String getCommudity_desc() {
        return commudity_desc;
    }

    public void setCommudity_desc(String commudity_desc) {
        this.commudity_desc = commudity_desc;
    }

    public String getFinal_amendment() {
        return final_amendment;
    }

    public void setFinal_amendment(String final_amendment) {
        this.final_amendment = final_amendment;
    }

    public String getResponse_details1() {
        return response_details1;
    }

    public void setResponse_details1(String response_details1) {
        this.response_details1 = response_details1;
    }

    public String getFirstapprovaltime() {
        return firstapprovaltime;
    }

    public void setFirstapprovaltime(String firstapprovaltime) {
        this.firstapprovaltime = firstapprovaltime;
    }

    public String getResponse_details2() {
        return response_details2;
    }

    public void setResponse_details2(String response_details2) {
        this.response_details2 = response_details2;
    }

    public String getSecondapprovaltime() {
        return secondapprovaltime;
    }

    public void setSecondapprovaltime(String secondapprovaltime) {
        this.secondapprovaltime = secondapprovaltime;
    }

    public String getResponse_details3() {
        return response_details3;
    }

    public void setResponse_details3(String response_details3) {
        this.response_details3 = response_details3;
    }

    public String getThirdapprovaltime() {
        return thirdapprovaltime;
    }

    public void setThirdapprovaltime(String thirdapprovaltime) {
        this.thirdapprovaltime = thirdapprovaltime;
    }

    public String getHold_application() {
        return hold_application;
    }

    public void setHold_application(String hold_application) {
        this.hold_application = hold_application;
    }

    public String getRejected_application() {
        return rejected_application;
    }

    public void setRejected_application(String rejected_application) {
        this.rejected_application = rejected_application;
    }

    public String getHold_date() {
        return hold_date;
    }

    public void setHold_date(String hold_date) {
        this.hold_date = hold_date;
    }

    public String getRejected_date() {
        return rejected_date;
    }

    public void setRejected_date(String rejected_date) {
        this.rejected_date = rejected_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaster_Line_No() {
        return master_Line_No;
    }

    public void setMaster_Line_No(String master_Line_No) {
        this.master_Line_No = master_Line_No;
    }

    public String getCStatus() {
        return CStatus;
    }

    public void setCStatus(String CStatus) {
        this.CStatus = CStatus;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}