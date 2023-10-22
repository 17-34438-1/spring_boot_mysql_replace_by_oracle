package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportContainerNotFoundReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportContainerNotFoundReportService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;

    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;

    public Integer getExportContainerNotFoundvvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportContainerNotFoundReport> resultList=OracleDbTemplate.query(sqlQuery,new ExportContainerVvdGkey());

        ExportContainerNotFoundReport exportContainerNotFoundReport;
        for(int i=0;i<resultList.size();i++){
            exportContainerNotFoundReport=resultList.get(i);
            vvdgkey=exportContainerNotFoundReport.getVvd_gkey();
        }
        return vvdgkey;
    }


    class ExportContainerVvdGkey implements RowMapper{

        @Override
        public ExportContainerNotFoundReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerNotFoundReport exportContainerNotFoundReport=new ExportContainerNotFoundReport();
            exportContainerNotFoundReport.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportContainerNotFoundReport;
        }
    }


    public List getVessleInformation(Integer vvdgkey){
        String sqlQuery="";


//        sqlQuery="SELECT vsl_vessels.name AS vsl_name,IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,\n" +
//                "IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string03,'')) AS berth_op,IFNULL(sparcsn4.argo_quay.id,'') AS berth,\n" +
//                "DATE(sparcsn4.vsl_vessel_visit_details.published_eta) AS ata,\n" +
//                "sparcsn4.argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
//                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
//                "INNER JOIN sparcsn4.vsl_vessel_berthings ON sparcsn4.vsl_vessel_berthings.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
//                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
//                "INNER JOIN sparcsn4.argo_quay ON sparcsn4.argo_quay.gkey=sparcsn4.vsl_vessel_berthings.quay\n" +
//                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";

        sqlQuery="SELECT vsl_vessels.name as vsl_name,COALESCE(vsl_vessel_visit_details.flex_string02,vsl_vessel_visit_details.flex_string03) AS berth_op,COALESCE(argo_quay.id,'') AS berth,\n" +
                "argo_carrier_visit.ata,argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessel_berthings ON vsl_vessel_berthings.vvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN argo_quay ON argo_quay.gkey=vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=OracleDbTemplate.query(sqlQuery,new ExportContainerNotFoundVesselInformation());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public  List getContainerNotFoundReport(Integer vvdgkey,String fromDate,String toDate){

        String sqlQuery="";

//        sqlQuery="\n" +
//                "SELECT * FROM (\n" +
//                "SELECT sparcsn4.inv_unit.id AS id,\n" +
//                "sparcsn4.inv_unit_fcy_visit.time_in AS fcy_time_in,\n" +
//                "inv_unit_fcy_visit.transit_state AS fcy_transit_state,\n" +
//                "(SELECT sparcsn4.vsl_vessel_visit_details.ib_vyg FROM sparcsn4.vsl_vessel_visit_details WHERE sparcsn4.vsl_vessel_visit_details.vvd_gkey=mis_exp_unit_load_failed.vvd_gkey) AS rot, \n" +
//                "sparcsn4.ref_equip_type.id AS iso,sparcsn4.ref_bizunit_scoped.id AS mlo, \n" +
//                "(SELECT ctmsmis.cont_yard(IF(sparcsn4.inv_unit_fcy_visit.last_pos_slot='', (SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7) FROM sparcsn4.srv_event INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey WHERE sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey AND sparcsn4.srv_event.event_type_gkey IN(18) AND sparcsn4.srv_event_field_changes.new_value IS NOT NULL AND sparcsn4.srv_event_field_changes.new_value !='' AND sparcsn4.srv_event_field_changes.new_value !='Y-CGP-.' AND sparcsn4.srv_event.gkey<(SELECT sparcsn4.srv_event.gkey FROM sparcsn4.srv_event INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey WHERE sparcsn4.srv_event.event_type_gkey IN(25,29,30) AND sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1) ORDER BY sparcsn4.srv_event.gkey DESC LIMIT 1) ,sparcsn4.inv_unit_fcy_visit.last_pos_slot))) AS yard,\n" +
//                "\n" +
//                "IF(sparcsn4.inv_unit_fcy_visit.last_pos_slot='', (SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7) FROM sparcsn4.srv_event INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey WHERE sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey AND sparcsn4.srv_event.event_type_gkey IN(18) AND sparcsn4.srv_event_field_changes.new_value IS NOT NULL AND sparcsn4.srv_event_field_changes.new_value !='' AND sparcsn4.srv_event_field_changes.new_value !='Y-CGP-.' AND sparcsn4.srv_event.gkey<(SELECT sparcsn4.srv_event.gkey FROM sparcsn4.srv_event INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey WHERE sparcsn4.srv_event.event_type_gkey IN(25,29,30) AND sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1) ORDER BY sparcsn4.srv_event.gkey DESC LIMIT 1) ,sparcsn4.inv_unit_fcy_visit.last_pos_slot) AS last_pos_slot, \n" +
//                "\n" +
//                "inv_unit.freight_kind,\n" +
//                "ctmsmis.mis_exp_unit_load_failed.goods_and_ctr_wt_kg AS weight,\n" +
//                "ctmsmis.mis_exp_unit_load_failed.pod,ctmsmis.mis_exp_unit_load_failed.stowage_pos,ref_commodity.short_name, sparcsn4.inv_unit_fcy_visit.flex_date01 AS assignmentdate, ctmsmis.mis_exp_unit_load_failed.user_id,mis_exp_unit_load_failed.vvd_gkey,\n" +
//                "sparcsn4.srv_event.placed_time\n" +
//                "FROM sparcsn4.inv_unit \n" +
//                "LEFT JOIN ctmsmis.mis_exp_unit_load_failed ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit_load_failed.gkey\n" +
//                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
//                "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.inv_unit.line_op\n" +
//                "INNER JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
//                "INNER JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey \n" +
//                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey \n" +
//                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
//                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
//                "INNER JOIN sparcsn4.srv_event ON sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey\n" +
//                "WHERE sparcsn4.srv_event.placed_time BETWEEN CONCAT('"+fromDate+" 00:00:00') AND CONCAT('"+toDate+" 01:00:00')\n" +
//                ") AS tmp  WHERE placed_time BETWEEN CONCAT('"+fromDate+" 00:00:00') AND CONCAT('"+toDate+" 23:59:59')\n" +
//                "OR vvd_gkey='"+vvdgkey+"'\n";



//        sqlQuery="SELECT inv_unit.gkey,inv_unit.id,vsl_vessel_visit_details.ib_vyg,g.id AS mlo,ref_equip_type.id AS iso,inv_unit.freight_kind,\n" +
//                "inv_unit.goods_and_ctr_wt_kg as weight ,REF_ROUTING_POINT.ID as pod, inv_unit_fcy_visit.LAST_POS_LOCTYPE AS coming_from ,inv_unit.seal_nbr1 AS sealno,\n" +
//                "vsl_vessels.name,ref_commodity.short_name,\n" +
//                "(select ref_bizunit_scoped.id from ref_bizunit_scoped\n" +
//                "inner join road_trucks on road_trucks.trkco_gkey=ref_bizunit_scoped.gkey\n" +
//                "inner join ROAD_TRUCK_VISIT_DETAILS on ROAD_TRUCK_VISIT_DETAILS.truck_gkey=road_trucks.gkey\n" +
//                "inner join road_truck_transactions on road_truck_transactions.truck_visit_gkey=road_truck_visit_details.tvdtls_gkey\n" +
//                "where road_truck_transactions.unit_gkey=inv_unit.gkey fetch first 1 rows only) AS coming_frm,\n" +
//                "(select srv_event.placed_time from srv_event where srv_event.applied_to_gkey=inv_unit.gkey and event_type_gkey IN(31488) \n" +
//                "ORDER BY srv_event.gkey DESC fetch first 1 rows only) as pre_advised_dt, inv_unit_fcy_visit.flex_date01 AS assignmentdate , inv_unit_fcy_visit.time_in AS fcy_time_in,srv_event.placed_time\n" +
//                "FROM  inv_unit\n" +
//                "inner join inv_unit_fcy_visit on inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
//                "INNER JOIN ( ref_bizunit_scoped g LEFT JOIN ( ref_agent_representation X LEFT JOIN ref_bizunit_scoped Y ON X.agent_gkey=Y.gkey ) ON g.gkey=X.bzu_gkey ) ON g.gkey = inv_unit.line_op\n" +
//                "inner join argo_carrier_visit on argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ob_cv\n" +
//                "inner join vsl_vessel_visit_details on vsl_vessel_visit_details.vvd_gkey=argo_carrier_visit.cvcvd_gkey\n" +
//                "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey \n" +
//                "INNER JOIN inv_goods ON inv_goods.gkey=inv_unit.goods\n" +
//                "INNER JOIN ref_commodity ON ref_commodity.gkey=inv_goods.commodity_gkey \n" +
//                "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
//                "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
//                "INNER JOIN ref_bizunit_scoped  ON inv_unit.line_op = ref_bizunit_scoped.gkey \n" +
//                "INNER JOIN REF_ROUTING_POINT ON INV_UNIT.POD1_GKEY = REF_ROUTING_POINT.GKEY\n" +
//                "INNER JOIN srv_event ON srv_event.applied_to_gkey=inv_unit.gkey\n" +
//                "WHERE to_char(srv_event.placed_time,'YYYY-MM-DD HH24-MI-SS')  BETWEEN '"+fromDate+"' and '"+toDate+"'";


//                sqlQuery="SELECT inv_unit.gkey,inv_unit.id,vsl_vessel_visit_details.ib_vyg,g.id AS mlo,ref_equip_type.id AS iso,inv_unit.freight_kind,\n" +
//                        "inv_unit.goods_and_ctr_wt_kg as weight ,REF_ROUTING_POINT.ID as pod, inv_unit_fcy_visit.LAST_POS_LOCTYPE AS coming_from ,inv_unit.seal_nbr1 AS sealno,\n" +
//                        "vsl_vessels.name,ref_commodity.short_name,inv_unit_fcy_visit.ARRIVE_POS_SLOT as stowage_pos,inv_unit_fcy_visit.last_pos_slot ,vsl_vessel_visit_details.vvd_gkey,\n" +
//                        "(select ref_bizunit_scoped.id from ref_bizunit_scoped\n" +
//                        "inner join road_trucks on road_trucks.trkco_gkey=ref_bizunit_scoped.gkey\n" +
//                        "inner join ROAD_TRUCK_VISIT_DETAILS on ROAD_TRUCK_VISIT_DETAILS.truck_gkey=road_trucks.gkey\n" +
//                        "inner join road_truck_transactions on road_truck_transactions.truck_visit_gkey=road_truck_visit_details.tvdtls_gkey\n" +
//                        "where road_truck_transactions.unit_gkey=inv_unit.gkey fetch first 1 rows only) AS coming_from,\n" +
//                        "(select srv_event.placed_time from srv_event where srv_event.applied_to_gkey=inv_unit.gkey and event_type_gkey IN(31488) \n" +
//                        "ORDER BY srv_event.gkey DESC fetch first 1 rows only) as pre_advised_dt, inv_unit_fcy_visit.flex_date01 AS assignmentdate , inv_unit_fcy_visit.time_in AS fcy_time_in,srv_event.placed_time\n" +
//                        "FROM  inv_unit\n" +
//                        "inner join inv_unit_fcy_visit on inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
//                        "INNER JOIN ( ref_bizunit_scoped g LEFT JOIN ( ref_agent_representation X LEFT JOIN ref_bizunit_scoped Y ON X.agent_gkey=Y.gkey ) ON g.gkey=X.bzu_gkey ) ON g.gkey = inv_unit.line_op\n" +
//                        "inner join argo_carrier_visit on argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ob_cv\n" +
//                        "inner join vsl_vessel_visit_details on vsl_vessel_visit_details.vvd_gkey=argo_carrier_visit.cvcvd_gkey\n" +
//                        "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey \n" +
//                        "INNER JOIN inv_goods ON inv_goods.gkey=inv_unit.goods\n" +
//                        "INNER JOIN ref_commodity ON ref_commodity.gkey=inv_goods.commodity_gkey \n" +
//                        "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
//                        "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
//                        "INNER JOIN ref_bizunit_scoped  ON inv_unit.line_op = ref_bizunit_scoped.gkey \n" +
//                        "INNER JOIN REF_ROUTING_POINT ON INV_UNIT.POD1_GKEY = REF_ROUTING_POINT.GKEY\n" +
//                        "INNER JOIN srv_event ON srv_event.applied_to_gkey=inv_unit.gkey\n" +
//                        "WHERE to_char(srv_event.placed_time,'YYYY-MM-DD HH24-MI-SS')  BETWEEN '"+fromDate+"' and '"+toDate+"' and vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";

        sqlQuery="select * from(\n" +
                "SELECT inv_unit.id AS id,\n" +
                "inv_unit_fcy_visit.time_in AS fcy_time_in,\n" +
                "inv_unit_fcy_visit.transit_state AS fcy_transit_state,\n" +
                "inv_unit_fcy_visit.ARRIVE_POS_SLOT as stowage_pos,inv_unit_fcy_visit.last_pos_slot, inv_unit_fcy_visit.LAST_POS_LOCTYPE AS coming_from ,\n" +
                "ref_equip_type.id AS iso,ref_bizunit_scoped.id AS mlo, REF_ROUTING_POINT.ID as pod,vsl_vessel_visit_details.vvd_gkey,vsl_vessel_visit_details.ib_vyg as rot,\n" +
                "inv_unit.freight_kind,\n" +
                "inv_unit.goods_and_ctr_wt_kg as weight,\n" +
                "srv_event.placed_time,\n" +
                "inv_unit_fcy_visit.flex_date01 AS assignmentdate ,\n" +
                "ref_commodity.short_name\n" +
                "FROM inv_unit \n" +
                "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ib_cv\n" +
                "INNER JOIN vsl_vessel_visit_details ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey \n" +
                "INNER JOIN ref_bizunit_scoped ON ref_bizunit_scoped.gkey=inv_unit.line_op\n" +
                "INNER JOIN inv_goods ON inv_goods.gkey=inv_unit.goods\n" +
                "INNER JOIN ref_commodity ON ref_commodity.gkey=inv_goods.commodity_gkey \n" +
                "INNER JOIN ref_equipment ON ref_equipment.gkey=INV_UNIT.eq_gkey\n" +
                "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN REF_ROUTING_POINT ON REF_ROUTING_POINT.GKEY=INV_UNIT.POD1_GKEY\n" +
                "INNER JOIN srv_event ON srv_event.applied_to_gkey=inv_unit.gkey\n" +
                "WHERE to_char(srv_event.placed_time,'YYYY-MM-DD')   between '"+fromDate+"' and '"+toDate+"' or vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'\n" +
                ")  tbl WHERE fcy_transit_state NOT IN ('S60_LOADED','S70_DEPARTED') ";

        List resultList=OracleDbTemplate.query(sqlQuery,new ExportContainerReportList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class  ExportContainerReportList implements RowMapper{
        @Override
        public ExportContainerNotFoundReport mapRow(ResultSet rs, int rowNum)throws SQLException{
            ExportContainerNotFoundReport exportContainerNotFoundReport=new ExportContainerNotFoundReport();
            exportContainerNotFoundReport.setId(rs.getString("id"));
            exportContainerNotFoundReport.setMlo(rs.getString("mlo"));
            exportContainerNotFoundReport.setIso(rs.getString("iso"));
            exportContainerNotFoundReport.setRot(rs.getString("rot"));
            exportContainerNotFoundReport.setFreight_kind(rs.getString("freight_kind"));
            exportContainerNotFoundReport.setWeight(rs.getString("weight"));
            //exportContainerNotFoundReport.setYard(rs.getString("yard"));
            exportContainerNotFoundReport.setLast_pos_slot(rs.getString("last_pos_slot"));
            exportContainerNotFoundReport.setFcy_time_in(rs.getTimestamp("fcy_time_in"));
            exportContainerNotFoundReport.setAssignmentdate(rs.getTimestamp("assignmentdate"));
            exportContainerNotFoundReport.setComing_from(rs.getString("coming_from"));
            exportContainerNotFoundReport.setPod(rs.getString("pod"));
            exportContainerNotFoundReport.setStowage_pos(rs.getString("stowage_pos"));
            //exportContainerNotFoundReport.setUser_id(rs.getString("user_id"));
            exportContainerNotFoundReport.setShort_name(rs.getString("short_name"));
            return exportContainerNotFoundReport;
        }
    }




    class ExportContainerNotFoundVesselInformation implements RowMapper {

        @Override
        public ExportContainerNotFoundReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerNotFoundReport exportContainerNotFoundReport=new ExportContainerNotFoundReport();
            exportContainerNotFoundReport.setVsl_name(rs.getString("vsl_name"));
            exportContainerNotFoundReport.setVoysNo(rs.getString("berth_op"));
            exportContainerNotFoundReport.setDdl_imp_rot_no(rs.getString("berth"));
            exportContainerNotFoundReport.setAta(rs.getTimestamp("ata"));
            return exportContainerNotFoundReport;
        }
    }




}
