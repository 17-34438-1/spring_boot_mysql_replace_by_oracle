package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.*;
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
public class MisAssignmentService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    public List getMisAssignmentAssignType(String terminal){
        List resultList=new ArrayList();
        String sqlQuery="";
        sqlQuery="SELECT DISTINCT \n" +
                "(CASE\n" +
                "WHEN mfdch_value='APPDLV2H' THEN 'APPDLV2HG'\n" +
                "WHEN mfdch_value='APPDLVGRD' THEN 'APPDLV2HG'\n" +
                "WHEN mfdch_value='DLVREF2H' THEN 'DLVREF2H'\n" +
                "WHEN mfdch_value='DLVREFGRD' THEN 'DLVREF2H'\n" +
                "ELSE mfdch_value END ) AS mfdch_value,(\n" +
                "CASE\n" +
                "WHEN mfdch_desc='Appraise Cum Delivery 2 High' THEN 'Appraise Cum DLV/GROUND/2H'\n" +
                "WHEN mfdch_desc='Appraise Cum Delivery Ground' THEN 'Appraise Cum DLV/GROUND/2H'\n" +
                "WHEN mfdch_desc='Delivery Reefer 2 High' THEN 'Reefer GROUND/2H'\n" +
                "WHEN mfdch_desc='Delivery Reefer Ground' THEN 'Reefer GROUND/2H'\n" +
                "ELSE\n" +
                "mfdch_desc\n" +
                "END\n" +
                ") AS mfdch_desc\n" +
                "FROM\n" +
                "(\n" +
                "SELECT (CASE\n" +
                "WHEN\n" +
                "config_metafield_lov.mfdch_value='APPREF'\n" +
                "THEN\n" +
                "'APPCUS'\n" +
                "WHEN\n" +
                "'"+terminal+"'='GCB' AND (mfdch_value='DLVGRD' OR mfdch_value='DLVOTH' OR mfdch_value='DLVHYS')\n" +
                "THEN\n" +
                "'DLV2H'\n" +
                "WHEN\n" +
                "'"+terminal+"'!='GCB' AND (mfdch_value='DLVGRD' OR mfdch_value='DLVOTH')\n" +
                "THEN\n" +
                "'DLV2H'\n" +
                "ELSE\n" +
                "config_metafield_lov.mfdch_value\n" +
                "END\t\t\n" +
                ") AS mfdch_value,\n" +
                "(CASE\n" +
                "WHEN\n" +
                "config_metafield_lov.mfdch_desc='Appraise Reefer'\n" +
                "THEN\n" +
                "'Customs Appraise'\n" +
                "WHEN\n" +
                "'"+terminal+"'='GCB' AND (mfdch_desc='Delivery Ground' OR mfdch_desc='Delivery 2 High' OR mfdch_desc='Delivery Others' OR mfdch_desc='Delivery Hyster')\n" +
                "THEN\n" +
                "'Delivery Others/Ground/2 High/Hyster'\n" +
                "WHEN\n" +
                "'"+terminal+"'!='GCB' AND (mfdch_desc='Delivery Ground' OR mfdch_desc='Delivery 2 High' OR mfdch_desc='Delivery Others')\n" +
                "THEN\n" +
                "'Delivery Others/Ground/2 High'\n" +
                "ELSE\n" +
                "config_metafield_lov.mfdch_desc\n" +
                "END\t\t\n" +
                ") AS mfdch_desc\n" +
                "\t\t\t\t\n" +
                "FROM sparcsn4.config_metafield_lov WHERE mfdch_metafield=331 AND mfdch_value!='--'\n" +
                ") AS tmp";
        resultList=secondaryDBTemplate.query(sqlQuery,new MisAssignmentService.MfdchValueAndDescription());
        return resultList;
    }
    public List getMisAssignmentBlockList( String terminal){
        List resultList=new ArrayList();
        String sqlQuery="";
        sqlQuery="SELECT DISTINCT block_cpa AS Block_No FROM ctmsmis.yard_block WHERE terminal='"+terminal+"' ORDER BY id";
        resultList=secondaryDBTemplate.query(sqlQuery,new MisAssignmentService.BlockNo());
        return resultList;
    }

    public List getMisAssignmentCctNctOfyList(String date,String terminal,String block,String assignType){
        List<MisAssignmentCctNctOfyModel> mainList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="SELECT DISTINCT mfdch_value,mfdch_desc \n" +
                "FROM ctmsmis.tmp_vcms_assignment\n" +
                "WHERE date(flex_date01)='"+date+"' AND \n" +
                "(CASE\n" +
                "WHEN\n" +
                "'"+assignType+"' !='ALLASSIGN'\n" +
                "THEN\n" +
                "Yard_No='"+terminal+"' AND mfdch_value='"+assignType+"'\n" +
                "ELSE\n" +
                "Yard_No='"+terminal+"'\n" +
                "END)\n" +
                "ORDER BY Yard_No,mfdch_value,flex_date01,bl_no";
        List<MisAssigmentModel> resultList= secondaryDBTemplate.query(sqlQuery,new MisAssignmentService.MfdchValueAndDescription());
        String mfdchValue="";
        String mfdchDescription="";
        for(int i=0;i< resultList.size(); i++){
            String query="";
            String query1="";
            List<MisAssigmentModel> list=new ArrayList<>();
            MisAssignmentCctNctOfyModel mainModel=new MisAssignmentCctNctOfyModel();
            MisAssigmentModel misAssigmentModel=resultList.get(i);
            mfdchValue=misAssigmentModel.getMfdch_value();
            mfdchDescription=misAssigmentModel.getMfdch_desc();
            mainModel.setMfdch_value(mfdchValue);
            mainModel.setMfdch_desc(mfdchDescription);
             query="SELECT DISTINCT * FROM ctmsmis.tmp_vcms_assignment\n" +
                    "WHERE assignmentDate = '"+date+"' AND mfdch_value='"+mfdchValue+"' AND Yard_No='"+terminal+"' ORDER BY Yard_No,mfdch_value,flex_date01,bl_no";
             List<MisAssigmentModel> vcmsInfoList=secondaryDBTemplate.query(query,new MisAssignmentService.TmpVcmsAssignmentInfo());
             int j=0;
             int t20=0;
             int t40=0;
             int total=0;
             int tues=0;
             String cnfName="";
             String blNo="";
             for(int k=0;k<vcmsInfoList.size();k++){
                 total=total+1;
                 MisAssigmentModel resultModel=new MisAssigmentModel();
                 MisAssigmentModel vcmsModel=vcmsInfoList.get(k);
                 String formattedHeight="";
                 String containerNo="";
                 String timeOut="";
                 String dlv="";
                 containerNo=vcmsModel.getCont_no();
                 containerNo=containerNo.replace("-","");
                 formattedHeight=String.format("%.1f",vcmsModel.getHeight());
                 if(!cnfName.equals(vcmsModel.getCf_name()) || !blNo.equals(vcmsModel.getBl_no())){
                     j=j+1;

                     cnfName=vcmsModel.getCf_name();
                     blNo=vcmsModel.getBl_no();
                 }
                 if(vcmsModel.getSize()==20){
                     t20=t20+1;

                 }
                 else {
                     t40=t40+1;
                 }
                 resultModel.setSlNo(j);
                 resultModel.setCf_name(vcmsModel.getCf_name());
                 resultModel.setV_name(vcmsModel.getV_name());
                 resultModel.setRot_no(vcmsModel.getRot_no());
                 resultModel.setMlo(vcmsModel.getMlo());
                 resultModel.setSeal_nbr1(vcmsModel.getSeal_nbr1());
                 resultModel.setCont_no(vcmsModel.getCont_no());
                 resultModel.setSize(vcmsModel.getSize());
                 resultModel.setBl_no(vcmsModel.getBl_no());
                 resultModel.setSlot(vcmsModel.getSlot());
                 resultModel.setRemarks(vcmsModel.getRemarks());
                 resultModel.setHeight(vcmsModel.getHeight());
                 resultModel.setFormattedHeight(formattedHeight);
                 query1="SELECT time_out FROM sparcsn4.inv_unit \n" +
                         "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                         "WHERE sparcsn4.inv_unit.id='"+containerNo+"' AND category='IMPRT'\n" +
                         "order by inv_unit.gkey desc limit 1";
                 List<MisAssigmentModel> timeOutList=secondaryDBTemplate.query(query1,new MisAssignmentService.TimeOut());
                 if(timeOutList.size()>0){
                    timeOut=timeOutList.get(0).getTime_out();
                 }
                 if(timeOut!=null){
                     dlv="Yes";
                 }
                 else{
                     dlv="";
                 }
                 resultModel.setDlv(dlv);
                 list.add(resultModel);

             }
             mainModel.setResultList(list);
             mainModel.setT20(t20);
             mainModel.setT40(t40);
             mainModel.setTotal(total);
             mainModel.setTues(t20+(t40*2));
             mainList.add(mainModel);



        }

        return mainList;
    }

    public List getMisAssignmentGcbList(String date,String terminal,String block,String assignType){
        List<MisAssignmentGcbModel> gcbList=new ArrayList<>();
        String blockQuery="";
        blockQuery="SELECT DISTINCT Block_No FROM ctmsmis.tmp_vcms_assignment\n" +
                "WHERE date(flex_date01)='"+date+"' AND \n" +
                "(CASE\n" +
                "WHEN\n" +
                "'"+block+"' !='ALLBLOCK'\n" +
                "THEN\n" +
                "(CASE\n" +
                "WHEN\n" +
                "'"+assignType+"' !='ALLASSIGN'\n" +
                "THEN\n" +
                "Yard_No='"+terminal+"' AND Block_No='"+block+"' AND mfdch_value='"+assignType+"'\n" +
                "ELSE\n" +
                "Yard_No='"+terminal+"' AND Block_No='"+block+"'\n" +
                "END)\n" +
                "ELSE\n" +
                "(CASE\n" +
                "WHEN\n" +
                "'"+assignType+"' !='ALLASSIGN'\n" +
                "THEN\n" +
                "Yard_No='"+terminal+"' AND mfdch_value='"+assignType+"'\n" +
                "ELSE\n" +
                "Yard_No='"+terminal+"'\n" +
                "END)\n" +
                "END)\n" +
                "ORDER BY Yard_No,Block_No,mfdch_value,flex_date01,bl_no";
        List<MisAssigmentModel> resultBlockList=secondaryDBTemplate.query(blockQuery,new MisAssignmentService.BlockNo());
        for(int a=0; a<resultBlockList.size();a++){
            List<MisAssignmentCctNctOfyModel> mainList=new ArrayList<>();
            MisAssignmentGcbModel misAssignmentGcbModel=new MisAssignmentGcbModel();
            String sqlQuery="";
            String blockNo="";
            MisAssigmentModel blockModel=resultBlockList.get(a);
            blockNo=blockModel.getBlock_No();
            misAssignmentGcbModel.setBlockNo(blockNo);
            if(block.equals("ALLBLOCK")){
                if(assignType.equals("ALLASSIGN")){
                    sqlQuery="select distinct mfdch_value,mfdch_desc from ctmsmis.tmp_vcms_assignment where Yard_No='"+terminal+"' and Block_No='"+blockNo+"' and date(flex_date01)='"+date+"'";

                }
                else{
                    sqlQuery="select distinct mfdch_value,mfdch_desc from ctmsmis.tmp_vcms_assignment where Yard_No='"+terminal+"' AND Block_No='"+blockNo+"' and mfdch_value='"+assignType+"' and date(flex_date01)='"+date+"'";

                }

            }
            else{
                if(assignType.equals("ALLASSIGN")){
                    sqlQuery="select distinct mfdch_value,mfdch_desc from ctmsmis.tmp_vcms_assignment where Yard_No='"+terminal+"' AND Block_No='"+blockNo+"' and date(flex_date01)='"+date+"'";

                }
                else{
                    sqlQuery="select distinct mfdch_value,mfdch_desc from ctmsmis.tmp_vcms_assignment where Yard_No='"+terminal+"' AND Block_No='"+blockNo+"' AND mfdch_value='"+assignType+"' and date(flex_date01)='"+date+"'";

                }

            }

            List<MisAssigmentModel> resultList= secondaryDBTemplate.query(sqlQuery,new MisAssignmentService.MfdchValueAndDescription());
            String mfdchValue="";
            String mfdchDescription="";
            for(int i=0;i< resultList.size(); i++){
                String query="";
                String query1="";
                List<MisAssigmentModel> list=new ArrayList<>();
                MisAssignmentCctNctOfyModel mainModel=new MisAssignmentCctNctOfyModel();
                MisAssigmentModel misAssigmentModel=resultList.get(i);
                mfdchValue=misAssigmentModel.getMfdch_value();
                mfdchDescription=misAssigmentModel.getMfdch_desc();
                mainModel.setMfdch_value(mfdchValue);
                mainModel.setMfdch_desc(mfdchDescription);
                query="SELECT DISTINCT * FROM ctmsmis.tmp_vcms_assignment\n" +
                        "WHERE mfdch_value='"+mfdchValue+"' and Block_No='"+blockNo+"' and assignmentDate='"+date+"' ORDER BY Yard_No,Block_No,mfdch_value,flex_date01";
                List<MisAssigmentModel> vcmsInfoList=secondaryDBTemplate.query(query,new MisAssignmentService.TmpVcmsAssignmentInfo());
                int j=0;
                int t20=0;
                int t40=0;
                int total=0;
                int tues=0;
                String cnfName="";
                String blNo="";
                for(int k=0;k<vcmsInfoList.size();k++){
                    total=total+1;
                    MisAssigmentModel resultModel=new MisAssigmentModel();
                    MisAssigmentModel vcmsModel=vcmsInfoList.get(k);
                    String formattedHeight="";
                    String containerNo="";
                    String timeOut="";
                    String dlv="";
                    containerNo=vcmsModel.getCont_no();
                    containerNo=containerNo.replace("-","");
                    formattedHeight=String.format("%.1f",vcmsModel.getHeight());
                    if(!cnfName.equals(vcmsModel.getCf_name()) || !blNo.equals(vcmsModel.getBl_no())){
                        j=j+1;
                        cnfName=vcmsModel.getCf_name();
                        blNo=vcmsModel.getBl_no();
                    }
                    if(vcmsModel.getSize()==20){
                        t20=t20+1;

                    }
                    else {
                        t40=t40+1;
                    }
                    resultModel.setSlNo(j);
                    resultModel.setCf_name(vcmsModel.getCf_name());
                    resultModel.setV_name(vcmsModel.getV_name());
                    resultModel.setRot_no(vcmsModel.getRot_no());
                    resultModel.setMlo(vcmsModel.getMlo());
                    resultModel.setSeal_nbr1(vcmsModel.getSeal_nbr1());
                    resultModel.setCont_no(vcmsModel.getCont_no());
                    resultModel.setSize(vcmsModel.getSize());
                    resultModel.setBl_no(vcmsModel.getBl_no());
                    resultModel.setSlot(vcmsModel.getSlot());
                    resultModel.setRemarks(vcmsModel.getRemarks());
                    resultModel.setHeight(vcmsModel.getHeight());
                    resultModel.setFormattedHeight(formattedHeight);
                    query1="SELECT time_out FROM sparcsn4.inv_unit \n" +
                            "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                            "WHERE sparcsn4.inv_unit.id='"+containerNo+"' AND category='IMPRT'\n" +
                            "order by inv_unit.gkey desc limit 1";
                    List<MisAssigmentModel> timeOutList=secondaryDBTemplate.query(query1,new MisAssignmentService.TimeOut());
                    if(timeOutList.size()>0){
                        timeOut=timeOutList.get(0).getTime_out();
                    }
                    if(timeOut!=null){
                        dlv="Yes";
                    }
                    else{
                        dlv="";
                    }
                    resultModel.setDlv(dlv);
                    list.add(resultModel);

                }
                mainModel.setResultList(list);
                mainModel.setT20(t20);
                mainModel.setT40(t40);
                mainModel.setTotal(total);
                mainModel.setTues(t20+(t40*2));
                mainList.add(mainModel);



            }
            misAssignmentGcbModel.setGcbList(mainList);
            gcbList.add(misAssignmentGcbModel);

        }
        return gcbList;
    }

    class MfdchValueAndDescription implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            MisAssigmentModel misAssigmentModel=new MisAssigmentModel();
            misAssigmentModel.setMfdch_value(rs.getString("mfdch_value"));
            misAssigmentModel.setMfdch_desc(rs.getString("mfdch_desc"));

            return misAssigmentModel;
        }
    }

    class BlockNo implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            MisAssigmentModel misAssigmentModel=new MisAssigmentModel();
            misAssigmentModel.setBlock_No(rs.getString("Block_No"));


            return misAssigmentModel;
        }
    }
    class TmpVcmsAssignmentInfo implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            MisAssigmentModel misAssigmentModel=new MisAssigmentModel();
            misAssigmentModel.setCf_name(rs.getString("cf_name"));
            misAssigmentModel.setV_name(rs.getString("v_name"));
            misAssigmentModel.setRot_no(rs.getString("rot_no"));
            misAssigmentModel.setMlo(rs.getString("mlo"));
            misAssigmentModel.setSeal_nbr1(rs.getString("seal_nbr1"));
            misAssigmentModel.setCont_no(rs.getString("cont_no"));
            misAssigmentModel.setSize(rs.getInt("size"));
            misAssigmentModel.setHeight(rs.getFloat("height"));
            misAssigmentModel.setBl_no(rs.getString("bl_no"));
            misAssigmentModel.setSlot(rs.getString("slot"));
            misAssigmentModel.setRemarks(rs.getString("remarks"));
            return misAssigmentModel;
        }
    }

    class TimeOut implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            MisAssigmentModel misAssigmentModel=new MisAssigmentModel();
            misAssigmentModel.setTime_out(rs.getString("time_out"));
            return misAssigmentModel;
        }
    }
}
