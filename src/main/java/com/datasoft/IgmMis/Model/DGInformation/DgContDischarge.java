package com.datasoft.IgmMis.Model.DGInformation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DgContDischarge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String MLocode;
    private String AGentCode;
    private String import_Rotation_No;
    private Integer total1;
    private Integer total2;
    private Integer total3;
    private Integer total4;
    private Integer total5;
    private Integer total6;
    private Integer total7;
    private Integer total8;
    private Integer total9;
    private Integer total10;
    private Integer total11;
    private Integer total12;
    private Integer total13;
    private Integer total14;
    private Integer total15;
    private Integer LADEN_20;
    private Integer LADEN_40;
    private Integer EMPTY_20;
    private Integer EMPTY_40;
    private Integer REFFER_20;
    private Integer REFFER_40;
    private  Integer IMDG_20;
    private Integer IMDG_40;
    private Integer TRANS_20;
    private Integer TRANS_40;
    private  Integer ICD_20;
    private Integer ICD_40;
    private  Integer LD_45;
    private String Vessel_Name;
    private String Total_number_of_bols;
    private String Total_number_of_packages;
    private String Total_number_of_containers;
    private String Total_gross_mass;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getEMPTY_20() {
        return EMPTY_20;
    }

    public Integer getREFFER_20() {
        return REFFER_20;
    }

    public String getTotal_number_of_bols() {
        return Total_number_of_bols;
    }

    public void setTotal_number_of_bols(String total_number_of_bols) {
        Total_number_of_bols = total_number_of_bols;
    }

    public String getTotal_number_of_packages() {
        return Total_number_of_packages;
    }

    public void setTotal_number_of_packages(String total_number_of_packages) {
        Total_number_of_packages = total_number_of_packages;
    }

    public String getTotal_number_of_containers() {
        return Total_number_of_containers;
    }

    public void setTotal_number_of_containers(String total_number_of_containers) {
        Total_number_of_containers = total_number_of_containers;
    }

    public String getTotal_gross_mass() {
        return Total_gross_mass;
    }

    public void setTotal_gross_mass(String total_gross_mass) {
        Total_gross_mass = total_gross_mass;
    }

    public Integer getTotal1() {
        return total1;
    }

    public void setTotal1(Integer total1) {
        this.total1 = total1;
    }

    public Integer getTotal2() {
        return total2;
    }

    public void setTotal2(Integer total2) {
        this.total2 = total2;
    }

    public Integer getTotal3() {
        return total3;
    }

    public void setTotal3(Integer total3) {
        this.total3 = total3;
    }

    public Integer getTotal4() {
        return total4;
    }

    public void setTotal4(Integer total4) {
        this.total4 = total4;
    }

    public Integer getTotal5() {
        return total5;
    }

    public void setTotal5(Integer total5) {
        this.total5 = total5;
    }

    public Integer getTotal6() {
        return total6;
    }

    public void setTotal6(Integer total6) {
        this.total6 = total6;
    }

    public Integer getTotal7() {
        return total7;
    }

    public void setTotal7(Integer total7) {
        this.total7 = total7;
    }

    public Integer getTotal8() {
        return total8;
    }

    public void setTotal8(Integer total8) {
        this.total8 = total8;
    }

    public Integer getTotal9() {
        return total9;
    }

    public void setTotal9(Integer total9) {
        this.total9 = total9;
    }

    public Integer getTotal10() {
        return total10;
    }

    public void setTotal10(Integer total10) {
        this.total10 = total10;
    }

    public Integer getTotal11() {
        return total11;
    }

    public void setTotal11(Integer total11) {
        this.total11 = total11;
    }

    public Integer getTotal12() {
        return total12;
    }

    public void setTotal12(Integer total12) {
        this.total12 = total12;
    }

    public Integer getTotal13() {
        return total13;
    }

    public void setTotal13(Integer total13) {
        this.total13 = total13;
    }

    public Integer getTotal14() {
        return total14;
    }

    public void setTotal14(Integer total14) {
        this.total14 = total14;
    }

    public Integer getTotal15() {
        return total15;
    }

    public void setTotal15(Integer total15) {
        this.total15 = total15;
    }



    public String getVessel_Name() {
        return Vessel_Name;
    }

    public void setVessel_Name(String vessel_Name) {
        Vessel_Name = vessel_Name;
    }

    private Integer MT_45;

    public Integer getEMPTY_20(Integer totalg3) {
        return EMPTY_20;
    }

    public void setEMPTY_20(Integer EMPTY_20) {
        this.EMPTY_20 = EMPTY_20;
    }

    public Integer getEMPTY_40() {
        return EMPTY_40;
    }

    public void setEMPTY_40(Integer EMPTY_40) {
        this.EMPTY_40 = EMPTY_40;
    }

    public Integer getREFFER_20(Integer totalg5) {
        return REFFER_20;
    }

    public void setREFFER_20(Integer REFFER_20) {
        this.REFFER_20 = REFFER_20;
    }

    public Integer getREFFER_40() {
        return REFFER_40;
    }

    public void setREFFER_40(Integer REFFER_40) {
        this.REFFER_40 = REFFER_40;
    }

    public Integer getIMDG_20() {
        return IMDG_20;
    }

    public void setIMDG_20(Integer IMDG_20) {
        this.IMDG_20 = IMDG_20;
    }

    public Integer getIMDG_40() {
        return IMDG_40;
    }

    public void setIMDG_40(Integer IMDG_40) {
        this.IMDG_40 = IMDG_40;
    }

    public Integer getTRANS_20() {
        return TRANS_20;
    }

    public void setTRANS_20(Integer TRANS_20) {
        this.TRANS_20 = TRANS_20;
    }

    public Integer getTRANS_40() {
        return TRANS_40;
    }

    public void setTRANS_40(Integer TRANS_40) {
        this.TRANS_40 = TRANS_40;
    }

    public Integer getICD_20() {
        return ICD_20;
    }

    public void setICD_20(Integer ICD_20) {
        this.ICD_20 = ICD_20;
    }

    public Integer getICD_40() {
        return ICD_40;
    }

    public void setICD_40(Integer ICD_40) {
        this.ICD_40 = ICD_40;
    }

    public Integer getLD_45() {
        return LD_45;
    }

    public void setLD_45(Integer LD_45) {
        this.LD_45 = LD_45;
    }

    public Integer getMT_45() {
        return MT_45;
    }

    public void setMT_45(Integer MT_45) {
        this.MT_45 = MT_45;
    }

    public Integer getLADEN_20() {
        return LADEN_20;
    }

    public void setLADEN_20(Integer LADEN_20) {
        this.LADEN_20 = LADEN_20;
    }

    public Integer getLADEN_40() {
        return LADEN_40;
    }

    public void setLADEN_40(Integer LADEN_40) {
        this.LADEN_40 = LADEN_40;
    }

    public String getAGentCode() {
        return AGentCode;
    }

    public void setAGentCode(String AGentCode) {
        this.AGentCode = AGentCode;
    }

    public String getMLocode() {
        return MLocode;
    }

    public void setMLocode(String MLocode) {
        this.MLocode = MLocode;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getImport_Rotation_No() {
        return import_Rotation_No;
    }

    public void setImport_Rotation_No(String import_Rotation_No) {
        this.import_Rotation_No = import_Rotation_No;
    }
}