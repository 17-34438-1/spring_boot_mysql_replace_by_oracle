package com.datasoft.IgmMis.Service.ImportReport;
import com.datasoft.IgmMis.Model.ImportReport.RemovalListOfOverFlowModel;
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
public class RemovalListOfOverFlowYardService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List getRemovalListOfOverFlowYard(String assignDate,String modify){
        String sqlQuery="";
        if(modify.equals("overflow")) {
            sqlQuery = "SELECT cont_no,\n" +
                    "(SELECT sparcsn4.ref_bizunit_scoped.name FROM sparcsn4.ref_bizunit_scoped\n" +
                    "INNER JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.consignee_bzu = sparcsn4.ref_bizunit_scoped.gkey\n" +
                    "WHERE  sparcsn4.inv_goods.gkey=tbl.goods\n" +
                    ") AS cf,\n" +
                    "(SELECT sparcsn4.ref_bizunit_scoped.sms_number FROM sparcsn4.ref_bizunit_scoped\n" +
                    "INNER JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.consignee_bzu = sparcsn4.ref_bizunit_scoped.gkey\n" +
                    "WHERE  sparcsn4.inv_goods.gkey=tbl.goods\n" +
                    ") AS sms_number,cont_status,\n" +
                    "IFNULL((SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                    "FROM sparcsn4.srv_event \n" +
                    "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey \n" +
                    "WHERE sparcsn4.srv_event.applied_to_gkey=tbl.gkey AND sparcsn4.srv_event.event_type_gkey IN(18,13,16)\n" +
                    "AND sparcsn4.srv_event_field_changes.new_value IS NOT NULL AND sparcsn4.srv_event_field_changes.new_value !='' AND \n" +
                    "sparcsn4.srv_event_field_changes.new_value !='Y-CGP-.' AND sparcsn4.srv_event.gkey <\n" +
                    "(SELECT sparcsn4.srv_event.gkey\n" +
                    "FROM sparcsn4.srv_event INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                    "WHERE sparcsn4.srv_event.event_type_gkey=4 AND sparcsn4.srv_event.applied_to_gkey=tbl.gkey AND metafield_id='unitFlexString01'\n" +
                    "AND new_value IS NOT NULL ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1) ORDER BY sparcsn4.srv_event.gkey \n" +
                    "DESC LIMIT 1),(SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7) FROM sparcsn4.srv_event \n" +
                    "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey \n" +
                    "WHERE sparcsn4.srv_event.applied_to_gkey=tbl.gkey AND sparcsn4.srv_event.event_type_gkey IN(18,13,16) \n" +
                    "ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1)) AS slot,\n" +
                    "(SELECT ctmsmis.cont_yard(slot)) AS Yard_No,rot_no,\n" +
                    "(SELECT sparcsn4.vsl_vessels.name FROM sparcsn4.vsl_vessels\n" +
                    "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vessel_gkey=sparcsn4.vsl_vessels.gkey\n" +
                    "WHERE sparcsn4.vsl_vessel_visit_details.ib_vyg=tbl.rot_no LIMIT 1) AS v_name,\n" +
                    "(SELECT RIGHT(sparcsn4.ref_equip_type.nominal_length,2) FROM sparcsn4.inv_unit_equip \n" +
                    "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
                    "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey \n" +
                    "WHERE sparcsn4.inv_unit_equip.unit_gkey=tbl.gkey) AS size,\n" +
                    "\n" +
                    "((SELECT RIGHT(sparcsn4.ref_equip_type.nominal_height,2) FROM sparcsn4.inv_unit_equip \n" +
                    "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
                    "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey \n" +
                    "WHERE sparcsn4.inv_unit_equip.unit_gkey=tbl.gkey)/10) AS height,\n" +
                    "(SELECT sparcsn4.ref_bizunit_scoped.id FROM sparcsn4.ref_bizunit_scoped WHERE gkey=tbl.line_op) AS mlo,\n" +
                    "mfdch_value,mfdch_desc,seal_nbr1\n" +
                    "FROM (\n" +
                    "SELECT inv_unit.id AS cont_no,sparcsn4.inv_unit.gkey,sparcsn4.inv_unit.goods,sparcsn4.inv_unit.line_op, inv_unit.freight_kind AS cont_status,\n" +
                    "sparcsn4.inv_unit_fcy_visit.flex_string10 AS rot_no,inv_unit.remark,config_metafield_lov.mfdch_value AS mfdch_value,\n" +
                    "config_metafield_lov.mfdch_desc AS mfdch_desc,seal_nbr1\n" +
                    "FROM sparcsn4.inv_unit \n" +
                    "INNER JOIN sparcsn4.inv_unit_fcy_visit  ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                    "INNER JOIN sparcsn4.config_metafield_lov ON inv_unit.flex_string01=config_metafield_lov.mfdch_value \n" +
                    "WHERE DATE(inv_unit_fcy_visit.flex_date01)='" + assignDate + "' AND config_metafield_lov.mfdch_value NOT IN ('CANCEL')\n" +
                    ") AS tbl WHERE remark LIKE 'overflow%'";
        }
        else if (modify.equals("all")){
            sqlQuery="SELECT a.id AS cont_no,k.name AS cf,IFNULL(k.sms_number,ctmsmis.mis_assignment_entry.phone_number) AS sms_number,\n" +
                    "a.freight_kind AS cont_status,\n" +
                    "IFNULL((SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                    "FROM sparcsn4.srv_event\n" +
                    "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                    "WHERE sparcsn4.srv_event.applied_to_gkey=a.gkey AND sparcsn4.srv_event.event_type_gkey IN(18,13,16) AND sparcsn4.srv_event_field_changes.new_value IS NOT NULL AND sparcsn4.srv_event_field_changes.new_value !='' AND sparcsn4.srv_event_field_changes.new_value !='Y-CGP-.' AND sparcsn4.srv_event.gkey<(SELECT sparcsn4.srv_event.gkey FROM sparcsn4.srv_event\n" +
                    "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                    "WHERE sparcsn4.srv_event.event_type_gkey=4 AND sparcsn4.srv_event.applied_to_gkey=a.gkey AND metafield_id='unitFlexString01' AND new_value IS NOT NULL ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1) ORDER BY sparcsn4.srv_event.gkey DESC LIMIT 1),(SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                    "FROM sparcsn4.srv_event\n" +
                    "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                    "WHERE sparcsn4.srv_event.applied_to_gkey=a.gkey AND sparcsn4.srv_event.event_type_gkey IN(18,13,16) ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1)) AS slot,\n" +
                    "(SELECT ctmsmis.cont_yard(slot)) AS Yard_No,\n" +
                    "(SELECT RIGHT(sparcsn4.ref_equip_type.nominal_length,2) FROM sparcsn4.inv_unit_equip \n" +
                    "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
                    "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey \n" +
                    "WHERE sparcsn4.inv_unit_equip.unit_gkey=a.gkey) AS size,\n" +
                    "((SELECT RIGHT(sparcsn4.ref_equip_type.nominal_height,2) FROM sparcsn4.inv_unit_equip \n" +
                    "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
                    "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey \n" +
                    "WHERE sparcsn4.inv_unit_equip.unit_gkey=a.gkey)/10) AS height,\n" +
                    "(SELECT sparcsn4.vsl_vessels.name FROM sparcsn4.vsl_vessels\n" +
                    "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vessel_gkey=sparcsn4.vsl_vessels.gkey\n" +
                    "WHERE sparcsn4.vsl_vessel_visit_details.ib_vyg=b.flex_string10 LIMIT 1) AS v_name,\n" +
                    "b.flex_string10 AS rot_no,\n" +
                    "g.id AS mlo,\n" +
                    "config_metafield_lov.mfdch_value AS mfdch_value,\n" +
                    "config_metafield_lov.mfdch_desc AS mfdch_desc,a.seal_nbr1\n" +
                    "FROM sparcsn4.inv_unit a\n" +
                    "INNER JOIN sparcsn4.inv_unit_fcy_visit b ON b.unit_gkey=a.gkey\n" +
                    "INNER JOIN sparcsn4.ref_bizunit_scoped g ON a.line_op = g.gkey\n" +
                    "LEFT JOIN ctmsmis.mis_assignment_entry ON ctmsmis.mis_assignment_entry.unit_gkey=a.gkey\n" +
                    "INNER JOIN sparcsn4.config_metafield_lov ON a.flex_string01=config_metafield_lov.mfdch_value\n" +
                    "INNER JOIN\n" +
                    "sparcsn4.inv_goods j ON j.gkey = a.goods\n" +
                    "LEFT JOIN\n" +
                    "sparcsn4.ref_bizunit_scoped k ON k.gkey = j.consignee_bzu\n" +
                    "WHERE DATE(flex_date01)='"+assignDate+"' AND config_metafield_lov.mfdch_value NOT IN ('CANCEL')";
        }

        List<RemovalListOfOverFlowModel> resultList=secondaryDBTemplate.query(sqlQuery, new RemovalListOfOverFlowYardService.RemovalList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());

        return resultList;

    }
    class RemovalList implements RowMapper{

        @Override
        public RemovalListOfOverFlowModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            RemovalListOfOverFlowModel removalListOfOverFlowModel = new RemovalListOfOverFlowModel();
            removalListOfOverFlowModel.setMfdch_value(rs.getString("mfdch_value"));
            removalListOfOverFlowModel.setCf(rs.getString("cf"));
            removalListOfOverFlowModel.setSms_number(rs.getString("sms_number"));
            removalListOfOverFlowModel.setCont_no(rs.getString("cont_no"));
            removalListOfOverFlowModel.setSize(rs.getInt("size"));
            removalListOfOverFlowModel.setHeight(rs.getFloat("height"));
            removalListOfOverFlowModel.setSeal_nbr1(rs.getString("seal_nbr1"));
            removalListOfOverFlowModel.setMlo(rs.getString("mlo"));
            removalListOfOverFlowModel.setCont_status(rs.getString("cont_status"));
            removalListOfOverFlowModel.setV_name(rs.getString("v_name"));
            removalListOfOverFlowModel.setRot_no(rs.getString("rot_no"));
            removalListOfOverFlowModel.setSlot(rs.getString("slot"));
            removalListOfOverFlowModel.setYard_No(rs.getString("Yard_No"));
            return removalListOfOverFlowModel ;
        }
    }
}
