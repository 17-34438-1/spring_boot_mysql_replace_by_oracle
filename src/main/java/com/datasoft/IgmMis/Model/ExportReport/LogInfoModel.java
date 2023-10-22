package com.datasoft.IgmMis.Model.ExportReport;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class LogInfoModel {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date logDate;



    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }


}
