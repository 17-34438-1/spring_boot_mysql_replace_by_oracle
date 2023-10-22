package com.datasoft.IgmMis.Repository.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.RotationWiseExportContainer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public  interface RotationWiseExportContainerRepository extends CrudRepository<RotationWiseExportContainer,Long> {

    @Query(value = "select igm_masters.Vessel_Name from igm_masters where Import_Rotation_No=:Import_Rotation_No", nativeQuery = true)
    public List[] igmmastersList(@Param("Import_Rotation_No") String Import_Rotation_No);





    @Query(value = "select distinct Organization_Name,cont_number,cont_size,cont_gross_weight,\n" +
            "cont_weight,cont_seal_number,cont_status,off_dock_id,cont_imo,\n" +
            "cont_un,commudity_code,cont_height,cont_iso_type,cont_type,mlocode,igm_masters.Port_Ship_ID,\n" +
            "REPLACE(Vessel_Name,' ','_' ) AS Vessel_Name,\n" +
            "(select Organization_Name from organization_profiles\n" +
            "where organization_profiles.id=igm_detail_container.off_dock_id) as offdock_name,\n" +
            "(select commudity_desc from commudity_detail where commudity_detail.commudity_code=\n" +
            "igm_detail_container.commudity_code) as commodity from igm_detail_container inner join \n" +
            "igm_details on igm_details.id=igm_detail_container.igm_detail_id inner join igm_masters on \n" +
            "igm_details.IGM_id=igm_masters.id inner join organization_profiles on organization_profiles.id=\n" +
            "igm_detail_container.org_id where igm_details.Import_Rotation_No=:Import_Rotation_No", nativeQuery = true)
    public List[] Organization_profilesList(@Param("Import_Rotation_No") String Import_Rotation_No);


}