package com.datasoft.IgmMis.Model.ExportReport;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ExportLoadedContainerListLoadAndEmpty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private Integer vvd_gkey;


    private  String vsl_name;
    private String berth_op;
    private String berth;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date ata;



    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date atd;



    private String size;


    private String height;
    private String mlo;
    private String mlo_name;

    private String freight_kind;
    private String weight;
    private String pod;
    private String stowage_pos;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date last_update;
    private String seal_no;
    private  String coming_from;
    private  String truck_no;
    private String craine_id;
    private String short_name;
    private String user_id;
    private String goods_and_ctr_wt_kg;


    private String onboard_LD_20;
    private String onboard_LD_40;
    private String onboard_MT_20;
    private String onboard_MT_40;
    private String onboard_LD_tues;
    private String onboard_MT_tues;
    private String balance_LD_20;
    private String balance_LD_40;
    private String balance_MT_20;
    private String balance_MT_40;
    private String balance_LD_tues;
    private String balance_MT_tues;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVvd_gkey() {
        return vvd_gkey;
    }

    public void setVvd_gkey(Integer vvd_gkey) {
        this.vvd_gkey = vvd_gkey;
    }

    public String getVsl_name() {
        return vsl_name;
    }

    public void setVsl_name(String vsl_name) {
        this.vsl_name = vsl_name;
    }

    public String getBerth_op() {
        return berth_op;
    }

    public void setBerth_op(String berth_op) {
        this.berth_op = berth_op;
    }

    public String getBerth() {
        return berth;
    }

    public void setBerth(String berth) {
        this.berth = berth;
    }

    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
    }

    public Date getAtd() {
        return atd;
    }

    public void setAtd(Date atd) {
        this.atd = atd;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMlo() {
        return mlo;
    }

    public void setMlo(String mlo) {
        this.mlo = mlo;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getStowage_pos() {
        return stowage_pos;
    }

    public void setStowage_pos(String stowage_pos) {
        this.stowage_pos = stowage_pos;
    }

    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }

    public String getSeal_no() {
        return seal_no;
    }

    public void setSeal_no(String seal_no) {
        this.seal_no = seal_no;
    }

    public String getComing_from() {
        return coming_from;
    }

    public void setComing_from(String coming_from) {
        this.coming_from = coming_from;
    }

    public String getTruck_no() {
        return truck_no;
    }

    public void setTruck_no(String truck_no) {
        this.truck_no = truck_no;
    }

    public String getCraine_id() {
        return craine_id;
    }

    public void setCraine_id(String craine_id) {
        this.craine_id = craine_id;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGoods_and_ctr_wt_kg() {
        return goods_and_ctr_wt_kg;
    }

    public void setGoods_and_ctr_wt_kg(String goods_and_ctr_wt_kg) {
        this.goods_and_ctr_wt_kg = goods_and_ctr_wt_kg;
    }

    public String getOnboard_LD_20() {
        return onboard_LD_20;
    }

    public void setOnboard_LD_20(String onboard_LD_20) {
        this.onboard_LD_20 = onboard_LD_20;
    }

    public String getOnboard_LD_40() {
        return onboard_LD_40;
    }

    public void setOnboard_LD_40(String onboard_LD_40) {
        this.onboard_LD_40 = onboard_LD_40;
    }

    public String getOnboard_MT_20() {
        return onboard_MT_20;
    }

    public void setOnboard_MT_20(String onboard_MT_20) {
        this.onboard_MT_20 = onboard_MT_20;
    }

    public String getOnboard_MT_40() {
        return onboard_MT_40;
    }

    public void setOnboard_MT_40(String onboard_MT_40) {
        this.onboard_MT_40 = onboard_MT_40;
    }

    public String getOnboard_LD_tues() {
        return onboard_LD_tues;
    }

    public void setOnboard_LD_tues(String onboard_LD_tues) {
        this.onboard_LD_tues = onboard_LD_tues;
    }

    public String getOnboard_MT_tues() {
        return onboard_MT_tues;
    }

    public void setOnboard_MT_tues(String onboard_MT_tues) {
        this.onboard_MT_tues = onboard_MT_tues;
    }

    public String getBalance_LD_20() {
        return balance_LD_20;
    }

    public void setBalance_LD_20(String balance_LD_20) {
        this.balance_LD_20 = balance_LD_20;
    }

    public String getBalance_LD_40() {
        return balance_LD_40;
    }

    public void setBalance_LD_40(String balance_LD_40) {
        this.balance_LD_40 = balance_LD_40;
    }

    public String getBalance_MT_20() {
        return balance_MT_20;
    }

    public void setBalance_MT_20(String balance_MT_20) {
        this.balance_MT_20 = balance_MT_20;
    }

    public String getBalance_MT_40() {
        return balance_MT_40;
    }

    public void setBalance_MT_40(String balance_MT_40) {
        this.balance_MT_40 = balance_MT_40;
    }

    public String getBalance_LD_tues() {
        return balance_LD_tues;
    }

    public void setBalance_LD_tues(String balance_LD_tues) {
        this.balance_LD_tues = balance_LD_tues;
    }

    public String getBalance_MT_tues() {
        return balance_MT_tues;
    }

    public void setBalance_MT_tues(String balance_MT_tues) {
        this.balance_MT_tues = balance_MT_tues;
    }
}