package com.datasoft.IgmMis.Repository.SpecialReport;

import com.datasoft.IgmMis.Model.SpecialReport.FeederDischargeSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeederDischargeSummaryRepository extends JpaRepository<FeederDischargeSummary, Long> {
    @Query(value = "select * from igm_details where Import_Rotation_No=:rot", nativeQuery = true)
    public List[] cntIgmDetails(@Param("rot") String rot);

    @Query(value = "SELECT Import_Rotation_No,Vessel_Name,Total_number_of_bols,Total_number_of_packages,\n" +
            "Total_number_of_containers,Total_gross_mass,Voy_No \n" +
            "FROM igm_masters WHERE Import_Rotation_No=:rot", nativeQuery = true)
    public List[] igmMaster(@Param("rot") String rot);

    @Query(value = "SELECT igm_masters.Import_Rotation_No,igm_masters.Vessel_Name,Voy_No,Net_Tonnage,Port_of_Shipment,Port_of_Destination,Sailed_Year,Submitee_Org_Id,\n" +
            "Name_of_Master,Organization_Name,is_Foreign,Vessel_Type,Actual_Berth,Actual_Berth_time\n" +
            "FROM igm_masters \n" +
            "LEFT JOIN organization_profiles ON organization_profiles.id=igm_masters.Submitee_Org_Id\n" +
            "LEFT JOIN vessels ON vessels.id=igm_masters.Vessel_Id\n" +
            "LEFT JOIN vessels_berth_detail ON vessels_berth_detail.Import_Rotation_No=igm_masters.Import_Rotation_No\n" +
            "WHERE igm_masters.Import_Rotation_No=:rot", nativeQuery = true)
    public List[] result_igm_master1(@Param("rot") String rot);

    @Query(value = "SELECT DISTINCT submitee_org_id,organization_profiles.Organization_Name AS Organization_Name,organization_profiles.Agent_Code,mlocode AS mlocode \n" +
            "FROM igm_details \n" +
            "INNER JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id \n" +
            "WHERE Import_Rotation_No=:rot ORDER BY Organization_Name,mlocode", nativeQuery = true)
    public List[] mloInfo(@Param("rot") String rot);

    @Query(value = "SELECT mlodescription,mlo_agent_code_ctms,agent_from,org_id FROM mlo_detail WHERE mlocode=:mlocode", nativeQuery = true)
    public List[] sql_agent_code(@Param("mlocode") String mlocode);

    @Query(value = "SELECT mlo_agent_code_ctms AS agent_code FROM mlo_detail WHERE mlocode=:mlocode", nativeQuery = true)
    public String agentCode(@Param("mlocode") String mlocode);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id <>'2592' AND cont_status NOT IN ('EMT','EMPTY','MT','ETY') AND cont_size =20 AND  igm_details.final_submit=1", nativeQuery = true)
    public Integer laden20FirstQuery(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id<>'2592'  AND cont_size =20 AND (cont_imo <> '' AND cont_un <> '' AND igm_details.final_submit=1)", nativeQuery = true)
    public Integer laden20SecondQuery(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id<>'2592' AND cont_status NOT IN ('EMT','EMPTY','MT','ETY') AND cont_size =40 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer laden40FirstQuery(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND type_of_igm<>'TS' AND off_dock_id<>'2592' AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
            "AND  cont_size =40 AND (cont_imo <> '' OR cont_un <> '') AND igm_details.final_submit=1", nativeQuery = true)
    public Integer laden40SecondQuery(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
            "AND type_of_igm<>'TS' AND off_dock_id<>'2592' AND   \n" +
            "(cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') \n" +
            "AND cont_size =20 AND (cont_imo = '' AND cont_un = '' AND igm_details.final_submit=1)", nativeQuery = true)
    public Integer empty20Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND \n" +
            "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
            "AND type_of_igm<>'TS' AND off_dock_id<>'2592' AND  \n" +
            "(cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') AND cont_size =40 \n" +
            "AND (cont_imo = '' AND cont_un = '' AND igm_details.final_submit=1)", nativeQuery = true)
    public Integer empty40Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_iso_type LIKE '%R%' AND cont_iso_type NOT IN ('DRY') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id<>'2592'  AND cont_size =20 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer reefer20Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_iso_type LIKE '%R%' AND cont_iso_type NOT IN ('DRY') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id<>'2592'  AND cont_size =40 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer reefer40Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_type NOT IN ('REFER','REEFER')AND type_of_igm<>'TS' \n" +
            "AND off_dock_id<>'2592'  AND cont_size =20 AND (cont_imo <> '' AND cont_un <> '' AND igm_details.final_submit=1)", nativeQuery = true)
    public Integer imdg20Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND type_of_igm<>'TS' AND off_dock_id<>'2592' AND \n" +
            "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
            "AND  cont_size =40 AND (cont_imo <> '' OR cont_un <> '') AND igm_details.final_submit=1", nativeQuery = true)
    public Integer imdg40Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND type_of_igm='TS' \n" +
            "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
            "AND off_dock_id<>'2592'  AND cont_size =20 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer trans20Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND type_of_igm='TS' AND \n" +
            "cont_iso_type NOT IN('22R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
            "AND off_dock_id<>'2592' AND  cont_size =40 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer trans40Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND \n" +
            "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
            "AND off_dock_id='5235'  AND cont_size =20 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer pangaon20Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND \n" +
            "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
            "AND off_dock_id='5235' AND  cont_size =40 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer pangaon40Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND \n" +
            "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
            "AND off_dock_id='2592'  AND cont_size =20 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer icd20Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND \n" +
            "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
            "AND off_dock_id='2592' AND  cont_size =40 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer icd40Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND \n" +
            "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
            "AND cont_size >40 AND igm_details.final_submit=1 AND (cont_status <> 'EMT' AND cont_status <> 'Empty' AND cont_status <> 'MT' AND cont_status <> 'ETY')", nativeQuery = true)
    public Integer l45Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode AND \n" +
            "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
            "AND cont_size >40 AND igm_details.final_submit=1 AND   \n" +
            "(cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') ", nativeQuery = true)
    public Integer e45Query(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT\n" +
            "IFNULL(SUM(fcl_20),0) AS fcl_20,\n" +
            "IFNULL(SUM(fcl_40),0) AS fcl_40,\n" +
            "IFNULL(SUM(fcl_tues),0) AS fcl_tues,\n" +
            "IFNULL(SUM(fcl_20),0)+IFNULL(SUM(fcl_40),0) AS fcl_box,\n" +
            "\n" +
            "IFNULL(SUM(lcl_20),0) AS lcl_20,\n" +
            "IFNULL(SUM(lcl_40),0) AS lcl_40,\n" +
            "IFNULL(SUM(lcl_tues),0) AS lcl_tues,\n" +
            "IFNULL(SUM(lcl_20),0)+IFNULL(SUM(lcl_40),0) AS lcl_box,\n" +
            "\n" +
            "IFNULL(SUM(icd_20),0) AS icd_20,\n" +
            "IFNULL(SUM(icd_40),0) AS icd_40,\n" +
            "IFNULL(SUM(icd_tues),0) AS icd_tues,\n" +
            "IFNULL(SUM(icd_20),0)+IFNULL(SUM(icd_40),0) AS icd_box,\n" +
            "\n" +
            "IFNULL(SUM(ref_20),0) AS ref_20,\n" +
            "IFNULL(SUM(ref_40),0) AS ref_40,\n" +
            "IFNULL(SUM(ref_tues),0) AS ref_tues,\n" +
            "IFNULL(SUM(ref_20),0)+IFNULL(SUM(ref_40),0) AS ref_box,\n" +
            "\n" +
            "IFNULL(SUM(png_20),0) AS png_20,\n" +
            "IFNULL(SUM(png_40),0) AS png_40,\n" +
            "IFNULL(SUM(png_tues),0) AS png_tues,\n" +
            "IFNULL(SUM(png_20),0)+IFNULL(SUM(png_40),0) AS png_box,\n" +
            "\n" +
            "IFNULL(SUM(emp_20),0) AS emp_20,\n" +
            "IFNULL(SUM(emp_40),0) AS emp_40,\n" +
            "IFNULL(SUM(emp_tues),0) AS emp_tues,\n" +
            "IFNULL(SUM(emp_20),0)+IFNULL(SUM(emp_40),0) AS emp_box,\n" +
            "\n" +
            "IFNULL(SUM(dep_20),0) AS dep_20,\n" +
            "IFNULL(SUM(dep_40),0) AS dep_40,\n" +
            "IFNULL(SUM(dep_tues),0) AS dep_tues,\n" +
            "IFNULL(SUM(dep_20),0)+IFNULL(SUM(dep_40),0) AS dep_box\n" +
            "\n" +
            "FROM (\n" +
            "SELECT \n" +
            "(CASE WHEN cont_size =20 AND cont_status='FCL' AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id=2591 THEN 1  \n" +
            "ELSE NULL END) AS fcl_20, \n" +
            "(CASE WHEN cont_size > 20 AND cont_status = 'FCL' AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id=2591 THEN 1  \n" +
            "ELSE NULL END) AS fcl_40,\n" +
            "(CASE WHEN cont_size=20 AND cont_status = 'FCL' AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id=2591 THEN 1 \n" +
            "ELSE (CASE WHEN cont_size>20 AND cont_status = 'FCL' AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id=2591 THEN 2 ELSE NULL END) END) AS fcl_tues,\n" +
            "\n" +
            "(CASE WHEN cont_size =20 AND cont_status='LCL' THEN 1  \n" +
            "ELSE NULL END) AS lcl_20, \n" +
            "(CASE WHEN cont_size > 20 AND cont_status = 'LCL'  THEN 1  \n" +
            "ELSE NULL END) AS lcl_40,\n" +
            "(CASE WHEN cont_size=20 AND cont_status = 'LCL' THEN 1 \n" +
            "ELSE (CASE WHEN cont_size>20 AND cont_status = 'LCL' THEN 2 ELSE NULL END) END) AS lcl_tues,\n" +
            "\n" +
            "(CASE WHEN cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND off_dock_id='2592'  AND cont_size =20 THEN 1  \n" +
            "ELSE NULL END) AS icd_20, \n" +
            "(CASE WHEN cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND off_dock_id='2592'  AND cont_size  > 20  THEN 1  \n" +
            "ELSE NULL END) AS icd_40,\n" +
            "(CASE WHEN cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND off_dock_id='2592'  AND cont_size =20 THEN 1 \n" +
            "ELSE (CASE WHEN cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND off_dock_id='2592'  AND cont_size >20 THEN 2 ELSE NULL END) END) AS icd_tues,\n" +
            "\n" +
            "(CASE WHEN cont_iso_type LIKE '%R%' AND cont_iso_type NOT IN ('DRY') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id<>'2592'  AND cont_size =20 THEN 1  \n" +
            "ELSE NULL END) AS ref_20, \n" +
            "(CASE WHEN cont_iso_type LIKE '%R%' AND cont_iso_type NOT IN ('DRY') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id<>'2592'  AND cont_size  > 20  THEN 1  \n" +
            "ELSE NULL END) AS ref_40,\n" +
            "(CASE WHEN cont_iso_type LIKE '%R%' AND cont_iso_type NOT IN ('DRY') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id<>'2592'  AND cont_size =20 THEN 1 \n" +
            "ELSE (CASE WHEN cont_iso_type LIKE '%R%' AND cont_iso_type NOT IN ('DRY') AND type_of_igm<>'TS' \n" +
            "AND off_dock_id<>'2592'  AND cont_size >20 THEN 2 ELSE NULL END) END) AS ref_tues,\n" +
            "\n" +
            "(CASE WHEN off_dock_id='5235'  AND cont_size =20 THEN 1  \n" +
            "ELSE NULL END) AS png_20, \n" +
            "(CASE WHEN off_dock_id='5235'  AND cont_size  > 20  THEN 1  \n" +
            "ELSE NULL END) AS png_40,\n" +
            "(CASE WHEN off_dock_id='5235'  AND cont_size =20 THEN 1 \n" +
            "ELSE (CASE WHEN off_dock_id='5235'  AND cont_size >20 THEN 2 ELSE NULL END) END) AS png_tues,\n" +
            "\n" +
            "(CASE WHEN cont_size =20 AND (cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') THEN 1  \n" +
            "ELSE NULL END) AS emp_20, \n" +
            "(CASE WHEN cont_size  > 20 AND (cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') THEN 1  \n" +
            "ELSE NULL END) AS emp_40,\n" +
            "(CASE WHEN cont_size =20 AND (cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') THEN 1 \n" +
            "ELSE (CASE WHEN cont_size >20 AND (cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') THEN 2 ELSE NULL END) END) AS emp_tues,\n" +
            "\n" +
            "(CASE WHEN cont_size =20 AND off_dock_id NOT IN ('2591','2592','5235') THEN 1  \n" +
            "ELSE NULL END) AS dep_20, \n" +
            "(CASE WHEN cont_size  > 20 AND off_dock_id NOT IN ('2591','2592','5235') THEN 1  \n" +
            "ELSE NULL END) AS dep_40,\n" +
            "(CASE WHEN cont_size =20 AND off_dock_id NOT IN ('2591','2592','5235') THEN 1 \n" +
            "ELSE (CASE WHEN cont_size >20 AND off_dock_id NOT IN ('2591','2592','5235') THEN 2 ELSE NULL END) END) AS dep_tues\n" +
            "\n" +
            "\n" +
            "FROM igm_detail_container\n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE  Import_Rotation_No=:rot AND igm_details.final_submit=1\n" +
            ") AS tmp", nativeQuery = true)
    public List[] contTypeWiseQuantity(@Param("rot") String rot);

    @Query(value = "SELECT DISTINCT submitee_org_id,organization_profiles.Organization_Name AS Organization_Name,\n" +
            "organization_profiles.Agent_Code,mlocode AS mlocode \n" +
            "FROM igm_details \n" +
            "INNER JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id \n" +
            "WHERE Import_Rotation_No=:rot \n" +
            "ORDER BY Organization_Name,mlocode", nativeQuery = true)
    public List[] orgInfo(@Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total \n" +
            "FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
            "AND type_of_igm<>'TS' AND off_dock_id <>'2592' AND cont_status IN ('FCL') AND cont_size =20 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer totalFcl20(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total \n" +
            "FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
            "AND type_of_igm<>'TS' AND off_dock_id<>'2592' AND cont_status IN ('FCL') AND cont_size =40 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer totalFcl40(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total \n" +
            "FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
            "AND type_of_igm<>'TS' AND off_dock_id<>'2592' AND cont_status IN ('LCL') AND cont_size =20 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer totalLcl20(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total \n" +
            "FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
            "AND type_of_igm<>'TS' AND off_dock_id<>'2592' AND cont_status IN ('LCL') AND cont_size =40 AND igm_details.final_submit=1", nativeQuery = true)
    public Integer totalLcl40(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total \n" +
            "FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
            "AND off_dock_id='2592' AND cont_size =20 AND igm_details.final_submit=1\n", nativeQuery = true)
    public Integer totalIcd20(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total \n" +
            "FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
            "AND off_dock_id='2592' AND  cont_size =40 AND igm_details.final_submit=1\n", nativeQuery = true)
    public Integer totalIcd40(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total \n" +
            "FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
            "AND type_of_igm<>'TS' AND off_dock_id<>'2592' AND  (cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') \n" +
            "AND cont_size =20 AND (cont_imo = '' AND cont_un = '' AND igm_details.final_submit=1)\n", nativeQuery = true)
    public Integer totalEmpty20(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);

    @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total \n" +
            "FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "WHERE Import_Rotation_No=:rot AND Submitee_Org_Id=:submitee_org_id AND mlocode=:mlocode \n" +
            "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
            "AND type_of_igm<>'TS' AND off_dock_id<>'2592' AND (cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') \n" +
            "AND cont_size =40 AND (cont_imo = '' AND cont_un = '' AND igm_details.final_submit=1)", nativeQuery = true)
    public Integer totalEmpty40(@Param("mlocode") String mlocode, @Param("submitee_org_id") String submitee_org_id, @Param("rot") String rot);


}
