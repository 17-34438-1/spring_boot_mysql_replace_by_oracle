package com.datasoft.IgmMis.Model.AccountSetting;

public class ChangePasswordModel {
    private String rtnValue;
    private String message;
    private String phoneNumber;
    private Integer count ;
    private String current;
    private Integer twoStepState;

    public String getRtnValue() {
        return rtnValue;
    }

    public void setRtnValue(String rtnValue) {
        this.rtnValue = rtnValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public Integer getTwoStepState() {
        return twoStepState;
    }

    public void setTwoStepState(Integer twoStepState) {
        this.twoStepState = twoStepState;
    }
}
