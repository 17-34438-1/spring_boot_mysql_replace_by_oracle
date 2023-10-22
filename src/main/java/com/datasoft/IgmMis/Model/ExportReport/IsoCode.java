package com.datasoft.IgmMis.Model.ExportReport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class IsoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore

    private Long id;


    private String cont_iso_type;
    private String cont_size;
    private String cont_height;
    private String cont_type;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCont_iso_type() {
        return cont_iso_type;
    }

    public void setCont_iso_type(String cont_iso_type) {
        this.cont_iso_type = cont_iso_type;
    }

    public String getCont_size() {
        return cont_size;
    }

    public void setCont_size(String cont_size) {
        this.cont_size = cont_size;
    }

    public String getCont_height() {
        return cont_height;
    }

    public void setCont_height(String cont_height) {
        this.cont_height = cont_height;
    }

    public String getCont_type() {
        return cont_type;
    }

    public void setCont_type(String cont_type) {
        this.cont_type = cont_type;
    }
}
