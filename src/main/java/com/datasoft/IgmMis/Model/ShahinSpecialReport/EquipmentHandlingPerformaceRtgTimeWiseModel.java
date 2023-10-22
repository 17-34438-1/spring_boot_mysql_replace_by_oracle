package com.datasoft.IgmMis.Model.ShahinSpecialReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class EquipmentHandlingPerformaceRtgTimeWiseModel {
    private List equipmentRtgList;
    private Integer total;
    private String eq;
    private Integer t_08_09;
    private Integer t_09_10;
    private Integer t_10_11;
    private Integer t_11_12;
    private Integer t_12_13;
    private Integer t_13_14;
    private Integer t_14_15;
    private Integer t_15_16;
    private Integer t_16_17;
    private Integer t_17_18;
    private Integer t_18_19;
    private Integer t_19_20;
    private Integer sum_08_09;
    private Integer sum_09_10;
    private Integer sum_10_11;
    private Integer sum_11_12;
    private Integer sum_12_13;
    private Integer sum_13_14;
    private Integer sum_14_15;
    private Integer sum_15_16;
    private Integer sum_16_17;
    private Integer sum_17_18;
    private Integer sum_18_19;
    private Integer sum_19_20;
    private Integer gkey;

    public Integer getGkey() {
        return gkey;
    }

    public void setGkey(Integer gkey) {
        this.gkey = gkey;
    }

    public String getEq() {
        return eq;
    }

    public void setEq(String eq) {
        this.eq = eq;
    }

    public Integer getT_08_09() {
        return t_08_09;
    }

    public void setT_08_09(Integer t_08_09) {
        this.t_08_09 = t_08_09;
    }

    public Integer getT_09_10() {
        return t_09_10;
    }

    public void setT_09_10(Integer t_09_10) {
        this.t_09_10 = t_09_10;
    }

    public Integer getT_10_11() {
        return t_10_11;
    }

    public void setT_10_11(Integer t_10_11) {
        this.t_10_11 = t_10_11;
    }

    public Integer getT_11_12() {
        return t_11_12;
    }

    public void setT_11_12(Integer t_11_12) {
        this.t_11_12 = t_11_12;
    }

    public Integer getT_12_13() {
        return t_12_13;
    }

    public void setT_12_13(Integer t_12_13) {
        this.t_12_13 = t_12_13;
    }

    public Integer getT_13_14() {
        return t_13_14;
    }

    public void setT_13_14(Integer t_13_14) {
        this.t_13_14 = t_13_14;
    }

    public Integer getT_14_15() {
        return t_14_15;
    }

    public void setT_14_15(Integer t_14_15) {
        this.t_14_15 = t_14_15;
    }

    public Integer getT_15_16() {
        return t_15_16;
    }

    public void setT_15_16(Integer t_15_16) {
        this.t_15_16 = t_15_16;
    }

    public Integer getT_16_17() {
        return t_16_17;
    }

    public void setT_16_17(Integer t_16_17) {
        this.t_16_17 = t_16_17;
    }

    public Integer getT_17_18() {
        return t_17_18;
    }

    public void setT_17_18(Integer t_17_18) {
        this.t_17_18 = t_17_18;
    }

    public Integer getT_18_19() {
        return t_18_19;
    }

    public void setT_18_19(Integer t_18_19) {
        this.t_18_19 = t_18_19;
    }

    public Integer getT_19_20() {
        return t_19_20;
    }

    public void setT_19_20(Integer t_19_20) {
        this.t_19_20 = t_19_20;
    }

    public List getEquipmentRtgList() {
        return equipmentRtgList;
    }

    public void setEquipmentRtgList(List equipmentRtgList) {
        this.equipmentRtgList = equipmentRtgList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSum_08_09() {
        return sum_08_09;
    }

    public void setSum_08_09(Integer sum_08_09) {
        this.sum_08_09 = sum_08_09;
    }

    public Integer getSum_09_10() {
        return sum_09_10;
    }

    public void setSum_09_10(Integer sum_09_10) {
        this.sum_09_10 = sum_09_10;
    }

    public Integer getSum_10_11() {
        return sum_10_11;
    }

    public void setSum_10_11(Integer sum_10_11) {
        this.sum_10_11 = sum_10_11;
    }

    public Integer getSum_11_12() {
        return sum_11_12;
    }

    public void setSum_11_12(Integer sum_11_12) {
        this.sum_11_12 = sum_11_12;
    }

    public Integer getSum_12_13() {
        return sum_12_13;
    }

    public void setSum_12_13(Integer sum_12_13) {
        this.sum_12_13 = sum_12_13;
    }

    public Integer getSum_13_14() {
        return sum_13_14;
    }

    public void setSum_13_14(Integer sum_13_14) {
        this.sum_13_14 = sum_13_14;
    }

    public Integer getSum_14_15() {
        return sum_14_15;
    }

    public void setSum_14_15(Integer sum_14_15) {
        this.sum_14_15 = sum_14_15;
    }

    public Integer getSum_15_16() {
        return sum_15_16;
    }

    public void setSum_15_16(Integer sum_15_16) {
        this.sum_15_16 = sum_15_16;
    }

    public Integer getSum_16_17() {
        return sum_16_17;
    }

    public void setSum_16_17(Integer sum_16_17) {
        this.sum_16_17 = sum_16_17;
    }

    public Integer getSum_17_18() {
        return sum_17_18;
    }

    public void setSum_17_18(Integer sum_17_18) {
        this.sum_17_18 = sum_17_18;
    }

    public Integer getSum_18_19() {
        return sum_18_19;
    }

    public void setSum_18_19(Integer sum_18_19) {
        this.sum_18_19 = sum_18_19;
    }

    public Integer getSum_19_20() {
        return sum_19_20;
    }

    public void setSum_19_20(Integer sum_19_20) {
        this.sum_19_20 = sum_19_20;
    }
}
