package com.datasoft.IgmMis.Controller;

import com.datasoft.IgmMis.Model.ShahinSpecialReport.ShahinLocationCertify;
import com.datasoft.IgmMis.Service.ShahinSpecialReport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/ShahinSpecialReport")
public class ShahinSpecialReportController {
    @Autowired
    private EquipmentHandlingPerformanceHistoryService equipmentHandlingPerormanceHistoryService;

    @Autowired
    private ShahinLoadedContainerService shahinLoadedContainerService;


    @Autowired
    private ShahinLocationCertifyService shahinLocationCertifyService;


    @Autowired
    private EquipmentHandlingPerformaceRtgTimeWiseService equipmentHandlingPerformaceRtgTimeWiseService;

    @Autowired
    private UpdateVesselForExportContainersService updateVesselForExportContainersService;




    @Autowired
    private MloWiseFinalLodingExportReportAppsService mloWiseFinalLodingExportReportAppsService;

    @Autowired
    private IGMContainerListService igmContainerListService;



    @RequestMapping(value = "/ShahinLocationCertify",method = RequestMethod.PATCH)
    public @ResponseBody
    List getLoadedContainerList(@RequestBody ShahinLocationCertify shahinLocationCertify ){
        String ddl_imp_cont_no=shahinLocationCertify.getDdl_imp_cont_no();
        String ddl_imp_bl_no=shahinLocationCertify.getDdl_imp_bl_no();
        return shahinLocationCertifyService.getLoadedContainerList(ddl_imp_cont_no,ddl_imp_bl_no );
    }


    @RequestMapping(value = "/ShahinLocationCertify/{shift}/{fromdate}",method = RequestMethod.GET)
    public @ResponseBody
    List getLoadedContainerList(@PathVariable String shift, @PathVariable String fromdate){
        return shahinLocationCertifyService.getLoadedContainerList(shift,fromdate );
    }

    @RequestMapping(value = "/EquipmentHandlingPerformance/{shift}/{fromdate} /{todate}/{fromTime}/{toTime}",method = RequestMethod.GET)
    public @ResponseBody
    List exportEquipmentHandlingPerformanceRTG(@PathVariable String shift, @PathVariable String fromdate , @PathVariable String todate , @PathVariable String fromTime, @PathVariable String toTime){
        return equipmentHandlingPerormanceHistoryService.EquipmentHandlingPerformance(shift,fromdate ,todate,fromTime,toTime);
    }


    //Start ExportLoadedContainerList

