package com.datasoft.IgmMis.Service.IgmOperation;


import com.datasoft.IgmMis.Model.IgmOperation.TodaysEdi;
import com.datasoft.IgmMis.Model.IgmOperation.TodaysEdiDeclaration;
import com.datasoft.IgmMis.Repository.IgmOperation.EdiDeclarationRepository;
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
public class TodaysEdiDeclarationService {

    @Autowired
    private EdiDeclarationRepository todaysEdiRepository;

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;


    public List TodayEdiDeclaration(){

        String sqlQuery="";
        sqlQuery="SELECT id,file_name_edi,file_name_stow\n" +
                "FROM edi_stow_info\n" +
                "WHERE file_status='0'\n" +
                "\n";
        List resultList=primaryDBTemplate.query(sqlQuery,new EdiDeclaration());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    public List getVesselListId(){
        String sqlQuery="";
        Integer id=0;
        sqlQuery="SELECT id,file_name_edi,file_name_stow\n" +
                "FROM edi_stow_info\n" +
                "WHERE file_status='0'\n" +
                "\n";
        List<TodaysEdiDeclaration> resultList=primaryDBTemplate.query(sqlQuery,new EdiDeclaration());
        TodaysEdiDeclaration todaysEdiDeclaration=new TodaysEdiDeclaration();
        for(int i=0;i<resultList.size();i++){
            todaysEdiDeclaration=resultList.get(i);
            id=todaysEdiDeclaration.getId();
            String sqlQuery1="SELECT Import_Rotation_No,Vessel_Name,Name_of_Master,Voy_No,VoyNoExp,grt,nrt,imo,loa_cm,flag,radio_call_sign,beam_cm,\n" +
                    "Organization_Name AS agent_name\t\t\t\n" +
                    "FROM igm_masters \n" +
                    "LEFT JOIN organization_profiles ON  organization_profiles.id = igm_masters.Submitee_Org_Id\n" +
                    "WHERE igm_masters.id = (SELECT igm_masters_id FROM edi_stow_info WHERE id='"+id+"')";
            List<TodaysEdi> result=primaryDBTemplate.query(sqlQuery1,new VesselInformation());
            TodaysEdi todaysEdi=new TodaysEdi();
            for(int j=0; j<result.size();j++){
                todaysEdi=result.get(j);


                todaysEdiDeclaration.setImport_Rotation_No(todaysEdi.getImport_Rotation_No());
                todaysEdiDeclaration.setVessel_Name(todaysEdi.getVessel_Name());
                todaysEdiDeclaration.setName_of_Master(todaysEdi.getName_of_Master());
                todaysEdiDeclaration.setVoy_No(todaysEdi.getVoy_No());
                todaysEdiDeclaration.setVoyNoExp(todaysEdi.getVoyNoExp());
                todaysEdiDeclaration.setGrt(todaysEdi.getGrt());
                todaysEdiDeclaration.setNrt(todaysEdi.getNrt());
                todaysEdiDeclaration.setImo(todaysEdi.getImo());
                todaysEdiDeclaration.setFlag(todaysEdi.getFlag());
                todaysEdiDeclaration.setLoa_cm(todaysEdi.getLoa_cm());
                todaysEdiDeclaration.setRadio_call_sign(todaysEdi.getRadio_call_sign());
                todaysEdiDeclaration.setBeam_cm(todaysEdi.getBeam_cm());
                todaysEdiDeclaration.setAgent_name(todaysEdi.getAgent_name());
            }

        }


        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;

    }



    public Boolean UpdateEdiDeclarationByIdAndLogin(String log_id,Integer id){
        if(todaysEdiRepository.updateTodaysEdi(log_id,id)==1) return true;
        else return false;
    }




    public List TodayEdiDeclarationById(Integer id){
        System.out.println("id:"+id);
        String sqlQuery="SELECT Import_Rotation_No,Vessel_Name,Name_of_Master,Voy_No,VoyNoExp,grt,nrt,imo,loa_cm,flag,radio_call_sign,beam_cm,\n" +
                "Organization_Name AS agent_name\t\t\t\n" +
                "FROM igm_masters \n" +
                "LEFT JOIN organization_profiles ON  organization_profiles.id = igm_masters.Submitee_Org_Id\n" +
                "WHERE igm_masters.id = (SELECT igm_masters_id FROM edi_stow_info WHERE id='"+id+"')";
        List<TodaysEdi> result=primaryDBTemplate.query(sqlQuery,new VesselInformation());

        List listAll = (List) result.stream().collect(Collectors.toList());
        return listAll;
    }
    class EdiDeclarationById implements RowMapper {
        @Override
        public TodaysEdiDeclaration mapRow(ResultSet rs, int rowNum) throws SQLException {

            TodaysEdiDeclaration todaysEdiDeclaration =new TodaysEdiDeclaration();
            todaysEdiDeclaration.setId(rs.getInt("id"));
            todaysEdiDeclaration.setFile_name_edi(rs.getString("file_name_edi"));
            todaysEdiDeclaration.setFile_name_stow(rs.getString("file_name_stow"));
            return todaysEdiDeclaration;
        }
    }
    class UpdateInformation implements RowMapper {
        @Override
        public TodaysEdiDeclaration mapRow(ResultSet rs, int rowNum) throws SQLException {

            TodaysEdiDeclaration todaysEdiDeclaration =new TodaysEdiDeclaration();
            todaysEdiDeclaration.setId(rs.getInt("id"));

            return todaysEdiDeclaration;
        }
    }


    class EdiDeclaration implements RowMapper {
        @Override
        public TodaysEdiDeclaration mapRow(ResultSet rs, int rowNum) throws SQLException {
            List<TodaysEdi> ediDeclaration=new ArrayList<>();
            TodaysEdiDeclaration todaysEdiDeclaration =new TodaysEdiDeclaration();
            todaysEdiDeclaration.setId(rs.getInt("id"));
            Integer id=rs.getInt("id");
            System.out.println("id:"+id);
            String sqlQuery="SELECT Import_Rotation_No,Vessel_Name,Name_of_Master,Voy_No,VoyNoExp,grt,nrt,imo,loa_cm,flag,radio_call_sign,beam_cm,\n" +
                    "Organization_Name AS agent_name\t\t\t\n" +
                    "FROM igm_masters \n" +
                    "LEFT JOIN organization_profiles ON  organization_profiles.id = igm_masters.Submitee_Org_Id\n" +
                    "WHERE igm_masters.id = (SELECT igm_masters_id FROM edi_stow_info WHERE id='"+id+"')";
            List<TodaysEdi> result=primaryDBTemplate.query(sqlQuery,new VesselInformation());
            TodaysEdi todaysEdi;
            System.out.println("Query:"+sqlQuery);
            for(int i=0; i<result.size();i++){
                todaysEdi=result.get(i);
                ediDeclaration.add(todaysEdi);
            }
            todaysEdiDeclaration.setVesselInfo(ediDeclaration);
            todaysEdiDeclaration.setFile_name_edi(rs.getString("file_name_edi"));
            todaysEdiDeclaration.setFile_name_stow(rs.getString("file_name_stow"));
            return todaysEdiDeclaration;
        }
    }


    public List TodayEdiDeclarationSearchByRotation(String rotation){
        String sqlQuery="SELECT id,file_name_edi,file_name_stow\n" +
                "FROM edi_stow_info\n" +
                "WHERE file_status='0' AND file_name_edi LIKE '%"+rotation+"%'";

        List resultList=primaryDBTemplate.query(sqlQuery,new EdiDeclarationSearchByRotation());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class EdiDeclarationSearchByRotation implements RowMapper {
        @Override
        public TodaysEdiDeclaration mapRow(ResultSet rs, int rowNum) throws SQLException {

            TodaysEdiDeclaration todaysEdiDeclaration =new TodaysEdiDeclaration();
            todaysEdiDeclaration.setId(rs.getInt("id"));
            todaysEdiDeclaration.setFile_name_edi(rs.getString("file_name_edi"));
            todaysEdiDeclaration.setFile_name_stow(rs.getString("file_name_stow"));
            return todaysEdiDeclaration;
        }
    }

    class VesselInformation implements RowMapper {
        @Override
        public TodaysEdi mapRow(ResultSet rs, int rowNum) throws SQLException {
            TodaysEdi todaysEdi =new TodaysEdi();
            todaysEdi.setImport_Rotation_No(rs.getString("Import_Rotation_No"));
            todaysEdi.setVessel_Name(rs.getString("Vessel_Name"));
            todaysEdi.setName_of_Master(rs.getString("Name_of_Master"));
            todaysEdi.setVoy_No(rs.getString("Voy_No"));
            todaysEdi.setVoyNoExp(rs.getString("VoyNoExp"));
            todaysEdi.setGrt(rs.getString("grt"));
            todaysEdi.setNrt(rs.getString("nrt"));
            todaysEdi.setImo(rs.getString("imo"));
            todaysEdi.setLoa_cm(rs.getString("loa_cm"));
            todaysEdi.setFlag(rs.getString("flag"));
            todaysEdi.setRadio_call_sign(rs.getString("radio_call_sign"));
            todaysEdi.setBeam_cm(rs.getString("beam_cm"));
            todaysEdi.setAgent_name(rs.getString("agent_name"));
            return todaysEdi;
        }
    }



}
