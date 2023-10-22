package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.*;
import org.springframework.stereotype.Service;

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
public class ImportContainerDischargeAndBalanceReportService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;


    public Integer getvvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM sparcsn4.vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";
        //System.out.println(sqlQuery);
        List<VvdgKeyContainerDischargeAndBalanceModel> resultList=secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeAndBalanceReportService.ContainerDischargeVvdGkey());
        VvdgKeyContainerDischargeAndBalanceModel vvdgKeyContainerDischargeAndBalanceModel;
        for(int i=0;i<resultList.size();i++){
            vvdgKeyContainerDischargeAndBalanceModel=resultList.get(i);
            vvdgkey=vvdgKeyContainerDischargeAndBalanceModel.getVvdgkey();

        }

        return vvdgkey;
    }
    public List getVoyNo(String rotation){
        String sqlQuery="";
        String voyNo="";
        sqlQuery="SELECT Voy_No FROM igm_masters WHERE Import_Rotation_No='"+rotation+"'";
        //System.out.println(sqlQuery);
        List resultList=primaryDBTemplate.query(sqlQuery,new ImportContainerDischargeAndBalanceReportService.ImportContainerAndBalanceVoyNo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        /*ImportContainerDischargeAndBalanceVoyNoModel importContainerDischargeAndBalanceVoyNoModel;
        for(int i=0;i<resultList.size();i++){
            importContainerDischargeAndBalanceVoyNoModel=resultList.get(i);
            voyNo=importContainerDischargeAndBalanceVoyNoModel.getVoy_No();

        }*/

        return listAll;
    }
    public List getVessleInformation(Integer vvdgkey){
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
        List resultList=secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeAndBalanceReportService.ImportContainerVesselInformation());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    public  List getContainerBalanceList(Integer vvdgkey){
        List newList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="SELECT CONCAT(SUBSTRING(sparcsn4.inv_unit.id,1,4),' ',SUBSTRING(sparcsn4.inv_unit.id,5)) AS id,sparcsn4.inv_unit_fcy_visit.time_in AS fcy_time_in,\n" +
                "sparcsn4.inv_unit_fcy_visit.last_pos_slot AS location,sparcsn4.inv_unit.seal_nbr1 AS sealno,\n" +
                "sparcsn4.ref_equip_type.id AS iso,sparcsn4.ref_bizunit_scoped.id AS mlo,sparcsn4.inv_unit.freight_kind,sparcsn4.inv_unit.goods_and_ctr_wt_kg AS weight,\n" +
                "ref_commodity.short_name,sparcsn4.inv_unit.remark\n" +
                "FROM  sparcsn4.inv_unit \n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ib_cv\n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.inv_unit.line_op\n" +
                "WHERE sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"' AND category='IMPRT' AND sparcsn4.inv_unit_fcy_visit.transit_state='S20_INBOUND'";
        List resultList=secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeAndBalanceReportService.ImportContainerBalanceList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public List getContainerDischargeList(Integer vvdgkey){
        List newList=new ArrayList<>();
        ImportContainerBalanceOnBoardSummaryModel importContainerBalanceOnBoardSummaryModel;
        String sqlQuery="";
        sqlQuery="SELECT CONCAT(SUBSTRING(sparcsn4.inv_unit.id,1,4),' ',SUBSTRING(sparcsn4.inv_unit.id,5)) AS id,sparcsn4.inv_unit_fcy_visit.time_in AS fcy_time_in,\n" +
                "sparcsn4.inv_unit_fcy_visit.last_pos_slot AS location,sparcsn4.inv_unit.seal_nbr1 AS sealno,\n" +
                "sparcsn4.ref_equip_type.id AS iso,sparcsn4.ref_bizunit_scoped.id AS mlo,sparcsn4.inv_unit.freight_kind,sparcsn4.inv_unit.goods_and_ctr_wt_kg AS weight,ref_commodity.short_name,sparcsn4.inv_unit.remark\n" +
                "FROM  sparcsn4.inv_unit \n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ib_cv\n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.inv_unit.line_op\n" +
                "WHERE sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"' AND category='IMPRT' \n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_in IS NOT NULL";
        List resultList= secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeAndBalanceReportService.ImportContainerDischargeList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());

        for(int i=0;i<listAll.size();i++){

        }

        return listAll;

    }
    public List getContainerDischargeSummary(Integer vvdgkey){

        String sqlQuery="";
        sqlQuery="SELECT gkey,\n" +
                "IFNULL(SUM(balance_LD_20),0) AS balance_LD_20,\n" +
                "IFNULL(SUM(balance_LD_40),0) AS balance_LD_40,\n" +
                "IFNULL(SUM(balance_MT_20),0) AS balance_MT_20,\n" +
                "IFNULL(SUM(balance_MT_40),0) AS balance_MT_40,\n" +
                "IFNULL(SUM(balance_LD_tues),0) AS balance_LD_tues,\n" +
                "IFNULL(SUM(balance_MT_tues),0) AS balance_MT_tues\n" +
                " FROM (\n" +
                "SELECT DISTINCT sparcsn4.inv_unit.gkey AS gkey,\n" +
                "(CASE WHEN RIGHT(nominal_length,2) = '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_20, \n" +
                "(CASE WHEN RIGHT(nominal_length,2) > '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_40,\n" +
                "(CASE WHEN RIGHT(nominal_length,2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_20, \n" +
                "(CASE WHEN RIGHT(nominal_length,2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_40, \n" +
                "(CASE WHEN RIGHT(nominal_length,2) = 20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(nominal_length,2) > 20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS balance_LD_tues, \n" +
                "(CASE WHEN RIGHT(nominal_length,2) = 20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(nominal_length,2) > 20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS balance_MT_tues\n" +
                "FROM  sparcsn4.inv_unit \n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ib_cv\n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.inv_unit.line_op\n" +
                "WHERE sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"' AND category='IMPRT' AND sparcsn4.inv_unit_fcy_visit.transit_state !='S20_INBOUND') AS tmp";
        List resultList= secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeAndBalanceReportService.ImportContainerDischargeSummary());
        List listAll = (List) resultList.stream().collect(Collectors.toList());

        return listAll;
    }

    public List getContainerBanlanceOnBoardSummary(Integer vvdgkey){
        ImportContainerBalanceOnBoardSummaryModel importContainerBalanceOnBoardSummaryModel;
        String sqlQuery="";
        sqlQuery="SELECT gkey,\n" +
                "IFNULL(SUM(onboard_LD_20),0) AS onboard_LD_20,\n" +
                "IFNULL(SUM(onboard_LD_40),0) AS onboard_LD_40,\n" +
                "IFNULL(SUM(onboard_MT_20),0) AS onboard_MT_20,\n" +
                "IFNULL(SUM(onboard_MT_40),0) AS onboard_MT_40,\n" +
                "IFNULL(SUM(onboard_LD_tues),0) AS onboard_LD_tues,\n" +
                "IFNULL(SUM(onboard_MT_tues),0) AS onboard_MT_tues\n" +
                " FROM (\n" +
                "SELECT DISTINCT sparcsn4.inv_unit.gkey AS gkey,\n" +
                "(CASE WHEN RIGHT(nominal_length,2) = 'NOM20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_20, \n" +
                "(CASE WHEN RIGHT(nominal_length,2) > '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_40,\n" +
                "(CASE WHEN RIGHT(nominal_length,2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS onboard_MT_20, \n" +
                "(CASE WHEN RIGHT(nominal_length,2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS onboard_MT_40, \n" +
                "(CASE WHEN RIGHT(nominal_length,2)=20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(nominal_length,2)>20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS onboard_LD_tues, \n" +
                "(CASE WHEN RIGHT(nominal_length,2)=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(nominal_length,5)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS onboard_MT_tues\n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ib_cv\n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "WHERE  sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"' AND category='IMPRT' AND sparcsn4.inv_unit_fcy_visit.transit_state ='S20_INBOUND'\n" +
                ") AS tmp";
        List<ImportContainerBalanceOnBoardSummaryModel> resultList= secondaryDBTemplate.query(sqlQuery,new ImportContainerDischargeAndBalanceReportService.ImportContainerBalanceOnBoardSummary());
        List listAll = (List) resultList.stream().collect(Collectors.toList());


        return listAll;
    }

    class ContainerDischargeVvdGkey implements RowMapper{

        @Override
        public VvdgKeyContainerDischargeAndBalanceModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            VvdgKeyContainerDischargeAndBalanceModel vvdgKeyContainerDischargeAndBalanceModel=new VvdgKeyContainerDischargeAndBalanceModel();
            vvdgKeyContainerDischargeAndBalanceModel.setVvdgkey( rs.getInt("vvd_gkey"));

            return vvdgKeyContainerDischargeAndBalanceModel;
        }
    }
    class ImportContainerAndBalanceVoyNo implements RowMapper{

        @Override
        public ImportContainerDischargeAndBalanceVoyNoModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeAndBalanceVoyNoModel importContainerDischargeAndBalanceVoyNoModel=new ImportContainerDischargeAndBalanceVoyNoModel();
            importContainerDischargeAndBalanceVoyNoModel.setVoy_No(rs.getString("Voy_No"));

            return importContainerDischargeAndBalanceVoyNoModel;
        }
    }
    class ImportContainerVesselInformation implements RowMapper{

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
    class ImportContainerBalanceList implements RowMapper{

        @Override
        public ImportContainerDischargeAndBalanceModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeAndBalanceModel importContainerDischargeAndBalanceModel=new ImportContainerDischargeAndBalanceModel();
            importContainerDischargeAndBalanceModel.setId(rs.getString("id"));
            importContainerDischargeAndBalanceModel.setFcy_time_in(rs.getString("fcy_time_in"));
            importContainerDischargeAndBalanceModel.setLocation(rs.getString("location"));
            importContainerDischargeAndBalanceModel.setLocation(rs.getString("sealno"));
            importContainerDischargeAndBalanceModel.setIso(rs.getString("iso"));
            importContainerDischargeAndBalanceModel.setMlo(rs.getString("mlo"));
            importContainerDischargeAndBalanceModel.setFreight_kind(rs.getString("freight_kind"));
            importContainerDischargeAndBalanceModel.setWeight(rs.getFloat("weight"));
            importContainerDischargeAndBalanceModel.setShort_name(rs.getString("short_name"));
            importContainerDischargeAndBalanceModel.setRemark(rs.getString("remark"));
            return importContainerDischargeAndBalanceModel;
        }
    }

    class ImportContainerDischargeList implements  RowMapper{

        @Override
        public ImportContainerDischargeAndBalanceModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeAndBalanceModel importContainerDischargeAndBalanceModel=new ImportContainerDischargeAndBalanceModel();
            importContainerDischargeAndBalanceModel.setId(rs.getString("id"));
            importContainerDischargeAndBalanceModel.setFcy_time_in(rs.getString("fcy_time_in"));
            importContainerDischargeAndBalanceModel.setLocation(rs.getString("location"));
            importContainerDischargeAndBalanceModel.setSealno(rs.getString("sealno"));
            importContainerDischargeAndBalanceModel.setIso(rs.getString("iso"));
            importContainerDischargeAndBalanceModel.setMlo(rs.getString("mlo"));
            importContainerDischargeAndBalanceModel.setFreight_kind(rs.getString("freight_kind"));
            importContainerDischargeAndBalanceModel.setWeight(rs.getFloat("weight"));
            importContainerDischargeAndBalanceModel.setShort_name(rs.getString("short_name"));
            importContainerDischargeAndBalanceModel.setRemark(rs.getString("remark"));


            return importContainerDischargeAndBalanceModel;
        }
    }
    class ImportContainerDischargeSummary implements RowMapper{

        @Override
        public ImportContainerDischargeSummaryModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerDischargeSummaryModel importContainerDischargeSummaryModel=new ImportContainerDischargeSummaryModel();
            importContainerDischargeSummaryModel.setGkey(rs.getInt("gkey"));
            importContainerDischargeSummaryModel.setBalance_LD_20(rs.getInt("balance_LD_20"));
            importContainerDischargeSummaryModel.setBalance_LD_40(rs.getInt("balance_LD_40"));
            importContainerDischargeSummaryModel.setBalance_MT_20(rs.getInt("balance_MT_20"));
            importContainerDischargeSummaryModel.setBalance_MT_40(rs.getInt("balance_MT_40"));
            importContainerDischargeSummaryModel.setBalance_LD_tues(rs.getInt("balance_LD_tues"));
            importContainerDischargeSummaryModel.setBalance_MT_tues(rs.getInt("balance_MT_tues"));
            return importContainerDischargeSummaryModel;
        }
    }

    class ImportContainerBalanceOnBoardSummary implements RowMapper{


        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportContainerBalanceOnBoardSummaryModel importContainerBalanceOnBoardSummaryModel= new ImportContainerBalanceOnBoardSummaryModel();
            importContainerBalanceOnBoardSummaryModel.setGkey(rs.getInt("gkey"));
            importContainerBalanceOnBoardSummaryModel.setOnboard_LD_20(rs.getInt("onboard_LD_20"));
            importContainerBalanceOnBoardSummaryModel.setOnboard_LD_40(rs.getInt("onboard_LD_40"));
            importContainerBalanceOnBoardSummaryModel.setOnboard_MT_20(rs.getInt("onboard_MT_20"));
            importContainerBalanceOnBoardSummaryModel.setOnboard_MT_40(rs.getInt("onboard_MT_40"));
            importContainerBalanceOnBoardSummaryModel.setOnboard_LD_tues(rs.getInt("onboard_LD_tues"));
            importContainerBalanceOnBoardSummaryModel.setOnboard_MT_tues(rs.getInt("onboard_MT_tues"));
            return importContainerBalanceOnBoardSummaryModel;
        }
    }
}
