package com.datasoft.IgmMis.Model.ExportReport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class ExportDateAndRotationWisePreAdvisedContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String cont_id;
    private String cont_size;

    private String goods_and_ctr_wt_kg;
    private String seal_no;
    private String isoType;
    private String offdock;
    private String vsl_name;
    private String rotation;
    private String cont_mlo;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date last_update;

    //    private String last_update;
    private String cont_status;
    private String pod;



    public String getCont_mlo() {
        return cont_mlo;
    }

    public void setCont_mlo(String cont_mlo) {
        this.cont_mlo = cont_mlo;
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

    public String getIsoType() {
        return isoType;
    }

    public void setIsoType(String isoType) {
        this.isoType = isoType;
    }

    public String getOffdock() {
        return offdock;
    }

    public void setOffdock(String offdock) {
        this.offdock = offdock;
    }

    public String getVsl_name() {
        return vsl_name;
    }

    public void setVsl_name(String vsl_name) {
        this.vsl_name = vsl_name;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }



    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }


    public String getCont_status() {
        return cont_status;
    }

    public void setCont_status(String cont_status) {
        this.cont_status = cont_status;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}