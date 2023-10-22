package com.datasoft.IgmMis.Service.IgmOperation;

import com.datasoft.IgmMis.Model.IgmOperation.UploadEdiModel;
import com.datasoft.IgmMis.Model.IgmOperation.ViewIgmGeneralInfoMainModel;
import com.datasoft.IgmMis.Model.IgmOperation.ViewIgmGeneralInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ViewIgmGeneralInfoService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    private final Path fileStorageLocation;
    @Autowired
   public ViewIgmGeneralInfoService(Environment env){
        this.fileStorageLocation = Paths.get(env.getProperty("app.file.igm-operation-edi", "./uploads/edi"))
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }

    }

    public List getIgmViewList(String type,Integer limit,Integer start,Integer status){
        List<ViewIgmGeneralInfoMainModel> mainResultList=new ArrayList<>();
        ViewIgmGeneralInfoMainModel mainResultModel=new ViewIgmGeneralInfoMainModel();
        List<ViewIgmGeneralInfoModel> resultList=new ArrayList();
        List<ViewIgmGeneralInfoModel> sqlQueryList=new ArrayList<>();

        String sqlQuery="";
        List<ViewIgmGeneralInfoModel> mainList=new ArrayList();
        String limitQuery="";
        limitQuery="SELECT igm_masters.id,igm_masters.Import_Rotation_No,igm_masters.Export_Rotation_No,igm_masters.Sailed_Year,\n" +
                "igm_masters.Sailed_Date,vessels_berth_detail.ETA_Date,Actual_Berth,final_clerance_files_ref_number,igm_masters.Vessel_Id,\n" +
                "igm_masters.Vessel_Name,igm_masters.Voy_No,igm_masters.Net_Tonnage,\n" +
                "igm_masters.Name_of_Master,igm_masters.Port_Ship_ID, Port_of_Shipment,igm_masters.Port_of_Destination,igm_masters.custom_approved,\n" +
                "igm_masters.file_clearence_date,Organization_Name AS org_name,igm_masters.Submitee_Org_Type \n" +
                "AS Submitee_Org_Type,igm_masters.S_Org_License_Number AS S_Org_License_Number,igm_masters.Submission_Date \n" +
                "AS Submission_Date,igm_masters.flag AS flag,igm_masters.imo AS imo,igm_masters.line_belongs_to AS line_belongs_to\n" +
                "FROM igm_masters\n" +
                "LEFT JOIN vessels_berth_detail ON vessels_berth_detail.igm_id = igm_masters.id\n" +
                "LEFT JOIN organization_profiles ON organization_profiles.id = igm_masters.Submitee_Org_Id\n" +
                "WHERE vsl_dec_type='"+type+"'\n" +
                "ORDER BY file_clearence_date DESC\n" +
                "LIMIT "+start+","+limit;

        sqlQuery="SELECT igm_masters.id,igm_masters.Import_Rotation_No,igm_masters.Export_Rotation_No,igm_masters.Sailed_Year,\n" +
                "igm_masters.Sailed_Date,vessels_berth_detail.ETA_Date,Actual_Berth,final_clerance_files_ref_number,igm_masters.Vessel_Id,\n" +
                "igm_masters.Vessel_Name,igm_masters.Voy_No,igm_masters.Net_Tonnage,\n" +
                "igm_masters.Name_of_Master,igm_masters.Port_Ship_ID, Port_of_Shipment,igm_masters.Port_of_Destination,igm_masters.custom_approved,\n" +
                "igm_masters.file_clearence_date,Organization_Name AS org_name,igm_masters.Submitee_Org_Type \n" +
                "AS Submitee_Org_Type,igm_masters.S_Org_License_Number AS S_Org_License_Number,igm_masters.Submission_Date \n" +
                "AS Submission_Date,igm_masters.flag AS flag,igm_masters.imo AS imo,igm_masters.line_belongs_to AS line_belongs_to\n" +
                "FROM igm_masters\n" +
                "LEFT JOIN vessels_berth_detail ON vessels_berth_detail.igm_id = igm_masters.id\n" +
                "LEFT JOIN organization_profiles ON organization_profiles.id = igm_masters.Submitee_Org_Id\n" +
                "WHERE vsl_dec_type='"+type+"'\n" +
                "ORDER BY file_clearence_date DESC";
        System.out.println(limitQuery);
        sqlQueryList=primaryDBTemplate.query(sqlQuery,new ViewIgmGeneralInfoService.MainResult());
        Integer resultSize=sqlQueryList.size();

        mainList=primaryDBTemplate.query(limitQuery,new ViewIgmGeneralInfoService.MainResult());
        System.out.println("size"+mainList.size());
        for(int i=0;i<mainList.size();i++){
            ViewIgmGeneralInfoModel resultModel= new ViewIgmGeneralInfoModel();
            Integer id=0;
            String myrot="";
            id=mainList.get(i).getId();
            myrot=mainList.get(i).getImport_Rotation_No();
            String str_edi_file_name="";
            List<ViewIgmGeneralInfoModel> str_edi_file_nameList=new ArrayList<>();
            str_edi_file_name="SELECT file_name_edi,file_name_stow FROM edi_stow_info WHERE igm_masters_id='"+id+"'";
            str_edi_file_nameList=primaryDBTemplate.query(str_edi_file_name,new ViewIgmGeneralInfoService.StrEdiFileName());
            String edi_file_name = "";
            String stow_file_name = "";
            for(int j=0; j<str_edi_file_nameList.size();j++){
                edi_file_name=str_edi_file_nameList.get(j).getFile_name_edi();
                stow_file_name=str_edi_file_nameList.get(j).getFile_name_stow();

            }
          Integer nmrwFile=str_edi_file_nameList.size();
            String strChkEdi="";
            List<ViewIgmGeneralInfoModel> strChkEdiList=new ArrayList<>();
            Integer ediSt=0;
            strChkEdi="select count(*) as rtnValue from edi_stow_info where ucase(file_name_edi)=ucase(concat(replace('"+myrot+"','/','_'),'.edi'))";
            strChkEdiList=primaryDBTemplate.query(strChkEdi,new ViewIgmGeneralInfoService.RtnValue());
            if(strChkEdiList.size()>0){
                ediSt=strChkEdiList.get(0).getRtnValue();

            }
            String strCntryCode="";
            List<ViewIgmGeneralInfoModel> strCntryCodeList=new ArrayList();
            String CntryCode="";
             strCntryCode="select sparcsn4.vsl_vessels.country_code \n" +
                    "\t\t\t\tfrom sparcsn4.vsl_vessel_visit_details\n" +
                    "\t\t\t\tinner join sparcsn4.vsl_vessels on sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                    "\t\t\t\twhere ib_vyg='"+myrot+"'";
            strCntryCodeList=secondaryDBTemplate.query(strCntryCode,new ViewIgmGeneralInfoService.CountryCode());
            if(strCntryCodeList.size()>0){
                CntryCode= strCntryCodeList.get(0).getCountry_code();
            }
            String strVvd="";
            List<ViewIgmGeneralInfoModel> strVvdList=new ArrayList<>();
            Integer vvdGkey=0;
            strVvd="SELECT vvd_gkey FROM sparcsn4.vsl_vessel_visit_details WHERE ib_vyg='"+myrot+"'";
            strVvdList=secondaryDBTemplate.query(strVvd,new ViewIgmGeneralInfoService.VvdGkey());
            if(strVvdList.size()>0){
                vvdGkey= strCntryCodeList.get(0).getVvd_gkey();
            }
            Integer nmrwVvd=strVvdList.size();

            resultModel.setSubmitee_Org_Type(mainList.get(i).getSubmitee_Org_Type());
            resultModel.setOrg_name(mainList.get(i).getOrg_name());
            resultModel.setS_Org_License_Number(mainList.get(i).getS_Org_License_Number());
            resultModel.setImport_Rotation_No(mainList.get(i).getImport_Rotation_No());
            resultModel.setExport_Rotation_No(mainList.get(i).getExport_Rotation_No());
            resultModel.setSailed_Year(mainList.get(i).getSailed_Year());
            resultModel.setSailed_Date(mainList.get(i).getSailed_Date());
            resultModel.setETA_Date(mainList.get(i).getETA_Date());
            resultModel.setActual_Berth(mainList.get(i).getActual_Berth());
            resultModel.setFile_clearence_date(mainList.get(i).getFile_clearence_date());
            resultModel.setVessel_Name(mainList.get(i).getVessel_Name());
            resultModel.setVoy_No(mainList.get(i).getVoy_No());
            resultModel.setNet_Tonnage(mainList.get(i).getNet_Tonnage());
            resultModel.setName_of_Master(mainList.get(i).getName_of_Master());
            resultModel.setPort_of_Shipment(mainList.get(i).getPort_of_Shipment());
            resultModel.setPort_of_Destination(mainList.get(i).getPort_of_Destination());
            resultModel.setSubmission_Date(mainList.get(i).getSubmission_Date());
            resultModel.setNmrwVvd(nmrwVvd);
            resultModel.setVvdGkey(vvdGkey);
            resultModel.setCntryCode(CntryCode);
            resultModel.setEdiSt(ediSt);
            resultModel.setNmrwFile(nmrwFile);
            resultModel.setId(id);
            resultModel.setFile_name_edi(edi_file_name);
            resultModel.setFile_name_stow(stow_file_name);
            resultList.add(resultModel);

        }
        Integer actualSize=0;
        if((resultSize%5)==0){
            actualSize=resultSize/5;
        }
        else {
            actualSize=(resultSize/5)+1;
        }
        Integer a=0;
        Integer b=5;
        Integer c=5;
        System.out.println("page "+actualSize);
        List<ViewIgmGeneralInfoMainModel> pageList=new ArrayList<>();
        for(int k=1; k<=actualSize; k++){
            ViewIgmGeneralInfoMainModel paginationModel=new ViewIgmGeneralInfoMainModel();
            paginationModel.setPageStratLimit(a);
            paginationModel.setPageEndLimit(c);
            paginationModel.setSl(k);
            if(b<=(start+15) && start<=resultSize){
                paginationModel.setState(1);
            }
            else{
                paginationModel.setState(0);
            }

            a=b;
            b=b+5;
            pageList.add(paginationModel);
        }
        mainResultModel.setResultList(resultList);
        mainResultModel.setPagination(pageList);
        mainResultList.add(mainResultModel);
        return mainResultList;
    }
    public List searchList(String type,String searchType, String searchValue){
        List<ViewIgmGeneralInfoMainModel> mainResultList=new ArrayList<>();
        ViewIgmGeneralInfoMainModel mainResultModel=new ViewIgmGeneralInfoMainModel();
        List<ViewIgmGeneralInfoModel> resultList=new ArrayList();
        List<ViewIgmGeneralInfoModel> sqlQueryList=new ArrayList<>();

        String sqlQuery="";
        List<ViewIgmGeneralInfoModel> mainList=new ArrayList();
        if(searchType.equals("VName")){
            sqlQuery="Select igms.id,igms.Import_Rotation_No,igms.Export_Rotation_No,igms.Sailed_Year,igms.Sailed_Date,vas.ETA_Date,Actual_Berth,final_clerance_files_ref_number,igms.Vessel_Id,igms.Vessel_Name,igms.Voy_No,igms.Net_Tonnage,\n" +
                    "\t\t\t\tigms.Name_of_Master,igms.Port_of_Shipment,igms.Port_of_Destination,igms.custom_approved,igms.file_clearence_date,igms.file_clearence_logintime,(select org.Organization_Name from organization_profiles org where org.id=igms.Submitee_Org_Id) as org_name,igms.Submitee_Org_Type as Submitee_Org_Type,igms.S_Org_License_Number as S_Org_License_Number,igms.Submission_Date as Submission_Date from igm_masters igms  \n" +
                    "\t\t\t\tleft join vessels_berth_detail vas on igms.id=vas.igm_id \n" +
                    "\t\t\t\twhere igms.delivery_status=0 and igms.vsl_final_submit=1 and igms.vsl_dec_type='"+type+"' and igms.Vessel_Name Like '%"+searchValue+"%' order by 1 desc";
        }
        else if(searchType.equals("port")){
            sqlQuery="Select igms.id,igms.Import_Rotation_No,igms.Export_Rotation_No,igms.Sailed_Year,igms.Sailed_Date,\n" +
                    "\t\t\t\tvas.ETA_Date,Actual_Berth,final_clerance_files_ref_number,igms.Vessel_Id,igms.Vessel_Name,\n" +
                    "\t\t\t\tigms.Voy_No,igms.Net_Tonnage,\n" +
                    "\t\t\t\tigms.Name_of_Master,igms.Port_of_Shipment,igms.Port_of_Destination,igms.custom_approved,\n" +
                    "\t\t\t\tigms.file_clearence_date,(select org.Organization_Name from organization_profiles org \n" +
                    "\t\t\t\twhere org.id=igms.Submitee_Org_Id) as org_name,igms.Submitee_Org_Type as Submitee_Org_Type,\n" +
                    "\t\t\t\tigms.S_Org_License_Number as S_Org_License_Number,igms.Submission_Date as Submission_Date \n" +
                    "\t\t\t\tfrom igm_masters igms  \n" +
                    "\t\t\t\tleft join vessels_berth_detail vas on igms.id=vas.igm_id \n" +
                    "\t\t\t\twhere igms.delivery_status=0 and igms.vsl_dec_type='"+type+"' and igms.Port_of_Shipment \n" +
                    "\t\t\t\tLike '%"+searchValue+"%' order by 1 desc";
        }
        else if(searchType.equals("Voy")){
            sqlQuery="Select igms.id,igms.Import_Rotation_No,igms.Export_Rotation_No,igms.Sailed_Year,igms.Sailed_Date,vas.ETA_Date,Actual_Berth,final_clerance_files_ref_number,igms.Vessel_Id,igms.Vessel_Name,igms.Voy_No,igms.Net_Tonnage,\n" +
                    "\t\t\t\tigms.Name_of_Master,igms.Port_of_Shipment,igms.Port_of_Destination,igms.custom_approved,igms.file_clearence_date,igms.file_clearence_logintime,(select org.Organization_Name from organization_profiles org where org.id=igms.Submitee_Org_Id) as org_name,igms.Submitee_Org_Type as Submitee_Org_Type,igms.S_Org_License_Number as S_Org_License_Number,igms.Submission_Date as Submission_Date from igm_masters igms  \n" +
                    "\t\t\t\tleft join vessels_berth_detail vas on igms.id=vas.igm_id \n" +
                    "\t\t\t\twhere igms.delivery_status=0 and igms.vsl_final_submit=1 and igms.vsl_dec_type='"+type+"' and igms.Voy_No Like '%"+searchValue+"%' order by 1 desc";
        }
        else if(searchType.equals("Import")){
            if(type.equals("GM") ||  type.equals("TS") ){
                sqlQuery="Select igms.id,igms.Import_Rotation_No,igms.Export_Rotation_No,igms.Sailed_Year,igms.Sailed_Date,vas.ETA_Date,Actual_Berth,final_clerance_files_ref_number,igms.Vessel_Id,igms.Vessel_Name,igms.Voy_No,igms.Net_Tonnage,\n" +
                        "\t\t\t\t\tigms.Name_of_Master,igms.Port_of_Shipment,igms.Port_of_Destination,igms.custom_approved,igms.file_clearence_date,igms.file_clearence_logintime,(select org.Organization_Name from organization_profiles org where org.id=igms.Submitee_Org_Id) as org_name,igms.Submitee_Org_Type as Submitee_Org_Type,igms.S_Org_License_Number as S_Org_License_Number,igms.Submission_Date as Submission_Date from igm_masters igms  \n" +
                        "\t\t\t\t\tleft join vessels_berth_detail vas on igms.id=vas.igm_id \n" +
                        "\t\t\t\t\twhere igms.delivery_status=0 and igms.vsl_final_submit=1 and igms.vsl_dec_type='GM' and igms.Import_Rotation_No Like '"+searchValue+"' order by 1 desc";
            }
            else {
                sqlQuery="Select igms.id,igms.Import_Rotation_No,igms.Export_Rotation_No,igms.Sailed_Year,igms.Sailed_Date,vas.ETA_Date,Actual_Berth,final_clerance_files_ref_number,igms.Vessel_Id,igms.Vessel_Name,igms.Voy_No,igms.Net_Tonnage,\n" +
                        "\t\t\t\t\tigms.Name_of_Master,igms.Port_of_Shipment,igms.Port_of_Destination,igms.custom_approved,igms.file_clearence_date,igms.file_clearence_logintime,(select org.Organization_Name from organization_profiles org where org.id=igms.Submitee_Org_Id) as org_name,igms.Submitee_Org_Type as Submitee_Org_Type,igms.S_Org_License_Number as S_Org_License_Number,igms.Submission_Date as Submission_Date from igm_masters igms  \n" +
                        "\t\t\t\t\tleft join vessels_berth_detail vas on igms.id=vas.igm_id \n" +
                        "\t\t\t\t\twhere igms.delivery_status=0 and igms.vsl_final_submit=1 and igms.vsl_dec_type='"+type+"' and igms.Import_Rotation_No Like '%"+searchValue+"%' order by 1 desc";
            }

        }
        else if(searchType.equals("Export")){
            sqlQuery="Select igms.id,igms.Import_Rotation_No,igms.Export_Rotation_No,igms.Sailed_Year,igms.Sailed_Date,vas.ETA_Date,Actual_Berth,final_clerance_files_ref_number,igms.Vessel_Id,igms.Vessel_Name,igms.Voy_No,igms.Net_Tonnage,\n" +
                    "\t\t\t\tigms.Name_of_Master,igms.Port_of_Shipment,igms.Port_of_Destination,igms.custom_approved,igms.file_clearence_date,igms.file_clearence_logintime,(select org.Organization_Name from organization_profiles org where org.id=igms.Submitee_Org_Id) as org_name,igms.Submitee_Org_Type as Submitee_Org_Type,igms.S_Org_License_Number as S_Org_License_Number,igms.Submission_Date as Submission_Date from igm_masters igms  \n" +
                    "\t\t\t\tleft join vessels_berth_detail vas on igms.id=vas.igm_id \n" +
                    "\t\t\t\twhere igms.delivery_status=0 and igms.vsl_final_submit=1 and igms.vsl_dec_type='"+type+"' and igms.Export_Rotation_No Like '%"+searchValue+"%' order by 1 desc";
        }
        else if(searchType.equals("All")){
            sqlQuery="Select igms.id,igms.Import_Rotation_No,igms.Export_Rotation_No,igms.Sailed_Year,igms.Sailed_Date,vas.ETA_Date,Actual_Berth,final_clerance_files_ref_number,igms.Vessel_Id,igms.Vessel_Name,igms.Voy_No,igms.Net_Tonnage,\n" +
                    "\t\t\t\tigms.Name_of_Master,igms.Port_of_Shipment,igms.Port_of_Destination,igms.custom_approved,igms.file_clearence_date,igms.file_clearence_logintime,(select org.Organization_Name from organization_profiles org where org.id=igms.Submitee_Org_Id) as org_name,igms.Submitee_Org_Type as Submitee_Org_Type,igms.S_Org_License_Number as S_Org_License_Number,igms.Submission_Date as Submission_Date from igm_masters igms  \n" +
                    "\t\t\t\tleft join vessels_berth_detail vas on igms.id=vas.igm_id \n" +
                    "\t\t\t\twhere igms.delivery_status=0 and igms.vsl_final_submit=1 and igms.vsl_dec_type='"+type+"' order by 1 desc limit 500";
        }

        System.out.println(sqlQuery);

        mainList=primaryDBTemplate.query(sqlQuery,new ViewIgmGeneralInfoService.SearchResult());
        for(int i=0;i<mainList.size();i++){
            ViewIgmGeneralInfoModel resultModel= new ViewIgmGeneralInfoModel();
            Integer id=0;
            String myrot="";
            id=mainList.get(i).getId();
            myrot=mainList.get(i).getImport_Rotation_No();
            String str_edi_file_name="";
            List<ViewIgmGeneralInfoModel> str_edi_file_nameList=new ArrayList<>();
            str_edi_file_name="SELECT file_name_edi,file_name_stow FROM edi_stow_info WHERE igm_masters_id='"+id+"'";
            str_edi_file_nameList=primaryDBTemplate.query(str_edi_file_name,new ViewIgmGeneralInfoService.StrEdiFileName());
            String edi_file_name = "";
            String stow_file_name = "";
            for(int j=0; j<str_edi_file_nameList.size();j++){
                edi_file_name=str_edi_file_nameList.get(j).getFile_name_edi();
                stow_file_name=str_edi_file_nameList.get(j).getFile_name_stow();

            }
            Integer nmrwFile=str_edi_file_nameList.size();
            String strChkEdi="";
            List<ViewIgmGeneralInfoModel> strChkEdiList=new ArrayList<>();
            Integer ediSt=0;
            strChkEdi="select count(*) as rtnValue from edi_stow_info where ucase(file_name_edi)=ucase(concat(replace('"+myrot+"','/','_'),'.edi'))";
            strChkEdiList=primaryDBTemplate.query(strChkEdi,new ViewIgmGeneralInfoService.RtnValue());
            if(strChkEdiList.size()>0){
                ediSt=strChkEdiList.get(0).getRtnValue();

            }
            String strCntryCode="";
            List<ViewIgmGeneralInfoModel> strCntryCodeList=new ArrayList();
            String CntryCode="";
            strCntryCode="select sparcsn4.vsl_vessels.country_code \n" +
                    "\t\t\t\tfrom sparcsn4.vsl_vessel_visit_details\n" +
                    "\t\t\t\tinner join sparcsn4.vsl_vessels on sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                    "\t\t\t\twhere ib_vyg='"+myrot+"'";
            strCntryCodeList=secondaryDBTemplate.query(strCntryCode,new ViewIgmGeneralInfoService.CountryCode());
            if(strCntryCodeList.size()>0){
                CntryCode= strCntryCodeList.get(0).getCountry_code();
            }
            String strVvd="";
            List<ViewIgmGeneralInfoModel> strVvdList=new ArrayList<>();
            Integer vvdGkey=0;
            strVvd="SELECT vvd_gkey FROM sparcsn4.vsl_vessel_visit_details WHERE ib_vyg='"+myrot+"'";
            strVvdList=secondaryDBTemplate.query(strVvd,new ViewIgmGeneralInfoService.VvdGkey());
            if(strVvdList.size()>0){
                vvdGkey= strCntryCodeList.get(0).getVvd_gkey();
            }
            Integer nmrwVvd=strVvdList.size();




            resultModel.setSubmitee_Org_Type(mainList.get(i).getSubmitee_Org_Type());
            resultModel.setOrg_name(mainList.get(i).getOrg_name());
            resultModel.setS_Org_License_Number(mainList.get(i).getS_Org_License_Number());
            resultModel.setImport_Rotation_No(mainList.get(i).getImport_Rotation_No());
            resultModel.setExport_Rotation_No(mainList.get(i).getExport_Rotation_No());
            resultModel.setSailed_Year(mainList.get(i).getSailed_Year());
            resultModel.setSailed_Date(mainList.get(i).getSailed_Date());
            resultModel.setETA_Date(mainList.get(i).getETA_Date());
            resultModel.setActual_Berth(mainList.get(i).getActual_Berth());
            resultModel.setFile_clearence_date(mainList.get(i).getFile_clearence_date());
            resultModel.setVessel_Name(mainList.get(i).getVessel_Name());
            resultModel.setVoy_No(mainList.get(i).getVoy_No());
            resultModel.setNet_Tonnage(mainList.get(i).getNet_Tonnage());
            resultModel.setName_of_Master(mainList.get(i).getName_of_Master());
            resultModel.setPort_of_Shipment(mainList.get(i).getPort_of_Shipment());
            resultModel.setPort_of_Destination(mainList.get(i).getPort_of_Destination());
            resultModel.setSubmission_Date(mainList.get(i).getSubmission_Date());
            resultModel.setNmrwVvd(nmrwVvd);
            resultModel.setVvdGkey(vvdGkey);
            resultModel.setCntryCode(CntryCode);
            resultModel.setEdiSt(ediSt);
            resultModel.setNmrwFile(nmrwFile);
            resultModel.setId(id);
            resultModel.setFile_name_edi(edi_file_name);
            resultModel.setFile_name_stow(stow_file_name);
            resultList.add(resultModel);

        }

        mainResultModel.setResultList(resultList);
        mainResultList.add(mainResultModel);

        return mainResultList ;
    }

