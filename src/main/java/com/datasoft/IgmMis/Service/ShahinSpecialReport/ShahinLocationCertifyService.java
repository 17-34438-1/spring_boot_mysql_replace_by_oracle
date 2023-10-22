package com.datasoft.IgmMis.Service.ShahinSpecialReport;

import com.datasoft.IgmMis.Model.ShahinSpecialReport.ShahinLocationCertify;
import com.datasoft.IgmMis.Model.ShahinSpecialReport.ShahinLocationCertifyModel;
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
public class ShahinLocationCertifyService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;

    public List getLoadedContainerList(String ddl_imp_cont_no, String ddl_imp_bl_no){

        List<ShahinLocationCertify> list=new ArrayList<>();
        List<ShahinLocationCertifyModel> listfcy=new ArrayList<>();
        ShahinLocationCertify lastResutModel=new ShahinLocationCertify();
        ShahinLocationCertifyModel lastResutfcyModel=new ShahinLocationCertifyModel();
        String rtnBlNo = "";
        String rtnRotation = "";

        String cont_number="";
        Long id;

        String totcontainerNo="";
        String totContQute="";
        Integer t20 = 0;
        Integer t40 = 0;
        Integer t45 = 0;
        Integer cont_size=0;


        System.out.println("ddl_imp_cont_no:"+ddl_imp_cont_no);
        System.out.println("ddl_imp_bl_no:"+ddl_imp_bl_no);
        if(ddl_imp_cont_no!="")
        {
            String sqlBl="select BL_No,igm_details.Import_Rotation_No from igm_details inner join igm_detail_container on igm_detail_container.igm_detail_id=igm_details.id where cont_number='"+ddl_imp_cont_no+"' order by igm_detail_container.id desc limit 1";

            List<ShahinLocationCertify> resultList=primaryDBTemplate.query(sqlBl,new LoadedContainerList());

            for(Integer i=0;i<resultList.size();i++){
                rtnBlNo = resultList.get(i).getDdl_imp_bl_no();
                lastResutModel.setDdl_imp_bl_no(rtnBlNo);

                rtnRotation=  resultList.get(i).getImport_Rotation_No();
                lastResutModel.setImport_Rotation_No(rtnRotation);
            }
        }


        if(ddl_imp_bl_no!="")
        {
            String sqlBl="select BL_No,igm_details.Import_Rotation_No from igm_details inner join igm_detail_container on igm_detail_container.igm_detail_id=igm_details.id where BL_No='"+ddl_imp_bl_no+"' order by igm_detail_container.id desc limit 1";
            List<ShahinLocationCertify> resultList=primaryDBTemplate.query(sqlBl,new LoadedContainerList());
            System.out.println("Sql:"+sqlBl);

            for(Integer i=0;i<resultList.size();i++){
                rtnBlNo = resultList.get(i).getDdl_imp_bl_no();

                lastResutModel.setDdl_imp_bl_no(rtnBlNo);
                rtnRotation=  resultList.get(i).getImport_Rotation_No();
                lastResutModel.setImport_Rotation_No(rtnRotation);
            }

        }
        String sqlBl="SELECT igm_details.id,cont_number,igm_details.Import_Rotation_No,(SELECT Vessel_Name FROM igm_masters \n" +
                "WHERE igm_masters.id=igm_details.IGM_id) AS vsl_name,igm_details.BL_No,\n" +
                "cont_size,cont_height,off_dock_id,\n" +
                "(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_detail_container.off_dock_id) AS offdock_name,\n" +
                "cont_status,cont_seal_number,cont_iso_type FROM igm_detail_container \n" +
                "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
                "WHERE igm_details.BL_No='"+rtnBlNo+"' AND igm_details.Import_Rotation_No='"+rtnRotation+"'\n" +
                "UNION\n" +
                "SELECT igm_details.id,cont_number,igm_details.Import_Rotation_No,(SELECT Vessel_Name FROM igm_masters \n" +
                "WHERE igm_masters.id=igm_supplimentary_detail.igm_master_id) AS vsl_name,igm_details.BL_No,\n" +
                "cont_size,cont_height,off_dock_id,\n" +
                "(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_sup_detail_container.off_dock_id) AS offdock_name,\n" +
                "cont_status,cont_seal_number,cont_iso_type FROM igm_sup_detail_container \n" +
                "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
                "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id\n" +
                "WHERE igm_supplimentary_detail.BL_No='"+rtnBlNo+"' AND igm_details.Import_Rotation_No='"+rtnRotation+"'";


        List<ShahinLocationCertify> resultListForLocation=primaryDBTemplate.query(sqlBl,new LoadedContainer());
        List<ShahinLocationCertify> listmodel=new ArrayList<>();
        for(Integer i=0;i<resultListForLocation.size();i++){

            lastResutModel=resultListForLocation.get(i);
            ShahinLocationCertify tempContainerListModel=resultListForLocation.get(i);
            cont_number=tempContainerListModel.getDdl_imp_cont_no();
            lastResutModel.setDdl_imp_cont_no(cont_number);

            id=tempContainerListModel.getId();
            lastResutModel.setId(id);

            lastResutModel.setCont_iso_type(tempContainerListModel.getCont_iso_type());
            lastResutModel.setCont_status(tempContainerListModel.getCont_status());
            lastResutModel.setOff_dock_id(tempContainerListModel.getOff_dock_id());
            lastResutModel.setOffdock_name(tempContainerListModel.getOffdock_name());

            rtnRotation=tempContainerListModel.getImport_Rotation_No();
            lastResutModel.setImport_Rotation_No(rtnRotation);
            lastResutModel.setVsl_name(tempContainerListModel.getVsl_name());
            lastResutModel.setCont_seal_number(tempContainerListModel.getCont_seal_number());
            cont_size=tempContainerListModel.getCont_size();
            lastResutModel.setCont_size(cont_size);
            lastResutModel.setCont_height(tempContainerListModel.getCont_height());

            if(tempContainerListModel.getCont_size()==20){
                t20 = t20+1;
            }

            else if(tempContainerListModel.getCont_size()==40){
                t40 = t40+1;
            }

            else if(tempContainerListModel.getCont_size()==45){
                t45 = t45+1;
            }

            System.out.println("t20.................:"+t20);
            System.out.println("t20.................:"+cont_size);

            String sqlSub_bl="select  group_concat(igm_supplimentary_detail.BL_No) as sub_bl from igm_supplimentary_detail where igm_detail_id='"+id+"'";
            List<ShahinLocationCertifyModel> resultListForSubBl=primaryDBTemplate.query(sqlSub_bl,new LoadedFcyForSubBlContainer());
            System.out.println("Sql123456:"+resultListForSubBl.size());
            ShahinLocationCertifyModel shahinLocationCertifySubBlModel;
            for(Integer j=0;j<resultListForSubBl.size();j++){
                shahinLocationCertifySubBlModel=resultListForSubBl.get(j);
                lastResutModel.setSub_bl(shahinLocationCertifySubBlModel.getSub_bl());
            }

            String sqlFcy="SELECT fcy_time_in,fcy_last_pos_slot,fcy_position_name,yard,fcy_time_out,(SELECT ctmsmis.cont_block(fcy_last_pos_slot,yard)) AS block,time_move FROM (\n" +
                    "SELECT time_in AS fcy_time_in,last_pos_slot AS fcy_last_pos_slot,last_pos_name AS fcy_position_name,ctmsmis.cont_yard(last_pos_slot) AS yard,time_out AS fcy_time_out,time_move \n" +
                    "FROM inv_unit a\n" +
                    "INNER JOIN \n" +
                    "inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=a.gkey\n" +
                    "INNER JOIN argo_carrier_visit h ON h.gkey = inv_unit_fcy_visit.actual_ib_cv\n" +
                    "INNER JOIN\n" +
                    "argo_visit_details i ON h.cvcvd_gkey = i.gkey\n" +
                    "INNER JOIN\n" +
                    "vsl_vessel_visit_details ww ON ww.vvd_gkey = i.gkey WHERE ib_vyg='"+rtnRotation+"' AND a.id='"+cont_number+"'\n" +
                    ") AS  tmp\n";


            List<ShahinLocationCertifyModel> resultListForFcy=SecondaryDBTemplate.query(sqlFcy,new LoadedFcyContainer());

            System.out.println("Sql123456:"+resultListForFcy.size());
            System.out.println("Sql123456:"+sqlFcy);

            ShahinLocationCertifyModel shahinLocationCertifyModel;
            for(Integer j=0;j<resultListForFcy.size();j++){
                shahinLocationCertifyModel=resultListForFcy.get(j);
                lastResutModel.setFcy_time_out(shahinLocationCertifyModel.getFcy_time_out());
                lastResutModel.setTime_move(shahinLocationCertifyModel.getTime_move());
                lastResutModel.setYard(shahinLocationCertifyModel.getYard());
                lastResutModel.setBlock(shahinLocationCertifyModel.getBlock());
                lastResutModel.setFcy_time_in(shahinLocationCertifyModel.getFcy_time_in());
                lastResutModel.setFcy_last_pos_slot(shahinLocationCertifyModel.getFcy_last_pos_slot());
                lastResutModel.setFcy_position_name(shahinLocationCertifyModel.getFcy_position_name());
            }
            if(totcontainerNo!="")
            {
                totcontainerNo=totcontainerNo+", "+cont_number;
                totContQute = totContQute+",'"+cont_number+"'";
            }
            else
            {
                totcontainerNo=cont_number;
                totContQute="'"+cont_number+"'";
            }
            list.add(lastResutModel);
        }
        lastResutModel.setT20(t20);
        lastResutModel.setT40(t40);
        lastResutModel.setT45(t45);
        lastResutModel.setCont(totContQute);

        String sqlTotCont="SELECT IFNULL(DATE(time_move),'') AS time_move,COUNT(a.id) AS totcont FROM inv_unit a INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=a.gkey INNER JOIN argo_carrier_visit h ON (h.gkey = a.declrd_ib_cv OR h.gkey = a.cv_gkey) INNER JOIN argo_visit_details i ON h.cvcvd_gkey = i.gkey INNER JOIN vsl_vessel_visit_details ww ON ww.vvd_gkey = i.gkey WHERE ib_vyg='"+rtnRotation+"' AND a.id IN("+totContQute+") GROUP BY DATE(time_move)";
        List<ShahinLocationCertify> resultListForTotCont=SecondaryDBTemplate.query(sqlTotCont,new LoadedFcyForTotalCountContainer());
        System.out.println("Sql123456:"+sqlTotCont);
        ShahinLocationCertify resultListForTotContQute;
        for(Integer j=0;j<resultListForTotCont.size();j++){
            resultListForTotContQute=resultListForTotCont.get(j);
            lastResutModel.setTotcont(resultListForTotContQute.getTotcont());
            lastResutModel.setTime_mov(resultListForTotContQute.getTime_mov());

        }

        return list;
    }

    class LoadedContainerList implements RowMapper {

        @Override
        public ShahinLocationCertify mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShahinLocationCertify shahinLocationCertify =new ShahinLocationCertify();
            shahinLocationCertify.setDdl_imp_bl_no(rs.getString("BL_No"));
            shahinLocationCertify.setImport_Rotation_No(rs.getString("Import_Rotation_No"));
            return shahinLocationCertify;
        }
    }



    class LoadedContainer implements RowMapper {

        @Override
        public ShahinLocationCertify mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShahinLocationCertify shahinLocationCertify =new ShahinLocationCertify();
            shahinLocationCertify.setId(rs.getLong("id"));
            shahinLocationCertify.setImport_Rotation_No(rs.getString("Import_Rotation_No"));
            shahinLocationCertify.setDdl_imp_cont_no(rs.getString("cont_number"));
            shahinLocationCertify.setDdl_imp_bl_no(rs.getString("BL_No"));

            shahinLocationCertify.setCont_iso_type(rs.getString("cont_iso_type"));
            shahinLocationCertify.setCont_status(rs.getString("cont_status"));
            shahinLocationCertify.setOff_dock_id(rs.getString("off_dock_id"));
            shahinLocationCertify.setOffdock_name(rs.getString("offdock_name"));
            shahinLocationCertify.setVsl_name(rs.getString("vsl_name"));
            shahinLocationCertify.setCont_seal_number(rs.getString("cont_seal_number"));
            shahinLocationCertify.setCont_size(rs.getInt("cont_size"));
            shahinLocationCertify.setCont_height(rs.getString("cont_height"));
            return shahinLocationCertify;
        }
    }



    class LoadedFcyContainer implements RowMapper {

        @Override
        public ShahinLocationCertifyModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShahinLocationCertifyModel shahinLocationCertify =new ShahinLocationCertifyModel();

            shahinLocationCertify.setFcy_time_out(rs.getTimestamp("fcy_time_out"));
            shahinLocationCertify.setTime_move(rs.getTimestamp("time_move"));
            shahinLocationCertify.setYard(rs.getString("yard"));
            shahinLocationCertify.setFcy_time_in(rs.getTimestamp("fcy_time_in"));
            shahinLocationCertify.setFcy_last_pos_slot(rs.getString("fcy_last_pos_slot"));
            shahinLocationCertify.setFcy_position_name(rs.getString("fcy_position_name"));

            return shahinLocationCertify;
        }
    }
    class LoadedFcyForSubBlContainer implements RowMapper {
        @Override
        public ShahinLocationCertifyModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShahinLocationCertifyModel shahinLocationCertify =new ShahinLocationCertifyModel();
            shahinLocationCertify.setSub_bl(rs.getString("sub_bl"));
            return shahinLocationCertify;
        }
    }
    class LoadedFcyForTotalCountContainer implements RowMapper {
        @Override
        public ShahinLocationCertify mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShahinLocationCertify shahinLocationCertify =new ShahinLocationCertify();
            shahinLocationCertify.setTotcont(rs.getString("totcont"));
            shahinLocationCertify.setTime_mov(rs.getTimestamp("time_move"));
            return shahinLocationCertify;
        }
    }



}

