package com.datasoft.IgmMis.Model.ImportReport;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OffDockDestinationWiseContainerVesselAndRotationModel {
    @Id
    private String Import_Rotation_No;
    private String Vessel_Name;

    public String getImport_Rotation_No() {
        return Import_Rotation_No;
    }

    public void setImport_Rotation_No(String import_Rotation_No) {
        Import_Rotation_No = import_Rotation_No;
    }

    public String getVessel_Name() {
        return Vessel_Name;
    }

    public void setVessel_Name(String vessel_Name) {
        Vessel_Name = vessel_Name;
    }
}
