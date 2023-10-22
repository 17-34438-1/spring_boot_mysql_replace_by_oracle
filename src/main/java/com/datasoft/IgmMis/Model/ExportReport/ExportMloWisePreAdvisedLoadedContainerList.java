package com.datasoft.IgmMis.Model.ExportReport;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class ExportMloWisePreAdvisedLoadedContainerList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String agent;


    private String cont_id;
    private String mlo;
    private String Loaded_20;
    private String Loaded_40;
    private String EMTY_20;
    private String EMTY_40;



    private String REEFER_20;
    private String REEFER_40;
    private String IMDG_20;
    private String IMDG_40;
    private String TRSHP_20;
    private String TRSHP_40;
    private String ICD_20;
    private String ICD_40;
    private String LD_20;
    private String LD_40;
    private String grand_tot;


    private String gkey;



    private String oont_size;
    private String goods_and_ctr_wt_kg;
    private String seal_no;
    private String cont_mlo;
    private String isoType;
    private String offdock;

    private String vsl_name;
    private String rotation;
    private String pod;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date last_update;
    private String cont_status;

    private String name;
    private String voys_no;
    private String cont_size;



    private String F_20;
    private String L_20;
    private String M_20;

    private String I_20;
    private String T_20;

    private String R_20;

    private String F_40;

    private String L_40;
    private String M_40;
    private String I_40;
    private String  T_40;

    private String R_40;

    private String FW_20;

    private String LW_20;

    private String MW_20;
    private String  IW_20;

    private  String TW_20;


    private String  RW_20;

    private String   IMDGW_20;
    private String FW_40;
    private String LW_40;
    private String   MW_40;
    private String IW_40;
    private String TW_40;
    private  String RW_40;
    private String IMDGW_40;

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getCont_id() {
        return cont_id;
    }

    public void setCont_id(String cont_id) {
        this.cont_id = cont_id;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMlo() {
        return mlo;
    }

    public void setMlo(String mlo) {
        this.mlo = mlo;
    }

    public String getLoaded_20() {
        return Loaded_20;
    }

    public void setLoaded_20(String loaded_20) {
        Loaded_20 = loaded_20;
    }

    public String getLoaded_40() {
        return Loaded_40;
    }

    public void setLoaded_40(String loaded_40) {
        Loaded_40 = loaded_40;
    }

    public String getEMTY_20() {
        return EMTY_20;
    }

    public void setEMTY_20(String EMTY_20) {
        this.EMTY_20 = EMTY_20;
    }

    public String getEMTY_40() {
        return EMTY_40;
    }

    public void setEMTY_40(String EMTY_40) {
        this.EMTY_40 = EMTY_40;
    }

    public String getREEFER_20() {
        return REEFER_20;
    }

    public void setREEFER_20(String REEFER_20) {
        this.REEFER_20 = REEFER_20;
    }

    public String getREEFER_40() {
        return REEFER_40;
    }

    public void setREEFER_40(String REEFER_40) {
        this.REEFER_40 = REEFER_40;
    }

    public String getIMDG_20() {
        return IMDG_20;
    }

    public void setIMDG_20(String IMDG_20) {
        this.IMDG_20 = IMDG_20;
    }

    public String getIMDG_40() {
        return IMDG_40;
    }

    public void setIMDG_40(String IMDG_40) {
        this.IMDG_40 = IMDG_40;
    }

    public String getTRSHP_20() {
        return TRSHP_20;
    }

    public void setTRSHP_20(String TRSHP_20) {
        this.TRSHP_20 = TRSHP_20;
    }

    public String getTRSHP_40() {
        return TRSHP_40;
    }

    public void setTRSHP_40(String TRSHP_40) {
        this.TRSHP_40 = TRSHP_40;
    }

    public String getICD_20() {
        return ICD_20;
    }

    public void setICD_20(String ICD_20) {
        this.ICD_20 = ICD_20;
    }

    public String getICD_40() {
        return ICD_40;
    }

    public void setICD_40(String ICD_40) {
        this.ICD_40 = ICD_40;
    }

    public String getLD_20() {
        return LD_20;
    }

    public void setLD_20(String LD_20) {
        this.LD_20 = LD_20;
    }

    public String getLD_40() {
        return LD_40;
    }

    public void setLD_40(String LD_40) {
        this.LD_40 = LD_40;
    }

    public String getGrand_tot() {
        return grand_tot;
    }

    public void setGrand_tot(String grand_tot) {
        this.grand_tot = grand_tot;
    }



    public String getGkey() {
        return gkey;
    }

    public void setGkey(String gkey) {
        this.gkey = gkey;
    }

    public String getOont_size() {
        return oont_size;
    }

    public void setOont_size(String oont_size) {
        this.oont_size = oont_size;
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

    public String getCont_mlo() {
        return cont_mlo;
    }

    public void setCont_mlo(String cont_mlo) {
        this.cont_mlo = cont_mlo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVoys_no() {
        return voys_no;
    }

    public void setVoys_no(String voys_no) {
        this.voys_no = voys_no;
    }

    public String getCont_size() {
        return cont_size;
    }

    public void setCont_size(String cont_size) {
        this.cont_size = cont_size;
    }

    public String getF_20() {
        return F_20;
    }

    public void setF_20(String f_20) {
        F_20 = f_20;
    }

    public String getL_20() {
        return L_20;
    }

    public void setL_20(String l_20) {
        L_20 = l_20;
    }

    public String getM_20() {
        return M_20;
    }

    public void setM_20(String m_20) {
        M_20 = m_20;
    }

    public String getI_20() {
        return I_20;
    }

    public void setI_20(String i_20) {
        I_20 = i_20;
    }

    public String getT_20() {
        return T_20;
    }

    public void setT_20(String t_20) {
        T_20 = t_20;
    }

    public String getR_20() {
        return R_20;
    }

    public void setR_20(String r_20) {
        R_20 = r_20;
    }

    public String getF_40() {
        return F_40;
    }

    public void setF_40(String f_40) {
        F_40 = f_40;
    }

    public String getL_40() {
        return L_40;
    }

    public void setL_40(String l_40) {
        L_40 = l_40;
    }

    public String getM_40() {
        return M_40;
    }

    public void setM_40(String m_40) {
        M_40 = m_40;
    }

    public String getI_40() {
        return I_40;
    }

    public void setI_40(String i_40) {
        I_40 = i_40;
    }

    public String getT_40() {
        return T_40;
    }

    public void setT_40(String t_40) {
        T_40 = t_40;
    }

    public String getR_40() {
        return R_40;
    }

    public void setR_40(String r_40) {
        R_40 = r_40;
    }

    public String getFW_20() {
        return FW_20;
    }

    public void setFW_20(String FW_20) {
        this.FW_20 = FW_20;
    }

    public String getLW_20() {
        return LW_20;
    }

    public void setLW_20(String LW_20) {
        this.LW_20 = LW_20;
    }

    public String getMW_20() {
        return MW_20;
    }

    public void setMW_20(String MW_20) {
        this.MW_20 = MW_20;
    }

    public String getIW_20() {
        return IW_20;
    }

    public void setIW_20(String IW_20) {
        this.IW_20 = IW_20;
    }

    public String getTW_20() {
        return TW_20;
    }

    public void setTW_20(String TW_20) {
        this.TW_20 = TW_20;
    }

    public String getRW_20() {
        return RW_20;
    }

    public void setRW_20(String RW_20) {
        this.RW_20 = RW_20;
    }

    public String getIMDGW_20() {
        return IMDGW_20;
    }

    public void setIMDGW_20(String IMDGW_20) {
        this.IMDGW_20 = IMDGW_20;
    }

    public String getFW_40() {
        return FW_40;
    }

    public void setFW_40(String FW_40) {
        this.FW_40 = FW_40;
    }

    public String getLW_40() {
        return LW_40;
    }

    public void setLW_40(String LW_40) {
        this.LW_40 = LW_40;
    }

    public String getMW_40() {
        return MW_40;
    }

    public void setMW_40(String MW_40) {
        this.MW_40 = MW_40;
    }

    public String getIW_40() {
        return IW_40;
    }

    public void setIW_40(String IW_40) {
        this.IW_40 = IW_40;
    }

    public String getTW_40() {
        return TW_40;
    }

    public void setTW_40(String TW_40) {
        this.TW_40 = TW_40;
    }

    public String getRW_40() {
        return RW_40;
    }

    public void setRW_40(String RW_40) {
        this.RW_40 = RW_40;
    }

    public String getIMDGW_40() {
        return IMDGW_40;
    }

    public void setIMDGW_40(String IMDGW_40) {
        this.IMDGW_40 = IMDGW_40;
    }

}
