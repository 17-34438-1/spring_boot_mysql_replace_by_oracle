package com.datasoft.IgmMis.Service.ShahinSpecialReport;

import com.datasoft.IgmMis.Model.ShahinSpecialReport.DateModel;
import com.datasoft.IgmMis.Model.ShahinSpecialReport.InfoModel;
import com.datasoft.IgmMis.Model.ShahinSpecialReport.EquipmentHandlingPerformanceHistory;
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
public class EquipmentHandlingPerformanceHistoryService {
    private String cond="";
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;

    public List EquipmentHandlingPerformance(String shift, String fromdate, String todate, String fromTime, String totime){

        System.out.println("Shift:"+shift);
        System.out.println("Fromdate:"+fromdate);
        System.out.println("Todate:"+todate);
        System.out.println("fromTime:"+fromTime);
        System.out.println("toTime:"+totime);
        String Query="";
        String eq="";
        String arr="";
        if(shift.equals("timewise"))
            cond = "between concat('"+fromdate+"',' "+fromTime+"') and concat('"+todate+"',' "+totime+"')";
        else if(shift.equals("Day"))
            cond = "between concat('"+fromdate+"',' 08:00:00') and concat('"+fromdate+"',' 20:00:00')";
        else
            cond = "between concat('"+fromdate+"',' 20:00:00') and concat(DATE_ADD('"+fromdate+"', interval 1 day),' 08:00:00')";

        Query="SELECT eq,created_by,SUM(impRcv) AS impRcv,SUM(keepDlv) AS keepDlv,SUM(dlvOcdOffDock) AS dlvOcdOffDock,SUM(shift) AS shift\n" +
                "FROM(\n" +
                "SELECT full_name AS eq,created_by,\n" +
                "(CASE WHEN move_kind='DSCH' THEN 1 ELSE 0 END) AS impRcv,\n" +
                "(CASE WHEN move_kind='YARD' THEN 1 ELSE 0 END) AS keepDlv,\n" +
                "(CASE WHEN move_kind IN('DLVR','SHOB') THEN 1 ELSE 0 END) AS dlvOcdOffDock,\n" +
                "(CASE WHEN move_kind='SHFT' THEN 1 ELSE 0 END) AS shift\n" +
                "FROM\n" +
                "(\n" +
                "SELECT full_name,move_kind,\n" +
                "(SELECT  placed_by FROM sparcsn4.srv_event WHERE srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  AS created_by\n" +
                "FROM sparcsn4.inv_move_event \n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE t_put "+cond+" AND (full_name LIKE 'RTG%') AND move_kind='DSCH' \n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT full_name,move_kind,\n" +
                "(SELECT  placed_by FROM sparcsn4.srv_event WHERE srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  AS created_by\n" +
                "FROM sparcsn4.inv_move_event \n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE t_put "+cond+" AND (full_name LIKE 'RTG%') AND move_kind='SHFT' \n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT full_name,move_kind,\n" +
                "(SELECT  placed_by FROM sparcsn4.srv_event WHERE srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  AS created_by\n" +
                "FROM sparcsn4.inv_move_event \n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE t_put "+cond+" AND (full_name LIKE 'RTG%') AND move_kind='DLVR' \n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT full_name,move_kind,\n" +
                "(SELECT  placed_by FROM sparcsn4.srv_event WHERE srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  AS created_by\n" +
                "FROM sparcsn4.inv_move_event \n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE t_put "+cond+" AND (full_name LIKE 'RTG%') AND move_kind='SHOB' \n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT full_name,move_kind,\n" +
                "(SELECT  placed_by FROM sparcsn4.srv_event WHERE srv_event.gkey=sparcsn4.inv_move_event.mve_gkey)  AS created_by\n" +
                "FROM sparcsn4.inv_move_event \n" +
                "INNER JOIN sparcsn4.xps_che ON (sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_fetch OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_carry OR sparcsn4.xps_che.gkey=sparcsn4.inv_move_event.che_put)\n" +
                "WHERE t_put "+cond+" AND (full_name LIKE 'RTG%') AND move_kind='YARD' ORDER BY full_name,move_kind) AS t) AS f GROUP BY eq ";

        System.out.println(Query);
        List<EquipmentHandlingPerformanceHistory> resultList=SecondaryDBTemplate.query(Query,new ContainerNotFound_Eq());

        System.out.println(resultList);
        EquipmentHandlingPerformanceHistory equipmentHandlingPerormanceHistory;
        for(int i=0;i<resultList.size();i++){
            equipmentHandlingPerormanceHistory=resultList.get(i);
            eq=equipmentHandlingPerormanceHistory.getEq();
            arr=equipmentHandlingPerormanceHistory.getCreated_by();

        }

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }



