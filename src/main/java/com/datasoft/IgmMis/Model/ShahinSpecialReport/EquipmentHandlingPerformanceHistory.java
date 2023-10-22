package com.datasoft.IgmMis.Model.ShahinSpecialReport;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class EquipmentHandlingPerformanceHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String eq;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date logDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date log_In;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date log_Out;
    private String short_name;

    private String logBy;
    private String created_by;
    private String imRtotal;
    private String keepDTotal;
    private String dOffTotal;
    private String shiftTotal;
    private Integer totalHandingBox;

    private Integer impRcv;
    private Integer keepDlv;
    private Integer dlvOcdOffDock;
    private Integer shift;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogBy() {
        return logBy;
    }

    public void setLogBy(String logBy) {
        this.logBy = logBy;
    }

    public String getEq() {
        return eq;
    }

    public void setEq(String eq) {
        this.eq = eq;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public Date getLog_In() {
        return log_In;
    }

    public void setLog_In(Date log_In) {
        this.log_In = log_In;
    }

    public Date getLog_Out() {
        return log_Out;
    }

    public void setLog_Out(Date log_Out) {
        this.log_Out = log_Out;
    }

    public Integer getTotalHandingBox() {
        return totalHandingBox;
    }

    public void setTotalHandingBox(Integer totalHandingBox) {
        this.totalHandingBox = totalHandingBox;
    }

    public Integer getImpRcv() {
        return impRcv;
    }

    public void setImpRcv(Integer impRcv) {
        this.impRcv = impRcv;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getImRtotal() {
        return imRtotal;
    }

    public void setImRtotal(String imRtotal) {
        this.imRtotal = imRtotal;
    }

    public String getKeepDTotal() {
        return keepDTotal;
    }

    public void setKeepDTotal(String keepDTotal) {
        this.keepDTotal = keepDTotal;
    }

    public String getdOffTotal() {
        return dOffTotal;
    }

    public void setdOffTotal(String dOffTotal) {
        this.dOffTotal = dOffTotal;
    }

    public String getShiftTotal() {
        return shiftTotal;
    }

    public void setShiftTotal(String shiftTotal) {
        this.shiftTotal = shiftTotal;
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

