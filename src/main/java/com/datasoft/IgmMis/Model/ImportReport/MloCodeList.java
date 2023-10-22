package com.datasoft.IgmMis.Model.ImportReport;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MloCodeList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //    @JsonIgnore
    private String mlocode;

    public String getMlocode() {
        return mlocode;
    }

    public void setMlocode(String mlocode) {
        this.mlocode = mlocode;
    }
}