    class ContainerNotFound_Eq implements RowMapper {
        @Override
        public EquipmentHandlingPerformanceHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
            String sqlQuery="";
            String sql="";
            EquipmentHandlingPerformanceHistory equipmentHandlingPerormanceHistory=new EquipmentHandlingPerformanceHistory();
            String eq=rs.getString("eq");
            equipmentHandlingPerormanceHistory.setEq(rs.getString("eq"));
            equipmentHandlingPerormanceHistory.setCreated_by(rs.getString("created_by"));
            String arr=rs.getString("created_by");
            String[] split = arr.split(":");
            String short_name=split[1];
            String[] splited = short_name.split("\\s+");
            equipmentHandlingPerormanceHistory.setShort_name(short_name);
            equipmentHandlingPerormanceHistory.setImpRcv(rs.getInt("impRcv"));
            equipmentHandlingPerormanceHistory.setKeepDlv(rs.getInt("keepDlv"));
            equipmentHandlingPerormanceHistory.setDlvOcdOffDock(rs.getInt("dlvOcdOffDock"));
            equipmentHandlingPerormanceHistory.setShift(rs.getInt("shift"));

            equipmentHandlingPerormanceHistory.setTotalHandingBox(rs.getInt("impRcv")+rs.getInt("keepDlv")+rs.getInt("dlvOcdOffDock")+rs.getInt("shift"));
            sqlQuery="select logDate,logBy from ctmsmis.mis_equip_log_in_out_info where logEquip='"+eq+"' and logType='in' and  logDate "+cond+" order by logDate limit 1";
            List<DateModel> result=SecondaryDBTemplate.query(sqlQuery,new LogInfo());

            DateModel dateModel;
            System.out.println("Query:"+sqlQuery);
            for(int i=0; i<result.size();i++){
                dateModel=result.get(i);
                equipmentHandlingPerormanceHistory.setLog_In(dateModel.getLogDate());
                equipmentHandlingPerormanceHistory.setLogBy(dateModel.getLogBy());
            }
            sqlQuery="select logDate from ctmsmis.mis_equip_log_in_out_info where logEquip='"+eq+"' and logType='out' and  logDate "+cond+"  order by logDate desc limit 1";
            List<InfoModel> res=SecondaryDBTemplate.query(sqlQuery,new LogDate());
            InfoModel infoModel;
            System.out.println("Sql:"+sqlQuery);
            for(int i=0; i<res.size();i++){
                infoModel=res.get(i);
                equipmentHandlingPerormanceHistory.setLog_Out(infoModel.getLogDate());
            }
            return equipmentHandlingPerormanceHistory;
        }



        class LogInfo implements RowMapper{

            @Override
            public DateModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                DateModel dateModel= new DateModel();
                dateModel.setLogDate(rs.getTimestamp("logDate"));
                dateModel.setLogBy(rs.getString("logBy"));
                return dateModel;
            }
        }

        class LogDate implements RowMapper{
            @Override
            public InfoModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                InfoModel infoModel=new InfoModel();
                infoModel.setLogDate(rs.getTimestamp("logDate"));
                return infoModel;
            }
        }
    }




}