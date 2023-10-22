package com.datasoft.IgmMis.Model.DGInformation;


import com.fasterxml.jackson.annotation.JsonFormat;
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
public class DgContainerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String slot;
    private String mlo;
    private String berth;
    private Integer rl_no;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date rl_date;


    private Integer obpc_number;



    private String obpc_date;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date Delivery_Status_date;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time_in;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time_out;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date ata;




    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getBerth() {
        return berth;
    }

    public void setBerth(String berth) {
        this.berth = berth;
    }       

    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
    }

    public String getMlo() {
        return mlo;
    }

    public void setMlo(String mlo) {
        this.mlo = mlo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRl_no() {
        return rl_no;
    }

    public void setRl_no(Integer rl_no) {
        this.rl_no = rl_no;
    }

    public Date getRl_date() {
        return rl_date;
    }

    public void setRl_date(Date rl_date) {
        this.rl_date = rl_date;
    }

    public Integer getObpc_number() {
        return obpc_number;
    }

    public void setObpc_number(Integer obpc_number) {
        this.obpc_number = obpc_number;
    }

    public String getObpc_date() {
        return obpc_date;
    }

    public void setObpc_date(String obpc_date) {
        this.obpc_date = obpc_date;
    }

    public Date getTime_in() {
        return time_in;
    }

    public void setTime_in(Date time_in) {
        this.time_in = time_in;
    }

    public Date getTime_out() {
        return time_out;
    }

    public void setTime_out(Date time_out) {
        this.time_out = time_out;
    }
}
