package com.datasoft.IgmMis.Model.ExportReport;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class ExportReportMloWiseExportSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Integer vvd_gkey;
    private String Voy_No;



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


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date atd;
    private String user_id;
    private String coming_from;
    private String mlo_name;
    private String mlo;
    private Integer D_20;
    private Integer D_40;
    private Integer H_40;
    private Integer H_45;
    private Integer RH_40;
    private Integer R_20;

    private Integer OT_20;
    private Integer FR_20;
    private Integer TK_20;
    private Integer FR_40;
    private Integer OT_40;

    private Integer MD_20;
    private Integer MD_40;
    private Integer MH_40;
    private Integer MH_45;

    private Integer MRH_40;
    private Integer MR_20;

    private Integer MOT_20;



    private Integer MFR_20;
    private Integer MTK_20;

    private Integer MFR_40;
    private Integer MOT_40;

    private Integer grand_tot;
    private Integer tues;


    public Integer getVvd_gkey() {
        return vvd_gkey;
    }

    public void setVvd_gkey(Integer vvd_gkey) {
        this.vvd_gkey = vvd_gkey;
    }


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

    public String getContNo() {
        return contNo;
    }

    public void setContNo(String contNo) {
        this.contNo = contNo;
    }
    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
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




    public Date getAtd() {
        return atd;
    }

    public void setAtd(Date atd) {
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

    public String getMlo_name() {
        return mlo_name;
    }

    public void setMlo_name(String mlo_name) {
        this.mlo_name = mlo_name;
    }

    public String getMlo() {
        return mlo;
    }

    public void setMlo(String mlo) {
        this.mlo = mlo;
    }

    public Integer getD_20() {
        return D_20;
    }

    public void setD_20(Integer d_20) {
        D_20 = d_20;
    }

    public Integer getD_40() {
        return D_40;
    }

    public void setD_40(Integer d_40) {
        D_40 = d_40;
    }

    public Integer getH_40() {
        return H_40;
    }

    public void setH_40(Integer h_40) {
        H_40 = h_40;
    }

    public Integer getH_45() {
        return H_45;
    }

    public void setH_45(Integer h_45) {
        H_45 = h_45;
    }

    public Integer getRH_40() {
        return RH_40;
    }

    public void setRH_40(Integer RH_40) {
        this.RH_40 = RH_40;
    }

    public Integer getR_20() {
        return R_20;
    }

    public void setR_20(Integer r_20) {
        R_20 = r_20;
    }

    public Integer getOT_20() {
        return OT_20;
    }

    public void setOT_20(Integer OT_20) {
        this.OT_20 = OT_20;
    }

    public Integer getFR_20() {
        return FR_20;
    }

    public void setFR_20(Integer FR_20) {
        this.FR_20 = FR_20;
    }

    public Integer getTK_20() {
        return TK_20;
    }

    public void setTK_20(Integer TK_20) {
        this.TK_20 = TK_20;
    }

    public Integer getFR_40() {
        return FR_40;
    }

    public void setFR_40(Integer FR_40) {
        this.FR_40 = FR_40;
    }

    public Integer getOT_40() {
        return OT_40;
    }

    public void setOT_40(Integer OT_40) {
        this.OT_40 = OT_40;
    }

    public Integer getMD_20() {
        return MD_20;
    }

    public void setMD_20(Integer MD_20) {
        this.MD_20 = MD_20;
    }

    public Integer getMD_40() {
        return MD_40;
    }

    public void setMD_40(Integer MD_40) {
        this.MD_40 = MD_40;
    }

    public Integer getMH_40() {
        return MH_40;
    }

    public void setMH_40(Integer MH_40) {
        this.MH_40 = MH_40;
    }

    public Integer getMH_45() {
        return MH_45;
    }

    public void setMH_45(Integer MH_45) {
        this.MH_45 = MH_45;
    }

    public Integer getMRH_40() {
        return MRH_40;
    }

    public void setMRH_40(Integer MRH_40) {
        this.MRH_40 = MRH_40;
    }

    public Integer getMR_20() {
        return MR_20;
    }

    public void setMR_20(Integer MR_20) {
        this.MR_20 = MR_20;
    }

    public Integer getMOT_20() {
        return MOT_20;
    }

    public void setMOT_20(Integer MOT_20) {
        this.MOT_20 = MOT_20;
    }

    public Integer getMFR_20() {
        return MFR_20;
    }

    public void setMFR_20(Integer MFR_20) {
        this.MFR_20 = MFR_20;
    }

    public Integer getMTK_20() {
        return MTK_20;
    }

    public void setMTK_20(Integer MTK_20) {
        this.MTK_20 = MTK_20;
    }

    public Integer getMFR_40() {
        return MFR_40;
    }

    public void setMFR_40(Integer MFR_40) {
        this.MFR_40 = MFR_40;
    }

    public Integer getMOT_40() {
        return MOT_40;
    }

    public void setMOT_40(Integer MOT_40) {
        this.MOT_40 = MOT_40;
    }

    public Integer getGrand_tot() {
        return grand_tot;
    }

    public void setGrand_tot(Integer grand_tot) {
        this.grand_tot = grand_tot;
    }

    public Integer getTues() {
        return tues;
    }

    public void setTues(Integer tues) {
        this.tues = tues;
    }


}