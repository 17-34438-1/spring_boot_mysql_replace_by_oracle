package com.datasoft.IgmMis.Model.ImportReport;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class EquipmentLoginLogout {
    @Id
    private Integer gKey;
    private String logDate;
    private String logType;
    private String logBy;
    private String logEquip;
    private String program;

    public Integer getgKey() {
        return gKey;
    }

    public void setgKey(Integer gKey) {
        this.gKey = gKey;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogBy() {
        return logBy;
    }

    public void setLogBy(String logBy) {
        this.logBy = logBy;
    }

    public String getLogEquip() {
        return logEquip;
    }

    public void setLogEquip(String logEquip) {
        this.logEquip = logEquip;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }
}
