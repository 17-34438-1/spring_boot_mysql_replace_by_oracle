package com.datasoft.IgmMis.Model.DGInformation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity


@Getter
@Setter
@NoArgsConstructor
public class DgContDischargeListByRotation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String import_Rotation_No;
    private String cont_number;
    private String cont_size;
    private String cont_height;

    private String mlocode;
    private String off_dock_id;
    private String cont_imo;
    private String cont_un;
    private String Description_of_Goods;
    private String cont_gross_weight;
    private String cont_status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImport_Rotation_No() {
        return import_Rotation_No;
    }

    public void setImport_Rotation_No(String import_Rotation_No) {
        this.import_Rotation_No = import_Rotation_No;
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

    public String getCont_height() {
        return cont_height;
    }

    public void setCont_height(String cont_height) {
        this.cont_height = cont_height;
    }

    public String getMlocode() {
        return mlocode;
    }

    public void setMlocode(String mlocode) {
        this.mlocode = mlocode;
    }

    public String getOff_dock_id() {
        return off_dock_id;
    }

    public void setOff_dock_id(String off_dock_id) {
        this.off_dock_id = off_dock_id;
    }

    public String getCont_imo() {
        return cont_imo;
    }

    public void setCont_imo(String cont_imo) {
        this.cont_imo = cont_imo;
    }

    public String getCont_un() {
        return cont_un;
    }

    public void setCont_un(String cont_un) {
        this.cont_un = cont_un;
    }

    public String getDescription_of_Goods() {
        return Description_of_Goods;
    }

    public void setDescription_of_Goods(String description_of_Goods) {
        Description_of_Goods = description_of_Goods;
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
}
