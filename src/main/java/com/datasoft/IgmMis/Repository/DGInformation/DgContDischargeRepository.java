package com.datasoft.IgmMis.Repository.DGInformation;


import com.datasoft.IgmMis.Model.DGInformation.DgContDischarge;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import javax.transaction.Transactional;
import java.util.List;

public interface DgContDischargeRepository extends CrudRepository<DgContDischarge,Long> {


  @Query(value = "select * from igm_details where Import_Rotation_No=:Import_Rotation_No", nativeQuery = true)
  public List[] igmdetailList(@Param("Import_Rotation_No") String Import_Rotation_No);

  @Query(value = "select Import_Rotation_No,Vessel_Name,Total_number_of_bols,Total_number_of_packages,Total_number_of_containers,Total_gross_mass from igm_masters \n" +
          "where Import_Rotation_No=:Import_Rotation_No", nativeQuery = true)
  public List[] igmmasterlList(@Param("Import_Rotation_No") String Import_Rotation_No);

  @Query(value = "SELECT igm_masters.Import_Rotation_No, igm_masters.Vessel_Name,Voy_No, Net_Tonnage, Port_of_Shipment,Port_of_Destination,Sailed_Year,Submitee_Org_Id,Name_of_Master,Organization_Name,is_Foreign,Vessel_Type,Actual_Berth,Actual_Berth_time\n" +
          "FROM igm_masters \n" +
          "LEFT JOIN organization_profiles ON organization_profiles.id=igm_masters.Submitee_Org_Id\n" +
          "LEFT JOIN vessels ON vessels.id=igm_masters.Vessel_Id\n" +
          "left join vessels_berth_detail on vessels_berth_detail.Import_Rotation_No=igm_masters.Import_Rotation_No\n" +
          "WHERE igm_masters.Import_Rotation_No=:Import_Rotation_No", nativeQuery = true)
  public List[] result_igm_master(@Param("Import_Rotation_No") String Import_Rotation_No);

  @Query(value = "SELECT DISTINCT submitee_org_id,organization_profiles.Organization_Name AS Organization_Name,organization_profiles.Agent_Code,mlocode AS mlocode\n" +
          "FROM igm_details \n" +
          "INNER JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id\n" +
          "INNER JOIN igm_detail_container  ON igm_detail_container.igm_detail_id=igm_details.id\n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '')", nativeQuery = true)
  public List[] result_igm_details(@Param("Import_Rotation_No") String Import_Rotation_No);


  String row_mlocode="";



  @Query(value = "select mlodescription,mlo_agent_code_ctms,agent_from,org_id\n" +
          "FROM mlo_detail \n" +
          "WHERE mlocode=:row_mlocode", nativeQuery = true)
  public List[] result_mlo_detail(@Param("row_mlocode") String row_mlocode);


  @Query(value = "SELECT COUNT(*) AS total FROM(SELECT DISTINCT cont_number FROM igm_detail_container\n" +
          "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR imco <> '' OR un <> '')\n" +
          "AND Submitee_Org_Id=:row_submitee_org_id  AND mlocode=:row_mlocode AND \n" +
          "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
          "AND off_dock_id <>'2592' AND cont_status NOT IN ('EMT','EMPTY','MT','ETY') AND cont_size =20 AND  igm_details.final_submit=1\n" +
          "\n" +
          "UNION\n" +
          "SELECT DISTINCT cont_number FROM igm_sup_detail_container \n" +
          "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
          "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id \n" +
          "WHERE igm_details.Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR imco <> '' OR un <> '')\n" +
          "AND igm_details.Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND \n" +
          "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND igm_details.type_of_igm<>'TS' \n" +
          "AND off_dock_id <>'2592' AND cont_status NOT IN ('EMT','EMPTY','MT','ETY') AND cont_size =20 AND  igm_details.final_submit=1) AS tbl", nativeQuery = true)
  public Integer result_igm_detail_container(@Param("Import_Rotation_No") String Import_Rotation_No,
                                             @Param("row_submitee_org_id") String row_submitee_org_id,
                                             @Param("row_mlocode") String row_mlocode);


  //public Integer result_igm_detail_container(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);




