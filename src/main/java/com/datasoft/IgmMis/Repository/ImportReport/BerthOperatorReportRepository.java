package com.datasoft.IgmMis.Repository.ImportReport;


import com.datasoft.IgmMis.Model.ImportReport.BerthOperatorReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BerthOperatorReportRepository extends CrudRepository<BerthOperatorReport,Long>{
    @Query(value = "SELECT DISTINCT mlocode FROM cchaportdb.igm_details WHERE Import_Rotation_No=:rotation", nativeQuery = true)
    List mloCodeByRotation(@Param("rotation") String rotation);

    @Query(value = "select Vessel_Name from igm_masters where Import_Rotation_No=:rotation", nativeQuery = true)
    List vslName(@Param("rotation") String rotation);

    @Query(value = "SELECT cont_imo AS imco,cont_un AS un,igm_details.Pack_Number,igm_details.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_detail_container.off_dock_id) AS depu,SUBSTRING(igm_details.Pack_Marks_Number, 1, 3) AS Pack_Marks_Number,igm_detail_container.id,igm_details.Import_Rotation_No,igm_details.Line_No,igm_details.BL_No,igm_details.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_details.Description_of_Goods AS dg,cont_status,cont_height,cont_type,mlocode, " +
            "Cont_gross_weight,Organization_Name,IFNULL(commudity_desc,'') AS commudity_desc,final_amendment,response_details1, " +
            "firstapprovaltime,response_details2,secondapprovaltime,response_details3,thirdapprovaltime,hold_application, " +
            "rejected_application,hold_date,rejected_date,'D' AS TYPE,master_Line_No " +
            "FROM igm_details " +
            "LEFT JOIN igm_detail_container ON igm_details.id=igm_detail_container.igm_detail_id " +
            "LEFT JOIN commudity_detail ON igm_detail_container.commudity_code=commudity_detail.commudity_code " +
            "LEFT JOIN igm_navy_response ON igm_details.id=igm_navy_response.igm_details_id " +
            "LEFT JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id " +
            "LEFT JOIN igm_supplimentary_detail ON igm_details.id=igm_supplimentary_detail.igm_detail_id WHERE igm_details.Import_Rotation_No LIKE :impRot " +
            "AND igm_detail_container.port_status IN('0','5') AND igm_supplimentary_detail.id IS NULL  AND igm_details.final_submit=1 " +
            "UNION " +
            "SELECT cont_imo AS imco,cont_un AS un,igm_supplimentary_detail.Pack_Number,igm_supplimentary_detail.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_sup_detail_container.off_dock_id) AS depu,SUBSTRING(igm_supplimentary_detail.Pack_Marks_Number, 1, 3) AS Pack_Marks_Number,igm_sup_detail_container.id,igm_supplimentary_detail.Import_Rotation_No,igm_supplimentary_detail.Line_No,igm_supplimentary_detail.BL_No,igm_supplimentary_detail.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_supplimentary_detail.Description_of_Goods AS dg,cont_status,cont_height,cont_type,(SELECT mlocode FROM igm_details WHERE id=igm_supplimentary_detail.igm_detail_id LIMIT 1) AS mlocode, " +
            "igm_supplimentary_detail.weight,Organization_Name,IFNULL(commudity_desc,'') AS commudity_desc,final_amendment,response_details1,firstapprovaltime,response_details2,secondapprovaltime,response_details3, " +
            "thirdapprovaltime,hold_application, " +
            "rejected_application,hold_date,rejected_date,'S' AS TYPE,master_Line_No " +
            "FROM igm_supplimentary_detail " +
            "LEFT JOIN igm_sup_detail_container ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id " +
            "LEFT JOIN commudity_detail ON igm_sup_detail_container.commudity_code=commudity_detail.commudity_code " +
            "LEFT JOIN igm_navy_response ON igm_supplimentary_detail.id=igm_navy_response.egm_details_id " +
            "LEFT JOIN organization_profiles ON igm_supplimentary_detail.Submitee_Org_Id=organization_profiles.id " +
            "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id " +
            "WHERE igm_supplimentary_detail.Import_Rotation_No LIKE :impRot " +
            "AND igm_sup_detail_container.port_status IN('0','5') " +
            "AND igm_supplimentary_detail.final_submit=1 ORDER BY cont_number,Organization_Name", nativeQuery = true)
    public List[] getBerthOptReport(@Param("impRot") String impRot);
}

