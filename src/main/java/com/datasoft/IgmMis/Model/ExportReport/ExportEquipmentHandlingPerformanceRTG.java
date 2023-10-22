package com.datasoft.IgmMis.Model.ExportReport;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class ExportEquipmentHandlingPerformanceRTG {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String eq;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date logDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date log_in_time;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date log_out_time;
    private String short_name;
    private String created_by;
    private String logBy;
    private Integer expRcv;
    private Integer yardMove;
    private Integer expShift;
    private Integer totalHandingBox;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEq() {
        return eq;
    }

    public void setEq(String eq) {
        this.eq = eq;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getLogBy() {
        return logBy;
    }

    public void setLogBy(String logBy) {
        this.logBy = logBy;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public Date getLog_in_time() {
        return log_in_time;
    }

    public void setLog_in_time(Date log_in_time) {
        this.log_in_time = log_in_time;
    }

    public Date getLog_out_time() {
        return log_out_time;
    }

    public void setLog_out_time(Date log_out_time) {
        this.log_out_time = log_out_time;
    }

    public Integer getExpRcv() {
        return expRcv;
    }

    public void setExpRcv(Integer expRcv) {
        this.expRcv = expRcv;
    }

    public Integer getYardMove() {
        return yardMove;
    }

    public void setYardMove(Integer yardMove) {
        this.yardMove = yardMove;
    }

    public Integer getExpShift() {
        return expShift;
    }

    public void setExpShift(Integer expShift) {
        this.expShift = expShift;
    }

    public Integer getTotalHandingBox() {
        return totalHandingBox;
    }

    public void setTotalHandingBox(Integer totalHandingBox) {
        this.totalHandingBox = totalHandingBox;
    }
}
