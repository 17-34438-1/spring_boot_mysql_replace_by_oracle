package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.ImportContainerBalanceOnBoardSummaryModel;
import com.datasoft.IgmMis.Model.ImportReport.ImportContainerDischargeSummaryReportTwoLast24HourModel;
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
public class ImportContainerDischargeSummaryReportTwoLast24HourService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List getVesselInfo(Integer vvdGkey){
        List<ImportContainerBalanceOnBoardSummaryModel> resultList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="SELECT vsl_vessels.name AS vsl_name,sparcsn4.vsl_vessel_visit_details.ob_vyg AS ex_Roation,\n" +
                "IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string03,'')) AS berth_op,\n" +
                "IFNULL(sparcsn4.argo_quay.id,'') AS berth,\n" +
                "ref_bizunit_scoped.id AS local_agent,\n" +
                "DATE(sparcsn4.vsl_vessel_visit_details.published_eta) AS published_eta,\n" +
                "\n" +
                "\n" +
                "CONCAT(HOUR(sparcsn4.vsl_vessel_visit_details.start_work),'',MINUTE(sparcsn4.vsl_vessel_visit_details.start_work)) AS discharge_start_time,\n" +
                "DATE(sparcsn4.vsl_vessel_visit_details.start_work) AS discharge_start,\n" +
                "CONCAT(HOUR(sparcsn4.vsl_vessel_visit_details.end_work),'',MINUTE(sparcsn4.vsl_vessel_visit_details.end_work)) AS discharge_completed_time,\n" +
                "DATE(sparcsn4.vsl_vessel_visit_details.end_work) AS discharge_completed,\n" +
                "(SELECT ata FROM sparcsn4.vsl_vessel_berthings \n" +
                "WHERE vsl_vessel_berthings.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey ORDER BY vsl_vessel_berthings.ata DESC LIMIT 1)AS ata,\n" +
                "(SELECT atd FROM sparcsn4.vsl_vessel_berthings \n" +
                "WHERE vsl_vessel_berthings.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey ORDER BY vsl_vessel_berthings.atd DESC LIMIT 1)AS atd\n" +
                "FROM vsl_vessel_visit_details\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessel_berthings ON sparcsn4.vsl_vessel_berthings.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped ON ref_bizunit_scoped.gkey=vsl_vessels.owner_gkey\n" +
                "INNER JOIN sparcsn4.argo_quay ON sparcsn4.argo_quay.gkey=sparcsn4.vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdGkey+"'";