  @Query(value = "SELECT COUNT(*) AS total FROM(SELECT DISTINCT cont_number FROM igm_detail_container\n" +
          "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR imco <> '' OR un <> '')\n" +
          "AND Submitee_Org_Id=:row_submitee_org_id  AND mlocode=:row_mlocode AND \n" +
          "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
          "AND off_dock_id <>'2592' AND cont_status NOT IN ('EMT','EMPTY','MT','ETY') AND cont_size =20 AND  igm_details.final_submit=1\n" +
          "\n" +
          "UNION\n" +
          "SELECT DISTINCT cont_number FROM igm_sup_detail_container \n" +
          "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
          "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id \n" +
          "WHERE igm_details.Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR imco <> '' OR un <> '')\n" +
          "AND igm_details.Submitee_Org_Id=:row_submitee_org_id  AND mlocode=:row_mlocode AND \n" +
          "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND igm_details.type_of_igm<>'TS' \n" +
          "AND off_dock_id <>'2592' AND cont_status NOT IN ('EMT','EMPTY','MT','ETY') AND cont_size =20 AND  igm_details.final_submit=1) AS tbl\n", nativeQuery = true)


  @Transactional
  public Integer total1(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);



  @Query(value = "SELECT COUNT(*) AS total FROM(SELECT DISTINCT cont_number FROM igm_detail_container\n" +
          "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR imco <> '' OR un <> '')\n" +
          "AND Submitee_Org_Id=:row_submitee_org_id  AND mlocode=:row_mlocode AND \n" +
          "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
          "AND off_dock_id <>'2592' AND cont_status NOT IN ('EMT','EMPTY','MT','ETY') AND cont_size =40 AND  igm_details.final_submit=1\n" +
          "\n" +
          "UNION\n" +
          "SELECT DISTINCT cont_number FROM igm_sup_detail_container \n" +
          "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
          "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id \n" +
          "WHERE igm_details.Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR imco <> '' OR un <> '')\n" +
          "AND igm_details.Submitee_Org_Id=:row_submitee_org_id  AND mlocode=:row_mlocode AND \n" +
          "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND igm_details.type_of_igm<>'TS' \n" +
          "AND off_dock_id <>'2592' AND cont_status NOT IN ('EMT','EMPTY','MT','ETY') AND cont_size =40 AND  igm_details.final_submit=1) AS tbl", nativeQuery = true)
  public Integer result_igm_details_container(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);

  @Query(value = "SELECT COUNT(*) AS total FROM(SELECT DISTINCT cont_number FROM igm_detail_container\n" +
          "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR imco <> '' OR un <> '')\n" +
          "AND Submitee_Org_Id=:row_submitee_org_id  AND mlocode=:row_mlocode AND \n" +
          "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
          "AND off_dock_id <>'2592' AND cont_status NOT IN ('EMT','EMPTY','MT','ETY') AND cont_size =40 AND  igm_details.final_submit=1\n" +
          "\n" +
          "UNION\n" +
          "SELECT DISTINCT cont_number FROM igm_sup_detail_container \n" +
          "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
          "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id \n" +
          "WHERE igm_details.Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR imco <> '' OR un <> '')\n" +
          "AND igm_details.Submitee_Org_Id=:row_submitee_org_id  AND mlocode=:row_mlocode AND \n" +
          "cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND igm_details.type_of_igm<>'TS' \n" +
          "AND off_dock_id <>'2592' AND cont_status NOT IN ('EMT','EMPTY','MT','ETY') AND cont_size =40 AND  igm_details.final_submit=1) AS tbl", nativeQuery = true)
  public Integer total2(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);




