package com.datasoft.IgmMis.Model.IgmOperation;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TodaysEdiDeclaration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String file_download_by;
    private String file_name_edi;
    private String file_name_stow;
    private List VesselInfo;

    private String Import_Rotation_No;
    private String Vessel_Name;
    private String Name_of_Master;
    private String Voy_No;
    private String VoyNoExp;
    private String grt;
    private String nrt;
    private String imo;
    private String loa_cm;
    private String flag;
    private String radio_call_sign;
    private String beam_cm;
    private String  agent_name;

    public String getFile_download_by() {
        return file_download_by;
    }

    public void setFile_download_by(String file_download_by) {
        this.file_download_by = file_download_by;
    }

    public String getImport_Rotation_No() {
        return Import_Rotation_No;
    }

    public void setImport_Rotation_No(String import_Rotation_No) {
        Import_Rotation_No = import_Rotation_No;
    }

    public String getVessel_Name() {
        return Vessel_Name;
    }

    public void setVessel_Name(String vessel_Name) {
        Vessel_Name = vessel_Name;
    }

    public String getName_of_Master() {
        return Name_of_Master;
    }

    public void setName_of_Master(String name_of_Master) {
        Name_of_Master = name_of_Master;
    }

    public String getVoy_No() {
        return Voy_No;
    }

    public void setVoy_No(String voy_No) {
        Voy_No = voy_No;
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

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public List getVesselInfo() {
        return VesselInfo;
    }

    public void setVesselInfo(List vesselInfo) {
        VesselInfo = vesselInfo;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getFile_name_edi() {
        return file_name_edi;
    }
    public void setFile_name_edi(String file_name_edi) {
        this.file_name_edi = file_name_edi;
    }
    public String getFile_name_stow() {
        return file_name_stow;
    }
    public void setFile_name_stow(String file_name_stow) {
        this.file_name_stow = file_name_stow;
    }
}
