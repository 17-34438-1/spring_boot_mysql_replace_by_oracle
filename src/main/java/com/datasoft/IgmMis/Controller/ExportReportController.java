package com.datasoft.IgmMis.Controller;

import com.datasoft.IgmMis.Model.ExportReport.*;
import com.datasoft.IgmMis.Model.ImportReport.ExportCopino;
import com.datasoft.IgmMis.Model.ResponseMessage;

import com.datasoft.IgmMis.Service.ExportReport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/ExportReport")
public class ExportReportController {
    @Autowired
    private PODService podListService;
   @Autowired
    private ExportMloWiseLoadedContainerService exportMloWiseLoadedContainerService;
    @Autowired
    private IsoCodeService isoCodeService;

    @Autowired
    private ExportCopinoService exportCopinoService;

    @Autowired
    private ExportContainerBlockReportService exportContainerBlockReportService;

    @Autowired
    private ExportContainerLoadingDetailService exportContainerLoadingDetailService;


    @Autowired
    private ExportDestinationWiseMloLoadedContainerListService exportDestinationWiseMloLoadedContainerListService;

    @Autowired
    private ExportLoadedContainerListLoadAndEmptyService exportLoadedContainerListLoadAndEmptyService;

    @Autowired
    private RotationWiseExportContainerReportService rotationWiseExportContainerReportService;

    @Autowired
    private ExportMloWiseExportSummaryService exportReportMloWiseExportSummaryService;

    @Autowired
    private ExportMloWiseExcelUploadedReportService exportMloWiseExcelUploadedReportService;

    @Autowired
    private ExportDateAndRotationWisePreAdvisedContainerService exportDateAndRotationWisePreAdvisedContainerService;

    @Autowired
    private ExportCommentsByShippingSectionOnExportVesselService exportCommentsByShippingSectionOnExportVesselService;


    @Autowired
    private ExportMloWisePreAdvisedLoadedContainerListService exportMloWisePreAdvisedLoadedContainerListService;



    @Autowired
    private RotationWiseExportContainerService rotationWiseExportContainerService;

    @Autowired
    private ExportVesselListWithStatusService exportVesselListWithStatusService;

    @Autowired
    private ExportContainerToBeLoadedListService exportContainerToBeLoadedListService;

    @Autowired
    private ExportLoadedContainerListService exportLoadedContainerListService;


    @Autowired
    private ExportEquipmentHandlingPerformanceRTGService exportEquipmentHandlingPerformanceRTGService;



    @Autowired
    private ExportContainerLoadingReportService exportContainerLoadingReportService;


    //Export Container Bay View
    @Autowired
    private ExportContainerBayViewService exportContainerBayViewService;

    //Export Blank Bay View
    @Autowired
    private ExportBlankBayViewService exportBlankBayViewService;



    @Autowired
    private ExportContainerNotFoundReportService exportContainerNotFoundReportService;


    //Upload Export Container
    @Autowired
    private UploadExportContainerExceFileService uploadExportContainerExceFileService;



    ResponseMessage responseMessage;

