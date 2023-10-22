package com.datasoft.IgmMis.Service.ShahinSpecialReport;

import com.datasoft.IgmMis.Model.ShahinSpecialReport.ShahinLoadedContainerList;
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
public class ShahinLoadedContainerService {


    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;

    public Integer get_vvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM sparcsn4.vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ShahinLoadedContainerList> resultList=SecondaryDBTemplate.query(sqlQuery,new LoadedVvdGkey());

        ShahinLoadedContainerList shahinLoadedContainerList;
        for(int i=0;i<resultList.size();i++){
            shahinLoadedContainerList=resultList.get(i);
            vvdgkey=shahinLoadedContainerList.getVvd_gkey();
        }
        return vvdgkey;
    }

    public List getVoyNo(String rotation){
        String sqlQuery="";
        String voyNo="";
        sqlQuery="SELECT Voy_No FROM igm_masters WHERE Import_Rotation_No='"+rotation+"'";
        System.out.println(sqlQuery);
        List resultList=primaryDBTemplate.query(sqlQuery,new LoadedVoyNo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());

        return listAll;
    }

    class LoadedVoyNo implements RowMapper {

        @Override
        public ShahinLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShahinLoadedContainerList shahinLoadedContainerList =new ShahinLoadedContainerList();
            shahinLoadedContainerList.setVoy_No(rs.getString("Voy_No"));

            return shahinLoadedContainerList;
        }
    }

    class LoadedVvdGkey implements RowMapper {

        @Override
        public ShahinLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShahinLoadedContainerList shahinLoadedContainerList=new ShahinLoadedContainerList();
            shahinLoadedContainerList.setVvd_gkey(rs.getInt("vvd_gkey"));

            return shahinLoadedContainerList;
        }
    }
    public List getLoadedContainerList(Integer vvdgkey,String fromdate,String todate,String fromTime,String toTime){
        String sqlQuery="";
        String cond="";
        String frmDate="";
        String tDate="";
        if(fromdate!="" && todate!="")
        {
            if(fromTime=="" || toTime=="")
                cond = " and date(mis_exp_unit.last_update) between '"+fromdate+"' and '"+todate+"'";
            else
            {
                frmDate = fromdate+" "+fromTime+":00";
                tDate = todate+" "+toTime+":00";
                cond = " and mis_exp_unit.last_update between '"+frmDate+"' and '"+tDate+"'";
            }
        }
        sqlQuery="SELECT ctmsmis.mis_exp_unit.gkey,CONCAT(SUBSTRING(ctmsmis.mis_exp_unit.cont_id,1,4),' ',SUBSTRING(ctmsmis.mis_exp_unit.cont_id,5)) AS id,mis_exp_unit.isoType AS iso,\n" +
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
                "mis_exp_unit.cont_mlo AS mlo,\n" +
                "cont_status AS freight_kind,ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg AS weight,ctmsmis.mis_exp_unit.coming_from AS coming_from,\n" +
                "ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos,ctmsmis.mis_exp_unit.last_update,ref_commodity.short_name,ctmsmis.mis_exp_unit.user_id\n" +
                "FROM ctmsmis.mis_exp_unit \n" +
                "LEFT JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
                "where mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' and mis_exp_unit.delete_flag='0' and snx_type=0 "+cond+" ";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new LoadedContainerList());
        System.out.println("Sql:"+sqlQuery);

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    public List getLoadedContainerSummaryReport(Integer vvdgkey,String fromdate,String todate,String fromTime,String toTime){
        String sqlQuery="";
        String cond="";
        String frmDate="";
        String tDate="";
        if(fromdate!="" && todate!="")
        {
            if(fromTime=="" || toTime=="")
                cond = " and date(mis_exp_unit.last_update) between '"+fromdate+"' and '"+todate+"'";
            else
            {
                frmDate = fromdate+" "+fromTime+":00";
                tDate = todate+" "+toTime+":00";
                cond = " and mis_exp_unit.last_update between '"+frmDate+"' and '"+tDate+"'";
            }
        }
        sqlQuery="select gkey,\n" +
                "IFNULL(SUM(onboard_LD_20),0) AS onboard_LD_20,\n" +
                "IFNULL(SUM(onboard_LD_40),0) AS onboard_LD_40,\n" +
                "IFNULL(SUM(onboard_MT_20),0) AS onboard_MT_20,\n" +
                "IFNULL(SUM(onboard_MT_40),0) AS onboard_MT_40,\n" +
                "IFNULL(SUM(onboard_LD_tues),0) AS onboard_LD_tues,\n" +
                "IFNULL(SUM(onboard_MT_tues),0) AS onboard_MT_tues\n" +
                "\n" +
                " from (\n" +
                "select distinct ctmsmis.mis_exp_unit.gkey as gkey,\n" +
                "(CASE WHEN mis_exp_unit.cont_size = '20' AND freight_kind in ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_20, \n" +
                "(CASE WHEN mis_exp_unit.cont_size > '20' AND freight_kind in ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS onboard_LD_40,\n" +
                "(CASE WHEN mis_exp_unit.cont_size = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS onboard_MT_20, \n" +
                "(CASE WHEN mis_exp_unit.cont_size > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS onboard_MT_40, \n" +
                "(CASE WHEN mis_exp_unit.cont_size=20 AND freight_kind in ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN mis_exp_unit.cont_size>20 AND freight_kind in ('FCL','LCL') THEN 2 ELSE NULL END) END) AS onboard_LD_tues, \n" +
                "(CASE WHEN mis_exp_unit.cont_size=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE WHEN mis_exp_unit.cont_size>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS onboard_MT_tues\n" +
                "\n" +
                "FROM ctmsmis.mis_exp_unit\n" +
                "left join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                "where  mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' and snx=0\n" +
                ") as tmp";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new LoadedContainerSummeryReport());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    public List getLoadedContainerSummaryReportList(Integer vvdgkey,String fromdate,String todate,String fromTime,String toTime){
        String sqlQuery="";
        String cond="";
        String frmDate="";
        String tDate="";
        if(fromdate!="" && todate!="")
        {
            if(fromTime=="" || toTime=="")
                cond = " and date(mis_exp_unit.last_update) between '"+fromdate+"' and '"+todate+"'";
            else
            {
                frmDate = fromdate+" "+fromTime+":00";
                tDate = todate+" "+toTime+":00";
                cond = " and mis_exp_unit.last_update between '"+frmDate+"' and '"+tDate+"'";
            }
        }
        sqlQuery="select gkey,\n" +
                "IFNULL(SUM(balance_LD_20),0) AS balance_LD_20,\n" +
                "IFNULL(SUM(balance_LD_40),0) AS balance_LD_40,\n" +
                "IFNULL(SUM(balance_MT_20),0) AS balance_MT_20,\n" +
                "IFNULL(SUM(balance_MT_40),0) AS balance_MT_40,\n" +
                "IFNULL(SUM(balance_LD_tues),0) AS balance_LD_tues,\n" +
                "IFNULL(SUM(balance_MT_tues),0) AS balance_MT_tues\n" +
                "\n" +
                " from (\n" +
                "select distinct sparcsn4.inv_unit.gkey as gkey,\n" +
                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind in ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_20, \n" +
                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind in ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_40,\n" +
                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_20, \n" +
                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_40, \n" +
                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind in ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind in ('FCL','LCL') THEN 2 ELSE NULL END) END) AS balance_LD_tues, \n" +
                "(CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE WHEN right(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS balance_MT_tues\n" +
                "\n" +
                "FROM sparcsn4.inv_unit\n" +
                "inner join sparcsn4.inv_unit_fcy_visit on sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "inner join sparcsn4.argo_carrier_visit on sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ob_cv\n" +
                "inner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "inner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "inner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "where  sparcsn4.argo_carrier_visit.cvcvd_gkey='"+vvdgkey+"' and category='EXPRT' and transit_state not in ('S60_LOADED','S70_DEPARTED','S99_RETIRED')\n" +
                ") as tmp";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new LoadedContainerSummeryReportList());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class LoadedContainerSummeryReport implements RowMapper{

        @Override
        public ShahinLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShahinLoadedContainerList shahinLoadedContainerList =new ShahinLoadedContainerList();
            shahinLoadedContainerList.setOnboard_LD_20(rs.getString("onboard_LD_20"));
            shahinLoadedContainerList.setOnboard_LD_40(rs.getString("onboard_LD_40"));
            shahinLoadedContainerList.setOnboard_MT_20(rs.getString("onboard_MT_20"));
            shahinLoadedContainerList.setOnboard_MT_40(rs.getString("onboard_MT_40"));
            shahinLoadedContainerList.setOnboard_LD_tues(rs.getString("onboard_LD_tues"));
            shahinLoadedContainerList.setOnboard_MT_tues(rs.getString("onboard_MT_tues"));

            return shahinLoadedContainerList;
        }
    }

    class LoadedContainerSummeryReportList implements RowMapper{

        @Override
        public ShahinLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShahinLoadedContainerList shahinLoadedContainerList =new ShahinLoadedContainerList();
            shahinLoadedContainerList.setBalance_LD_20(rs.getString("balance_LD_20"));
            shahinLoadedContainerList.setBalance_LD_40(rs.getString("balance_LD_40"));
            shahinLoadedContainerList.setBalance_MT_20(rs.getString("balance_MT_20"));
            shahinLoadedContainerList.setBalance_MT_40(rs.getString("balance_MT_40"));
            shahinLoadedContainerList.setBalance_LD_tues(rs.getString("balance_LD_tues"));
            shahinLoadedContainerList.setBalance_MT_tues(rs.getString("balance_MT_tues"));
            return shahinLoadedContainerList;
        }
    }

    class LoadedContainerList implements RowMapper{

        @Override
        public ShahinLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShahinLoadedContainerList shahinLoadedContainerList =new ShahinLoadedContainerList();
            shahinLoadedContainerList.setId(rs.getString("id"));
            shahinLoadedContainerList.setIso(rs.getString("iso"));
            shahinLoadedContainerList.setTYPE(rs.getString("TYPE"));
            shahinLoadedContainerList.setMlo(rs.getString("mlo"));
            shahinLoadedContainerList.setFreight_kind(rs.getString("freight_kind"));
            shahinLoadedContainerList.setWeight(rs.getString("weight"));
            shahinLoadedContainerList.setPod(rs.getString("pod"));
            shahinLoadedContainerList.setStowage_pos(rs.getString("stowage_pos"));
            shahinLoadedContainerList.setLast_update(rs.getTimestamp("last_update"));
            shahinLoadedContainerList.setComing_from(rs.getString("coming_from"));
            shahinLoadedContainerList.setShort_name(rs.getString("short_name"));
            shahinLoadedContainerList.setUser_id(rs.getString("user_id"));

            return shahinLoadedContainerList;
        }
    }


    public List getVessleInformation(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="SELECT vsl_vessels.name AS vsl_name,IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,\n" +
                "IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string03,'')) AS berth_op,IFNULL(sparcsn4.argo_quay.id,'') AS berth,\n" +
                "DATE(sparcsn4.vsl_vessel_visit_details.published_eta) AS ata,\n" +
                "sparcsn4.argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessel_berthings ON sparcsn4.vsl_vessel_berthings.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN sparcsn4.argo_quay ON sparcsn4.argo_quay.gkey=sparcsn4.vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new LoadedContainerVesselInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class LoadedContainerVesselInfo implements RowMapper{

        @Override
        public ShahinLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShahinLoadedContainerList shahinLoadedContainerList =new ShahinLoadedContainerList();
            shahinLoadedContainerList.setVsl_name(rs.getString("vsl_name"));
            shahinLoadedContainerList.setBerth_op(rs.getString("berth_op"));
            shahinLoadedContainerList.setBerth(rs.getString("berth"));
            shahinLoadedContainerList.setAta(rs.getDate("ata"));
            shahinLoadedContainerList.setAtd(rs.getTimestamp("atd"));

            return shahinLoadedContainerList;
        }
    }

}
