package com.datasoft.IgmMis.Model.ImportReport;

import java.util.List;

public class MisAssignmentCctNctOfyModel {
    private String mfdch_value;
    private String mfdch_desc;
    private List resultList;
    private Integer t20;
    private  Integer t40;
    private Integer total;
    private Integer tues;

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

    public List getResultList() {
        return resultList;
    }

    public void setResultList(List resultList) {
        this.resultList = resultList;
    }

    public Integer getT20() {
        return t20;
    }

    public void setT20(Integer t20) {
        this.t20 = t20;
    }

    public Integer getT40() {
        return t40;
    }

    public void setT40(Integer t40) {
        this.t40 = t40;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTues() {
        return tues;
    }

    public void setTues(Integer tues) {
        this.tues = tues;
    }
}
