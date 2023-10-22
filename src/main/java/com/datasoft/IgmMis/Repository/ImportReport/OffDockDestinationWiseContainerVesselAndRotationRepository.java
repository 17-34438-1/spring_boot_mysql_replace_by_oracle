package com.datasoft.IgmMis.Repository.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.OffDockDestinationWiseContainerVesselAndRotationModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffDockDestinationWiseContainerVesselAndRotationRepository extends CrudRepository<OffDockDestinationWiseContainerVesselAndRotationModel,String> {
    @Query(value = "SELECT Import_Rotation_No,Vessel_Name FROM igm_masters \n" +
            "WHERE igm_masters.Import_Rotation_No=:importRotation",nativeQuery = true)
    public List<OffDockDestinationWiseContainerVesselAndRotationModel> getOffDockDestinationWiseContainerVesselAndRotation(@Param("importRotation") String importRotation);
}
