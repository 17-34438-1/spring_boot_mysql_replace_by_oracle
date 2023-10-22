package com.datasoft.IgmMis.Model.ImportReport;

import java.util.List;

public class ImportContainerBayViewMainModel {

    private Integer id;
    private String vslId;
    private Integer bay;
    private Integer paired;
    private Integer pairedWith;
    private Integer centerLineA;
    private Integer gapLineA;
    private Integer isBelow;
    private Integer centerLineB;
    private Integer gapLineB;
    private Integer minRowLimAbv;
    private Integer minRowLimBlw;
    private Integer maxRowLimAbv;
    private Integer maxRowLimBlw;
    private String gapUpperRow;
    private String gapLowerRow;

    private String title;
    private Integer maxCol;
    private List firstLimit;
    private List lastLimit;

    private List dynamicRowList;

    private List table1TonsList;
    private List table1TonsList2;
    private String table1Ton;
    private Integer table1TotalSate;

    private List dynamicRowListBelow;

    private List table2TonsList;
    private List table2TonsList2;
    private String table2Ton;
    private Integer table2TotalSate;
    private List belowFirstList;
    private List belowLastList;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVslId() {
        return vslId;
    }

    public void setVslId(String vslId) {
        this.vslId = vslId;
    }

    public Integer getBay() {
        return bay;
    }

    public void setBay(Integer bay) {
        this.bay = bay;
    }

    public Integer getPaired() {
        return paired;
    }

    public void setPaired(Integer paired) {
        this.paired = paired;
    }

    public Integer getPairedWith() {
        return pairedWith;
    }

    public void setPairedWith(Integer pairedWith) {
        this.pairedWith = pairedWith;
    }

    public Integer getCenterLineA() {
        return centerLineA;
    }

    public void setCenterLineA(Integer centerLineA) {
        this.centerLineA = centerLineA;
    }

    public Integer getGapLineA() {
        return gapLineA;
    }

    public void setGapLineA(Integer gapLineA) {
        this.gapLineA = gapLineA;
    }

    public Integer getIsBelow() {
        return isBelow;
    }

    public void setIsBelow(Integer isBelow) {
        this.isBelow = isBelow;
    }

    public Integer getCenterLineB() {
        return centerLineB;
    }

    public void setCenterLineB(Integer centerLineB) {
        this.centerLineB = centerLineB;
    }

    public Integer getGapLineB() {
        return gapLineB;
    }

    public void setGapLineB(Integer gapLineB) {
        this.gapLineB = gapLineB;
    }

    public Integer getMinRowLimAbv() {
        return minRowLimAbv;
    }

    public void setMinRowLimAbv(Integer minRowLimAbv) {
        this.minRowLimAbv = minRowLimAbv;
    }

    public Integer getMinRowLimBlw() {
        return minRowLimBlw;
    }

    public Integer getMaxRowLimAbv() {
        return maxRowLimAbv;
    }

    public void setMaxRowLimAbv(Integer maxRowLimAbv) {
        this.maxRowLimAbv = maxRowLimAbv;
    }

    public void setMinRowLimBlw(Integer minRowLimBlw) {
        this.minRowLimBlw = minRowLimBlw;
    }

    public Integer getMaxRowLimBlw() {
        return maxRowLimBlw;
    }

    public void setMaxRowLimBlw(Integer maxRowLimBlw) {
        this.maxRowLimBlw = maxRowLimBlw;
    }

    public String getGapUpperRow() {
        return gapUpperRow;
    }

    public void setGapUpperRow(String gapUpperRow) {
        this.gapUpperRow = gapUpperRow;
    }

    public String getGapLowerRow() {
        return gapLowerRow;
    }

    public void setGapLowerRow(String gapLowerRow) {
        this.gapLowerRow = gapLowerRow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMaxCol() {
        return maxCol;
    }

    public void setMaxCol(Integer maxCol) {
        this.maxCol = maxCol;
    }

    public List getFirstLimit() {
        return firstLimit;
    }

    public void setFirstLimit(List firstLimit) {
        this.firstLimit = firstLimit;
    }

    public List getLastLimit() {
        return lastLimit;
    }

    public void setLastLimit(List lastLimit) {
        this.lastLimit = lastLimit;
    }

    public List getDynamicRowList() {
        return dynamicRowList;
    }

    public void setDynamicRowList(List dynamicRowList) {
        this.dynamicRowList = dynamicRowList;
    }

    public List getTable1TonsList() {
        return table1TonsList;
    }

    public void setTable1TonsList(List table1TonsList) {
        this.table1TonsList = table1TonsList;
    }

    public String getTable1Ton() {
        return table1Ton;
    }

    public void setTable1Ton(String table1Ton) {
        this.table1Ton = table1Ton;
    }

    public Integer getTable1TotalSate() {
        return table1TotalSate;
    }

    public void setTable1TotalSate(Integer table1TotalSate) {
        this.table1TotalSate = table1TotalSate;
    }

    public List getTable1TonsList2() {
        return table1TonsList2;
    }

    public void setTable1TonsList2(List table1TonsList2) {
        this.table1TonsList2 = table1TonsList2;
    }

    public List getDynamicRowListBelow() {
        return dynamicRowListBelow;
    }

    public void setDynamicRowListBelow(List dynamicRowListBelow) {
        this.dynamicRowListBelow = dynamicRowListBelow;
    }

    public List getTable2TonsList() {
        return table2TonsList;
    }

    public void setTable2TonsList(List table2TonsList) {
        this.table2TonsList = table2TonsList;
    }

    public List getTable2TonsList2() {
        return table2TonsList2;
    }

    public void setTable2TonsList2(List table2TonsList2) {
        this.table2TonsList2 = table2TonsList2;
    }

    public String getTable2Ton() {
        return table2Ton;
    }

    public void setTable2Ton(String table2Ton) {
        this.table2Ton = table2Ton;
    }

    public Integer getTable2TotalSate() {
        return table2TotalSate;
    }

    public void setTable2TotalSate(Integer table2TotalSate) {
        this.table2TotalSate = table2TotalSate;
    }

    public List getBelowFirstList() {
        return belowFirstList;
    }

    public void setBelowFirstList(List belowFirstList) {
        this.belowFirstList = belowFirstList;
    }

    public List getBelowLastList() {
        return belowLastList;
    }

    public void setBelowLastList(List belowLastList) {
        this.belowLastList = belowLastList;
    }
}
