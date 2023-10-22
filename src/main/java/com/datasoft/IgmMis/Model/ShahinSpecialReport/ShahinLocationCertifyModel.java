package com.datasoft.IgmMis.Model.ShahinSpecialReport;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class ShahinLocationCertifyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String sub_bl;
    private String yard;
    private String block;
    private String fcy_last_pos_slot;

    private String fcy_position_name;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fcy_time_out;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time_move;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fcy_time_in;

    public String getSub_bl() {
        return sub_bl;
    }

    public void setSub_bl(String sub_bl) {
        this.sub_bl = sub_bl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
