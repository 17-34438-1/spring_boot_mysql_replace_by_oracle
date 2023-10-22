package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.AssignmentAndDeliveryEmptySummaryModel;
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
public class AssignmentAndDeliveryEmptySummaryService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    public List getAssignmentEmptySummary(String fromDate,String yardName){
        List<AssignmentAndDeliveryEmptySummaryModel> list=new ArrayList<>();
        AssignmentAndDeliveryEmptySummaryModel lastResutModel=new AssignmentAndDeliveryEmptySummaryModel();
        String sqlQuery="";
        sqlQuery="SELECT DISTINCT *,\n" +
                "(CASE \n" +
                "WHEN delivery >= CONCAT('"+fromDate+"',' 08:00:00') AND delivery <CONCAT('"+fromDate+"',' 16:00:00') THEN 'Shift A'\n" +
                "WHEN delivery >= CONCAT('"+fromDate+"',' 16:00:00') AND delivery <CONCAT(DATE_ADD('"+fromDate+"',INTERVAL 1 DAY),' 00:00:00') THEN 'Shift B'\n" +
                "WHEN delivery >= CONCAT(DATE_ADD('"+fromDate+"',INTERVAL 1 DAY),' 00:00:00') AND delivery <CONCAT(DATE_ADD('"+fromDate+"',INTERVAL 1 DAY),' 08:00:00') THEN 'Shift C'\n" +
                "END) AS shift,\n" +
                "(CASE WHEN delivery IS NULL THEN 2 ELSE 1 END) AS sl\n" +
                "FROM (\n" +
                "SELECT a.id AS cont_no,\n" +
                "(SELECT sparcsn4.ref_equip_type.id FROM sparcsn4.inv_unit_equip\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey \n" +
                "WHERE sparcsn4.inv_unit_equip.unit_gkey=a.gkey) AS iso_code,\n" +
                "b.flex_string10 AS rot_no,\n" +
                "b.time_in AS dischargetime,\n" +
                "b.time_out AS delivery,\n" +
                "g.id AS mlo,\n" +
                "k.name AS cf,\n" +
                "sparcsn4.config_metafield_lov.mfdch_desc,\n" +
                "a.freight_kind AS statu,\n" +
                "a.goods_and_ctr_wt_kg AS weight,\n" +
                "(SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                "FROM sparcsn4.srv_event\n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                "WHERE sparcsn4.srv_event.applied_to_gkey=a.gkey AND sparcsn4.srv_event.event_type_gkey=18 LIMIT 1) AS carrentPosition,\n" +
                "(SELECT ctmsmis.cont_yard((SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                "FROM sparcsn4.srv_event\n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                "WHERE sparcsn4.srv_event.applied_to_gkey=a.gkey AND sparcsn4.srv_event.event_type_gkey=18 LIMIT 1))) AS Yard_No,\n" +
                "(SELECT ctmsmis.cont_block((SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                "FROM sparcsn4.srv_event\n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                "WHERE sparcsn4.srv_event.applied_to_gkey=a.gkey AND sparcsn4.srv_event.event_type_gkey=18 LIMIT 1),Yard_No)) AS Block_No,\n" +
                "(SELECT sparcsn4.srv_event.created FROM  sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                "WHERE applied_to_gkey=a.gkey AND event_type_gkey=4 AND sparcsn4.srv_event_field_changes.new_value='E' LIMIT 1) AS proEmtyDate,\n" +
                "b.flex_date01 AS assignmentdate, IF(UCASE(a.flex_string15) LIKE '%STAY%',1,0) AS stay\n" +
                "\n" +
                "FROM sparcsn4.inv_unit a\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit b ON b.unit_gkey=a.gkey\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped g ON a.line_op = g.gkey\n" +
                "INNER JOIN sparcsn4.config_metafield_lov ON a.flex_string01=config_metafield_lov.mfdch_value\n" +
                "\n" +
                "INNER JOIN\n" +
                "\t    sparcsn4.inv_goods j ON j.gkey = a.goods\n" +
                "LEFT JOIN\n" +
                "\t    sparcsn4.ref_bizunit_scoped k ON k.gkey = j.consignee_bzu\n" +
                "WHERE ( b.flex_date01 BETWEEN  CONCAT('"+fromDate+"', ' 00:00:00') AND CONCAT('"+fromDate+"', ' 23:59:59') ) AND config_metafield_lov.mfdch_value NOT IN ('CANCEL','OCD','APPCUS','APPOTH','APPREF')\n" +
                ") AS tmp WHERE Yard_No='"+yardName+"' ORDER BY sl,Yard_No,shift,proEmtyDate";

        List<AssignmentAndDeliveryEmptySummaryModel> resultList=secondaryDBTemplate.query(sqlQuery,new AssignmentAndDeliveryEmptySummaryService.AssignmentAndDeliveryEmptySummary());
        Integer j20=0;
        Integer j40=0;
        Integer a20 = 0;
        Integer a40 = 0;
        Integer b20 = 0;
        Integer c20 = 0;
        Integer b40=0;
        Integer c40=0;
        Integer stayed=0;
        for(int i=0;i<resultList.size();i++){
            AssignmentAndDeliveryEmptySummaryModel resultRow=resultList.get(i);
            String containerNo=resultRow.getCont_no();
            Integer stay=resultRow.getStay();
            stayed=stayed+stay;
            String shift=resultRow.getShift();

            String query="";
            String iso="";
            String subIso="";



            query="select cont_iso_type from igm_detail_container where cont_number='"+containerNo+"'";
            List<AssignmentAndDeliveryEmptySummaryModel> contIsoTypeList= primaryDBTemplate.query(query,new AssignmentAndDeliveryEmptySummaryService.CotainerIsoType());
            if(contIsoTypeList.size()>0){
                for(int j=0;j<contIsoTypeList.size();j++) {

                    iso = contIsoTypeList.get(j).getCont_iso_type();
                    subIso=iso.substring(0,1);


                }

                resultRow.setSubIso(subIso);

            }

            if(subIso.equals("2")){
                j20=j20+1;
            }
            else{
                j40=j40+1;
            }
            if(subIso.equals("2")){
                if(shift.equals("Shift A")){
                    a20=a20+1;

                }
                else if(shift.equals("Shift B")){
                    b20=b20+1;

                }
                else if(shift.equals("Shift C")){
                    c20=c20+1;

                }
            }
            else{
                if(shift.equals("Shift A")){
                    a40=a40+1;

                }
                else if(shift.equals("Shift B")){
                    b40=b40+1;

                }
                else if(shift.equals("Shift C")){
                    c40=c40+1;

                }

            }


        }

        lastResutModel.setA20(a20);
        lastResutModel.setA40(a40);
        lastResutModel.setB20(b20);
        lastResutModel.setB40(b40);
        lastResutModel.setC20(c20);
        lastResutModel.setC40(c40);
        lastResutModel.setJ20(j20);
        lastResutModel.setJ40(j40);
        lastResutModel.setStayed(stayed);

        list.add(lastResutModel);

        return list;
    }

    class AssignmentAndDeliveryEmptySummary implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssignmentAndDeliveryEmptySummaryModel assignmentAndDeliveryEmptySummaryModel=new AssignmentAndDeliveryEmptySummaryModel();
            assignmentAndDeliveryEmptySummaryModel.setCont_no(rs.getString("cont_no"));
            assignmentAndDeliveryEmptySummaryModel.setIso_code(rs.getString("iso_code"));
            assignmentAndDeliveryEmptySummaryModel.setRot_no(rs.getString("rot_no"));
            assignmentAndDeliveryEmptySummaryModel.setDischargetime(rs.getString("dischargetime"));
            assignmentAndDeliveryEmptySummaryModel.setDelivery(rs.getString("delivery"));
            assignmentAndDeliveryEmptySummaryModel.setMlo(rs.getString("mlo"));
            assignmentAndDeliveryEmptySummaryModel.setCf(rs.getString("cf"));
            assignmentAndDeliveryEmptySummaryModel.setMfdch_desc(rs.getString("mfdch_desc"));
            assignmentAndDeliveryEmptySummaryModel.setStatu(rs.getString("statu"));
            assignmentAndDeliveryEmptySummaryModel.setWeight(rs.getFloat("weight"));
            assignmentAndDeliveryEmptySummaryModel.setCarrentPosition(rs.getString("carrentPosition"));
            assignmentAndDeliveryEmptySummaryModel.setYard_No(rs.getString("Yard_No"));
            assignmentAndDeliveryEmptySummaryModel.setBlock_No(rs.getString("Block_No"));
            assignmentAndDeliveryEmptySummaryModel.setProEmtyDate(rs.getString("proEmtyDate"));
            assignmentAndDeliveryEmptySummaryModel.setAssignmentdate(rs.getString("assignmentdate"));
            assignmentAndDeliveryEmptySummaryModel.setStay(rs.getInt("stay"));
            assignmentAndDeliveryEmptySummaryModel.setShift(rs.getString("shift"));
            assignmentAndDeliveryEmptySummaryModel.setSl(rs.getInt("sl"));
            return assignmentAndDeliveryEmptySummaryModel;
        }

    }
    class CotainerIsoType implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssignmentAndDeliveryEmptySummaryModel assignmentAndDeliveryEmptyDetailModel=new AssignmentAndDeliveryEmptySummaryModel();
            assignmentAndDeliveryEmptyDetailModel.setCont_iso_type(rs.getString("cont_iso_type"));

            return assignmentAndDeliveryEmptyDetailModel;
        }
    }
}
