package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportContainerBlockReport;
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
public class ExportContainerBlockReportService {
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
        List<ExportContainerBlockReport> resultList=OracleDbTemplate.query(sqlQuery,new ExportContainerBlockVvdGkey());

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

        List resultList=primaryDBTemplate.query(sqlQuery,new ExportContainerBlockVoyNo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());

        return listAll;
    }

    public List getVessleInformation(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="SELECT vsl_vessels.name AS vsl_name,COALESCE(vsl_vessel_visit_details.flex_string02,vsl_vessel_visit_details.flex_string03) AS berth_op,COALESCE(argo_quay.id,'') AS berth,\n" +
                "argo_carrier_visit.ata,argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessel_berthings ON vsl_vessel_berthings.vvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN argo_quay ON argo_quay.gkey=vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=OracleDbTemplate.query(sqlQuery,new ExportContainerBlockReportInformation());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public  List getContainerBalanceList(Integer vvdgkey){
        List newList=new ArrayList<>();
        String sqlQuery="";

//        sqlQuery="SELECT sparcsn4.ref_bizunit_scoped.id AS mlo,sparcsn4.ref_bizunit_scoped.name AS mlo_name,sparcsn4.inv_unit.id AS contNo,sparcsn4.ref_equip_type.id AS iso,\n" +
//                "sparcsn4.inv_unit.freight_kind AS contStatus,sparcsn4.inv_unit.goods_and_ctr_wt_kg AS weight,sparcsn4.inv_unit.remark AS remarks,ref_commodity.short_name AS commodity,\n" +
//                "ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos,ctmsmis.mis_exp_unit.user_id,ctmsmis.mis_exp_unit.coming_from\n" +
//                "FROM sparcsn4.vsl_vessel_visit_details \n" +
//                "LEFT JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
//                "LEFT JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.actual_ob_cv = sparcsn4.argo_carrier_visit.gkey\n" +
//                "LEFT JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
//                "INNER JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
//                "LEFT JOIN sparcsn4.ref_bizunit_scoped  ON sparcsn4.inv_unit.line_op = sparcsn4.ref_bizunit_scoped.gkey \n" +
//                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
//                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
//                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
//                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
//                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
//                "INNER JOIN ctmsmis.mis_exp_unit ON sparcsn4.inv_unit.id=ctmsmis.mis_exp_unit.cont_id\n" +
//                "WHERE sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";

                sqlQuery="\n" +
                        "SELECT inv_unit.gkey,inv_unit.id AS contNo,inv_unit.projected_pod_gkey,inv_unit_fcy_visit.transit_state,SUBSTR(ref_equip_type.nominal_length,-2, LENGTH( ref_equip_type.nominal_length)) AS cont_size, SUBSTR(ref_equip_type.nominal_height, -2, LENGTH( ref_equip_type.nominal_height)) AS height,\n" +
                        "ref_bizunit_scoped.name AS mlo_name,inv_unit.seal_nbr1 AS sealno, vsl_vessel_visit_details.ib_vyg,vsl_vessel_visit_details.vvd_gkey,\n" +
                        "ref_equip_type.id AS iso,ref_bizunit_scoped.id AS mlo,inv_unit.freight_kind as contStatus,inv_unit.goods_and_ctr_wt_kg AS weight,\n" +
                        "ref_commodity.short_name AS commodity,inv_unit.remark as remarks, inv_unit_fcy_visit.ARRIVE_POS_SLOT as stowage_pos, inv_unit_fcy_visit.LAST_POS_NAME,\n" +
                        "argo_carrier_visit.id AS truck,\n" +
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
                System.out.println("Query:"+sqlQuery);
        List resultList=OracleDbTemplate.query(sqlQuery,new ExportContainerBlockReportList());
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
            ExportContainerBlockReport exportContainerBlockReport=new ExportContainerBlockReport();
            exportContainerBlockReport.setVsl_name(rs.getString("vsl_name"));
            exportContainerBlockReport.setBerth_op(rs.getString("berth_op"));
            exportContainerBlockReport.setBerth(rs.getString("berth"));
            exportContainerBlockReport.setAta(rs.getDate("ata"));
            exportContainerBlockReport.setAtd(rs.getString("atd"));
            return exportContainerBlockReport;
        }
    }
}
