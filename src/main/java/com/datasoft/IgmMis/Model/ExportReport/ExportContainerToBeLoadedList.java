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
public class ExportContainerToBeLoadedList {
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
    private String Voy_No;
    private String iso;
    private String mlo;
    private String freight_kind;
    private String weight;
    private String pod;
    private String fcy_last_pos_slot;
    private String short_name;
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

    public String getVoy_No() {
        return Voy_No;
    }

    public void setVoy_No(String voy_No) {
        Voy_No = voy_No;
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

    public String getFcy_last_pos_slot() {
        return fcy_last_pos_slot;
    }

    public void setFcy_last_pos_slot(String fcy_last_pos_slot) {
        this.fcy_last_pos_slot = fcy_last_pos_slot;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
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