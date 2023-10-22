package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportContainerLoading;
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
public class ExportContainerLoadingReportService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;


    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;

    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;

    public Integer get_Gkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportContainerLoading> resultList=OracleDbTemplate.query(sqlQuery,new ExportMloWiseExportSummaryVvdGkey());

        ExportContainerLoading exportContainerLoading;
        for(int i=0;i<resultList.size();i++){
            exportContainerLoading=resultList.get(i);
            vvdgkey=exportContainerLoading.getVvd_gkey();
        }
        return vvdgkey;
    }



    public List getExportContainerVoyNo(String rotation){
        String sqlQuery="";
        String voyNo="";
        sqlQuery="SELECT Voy_No FROM igm_masters WHERE Import_Rotation_No='"+rotation+"'";
        System.out.println(sqlQuery);
        List resultList=primaryDBTemplate.query(sqlQuery,new ExportMloWiseExportSummaryVoyNo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());

        return listAll;
    }

    public List get_container_Loading_vessel_info(Integer vvdgkey){
        String sqlQuery="";

        sqlQuery="SELECT vsl_vessels.name as vsl_name,COALESCE(vsl_vessel_visit_details.flex_string02,vsl_vessel_visit_details.flex_string03) AS berth_op,COALESCE(argo_quay.id,'') AS berth,\n" +
                "argo_carrier_visit.ata,argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessel_berthings ON vsl_vessel_berthings.vvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN argo_quay ON argo_quay.gkey=vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=OracleDbTemplate.query(sqlQuery,new ExportMloWiseExportSummaryVesselInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public List getExportContainer(Integer vvdgkey,String fromdate,String todate,String fromTime,String toTime){
        String cond="";
        String frmDate="";
        String tDate="";
        if(fromdate!="" && todate!="")
        {

            if(fromTime!="")
            {
                frmDate = fromdate+ " " +fromTime+ ":00";
            }

            else
            {
                frmDate = fromdate+ "00:00:00";
                System.out.println("FromDate:"+frmDate);
            }


            if(toTime!=""){
                tDate = todate+ " " +toTime+ ":00";
            }

            else
            {
                tDate = todate+ "23:59:59";
                System.out.println("toDate:"+tDate);
            }
             cond = "AND to_char(inv_unit_fcy_visit.time_load,'yyyy-mm-dd') BETWEEN '"+frmDate+"' and '"+tDate+"'";
        }
        else
        {
            cond = " ";
        }



//        String sqlQuery="SELECT gkey,mlo,(SELECT NAME FROM sparcsn4.ref_bizunit_scoped WHERE id=mlo AND NAME !='NULL' LIMIT 1) AS mlo_name,\n" +
//                "IFNULL(SUM(D_20),0) AS D_20,\n" +
//                "IFNULL(SUM(D_40),0) AS D_40,\n" +
//                "IFNULL(SUM(H_40),0) AS H_40,\n" +
//                "IFNULL(SUM(H_45),0) AS H_45,\n" +
//                "\n" +
//                "IFNULL(SUM(R_20),0) AS R_20,\n" +
//                "IFNULL(SUM(RH_40),0) AS RH_40,\n" +
//                "\n" +
//                "IFNULL(SUM(OT_20),0) AS OT_20,\n" +
//                "IFNULL(SUM(OT_40),0) AS OT_40,\n" +
//                "\n" +
//                "IFNULL(SUM(FR_20),0) AS FR_20,\n" +
//                "IFNULL(SUM(FR_40),0) AS FR_40,\n" +
//                "\n" +
//                "IFNULL(SUM(TK_20),0) AS TK_20,\n" +
//                "\n" +
//                "IFNULL(SUM(MD_20),0) AS MD_20,\n" +
//                "IFNULL(SUM(MD_40),0) AS MD_40,\n" +
//                "IFNULL(SUM(MH_40),0) AS MH_40,\n" +
//                "IFNULL(SUM(MH_45),0) AS MH_45,\n" +
//                "\n" +
//                "IFNULL(SUM(MR_20),0) AS MR_20,\n" +
//                "IFNULL(SUM(MRH_40),0) AS MRH_40,\n" +
//                "\n" +
//                "IFNULL(SUM(MOT_20),0) AS MOT_20,\n" +
//                "IFNULL(SUM(MOT_40),0) AS MOT_40,\n" +
//                "\n" +
//                "IFNULL(SUM(MFR_20),0) AS MFR_20,\n" +
//                "IFNULL(SUM(MFR_40),0) AS MFR_40,\n" +
//                "\n" +
//                "IFNULL(SUM(MTK_20),0) AS MTK_20,\n" +
//                "\n" +
//                "IFNULL(SUM(grand_tot),0) AS grand_tot,\n" +
//                "IFNULL(SUM(tues),0) AS tues,\n" +
//                "SUM(goods_and_ctr_wt_kg) AS weight\n" +
//                " FROM (\n" +
//                "SELECT DISTINCT ctmsmis.mis_exp_unit.gkey AS gkey,cont_mlo AS mlo,inv_unit.goods_and_ctr_wt_kg,\n" +
//                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
//                "ELSE NULL END) AS D_20, \n" +
//                "(CASE WHEN cont_size = '40' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
//                "ELSE NULL END) AS D_40, \n" +
//                "(CASE WHEN cont_size = '40' AND cont_height='96' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
//                "ELSE NULL END) AS H_40, \n" +
//                "(CASE WHEN cont_size = '45' AND cont_height='96' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1\n" +
//                "\t  WHEN cont_size = '42' AND cont_height='90' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1\n" +
//                "ELSE NULL END) AS H_45,\n" +
//                "\n" +
//                "\n" +
//                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS R_20, \n" +
//                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS RH_40,\n" +
//                "\n" +
//                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('UT')  THEN 1  \n" +
//                "ELSE NULL END) AS OT_20,\n" +
//                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('UT')  THEN 1  \n" +
//                "ELSE NULL END) AS OT_40,\n" +
//                "\n" +
//                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('PF','PC')  THEN 1  \n" +
//                "ELSE NULL END) AS FR_20,\n" +
//                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('PF','PC')  THEN 1  \n" +
//                "ELSE NULL END) AS FR_40,\n" +
//                "\n" +
//                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('TN','TD','TG')  THEN 1  \n" +
//                "ELSE NULL END) AS TK_20,\n" +
//                "\n" +
//                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
//                "ELSE NULL END) AS MD_20, \n" +
//                "(CASE WHEN cont_size = '40' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
//                "ELSE NULL END) AS MD_40, \n" +
//                "(CASE WHEN cont_size = '40' AND cont_height='96' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
//                "ELSE NULL END) AS MH_40, \n" +
//                "(CASE WHEN cont_size = '45' AND cont_height='96' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
//                "ELSE NULL END) AS MH_45,\n" +
//                "\n" +
//                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS MR_20, \n" +
//                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
//                "ELSE NULL END) AS MRH_40,\n" +
//                "\n" +
//                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('UT')  THEN 1  \n" +
//                "ELSE NULL END) AS MOT_20,\n" +
//                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('UT')  THEN 1  \n" +
//                "ELSE NULL END) AS MOT_40,\n" +
//                "\n" +
//                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('PF','PC')  THEN 1  \n" +
//                "ELSE NULL END) AS MFR_20,\n" +
//                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('PF','PC')  THEN 1  \n" +
//                "ELSE NULL END) AS MFR_40,\n" +
//                "\n" +
//                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('TN','TD','TG')  THEN 1  \n" +
//                "ELSE NULL END) AS MTK_20,\n" +
//                "(CASE WHEN cont_size IN('20','40','45','42')  THEN 1 ELSE NULL END) AS grand_tot,\n" +
//                "(CASE WHEN cont_size=20  THEN 1 ELSE 2 END) AS tues     \n" +
//                "FROM ctmsmis.mis_exp_unit\n" +
//                "LEFT JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey\n" +
//                " \n" +
//                "WHERE  mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' AND snx_type=0 AND cont_mlo IS NOT NULL "+cond+"\n" +
//                ") AS tmp GROUP BY mlo WITH ROLLUP";




        String sqlQuery="SELECT gkey,mlo,mlo_name,\n" +
                "NVL(SUM(D_20),0) AS D_20,\n" +
                "NVL(SUM(D_40),0) AS D_40,\n" +
                "NVL(SUM(H_40),0) AS H_40,\n" +
                "NVL(SUM(H_45),0) AS H_45,\n" +
                "\n" +
                "NVL(SUM(R_20),0) AS R_20,\n" +
                "NVL(SUM(RH_40),0) AS RH_40,\n" +
                "\n" +
                "NVL(SUM(OT_20),0) AS OT_20,\n" +
                "NVL(SUM(OT_40),0) AS OT_40,\n" +
                "\n" +
                "NVL(SUM(FR_20),0) AS FR_20,\n" +
                "NVL(SUM(FR_40),0) AS FR_40,\n" +
                "\n" +
                "NVL(SUM(TK_20),0) AS TK_20,\n" +
                "\n" +
                "NVL(SUM(MD_20),0) AS MD_20,\n" +
                "NVL(SUM(MD_40),0) AS MD_40,\n" +
                "NVL(SUM(MH_40),0) AS MH_40,\n" +
                "NVL(SUM(MH_45),0) AS MH_45,\n" +
                "\n" +
                "NVL(SUM(MR_20),0) AS MR_20,\n" +
                "NVL(SUM(MRH_40),0) AS MRH_40,\n" +
                "\n" +
                "NVL(SUM(MOT_20),0) AS MOT_20,\n" +
                "NVL(SUM(MOT_40),0) AS MOT_40,\n" +
                "\n" +
                "NVL(SUM(MFR_20),0) AS MFR_20,\n" +
                "NVL(SUM(MFR_40),0) AS MFR_40,\n" +
                "\n" +
                "NVL(SUM(MTK_20),0) AS MTK_20,\n" +
                "\n" +
                "NVL(SUM(grand_tot),0) AS grand_tot,\n" +
                "NVL(SUM(tues),0) AS tues,\n" +
                "SUM(goods_and_ctr_wt_kg) AS weight\n" +
                "FROM (\n" +
                "SELECT DISTINCT inv_unit.gkey AS gkey,vsl_vessel_visit_details.ib_vyg,r.id AS mlo,r.name AS mlo_name,inv_unit.goods_and_ctr_wt_kg,vsl_vessel_visit_details.vvd_gkey,\n" +
                "(CASE WHEN  SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1 \n" +
                "ELSE NULL END) AS D_20,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '40' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1 \n" +
                "ELSE NULL END) AS D_40,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '40' AND (SUBSTR(ref_equip_type.nominal_height,-2))='96' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1 \n" +
                "ELSE NULL END) AS H_40,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '45' AND (SUBSTR(ref_equip_type.nominal_height,-2))='96' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1\n" +
                "WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '42' AND (SUBSTR(ref_equip_type.nominal_height,-2))='90' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1\n" +
                "ELSE NULL END) AS H_45,\n" +
                "\n" +
                "\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1 \n" +
                "ELSE NULL END) AS R_20,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '40' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1 \n" +
                "ELSE NULL END) AS RH_40,\n" +
                "\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group IN ('UT')  THEN 1 \n" +
                "ELSE NULL END) AS OT_20,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '40' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group IN ('UT')  THEN 1 \n" +
                "ELSE NULL END) AS OT_40,\n" +
                "\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group IN ('PF','PC')  THEN 1 \n" +
                "ELSE NULL END) AS FR_20,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '40' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group IN ('PF','PC')  THEN 1 \n" +
                "ELSE NULL END) AS FR_40,\n" +
                "\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind IN ('FCL','LCL') AND ref_equip_type.iso_group IN ('TN','TD','TG')  THEN 1 \n" +
                "ELSE NULL END) AS TK_20,\n" +
                "\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind ='MTY' AND ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1 \n" +
                "ELSE NULL END) AS MD_20,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '40' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind ='MTY' AND ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1 \n" +
                "ELSE NULL END) AS MD_40,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '40' AND (SUBSTR(ref_equip_type.nominal_height,-2))='96' AND freight_kind ='MTY' AND ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1 \n" +
                "ELSE NULL END) AS MH_40,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '45' AND (SUBSTR(ref_equip_type.nominal_height,-2))='96' AND freight_kind ='MTY' AND ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1 \n" +
                "ELSE NULL END) AS MH_45,\n" +
                "\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind ='MTY' AND ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1 \n" +
                "ELSE NULL END) AS MR_20,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '40' AND freight_kind ='MTY' AND ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1 \n" +
                "ELSE NULL END) AS MRH_40,\n" +
                "\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind ='MTY' AND ref_equip_type.iso_group IN ('UT')  THEN 1 \n" +
                "ELSE NULL END) AS MOT_20,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '40' AND freight_kind ='MTY' AND ref_equip_type.iso_group IN ('UT')  THEN 1 \n" +
                "ELSE NULL END) AS MOT_40,\n" +
                "\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind ='MTY' AND ref_equip_type.iso_group IN ('PF','PC')  THEN 1 \n" +
                "ELSE NULL END) AS MFR_20,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '40' AND freight_kind ='MTY' AND ref_equip_type.iso_group IN ('PF','PC')  THEN 1 \n" +
                "ELSE NULL END) AS MFR_40,\n" +
                "\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) = '20' AND (SUBSTR(ref_equip_type.nominal_height,-2))='86' AND freight_kind ='MTY' AND ref_equip_type.iso_group IN ('TN','TD','TG')  THEN 1 \n" +
                "ELSE NULL END) AS MTK_20,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2) IN('20','40','45','42')  THEN 1 ELSE NULL END) AS grand_tot,\n" +
                "(CASE WHEN SUBSTR(ref_equip_type.nominal_length,-2)=20  THEN 1 ELSE 2 END) AS tues \n" +
                "FROM inv_unit\n" +
                "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ob_cv\n" +
                "INNER JOIN argo_visit_details ON argo_visit_details.gkey=argo_carrier_visit.cvcvd_gkey\n" +
                "INNER JOIN vsl_vessel_visit_details ON vsl_vessel_visit_details.vvd_gkey=argo_visit_details.gkey\n" +
                "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
                "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN  ( ref_bizunit_scoped r       \n" +
                "LEFT JOIN ( ref_agent_representation X       \n" +
                "LEFT JOIN ref_bizunit_scoped Y ON X.agent_gkey=Y.gkey )  ON r.gkey=X.bzu_gkey        )  ON r.gkey = inv_unit.line_op\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"' "+cond+"\n" +
                ")\n" +
                "GROUP BY gkey,mlo,mlo_name";



        System.out.println("result:"+sqlQuery);
        List resultList=OracleDbTemplate.query(sqlQuery,new exportContainerLodingReport());
        System.out.println("sql:"+resultList);
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class ExportMloWiseExportSummaryVesselInfo implements RowMapper{

        @Override
        public ExportContainerLoading mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerLoading exportContainerLoading=new ExportContainerLoading();
            exportContainerLoading.setVsl_name(rs.getString("vsl_name"));
            exportContainerLoading.setBerth_op(rs.getString("berth_op"));
            exportContainerLoading.setBerth(rs.getString("berth"));
            exportContainerLoading.setAta(rs.getDate("ata"));
            exportContainerLoading.setAtd(rs.getTimestamp("atd"));
            return exportContainerLoading;
        }
    }


    class ExportMloWiseExportSummaryVvdGkey implements RowMapper {

        @Override
        public ExportContainerLoading mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerLoading exportContainerLoading=new ExportContainerLoading();
            exportContainerLoading.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportContainerLoading;
        }
    }

    class ExportMloWiseExportSummaryVoyNo implements RowMapper{

        @Override
        public ExportContainerLoading mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerLoading exportContainerLoading=new ExportContainerLoading();
            exportContainerLoading.setVoy_No(rs.getString("Voy_No"));

            return exportContainerLoading;
        }
    }
    class exportContainerLodingReport implements RowMapper{
        @Override
        public ExportContainerLoading mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerLoading exportContainerLoading=new ExportContainerLoading();
            exportContainerLoading.setMlo(rs.getString("mlo"));
            exportContainerLoading.setD_20(rs.getInt("d_20"));
            exportContainerLoading.setD_40(rs.getInt("d_40"));
            exportContainerLoading.setH_40(rs.getInt("h_40"));
            exportContainerLoading.setH_45(rs.getInt("h_45"));
            exportContainerLoading.setR_20(rs.getInt("r_20"));
            exportContainerLoading.setRH_40(rs.getInt("rh_40"));
            exportContainerLoading.setOT_20(rs.getInt("ot_20"));
            exportContainerLoading.setOT_40(rs.getInt("ot_40"));
            exportContainerLoading.setFR_20(rs.getInt("fr_20"));
            exportContainerLoading.setFR_40(rs.getInt("fr_40"));
            exportContainerLoading.setTK_20(rs.getInt("tk_20"));
            exportContainerLoading.setMD_20(rs.getInt("md_20"));
            exportContainerLoading.setMD_40(rs.getInt("md_40"));
            exportContainerLoading.setMH_40(rs.getInt("mh_40"));
            exportContainerLoading.setMH_45(rs.getInt("mh_45"));
            exportContainerLoading.setMRH_40(rs.getInt("mrh_40"));
            exportContainerLoading.setMR_20(rs.getInt("mr_20"));
            exportContainerLoading.setMOT_20(rs.getInt("mot_20"));
            exportContainerLoading.setMFR_20(rs.getInt("mfr_20"));
            exportContainerLoading.setMTK_20(rs.getInt("mtk_20"));
            exportContainerLoading.setMFR_40(rs.getInt("mfr_40"));
            exportContainerLoading.setMOT_40(rs.getInt("mot_40"));
            exportContainerLoading.setGrand_tot(rs.getInt("grand_tot"));
            exportContainerLoading.setTues(rs.getInt("tues"));
            exportContainerLoading.setWeight(rs.getInt("weight"));
            return exportContainerLoading;

        }
    }
}