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
public class ExportContainerNotFoundReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private Integer vvd_gkey;
    private String vsl_name;
    private String voysNo;
    private String ddl_imp_rot_no;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date ata;


    private String rot;
    private String iso;
    private String mlo;
    private String freight_kind;
    private String weight;
    private String yard;
    private String last_pos_slot;
    private String coming_from;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fcy_time_in;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date assignmentdate;


    private String pod;
    private String stowage_pos;
    private String short_name;
    private String user_id;

    public String getComing_from() {
        return coming_from;
    }

    public void setComing_from(String coming_from) {
        this.coming_from = coming_from;
    }

    public Integer getVvd_gkey() {
        return vvd_gkey;
    }

    public void setVvd_gkey(Integer vvd_gkey) {
        this.vvd_gkey = vvd_gkey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVsl_name() {
        return vsl_name;
    }

    public void setVsl_name(String vsl_name) {
        this.vsl_name = vsl_name;
    }

    public String getVoysNo() {
        return voysNo;
    }

    public void setVoysNo(String voysNo) {
        this.voysNo = voysNo;
    }

    public String getDdl_imp_rot_no() {
        return ddl_imp_rot_no;
    }

    public void setDdl_imp_rot_no(String ddl_imp_rot_no) {
        this.ddl_imp_rot_no = ddl_imp_rot_no;
    }

    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
    }

    public String getRot() {
        return rot;
    }

    public void setRot(String rot) {
        this.rot = rot;
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

    public String getYard() {
        return yard;
    }

    public void setYard(String yard) {
        this.yard = yard;
    }

    public String getLast_pos_slot() {
        return last_pos_slot;
    }

    public void setLast_pos_slot(String last_pos_slot) {
        this.last_pos_slot = last_pos_slot;
    }

    public Date getFcy_time_in() {
        return fcy_time_in;
    }

    public void setFcy_time_in(Date fcy_time_in) {
        this.fcy_time_in = fcy_time_in;
    }

    public Date getAssignmentdate() {
        return assignmentdate;
    }

    public void setAssignmentdate(Date assignmentdate) {
        this.assignmentdate = assignmentdate;
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


}
