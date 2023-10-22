package com.datasoft.IgmMis.Model.ImportReport;

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
public class RemovalListOverflow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cont_no;
    private String cf;
    private String sms_number;
    private String cont_status;
    private String slot;
    private String yard_No;
    private String rot_no;
    private String v_name;
    private String size;
    private String height;
    private String mlo;
    private String mfdch_value;
    private String mfdch_desc;
    private String seal_nbr1;

    private String assignment_date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCont_no() {
        return cont_no;
    }

    public void setCont_no(String cont_no) {
        this.cont_no = cont_no;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getSms_number() {
        return sms_number;
    }

    public void setSms_number(String sms_number) {
        this.sms_number = sms_number;
    }

    public String getCont_status() {
        return cont_status;
    }

    public void setCont_status(String cont_status) {
        this.cont_status = cont_status;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getYard_No() {
        return yard_No;
    }

    public void setYard_No(String yard_No) {
        this.yard_No = yard_No;
    }

    public String getRot_no() {
        return rot_no;
    }

    public void setRot_no(String rot_no) {
        this.rot_no = rot_no;
    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
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

    public String getMfdch_value() {
        return mfdch_value;
    }

    public void setMfdch_value(String mfdch_value) {
        this.mfdch_value = mfdch_value;
    }

    public String getMfdch_desc() {
        return mfdch_desc;
    }

    public void setMfdch_desc(String mfdch_desc) {
        this.mfdch_desc = mfdch_desc;
    }

    public String getSeal_nbr1() {
        return seal_nbr1;
    }

    public void setSeal_nbr1(String seal_nbr1) {
        this.seal_nbr1 = seal_nbr1;
    }

    public String getAssignment_date() {
        return assignment_date;
    }

    public void setAssignment_date(String assignment_date) {
        this.assignment_date = assignment_date;
    }
}
