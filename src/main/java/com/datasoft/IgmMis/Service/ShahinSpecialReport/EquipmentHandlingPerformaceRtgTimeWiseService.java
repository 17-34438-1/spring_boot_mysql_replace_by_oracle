package com.datasoft.IgmMis.Service.ShahinSpecialReport;

import com.datasoft.IgmMis.Model.ShahinSpecialReport.EquipmentHandlingPerformaceRtgTimeWiseModel;
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
public class EquipmentHandlingPerformaceRtgTimeWiseService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

  public List equipmentHandlingPerformaceRtgTimeWise1(String fromDate, String shift){
      List<EquipmentHandlingPerformaceRtgTimeWiseModel> resultList= new ArrayList<>();
      EquipmentHandlingPerformaceRtgTimeWiseModel resultModel= new EquipmentHandlingPerformaceRtgTimeWiseModel();
      List<EquipmentHandlingPerformaceRtgTimeWiseModel> queryList=new ArrayList<>();
      String sqlQuery="";
      if(shift.equals("Night")){
          sqlQuery="SELECT eq,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 20:00:00') AND CONCAT('"+fromDate+" ',' 20:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 20:00:00') AND CONCAT('"+fromDate+" ',' 20:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_08_09,\n" +
                  "\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 21:00:00') AND CONCAT('"+fromDate+" ',' 21:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 21:00:00') AND CONCAT('"+fromDate+" ',' 21:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_09_10,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 22:00:00') AND CONCAT('"+fromDate+" ',' 22:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 22:00:00') AND CONCAT('"+fromDate+" ',' 22:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_10_11,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 23:00:00') AND CONCAT('"+fromDate+" ','23:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 23:00:00') AND CONCAT('"+fromDate+" ',' 23:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_11_12,\n" +
                  "\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 00:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 00:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 00:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 00:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_12_13,\n" +
                  "\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 01:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 01:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 01:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 01:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_13_14,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 02:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 02:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 02:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 02:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_14_15,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 03:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 03:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 03:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 03:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_15_16,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 04:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 04:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 04:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 04:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_16_17,\n" +
                  "\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 05:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 05:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 05:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 05:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_17_18,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 06:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 06:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 06:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 06:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_18_19,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 07:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 07:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 07:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 07:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_19_20\n" +
                  "\n" +
                  "\t\t\t\tFROM(\n" +
                  "\t\t\t\tSELECT sparcsn4.xps_che.gkey,full_name AS eq\n" +
                  "\t\t\t\tFROM sparcsn4.xps_che \n" +
                  "\t\t\t\tWHERE full_name LIKE 'RTG%' AND full_name !='RTG200' \n" +
                  "\t\t\t\tORDER BY full_name \n" +
                  "\t\t\t\t) AS tbl GROUP BY eq";

      }
      else if(shift.equals("Day")){
          sqlQuery="SELECT eq,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 08:00:00') AND CONCAT('"+fromDate+" ',' 08:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 08:00:00') AND CONCAT('"+fromDate+" ',' 08:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_08_09,\n" +
                  "\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 09:00:00') AND CONCAT('"+fromDate+" ',' 09:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 09:00:00') AND CONCAT('"+fromDate+" ',' 09:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_09_10,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 10:00:00') AND CONCAT('"+fromDate+" ',' 10:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 10:00:00') AND CONCAT('"+fromDate+" ',' 10:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_10_11,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 11:00:00') AND CONCAT('"+fromDate+" ','11:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 11:00:00') AND CONCAT('"+fromDate+" ',' 11:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_11_12,\n" +
                  "\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 12:00:00') AND CONCAT('"+fromDate+" ',' 12:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 12:00:00') AND CONCAT('"+fromDate+" ',' 12:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_12_13,\n" +
                  "\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 13:00:00') AND CONCAT('"+fromDate+" ',' 13:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 13:00:00') AND CONCAT('"+fromDate+" ',' 13:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_13_14,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 14:00:00') AND CONCAT('"+fromDate+" ',' 14:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 14:00:00') AND CONCAT('"+fromDate+" ',' 14:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_14_15,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 15:00:00') AND CONCAT('"+fromDate+" ',' 15:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 15:00:00') AND CONCAT('"+fromDate+" ',' 15:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_15_16,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 16:00:00') AND CONCAT('"+fromDate+" ',' 16:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 16:00:00') AND CONCAT('"+fromDate+" ',' 16:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_16_17,\n" +
                  "\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 17:00:00') AND CONCAT('"+fromDate+" ',' 17:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 17:00:00') AND CONCAT('"+fromDate+" ',' 17:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_17_18,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 18:00:00') AND CONCAT('"+fromDate+" ',' 18:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 18:00:00') AND CONCAT('"+fromDate+" ',' 18:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_18_19,\n" +
                  "\t\t\t\t((SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_fetch=tbl.gkey AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 19:00:00') AND CONCAT('"+fromDate+" ',' 19:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                  "\t\t\t\t+\n" +
                  "\t\t\t\t(SELECT COUNT(*)\n" +
                  "\t\t\t\tFROM sparcsn4.inv_move_event\n" +
                  "\t\t\t\tWHERE che_put=tbl.gkey AND t_put BETWEEN CONCAT('"+fromDate+" ',' 19:00:00') AND CONCAT('"+fromDate+" ',' 19:59:59')\n" +
                  "\t\t\t\tAND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                  "\t\t\t\t) AS t_19_20\n" +
                  "\n" +
                  "\t\t\t\tFROM(\n" +
                  "\t\t\t\tSELECT sparcsn4.xps_che.gkey,full_name AS eq\n" +
                  "\t\t\t\tFROM sparcsn4.xps_che \n" +
                  "\t\t\t\tWHERE full_name LIKE 'RTG%' AND full_name !='RTG200' \n" +
                  "\t\t\t\tORDER BY full_name \n" +
                  "\t\t\t\t) AS tbl GROUP BY eq";

      }
      System.out.println(sqlQuery);
     // queryList=secondaryDBTemplate.query(sqlQuery,new EquipmentHandlingPerformaceRtgTimeWiseService.EquipmentHandingPerformace());

      Integer sum_08_09=0;
      Integer sum_09_10=0;
      Integer sum_10_11=0;
      Integer sum_11_12=0;
      Integer sum_12_13=0;
      Integer sum_13_14=0;
      Integer sum_14_15=0;
      Integer sum_15_16=0;
      Integer sum_16_17=0;
      Integer sum_17_18=0;
      Integer sum_18_19=0;
      Integer sum_19_20=0;
      List resultSubList=new ArrayList();
      for(int i=0;i<queryList.size();i++){

           EquipmentHandlingPerformaceRtgTimeWiseModel queryModel=queryList.get(i);
          resultSubList.add(queryModel);
           sum_08_09=sum_08_09+queryModel.getT_08_09();
           sum_09_10=sum_09_10+queryModel.getT_09_10();
           sum_10_11=sum_10_11+queryModel.getT_10_11();
           sum_11_12=sum_11_12+queryModel.getT_11_12();
           sum_12_13=sum_12_13+queryModel.getT_12_13();
           sum_13_14=sum_13_14+queryModel.getT_13_14();
           sum_14_15=sum_14_15+queryModel.getT_14_15();
           sum_15_16=sum_15_16+queryModel.getT_15_16();
           sum_16_17=sum_16_17+queryModel.getT_16_17();
           sum_17_18=sum_17_18+queryModel.getT_17_18();
           sum_18_19=sum_18_19+queryModel.getT_18_19();
           sum_19_20=sum_19_20+queryModel.getT_19_20();
      }
      resultModel.setEquipmentRtgList(resultSubList);

      resultModel.setSum_08_09(sum_08_09);
      resultModel.setSum_09_10(sum_09_10);
      resultModel.setSum_10_11(sum_10_11);
      resultModel.setSum_11_12(sum_11_12);
      resultModel.setSum_12_13(sum_12_13);
      resultModel.setSum_13_14(sum_13_14);
      resultModel.setSum_14_15(sum_14_15);
      resultModel.setT_15_16(sum_15_16);
      resultModel.setT_16_17(sum_16_17);
      resultModel.setT_17_18(sum_17_18);
      resultModel.setT_18_19(sum_18_19);
      resultModel.setT_19_20(sum_19_20);
      resultModel.setTotal(sum_08_09+sum_09_10+sum_10_11+ sum_11_12+ sum_12_13+sum_14_15+ sum_15_16+ sum_16_17+sum_17_18+ sum_18_19+sum_19_20);

      resultList.add(resultModel);


      return resultList;
  }
    public List equipmentHandlingPerformaceRtgTimeWise(String fromDate, String shift){
        List<EquipmentHandlingPerformaceRtgTimeWiseModel> resultList= new ArrayList<>();
        EquipmentHandlingPerformaceRtgTimeWiseModel resultModel= new EquipmentHandlingPerformaceRtgTimeWiseModel();
        List<EquipmentHandlingPerformaceRtgTimeWiseModel> queryList=new ArrayList<>();
        String sqlQuery="";
        Integer sum_08_09=0;
        Integer sum_09_10=0;
        Integer sum_10_11=0;
        Integer sum_11_12=0;
        Integer sum_12_13=0;
        Integer sum_13_14=0;
        Integer sum_14_15=0;
        Integer sum_15_16=0;
        Integer sum_16_17=0;
        Integer sum_17_18=0;
        Integer sum_18_19=0;
        Integer sum_19_20=0;
        sqlQuery="SELECT sparcsn4.xps_che.gkey,full_name AS eq\n" +
                "FROM sparcsn4.xps_che \n" +
                "WHERE full_name LIKE 'RTG%' AND full_name !='RTG200' \n" +
                "ORDER BY full_name ";
        queryList=secondaryDBTemplate.query(sqlQuery,new EquipmentHandlingPerformaceRtgTimeWiseService.GkeyAndEq());
        List<EquipmentHandlingPerformaceRtgTimeWiseModel> resultSubList=new ArrayList();
        for(int i=0;i<queryList.size();i++){
            Integer totalRes=0;
            EquipmentHandlingPerformaceRtgTimeWiseModel resultSubModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            Integer gkey=0;
            String eq="";
            gkey=queryList.get(i).getGkey();
            eq=queryList.get(i).getEq();
            resultSubModel.setEq(eq);

            String t_08_09_query="";
            Integer t_08_09=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_08_09_List=new ArrayList();
            if(shift.equals("Day")){
                t_08_09_query="SELECT((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 08:00:00') AND CONCAT('"+fromDate+" ',' 08:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 08:00:00') AND CONCAT('"+fromDate+" ',' 08:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_08_09";
            }
            else if(shift.equals("Night")){
                t_08_09_query="SELECT((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 20:00:00') AND CONCAT('"+fromDate+" ',' 20:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 20:00:00') AND CONCAT('"+fromDate+" ',' 20:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_08_09";
            }
            t_08_09_List=secondaryDBTemplate.query(t_08_09_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_08_09());
            if(t_08_09_List.size()>0){
                t_08_09= t_08_09_List.get(0).getT_08_09();
            }

            resultSubModel.setT_08_09(t_08_09);
            sum_08_09=sum_08_09+t_08_09;

            String t_09_10_query="";
            Integer t_09_10=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_09_10_List=new ArrayList();
            if(shift.equals("Day")){
                t_09_10_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 09:00:00') AND CONCAT('"+fromDate+" ',' 09:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 09:00:00') AND CONCAT('"+fromDate+" ',' 09:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_09_10";
            }
            else if(shift.equals("Night")){
                t_09_10_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 21:00:00') AND CONCAT('"+fromDate+" ',' 21:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 21:00:00') AND CONCAT('"+fromDate+" ',' 21:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_09_10";
            }
            t_09_10_List=secondaryDBTemplate.query(t_09_10_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_09_10());
            if(t_09_10_List.size()>0){
                t_09_10= t_09_10_List.get(0).getT_09_10();
            }
            resultSubModel.setT_09_10(t_09_10);
            sum_09_10=sum_09_10+t_09_10;

            String t_10_11_query="";
            Integer t_10_11=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_10_11_List=new ArrayList();
            if(shift.equals("Day")){
                t_10_11_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 10:00:00') AND CONCAT('"+fromDate+" ',' 10:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 10:00:00') AND CONCAT('"+fromDate+" ',' 10:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_10_11";
            }
            else if(shift.equals("Night")){
                t_10_11_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 22:00:00') AND CONCAT('"+fromDate+" ',' 22:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 22:00:00') AND CONCAT('"+fromDate+" ',' 22:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_10_11";
            }
            t_10_11_List=secondaryDBTemplate.query(t_10_11_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_10_11());
            if(t_10_11_List.size()>0){
                t_10_11= t_10_11_List.get(0).getT_10_11();
            }
            resultSubModel.setT_10_11(t_10_11);
            sum_10_11=sum_10_11+t_10_11;


            String t_11_12_query="";
            Integer t_11_12=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_11_12_List=new ArrayList();
            if(shift.equals("Day")){
                t_11_12_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 11:00:00') AND CONCAT('"+fromDate+" ','11:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 11:00:00') AND CONCAT('"+fromDate+" ',' 11:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_11_12";
            }
            else if(shift.equals("Night")){
                t_11_12_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 23:00:00') AND CONCAT('"+fromDate+" ','23:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 23:00:00') AND CONCAT('"+fromDate+" ',' 23:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_11_12";
            }
            t_11_12_List=secondaryDBTemplate.query(t_11_12_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_11_12());
            if(t_11_12_List.size()>0){
                t_11_12= t_11_12_List.get(0).getT_11_12();
            }
            resultSubModel.setT_11_12(t_11_12);
            sum_11_12=sum_11_12+t_11_12;


            String t_12_13_query="";
            Integer t_12_13=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_12_13_List=new ArrayList();
            if(shift.equals("Day")){
                t_12_13_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 12:00:00') AND CONCAT('"+fromDate+" ',' 12:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 12:00:00') AND CONCAT('"+fromDate+" ',' 12:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_12_13";
            }
            else if(shift.equals("Night")){
                t_12_13_query="SELECT((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 00:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 00:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 00:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 00:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_12_13";
            }
            t_12_13_List=secondaryDBTemplate.query(t_12_13_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_12_13());
            if(t_12_13_List.size()>0){
                t_12_13= t_12_13_List.get(0).getT_12_13();
            }
            resultSubModel.setT_12_13(t_12_13);
            sum_12_13=sum_12_13+t_12_13;


            String t_13_14_query="";
            Integer t_13_14=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_13_14_List=new ArrayList();
            if(shift.equals("Day")){
                t_13_14_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 13:00:00') AND CONCAT('"+fromDate+" ',' 13:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 13:00:00') AND CONCAT('"+fromDate+" ',' 13:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_13_14";
            }
            else if(shift.equals("Night")){
                t_13_14_query="Select ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 01:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 01:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 01:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 01:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_13_14";
            }
            t_13_14_List=secondaryDBTemplate.query(t_13_14_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_13_14());
            if(t_13_14_List.size()>0){
                t_13_14= t_13_14_List.get(0).getT_13_14();
            }
            resultSubModel.setT_13_14(t_13_14);
            sum_13_14=sum_13_14+t_13_14;


            String t_14_15_query="";
            Integer t_14_15=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_14_15_List=new ArrayList();
            if(shift.equals("Day")){
                t_14_15_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('2022-08-24 ',' 14:00:00') AND CONCAT('2022-08-24 ',' 14:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 14:00:00') AND CONCAT('"+fromDate+" ',' 14:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_14_15";
            }
            else if(shift.equals("Night")){
                t_14_15_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 02:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 02:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 02:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 02:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_14_15";
            }
            t_14_15_List=secondaryDBTemplate.query(t_14_15_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_14_15());
            if(t_14_15_List.size()>0){
                t_14_15= t_14_15_List.get(0).getT_14_15();
            }
            resultSubModel.setT_14_15(t_14_15);
            sum_14_15=sum_14_15+t_14_15;


            String t_15_16_query="";
            Integer t_15_16=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_15_16_List=new ArrayList();
            if(shift.equals("Day")){
                t_15_16_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 15:00:00') AND CONCAT('"+fromDate+" ',' 15:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 15:00:00') AND CONCAT('"+fromDate+" ',' 15:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_15_16";
            }
            else if(shift.equals("Night")){
                t_15_16_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 03:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 03:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 03:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 03:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_15_16";
            }
            t_15_16_List=secondaryDBTemplate.query(t_15_16_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_15_16());
            if(t_15_16_List.size()>0){
                t_15_16= t_15_16_List.get(0).getT_15_16();
            }
            resultSubModel.setT_15_16(t_15_16);
            sum_15_16=sum_15_16+t_15_16;


            String t_16_17_query="";
            Integer t_16_17=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_16_17_List=new ArrayList();
            if(shift.equals("Day")){
                t_16_17_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 16:00:00') AND CONCAT('"+fromDate+" ',' 16:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 16:00:00') AND CONCAT('"+fromDate+"  ',' 16:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_16_17";
            }
            else if(shift.equals("Night")){
                t_16_17_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 04:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 04:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 04:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 04:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_16_17";
            }
            t_16_17_List=secondaryDBTemplate.query(t_16_17_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_16_17());
            if(t_16_17_List.size()>0){
                t_16_17= t_16_17_List.get(0).getT_16_17();
            }
            resultSubModel.setT_16_17(t_16_17);
            sum_16_17=sum_16_17+t_16_17;


            String t_17_18_query="";
            Integer t_17_18=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_17_18_List=new ArrayList();
            if(shift.equals("Day")){
                t_17_18_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 17:00:00') AND CONCAT('"+fromDate+" ',' 17:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 17:00:00') AND CONCAT('"+fromDate+" ',' 17:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_17_18";
            }
            else if(shift.equals("Night")){
                t_17_18_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 05:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 05:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 05:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 05:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_17_18";
            }
            t_17_18_List=secondaryDBTemplate.query(t_17_18_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_17_18());
            if(t_17_18_List.size()>0){
                t_17_18= t_17_18_List.get(0).getT_17_18();
            }
            resultSubModel.setT_17_18(t_17_18);
            sum_17_18=sum_17_18+t_17_18;


            String t_18_19_query="";
            Integer t_18_19=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_18_19_List=new ArrayList();
            if(shift.equals("Day")){
                t_18_19_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 18:00:00') AND CONCAT('"+fromDate+" ',' 18:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 18:00:00') AND CONCAT('"+fromDate+" ',' 18:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_18_19";
            }
            else if(shift.equals("Night")){
                t_18_19_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 06:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 06:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 06:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 06:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_18_19";
            }
            t_18_19_List=secondaryDBTemplate.query(t_18_19_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_18_19());
            if(t_18_19_List.size()>0){
                t_18_19= t_18_19_List.get(0).getT_18_19();
            }
            resultSubModel.setT_18_19(t_18_19);
            sum_18_19=sum_18_19+t_18_19;


            String t_19_20_query="";
            Integer t_19_20=0;
            List<EquipmentHandlingPerformaceRtgTimeWiseModel> t_19_20_List=new ArrayList();
            if(shift.equals("Day")){
                t_19_20_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT('"+fromDate+" ',' 19:00:00') AND CONCAT('"+fromDate+" ',' 19:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT('"+fromDate+" ',' 19:00:00') AND CONCAT('"+fromDate+" ',' 19:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_19_20";
            }
            else if(shift.equals("Night")){
                t_19_20_query="SELECT ((SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_fetch='"+gkey+"' AND t_fetch BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 07:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 07:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind='YARD')\n" +
                        "+\n" +
                        "(SELECT COUNT(*)\n" +
                        "FROM sparcsn4.inv_move_event\n" +
                        "WHERE che_put='"+gkey+"' AND t_put BETWEEN CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 07:00:00') AND CONCAT(DATE_ADD('"+fromDate+" ', INTERVAL 1 DAY),' 07:59:59')\n" +
                        "AND sparcsn4.inv_move_event.move_kind IN('DSCH','DLVR','SHFT'))\n" +
                        ") AS t_19_20";
            }
            t_19_20_List=secondaryDBTemplate.query(t_19_20_query,new EquipmentHandlingPerformaceRtgTimeWiseService.t_19_20());
            if(t_19_20_List.size()>0){
                t_19_20= t_19_20_List.get(0).getT_19_20();
            }
            resultSubModel.setT_19_20(t_19_20);
            sum_19_20=sum_19_20+t_19_20;





            totalRes=t_08_09+t_09_10+t_10_11+t_11_12+t_12_13+t_13_14+t_14_15+t_15_16+t_16_17+t_17_18+t_18_19+t_19_20;
            resultSubModel.setTotal(totalRes);
            resultSubList.add(resultSubModel);

        }
        resultModel.setEquipmentRtgList(resultSubList);
        resultModel.setSum_08_09(sum_08_09);
        resultModel.setSum_09_10(sum_09_10);
        resultModel.setSum_10_11(sum_10_11);
        resultModel.setSum_11_12(sum_11_12);
        resultModel.setSum_12_13(sum_12_13);
        resultModel.setSum_13_14(sum_13_14);
        resultModel.setSum_14_15(sum_14_15);
        resultModel.setSum_15_16(sum_15_16);
        resultModel.setSum_16_17(sum_16_17);
        resultModel.setSum_17_18(sum_17_18);
        resultModel.setSum_18_19(sum_18_19);
        resultModel.setSum_19_20(sum_19_20);
        resultModel.setTotal(sum_08_09+sum_09_10+sum_10_11+ sum_11_12+ sum_12_13+sum_14_15+ sum_15_16+ sum_16_17+sum_17_18+ sum_18_19+sum_19_20);
        resultList.add(resultModel);

return resultList;
    }

    class GkeyAndEq implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setEq(rs.getString("eq"));
            equipmentHandlingPerformaceRtgTimeWiseModel.setGkey(rs.getInt("gkey"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_08_09 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_08_09(rs.getInt("t_08_09"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_09_10 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_09_10(rs.getInt("t_09_10"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_10_11 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_10_11(rs.getInt("t_10_11"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_11_12 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_11_12(rs.getInt("t_11_12"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_12_13 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_12_13(rs.getInt("t_12_13"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_13_14 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_13_14(rs.getInt("t_13_14"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_14_15 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_14_15(rs.getInt("t_14_15"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_15_16 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_15_16(rs.getInt("t_15_16"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_16_17 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_16_17(rs.getInt("t_16_17"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_17_18 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_17_18(rs.getInt("t_17_18"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_18_19 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_18_19(rs.getInt("t_18_19"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }
    class t_19_20 implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
            equipmentHandlingPerformaceRtgTimeWiseModel.setT_19_20(rs.getInt("t_19_20"));
            return equipmentHandlingPerformaceRtgTimeWiseModel;
        }
    }

  class  EquipmentHandingPerformace implements RowMapper{

      @Override
      public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
          EquipmentHandlingPerformaceRtgTimeWiseModel equipmentHandlingPerformaceRtgTimeWiseModel=new EquipmentHandlingPerformaceRtgTimeWiseModel();
          equipmentHandlingPerformaceRtgTimeWiseModel.setEq(rs.getString("eq"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_08_09(rs.getInt("t_08_09"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_09_10(rs.getInt("t_09_10"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_10_11(rs.getInt("t_10_11"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_11_12(rs.getInt("t_11_12"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_12_13(rs.getInt("t_12_13"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_13_14(rs.getInt("t_13_14"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_14_15(rs.getInt("t_14_15"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_15_16(rs.getInt("t_15_16"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_16_17(rs.getInt("t_16_17"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_17_18(rs.getInt("t_17_18"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_18_19(rs.getInt("t_18_19"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setT_19_20(rs.getInt("t_19_20"));
          equipmentHandlingPerformaceRtgTimeWiseModel.setTotal(rs.getInt("t_08_09")+rs.getInt("t_09_10")+rs.getInt("t_10_11")+
                  rs.getInt("t_11_12")+ rs.getInt("t_12_13")+rs.getInt("t_13_14")+rs.getInt("t_14_15")+
                  rs.getInt("t_15_16")+rs.getInt("t_16_17")+rs.getInt("t_17_18")+rs.getInt("t_18_19")+
                  rs.getInt("t_19_20"));
          return equipmentHandlingPerformaceRtgTimeWiseModel;
      }
  }
}
