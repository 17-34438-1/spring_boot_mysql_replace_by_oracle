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
public class RotationWiseExportContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String Organization_Name;
    private String cont_number;
    private String cont_size;
    private String cont_gross_weight;
    private String cont_weight;
    private String cont_seal_number;
    private String cont_status;
    private String off_dock_id;
    private String cont_imo;
    private String cont_un;
    private String commudity_code;
    private String cont_height;
    private String cont_iso_type;
    private String cont_type;
    private String mlocode;
    private String Port_Ship_ID;
    private String Vessel_Name;
    private String offdock_name;
    private String commodity;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getOrganization_Name() {
        return Organization_Name;
    }

    public void setOrganization_Name(String organization_Name) {
        Organization_Name = organization_Name;
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

    public String getCont_gross_weight() {
        return cont_gross_weight;
    }

    public void setCont_gross_weight(String cont_gross_weight) {
        this.cont_gross_weight = cont_gross_weight;
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

    public String getCont_status() {
        return cont_status;
    }

    public void setCont_status(String cont_status) {
        this.cont_status = cont_status;
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

    public String getCommudity_code() {
        return commudity_code;
    }

    public void setCommudity_code(String commudity_code) {
        this.commudity_code = commudity_code;
    }

    public String getCont_height() {
        return cont_height;
    }

    public void setCont_height(String cont_height) {
        this.cont_height = cont_height;
    }

    public String getCont_iso_type() {
        return cont_iso_type;
    }

    public void setCont_iso_type(String cont_iso_type) {
        this.cont_iso_type = cont_iso_type;
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

    public String getPort_Ship_ID() {
        return Port_Ship_ID;
    }

    public void setPort_Ship_ID(String port_Ship_ID) {
        Port_Ship_ID = port_Ship_ID;
    }

    public String getVessel_Name() {
        return Vessel_Name;
    }

    public void setVessel_Name(String vessel_Name) {
        Vessel_Name = vessel_Name;
    }

    public String getOffdock_name() {
        return offdock_name;
    }

    public void setOffdock_name(String offdock_name) {
        this.offdock_name = offdock_name;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }
}