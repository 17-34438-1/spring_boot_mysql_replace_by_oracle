package com.datasoft.IgmMis.Model.ImportReport;

import java.util.List;

public class HeadDeliveryRegisterCctNctModel {
    private String mfdch_desc;
    private String mfdch_value;
    private List resultInfo;

    public String getMfdch_desc() {
        return mfdch_desc;
    }

    public void setMfdch_desc(String mfdch_desc) {
        this.mfdch_desc = mfdch_desc;
    }

    public String getMfdch_value() {
        return mfdch_value;
    }

    public void setMfdch_value(String mfdch_value) {
        this.mfdch_value = mfdch_value;
    }

    public List getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(List resultInfo) {
        this.resultInfo = resultInfo;
    }
}
