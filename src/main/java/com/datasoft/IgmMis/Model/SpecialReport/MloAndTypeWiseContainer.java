package com.datasoft.IgmMis.Model.SpecialReport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class MloAndTypeWiseContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    public String mloCode;
    public Integer fcl20;
    public Integer fcl40;
    public Integer lcl20;
    public Integer lcl40;
    public Integer icd20;
    public Integer icd40;
    public Integer empty20;
    public Integer empty40;
    public Integer box;
    public Integer teus;
}