    @RequestMapping(value = "/ShahinLoadedContainerVvd_Gkey/{rotaition_No}",method = RequestMethod.GET)
    @ResponseBody
    public Integer getGkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=shahinLoadedContainerService.get_vvdGkey(Rotation);
        return vvd_gkey;
    }
    @RequestMapping(value = "/ShahinLoadedContainerVoyNo/{import_Rotaition_No}",method = RequestMethod.GET)
    @ResponseBody
    public List getExportLoadedVoyNo(@PathVariable String import_Rotaition_No){

        String Rotation_Number=import_Rotaition_No.replace('_','/');
        return shahinLoadedContainerService.getVoyNo(Rotation_Number);
    }

    @RequestMapping(value = "/ShahinLoadedContainerVesselInfo/{Rotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List  getExportLoadedContainerInfo(@PathVariable String Rotaition){
        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=shahinLoadedContainerService.get_vvdGkey(rotation);
        return shahinLoadedContainerService.getVessleInformation(vvdgkey);
    }

    @RequestMapping(value = "/ShahinReportLoadedContainer/{importRotaition}/{fromdate}/{todate}/{toTime}/{fromTime}",method = RequestMethod.GET)
    @ResponseBody
    public List getExportLoadedContainer(@PathVariable String importRotaition,@PathVariable String fromdate,@PathVariable String todate,@PathVariable String fromTime,@PathVariable String toTime){
        String rotation_no=importRotaition.replace('_','/');
        Integer vvd_gkey=shahinLoadedContainerService.get_vvdGkey(rotation_no);
        return shahinLoadedContainerService.getLoadedContainerList(vvd_gkey,fromdate,todate,fromTime,toTime);
    }

    @RequestMapping(value = "/ShahinReportLoadedContainerSummeryOnboardReport/{importRotaition}/{fromdate}/{todate}/{toTime}/{fromTime}",method = RequestMethod.GET)
    @ResponseBody
    public List getExportLoadedContainerSummeryReport(@PathVariable String importRotaition,@PathVariable String fromdate,@PathVariable String todate,@PathVariable String fromTime,@PathVariable String toTime){
        String rotation_no=importRotaition.replace('_','/');
        Integer vvd_gkey=shahinLoadedContainerService.get_vvdGkey(rotation_no);
        return shahinLoadedContainerService.getLoadedContainerSummaryReport(vvd_gkey,fromdate,todate,fromTime,toTime);
    }

    @RequestMapping(value = "/ShahinReportLoadedContainerSummeryBalanceReportList/{importRotaition}/{fromdate}/{todate}/{toTime}/{fromTime}",method = RequestMethod.GET)
    @ResponseBody
    public List getExportLoadedContainerSummeryReportList(@PathVariable String importRotaition,@PathVariable String fromdate,@PathVariable String todate,@PathVariable String fromTime,@PathVariable String toTime){
        String rotation_no=importRotaition.replace('_','/');
        Integer vvd_gkey=shahinLoadedContainerService.get_vvdGkey(rotation_no);
        return shahinLoadedContainerService.getLoadedContainerSummaryReportList(vvd_gkey,fromdate,todate,fromTime,toTime);
    }


    //End ExportLoadedContainerList


    @RequestMapping(value = "/EquipmentHandlingPerformaceRtgTimeWise/{fromDate}/{shift}", method= RequestMethod.GET)
    @ResponseBody
    public List equipmentHandlingPerformaceRtgTimeWise(@PathVariable String fromDate, @PathVariable String shift)  {
        List list =new ArrayList<>();

        list=equipmentHandlingPerformaceRtgTimeWiseService.equipmentHandlingPerformaceRtgTimeWise(fromDate,shift);

        return list;
    }


    @RequestMapping(value = "/UpdateVesselForExportContainer/{preRotation}/{newRotation}/{containers}", method= RequestMethod.GET)
    @ResponseBody
    public List updateVesselForExportContainer(@PathVariable String preRotation, @PathVariable String newRotation, @PathVariable String containers)  {
        List list =new ArrayList<>();
        String message="";
        String previousRotation="";
        String latestRotation="";
        previousRotation=preRotation.replace('-','/');
        latestRotation=newRotation.replace('-','/');
        list=updateVesselForExportContainersService.updateVesselForExportContainers(previousRotation,latestRotation,containers);
        return list;

    }


    //////////////StartShahinLoadedContainer//////////



    @RequestMapping(value = "/ExportVesselListVvd_Gkey/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public Integer getVesselListvvdGkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=mloWiseFinalLodingExportReportAppsService.getVesselListvvdGkey(Rotation);
        return vvd_gkey;
    }


    @RequestMapping(value = "/ExportReportVessleInformation/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List getVessleListInformation(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=mloWiseFinalLodingExportReportAppsService.getVesselListvvdGkey(rotation);
        return mloWiseFinalLodingExportReportAppsService.getVessleInformation(vvdgkey);

    }




    @RequestMapping(value = "/ShahinVesselListWithStatusForExportUploadReport/{Rotaition}/{fromdate}/{todate}/{toTime}/{fromTime}",method = RequestMethod.GET)
    public   @ResponseBody List  getVessleListWithStatusInfoForExportUploadReport(@PathVariable String Rotaition,@PathVariable String fromdate,@PathVariable String todate,@PathVariable String fromTime,@PathVariable String toTime){
        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=mloWiseFinalLodingExportReportAppsService.getVesselListvvdGkey(rotation);
        return  mloWiseFinalLodingExportReportAppsService.getMloInfoForExportUploadReport(vvdgkey,fromdate,todate,fromTime,toTime);
    }


    @RequestMapping(value = "/ShahinReportContainerLoadingReport/{importRotaition}/{fromdate}/{todate}/{toTime}/{fromTime}",method = RequestMethod.GET)
    public @ResponseBody List getShahinSpacialReportContainerLoading(@PathVariable String importRotaition,@PathVariable String fromdate,@PathVariable String todate,@PathVariable String fromTime,@PathVariable String toTime){
        String rotation_no=importRotaition.replace('_','/');
        Integer vvd_gkey=mloWiseFinalLodingExportReportAppsService.getVesselListvvdGkey(rotation_no);
        return mloWiseFinalLodingExportReportAppsService.getExportContainer(vvd_gkey,fromdate,todate,fromTime,toTime);
    }



    //End ShahinLoadedContainerList


    // IGM Container List
    @RequestMapping(value = "/IgmContainerListForShahinSpecialReport/{importRotaition}/{searchType}", method= RequestMethod.GET)
    @ResponseBody
    public List getIgmContainerList(@PathVariable String importRotaition, @PathVariable Integer searchType)  {
        List list =new ArrayList<>();
        String rotation="";
        rotation=importRotaition.replace('-','/');

        list=igmContainerListService.IGMContainerList(rotation,searchType);

        return list;
    }





}
