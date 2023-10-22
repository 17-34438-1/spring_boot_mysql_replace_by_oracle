package com.datasoft.IgmMis.Model.ImportReport;

public class EquipmentHandlingPerformanceRtgModel {
    private String eq;
    private String log_in_time;
    private String log_out_time;
   // private String short_name;
    private String log_by;
    private Integer impRcv;
    private Integer keepDlv;
    private Integer dlvOcdOffDock;
    private Integer shift;
    private Integer totalHandingBox;

    public String getEq() {
        return eq;
    }

    public void setEq(String eq) {
        this.eq = eq;
    }

    public String getLog_in_time() {
        return log_in_time;
    }

    public void setLog_in_time(String log_in_time) {
        this.log_in_time = log_in_time;
    }

    public String getLog_out_time() {
        return log_out_time;
    }

    public void setLog_out_time(String log_out_time) {
        this.log_out_time = log_out_time;
    }

   /* public String getShort_name() {
        return short_name;
    }*/

   /* public void setShort_name(String short_name) {
        this.short_name = short_name;
    }*/

    public String getLog_by() {
        return log_by;
    }

    public void setLog_by(String log_by) {
        this.log_by = log_by;
    }

    public Integer getImpRcv() {
        return impRcv;
    }

    public void setImpRcv(Integer impRcv) {
        this.impRcv = impRcv;
    }

    public Integer getKeepDlv() {
        return keepDlv;
    }

    public void setKeepDlv(Integer keepDlv) {
        this.keepDlv = keepDlv;
    }

    public Integer getDlvOcdOffDock() {
        return dlvOcdOffDock;
    }

    public void setDlvOcdOffDock(Integer dlvOcdOffDock) {
        this.dlvOcdOffDock = dlvOcdOffDock;
    }

    public Integer getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }

    public Integer getTotalHandingBox() {
        return totalHandingBox;
    }

    public void setTotalHandingBox(Integer totalHandingBox) {
        this.totalHandingBox = totalHandingBox;
    }
}
