package com.datasoft.IgmMis.Service.ImportReport;
import com.datasoft.IgmMis.Model.ImportReport.ContainerDischargeAppModel;
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
public class ContainerDischargeAppService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List getAllContainerDischargeList(){
        String sqlQuery="";
        sqlQuery="SELECT ctmsmis.mis_exp_unit.gkey,mis_exp_unit.cont_id AS id,mis_exp_unit.rotation,mis_exp_unit.isoType AS iso,\n" +
                "(CASE \n" +
                "WHEN mis_exp_unit.cont_size= '20' AND mis_exp_unit.cont_height = '86' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '2D'\n" +
                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '4D'\n" +
                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.cont_height='96' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '4H'\n" +
                "WHEN mis_exp_unit.cont_size = '45' AND mis_exp_unit.cont_height='96' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '45H'\n" +
                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('RS','RT','RE') THEN '2RF'\n" +
                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.isoGroup IN ('RS','RT','RE') THEN '4RH'\n" +
                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('UT') THEN '2OT'\n" +
                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.isoGroup IN ('UT') THEN '4OT'\n" +
                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('PF','PC') THEN '2FR'\n" +
                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.isoGroup IN ('PF','PC') THEN '4FR'\n" +
                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('TN','TD','TG') THEN '2TK'\n" +
                "ELSE ''\n" +
                "END\n" +
                ") AS TYPE,\n" +
                "mis_exp_unit.cont_mlo AS mlo,ctmsmis.mis_exp_unit.current_position,\n" +
                "cont_status AS freight_kind,ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg AS weight,ctmsmis.mis_exp_unit.coming_from AS coming_from,\n" +
                "ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos,ctmsmis.mis_exp_unit.last_update,\n" +
                "ref_commodity.short_name,ctmsmis.mis_exp_unit.user_id,sparcsn4.inv_goods.destination\n" +
                "FROM ctmsmis.mis_exp_unit \n" +
                "INNER JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
                "INNER JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
                "WHERE  mis_exp_unit.snx_type=2 AND sparcsn4.inv_unit_fcy_visit.transit_state='S20_INBOUND' ORDER BY last_update DESC";
        System.out.println(sqlQuery);
        List resultList=secondaryDBTemplate.query(sqlQuery,new ContainerDischargeAppService.AllContainerDischargeAppList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());


