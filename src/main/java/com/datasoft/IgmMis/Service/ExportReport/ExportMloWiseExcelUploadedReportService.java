package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportContainerBlockReport;
import com.datasoft.IgmMis.Model.ExportReport.ExportMloWiseExcelUploadedReport;
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
public class ExportMloWiseExcelUploadedReportService {
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


        List<ExportMloWiseExcelUploadedReport> resultList=SecondaryDBTemplate.query(sqlQuery,new ExportMloWiseExcelUploadedReportVvdGkey());

        ExportMloWiseExcelUploadedReport exportMloWiseExcelUploadedReport;
        for(int i=0;i<resultList.size();i++){
            exportMloWiseExcelUploadedReport=resultList.get(i);
            vvdgkey=exportMloWiseExcelUploadedReport.getVvd_gkey();
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

    class ExportContainerBlockVoyNo implements RowMapper{

        @Override
        public ExportMloWiseExcelUploadedReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWiseExcelUploadedReport exportMloWiseExcelUploadedReport=new ExportMloWiseExcelUploadedReport();
            exportMloWiseExcelUploadedReport.setVoy_No(rs.getString("Voy_No"));

            return exportMloWiseExcelUploadedReport;
        }
    }

    class ExportMloWiseExcelUploadedReportVvdGkey implements RowMapper {

        @Override
        public ExportMloWiseExcelUploadedReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWiseExcelUploadedReport exportMloWiseExcelUploadedReport=new ExportMloWiseExcelUploadedReport();
            exportMloWiseExcelUploadedReport.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportMloWiseExcelUploadedReport;
        }
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
        List resultList=SecondaryDBTemplate.query(sqlQuery,new ExportMloWiseExcelUploadedVesselInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }



    class ExportMloWiseExcelUploadedVesselInfo implements RowMapper{

        @Override
        public ExportMloWiseExcelUploadedReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWiseExcelUploadedReport exportMloWiseExcelUploadedReport =new ExportMloWiseExcelUploadedReport();
            exportMloWiseExcelUploadedReport.setVsl_name(rs.getString("vsl_name"));
            exportMloWiseExcelUploadedReport.setBerth_op(rs.getString("berth_op"));
            exportMloWiseExcelUploadedReport.setBerth(rs.getString("berth"));
            exportMloWiseExcelUploadedReport.setAta(rs.getDate("ata"));
            exportMloWiseExcelUploadedReport.setAtd(rs.getTimestamp("atd"));

            return exportMloWiseExcelUploadedReport;
        }
    }


    public List getMloWiseExcelUploadedReport(Integer vvdgkey){
        String sqlQuery="\n" +
                "SELECT ctmsmis.mis_exp_unit.gkey,CONCAT(SUBSTRING(ctmsmis.mis_exp_unit.cont_id,1,4),SUBSTRING(ctmsmis.mis_exp_unit.cont_id,5)) AS id,\n" +
                "sparcsn4.ref_equip_type.id AS iso,RIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size,RIGHT(sparcsn4.ref_equip_type.nominal_height,2) AS height,\n" +
                "mis_exp_unit.cont_mlo AS mlo,ctmsmis.mis_exp_unit.craine_id,ctmsmis.mis_exp_unit.seal_no,cont_status AS freight_kind,ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg AS weight,\n" +
                "ctmsmis.mis_exp_unit.coming_from AS coming_from,ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos,ctmsmis.mis_exp_unit.last_update,ref_commodity.short_name,\n" +
                "ctmsmis.mis_exp_unit.user_id,ctmsmis.mis_exp_unit.truck_no,ctmsmis.mis_exp_unit.shipmentType\n" +
                "FROM ctmsmis.mis_exp_unit\n" +
                "LEFT JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped g ON sparcsn4.inv_unit.line_op = g.gkey\n" +
                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "WHERE mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' AND snx_type=0 AND mis_exp_unit.delete_flag='0' ORDER BY mis_exp_unit.cont_mlo,cont_status";

        List resultList=SecondaryDBTemplate.query(sqlQuery,new ExportMloWiseExcelUploadedInformation());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class ExportMloWiseExcelUploadedInformation implements RowMapper{

        @Override
        public ExportMloWiseExcelUploadedReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWiseExcelUploadedReport exportMloWiseExcelUploadedReport =new ExportMloWiseExcelUploadedReport();
            exportMloWiseExcelUploadedReport.setId(rs.getString("id"));
            exportMloWiseExcelUploadedReport.setIso(rs.getString("iso"));
            exportMloWiseExcelUploadedReport.setSize(rs.getString("size"));
            exportMloWiseExcelUploadedReport.setHeight(rs.getString("height"));
            exportMloWiseExcelUploadedReport.setMlo(rs.getString("mlo"));
            exportMloWiseExcelUploadedReport.setFreight_kind(rs.getString("freight_kind"));

            exportMloWiseExcelUploadedReport.setWeight(rs.getString("weight"));
            exportMloWiseExcelUploadedReport.setStowage_pos(rs.getString("stowage_pos"));
            exportMloWiseExcelUploadedReport.setPod(rs.getString("pod"));
            exportMloWiseExcelUploadedReport.setLast_update(rs.getTimestamp("last_update"));
            exportMloWiseExcelUploadedReport.setSeal_no(rs.getString("seal_no"));
            exportMloWiseExcelUploadedReport.setComing_from(rs.getString("coming_from"));
            exportMloWiseExcelUploadedReport.setTruck_no(rs.getString("truck_no"));
            exportMloWiseExcelUploadedReport.setCraine_id(rs.getString("craine_id"));
            exportMloWiseExcelUploadedReport.setShort_name(rs.getString("short_name"));
            exportMloWiseExcelUploadedReport.setUser_id(rs.getString("user_id"));
            return exportMloWiseExcelUploadedReport;
        }
    }


}