package com.datasoft.IgmMis.Model.DGInformation;


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
public class DgNotifyParty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String notify_name;
    private String Address_1;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotify_name() {
        return notify_name;
    }

    public void setNotify_name(String notify_name) {
        this.notify_name = notify_name;
    }

    public String getAddress_1() {
        return Address_1;
    }

    public void setAddress_1(String address_1) {
        Address_1 = address_1;
    }
}
