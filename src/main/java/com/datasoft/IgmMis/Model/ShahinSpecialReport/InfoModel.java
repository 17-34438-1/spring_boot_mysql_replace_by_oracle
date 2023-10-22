package com.datasoft.IgmMis.Model.ShahinSpecialReport;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class InfoModel {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date logDate;
    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }


}
