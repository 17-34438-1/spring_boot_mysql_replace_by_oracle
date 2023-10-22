package com.datasoft.IgmMis.Model.IgmOperation;

import java.util.List;

public class ViewIgmGeneralInfoMainModel {
    private Integer pageStratLimit;
    private Integer pageEndLimit;
    private Integer State;
    private List resultList;
    private List pagination;
    private Integer sl;

    public Integer getPageStratLimit() {
        return pageStratLimit;
    }

    public void setPageStratLimit(Integer pageStratLimit) {
        this.pageStratLimit = pageStratLimit;
    }

    public Integer getPageEndLimit() {
        return pageEndLimit;
    }

    public void setPageEndLimit(Integer pageEndLimit) {
        this.pageEndLimit = pageEndLimit;
    }

    public Integer getState() {
        return State;
    }

    public void setState(Integer state) {
        State = state;
    }

    public List getResultList() {
        return resultList;
    }

    public void setResultList(List resultList) {
        this.resultList = resultList;
    }

    public List getPagination() {
        return pagination;
    }

    public void setPagination(List pagination) {
        this.pagination = pagination;
    }

    public Integer getSl() {
        return sl;
    }

    public void setSl(Integer sl) {
        this.sl = sl;
    }
}
