package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportEquipmentHandlingPerformanceRTG;
import com.datasoft.IgmMis.Model.ExportReport.LogDateModel;
import com.datasoft.IgmMis.Model.ExportReport.LogInfoModel;
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
public class ExportEquipmentHandlingPerformanceRTGService {

    private String cond="";
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    public List EquipmentHandlingPerformanceRTG(String shift, String fromdate, String todate, String fromTime, String totime){

        String Query="";
        String eq="";
        String arr="";
        if(shift.equals("timewise"))
            cond = "between concat('"+fromdate+"',' "+fromTime+"') and concat('"+todate+"',' "+totime+"')";
        else if(shift.equals("Day"))
            cond = "between concat('"+fromdate+"',' 08:00:00') and concat('"+fromdate+"',' 20:00:00')";
        else
            cond = "between concat('"+fromdate+"',' 20:00:00') and concat(DATE_ADD('"+fromdate+"', interval 1 day),' 08:00:00')";

        Query="SELECT eq, created_by,SUM(expRcv) AS expRcv,SUM(yardMove) AS yardMove,SUM(expShift) AS expShift\n" +
                "FROM(\n" +
                "SELECT full_name AS eq,created_by,\n" +
                "(CASE WHEN move_kind='RECV' THEN 1 ELSE 0 END) AS expRcv,\n" +
                "(CASE WHEN move_kind='YARD' THEN 1 ELSE 0 END) AS yardMove,\n" +
                "(CASE WHEN move_kind='SHFT' THEN 1 ELSE 0 END) AS expShift\n" +
                " FROM\n" +
                "(\n" +
                "SELECT full_name,move_kind,\n" +
                "(SELECT  placed_by FROM sparcsn4.srv_event WHERE srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  AS created_by\n" +
                "FROM sparcsn4.inv_move_event \n" +
                "\n" +
                "INNER JOIN sparcsn4.srv_event ON  sparcsn4.srv_event.gkey=sparcsn4.inv_move_event.mve_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit ON sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey\n" +
                "\n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE t_put "+cond+" AND CONCAT('2021-10-04',' 20:00:00') AND (full_name LIKE 'RTG%') AND move_kind='RECV' \n" +
                "AND inv_unit.category='EXPRT'\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT full_name,move_kind,\n" +
                "(SELECT  placed_by FROM sparcsn4.srv_event WHERE srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  AS created_by\n" +
                "\n" +
                "FROM sparcsn4.inv_move_event \n" +
                "INNER JOIN sparcsn4.srv_event ON  sparcsn4.srv_event.gkey=sparcsn4.inv_move_event.mve_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit ON sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey\n" +
                "\n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE t_put "+cond+" AND CONCAT('2021-09-26',' 20:00:00') AND (full_name LIKE 'RTG%') AND move_kind='SHFT' \n" +
                "AND inv_unit.category='EXPRT'\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT full_name,move_kind,\n" +
                "(SELECT  placed_by FROM sparcsn4.srv_event WHERE srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  AS created_by\n" +
                "FROM sparcsn4.inv_move_event \n" +
                "INNER JOIN sparcsn4.srv_event ON  sparcsn4.srv_event.gkey=sparcsn4.inv_move_event.mve_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit ON sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey\n" +
                "\n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE t_put "+cond+" AND CONCAT('2021-10-04',' 20:00:00') AND (full_name LIKE 'RTG%') AND move_kind='YARD' \n" +
                "AND inv_unit.category='EXPRT'\n" +
                "\n" +
                "ORDER BY full_name,move_kind) AS t) AS f GROUP BY eq";
        System.out.println(Query);
        List<ExportEquipmentHandlingPerformanceRTG> resultList=SecondaryDBTemplate.query(Query,new ContainerNotFound_Eq());

        System.out.println(resultList);
        ExportEquipmentHandlingPerformanceRTG exportEquipmentHandlingPerformanceRTG;
        for(int i=0;i<resultList.size();i++){
            exportEquipmentHandlingPerformanceRTG=resultList.get(i);
            eq=exportEquipmentHandlingPerformanceRTG.getEq();
            arr=exportEquipmentHandlingPerformanceRTG.getCreated_by();
            System.out.println("eq Type All:"+eq);
            System.out.println("crated_by:"+arr);
        }
        System.out.println("eq Type:"+eq);
        System.out.println("crated_by:"+arr);
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class ContainerNotFound_Eq implements RowMapper {
        @Override
        public ExportEquipmentHandlingPerformanceRTG mapRow(ResultSet rs, int rowNum) throws SQLException {
            String sqlQuery="";
            String sql="";
            ExportEquipmentHandlingPerformanceRTG exportEquipmentHandlingPerformanceRTG=new ExportEquipmentHandlingPerformanceRTG();
            String eq=rs.getString("eq");
            exportEquipmentHandlingPerformanceRTG.setEq(rs.getString("eq"));
            exportEquipmentHandlingPerformanceRTG.setCreated_by(rs.getString("created_by"));
            String arr=rs.getString("created_by");
            String[] split = arr.split(":");
            String short_name=split[1];
            String[] splited = short_name.split("\\s+");
            exportEquipmentHandlingPerformanceRTG.setShort_name(short_name);
            exportEquipmentHandlingPerformanceRTG.setExpRcv(rs.getInt("expRcv"));
            exportEquipmentHandlingPerformanceRTG.setYardMove(rs.getInt("yardMove"));
            exportEquipmentHandlingPerformanceRTG.setExpShift(rs.getInt("expShift"));
            exportEquipmentHandlingPerformanceRTG.setTotalHandingBox(rs.getInt("expRcv")+rs.getInt("yardMove")+rs.getInt("expShift"));
            sqlQuery="select logDate,logBy from ctmsmis.mis_equip_log_in_out_info where logEquip='"+eq+"' and logType='in' and  logDate "+cond+" order by logDate limit 1";
            List<LogDateModel> result=SecondaryDBTemplate.query(sqlQuery,new LogInfo());

            LogDateModel logDateModel;
            System.out.println("Query:"+sqlQuery);
            for(int i=0; i<result.size();i++){

                logDateModel=result.get(i);
                exportEquipmentHandlingPerformanceRTG.setLog_in_time(logDateModel.getLogDate());
                exportEquipmentHandlingPerformanceRTG.setLogBy(logDateModel.getLogBy());
            }
            sqlQuery="select logDate from ctmsmis.mis_equip_log_in_out_info where logEquip='"+eq+"' and logType='out' and  logDate "+cond+"  order by logDate desc limit 1";
            List<LogInfoModel> res=SecondaryDBTemplate.query(sqlQuery,new LogDate());
            LogInfoModel logDate;
            System.out.println("Sql:"+sqlQuery);
            for(int i=0; i<res.size();i++){
                logDate=res.get(i);

                exportEquipmentHandlingPerformanceRTG.setLog_out_time(logDate.getLogDate());
            }
            return exportEquipmentHandlingPerformanceRTG;
        }
    }

    class LogInfo implements RowMapper{

        @Override
        public LogDateModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            LogDateModel logDateModel= new LogDateModel();
            logDateModel.setLogDate(rs.getTimestamp("logDate"));
            logDateModel.setLogBy(rs.getString("logBy"));
            return logDateModel;
        }
    }
    class LogDate implements RowMapper{

        @Override
        public LogInfoModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            LogInfoModel logInfoModel=new LogInfoModel();
            logInfoModel.setLogDate(rs.getTimestamp("logDate"));
            return logInfoModel;
        }
    }
}
