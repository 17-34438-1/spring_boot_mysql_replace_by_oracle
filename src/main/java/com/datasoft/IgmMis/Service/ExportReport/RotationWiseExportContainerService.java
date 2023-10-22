package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.RotationWiseExportContainer;
import com.datasoft.IgmMis.Repository.ExportReport.RotationWiseExportContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RotationWiseExportContainerService {
    @Autowired
    private RotationWiseExportContainerRepository rotationWiseExportContainerRepository;

    public List<RotationWiseExportContainer> RotationWiseExportContainerDetails(String Import_Rotation_No) {
        System.out.println("Rotation:"+Import_Rotation_No);
        List<RotationWiseExportContainer> rotationWiseExportContainer  = new ArrayList<>();
        String Organization_Name="";
        String cont_number="";
        String cont_size="";
        String cont_gross_weight="";
        String cont_weight="";
        String cont_seal_number="";
        String cont_status="";
        String off_dock_id="";
        String cont_imo="";
        String cont_un="";
        String commudity_code="";
        String cont_height="";
        String cont_iso_type="";
        String cont_type="";
        String mlocode="";
        String Port_Ship_ID="";
        String Vessel_Name="";
        String offdock_name="";
        String commodity="";



        List organization_profilesList[] = rotationWiseExportContainerRepository.Organization_profilesList(Import_Rotation_No);
        System.out.println(organization_profilesList.length);
        for (int i = 0; i < organization_profilesList.length; i++) {

            RotationWiseExportContainer container=new RotationWiseExportContainer();
            Organization_Name = organization_profilesList[i].get(0).toString();
            container.setOrganization_Name(Organization_Name);
            cont_number = organization_profilesList[i].get(1).toString();
            container.setCont_number(cont_number);
            cont_size = organization_profilesList[i].get(2).toString();
            container.setCont_size(cont_size);
            cont_gross_weight = organization_profilesList[i].get(3).toString();
            container.setCont_gross_weight(cont_gross_weight);
            cont_weight = organization_profilesList[i].get(4).toString();
            container.setCont_weight(cont_weight);
            cont_seal_number = organization_profilesList[i].get(5).toString();
            container.setCont_seal_number(cont_seal_number);
            cont_status = organization_profilesList[i].get(6).toString();
            container.setCont_status(cont_status);
            off_dock_id = organization_profilesList[i].get(7).toString();
            container.setOff_dock_id(off_dock_id);
            cont_imo = organization_profilesList[i].get(8).toString();
            container.setCont_imo(cont_imo);
            cont_un = organization_profilesList[i].get(9).toString();
            container.setCont_un(cont_un);
            commudity_code = organization_profilesList[i].get(10).toString();
            container.setCommudity_code(commudity_code);
            cont_height = organization_profilesList[i].get(11).toString();
            container.setCont_height(cont_height);
            cont_iso_type = organization_profilesList[i].get(12).toString();
            container.setCont_iso_type(cont_iso_type);
            cont_type = organization_profilesList[i].get(13).toString();
            container.setCont_type(cont_type);
            mlocode = organization_profilesList[i].get(14).toString();
            container.setMlocode(mlocode);
            Port_Ship_ID = organization_profilesList[i].get(15).toString();
            container.setPort_Ship_ID(Port_Ship_ID);
            Vessel_Name = organization_profilesList[i].get(16).toString();
            container.setVessel_Name(Vessel_Name);
            offdock_name = organization_profilesList[i].get(17).toString();
            container.setOff_dock_id(off_dock_id);

            rotationWiseExportContainer.add(container);
        }

        System.out.println("igm_masters_Vessel_Name:"+Vessel_Name );

        return  rotationWiseExportContainer;

    }
}