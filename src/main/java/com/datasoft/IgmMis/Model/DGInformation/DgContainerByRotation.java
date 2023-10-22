package com.datasoft.IgmMis.Model.DGInformation;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class DgContainerByRotation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String vessel_Name;
    private String Import_Rotation_No;
    private String cont_number;
    private String cont_size;
    private String cont_seal_number;
    private String cont_status;

    private String Description_of_Goods;
    private String cont_imo;
    private String mlocode;
    private String Remarks;
    private String cont_un;
    private String berth;
    private String mlo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVessel_Name() {
        return vessel_Name;
    }

    public void setVessel_Name(String vessel_Name) {
        this.vessel_Name = vessel_Name;
    }

    public String getImport_Rotation_No() {
        return Import_Rotation_No;
    }

    public void setImport_Rotation_No(String import_Rotation_No) {
        Import_Rotation_No = import_Rotation_No;
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

    public String getCont_seal_number() {
        return cont_seal_number;
    }

    public void setCont_seal_number(String cont_seal_number) {
        this.cont_seal_number = cont_seal_number;
    }

    public String getCont_status() {
        return cont_status;
    }

    public void setCont_status(String cont_status) {
        this.cont_status = cont_status;
    }

    public String getDescription_of_Goods() {
        return Description_of_Goods;
    }

    public void setDescription_of_Goods(String description_of_Goods) {
        Description_of_Goods = description_of_Goods;
    }

    public String getCont_imo() {
        return cont_imo;
    }

    public void setCont_imo(String cont_imo) {
        this.cont_imo = cont_imo;
    }

    public String getMlocode() {
        return mlocode;
    }

    public void setMlocode(String mlocode) {
        this.mlocode = mlocode;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getCont_un() {
        return cont_un;
    }

    public void setCont_un(String cont_un) {
        this.cont_un = cont_un;
    }

    public String getBerth() {
        return berth;
    }

    public void setBerth(String berth) {
        this.berth = berth;
    }

    public String getMlo() {
        return mlo;
    }

    public void setMlo(String mlo) {
        this.mlo = mlo;
    }
}