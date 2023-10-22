package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportMloWiseLoadedContainer;
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
public class ExportMloWiseLoadedContainerService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;
    public Integer get_Mlo_Wise_Loaded_Container_vvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportMloWiseLoadedContainer> resultList=OracleDbTemplate.query(sqlQuery,new ExportMloWiseLoadedContainerVvdGkey());

        ExportMloWiseLoadedContainer exportMloWiseLoadedContainer;
        for(int i=0;i<resultList.size();i++){
            exportMloWiseLoadedContainer=resultList.get(i);
            vvdgkey=exportMloWiseLoadedContainer.getVvd_gkey();
        }
        return vvdgkey;
    }

    class ExportMloWiseLoadedContainerVvdGkey implements RowMapper {

        @Override
        public ExportMloWiseLoadedContainer mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWiseLoadedContainer exportMloWiseLoadedContainer=new ExportMloWiseLoadedContainer();
            exportMloWiseLoadedContainer.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportMloWiseLoadedContainer;
        }
    }



    public List getMloWiseLoadedContainerVessleInformation(Integer vvdgkey){
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
//
        sqlQuery="SELECT vsl_vessels.name AS vsl_name,COALESCE(vsl_vessel_visit_details.flex_string02,vsl_vessel_visit_details.flex_string03) AS berthop,COALESCE(argo_quay.id,'') AS berth,\n" +
                "argo_carrier_visit.ata,argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessel_berthings ON vsl_vessel_berthings.vvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN argo_quay ON argo_quay.gkey=vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=OracleDbTemplate.query(sqlQuery,new ExportMloWiseContainerVesselInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    public List getMloWiseLoadedContainerInfo(Integer vvdgkey){
        String sqlQuery="";
//        sqlQuery="SELECT ctmsmis.mis_exp_unit.gkey,CONCAT(SUBSTRING(ctmsmis.mis_exp_unit.cont_id,1,4),' ',SUBSTRING(ctmsmis.mis_exp_unit.cont_id,5)) AS id,seal_no,mis_exp_unit.goods_and_ctr_wt_kg AS goods_and_ctr_wt_kg,\n" +
//                "RIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size,RIGHT(sparcsn4.ref_equip_type.nominal_height,2) AS height,\n" +
//                "truck_no,craine_id,\n" +
//                "cont_mlo AS mlo,\n" +
//                "(SELECT NAME FROM sparcsn4.ref_bizunit_scoped WHERE id= mis_exp_unit.cont_mlo ORDER BY id LIMIT 1) AS mlo_name,\n" +
//                "\n" +
//                "sparcsn4.inv_unit.freight_kind,ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg AS weight,ctmsmis.mis_exp_unit.coming_from AS coming_from,\n" +
//                "ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos,ctmsmis.mis_exp_unit.last_update,ctmsmis.mis_exp_unit.user_id\n" +
//                "FROM ctmsmis.mis_exp_unit\n" +
//                "INNER JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
//                "\n" +
//                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
//                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
//                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
//                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
//                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
//                "WHERE mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' AND mis_exp_unit.delete_flag='0' AND snx_type=0 ORDER BY mlo,id\n";
//

        sqlQuery="\n" +
                "SELECT inv_unit.gkey,inv_unit.id,inv_unit.projected_pod_gkey,inv_unit_fcy_visit.transit_state,SUBSTR(ref_equip_type.nominal_length,-2, LENGTH( ref_equip_type.nominal_length)) AS cont_size, SUBSTR(ref_equip_type.nominal_height, -2, LENGTH( ref_equip_type.nominal_height)) AS height,\n" +
                "ref_bizunit_scoped.name AS mlo_name,inv_unit.seal_nbr1 AS seal_no,vsl_vessel_visit_details.vvd_gkey,\n" +
                "ref_equip_type.id AS iso,ref_bizunit_scoped.id AS mlo,inv_unit.freight_kind,inv_unit.goods_and_ctr_wt_kg,\n" +
                "ref_commodity.short_name AS commodity,inv_unit.remark as remarks, inv_unit_fcy_visit.ARRIVE_POS_SLOT as stowage_pos, inv_unit_fcy_visit.LAST_POS_NAME,\n" +
                "argo_carrier_visit.id AS truck_no,\n" +
                "REF_ROUTING_POINT.ID as pod, inv_unit_fcy_visit.LAST_POS_LOCTYPE AS coming_from\n" +
                "\n" +
                "FROM inv_unit\n" +
                "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ob_cv \n" +
                "INNER JOIN vsl_vessel_visit_details ON vsl_vessel_visit_details.vvd_gkey=argo_carrier_visit.cvcvd_gkey \n" +
                "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey \n" +
                "INNER JOIN ref_bizunit_scoped  ON inv_unit.line_op = ref_bizunit_scoped.gkey \n" +
                "INNER JOIN REF_ROUTING_POINT ON INV_UNIT.POD1_GKEY = REF_ROUTING_POINT.GKEY\n" +
                "\n" +
                "LEFT JOIN inv_goods ON inv_goods.gkey=inv_unit.goods \n" +
                "LEFT JOIN ref_commodity ON ref_commodity.gkey=inv_goods.commodity_gkey \n" +
                "INNER JOIN ref_equipment ON ref_equipment.gkey=INV_UNIT.eq_gkey\n" +
                "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";

        List resultList=OracleDbTemplate.query(sqlQuery,new ExportMloWiseContainerInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    public List getMloWiseLoadedContainerInformation(Integer vvdgkey){
        String sqlQuery="";
//                 sqlQuery="select gkey,\n" +
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

//                sqlQuery="select gkey,\n" +
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

        List resultList=OracleDbTemplate.query(sqlQuery,new MloWiseLoadedContainerInformation());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public List getMloWiseLoadedContainerList(Integer vvdgkey){
        String sqlQuery;
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
                "FROM \n" +
                "( \n" +
                "SELECT DISTINCT inv_unit.gkey AS gkey, inv_unit_fcy_visit.time_in AS fcy_transit_state, \n" +
                "(CASE WHEN substr(ref_equip_type.nominal_length,-2) = '20' AND freight_kind IN ('FCL','LCL') THEN 1 ELSE NULL END) AS balance_LD_20,\n" +
                "(CASE WHEN substr(ref_equip_type.nominal_length,-2) > '20' AND freight_kind IN ('FCL','LCL') THEN 1 ELSE NULL END) AS balance_LD_40,\n" +
                "(CASE WHEN  substr(ref_equip_type.nominal_length,-2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_20, \n" +
                "(CASE WHEN  substr(ref_equip_type.nominal_length,-2) > '20' AND freight_kind ='MTY'  THEN 1 \n" +
                "ELSE NULL END) AS balance_MT_40, \n" +
                "(CASE WHEN  substr(ref_equip_type.nominal_length,-2)=20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE  WHEN substr(ref_equip_type.nominal_length,-2)>20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS balance_LD_tues, \n" +
                "(CASE WHEN  substr(ref_equip_type.nominal_length,-2)=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE  WHEN substr(ref_equip_type.nominal_length,-2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS balance_MT_tues\n" +
                "FROM inv_unit\n" +
                "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ob_cv \n" +
                "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
                "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                "WHERE argo_carrier_visit.cvcvd_gkey='"+vvdgkey+"' AND category='EXPRT' \n" +
                ")tmp";
        List resultList=OracleDbTemplate.query(sqlQuery,new MloWiseLoadedContainerList());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class ExportMloWiseContainerVesselInfo implements RowMapper{

        @Override
        public ExportMloWiseLoadedContainer mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWiseLoadedContainer exportMloWiseLoadedContainer =new ExportMloWiseLoadedContainer();
            exportMloWiseLoadedContainer.setVsl_name(rs.getString("vsl_name"));
            exportMloWiseLoadedContainer.setBerth_op(rs.getString("berth_op"));
            exportMloWiseLoadedContainer.setBerth(rs.getString("berth"));
            exportMloWiseLoadedContainer.setAta(rs.getDate("ata"));
            exportMloWiseLoadedContainer.setAtd(rs.getTimestamp("atd"));

            return exportMloWiseLoadedContainer;
        }
    }

    class ExportMloWiseContainerInfo implements RowMapper{

        @Override
        public ExportMloWiseLoadedContainer mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWiseLoadedContainer exportMloWiseLoadedContainer =new ExportMloWiseLoadedContainer();
            exportMloWiseLoadedContainer.setId(rs.getString("id"));
            exportMloWiseLoadedContainer.setHeight(rs.getString("height"));
            exportMloWiseLoadedContainer.setFreight_kind(rs.getString("freight_kind"));
            exportMloWiseLoadedContainer.setStowage_pos(rs.getString("stowage_pos"));
            exportMloWiseLoadedContainer.setSeal_no(rs.getString("seal_no"));
            exportMloWiseLoadedContainer.setMlo(rs.getString("mlo"));
            exportMloWiseLoadedContainer.setMlo_name(rs.getString("mlo_name"));
            exportMloWiseLoadedContainer.setGoods_and_ctr_wt_kg(rs.getString("goods_and_ctr_wt_kg"));
            exportMloWiseLoadedContainer.setPod(rs.getString("pod"));
            exportMloWiseLoadedContainer.setTruck_no(rs.getString("truck_no"));
            exportMloWiseLoadedContainer.setComing_from(rs.getString("coming_from"));
            exportMloWiseLoadedContainer.setCraine_id(rs.getString("craine_id"));

            return exportMloWiseLoadedContainer;
        }
    }
    class MloWiseLoadedContainerInformation implements RowMapper{

        @Override
        public ExportMloWiseLoadedContainer mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWiseLoadedContainer exportMloWiseLoadedContainer =new ExportMloWiseLoadedContainer();
            exportMloWiseLoadedContainer.setOnboard_LD_20(rs.getString("onboard_LD_20"));
            exportMloWiseLoadedContainer.setOnboard_LD_40(rs.getString("onboard_LD_40"));
            exportMloWiseLoadedContainer.setOnboard_MT_20(rs.getString("onboard_MT_20"));
            exportMloWiseLoadedContainer.setOnboard_MT_40(rs.getString("onboard_MT_40"));
            exportMloWiseLoadedContainer.setOnboard_LD_tues(rs.getString("onboard_LD_tues"));
            exportMloWiseLoadedContainer.setOnboard_MT_tues(rs.getString("onboard_MT_tues"));

            return exportMloWiseLoadedContainer;
        }
    }
    class MloWiseLoadedContainerList implements RowMapper{

        @Override
        public ExportMloWiseLoadedContainer mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWiseLoadedContainer exportMloWiseLoadedContainer =new ExportMloWiseLoadedContainer();
            exportMloWiseLoadedContainer.setBalance_LD_20(rs.getString("balance_LD_20"));
            exportMloWiseLoadedContainer.setBalance_LD_40(rs.getString("balance_LD_40"));
            exportMloWiseLoadedContainer.setBalance_MT_20(rs.getString("balance_MT_20"));
            exportMloWiseLoadedContainer.setBalance_MT_40(rs.getString("balance_MT_40"));
            exportMloWiseLoadedContainer.setBalance_LD_tues(rs.getString("balance_LD_tues"));
            exportMloWiseLoadedContainer.setBalance_MT_tues(rs.getString("balance_MT_tues"));

            return exportMloWiseLoadedContainer;
        }
    }

}
