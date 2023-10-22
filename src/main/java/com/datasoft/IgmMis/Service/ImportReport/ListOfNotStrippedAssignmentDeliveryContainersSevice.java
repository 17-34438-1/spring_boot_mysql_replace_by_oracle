package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.ListOfNotStrippedAssignmentDeliveryContainersMainModel;
import com.datasoft.IgmMis.Model.ImportReport.ListOfNotStrippedAssignmentDeliveryContainersModel;
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
public class ListOfNotStrippedAssignmentDeliveryContainersSevice {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    public List getListOfNotStrippedAssignmentDeliveryContainers(String fromDate, String yardName ){
        List<ListOfNotStrippedAssignmentDeliveryContainersModel> resultList=new ArrayList<>();
        List<ListOfNotStrippedAssignmentDeliveryContainersModel> temp=new ArrayList<>();
        List<ListOfNotStrippedAssignmentDeliveryContainersMainModel> list=new ArrayList<>();
        ListOfNotStrippedAssignmentDeliveryContainersMainModel lastResultModel=new ListOfNotStrippedAssignmentDeliveryContainersMainModel();

        String sqlQuery="";
        sqlQuery="SELECT * FROM \n" +
                "(\n" +
                "SELECT DISTINCT *,\n" +
                "(CASE \n" +
                "WHEN delivery >= CONCAT('"+fromDate+"',' 08:00:00') AND delivery <CONCAT('"+fromDate+"',' 16:00:00') THEN 'Shift A'\n" +
                "WHEN delivery >= CONCAT('"+fromDate+"',' 16:00:00') AND delivery <CONCAT(DATE_ADD('"+fromDate+"',INTERVAL 1 DAY),' 00:00:00') THEN'Shift B'\n" +
                "WHEN delivery >= CONCAT(DATE_ADD('"+fromDate+"',INTERVAL 1 DAY),' 00:00:00') AND delivery <CONCAT(DATE_ADD('"+fromDate+"',INTERVAL 1 DAY),' 08:00:00') THEN 'Shift C'\n" +
                "END) AS shift,\n" +
                "(CASE WHEN delivery IS NULL THEN 2 ELSE 1 END) AS sl\n" +
                "FROM (\n" +
                "SELECT a.id AS cont_no,\n" +
                "(SELECT sparcsn4.ref_equip_type.id FROM sparcsn4.inv_unit_equip\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey \n" +
                "WHERE sparcsn4.inv_unit_equip.unit_gkey=a.gkey LIMIT 1) AS iso_code,\n" +
                "b.flex_string10 AS rot_no,\n" +
                "b.time_in AS dischargetime,\n" +
                "b.time_out AS delivery,\n" +
                "g.id AS mlo,\n" +
                "k.name AS cf,\n" +
                "sparcsn4.config_metafield_lov.mfdch_desc,\n" +
                "a.freight_kind AS statu,\n" +
                "a.goods_and_ctr_wt_kg AS weight,\n" +
                "(SELECT ctmsmis.mis_exp_unit_load_failed.last_update\n" +
                "FROM ctmsmis.mis_exp_unit_load_failed WHERE ctmsmis.mis_exp_unit_load_failed.gkey=a.gkey) AS last_update,\n" +
                "(SELECT ctmsmis.mis_exp_unit_load_failed.user_id\n" +
                "FROM ctmsmis.mis_exp_unit_load_failed WHERE ctmsmis.mis_exp_unit_load_failed.gkey=a.gkey) AS user_id,\n" +
                "\n" +
                "IFNULL((SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                "FROM sparcsn4.srv_event\n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                "WHERE sparcsn4.srv_event.applied_to_gkey=a.gkey  AND sparcsn4.srv_event.event_type_gkey IN(18,13,16) AND sparcsn4.srv_event_field_changes.new_value IS NOT NULL AND sparcsn4.srv_event_field_changes.new_value !='' AND sparcsn4.srv_event_field_changes.new_value !='Y-CGP-.' AND sparcsn4.srv_event.gkey<(SELECT sparcsn4.srv_event.gkey FROM sparcsn4.srv_event\n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                "WHERE sparcsn4.srv_event.event_type_gkey=4 AND sparcsn4.srv_event.applied_to_gkey=a.gkey AND metafield_id='unitFlexString01' AND new_value IS NOT NULL ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1) ORDER BY sparcsn4.srv_event.gkey DESC LIMIT 1),(SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                "FROM sparcsn4.srv_event\n" +
                "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                "WHERE sparcsn4.srv_event.applied_to_gkey=a.gkey  AND sparcsn4.srv_event.event_type_gkey IN(18,13,16) ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1)) AS carrentPosition,\n" +
                "(SELECT ctmsmis.cont_yard(carrentPosition)) AS Yard_No,\n" +
                "(SELECT ctmsmis.cont_block(carrentPosition,Yard_No)) AS Block_No,\n" +
                "(SELECT creator FROM sparcsn4.srv_event WHERE applied_to_gkey=a.gkey AND event_type_gkey=30 ORDER BY gkey DESC LIMIT 1) AS stripped_by,\n" +
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
                "sparcsn4.inv_goods j ON j.gkey = a.goods\n" +
                "LEFT JOIN\n" +
                "sparcsn4.ref_bizunit_scoped k ON k.gkey = j.consignee_bzu\n" +
                "WHERE DATE(b.flex_date01)='"+fromDate+"' AND config_metafield_lov.mfdch_value NOT IN ('CANCEL','OCD','APPCUS','APPOTH','APPREF')\n" +
                ") AS tmp WHERE Yard_No='"+yardName+"' ORDER BY sl,Yard_No,shift,proEmtyDate) AS final WHERE delivery IS NULL";
        resultList=secondaryDBTemplate.query(sqlQuery,new ListOfNotStrippedAssignmentDeliveryContainersSevice.AssignmentAndDeliveryEmptyList());


        for(int i=0;i<resultList.size();i++){
            String query="";
            String iso="";
            String subIso="";

            ListOfNotStrippedAssignmentDeliveryContainersModel resultRow = resultList.get(i);
            String containerNo=resultRow.getCont_no();
            String shift=resultRow.getShift();
            query="select cont_iso_type from igm_detail_container where cont_number='"+containerNo+"'";
           List<ListOfNotStrippedAssignmentDeliveryContainersModel> contIsoTypeList= primaryDBTemplate.query(query,new ListOfNotStrippedAssignmentDeliveryContainersSevice.CotainerIsoType());
            if(contIsoTypeList.size()>0){
                for(int j=0;j<contIsoTypeList.size();j++) {

                    iso = contIsoTypeList.get(j).getCont_iso_type();
                    subIso=iso.substring(0,1);



                }
                resultRow.setCont_iso_type(iso);
                resultRow.setSubIso(subIso);

            }
            temp.add(resultRow);
        }
        lastResultModel.setStrippedAssignmentDeliveryContainersModelList(temp);
        Integer total20=0;
        Integer total40=0;
        String containers="";

        for(int j=0;j<temp.size();j++){

            ListOfNotStrippedAssignmentDeliveryContainersModel tempModel=temp.get(j);
            containers=containers+","+tempModel.getCont_no();
            if(tempModel.getSubIso().equals("2")){
                total20=total20+1;
            }
            else{
                total40=total40+1;
            }

        }
        String totalContainers=containers.substring(1);
        lastResultModel.setTotal20(total20);
        lastResultModel.setTotal40(total40);
        lastResultModel.setTotalContainers(totalContainers);
        list.add(lastResultModel);
        return list;
    }

    class AssignmentAndDeliveryEmptyList implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ListOfNotStrippedAssignmentDeliveryContainersModel assignmentAndDeliveryEmptyDetailModel=new ListOfNotStrippedAssignmentDeliveryContainersModel();
            assignmentAndDeliveryEmptyDetailModel.setCont_no(rs.getString("cont_no"));
            assignmentAndDeliveryEmptyDetailModel.setIso_code(rs.getString("iso_code"));
            assignmentAndDeliveryEmptyDetailModel.setRot_no(rs.getString("rot_no"));
            assignmentAndDeliveryEmptyDetailModel.setDischargetime(rs.getString("dischargetime"));
            assignmentAndDeliveryEmptyDetailModel.setMlo(rs.getString("mlo"));
            assignmentAndDeliveryEmptyDetailModel.setCf(rs.getString("cf"));
            assignmentAndDeliveryEmptyDetailModel.setMfdch_desc(rs.getString("mfdch_desc"));
            assignmentAndDeliveryEmptyDetailModel.setStatu(rs.getString("statu"));
            assignmentAndDeliveryEmptyDetailModel.setWeight(rs.getInt("weight"));
            assignmentAndDeliveryEmptyDetailModel.setLast_update(rs.getString("last_update"));
            assignmentAndDeliveryEmptyDetailModel.setUser_id(rs.getString("user_id"));
            assignmentAndDeliveryEmptyDetailModel.setCarrentPosition(rs.getString("carrentPosition"));
            assignmentAndDeliveryEmptyDetailModel.setYard_No(rs.getString("Yard_No"));
            assignmentAndDeliveryEmptyDetailModel.setBlock_No(rs.getString("Block_No"));
            assignmentAndDeliveryEmptyDetailModel.setStripped_by(rs.getString("stripped_by"));
            assignmentAndDeliveryEmptyDetailModel.setProEmtyDate(rs.getString("proEmtyDate"));
            assignmentAndDeliveryEmptyDetailModel.setAssignmentdate(rs.getString("assignmentdate"));
            assignmentAndDeliveryEmptyDetailModel.setStay(rs.getInt("stay"));
            assignmentAndDeliveryEmptyDetailModel.setShift(rs.getString("shift"));
            assignmentAndDeliveryEmptyDetailModel.setSl(rs.getInt("sl"));



            return assignmentAndDeliveryEmptyDetailModel;
        }

    }
    class CotainerIsoType implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ListOfNotStrippedAssignmentDeliveryContainersModel  assignmentAndDeliveryEmptyDetailModel=new ListOfNotStrippedAssignmentDeliveryContainersModel();
            assignmentAndDeliveryEmptyDetailModel.setCont_iso_type(rs.getString("cont_iso_type"));

            return assignmentAndDeliveryEmptyDetailModel;
        }
    }
}