// Edi Upload Start
    public List getEdiUploadFileInfo(String id){
      List resultList=new ArrayList();
      UploadEdiModel uploadEdiModel=new UploadEdiModel();
      String sqlQuery="";
      sqlQuery="SELECT Import_Rotation_No,Voy_No,VoyNoExp,Vessel_Name,grt,nrt,imo,loa_cm,flag,radio_call_sign,beam_cm \n" +
              "FROM igm_masters WHERE id='"+id+"'";
      resultList=primaryDBTemplate.query(sqlQuery,new ViewIgmGeneralInfoService.UploadFileInfo());
      return resultList;
    }
    public String storeFile(MultipartFile file, String rotation)   {

        String orginalFileName="";
        String fileExtention="";
        orginalFileName=file.getOriginalFilename();
        String orginalFileNameArr[]=orginalFileName.split("\\.");
        fileExtention=orginalFileNameArr[1];
        rotation=rotation.replace("/","_");
        String fileName=rotation+"."+fileExtention;


        try {
            // Check if the filename contains invalid characters

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }

    }

    public List insertionOrUpdateAfterEdiUpload( String rotation,String imp_voyage,String exp_voyage,
                                                 String vslName, String imo_no, String loa,String nrt,
                                                 String grt, String flag,String call_sign,String beam,
                                                 String loginId,String ip,String filenmedi,String filenmstow){
        List<UploadEdiModel> resultList=new ArrayList();
        UploadEdiModel resultModel=new UploadEdiModel();
        Integer state=0;
        String sqlQuery="";
        List<UploadEdiModel>  sqlQueryList=new ArrayList();
        sqlQuery="SELECT id as rtnValue FROM igm_masters WHERE Import_Rotation_No='"+rotation+"'";
        sqlQueryList=primaryDBTemplate.query(sqlQuery,new ViewIgmGeneralInfoService.RtnValuForEdiUpload());
        Integer igm_masters_id=0;
        if(sqlQueryList.size()>0){
            igm_masters_id=sqlQueryList.get(0).getRtnValue();
        }
        String countQuery="";
        List<UploadEdiModel>  countQueryList=new ArrayList();
        countQuery="SELECT COUNT(igm_masters_id) AS rtnValue FROM edi_stow_info WHERE igm_masters_id='"+igm_masters_id+"'";
        countQueryList=primaryDBTemplate.query(countQuery,new ViewIgmGeneralInfoService.RtnValuForEdiUpload());
        Integer rtn_count_id=0;
        if(countQueryList.size()>0){
            rtn_count_id=countQueryList.get(0).getRtnValue();
        }
        if(rtn_count_id>0){
         String stowInfoUpdateQuery="";
            stowInfoUpdateQuery="UPDATE edi_stow_info_copy SET file_name_edi='"+filenmedi+"',file_name_stow='"+filenmstow+"',file_upload_by='"+loginId+"',file_upload_date=NOW() WHERE igm_masters_id='"+igm_masters_id+"'";
            primaryDBTemplate.update(stowInfoUpdateQuery);

        }
        else{
            String stowInfoInsertQuery="";
            stowInfoInsertQuery="INSERT INTO edi_stow_info_copy(igm_masters_id,file_name_edi,file_name_stow,file_upload_by,file_upload_date) VALUES('"+igm_masters_id+"','"+filenmedi+"','"+filenmstow+"','"+loginId+"',NOW())";
            primaryDBTemplate.update(stowInfoInsertQuery);

        }
        String igmMastersUpdateQuery="";
        igmMastersUpdateQuery="UPDATE igm_masters\n" +
                "\t\t\tSET Voy_No='"+imp_voyage+"',VoyNoExp='"+exp_voyage+"',Vessel_Name='"+vslName+"',grt='"+grt+"',nrt='"+nrt+"',imo='"+imo_no+"',loa_cm='"+loa+"',flag='"+flag+"',radio_call_sign='"+call_sign+"',beam_cm='"+beam+"' WHERE id='"+igm_masters_id+"'";
        System.out.println(igmMastersUpdateQuery);
        //  primaryDBTemplate.update(igmMastersUpdateQuery);

        state=1;
        String message="";
        if(state==1){
            message="Successful";

        }
        else{
            message="Failed";
        }
        resultModel.setMessage(message);
        resultList.add(resultModel);



        return resultList;
    }

    //End Edi Upload



    //Download Edi
    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
           throw new RuntimeException("File not found " + fileName, ex);

        }
    }


    class MainResult implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ViewIgmGeneralInfoModel viewIgmGeneralInfoModel=new ViewIgmGeneralInfoModel();
            viewIgmGeneralInfoModel.setId(rs.getInt("id"));
            viewIgmGeneralInfoModel.setImport_Rotation_No(rs.getString("Import_Rotation_No"));
            viewIgmGeneralInfoModel.setExport_Rotation_No(rs.getString("Export_Rotation_No"));
            viewIgmGeneralInfoModel.setSailed_Date(rs.getString("Sailed_Date"));
            viewIgmGeneralInfoModel.setSailed_Year(rs.getString("Sailed_Year"));
            viewIgmGeneralInfoModel.setETA_Date(rs.getString("ETA_Date"));
            viewIgmGeneralInfoModel.setActual_Berth(rs.getString("Actual_Berth"));
            viewIgmGeneralInfoModel.setFinal_clerance_files_ref_number(rs.getString("final_clerance_files_ref_number"));
            viewIgmGeneralInfoModel.setVessel_Id(rs.getString("Vessel_Id"));
            viewIgmGeneralInfoModel.setVessel_Name(rs.getString("Vessel_Name"));
            viewIgmGeneralInfoModel.setVoy_No(rs.getString("Voy_No"));
            viewIgmGeneralInfoModel.setNet_Tonnage(rs.getString("Net_Tonnage"));
            viewIgmGeneralInfoModel.setName_of_Master(rs.getString("Name_of_Master"));
            viewIgmGeneralInfoModel.setPort_Ship_ID(rs.getString("Port_Ship_ID"));
            viewIgmGeneralInfoModel.setPort_of_Shipment(rs.getString("Port_of_Shipment"));
            viewIgmGeneralInfoModel.setPort_of_Destination(rs.getString("Port_of_Destination"));
            viewIgmGeneralInfoModel.setCustom_approved(rs.getInt("custom_approved"));
            viewIgmGeneralInfoModel.setFile_clearence_date(rs.getString("file_clearence_date"));
            viewIgmGeneralInfoModel.setOrg_name(rs.getString("org_name"));
            viewIgmGeneralInfoModel.setSubmitee_Org_Type(rs.getString("Submitee_Org_Type"));
            viewIgmGeneralInfoModel.setS_Org_License_Number(rs.getString("S_Org_License_Number"));
            viewIgmGeneralInfoModel.setSubmission_Date(rs.getString("Submission_Date"));
            viewIgmGeneralInfoModel.setFlag(rs.getString("flag"));
            viewIgmGeneralInfoModel.setImo(rs.getString("imo"));
            viewIgmGeneralInfoModel.setLine_belongs_to(rs.getString("line_belongs_to"));



            return viewIgmGeneralInfoModel ;
        }
    }



    class StrEdiFileName implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ViewIgmGeneralInfoModel viewIgmGeneralInfoModel=new ViewIgmGeneralInfoModel();
            viewIgmGeneralInfoModel.setFile_name_edi(rs.getString("file_name_edi"));
            viewIgmGeneralInfoModel.setFile_name_stow(rs.getString("file_name_stow"));
            return viewIgmGeneralInfoModel;
        }
    }
    class RtnValue implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ViewIgmGeneralInfoModel viewIgmGeneralInfoModel=new ViewIgmGeneralInfoModel();
            viewIgmGeneralInfoModel.setRtnValue(rs.getInt("rtnValue"));
            return viewIgmGeneralInfoModel;
        }
    }
    class CountryCode implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ViewIgmGeneralInfoModel viewIgmGeneralInfoModel=new ViewIgmGeneralInfoModel();
            viewIgmGeneralInfoModel.setCountry_code(rs.getString("country_code"));
            return viewIgmGeneralInfoModel;
        }
    }
    class VvdGkey implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ViewIgmGeneralInfoModel viewIgmGeneralInfoModel=new ViewIgmGeneralInfoModel();
            viewIgmGeneralInfoModel.setVvd_gkey(rs.getInt("vvd_gkey"));
            return viewIgmGeneralInfoModel;
        }
    }

    class SearchResult implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ViewIgmGeneralInfoModel viewIgmGeneralInfoModel=new ViewIgmGeneralInfoModel();
            viewIgmGeneralInfoModel.setId(rs.getInt("id"));
            viewIgmGeneralInfoModel.setImport_Rotation_No(rs.getString("Import_Rotation_No"));
            viewIgmGeneralInfoModel.setExport_Rotation_No(rs.getString("Export_Rotation_No"));
            viewIgmGeneralInfoModel.setSailed_Date(rs.getString("Sailed_Date"));
            viewIgmGeneralInfoModel.setSailed_Year(rs.getString("Sailed_Year"));
            viewIgmGeneralInfoModel.setETA_Date(rs.getString("ETA_Date"));
            viewIgmGeneralInfoModel.setActual_Berth(rs.getString("Actual_Berth"));
            viewIgmGeneralInfoModel.setFinal_clerance_files_ref_number(rs.getString("final_clerance_files_ref_number"));
            viewIgmGeneralInfoModel.setVessel_Id(rs.getString("Vessel_Id"));
            viewIgmGeneralInfoModel.setVessel_Name(rs.getString("Vessel_Name"));
            viewIgmGeneralInfoModel.setVoy_No(rs.getString("Voy_No"));
            viewIgmGeneralInfoModel.setNet_Tonnage(rs.getString("Net_Tonnage"));
            viewIgmGeneralInfoModel.setName_of_Master(rs.getString("Name_of_Master"));
            viewIgmGeneralInfoModel.setPort_of_Shipment(rs.getString("Port_of_Shipment"));
            viewIgmGeneralInfoModel.setPort_of_Destination(rs.getString("Port_of_Destination"));
            viewIgmGeneralInfoModel.setCustom_approved(rs.getInt("custom_approved"));
            viewIgmGeneralInfoModel.setFile_clearence_date(rs.getString("file_clearence_date"));
            viewIgmGeneralInfoModel.setOrg_name(rs.getString("org_name"));
            viewIgmGeneralInfoModel.setSubmitee_Org_Type(rs.getString("Submitee_Org_Type"));
            viewIgmGeneralInfoModel.setS_Org_License_Number(rs.getString("S_Org_License_Number"));
            viewIgmGeneralInfoModel.setSubmission_Date(rs.getString("Submission_Date"));




            return viewIgmGeneralInfoModel ;
        }
    }

   class UploadFileInfo implements RowMapper{
       @Override
       public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
           UploadEdiModel uploadEdiModel=new UploadEdiModel();
           uploadEdiModel.setImport_Rotation_No(rs.getString("Import_Rotation_No"));
           uploadEdiModel.setVoy_No(rs.getString("Voy_No"));
           uploadEdiModel.setVessel_Name(rs.getString("Vessel_Name"));
           uploadEdiModel.setVoyNoExp(rs.getString("VoyNoExp"));
           uploadEdiModel.setGrt(rs.getString("grt"));
           uploadEdiModel.setNrt(rs.getString("nrt"));
           uploadEdiModel.setImo(rs.getString("imo"));
           uploadEdiModel.setLoa_cm(rs.getString("loa_cm"));
           uploadEdiModel.setFlag(rs.getString("flag"));
           uploadEdiModel.setRadio_call_sign(rs.getString("radio_call_sign"));
           uploadEdiModel.setBeam_cm(rs.getString("beam_cm"));
           return uploadEdiModel;
       }
   }

   class RtnValuForEdiUpload implements RowMapper{
       @Override
       public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
           UploadEdiModel uploadEdiModel=new UploadEdiModel();
           uploadEdiModel.setRtnValue(rs.getInt("rtnValue"));

           return uploadEdiModel;
       }
   }


}
