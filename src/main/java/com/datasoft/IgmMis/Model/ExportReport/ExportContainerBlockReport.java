package com.datasoft.IgmMis.Model.ExportReport;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ExportContainerBlockReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String Voy_No;
    private String mlo;
    private  String mlo_name;
    private String contNo;
    private String iso;
    private String contStatus;
    private String weight;
    private String remarks;
    private String commodity;
    private String pod;
    private String stowage_pos;
    private  String vsl_name;
    private String berth_op;
    private String berth;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date ata;
    private  String atd;
    private String user_id;
    private String coming_from;
    private Integer vvd_gkey;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVoy_No() {
        return Voy_No;
    }

    public void setVoy_No(String voy_No) {
        Voy_No = voy_No;
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

    public String getContNo() {
        return contNo;
    }

    public void setContNo(String contNo) {
        this.contNo = contNo;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getContStatus() {
        return contStatus;
    }

    public void setContStatus(String contStatus) {
        this.contStatus = contStatus;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
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

    public String getAtd() {
        return atd;
    }

    public void setAtd(String atd) {
        this.atd = atd;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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
}
