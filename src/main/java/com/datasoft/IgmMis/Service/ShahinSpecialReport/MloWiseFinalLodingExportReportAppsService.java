package com.datasoft.IgmMis.Service.ShahinSpecialReport;


import com.datasoft.IgmMis.Model.ShahinSpecialReport.MloWiseFinalLodingExportReportApps;
import com.datasoft.IgmMis.Model.ShahinSpecialReport.MloWiseFinalLodingReportApps;
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
public class MloWiseFinalLodingExportReportAppsService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;

    public Integer getVesselListvvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM sparcsn4.vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<MloWiseFinalLodingExportReportApps> resultList=SecondaryDBTemplate.query(sqlQuery,new VesselListvvdGkey());

        MloWiseFinalLodingExportReportApps mloWiseFinalLodingExportReportApps;
        for(int i=0;i<resultList.size();i++){
            mloWiseFinalLodingExportReportApps=resultList.get(i);
            vvdgkey=mloWiseFinalLodingExportReportApps.getVvd_gkey();
        }
        return vvdgkey;
    }




    class VesselListvvdGkey implements RowMapper {

        @Override
        public MloWiseFinalLodingExportReportApps mapRow(ResultSet rs, int rowNum) throws SQLException {
            MloWiseFinalLodingExportReportApps mloWiseFinalLodingExportReportApps=new MloWiseFinalLodingExportReportApps();
            mloWiseFinalLodingExportReportApps.setVvd_gkey(rs.getInt("vvd_gkey"));

            return mloWiseFinalLodingExportReportApps;
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
        List resultList=SecondaryDBTemplate.query(sqlQuery,new VessleInformation());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }




    class VessleInformation implements RowMapper{

        @Override
        public MloWiseFinalLodingExportReportApps mapRow(ResultSet rs, int rowNum) throws SQLException {
            MloWiseFinalLodingExportReportApps mloWiseFinalLodingExportReportApps =new MloWiseFinalLodingExportReportApps();
            mloWiseFinalLodingExportReportApps.setVsl_name(rs.getString("vsl_name"));
            mloWiseFinalLodingExportReportApps.setBerth_op(rs.getString("berth_op"));
            mloWiseFinalLodingExportReportApps.setBerth(rs.getString("berth"));
            mloWiseFinalLodingExportReportApps.setAta(rs.getDate("ata"));
            mloWiseFinalLodingExportReportApps.setAtd(rs.getTimestamp("atd"));

            return mloWiseFinalLodingExportReportApps;
        }
    }





    public List getMloInfoForExportUploadReport(Integer vvdgkey,String fromdate,String todate,String fromTime,String toTime){
        List<MloWiseFinalLodingExportReportApps> mainModel=new ArrayList<>();
        MloWiseFinalLodingExportReportApps mloWiseFirstModel=new MloWiseFinalLodingExportReportApps();
        String cont_mlo="";
        String cond="";
        String sqlQueryforMlo="";
        String frmDate="";
        String tDate="";
        String sqlQueryForContainerList="";

        if(fromdate!="" && todate!="")
        {
            if(fromTime!="")
                frmDate = fromdate+ " " +fromTime+ ":00";

            System.out.println("FromDate:"+frmDate);

            if(toTime!="")
                tDate = todate+ " " +toTime+ ":00";

            System.out.println("toDate:"+tDate);

            cond = " and mis_exp_unit.vvd_gkey='"+vvdgkey+"' and mis_exp_unit.last_update BETWEEN '"+frmDate+"' and '"+tDate+"'";
        }
        else
        {
            cond = " and mis_exp_unit.vvd_gkey='"+vvdgkey+"'";

        }



        sqlQueryforMlo="\n" +
                "SELECT DISTINCT cont_mlo AS mlo\n" +
                "FROM ctmsmis.mis_exp_unit \n" +
                "INNER JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=mis_exp_unit.gkey \n" +
                "WHERE mis_exp_unit.preAddStat='0' AND mis_exp_unit.delete_flag='0' "+cond+" ORDER BY cont_mlo,craine_id,sparcsn4.inv_unit.id\n" +
                "\n";


        List<MloWiseFinalLodingExportReportApps> resultList=SecondaryDBTemplate.query(sqlQueryforMlo,new MloInfoForExportUploadReport());
        System.out.println("firstCotainerSql:"+sqlQueryforMlo);


        for(int i=0;i<resultList.size();i++){
            mloWiseFirstModel=resultList.get(i);
            cont_mlo=mloWiseFirstModel.getMlo();
            mloWiseFirstModel.setMlo(cont_mlo);
            System.out.println("cont_mlo:"+cont_mlo);


            sqlQueryForContainerList="\n" +
                    "\n" +
                    "SELECT * FROM\n" +
                    "(SELECT CONCAT(SUBSTRING(sparcsn4.inv_unit.id,1,4),' ',SUBSTRING(sparcsn4.inv_unit.id,5)) AS id,\n" +
                    "ctmsmis.mis_exp_unit.cont_size AS size,\n" +
                    "(ctmsmis.mis_exp_unit.cont_height)/10 AS height,ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.craine_id,\n" +
                    "(SELECT SUBSTRING(id,1,3) FROM sparcsn4.argo_quay \n" +
                    "\tINNER JOIN sparcsn4.vsl_vessel_berthings brt ON brt.quay=sparcsn4.argo_quay.gkey \n" +
                    "\tWHERE brt.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey ORDER BY brt.ata DESC LIMIT 1)AS berth,\n" +
                    "sparcsn4.ref_equip_type.id AS iso,ctmsmis.mis_exp_unit.cont_status AS freight_kind,cont_mlo AS mlo,\n" +
                    "ctmsmis.mis_exp_unit.seal_no AS seal_no2,ctmsmis.mis_exp_unit.truck_no AS truck_no,\n" +
                    "ctmsmis.mis_exp_unit.truck_no LIKE '%sp%' AS truck_no1,\n" +
                    "ctmsmis.mis_exp_unit.stowage_pos,ctmsmis.mis_exp_unit.last_update,'' AS remark,mis_exp_unit.goods_and_ctr_wt_kg AS weight\n" +
                    " FROM ctmsmis.mis_exp_unit \n" +
                    " INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=ctmsmis.mis_exp_unit.vvd_gkey\n" +
                    "INNER JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=mis_exp_unit.gkey \n" +
                    "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
                    "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
                    "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                    "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                    "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                    "WHERE mis_exp_unit.preAddStat='0' AND mis_exp_unit.delete_flag='0' AND snx_type='0' AND cont_mlo='"+cont_mlo+"' "+cond+" ORDER BY cont_mlo,craine_id) AS tbl WHERE size IN('20','40','45','42')";
            List<MloWiseFinalLodingReportApps> resultListForContainerInfo=SecondaryDBTemplate.query(sqlQueryForContainerList,new VessleListWithStatusInfoForExportUploadReport());
            System.out.println("secoundCotainerSql:"+sqlQueryForContainerList);
            List<MloWiseFinalLodingReportApps> temlist=new ArrayList();
            Integer weight=0;
            Integer totalWeight=0;

            for(Integer j=0;j<resultListForContainerInfo.size();j++){
                MloWiseFinalLodingReportApps mloWiseThirdTempModel=new MloWiseFinalLodingReportApps();
                MloWiseFinalLodingReportApps mloWiseSecoundTempModel;
                mloWiseSecoundTempModel=resultListForContainerInfo.get(j);
                mloWiseThirdTempModel.setId(mloWiseSecoundTempModel.getId());
                mloWiseThirdTempModel.setMlo(mloWiseSecoundTempModel.getMlo());
                mloWiseThirdTempModel.setIso(mloWiseSecoundTempModel.getIso());
                mloWiseThirdTempModel.setSize(mloWiseSecoundTempModel.getSize());
                mloWiseThirdTempModel.setHeight(mloWiseSecoundTempModel.getHeight());
                mloWiseThirdTempModel.setFreight_kind(mloWiseSecoundTempModel.getFreight_kind());
                mloWiseThirdTempModel.setWeight(mloWiseSecoundTempModel.getWeight());
                totalWeight=totalWeight+mloWiseSecoundTempModel.getWeight();
                mloWiseThirdTempModel.setStowage_pos(mloWiseSecoundTempModel.getStowage_pos());
                mloWiseThirdTempModel.setLast_update(mloWiseSecoundTempModel.getLast_update());
                mloWiseThirdTempModel.setSeal_no(mloWiseSecoundTempModel.getSeal_no());
                mloWiseThirdTempModel.setTruck_no(mloWiseSecoundTempModel.getTruck_no());
                mloWiseThirdTempModel.setCraine_id(mloWiseSecoundTempModel.getCraine_id());
                temlist.add(mloWiseThirdTempModel);


            }

            mloWiseFirstModel.setTotalWeight(totalWeight);

            mloWiseFirstModel.setContainerList(temlist);
            mainModel.add(mloWiseFirstModel);
        }




        return mainModel;
    }



    class MloInfoForExportUploadReport implements RowMapper{

        @Override
        public MloWiseFinalLodingExportReportApps mapRow(ResultSet rs, int rowNum) throws SQLException {
            MloWiseFinalLodingExportReportApps mloWiseFinalLodingExportReportApps =new MloWiseFinalLodingExportReportApps();
            String mlo=rs.getString("mlo");
            mloWiseFinalLodingExportReportApps.setMlo(mlo);
            return mloWiseFinalLodingExportReportApps;
        }
    }





    class VessleListWithStatusInfoForExportUploadReport implements RowMapper{

        @Override
        public MloWiseFinalLodingReportApps mapRow(ResultSet rs, int rowNum) throws SQLException {
            MloWiseFinalLodingReportApps mloWiseFinalLodingExportReportApps =new MloWiseFinalLodingReportApps();


            mloWiseFinalLodingExportReportApps.setId(rs.getString("id"));
            mloWiseFinalLodingExportReportApps.setIso(rs.getString("iso"));
            mloWiseFinalLodingExportReportApps.setMlo(rs.getString("mlo"));
            mloWiseFinalLodingExportReportApps.setSize(rs.getString("size"));
            mloWiseFinalLodingExportReportApps.setHeight(rs.getString("height"));
            mloWiseFinalLodingExportReportApps.setFreight_kind(rs.getString("freight_kind"));
            mloWiseFinalLodingExportReportApps.setWeight(rs.getInt("weight"));
            mloWiseFinalLodingExportReportApps.setStowage_pos(rs.getString("stowage_pos"));
            mloWiseFinalLodingExportReportApps.setPod(rs.getString("pod"));
            mloWiseFinalLodingExportReportApps.setLast_update(rs.getTimestamp("last_update"));
            mloWiseFinalLodingExportReportApps.setSeal_no(rs.getString("seal_no2"));
            mloWiseFinalLodingExportReportApps.setTruck_no(rs.getString("truck_no"));
            mloWiseFinalLodingExportReportApps.setCraine_id(rs.getString("craine_id"));
            return mloWiseFinalLodingExportReportApps;
        }
    }




    public List getExportContainer(Integer vvdgkey,String fromdate,String todate,String fromTime,String toTime){
        String cond="";
        String frmDate="";
        String tDate="";
        if(fromdate!="" && todate!="")
        {
            if(fromTime!="")

                frmDate = fromdate+ " " +fromTime+ ":00";
            else
                frmDate = fromdate+ "00:00:00";
            System.out.println("FromDate:"+frmDate);
            if(toTime!="")
                tDate = todate+ " " +toTime+ ":00";
            else
                tDate = todate+ "23:59:59";
            System.out.println("toDate:"+tDate);
            cond = "AND mis_exp_unit.last_update BETWEEN '"+frmDate+"' and '"+tDate+"'";
        }
        else
        {
            cond = " ";
        }



        String sqlQuery="SELECT gkey,mlo,(SELECT NAME FROM sparcsn4.ref_bizunit_scoped WHERE id=mlo AND NAME !='NULL' LIMIT 1) AS mlo_name,\n" +
                "IFNULL(SUM(D_20),0) AS D_20,\n" +
                "IFNULL(SUM(D_40),0) AS D_40,\n" +
                "IFNULL(SUM(H_40),0) AS H_40,\n" +
                "IFNULL(SUM(H_45),0) AS H_45,\n" +
                "\n" +
                "IFNULL(SUM(R_20),0) AS R_20,\n" +
                "IFNULL(SUM(RH_40),0) AS RH_40,\n" +
                "\n" +
                "IFNULL(SUM(OT_20),0) AS OT_20,\n" +
                "IFNULL(SUM(OT_40),0) AS OT_40,\n" +
                "\n" +
                "IFNULL(SUM(FR_20),0) AS FR_20,\n" +
                "IFNULL(SUM(FR_40),0) AS FR_40,\n" +
                "\n" +
                "IFNULL(SUM(TK_20),0) AS TK_20,\n" +
                "\n" +
                "IFNULL(SUM(MD_20),0) AS MD_20,\n" +
                "IFNULL(SUM(MD_40),0) AS MD_40,\n" +
                "IFNULL(SUM(MH_40),0) AS MH_40,\n" +
                "IFNULL(SUM(MH_45),0) AS MH_45,\n" +
                "\n" +
                "IFNULL(SUM(MR_20),0) AS MR_20,\n" +
                "IFNULL(SUM(MRH_40),0) AS MRH_40,\n" +
                "\n" +
                "IFNULL(SUM(MOT_20),0) AS MOT_20,\n" +
                "IFNULL(SUM(MOT_40),0) AS MOT_40,\n" +
                "\n" +
                "IFNULL(SUM(MFR_20),0) AS MFR_20,\n" +
                "IFNULL(SUM(MFR_40),0) AS MFR_40,\n" +
                "\n" +
                "IFNULL(SUM(MTK_20),0) AS MTK_20,\n" +
                "\n" +
                "IFNULL(SUM(grand_tot),0) AS grand_tot,\n" +
                "IFNULL(SUM(tues),0) AS tues,\n" +
                "SUM(goods_and_ctr_wt_kg) AS weight\n" +
                " FROM (\n" +
                "SELECT DISTINCT ctmsmis.mis_exp_unit.gkey AS gkey,cont_mlo AS mlo,inv_unit.goods_and_ctr_wt_kg,\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS D_20, \n" +
                "(CASE WHEN cont_size = '40' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS D_40, \n" +
                "(CASE WHEN cont_size = '40' AND cont_height='96' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS H_40, \n" +
                "(CASE WHEN cont_size = '45' AND cont_height='96' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1\n" +
                "\t  WHEN cont_size = '42' AND cont_height='90' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1\n" +
                "ELSE NULL END) AS H_45,\n" +
                "\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS R_20, \n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS RH_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS OT_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS OT_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS FR_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS FR_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status IN ('FCL','LCL') AND isoGroup IN ('TN','TD','TG')  THEN 1  \n" +
                "ELSE NULL END) AS TK_20,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MD_20, \n" +
                "(CASE WHEN cont_size = '40' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MD_40, \n" +
                "(CASE WHEN cont_size = '40' AND cont_height='96' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MH_40, \n" +
                "(CASE WHEN cont_size = '45' AND cont_height='96' AND mis_exp_unit.cont_status ='MTY' AND isoGroup NOT IN ('RS','RT','RE','UT','TN','TD','TG','PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MH_45,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS MR_20, \n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('RS','RT','RE')  THEN 1  \n" +
                "ELSE NULL END) AS MRH_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS MOT_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('UT')  THEN 1  \n" +
                "ELSE NULL END) AS MOT_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MFR_20,\n" +
                "(CASE WHEN cont_size = '40' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('PF','PC')  THEN 1  \n" +
                "ELSE NULL END) AS MFR_40,\n" +
                "\n" +
                "(CASE WHEN cont_size = '20' AND cont_height='86' AND mis_exp_unit.cont_status ='MTY' AND isoGroup IN ('TN','TD','TG')  THEN 1  \n" +
                "ELSE NULL END) AS MTK_20,\n" +
                "(CASE WHEN cont_size IN('20','40','45','42')  THEN 1 ELSE NULL END) AS grand_tot,\n" +
                "(CASE WHEN cont_size=20  THEN 1 ELSE 2 END) AS tues     \n" +
                "FROM ctmsmis.mis_exp_unit\n" +
                "LEFT JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey\n" +
                " \n" +
                "WHERE  mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' AND snx_type=0 AND cont_mlo IS NOT NULL "+cond+"\n" +
                ") AS tmp GROUP BY mlo WITH ROLLUP";

        System.out.println("result:"+sqlQuery);
        List resultList=SecondaryDBTemplate.query(sqlQuery,new VessleListWithStatusForMloWiseExportSummary());
        System.out.println("sql:"+resultList);
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class VessleListWithStatusForMloWiseExportSummary implements RowMapper{

        @Override
        public MloWiseFinalLodingExportReportApps mapRow(ResultSet rs, int rowNum) throws SQLException {
            MloWiseFinalLodingExportReportApps mloWiseFinalLodingExportReportApps =new MloWiseFinalLodingExportReportApps();
            mloWiseFinalLodingExportReportApps.setMlo_name(rs.getString("mlo_name"));
            mloWiseFinalLodingExportReportApps.setMlo(rs.getString("mlo"));
            mloWiseFinalLodingExportReportApps.setD_20(rs.getInt("D_20"));
            mloWiseFinalLodingExportReportApps.setD_40(rs.getInt("D_40"));
            mloWiseFinalLodingExportReportApps.setH_40(rs.getInt("H_40"));
            mloWiseFinalLodingExportReportApps.setH_45(rs.getInt("H_45"));
            mloWiseFinalLodingExportReportApps.setRH_40(rs.getInt("RH_40"));
            mloWiseFinalLodingExportReportApps.setR_20(rs.getInt("R_20"));
            mloWiseFinalLodingExportReportApps.setOT_20(rs.getInt("OT_20"));
            mloWiseFinalLodingExportReportApps.setFR_20(rs.getInt("FR_20"));
            mloWiseFinalLodingExportReportApps.setTK_20(rs.getInt("TK_20"));
            mloWiseFinalLodingExportReportApps.setFR_40(rs.getInt("FR_40"));
            mloWiseFinalLodingExportReportApps.setOT_40(rs.getInt("OT_40"));
            mloWiseFinalLodingExportReportApps.setMD_20(rs.getInt("MD_20"));
            mloWiseFinalLodingExportReportApps.setMD_40(rs.getInt("MD_40"));
            mloWiseFinalLodingExportReportApps.setMH_40(rs.getInt("MH_40"));
            mloWiseFinalLodingExportReportApps.setMH_45(rs.getInt("MH_45"));
            mloWiseFinalLodingExportReportApps.setMRH_40(rs.getInt("MRH_40"));
            mloWiseFinalLodingExportReportApps.setMR_20(rs.getInt("MR_20"));
            mloWiseFinalLodingExportReportApps.setMOT_20(rs.getInt("MOT_20"));
            mloWiseFinalLodingExportReportApps.setMFR_20(rs.getInt("MFR_20"));
            mloWiseFinalLodingExportReportApps.setMTK_20(rs.getInt("MTK_20"));
            mloWiseFinalLodingExportReportApps.setMFR_40(rs.getInt("MFR_40"));
            mloWiseFinalLodingExportReportApps.setMOT_40(rs.getInt("MOT_40"));
            mloWiseFinalLodingExportReportApps.setGrand_tot(rs.getInt("grand_tot"));
            mloWiseFinalLodingExportReportApps.setTues(rs.getInt("tues"));

            return mloWiseFinalLodingExportReportApps;
        }
    }


}



