package com.datasoft.IgmMis.Service.ImportReport;
import com.datasoft.IgmMis.Model.ImportReport.MloDischargeSummaryListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MloDischargeSummaryListService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    public List getAgentList(String rotation){
        String sqlQuery="";
        sqlQuery="SELECT  DISTINCT Organization_Name,organization_profiles.id FROM organization_profiles INNER JOIN \n" +
                "igm_details \n" +
                "ON igm_details.Submitee_Org_Id=organization_profiles.id\n" +
                "WHERE igm_details.Import_Rotation_No='"+rotation+"'";
        List resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.Agent());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    public List getMloList(String orgId){
        String sqlQuery="";
        String type="";
        if(orgId.equals("2619")){
            type="'2619' or Submitee_Org_Id='153'";
        }
        else {
            type="'"+orgId+"'";
        }

        sqlQuery="SELECT DISTINCT igm_details.mlocode FROM igm_details WHERE Submitee_Org_Id="+type;
        List resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.Mlo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    public List getIgmInfo(String rotation){
        String sqlQuery="";
        sqlQuery="SELECT\n" +
                "Import_Rotation_No,\n" +
                "vessels.Vessel_Name,\n" +
                "Voy_No,\n" +
                "Net_Tonnage,\n" +
                "Port_of_Shipment,\n" +
                "Port_of_Destination,\n" +
                "Sailed_Year,\n" +
                "Submitee_Org_Id,\n" +
                "Name_of_Master,\n" +
                "Organization_Name,\n" +
                "Submitee_Org_Type,\n" +
                "is_Foreign,\n" +
                "Vessel_Type,\n" +
                "Name_of_Master,\n" +
                "Port_of_Registry\n" +
                "FROM\n" +
                "igm_masters \n" +
                "LEFT JOIN organization_profiles ON \n" +
                "organization_profiles.id=igm_masters.Submitee_Org_Id\n" +
                "LEFT JOIN vessels ON vessels.id=igm_masters.Vessel_Id\n" +
                "WHERE \n" +
                "igm_masters.Import_Rotation_No='"+rotation+"'";

        List resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.IgmInfo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    public List getDateInfo(String rotation){
        String sqlQuery="";
        sqlQuery="SELECT ETA_Date,ETD_Date,Actual_Berth,Actual_Berth_time FROM vessels_berth_detail\n" +
                "WHERE Import_Rotation_No='"+rotation+"'";
        List resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.DateInfo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public List getMloDischargeSummaryList(String rotation, String orgId, String mlo){
        String sqlQuery="";
        List list=new ArrayList<>();
        sqlQuery="";
        List<MloDischargeSummaryListModel> resultList=new ArrayList<>();
        MloDischargeSummaryListModel row=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row3=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row5=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row7=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row9=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row11=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row110=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row13=new MloDischargeSummaryListModel();

        MloDischargeSummaryListModel row2=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row4=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row6=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row8=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row10=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row12=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row120=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row14=new MloDischargeSummaryListModel();

        MloDischargeSummaryListModel row21=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row22=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row23=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row24=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row25=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row26=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row27=new MloDischargeSummaryListModel();
        MloDischargeSummaryListModel row260=new MloDischargeSummaryListModel();

        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand \n" +
                    "\t(cont_status='FCL' or cont_status='FCL/PART' or cont_status='PRT')  \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and (cont_size='20' or cont_size='10') and\n" +
                    "    igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1" ;
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\t and \n" +
                    "\t(cont_status='FCL' or cont_status='FCL/PART' or cont_status='PRT') \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and (cont_size='20' or cont_size='10') and\n" +
                    "    igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"'  and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand \n" +
                    "\tcont_status='LCL' \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and (cont_size='20' or cont_size='10') and\n" +
                    "igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1 " ;
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand\n" +
                    "\tcont_status='LCL' \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and (cont_size='20' or cont_size='10') and\n" +
                    "igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and igm_details.final_submit=1\tand mlocode='"+mlo+"'";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row3 = resultList.get(0);
        }

        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\t      FROM igm_details,igm_detail_container WHERE \n" +
                    "\t      igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "          and\n" +
                    "\t      type_of_igm<>'TS' and cont_iso_type  not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and cont_iso_type not in ('DRY')\n" +
                    "\t      and\n" +
                    "\t      (cont_status='EMT' or cont_status='Empty'or cont_status='MT' or cont_status='MTY' or cont_status='ETY')\n" +
                    "\t\t  and off_dock_id<>'2592' \n" +
                    "          and (cont_size='20' or cont_size='10') and\n" +
                    "igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1" ;
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\t      FROM igm_details,igm_detail_container WHERE \n" +
                    "\t      igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "          and\n" +
                    "\t      type_of_igm<>'TS' and cont_iso_type  not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and cont_iso_type not in ('DRY')\n" +
                    "\t      and\n" +
                    "\t        (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='MTY' or cont_status='ETY')\n" +
                    "\t\t  and off_dock_id<>'2592' \n" +
                    "          and (cont_size='20' or cont_size='10') and\n" +
                    "igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and igm_details.final_submit=1\tand mlocode='"+mlo+"'";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row5 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand off_dock_id='2592' \n" +
                    "    and (cont_size='20' or cont_size='10') and\n" +
                    "igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1" ;
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand off_dock_id='2592' \n" +
                    "    and (cont_size='20' or cont_size='10') and\n" +
                    "igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and igm_details.final_submit=1\tand mlocode='"+mlo+"'" ;
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row7 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand type_of_igm='TS' \n" +
                    "\tand (cont_size='20' or cont_size='10') and\n" +
                    "igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand type_of_igm='TS' \n" +
                    "\tand (cont_size='20' or cont_size='10') and\n" +
                    "igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and igm_details.final_submit=1\tand mlocode='"+mlo+"'";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row9 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand cont_iso_type in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and cont_iso_type not in ('DRY')  and (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY' )\n" +
                    "\tand (cont_size='20' or cont_size='10')\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1 " ;
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand cont_iso_type in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and cont_iso_type not in ('DRY')  and (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY')\n" +
                    "\tand (cont_size='20' or cont_size='10')\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"'  and mlocode='"+mlo+"' and igm_details.final_submit=1" ;
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row11 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand cont_iso_type in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and cont_iso_type not in ('DRY') and (!(cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY'))\n" +
                    "\tand (cont_size='20' or cont_size='10')\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand cont_iso_type in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and cont_iso_type not in ('DRY') and (!(cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY'))\n" +
                    "\tand (cont_size='20' or cont_size='10')\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"'  and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row110 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id  and (cont_un   <> '' or cont_imo <> '')\n" +
                    "\tand (cont_size='20' or cont_size='10') and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id  and (cont_un   <> '' or cont_imo <> '')\n" +
                    "\tand (cont_size='20' or cont_size='10') and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"'  and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row13 = resultList.get(0);
        }


        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand \n" +
                    "\t(cont_status='FCL' or cont_status='FCL/PART' or cont_status='PRT') \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and cont_size='40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand \n" +
                    "\t(cont_status='FCL' or cont_status='FCL/PART' or cont_status='PRT') \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and cont_size='40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"'  and igm_details.final_submit=1\tand mlocode='"+mlo+"'";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row2 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand\n" +
                    "\tcont_status='LCL' \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and cont_size='40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand\n" +
                    "\tcont_status='LCL' \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and cont_size='40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and igm_details.final_submit=1  and mlocode='"+mlo+"'";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row4 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\t      FROM igm_details,igm_detail_container WHERE \n" +
                    "\t      igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "          and\n" +
                    "\t      type_of_igm<>'TS' \n" +
                    "\t      and\n" +
                    "\t        (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='MTY' or cont_status='ETY') and (!(cont_type='REEFER' or cont_type='REFER'))\n" +
                    "\t\t  and off_dock_id<>'2592' \n" +
                    "          and cont_size='40'\n" +
                    "          and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\t      FROM igm_details,igm_detail_container WHERE \n" +
                    "\t      igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "          and\n" +
                    "\t      type_of_igm<>'TS' \n" +
                    "\t      and\n" +
                    "\t       (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='MTY' or cont_status='ETY') and (!(cont_type='REEFER' or cont_type='REFER'))\n" +
                    "\t\t  and off_dock_id<>'2592' \n" +
                    "          and cont_size='40'\n" +
                    "          and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"'   and mlocode='"+mlo+"'  and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row6 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand off_dock_id='2592' \n" +
                    "    and cont_size='40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand off_dock_id='2592' \n" +
                    "    and cont_size='40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and igm_details.final_submit=1  and mlocode='"+mlo+"'";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row8 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand type_of_igm='TS' \n" +
                    "\tand cont_size='40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand type_of_igm='TS' \n" +
                    "\tand cont_size='40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"'  and mlocode='"+mlo+"'\n" +
                    "\n" +
                    "    group by Submitee_Org_Id and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row10 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand cont_iso_type in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and cont_iso_type not in ('DRY') and (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY')\n" +
                    "\tand cont_size='40'\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand cont_iso_type in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and cont_iso_type not in ('DRY') and (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY')\n" +
                    "\tand cont_size='40'\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row12 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand cont_iso_type in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and cont_iso_type not in ('DRY') and (!(cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY'))\n" +
                    "\tand cont_size='40'\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand cont_iso_type in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and cont_iso_type not in ('DRY') and (!(cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY'))\n" +
                    "\tand cont_size='40'\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"'  and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row120 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id  and (cont_un   <> '' or cont_imo <> '')\n" +
                    "\tand cont_size='40' and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id  and (cont_un   <> '' or cont_imo <> '')\n" +
                    "\tand cont_size='40' and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"'  and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row14 = resultList.get(0);
        }

        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand\n" +
                    "\t(cont_status='FCL' or cont_status='FCL/PART' or cont_status='PRT') \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and cont_size>'40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1 ";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand\n" +
                    "\t(cont_status='FCL' or cont_status='FCL/PART' or cont_status='PRT') \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and cont_size>'40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"'  and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row21 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand\n" +
                    "\tcont_status='LCL' \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and cont_size>'40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand\n" +
                    "\tcont_status='LCL' \n" +
                    "\tand type_of_igm<>'TS' \n" +
                    "    and off_dock_id<>'2592' \n" +
                    "    and cont_size>'40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row22 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT  count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\t      FROM igm_details,igm_detail_container WHERE \n" +
                    "\t      igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "          and\n" +
                    "\t      type_of_igm<>'TS'  and (!(cont_type='REEFER' or cont_type='REFER'))\n" +
                    "\t      and\n" +
                    "\t       (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='MTY' or cont_status='ETY')\n" +
                    "\t\t  and off_dock_id<>'2592' \n" +
                    "          and cont_size>'40'\n" +
                    "          and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT  count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\t      FROM igm_details,igm_detail_container WHERE \n" +
                    "\t      igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "          and\n" +
                    "\t      type_of_igm<>'TS' and (!(cont_type='REEFER' or cont_type='REFER'))\n" +
                    "\t      and\n" +
                    "\t        (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='MTY' or cont_status='ETY') \n" +
                    "\t\t  and off_dock_id<>'2592' \n" +
                    "          and cont_size>'40'\n" +
                    "          and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row23 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand off_dock_id='2592' \n" +
                    "    and cont_size>'40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand off_dock_id='2592' \n" +
                    "    and cont_size>'40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row24 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand type_of_igm='TS' \n" +
                    "\tand cont_size>'40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' \n" +
                    "and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand type_of_igm='TS' \n" +
                    "\tand cont_size>'40'\n" +
                    "    and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"'  and mlocode='"+mlo+"'\n" +
                    "and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row25 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand (cont_type='REEFER' OR  cont_type='REFER') and (!(cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY'))\n" +
                    "\tand cont_size>'40'\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand (cont_type='REEFER' OR  cont_type='REFER') and (!(cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY'))\n" +
                    "\tand cont_size>'40'\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row26 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand (cont_type='REEFER' OR  cont_type='REFER') and (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY')\n" +
                    "\tand cont_size>'40'\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id \n" +
                    "\tand (cont_type='REEFER' OR  cont_type='REFER') and (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY')\n" +
                    "\tand cont_size>'40'\n" +
                    "\tand igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row260 = resultList.get(0);
        }
        if(orgId.equals("all")){
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id  and (cont_un   <> '' or cont_imo <> '')\n" +
                    "\tand cont_size>'40' and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.final_submit=1";
        }
        else {
            sqlQuery="SELECT count(distinct cont_number) as number,sum(cont_gross_weight) as gross_weight,sum(cont_gross_weight+cont_weight) as net_weight,sum(cont_weight) as tare_weight\n" +
                    "\tFROM igm_details,igm_detail_container WHERE \n" +
                    "\tigm_details.id=igm_detail_container.igm_detail_id  and (cont_un   <> '' or cont_imo <> '')\n" +
                    "\tand cont_size>'40' and igm_details.Import_Rotation_No='"+rotation+"' and igm_details.Submitee_Org_Id='"+orgId+"' and mlocode='"+mlo+"' and igm_details.final_submit=1";
        }
        resultList=primaryDBTemplate.query(sqlQuery,new MloDischargeSummaryListService.CommonResult());

        if(resultList.size()>0) {
            row27 = resultList.get(0);
        }



        list.add(row);//0
        list.add(row2);//1
        list.add(row3);//2
        list.add(row4);//3
        list.add(row5);//4
        list.add(row6);//5
        list.add(row7);//6
        list.add(row8);//7
        list.add(row9);//8
        list.add(row10);//9
        list.add(row11);//10
        list.add(row12);//11
        list.add(row13);//12
        list.add(row14);//13
        list.add(row21);//14
        list.add(row22);//15
        list.add(row23);//16
        list.add(row24);//17
        list.add(row25);//18
        list.add(row26);//19
        list.add(row27);//20
        list.add(row110);//21
        list.add(row120);//22
        list.add(row260);//23

        return list;


    }
    class Agent implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            MloDischargeSummaryListModel mloDischargeSummaryListModel=new MloDischargeSummaryListModel();
            mloDischargeSummaryListModel.setOrgId(rs.getString("id"));
            mloDischargeSummaryListModel.setOrganization_Name(rs.getString("Organization_Name"));
            return mloDischargeSummaryListModel ;
        }
    }
    class Mlo implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            MloDischargeSummaryListModel mloDischargeSummaryListModel=new MloDischargeSummaryListModel();
            mloDischargeSummaryListModel.setMlocode(rs.getString("mlocode"));
            return mloDischargeSummaryListModel ;
        }
    }
    class CommonResult implements RowMapper{

        @Override
        public MloDischargeSummaryListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            MloDischargeSummaryListModel mloDischargeSummaryListModel=new MloDischargeSummaryListModel();
            mloDischargeSummaryListModel.setNumber(rs.getInt("number"));
            mloDischargeSummaryListModel.setGross_weight(rs.getInt("gross_weight"));
            mloDischargeSummaryListModel.setNet_weight(rs.getInt("net_weight"));
            mloDischargeSummaryListModel.setTare_weight(rs.getInt("tare_weight"));

            return mloDischargeSummaryListModel;
        }
    }
    class IgmInfo implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            MloDischargeSummaryListModel mloDischargeSummaryListModel=new MloDischargeSummaryListModel();
            mloDischargeSummaryListModel.setVessel_Name(rs.getString("Vessel_Name"));
            mloDischargeSummaryListModel.setVoy_No(rs.getString("Voy_No"));
            mloDischargeSummaryListModel.setNet_Tonnage(rs.getString("Net_Tonnage"));
            mloDischargeSummaryListModel.setPort_of_Shipment(rs.getString("Port_of_Shipment"));
            mloDischargeSummaryListModel.setPort_of_Destination(rs.getString("Port_of_Destination"));
            mloDischargeSummaryListModel.setSailed_Year(rs.getString("Sailed_Year"));
            mloDischargeSummaryListModel.setSubmitee_Org_Id(rs.getInt("Submitee_Org_Id"));
            mloDischargeSummaryListModel.setName_of_Master(rs.getString("Name_of_Master"));
            mloDischargeSummaryListModel.setOrganization_Name(rs.getString("Organization_Name"));
            mloDischargeSummaryListModel.setSubmitee_Org_Type(rs.getString("Submitee_Org_Type"));
            mloDischargeSummaryListModel.setIs_Foreign(rs.getString("is_Foreign"));
            mloDischargeSummaryListModel.setVessel_Type(rs.getString("Vessel_Type"));
            mloDischargeSummaryListModel.setPort_of_Registry(rs.getString("Port_of_Registry"));

            return mloDischargeSummaryListModel;
        }
    }
    class DateInfo implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            MloDischargeSummaryListModel mloDischargeSummaryListModel=new MloDischargeSummaryListModel();
            mloDischargeSummaryListModel.setETA_Date(rs.getString("ETA_Date"));
            mloDischargeSummaryListModel.setETD_Date(rs.getString("ETD_Date"));
            mloDischargeSummaryListModel.setActual_Berth(rs.getString("Actual_Berth"));
            mloDischargeSummaryListModel.setActual_Berth_time(rs.getString("Actual_Berth_time"));
            return mloDischargeSummaryListModel;
        }
    }
}
