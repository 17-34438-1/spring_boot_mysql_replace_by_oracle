package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportLoadedContainerListLoadAndEmpty;

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
public class ExportLoadedContainerListLoadAndEmptyService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;

    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;


    public Integer get_Loaded_Container_vvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportLoadedContainerListLoadAndEmpty> resultList=OracleDbTemplate.query(sqlQuery,new ExportLoadedContainerVvdGkey());

        ExportLoadedContainerListLoadAndEmpty exportLoadedContainerListLoadAndEmpty;
        for(int i=0;i<resultList.size();i++){
            exportLoadedContainerListLoadAndEmpty=resultList.get(i);
            vvdgkey=exportLoadedContainerListLoadAndEmpty.getVvd_gkey();
        }
        return vvdgkey;
    }

    class ExportLoadedContainerVvdGkey implements RowMapper {

        @Override
        public ExportLoadedContainerListLoadAndEmpty mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportLoadedContainerListLoadAndEmpty exportLoadedContainerListLoadAndEmpty=new ExportLoadedContainerListLoadAndEmpty();
            exportLoadedContainerListLoadAndEmpty.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportLoadedContainerListLoadAndEmpty;
        }
    }



    public List getExportLoadedContainerListLoadandEmptyVessleInformation(Integer vvdgkey){
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
    public List getLoadedContainerListLoadandEmptyInfo(Integer vvdgkey){
        String sqlQuery="";


//        sqlQuery="SELECT ctmsmis.mis_exp_unit.gkey,CONCAT(SUBSTRING(ctmsmis.mis_exp_unit.cont_id,1,4),SUBSTRING(ctmsmis.mis_exp_unit.cont_id,5)) AS id, \n" +
//                "RIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size,RIGHT(sparcsn4.ref_equip_type.nominal_height,2) AS height, \n" +
//                "mis_exp_unit.cont_mlo AS mlo,sparcsn4.ref_bizunit_scoped.name AS mlo_name,ctmsmis.mis_exp_unit.craine_id,ctmsmis.mis_exp_unit.seal_no,cont_status AS freight_kind,\n" +
//                "ctmsmis.mis_exp_unit.coming_from AS coming_from,ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos, \n" +
//                "ctmsmis.mis_exp_unit.user_id,ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg AS goods_and_ctr_wt_kg,ctmsmis.mis_exp_unit.truck_no\n" +
//                "FROM ctmsmis.mis_exp_unit \n" +
//                "LEFT JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey  \n" +
//                "INNER JOIN sparcsn4.ref_bizunit_scoped  ON sparcsn4.inv_unit.line_op = sparcsn4.ref_bizunit_scoped.gkey \n" +
//                " \n" +
//                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods \n" +
//                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey \n" +
//                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey \n" +
//                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
//                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey \n" +
//                "WHERE mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' AND snx_type=0 AND mis_exp_unit.delete_flag='0' ORDER BY mis_exp_unit.cont_mlo,cont_status\n" +
//                "\n";


        sqlQuery="SELECT  CONCAT(CONCAT(substr(inv_unit.id,1,4),' '),substr(inv_unit.id,5)) AS id,inv_unit_fcy_visit.time_in AS fcy_time_in,substr(ref_equip_type.nominal_height,-2) as height,inv_unit_fcy_visit.last_pos_slot AS stowage_pos,\n" +
                "inv_unit_fcy_visit.last_pos_slot AS location,inv_unit.seal_nbr1 AS sealno,\n" +
                "ref_equip_type.id AS iso,ref_bizunit_scoped.id AS mlo,inv_unit.freight_kind,inv_unit.goods_and_ctr_wt_kg AS weight, REF_ROUTING_POINT.ID as pod,inv_unit_fcy_visit.LAST_POS_LOCTYPE AS coming_from,argo_carrier_visit.id AS truck_no,\n" +
                "ref_commodity.short_name\n" +
                "FROM  inv_unit \n" +
                "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ib_cv\n" +
                "INNER JOIN vsl_vessel_visit_details ON vsl_vessel_visit_details.vvd_gkey=argo_carrier_visit.cvcvd_gkey\n" +
                "LEFT JOIN inv_goods ON inv_goods.gkey=inv_unit.goods\n" +
                "LEFT JOIN ref_commodity ON ref_commodity.gkey=inv_goods.commodity_gkey\n" +
                "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
                "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN ref_bizunit_scoped ON ref_bizunit_scoped.gkey=inv_unit.line_op\n" +
                "INNER JOIN REF_ROUTING_POINT ON REF_ROUTING_POINT.GKEY=INV_UNIT.POD1_GKEY\n" +
                "WHERE category='EXPRT' \n" +
                "AND transit_state NOT IN ('S60_LOADED','S70_DEPARTED','S99_RETIRED')";

        List resultList=OracleDbTemplate.query(sqlQuery,new ExportLoadedContainerListLoadandEmptyInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    public List getLoadedContainerInformation(Integer vvdgkey){
        String sqlQuery="";

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
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind in ('FCL','LCL')  THEN 1  \n" +
//                "ELSE NULL END) AS onboard_LD_20, \n" +
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind in ('FCL','LCL')  THEN 1  \n" +
//                "ELSE NULL END) AS onboard_LD_40,\n" +
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
//                "ELSE NULL END) AS onboard_MT_20, \n" +
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
//                "ELSE NULL END) AS onboard_MT_40, \n" +
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind in ('FCL','LCL') THEN 1 \n" +
//                "ELSE (CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind in ('FCL','LCL') THEN 2 ELSE NULL END) END) AS onboard_LD_tues, \n" +
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind='MTY' THEN 1 \n" +
//                "ELSE (CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS onboard_MT_tues\n" +
//                "\n" +
//                "FROM ctmsmis.mis_exp_unit\n" +
//                "inner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
//                "inner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
//                "inner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
//                "inner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
//                "where  mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' and snx=0\n" +
//                ") as tmp";



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



        List resultList=OracleDbTemplate.query(sqlQuery,new LoadedContainerInformation());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public List getLoadedContainerList(Integer vvdgkey){
        String sqlQuery="";
//        sqlQuery="select gkey,\n" +
//                "IFNULL(SUM(balance_LD_20),0) AS balance_LD_20,\n" +
//                "IFNULL(SUM(balance_LD_40),0) AS balance_LD_40,\n" +
//                "IFNULL(SUM(balance_MT_20),0) AS balance_MT_20,\n" +
//                "IFNULL(SUM(balance_MT_40),0) AS balance_MT_40,\n" +
//                "IFNULL(SUM(balance_LD_tues),0) AS balance_LD_tues,\n" +
//                "IFNULL(SUM(balance_MT_tues),0) AS balance_MT_tues\n" +
//                "\n" +
//                " from (\n" +
//                "select distinct sparcsn4.inv_unit.gkey as gkey,\n" +
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind in ('FCL','LCL')  THEN 1  \n" +
//                "ELSE NULL END) AS balance_LD_20, \n" +
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind in ('FCL','LCL')  THEN 1  \n" +
//                "ELSE NULL END) AS balance_LD_40,\n" +
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
//                "ELSE NULL END) AS balance_MT_20, \n" +
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
//                "ELSE NULL END) AS balance_MT_40, \n" +
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind in ('FCL','LCL') THEN 1 \n" +
//                "ELSE (CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind in ('FCL','LCL') THEN 2 ELSE NULL END) END) AS balance_LD_tues, \n" +
//                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind='MTY' THEN 1 \n" +
//                "ELSE (CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS balance_MT_tues\n" +
//                "\n" +
//                "FROM sparcsn4.inv_unit\n" +
//                "inner join sparcsn4.inv_unit_fcy_visit on sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
//                "inner join sparcsn4.argo_carrier_visit on sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ob_cv\n" +
//                "inner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
//                "inner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
//                "inner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
//                "where  sparcsn4.argo_carrier_visit.cvcvd_gkey='"+vvdgkey+"' and category='EXPRT' and transit_state not in ('S60_LOADED','S70_DEPARTED','S99_RETIRED')\n" +
//                ") as tmp";

        sqlQuery="SELECT \n" +
                "NVL(SUM(balance_LD_20),0) AS balance_LD_20,\n" +
                "NVL(SUM(balance_LD_40),0) AS balance_LD_40,\n" +
                "NVL(SUM(balance_MT_20),0) AS balance_MT_20,\n" +
                "NVL(SUM(balance_MT_40),0) AS balance_MT_40,\n" +
                "NVL(SUM(balance_LD_tues),0) AS balance_LD_tues,\n" +
                "NVL(SUM(balance_MT_tues),0) AS balance_MT_tues\n" +
                "FROM (\n" +
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
                "FROM inv_unit\n" +
                "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ob_cv\n" +
                "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
                "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                "WHERE  argo_carrier_visit.cvcvd_gkey='"+vvdgkey+"' AND category='EXPRT' \n" +
                ")tmp";
        List resultList=OracleDbTemplate.query(sqlQuery,new LoadedContainerList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class ExportLoadedContainerVesselInfo implements RowMapper{

        @Override
        public ExportLoadedContainerListLoadAndEmpty mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportLoadedContainerListLoadAndEmpty exportLoadedContainerListLoadAndEmpty =new ExportLoadedContainerListLoadAndEmpty();
            exportLoadedContainerListLoadAndEmpty.setVsl_name(rs.getString("vsl_name"));
            exportLoadedContainerListLoadAndEmpty.setBerth_op(rs.getString("berth_op"));
            exportLoadedContainerListLoadAndEmpty.setBerth(rs.getString("berth"));
            exportLoadedContainerListLoadAndEmpty.setAta(rs.getDate("ata"));
            exportLoadedContainerListLoadAndEmpty.setAtd(rs.getTimestamp("atd"));

            return exportLoadedContainerListLoadAndEmpty;
        }
    }

    class ExportLoadedContainerListLoadandEmptyInfo implements RowMapper{

        @Override
        public ExportLoadedContainerListLoadAndEmpty mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportLoadedContainerListLoadAndEmpty exportLoadedContainerListLoadAndEmpty =new ExportLoadedContainerListLoadAndEmpty();
            exportLoadedContainerListLoadAndEmpty.setId(rs.getString("id"));

            exportLoadedContainerListLoadAndEmpty.setHeight(rs.getString("height"));
            exportLoadedContainerListLoadAndEmpty.setFreight_kind(rs.getString("freight_kind"));
            exportLoadedContainerListLoadAndEmpty.setStowage_pos(rs.getString("stowage_pos"));
            exportLoadedContainerListLoadAndEmpty.setSeal_no(rs.getString("sealno"));
            exportLoadedContainerListLoadAndEmpty.setGoods_and_ctr_wt_kg(rs.getString("weight"));
            exportLoadedContainerListLoadAndEmpty.setPod(rs.getString("pod"));
            exportLoadedContainerListLoadAndEmpty.setTruck_no(rs.getString("truck_no"));
            exportLoadedContainerListLoadAndEmpty.setComing_from(rs.getString("coming_from"));
//          exportLoadedContainerListLoadAndEmpty.setCraine_id(rs.getString("craine_id"));

            return exportLoadedContainerListLoadAndEmpty;
        }
    }
    class LoadedContainerInformation implements RowMapper{

        @Override
        public ExportLoadedContainerListLoadAndEmpty mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportLoadedContainerListLoadAndEmpty exportLoadedContainerListLoadAndEmpty =new ExportLoadedContainerListLoadAndEmpty();
            exportLoadedContainerListLoadAndEmpty.setOnboard_LD_20(rs.getString("onboard_LD_20"));
            exportLoadedContainerListLoadAndEmpty.setOnboard_LD_40(rs.getString("onboard_LD_40"));
            exportLoadedContainerListLoadAndEmpty.setOnboard_MT_20(rs.getString("onboard_MT_20"));
            exportLoadedContainerListLoadAndEmpty.setOnboard_MT_40(rs.getString("onboard_MT_40"));
            exportLoadedContainerListLoadAndEmpty.setOnboard_LD_tues(rs.getString("onboard_LD_tues"));
            exportLoadedContainerListLoadAndEmpty.setOnboard_MT_tues(rs.getString("onboard_MT_tues"));

            return exportLoadedContainerListLoadAndEmpty;
        }
    }
    class LoadedContainerList implements RowMapper{

        @Override
        public ExportLoadedContainerListLoadAndEmpty mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportLoadedContainerListLoadAndEmpty exportLoadedContainerListLoadAndEmpty =new ExportLoadedContainerListLoadAndEmpty();

            exportLoadedContainerListLoadAndEmpty.setBalance_LD_20(rs.getString("balance_LD_20"));
            exportLoadedContainerListLoadAndEmpty.setBalance_LD_40(rs.getString("balance_LD_40"));
            exportLoadedContainerListLoadAndEmpty.setBalance_MT_20(rs.getString("balance_MT_20"));
            exportLoadedContainerListLoadAndEmpty.setBalance_MT_40(rs.getString("balance_MT_40"));
            exportLoadedContainerListLoadAndEmpty.setBalance_LD_tues(rs.getString("balance_LD_tues"));
            exportLoadedContainerListLoadAndEmpty.setBalance_MT_tues(rs.getString("balance_MT_tues"));

            return exportLoadedContainerListLoadAndEmpty;
        }
    }

}