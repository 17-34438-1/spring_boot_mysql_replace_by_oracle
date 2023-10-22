package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportContainerBlockReport;
import org.apache.catalina.valves.rewrite.RewriteMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportContainerBlockReportService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    public Integer getvvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM sparcsn4.vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportContainerBlockReport> resultList=SecondaryDBTemplate.query(sqlQuery,new ExportContainerBlockVvdGkey());

        ExportContainerBlockReport exportContainerBlockReport;
        for(int i=0;i<resultList.size();i++){
            exportContainerBlockReport=resultList.get(i);
            vvdgkey=exportContainerBlockReport.getVvd_gkey();
        }
        return vvdgkey;
    }
    public List getVoyNo(String rotation){
        String sqlQuery="";
        String voyNo="";
        sqlQuery="SELECT Voy_No FROM igm_masters WHERE Import_Rotation_No='"+rotation+"'";
        System.out.println(sqlQuery);
        List resultList=primaryDBTemplate.query(sqlQuery,new ExportContainerBlockVoyNo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());

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
        List resultList=SecondaryDBTemplate.query(sqlQuery,new ExportContainerBlockReportInformation());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public  List getContainerBalanceList(Integer vvdgkey){
        List newList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="SELECT sparcsn4.ref_bizunit_scoped.id AS mlo,sparcsn4.ref_bizunit_scoped.name AS mlo_name,sparcsn4.inv_unit.id AS contNo,sparcsn4.ref_equip_type.id AS iso,\n" +
                "sparcsn4.inv_unit.freight_kind AS contStatus,sparcsn4.inv_unit.goods_and_ctr_wt_kg AS weight,sparcsn4.inv_unit.remark AS remarks,ref_commodity.short_name AS commodity,\n" +
                "ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos,ctmsmis.mis_exp_unit.user_id,ctmsmis.mis_exp_unit.coming_from\n" +
                "FROM sparcsn4.vsl_vessel_visit_details \n" +
                "LEFT JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "LEFT JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.actual_ob_cv = sparcsn4.argo_carrier_visit.gkey\n" +
                "LEFT JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "LEFT JOIN sparcsn4.ref_bizunit_scoped  ON sparcsn4.inv_unit.line_op = sparcsn4.ref_bizunit_scoped.gkey \n" +
                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN ctmsmis.mis_exp_unit ON sparcsn4.inv_unit.id=ctmsmis.mis_exp_unit.cont_id\n" +
                "WHERE sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new ExportContainerBlockReportList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    class  ExportContainerBlockReportList implements RowMapper{
        @Override
        public ExportContainerBlockReport mapRow(ResultSet rs,int rowNum)throws SQLException{
            ExportContainerBlockReport exportContainerBlockReport=new ExportContainerBlockReport();
            exportContainerBlockReport.setMlo(rs.getString("mlo"));
            exportContainerBlockReport.setMlo_name(rs.getString("mlo_name"));
            exportContainerBlockReport.setContNo(rs.getString("contNo"));
            exportContainerBlockReport.setIso(rs.getString("iso"));
            exportContainerBlockReport.setContStatus(rs.getString("contStatus"));
            exportContainerBlockReport.setWeight(rs.getString("weight"));
            exportContainerBlockReport.setRemarks(rs.getString("remarks"));
            exportContainerBlockReport.setCommodity(rs.getString("commodity"));
            exportContainerBlockReport.setPod(rs.getString("pod"));
            exportContainerBlockReport.setStowage_pos(rs.getString("stowage_pos"));
            exportContainerBlockReport.setUser_id(rs.getString("user_id"));
            exportContainerBlockReport.setComing_from(rs.getString("coming_from"));
            return exportContainerBlockReport;
        }
    }
    class ExportContainerBlockVvdGkey implements RowMapper{

        @Override
        public ExportContainerBlockReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerBlockReport exportContainerBlockReport=new ExportContainerBlockReport();
            exportContainerBlockReport.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportContainerBlockReport;
        }
    }

    class ExportContainerBlockVoyNo implements RowMapper{

        @Override
        public ExportContainerBlockReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerBlockReport exportContainerBlockReport=new ExportContainerBlockReport();
            exportContainerBlockReport.setVoy_No(rs.getString("Voy_No"));

            return exportContainerBlockReport;
        }
    }

    class ExportContainerBlockReportInformation implements RowMapper{

        @Override
        public ExportContainerBlockReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerBlockReport importDischargeAndBanlaceVesselInfoModel=new ExportContainerBlockReport();
            importDischargeAndBanlaceVesselInfoModel.setVsl_name(rs.getString("vsl_name"));
            importDischargeAndBanlaceVesselInfoModel.setBerth_op(rs.getString("berth_op"));
            importDischargeAndBanlaceVesselInfoModel.setBerth(rs.getString("berth"));
            importDischargeAndBanlaceVesselInfoModel.setAta(rs.getDate("ata"));
            importDischargeAndBanlaceVesselInfoModel.setAtd(rs.getString("atd"));
            return importDischargeAndBanlaceVesselInfoModel;
        }
    }
}
