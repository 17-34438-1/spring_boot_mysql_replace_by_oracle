package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.AppRaiseReSlotLocationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Service
public class AppRaiseReSlotLocationService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List getAppRaiseReSlotLocationList(String searchDate, String containers){
        List<AppRaiseReSlotLocationModel> resultList=new ArrayList<>();
        String sqlQuery="";
        String resultContainers = "";
        if(containers.length()<=0 || containers.equals("empty")) {
             resultContainers = "''";
        }
        else{
            String[] splitedContainers=containers.split(",");
            for (int i=0; i<splitedContainers.length; i++){
                resultContainers=resultContainers+","+"'"+splitedContainers[i]+"'";

            }
            resultContainers=resultContainers.substring(1);

        }

        sqlQuery="SELECT * FROM (\n" +
                "SELECT id,inv_unit_fcy_visit.time_out,category,inv_unit.remark,\n" +
                "(SELECT RIGHT(sparcsn4.ref_equip_type.nominal_height,2)/10 FROM ref_equip_type \n" +
                "INNER JOIN sparcsn4.ref_equipment ON ref_equipment.eqtyp_gkey=ref_equip_type.gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON inv_unit_equip.eq_gkey=ref_equipment.gkey\n" +
                "WHERE sparcsn4.inv_unit_equip.unit_gkey=inv_unit.gkey\n" +
                ") AS height,\n" +
                "\n" +
                "(SELECT RIGHT(sparcsn4.ref_equip_type.nominal_length,2) FROM ref_equip_type \n" +
                "INNER JOIN sparcsn4.ref_equipment ON ref_equipment.eqtyp_gkey=ref_equip_type.gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON inv_unit_equip.eq_gkey=ref_equipment.gkey\n" +
                "WHERE sparcsn4.inv_unit_equip.unit_gkey=inv_unit.gkey\n" +
                ") AS size,\n" +
                "\n" +
                "( SELECT sparcsn4.vsl_vessels.name FROM sparcsn4.vsl_vessel_visit_details\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "WHERE sparcsn4.vsl_vessel_visit_details.ib_vyg=sparcsn4.inv_unit_fcy_visit.flex_string10 \n" +
                "ORDER BY vvd_gkey DESC LIMIT 1\n" +
                ") AS vesselName,\n" +
                "(SELECT inv_goods.destination FROM inv_goods WHERE inv_goods.gkey=inv_unit.goods) AS destination,\n" +
                "(SELECT fm_pos_slot FROM inv_move_event WHERE inv_move_event.ufv_gkey=inv_unit_fcy_visit.gkey AND move_kind IN ('YARD','SHFT') ORDER BY mve_gkey DESC LIMIT 1) AS fm_pos_slot,\n" +
                "(SELECT to_pos_slot FROM inv_move_event WHERE inv_move_event.ufv_gkey=inv_unit_fcy_visit.gkey AND move_kind IN ('YARD','SHFT') ORDER BY mve_gkey DESC LIMIT 1) AS to_pos_slot,\n" +
                "(SELECT short_name FROM xps_che \n" +
                "INNER JOIN inv_move_event ON xps_che.gkey=inv_move_event.che_carry  WHERE inv_move_event.ufv_gkey=inv_unit_fcy_visit.gkey AND move_kind IN ('YARD','SHFT')ORDER BY mve_gkey DESC LIMIT 1) AS che_carry,\n" +
                "(SELECT move_kind FROM inv_move_event WHERE inv_move_event.ufv_gkey=inv_unit_fcy_visit.gkey AND move_kind IN ('YARD','SHFT')ORDER BY mve_gkey DESC LIMIT 1) AS move_kind,\n" +
                "sparcsn4.config_metafield_lov.mfdch_desc,\n" +
                "(SELECT t_put FROM inv_move_event WHERE inv_move_event.ufv_gkey=inv_unit_fcy_visit.gkey AND move_kind IN ('YARD','SHFT')ORDER BY mve_gkey DESC LIMIT 1) AS carriedTime,\n" +
                "inv_unit.freight_kind,\n" +
                "DATE(inv_unit_fcy_visit.flex_date01) flex_date01,\n" +
                "\n" +
                "(SELECT ctmsmis.cont_yard((SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                "FROM sparcsn4.srv_event\n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                "WHERE sparcsn4.srv_event.applied_to_gkey=inv_unit.gkey AND sparcsn4.srv_event.event_type_gkey=18 LIMIT 1))) AS Yard_No\n" +
                "\n" +
                "FROM sparcsn4.inv_unit \n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.config_metafield_lov ON inv_unit.flex_string01=config_metafield_lov.mfdch_value\n" +
                "WHERE DATE(inv_unit_fcy_visit.flex_date01) ='"+searchDate+"' AND id IN("+resultContainers+")\n" +
                ") AS ass ORDER BY Id";
        resultList =secondaryDBTemplate.query(sqlQuery,new AppRaiseReSlotLocationService.AppRaiseReSlotLocation());
        return resultList;
    }

    class AppRaiseReSlotLocation implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            AppRaiseReSlotLocationModel appRaiseReSlotLocationModel=new AppRaiseReSlotLocationModel();
            appRaiseReSlotLocationModel.setId(rs.getString("id"));
            appRaiseReSlotLocationModel.setTime_out(rs.getString("time_out"));
            appRaiseReSlotLocationModel.setCategory(rs.getString("category"));
            appRaiseReSlotLocationModel.setRemark(rs.getString("remark"));
            appRaiseReSlotLocationModel.setHeight(rs.getFloat("height"));
            appRaiseReSlotLocationModel.setSize(rs.getInt("size"));
            appRaiseReSlotLocationModel.setVesselName(rs.getString("vesselName"));
            appRaiseReSlotLocationModel.setDestination(rs.getInt("destination"));
            appRaiseReSlotLocationModel.setFm_pos_slot(rs.getString("fm_pos_slot"));
            appRaiseReSlotLocationModel.setTo_pos_slot(rs.getString("to_pos_slot"));
            appRaiseReSlotLocationModel.setChe_carry(rs.getString("che_carry"));
            appRaiseReSlotLocationModel.setMove_kind(rs.getString("move_kind"));
            appRaiseReSlotLocationModel.setMfdch_desc(rs.getString("mfdch_desc"));
            appRaiseReSlotLocationModel.setCarriedTime(rs.getString("carriedTime"));
            appRaiseReSlotLocationModel.setFreight_kind(rs.getString("freight_kind"));
            appRaiseReSlotLocationModel.setFlex_date01(rs.getString("flex_date01"));
            appRaiseReSlotLocationModel.setYard_No(rs.getString("Yard_No"));
            return appRaiseReSlotLocationModel;
        }
    }
}
