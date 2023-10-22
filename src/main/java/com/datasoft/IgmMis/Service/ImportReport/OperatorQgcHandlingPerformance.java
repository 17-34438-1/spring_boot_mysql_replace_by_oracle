package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.OperatorQgcHandlingPerformanceModel;
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
public class OperatorQgcHandlingPerformance {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List getOperatorQgcHandingPerformanceList(String shift, String fromDate, String fromTime, String toDate, String toTime) {
        String condition="";
        String sqlQuery="";
        if(shift.equals("TimeWise")){
            fromTime=fromTime+":00";
            toTime=toTime+":00";
            condition = "between concat('"+fromDate+"',' "+fromTime+"') and concat('"+toDate+"',' "+toTime+"')";
        }

        else if(shift.equals("Day")){
            condition = "between concat('"+fromDate+"',' 08:00:00') and concat('"+fromDate+"',' 20:00:00')";
        }

        else{
            condition = "between concat('"+fromDate+"',' 20:00:00') and concat(DATE_ADD('"+fromDate+"', interval 1 day),' 08:00:00')";
        }
        sqlQuery="SELECT placed_by,full_name,SUM(move_kind) AS total_move FROM(\n" +
                "SELECT SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5) AS placed_by,full_name,IF(move_kind='DSCH',1,0) AS move_kind\n" +
                "FROM sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.inv_move_event ON sparcsn4.inv_move_event.mve_gkey=sparcsn4.srv_event.gkey\n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE sparcsn4.srv_event.created "+condition+"  AND creator='-xps-' AND (full_name LIKE 'QGC%')\n" +
                " AND ((SELECT COUNT(placed_by) FROM xps_user WHERE  xps_user.name = SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5))<1) \n" +
                ") AS t1 \n" +
                "GROUP BY placed_by,full_name";
        System.out.println("query "+ sqlQuery);


        List resultList=secondaryDBTemplate.query(sqlQuery,new OperatorQgcHandlingPerformanceList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        System.out.println("length: "+listAll.size());

        return listAll;
    }

   class OperatorQgcHandlingPerformanceList implements RowMapper{

       @Override
       public OperatorQgcHandlingPerformanceModel mapRow(ResultSet rs, int rowNum) throws SQLException {
           OperatorQgcHandlingPerformanceModel operatorQgcHandlingPerformanceModel=new OperatorQgcHandlingPerformanceModel();
           operatorQgcHandlingPerformanceModel.setPlaced_by(rs.getString("placed_by"));
           operatorQgcHandlingPerformanceModel.setFull_name(rs.getString("full_name"));
           operatorQgcHandlingPerformanceModel.setTotal_move(rs.getInt("total_move"));
           operatorQgcHandlingPerformanceModel.setTotalHandingBox(rs.getInt("total_move"));

           return operatorQgcHandlingPerformanceModel;
       }
   }

}
