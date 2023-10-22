package com.datasoft.IgmMis.Service.ExportReport;
import com.datasoft.IgmMis.Model.ExportReport.ExportMloWisePreAdvisedLoadedContainerList;
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
public class ExportMloWisePreAdvisedLoadedContainerListService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;


    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;


    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;



    public List getMloWisePreAdvisedLoadedContainer(String ddl_imp_rot_no){
        String sqlQuery="";
//        sqlQuery="SELECT gkey,mlo,agent,\n" +
//                "IFNULL(SUM(Loaded_20),0) AS Loaded_20,\n" +
//                "IFNULL(SUM(Loaded_40),0) AS Loaded_40,\n" +
//                "IFNULL(SUM(EMTY_20),0) AS EMTY_20,\n" +
//                "IFNULL(SUM(EMTY_40),0) AS EMTY_40,\n" +
//                "\n" +
//                "IFNULL(SUM(REEFER_20),0) AS REEFER_20,\n" +
//                "IFNULL(SUM(REEFER_40),0) AS REEFER_40,\n" +
//                "IFNULL(SUM(IMDG_20),0) AS IMDG_20,\n" +
//                "IFNULL(SUM(IMDG_40),0) AS IMDG_40,\n" +
//                "IFNULL(SUM(TRSHP_20),0) AS TRSHP_20,\n" +
//                "IFNULL(SUM(TRSHP_40),0) AS TRSHP_40,\n" +
//                "IFNULL(SUM(ICD_20),0) AS ICD_20,\n" +
//                "IFNULL(SUM(ICD_40),0) AS ICD_40,\n" +
//                "IFNULL(SUM(LD_20),0) AS LD_20,\n" +
//                "IFNULL(SUM(LD_40),0) AS LD_40,\n" +
//                "\n" +
//                "IFNULL(SUM(grand_tot),0) AS grand_tot,\n" +
//                "IFNULL(SUM(tues),0) AS tues\n" +
//                "FROM (\n" +
//                "SELECT DISTINCT ctmsmis.mis_exp_unit_preadv_req.gkey AS gkey,mis_exp_unit_preadv_req.cont_mlo AS mlo,mis_exp_unit_preadv_req.agent AS agent,\n" +
//                "(CASE WHEN cont_size =20  AND cont_status IN ('FCL','LCL') and ctmsmis.mis_exp_unit_preadv_req.isoType not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3','45R5') THEN 1  \n" +
//                "ELSE NULL END) AS Loaded_20, \n" +
//                "(CASE WHEN cont_size!=20  AND cont_status IN ('FCL','LCL') and ctmsmis.mis_exp_unit_preadv_req.isoType not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3','45R5')  THEN 1  \n" +
//                "ELSE NULL END) AS Loaded_40, \n" +
//                "\n" +
//                "(CASE WHEN cont_size = 20 AND cont_status ='MTY'  THEN 1  \n" +
//                "ELSE NULL END) AS EMTY_20, \n" +
//                "(CASE WHEN cont_size != 20 AND cont_status ='MTY'  THEN 1  \n" +
//                "ELSE NULL END) AS EMTY_40, \n" +
//                "(CASE WHEN cont_size = 20 AND cont_status IN ('FCL','LCL') AND ctmsmis.mis_exp_unit_preadv_req.isoType in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3','45R5')  THEN 1  \n" +
//                "ELSE NULL END) AS REEFER_20, \n" +
//                "(CASE WHEN cont_size != 20 AND cont_status IN ('FCL','LCL') AND ctmsmis.mis_exp_unit_preadv_req.isoType in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3','45R5')  THEN 1  \n" +
//                "ELSE NULL END) AS REEFER_40,\n" +
//                "'' AS IMDG_20, \n" +
//                "'' AS IMDG_40, \n" +
//                "(CASE WHEN cont_size =20  AND cont_status IN ('FCL','LCL','MTY') AND ctmsmis.mis_exp_unit_preadv_req.cont_category='TRSHP' THEN 1  \n" +
//                "ELSE NULL END) AS TRSHP_20, \n" +
//                "(CASE WHEN cont_size!=20  AND cont_status IN ('FCL','LCL','MTY') AND ctmsmis.mis_exp_unit_preadv_req.cont_category='TRSHP' THEN 1  \n" +
//                "ELSE NULL END) AS TRSHP_40, \n" +
//                "'' AS ICD_20, \n" +
//                "'' AS ICD_40, \n" +
//                "'' AS LD_20, \n" +
//                "'' AS LD_40, \n" +
//                "1 AS grand_tot,\n" +
//                " (CASE WHEN cont_size=20  THEN 1 ELSE 2 END) AS tues \n" +
//                "FROM  ctmsmis.mis_exp_unit_preadv_req\n" +
//                "WHERE  mis_exp_unit_preadv_req.rotation='"+ddl_imp_rot_no+ "'\n" +
//                ") AS tmp GROUP BY mlo ";


                sqlQuery="SELECT mlo,\n" +
                        "NVL(SUM(Loaded_20),0) AS Loaded_20,\n" +
                        "NVL(SUM(Loaded_40),0) AS Loaded_40,\n" +
                        "NVL(SUM(EMTY_20),0) AS EMTY_20,\n" +
                        "NVL(SUM(EMTY_40),0) AS EMTY_40,\n" +
                        "NVL(SUM(REEFER_20),0) AS REEFER_20,\n" +
                        "NVL(SUM(REEFER_40),0) AS REEFER_40,\n" +
                        "NVL(SUM(IMDG_20),0) AS IMDG_20,\n" +
                        "NVL(SUM(IMDG_40),0) AS IMDG_40,\n" +
                        "NVL(SUM(TRSHP_20),0) AS TRSHP_20,\n" +
                        "NVL(SUM(TRSHP_40),0) AS TRSHP_40,\n" +
                        "NVL(SUM(ICD_20),0) AS ICD_20,\n" +
                        "NVL(SUM(ICD_40),0) AS ICD_40,\n" +
                        "NVL(SUM(LD_20),0) AS LD_20,\n" +
                        "NVL(SUM(LD_40),0) AS LD_40,\n" +
                        "NVL(SUM(grand_tot),0) AS grand_tot,\n" +
                        "NVL(SUM(tues),0) AS tues\n" +
                        "FROM (\n" +
                        "SELECT DISTINCT vsl_vessel_visit_details.ib_vyg,inv_unit.gkey AS gkey,g.id AS mlo,Y.id AS agent,\n" +
                        "(CASE WHEN substr(ref_equip_type.nominal_length,-2) =20  AND inv_unit.freight_kind IN ('FCL','LCL') and ref_equip_type.id not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3','45R5') THEN 1  \n" +
                        "ELSE NULL END) AS Loaded_20, \n" +
                        "(CASE WHEN substr(ref_equip_type.nominal_length,-2) !=20  AND inv_unit.freight_kind IN ('FCL','LCL') and ref_equip_type.id not in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3','45R5')  THEN 1  \n" +
                        "ELSE NULL END) AS Loaded_40, \n" +
                        "(CASE WHEN substr(ref_equip_type.nominal_length,-2) = 20 AND inv_unit.freight_kind ='MTY'  THEN 1  \n" +
                        "ELSE NULL END) AS EMTY_20, \n" +
                        "(CASE WHEN substr(ref_equip_type.nominal_length,-2) != 20 AND inv_unit.freight_kind ='MTY'  THEN 1  \n" +
                        "ELSE NULL END) AS EMTY_40, \n" +
                        "(CASE WHEN substr(ref_equip_type.nominal_length,-2) = 20 AND inv_unit.freight_kind IN ('FCL','LCL') AND ref_equip_type.id in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3','45R5')  THEN 1  \n" +
                        "ELSE NULL END) AS REEFER_20, \n" +
                        "(CASE WHEN substr(ref_equip_type.nominal_length,-2) != 20 AND inv_unit.freight_kind IN ('FCL','LCL') AND ref_equip_type.id in('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3','45R5')  THEN 1  \n" +
                        "ELSE NULL END) AS REEFER_40,\n" +
                        "'' AS IMDG_20, \n" +
                        "'' AS IMDG_40, \n" +
                        "(CASE WHEN substr(ref_equip_type.nominal_length,-2) =20  AND inv_unit.freight_kind IN ('FCL','LCL','MTY') AND inv_unit.category='TRSHP' THEN 1  \n" +
                        "ELSE NULL END) AS TRSHP_20, \n" +
                        "(CASE WHEN substr(ref_equip_type.nominal_length,-2) !=20  AND inv_unit.freight_kind IN ('FCL','LCL','MTY') AND inv_unit.category='TRSHP' THEN 1  \n" +
                        "ELSE NULL END) AS TRSHP_40, \n" +
                        "'' AS ICD_20, \n" +
                        "'' AS ICD_40, \n" +
                        "'' AS LD_20, \n" +
                        "'' AS LD_40, \n" +
                        "1 AS grand_tot,\n" +
                        "(CASE WHEN substr(ref_equip_type.nominal_length,-2) =20  THEN 1 ELSE 2 END) AS tues \n" +
                        "FROM  inv_unit\n" +
                        "inner join inv_unit_fcy_visit on inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                        "INNER JOIN ( ref_bizunit_scoped g LEFT JOIN ( ref_agent_representation X LEFT JOIN ref_bizunit_scoped Y ON X.agent_gkey=Y.gkey ) ON g.gkey=X.bzu_gkey ) ON g.gkey = inv_unit.line_op\n" +
                        "inner join argo_carrier_visit on argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ob_cv\n" +
                        "inner join vsl_vessel_visit_details on vsl_vessel_visit_details.vvd_gkey=argo_carrier_visit.cvcvd_gkey\n" +
                        "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
                        "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                        "WHERE  vsl_vessel_visit_details.ib_vyg='"+ddl_imp_rot_no+"'\n" +
                        ") tmp GROUP BY mlo ";
        List resultList=OracleDbTemplate.query(sqlQuery,new MloWisePreAdvisedLoadedContainer());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    public List getMloWisePreadviceViewList(String Rotation,String mlo){
        String rotation=Rotation.replace('_','/');

        //        String sqlQuery="SELECT ctmsmis.mis_exp_unit_preadv_req.gkey AS gkey,mis_exp_unit_preadv_req.cont_id AS cont_id,mis_exp_unit_preadv_req.cont_mlo,\n" +
        //                "mis_exp_unit_preadv_req.cont_size AS cont_size,mis_exp_unit_preadv_req.isoType,'' AS loc,'' AS emtyDate,\n" +
        //                "mis_exp_unit_preadv_req.rotation,mis_exp_unit_preadv_req.cont_status,mis_exp_unit_preadv_req.pod,\n" +
        //                "goods_and_ctr_wt_kg,seal_no,last_update,cont_mlo,\n" +
        //                "(SELECT CODE  FROM ctmsmis.offdoc WHERE id= ctmsmis.mis_exp_unit_preadv_req.transOp) AS offdock,\n" +
        //                "(SELECT NAME FROM sparcsn4.vsl_vessel_visit_details\n" +
        //                " INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
        //                " WHERE ib_vyg=ctmsmis.mis_exp_unit_preadv_req.rotation) AS vsl_name\n" +
        //                "FROM  ctmsmis.mis_exp_unit_preadv_req \n" +
        //                "WHERE  mis_exp_unit_preadv_req.rotation='"+rotation+"' AND cont_mlo='"+mlo+"' ORDER BY cont_mlo,cont_id\n" +
        //                "\n";

        String sqlQuery="SELECT vsl_vessel_visit_details.*,vsl_vessel_visit_details.ib_vyg as rotation, inv_unit.id as cont_id,inv_unit_fcy_visit.time_in AS fcy_time_in,vsl_vessel_visit_details.ib_vyg,\n" +
                "SUBSTR(ref_equip_type.nominal_length,-2, LENGTH( ref_equip_type.nominal_length)) AS cont_size, SUBSTR(ref_equip_type.nominal_height, -2, LENGTH( ref_equip_type.nominal_height)) AS height,inv_unit.seal_nbr1 AS seal_no,\n" +
                "ref_equip_type.id AS isotype,ref_bizunit_scoped.id AS cont_mlo,inv_unit.freight_kind as cont_status,inv_unit.goods_and_ctr_wt_kg AS goods_and_ctr_wt_kg,\n" +
                "ref_commodity.short_name,inv_unit.remark,\n" +
                "(SELECT rt.truck_id FROM road_truck_transactions rtt\n" +
                "INNER JOIN road_trucks rt ON rt.trkco_gkey=rtt.trkco_gkey\n" +
                "WHERE rtt.unit_gkey=inv_unit.gkey fetch first 1 rows only) AS truck_no,inv_unit_fcy_visit.last_pos_slot AS stowage_pos,\n" +
                "REF_ROUTING_POINT.ID as pod,inv_unit_fcy_visit.LAST_POS_LOCTYPE AS coming_from\n" +
                "FROM  inv_unit \n" +
                "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ib_cv\n" +
                "INNER JOIN vsl_vessel_visit_details ON vsl_vessel_visit_details.vvd_gkey=argo_carrier_visit.cvcvd_gkey\n" +
                "LEFT JOIN inv_goods ON inv_goods.gkey=inv_unit.goods\n" +
                "LEFT JOIN ref_commodity ON ref_commodity.gkey=inv_goods.commodity_gkey\n" +
                "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
                "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN ref_bizunit_scoped ON ref_bizunit_scoped.gkey=inv_unit.line_op\n" +
                "INNER JOIN REF_ROUTING_POINT ON INV_UNIT.POD1_GKEY = REF_ROUTING_POINT.GKEY\n" +
                "WHERE  vsl_vessel_visit_details.ib_vyg='"+rotation+"' and ref_bizunit_scoped.id='"+mlo+"'";


        List resultList=SecondaryDBTemplate.query(sqlQuery,new MloWisePreadviceViewList());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    public List getMloWiseSummaryList(String Rotation,String mlo){


//        String sqlQuery="\n" +
//                "SELECT cont_mlo,last_update,NAME,voys_no,cont_size,\n" +
//                "IFNULL(SUM(F_20),0) AS F_20,\n" +
//                "IFNULL(SUM(L_20),0) AS L_20,\n" +
//                "IFNULL(SUM(M_20),0) AS M_20,\n" +
//                "IFNULL(SUM(I_20),0) AS I_20,\n" +
//                "IFNULL(SUM(T_20),0) AS T_20,\n" +
//                "IFNULL(SUM(R_20),0) AS R_20,\n" +
//                "IFNULL(SUM(IMDG_20),0) AS IMDG_20, \n" +
//                "IFNULL(SUM(F_40),0) AS F_40,\n" +
//                "IFNULL(SUM(L_40),0) AS L_40,\n" +
//                "IFNULL(SUM(M_40),0) AS M_40,\n" +
//                "IFNULL(SUM(I_40),0) AS I_40,\n" +
//                "IFNULL(SUM(T_40),0) AS T_40,\n" +
//                "IFNULL(SUM(R_40),0) AS R_40,\n" +
//                "IFNULL(SUM(IMDG_40),0) AS IMDG_40,\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "IFNULL(SUM(FW_20),0) AS FW_20,\n" +
//                "IFNULL(SUM(LW_20),0) AS LW_20,\n" +
//                "IFNULL(SUM(MW_20),0) AS MW_20,\n" +
//                "IFNULL(SUM(IW_20),0) AS IW_20,\n" +
//                "IFNULL(SUM(TW_20),0) AS TW_20,\n" +
//                "IFNULL(SUM(RW_20),0) AS RW_20,\n" +
//                "IFNULL(SUM(IMDGW_20),0) AS IMDGW_20, \n" +
//                "IFNULL(SUM(FW_40),0) AS FW_40,\n" +
//                "IFNULL(SUM(LW_40),0) AS LW_40,\n" +
//                "IFNULL(SUM(MW_40),0) AS MW_40,\n" +
//                "IFNULL(SUM(IW_40),0) AS IW_40,\n" +
//                "IFNULL(SUM(TW_40),0) AS TW_40,\n" +
//                "IFNULL(SUM(RW_40),0) AS RW_40,\n" +
//                "IFNULL(SUM(IMDGW_40),0) AS IMDGW_40\n" +
//                "FROM (\n" +
//                "\n" +
//                "SELECT cont_mlo,DATE(last_update) AS last_update,sparcsn4.vsl_vessels.name,voys_no,cont_size,\n" +
//                "(CASE WHEN cont_size = '20' AND cont_status IN ('FCL') AND isoGroup NOT IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS F_20,\n" +
//                "(CASE WHEN cont_size = '20' AND cont_status IN ('LCL') AND isoGroup NOT IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS L_20,\n" +
//                "(CASE WHEN cont_size = '20' AND cont_status IN ('MTY') AND isoGroup NOT IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS M_20,\n" +
//                "0 AS I_20,\n" +
//                "0 AS T_20,\n" +
//                "(CASE WHEN cont_size = '20' AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS R_20,\n" +
//                "0 AS IMDG_20,\n" +
//                "(CASE WHEN cont_size IN ('40','45') AND cont_status IN ('FCL') AND isoGroup NOT IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS F_40,\n" +
//                "(CASE WHEN cont_size IN ('40','45') AND cont_status IN ('LCL') AND isoGroup NOT IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS L_40,\n" +
//                "(CASE WHEN cont_size IN ('40','45') AND cont_status IN ('MTY') AND isoGroup NOT IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS M_40,\n" +
//                "0 AS I_40,\n" +
//                "0 AS T_40,\n" +
//                "(CASE WHEN cont_size IN ('40','45') AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS R_40,\n" +
//                "0 AS IMDG_40,\n" +
//                "(CASE WHEN cont_size = '20' AND cont_status IN ('FCL') AND isoGroup NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
//                "ELSE NULL END) AS FW_20,\n" +
//                "(CASE WHEN cont_size = '20' AND cont_status IN ('LCL') AND isoGroup NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
//                "ELSE NULL END) AS LW_20,\n" +
//                "(CASE WHEN cont_size = '20' AND cont_status IN ('MTY') AND isoGroup NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg \n" +
//                "ELSE NULL END) AS MW_20,\n" +
//                "0 AS IW_20,\n" +
//                "0 AS TW_20,\n" +
//                "(CASE WHEN cont_size = '20' AND isoGroup IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
//                "ELSE NULL END) AS RW_20,\n" +
//                "0 AS IMDGW_20,\n" +
//                "(CASE WHEN cont_size IN ('40','45') AND cont_status IN ('FCL') AND isoGroup NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
//                "ELSE NULL END) AS FW_40,\n" +
//                "(CASE WHEN cont_size IN ('40','45') AND cont_status IN ('LCL') AND isoGroup NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
//                "ELSE NULL END) AS LW_40,\n" +
//                "(CASE WHEN cont_size IN ('40','45') AND cont_status IN ('MTY') AND isoGroup NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
//                "ELSE NULL END) AS MW_40,\n" +
//                "0 AS IW_40,\n" +
//                "0 AS TW_40,\n" +
//                "(CASE WHEN cont_size IN ('40','45') AND isoGroup IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
//                "ELSE NULL END) AS RW_40,\n" +
//                "0 AS IMDGW_40\n" +
//                "\n" +
//                "FROM ctmsmis.mis_exp_unit_preadv_req \n" +
//                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=ctmsmis.mis_exp_unit_preadv_req.vvd_gkey\n" +
//                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
//                "WHERE rotation='"+Rotation+"' AND cont_mlo='"+mlo+"'\n" +
//                ") AS tmp ";
//
//
                String sqlQuery="\n" +
                        "SELECT gkey,mlo,\n" +
                        "NVL(SUM(F_20),0) AS F_20,\n" +
                        "NVL(SUM(L_20),0) AS L_20,\n" +
                        "NVL(SUM(M_20),0) AS M_20,\n" +
                        "NVL(SUM(I_20),0) AS I_20,\n" +
                        "NVL(SUM(T_20),0) AS T_20,\n" +
                        "NVL(SUM(R_20),0) AS R_20,\n" +
                        "NVL(SUM(IMDG_20),0) AS IMDG_20, \n" +
                        "NVL(SUM(F_40),0) AS F_40,\n" +
                        "NVL(SUM(L_40),0) AS L_40,\n" +
                        "NVL(SUM(M_40),0) AS M_40,\n" +
                        "NVL(SUM(I_40),0) AS I_40,\n" +
                        "NVL(SUM(T_40),0) AS T_40,\n" +
                        "NVL(SUM(R_40),0) AS R_40,\n" +
                        "NVL(SUM(IMDG_40),0) AS IMDG_40,\n" +
                        "NVL(SUM(FW_20),0) AS FW_20,\n" +
                        "NVL(SUM(LW_20),0) AS LW_20,\n" +
                        "NVL(SUM(MW_20),0) AS MW_20,\n" +
                        "NVL(SUM(IW_20),0) AS IW_20,\n" +
                        "NVL(SUM(TW_20),0) AS TW_20,\n" +
                        "NVL(SUM(RW_20),0) AS RW_20,\n" +
                        "NVL(SUM(IMDGW_20),0) AS IMDGW_20, \n" +
                        "NVL(SUM(FW_40),0) AS FW_40,\n" +
                        "NVL(SUM(LW_40),0) AS LW_40,\n" +
                        "NVL(SUM(MW_40),0) AS MW_40,\n" +
                        "NVL(SUM(IW_40),0) AS IW_40,\n" +
                        "NVL(SUM(TW_40),0) AS TW_40,\n" +
                        "NVL(SUM(RW_40),0) AS RW_40,\n" +
                        "NVL(SUM(IMDGW_40),0) AS IMDGW_40\n" +
                        "FROM (\n" +
                        "\n" +
                        "SELECT  inv_unit.gkey AS gkey,vsl_vessel_visit_details.ib_vyg,ref_bizunit_scoped.id AS mlo,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND freight_kind IN ('FCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN 1  \n" +
                        "ELSE NULL END) AS F_20,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND freight_kind IN ('LCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN 1  \n" +
                        "ELSE NULL END) AS L_20,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND freight_kind IN ('MTY') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN 1  \n" +
                        "ELSE NULL END) AS M_20,\n" +
                        "0 AS I_20,\n" +
                        "0 AS T_20,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1  \n" +
                        "ELSE NULL END) AS R_20,\n" +
                        "0 AS IMDG_20,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) IN ('40','45') AND freight_kind IN ('FCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN 1  \n" +
                        "ELSE NULL END) AS F_40,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) IN ('40','45') AND freight_kind IN ('LCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN 1  \n" +
                        "ELSE NULL END) AS L_40,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) IN ('40','45') AND freight_kind IN ('MTY') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN 1  \n" +
                        "ELSE NULL END) AS M_40,\n" +
                        "0 AS I_40,\n" +
                        "0 AS T_40,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) IN ('40','45') AND ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1  \n" +
                        "ELSE NULL END) AS R_40,\n" +
                        "0 AS IMDG_40,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND freight_kind IN ('FCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
                        "ELSE NULL END) AS FW_20,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND freight_kind IN ('LCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
                        "ELSE NULL END) AS LW_20,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND freight_kind IN ('MTY') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg \n" +
                        "ELSE NULL END) AS MW_20,\n" +
                        "0 AS IW_20,\n" +
                        "0 AS TW_20,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND ref_equip_type.iso_group IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
                        "ELSE NULL END) AS RW_20,\n" +
                        "0 AS IMDGW_20,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) IN ('40','45') AND freight_kind IN ('FCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
                        "ELSE NULL END) AS FW_40,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) IN ('40','45') AND freight_kind IN ('LCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
                        "ELSE NULL END) AS LW_40,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) IN ('40','45') AND freight_kind IN ('MTY') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
                        "ELSE NULL END) AS MW_40,\n" +
                        "0 AS IW_40,\n" +
                        "0 AS TW_40,\n" +
                        "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) IN ('40','45') AND ref_equip_type.iso_group IN ('RS','RT','RE')  THEN goods_and_ctr_wt_kg  \n" +
                        "ELSE NULL END) AS RW_40,\n" +
                        "0 AS IMDGW_40\n" +
                        "\n" +
                        "FROM inv_unit\n" +
                        "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                        "INNER JOIN argo_carrier_visit ON argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ob_cv\n" +
                        "INNER JOIN argo_visit_details ON argo_visit_details.gkey=argo_carrier_visit.cvcvd_gkey\n" +
                        "INNER JOIN vsl_vessel_visit_details ON vsl_vessel_visit_details.vvd_gkey=argo_visit_details.gkey\n" +
                        "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
                        "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                        "INNER JOIN ref_bizunit_scoped  ON inv_unit.line_op = ref_bizunit_scoped.gkey \n" +
                        "WHERE vsl_vessel_visit_details.ib_vyg='"+Rotation+"'  AND ref_bizunit_scoped.id='MSL'\n" +
                        ") tmp GROUP BY gkey,mlo";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new MloWiseSummary());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class MloWiseSummary implements RowMapper {

        @Override
        public ExportMloWisePreAdvisedLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWisePreAdvisedLoadedContainerList exportMloWisePreAdvisedLoadedContainerList = new ExportMloWisePreAdvisedLoadedContainerList();


            exportMloWisePreAdvisedLoadedContainerList.setCont_mlo(rs.getString("cont_mlo"));
//            exportMloWisePreAdvisedLoadedContainerList.setLast_update(rs.getTimestamp("last_update"));
//            exportMloWisePreAdvisedLoadedContainerList.setName(rs.getString("name"));
//            exportMloWisePreAdvisedLoadedContainerList.setVoys_no(rs.getString("voys_no"));
//            exportMloWisePreAdvisedLoadedContainerList.setCont_size(rs.getString("cont_size"));

            exportMloWisePreAdvisedLoadedContainerList.setF_20(rs.getString("F_20"));
            exportMloWisePreAdvisedLoadedContainerList.setL_20(rs.getString("L_20"));
            exportMloWisePreAdvisedLoadedContainerList.setM_20(rs.getString("M_20"));
            exportMloWisePreAdvisedLoadedContainerList.setI_20(rs.getString("I_20"));
            exportMloWisePreAdvisedLoadedContainerList.setT_20(rs.getString("T_20"));
            exportMloWisePreAdvisedLoadedContainerList.setR_20(rs.getString("R_20"));
            exportMloWisePreAdvisedLoadedContainerList.setIMDG_20(rs.getString("imdg_20"));
            exportMloWisePreAdvisedLoadedContainerList.setF_40(rs.getString("F_40"));
            exportMloWisePreAdvisedLoadedContainerList.setL_40(rs.getString("L_40"));
            exportMloWisePreAdvisedLoadedContainerList.setM_40(rs.getString("M_40"));
            exportMloWisePreAdvisedLoadedContainerList.setI_40(rs.getString("I_40"));
            exportMloWisePreAdvisedLoadedContainerList.setT_40(rs.getString("t_40"));
            exportMloWisePreAdvisedLoadedContainerList.setR_40(rs.getString("r_40"));
            exportMloWisePreAdvisedLoadedContainerList.setIMDG_40(rs.getString("imdg_40"));
            exportMloWisePreAdvisedLoadedContainerList.setFW_20(rs.getString("fw_20"));
            exportMloWisePreAdvisedLoadedContainerList.setLW_20(rs.getString("lw_20"));
            exportMloWisePreAdvisedLoadedContainerList.setMW_20(rs.getString("mw_20"));
            exportMloWisePreAdvisedLoadedContainerList.setIW_20(rs.getString("iw_20"));
            exportMloWisePreAdvisedLoadedContainerList.setTW_20(rs.getString("tw_20"));
            exportMloWisePreAdvisedLoadedContainerList.setRW_20(rs.getString("rw_20"));
            exportMloWisePreAdvisedLoadedContainerList.setIMDGW_20(rs.getString("imdgw_20"));
            exportMloWisePreAdvisedLoadedContainerList.setFW_40(rs.getString("fw_40"));
            exportMloWisePreAdvisedLoadedContainerList.setLW_40(rs.getString("lw_40"));
            exportMloWisePreAdvisedLoadedContainerList.setMW_40(rs.getString("mw_40"));
            exportMloWisePreAdvisedLoadedContainerList.setIW_40(rs.getString("iw_40"));
            exportMloWisePreAdvisedLoadedContainerList.setTW_40(rs.getString("tw_40"));
            exportMloWisePreAdvisedLoadedContainerList.setRW_40(rs.getString("rw_40"));
            exportMloWisePreAdvisedLoadedContainerList.setIMDGW_40(rs.getString("imdgw_40"));


            return exportMloWisePreAdvisedLoadedContainerList;
        }
    }

    class MloWisePreAdvisedLoadedContainer implements RowMapper {

        @Override
        public ExportMloWisePreAdvisedLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWisePreAdvisedLoadedContainerList exportMloWisePreAdvisedLoadedContainerList=new ExportMloWisePreAdvisedLoadedContainerList();
            exportMloWisePreAdvisedLoadedContainerList.setAgent(rs.getString("agent"));
            exportMloWisePreAdvisedLoadedContainerList.setMlo(rs.getString("mlo"));
            exportMloWisePreAdvisedLoadedContainerList.setLoaded_20(rs.getString("Loaded_20"));
            exportMloWisePreAdvisedLoadedContainerList.setLoaded_40(rs.getString("Loaded_40"));
            exportMloWisePreAdvisedLoadedContainerList.setEMTY_20(rs.getString("EMTY_20"));
            exportMloWisePreAdvisedLoadedContainerList.setEMTY_40(rs.getString("EMTY_40"));
            exportMloWisePreAdvisedLoadedContainerList.setREEFER_20(rs.getString("REEFER_20"));
            exportMloWisePreAdvisedLoadedContainerList.setREEFER_40(rs.getString("REEFER_40"));
            exportMloWisePreAdvisedLoadedContainerList.setIMDG_20(rs.getString("IMDG_20"));
            exportMloWisePreAdvisedLoadedContainerList.setIMDG_40(rs.getString("IMDG_40"));
            exportMloWisePreAdvisedLoadedContainerList.setTRSHP_20(rs.getString("TRSHP_20"));
            exportMloWisePreAdvisedLoadedContainerList.setTRSHP_40(rs.getString("TRSHP_40"));
            exportMloWisePreAdvisedLoadedContainerList.setICD_20(rs.getString("ICD_20"));
            exportMloWisePreAdvisedLoadedContainerList.setICD_40(rs.getString("ICD_40"));
            exportMloWisePreAdvisedLoadedContainerList.setLD_20(rs.getString("LD_20"));
            exportMloWisePreAdvisedLoadedContainerList.setLD_40(rs.getString("LD_40"));
            exportMloWisePreAdvisedLoadedContainerList.setGrand_tot(rs.getString("grand_tot"));

            return exportMloWisePreAdvisedLoadedContainerList;
        }
    }


    class MloWisePreadviceViewList implements RowMapper {

        @Override
        public ExportMloWisePreAdvisedLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWisePreAdvisedLoadedContainerList exportMloWisePreAdvisedLoadedContainerList=new ExportMloWisePreAdvisedLoadedContainerList();
            exportMloWisePreAdvisedLoadedContainerList.setGkey(rs.getString("gkey"));
            exportMloWisePreAdvisedLoadedContainerList.setCont_id(rs.getString("cont_id"));

            exportMloWisePreAdvisedLoadedContainerList.setCont_mlo(rs.getString("cont_mlo"));
            exportMloWisePreAdvisedLoadedContainerList.setCont_size(rs.getString("cont_size"));
            exportMloWisePreAdvisedLoadedContainerList.setIsoType(rs.getString("isotype"));
//            exportMloWisePreAdvisedLoadedContainerList.setOffdock(rs.getString("offdock"));
//            exportMloWisePreAdvisedLoadedContainerList.setVsl_name(rs.getString("vsl_name"));

            exportMloWisePreAdvisedLoadedContainerList.setRotation(rs.getString("rotation"));
            exportMloWisePreAdvisedLoadedContainerList.setPod(rs.getString("pod"));
            exportMloWisePreAdvisedLoadedContainerList.setCont_status(rs.getString("cont_status"));
            exportMloWisePreAdvisedLoadedContainerList.setGoods_and_ctr_wt_kg(rs.getString("goods_and_ctr_wt_kg"));
            exportMloWisePreAdvisedLoadedContainerList.setSeal_no(rs.getString("seal_no"));
//            exportMloWisePreAdvisedLoadedContainerList.setLast_update(rs.getTimestamp("last_update"));
//            exportMloWisePreAdvisedLoadedContainerList.setCont_mlo(rs.getString("cont_mlo"));

            return exportMloWisePreAdvisedLoadedContainerList;
        }

    }


}
