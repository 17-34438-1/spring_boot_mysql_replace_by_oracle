package com.datasoft.IgmMis.Service.ImportReport;




import com.datasoft.IgmMis.Model.ImportReport.YardWiseAssignmentAndDeliveryEmptyDetailMainModel;
import com.datasoft.IgmMis.Model.ImportReport.YardWiseAssignmentAndDeliveryEmptyDetailModel;
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
public class YardWiseAssignmentAndDeliveryEmptyDetailService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    public List getBlockList(String yardName){
        String sqlQuery="";
        sqlQuery="select distinct block_cpa as block from ctmsmis.yard_block where terminal='"+yardName+"' and  block_cpa!='NULL' ORDER BY block ASC";
        List resultList=secondaryDBTemplate.query(sqlQuery,new YardWiseAssignmentAndDeliveryEmptyDetailService.BlockList());
        return resultList;

    }

    public List getYardWiseAssignmentAndDeliveryDetail(String fromDate, String yardName, String blockName){
        List<YardWiseAssignmentAndDeliveryEmptyDetailModel> resultList=new ArrayList<>();
        List<YardWiseAssignmentAndDeliveryEmptyDetailMainModel> list = new ArrayList<>();
        ArrayList<YardWiseAssignmentAndDeliveryEmptyDetailModel>shiftAList=new ArrayList<>();
        ArrayList<YardWiseAssignmentAndDeliveryEmptyDetailModel>shiftBList=new ArrayList<>();
        ArrayList<YardWiseAssignmentAndDeliveryEmptyDetailModel>shiftCList=new ArrayList<>();
        ArrayList<YardWiseAssignmentAndDeliveryEmptyDetailModel>noShitList=new ArrayList<>();
        YardWiseAssignmentAndDeliveryEmptyDetailMainModel lastResutModel= new YardWiseAssignmentAndDeliveryEmptyDetailMainModel();
        String sqlQuery="";
        String find="";
        if(blockName.equals("") || blockName==null)
        {
            find="Yard_No='"+yardName+"'";
        }
        else{
            find="Yard_No='"+yardName+"' and Block_No='"+blockName+"'";
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
                "(SELECT ctmsmis.cont_block(carrentPosition,ctmsmis.cont_yard(carrentPosition))) AS Block_No,\n" +
                " IF(UCASE(a.stay) LIKE '%STAY%',1,0) AS stay,\n" +
                "\n" +
                "(SELECT creator FROM sparcsn4.srv_event WHERE applied_to_gkey=a.unit_gkey AND event_type_gkey=30 ORDER BY gkey DESC LIMIT 1) AS stripped_by,\n" +
                "\n" +
                "(SELECT sparcsn4.srv_event.created FROM  sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                "WHERE applied_to_gkey=a.unit_gkey AND event_type_gkey=4 AND sparcsn4.srv_event_field_changes.new_value='E' LIMIT 1) AS proEmtyDate\n" +
                "\n" +
                "FROM ctmsmis.tmp_vcms_assignment a\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit b ON b.unit_gkey=a.unit_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=a.unit_gkey\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped g ON sparcsn4.inv_unit.line_op = g.gkey \n" +
                "WHERE DATE(b.flex_date01)='"+fromDate+"' AND mfdch_value NOT IN ('CANCEL','OCD','APPCUS','APPOTH','APPREF')\n" +
                ") AS tmp WHERE "+find+" ORDER BY sl,Yard_No,shift,proEmtyDate";
        resultList=secondaryDBTemplate.query(sqlQuery,new YardWiseAssignmentAndDeliveryEmptyDetailService.AssignmentAndDeliveryEmptyList());
        Integer j20=0;
        Integer j40=0;
        Integer a20 = 0;
        Integer a40 = 0;
        Integer b20 = 0;
        Integer c20 = 0;
        Integer b40=0;
        Integer c40=0;
        for(int i=0;i<resultList.size();i++){
           // String containerNo="";
            //String shift="";
            //ArrayList<YardWiseAssignmentAndDeliveryEmptyDetailModel>shiftAList=new ArrayList<>();
           // ArrayList<YardWiseAssignmentAndDeliveryEmptyDetailModel>shiftBList=new ArrayList<>();
           // ArrayList<YardWiseAssignmentAndDeliveryEmptyDetailModel>shiftCList=new ArrayList<>();
           // ArrayList<YardWiseAssignmentAndDeliveryEmptyDetailModel>noShitList=new ArrayList<>();

            YardWiseAssignmentAndDeliveryEmptyDetailModel resultRow = resultList.get(i);
            String containerNo=resultRow.getCont_no();
             String shift=resultRow.getShift();

            String query="";
            String iso="";
            String subIso="";



            query="select cont_iso_type from igm_detail_container where cont_number='"+containerNo+"'";
            List<YardWiseAssignmentAndDeliveryEmptyDetailModel> contIsoTypeList= primaryDBTemplate.query(query,new YardWiseAssignmentAndDeliveryEmptyDetailService.CotainerIsoType());
            if(contIsoTypeList.size()>0){
                for(int j=0;j<contIsoTypeList.size();j++) {

                    iso = contIsoTypeList.get(j).getCont_iso_type();
                    subIso=iso.substring(0,1);


                }
                resultRow.setCont_iso_type(iso);
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



            if(yardName.equals("CCT")){
            }
            else if(yardName.equals("NCT")){

            }
            else if(yardName.equals("GCB")){

            }
            else if(yardName.equals("SCY")){

            }
            else if(yardName.equals("OFY2")){

            }

            if(shift.equals("Shift A")){

                shiftAList.add(resultRow);
            }
            else if(shift.equals("Shift B")){
                shiftBList.add(resultRow);
            }
            else if(shift.equals("Shift C")){
                shiftCList.add(resultRow);
            }
            else if(shift.equals("")|| shift.equals(null)){
                noShitList.add(resultRow);

            }

            //list.add(resultRow);

        }
        int shifTATotal20=0;
        int shifTATotal40=0;
        if(shiftAList.size()>0) {
            for (int k = 0; k < shiftAList.size(); k++) {
                YardWiseAssignmentAndDeliveryEmptyDetailModel eachRow = new YardWiseAssignmentAndDeliveryEmptyDetailModel();
                eachRow = shiftAList.get(k);
                if (eachRow.getSubIso().equals("2")) {
                    shifTATotal20 = shifTATotal20 + 1;
                } else {
                    shifTATotal40 = shifTATotal40 + 1;

                }


            }
        }
        int shifTBTotal20=0;
        int shifTBTotal40=0;
        if(shiftBList.size()>0) {
            for (int k = 0; k < shiftBList.size(); k++) {

                YardWiseAssignmentAndDeliveryEmptyDetailModel eachRow = new YardWiseAssignmentAndDeliveryEmptyDetailModel();
                eachRow = shiftBList.get(k);
                if (eachRow.getSubIso().equals("2")) {
                    shifTBTotal20 = shifTBTotal20 + 1;
                } else {
                    shifTBTotal40 = shifTBTotal40 + 1;

                }


            }
        }
        int shifTCTotal20=0;
        int shifTCTotal40=0;
        if(shiftCList.size()>0) {
            for (int k = 0; k < shiftCList.size(); k++) {

                YardWiseAssignmentAndDeliveryEmptyDetailModel eachRow = new YardWiseAssignmentAndDeliveryEmptyDetailModel();
                eachRow = shiftCList.get(k);
                if (eachRow.getSubIso().equals("2")) {
                    shifTCTotal20 = shifTCTotal20 + 1;
                } else {
                    shifTCTotal40 = shifTCTotal40 + 1;

                }


            }
        }
        int noShifTotal20=0;
        int noShifTotal40=0;
        if(noShitList.size()>0) {
            for (int k = 0; k < noShitList.size(); k++) {
                YardWiseAssignmentAndDeliveryEmptyDetailModel eachRow = new YardWiseAssignmentAndDeliveryEmptyDetailModel();
                eachRow = noShitList.get(k);
                if (eachRow.getSubIso().equals("2")) {
                    noShifTotal20 = noShifTotal20 + 1;
                } else {
                    noShifTotal40 = noShifTotal40 + 1;

                }


            }
        }

        lastResutModel.setShifA(shiftAList);
        lastResutModel.setShifTATotal20(shifTATotal20);
        lastResutModel.setShifTATotal40(shifTATotal40);

        lastResutModel.setShifB(shiftBList);
        lastResutModel.setShifTBTotal20(shifTBTotal20);
        lastResutModel.setShifTBTotal40(shifTBTotal40);

        lastResutModel.setShifC(shiftCList);
        lastResutModel.setShifTCTotal20(shifTCTotal20);
        lastResutModel.setShifTCTotal40(shifTCTotal40);

        lastResutModel.setNoshiftyardList(noShitList);
        lastResutModel.setNoshiftyard20(noShifTotal20);
        lastResutModel.setNoshiftyard40(noShifTotal40);

        lastResutModel.setA20(a20);
        lastResutModel.setA40(a40);
        lastResutModel.setB20(b20);
        lastResutModel.setB40(b40);
        lastResutModel.setC20(c20);
        lastResutModel.setC40(c40);
        lastResutModel.setJ20(j20);
        lastResutModel.setJ40(j40);

        list.add(lastResutModel);
        return list;


    }

    class AssignmentAndDeliveryEmptyList implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            YardWiseAssignmentAndDeliveryEmptyDetailModel assignmentAndDeliveryEmptyDetailModel=new YardWiseAssignmentAndDeliveryEmptyDetailModel();
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
            YardWiseAssignmentAndDeliveryEmptyDetailModel assignmentAndDeliveryEmptyDetailModel=new YardWiseAssignmentAndDeliveryEmptyDetailModel();
            assignmentAndDeliveryEmptyDetailModel.setCont_iso_type(rs.getString("cont_iso_type"));

            return assignmentAndDeliveryEmptyDetailModel;
        }
    }
    class BlockList implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            YardWiseAssignmentAndDeliveryEmptyDetailModel assignmentAndDeliveryEmptyDetailModel=new YardWiseAssignmentAndDeliveryEmptyDetailModel();
            assignmentAndDeliveryEmptyDetailModel.setBlock(rs.getString("block"));
            return assignmentAndDeliveryEmptyDetailModel;
        }
    }
}
