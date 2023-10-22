package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportLoadedContainerList;
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
public class ExportLoadedContainerListService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;

    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;


    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;


    public Integer get_vvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportLoadedContainerList> resultList=OracleDbTemplate.query(sqlQuery,new ExportLoadedVvdGkey());

        ExportLoadedContainerList exportLoadedContainerList;
        for(int i=0;i<resultList.size();i++){
            exportLoadedContainerList=resultList.get(i);
            vvdgkey=exportLoadedContainerList.getVvd_gkey();
        }
        return vvdgkey;
    }

    public List getVoyNo(String rotation){
        String sqlQuery="";
        String voyNo="";
        sqlQuery="SELECT Voy_No FROM igm_masters WHERE Import_Rotation_No='"+rotation+"'";
        System.out.println(sqlQuery);
        List resultList=primaryDBTemplate.query(sqlQuery,new ExportLoadedVoyNo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());

        return listAll;
    }

    class ExportLoadedVoyNo implements RowMapper {

        @Override
        public ExportLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportLoadedContainerList exportLoadedContainerList =new ExportLoadedContainerList();
            exportLoadedContainerList.setVoy_No(rs.getString("Voy_No"));

            return exportLoadedContainerList;
        }
    }

    class ExportLoadedVvdGkey implements RowMapper {

        @Override
        public ExportLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportLoadedContainerList exportLoadedContainerList=new ExportLoadedContainerList();
            exportLoadedContainerList.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportLoadedContainerList;
        }
    }



    public List getLoadedContainerList(Integer vvdgkey,String fromdate,String todate,String fromTime,String toTime){


        String sqlQuery="";
        String cond="";
        String frmDate="";
        String tDate="";
        if(fromdate!="" && todate!="")
        {
            if(fromTime=="" || toTime==""){
                cond = " and to_char(inv_unit_fcy_visit.time_load,'yyyy-mm-dd') between '"+fromdate+"' and '"+todate+"'";
            }

            else
            {
                frmDate = fromdate+" "+fromTime+":00";
                tDate = todate+" "+toTime+":00";
                cond = " and to_char(inv_unit_fcy_visit.time_load,'yyyy-mm-dd') between '"+frmDate+"' and '"+tDate+"'";
            }
        }

//
//        sqlQuery="SELECT ctmsmis.mis_exp_unit.gkey,CONCAT(SUBSTRING(ctmsmis.mis_exp_unit.cont_id,1,4),' ',SUBSTRING(ctmsmis.mis_exp_unit.cont_id,5)) AS id,mis_exp_unit.isoType AS iso,\n" +
//                "(CASE \n" +
//                "WHEN mis_exp_unit.cont_size= '20' AND mis_exp_unit.cont_height = '86' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '2D'\n" +
//                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '4D'\n" +
//                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.cont_height='96' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '4H'\n" +
//                "WHEN mis_exp_unit.cont_size = '45' AND mis_exp_unit.cont_height='96' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '45H'\n" +
//                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('RS','RT','RE') THEN '2RF'\n" +
//                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.isoGroup IN ('RS','RT','RE') THEN '4RH'\n" +
//                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('UT') THEN '2OT'\n" +
//                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.isoGroup IN ('UT') THEN '4OT'\n" +
//                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('PF','PC') THEN '2FR'\n" +
//                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.isoGroup IN ('PF','PC') THEN '4FR'\n" +
//                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('TN','TD','TG') THEN '2TK'\n" +
//                "ELSE ''\n" +
//                "END\n" +
//                ") AS TYPE,\n" +
//                "mis_exp_unit.cont_mlo AS mlo,\n" +
//                "cont_status AS freight_kind,ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg AS weight,ctmsmis.mis_exp_unit.coming_from AS coming_from,\n" +
//                "ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos,ctmsmis.mis_exp_unit.last_update,ref_commodity.short_name,ctmsmis.mis_exp_unit.user_id\n" +
//                "FROM ctmsmis.mis_exp_unit \n" +
//                "LEFT JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
//                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
//                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
//                "where mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' and mis_exp_unit.delete_flag='0' and snx_type=0 "+cond+" ";


sqlQuery="SELECT inv_unit.gkey,inv_unit.id AS cont_id,ref_equip_type.id AS iso,\n" +
        "(CASE WHEN substr(ref_equip_type.nominal_length,-2)= '20' AND substr(ref_equip_type.nominal_height,-2) = '86' \n" +
        "AND ref_equip_type.id NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '2D' WHEN substr(ref_equip_type.nominal_length,-2) = '40'\n" +
        "AND substr(ref_equip_type.nominal_height,-2)='86' AND ref_equip_type.id NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '4D'\n" +
        "WHEN substr(ref_equip_type.nominal_length,-2) = '40' AND substr(ref_equip_type.nominal_height,-2)='96'\n" +
        "AND ref_equip_type.id NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '4H' WHEN substr(ref_equip_type.nominal_length,-2) = '45'\n" +
        "AND substr(ref_equip_type.nominal_height,-2)='96' AND ref_equip_type.id NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '45H' \n" +
        "WHEN substr(ref_equip_type.nominal_length,-2) = '20' AND substr(ref_equip_type.nominal_height,-2)='86' \n" +
        "AND ref_equip_type.id IN ('RS','RT','RE') THEN '2RF' WHEN substr(ref_equip_type.nominal_length,-2) = '40' \n" +
        "AND ref_equip_type.id IN ('RS','RT','RE') THEN '4RH' WHEN substr(ref_equip_type.nominal_length,-2) = '20'\n" +
        "AND substr(ref_equip_type.nominal_height,-2)='86' AND ref_equip_type.id IN ('UT') THEN '2OT'\n" +
        "WHEN substr(ref_equip_type.nominal_length,-2) = '40' AND ref_equip_type.id IN ('UT') THEN '4OT' \n" +
        "WHEN substr(ref_equip_type.nominal_length,-2) = '20' AND substr(ref_equip_type.nominal_height,-2)='86' AND ref_equip_type.id IN ('PF','PC') THEN '2FR' \n" +
        "WHEN substr(ref_equip_type.nominal_length,-2) = '40' AND ref_equip_type.id IN ('PF','PC') THEN '4FR'\n" +
        "WHEN substr(ref_equip_type.nominal_length,-2) = '20' AND substr(ref_equip_type.nominal_height,-2)='86'\n" +
        "AND ref_equip_type.id IN ('TN','TD','TG') THEN '2TK' ELSE '' END ) AS TYPE, vsl_vessel_visit_details.ib_vyg,g.id AS mlo,\n" +
        "inv_unit_fcy_visit.ARRIVE_POS_SLOT as stowage_pos, inv_unit.freight_kind,inv_unit.goods_and_ctr_wt_kg AS weight,\n" +
        "REF_ROUTING_POINT.ID as pod,ref_commodity.short_name, (select ref_bizunit_scoped.id from ref_bizunit_scoped\n" +
        "inner join road_trucks on road_trucks.trkco_gkey=ref_bizunit_scoped.gkey\n" +
        "inner join ROAD_TRUCK_VISIT_DETAILS on ROAD_TRUCK_VISIT_DETAILS.truck_gkey=road_trucks.gkey \n" +
        "inner join road_truck_transactions on road_truck_transactions.truck_visit_gkey=road_truck_visit_details.tvdtls_gkey\n" +
        "where road_truck_transactions.unit_gkey=inv_unit.gkey fetch first 1 rows only) AS coming_from, vsl_vessel_visit_details.vvd_gkey, \n" +
        "(select srv_event.placed_time from srv_event where srv_event.applied_to_gkey=inv_unit.gkey and event_type_gkey IN(31488)\n" +
        "ORDER BY srv_event.gkey DESC fetch first 1 rows only) as last_update\n" +
        "FROM inv_unit\n" +
        "inner join inv_unit_fcy_visit on inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
        "INNER JOIN ( ref_bizunit_scoped g LEFT JOIN ( ref_agent_representation X \n" +
        "LEFT JOIN ref_bizunit_scoped Y ON X.agent_gkey=Y.gkey ) ON g.gkey=X.bzu_gkey ) ON g.gkey = inv_unit.line_op \n" +
        "inner join argo_carrier_visit on argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ob_cv \n" +
        "inner join vsl_vessel_visit_details on vsl_vessel_visit_details.vvd_gkey=argo_carrier_visit.cvcvd_gkey \n" +
        "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
        "LEFT JOIN inv_goods ON inv_goods.gkey=inv_unit.goods\n" +
        "LEFT JOIN ref_commodity ON ref_commodity.gkey=inv_goods.commodity_gkey\n" +
        "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey \n" +
        "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey \n" +
        "INNER JOIN ref_bizunit_scoped ON inv_unit.line_op = ref_bizunit_scoped.gkey\n" +
        "INNER JOIN REF_ROUTING_POINT ON INV_UNIT.POD1_GKEY = REF_ROUTING_POINT.GKEY\n" +
        "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";

        List resultList=OracleDbTemplate.query(sqlQuery,new ExportReportLoadedContainerList());
        System.out.println("Sql:"+sqlQuery);

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public List getLoadedContainerSummaryReport(Integer vvdgkey,String fromdate,String todate,String fromTime,String toTime){
        String sqlQuery="";
        String cond="";
        String frmDate="";
        String tDate="";
        if(fromdate!="" && todate!="")
        {
            if(fromTime=="" || toTime=="")
            {
                cond = " and to_char(inv_unit_fcy_visit.time_load,'yyyy-mm-dd') between '"+fromdate+"' and '"+todate+"'";
            }
            else
            {
                frmDate = fromdate+" "+fromTime+":00";
                tDate = todate+" "+toTime+":00";
                cond = " and to_char(inv_unit_fcy_visit.time_load,'yyyy-mm-dd') between '"+frmDate+"' and '"+tDate+"'";
            }
        }
//        sqlQuery="select gkey,\n" +
//                "IFNULL(SUM(onboard_LD_20),0) AS onboard_LD_20,\n" +
//                "IFNULL(SUM(onboard_LD_40),0) AS onboard_LD_40,\n" +
//                "IFNULL(SUM(onboard_MT_20),0) AS onboard_MT_20,\n" +
//                "IFNULL(SUM(onboard_MT_40),0) AS onboard_MT_40,\n" +
//                "IFNULL(SUM(onboard_LD_tues),0) AS onboard_LD_tues,\n" +
//                "IFNULL(SUM(onboard_MT_tues),0) AS onboard_MT_tues\n" +
//                "\n" +
//                " from (\n" +
//                "select distinct ctmsmis.mis_exp_unit.gkey as gkey,\n" +
//                "(CASE WHEN mis_exp_unit.cont_size = '20' AND freight_kind in ('FCL','LCL')  THEN 1  \n" +
//                "ELSE NULL END) AS onboard_LD_20, \n" +
//                "(CASE WHEN mis_exp_unit.cont_size > '20' AND freight_kind in ('FCL','LCL')  THEN 1  \n" +
//                "ELSE NULL END) AS onboard_LD_40,\n" +
//                "(CASE WHEN mis_exp_unit.cont_size = '20' AND freight_kind ='MTY'  THEN 1  \n" +
//                "ELSE NULL END) AS onboard_MT_20, \n" +
//                "(CASE WHEN mis_exp_unit.cont_size > '20' AND freight_kind ='MTY'  THEN 1  \n" +
//                "ELSE NULL END) AS onboard_MT_40, \n" +
//                "(CASE WHEN mis_exp_unit.cont_size=20 AND freight_kind in ('FCL','LCL') THEN 1 \n" +
//                "ELSE (CASE WHEN mis_exp_unit.cont_size>20 AND freight_kind in ('FCL','LCL') THEN 2 ELSE NULL END) END) AS onboard_LD_tues, \n" +
//                "(CASE WHEN mis_exp_unit.cont_size=20 AND freight_kind='MTY' THEN 1 \n" +
//                "ELSE (CASE WHEN mis_exp_unit.cont_size>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS onboard_MT_tues\n" +
//                "\n" +
//                "FROM ctmsmis.mis_exp_unit\n" +
//                "left join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
//                "where  mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' and snx=0\n" +
//                ") as tmp";
//
//

        sqlQuery="SELECT \n" +
                "NVL(SUM(onboard_LD_20),0) AS onboard_LD_20,\n" +
                "NVL(SUM(onboard_LD_40),0) AS onboard_LD_40,\n" +
                "NVL(SUM(onboard_MT_20),0) AS onboard_MT_20,\n" +
                "NVL(SUM(onboard_MT_40),0) AS onboard_MT_40,\n" +
                "NVL(SUM(onboard_LD_tues),0) AS onboard_LD_tues,\n" +
                "NVL(SUM(onboard_MT_tues),0) AS onboard_MT_tues\n" +
                "\n" +
                " FROM (\n" +
                "SELECT DISTINCT inv_unit.gkey AS gkey, inv_unit_fcy_visit.time_in AS fcy_time_in,\n" +
                "(CASE WHEN substr(ref_equip_type.nominal_length,-2) = '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_20, \n" +
                "(CASE WHEN substr(ref_equip_type.nominal_length,-2) > '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_40,\n" +
                "(CASE WHEN substr(ref_equip_type.nominal_length,-2) = '20' AND freight_kind ='MTY'  THEN 1 \n" +
                "ELSE NULL END) AS onboard_MT_20, \n" +
                "(CASE WHEN substr(ref_equip_type.nominal_length,-2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS onboard_MT_40, \n" +
                "(CASE WHEN substr(ref_equip_type.nominal_length,-2)=20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN substr(ref_equip_type.nominal_length,-2)>20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS onboard_LD_tues, \n" +
                "(CASE WHEN substr(ref_equip_type.nominal_length,-2)=20 AND freight_kind='MTY' THEN 1\n" +
                "ELSE (CASE WHEN substr(ref_equip_type.nominal_length,2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS onboard_MT_tues\n" +
                "FROM inv_unit\n" +
                "\n" +
                "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey \n" +
                "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                "INNER JOIN argo_carrier_visit ON inv_unit_fcy_visit.actual_ib_cv=argo_carrier_visit.gkey \n" +
                "INNER JOIN vsl_vessel_visit_details ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey \n" +
                "WHERE  vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'  AND inv_unit.category='EXPRT' \n" +
                "AND inv_unit_fcy_visit.transit_state='S20_INBOUND' AND inv_unit_fcy_visit.time_in IS NULL\n" +
                ")  tmp WHERE fcy_time_in IS NULL";

        List resultList=OracleDbTemplate.query(sqlQuery,new ExportReportLoadedContainerSummeryReport());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    public List getLoadedContainerSummaryReportList(Integer vvdgkey,String fromdate,String todate,String fromTime,String toTime){
        String sqlQuery="";
        String cond="";
        String frmDate="";
        String tDate="";
        if(fromdate!="" && todate!="")
        {
            if(fromTime=="" || toTime==""){
                cond = " and to_char(inv_unit_fcy_visit.time_load,'yyyy-mm-dd') between '"+fromdate+"' and '"+todate+"'";
            }

            else
            {
                frmDate = fromdate+" "+fromTime+":00";
                tDate = todate+" "+toTime+":00";
                cond = " and to_char(inv_unit_fcy_visit.time_load,'yyyy-mm-dd') between '"+frmDate+"' and '"+tDate+"'";
            }
        }
        sqlQuery="SELECT \n" +
                "NVL(SUM(balance_LD_20),0) AS balance_LD_20,\n" +
                "NVL(SUM(balance_LD_40),0) AS balance_LD_40,\n" +
                "NVL(SUM(balance_MT_20),0) AS balance_MT_20,\n" +
                "NVL(SUM(balance_MT_40),0) AS balance_MT_40,\n" +
                "NVL(SUM(balance_LD_tues),0) AS balance_LD_tues,\n" +
                "NVL(SUM(balance_MT_tues),0) AS balance_MT_tues\n" +
                "\n" +
                " FROM (\n" +
                "SELECT DISTINCT inv_unit.gkey AS gkey, inv_unit_fcy_visit.time_in AS fcy_transit_state,\n" +
                "(CASE WHEN  substr(ref_equip_type.nominal_length,-2) = '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_20, \n" +
                "(CASE WHEN  substr(ref_equip_type.nominal_length,-2) > '20' AND freight_kind IN ('FCL','LCL')  THEN 1 \n" +
                "ELSE NULL END) AS balance_LD_40,\n" +
                "(CASE WHEN  substr(ref_equip_type.nominal_length,-2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_20, \n" +
                "(CASE WHEN  substr(ref_equip_type.nominal_length,-2) > '20' AND freight_kind ='MTY'  THEN 1 \n" +
                "ELSE NULL END) AS balance_MT_40, \n" +
                "(CASE WHEN  substr(ref_equip_type.nominal_length,-2)=20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE  WHEN substr(ref_equip_type.nominal_length,-2)>20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS balance_LD_tues, \n" +
                "(CASE WHEN  substr(ref_equip_type.nominal_length,-2)=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE  WHEN substr(ref_equip_type.nominal_length,-2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS balance_MT_tues\n" +
                "\n" +
                "FROM inv_unit\n" +
                "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ob_cv\n" +
                "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
                "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                "WHERE argo_carrier_visit.cvcvd_gkey='"+vvdgkey+"' AND category='EXPRT'\n" +
                ")tmp";
        List resultList=OracleDbTemplate.query(sqlQuery,new ExportReportLoadedContainerSummeryReportList());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class ExportReportLoadedContainerSummeryReport implements RowMapper{

        @Override
        public ExportLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportLoadedContainerList exportLoadedContainerList =new ExportLoadedContainerList();
            exportLoadedContainerList.setOnboard_LD_20(rs.getString("onboard_LD_20"));
            exportLoadedContainerList.setOnboard_LD_40(rs.getString("onboard_LD_40"));
            exportLoadedContainerList.setOnboard_MT_20(rs.getString("onboard_MT_20"));
            exportLoadedContainerList.setOnboard_MT_40(rs.getString("onboard_MT_40"));

            exportLoadedContainerList.setOnboard_LD_tues(rs.getString("onboard_LD_tues"));
            exportLoadedContainerList.setOnboard_MT_tues(rs.getString("onboard_MT_tues"));

            return exportLoadedContainerList;
        }
    }class ExportReportLoadedContainerSummeryReportList implements RowMapper{

        @Override
        public ExportLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportLoadedContainerList exportLoadedContainerList =new ExportLoadedContainerList();

            exportLoadedContainerList.setBalance_LD_20(rs.getString("balance_LD_20"));

            exportLoadedContainerList.setBalance_LD_40(rs.getString("balance_LD_40"));

            exportLoadedContainerList.setBalance_MT_20(rs.getString("balance_MT_20"));
            exportLoadedContainerList.setBalance_MT_40(rs.getString("balance_MT_40"));
            exportLoadedContainerList.setBalance_LD_tues(rs.getString("balance_LD_tues"));
            exportLoadedContainerList.setBalance_MT_tues(rs.getString("balance_MT_tues"));

            return exportLoadedContainerList;
        }
    }

    class ExportReportLoadedContainerList implements RowMapper{

        @Override
        public ExportLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportLoadedContainerList exportLoadedContainerList =new ExportLoadedContainerList();
            exportLoadedContainerList.setId(rs.getString("cont_id"));
            exportLoadedContainerList.setIso(rs.getString("iso"));
            exportLoadedContainerList.setTYPE(rs.getString("TYPE"));
            exportLoadedContainerList.setMlo(rs.getString("mlo"));
            exportLoadedContainerList.setFreight_kind(rs.getString("freight_kind"));

            exportLoadedContainerList.setWeight(rs.getString("weight"));
            exportLoadedContainerList.setPod(rs.getString("pod"));
            exportLoadedContainerList.setStowage_pos(rs.getString("stowage_pos"));

            exportLoadedContainerList.setLast_update(rs.getTimestamp("last_update"));

            exportLoadedContainerList.setComing_from(rs.getString("coming_from"));
            exportLoadedContainerList.setShort_name(rs.getString("short_name"));
//            exportLoadedContainerList.setUser_id(rs.getString("user_id"));

            return exportLoadedContainerList;
        }
    }


    public List getVessleInformation(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="SELECT vsl_vessels.name AS vsl_name,COALESCE(vsl_vessel_visit_details.flex_string02,vsl_vessel_visit_details.flex_string03) AS berth_op,COALESCE(argo_quay.id,'') AS berth,\n" +
                "argo_carrier_visit.ata,argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessel_berthings ON vsl_vessel_berthings.vvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN argo_quay ON argo_quay.gkey=vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=OracleDbTemplate.query(sqlQuery,new ExportLoadedContainerVesselInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class ExportLoadedContainerVesselInfo implements RowMapper{

        @Override
        public ExportLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportLoadedContainerList exportLoadedContainerList =new ExportLoadedContainerList();
            exportLoadedContainerList.setVsl_name(rs.getString("vsl_name"));
            exportLoadedContainerList.setBerth_op(rs.getString("berth_op"));
            exportLoadedContainerList.setBerth(rs.getString("berth"));
            exportLoadedContainerList.setAta(rs.getDate("ata"));
            exportLoadedContainerList.setAtd(rs.getTimestamp("atd"));

            return exportLoadedContainerList;
        }
    }

}