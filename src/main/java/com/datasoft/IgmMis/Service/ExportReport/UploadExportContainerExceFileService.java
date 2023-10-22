package com.datasoft.IgmMis.Service.ExportReport;
import com.datasoft.IgmMis.Model.ExportReport.UploadExportContainer;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.env.Environment;
import java.io.IOException;
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
public class UploadExportContainerExceFileService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;



    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;

    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;




    private final Path fileStorageLocation;

    @Autowired
    public UploadExportContainerExceFileService(Environment env) {
        this.fileStorageLocation = Paths.get(env.getProperty("app.file.upload-dir", "./uploads/exportContainer/excelfiles"))
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime=formatter.format(new Date());
        String dateTimeArr[] = dateTime.split(" ");
        String date="";
        String time="";
        String hour="";
        String minute="";
        String second="";

        date=dateTimeArr[0];
        time=dateTimeArr[1];
        String timeArr[]=time.split(":");
        hour=timeArr[0];
        minute=timeArr[1];
        second=timeArr[2];



        String fileName =
               date+"_"+hour+"-"+minute+"-"+second+"_"+file.getOriginalFilename();

        try {
            // Check if the filename contains invalid characters

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }





   public List uploadExcelFile(MultipartFile multipartFile,String updateby,String ip)  throws IOException {
       List resultList=new ArrayList();
       UploadExportContainer resultModel=new UploadExportContainer();
       String rotation="";
       Integer excelTotalRow=0;
       Integer totalResultRow=0;
       Integer i=2;
       HSSFWorkbook workbook=new HSSFWorkbook(multipartFile.getInputStream());
       HSSFSheet worksheet=workbook.getSheetAt(0);
       excelTotalRow=worksheet.getPhysicalNumberOfRows();
       rotation=worksheet.getRow(0).getCell(5).getStringCellValue();
       System.out.println(rotation);
       List<UploadExportContainer> vvdgkeyList=new ArrayList();

       String vvdGkeyQuery="";
       Integer vvdGkey=0;
       vvdGkeyQuery="SELECT vvd_gkey AS rtnValue FROM vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";
        vvdgkeyList=OracleDbTemplate.query(vvdGkeyQuery,new UploadExportContainerExceFileService.RtnValue());
        if(vvdgkeyList.size()>0){
            vvdGkey=vvdgkeyList.get(0).getRtnValue();
            while (i<excelTotalRow){
                String cellValue="";
                try{
                    HSSFRow row=worksheet.getRow(i);
                    Cell cell=row.getCell(1);
                    cellValue=cell.getStringCellValue().trim();
               }
                catch (NullPointerException e){
                }
               // HSSFRow row=worksheet.getRow(i);
               // cellValue=worksheet.getRow(i).getCell(1).getStringCellValue().trim();
                if(!cellValue.equals("")){
                    totalResultRow=totalResultRow+1;
                }
                i=i+1;
            }

            Integer row=2;
            Integer stat=0;
            Integer count_stowage=0;

            List<UploadExportContainer> validationList=new ArrayList<>();

            while(row < (totalResultRow+2)){
                UploadExportContainer validationModel=new UploadExportContainer();

                String stowage="";
                String pod="";
                String container ="";
                Integer count_container=0;
                Integer count_pod=0;
                String stowCont="";
                container=worksheet.getRow(row).getCell(1).getStringCellValue().trim();
                container=container.replaceAll("[^A-Za-z0-9]","");
                pod=worksheet.getRow(row).getCell(8).getStringCellValue();
                stowage=worksheet.getRow(row).getCell(9).getStringCellValue();
                if(stowage!=null)
                {
                    stowage =stowage.replaceAll("[^0-9]", "");

                    if(stowage.length()==5){
                        stowage = "0"+stowage;
                    }

                    else{
                        stowage=""+stowage;
                    }

                    String sqlStowChkQuery="";
                   List<UploadExportContainer> sqlStowChkQueryList=new ArrayList<>();
                    sqlStowChkQuery="SELECT COUNT(*) AS rtnValue\n" +
                            "\t\t\t\tFROM mis_exp_unit\n" +
                            "\t\t\t\tWHERE mis_exp_unit.vvd_gkey='"+vvdGkey+"' AND mis_exp_unit.stowage_pos='"+stowage+"'";
                    sqlStowChkQueryList=primaryDBTemplate.query(sqlStowChkQuery,new UploadExportContainerExceFileService.RtnValue());
                    if(sqlStowChkQueryList.size()>0){
                        count_stowage=sqlStowChkQueryList.get(0).getRtnValue();
                    }

                }
                List<UploadExportContainer> countContainerList=new ArrayList<>();
                String countContainerQuery="";
                countContainerQuery="SELECT count(id) AS rtnValue FROM inv_unit WHERE id='"+container+"'";
                countContainerList=OracleDbTemplate.query(countContainerQuery,new UploadExportContainerExceFileService.RtnValue());
                if(countContainerList.size()>0){
                    count_container=countContainerList.get(0).getRtnValue();
                }
                List<UploadExportContainer> sqlPodCheckList=new ArrayList<>();
                String sqlPodCheckQuery="";
//                sqlPodCheckQuery="SELECT count(sparcsn4.ref_routing_point.id) as rtnValue FROM sparcsn4.vsl_vessel_visit_details\n" +
//                        "\t\t\tINNER JOIN sparcsn4.argo_visit_details ON sparcsn4.argo_visit_details.gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
//                        "\t\t\tINNER JOIN sparcsn4.ref_point_calls ON sparcsn4.ref_point_calls.itin_gkey=sparcsn4.argo_visit_details.itinereray\n" +
//                        "\t\t\tINNER JOIN sparcsn4.ref_routing_point ON sparcsn4.ref_point_calls.point_gkey=sparcsn4.ref_routing_point.gkey \n" +
//                        "\t\t\tWHERE sparcsn4.vsl_vessel_visit_details.ib_vyg='"+rotation+"' AND id='"+pod+"'";

                sqlPodCheckQuery="SELECT count(ref_routing_point.id) as rtnValue FROM vsl_vessel_visit_details\n" +
                        "INNER JOIN argo_visit_details ON argo_visit_details.gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                        "INNER JOIN ref_point_calls ON ref_point_calls.itin_gkey=argo_visit_details.itinereray\n" +
                        "INNER JOIN ref_routing_point ON ref_point_calls.point_gkey=ref_routing_point.gkey \n" +
                        "WHERE vsl_vessel_visit_details.ib_vyg='"+rotation+"' AND id='"+pod+"'";
                sqlPodCheckList=secondaryDBTemplate.query(sqlPodCheckQuery,new UploadExportContainerExceFileService.RtnValue());

                if(sqlPodCheckList.size()>0){
                    count_pod=sqlPodCheckList.get(0).getRtnValue();
                }

                if(count_container==0)			//check in n4
                {
                    stat=stat+1;
                    validationModel.setField(container);
                    validationModel.setDescription("Container is not available or Wrong Container No.");
                    validationList.add(validationModel);

                }
                else if(stowage==null)			//check blank stowage
                {
                    stat=stat+1;
                    validationModel.setField(container);
                    validationModel.setDescription("Stowage position of container is blank");
                    validationList.add(validationModel);

                }

                else if(count_stowage>0){
                    List<UploadExportContainer> strStowContList=new ArrayList<>();
                  String strStowContQuery="";
                  strStowContQuery="SELECT cont_id AS rtnValue\n" +
                          "\t\t\t\tFROM mis_exp_unit\n" +
                          "\t\t\t\tWHERE mis_exp_unit.vvd_gkey='"+vvdGkey+"' AND mis_exp_unit.stowage_pos='"+stowage+"'";
                    strStowContList=primaryDBTemplate.query(strStowContQuery,new UploadExportContainerExceFileService.ContId());
                    if(strStowContList.size()>0) {
                        stowCont=strStowContList.get(0).getContId();
                        if (!(stowCont.equals(container))) {
                            stat = stat + 1;
                            validationModel.setField(container);
                            validationModel.setDescription("Stowage Position of container is duplicate");
                            validationList.add(validationModel);
                        }
                    }

                }
                else if(count_pod==0){
                    stat=stat+1;
                    validationModel.setField(pod);
                    validationModel.setDescription("Port of destination is not valid");
                    validationList.add(validationModel);
                }
                row++;
               // validationList.add(validationModel);
            }
            resultModel.setValidationTableList(validationList);
            if(stat>0){
                resultModel.setMessage("errors");

            }
            else{
                row=2;
                i=0;
                stat = 0;
                while(row < (totalResultRow+2)){
                    String container ="";
                    container=worksheet.getRow(row).getCell(1).getStringCellValue().trim();
                    container=container.replaceAll("[^A-Za-z0-9]","");
                    List<UploadExportContainer> strGkeyQueryList=new ArrayList<>();
                    String strGkeyQuery="";
                    Integer gkey=0;
                    String iso="";
                    String mlo="";
                    String cont_status="";
                    Integer weight=0;
                    String pod="";
                    String stowage="";
                    String loaded_time="";
                    String seal_no="";
                    String coming_from="";
                    String truck_no="";
                    String craine_id="";
                    String commodity="";
                    String shift="";
                   String date="";
                  //  Date date;
                    Integer count=0;
                    Integer size=0;
                    Integer height=0;
                    String isoGroup="";
                    Integer yes=0;
                    System.out.println("row"+ row);

                    strGkeyQuery="SELECT gkey as rtnValue FROM inv_unit WHERE id='"+container+"' ORDER BY gkey DESC LIMIT 1";
                    strGkeyQueryList=OracleDbTemplate.query(strGkeyQuery,new UploadExportContainerExceFileService.RtnValue());
                    if(strGkeyQueryList.size()>0) {
                        gkey = strGkeyQueryList.get(0).getRtnValue();
                        iso = worksheet.getRow(row).getCell(2).getStringCellValue().trim();
                        mlo = worksheet.getRow(row).getCell(5).getStringCellValue();
                    }
                        if(mlo==null){
                            List<UploadExportContainer> sqlMloQueryList= new ArrayList<>();
                            String sqlMloQuery="";


//                            sqlMloQuery="select sparcsn4.ref_bizunit_scoped.id as rtnValue\n" +
//                                    "\t\t\t\tfrom sparcsn4.inv_unit\n" +
//                                    "\t\t\t\tinner join sparcsn4.ref_bizunit_scoped on sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.inv_unit.line_op\n" +
//                                    "\t\t\t\twhere sparcsn4.inv_unit.id='"+container+"' and sparcsn4.inv_unit.category='IMPRT' order by sparcsn4.inv_unit.gkey desc LIMIT 1";



                                    sqlMloQuery="select ref_bizunit_scoped.id as rtnValue,inv_unit.id\n" +
                                            "from inv_unit\n" +
                                            "inner join ref_bizunit_scoped on ref_bizunit_scoped.gkey=inv_unit.line_op\n" +
                                            "where inv_unit.id='"+container+"' and inv_unit.category='IMPRT' order by inv_unit.gkey desc fetch first 1 rows only";


                            sqlMloQueryList=OracleDbTemplate.query(sqlMloQuery,new UploadExportContainerExceFileService.RtnValue());
                            if(sqlMloQueryList.size()>0){
                                mlo=sqlMloQueryList.get(0).getRtnValue()+"";
                            }

                        }
                        cont_status=worksheet.getRow(row).getCell(6).getStringCellValue();
                        weight= (int) worksheet.getRow(row).getCell(7).getNumericCellValue();
                        pod=worksheet.getRow(row).getCell(8).getStringCellValue();
                        stowage= worksheet.getRow(row).getCell(9).getStringCellValue();
                        stowage =  stowage =stowage.replaceAll("[^0-9]", "");
                        if(stowage.length()==5){
                            stowage = "0"+stowage;
                        }
                        else{
                            stowage=""+stowage;
                        }
                        loaded_time= worksheet.getRow(row).getCell(10).getStringCellValue();
                        seal_no=worksheet.getRow(row).getCell(11).getStringCellValue();
                        coming_from=worksheet.getRow(row).getCell(12).getStringCellValue();
                        truck_no=worksheet.getRow(row).getCell(13).getStringCellValue();
                        craine_id=worksheet.getRow(row).getCell(14).getStringCellValue();
                        commodity=worksheet.getRow(row).getCell(15).getStringCellValue();
                        shift=worksheet.getRow(row).getCell(16).getStringCellValue();
                        date =worksheet.getRow(row).getCell(17).getStringCellValue();
                        if(date.equals("") || date==null){
                            date="0000-00-00";
                        }
                        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        //date=formatter.format(date);

                    if(iso.equals("")){
                        List<UploadExportContainer> getIsoTypeQryList=new ArrayList<>();
                       String getIsoTypeQry="";
//                        getIsoTypeQry="select sparcsn4.ref_equip_type.id as iso\n" +
//                                "\t\t\t\tfrom ctmsmis.mis_exp_unit\n" +
//                                "\t\t\t\tleft join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
//                                "\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
//                                "\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
//                                "\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
//                                "\t\t\t\twhere mis_exp_unit.vvd_gkey='"+vvdGkey+"' and mis_exp_unit.cont_id='"+container+"'\n" +
//                                "\t\t\t\tAND mis_exp_unit.preAddStat='0' and snx_type=0 and mis_exp_unit.delete_flag='0'";


                                getIsoTypeQry="SELECT ref_equip_type.id as iso,vsl_vessel_visit_details.vvd_gkey,inv_unit.gkey\n" +
                                        "FROM inv_unit\n" +
                                        "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                                        "INNER JOIN argo_carrier_visit ON argo_carrier_visit.gkey=inv_unit_fcy_visit.actual_ob_cv\n" +
                                        "INNER JOIN argo_visit_details ON argo_visit_details.gkey=argo_carrier_visit.cvcvd_gkey \n" +
                                        "INNER JOIN vsl_vessel_visit_details ON vsl_vessel_visit_details.vvd_gkey=argo_visit_details.gkey \n" +
                                        "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
                                        "INNER JOIN ref_equip_type ON ref_equipment.eqtyp_gkey=ref_equip_type.gkey \n" +
                                        "WHERE  inv_unit.gkey='"+vvdGkey+"'";


                        getIsoTypeQryList=secondaryDBTemplate.query(getIsoTypeQry,new UploadExportContainerExceFileService.Iso());
                        if(getIsoTypeQryList.size()>0){
                            iso=getIsoTypeQryList.get(0).getIso();
                        }

                    }
                    List<UploadExportContainer> strCountQueryList=new ArrayList<>();
                   String strCountQuery="";
                    strCountQuery="SELECT COUNT(gkey) AS rtnValue FROM mis_exp_unit WHERE vvd_gkey='"+vvdGkey+"' AND cont_id='"+container+"' and snx_type=0";
                    strCountQueryList=primaryDBTemplate.query(strCountQuery,new UploadExportContainerExceFileService.RtnValue());
                    if(strCountQueryList.size()>0){
                        count=strCountQueryList.get(0).getRtnValue();
                    }
                    List<UploadExportContainer> strSizeHeightGroupQueryList=new ArrayList<>();
                    String strSizeHeightGroupQuery="";
//                    strSizeHeightGroupQuery="SELECT \n" +
//                            "\t\t\tRIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size,\n" +
//                            "\t\t\tRIGHT(sparcsn4.ref_equip_type.nominal_height,2) AS height,\n" +
//                            "\t\t\tsparcsn4.ref_equip_type.iso_group AS isogroup \n" +
//                            "\t\t\tFROM sparcsn4.ref_equip_type WHERE id='"+iso+"'";

                    strSizeHeightGroupQuery="SELECT ref_equip_type.id,\n" +
                            "SUBSTR(ref_equip_type.nominal_length,-2, LENGTH( ref_equip_type.nominal_length)) AS cont_size, SUBSTR(ref_equip_type.nominal_height, -2, LENGTH( ref_equip_type.nominal_height)) AS height,\n" +
                            "ref_equip_type.iso_group AS isogroup \n" +
                            "FROM ref_equip_type WHERE id='"+iso+"'";
                    strSizeHeightGroupQueryList=secondaryDBTemplate.query(strSizeHeightGroupQuery,new UploadExportContainerExceFileService.SizeHeightGroup());
                    if(strSizeHeightGroupQueryList.size()>0){
                        size=strSizeHeightGroupQueryList.get(0).getSize();
                        height=strSizeHeightGroupQueryList.get(0).getHeight();
                        isoGroup=strSizeHeightGroupQueryList.get(0).getIsogroup();
                    }

                    if(count>0){
                            Integer presentGky=0;
                            List<UploadExportContainer> strPgkeyQueryList=new ArrayList<>();
                            String strPgkeyQuery="";
                            String  strUpdateQuery = "";
                            String sqlUpdatelogQuery="";
                            strPgkeyQuery="SELECT gkey AS rtnValue FROM mis_exp_unit WHERE vvd_gkey='"+vvdGkey+"' AND cont_id='"+container+"' and snx_type=0";
                            strPgkeyQueryList=primaryDBTemplate.query(strPgkeyQuery,new UploadExportContainerExceFileService.RtnValue());
                            if(strPgkeyQueryList.size()>0){
                                presentGky=strPgkeyQueryList.get(0).getRtnValue();

                            }
                            if(!(presentGky.equals(gkey))){
                                strUpdateQuery="UPDATE mis_exp_unit_copy SET gkey='"+gkey+"',cont_id='"+container+"',isoType='"+iso+"',cont_size='"+size+"',cont_height='"+height+"',isoGroup='"+isoGroup+"',cont_status='"+cont_status+"',cont_mlo='"+mlo+"',vvd_gkey='"+vvdGkey+"',rotation='"+rotation+"',stowage_pos='"+stowage+"',user_id='"+updateby+"',seal_no='"+seal_no+"',goods_and_ctr_wt_kg='"+weight+"',pod='"+pod+"',truck_no='"+truck_no+"',re_status=1,craine_id='"+craine_id+"',last_update=NOW(),updated_in_n4=1,coming_from='"+coming_from+"',shift='"+shift+"',date='"+date+"' WHERE cont_id='"+container+"' AND vvd_gkey='"+vvdGkey+"' and snx_type=0";

                            }
                            else{
                                strUpdateQuery="UPDATE mis_exp_unit_copy SET gkey='"+gkey+"',cont_id='"+container+"',isoType='"+iso+"',cont_size='"+size+"',cont_height='"+height+"',isoGroup='"+isoGroup+"',cont_status='"+cont_status+"',cont_mlo='"+mlo+"',vvd_gkey='"+vvdGkey+"',rotation='"+rotation+"',stowage_pos='"+stowage+"',user_id='"+updateby+"',seal_no='"+seal_no+"',goods_and_ctr_wt_kg='"+weight+"',pod='"+pod+"',truck_no='"+truck_no+"',re_status=1,craine_id='"+craine_id+"',updated_in_n4=1,coming_from='"+coming_from+"',shift='"+shift+"',date='"+date+"' WHERE cont_id='"+container+"' AND vvd_gkey='"+vvdGkey+"' and snx_type=0";
                            }

                            //run update query here
                            yes=primaryDBTemplate.update(strUpdateQuery);
                            System.out.println("Update mis_exp_unit_copy "+yes+" container "+container);


                            sqlUpdatelogQuery="INSERT INTO mis_exp_unit_update_log_copy(cont_id,rotation,update_at,update_by,ip_address)\n" +
                                    "VALUES('"+container+"','"+rotation+"',NOW(),'"+updateby+"','"+ip+"')";

                            //run Insert query here
                            yes=primaryDBTemplate.update(sqlUpdatelogQuery);
                            System.out.println("Insertmis_exp_unit_update_log_copy "+yes);



                        }
                    else{
                       String  strInsertQuery="";
                       strInsertQuery="INSERT INTO mis_exp_unit_copy(gkey,cont_id,cont_status,cont_mlo,isoType,cont_size,cont_height,isoGroup,vvd_gkey,rotation,stowage_pos,last_update,updated_in_n4,user_id,seal_no,goods_and_ctr_wt_kg,pod,truck_no,re_status,craine_id,coming_from,shift,date) \n" +
                               "VALUES ('"+gkey+"','"+container+"','"+cont_status+"','"+mlo+"','"+iso+"','"+size+"','"+height+"','"+isoGroup+"','"+vvdGkey+"','"+rotation+"','"+stowage+"',now(),1,'"+updateby+"','"+seal_no+"','"+weight+"','"+pod+"','"+truck_no+"',1,'"+craine_id+"','"+coming_from+"','"+shift+"','"+date+"')";
                        yes=primaryDBTemplate.update(strInsertQuery);
                        System.out.println("Insertmis_exp_unit_copy "+yes);

                    }

                    if(yes>0){
                        stat = stat+1;

                    }
                    else{
                        stat = stat;

                    }

                    row=row+1;

                }
                if(stat>0){
                 resultModel.setMessage("Successful");
                }
                else{
                    resultModel.setMessage("Failed");

                }

            }



        }
        else{
            resultModel.setMessage("rotationError");
            resultModel.setDescription("Rotation "+rotation+" is not valid. Please provide correct rotation.");

        }
        resultList.add(resultModel);




      /* for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
           // Test tempStudent = new Test();
          //  XSSFRow row = worksheet.getRow(i);
          //  tempStudent.setId((int) row.getCell(0).getNumericCellValue());
          //  tempStudent.setContent(row.getCell(1).getStringCellValue());
          //  tempStudentList.add(tempStudent);
        }*/


       return resultList;
    }
    class RtnValue implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            UploadExportContainer uploadExportContainer=new UploadExportContainer();
            uploadExportContainer.setRtnValue(rs.getInt("rtnValue"));
            return uploadExportContainer;
        }
    }
    class ContId implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            UploadExportContainer uploadExportContainer=new UploadExportContainer();
            uploadExportContainer.setContId(rs.getString("rtnValue"));
            return uploadExportContainer;
        }
    }
    class Iso implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            UploadExportContainer uploadExportContainer=new UploadExportContainer();
            uploadExportContainer.setIso(rs.getString("iso"));
            return uploadExportContainer;
        }
    }

    class SizeHeightGroup implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            UploadExportContainer uploadExportContainer=new UploadExportContainer();
            uploadExportContainer.setSize(rs.getInt("cont_size"));
            uploadExportContainer.setHeight(rs.getInt("height"));
            uploadExportContainer.setIsogroup(rs.getString("isogroup"));
            return uploadExportContainer;
        }
    }
}