        resultList= secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeSummaryReportTwoLast24HourService.VesselInfo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;

    }
    public List getSummary3(Integer vvdGkey,String formDate){
        List<ImportContainerBalanceOnBoardSummaryModel> resultList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="SELECT\n" +
                "IFNULL(SUM(discharge_done_LD_20),0) AS discharge_done_LD_20,\n" +
                "IFNULL(SUM(discharge_done_LD_40),0) AS discharge_done_LD_40,\n" +
                "IFNULL(SUM(discharge_done_MT_20),0) AS discharge_done_MT_20,\n" +
                "IFNULL(SUM(discharge_done_MT_40),0) AS discharge_done_MT_40,\n" +
                "IFNULL(SUM(dischage_LD_tues),0) AS dischage_LD_tues,\n" +
                "IFNULL(SUM(discharge_MT_tues),0) AS discharge_MT_tues\n" +
                "FROM \n" +
                "(\n" +
                "SELECT \n" +
                "(CASE WHEN size = 20\n" +
                "AND fcy_time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND fcy_time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS discharge_done_LD_20, \n" +
                "(CASE WHEN size !=20\n" +
                "AND fcy_time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND fcy_time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS discharge_done_LD_40,\n" +
                "(CASE WHEN size = 20\n" +
                "AND fcy_time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND fcy_time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS discharge_done_MT_20, \n" +
                "(CASE WHEN size !=20\n" +
                "AND fcy_time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND fcy_time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS discharge_done_MT_40, \n" +
                "(CASE WHEN size=20 \n" +
                "AND fcy_time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND fcy_time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN size>20 \n" +
                "AND fcy_time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND fcy_time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS dischage_LD_tues, \n" +
                "(CASE WHEN size=20 \n" +
                "AND fcy_time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND fcy_time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE WHEN size>20 \n" +
                "AND fcy_time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND fcy_time_in <CONCAT('"+formDate+"',' 08:00:01')\n" +
                "AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS discharge_MT_tues\n" +
                "FROM\n" +
                "(\n" +
                "SELECT RIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size, sparcsn4.inv_unit_fcy_visit.time_in AS  fcy_time_in,sparcsn4.inv_unit.freight_kind\n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.inv_unit_fcy_visit.actual_ib_cv=sparcsn4.argo_carrier_visit.gkey \n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey \n" +
                "WHERE  sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdGkey+"' AND category='IMPRT'\n" +
                ") AS tmp WHERE fcy_time_in IS NOT NULL\n" +
                ") AS final";

        resultList= secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeSummaryReportTwoLast24HourService.Summary3());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;

    }

    public List getSummary(Integer vvdGkey){
        List<ImportContainerBalanceOnBoardSummaryModel> resultList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="SELECT gkey,\n" +
                "IFNULL(SUM(onboard_LD_20),0) AS onboard_LD_20,\n" +
                "IFNULL(SUM(onboard_LD_40),0) AS onboard_LD_40,\n" +
                "IFNULL(SUM(onboard_MT_20),0) AS onboard_MT_20,\n" +
                "IFNULL(SUM(onboard_MT_40),0) AS onboard_MT_40,\n" +
                "IFNULL(SUM(onboard_LD_tues),0) AS onboard_LD_tues,\n" +
                "IFNULL(SUM(onboard_MT_tues),0) AS onboard_MT_tues\n" +
                "\n" +
                " FROM (\n" +
                "SELECT DISTINCT sparcsn4.inv_unit.gkey AS gkey, sparcsn4.inv_unit_fcy_visit.time_in AS fcy_time_in,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_40,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS onboard_MT_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS onboard_MT_40, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS onboard_LD_tues, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS onboard_MT_tues\n" +
                "\n" +
                " \n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.inv_unit_fcy_visit.actual_ib_cv=sparcsn4.argo_carrier_visit.gkey \n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey \n" +
                "WHERE  sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdGkey+"'  AND sparcsn4.inv_unit.category='IMPRT' \n" +
                "AND sparcsn4.inv_unit_fcy_visit.transit_state='S20_INBOUND' AND sparcsn4.inv_unit_fcy_visit.time_in IS NULL\n" +
                ") AS tmp WHERE fcy_time_in IS NULL";

        resultList= secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeSummaryReportTwoLast24HourService.Summary());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;

    }
    public List getSummary2(Integer vvdGkey){
        String sqlQuery="";
        sqlQuery="SELECT gkey,\n" +
                "IFNULL(SUM(balance_LD_20),0) AS balance_LD_20,\n" +
                "IFNULL(SUM(balance_LD_40),0) AS balance_LD_40,\n" +
                "IFNULL(SUM(balance_MT_20),0) AS balance_MT_20,\n" +
                "IFNULL(SUM(balance_MT_40),0) AS balance_MT_40,\n" +
                "IFNULL(SUM(balance_LD_tues),0) AS balance_LD_tues,\n" +
                "IFNULL(SUM(balance_MT_tues),0) AS balance_MT_tues\n" +
                "\n" +
                " FROM (\n" +
                "SELECT DISTINCT sparcsn4.inv_unit.gkey AS gkey, inv_unit_fcy_visit.time_in AS fcy_transit_state,\n" +
                "\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_20, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_40,\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_20, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_40, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE  WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS balance_LD_tues, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE  WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS balance_MT_tues\n" +
                "\n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ob_cv\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "WHERE  sparcsn4.argo_carrier_visit.cvcvd_gkey='"+vvdGkey+"' AND category='IMPRT' AND inv_unit_fcy_visit.time_in NOT IN ('S20_INBOUND')\n" +
                ") AS tmp";

        List<ImportContainerBalanceOnBoardSummaryModel> resultList= secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeSummaryReportTwoLast24HourService.Summary2());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;

    }

    public List getSummary4(Integer vvdGkey,String formDate){
        String sqlQuery="";
        sqlQuery="SELECT gkey,\n" +
                "IFNULL(SUM(discharge_done_LD_20),0) AS discharge_done_LD_20,\n" +
                "IFNULL(SUM(discharge_done_LD_40),0) AS discharge_done_LD_40,\n" +
                "IFNULL(SUM(discharge_done_MT_20),0) AS discharge_done_MT_20,\n" +
                "IFNULL(SUM(discharge_done_MT_40),0) AS discharge_done_MT_40,\n" +
                "IFNULL(SUM(dischage_LD_tues),0) AS dischage_LD_tues,\n" +
                "IFNULL(SUM(discharge_MT_tues),0) AS discharge_MT_tues\n" +
                "\n" +
                " FROM (\n" +
                "SELECT DISTINCT sparcsn4.inv_unit.gkey AS gkey,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = 20\n" +
                "AND inv_unit_fcy_visit.time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND inv_unit_fcy_visit.time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS discharge_done_LD_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) !=20\n" +
                "AND inv_unit_fcy_visit.time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND inv_unit_fcy_visit.time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND sparcsn4.inv_unit.freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS discharge_done_LD_40,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = 20\n" +
                "AND inv_unit_fcy_visit.time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND inv_unit_fcy_visit.time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND sparcsn4.inv_unit.freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS discharge_done_MT_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) !=20\n" +
                "AND inv_unit_fcy_visit.time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND inv_unit_fcy_visit.time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND sparcsn4.inv_unit.freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS discharge_done_MT_40, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 \n" +
                "AND inv_unit_fcy_visit.time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND inv_unit_fcy_visit.time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 \n" +
                "AND inv_unit_fcy_visit.time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND inv_unit_fcy_visit.time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS dischage_LD_tues, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 \n" +
                "AND inv_unit_fcy_visit.time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND inv_unit_fcy_visit.time_in <CONCAT('"+formDate+"',' 08:00:01') \n" +
                "AND sparcsn4.inv_unit.freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 \n" +
                "AND inv_unit_fcy_visit.time_in >CONCAT(DATE_ADD('"+formDate+"', INTERVAL -1 DAY),' 08:00:00')\n" +
                "AND inv_unit_fcy_visit.time_in <CONCAT('"+formDate+"',' 08:00:01')\n" +
                "AND sparcsn4.inv_unit.freight_kind='MTY' THEN 2 ELSE NULL END) END) AS discharge_MT_tues\n" +
                "\n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ob_cv\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "WHERE  sparcsn4.argo_carrier_visit.cvcvd_gkey='20076565' AND category='EXPRT' AND inv_unit_fcy_visit.time_in IS NOT NULL\n" +
                "\n" +
                ") AS tmp";

        List<ImportContainerBalanceOnBoardSummaryModel> resultList= secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeSummaryReportTwoLast24HourService.Summary4());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;

    }
    public List getSummary5(Integer vvdGkey){
        List<ImportContainerBalanceOnBoardSummaryModel> resultList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="SELECT gkey,\n" +
                "IFNULL(SUM(balance_LD_20),0) AS balance_LD_20,\n" +
                "IFNULL(SUM(balance_LD_40),0) AS balance_LD_40,\n" +
                "IFNULL(SUM(balance_MT_20),0) AS balance_MT_20,\n" +
                "IFNULL(SUM(balance_MT_40),0) AS balance_MT_40,\n" +
                "IFNULL(SUM(balance_LD_tues),0) AS balance_LD_tues,\n" +
                "IFNULL(SUM(balance_MT_tues),0) AS balance_MT_tues\n" +
                "\n" +
                " FROM (\n" +
                "SELECT DISTINCT sparcsn4.inv_unit.gkey AS gkey, inv_unit_fcy_visit.time_in AS fcy_transit_state,\n" +
                "\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_20, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_40,\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_20, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_40, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE  WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS balance_LD_tues, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE  WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS balance_MT_tues\n" +
                "\n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ob_cv\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "WHERE  sparcsn4.argo_carrier_visit.cvcvd_gkey='"+vvdGkey+"' AND category='EXPRT' AND inv_unit_fcy_visit.time_in NOT IN ('S20_INBOUND')\n" +
                ") AS tmp";

        resultList= secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeSummaryReportTwoLast24HourService.Summary5());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;

    }
    public List getSummary6(Integer vvdGkey){
        List<ImportContainerBalanceOnBoardSummaryModel> resultList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="SELECT gkey,\n" +
                "IFNULL(SUM(onboard_LD_20),0) AS onboard_LD_20,\n" +
                "IFNULL(SUM(onboard_LD_40),0) AS onboard_LD_40,\n" +
                "IFNULL(SUM(onboard_MT_20),0) AS onboard_MT_20,\n" +
                "IFNULL(SUM(onboard_MT_40),0) AS onboard_MT_40,\n" +
                "IFNULL(SUM(onboard_LD_tues),0) AS onboard_LD_tues,\n" +
                "IFNULL(SUM(onboard_MT_tues),0) AS onboard_MT_tues\n" +
                "\n" +
                " FROM (\n" +
                "SELECT DISTINCT sparcsn4.inv_unit.gkey AS gkey, sparcsn4.inv_unit_fcy_visit.time_in AS fcy_time_in,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_40,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS onboard_MT_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS onboard_MT_40, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS onboard_LD_tues, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS onboard_MT_tues\n" +
                "\n" +
                " \n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.inv_unit_fcy_visit.actual_ib_cv=sparcsn4.argo_carrier_visit.gkey \n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey \n" +
                "WHERE  sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdGkey+"'  AND sparcsn4.inv_unit.category='EXPRT' \n" +
                "AND sparcsn4.inv_unit_fcy_visit.transit_state='S20_INBOUND' AND sparcsn4.inv_unit_fcy_visit.time_in IS NULL\n" +
                ") AS tmp WHERE fcy_time_in IS NULL";

        resultList= secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeSummaryReportTwoLast24HourService.Summary6());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;

    }



    class  VesselInfo implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeSummaryReportTwoLast24HourModel importContainerDischargeSummaryReportTwoLast24HourModel=new ImportContainerDischargeSummaryReportTwoLast24HourModel();
            importContainerDischargeSummaryReportTwoLast24HourModel.setVsl_name(rs.getString("vsl_name"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setRotation(rs.getString("ex_Roation"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBerth_op(rs.getString("berth_op"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBerth(rs.getString("berth"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setLocal_agent(rs.getString("local_agent"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setPublished_eta(rs.getString("published_eta"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_start_time(rs.getInt("discharge_start_time"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_start(rs.getString("discharge_start"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_completed_time(rs.getInt("discharge_completed_time"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_completed(rs.getString("discharge_completed"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setAta(rs.getString("ata"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setAtd(rs.getString("atd"));

            return importContainerDischargeSummaryReportTwoLast24HourModel;
        }
    }
    class Summary3 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeSummaryReportTwoLast24HourModel importContainerDischargeSummaryReportTwoLast24HourModel=new ImportContainerDischargeSummaryReportTwoLast24HourModel();
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_done_LD_20(rs.getInt("discharge_done_LD_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_done_LD_40(rs.getInt("discharge_done_LD_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_done_MT_20(rs.getInt("discharge_done_MT_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_done_MT_40(rs.getInt("discharge_done_MT_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischage_LD_tues(rs.getInt("dischage_LD_tues"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_MT_tues(rs.getInt("discharge_MT_tues"));
            return importContainerDischargeSummaryReportTwoLast24HourModel;
        }
    }
    class Summary implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeSummaryReportTwoLast24HourModel importContainerDischargeSummaryReportTwoLast24HourModel=new ImportContainerDischargeSummaryReportTwoLast24HourModel();
            importContainerDischargeSummaryReportTwoLast24HourModel.setGkey(rs.getString("gkey"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_LD_20(rs.getInt("onboard_LD_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_LD_40(rs.getInt("onboard_LD_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_MT_20(rs.getInt("onboard_MT_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_MT_40(rs.getInt("onboard_MT_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_LD_tues(rs.getInt("onboard_LD_tues"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_MT_tues(rs.getInt("onboard_MT_tues"));
            return importContainerDischargeSummaryReportTwoLast24HourModel;
        }
    }
    class Summary2 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeSummaryReportTwoLast24HourModel importContainerDischargeSummaryReportTwoLast24HourModel=new ImportContainerDischargeSummaryReportTwoLast24HourModel();
            importContainerDischargeSummaryReportTwoLast24HourModel.setGkey(rs.getString("gkey"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_LD_20(rs.getInt("balance_LD_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_LD_40(rs.getInt("balance_LD_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_MT_20(rs.getInt("balance_MT_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_MT_40(rs.getInt("balance_MT_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_LD_tues(rs.getInt("balance_LD_tues"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_MT_tues(rs.getInt("balance_MT_tues"));
            return importContainerDischargeSummaryReportTwoLast24HourModel;
        }
    }

    class Summary4 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeSummaryReportTwoLast24HourModel importContainerDischargeSummaryReportTwoLast24HourModel=new ImportContainerDischargeSummaryReportTwoLast24HourModel();
            importContainerDischargeSummaryReportTwoLast24HourModel.setGkey(rs.getString("gkey"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_done_LD_20(rs.getInt("discharge_done_LD_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_done_LD_40(rs.getInt("discharge_done_LD_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_done_MT_20(rs.getInt("discharge_done_MT_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_done_MT_40(rs.getInt("discharge_done_MT_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischage_LD_tues(rs.getInt("dischage_LD_tues"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setDischarge_MT_tues(rs.getInt("discharge_MT_tues"));
            return importContainerDischargeSummaryReportTwoLast24HourModel;
        }
    }
    class Summary5 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeSummaryReportTwoLast24HourModel importContainerDischargeSummaryReportTwoLast24HourModel=new ImportContainerDischargeSummaryReportTwoLast24HourModel();
            importContainerDischargeSummaryReportTwoLast24HourModel.setGkey(rs.getString("gkey"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_LD_20(rs.getInt("balance_LD_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_LD_40(rs.getInt("balance_LD_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_MT_20(rs.getInt("balance_MT_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_MT_40(rs.getInt("balance_MT_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_LD_tues(rs.getInt("balance_LD_tues"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setBalance_MT_tues(rs.getInt("balance_MT_tues"));
            return importContainerDischargeSummaryReportTwoLast24HourModel;
        }
    }
    class Summary6 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeSummaryReportTwoLast24HourModel importContainerDischargeSummaryReportTwoLast24HourModel=new ImportContainerDischargeSummaryReportTwoLast24HourModel();
            importContainerDischargeSummaryReportTwoLast24HourModel.setGkey(rs.getString("gkey"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_LD_20(rs.getInt("onboard_LD_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_LD_40(rs.getInt("onboard_LD_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_MT_20(rs.getInt("onboard_MT_20"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_MT_40(rs.getInt("onboard_MT_40"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_LD_tues(rs.getInt("onboard_LD_tues"));
            importContainerDischargeSummaryReportTwoLast24HourModel.setOnboard_MT_tues(rs.getInt("onboard_MT_tues"));
            return importContainerDischargeSummaryReportTwoLast24HourModel;
        }
    }
}