    @RequestMapping(value = "/podlist", method = RequestMethod.GET)
    public @ResponseBody
    Object podList(){
        List<Object> podList = new ArrayList<Object>();
        String exception = null;
        try {
            podList = podListService.PODList();
            return podList;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return podList;
    }

    @RequestMapping(value = "/PodListByPlaceCode/{place_code}", method = RequestMethod.GET)
    public @ResponseBody List<PODList> listTest(@PathVariable("place_code") String place_code) throws SQLException {

        return podListService.podListByPlaceCode(place_code);
    }

    @RequestMapping(value = "/IsoList",method = RequestMethod.GET)
    public @ResponseBody List<IsoCode> list()throws SQLException{
        return isoCodeService.IsoCodeList();
    }

    @RequestMapping(value = "/IsoCodeData/{cont_iso_type}",method = RequestMethod.GET)
    public @ResponseBody List<IsoCode> listOfIsoData(@PathVariable("cont_iso_type") String cont_iso_type){
        return isoCodeService.IsoCodeListFor_cont_iso_type(cont_iso_type);
    }

    @RequestMapping(value = "/copino/{Import_Rotation_No}",method = RequestMethod.GET)
    public @ResponseBody List<ExportCopino>ListofCopinoData(@PathVariable("Import_Rotation_No")String Import_Rotation_No) throws SQLException{
        return exportCopinoService.ExportCopinoData(Import_Rotation_No);
    }


    @RequestMapping(value = "/copinoData/{Rotation_No}",method = RequestMethod.GET)
    public @ResponseBody List<ExportCopino>ListOfCopino(@PathVariable("Rotation_No")String Rotation_No) throws SQLException{
        return exportCopinoService.CopinoData(Rotation_No);
    }








    @RequestMapping(value = "/ExportContainerLoadingDetail/{importRotaition}",method = RequestMethod.GET)

    @ResponseBody
    public Integer getvvd_gkey(@PathVariable String importRotaition){
        String rotation=importRotaition.replace('_','/');
        Integer vvdgkey=exportContainerLoadingDetailService.get_Gkey(rotation);
        return vvdgkey;
    }




    @RequestMapping(value = "/ExportContainerLoadingDetailsVesselInfo/{importRotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getContainerLoadingVesselInfo(@PathVariable String importRotaition){

        String rotation=importRotaition.replace('_','/');
        Integer vvdgkey=exportContainerLoadingDetailService.get_Gkey(rotation);
        return exportContainerLoadingDetailService.get_container_Loading_Details_vessel_info(vvdgkey);

    }

    @RequestMapping(value = "/ExportContatinerLoadingDetailtList/{importRotaition_No}",method = RequestMethod.GET)
    public @ResponseBody List getContainerLoadingDetailList(@PathVariable String importRotaition_No){
        String rotation=importRotaition_No.replace('_','/');
        Integer vvdgkey=exportContainerLoadingDetailService.get_Gkey(rotation);
        return exportContainerLoadingDetailService.getContainerLoadedDetails(vvdgkey);

    }











    @RequestMapping(value = "/ExportContainerBlockReport/{importRotaition}",method = RequestMethod.GET)

    @ResponseBody
    public Integer getvvdgkey(@PathVariable String importRotaition){
        String rotation=importRotaition.replace('_','/');
        Integer vvdgkey=exportContainerBlockReportService.getvvdGkey(rotation);
        return vvdgkey;
    }

    @RequestMapping(value = "/ExportContainerBlockReportVoyNo/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getVoyNo(@PathVariable String importRotaition){

        String rotation=importRotaition.replace('_','/');
        return exportContainerBlockReportService.getVoyNo(rotation);

    }


    @RequestMapping(value = "/ExportContainerBlockReportVesselInfo/{importRotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getContainerVesselInfo(@PathVariable String importRotaition){

        String rotation=importRotaition.replace('_','/');
        Integer vvdgkey=exportContainerBlockReportService.getvvdGkey(rotation);
        return exportContainerBlockReportService.getVessleInformation(vvdgkey);

    }

    @RequestMapping(value = "/ExportContatinerBlockReportList/{importRotaition_No}",method = RequestMethod.GET)
    public @ResponseBody List getContainerBlockList(@PathVariable String importRotaition_No){
        String rotation=importRotaition_No.replace('_','/');
        Integer vvdgkey=exportContainerBlockReportService.getvvdGkey(rotation);
        return exportContainerBlockReportService.getContainerBalanceList(vvdgkey);

    }

    @RequestMapping(value = "/ExportDestinationWiseMloLoadedContainerListVvd_Gkey/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public Integer get_Destination_Wise_Mlo_Loaded_Container_vvdGkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=exportDestinationWiseMloLoadedContainerListService.get_Destination_Wise_Mlo_Loaded_Container_vvdGkey(Rotation);
        return vvd_gkey;
    }


    @RequestMapping(value = "/ExportDestinationWiseMloLoadedContainerVesselInfo/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getDestinationWiseMloLoadedContainerVessleInformation(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportDestinationWiseMloLoadedContainerListService.get_Destination_Wise_Mlo_Loaded_Container_vvdGkey(rotation);
        return exportDestinationWiseMloLoadedContainerListService.getDestinationWiseMloLoadedContainerVessleInformation(vvdgkey);

    }


    @RequestMapping(value = "/ExportDestinationWiseMloLoadedContainerInfo/{Rotaition}",method = RequestMethod.GET)
    public   @ResponseBody List  getDestinationWiseMloLoadedContainerInfo(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportDestinationWiseMloLoadedContainerListService.get_Destination_Wise_Mlo_Loaded_Container_vvdGkey(rotation);
        return exportDestinationWiseMloLoadedContainerListService.getDestinationWiseMloLoadedContainerInfo(vvdgkey);

    }

    //StartExportLoadedContainerListLoadandEmpty

    @RequestMapping(value = "/ExportLoadedContainerListLoadandEmptyVvd_Gkey/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public Integer get_Loaded_Container_vvdGkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=exportLoadedContainerListLoadAndEmptyService.get_Loaded_Container_vvdGkey(Rotation);
        return vvd_gkey;
    }


    @RequestMapping(value = "/ExportLoadedContainerListLoadandEmptyVesselInfo/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getExportLoadedContainerListLoadandEmptyVessleInformation(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportLoadedContainerListLoadAndEmptyService.get_Loaded_Container_vvdGkey(rotation);
        return exportLoadedContainerListLoadAndEmptyService.getExportLoadedContainerListLoadandEmptyVessleInformation(vvdgkey);

    }


    @RequestMapping(value = "/ExportLoadedContainerListLoadandEmptyInfo/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getLoadedContainerListLoadandEmptyInfo(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportLoadedContainerListLoadAndEmptyService.get_Loaded_Container_vvdGkey(rotation);
        return exportLoadedContainerListLoadAndEmptyService.getLoadedContainerListLoadandEmptyInfo(vvdgkey);

    }


    @RequestMapping(value = "/ExportLoadedContainerListLoadandEmptyContainerOnboardInfo/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getLoadedContainerInformation(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportLoadedContainerListLoadAndEmptyService.get_Loaded_Container_vvdGkey(rotation);
        return exportLoadedContainerListLoadAndEmptyService.getLoadedContainerInformation(vvdgkey);

    }
    @RequestMapping(value = "/ExportLoadedContainerListLoadandEmptyLoadedContainerBalanceList/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getLoadedContainerList(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportLoadedContainerListLoadAndEmptyService.get_Loaded_Container_vvdGkey(rotation);
        return exportLoadedContainerListLoadAndEmptyService.getLoadedContainerList(vvdgkey);

    }
    //EndExportLoadedContainerListLoadandEmpty





    //StartExportMloWiseLoadedContainer
    @RequestMapping(value = "/Export Mlo Wise Loaded Container vvdGkey/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public Integer get_Mlo_Wise_Container_vvdGkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=exportMloWiseLoadedContainerService.get_Mlo_Wise_Loaded_Container_vvdGkey(Rotation);
        return vvd_gkey;
    }

    @RequestMapping(value = "/MloWiseLoadedContainerVessleInformation/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getMloWiseLoadedContainerVessleInformation(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportMloWiseLoadedContainerService.get_Mlo_Wise_Loaded_Container_vvdGkey(rotation);
        return exportMloWiseLoadedContainerService.getMloWiseLoadedContainerVessleInformation(vvdgkey);

    }

    @RequestMapping(value = "/MloWiseLoadedContainerOnboardInformation/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getMloWiseLoadedContainerInformation(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportMloWiseLoadedContainerService.get_Mlo_Wise_Loaded_Container_vvdGkey(rotation);
        return exportMloWiseLoadedContainerService.getMloWiseLoadedContainerInformation(vvdgkey);

    }

    @RequestMapping(value = "/MloWiseLoadedContainerInfo/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getMloWiseLoadedContainerInfo(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportMloWiseLoadedContainerService.get_Mlo_Wise_Loaded_Container_vvdGkey(rotation);
        return exportMloWiseLoadedContainerService.getMloWiseLoadedContainerInfo(vvdgkey);

    }

    @RequestMapping(value = "/MloWiseLoadedContainerBalanceList/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getMloWiseLoadedContainerList(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportMloWiseLoadedContainerService.get_Mlo_Wise_Loaded_Container_vvdGkey(rotation);
        return exportMloWiseLoadedContainerService.getMloWiseLoadedContainerList(vvdgkey);

    }

    //EndExportMloWiseLoadedContainer





    //EndExportContainerLoding

  @RequestMapping(value = "/ExportEquipmentHandlingPerformanceRTG/{shift}/{fromdate} /{todate}/{fromTime}/{toTime}",method = RequestMethod.GET)

    public @ResponseBody List exportEquipmentHandlingPerformanceRTG(@PathVariable String shift,@PathVariable String fromdate ,@PathVariable String todate ,@PathVariable String fromTime,@PathVariable String toTime){


        return exportEquipmentHandlingPerformanceRTGService.EquipmentHandlingPerformanceRTG(shift,fromdate ,todate,fromTime,toTime);

    }

    //EndExportContainerNotFoundReport




///////////////ExportContainerNotFound//////////////



    @RequestMapping(value = "/ExportContainerNotFoundVvd_Gkey/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public Integer getExportContainerNotFoundvvdGkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=exportContainerNotFoundReportService.getExportContainerNotFoundvvdGkey(Rotation);
        return vvd_gkey;
    }



    @RequestMapping(value = "/ExportContainerNotFoundVessleInformation/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List getContainerNotFoundVessleInformation(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportContainerNotFoundReportService.getExportContainerNotFoundvvdGkey(rotation);
        return exportContainerNotFoundReportService.getVessleInformation(vvdgkey);

    }



    @RequestMapping(value = "/ExportContainerNotFoundReport/{Rotaition}/{fromDate}/{toDate}",method = RequestMethod.GET)

    public   @ResponseBody List getContainerNotFoundReport(@PathVariable String Rotaition,@PathVariable String fromDate,@PathVariable String toDate){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportContainerNotFoundReportService.getExportContainerNotFoundvvdGkey(rotation);
        return exportContainerNotFoundReportService.getContainerNotFoundReport(vvdgkey,fromDate,toDate);

    }


/////////////EndExportContainerNotFound//////////////////
















    //StartRotationWiseExportContainerReport
    @RequestMapping(value = "/ExportRotationWiseExportContainerReport/{fromdate}/{todate}",method = RequestMethod.GET)
    public @ResponseBody List getContainerNotFoundLoginData(@PathVariable String fromdate,@PathVariable String todate){
        return rotationWiseExportContainerReportService.getRotationWiseExportContainerReport(fromdate,todate);
    }
    //EndRotationWiseExportContainerReport

    //StartExportMloWiseSummary
    @RequestMapping(value = "/ExportMloWiseSummaryVoyNo/{import_Rotaition_No}",method = RequestMethod.GET)
    @ResponseBody
    public List getMloWiseSummaryVoyNo(@PathVariable String import_Rotaition_No){

        String rotation=import_Rotaition_No.replace('_','/');
        return exportReportMloWiseExportSummaryService.getMloWiseSummaryVoyNo(rotation);

    }

    @RequestMapping(value = "/ExportMLOWiseSummaryVvd_Gkey/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public Integer get_vvdGkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=exportReportMloWiseExportSummaryService.get_vvdGkey(Rotation);
        return vvd_gkey;
    }


    @RequestMapping(value = "/ExportReportMloWiseExportSummaryVesselInfo/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  get_Container_Vessel_Info(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportReportMloWiseExportSummaryService.get_vvdGkey(rotation);
        return exportReportMloWiseExportSummaryService.get_Container_Vessel_Info(vvdgkey);

    }

    @RequestMapping(value = "/ExportReportMloWiseExportSummaryList/{importRotaition}",method = RequestMethod.GET)

    public @ResponseBody List getMloWiseExportSummary(@PathVariable String importRotaition){
        String rotation_no_mlo=importRotaition.replace('_','/');
        Integer vvd_gkey=exportReportMloWiseExportSummaryService.get_vvdGkey(rotation_no_mlo);
        return exportReportMloWiseExportSummaryService.getMloWiseExportSummary(vvd_gkey);

    }
    //EndExportMloWiseSummary

    // StartExportMloWiseExcelUploadedReport

    @RequestMapping(value = "/ExportMloWiseExcelUploadedReportVoyNo/{import_Rotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getVoyNoInfo(@PathVariable String import_Rotaition){

        String rotation=import_Rotaition.replace('_','/');
        return exportMloWiseExcelUploadedReportService.getVoyNo(rotation);

    }


    @RequestMapping(value = "/ExportMLOExcelUploadedReport/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public Integer getvvdGkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=exportMloWiseExcelUploadedReportService.getvvdGkey(Rotation);
        return vvd_gkey;
    }



    @RequestMapping(value = "/ExportMLOExcelUploadedReportVesselInformation/{Rotaition}",method = RequestMethod.GET)
    public   @ResponseBody List  getVessleInformation(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');

        Integer vvdgkey=exportMloWiseExcelUploadedReportService.getvvdGkey(rotation);
        return exportMloWiseExcelUploadedReportService.getVessleInformation(vvdgkey);

    }


    @RequestMapping(value = "/ExportMloWiseExcelUploadedReportSummary/{importRotaition}",method = RequestMethod.GET)

    public @ResponseBody List getMloWiseExcelUploadedReport(@PathVariable String importRotaition){
        String rotation_no_mlo=importRotaition.replace('_','/');
        Integer vvd_gkey=exportMloWiseExcelUploadedReportService.getvvdGkey(rotation_no_mlo);
        return exportMloWiseExcelUploadedReportService.getMloWiseExcelUploadedReport(vvd_gkey);

    }
    //endExportMloWiseExcelUploadedReport

    //ExportReportDateAndRotationWisePre-AdvisedContainer
    @RequestMapping(value = "/ExportReportDateAndRotationWisePre-AdvisedContainer/{importRotaition}/{FromDate}",method = RequestMethod.GET)
    public @ResponseBody List<ExportDateAndRotationWisePreAdvisedContainer> getExportReportDateAndRotationWisePreAdvisedContainer(@PathVariable String importRotaition, @PathVariable String FromDate){
        String Rotation=importRotaition.replace('_','/');
        return exportDateAndRotationWisePreAdvisedContainerService.getDateAndRotationWisePreAdvisedContainer(Rotation,FromDate);
    }
    //EndExportReportDateAndRotationWisePre-AdvisedContainer

    //ExportReportExportCommentsByShippingSectionOnExportVessel
    @RequestMapping(value = "/ExportReportExportCommentsByShippingSectionOnExportVessel/{FromDate}/{ToDate}",method = RequestMethod.GET)
    public @ResponseBody List<ExportCommentsByShippingSectionOnExportVessel> getExportReportExportCommentsByShippingSectionOnExportVessel(@PathVariable String FromDate, @PathVariable String ToDate){
        return exportCommentsByShippingSectionOnExportVesselService.CommentsByShippingSectionOnExportVessel(FromDate,ToDate);
    }
    //EndExportReportExportCommentsByShippingSectionOnExportVessel

    //StartRotationWiseExportContainer
    @RequestMapping(value = "/RotationWiseExportContainer/{import_Rotation_No}", method = RequestMethod.GET)
    public @ResponseBody
    List<RotationWiseExportContainer> RotationWiseExportContainerDetails(@PathVariable("import_Rotation_No") String import_Rotation_No){
        String Rotation=import_Rotation_No.replace('_','/');
        return rotationWiseExportContainerService.RotationWiseExportContainerDetails(Rotation);
    }
    //endRotationWiseExportContainer

    //StartExportVesselListWithStatus


    @RequestMapping(value = "/ExportVesselListVvd_Gkey/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public Integer getVesselListvvdGkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=exportVesselListWithStatusService.getVesselListvvdGkey(Rotation);
        return vvd_gkey;
    }

    //getVessleInformation


    @RequestMapping(value = "/ExportReportVessleInformation/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List getVessleListInformation(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportVesselListWithStatusService.getVesselListvvdGkey(rotation);
        return exportVesselListWithStatusService.getVessleInformation(vvdgkey);

    }



    @RequestMapping(value = "/ExportReportVesselVoyNo/{import_Rotaition_No}",method = RequestMethod.GET)
    @ResponseBody
    public List getVesselListVoyNo(@PathVariable String import_Rotaition_No){

        String Rotation_Number=import_Rotaition_No.replace('_','/');

        return exportVesselListWithStatusService.getVesselListVoyNo(Rotation_Number);

    }

    @RequestMapping(value = "/ExportVesselListWithStatus",method = RequestMethod.GET)
    public @ResponseBody List<ExportVesselListWithStatus> ExportVesselList()throws SQLException{
        return exportVesselListWithStatusService.getVessleListWithStatusInfo();
    }



    @RequestMapping(value = "/ExportVesselListWithStatusForRotation/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public List getVessleListStatusWithRotation(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        return exportVesselListWithStatusService.getVessleListStatusWithRotation(Rotation);

    }


    @RequestMapping(value = "/ExportVesselListWithStatusForExportUploadReport/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getVessleListWithStatusInfoForExportUploadReport(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportVesselListWithStatusService.getVesselListvvdGkey(rotation);
        return exportVesselListWithStatusService.getVessleListWithStatusInfoForExportUploadReport(vvdgkey);

    }

    @RequestMapping(value = "/ExportVessleListWithStatusForMloWiseExportSummary/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getVessleListWithStatusForMloWiseExportSummary(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportVesselListWithStatusService.getVesselListvvdGkey(rotation);
        return exportVesselListWithStatusService.getVessleListWithStatusForMloWiseExportSummary(vvdgkey);

    }
    //EndVesselListWithStatus

    //StartContainerToBeLoaded
    @RequestMapping(value = "/ExportContainerToBeLoadedVvd_Gkey/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public Integer getExport_Container_vvdGkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=exportContainerToBeLoadedListService.getExport_Container_vvdGkey(Rotation);
        return vvd_gkey;
    }


    @RequestMapping(value = "/ExportContainerToBeLoadingVoyNo/{import_Rotaition_No}",method = RequestMethod.GET)
    @ResponseBody
    public List getExport_Container_VoyNo(@PathVariable String import_Rotaition_No){

        String Rotation_Number=import_Rotaition_No.replace('_','/');
        return exportContainerToBeLoadedListService.getExport_Container_VoyNo(Rotation_Number);

    }



    @RequestMapping(value = "/ExportContainerToBeLodingVesselInfo/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getExportContainerToBeLodingVesselInfo(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportContainerToBeLoadedListService.getExport_Container_vvdGkey(rotation);
        return exportContainerToBeLoadedListService.getExportContainerVessleInformation(vvdgkey);

    }



    @RequestMapping(value = "/ExportContainerToBeLoadedInformation/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getExportContainerToBeLoadedInformation(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportContainerToBeLoadedListService.getExport_Container_vvdGkey(rotation);
        return exportContainerToBeLoadedListService.getExportContainerToBeLoadedInformation(vvdgkey);

    }



    @RequestMapping(value = "/ExportContainerToBeLoadedBalanceList/{Rotaition}",method = RequestMethod.GET)

    public @ResponseBody List  getExportContainerToBeLoadedList(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportContainerToBeLoadedListService.getExport_Container_vvdGkey(rotation);
        return exportContainerToBeLoadedListService.getExportContainerToBeLoadedList(vvdgkey);

    }
    //EndContainerToBeLoaded

    //Start ExportLoadedContainerList

    @RequestMapping(value = "/ExportLoadedContainerVvd_Gkey/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public Integer getGkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=exportLoadedContainerListService.get_vvdGkey(Rotation);
        return vvd_gkey;
    }
    @RequestMapping(value = "/ExportLoadedContainerVoyNo/{import_Rotaition_No}",method = RequestMethod.GET)
    @ResponseBody
    public List getExportLoadedVoyNo(@PathVariable String import_Rotaition_No){

        String Rotation_Number=import_Rotaition_No.replace('_','/');
        return exportLoadedContainerListService.getVoyNo(Rotation_Number);

    }
    @RequestMapping(value = "/ExportLoadedContainerVesselInfo/{Rotaition}",method = RequestMethod.GET)
    public   @ResponseBody List  getExportLoadedContainerInfo(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportLoadedContainerListService.get_vvdGkey(rotation);
        return exportLoadedContainerListService.getVessleInformation(vvdgkey);

    }
    @RequestMapping(value = "/ExportReportLoadedContainer/{importRotaition}/{fromdate}/{todate}/{toTime}/{fromTime}",method = RequestMethod.GET)

    public @ResponseBody List getExportLoadedContainer(@PathVariable String importRotaition,@PathVariable String fromdate,@PathVariable String todate,@PathVariable String fromTime,@PathVariable String toTime){
        String rotation_no=importRotaition.replace('_','/');
        Integer vvd_gkey=exportLoadedContainerListService.get_vvdGkey(rotation_no);
        return exportLoadedContainerListService.getLoadedContainerList(vvd_gkey,fromdate,todate,fromTime,toTime);

    }

    @RequestMapping(value = "/ExportReportLoadedContainerSummeryOnboardReport/{importRotaition}/{fromdate}/{todate}/{toTime}/{fromTime}",method = RequestMethod.GET)

    public @ResponseBody List getExportLoadedContainerSummeryReport(@PathVariable String importRotaition,@PathVariable String fromdate,@PathVariable String todate,@PathVariable String fromTime,@PathVariable String toTime){
        String rotation_no=importRotaition.replace('_','/');
        Integer vvd_gkey=exportLoadedContainerListService.get_vvdGkey(rotation_no);
        return exportLoadedContainerListService.getLoadedContainerSummaryReport(vvd_gkey,fromdate,todate,fromTime,toTime);

    }

    @RequestMapping(value = "/ExportReportLoadedContainerSummeryBalanceReportList/{importRotaition}/{fromdate}/{todate}/{toTime}/{fromTime}",method = RequestMethod.GET)

    public @ResponseBody List getExportLoadedContainerSummeryReportList(@PathVariable String importRotaition,@PathVariable String fromdate,@PathVariable String todate,@PathVariable String fromTime,@PathVariable String toTime){
        String rotation_no=importRotaition.replace('_','/');
        Integer vvd_gkey=exportLoadedContainerListService.get_vvdGkey(rotation_no);
        return exportLoadedContainerListService.getLoadedContainerSummaryReportList(vvd_gkey,fromdate,todate,fromTime,toTime);

    }


    //End ExportLoadedContainerList





    //StartExportContainerLoding

    @RequestMapping(value = "/ExportContainerVvd_Gkey/{rotaition_No}",method = RequestMethod.GET)

    @ResponseBody
    public Integer get_Gkey(@PathVariable String rotaition_No){
        String Rotation=rotaition_No.replace('_','/');
        Integer vvd_gkey=exportContainerLoadingReportService.get_Gkey(Rotation);
        return vvd_gkey;
    }





    @RequestMapping(value = "/ExportContainerLoadingVoyNo/{import_Rotaition_No}",method = RequestMethod.GET)
    @ResponseBody
    public List getExportVoyNo(@PathVariable String import_Rotaition_No){

        String Rotation_Number=import_Rotaition_No.replace('_','/');
        return exportContainerLoadingReportService.getExportContainerVoyNo(Rotation_Number);

    }



    @RequestMapping(value = "/ExportContainerLodingVesselInfo/{Rotaition}",method = RequestMethod.GET)

    public   @ResponseBody List  getExportContainerLodingVesselInfo(@PathVariable String Rotaition){

        String rotation=Rotaition.replace('_','/');
        Integer vvdgkey=exportContainerLoadingReportService.get_Gkey(rotation);
        return exportContainerLoadingReportService.get_container_Loading_vessel_info(vvdgkey);

    }


    @RequestMapping(value = "/ExportReportContainerLoading/{importRotaition}/{fromdate}/{todate}/{toTime}/{fromTime}",method = RequestMethod.GET)

    public @ResponseBody List getExportReportContainerLoading(@PathVariable String importRotaition,@PathVariable String fromdate,@PathVariable String todate,@PathVariable String fromTime,@PathVariable String toTime){
        String rotation_no=importRotaition.replace('_','/');
        Integer vvd_gkey=exportContainerLoadingReportService.get_Gkey(rotation_no);
        return exportContainerLoadingReportService.getExportContainer(vvd_gkey,fromdate,todate,fromTime,toTime);

    }

    //EndExportContainerLoding

    //StartExportMloWisePreAdvisedLoadedContainerListService

    @RequestMapping(value = "/ExportMloWisePreAdvisedLoadedContainerList/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getMloWisePreAdvisedLoadedContainer(@PathVariable String importRotaition){

        String rotation=importRotaition.replace('_','/');
        return exportMloWisePreAdvisedLoadedContainerListService.getMloWisePreAdvisedLoadedContainer(rotation);

    }


    @RequestMapping(value = "/ExportMloWisePreAdvisedViewList/{import_Rotaition}/{Mlo}",method = RequestMethod.GET)
    @ResponseBody
    public List getMloWisePreAdvisedViewList(@PathVariable String import_Rotaition,@PathVariable String Mlo){

        String rotation=import_Rotaition.replace('_','/');
        return exportMloWisePreAdvisedLoadedContainerListService.getMloWisePreadviceViewList(rotation,Mlo);

    }



    @RequestMapping(value = "/ExportMloWiseSummeryList/{import_rotaition}/{Mlo}",method = RequestMethod.GET)
    @ResponseBody
    public List getMloWiseSummeryList(@PathVariable String import_rotaition,@PathVariable String Mlo){

        String rotation=import_rotaition.replace('_','/');
        return exportMloWisePreAdvisedLoadedContainerListService.getMloWiseSummaryList(rotation,Mlo);

    }

    //EndExportMloWisePreAdvisedLoadedContainerListService




    //Export Container Bay View

    @RequestMapping(value = "/ExportContainerBayVesselInfo/{rotation}",method = RequestMethod.GET)
    @ResponseBody
    public List getExportContainerBayVesselInfo(@PathVariable String  rotation ){
        List list =new ArrayList<>();
        String expRotation="";
        Integer vvdgkey=0;
        expRotation=rotation.replace("-","/");
        vvdgkey=exportContainerBayViewService.getVvdGkey(expRotation);
        list=exportContainerBayViewService.getContainerBayVesselInfo(vvdgkey);

        return list;
    }
    @RequestMapping(value = "/ExportContainerBayViewList/{rotation}",method = RequestMethod.GET)
    @ResponseBody
    public List getExportContainerBayViewList(@PathVariable String rotation ){
        List list =new ArrayList<>();
        String expRotation="";
        Integer vvdgkey=0;
        expRotation=rotation.replace("-","/");
        vvdgkey=exportContainerBayViewService.getVvdGkey(expRotation);
        list=exportContainerBayViewService.getContainerBayViewList(vvdgkey);

        return list;
    }

    //Export Blank Bay View
    @RequestMapping(value = "/ExportBlankBayViewVesselList/",method = RequestMethod.GET)
    @ResponseBody
    public List getExportBlankBayViewVesselList( ){
        List list =new ArrayList<>();

        list=exportBlankBayViewService.getVesselList();

        return list;
    }

    @RequestMapping(value = "/ExportBlankBayVesselInfo/{vvdgkey}",method = RequestMethod.GET)
    @ResponseBody
    public List getExportBlankBayVesselInfo(@PathVariable Integer  vvdgkey ){
        List list =new ArrayList<>();
        list=exportBlankBayViewService.getBlankBayVesselInfo(vvdgkey);

        return list;
    }

    @RequestMapping(value = "/ExportBlankBayViewList/{vvdgkey}",method = RequestMethod.GET)
    @ResponseBody
    public List getExportBlankBayViewList(@PathVariable Integer vvdgkey ){
        List list =new ArrayList<>();
        list=exportBlankBayViewService.getBlankBayViewList(vvdgkey);

        return list;
    }

    // Upload Excel File For Export Container
    @RequestMapping(value = "/UploadExportContainerExcelFile", method=RequestMethod.POST)
    @ResponseBody
    public List uploadExportContainer(@RequestParam("excelfile") MultipartFile multipartFile, @RequestParam("loginId") String loginId, @RequestParam("ip") String ip) throws IOException {
        List list =new ArrayList<>();
        uploadExportContainerExceFileService.storeFile(multipartFile);
        list=uploadExportContainerExceFileService.uploadExcelFile(multipartFile,loginId,ip);

        return list;
    }

}
