package com.datasoft.IgmMis.Service.Pangaon;
import com.datasoft.IgmMis.Model.Pangaon.ConvertPanGaonContainerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConvertPanGaonContainerService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    private final Path fileStorageLocation;
    //final private ChannelSftp channelSftp;


    @Autowired
    public ConvertPanGaonContainerService(Environment env){
        this.fileStorageLocation = Paths.get(env.getProperty("app.file.convert-pangaon-container", "./uploads/pangaon/xml"))
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }



    public List convertPangaonContainer(String visit) throws IOException{
        List<ConvertPanGaonContainerModel> resultList=new ArrayList<>();
        List<ConvertPanGaonContainerModel> strMainList=new ArrayList<>();
        ConvertPanGaonContainerModel resultModel=new ConvertPanGaonContainerModel();
        String file_old = "";
        String myFile_old="";
        file_old = visit;
        myFile_old="PANGOAN-"+file_old+".xml";

        String UNB = "<?xml version='1.0' encoding='UTF-8'?>\n";
        UNB = UNB+"<argo:snx xmlns:argo='http://www.navis.com/argo' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.navis.com/argo snx.xsd'>"+"\n";
        String NAD = "";
        String END = "";
        String strMainQuery="";
        Integer st = 0;
        String strCont = "";
        InputStream tmpStrem;
        strMainQuery="select * from ctmsmis.mis_pangoan_unit where visit_id='"+visit+"'";
        strMainList=secondaryDBTemplate.query(strMainQuery,new ConvertPanGaonContainerService.MainResult());
        List<ConvertPanGaonContainerModel> resultSubList=new ArrayList<>();
        for(int i=0;i<strMainList.size();i++){
            ConvertPanGaonContainerModel resultSubModel =new ConvertPanGaonContainerModel();
            String cont_id="";
            cont_id=strMainList.get(i).getCont_id();
            String strTransQuery="";
            String Trans="";
            String cat="";
            List<ConvertPanGaonContainerModel>strTransList=new ArrayList<>();
            strTransQuery="select sparcsn4.inv_unit_fcy_visit.transit_state,sparcsn4.inv_unit.category from sparcsn4.inv_unit \n" +
                    "\t\tinner join sparcsn4.inv_unit_fcy_visit on sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                    "\t\twhere sparcsn4.inv_unit.id='"+cont_id+"' order by sparcsn4.inv_unit_fcy_visit.gkey";
            strTransList=secondaryDBTemplate.query(strTransQuery,new ConvertPanGaonContainerService.CategoryAndTransitState());
            for(int j=0;j<strTransList.size();j++){
                Trans=strTransList.get(j).getTransit_state();
                cat=strTransList.get(j).getCategory();
            }
            Integer s=0;
            if(Trans.equals("S60_LOADED") || Trans.equals("S20_INBOUND") || Trans.equals("S40_YARD")){
                s = 1;
            }
            if(s==0){
                NAD=NAD+"<unit id='"+strMainList.get(i).getCont_id()+"'"+" category='"+strMainList.get(i).getCategory()+"'"+" transit-state='INBOUND'"+" visit-state='ACTIVE'"+" freight-kind='"+strMainList.get(i).getFried_kind()+"'"+" weight-kg='"+strMainList.get(i).getGross_weight()+"'"+"  line='"+strMainList.get(i).getMlo()+"'"+"  xml-status='0'>\n";
                NAD=NAD+"<equipment eqid='"+strMainList.get(i).getCont_id()+"'"+" type='"+strMainList.get(i).getIso_code()+"'"+" class='CTR'"+" life-cycle-state='ACT'"+" role='PRIMARY'></equipment>\n";
                NAD=NAD+"<routing pol='CGP' pod-1='CGP'>"+"\n";
                NAD=NAD+"<carrier direction='IB' qualifier='DECLARED' mode='VESSEL'"+" id='"+strMainList.get(i).getVisit_id()+"'"+"></carrier>"+"\n";
                NAD=NAD+"<carrier direction='IB' qualifier='ACTUAL' mode='VESSEL'"+" id='"+strMainList.get(i).getVisit_id()+"'"+"></carrier>"+"\n";
                NAD=NAD+"<carrier direction='OB' qualifier='DECLARED' mode='VESSEL' id='GEN_CARRIER'"+"></carrier>"+"\n";
                NAD=NAD+"<carrier direction='OB' qualifier='ACTUAL' mode='VESSEL' id='GEN_CARRIER'"+"></carrier>"+"\n";
                NAD=NAD+"</routing>"+"\n";
                NAD=NAD+"<contents weight-kg='"+strMainList.get(i).getGross_weight()+"'"+"/>"+"\n";
                NAD=NAD+"<unit-etc category='"+strMainList.get(i).getCategory()+"'"+" line='"+strMainList.get(i).getMlo()+"'"+"/>"+"\n";
                if(strMainList.get(i).getFried_kind().equals("FCL") || strMainList.get(i).getFried_kind().equals("LCL")){
                    NAD=NAD+"<seals seal-1='"+strMainList.get(i).getSeal()+"'"+" />"+"\n";

                }
                NAD=NAD+"</unit>"+"\n";

            }
           else if(s==1){
               String contId="";
               String category="";
               String fried_kind="";
               String mlo="";
               String iso_code="";
               contId=strMainList.get(i).getCont_id();
               category=strMainList.get(i).getCategory();
                fried_kind=strMainList.get(i).getFried_kind();
               mlo=strMainList.get(i).getMlo();
               iso_code=strMainList.get(i).getIso_code();
               strCont=strCont+contId+",";
               resultSubModel.setCont_id(contId);
               resultSubModel.setCategory(category);
               resultSubModel.setFried_kind(fried_kind);
               resultSubModel.setMlo(mlo);
               resultSubModel.setIso_code(iso_code);
               resultSubList.add(resultSubModel);
                st++;
           }

        }
        END = END+"</argo:snx>"+"\n";
        resultModel.setResultSubList(resultSubList);
        resultModel.setNAD(NAD);
        if(st>0){

        }
        else{
            resultModel.setXmlData(UNB+NAD+END);
        }

        resultModel.setFileName(myFile_old);
        resultModel.setStrCont(strCont);
        resultList.add(resultModel);

        if(st>0){

        }
        else {

            File tmpFile = File.createTempFile("test", ".tmp");
            FileWriter writer = new FileWriter(tmpFile);
            writer.write(UNB);
            writer.write(NAD);
            writer.write(END);
            writer.close();
             tmpStrem = new FileInputStream(tmpFile);
            Path targetLocation=this.fileStorageLocation.resolve(myFile_old);
            Files.copy(tmpStrem,targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }

        return resultList;

    }


    class MainResult implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertPanGaonContainerModel convertPanGaonContainerModel=new ConvertPanGaonContainerModel();
            convertPanGaonContainerModel.setAkey(rs.getInt("akey"));
            convertPanGaonContainerModel.setCont_id(rs.getString("cont_id"));
            convertPanGaonContainerModel.setIso_code(rs.getString("iso_code"));
            convertPanGaonContainerModel.setVisit_id(rs.getString("visit_id"));
            convertPanGaonContainerModel.setGross_weight(rs.getString("gross_weight"));
            convertPanGaonContainerModel.setCategory(rs.getString("category"));
            convertPanGaonContainerModel.setFried_kind(rs.getString("fried_kind"));
            convertPanGaonContainerModel.setSeal(rs.getString("seal"));
            convertPanGaonContainerModel.setLast_update(rs.getString("last_update"));
            convertPanGaonContainerModel.setUser_id(rs.getString("user_id"));
            convertPanGaonContainerModel.setIp_address(rs.getString("ip_address"));
            convertPanGaonContainerModel.setConv_st(rs.getInt("conv_st"));
            convertPanGaonContainerModel.setMlo(rs.getString("mlo"));

            return convertPanGaonContainerModel;
        }
    }
    class CategoryAndTransitState implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertPanGaonContainerModel convertPanGaonContainerModel=new ConvertPanGaonContainerModel();
            convertPanGaonContainerModel.setTransit_state(rs.getString("transit_state"));
            convertPanGaonContainerModel.setCategory(rs.getString("category"));


            return convertPanGaonContainerModel;
        }
    }

}
