package com.datasoft.IgmMis.Model.ImportReport;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ExportCopino {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cont_id;
    private String cont_size;
    private String cont_height;
    private String isoType;

    private String cont_mlo;
    private String cont_status;


    private String goods_and_ctr_wt_kg;
    private String seal_no;
    private String Import_rotation_No;
    private String NAME;
    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getImport_rotation_No() {
        return Import_rotation_No;
    }

    public void setImport_rotation_No(String import_rotation_No) {
        Import_rotation_No = import_rotation_No;
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCont_id() {
        return cont_id;
    }

    public void setCont_id(String cont_id) {
        this.cont_id = cont_id;
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

    public String getIsoType() {
        return isoType;
    }

    public void setIsoType(String isoType) {
        this.isoType = isoType;
    }

    public String getCont_mlo() {
        return cont_mlo;
    }

    public void setCont_mlo(String cont_mlo) {
        this.cont_mlo = cont_mlo;
    }

    public String getCont_status() {
        return cont_status;
    }

    public void setCont_status(String cont_status) {
        this.cont_status = cont_status;
    }

    public String getGoods_and_ctr_wt_kg() {
        return goods_and_ctr_wt_kg;
    }

    public void setGoods_and_ctr_wt_kg(String goods_and_ctr_wt_kg) {
        this.goods_and_ctr_wt_kg = goods_and_ctr_wt_kg;
    }

    public String getSeal_no() {
        return seal_no;
    }

    public void setSeal_no(String seal_no) {
        this.seal_no = seal_no;
    }

}
