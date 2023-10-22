package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.OffDockDestinationDistrictWiseContainerModel;
import com.datasoft.IgmMis.Model.ImportReport.OffDockDestinationOrgWiseContainerModel;
import com.datasoft.IgmMis.Model.ImportReport.OffDockDestinationWiseContainerVesselAndRotationModel;
import com.datasoft.IgmMis.Repository.ImportReport.OffDockDestinationDistrictWiseContainerRepository;
import com.datasoft.IgmMis.Repository.ImportReport.OffDockDestinationOrgWiseContainer;
import com.datasoft.IgmMis.Repository.ImportReport.OffDockDestinationWiseContainerVesselAndRotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OffDockDestinationWiseContainerService{
    @Autowired
    private OffDockDestinationOrgWiseContainer offDockDestinationOrgWiseContainer;
    @Autowired
    private OffDockDestinationWiseContainerVesselAndRotationRepository offDockDestinationWiseContainerVesselAndRotationRepository;
    @Autowired
    OffDockDestinationDistrictWiseContainerRepository offDockDestinationDistrictWiseContainerRepository;

    public List<OffDockDestinationOrgWiseContainerModel>  getOffDockDestinationOrgWiseContainerList(String importRotation){
        List  resultList= new ArrayList<>();
        resultList=offDockDestinationOrgWiseContainer.getOffDockDestinationOrgWiseContainerList(importRotation);

        return resultList;

    }
    public List<OffDockDestinationWiseContainerVesselAndRotationModel> getOffDockDestinationWiseContainerVesselAndRotation(String importRotation){
        List<OffDockDestinationWiseContainerVesselAndRotationModel>  resultList=new ArrayList<>();
        resultList=offDockDestinationWiseContainerVesselAndRotationRepository.getOffDockDestinationWiseContainerVesselAndRotation(importRotation);

        return resultList;

    }

    public List<OffDockDestinationDistrictWiseContainerModel> getOffDockDestinationDistrictWiseContainerList(String improtRotation){
        //Integer offDockId=2591;
        List<OffDockDestinationDistrictWiseContainerModel> resultList=new ArrayList<>();
        resultList=offDockDestinationDistrictWiseContainerRepository.getOffDockDestinationDistrictWiseContainerList(improtRotation);

        return resultList;
    }

}

