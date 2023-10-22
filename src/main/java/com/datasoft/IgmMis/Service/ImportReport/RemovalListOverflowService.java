package com.datasoft.IgmMis.Service.ImportReport;


import com.datasoft.IgmMis.Model.ImportReport.RemovalListOverflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RemovalListOverflowService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    public List removalListOverflow(String assignment_date) throws SQLException {

        String sqlRemovalListOfOverflowYard = "SELECT cont_no,\n" +
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
                "WHERE DATE(inv_unit_fcy_visit.flex_date01)='"+assignment_date+"' AND config_metafield_lov.mfdch_value NOT IN ('CANCEL')\n" +
                ") AS tbl WHERE remark LIKE 'overflow%'";

        List removalListOverflow = secondaryDBTemplate.query(sqlRemovalListOfOverflowYard, new removalListRowMapper());
        List listAll = (List) removalListOverflow.stream().collect(Collectors.toList());

        return listAll;
    }

    class removalListRowMapper implements RowMapper {

        @Override
        public RemovalListOverflow mapRow(ResultSet rs, int rowNum) throws SQLException {
            RemovalListOverflow removalListOverflow = new RemovalListOverflow();

            removalListOverflow.setCont_no(rs.getString("cont_no"));
            removalListOverflow.setCf(rs.getString("cf"));
            removalListOverflow.setSms_number(rs.getString("sms_number"));
            removalListOverflow.setCont_status(rs.getString("cont_status"));
            removalListOverflow.setSlot(rs.getString("slot"));
            removalListOverflow.setYard_No(rs.getString("yard_No"));
            removalListOverflow.setRot_no(rs.getString("rot_no"));
            removalListOverflow.setV_name(rs.getString("v_name"));
            removalListOverflow.setSize(rs.getString("size"));
            removalListOverflow.setHeight(rs.getString("height"));
            removalListOverflow.setMlo(rs.getString("mlo"));
            removalListOverflow.setMfdch_value(rs.getString("mfdch_value"));
            removalListOverflow.setMfdch_desc(rs.getString("mfdch_desc"));
            removalListOverflow.setSeal_nbr1(rs.getString("seal_nbr1"));

            return removalListOverflow;
        }
    }


}
