package com.datasoft.IgmMis.Controller;

import com.datasoft.IgmMis.Model.DGInformation.DgContDischarge;

import com.datasoft.IgmMis.Model.DGInformation.DgContDischargeListByRotation;
import com.datasoft.IgmMis.Service.DGInformation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/DgInfo")
public class DGInformationController {

    @Autowired
    private DgContainerDeliveryReportService dgContainerDeliveryReportService;



    @Autowired
    private DgContDischargeService dgContDischargeService;

    @Autowired
    private DgContDischargeListForRotationService dgContDischargeListForRotationService;



    @Autowired
    private DGManifestService dgManifestService;

    @RequestMapping(value = "/DgDischarge/{Import_Rotation_No}", method = RequestMethod.GET)
    public @ResponseBody
    List<DgContDischarge> list(@PathVariable("Import_Rotation_No") String Import_Rotation_No){

        return dgContDischargeService.DgContDischargeList(Import_Rotation_No);
    }



    @RequestMapping(value = "/DgContDischargeListForRotation/{Import_Rotation_No}", method = RequestMethod.GET)
    public @ResponseBody
    List<DgContDischargeListByRotation> listOfDgInfoData(@PathVariable("Import_Rotation_No") String Import_Rotation_No) throws SQLException {

        return dgContDischargeListForRotationService.DgContDischargeForRotationList(Import_Rotation_No);
    }



    @RequestMapping(value = "/DgContainerDeliveryReport/{shift}/{rotation}/{fromdate}/{todate}",method = RequestMethod.GET)

    public @ResponseBody List DgContainerDeliveryReport(@PathVariable String shift,@PathVariable String rotation,@PathVariable String fromdate,@PathVariable String todate){

        String Rotation=rotation.replace('_','/');
        return dgContainerDeliveryReportService.DgContainerDeliveryReport(shift,Rotation,fromdate,todate);

    }



    @Autowired
    private DgContainerByRotationService dgContainerByRotationService;
    @RequestMapping(value = "/DgContainerByRotation/{rotation}",method = RequestMethod.GET)

    public @ResponseBody List DgContainerByRotation(@PathVariable String rotation){

        String Rotation=rotation.replace('_','/');
        return dgContainerByRotationService.DgContainerByRotation(Rotation);

    }




    @RequestMapping(value = "/DgManifestReport/{rotation}",method = RequestMethod.GET)
    public @ResponseBody List DgManifest(@PathVariable String rotation){
        String Rotation=rotation.replace('_','/');
        return dgManifestService.getDgManifest(Rotation);

    }

}
