package com.datasoft.IgmMis.Service.Pangaon;

import com.datasoft.IgmMis.Model.Pangaon.UploadExcelFileForPangaonModel;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
public class UploadExcelFileForPangaonService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    private final Path fileStorageLocation;
    @Autowired
    public UploadExcelFileForPangaonService(Environment env) {
        this.fileStorageLocation = Paths.get(env.getProperty("app.file.pangaon-excelfile", "./uploads/pangaon/excelfiles"))
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
    public List uploadExcelFile(MultipartFile multipartFile, String updateby, String ip)  throws IOException {
        List resultList=new ArrayList();
        UploadExcelFileForPangaonModel resultModel=new UploadExcelFileForPangaonModel();
        Integer excelTotalRow=0;
        Integer totalResultRow=0;
        Integer i=1;
        HSSFWorkbook workbook=new HSSFWorkbook(multipartFile.getInputStream());
        HSSFSheet worksheet=workbook.getSheetAt(0);
        excelTotalRow=worksheet.getPhysicalNumberOfRows();
        System.out.println(excelTotalRow);

        while (i<excelTotalRow){
            String cellValue="";
            try{
                HSSFRow row=worksheet.getRow(i);
                Cell cell=row.getCell(2);
                cellValue=cell.getStringCellValue().trim();

            }catch (NullPointerException e){

            }
           // HSSFRow row=worksheet.getRow(i);
         //   Cell cell=row.getCell(2);

         /*   if(worksheet.getRow(i).getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue()!=null){
                cellValue=worksheet.getRow(i).getCell(2).getStringCellValue();
            }*/


           // cellValue=worksheet.getRow(i).getCell(2).getStringCellValue();
            System.out.println(cellValue+" i: "+ i);
            if(!cellValue.equals("")){
                totalResultRow=totalResultRow+1;


            }
            i=i+1;
        }
        System.out.println("totalRow :"+totalResultRow);

        Integer row=1;
        Integer stat=0;
        i=0;
        while(row < (totalResultRow+1)){
            Integer yes=0;
            String mlo="";
            String cont_no="";
            String visit="";
            Integer weight=0;
            String category="";
            String status="";
            String seal="";
            mlo=worksheet.getRow(row).getCell(1).getStringCellValue();
            cont_no=worksheet.getRow(row).getCell(2).getStringCellValue();
            visit=worksheet.getRow(row).getCell(6).getStringCellValue();
            weight=(int) worksheet.getRow(row).getCell(7).getNumericCellValue();
            category=worksheet.getRow(row).getCell(8).getStringCellValue();
            status=worksheet.getRow(row).getCell(9).getStringCellValue();
            seal=worksheet.getRow(row).getCell(10).getStringCellValue();
            cont_no=cont_no.replaceAll("[^A-Za-z0-9]","").trim();
            System.out.println("cont_no "+cont_no);
            System.out.println("row "+row);

            String sqlIso="";
            List<UploadExcelFileForPangaonModel> isoList=new ArrayList();
            sqlIso="SELECT sparcsn4.ref_equip_type.id AS rtnValue FROM sparcsn4.inv_unit\n" +
                    "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                    "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                    "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                    "WHERE sparcsn4.inv_unit.id='APZU3267392' LIMIT 1";
            String isoCode="";
            isoList=secondaryDBTemplate.query(sqlIso,new UploadExcelFileForPangaonService.Iso());
            if(isoList.size()>0){
                isoCode=isoList.get(0).getRtnValue();
            }

            String strQuery="";
            strQuery="replace into ctmsmis.mis_pangoan_unit_copy(cont_id,mlo,iso_code,visit_id,gross_weight,category,fried_kind,seal,last_update,user_id,ip_address) \n" +
                    "\t\t\tvalues('"+cont_no+"','"+mlo+"','"+isoCode+"','"+visit+"','"+weight+"','"+category+"','"+status+"','"+seal+"',now(),'"+updateby+"','"+ip+"')";
            yes=secondaryDBTemplate.update(strQuery);


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
        resultList.add(resultModel);

        return resultList;
    }

    class Iso implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            UploadExcelFileForPangaonModel uploadExcelFileForPangaonModel=new UploadExcelFileForPangaonModel();
            uploadExcelFileForPangaonModel.setRtnValue(rs.getString("rtnValue"));
            return uploadExcelFileForPangaonModel;
        }
    }
}