  @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')AND type_of_igm<>'TS' \n" +
          "AND off_dock_id<>'2592' AND   (cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') AND cont_size =20 AND (cont_imo = '' AND cont_un = '' AND igm_details.final_submit=1)", nativeQuery = true)
  public Integer total3(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);



  @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') AND type_of_igm<>'TS' \n" +
          "AND off_dock_id<>'2592' AND  (cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') AND cont_size =40 AND (cont_imo = '' AND cont_un = '' AND igm_details.final_submit=1)", nativeQuery = true)
  public Integer total4(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);

  @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND cont_iso_type LIKE '%R%' AND cont_iso_type NOT IN ('DRY') AND type_of_igm<>'TS' \n" +
          "AND off_dock_id<>'2592'  AND cont_size =20 AND igm_details.final_submit=1", nativeQuery = true)
  public Integer total5(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);



  @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND cont_iso_type LIKE '%R%' AND cont_iso_type NOT IN ('DRY') AND type_of_igm<>'TS' \n" +
          "AND off_dock_id<>'2592'  AND cont_size =40 AND igm_details.final_submit=1", nativeQuery = true)
  public Integer total6(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);

  @Query(value = "SELECT COUNT(*) AS total FROM ( SELECT DISTINCT cont_number FROM igm_detail_container\n" +
          "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No  AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND igm_details.Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND cont_type NOT IN ('REFER','REEFER')AND type_of_igm<>'TS' \n" +
          "AND off_dock_id<>'2592'  AND cont_size =20 AND (cont_imo <> '' AND cont_un <> '' AND igm_details.final_submit=1)\n" +
          "\n" +
          "UNION\n" +
          "\n" +
          "SELECT DISTINCT cont_number FROM igm_sup_detail_container \n" +
          "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
          "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id \n" +
          "WHERE igm_details.Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR imco <> '' OR un <> '') AND\n" +
          "igm_details.Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND cont_type NOT IN ('REFER','REEFER')AND igm_details.type_of_igm<>'TS' \n" +
          "AND off_dock_id<>'2592'  AND cont_size =20 AND (cont_imo <> '' AND cont_un <> '' AND igm_details.final_submit=1)) AS tbl ", nativeQuery = true)
  public Integer total7(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);

  @Query(value = "SELECT COUNT(*) AS total FROM ( SELECT DISTINCT cont_number FROM igm_detail_container\n" +
          "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND igm_details.Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND cont_type NOT IN ('REFER','REEFER')AND type_of_igm<>'TS' \n" +
          "AND off_dock_id<>'2592'  AND cont_size =40 AND (cont_imo <> '' AND cont_un <> '' AND igm_details.final_submit=1)\n" +
          "\n" +
          "UNION\n" +
          "\n" +
          "SELECT DISTINCT cont_number FROM igm_sup_detail_container \n" +
          "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
          "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id \n" +
          "WHERE igm_details.Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR imco <> '' OR un <> '') AND\n" +
          "igm_details.Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND cont_type NOT IN ('REFER','REEFER')AND igm_details.type_of_igm<>'TS' \n" +
          "AND off_dock_id<>'2592'  AND cont_size =40 AND (cont_imo <> '' AND cont_un <> '' AND igm_details.final_submit=1)) AS tbl ", nativeQuery = true)
  public Integer total8(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);




  @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND type_of_igm='TS' AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
          "AND off_dock_id<>'2592'  AND cont_size =20 AND igm_details.final_submit=1", nativeQuery = true)
  public Integer total9(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);

  @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND type_of_igm='TS' AND cont_iso_type NOT IN('22R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
          "AND off_dock_id<>'2592' AND  cont_size =40 AND igm_details.final_submit=1\n", nativeQuery = true)
  public Integer total10(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);



  @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
          "AND off_dock_id='2592'  AND cont_size =20 AND igm_details.final_submit=1", nativeQuery = true)
  public Integer total11(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);



  @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
          "AND off_dock_id='2592' AND  cont_size =40 AND igm_details.final_submit=1", nativeQuery = true)
  public Integer total12(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);


  @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode  AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
          "AND cont_size >40 AND igm_details.final_submit=1 AND (cont_status <> 'EMT' AND cont_status <> 'Empty' AND cont_status <> 'MT' AND cont_status <> 'ETY')", nativeQuery = true)
  public Integer total13(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);



  @Query(value = "SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
          "WHERE Import_Rotation_No=:Import_Rotation_No AND (cont_imo <> '' OR igm_details.imco <> '' OR igm_details.un <> '') AND Submitee_Org_Id=:row_submitee_org_id AND mlocode=:row_mlocode AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
          "AND cont_size >40 AND igm_details.final_submit=1 AND (cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') ", nativeQuery = true)
  public Integer total14(@Param("Import_Rotation_No") String Import_Rotation_No,@Param("row_submitee_org_id") String row_submitee_org_id,@Param("row_mlocode") String row_mlocode);


}
