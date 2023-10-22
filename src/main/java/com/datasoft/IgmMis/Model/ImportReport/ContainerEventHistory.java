package com.datasoft.IgmMis.Model.ImportReport;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
@Entity
public class ContainerEventHistory {
    @Id
    private Integer gkey;
    private String time_move;
    private String time_in;
    private String time_out;
    private String category;
    private String freight_kind;
    private String transit_state;
    private String last_pos_name;
    private String mlo;

    public Integer getGkey() {
        return gkey;
    }

    public void setGkey(Integer gkey) {
        this.gkey = gkey;
    }

    public String getTime_move() {
        return time_move;
    }

    public void setTime_move(String time_move) {
        this.time_move = time_move;
    }

    public String getTime_in() {
        return time_in;
    }

    public void setTime_in(String time_in) {
        this.time_in = time_in;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFreight_kind() {
        return freight_kind;
    }

    public void setFreight_kind(String freight_kind) {
        this.freight_kind = freight_kind;
    }

    public String getTransit_state() {
        return transit_state;
    }

    public void setTransit_state(String transit_state) {
        this.transit_state = transit_state;
    }

    public String getLast_pos_name() {
        return last_pos_name;
    }

    public void setLast_pos_name(String last_pos_name) {
        this.last_pos_name = last_pos_name;
    }

    public String getMlo() {
        return mlo;
    }

    public void setMlo(String mlo) {
        this.mlo = mlo;
    }
}
