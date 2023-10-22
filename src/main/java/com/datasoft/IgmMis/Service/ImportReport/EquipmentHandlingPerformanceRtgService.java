package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.EquipmentHandlingPerformanceRtgModel;
import com.datasoft.IgmMis.Model.ImportReport.LogDateModel;
import com.datasoft.IgmMis.Model.ImportReport.LogInfoModel;
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
public class EquipmentHandlingPerformanceRtgService {
    private  String condition="";
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List getEquipmentPerformanceRtgList(String shift, String fromDate, String fromTime, String toDate, String toTime) {

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
        sqlQuery="select eq,created_by,sum(impRcv) as impRcv,sum(keepDlv) as keepDlv,sum(dlvOcdOffDock) as dlvOcdOffDock,sum(shift) as shift\n" +
                "from(\n" +
                "select full_name as eq,created_by,\n" +
                "(case when move_kind='DSCH' then 1 else 0 end) as impRcv,\n" +
                "(case when move_kind='YARD' then 1 else 0 end) as keepDlv,\n" +
                "(case when move_kind in('DLVR','SHOB') then 1 else 0 end) as dlvOcdOffDock,\n" +
                "(case when move_kind='SHFT' then 1 else 0 end) as shift\n" +
                "from\n" +
                "(\n" +
                "select full_name,move_kind,\n" +
                "(select  placed_by from sparcsn4.srv_event where srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  as created_by\n" +
                "from sparcsn4.inv_move_event \n" +
                "inner join sparcsn4.xps_che on (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch or sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry or sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "where t_put "+condition+" and (full_name like 'RTG%') and move_kind='DSCH' \n" +
                "\n" +
                "union all\n" +
                "\n" +
                "select full_name,move_kind,\n" +
                "(select  placed_by from sparcsn4.srv_event where srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  as created_by\n" +
                "from sparcsn4.inv_move_event \n" +
                "inner join sparcsn4.xps_che on (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch or sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry or sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "where t_put "+condition+" and (full_name like 'RTG%') and move_kind='SHFT' \n" +
                "\n" +
                "union all\n" +
                "\n" +
                "select full_name,move_kind,\n" +
                "(select  placed_by from sparcsn4.srv_event where srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  as created_by\n" +
                "from sparcsn4.inv_move_event \n" +
                "inner join sparcsn4.xps_che on (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch or sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry or sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "where t_put "+condition+" and (full_name like 'RTG%') and move_kind='DLVR' \n" +
                "\n" +
                "union all\n" +
                "\n" +
                "select full_name,move_kind,\n" +
                "(select  placed_by from sparcsn4.srv_event where srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  as created_by\n" +
                "from sparcsn4.inv_move_event \n" +
                "inner join sparcsn4.xps_che on (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch or sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry or sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "where t_put "+condition+" and (full_name like 'RTG%') and move_kind='SHOB' \n" +
                "\n" +
                "union all\n" +
                "\n" +
                "select full_name,move_kind,\n" +
                "(select  placed_by from sparcsn4.srv_event where srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  as created_by\n" +
                "from sparcsn4.inv_move_event \n" +
                "inner join sparcsn4.xps_che on (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch or sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry or sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "where t_put "+condition+" and (full_name like 'RTG%') and move_kind='YARD' order by full_name,move_kind) as t) as f group by eq ";
        System.out.println("query "+ sqlQuery);


        List resultList=secondaryDBTemplate.query(sqlQuery,new EquipmentHandlingPerformanceRtgList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        System.out.println("length: "+listAll.size());

        return listAll;
    }
    class EquipmentHandlingPerformanceRtgList implements RowMapper {

        @Override
        public EquipmentHandlingPerformanceRtgModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            String sqlQuery="";
            String query="";
            EquipmentHandlingPerformanceRtgModel equipmentHandlingPerformanceRtgModel=new EquipmentHandlingPerformanceRtgModel();
            String eq=rs.getString("eq");
            equipmentHandlingPerformanceRtgModel.setEq(rs.getString("eq"));
            equipmentHandlingPerformanceRtgModel.setImpRcv(rs.getInt("impRcv"));
           // equipmentHandlingPerformanceRtgModel.setShort_name(rs.getString("short_name"));
            equipmentHandlingPerformanceRtgModel.setKeepDlv(rs.getInt("keepDlv"));
            equipmentHandlingPerformanceRtgModel.setDlvOcdOffDock(rs.getInt("dlvOcdOffDock"));
            equipmentHandlingPerformanceRtgModel.setShift(rs.getInt("shift"));
            equipmentHandlingPerformanceRtgModel.setTotalHandingBox(rs.getInt("impRcv")+rs.getInt("keepDlv")+rs.getInt("dlvOcdOffDock")+rs.getInt("shift"));
            sqlQuery="select logDate,logBy from ctmsmis.mis_equip_log_in_out_info where logEquip='"+eq+"' and logType='in' and  logDate "+condition+" order by logDate limit 1";
            List<LogInfoModel> result=secondaryDBTemplate.query(sqlQuery,new LogInfo());
            LogInfoModel logInfoModel;
           for(int i=0; i<result.size();i++){
                logInfoModel=result.get(i);
               equipmentHandlingPerformanceRtgModel.setLog_in_time(logInfoModel.getLogDate());
               equipmentHandlingPerformanceRtgModel.setLog_by(logInfoModel.getLogBy());
           }
           query="select logDate from ctmsmis.mis_equip_log_in_out_info where logEquip='"+eq+"' and logType='out' and  logDate "+condition+"  order by logDate desc limit 1";
           List<LogDateModel> res=secondaryDBTemplate.query(query,new LogDate());
           LogDateModel logDateModel;
           for(int i=0; i<res.size();i++){
               logDateModel=res.get(i);
               equipmentHandlingPerformanceRtgModel.setLog_out_time(logDateModel.getLogDate());
           }
  return equipmentHandlingPerformanceRtgModel;
        }

    }
   class LogInfo implements RowMapper{

       @Override
       public LogInfoModel mapRow(ResultSet rs, int rowNum) throws SQLException {
           LogInfoModel logInfoModel= new LogInfoModel();
           logInfoModel.setLogDate(rs.getString("logDate"));
           logInfoModel.setLogBy(rs.getString("logBy"));
           return logInfoModel;
       }
   }
    class LogDate implements RowMapper{

        @Override
        public LogDateModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            LogDateModel logDateModel=new LogDateModel();
            logDateModel.setLogDate(rs.getString("logDate"));

            return logDateModel;
        }
    }
}

