package com.datasoft.IgmMis.Service.ExportReport;

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

    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;

    public Integer getvvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportMloWiseExcelUploadedReport> resultList=OracleDbTemplate.query(sqlQuery,new ExportMloWiseExcelUploadedReportVvdGkey());

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

//        sqlQuery="SELECT vsl_vessels.name AS vsl_name,IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,\n" +
//                "IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string03,'')) AS berth_op,IFNULL(sparcsn4.argo_quay.id,'') AS berth,\n" +
//                "DATE(sparcsn4.vsl_vessel_visit_details.published_eta) AS ata,\n" +
//                "sparcsn4.argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
//                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
//                "INNER JOIN sparcsn4.vsl_vessel_berthings ON sparcsn4.vsl_vessel_berthings.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
//                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
//                "INNER JOIN sparcsn4.argo_quay ON sparcsn4.argo_quay.gkey=sparcsn4.vsl_vessel_berthings.quay\n" +
//                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";

        sqlQuery="SELECT vsl_vessels.name AS vsl_name,COALESCE(vsl_vessel_visit_details.flex_string02,vsl_vessel_visit_details.flex_string03) AS berthop,COALESCE(argo_quay.id,'') AS berth,\n" +
        "argo_carrier_visit.ata,argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
        "INNER JOIN argo_carrier_visit ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
        "INNER JOIN vsl_vessel_berthings ON vsl_vessel_berthings.vvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
        "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
        "INNER JOIN argo_quay ON argo_quay.gkey=vsl_vessel_berthings.quay\n" +
        "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";




        List resultList=OracleDbTemplate.query(sqlQuery,new ExportMloWiseExcelUploadedVesselInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }



    class ExportMloWiseExcelUploadedVesselInfo implements RowMapper{

        @Override
        public ExportMloWiseExcelUploadedReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWiseExcelUploadedReport exportMloWiseExcelUploadedReport =new ExportMloWiseExcelUploadedReport();
            exportMloWiseExcelUploadedReport.setVsl_name(rs.getString("vsl_name"));
            exportMloWiseExcelUploadedReport.setBerth_op(rs.getString("berthop"));
            exportMloWiseExcelUploadedReport.setBerth(rs.getString("berth"));
            exportMloWiseExcelUploadedReport.setAta(rs.getDate("ata"));
            exportMloWiseExcelUploadedReport.setAtd(rs.getTimestamp("atd"));

            return exportMloWiseExcelUploadedReport;
        }
    }


    public List getMloWiseExcelUploadedReport(Integer vvdgkey){
//        String sqlQuery="\n" +
//                "SELECT ctmsmis.mis_exp_unit.gkey,CONCAT(SUBSTRING(ctmsmis.mis_exp_unit.cont_id,1,4),SUBSTRING(ctmsmis.mis_exp_unit.cont_id,5)) AS id,\n" +
//                "sparcsn4.ref_equip_type.id AS iso,RIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size,RIGHT(sparcsn4.ref_equip_type.nominal_height,2) AS height,\n" +
//                "mis_exp_unit.cont_mlo AS mlo,ctmsmis.mis_exp_unit.craine_id,ctmsmis.mis_exp_unit.seal_no,cont_status AS freight_kind,ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg AS weight,\n" +
//                "ctmsmis.mis_exp_unit.coming_from AS coming_from,ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos,ctmsmis.mis_exp_unit.last_update,ref_commodity.short_name,\n" +
//                "ctmsmis.mis_exp_unit.user_id,ctmsmis.mis_exp_unit.truck_no,ctmsmis.mis_exp_unit.shipmentType\n" +
//                "FROM ctmsmis.mis_exp_unit\n" +
//                "LEFT JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
//                "INNER JOIN sparcsn4.ref_bizunit_scoped g ON sparcsn4.inv_unit.line_op = g.gkey\n" +
//                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
//                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
//                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
//                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
//                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
//                "WHERE mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' AND snx_type=0 AND mis_exp_unit.delete_flag='0' ORDER BY mis_exp_unit.cont_mlo,cont_status";

        String sqlQuery="\n" +
        "SELECT inv_unit.gkey,inv_unit.id,inv_unit.projected_pod_gkey,inv_unit_fcy_visit.transit_state,SUBSTR(ref_equip_type.nominal_length,-2, LENGTH( ref_equip_type.nominal_length)) AS cont_size, SUBSTR(ref_equip_type.nominal_height, -2, LENGTH( ref_equip_type.nominal_height)) AS height,\n" +
        "ref_bizunit_scoped.name AS mlo_name,inv_unit.seal_nbr1 AS seal_no,vsl_vessel_visit_details.vvd_gkey,\n" +
        "ref_equip_type.id AS iso,ref_bizunit_scoped.id AS mlo,inv_unit.freight_kind,inv_unit.goods_and_ctr_wt_kg as weight,\n" +
        "ref_commodity.short_name,inv_unit.remark as remarks, inv_unit_fcy_visit.ARRIVE_POS_SLOT as stowage_pos, inv_unit_fcy_visit.LAST_POS_NAME,\n" +
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
        "INNER JOIN ref_equip_type ON ref_equip_type.gkey=ref_equipment.eqtyp_gkey \n" +
        "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";

        List resultList=OracleDbTemplate.query(sqlQuery,new ExportMloWiseExcelUploadedInformation());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class ExportMloWiseExcelUploadedInformation implements RowMapper{

        @Override
        public ExportMloWiseExcelUploadedReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportMloWiseExcelUploadedReport exportMloWiseExcelUploadedReport =new ExportMloWiseExcelUploadedReport();
            exportMloWiseExcelUploadedReport.setId(rs.getString("id"));
            exportMloWiseExcelUploadedReport.setIso(rs.getString("iso"));
            exportMloWiseExcelUploadedReport.setSize(rs.getString("cont_size"));
            exportMloWiseExcelUploadedReport.setHeight(rs.getString("height"));
            exportMloWiseExcelUploadedReport.setMlo(rs.getString("mlo"));
            exportMloWiseExcelUploadedReport.setFreight_kind(rs.getString("freight_kind"));

            exportMloWiseExcelUploadedReport.setWeight(rs.getString("weight"));
            exportMloWiseExcelUploadedReport.setStowage_pos(rs.getString("stowage_pos"));
            exportMloWiseExcelUploadedReport.setPod(rs.getString("pod"));
            //exportMloWiseExcelUploadedReport.setLast_update(rs.getTimestamp("last_update"));
            exportMloWiseExcelUploadedReport.setSeal_no(rs.getString("seal_no"));
            exportMloWiseExcelUploadedReport.setComing_from(rs.getString("coming_from"));
            exportMloWiseExcelUploadedReport.setTruck_no(rs.getString("truck_no"));
            //exportMloWiseExcelUploadedReport.setCraine_id(rs.getString("craine_id"));
            exportMloWiseExcelUploadedReport.setShort_name(rs.getString("short_name"));
           // exportMloWiseExcelUploadedReport.setUser_id(rs.getString("user_id"));
            return exportMloWiseExcelUploadedReport;
        }
    }


}