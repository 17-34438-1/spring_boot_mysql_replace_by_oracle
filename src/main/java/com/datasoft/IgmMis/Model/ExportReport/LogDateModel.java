package com.datasoft.IgmMis.Model.ExportReport;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class LogDateModel {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date logDate;
    private String logBy;

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


}