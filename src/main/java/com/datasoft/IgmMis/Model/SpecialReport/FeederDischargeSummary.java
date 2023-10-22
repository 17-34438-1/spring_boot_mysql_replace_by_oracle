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
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class FeederDischargeSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String rotation;
    private String vesselName;
    private String totalBls;
    private String totalPackage;
    private String totalContainers;
    private String totalGrossMass;
    private String voyageNo;
    private Integer allMlo;
    private String agentName;
    private String message;

    private String fcl20;
    private String fcl40;
    private String fclBox;
    private String fclTeus;

    private String lcl20;
    private String lcl40;
    private String lclBox;
    private String lclTeus;

    private String icd20;
    private String icd40;
    private String icdBox;
    private String icdTeus;

    private String reefer20;
    private String reefer40;
    private String reeferBox;
    private String reeferTeus;

    private String png20;
    private String png40;
    private String pngBox;
    private String pngTeus;

    private String depot20;
    private String depot40;
    private String depotBox;
    private String depotTeus;

    private String empty20;
    private String empty40;
    private String emptyBox;
    private String emptyTeus;

    private Integer total20;
    private Integer total40;
    private Integer totalBox;
    private Integer totalTeus;

    private String formatType;

    private Integer totalLaden20;
    private Integer totalLaden40;
    private Integer totalEmpty20;
    private Integer totalEmpty40;
    private Integer totalReffer20;
    private Integer totalReffer40;
    private Integer totalImdg20;
    private Integer totalImdg40;
    private Integer totalTrans20;
    private Integer totalTrans40;
    private Integer totalPangaon20;
    private Integer totalPangaon40;
    private Integer totalIcd20;
    private Integer totalIcd40;
    private Integer totalL45;
    private Integer totalE45;
    private Integer mloWiseTotalContainerQty;

    private Integer mloAndTypeWiseTotalFcl20;
    private Integer mloAndTypeWiseTotalFcl40;
    private Integer mloAndTypeWiseTotalLcl20;
    private Integer mloAndTypeWiseTotalLcl40;
    private Integer mloAndTypeWiseTotalIcd20;
    private Integer mloAndTypeWiseTotalIcd40;
    private Integer mloAndTypeWiseTotalEmpty20;
    private Integer mloAndTypeWiseTotalEmpty40;
    private Integer mloAndTypeWiseTotalBox;
    private Integer mloAndTypeWiseTotalTeus;


    ArrayList<MloWiseContainerInfo> mloWiseContainerInfos;
    ArrayList<MloAndTypeWiseContainer> mloAndTypeWiseContainers;
}
