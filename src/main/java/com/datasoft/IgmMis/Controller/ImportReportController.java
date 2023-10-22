package com.datasoft.IgmMis.Controller;

import com.datasoft.IgmMis.Model.ImportReport.*;
import com.datasoft.IgmMis.Model.ResponseMessage;
import com.datasoft.IgmMis.Service.ImportReport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/importReports")
public class ImportReportController {

    @Autowired
    private RemovalListOverflowService removalListOverflowService;

    @Autowired
    private ContainerEventHistoryService containerEventHistoryService;
    @Autowired
    private EquipmentLoginLogoutService equipmentLoginLogoutService;
    @Autowired
    private OperatorRtgHandlingPerformance operatorRtgHandlingPerformance;
    @Autowired
    private OperatorQgcHandlingPerformance operatorQgcHandlingPerformance;
    @Autowired
    private OperatorScHandlingPerformanceService operatorScHandlingPerformanceService;
    @Autowired
    private EquipmentHandlingPerformanceRtgService equipmentHandlingPerformanceRtgService;
    @Autowired
    private OffDockDestinationWiseContainerService offDockDestinationWiseContainerService;
    @Autowired
    private ImportContainerDischargeAndBalanceReportService importContainerDischargeAndBalanceReportService;
    @Autowired
    private  OffDockWiseBlockedContainerListService offDockWiseBlockedContainerListService;
    @Autowired
    private RemovalListOfOverFlowYardService removalListOfOverFlowYardService;
    @Autowired
    private FeederDischrageSummaryListService feederDischrageSummaryListService;
    @Autowired
    private MloWiseImportSummaryBerthOperatorService mloWiseImportSummaryBerthOperatorService;
    @Autowired
    private ImportContainerDischargeSummary24HourService importContainerDischargeSummary24HourService;
    @Autowired
    private ImportContainerDischargeSummaryReportTwoLast24HourService importContainerDischargeSummaryReportTwoLast24HourService;
    @Autowired
    private MloDischargeSummaryListService mloDischargeSummaryListService;
    @Autowired
    private RemovalListOfOverFlowYardLclService removalListOfOverFlowYardLclService;
    @Autowired
    private ContainerDischargeAppService containerDischargeAppService;


    //All assignment

    @Autowired
    private AssignmentAndDeliveryEmptyDetailService assignmentAndDeliveryEmptyDetailService;
    @Autowired
    private YardWiseAssignmentAndDeliveryEmptyDetailService yardWiseAssignmentAndDeliveryEmptyDetailService;
    @Autowired
    private AllAssingnmentContainerSearchService allAssingnmentContainerSearchService;
    @Autowired
    private ListOfNotStrippedAssignmentDeliveryContainersSevice listOfNotStrippedAssignmentDeliveryContainersSevice;
    @Autowired
    private AppRaiseReSlotLocationService appRaiseReSlotLocationService;
    @Autowired
    private AssignmentAndDeliveryEmptySummaryService assignmentAndDeliveryEmptySummaryService;
    @Autowired
    private  HeadDeliveryRegisterService headDeliveryRegisterService;
    @Autowired
    private MisAssignmentService misAssignmentService;


    ResponseMessage responseMessage;

