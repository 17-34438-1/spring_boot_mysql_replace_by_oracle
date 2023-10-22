package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.OffDockWiseBockedContainerListModel;
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
public class OffDockWiseBlockedContainerListService {

    private List<OffDockWiseBockedContainerListModel> offDockWiseBockedContainerResultList;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    public List getOffDockWiseBlockCotainerList(){
        String sqlQuery="";
        sqlQuery="SELECT * FROM ctmsmis.mis_block_unit \n" +
                "WHERE offdoc_code IS NOT NULL";
        List<OffDockWiseBockedContainerListModel> resultList=secondaryDBTemplate.query(sqlQuery,new OffDockWiseBlockedContainerListService.Containers());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        OffDockWiseBockedContainerListModel  offDockWiseBockedContainerListModelTemp;
        List<OffDockWiseBockedContainerListModel> list;
        offDockWiseBockedContainerResultList=new ArrayList<>();
        for(int i=0;i<resultList.size();i++){
            String query="";
            OffDockWiseBockedContainerListModel offDockWiseBockedContainerListModel=new OffDockWiseBockedContainerListModel();
            offDockWiseBockedContainerListModelTemp=resultList.get(i);
            String contId=offDockWiseBockedContainerListModelTemp.getCont_id();
            String offDockName=offDockWiseBockedContainerListModelTemp.getOffdoc_name();
            offDockWiseBockedContainerListModel.setCont_id(contId);
            offDockWiseBockedContainerListModel.setOffdoc_name(offDockName);


            query="SELECT RIGHT(nominal_length,2) AS size, RIGHT(sparcsn4.ref_equip_type.nominal_height,2)/10 AS height,\n" +
                    "\tref_bizunit_scoped.id AS mlo,ref_bizunit_scoped.name AS mlo_name, sparcsn4.vsl_vessel_visit_details.ib_vyg AS rotation, sparcsn4.vsl_vessels.name AS vsl_name \n" +
                    "\tFROM  inv_unit \n" +
                    "\tINNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=inv_unit.gkey \n" +
                    "\tINNER JOIN sparcsn4.ref_equipment ON sparcsn4.inv_unit_equip.eq_gkey=sparcsn4.ref_equipment.gkey\n" +
                    "\tINNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equipment.eqtyp_gkey=sparcsn4.ref_equip_type.gkey\n" +
                    "\tINNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                    "\tLEFT JOIN sparcsn4.ref_bizunit_scoped  ON inv_unit.line_op = ref_bizunit_scoped.gkey \n" +
                    "\tLEFT JOIN sparcsn4.argo_carrier_visit ON sparcsn4.inv_unit_fcy_visit.actual_ob_cv = sparcsn4.argo_carrier_visit.gkey\n" +
                    "\tLEFT JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                    "\tLEFT JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey \n" +
                    "\tWHERE sparcsn4.inv_unit.id='"+contId+"' ORDER BY sparcsn4.inv_unit.create_time DESC LIMIT 1";

            list=secondaryDBTemplate.query(query,new OffDockWiseBlockedContainerListService.ContainerInformation());
            for(int j=0;j<list.size();j++){
                offDockWiseBockedContainerListModelTemp=list.get(j);
                offDockWiseBockedContainerListModel.setVsl_name(offDockWiseBockedContainerListModelTemp.getVsl_name());
                offDockWiseBockedContainerListModel.setRotation(offDockWiseBockedContainerListModelTemp.getRotation());
                offDockWiseBockedContainerListModel.setSize(offDockWiseBockedContainerListModelTemp.getSize());
                offDockWiseBockedContainerListModel.setHeight(offDockWiseBockedContainerListModelTemp.getHeight());
                offDockWiseBockedContainerListModel.setHeightInString(offDockWiseBockedContainerListModelTemp.getHeightInString());
                offDockWiseBockedContainerListModel.setMlo_name(offDockWiseBockedContainerListModelTemp.getMlo_name());
            }
            query="SELECT freight_kind FROM sparcsn4.inv_unit WHERE id='"+contId+"' ORDER BY sparcsn4.inv_unit.gkey DESC LIMIT 1";
            list  =secondaryDBTemplate.query(query,new OffDockWiseBlockedContainerListService.OffDockWiseBockedContainerFreightKind());
            for(int j=0;j < list.size();j++){
                offDockWiseBockedContainerListModelTemp=list.get(j);
                offDockWiseBockedContainerListModel.setFreight_kind(offDockWiseBockedContainerListModelTemp.getFreight_kind());
            }
            query="SELECT sparcsn4.inv_unit_fcy_visit.last_pos_name\n" +
                    "FROM sparcsn4.inv_unit \n" +
                    "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                    "WHERE id='"+contId+"' ORDER BY sparcsn4.inv_unit.gkey DESC LIMIT 1";
            list  =secondaryDBTemplate.query(query,new OffDockWiseBlockedContainerListService.OffDockWiseBockedContainerLastPostionName());
            for(int j=0;j<list.size();j++){
                offDockWiseBockedContainerListModelTemp=list.get(j);
                offDockWiseBockedContainerListModel.setLast_pos_name(offDockWiseBockedContainerListModelTemp.getLast_pos_name());
            }
            query="SELECT sparcsn4.inv_unit_fcy_visit.time_out\n" +
                    "FROM sparcsn4.inv_unit \n" +
                    "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                    "WHERE id='"+contId+"' ORDER BY sparcsn4.inv_unit.gkey DESC LIMIT 1";
            list=secondaryDBTemplate.query(query,new OffDockWiseBlockedContainerListService.OffDockWiseBockedContainerTimeOut());

            for(int j=0;j<list.size();j++){
                offDockWiseBockedContainerListModelTemp=list.get(j);
                offDockWiseBockedContainerListModel.setTime_out(offDockWiseBockedContainerListModelTemp.getTime_out());
            }

            query="SELECT sparcsn4.inv_unit_fcy_visit.time_in\n" +
                    "FROM sparcsn4.inv_unit \n" +
                    "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                    "WHERE id='"+contId+"' ORDER BY sparcsn4.inv_unit.gkey ASC LIMIT 1";
            list=secondaryDBTemplate.query(query,new OffDockWiseBlockedContainerListService.OffDockWiseBockedContainerTimeIn());

            for(int j=0;j<list.size();j++){
                offDockWiseBockedContainerListModelTemp=list.get(j);
                offDockWiseBockedContainerListModel.setTime_in(offDockWiseBockedContainerListModelTemp.getTime_in());
            }
            query="SELECT \n" +
                    "TIMESTAMPDIFF(DAY,intime,timeout) AS totalDays\n" +
                    "FROM\n" +
                    "(\n" +
                    "SELECT sparcsn4.inv_unit_fcy_visit.time_in AS intime,\n" +
                    "(SELECT sparcsn4.inv_unit_fcy_visit.time_out\n" +
                    "FROM sparcsn4.inv_unit \n" +
                    "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                    "WHERE id='"+contId+"' ORDER BY sparcsn4.inv_unit.gkey DESC LIMIT 1) AS timeout\n" +
                    "FROM sparcsn4.inv_unit \n" +
                    "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                    "WHERE id='"+contId+"' ORDER BY sparcsn4.inv_unit.gkey ASC LIMIT 1\n" +
                    ") AS tmp";
            list=secondaryDBTemplate.query(query,new OffDockWiseBlockedContainerListService.OffDockWiseBockedContainertotalDays());

            for(int j=0;j<list.size();j++){
                offDockWiseBockedContainerListModelTemp=list.get(j);
                offDockWiseBockedContainerListModel.setTotalDays(offDockWiseBockedContainerListModelTemp.getTotalDays());
            }


            offDockWiseBockedContainerResultList.add(offDockWiseBockedContainerListModel);



        }
        //  System.out.println("result"+ offDockWiseBockedContainerResultList.size());
        return offDockWiseBockedContainerResultList;
    }


    class Containers implements RowMapper{
        @Override
        public OffDockWiseBockedContainerListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OffDockWiseBockedContainerListModel offDockWiseBockedContainerListModel=new OffDockWiseBockedContainerListModel();
            offDockWiseBockedContainerListModel.setCont_id(rs.getString("cont_id"));
            offDockWiseBockedContainerListModel.setOffdoc_name(rs.getString("offdoc_name"));
            return offDockWiseBockedContainerListModel;
        }
    }
    class ContainerInformation implements RowMapper{

        @Override
        public OffDockWiseBockedContainerListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OffDockWiseBockedContainerListModel offDockWiseBockedContainerListModel=new OffDockWiseBockedContainerListModel();
            offDockWiseBockedContainerListModel.setVsl_name(rs.getString("vsl_name"));
            offDockWiseBockedContainerListModel.setRotation(rs.getString("rotation"));
            offDockWiseBockedContainerListModel.setSize(rs.getInt("size"));
            offDockWiseBockedContainerListModel.setHeight(rs.getInt("height"));
            float heightInfloat=rs.getFloat("height")/10;
            String heightInString=String.format("%.02f",heightInfloat);
            offDockWiseBockedContainerListModel.setHeightInString(heightInString);
            offDockWiseBockedContainerListModel.setMlo_name(rs.getString("mlo_name"));

            return offDockWiseBockedContainerListModel;
        }

    }
    class OffDockWiseBockedContainerFreightKind implements RowMapper{

        @Override
        public OffDockWiseBockedContainerListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OffDockWiseBockedContainerListModel offDockWiseBockedContainerListModel=new OffDockWiseBockedContainerListModel();
            offDockWiseBockedContainerListModel.setFreight_kind(rs.getString("freight_kind"));

            return offDockWiseBockedContainerListModel;
        }
    }
    class OffDockWiseBockedContainerLastPostionName implements RowMapper{

        @Override
        public OffDockWiseBockedContainerListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OffDockWiseBockedContainerListModel offDockWiseBockedContainerListModel=new OffDockWiseBockedContainerListModel();
            offDockWiseBockedContainerListModel.setLast_pos_name(rs.getString("last_pos_name"));

            return offDockWiseBockedContainerListModel;
        }
    }
    class OffDockWiseBockedContainerTimeOut implements RowMapper{

        @Override
        public OffDockWiseBockedContainerListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OffDockWiseBockedContainerListModel offDockWiseBockedContainerListModel=new OffDockWiseBockedContainerListModel();
            offDockWiseBockedContainerListModel.setTime_out(rs.getString("time_out"));

            return offDockWiseBockedContainerListModel;
        }
    }

    class OffDockWiseBockedContainerTimeIn implements RowMapper{

        @Override
        public OffDockWiseBockedContainerListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OffDockWiseBockedContainerListModel offDockWiseBockedContainerListModel=new OffDockWiseBockedContainerListModel();
            offDockWiseBockedContainerListModel.setTime_in(rs.getString("time_in"));

            return offDockWiseBockedContainerListModel;
        }
    }
    class OffDockWiseBockedContainertotalDays implements RowMapper{

        @Override
        public OffDockWiseBockedContainerListModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OffDockWiseBockedContainerListModel offDockWiseBockedContainerListModel=new OffDockWiseBockedContainerListModel();
            offDockWiseBockedContainerListModel.setTotalDays(rs.getInt("totalDays"));

            return offDockWiseBockedContainerListModel;
        }
    }

}
