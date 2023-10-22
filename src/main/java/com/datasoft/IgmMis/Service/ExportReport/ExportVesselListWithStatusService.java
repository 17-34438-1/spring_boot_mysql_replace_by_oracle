package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportMloWiseExcelUploadedReport;
import com.datasoft.IgmMis.Model.ExportReport.ExportVesselListWithStatus;
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
public class ExportVesselListWithStatusService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;
    public Integer getVesselListvvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportVesselListWithStatus> resultList=OracleDbTemplate.query(sqlQuery,new VesselListvvdGkey());

        ExportVesselListWithStatus exportVesselListWithStatus;
        for(int i=0;i<resultList.size();i++){
            exportVesselListWithStatus=resultList.get(i);
            vvdgkey=exportVesselListWithStatus.getVvd_gkey();
        }
        return vvdgkey;
    }

    public List getVesselListVoyNo(String rotation){
        String sqlQuery="";
        String voyNo="";
        sqlQuery="SELECT Voy_No FROM igm_masters WHERE Import_Rotation_No='"+rotation+"'";
        System.out.println(sqlQuery);
        List resultList=primaryDBTemplate.query(sqlQuery,new VesselListVoyNo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());

        return listAll;
    }

    class VesselListVoyNo implements RowMapper {

        @Override
        public ExportVesselListWithStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportVesselListWithStatus exportVesselListWithStatus=new ExportVesselListWithStatus();
            exportVesselListWithStatus.setVoy_No(rs.getString("Voy_No"));

            return exportVesselListWithStatus;
        }
    }

    class VesselListvvdGkey implements RowMapper {

        @Override
        public ExportVesselListWithStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportVesselListWithStatus exportVesselListWithStatus=new ExportVesselListWithStatus();
            exportVesselListWithStatus.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportVesselListWithStatus;
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
//

        sqlQuery="SELECT vsl_vessel_visit_details.vvd_gkey,vsl_vessels.name,vsl_vessel_visit_details.ib_vyg,\n" +
                "vsl_vessel_visit_details.ob_vyg,SUBSTR(argo_carrier_visit.phase, 3, LENGTH( argo_carrier_visit.phase)) AS phase_num,SUBSTR(argo_carrier_visit.phase,3) AS phase_str,argo_visit_details.eta,\n" +
                "argo_visit_details.etd,argo_carrier_visit.ata,argo_carrier_visit.atd,ref_bizunit_scoped.id AS agent,\n" +
                "COALESCE(vsl_vessel_visit_details.flex_string02,vsl_vessel_visit_details.flex_string03) AS berthop\n" +
                "FROM argo_carrier_visit\n" +
                "INNER JOIN argo_visit_details ON argo_visit_details.gkey=argo_carrier_visit.cvcvd_gkey\n" +
                "INNER JOIN vsl_vessel_visit_details ON vsl_vessel_visit_details.vvd_gkey=argo_visit_details.gkey\n" +
                "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN ref_bizunit_scoped ON ref_bizunit_scoped.gkey=vsl_vessel_visit_details.bizu_gkey\n" +
                "WHERE vsl_vessel_visit_details.ib_vyg = '"+vvdgkey+"' ORDER BY argo_carrier_visit.phase";


        List resultList=OracleDbTemplate.query(sqlQuery,new VessleInformation());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    public List getVessleListStatusWithRotation(String rotation){
        String sqlQuery="";
//        sqlQuery="\n" +
//                "SELECT sparcsn4.vsl_vessel_visit_details.vvd_gkey,sparcsn4.vsl_vessels.name,sparcsn4.vsl_vessel_visit_details.ib_vyg,sparcsn4.vsl_vessel_visit_details.ob_vyg,\n" +
//                "LEFT(sparcsn4.argo_carrier_visit.phase,2) AS phase_num,SUBSTR(sparcsn4.argo_carrier_visit.phase,3) AS phase_str,sparcsn4.argo_visit_details.eta,\n" +
//                "sparcsn4.argo_visit_details.etd,sparcsn4.argo_carrier_visit.ata,\n" +
//                "sparcsn4.argo_carrier_visit.atd,sparcsn4.ref_bizunit_scoped.id AS agent,\n" +
//                "IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,sparcsn4.vsl_vessel_visit_details.flex_string03) AS berthop\n" +
//                "FROM sparcsn4.argo_carrier_visit\n" +
//                "INNER JOIN sparcsn4.argo_visit_details ON sparcsn4.argo_visit_details.gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
//                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_visit_details.gkey\n" +
//                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
//                "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.vsl_vessel_visit_details.bizu_gkey\n" +
//                "WHERE sparcsn4.vsl_vessel_visit_details.ib_vyg = '"+rotation+"'\n" +
//                "ORDER BY sparcsn4.argo_carrier_visit.phase\n";


                sqlQuery="";

        List resultList=SecondaryDBTemplate.query(sqlQuery,new VessleListStatusWithRotation());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public List getVessleListWithStatusInfo(){
        String sqlQuery="";
        sqlQuery="SELECT sparcsn4.vsl_vessel_visit_details.vvd_gkey,sparcsn4.vsl_vessels.name,sparcsn4.vsl_vessel_visit_details.ib_vyg,sparcsn4.vsl_vessel_visit_details.ob_vyg,\n" +
                "LEFT(sparcsn4.argo_carrier_visit.phase,2) AS phase_num,SUBSTR(sparcsn4.argo_carrier_visit.phase,3) AS phase_str,sparcsn4.argo_visit_details.eta,\n" +
                "sparcsn4.argo_visit_details.etd,sparcsn4.argo_carrier_visit.ata,\n" +
                "sparcsn4.argo_carrier_visit.atd,sparcsn4.ref_bizunit_scoped.id AS agent,\n" +
                "IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,sparcsn4.vsl_vessel_visit_details.flex_string03) AS berthop\n" +
                "FROM sparcsn4.argo_carrier_visit\n" +
                "INNER JOIN sparcsn4.argo_visit_details ON sparcsn4.argo_visit_details.gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_visit_details.gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.vsl_vessel_visit_details.bizu_gkey\n" +
                "WHERE sparcsn4.argo_carrier_visit.phase IN ('20INBOUND','30ARRIVED','40WORKING','50COMPLETE','60DEPARTED')\n" +
                "ORDER BY sparcsn4.argo_carrier_visit.phase";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new VessleListWithStatusInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    public List getVessleListWithStatusInfoForExportUploadReport(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="\n" +
                "SELECT ctmsmis.mis_exp_unit.gkey,CONCAT(SUBSTRING(ctmsmis.mis_exp_unit.cont_id,1,4),SUBSTRING(ctmsmis.mis_exp_unit.cont_id,5)) AS id,sparcsn4.ref_equip_type.id AS iso,RIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size,RIGHT(sparcsn4.ref_equip_type.nominal_height,2) AS height,mis_exp_unit. cont_mlo,ctmsmis.mis_exp_unit.craine_id,ctmsmis.mis_exp_unit.seal_no,cont_status AS freight_kind,ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg AS weight,ctmsmis.mis_exp_unit.coming_from AS coming_from,ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos,ctmsmis.mis_exp_unit.last_update,ref_commodity.short_name,ctmsmis.mis_exp_unit.user_id,ctmsmis.mis_exp_unit.truck_no\n" +
                "FROM ctmsmis.mis_exp_unit\n" +
                "LEFT JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped g ON sparcsn4.inv_unit.line_op = g.gkey\n" +
                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "WHERE mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' AND snx_type=0 AND mis_exp_unit.delete_flag='0' ORDER BY mis_exp_unit.cont_mlo,cont_status\t\t\t\t\n" +
                "\n";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new VessleListWithStatusInfoForExportUploadReport());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public List getVessleListWithStatusForMloWiseExportSummary(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="\n" +
                "SELECT gkey,mlo,(SELECT NAME FROM sparcsn4.ref_bizunit_scoped WHERE id=mlo AND NAME !='NULL' LIMIT 1) AS mlo_name,\n" +
                "IFNULL(SUM(D_20),0) AS D_20,\n" +
                "IFNULL(SUM(D_40),0) AS D_40,\n" +
                "IFNULL(SUM(H_40),0) AS H_40,\n" +
                "IFNULL(SUM(H_45),0) AS H_45,\n" +
                "\n" +
                "IFNULL(SUM(R_20),0) AS R_20,\n" +
                "IFNULL(SUM(RH_40),0) AS RH_40,\n" +
                "\n" +
                "IFNULL(SUM(OT_20),0) AS OT_20,\n" +
                "IFNULL(SUM(OT_40),0) AS OT_40,\n" +
                "\n" +
                "IFNULL(SUM(FR_20),0) AS FR_20,\n" +
                "IFNULL(SUM(FR_40),0) AS FR_40,\n" +
                "\n" +
                "IFNULL(SUM(TK_20),0) AS TK_20,\n" +
                "\n" +
                "IFNULL(SUM(MD_20),0) AS MD_20,\n" +
                "IFNULL(SUM(MD_40),0) AS MD_40,\n" +
                "IFNULL(SUM(MH_40),0) AS MH_40,\n" +
                "IFNULL(SUM(MH_45),0) AS MH_45,\n" +
                "\n" +
                "IFNULL(SUM(MR_20),0) AS MR_20,\n" +
                "IFNULL(SUM(MRH_40),0) AS MRH_40,\n" +
                "\n" +
                "IFNULL(SUM(MOT_20),0) AS MOT_20,\n" +
                "IFNULL(SUM(MOT_40),0) AS MOT_40,\n" +
                "\n" +
                "IFNULL(SUM(MFR_20),0) AS MFR_20,\n" +
                "IFNULL(SUM(MFR_40),0) AS MFR_40,\n" +
                "\n" +
                "IFNULL(SUM(MTK_20),0) AS MTK_20,\n" +
                "\n" +
                "IFNULL(SUM(grand_tot),0) AS grand_tot,\n" +
                "IFNULL(SUM(tues),0) AS tues\n" +
                " FROM (\n" +
                "SELECT DISTINCT ctmsmis.mis_exp_unit.gkey AS gkey,cont_mlo AS mlo,\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS D_20, \n" +
                "(CASE WHEN cont_size = '40' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS D_40, \n" +
                "(CASE WHEN cont_size = '40' AND cont_height='96' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS H_40, \n" +
                "(CASE WHEN cont_size = '45' AND cont_height='96' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1\n" +
                "\t  WHEN cont_size = '42' AND cont_height='90' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1\n" +
                "ELSE NULL END) AS H_45,\n" +
                "\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS R_20, \n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS RH_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS OT_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS OT_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS FR_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS FR_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('TN','TD','TG')  THEN 1  \n" +
                "ELSE NULL END) AS TK_20,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MD_20, \n" +
                "(CASE WHEN cont_size = '40' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MD_40, \n" +
                "(CASE WHEN cont_size = '40' AND cont_height='96' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MH_40, \n" +
                "(CASE WHEN cont_size = '45' AND cont_height='96' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MH_45,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS MR_20, \n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS MRH_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS MOT_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS MOT_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MFR_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MFR_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('TN','TD','TG')  THEN 1  \n" +
                "ELSE NULL END) AS MTK_20,\n" +
                "(CASE WHEN cont_size IN('20','40','45','42')  THEN 1 ELSE NULL END) AS grand_tot,\n" +
                "(CASE WHEN cont_size=20  THEN 1 ELSE 2 END) AS tues     \n" +
                "FROM ctmsmis.mis_exp_unit\n" +
                "LEFT JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey\n" +
                " \n" +
                "WHERE  mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' AND snx_type=0 AND cont_mlo IS NOT NULL\n" +
                ") AS tmp GROUP BY mlo WITH ROLLUP\n";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new VessleListWithStatusForMloWiseExportSummary());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class VessleListWithStatusInfo implements RowMapper{

        @Override
        public ExportVesselListWithStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportVesselListWithStatus exportVesselListWithStatus =new ExportVesselListWithStatus();
            exportVesselListWithStatus.setName(rs.getString("name"));
            exportVesselListWithStatus.setIb_vyg(rs.getString("ib_vyg"));
            exportVesselListWithStatus.setOb_vyg(rs.getString("ob_vyg"));
            exportVesselListWithStatus.setPhase_str(rs.getString("phase_str"));
            exportVesselListWithStatus.setEta(rs.getTimestamp("eta"));
            exportVesselListWithStatus.setEtd(rs.getTimestamp("etd"));
            exportVesselListWithStatus.setAta(rs.getTimestamp("ata"));
            exportVesselListWithStatus.setAtd(rs.getTimestamp("atd"));
            exportVesselListWithStatus.setAgent(rs.getString("agent"));
            exportVesselListWithStatus.setBerthop(rs.getString("berthop"));

            return exportVesselListWithStatus;
        }
    }


    class VessleListWithStatusForMloWiseExportSummary implements RowMapper{

        @Override
        public ExportVesselListWithStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportVesselListWithStatus exportVesselListWithStatus =new ExportVesselListWithStatus();
            exportVesselListWithStatus.setMlo_name(rs.getString("mlo_name"));
            exportVesselListWithStatus.setMlo(rs.getString("mlo"));
            exportVesselListWithStatus.setD_20(rs.getInt("D_20"));
            exportVesselListWithStatus.setD_40(rs.getInt("D_40"));
            exportVesselListWithStatus.setH_40(rs.getInt("H_40"));
            exportVesselListWithStatus.setH_45(rs.getInt("H_45"));
            exportVesselListWithStatus.setRH_40(rs.getInt("RH_40"));
            exportVesselListWithStatus.setR_20(rs.getInt("R_20"));
            exportVesselListWithStatus.setOT_20(rs.getInt("OT_20"));
            exportVesselListWithStatus.setFR_20(rs.getInt("FR_20"));
            exportVesselListWithStatus.setTK_20(rs.getInt("TK_20"));
            exportVesselListWithStatus.setFR_40(rs.getInt("FR_40"));
            exportVesselListWithStatus.setOT_40(rs.getInt("OT_40"));
            exportVesselListWithStatus.setMD_20(rs.getInt("MD_20"));
            exportVesselListWithStatus.setMD_40(rs.getInt("MD_40"));
            exportVesselListWithStatus.setMH_40(rs.getInt("MH_40"));
            exportVesselListWithStatus.setMH_45(rs.getInt("MH_45"));
            exportVesselListWithStatus.setMRH_40(rs.getInt("MRH_40"));
            exportVesselListWithStatus.setMR_20(rs.getInt("MR_20"));
            exportVesselListWithStatus.setMOT_20(rs.getInt("MOT_20"));
            exportVesselListWithStatus.setMFR_20(rs.getInt("MFR_20"));
            exportVesselListWithStatus.setMTK_20(rs.getInt("MTK_20"));
            exportVesselListWithStatus.setMFR_40(rs.getInt("MFR_40"));
            exportVesselListWithStatus.setMOT_40(rs.getInt("MOT_40"));
            exportVesselListWithStatus.setGrand_tot(rs.getInt("grand_tot"));
            exportVesselListWithStatus.setTues(rs.getInt("tues"));

            return exportVesselListWithStatus;
        }
    }
    class VessleListWithStatusInfoForExportUploadReport implements RowMapper{

        @Override
        public ExportVesselListWithStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportVesselListWithStatus exportVesselListWithStatus =new ExportVesselListWithStatus();
            exportVesselListWithStatus.setId(rs.getString("id"));
            exportVesselListWithStatus.setIso(rs.getString("iso"));
            exportVesselListWithStatus.setSize(rs.getString("size"));
            exportVesselListWithStatus.setHeight(rs.getString("height"));
            exportVesselListWithStatus.setCont_mlo(rs.getString("cont_mlo"));
            exportVesselListWithStatus.setFreight_kind(rs.getString("freight_kind"));
            exportVesselListWithStatus.setWeight(rs.getString("weight"));
            exportVesselListWithStatus.setStowage_pos(rs.getString("stowage_pos"));
            exportVesselListWithStatus.setPod(rs.getString("pod"));
            exportVesselListWithStatus.setLast_update(rs.getTimestamp("last_update"));
            exportVesselListWithStatus.setSeal_no(rs.getString("seal_no"));
            exportVesselListWithStatus.setComing_from(rs.getString("coming_from"));
            exportVesselListWithStatus.setTruck_no(rs.getString("truck_no"));
            exportVesselListWithStatus.setCraine_id(rs.getString("craine_id"));
            exportVesselListWithStatus.setShort_name(rs.getString("short_name"));
            exportVesselListWithStatus.setUser_id(rs.getString("user_id"));

            return exportVesselListWithStatus;
        }
    }
    class VessleListStatusWithRotation implements RowMapper{

        @Override
        public ExportVesselListWithStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportVesselListWithStatus exportVesselListWithStatus =new ExportVesselListWithStatus();

            exportVesselListWithStatus.setIb_vyg(rs.getString("ib_vyg"));
            exportVesselListWithStatus.setOb_vyg(rs.getString("ob_vyg"));
            exportVesselListWithStatus.setPhase_str(rs.getString("phase_str"));
            exportVesselListWithStatus.setEta(rs.getTimestamp("eta"));
            exportVesselListWithStatus.setEtd(rs.getTimestamp("etd"));
            exportVesselListWithStatus.setAta(rs.getTimestamp("ata"));
            exportVesselListWithStatus.setAtd(rs.getTimestamp("atd"));
            exportVesselListWithStatus.setAgent(rs.getString("agent"));
            exportVesselListWithStatus.setBerthop(rs.getString("berthop"));
            return exportVesselListWithStatus;
        }
    }
    class VessleInformation implements RowMapper{

        @Override
        public ExportVesselListWithStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportVesselListWithStatus exportVesselListWithStatus =new ExportVesselListWithStatus();
            exportVesselListWithStatus.setVsl_name(rs.getString("vsl_name"));
            exportVesselListWithStatus.setBerth_op(rs.getString("berth_op"));
            exportVesselListWithStatus.setBerth(rs.getString("berth"));
            exportVesselListWithStatus.setAta(rs.getDate("ata"));
            exportVesselListWithStatus.setAtd(rs.getTimestamp("atd"));

            return exportVesselListWithStatus;
        }
    }
}