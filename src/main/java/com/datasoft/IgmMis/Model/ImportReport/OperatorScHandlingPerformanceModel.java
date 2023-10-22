package com.datasoft.IgmMis.Model.ImportReport;

public class OperatorScHandlingPerformanceModel {
    private String placed_by;
    private Integer impRcv;
    private Integer keepDlv;
    private Integer dlvOcdOffDock;
    private Integer shift;
    private Integer totalHandingBox;

    public Integer getTotalHandingBox() {
        return totalHandingBox;
    }

    public void setTotalHandingBox(Integer totalHandingBox) {
        this.totalHandingBox = totalHandingBox;
    }

    public String getPlaced_by() {
        return placed_by;
    }

    public void setPlaced_by(String placed_by) {
        this.placed_by = placed_by;
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
}
