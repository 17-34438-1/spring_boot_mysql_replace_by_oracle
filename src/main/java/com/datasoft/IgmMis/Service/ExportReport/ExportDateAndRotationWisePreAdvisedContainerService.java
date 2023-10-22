package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportDateAndRotationWisePreAdvisedContainer;
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
public class ExportDateAndRotationWisePreAdvisedContainerService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;

    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;



    public List getDateAndRotationWisePreAdvisedContainer(String rotation,String fromdate){

//        String sqlQuery="SELECT ctmsmis.mis_exp_unit_preadv_req.gkey AS gkey,mis_exp_unit_preadv_req.cont_id AS cont_id,mis_exp_unit_preadv_req.cont_mlo,\n" +
//                "mis_exp_unit_preadv_req.cont_size AS cont_size,mis_exp_unit_preadv_req.isoType,'' AS loc,'' AS emtyDate,\n" +
//                "mis_exp_unit_preadv_req.rotation,mis_exp_unit_preadv_req.cont_status,mis_exp_unit_preadv_req.pod,\n" +
//                "goods_and_ctr_wt_kg,seal_no,last_update,cont_mlo,\n" +
//                "(select code  from ctmsmis.offdoc where id= ctmsmis.mis_exp_unit_preadv_req.transOp) as offdock,\n" +
//                "(SELECT NAME FROM sparcsn4.vsl_vessel_visit_details\n" +
//                " INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
//                " WHERE ib_vyg=ctmsmis.mis_exp_unit_preadv_req.rotation) AS vsl_name\n" +
//                "FROM  ctmsmis.mis_exp_unit_preadv_req \n" +
//                "WHERE  mis_exp_unit_preadv_req.rotation='"+rotation+"' AND date(last_update)='"+fromdate+"'order by cont_mlo";


 String sqlQuery="\n" +
         "SELECT inv_unit.gkey,vsl_vessel_visit_details.ib_vyg as rotation,inv_unit_fcy_visit.time_load,inv_unit.id AS cont_id,inv_unit_fcy_visit.transit_state,SUBSTR(ref_equip_type.nominal_length,-2, LENGTH( ref_equip_type.nominal_length)) AS cont_size,\n" +
         "inv_unit.seal_nbr1 AS seal_no,inv_unit_fcy_visit.time_load,\n" +
         "ref_equip_type.id AS isoType,ref_bizunit_scoped.id AS cont_mlo,inv_unit.freight_kind as cont_status,inv_unit.goods_and_ctr_wt_kg,\n" +
         "ref_commodity.short_name AS commodity,inv_unit.remark as remarks, inv_unit_fcy_visit.ARRIVE_POS_SLOT as stowage_pos,\n" +
         "REF_ROUTING_POINT.ID as pod,vsl_vessels.name as vsl_name\n" +
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
         "WHERE vsl_vessel_visit_details.ib_vyg='"+rotation+"' and  to_char(inv_unit_fcy_visit.time_load,'yyyy-mm-dd')='"+fromdate+ "'\n";

        System.out.println("result:"+sqlQuery);
        List resultList=OracleDbTemplate.query(sqlQuery,new DateAndRotationWisePreAdvisedContainer());
        System.out.println("sql:"+resultList);
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class DateAndRotationWisePreAdvisedContainer implements RowMapper {

        @Override
        public ExportDateAndRotationWisePreAdvisedContainer mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportDateAndRotationWisePreAdvisedContainer exportDateAndRotationWisePreAdvisedContainer=new ExportDateAndRotationWisePreAdvisedContainer();exportDateAndRotationWisePreAdvisedContainer.setCont_id(rs.getString("cont_id"));
            exportDateAndRotationWisePreAdvisedContainer.setCont_size(rs.getString("cont_size"));
            exportDateAndRotationWisePreAdvisedContainer.setGoods_and_ctr_wt_kg(rs.getString("goods_and_ctr_wt_kg"));
            exportDateAndRotationWisePreAdvisedContainer.setSeal_no(rs.getString("seal_no"));
            exportDateAndRotationWisePreAdvisedContainer.setCont_mlo(rs.getString("cont_mlo"));
            exportDateAndRotationWisePreAdvisedContainer.setIsoType(rs.getString("isoType"));
            exportDateAndRotationWisePreAdvisedContainer.setOffdock(rs.getString("offdock"));
            exportDateAndRotationWisePreAdvisedContainer.setVsl_name(rs.getString("vsl_name"));
            exportDateAndRotationWisePreAdvisedContainer.setRotation(rs.getString("rotation"));
            //exportDateAndRotationWisePreAdvisedContainer.setLast_update(rs.getTimestamp("last_update"));
            exportDateAndRotationWisePreAdvisedContainer.setCont_status(rs.getString("cont_status"));
            exportDateAndRotationWisePreAdvisedContainer.setPod(rs.getString("pod"));
            return exportDateAndRotationWisePreAdvisedContainer;
        }
    }


}