package com.datasoft.IgmMis.Model.ShahinSpecialReport;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


public class ShahinLocationCertify {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ddl_imp_cont_no;
    private String ddl_imp_bl_no;


    private String Import_Rotation_No;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fcy_time_out;


    private String block;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fcy_time_in;
    private String fcy_last_pos_slot;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time_move;
    private String yard;
    private String fcy_position_name;
    private String sub_bl;
    private String totcont;
    private String cont;


    private Integer t20 ;
    private Integer t40 ;
    private Integer t45;

    private String cont_iso_type;
    private String cont_status;
    private  String off_dock_id;
    private String offdock_name;
    private  String vsl_name;
    private String cont_seal_number;
    private Integer cont_size;
    private String cont_height;


    public Integer getT20() {
        return t20;
    }

    public void setT20(Integer t20) {
        this.t20 = t20;
    }

    public Integer getT40() {
        return t40;
    }

    public void setT40(Integer t40) {
        this.t40 = t40;
    }

    public Integer getT45() {
        return t45;
    }

    public void setT45(Integer t45) {
        this.t45 = t45;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date time_mov;

    public Date getTime_mov() {
        return time_mov;
    }

    public void setTime_mov(Date time_mov) {
        this.time_mov = time_mov;
    }

    public String getTotcont() {
        return totcont;
    }

    public void setTotcont(String totcont) {
        this.totcont = totcont;
    }


    public String getSub_bl() {
        return sub_bl;
    }

    public void setSub_bl(String sub_bl) {
        this.sub_bl = sub_bl;
    }




    public String getYard() {
        return yard;
    }

    public void setYard(String yard) {
        this.yard = yard;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public Date getFcy_time_out() {
        return fcy_time_out;
    }

    public void setFcy_time_out(Date fcy_time_out) {
        this.fcy_time_out = fcy_time_out;
    }

    public Date getTime_move() {
        return time_move;
    }

    public void setTime_move(Date time_move) {
        this.time_move = time_move;
    }

    public Date getFcy_time_in() {
        return fcy_time_in;
    }

    public void setFcy_time_in(Date fcy_time_in) {
        this.fcy_time_in = fcy_time_in;
    }

    public String getFcy_last_pos_slot() {
        return fcy_last_pos_slot;
    }

    public void setFcy_last_pos_slot(String fcy_last_pos_slot) {
        this.fcy_last_pos_slot = fcy_last_pos_slot;
    }

    public String getFcy_position_name() {
        return fcy_position_name;
    }

    public void setFcy_position_name(String fcy_position_name) {
        this.fcy_position_name = fcy_position_name;
    }

    public String getCont_iso_type() {
        return cont_iso_type;
    }

    public void setCont_iso_type(String cont_iso_type) {
        this.cont_iso_type = cont_iso_type;
    }

    public String getCont_status() {
        return cont_status;
    }

    public void setCont_status(String cont_status) {
        this.cont_status = cont_status;
    }

    public String getOff_dock_id() {
        return off_dock_id;
    }

    public void setOff_dock_id(String off_dock_id) {
        this.off_dock_id = off_dock_id;
    }

    public String getOffdock_name() {
        return offdock_name;
    }

    public void setOffdock_name(String offdock_name) {
        this.offdock_name = offdock_name;
    }

    public String getVsl_name() {
        return vsl_name;
    }

    public void setVsl_name(String vsl_name) {
        this.vsl_name = vsl_name;
    }

    public String getCont_seal_number() {
        return cont_seal_number;
    }

    public void setCont_seal_number(String cont_seal_number) {
        this.cont_seal_number = cont_seal_number;
    }

    public Integer getCont_size() {
        return cont_size;
    }

    public void setCont_size(Integer cont_size) {
        this.cont_size = cont_size;
    }

    public String getCont_height() {
        return cont_height;
    }

    public void setCont_height(String cont_height) {
        this.cont_height = cont_height;
    }

    public String getDdl_imp_cont_no() {
        return ddl_imp_cont_no;
    }

    public void setDdl_imp_cont_no(String ddl_imp_cont_no) {
        this.ddl_imp_cont_no = ddl_imp_cont_no;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDdl_imp_bl_no() {
        return ddl_imp_bl_no;
    }

    public void setDdl_imp_bl_no(String ddl_imp_bl_no) {
        this.ddl_imp_bl_no = ddl_imp_bl_no;
    }

    public String getImport_Rotation_No() {
        return Import_Rotation_No;
    }

    public void setImport_Rotation_No(String import_Rotation_No) {
        Import_Rotation_No = import_Rotation_No;
    }
}

