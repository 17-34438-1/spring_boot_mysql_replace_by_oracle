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
public class MloWiseContainerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String agentCode;
    private String mloCode;
    private Integer laden20;
    private Integer laden40;
    private Integer empty20;
    private Integer empty40;
    private Integer reefer20;
    private Integer reefer40;
    private Integer imdg20;
    private Integer imdg40;
    private Integer trans20;
    private Integer trans40;
    private Integer pangaon20;
    private Integer pangaon40;
    private Integer Icd40;
    private Integer Icd20;
    private Integer l45;
    private Integer e45;
    private Integer mloWiseTotalContainer;


}
