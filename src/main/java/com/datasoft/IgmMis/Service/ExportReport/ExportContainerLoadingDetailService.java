package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportContainerBlockReport;
import com.datasoft.IgmMis.Model.ExportReport.ExportContainerLoading;
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
public class ExportContainerLoadingDetailService {



    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;


    public Integer get_Gkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportContainerLoading> resultList=OracleDbTemplate.query(sqlQuery,new ExportContainerLoadingVvdGkey());

        ExportContainerLoading exportContainerLoading;
        for(int i=0;i<resultList.size();i++){
            exportContainerLoading=resultList.get(i);
            vvdgkey=exportContainerLoading.getVvd_gkey();
        }
        return vvdgkey;
    }
    public List get_container_Loading_Details_vessel_info(Integer vvdgkey){
        String sqlQuery="";

        sqlQuery="SELECT vsl_vessels.name as vsl_name,COALESCE(vsl_vessel_visit_details.flex_string02,vsl_vessel_visit_details.flex_string03) AS berth_op,COALESCE(argo_quay.id,'') AS berth,\n" +
                "argo_carrier_visit.ata,argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessel_berthings ON vsl_vessel_berthings.vvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN argo_quay ON argo_quay.gkey=vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=OracleDbTemplate.query(sqlQuery,new ExportContainerLoadingReportVesselInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    public  List getContainerLoadedDetails(Integer vvdgkey){
        List newList=new ArrayList<>();
        String sqlQuery="";

        sqlQuery="\n" +
                "SELECT vsl_vessel_visit_details.vvd_gkey, CONCAT(CONCAT(substr(inv_unit.id,1,4),' '),substr(inv_unit.id,5)) AS id,inv_unit_fcy_visit.time_in AS fcy_time_in,\n" +
                "SUBSTR(ref_equip_type.nominal_length,-2, LENGTH( ref_equip_type.nominal_length)) AS cont_size, SUBSTR(ref_equip_type.nominal_height, -2, LENGTH( ref_equip_type.nominal_height)) AS height,inv_unit.seal_nbr1 AS seal_no,\n" +
                "ref_equip_type.id AS iso,ref_bizunit_scoped.id AS mlo,inv_unit.freight_kind,inv_unit.goods_and_ctr_wt_kg AS goods_and_ctr_wt_kg,\n" +
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
                "WHERE  vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'\n";
        System.out.println("Query:"+sqlQuery);
        List resultList=OracleDbTemplate.query(sqlQuery,new ExportContainerLoadedDetailList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    class  ExportContainerLoadedDetailList implements RowMapper {
        @Override
        public ExportContainerBlockReport mapRow(ResultSet rs, int rowNum)throws SQLException {
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
    class ExportContainerLoadingVvdGkey implements RowMapper{

        @Override
        public ExportContainerBlockReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerBlockReport exportContainerBlockReport=new ExportContainerBlockReport();
            exportContainerBlockReport.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportContainerBlockReport;
        }
    }
    class ExportContainerLoadingReportVesselInfo implements RowMapper{

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
