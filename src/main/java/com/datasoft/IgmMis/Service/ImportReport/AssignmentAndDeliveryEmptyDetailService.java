package com.datasoft.IgmMis.Service.ImportReport;


import com.datasoft.IgmMis.Model.ImportReport.AssignmentAndDeliveryEmptyDetailModel;
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
public class AssignmentAndDeliveryEmptyDetailService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List getAssignmentAndDeliveryDetail(String fromDate, String loginId,String yardName){
        List<AssignmentAndDeliveryEmptyDetailModel> resultList=new ArrayList<>();
        List<AssignmentAndDeliveryEmptyDetailModel> list = new ArrayList<>();
       // List<AssignmentAndDeliveryEmptyDetailMainModel> mainList=new ArrayList<>();
        String condition="";
        String sqlQuery="";
        String substr1="";
        String substr2="";
        if(loginId.equals("pass")){
            substr1=" AND mfdch_value!='CANCEL'";
            substr2=" order by sl,Yard_No,shift,proEmtyDate";

        }
        else{
            substr1="AND mfdch_value NOT IN ('CANCEL','OCD','APPCUS','APPOTH','APPREF')";
            if(yardName.equals("all")){
               substr2=" order by sl,Yard_No,shift,proEmtyDate";
            }
            else {
                substr2=" where Yard_No='"+yardName+"' order by sl,Yard_No,shift,proEmtyDate";

            }
        }
        sqlQuery="SELECT DISTINCT *,\n" +
                "(CASE \n" +
                "WHEN delivery >= CONCAT('"+fromDate+"',' 08:00:00') AND delivery < CONCAT('"+fromDate+"',' 16:00:00') THEN 'Shift A'\n" +
                "WHEN delivery >= CONCAT('"+fromDate+"',' 16:00:00') AND delivery < CONCAT(DATE_ADD('"+fromDate+"',INTERVAL 1 DAY),' 00:00:00') THEN 'Shift B'\n" +
                "WHEN delivery >= CONCAT(DATE_ADD('"+fromDate+"',INTERVAL 1 DAY),' 00:00:00') AND delivery < CONCAT(DATE_ADD('"+fromDate+"',INTERVAL 1 DAY),' 08:00:00') THEN 'Shift C'\n" +
                "END) AS shift,\n" +
                "(CASE WHEN delivery IS NULL THEN 2 ELSE 1 END) AS sl\n" +
                "FROM (\n" +
                "\n" +
                "SELECT a.cont_no,\n" +
                "b.flex_date01,\n" +
                "a.iso_code,\n" +
                "sparcsn4.inv_unit.goods_and_ctr_wt_kg AS weight,\n" +
                "g.id AS mlo,\n" +
                "b.flex_string10 AS rot_no,\n" +
                "b.time_in AS dischargetime,\n" +
                "b.time_out AS delivery,\n" +
                "a.assignmentDate AS assignmentdate,\n" +
                "a.cf_name AS cf,\n" +
                "a.mfdch_desc,\n" +
                "a.cont_status AS statu,\n" +
                "(SELECT ctmsmis.mis_exp_unit_load_failed.last_update\n" +
                "FROM ctmsmis.mis_exp_unit_load_failed WHERE ctmsmis.mis_exp_unit_load_failed.gkey=a.unit_gkey) AS last_update,\n" +
                "(SELECT ctmsmis.mis_exp_unit_load_failed.user_id\n" +
                "FROM ctmsmis.mis_exp_unit_load_failed WHERE ctmsmis.mis_exp_unit_load_failed.gkey=a.unit_gkey) AS user_id,\n" +
                "IFNULL((SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7) FROM sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey \n" +
                "WHERE sparcsn4.srv_event.applied_to_gkey=a.unit_gkey AND sparcsn4.srv_event.event_type_gkey IN(18,13,16) \n" +
                "AND sparcsn4.srv_event_field_changes.new_value IS NOT NULL AND sparcsn4.srv_event_field_changes.new_value !=''\n" +
                "AND sparcsn4.srv_event_field_changes.new_value !='Y-CGP-.' AND sparcsn4.srv_event.gkey<(SELECT sparcsn4.srv_event.gkey \n" +
                "FROM sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey \n" +
                "WHERE sparcsn4.srv_event.event_type_gkey=4 AND sparcsn4.srv_event.applied_to_gkey=a.unit_gkey AND metafield_id='unitFlexString01'\n" +
                "AND new_value IS NOT NULL ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1) \n" +
                "ORDER BY sparcsn4.srv_event.gkey DESC LIMIT 1),(SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7) \n" +
                "FROM sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                "WHERE sparcsn4.srv_event.applied_to_gkey=a.unit_gkey AND sparcsn4.srv_event.event_type_gkey IN(18,13,16) \n" +
                "ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1)) AS carrentPosition,\n" +
                "(SELECT ctmsmis.cont_yard(carrentPosition)) AS Yard_No, \n" +
                "(SELECT ctmsmis.cont_block(carrentPosition,Yard_No)) AS Block_No,\n" +
                " IF(UCASE(a.stay) LIKE '%STAY%',1,0) AS stay,\n" +
                "(SELECT creator FROM sparcsn4.srv_event WHERE applied_to_gkey=a.unit_gkey AND event_type_gkey=30 ORDER BY gkey DESC LIMIT 1) AS stripped_by,\n" +
                "(SELECT sparcsn4.srv_event.created FROM  sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                "WHERE applied_to_gkey=a.unit_gkey AND event_type_gkey=4 AND sparcsn4.srv_event_field_changes.new_value='E' LIMIT 1) AS proEmtyDate\n" +
                "FROM ctmsmis.tmp_vcms_assignment a\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit b ON b.unit_gkey=a.unit_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=a.unit_gkey\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped g ON sparcsn4.inv_unit.line_op = g.gkey \n" +
                "WHERE ( b.flex_date01 BETWEEN  CONCAT('"+fromDate+"', ' 00:00:00') AND CONCAT('"+fromDate+"', ' 23:59:59') )  "+substr1+"  \n" +
                ") AS tmp  "+substr2;

        resultList=secondaryDBTemplate.query(sqlQuery,new AssignmentAndDeliveryEmptyDetailService.AssignmentAndDeliveryEmptyList());
        //List listAll = (List) resultList.stream().collect(Collectors.toList());
        for(int i=0;i<resultList.size();i++){
            AssignmentAndDeliveryEmptyDetailModel resultRow = resultList.get(i);

            String containerNo=resultRow.getCont_no();
            String query="";
            String iso="";
            String subIso="";

            query="select cont_iso_type from igm_detail_container where cont_number='"+containerNo+"'";
            List<AssignmentAndDeliveryEmptyDetailModel> contIsoTypeList= primaryDBTemplate.query(query,new AssignmentAndDeliveryEmptyDetailService.CotainerIsoType());
            if(contIsoTypeList.size()>0){
                for(int j=0;j<contIsoTypeList.size();j++) {
                    iso = contIsoTypeList.get(j).getCont_iso_type();
                    subIso=iso.substring(0,1);

                }
                resultRow.setCont_iso_type(iso);
                resultRow.setSubIso(subIso);
            }

            list.add(resultRow);

        }

       /* for(int k=0;k<list.size();k++){
            AssignmentAndDeliveryEmptyDetailModel assignmentAndDeliveryEmptyDetailModel=new AssignmentAndDeliveryEmptyDetailModel();
            AssignmentAndDeliveryEmptyDetailMainModel andDeliveryEmptyDetailMainModel=new AssignmentAndDeliveryEmptyDetailMainModel();
            assignmentAndDeliveryEmptyDetailModel= list.get(k);
            String yard=assignmentAndDeliveryEmptyDetailModel.getYard_No();

            if(yard.equals("CCT")){

            }
            else{

            }

        }*/


        return list;

    }
    class AssignmentAndDeliveryEmptyList implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssignmentAndDeliveryEmptyDetailModel assignmentAndDeliveryEmptyDetailModel=new AssignmentAndDeliveryEmptyDetailModel();
            assignmentAndDeliveryEmptyDetailModel.setCont_no(rs.getString("cont_no"));
            assignmentAndDeliveryEmptyDetailModel.setFlex_date01(rs.getString("flex_date01"));
            assignmentAndDeliveryEmptyDetailModel.setIso_code(rs.getString("iso_code"));
            assignmentAndDeliveryEmptyDetailModel.setWeight(rs.getInt("weight"));
            assignmentAndDeliveryEmptyDetailModel.setMlo(rs.getString("mlo"));
            assignmentAndDeliveryEmptyDetailModel.setRot_no(rs.getString("rot_no"));
            assignmentAndDeliveryEmptyDetailModel.setDischargetime(rs.getString("dischargetime"));
            assignmentAndDeliveryEmptyDetailModel.setDelivery(rs.getString("delivery"));
            assignmentAndDeliveryEmptyDetailModel.setAssignmentdate(rs.getString("assignmentdate"));
            assignmentAndDeliveryEmptyDetailModel.setCf(rs.getString("cf"));
            assignmentAndDeliveryEmptyDetailModel.setMfdch_desc(rs.getString("mfdch_desc"));
            assignmentAndDeliveryEmptyDetailModel.setStatu(rs.getString("statu"));
            assignmentAndDeliveryEmptyDetailModel.setLast_update(rs.getString("last_update"));
            assignmentAndDeliveryEmptyDetailModel.setUser_id(rs.getString("user_id"));
            assignmentAndDeliveryEmptyDetailModel.setCarrentPosition(rs.getString("carrentPosition"));
            assignmentAndDeliveryEmptyDetailModel.setYard_No(rs.getString("Yard_No"));
            assignmentAndDeliveryEmptyDetailModel.setBlock_No(rs.getString("Block_No"));
            assignmentAndDeliveryEmptyDetailModel.setStay(rs.getInt("stay"));
            assignmentAndDeliveryEmptyDetailModel.setStripped_by(rs.getString("stripped_by"));
            assignmentAndDeliveryEmptyDetailModel.setProEmtyDate(rs.getString("proEmtyDate"));
            assignmentAndDeliveryEmptyDetailModel.setShift(rs.getString("shift"));
            assignmentAndDeliveryEmptyDetailModel.setSl(rs.getInt("sl"));



            return assignmentAndDeliveryEmptyDetailModel;
        }
    }

    class CotainerIsoType implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssignmentAndDeliveryEmptyDetailModel assignmentAndDeliveryEmptyDetailModel=new AssignmentAndDeliveryEmptyDetailModel();
            assignmentAndDeliveryEmptyDetailModel.setCont_iso_type(rs.getString("cont_iso_type"));

            return assignmentAndDeliveryEmptyDetailModel;
        }
    }


}
