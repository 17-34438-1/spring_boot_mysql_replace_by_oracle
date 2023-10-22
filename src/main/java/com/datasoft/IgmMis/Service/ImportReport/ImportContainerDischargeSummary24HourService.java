package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.ImportContainerDischargeSummary24HourModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ImportContainerDischargeSummary24HourService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    private List<ImportContainerDischargeSummary24HourModel> resultList;
    public List getImportContainer24HourSummaryList(String fromDate,String fromTime, String toDate,String toTime,Integer vvdGkey){
        String sqlQuery="";
        String condition="";

        if(fromDate!="" && toDate!="")
        {
            if(fromTime!="")
                fromDate = fromDate+" "+fromTime+":00";
            else
                fromDate = fromDate+" 00:00:00";

            if(toTime!="") {
                toDate= toDate + " " + toTime + ":00";
            }
            else {
                toDate = toDate+ " 23:59:59";
            }

            condition = " where time_in between '"+fromDate+"' and '"+toDate+"'";
        }
        else
        {
            condition = " ";
        }
        System.out.println("condition :"+ condition );
        sqlQuery="SELECT gkey,mlo,\n" +
                "IFNULL(SUM(D_20),0) AS D_20,\n" +
                "IFNULL(SUM(D_40),0) AS D_40,\n" +
                "IFNULL(SUM(H_40),0) AS H_40,\n" +
                "IFNULL(SUM(H_45),0) AS H_45,\n" +
                "IFNULL(SUM(R_20),0) AS R_20,\n" +
                "IFNULL(SUM(RH_40),0) AS RH_40,\n" +
                "IFNULL(SUM(OT_20),0) AS OT_20,\n" +
                "IFNULL(SUM(OT_40),0) AS OT_40,\n" +
                "IFNULL(SUM(FR_20),0) AS FR_20,\n" +
                "IFNULL(SUM(FR_40),0) AS FR_40,\n" +
                "IFNULL(SUM(TK_20),0) AS TK_20,\n" +
                "IFNULL(SUM(MD_20),0) AS MD_20,\n" +
                "IFNULL(SUM(MD_40),0) AS MD_40,\n" +
                "IFNULL(SUM(MH_40),0) AS MH_40,\n" +
                "IFNULL(SUM(MH_45),0) AS MH_45,\n" +
                "IFNULL(SUM(MR_20),0) AS MR_20,\n" +
                "IFNULL(SUM(MRH_40),0) AS MRH_40,\n" +
                "IFNULL(SUM(MOT_20),0) AS MOT_20,\n" +
                "IFNULL(SUM(MOT_40),0) AS MOT_40,\n" +
                "IFNULL(SUM(MFR_20),0) AS MFR_20,\n" +
                "IFNULL(SUM(MFR_40),0) AS MFR_40,\n" +
                "IFNULL(SUM(MTK_20),0) AS MTK_20,\n" +
                "IFNULL(SUM(grand_tot),0) AS grand_tot,\n" +
                "IFNULL(SUM(tues),0) AS tues,\n" +
                "SUM(goods_and_ctr_wt_kg) AS weight,time_in\n" +
                " FROM (\n" +
                "SELECT DISTINCT sparcsn4.inv_unit.gkey AS gkey,\n" +
                "(SELECT disch_dt FROM ctmsmis.mis_disch_cont WHERE gkey=sparcsn4.inv_unit.gkey) AS time_in,\n" +
                "r.id AS mlo,goods_and_ctr_wt_kg,\n" +
                "RIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size,\n" +
                "RIGHT(sparcsn4.ref_equip_type.nominal_height,2) AS height,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS D_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS D_40, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='96' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS H_40, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '45' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='96' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS H_45,\n" +
                "\n" +
                "\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS R_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS RH_40,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS OT_20,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS OT_40,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS FR_20,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS FR_40,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('TN','TD','TG')  THEN 1  \n" +
                "ELSE NULL END) AS TK_20,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MD_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MD_40, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='96' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MH_40, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '45' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='96' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MH_45,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS MR_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS MRH_40,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS MOT_20,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS MOT_40,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MFR_20,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MFR_40,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2)='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('TN','TD','TG')  THEN 1  \n" +
                "ELSE NULL END) AS MTK_20,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) IN('20','40','45','42')  THEN 1 ELSE NULL END) AS grand_tot,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20  THEN 1 ELSE 2 END) AS tues    \n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON argo_carrier_visit.gkey=sparcsn4.inv_unit.declrd_ib_cv\n" +
                "INNER JOIN sparcsn4.argo_visit_details ON sparcsn4.argo_visit_details.gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_visit_details.gkey\n" +
                "INNER JOIN sparcsn4.inv_goods ON inv_goods.gkey=inv_unit.goods\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN  ( sparcsn4.ref_bizunit_scoped r                                                 \n" +
                "LEFT JOIN ( sparcsn4.ref_agent_representation X                                                 \n" +
                "LEFT JOIN sparcsn4.ref_bizunit_scoped Y ON X.agent_gkey=Y.gkey )  ON r.gkey=X.bzu_gkey)  ON r.gkey = inv_unit.line_op \n" +
                "WHERE inv_unit.category='IMPRT' AND vvd_gkey='"+vvdGkey+"' \n" +
                " ) AS tmp "+condition+" GROUP BY mlo WITH ROLLUP";
        System.out.println(sqlQuery);

        List<ImportContainerDischargeSummary24HourModel> list=secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeSummary24HourService.CotainerDischargeSummary());
        return list;
    }
    public List getImportCotainerDischarge24HourDetail(String fromDate,String fromTime, String toDate,String toTime,Integer vvdGkey){
        String sqlQuery="";
        String condition="";

        if(fromDate!="" && toDate!="")
        {
            if(fromTime!="")
                fromDate = fromDate+" "+fromTime+":00";
            else
                fromDate = fromDate+" 00:00:00";

            if(toTime!="") {
                toDate= toDate + " " + toTime + ":00";
            }
            else {
                toDate = toDate+ " 23:59:59";
            }

            condition = " AND time_in between '"+fromDate+"' and '"+toDate+"'";
        }
        else
        {
            condition = " ";
        }
        System.out.println("condition :"+ condition );
        sqlQuery="SELECT DISTINCT r.id AS totMlo\n" +
                "FROM sparcsn4.inv_unit \n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON argo_carrier_visit.gkey=sparcsn4.inv_unit.declrd_ib_cv \n" +
                "INNER JOIN sparcsn4.argo_visit_details ON sparcsn4.argo_visit_details.gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey \n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_visit_details.gkey\n" +
                "INNER JOIN  ( sparcsn4.ref_bizunit_scoped r        \n" +
                "LEFT JOIN ( sparcsn4.ref_agent_representation X        \n" +
                "LEFT JOIN sparcsn4.ref_bizunit_scoped Y ON X.agent_gkey=Y.gkey )  ON r.gkey=X.bzu_gkey        )  ON r.gkey = inv_unit.line_op \n" +
                "WHERE inv_unit.category='IMPRT' AND sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdGkey+"' "+condition+" ORDER BY totMlo";


        List<ImportContainerDischargeSummary24HourModel> list=secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeSummary24HourService.Mlo());
        resultList=new ArrayList<>();
        for(int i=0; i<list.size();i++){
            ImportContainerDischargeSummary24HourModel detailModel;

            String query="";
            ImportContainerDischargeSummary24HourModel importContainerDischargeSummary24HourModel=list.get(i);
            String totMlo =importContainerDischargeSummary24HourModel.getTotMlo();
            query="SELECT * FROM\n" +
                    "(\n" +
                    "SELECT CONCAT(SUBSTRING(sparcsn4.inv_unit.id,1,4),' ',SUBSTRING(sparcsn4.inv_unit.id,5)) AS cont_no,\n" +
                    "r.id AS mlo,\n" +
                    "(SELECT RIGHT(sparcsn4.ref_equip_type.nominal_length,2) FROM sparcsn4.inv_unit_equip\n" +
                    "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
                    "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                    "WHERE sparcsn4.inv_unit_equip.unit_gkey=inv_unit.gkey\n" +
                    ")  AS size,\n" +
                    "(SELECT RIGHT(sparcsn4.ref_equip_type.nominal_height,2) FROM sparcsn4.inv_unit_equip\n" +
                    "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
                    "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                    "WHERE sparcsn4.inv_unit_equip.unit_gkey=inv_unit.gkey\n" +
                    ")  AS height,\n" +
                    "sparcsn4.ref_equip_type.id AS iso,sparcsn4.ref_equip_type.iso_group AS iso_group,\n" +
                    "sparcsn4.vsl_vessel_visit_details.vvd_gkey,\n" +
                    " sparcsn4.inv_unit.freight_kind AS freight_kind,\n" +
                    "CASE WHEN sparcsn4.inv_goods.destination='2591' THEN 'Port'\n" +
                    "WHEN sparcsn4.inv_goods.destination IS NULL THEN ''\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2592' THEN 'ICD'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2594' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2595' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2596' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2597' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2598' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2599' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2600' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2601' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2603' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2620' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2643' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2646' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '2647' THEN 'other'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '3328' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '3450' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '3697' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '3709' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '3725' THEN 'Depot'\n" +
                    "WHEN sparcsn4.inv_goods.destination = '4013' THEN 'Depot'\n" +
                    "ELSE 'Depot' END AS desti,\n" +
                    "sparcsn4.inv_unit.seal_nbr1 AS seal_nbr1,'' AS remark,\n" +
                    "\n" +
                    "(SELECT ctmsmis.cont_yard((SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                    "FROM sparcsn4.srv_event\n" +
                    "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                    "WHERE sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey AND sparcsn4.srv_event.event_type_gkey=18 LIMIT 1))) AS Yard_No,\n" +
                    "(SELECT disch_dt FROM ctmsmis.mis_disch_cont WHERE gkey=sparcsn4.inv_unit.gkey) AS time_in,\n" +
                    "sparcsn4.inv_unit_fcy_visit.time_in AS timein,\n" +
                    "(SELECT trailer FROM ctmsmis.mis_disch_cont WHERE gkey=sparcsn4.inv_unit.gkey) AS truck_id,\n" +
                    "\n" +
                    "(SELECT frmpos FROM ctmsmis.mis_disch_cont WHERE gkey=sparcsn4.inv_unit.gkey) AS frmpos,sparcsn4.inv_unit.goods_and_ctr_wt_kg AS weight\n" +
                    "\n" +
                    "FROM sparcsn4.inv_unit \n" +
                    "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                    "INNER JOIN sparcsn4.argo_carrier_visit ON argo_carrier_visit.gkey=sparcsn4.inv_unit.declrd_ib_cv\n" +
                    "INNER JOIN sparcsn4.argo_visit_details ON sparcsn4.argo_visit_details.gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
                    "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_visit_details.gkey\n" +
                    "INNER JOIN sparcsn4.inv_goods ON inv_goods.gkey=inv_unit.goods\n" +
                    "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                    "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                    "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                    "INNER JOIN  ( sparcsn4.ref_bizunit_scoped r  \n" +
                    "LEFT JOIN ( sparcsn4.ref_agent_representation X  \n" +
                    "LEFT JOIN sparcsn4.ref_bizunit_scoped Y ON X.agent_gkey=Y.gkey )               \n" +
                    "ON r.gkey=X.bzu_gkey)  ON r.gkey = sparcsn4.inv_unit.line_op \n" +
                    "WHERE inv_unit.category='IMPRT' AND sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdGkey+"' ORDER BY time_in,frmpos,sparcsn4.inv_unit.id\n" +
                    ") AS tbl WHERE mlo= '"+totMlo+"'  "+condition+" ORDER BY mlo";
            System.out.println(query);
            List<ImportContainerDischargeSummary24HourModel> detailList =secondaryDBTemplate.query(query,new ImportContainerDischargeSummary24HourService.ImportContainer24HourDetail());
            if(detailList.size()>0){
                for(int j=0;j<detailList.size();j++){
                    detailModel=detailList.get(j);
                    resultList.add(detailModel);
                }

            }


        }
        return resultList;
    }

    class CotainerDischargeSummary implements RowMapper {
        @Override
        public ImportContainerDischargeSummary24HourModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeSummary24HourModel importContainerDischargeSummary24HourModel=new ImportContainerDischargeSummary24HourModel();
            importContainerDischargeSummary24HourModel.setD_20(rs.getInt("D_20"));
            importContainerDischargeSummary24HourModel.setD_40(rs.getInt("D_40"));
            importContainerDischargeSummary24HourModel.setH_40(rs.getInt("H_40"));
            importContainerDischargeSummary24HourModel.setH_45(rs.getInt("H_45"));
            importContainerDischargeSummary24HourModel.setR_20(rs.getInt("R_20"));
            importContainerDischargeSummary24HourModel.setRH_40(rs.getInt("RH_40"));
            importContainerDischargeSummary24HourModel.setOT_20(rs.getInt("OT_20"));
            importContainerDischargeSummary24HourModel.setOT_40(rs.getInt("OT_40"));
            importContainerDischargeSummary24HourModel.setFR_20(rs.getInt("FR_20"));
            importContainerDischargeSummary24HourModel.setFR_40(rs.getInt("FR_40"));
            importContainerDischargeSummary24HourModel.setTK_20(rs.getInt("TK_20"));
            importContainerDischargeSummary24HourModel.setMD_20(rs.getInt("MD_20"));
            importContainerDischargeSummary24HourModel.setMD_40(rs.getInt("MD_40"));
            importContainerDischargeSummary24HourModel.setMH_40(rs.getInt("MH_40"));
            importContainerDischargeSummary24HourModel.setMH_45(rs.getInt("MH_45"));
            importContainerDischargeSummary24HourModel.setMR_20(rs.getInt("MR_20"));
            importContainerDischargeSummary24HourModel.setMRH_40(rs.getInt("MRH_40"));
            importContainerDischargeSummary24HourModel.setMOT_20(rs.getInt("MOT_20"));
            importContainerDischargeSummary24HourModel.setMOT_40(rs.getInt("MOT_40"));
            importContainerDischargeSummary24HourModel.setMFR_20(rs.getInt("MFR_20"));
            importContainerDischargeSummary24HourModel.setMFR_40(rs.getInt("MFR_40"));
            importContainerDischargeSummary24HourModel.setMTK_20(rs.getInt("MTK_20"));
            importContainerDischargeSummary24HourModel.setGrand_tot(rs.getInt("grand_tot"));
            importContainerDischargeSummary24HourModel.setTues(rs.getInt("tues"));
            importContainerDischargeSummary24HourModel.setWeight(rs.getInt("weight"));
            importContainerDischargeSummary24HourModel.setTime_in(rs.getString("time_in"));
            importContainerDischargeSummary24HourModel.setMlo(rs.getString("mlo"));





            return importContainerDischargeSummary24HourModel;
        }
    }
    class Mlo implements RowMapper{
        @Override
        public ImportContainerDischargeSummary24HourModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeSummary24HourModel importContainerDischargeSummary24HourModel= new ImportContainerDischargeSummary24HourModel();
            importContainerDischargeSummary24HourModel.setTotMlo(rs.getString("totMlo"));

            return importContainerDischargeSummary24HourModel;
        }
    }
    class ImportContainer24HourDetail implements RowMapper{
        @Override
        public ImportContainerDischargeSummary24HourModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeSummary24HourModel importContainerDischargeSummary24HourModel=new ImportContainerDischargeSummary24HourModel();
            importContainerDischargeSummary24HourModel.setCont_no(rs.getString("cont_no"));
            importContainerDischargeSummary24HourModel.setSize(rs.getInt("size"));
            importContainerDischargeSummary24HourModel.setHeight(rs.getFloat("height"));
            importContainerDischargeSummary24HourModel.setIso(rs.getString("iso"));
            importContainerDischargeSummary24HourModel.setIso_group(rs.getString("iso_group"));
            importContainerDischargeSummary24HourModel.setFreight_kind(rs.getString("freight_kind"));
            importContainerDischargeSummary24HourModel.setSeal_nbr1(rs.getString("seal_nbr1"));
            importContainerDischargeSummary24HourModel.setMlo(rs.getString("mlo"));
            importContainerDischargeSummary24HourModel.setFrmpos(rs.getString("frmpos"));
            importContainerDischargeSummary24HourModel.setTruck_id(rs.getString("truck_id"));
            importContainerDischargeSummary24HourModel.setDesti(rs.getString("desti"));
            importContainerDischargeSummary24HourModel.setYard_No(rs.getString("Yard_No"));
            importContainerDischargeSummary24HourModel.setWeight(rs.getInt("weight"));
            importContainerDischargeSummary24HourModel.setTime_in(rs.getString("time_in"));
            importContainerDischargeSummary24HourModel.setTimein(rs.getString("timein"));
            importContainerDischargeSummary24HourModel.setRemark(rs.getString("remark"));
            return importContainerDischargeSummary24HourModel;
        }
    }
}
