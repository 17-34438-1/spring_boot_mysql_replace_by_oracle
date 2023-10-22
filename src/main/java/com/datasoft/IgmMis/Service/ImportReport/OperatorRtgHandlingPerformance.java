package com.datasoft.IgmMis.Service.ImportReport;
import com.datasoft.IgmMis.Model.ImportReport.OperatorRtgHandlingPerformanceModel;
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
public class OperatorRtgHandlingPerformance {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List getOperatorRtgHandingPerformanceList( String shift,String fromDate,String fromTime,String toDate,String toTime) {
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
        sqlQuery="SELECT placed_by,full_name,SUM(impRcv) AS impRcv,SUM(keepDlv) AS keepDlv,SUM(dlvOcdOffDock) AS dlvOcdOffDock,SUM(shift) AS shift\n" +
                "FROM (\n" +
                "SELECT placed_by,\n" +
                "(CASE WHEN move_kind='DSCH' THEN 1 ELSE 0 END) AS impRcv,\n" +
                "(CASE WHEN move_kind='YARD' THEN 1 ELSE 0 END) AS keepDlv,\n" +
                "(CASE WHEN move_kind IN('DLVR','SHOB') THEN 1 ELSE 0 END) AS dlvOcdOffDock,\n" +
                "(CASE WHEN move_kind='SHFT' THEN 1 ELSE 0 END) AS shift,full_name\n" +
                "FROM\n" +
                "(\n" +
                "SELECT SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5) AS placed_by,full_name,move_kind\n" +
                "FROM sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.inv_move_event ON sparcsn4.inv_move_event.mve_gkey=sparcsn4.srv_event.gkey\n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE sparcsn4.srv_event.created "+condition+" AND creator='-xps-' AND (full_name LIKE 'RTG%')\n" +
                "AND move_kind='DSCH' AND ((SELECT COUNT(placed_by) FROM xps_user WHERE  xps_user.name = SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5))<1)\n" +
                "UNION ALL\n" +
                "SELECT SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5) AS placed_by,full_name,move_kind\n" +
                "FROM sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.inv_move_event ON sparcsn4.inv_move_event.mve_gkey=sparcsn4.srv_event.gkey\n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE sparcsn4.srv_event.created "+condition+" AND creator='-xps-' AND (full_name LIKE 'RTG%')\n" +
                "AND move_kind='SHFT' AND ((SELECT COUNT(placed_by) FROM xps_user WHERE  xps_user.name = SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5))<1)\n" +
                "UNION ALL\n" +
                "SELECT SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5) AS placed_by,full_name,move_kind\n" +
                "FROM sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.inv_move_event ON sparcsn4.inv_move_event.mve_gkey=sparcsn4.srv_event.gkey\n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE sparcsn4.srv_event.created "+condition+" AND creator='-xps-' AND (full_name LIKE 'RTG%')\n" +
                "AND move_kind='DLVR' AND ((SELECT COUNT(placed_by) FROM xps_user WHERE  xps_user.name = SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5))<1)\n" +
                "UNION ALL\n" +
                "SELECT SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5) AS placed_by,full_name,move_kind\n" +
                "FROM sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.inv_move_event ON sparcsn4.inv_move_event.mve_gkey=sparcsn4.srv_event.gkey\n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE sparcsn4.srv_event.created "+condition+" AND creator='-xps-' AND (full_name LIKE 'RTG%')\n" +
                "AND move_kind='SHOB' AND ((SELECT COUNT(placed_by) FROM xps_user WHERE  xps_user.name = SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5))<1)\n" +
                "UNION ALL\n" +
                "SELECT SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5) AS placed_by,full_name,move_kind\n" +
                "FROM sparcsn4.srv_event \n" +
                "INNER JOIN sparcsn4.inv_move_event ON sparcsn4.inv_move_event.mve_gkey=sparcsn4.srv_event.gkey\n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE sparcsn4.srv_event.created "+condition+" AND creator='-xps-' AND (full_name LIKE 'RTG%')\n" +
                "AND move_kind='YARD' AND ((SELECT COUNT(placed_by) FROM xps_user WHERE  xps_user.name = SUBSTRING(SUBSTRING_INDEX(placed_by,':',2),5))<1)\n" +
                ") AS tbl ) AS final GROUP BY placed_by,full_name ORDER BY placed_by,full_name";
        System.out.println("query "+ sqlQuery);


        List resultList=secondaryDBTemplate.query(sqlQuery,new OperatorRtgHandlingPerformanceList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        System.out.println("length: "+listAll.size());

        return listAll;
    }

    class OperatorRtgHandlingPerformanceList implements RowMapper{

        @Override
        public OperatorRtgHandlingPerformanceModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OperatorRtgHandlingPerformanceModel operatorRtgHandlingPerformanceModel=new OperatorRtgHandlingPerformanceModel();
            operatorRtgHandlingPerformanceModel.setPlaced_by(rs.getString("placed_by"));

            operatorRtgHandlingPerformanceModel.setFull_name(rs.getString("full_name"));
            System.out.println(rs.getString("full_name"));
            operatorRtgHandlingPerformanceModel.setImpRcv(rs.getInt("impRcv"));
            System.out.println(rs.getInt("impRcv"));
            operatorRtgHandlingPerformanceModel.setKeepDlv(rs.getInt("keepDlv"));
            System.out.println(rs.getInt("keepDlv"));
            operatorRtgHandlingPerformanceModel.setDlvOcdOffDock(rs.getInt("dlvOcdOffDock"));
            System.out.println(rs.getInt("dlvOcdOffDock"));
            operatorRtgHandlingPerformanceModel.setShift(rs.getInt("shift"));
            System.out.println(rs.getInt("shift"));

            operatorRtgHandlingPerformanceModel.setTotalHandingBox(rs.getInt("impRcv")+rs.getInt("keepDlv")+rs.getInt("dlvOcdOffDock")+rs.getInt("shift"));

            return operatorRtgHandlingPerformanceModel;
        }
    }
}
