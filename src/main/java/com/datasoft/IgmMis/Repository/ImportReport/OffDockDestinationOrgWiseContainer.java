package com.datasoft.IgmMis.Repository.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.OffDockDestinationOrgWiseContainerModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffDockDestinationOrgWiseContainer extends CrudRepository<OffDockDestinationOrgWiseContainerModel,String> {
    @Query(value = "SELECT DISTINCT cont_number AS id,cont_size,cont_status,cont_height,Organization_Name,off_dock_id,mlocode FROM igm_detail_container \n" +
            "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
            "INNER JOIN organization_profiles ON igm_detail_container.off_dock_id= organization_profiles.id \n" +
            "WHERE Import_Rotation_No=:importRotation AND igm_detail_container.off_dock_id NOT IN ('2591','2592')\n" +
            "ORDER BY Organization_Name",nativeQuery = true)
    public List<OffDockDestinationOrgWiseContainerModel> getOffDockDestinationOrgWiseContainerList(@Param("importRotation") String importRotation);
}