        return listAll;

    }
    public List getYardList(){
        String sqlQuery="";
        sqlQuery="select distinct current_position from ctmsmis.mis_exp_unit\n" +
                "where  mis_exp_unit.delete_flag='0' and mis_exp_unit.snx_type=2 and current_position!=''";
        List resultList=secondaryDBTemplate.query(sqlQuery,new ContainerDischargeAppService.YardList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());


        return listAll;

    }
    public List getContainerDischargeAppList(Integer vvdGkey,String rotation, String searchBy,String yard,String fromDate,String toDate,String fromTime,String toTime){
        // List resultList=new ArrayList<>();
        String condition="";
        String sqlQuery="";
        if(searchBy.equals("dateRange"))
        {
            condition = " and date(mis_exp_unit.last_update) between '"+fromDate+" and '"+toDate+"' and mis_exp_unit.rotation = '"+rotation+"'";
        }
        else if (searchBy.equals("dateTime"))
        {
            String fDate = fromDate+" "+fromTime+":00";
            String tDate = toDate+" "+toTime+":00";
            condition = " and mis_exp_unit.last_update between '"+fDate+"' and '"+tDate+"' and mis_exp_unit.rotation = '"+rotation+"' ";
        }
        else if(searchBy.equals("yard") && (!rotation.equals("empty")))
        {
            if(fromDate.equals("empty") || toDate.equals("empty"))
            {
                condition = " and current_position = '"+yard+"' and mis_exp_unit.rotation = '"+rotation+"' and mis_exp_unit.vvd_gkey='"+vvdGkey+"'";
            }
            else
            {
                condition = " and date(mis_exp_unit.last_update) between '"+fromDate+"' and '"+toDate+"' and current_position = '"+yard+"' and mis_exp_unit.rotation = '"+rotation+"' and mis_exp_unit.vvd_gkey='"+vvdGkey+"'";
            }
        }
        else if(searchBy.equals("yard") && rotation.equals("empty"))
        {
            if(fromDate.equals("empty") || toDate.equals("empty"))
            {
                condition = " and current_position = '"+yard+"'";
            }
            else
            {
                condition = " and date(mis_exp_unit.last_update) between '"+fromDate+"' and '"+toDate+"' and current_position = '"+yard+"'";

            }

        }
        else if(searchBy.equals("all"))
        {
            condition = " and mis_exp_unit.rotation = '"+rotation+"'";
        }

        sqlQuery="SELECT ctmsmis.mis_exp_unit.gkey,ctmsmis.mis_exp_unit.cont_id AS id,mis_exp_unit.isoType AS iso,\n" +
                "(CASE \n" +
                "WHEN mis_exp_unit.cont_size= '20' AND mis_exp_unit.cont_height = '86' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '2D'\n" +
                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '4D'\n" +
                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.cont_height='96' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '4H'\n" +
                "WHEN mis_exp_unit.cont_size = '45' AND mis_exp_unit.cont_height='96' AND mis_exp_unit.isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC') THEN '45H'\n" +
                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('RS','RT','RE') THEN '2RF'\n" +
                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.isoGroup IN ('RS','RT','RE') THEN '4RH'\n" +
                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('UT') THEN '2OT'\n" +
                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.isoGroup IN ('UT') THEN '4OT'\n" +
                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('PF','PC') THEN '2FR'\n" +
                "WHEN mis_exp_unit.cont_size = '40' AND mis_exp_unit.isoGroup IN ('PF','PC') THEN '4FR'\n" +
                "WHEN mis_exp_unit.cont_size = '20' AND mis_exp_unit.cont_height='86' AND mis_exp_unit.isoGroup IN ('TN','TD','TG') THEN '2TK'\n" +
                "ELSE ''\n" +
                "END\n" +
                ") AS TYPE,\n" +
                "mis_exp_unit.rotation,\n" +
                "mis_exp_unit.cont_mlo AS mlo,ctmsmis.mis_exp_unit.current_position,\n" +
                "cont_status AS freight_kind,ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg AS weight,ctmsmis.mis_exp_unit.coming_from AS coming_from,\n" +
                "ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos,ctmsmis.mis_exp_unit.last_update,ref_commodity.short_name,ctmsmis.mis_exp_unit.user_id\n" +
                "FROM ctmsmis.mis_exp_unit \n" +
                "INNER JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                "INNER join sparcsn4.inv_unit_fcy_visit on sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
                "INNER JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
                "where mis_exp_unit.delete_flag='0' and mis_exp_unit.snx_type=2  and sparcsn4.inv_unit_fcy_visit.transit_state='S20_INBOUND' \n"+condition;
        System.out.println(sqlQuery);
        List resultList=secondaryDBTemplate.query(sqlQuery,new ContainerDischargeAppService.ContainerDischargeAppList());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public List getContainerDischargeAppOnBoardSummary(Integer vvdGkey){
        String sqlQuery="";
        sqlQuery="SELECT gkey,\n" +
                "IFNULL(SUM(onboard_LD_20),0) AS onboard_LD_20,\n" +
                "IFNULL(SUM(onboard_LD_40),0) AS onboard_LD_40,\n" +
                "IFNULL(SUM(onboard_MT_20),0) AS onboard_MT_20,\n" +
                "IFNULL(SUM(onboard_MT_40),0) AS onboard_MT_40,\n" +
                "IFNULL(SUM(onboard_LD_tues),0) AS onboard_LD_tues,\n" +
                "IFNULL(SUM(onboard_MT_tues),0) AS onboard_MT_tues\n" +
                "\n" +
                " FROM (\n" +
                "SELECT DISTINCT ctmsmis.mis_exp_unit.gkey AS gkey,\n" +
                "(CASE WHEN mis_exp_unit.cont_size = '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_20, \n" +
                "(CASE WHEN mis_exp_unit.cont_size > '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_40,\n" +
                "(CASE WHEN mis_exp_unit.cont_size = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS onboard_MT_20, \n" +
                "(CASE WHEN mis_exp_unit.cont_size > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS onboard_MT_40, \n" +
                "(CASE WHEN mis_exp_unit.cont_size=20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN mis_exp_unit.cont_size>20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS onboard_LD_tues, \n" +
                "(CASE WHEN mis_exp_unit.cont_size=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE WHEN mis_exp_unit.cont_size>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS onboard_MT_tues\n" +
                "\n" +
                "FROM ctmsmis.mis_exp_unit\n" +
                "LEFT JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "WHERE  mis_exp_unit.vvd_gkey='"+vvdGkey+"' AND mis_exp_unit.preAddStat='0' AND mis_exp_unit.snx_type=2 AND sparcsn4.inv_unit_fcy_visit.transit_state='S20_INBOUND'\n" +
                ") AS tmp";
        List resultList=secondaryDBTemplate.query(sqlQuery,new ContainerDischargeAppService.ContainerDischargeAppOnBoardSummary());
        List listAll = (List) resultList.stream().collect(Collectors.toList());


        return listAll;

    }
    public List getContainerDischargeAppBalanceSummary(Integer vvdGkey){
        String sqlQuery="";
        sqlQuery="SELECT gkey,\n" +
                "IFNULL(SUM(balance_LD_20),0) AS balance_LD_20,\n" +
                "IFNULL(SUM(balance_LD_40),0) AS balance_LD_40,\n" +
                "IFNULL(SUM(balance_MT_20),0) AS balance_MT_20,\n" +
                "IFNULL(SUM(balance_MT_40),0) AS balance_MT_40,\n" +
                "IFNULL(SUM(balance_LD_tues),0) AS balance_LD_tues,\n" +
                "IFNULL(SUM(balance_MT_tues),0) AS balance_MT_tues\n" +
                "\n" +
                " FROM (\n" +
                "SELECT DISTINCT sparcsn4.inv_unit.gkey AS gkey,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_40,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_40, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS balance_LD_tues, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS balance_MT_tues\n" +
                "\n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ob_cv\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "LEFT JOIN ctmsmis.mis_exp_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                "\n" +
                "WHERE  sparcsn4.argo_carrier_visit.cvcvd_gkey='"+vvdGkey+"' AND category='EXPRT' AND mis_exp_unit.snx_type=2 AND transit_state NOT IN ('S60_LOADED','S70_DEPARTED','S99_RETIRED')\n" +
                ") AS tmp";
        List resultList=secondaryDBTemplate.query(sqlQuery,new ContainerDischargeAppService.ContainerDischargeAppBalanceSummary());
        List listAll = (List) resultList.stream().collect(Collectors.toList());


        return listAll;

    }

    class  AllContainerDischargeAppList implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ContainerDischargeAppModel containerDischargeAppModel=new ContainerDischargeAppModel();
            containerDischargeAppModel.setGkey(rs.getInt("gkey"));
            containerDischargeAppModel.setId(rs.getString("id"));
            containerDischargeAppModel.setRotation(rs.getString("rotation"));
            containerDischargeAppModel.setIso(rs.getString("iso"));
            containerDischargeAppModel.setTYPE(rs.getString("TYPE"));
            containerDischargeAppModel.setMlo(rs.getString("mlo"));
            containerDischargeAppModel.setCurrent_position(rs.getString("current_position"));
            containerDischargeAppModel.setFreight_kind(rs.getString("freight_kind"));
            containerDischargeAppModel.setWeight(rs.getFloat("weight"));
            containerDischargeAppModel.setComing_from(rs.getString("coming_from"));
            containerDischargeAppModel.setPod(rs.getString("pod"));
            containerDischargeAppModel.setStowage_pos(rs.getInt("stowage_pos"));
            containerDischargeAppModel.setLast_update(rs.getString("last_update"));
            containerDischargeAppModel.setShort_name(rs.getString("short_name"));
            containerDischargeAppModel.setUser_id(rs.getString("user_id"));
            containerDischargeAppModel.setDestination(rs.getInt("destination"));

            return containerDischargeAppModel;
        }

    }
    class YardList implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ContainerDischargeAppModel containerDischargeAppModel=new ContainerDischargeAppModel();
            containerDischargeAppModel.setCurrent_position(rs.getString("current_position"));
            return containerDischargeAppModel;
        }
    }
    class ContainerDischargeAppList implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ContainerDischargeAppModel containerDischargeAppModel=new ContainerDischargeAppModel();
            containerDischargeAppModel.setId(rs.getString("id"));
            containerDischargeAppModel.setRotation(rs.getString("rotation"));
            containerDischargeAppModel.setIso(rs.getString("iso"));
            containerDischargeAppModel.setTYPE(rs.getString("TYPE"));
            containerDischargeAppModel.setMlo(rs.getString("mlo"));
            containerDischargeAppModel.setCurrent_position(rs.getString("current_position"));
            containerDischargeAppModel.setFreight_kind(rs.getString("freight_kind"));
            containerDischargeAppModel.setWeight(rs.getFloat("weight"));
            containerDischargeAppModel.setComing_from(rs.getString("coming_from"));
            containerDischargeAppModel.setPod(rs.getString("pod"));
            containerDischargeAppModel.setLast_update(rs.getString("last_update"));
            containerDischargeAppModel.setShort_name(rs.getString("short_name"));
            containerDischargeAppModel.setUser_id(rs.getString("user_id"));

            return containerDischargeAppModel;
        }
    }
    class ContainerDischargeAppOnBoardSummary implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ContainerDischargeAppModel containerDischargeAppModel=new ContainerDischargeAppModel();
            containerDischargeAppModel.setOnboard_LD_20(rs.getInt("onboard_LD_20"));
            containerDischargeAppModel.setOnboard_LD_40(rs.getInt("onboard_LD_40"));
            containerDischargeAppModel.setOnboard_MT_20(rs.getInt("onboard_MT_20"));
            containerDischargeAppModel.setOnboard_MT_40(rs.getInt("onboard_MT_40"));
            containerDischargeAppModel.setOnboard_LD_tues(rs.getInt("onboard_LD_tues"));
            containerDischargeAppModel.setOnboard_MT_tues(rs.getInt("onboard_MT_tues"));
            return containerDischargeAppModel;
        }
    }
    class ContainerDischargeAppBalanceSummary implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ContainerDischargeAppModel containerDischargeAppModel=new ContainerDischargeAppModel();
            containerDischargeAppModel.setBalance_LD_20(rs.getInt("balance_LD_20"));
            containerDischargeAppModel.setBalance_LD_40(rs.getInt("balance_LD_40"));
            containerDischargeAppModel.setBalance_MT_20(rs.getInt("balance_MT_20"));
            containerDischargeAppModel.setBalance_MT_40(rs.getInt("balance_MT_40"));
            containerDischargeAppModel.setBalance_LD_tues(rs.getInt("balance_LD_tues"));
            containerDischargeAppModel.setBalance_MT_tues(rs.getInt("balance_MT_tues"));
            return containerDischargeAppModel;
        }
    }
}
