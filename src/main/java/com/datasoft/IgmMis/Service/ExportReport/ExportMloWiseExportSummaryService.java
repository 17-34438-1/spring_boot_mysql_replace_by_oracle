package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportReportMloWiseExportSummary;
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
public class ExportMloWiseExportSummaryService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;

    public Integer get_vvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM sparcsn4.vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportReportMloWiseExportSummary> resultList=SecondaryDBTemplate.query(sqlQuery,new ExportMloWiseExportSummaryVvdGkey());

        ExportReportMloWiseExportSummary exportReportMloWiseExportSummary;
        for(int i=0;i<resultList.size();i++){
            exportReportMloWiseExportSummary=resultList.get(i);
            vvdgkey=exportReportMloWiseExportSummary.getVvd_gkey();
        }
        return vvdgkey;
    }


    public List getMloWiseSummaryVoyNo(String rotation){
        String sqlQuery="";
        String voyNo="";
        sqlQuery="SELECT Voy_No FROM igm_masters WHERE Import_Rotation_No='"+rotation+"'";
        System.out.println(sqlQuery);
        List resultList=primaryDBTemplate.query(sqlQuery,new ExportMloWiseExportSummaryVoyNo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());

        return listAll;
    }

    class ExportMloWiseExportSummaryVoyNo implements RowMapper{

        @Override
        public ExportReportMloWiseExportSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportReportMloWiseExportSummary exportReportMloWiseExportSummary=new ExportReportMloWiseExportSummary();
            exportReportMloWiseExportSummary.setVoy_No(rs.getString("Voy_No"));

            return exportReportMloWiseExportSummary;
        }
    }


    public List get_Container_Vessel_Info(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="SELECT vsl_vessels.name AS vsl_name,IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,\n" +
                "IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string03,'')) AS berth_op,IFNULL(sparcsn4.argo_quay.id,'') AS berth,\n" +
                "DATE(sparcsn4.vsl_vessel_visit_details.published_eta) AS ata,\n" +
                "sparcsn4.argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessel_berthings ON sparcsn4.vsl_vessel_berthings.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN sparcsn4.argo_quay ON sparcsn4.argo_quay.gkey=sparcsn4.vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new ExportMloWiseExportSummaryVesselInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public List getMloWiseExportSummary(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="select gkey,mlo,(select name from sparcsn4.ref_bizunit_scoped where id=mlo and name !='NULL' limit 1) as mlo_name,\n" +
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
                " from (\n" +
                "select distinct ctmsmis.mis_exp_unit.gkey as gkey,cont_mlo as mlo,\n" +
                "(CASE WHEN cont_size = '20' and cont_height='86' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup not in ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS D_20, \n" +
                "(CASE WHEN cont_size = '40' and cont_height='86' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup not in ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS D_40, \n" +
                "(CASE WHEN cont_size = '40' and cont_height='96' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup not in ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS H_40, \n" +
                "(CASE WHEN cont_size = '45' and cont_height='96' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup not in ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1\n" +
                "\t  WHEN cont_size = '42' and cont_height='90' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup not in ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1\n" +
                "ELSE NULL END) AS H_45,\n" +
                "\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' and cont_height='86' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup in ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS R_20, \n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup in ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS RH_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' and cont_height='86' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup in ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS OT_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup in ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS OT_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' and cont_height='86' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup in ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS FR_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup in ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS FR_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' and cont_height='86' AND mis_exp_unit.cont_status in ('FCL','LCL') AND isoGroup in ('TN','TD','TG')  THEN 1  \n" +
                "ELSE NULL END) AS TK_20,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' and cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup not in ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MD_20, \n" +
                "(CASE WHEN cont_size = '40' and cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup not in ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MD_40, \n" +
                "(CASE WHEN cont_size = '40' and cont_height='96' AND mis_exp_unit.cont_status ='MTY' AND isoGroup not in ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MH_40, \n" +
                "(CASE WHEN cont_size = '45' and cont_height='96' AND mis_exp_unit.cont_status ='MTY' AND isoGroup not in ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MH_45,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' and cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup in ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS MR_20, \n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup in ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS MRH_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' and cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup in ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS MOT_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup in ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS MOT_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' and cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup in ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MFR_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup in ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MFR_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' and cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup in ('TN','TD','TG')  THEN 1  \n" +
                "ELSE NULL END) AS MTK_20,\n" +
                "(CASE WHEN cont_size in('20','40','45','42')  THEN 1 ELSE NULL END) AS grand_tot,\n" +
                "(CASE WHEN cont_size=20  THEN 1 ELSE 2 END) AS tues     \n" +
                "FROM ctmsmis.mis_exp_unit\n" +
                "left join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey\n" +
                " \n" +
                "where  mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' and snx_type=0 and cont_mlo is not null\n" +
                ") as tmp group by mlo WITH ROLLUP";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new ExportReportMloWiseExportSummaryDetails());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    class ExportMloWiseExportSummaryVvdGkey implements RowMapper {

        @Override
        public ExportReportMloWiseExportSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportReportMloWiseExportSummary exportReportMloWiseExportSummary=new ExportReportMloWiseExportSummary();
            exportReportMloWiseExportSummary.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportReportMloWiseExportSummary;
        }
    }
    class ExportReportMloWiseExportSummaryDetails implements RowMapper{
        @Override
        public ExportReportMloWiseExportSummary mapRow(ResultSet rs, int rowNum)throws SQLException{
            ExportReportMloWiseExportSummary exportReportMloWiseExportSummaryModel=new ExportReportMloWiseExportSummary();
            exportReportMloWiseExportSummaryModel.setMlo_name(rs.getString("mlo_name"));
            exportReportMloWiseExportSummaryModel.setMlo(rs.getString("mlo"));

            exportReportMloWiseExportSummaryModel.setD_20(rs.getInt("D_20"));
            exportReportMloWiseExportSummaryModel.setD_40(rs.getInt("D_40"));
            exportReportMloWiseExportSummaryModel.setH_40(rs.getInt("H_40"));
            exportReportMloWiseExportSummaryModel.setH_45(rs.getInt("H_45"));
            exportReportMloWiseExportSummaryModel.setRH_40(rs.getInt("RH_40"));
            exportReportMloWiseExportSummaryModel.setR_20(rs.getInt("R_20"));
            exportReportMloWiseExportSummaryModel.setOT_20(rs.getInt("OT_20"));
            exportReportMloWiseExportSummaryModel.setFR_20(rs.getInt("FR_20"));

            exportReportMloWiseExportSummaryModel.setTK_20(rs.getInt("TK_20"));
            exportReportMloWiseExportSummaryModel.setFR_40(rs.getInt("FR_40"));
            exportReportMloWiseExportSummaryModel.setOT_40(rs.getInt("OT_40"));
            exportReportMloWiseExportSummaryModel.setMD_20(rs.getInt("MD_20"));
            exportReportMloWiseExportSummaryModel.setMD_40(rs.getInt("MD_40"));

            exportReportMloWiseExportSummaryModel.setMH_40(rs.getInt("MH_40"));

            exportReportMloWiseExportSummaryModel.setMH_45(rs.getInt("MH_45"));
            exportReportMloWiseExportSummaryModel.setMRH_40(rs.getInt("MRH_40"));
            exportReportMloWiseExportSummaryModel.setMR_20(rs.getInt("MR_20"));
            exportReportMloWiseExportSummaryModel.setMOT_20(rs.getInt("MOT_20"));
            exportReportMloWiseExportSummaryModel.setMFR_20(rs.getInt("MFR_20"));

            exportReportMloWiseExportSummaryModel.setMTK_20(rs.getInt("MTK_20"));
            exportReportMloWiseExportSummaryModel.setMFR_40(rs.getInt("MFR_40"));
            exportReportMloWiseExportSummaryModel.setMOT_40(rs.getInt("MOT_40"));
            exportReportMloWiseExportSummaryModel.setGrand_tot(rs.getInt("grand_tot"));
            exportReportMloWiseExportSummaryModel.setTues(rs.getInt("tues"));
            return exportReportMloWiseExportSummaryModel;
        }
    }

    class ExportMloWiseExportSummaryVesselInfo implements RowMapper{

        @Override
        public ExportReportMloWiseExportSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportReportMloWiseExportSummary exportReportMloWiseExportSummary=new ExportReportMloWiseExportSummary();
            exportReportMloWiseExportSummary.setVsl_name(rs.getString("vsl_name"));
            exportReportMloWiseExportSummary.setBerth_op(rs.getString("berth_op"));
            exportReportMloWiseExportSummary.setBerth(rs.getString("berth"));
            exportReportMloWiseExportSummary.setAta(rs.getDate("ata"));
            exportReportMloWiseExportSummary.setAtd(rs.getTimestamp("atd"));
            return exportReportMloWiseExportSummary;
        }
    }
}