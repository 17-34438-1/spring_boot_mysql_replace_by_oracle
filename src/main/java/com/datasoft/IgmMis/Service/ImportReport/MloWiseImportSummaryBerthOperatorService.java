package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.ImportDischargeAndBanlaceVesselInfoModel;
import com.datasoft.IgmMis.Model.ImportReport.MloWiseImportSummary;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MloWiseImportSummaryBerthOperatorService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    public List getMloWiseImportSummaryVessleInformation(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="SELECT vsl_vessels.name AS vsl_name,IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string03,'')) AS berth_op,IFNULL(sparcsn4.argo_quay.id,'') AS berth,sparcsn4.argo_carrier_visit.ata,sparcsn4.argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessel_berthings ON sparcsn4.vsl_vessel_berthings.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN sparcsn4.argo_quay ON sparcsn4.argo_quay.gkey=sparcsn4.vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=secondaryDBTemplate.query(sqlQuery,new MloWiseImportSummaryBerthOperatorService.ImportContainerVesselInformation());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    public List getMloWiseImportSummaryLoadedList(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="SELECT gkey,mlo,mlo_name,\n" +
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
                "SELECT DISTINCT sparcsn4.inv_unit.gkey AS gkey, \n" +
                " ref_bizunit_scoped.id AS mlo, \n" +
                " ref_bizunit_scoped.name  AS mlo_name,\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS D_20, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS D_40, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='96' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS H_40, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '45' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='96' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS H_45,\n" +
                "\n" +
                "\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS R_20, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS RH_40,\n" +
                "\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS OT_20,\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS OT_40,\n" +
                "\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS FR_20,\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS FR_40,\n" +
                "\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind IN ('FCL','LCL') AND sparcsn4.ref_equip_type.iso_group IN ('TN','TD','TG')  THEN 1  \n" +
                "ELSE NULL END) AS TK_20,\n" +
                "\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MD_20, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MD_40, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='96' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MH_40, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '45' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='96' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MH_45,\n" +
                "\n" +
                "\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS MR_20, \n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS MRH_40,\n" +
                "\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS MOT_20,\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS MOT_40,\n" +
                "\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MFR_20,\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '40' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MFR_40,\n" +
                "\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND RIGHT(sparcsn4.ref_equip_type.nominal_height,2) ='86' AND freight_kind ='MTY' AND sparcsn4.ref_equip_type.iso_group IN ('TN','TD','TG')  THEN 1  \n" +
                "ELSE NULL END) AS MTK_20,\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2) IN('20','40','45','42')  THEN 1 ELSE NULL END) AS grand_tot,\n" +
                "(CASE WHEN  RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20  THEN 1 ELSE 2 END) AS tues     \n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN  sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey \n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit  ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON  sparcsn4.inv_unit_fcy_visit.actual_ib_cv=sparcsn4.argo_carrier_visit.gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.inv_goods  ON sparcsn4.inv_goods.gkey = inv_unit.goods\n" +
                "LEFT JOIN sparcsn4.ref_bizunit_scoped  ON sparcsn4.ref_bizunit_scoped.gkey = sparcsn4.inv_goods.consignee_bzu\n" +
                "WHERE  sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"' AND sparcsn4.inv_unit.category='IMPRT' AND ref_bizunit_scoped.id IS NOT NULL\n" +
                ") AS tmp GROUP BY mlo WITH ROLLUP";

        List resultList=secondaryDBTemplate.query(sqlQuery,new MloWiseImportSummaryBerthOperatorService.SummaryLoadedList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class ImportContainerVesselInformation implements RowMapper {

        @Override
        public ImportDischargeAndBanlaceVesselInfoModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportDischargeAndBanlaceVesselInfoModel importDischargeAndBanlaceVesselInfoModel=new ImportDischargeAndBanlaceVesselInfoModel();
            importDischargeAndBanlaceVesselInfoModel.setVsl_name(rs.getString("vsl_name"));
            importDischargeAndBanlaceVesselInfoModel.setBerth_op(rs.getString("berth_op"));
            importDischargeAndBanlaceVesselInfoModel.setBerth(rs.getString("berth"));
            importDischargeAndBanlaceVesselInfoModel.setAta(rs.getString("ata"));
            importDischargeAndBanlaceVesselInfoModel.setAtd(rs.getString("atd"));
            return importDischargeAndBanlaceVesselInfoModel;
        }
    }

    class SummaryLoadedList implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            MloWiseImportSummary mloWiseImportSummary=new MloWiseImportSummary();
            mloWiseImportSummary.setGkey(rs.getString("gkey"));
            mloWiseImportSummary.setMlo(rs.getString("mlo"));
            mloWiseImportSummary.setMlo_name(rs.getString("mlo_name"));
            mloWiseImportSummary.setD_20(rs.getInt("D_20"));
            mloWiseImportSummary.setD_40(rs.getInt("D_40"));
            mloWiseImportSummary.setH_40(rs.getInt("H_40"));
            mloWiseImportSummary.setH_45(rs.getInt("H_45"));
            mloWiseImportSummary.setR_20(rs.getInt("R_20"));
            mloWiseImportSummary.setRH_40(rs.getInt("RH_40"));
            mloWiseImportSummary.setOT_20(rs.getInt("OT_20"));
            mloWiseImportSummary.setOT_40(rs.getInt("OT_40"));
            mloWiseImportSummary.setFR_20(rs.getInt("FR_20"));
            mloWiseImportSummary.setFR_40(rs.getInt("FR_40"));
            mloWiseImportSummary.setTK_20(rs.getInt("TK_20"));
            mloWiseImportSummary.setMD_20(rs.getInt("MD_20"));
            mloWiseImportSummary.setMD_40(rs.getInt("MD_40"));
            mloWiseImportSummary.setMH_40(rs.getInt("MH_40"));
            mloWiseImportSummary.setMH_45(rs.getInt("MH_45"));
            mloWiseImportSummary.setMR_20(rs.getInt("MR_20"));
            mloWiseImportSummary.setMRH_40(rs.getInt("MRH_40"));
            mloWiseImportSummary.setMOT_20(rs.getInt("MOT_20"));
            mloWiseImportSummary.setMOT_40(rs.getInt("MOT_40"));
            mloWiseImportSummary.setMFR_20(rs.getInt("MFR_20"));
            mloWiseImportSummary.setMFR_40(rs.getInt("MFR_40"));
            mloWiseImportSummary.setMTK_20(rs.getInt("MTK_20"));
            mloWiseImportSummary.setGrand_tot(rs.getInt("grand_tot"));
            mloWiseImportSummary.setTues(rs.getInt("tues"));

            return mloWiseImportSummary;
        }
    }
}
