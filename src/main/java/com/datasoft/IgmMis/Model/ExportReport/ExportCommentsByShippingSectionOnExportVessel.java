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
public class ExportCommentsByShippingSectionOnExportVessel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String vvd_gkey;
    private String name;
    private String ib_vyg;
    private String ob_vyg;
    private String agent;
    private String berthop;
    private String phase_str;
    private String phase_num;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date eta;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date etd;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date ata;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date atd;

    public Date getEta() {
        return eta;
    }

    private String comments;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date comments_time;

    private String pre_comments;
    private String comments_by;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date pre_comments_time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getBerthop() {
        return berthop;
    }

    public void setBerthop(String berthop) {
        this.berthop = berthop;
    }

    public String getPhase_str() {
        return phase_str;
    }

    public void setPhase_str(String phase_str) {
        this.phase_str = phase_str;
    }




    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }


    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public Date getComments_time() {
        return comments_time;
    }

    public void setComments_time(Date comments_time) {
        this.comments_time = comments_time;
    }

    public String getPre_comments() {
        return pre_comments;
    }

    public void setPre_comments(String pre_comments) {
        this.pre_comments = pre_comments;
    }

    public String getComments_by() {
        return comments_by;
    }

    public void setComments_by(String comments_by) {
        this.comments_by = comments_by;
    }

    public Date getPre_comments_time() {
        return pre_comments_time;
    }

    public void setPre_comments_time(Date pre_comments_time) {
        this.pre_comments_time = pre_comments_time;
    }

    public String getVvd_gkey() {
        return vvd_gkey;
    }

    public void setVvd_gkey(String vvd_gkey) {
        this.vvd_gkey = vvd_gkey;
    }

    public String getPhase_num() {
        return phase_num;
    }

    public void setPhase_num(String phase_num) {
        this.phase_num = phase_num;
    }


}