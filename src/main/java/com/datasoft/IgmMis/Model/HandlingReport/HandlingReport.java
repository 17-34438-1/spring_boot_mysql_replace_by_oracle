package com.datasoft.IgmMis.Model.HandlingReport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class HandlingReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    private String name;
    private String ib_vyg;
    private String ob_vyg;
    private String shipping_agent;
    private String berth;
    private Integer vvd_gkey;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date arrived_date;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date  departure_date;


    private Integer disch_load20;
    private Integer disch_load40;
    private Integer disch_mty20;
    private Integer disch_mty40;
    private Integer load_teus;
    private Integer mty_teus;
    private Integer tot_disch_load20;
    private Integer tot_disch_load40;
    private Integer tot_disch_mty20;
    private Integer tot_disch_mty40;
    private Integer tot_disch_load_teus;
    private Integer tot_disch_mty_teus;
    private Integer bal_load20;
    private Integer bal_load40;
    private Integer bal_mty20;
    private Integer bal_mty40;
    private Integer bal_load_teus;
    private Integer bal_mty_teus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIb_vyg() {
        return ib_vyg;
    }

    public void setIb_vyg(String ib_vyg) {
        this.ib_vyg = ib_vyg;
    }

    public String getOb_vyg() {
        return ob_vyg;
    }

    public void setOb_vyg(String ob_vyg) {
        this.ob_vyg = ob_vyg;
    }

    public String getShipping_agent() {
        return shipping_agent;
    }

    public void setShipping_agent(String shipping_agent) {
        this.shipping_agent = shipping_agent;
    }

    public String getBerth() {
        return berth;
    }

    public void setBerth(String berth) {
        this.berth = berth;
    }

    public Date getArrived_date() {
        return arrived_date;
    }

    public void setArrived_date(Date arrived_date) {
        this.arrived_date = arrived_date;
    }

    public Date getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(Date departure_date) {
        this.departure_date = departure_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDisch_load20() {
        return disch_load20;
    }

    public void setDisch_load20(Integer disch_load20) {
        this.disch_load20 = disch_load20;
    }

    public Integer getDisch_load40() {
        return disch_load40;
    }

    public void setDisch_load40(Integer disch_load40) {
        this.disch_load40 = disch_load40;
    }

    public Integer getDisch_mty20() {
        return disch_mty20;
    }

    public void setDisch_mty20(Integer disch_mty20) {
        this.disch_mty20 = disch_mty20;
    }

    public Integer getDisch_mty40() {
        return disch_mty40;
    }

    public void setDisch_mty40(Integer disch_mty40) {
        this.disch_mty40 = disch_mty40;
    }

    public Integer getLoad_teus() {
        return load_teus;
    }

    public void setLoad_teus(Integer load_teus) {
        this.load_teus = load_teus;
    }

    public Integer getMty_teus() {
        return mty_teus;
    }

    public void setMty_teus(Integer mty_teus) {
        this.mty_teus = mty_teus;
    }

    public Integer getTot_disch_load20() {
        return tot_disch_load20;
    }

    public void setTot_disch_load20(Integer tot_disch_load20) {
        this.tot_disch_load20 = tot_disch_load20;
    }

    public Integer getTot_disch_load40() {
        return tot_disch_load40;
    }

    public void setTot_disch_load40(Integer tot_disch_load40) {
        this.tot_disch_load40 = tot_disch_load40;
    }

    public Integer getTot_disch_mty20() {
        return tot_disch_mty20;
    }

    public void setTot_disch_mty20(Integer tot_disch_mty20) {
        this.tot_disch_mty20 = tot_disch_mty20;
    }

    public Integer getTot_disch_mty40() {
        return tot_disch_mty40;
    }

    public void setTot_disch_mty40(Integer tot_disch_mty40) {
        this.tot_disch_mty40 = tot_disch_mty40;
    }

    public Integer getTot_disch_load_teus() {
        return tot_disch_load_teus;
    }

    public void setTot_disch_load_teus(Integer tot_disch_load_teus) {
        this.tot_disch_load_teus = tot_disch_load_teus;
    }

    public Integer getTot_disch_mty_teus() {
        return tot_disch_mty_teus;
    }

    public void setTot_disch_mty_teus(Integer tot_disch_mty_teus) {
        this.tot_disch_mty_teus = tot_disch_mty_teus;
    }

    public Integer getBal_load20() {
        return bal_load20;
    }

    public void setBal_load20(Integer bal_load20) {
        this.bal_load20 = bal_load20;
    }

    public Integer getBal_load40() {
        return bal_load40;
    }

    public void setBal_load40(Integer bal_load40) {
        this.bal_load40 = bal_load40;
    }

    public Integer getBal_mty20() {
        return bal_mty20;
    }

    public void setBal_mty20(Integer bal_mty20) {
        this.bal_mty20 = bal_mty20;
    }

    public Integer getBal_mty40() {
        return bal_mty40;
    }

    public void setBal_mty40(Integer bal_mty40) {
        this.bal_mty40 = bal_mty40;
    }

    public Integer getBal_load_teus() {
        return bal_load_teus;
    }

    public void setBal_load_teus(Integer bal_load_teus) {
        this.bal_load_teus = bal_load_teus;
    }

    public Integer getBal_mty_teus() {
        return bal_mty_teus;
    }

    public void setBal_mty_teus(Integer bal_mty_teus) {
        this.bal_mty_teus = bal_mty_teus;
    }
}