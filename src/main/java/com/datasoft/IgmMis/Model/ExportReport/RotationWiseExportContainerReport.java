package com.datasoft.IgmMis.Model.ExportReport;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor


public class RotationWiseExportContainerReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String vsl_visit_dtls_ib_vyg;
    private String v_name;
    private String PHASE;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")

    private Date ata;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date atd;

    private String bop;
    private String exp_cont;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVsl_visit_dtls_ib_vyg() {
        return vsl_visit_dtls_ib_vyg;
    }

    public void setVsl_visit_dtls_ib_vyg(String vsl_visit_dtls_ib_vyg) {
        this.vsl_visit_dtls_ib_vyg = vsl_visit_dtls_ib_vyg;
    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    public String getPHASE() {
        return PHASE;
    }

    public void setPHASE(String PHASE) {
        this.PHASE = PHASE;
    }

    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
    }

    public Date getAtd() {
        return atd;
    }

    public void setAtd(Date atd) {
        this.atd = atd;
    }

    public String getBop() {
        return bop;
    }

    public void setBop(String bop) {
        this.bop = bop;
    }

    public String getExp_cont() {
        return exp_cont;
    }

    public void setExp_cont(String exp_cont) {
        this.exp_cont = exp_cont;
    }
}
