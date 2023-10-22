package com.datasoft.IgmMis.Service.ShahinSpecialReport;

import com.datasoft.IgmMis.Model.ShahinSpecialReport.UpdateVesselForExportContainersModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class UpdateVesselForExportContainersService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List updateVesselForExportContainers(String preRotation,String newRotation,String containers){
        List<UpdateVesselForExportContainersModel> resultList=new ArrayList<>();
        UpdateVesselForExportContainersModel updateVesselForExportContainersModel=new UpdateVesselForExportContainersModel();
        String message="";
        preRotation=preRotation.trim();
        newRotation=newRotation.trim();
        containers=containers.trim();
        String containersArr[] = containers.split(",");
        List<String> containersList= Arrays.asList(containersArr);
        String conts = "";
        for(int i=0;i<containersList.size();i++){
            String container="";
            container=containersList.get(i).trim();
            if(i==containersList.size()-1){
                conts += "'"+container+"'";
            }
            else{
                conts += "'"+container+"',";
            }
        }

        String sqlPreRotationQuery="";
        String sqlNewRotationQuery="";

        List<UpdateVesselForExportContainersModel> preRotationList=new ArrayList<>();
        List<UpdateVesselForExportContainersModel> newRotationList=new ArrayList<>();
        sqlPreRotationQuery="select vvd_gkey as rtnValue from sparcsn4.vsl_vessel_visit_details where ib_vyg='"+preRotation+"'";
        sqlNewRotationQuery="select vvd_gkey as rtnValue from sparcsn4.vsl_vessel_visit_details where ib_vyg='"+newRotation+"'";
        preRotationList=secondaryDBTemplate.query(sqlPreRotationQuery,new UpdateVesselForExportContainersService.RtnValue());
        newRotationList=secondaryDBTemplate.query(sqlNewRotationQuery,new UpdateVesselForExportContainersService.RtnValue());
       Integer preVvdGky=0;
       Integer newVvdGky=0;
       Integer yes=0;
       if(preRotationList.size()>0){
           preVvdGky=preRotationList.get(0).getRtnValue();
       }
      if(newRotationList.size()>0){
          newVvdGky=newRotationList.get(0).getRtnValue();
      }

       String updateQuery="";
       updateQuery="update ctmsmis.mis_exp_unit_copy set vvd_gkey="+newVvdGky+",snx_type=1,last_update=NOW() where vvd_gkey="+preVvdGky+" and cont_id in("+conts+")";
       yes=secondaryDBTemplate.update(updateQuery);

        if(yes>0){
            message="Container(s) "+containers+" Successfully transfered from "+preRotation+" to "+newRotation+".";

        }
        else{
            message="Container(s) "+containers+" not transfered from "+preRotation+" to "+newRotation+".";

        }


        updateVesselForExportContainersModel.setMessage(message);
        resultList.add(updateVesselForExportContainersModel);
        return resultList;
    }

    class RtnValue implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            UpdateVesselForExportContainersModel updateVesselForExportContainersModel=new UpdateVesselForExportContainersModel();
            updateVesselForExportContainersModel.setRtnValue(rs.getInt("rtnValue"));

            return updateVesselForExportContainersModel;
        }
    }
}