    @RequestMapping(value = "/removalListOfOverflowYard", method = RequestMethod.POST)
    public @ResponseBody Object removalListOfOverflowYard(@RequestBody RemovalListOverflow removalListOverflow){
        String assignment_date = removalListOverflow.getAssignment_date();
        List<Object> list = new ArrayList<Object>();
        String exception = null;
        try {
            list = removalListOverflowService.removalListOverflow(assignment_date);
            return list;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return list;
    }


//    OVI API Starts...
@RequestMapping(value = "/ContainerEventHistory/{cont_number}", method = RequestMethod.GET)
@ResponseBody
public List getContainerEventHistory(@PathVariable String cont_number){
    List containerEventHistoryList = new ArrayList<>();
    String exception = null;
    try {
        containerEventHistoryList = containerEventHistoryService.getContainerEventHistory(cont_number);
        return containerEventHistoryList;
    }catch(Exception ex) {
        ex.printStackTrace();
        exception = ex.getMessage();
    }
    return containerEventHistoryList;
}

    @RequestMapping(value = "/EquipmentLoginLogout/{searchCriteria}", method = RequestMethod.GET)
    @ResponseBody
    public List getEquipmentLoginLogoutSearchValue(@PathVariable String searchCriteria ){
        List  equipmentSearchValue = new ArrayList<>();
        String exception = null;
        try {
            equipmentSearchValue = equipmentLoginLogoutService.getSearchValue(searchCriteria);
            return equipmentSearchValue;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return equipmentSearchValue;
    }

    @RequestMapping(value = "/EquipmentLoginLogout/{fromDate}/{shift}/{searchCriteria}/{searchValue}", method = RequestMethod.GET)
    @ResponseBody
    public List<EquipmentLoginLogout> getEquipmentLoginLogoutList(@PathVariable String fromDate, @PathVariable String shift, @PathVariable String searchCriteria, @PathVariable String searchValue ){
        List<EquipmentLoginLogout>  equipmentLoginLogoutList = new ArrayList<>();
        String exception = null;
        try {
            equipmentLoginLogoutList = equipmentLoginLogoutService.getEquipmentLoginLogoutList(fromDate,shift,searchCriteria,searchValue);
            return equipmentLoginLogoutList;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return equipmentLoginLogoutList;
    }

    @RequestMapping(value = "/OperatorRtgHandlingPerformance/{shift}/{fromDate}/{fromTime}/{toDate}/{toTime}", method = RequestMethod.GET)
    @ResponseBody
    public List<OperatorRtgHandlingPerformanceModel> getOperatorRtgHandlingPerformanceList(@PathVariable String shift, @PathVariable String fromDate, @PathVariable String fromTime, @PathVariable String toDate, @PathVariable String toTime ){
        List<OperatorRtgHandlingPerformanceModel>  operatorRtgHandlingPerformanceList = new ArrayList<>();
        String exception = null;
        try {
            operatorRtgHandlingPerformanceList = operatorRtgHandlingPerformance.getOperatorRtgHandingPerformanceList(shift,fromDate,fromTime,toDate,toTime);
            return operatorRtgHandlingPerformanceList ;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return operatorRtgHandlingPerformanceList;
    }

    @RequestMapping(value = "/OperatorQgcHandlingPerformance/{shift}/{fromDate}/{fromTime}/{toDate}/{toTime}", method = RequestMethod.GET)
    @ResponseBody
    public List<OperatorQgcHandlingPerformanceModel> getOperatorQgcHandlingPerformanceList(@PathVariable String shift, @PathVariable String fromDate, @PathVariable String fromTime, @PathVariable String toDate, @PathVariable String toTime ){
        List<OperatorQgcHandlingPerformanceModel>  operatorQgcHandlingPerformanceList = new ArrayList<>();
        String exception = null;
        try {
            operatorQgcHandlingPerformanceList = operatorQgcHandlingPerformance.getOperatorQgcHandingPerformanceList(shift,fromDate,fromTime,toDate,toTime);
            return operatorQgcHandlingPerformanceList ;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return operatorQgcHandlingPerformanceList;
    }

    @RequestMapping(value = "/OperatorScHandlingPerformance/{shift}/{fromDate}/{fromTime}/{toDate}/{toTime}", method = RequestMethod.GET)
    @ResponseBody
    public List<OperatorScHandlingPerformanceModel> getOperatorScHandlingPerformanceList(@PathVariable String shift, @PathVariable String fromDate, @PathVariable String fromTime, @PathVariable String toDate, @PathVariable String toTime ){
        List<OperatorScHandlingPerformanceModel>  operatorScHandlingPerformanceList = new ArrayList<>();
        String exception = null;
        try {
            operatorScHandlingPerformanceList = operatorScHandlingPerformanceService.getOperatorScHandingPerformanceList(shift,fromDate,fromTime,toDate,toTime);
            return operatorScHandlingPerformanceList ;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return operatorScHandlingPerformanceList;
    }
    @RequestMapping(value = "/EquipmentHandlingPerformanceRtg/{shift}/{fromDate}/{fromTime}/{toDate}/{toTime}", method = RequestMethod.GET)
    @ResponseBody
    public List<EquipmentHandlingPerformanceRtgModel> getEquipmentHandlingPerformanceRtgList(@PathVariable String shift, @PathVariable String fromDate, @PathVariable String fromTime, @PathVariable String toDate, @PathVariable String toTime ){
        List<EquipmentHandlingPerformanceRtgModel>  equipmenthandlingPerformanceRtgList = new ArrayList<>();
        String exception = null;
        try {
            equipmenthandlingPerformanceRtgList = equipmentHandlingPerformanceRtgService.getEquipmentPerformanceRtgList(shift,fromDate,fromTime,toDate,toTime);
            return equipmenthandlingPerformanceRtgList ;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return equipmenthandlingPerformanceRtgList;
    }

    @RequestMapping(value = "/OffDockDestinationOrgWiseContainer/{importRotation}", method = RequestMethod.GET)
    @ResponseBody
    public List<OffDockDestinationOrgWiseContainerModel> getOffDockDestinationOrgWiseContainerList(@PathVariable String importRotation ){
        String rotation=importRotation.replace('-','/');
        System.out.println("Rotation :"+ rotation);
        List<OffDockDestinationOrgWiseContainerModel>  offDockDestinationOrgWiseContainerList = new ArrayList<>();
        String exception = null;
        try {
            offDockDestinationOrgWiseContainerList = offDockDestinationWiseContainerService.getOffDockDestinationOrgWiseContainerList(rotation);
            return offDockDestinationOrgWiseContainerList ;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return offDockDestinationOrgWiseContainerList;
    }

    @RequestMapping(value = "/OffDockDestinationWiseContainerVesselNameAndRotation/{importRotation}", method = RequestMethod.GET)
    @ResponseBody
    public List<OffDockDestinationWiseContainerVesselAndRotationModel> getOffDockDestinationWiseContainerVesselAndRotation(@PathVariable String importRotation ){
        String rotation=importRotation.replace('-','/');
        System.out.println("Rotation :"+ rotation);
        List<OffDockDestinationWiseContainerVesselAndRotationModel> vesselNameAndRotation  = new ArrayList<>();
        String exception = null;
        try {
            vesselNameAndRotation = offDockDestinationWiseContainerService.getOffDockDestinationWiseContainerVesselAndRotation(rotation);
            return vesselNameAndRotation ;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return vesselNameAndRotation;
    }

    @RequestMapping(value = "/OffDockDestinationDistrictWiseContainer/{importRotation}", method = RequestMethod.GET)
    @ResponseBody
    public List<OffDockDestinationDistrictWiseContainerModel> getOffDockDestinationDistrictWiseContainerList(@PathVariable String importRotation ){
        String rotation=importRotation.replace('-','/');
        System.out.println("Rotation :"+ rotation);
        List<OffDockDestinationDistrictWiseContainerModel> OffDockDestinationDistrictWiseContainerList  = new ArrayList<>();
        String exception = null;
        try {
            OffDockDestinationDistrictWiseContainerList = offDockDestinationWiseContainerService.getOffDockDestinationDistrictWiseContainerList(rotation);
            return OffDockDestinationDistrictWiseContainerList ;
        }catch(Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        return OffDockDestinationDistrictWiseContainerList;
    }
//    OVI API Ends...
@RequestMapping(value = "/ImportContainerDischargeAndBalaceReportVvdgkey/{importRotaition}",method = RequestMethod.GET)
@ResponseBody
public Integer getVvdgkey(@PathVariable String importRotaition){
    String rotation=importRotaition.replace('-','/');
    Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
    return vvdgkey;
}
    @RequestMapping(value = "/ImportContainerDischargeAndBalaceReportVoyNo/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getVoyNo(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        list=importContainerDischargeAndBalanceReportService.getVoyNo(rotation);
        return list;
    }

    @RequestMapping(value = "/ImportContainerDischargeList/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getContainerDischargeList(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeAndBalanceReportService.getContainerDischargeList(vvdgkey);

        return list;
    }
    @RequestMapping(value = "/ImportContainerBalaceOnBoardList/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getContainerBalaceOnBoardList(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeAndBalanceReportService.getContainerBalanceList(vvdgkey);

        return list;
    }

    @RequestMapping(value = "/ImportContainerDischargeAndBalaceReportVesselInfo/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getContainerVesselInfo(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeAndBalanceReportService.getVessleInformation(vvdgkey);

        return list;
    }

    @RequestMapping(value = "/ImportContainerDischargeSummary/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getContainerDischargeSummary(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeAndBalanceReportService.getContainerDischargeSummary(vvdgkey);

        return list;
    }

    @RequestMapping(value = "/ImportContainerBalanceOnBoardSummary/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getContainerBalanceOnBoardSummary(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeAndBalanceReportService.getContainerBanlanceOnBoardSummary(vvdgkey);

        return list;
    }

    @RequestMapping(value = "/OffDockWiseBlockedCotainerList",method = RequestMethod.GET)
    @ResponseBody
    public List getOffDockWiseBlockCotainerList(){
        List list =new ArrayList<>();
        list=offDockWiseBlockedContainerListService.getOffDockWiseBlockCotainerList();
        System.out.println("res "+ list.size());

        return list;
    }
    @RequestMapping(value = "/RemovalListOfOverFlowYard/{assignDate}/{modify}",method = RequestMethod.GET)
    @ResponseBody
    public List getRemovalListOfOverFlowYard(@PathVariable String assignDate,@PathVariable String modify){
        List list =new ArrayList<>();
        list=removalListOfOverFlowYardService.getRemovalListOfOverFlowYard(assignDate,modify);

        return list;
    }

    @RequestMapping(value = "/vesselInfoForFeederDischargeSummaryList/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getVesselInfoForFeederDischargeSummaryList(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');

        list=feederDischrageSummaryListService.getVesselInfo(rotation);

        return list;
    }
    @RequestMapping(value = "/feederDischargeSummaryList/{importRotaition}/{type}",method = RequestMethod.GET)
    @ResponseBody
    public List getFeederDischargeSummaryList(@PathVariable String importRotaition,@PathVariable String type){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');

        if(type.equals("selected")){
            list=feederDischrageSummaryListService.getFeederDischargeSummaryListOffDockWise(rotation);
        }
        else{

            list=feederDischrageSummaryListService.getFeederDischargeSummaryList(rotation);
        }

        return list;
    }
    // Mlo Wise Import Summary (berth operator)
    @RequestMapping(value = "/MloWiseImportSummaryVesselInformation/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getMloWiseImportSummaryVesselInfo(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=mloWiseImportSummaryBerthOperatorService.getMloWiseImportSummaryVessleInformation(vvdgkey);

        return list;
    }

    @RequestMapping(value = "/MloWiseImportSummaryLoadedListVoyNo/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getMloWiseImportSummaryLoadedListVoyNo(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        list=importContainerDischargeAndBalanceReportService.getVoyNo(rotation);
        return list;
    }
    @RequestMapping(value = "/MloWiseImportSummaryLoadedList/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getMloWiseImportSummaryLoadedList(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=mloWiseImportSummaryBerthOperatorService.getMloWiseImportSummaryLoadedList(vvdgkey);

        return list;
    }
    @RequestMapping(value = "/ImportContainerDischargeDetailLast24HourVesselInformation/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getImportContainerDischargeDetailLast24HourVesselInfo(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=mloWiseImportSummaryBerthOperatorService.getMloWiseImportSummaryVessleInformation(vvdgkey);

        return list;
    }

    @RequestMapping(value = "/ImportContainerDischargeDetailLast24HourVoyNo/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getImportContainerDischargeDetailLast24HourVoyNo(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        list=importContainerDischargeAndBalanceReportService.getVoyNo(rotation);
        return list;
    }
    @RequestMapping(value = "/ImportContainerDischargeDetailLast24HourSummaryList/{importRotaition}/{fromDate}/{fromTime}/{toDate}/{toTime}",method = RequestMethod.GET)
    @ResponseBody
    public List getImportContainer24HourSummaryList(@PathVariable String importRotaition,@PathVariable String fromDate,@PathVariable String fromTime,@PathVariable String toDate,@PathVariable String toTime){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        if(fromDate.equals("empty")){
            fromDate="";
        }
        if(fromTime.equals("empty")){
            fromTime="";
        }
        if(toDate.equals("empty")){
            toDate="";
        }
        if(toTime.equals("empty")){
            toTime="";
        }

        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeSummary24HourService.getImportContainer24HourSummaryList(fromDate,fromTime,toDate,toTime,vvdgkey);

        return list;
    }
    @RequestMapping(value = "/ImportContainerDischargeDetailLast24HourDetailList/{importRotaition}/{fromDate}/{fromTime}/{toDate}/{toTime}",method = RequestMethod.GET)
    @ResponseBody
    public List getImportContainer24HourDetailList(@PathVariable String importRotaition,@PathVariable String fromDate,@PathVariable String fromTime,@PathVariable String toDate,@PathVariable String toTime){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        if(fromDate.equals("empty")){
            fromDate="";
        }
        if(fromTime.equals("empty")){
            fromTime="";
        }
        if(toDate.equals("empty")){
            toDate="";
        }
        if(toTime.equals("empty")){
            toTime="";
        }

        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeSummary24HourService.getImportCotainerDischarge24HourDetail(fromDate,fromTime,toDate,toTime,vvdgkey);


        return list;
    }
    //Import Container Discharge Summary Reprot two
    @RequestMapping(value = "/importContainerDischargeSummaryReportTwoLast24HourVesselInfo/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getImportContainerDischargeSummaryReportTwoLast24HourVesselInfo(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeSummaryReportTwoLast24HourService.getVesselInfo(vvdgkey);

        return list;
    }
    @RequestMapping(value = "/importContainerDischargeSummaryReportTwoLast24HourSummary3/{importRotaition}/{formDate}",method = RequestMethod.GET)
    @ResponseBody
    public List getImportContainerDischargeSummaryReportTwoLast24HourSummary3(@PathVariable String importRotaition, @PathVariable String formDate){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeSummaryReportTwoLast24HourService.getSummary3(vvdgkey,formDate);

        return list;
    }

    @RequestMapping(value = "/importContainerDischargeSummaryReportTwoLast24HourSummary/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getImportContainerDischargeSummaryReportTwoLast24HourSummary(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeSummaryReportTwoLast24HourService.getSummary(vvdgkey);

        return list;
    }

    @RequestMapping(value = "/importContainerDischargeSummaryReportTwoLast24HourSummary2/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getImportContainerDischargeSummaryReportTwoLast24HourSummary2(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeSummaryReportTwoLast24HourService.getSummary2(vvdgkey);

        return list;
    }
    @RequestMapping(value = "/importContainerDischargeSummaryReportTwoLast24HourSummary4/{importRotaition}/{formDate}",method = RequestMethod.GET)
    @ResponseBody
    public List getImportContainerDischargeSummaryReportTwoLast24HourSummary4(@PathVariable String importRotaition, @PathVariable String formDate){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeSummaryReportTwoLast24HourService.getSummary4(vvdgkey,formDate);

        return list;
    }
    @RequestMapping(value = "/importContainerDischargeSummaryReportTwoLast24HourSummary5/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getImportContainerDischargeSummaryReportTwoLast24HourSummary5(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeSummaryReportTwoLast24HourService.getSummary5(vvdgkey);

        return list;
    }
    @RequestMapping(value = "/importContainerDischargeSummaryReportTwoLast24HourSummary6/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getImportContainerDischargeSummaryReportTwoLast24HourSummary6(@PathVariable String importRotaition){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeSummaryReportTwoLast24HourService.getSummary6(vvdgkey);

        return list;
    }

    //Mlo Discharge Summary List

    @RequestMapping(value = "/MloDischargeSummaryAgentList/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getAgentList(@PathVariable String importRotaition ){
        List list =new ArrayList<>();

        String rotation=importRotaition.replace('-','/');
        list=mloDischargeSummaryListService.getAgentList(rotation);

        return list;
    }
    @RequestMapping(value = "/MloDischargeSummaryMloList/{orgId}",method = RequestMethod.GET)
    @ResponseBody
    public List getMloList(@PathVariable String orgId ){
        List list =new ArrayList<>();

        list=mloDischargeSummaryListService.getMloList(orgId);

        return list;
    }

    @RequestMapping(value = "/MloDischargeSummaryIgmInfo/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getIgmInfo(@PathVariable String importRotaition ){
        List list =new ArrayList<>();

        String rotation=importRotaition.replace('-','/');
        list=mloDischargeSummaryListService.getIgmInfo(rotation);

        return list;
    }
    @RequestMapping(value = "/MloDischargeSummaryDateInfo/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getDateInfo(@PathVariable String importRotaition ){
        List list =new ArrayList<>();

        String rotation=importRotaition.replace('-','/');
        list=mloDischargeSummaryListService.getDateInfo(rotation);

        return list;
    }

    @RequestMapping(value = "/MloDischargeSummaryList/{importRotaition}/{orgId}/{mlo}",method = RequestMethod.GET)
    @ResponseBody
    public List getMloDischargeSummaryList(@PathVariable String importRotaition,@PathVariable String orgId,@PathVariable String mlo ){
        List list =new ArrayList<>();
        String rotation=importRotaition.replace('-','/');
        list=mloDischargeSummaryListService.getMloDischargeSummaryList(rotation,orgId,mlo);

        return list;
    }

    //Removal List Of Overflow Yard Lcl
    @RequestMapping(value = "/RemovalListOfOverFlowYardLcl/{assignDate}",method = RequestMethod.GET)
    @ResponseBody
    public List getRemovalListOfOverFlowYardLcl(@PathVariable String assignDate){
        List list =new ArrayList<>();
        list=removalListOfOverFlowYardLclService.getRemovalListOfOverFlowYardLcl(assignDate);

        return list;
    }
    @RequestMapping(value = "/AllContainerDischargeAppList",method = RequestMethod.GET)
    @ResponseBody
    public List getAllContainerDischargeAppList(){
        List list =new ArrayList<>();
        list=containerDischargeAppService.getAllContainerDischargeList();
        return list;
    }
    @RequestMapping(value = "/ContainerDischargeYardList",method = RequestMethod.GET)
    @ResponseBody
    public List getYardList(){
        List list =new ArrayList<>();
        list=containerDischargeAppService.getYardList();
        return list;
    }
    @RequestMapping(value = "/ContainerDischargeAppVesselInfo/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getContainerDischargeAppVesselInfo(@PathVariable String importRotaition){
        List list =new ArrayList<>();
       /* String rotation="";
        if(!importRotaition.equals("empty")){
            rotation=importRotaition.replace('-','/');
        }*/
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=importContainerDischargeAndBalanceReportService.getVessleInformation(vvdgkey);

        return list;
    }
    @RequestMapping(value = "/ContainerDischargeAppVoyNo/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getContainerDischargeVoyNo(@PathVariable String importRotaition){
        List list =new ArrayList<>();
       /* String rotation="";
        if(!importRotaition.equals("empty")){
            rotation=importRotaition.replace('-','/');
        }*/
        String rotation=importRotaition.replace('-','/');
        list=importContainerDischargeAndBalanceReportService.getVoyNo(rotation);
        return list;
    }
    @RequestMapping(value = "/ContainerDischargeAppList/{importRotaition}/{searchBy}/{yard}/{fromDate}/{toDate}/{fromTime}/{toTime}",method = RequestMethod.GET)
    @ResponseBody
    public List getContainerDischargeAppList(@PathVariable String importRotaition,@PathVariable String searchBy,@PathVariable String yard,@PathVariable String fromDate,@PathVariable String toDate,@PathVariable String fromTime,@PathVariable String toTime ){
        List list =new ArrayList<>();
      /*  String rotation="";
        if(!importRotaition.equals("empty")){
            rotation=importRotaition.replace('-','/');
        }*/

        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=containerDischargeAppService.getContainerDischargeAppList(vvdgkey,rotation,searchBy,yard,fromDate,toDate,fromTime,toTime);

        return list;
    }

    @RequestMapping(value = "/ContainerDischargeListAppOnBoardSummary/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getContainerDischargeAppOnBoardSummary(@PathVariable String importRotaition ){
        List list =new ArrayList<>();
        //String rotation="";
       /* if(!importRotaition.equals("empty")){
            rotation=importRotaition.replace('-','/');
        }*/
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=containerDischargeAppService.getContainerDischargeAppOnBoardSummary(vvdgkey);

        return list;
    }
    @RequestMapping(value = "/ContainerDischargeListAppBalanceSummary/{importRotaition}",method = RequestMethod.GET)
    @ResponseBody
    public List getContainerDischargeAppBalanceSummary(@PathVariable String importRotaition ){
        List list =new ArrayList<>();
        //String rotation="";
       /* if(!importRotaition.equals("empty")){
            rotation=importRotaition.replace('-','/');
        }*/
        String rotation=importRotaition.replace('-','/');
        Integer vvdgkey=importContainerDischargeAndBalanceReportService.getvvdGkey(rotation);
        list=containerDischargeAppService.getContainerDischargeAppOnBoardSummary(vvdgkey);

        return list;
    }


    //All ASSIGNMENT/DELIVERY EMPTY DETAILS

    //ASSIGNMENT/DELIVERY EMPTY DETAILS
    @RequestMapping(value = "/AssignmentAndDeliveryDetail/{fromDate}/{loginId}/{yardName}",method = RequestMethod.GET)
    @ResponseBody
    public List getAssignmentAndDeliveryDetail(@PathVariable String fromDate,@PathVariable String loginId,@PathVariable String yardName){
        List list =new ArrayList<>();
        list=assignmentAndDeliveryEmptyDetailService.getAssignmentAndDeliveryDetail(fromDate,loginId,yardName);
        return list;
    }

    //Yard Wise ASSIGNMENT/DELIVERY EMPTY DETAILS

    @RequestMapping(value = "/YardWiseAssignmentAndDeliveryDetailBlockList/{yardName}",method = RequestMethod.GET)
    @ResponseBody
    public List getYardWiseAssignmentAndDeliveryDetailBlockList(@PathVariable String yardName){
        List list =new ArrayList<>();
        list=yardWiseAssignmentAndDeliveryEmptyDetailService.getBlockList(yardName);
        return list;
    }

    @RequestMapping(value = "/YardWiseAssignmentAndDeliveryDetail/{fromDate}/{yardName}/{blockName}",method = RequestMethod.GET)
    @ResponseBody
    public List getYardWiseAssignmentAndDeliveryDetail(@PathVariable String fromDate,@PathVariable String yardName,@PathVariable String blockName){
        List list =new ArrayList<>();
        list=yardWiseAssignmentAndDeliveryEmptyDetailService.getYardWiseAssignmentAndDeliveryDetail(fromDate,yardName,blockName);
        return list;
    }

    // All ASSIGNMENT Container Search
    @RequestMapping(value = "/AllASSIGNMENTContainerSearchResult/{assignDate}/{containerNo}",method = RequestMethod.GET)
    @ResponseBody
    public List getAllASSIGNMENTContainerSearchResult(@PathVariable String assignDate,@PathVariable String containerNo){
        List list =new ArrayList<>();
        list=allAssingnmentContainerSearchService.getContainerSearchResult(assignDate,containerNo);
        return list;
    }

    // All SSIGNMENT/DELIVERY EMPTY Summary
    @RequestMapping(value = "/AssignmentAndDeliverySummary/{assignDate}/{yardName}",method = RequestMethod.GET)
    @ResponseBody
    public List getAssignmentEmptySummary(@PathVariable String assignDate,@PathVariable String yardName){
        List list =new ArrayList<>();
        list=assignmentAndDeliveryEmptySummaryService.getAssignmentEmptySummary(assignDate,yardName);
        return list;
    }

    // ListOfNotStrippedAssignmentDeliveryContainers
    @RequestMapping(value = "/ListOfNotStrippedAssignmentDeliveryContainers/{date}/{yardName}",method = RequestMethod.GET)
    @ResponseBody
    public List getListOfNotStrippedAssignmentDeliveryContainers(@PathVariable String date,@PathVariable String yardName){
        List list =new ArrayList<>();
        list=listOfNotStrippedAssignmentDeliveryContainersSevice.getListOfNotStrippedAssignmentDeliveryContainers(date,yardName);
        return list;
    }

    // AppRaiseReSlotLocationList
    @RequestMapping(value = "/AppRaiseReSlotLocationList/{searchDate}/{containers}",method = RequestMethod.GET)
    @ResponseBody
    public List getAppRaiseReSlotLocationList(@PathVariable String searchDate,@PathVariable String containers){
        List list =new ArrayList<>();
        list=appRaiseReSlotLocationService.getAppRaiseReSlotLocationList(searchDate,containers);
        return list;
    }
    // HEAD DELIVERY REGISTER REPORT
    @RequestMapping(value = "/HeadDeliveryRegisterAssignType/{terminal}",method = RequestMethod.GET)
    @ResponseBody
    public List getAssignType(@PathVariable String terminal){
        List list =new ArrayList<>();
        list=headDeliveryRegisterService.getAssignType(terminal);
        return list;
    }

    @RequestMapping(value = "/HeadDeliveryRegisterBlocklList/{terminal}",method = RequestMethod.GET)
    @ResponseBody
    public List getBlockList(@PathVariable String terminal){
        List list =new ArrayList<>();
        list=headDeliveryRegisterService.getBlockList(terminal);
        return list;
    }

    @RequestMapping(value = "/HeadDeliveryRegisterList/{date}/{terminal}/{block}/{assignType}",method = RequestMethod.GET)
    @ResponseBody
    public List getHeadDeliveryRegisterList(@PathVariable String date,@PathVariable String terminal,@PathVariable String block,@PathVariable String assignType){
        List list =new ArrayList<>();
        if(terminal.equals("CCT") || terminal.equals("NCT")) {
            list = headDeliveryRegisterService.getHeadDeliveryRegisterCctNctList(date, terminal, block, assignType);
        }
        else if (terminal.equals("GCB")){
            list = headDeliveryRegisterService.getHeadDeliveryRegisterGcbList(date, terminal, block, assignType);
        }
        return list;
    }
    // Mis Assignment
    @RequestMapping(value = "/MisAssignmentAssignType/{terminal}",method = RequestMethod.GET)
    @ResponseBody
    public List getMisAssignmentAssignType(@PathVariable String terminal){
        List list =new ArrayList<>();
        list=misAssignmentService.getMisAssignmentAssignType(terminal);
        return list;
    }
    @RequestMapping(value = "/MisAssignmentBlockList/{terminal}",method = RequestMethod.GET)
    @ResponseBody
    public List getMisAssignmentBlockList(@PathVariable String terminal){
        List list =new ArrayList<>();
        list=misAssignmentService.getMisAssignmentBlockList(terminal);
        return list;
    }

    @RequestMapping(value = "/MisAssignmentList/{date}/{terminal}/{block}/{assignType}",method = RequestMethod.GET)
    @ResponseBody
    public List getMisAssignmentList(@PathVariable String date,@PathVariable String terminal,@PathVariable String block,@PathVariable String assignType){
        List list =new ArrayList<>();
        if(terminal.equals("CCT") || terminal.equals("NCT") || terminal.equals("OFY")) {
            list = misAssignmentService.getMisAssignmentCctNctOfyList(date, terminal, block, assignType);
        }
        else if (terminal.equals("GCB")){
            list = misAssignmentService.getMisAssignmentGcbList(date, terminal, block, assignType);
        }
        return list;
    }


}
