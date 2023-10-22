package com.datasoft.IgmMis.Model.DGInformation;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity


@Getter
@Setter
@NoArgsConstructor
public class DgContainerDeliveryReport {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cont_number;
    private String cont_size;
    private String mlocode;
    private String mlo;
    private String Import_Rotation_No;
    private String cont_status;

    private String cont_un;
    private String Line_No;
    private String BL_No;
    private String Pack_Number;
    private String Pack_Description;
    private String Pack_Marks_Number;
    private String Description_of_Goods;

    private float weight;
    private String weight_unit;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date Delivery_Status_date;

    private  String vessel_Name;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date ata;


    private  String location;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date discharge_dt;

    private  String cont_imo;

    private String Notify_name;

    public Date getDelivery_Status_date() {
        return Delivery_Status_date;
    }

    public void setDelivery_Status_date(Date delivery_Status_date) {
        Delivery_Status_date = delivery_Status_date;
    }

    private Integer rl_no;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date rl_date;


    private Integer obpc_number;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date Discharged_Status_date;

    private String obpc_date;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time_in;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time_out;
    private Integer vvd_gkey;
    private String slot;

    public void setDischarge_dt(Date discharge_dt) {
        this.discharge_dt = discharge_dt;
    }

    public Date getDischarge_dt() {
        return discharge_dt;
    }

    public Date getDischarged_Status_date() {
        return Discharged_Status_date;
    }

    public void setDischarged_Status_date(Date discharged_Status_date) {
        Discharged_Status_date = discharged_Status_date;
    }

    public Integer getVvd_gkey() {
        return vvd_gkey;
    }

    public void setVvd_gkey(Integer vvd_gkey) {
        this.vvd_gkey = vvd_gkey;
    }

    public Integer getRl_no() {
        return rl_no;
    }

    public void setRl_no(Integer rl_no) {
        this.rl_no = rl_no;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public void setObpc_number(Integer obpc_number) {
        this.obpc_number = obpc_number;
    }

    public Date getTime_in() {
        return time_in;
    }

    public void setTime_in(Date time_in) {
        this.time_in = time_in;
    }

    public Date getTime_out() {
        return time_out;
    }

    public void setTime_out(Date time_out) {
        this.time_out = time_out;
    }

    public String getMlo() {
        return mlo;
    }

    public void setMlo(String mlo) {
        this.mlo = mlo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMlocode() {
        return mlocode;
    }

    public void setMlocode(String mlocode) {
        this.mlocode = mlocode;
    }

    public String getImport_Rotation_No() {
        return Import_Rotation_No;
    }

    public void setImport_Rotation_No(String import_Rotation_No) {
        Import_Rotation_No = import_Rotation_No;
    }

    public String getCont_status() {
        return cont_status;
    }

    public void setCont_status(String cont_status) {
        this.cont_status = cont_status;
    }

    public String getCont_un() {
        return cont_un;
    }

    public void setCont_un(String cont_un) {
        this.cont_un = cont_un;
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public String getVessel_Name() {
        return vessel_Name;
    }

    public void setVessel_Name(String vessel_Name) {
        this.vessel_Name = vessel_Name;
    }

    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }




    public String getCont_imo() {
        return cont_imo;
    }

    public void setCont_imo(String cont_imo) {
        this.cont_imo = cont_imo;
    }

    public String getNotify_name() {
        return Notify_name;
    }

    public void setNotify_name(String notify_name) {
        Notify_name = notify_name;
    }


    public Date getRl_date() {
        return rl_date;
    }

    public void setRl_date(Date rl_date) {
        this.rl_date = rl_date;
    }




    public Integer getObpc_number() {
        return obpc_number;
    }

    public String getObpc_date() {
        return obpc_date;
    }

    public void setObpc_date(String obpc_date) {
        this.obpc_date = obpc_date;
    }
}
