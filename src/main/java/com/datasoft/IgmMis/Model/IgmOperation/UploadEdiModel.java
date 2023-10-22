package com.datasoft.IgmMis.Model.IgmOperation;

public class UploadEdiModel {
    private String Import_Rotation_No;
    private String Voy_No;
    private String Vessel_Name;
    private String VoyNoExp;
    private String grt;
    private String nrt;
    private String imo;
    private String loa_cm;
    private String flag;
    private String radio_call_sign;
    private String beam_cm;
    private Integer rtnValue;
    private String message;

    public String getImport_Rotation_No() {
        return Import_Rotation_No;
    }

    public void setImport_Rotation_No(String import_Rotation_No) {
        Import_Rotation_No = import_Rotation_No;
    }

    public String getVoy_No() {
        return Voy_No;
    }

    public void setVoy_No(String voy_No) {
        Voy_No = voy_No;
    }

    public String getVessel_Name() {
        return Vessel_Name;
    }

    public void setVessel_Name(String vessel_Name) {
        Vessel_Name = vessel_Name;
    }

    public String getVoyNoExp() {
        return VoyNoExp;
    }

    public void setVoyNoExp(String voyNoExp) {
        VoyNoExp = voyNoExp;
    }

    public String getGrt() {
        return grt;
    }

    public void setGrt(String grt) {
        this.grt = grt;
    }

    public String getNrt() {
        return nrt;
    }

    public void setNrt(String nrt) {
        this.nrt = nrt;
    }

    public String getImo() {
        return imo;
    }

    public void setImo(String imo) {
        this.imo = imo;
    }

    public String getLoa_cm() {
        return loa_cm;
    }

    public void setLoa_cm(String loa_cm) {
        this.loa_cm = loa_cm;
    }



    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRadio_call_sign() {
        return radio_call_sign;
    }

    public void setRadio_call_sign(String radio_call_sign) {
        this.radio_call_sign = radio_call_sign;
    }

    public String getBeam_cm() {
        return beam_cm;
    }

    public void setBeam_cm(String beam_cm) {
        this.beam_cm = beam_cm;
    }

    public Integer getRtnValue() {
        return rtnValue;
    }

    public void setRtnValue(Integer rtnValue) {
        this.rtnValue = rtnValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
