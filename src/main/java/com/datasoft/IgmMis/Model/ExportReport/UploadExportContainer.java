package com.datasoft.IgmMis.Model.ExportReport;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UploadExportContainer {
   private Integer rtnValue;
   private String contId;
   private String message;
   private List validationTableList;
   private String field;
   private String description;
   private String iso;
   private Integer size;
   private Integer height;
   private String isogroup;

    public Integer getRtnValue() {
        return rtnValue;
    }

    public void setRtnValue(Integer rtnValue) {
        this.rtnValue = rtnValue;
    }

    public String getContId() {
        return contId;
    }

    public void setContId(String contId) {
        this.contId = contId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List getValidationTableList() {
        return validationTableList;
    }

    public void setValidationTableList(List validationTableList) {
        this.validationTableList = validationTableList;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getIsogroup() {
        return isogroup;
    }

    public void setIsogroup(String isogroup) {
        this.isogroup = isogroup;
    }
}
