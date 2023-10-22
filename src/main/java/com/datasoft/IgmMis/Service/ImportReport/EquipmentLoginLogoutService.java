package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.EquipmentLoginLogout;
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
public class EquipmentLoginLogoutService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List getSearchValue(String searchCriteria) throws SQLException {
        String sqlContInfo="";
        if(searchCriteria.equals("equipmentName")){
             sqlContInfo = "select distinct logEquip as id from ctmsmis.mis_equip_log_in_out_info where logEquip like'RTG%'";
        }
        else if(searchCriteria.equals("userName")){
             sqlContInfo = "select distinct logBy as id from ctmsmis.mis_equip_log_in_out_info where logEquip like'RTG%'";
        }



        List searchValue = secondaryDBTemplate.query(sqlContInfo, new EquipmentLoginLogoutSearchValueRowMapper());
        List listAll = (List) searchValue.stream().collect(Collectors.toList());

        return listAll;
    }

    public List getEquipmentLoginLogoutList(String fromDate,String shift,  String searchCriteria, String searchValue ) throws SQLException {
        String sqlContInfo="";
        String condition="";
        if(shift.equals("Day")){
          condition="between concat('"+fromDate+"'"+",' 08:00:00') and concat('"+fromDate+"'"+",' 20:00:00')";
        }
        else{
            condition="between concat('"+fromDate+"'"+",' 20:00:00') and concat(DATE_ADD('"+fromDate+"'"+", interval 1 day),' 08:00:00')";

        }
        if(searchCriteria.equals("all")){
            sqlContInfo = "select * from ctmsmis.mis_equip_log_in_out_info where LogDate"+" " +condition+ "\n order by logDate,logEquip";
            System.out.println(sqlContInfo);
        }
        else if(searchCriteria.equals("userName")){
            sqlContInfo = "select * from ctmsmis.mis_equip_log_in_out_info where logBy='"+searchValue+"'"+" " + "and LogDate"+" "+condition+ "\n order by logDate";
            System.out.println(sqlContInfo);
        }
        else if(searchCriteria.equals("equipmentName")){
            sqlContInfo = "select * from ctmsmis.mis_equip_log_in_out_info where logEquip='"+searchValue+"'"+" " + "and LogDate"+" "+condition+ "\n order by logDate";
            System.out.println(sqlContInfo);

        }

        List equipmentLogLogoutList = secondaryDBTemplate.query(sqlContInfo, new EquipmentLoginLogoutList());
        List listAll = (List) equipmentLogLogoutList.stream().collect(Collectors.toList());

        return listAll;
    }


    class EquipmentLoginLogoutSearchValueRowMapper implements RowMapper {

        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            String seachValue= rs.getString("id");
            return seachValue;
        }
    }
    class  EquipmentLoginLogoutList implements RowMapper{

        @Override
        public EquipmentLoginLogout mapRow(ResultSet rs, int rowNum) throws SQLException {
           EquipmentLoginLogout equipmentLoginLogout =new EquipmentLoginLogout();
           equipmentLoginLogout.setgKey(rs.getInt("gKey"));
           equipmentLoginLogout.setLogDate(rs.getString("logDate"));
           equipmentLoginLogout.setLogType(rs.getString("logType"));
           equipmentLoginLogout.setLogBy(rs.getString("logBy"));
           equipmentLoginLogout.setLogEquip(rs.getString("logEquip"));
           equipmentLoginLogout.setProgram(rs.getString("program"));
            return equipmentLoginLogout;
        }
    }
}
