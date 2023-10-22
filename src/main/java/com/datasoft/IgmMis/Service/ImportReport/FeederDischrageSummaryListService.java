package com.datasoft.IgmMis.Service.ImportReport;
import com.datasoft.IgmMis.Model.ImportReport.FeederDischargeSummaryListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Service
public class FeederDischrageSummaryListService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;

    private List<FeederDischargeSummaryListModel> list;

    public List getVesselInfo(String rotation){
        String query="";
        String sqlQuery="";
        List<FeederDischargeSummaryListModel> vesselInfo=new ArrayList<>();
        query="SELECT * FROM igm_details WHERE Import_Rotation_No='"+rotation+"'";
        List resultList=primaryDBTemplate.query(query,new FeederDischrageSummaryListService.IgmDetail());
        if(resultList.size()<1){
            //  vesselInfo =feederDischargeSummaryListRepository.getVesselInfoFromIgmMaster(rotation);
            sqlQuery="select Import_Rotation_No,Vessel_Name,Total_number_of_bols,Total_number_of_packages,Total_number_of_containers,Total_gross_mass from igm_masters where Import_Rotation_No='"+rotation+"'";
            vesselInfo=primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.VesselInfoFromIgmMaster());

        }
        else{
            sqlQuery="SELECT igm_masters.Import_Rotation_No,igm_masters.Vessel_Name,Voy_No,Net_Tonnage,Port_of_Shipment,\n" +
                    "Port_of_Destination,Sailed_Year,Submitee_Org_Id,Name_of_Master,Organization_Name,is_Foreign,Vessel_Type,\n" +
                    "Actual_Berth,Actual_Berth_time\n" +
                    "FROM\n" +
                    "igm_masters \n" +
                    "LEFT JOIN organization_profiles ON \n" +
                    "organization_profiles.id=igm_masters.Submitee_Org_Id\n" +
                    "LEFT JOIN vessels ON vessels.id=igm_masters.Vessel_Id\n" +
                    "LEFT JOIN vessels_berth_detail ON vessels_berth_detail.Import_Rotation_No=igm_masters.Import_Rotation_No\n" +
                    "WHERE igm_masters.Import_Rotation_No='"+rotation+"'";
            vesselInfo=primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.VesselInfoFromIgmMaster1());

        }
        return vesselInfo;
    }
    public List getFeederDischargeSummaryListOffDockWise(String rotation){
        Integer j=0;
        Integer totalRes=0;
        Integer total=0;
        Integer total1=0;
        Integer total3=0;
        Integer total4=0;
        Integer total5=0;
        Integer total6=0;
        Integer total7=0;
        Integer total8=0;
        Integer totalImdgTues=0;
        Integer totalTransTues=0;
        Integer totalTues = 0;
        Integer totalRefferTues = 0;
        Integer totalEmptyTues = 0;
        Integer totalLadenTues = 0;
        Integer total9=0;
        Integer total10=0;
        Integer total11=0;
        Integer total12=0;
        Integer totalRes1=0;
        Integer total2=0;
        Integer totalNew=0;
        Integer grant20=0;
        Integer grant40=0;
        Integer grant=0;
        Integer sum=0;
        Integer row1Total=0;
        Integer row1Mty20=0;
        Integer row1Ref20=0;
        Integer row1Dmg20=0;
        Integer row1Tran20=0;
        Integer row145ld=0;
        Integer newRow1=0;
        Integer row1Mty40=0;
        Integer row1Ref40=0;
        Integer row1Dmg40=0;
        Integer row1tran40=0;
        Integer row145Mty=0;

        FeederDischargeSummaryListModel feederDischargeSummaryListModel;

        // feederDischargeSummaryListModel=new FeederDischargeSummaryListModel();
        
        List<FeederDischargeSummaryListModel> feederDischargeSummaryListModeOffDocklList=new ArrayList<>();
        String query="";
        String sqlQuery="";
        // String sqlQuery1="";
        query="SELECT DISTINCT submitee_org_id,organization_profiles.Organization_Name AS Organization_Name,organization_profiles.Agent_Code,mlocode AS mlocode FROM igm_details \n" +
                "LEFT JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id \n" +
                "WHERE Import_Rotation_No='"+rotation+"' ORDER BY Organization_Name,mlocode";

        List<FeederDischargeSummaryListModel> list=primaryDBTemplate.query(query,new FeederDischrageSummaryListService.OrgProfile());
        for(int i=0;i<list.size();i++){
            feederDischargeSummaryListModel=new FeederDischargeSummaryListModel();
            sum=sum+grant;
            feederDischargeSummaryListModel.setOrganization_Name(list.get(i).getOrganization_Name());
            String mloCode=list.get(i).getMlocode();
            Integer submittedOrgId=list.get(i).getSubmitee_org_id();
            sqlQuery="select mlodescription,mlo_agent_code_ctms,agent_from,org_id from mlo_detail where mlocode='"+mloCode+"'";
            List<FeederDischargeSummaryListModel> sqlAgentCodeList=primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.SqlAgentCode());
            if(sqlAgentCodeList.size()>0) {
                feederDischargeSummaryListModel.setMlodescription(sqlAgentCodeList.get(0).getMlodescription());
                feederDischargeSummaryListModel.setMlo_agent_code_ctms(sqlAgentCodeList.get(0).getMlo_agent_code_ctms());
                feederDischargeSummaryListModel.setMlocode(list.get(i).getMlocode());
            }
            sqlQuery="select count(distinct cont_number) as total \n" +
                    "from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and type_of_igm<>'TS' \n" +
                    "and off_dock_id  IN('2594','2595','2596','2597','2598','2599','2600','2601','2603','2620','2624','2643','2645','2646','2647','3328','3450','3697','3709','3725','4013','5884') and cont_status not in ('EMT','EMPTY','MT','ETY') and cont_size =20 and  igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel> row1List=primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1List.size()>0){
                row1Total=row1List.get(0).getTotal();
            }

            sqlQuery="SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "WHERE Import_Rotation_No='"+rotation+"' AND Submitee_Org_Id='"+submittedOrgId+"' AND mlocode='"+mloCode+"' AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')AND type_of_igm<>'TS' \n" +
                    "AND off_dock_id IN('2594','2595','2596','2597','2598','2599','2600','2601','2603','2620','2624','2643','2645','2646','2647','3328','3450','3697','3709','3725','4013','5884') AND   (cont_status='EMT' OR cont_status='Empty' OR cont_status='MT' OR cont_status='ETY') AND cont_size =20 AND (cont_imo = '' AND cont_un = '' AND igm_details.final_submit=1)";
            List<FeederDischargeSummaryListModel>row1Mty20List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Mty20List.size()>0) {
                row1Mty20 = row1Mty20List.get(0).getTotal();
            }
            sqlQuery="SELECT COUNT(DISTINCT cont_number) AS total FROM igm_detail_container INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "WHERE Import_Rotation_No='"+rotation+"' AND Submitee_Org_Id='"+submittedOrgId+"' AND mlocode='"+mloCode+"' AND cont_iso_type LIKE '%R%' AND cont_iso_type NOT IN ('DRY') AND type_of_igm<>'TS' \n" +
                    "AND off_dock_id IN('2594','2595','2596','2597','2598','2599','2600','2601','2603','2620','2624','2643','2645','2646','2647','3328','3450','3697','3709','3725','4013','5884')  AND cont_size =20 AND igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel>row1Ref20List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Ref20List.size()>0) {
                row1Ref20 = row1Ref20List.get(0).getTotal();
            }

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and type_of_igm<>'TS' \n" +
                    "and off_dock_id  IN('2594','2595','2596','2597','2598','2599','2600','2601','2603','2620','2624','2643','2645','2646','2647','3328','3450','3697','3709','3725','4013','5884')  and cont_size =20 and (cont_imo <> '' and cont_un <> '' and igm_details.final_submit=1)";
            List<FeederDischargeSummaryListModel>row1Dmg20List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Dmg20List.size()>0) {
                row1Dmg20=row1Dmg20List.get(0).getTotal();
            }

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and type_of_igm='TS' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
                    "and off_dock_id  IN('2594','2595','2596','2597','2598','2599','2600','2601','2603','2620','2624','2643','2645','2646','2647','3328','3450','3697','3709','3725','4013','5884')   and cont_size =20 and igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel>rowTran20List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(rowTran20List.size()>0) {
                row1Tran20=rowTran20List.get(0).getTotal();

            }
            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='+"+mloCode+"'  and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
                    "and cont_size >40 and igm_details.final_submit=1 and (cont_status <> 'EMT' and cont_status <> 'Empty' and cont_status <> 'MT' and cont_status <> 'ETY')";
            List<FeederDischargeSummaryListModel>row145ldList =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row145ldList.size()>0) {
                row145ld = row145ldList.get(0).getTotal();
            }

            totalRes=row1Total-row1Mty20-row1Ref20-row1Dmg20-row1Tran20-row145ld;
            total1=row1Total-row1Mty20-row1Ref20-row1Dmg20-row1Tran20-row145ld;
            total=total+total1;
            feederDischargeSummaryListModel.setTotalRes(totalRes);

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and type_of_igm<>'TS' \n" +
                    "and off_dock_id  IN('2594','2595','2596','2597','2598','2599','2600','2601','2603','2620','2624','2643','2645','2646','2647','3328','3450','3697','3709','3725','4013','5884') and cont_status not in ('EMT','EMPTY','MT','ETY') and cont_size =40 and igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel> newRow1List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(newRow1List.size()>0){
                newRow1 =newRow1List.get(0).getTotal();

            }

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')and type_of_igm<>'TS' \n" +
                    "and off_dock_id IN('2594','2595','2596','2597','2598','2599','2600','2601','2603','2620','2624','2643','2645','2646','2647','3328','3450','3697','3709','3725','4013','5884') and   (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY') and cont_size =40 and (cont_imo = '' and cont_un = '' and igm_details.final_submit=1)";

            List<FeederDischargeSummaryListModel> row1MtyList =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1MtyList.size()>0){
                row1Mty40 =row1MtyList.get(0).getTotal();
            }

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type like '%R%' and cont_iso_type not in ('DRY') and type_of_igm<>'TS' \n" +
                    "and off_dock_id IN('2594','2595','2596','2597','2598','2599','2600','2601','2603','2620','2624','2643','2645','2646','2647','3328','3450','3697','3709','3725','4013','5884')  and cont_size =40 and igm_details.final_submit=1";

            List<FeederDischargeSummaryListModel> row1RefList =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1RefList.size()>0){
                row1Ref40 =row1RefList.get(0).getTotal();
            }

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and type_of_igm<>'TS' \n" +
                    "and off_dock_id  IN('2594','2595','2596','2597','2598','2599','2600','2601','2603','2620','2624','2643','2645','2646','2647','3328','3450','3697','3709','3725','4013','5884')  and cont_size =40 and (cont_imo <> '' and cont_un <> '' and igm_details.final_submit=1)";


            List<FeederDischargeSummaryListModel> row1DmgList =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1DmgList.size()>0) {
                row1Dmg40 = row1DmgList.get(0).getTotal();
            }

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and type_of_igm='TS' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
                    "and off_dock_id  IN('2594','2595','2596','2597','2598','2599','2600','2601','2603','2620','2624','2643','2645','2646','2647','3328','3450','3697','3709','3725','4013','5884')   and cont_size =40 and igm_details.final_submit=1";

            List<FeederDischargeSummaryListModel> row1tranList =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1tranList.size()>0) {
                row1tran40 =row1tranList.get(0).getTotal();
            }

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
                    "and cont_size >40 and igm_details.final_submit=1 and   (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY') ";
            List<FeederDischargeSummaryListModel> row145MtyList =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row145MtyList.size()>0){
                row145Mty =row145MtyList.get(0).getTotal();
            }
            //System.out.println("newRow1 : "+newRow1+" row1Mty40 : "+row1Mty40 +" row1Ref40 : " + row1Ref40 +" row1Ref40 : " +row1Dmg40+ " row1tran40 : "+ row1tran40 + " row145Mty: "+ row145Mty  );
            totalRes1=newRow1-row1Mty40-row1Ref40-row1Dmg40-row1tran40-row145Mty;
            total2=newRow1-row1Mty40-row1Ref40-row1Dmg40-row1tran40-row145Mty;
            totalNew=totalNew+total2;
            feederDischargeSummaryListModel.setTotalRes1(totalRes1);
            feederDischargeSummaryListModel.setRowLadenTeus((total1*1)+(total2*2));
            feederDischargeSummaryListModel.setRow1Mty20(row1Mty20);
            total3=feederDischargeSummaryListModel.getRow1Mty20();
            feederDischargeSummaryListModel.setRow1Mty40(row1Mty40);
            total4=feederDischargeSummaryListModel.getRow1Mty40();
            feederDischargeSummaryListModel.setRowEmptyTeus((total3*1)+(total4*2));
            feederDischargeSummaryListModel.setRow1Ref20(row1Ref20);
            total5=feederDischargeSummaryListModel.getRow1Ref20();
            feederDischargeSummaryListModel.setRow1Ref40(row1Ref40);
            total6=feederDischargeSummaryListModel.getRow1Ref40();
            feederDischargeSummaryListModel.setRowRefferTeus((total5*1)+(total6*2));
            feederDischargeSummaryListModel.setRow1Dmg20(row1Dmg20);
            total7=feederDischargeSummaryListModel.getRow1Dmg20();
            feederDischargeSummaryListModel.setRow1Dmg40(row1Dmg40);
            total8=feederDischargeSummaryListModel.getRow1Dmg40();
            feederDischargeSummaryListModel.setRowImdgTeus((total7*1)+(total8*2));
            feederDischargeSummaryListModel.setRow1tran20(row1Tran20);
            total9=feederDischargeSummaryListModel.getRow1tran20();
            feederDischargeSummaryListModel.setRow1tran40(row1tran40);
            total10=feederDischargeSummaryListModel.getRow1tran40();
            feederDischargeSummaryListModel.setRowTransTeus1((total9*1)+(total10*2));

            feederDischargeSummaryListModel.setRow145Id(row145ld);
            total11=feederDischargeSummaryListModel.getRow145Id();
            feederDischargeSummaryListModel.setRow145Mty(row145Mty);
            total12=feederDischargeSummaryListModel.getRow145Mty();
            feederDischargeSummaryListModel.setGrant20(total1+total3+total5+total7+total9);
            feederDischargeSummaryListModel.setGrant40(total2+total4+total6+total8+total10+total11+total12);
            feederDischargeSummaryListModel.setGrant(total1+total2+total3+total4+total5+total6+total7+total8+total9+total10+total11+total12);
            //  grant=feederDischargeSummaryListModel.getGrant();
            feederDischargeSummaryListModel.setRowTues((feederDischargeSummaryListModel.getGrant20()*1)+(feederDischargeSummaryListModel.getGrant40()*2));
            totalTues=totalTues+feederDischargeSummaryListModel.getRowTues();
            feederDischargeSummaryListModeOffDocklList.add(feederDischargeSummaryListModel);


        }


        return feederDischargeSummaryListModeOffDocklList;
    }

    public List getFeederDischargeSummaryList(String rotation){

        Integer j=0;
        Integer totalRes=0;
        Integer total=0;
        Integer total1=0;
        Integer total3=0;
        Integer total4=0;
        Integer total5=0;
        Integer total6=0;
        Integer total7=0;
        Integer total8=0;
        Integer totalImdgTues=0;
        Integer totalTransTues=0;
        Integer totalTues = 0;
        Integer totalRefferTues = 0;
        Integer totalEmptyTues = 0;
        Integer totalLadenTues = 0;
        Integer total9=0;
        Integer total10=0;
        Integer total11=0;
        Integer total12=0;
        Integer totalRes1=0;
        Integer total2=0;
        Integer totalNew=0;
        Integer grant20=0;
        Integer grant40=0;
        Integer grant=0;
        Integer sum=0;
        Integer row1Total=0;
        Integer row1Mty20=0;
        Integer row1Ref20=0;
        Integer row1Dmg20=0;
        Integer row1Tran20=0;
        Integer row145ld=0;
        Integer newRow1=0;
        Integer row1Mty40=0;
        Integer row1Ref40=0;
        Integer row1Dmg40=0;
        Integer row1tran40=0;
        Integer row145Mty=0;
        Integer row1Laden20Total=0;
        Integer laden20=0;
        Integer row1Laden40Total=0;
        Integer laden40=0;
        Integer total13=0;
        Integer total14=0;
        Integer rowTues=0;
        FeederDischargeSummaryListModel feederDischargeSummaryListModel;
        // feederDischargeSummaryListModel=new FeederDischargeSummaryListModel();
        List<FeederDischargeSummaryListModel> feederDischargeSummaryListModeOffDocklList=new ArrayList<>();
        String query="";
        String sqlQuery="";
        // String sqlQuery1="";
        query="SELECT DISTINCT submitee_org_id,organization_profiles.Organization_Name AS Organization_Name,organization_profiles.Agent_Code,mlocode AS mlocode FROM igm_details \n" +
                "LEFT JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id \n" +
                "WHERE Import_Rotation_No='"+rotation+"' ORDER BY Organization_Name,mlocode";

        List<FeederDischargeSummaryListModel> list=primaryDBTemplate.query(query,new FeederDischrageSummaryListService.OrgProfile());
        for(int i=0;i<list.size();i++){
            feederDischargeSummaryListModel=new FeederDischargeSummaryListModel();
            sum=sum+grant;
            feederDischargeSummaryListModel.setOrganization_Name(list.get(i).getOrganization_Name());
            String mloCode=list.get(i).getMlocode();
            Integer submittedOrgId=list.get(i).getSubmitee_org_id();
            sqlQuery="select mlodescription,mlo_agent_code_ctms,agent_from,org_id from mlo_detail where mlocode='"+mloCode+"'";
            List<FeederDischargeSummaryListModel> sqlAgentCodeList=primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.SqlAgentCode());
            if(sqlAgentCodeList.size()>0) {
                feederDischargeSummaryListModel.setMlodescription(sqlAgentCodeList.get(0).getMlodescription());
                feederDischargeSummaryListModel.setMlo_agent_code_ctms(sqlAgentCodeList.get(0).getMlo_agent_code_ctms());
                feederDischargeSummaryListModel.setMlocode(list.get(i).getMlocode());
            }
            sqlQuery="select count(distinct cont_number) as total \n" +
                    "from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and type_of_igm<>'TS' \n" +
                    "and off_dock_id <>'2592' and cont_status not in ('EMT','EMPTY','MT','ETY') and cont_size =20 and  igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel> row1Laden20List=primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Laden20List.size()>0){
                row1Laden20Total=row1Laden20List.get(0).getTotal();
            }

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and type_of_igm<>'TS' \n" +
                    "and off_dock_id<>'2592'  and cont_size =20 and (cont_imo <> '' and cont_un <> '' and igm_details.final_submit=1)";
            List<FeederDischargeSummaryListModel>laden20List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(laden20List.size()>0) {
                laden20 = laden20List.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setTotalRes(row1Laden20Total-laden20);
            total1=row1Laden20Total-laden20;

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and type_of_igm<>'TS' \n" +
                    "and off_dock_id<>'2592' and cont_status not in ('EMT','EMPTY','MT','ETY') and cont_size =40 and igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel> row1Laden40List=primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Laden40List.size()>0){
                row1Laden40Total=row1Laden40List.get(0).getTotal();
            }

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and type_of_igm<>'TS' and off_dock_id<>'2592' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
                    "and  cont_size =40 and (cont_imo <> '' or cont_un <> '') and igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel>laden40List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(laden40List.size()>0) {
                laden40 = laden40List.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setTotalRes1(row1Laden40Total-laden40);
            total2=row1Laden40Total-laden40;
            feederDischargeSummaryListModel.setRowLadenTeus((total1*1)+(total2*2));

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')and type_of_igm<>'TS' \n" +
                    "and off_dock_id<>'2592' and   (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY') and cont_size =20 and (cont_imo = '' and cont_un = '' and igm_details.final_submit=1)";
            List<FeederDischargeSummaryListModel>row1Mty20List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Mty20List.size()>0) {
                row1Mty20 = row1Mty20List.get(0).getTotal();
            }

            feederDischargeSummaryListModel.setRow1Mty20(row1Mty20);
            total3=row1Mty20;


            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') and type_of_igm<>'TS' \n" +
                    "and off_dock_id<>'2592' and  (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY') and cont_size =40 and (cont_imo = '' and cont_un = '' and igm_details.final_submit=1)";
            List<FeederDischargeSummaryListModel>row1Mty40List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Mty40List.size()>0) {
                row1Mty40 = row1Mty40List.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setRow1Mty40(row1Mty40);
            total4=row1Mty40;
            feederDischargeSummaryListModel.setRowEmptyTeus((total3*1)+(total4*2));

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type like '%R%' and cont_iso_type not in ('DRY') and type_of_igm<>'TS' \n" +
                    "and off_dock_id<>'2592'  and cont_size =20 and igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel>row1Ref20List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Ref20List.size()>0) {
                row1Ref20 = row1Ref20List.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setRow1Ref20(row1Ref20);
            total5=row1Ref20;

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type like '%R%' and cont_iso_type not in ('DRY') and type_of_igm<>'TS' \n" +
                    "and off_dock_id<>'2592'  and cont_size =40 and igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel>row1Ref40List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Ref40List.size()>0) {
                row1Ref40 = row1Ref40List.get(0).getTotal();
            }

            feederDischargeSummaryListModel.setRow1Ref40(row1Ref40);
            total6=row1Ref40;
            feederDischargeSummaryListModel.setRowRefferTeus((total5*1)+(total6*2));

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_type not in ('REFER','REEFER')and type_of_igm<>'TS' \n" +
                    "and off_dock_id<>'2592'  and cont_size =20 and (cont_imo <> '' and cont_un <> '' and igm_details.final_submit=1)";
            List<FeederDischargeSummaryListModel>row1Dmg20List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Dmg20List.size()>0) {
                row1Dmg20 = row1Dmg20List.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setRow1Dmg20(row1Dmg20);
            total7=row1Dmg20;

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and type_of_igm<>'TS' and off_dock_id<>'2592' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
                    "and  cont_size =40 and (cont_imo <> '' or cont_un <> '') and igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel>row1Dmg40List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Dmg40List.size()>0) {
                row1Dmg40 = row1Dmg40List.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setRow1Dmg40(row1Dmg40);
            total8=row1Dmg40;
            feederDischargeSummaryListModel.setRowImdgTeus((total7*1)+(total8*2));

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and type_of_igm='TS' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
                    "and off_dock_id<>'2592'  and cont_size =20 and igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel>row1Tran20List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Tran20List.size()>0) {
                row1Tran20 = row1Tran20List.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setRow1tran20(row1Tran20);
            total9=row1Tran20;

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and type_of_igm='TS' and cont_iso_type not in('22R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
                    "and off_dock_id<>'2592' and  cont_size =40 and igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel>row1Tran40List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Tran40List.size()>0) {
                row1tran40 = row1Tran40List.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setRow1tran40(row1tran40);
            total10=row1tran40;
            feederDischargeSummaryListModel.setRowTransTeus1((total9*1)+(total10*2));

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3') \n" +
                    "and off_dock_id='2592'  and cont_size =20 and igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel>row145IdList =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row145IdList.size()>0) {
                row145ld = row145IdList.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setRow145Id(row145ld);
            total11=row145ld;
            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
                    "and off_dock_id='2592' and  cont_size =40 and igm_details.final_submit=1";
            List<FeederDischargeSummaryListModel>row145MtyList =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row145MtyList.size()>0) {
                row145Mty = row145MtyList.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setRow145Mty(row145Mty);
            total12=row145Mty;
            feederDischargeSummaryListModel.setGrant20((total11*1)+(total12*2));

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"'  and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
                    " and cont_size >40 and igm_details.final_submit=1 and (cont_status <> 'EMT' and cont_status <> 'Empty' and cont_status <> 'MT' and cont_status <> 'ETY')";
            List<FeederDischargeSummaryListModel>row1Grant40List =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1Grant40List.size()>0) {
                grant40 = row1Grant40List.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setGrant40(grant40);
            total13=grant40;

            sqlQuery="select count(distinct cont_number) as total from igm_detail_container inner join igm_details on igm_details.id=igm_detail_container.igm_detail_id \n" +
                    "where Import_Rotation_No='"+rotation+"' and Submitee_Org_Id='"+submittedOrgId+"' and mlocode='"+mloCode+"' and cont_iso_type not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
                    " and cont_size >40 and igm_details.final_submit=1 and   (cont_status='EMT' or cont_status='Empty' or cont_status='MT' or cont_status='ETY')";
            List<FeederDischargeSummaryListModel>row1TuesList =primaryDBTemplate.query(sqlQuery,new FeederDischrageSummaryListService.Row1());
            if(row1TuesList.size()>0) {
                rowTues = row1TuesList.get(0).getTotal();
            }
            feederDischargeSummaryListModel.setRowTues(rowTues);
            total14=rowTues;
            feederDischargeSummaryListModel.setGrant(total1+total2+total3+total4+total5+total6+total7+total8+total9+total10+total11+total12+total13+total14);
            feederDischargeSummaryListModeOffDocklList.add(feederDischargeSummaryListModel);



        }


        return feederDischargeSummaryListModeOffDocklList;

    }




    class IgmDetail implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            return null;
        }
    }
    class VesselInfoFromIgmMaster implements RowMapper{

        @Override
        public FeederDischargeSummaryListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            FeederDischargeSummaryListModel feederDischargeSummaryListModel=new FeederDischargeSummaryListModel();
            feederDischargeSummaryListModel.setImport_Rotation_No(rs.getString("Import_Rotation_No"));
            feederDischargeSummaryListModel.setVessel_Name(rs.getString("Vessel_Name"));
            return feederDischargeSummaryListModel;
        }
    }
    class VesselInfoFromIgmMaster1 implements RowMapper{

        @Override
        public FeederDischargeSummaryListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            FeederDischargeSummaryListModel feederDischargeSummaryListModel=new FeederDischargeSummaryListModel();
            feederDischargeSummaryListModel.setImport_Rotation_No(rs.getString("Import_Rotation_No"));
            feederDischargeSummaryListModel.setVessel_Name(rs.getString("Vessel_Name"));
            return feederDischargeSummaryListModel;
        }
    }
    class OrgProfile implements RowMapper{

        @Override
        public FeederDischargeSummaryListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            FeederDischargeSummaryListModel feederDischargeSummaryListModel=new FeederDischargeSummaryListModel();
            feederDischargeSummaryListModel.setSubmitee_org_id(rs.getInt("submitee_org_id"));
            feederDischargeSummaryListModel.setOrganization_Name(rs.getString("Organization_Name"));
            feederDischargeSummaryListModel.setAgent_Code(rs.getString("Agent_Code"));
            feederDischargeSummaryListModel.setMlocode(rs.getString("mlocode"));
            return feederDischargeSummaryListModel ;
        }
    }
    class SqlAgentCode implements RowMapper{

        @Override
        public FeederDischargeSummaryListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            FeederDischargeSummaryListModel feederDischargeSummaryListModel=new FeederDischargeSummaryListModel();
            feederDischargeSummaryListModel.setMlodescription(rs.getString("mlodescription"));
            feederDischargeSummaryListModel.setMlo_agent_code_ctms(rs.getString("mlo_agent_code_ctms"));
            feederDischargeSummaryListModel.setAgent_from(rs.getString("agent_from"));
            feederDischargeSummaryListModel.setOrg_id(rs.getInt("org_id"));
            return feederDischargeSummaryListModel;
        }
    }
    class Row1 implements RowMapper{

        @Override
        public FeederDischargeSummaryListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            FeederDischargeSummaryListModel feederDischargeSummaryListModel=new FeederDischargeSummaryListModel();
            feederDischargeSummaryListModel.setTotal(rs.getInt("total"));

            return feederDischargeSummaryListModel;
        }
    }


}
