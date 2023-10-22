package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.HeadDeliveryRegisterCctNctModel;
import com.datasoft.IgmMis.Model.ImportReport.HeadDeliveryRegisterContainerNoModel;
import com.datasoft.IgmMis.Model.ImportReport.HeadDeliveryRegisterGcbModel;
import com.datasoft.IgmMis.Model.ImportReport.HeadDeliveryRegisterModel;
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
public class HeadDeliveryRegisterService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List getAssignType( String terminal){
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
        resultList=secondaryDBTemplate.query(sqlQuery,new HeadDeliveryRegisterService.MfdchValueAndDescription());
        return resultList;
    }
    public List getBlockList( String terminal){
        List resultList=new ArrayList();
        String sqlQuery="";
        sqlQuery="SELECT DISTINCT block_cpa AS Block_No FROM ctmsmis.yard_block WHERE terminal='"+terminal+"' ORDER BY id";
        resultList=secondaryDBTemplate.query(sqlQuery,new HeadDeliveryRegisterService.BlockNo());
        return resultList;
    }
    public List getHeadDeliveryRegisterCctNctList(String date,String terminal,String block,String assignType){
        List<HeadDeliveryRegisterCctNctModel> mainList=new ArrayList<>();
            String sqlQuery="";
            if(!assignType.equals("ALLASSIGN")){
                sqlQuery="SELECT DISTINCT mfdch_value,mfdch_desc FROM ctmsmis.tmp_vcms_assignment WHERE assignmentDate = '"+date+"' AND \n" +
                        "Yard_No='"+terminal+"' AND mfdch_value='"+assignType+"'\n" +
                        "ORDER BY Yard_No,mfdch_value,flex_date01,bl_no";

            }
            else{
                sqlQuery="SELECT DISTINCT mfdch_value,mfdch_desc FROM ctmsmis.tmp_vcms_assignment WHERE assignmentDate = '"+date+"' AND \n" +
                        "Yard_No='"+terminal+"'\n" +
                        "ORDER BY Yard_No,mfdch_value,flex_date01,bl_no";
            }

            List<HeadDeliveryRegisterModel> resultList= secondaryDBTemplate.query(sqlQuery,new HeadDeliveryRegisterService.MfdchValueAndDescription());
            String mfdchValue="";
            String mfdchDescription="";
            for(int i=0;i<resultList.size();i++){
                List<HeadDeliveryRegisterModel> list=new ArrayList<>();
                HeadDeliveryRegisterCctNctModel mainModel=new HeadDeliveryRegisterCctNctModel();
                String query1="";
                HeadDeliveryRegisterModel tempModel=resultList.get(i);
                mfdchValue=tempModel.getMfdch_value();
                mfdchDescription=tempModel.getMfdch_desc();
                mainModel.setMfdch_desc(mfdchDescription);
                mainModel.setMfdch_value(mfdchValue);

                query1="SELECT DISTINCT cf_name,cont_no,size,v_name,rot_no,bl_no FROM ctmsmis.tmp_vcms_assignment\n" +
                        "WHERE assignmentDate = '"+date+"' AND mfdch_value='"+mfdchValue+"' AND Yard_No='"+terminal+"' ORDER BY Yard_No,mfdch_value,flex_date01,bl_no";
                List<HeadDeliveryRegisterModel> tmpVcmsList= secondaryDBTemplate.query(query1,new HeadDeliveryRegisterService.TmpVcmsAssignmentInfo());
                for(int j=0; j<tmpVcmsList.size();j++){
                    String query2="";
                    String query3="";
                    String query4="";
                    String  rotation="";
                    String  blno="";
                    String containerNo="";
                    String cfName="";
                    String vesselName="";
                    String manif="";
                    String stc="";
                    String weight="";
                    String beNo = "";
                    String beDate ="";
                    Integer size=0;
                    String containerAndSize="";
                    HeadDeliveryRegisterModel resultModel=new HeadDeliveryRegisterModel();
                    HeadDeliveryRegisterModel cfInfoModel=tmpVcmsList.get(j);

                    rotation=cfInfoModel.getRot_no();
                    blno=cfInfoModel.getBl_no();
                    containerNo=cfInfoModel.getCont_no();
                    size=cfInfoModel.getSize();
                    cfName=cfInfoModel.getCf_name();
                    vesselName=cfInfoModel.getV_name();
                    containerAndSize=containerNo+" x "+size+"'";
                    manif=rotation.replace('/',' ');



                    resultModel.setRot_no(rotation);
                    resultModel.setBl_no(blno);
                    resultModel.setCont_no(containerNo);
                    resultModel.setCf_name(cfName);
                    resultModel.setV_name(vesselName);
                    resultModel.setSize(size);
                    resultModel.setContainerNoAndSize(containerAndSize);

                    query2="SELECT CONCAT(Pack_Number,' ',Pack_Description) AS stc,concat(weight,' KG') as weight FROM igm_details WHERE Import_Rotation_No='"+rotation+"' AND replace(BL_No,'/','')='"+blno+"'";
                    List<HeadDeliveryRegisterModel> stcAndWeightList= primaryDBTemplate.query(query2,new HeadDeliveryRegisterService.StcAndWeight());
                    if(stcAndWeightList.size()<=0){
                        query2="SELECT CONCAT(Pack_Number,' ',Pack_Description) AS stc,concat(weight,' KG') as weight FROM igm_supplimentary_detail WHERE Import_Rotation_No='"+rotation+"' AND replace(BL_No,'/','')='"+blno+"'";
                        stcAndWeightList= primaryDBTemplate.query(query2,new HeadDeliveryRegisterService.StcAndWeight());
                    }
                    if(stcAndWeightList.size()>0){
                        stc=stcAndWeightList.get(0).getStc();
                        weight=stcAndWeightList.get(0).getWeight();

                    }
                    resultModel.setStc(stc);
                    resultModel.setWeight(weight);
                    query3="SELECT reg_no,reg_date FROM sad_info \n" +
                            "INNER JOIN sad_item ON sad_item.sad_id = sad_info.id\n" +
                            "WHERE manif_num = '"+manif+"' AND sum_declare = '"+blno+"' LIMIT 1";
                    List<HeadDeliveryRegisterModel> regInfoList=primaryDBTemplate.query(query3,new HeadDeliveryRegisterService.RegInfo());
                    if(regInfoList.size()>0){
                        beNo=regInfoList.get(0).getReg_no();
                        beDate=regInfoList.get(0).getReg_date();
                    }
                    resultModel.setBeNo(beNo);
                    resultModel.setBeDate(beDate);
                    query4="SELECT truck_id,actual_delv_pack,Pack_Unit FROM do_truck_details_entry\n" +
                            "INNER JOIN igm_pack_unit ON igm_pack_unit.id = do_truck_details_entry.actual_delv_unit\n" +
                            "WHERE cont_no = '"+containerNo+"'";
                    List<HeadDeliveryRegisterModel>truckInfoList=primaryDBTemplate.query(query4,new HeadDeliveryRegisterService.TruckInfo());
                    List temlist=new ArrayList();
                    for(int k=0;k<truckInfoList.size();k++){
                        HeadDeliveryRegisterModel tempTruckModel=truckInfoList.get(k);
                        temlist.add(tempTruckModel);

                    }
                    resultModel.setTruckInfo(temlist);
                    list.add(resultModel);
                }
                List<HeadDeliveryRegisterModel> newList=new ArrayList();
                String cfName="";
                String blNo="";
                for(int c=0;c<list.size();c++){

                    List<HeadDeliveryRegisterContainerNoModel> containerList=new ArrayList<>();

                    HeadDeliveryRegisterModel tmpModel=list.get(c);

                    if(!cfName.equals(tmpModel.getCf_name()) || !blNo.equals(tmpModel.getBl_no())){
                        HeadDeliveryRegisterModel newModel=new HeadDeliveryRegisterModel();
                        newModel.setCf_name(tmpModel.getCf_name());
                        newModel.setCont_no(tmpModel.getCont_no());
                        newModel.setSize(tmpModel.getSize());
                        newModel.setV_name(tmpModel.getV_name());
                        newModel.setRot_no(tmpModel.getRot_no());
                        newModel.setBl_no(tmpModel.getBl_no());
                        newModel.setContainerNoAndSize(tmpModel.getContainerNoAndSize());
                        newModel.setStc(tmpModel.getStc());
                        newModel.setWeight(tmpModel.getWeight());
                        newModel.setReg_date(tmpModel.getReg_date());
                        newModel.setReg_no(tmpModel.getReg_no());
                        newModel.setBeNo(tmpModel.getBeNo());
                        newModel.setBeDate(tmpModel.getBeDate());
                        newModel.setTruck_id(tmpModel.getTruck_id());
                        newModel.setActual_delv_pack(tmpModel.getActual_delv_pack());
                        newModel.setTruckInfo(tmpModel.getTruckInfo());
                        newModel.setBlock_No(tmpModel.getBlock_No());
                        newModel.setPack_Unit(tmpModel.getPack_Unit());
                        String cfName2=tmpModel.getCf_name();
                        String blNo2=tmpModel.getBl_no();
                        for(int a=0;a<list.size();a++){
                            HeadDeliveryRegisterModel tmpModel2=list.get(a);
                            if(cfName2.equals(tmpModel2.getCf_name()) || blNo2.equals(tmpModel2.getBl_no())){
                                HeadDeliveryRegisterContainerNoModel headDeliveryRegisterContainerNoModel=new HeadDeliveryRegisterContainerNoModel();
                                headDeliveryRegisterContainerNoModel.setCont_no(tmpModel2.getCont_no());
                                headDeliveryRegisterContainerNoModel.setContainerNoAndSize(tmpModel2.getContainerNoAndSize());
                                containerList.add(headDeliveryRegisterContainerNoModel);
                                newModel.setContainerList(containerList);
                            }
                        }
                        newList.add(newModel);
                        cfName=tmpModel.getCf_name();
                        blNo=tmpModel.getBl_no();


                    }
                }
               // mainModel.setResultInfo(list);
                 mainModel.setResultInfo(newList);
                mainList.add(mainModel);

            }

        return mainList;
    }

    public List getHeadDeliveryRegisterGcbList(String date,String terminal,String block,String assignType){
        List<HeadDeliveryRegisterGcbModel> mainGcbList=new ArrayList<>();
        String sqlQuery="";
        String query="";
        if(!assignType.equals("ALLASSIGN")){
            sqlQuery="SELECT DISTINCT mfdch_value,mfdch_desc \n" +
                    "FROM ctmsmis.tmp_vcms_assignment \n" +
                    "WHERE date(flex_date01)='"+date+"' and Block_No='"+block+"' AND mfdch_value='"+assignType+"'";

        }
        else{
            sqlQuery="SELECT DISTINCT mfdch_value,mfdch_desc \n" +
                    "FROM ctmsmis.tmp_vcms_assignment\n" +
                    "WHERE date(flex_date01)='"+date+"' and Block_No='"+block+"'";
        }
        if(block.equals("ALLBLOCK")){
            query="SELECT DISTINCT Block_No FROM ctmsmis.tmp_vcms_assignment WHERE assignmentDate = '"+date+"' AND Yard_No = 'GCB'";
        }
        else{
            query="SELECT DISTINCT Block_No FROM ctmsmis.tmp_vcms_assignment WHERE assignmentDate = '"+date+"' AND Yard_No = 'GCB' AND Block_No = '"+block+"'";

        }
        List<HeadDeliveryRegisterModel> resultList= secondaryDBTemplate.query(sqlQuery,new HeadDeliveryRegisterService.MfdchValueAndDescription());
        List<HeadDeliveryRegisterModel> blockList= secondaryDBTemplate.query(query,new HeadDeliveryRegisterService.BlockNo());
        for(int b=0; b<blockList.size();b++){
            List<HeadDeliveryRegisterCctNctModel> mainList=new ArrayList<>();
            HeadDeliveryRegisterGcbModel gcbModel =new HeadDeliveryRegisterGcbModel();
            String blockNo="";
            blockNo=blockList.get(b).getBlock_No();
            gcbModel.setBlockNo(blockNo);

            for(int i=0;i<resultList.size();i++){
                String mfdchValue="";
                String mfdchDescription="";
                List<HeadDeliveryRegisterModel> list=new ArrayList<>();
                HeadDeliveryRegisterCctNctModel mainModel=new HeadDeliveryRegisterCctNctModel();
                String query1="";
                HeadDeliveryRegisterModel tempModel=resultList.get(i);
                mfdchValue=tempModel.getMfdch_value();
                mfdchDescription=tempModel.getMfdch_desc();
                mainModel.setMfdch_desc(mfdchDescription);
                mainModel.setMfdch_value(mfdchValue);

                query1="SELECT DISTINCT cf_name,cont_no,size,v_name,rot_no,bl_no FROM ctmsmis.tmp_vcms_assignment\n" +
                        "WHERE assignmentDate = '"+date+"' AND Yard_No='GCB' AND mfdch_value='"+mfdchValue+"' and Block_No='"+block+"' ORDER BY Yard_No,mfdch_value,flex_date01,bl_no";
                List<HeadDeliveryRegisterModel> tmpVcmsList= secondaryDBTemplate.query(query1,new HeadDeliveryRegisterService.TmpVcmsAssignmentInfo());
                for(int j=0; j<tmpVcmsList.size();j++){
                    String query2="";
                    String query3="";
                    String query4="";
                    String  rotation="";
                    String  blno="";
                    String containerNo="";
                    String cfName="";
                    String vesselName="";
                    String manif="";
                    String stc="";
                    String weight="";
                    String beNo = "";
                    String beDate ="";
                    Integer size=0;
                    String containerAndSize="";
                    HeadDeliveryRegisterModel resultModel=new HeadDeliveryRegisterModel();
                    HeadDeliveryRegisterModel cfInfoModel=tmpVcmsList.get(j);

                    rotation=cfInfoModel.getRot_no();
                    blno=cfInfoModel.getBl_no();
                    containerNo=cfInfoModel.getCont_no();
                    size=cfInfoModel.getSize();
                    cfName=cfInfoModel.getCf_name();
                    vesselName=cfInfoModel.getV_name();
                    containerAndSize=containerNo+" x "+size+"'";
                    manif=rotation.replace('/',' ');



                    resultModel.setRot_no(rotation);
                    resultModel.setBl_no(blno);
                    resultModel.setCont_no(containerNo);
                    resultModel.setCf_name(cfName);
                    resultModel.setV_name(vesselName);
                    resultModel.setSize(size);
                    resultModel.setContainerNoAndSize(containerAndSize);

                    query2="SELECT CONCAT(Pack_Number,' ',Pack_Description) AS stc,concat(weight,' KG') as weight FROM igm_details WHERE Import_Rotation_No='"+rotation+"' AND replace(BL_No,'/','')='"+blno+"'";
                    List<HeadDeliveryRegisterModel> stcAndWeightList= primaryDBTemplate.query(query2,new HeadDeliveryRegisterService.StcAndWeight());
                    if(stcAndWeightList.size()<=0){
                        query2="SELECT CONCAT(Pack_Number,' ',Pack_Description) AS stc,concat(weight,' KG') as weight FROM igm_supplimentary_detail WHERE Import_Rotation_No='"+rotation+"' AND replace(BL_No,'/','')='"+blno+"'";
                        stcAndWeightList= primaryDBTemplate.query(query2,new HeadDeliveryRegisterService.StcAndWeight());
                    }
                    if(stcAndWeightList.size()>0){
                        stc=stcAndWeightList.get(0).getStc();
                        weight=stcAndWeightList.get(0).getWeight();

                    }
                    resultModel.setStc(stc);
                    resultModel.setWeight(weight);
                    query3="SELECT reg_no,reg_date FROM sad_info \n" +
                            "INNER JOIN sad_item ON sad_item.sad_id = sad_info.id\n" +
                            "WHERE manif_num = '"+manif+"' AND sum_declare = '"+blno+"' LIMIT 1";
                    List<HeadDeliveryRegisterModel> regInfoList=primaryDBTemplate.query(query3,new HeadDeliveryRegisterService.RegInfo());
                    if(regInfoList.size()>0){
                        beNo=regInfoList.get(0).getReg_no();
                        beDate=regInfoList.get(0).getReg_date();
                    }
                    resultModel.setBeNo(beNo);
                    resultModel.setBeDate(beDate);
                    query4="SELECT truck_id,actual_delv_pack,Pack_Unit FROM do_truck_details_entry\n" +
                            "INNER JOIN igm_pack_unit ON igm_pack_unit.id = do_truck_details_entry.actual_delv_unit\n" +
                            "WHERE cont_no = '"+containerNo+"'";
                    List<HeadDeliveryRegisterModel>truckInfoList=primaryDBTemplate.query(query4,new HeadDeliveryRegisterService.TruckInfo());
                    List temlist=new ArrayList();
                    for(int k=0;k<truckInfoList.size();k++){
                        HeadDeliveryRegisterModel tempTruckModel=truckInfoList.get(k);
                        temlist.add(tempTruckModel);

                    }
                    resultModel.setTruckInfo(temlist);
                    list.add(resultModel);
                }
                List<HeadDeliveryRegisterModel> newList=new ArrayList();
                String cfName="";
                String blNo="";
                for(int c=0;c<list.size();c++){

                    List<HeadDeliveryRegisterContainerNoModel> containerList=new ArrayList<>();

                    HeadDeliveryRegisterModel tmpModel=list.get(c);

                    if(!cfName.equals(tmpModel.getCf_name()) || !blNo.equals(tmpModel.getBl_no())){
                        HeadDeliveryRegisterModel newModel=new HeadDeliveryRegisterModel();
                        newModel.setCf_name(tmpModel.getCf_name());
                        newModel.setCont_no(tmpModel.getCont_no());
                        newModel.setSize(tmpModel.getSize());
                        newModel.setV_name(tmpModel.getV_name());
                        newModel.setRot_no(tmpModel.getRot_no());
                        newModel.setBl_no(tmpModel.getBl_no());
                        newModel.setContainerNoAndSize(tmpModel.getContainerNoAndSize());
                        newModel.setStc(tmpModel.getStc());
                        newModel.setWeight(tmpModel.getWeight());
                        newModel.setReg_date(tmpModel.getReg_date());
                        newModel.setReg_no(tmpModel.getReg_no());
                        newModel.setBeNo(tmpModel.getBeNo());
                        newModel.setBeDate(tmpModel.getBeDate());
                        newModel.setTruck_id(tmpModel.getTruck_id());
                        newModel.setActual_delv_pack(tmpModel.getActual_delv_pack());
                        newModel.setTruckInfo(tmpModel.getTruckInfo());
                        newModel.setBlock_No(tmpModel.getBlock_No());
                        newModel.setPack_Unit(tmpModel.getPack_Unit());
                        String cfName2=tmpModel.getCf_name();
                        String blNo2=tmpModel.getBl_no();
                        for(int a=0;a<list.size();a++){
                            HeadDeliveryRegisterModel tmpModel2=list.get(a);
                            if(cfName2.equals(tmpModel2.getCf_name()) || blNo2.equals(tmpModel2.getBl_no())){
                                HeadDeliveryRegisterContainerNoModel headDeliveryRegisterContainerNoModel=new HeadDeliveryRegisterContainerNoModel();
                                headDeliveryRegisterContainerNoModel.setCont_no(tmpModel2.getCont_no());
                                headDeliveryRegisterContainerNoModel.setContainerNoAndSize(tmpModel2.getContainerNoAndSize());
                                containerList.add(headDeliveryRegisterContainerNoModel);
                                newModel.setContainerList(containerList);
                            }
                        }
                        newList.add(newModel);
                        cfName=tmpModel.getCf_name();
                        blNo=tmpModel.getBl_no();


                    }
                }
                //mainModel.setResultInfo(list);
                mainModel.setResultInfo(newList);
                mainList.add(mainModel);

            }
            gcbModel.setGcbList(mainList);
            mainGcbList.add(gcbModel);
        }
        return mainGcbList;
    }

    class MfdchValueAndDescription implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            HeadDeliveryRegisterModel headDeliveryRegisterModel=new HeadDeliveryRegisterModel();
            headDeliveryRegisterModel.setMfdch_value(rs.getString("mfdch_value"));
            headDeliveryRegisterModel.setMfdch_desc(rs.getString("mfdch_desc"));

            return headDeliveryRegisterModel;
        }
    }
    class BlockNo implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            HeadDeliveryRegisterModel headDeliveryRegisterModel=new HeadDeliveryRegisterModel();
            headDeliveryRegisterModel.setBlock_No(rs.getString("Block_No"));


            return headDeliveryRegisterModel;
        }
    }
    class TmpVcmsAssignmentInfo implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            HeadDeliveryRegisterModel headDeliveryRegisterModel=new HeadDeliveryRegisterModel();
            headDeliveryRegisterModel.setCf_name(rs.getString("cf_name"));
            headDeliveryRegisterModel.setCont_no(rs.getString("cont_no"));
            headDeliveryRegisterModel.setSize(rs.getInt("size"));
            headDeliveryRegisterModel.setV_name(rs.getString("v_name"));
            headDeliveryRegisterModel.setRot_no(rs.getString("rot_no"));
            headDeliveryRegisterModel.setBl_no(rs.getString("bl_no"));
            return headDeliveryRegisterModel;
        }


    }
    class StcAndWeight implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            HeadDeliveryRegisterModel headDeliveryRegisterModel = new HeadDeliveryRegisterModel();
            headDeliveryRegisterModel.setStc(rs.getString("stc"));
            headDeliveryRegisterModel.setWeight(rs.getString("weight"));
            return headDeliveryRegisterModel;
        }
    }

    class RegInfo implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            HeadDeliveryRegisterModel headDeliveryRegisterModel = new HeadDeliveryRegisterModel();
            headDeliveryRegisterModel.setReg_no(rs.getString("reg_no"));
            headDeliveryRegisterModel.setReg_date(rs.getString("reg_date"));
            return headDeliveryRegisterModel;
        }
    }
    class TruckInfo implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            HeadDeliveryRegisterModel headDeliveryRegisterModel = new HeadDeliveryRegisterModel();
            headDeliveryRegisterModel.setTruck_id(rs.getString("truck_id"));
            headDeliveryRegisterModel.setActual_delv_pack(rs.getFloat("actual_delv_pack"));
            headDeliveryRegisterModel.setPack_Unit(rs.getString("Pack_Unit"));
            return headDeliveryRegisterModel;
        }
    }
}
