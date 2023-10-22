package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ImportReport.AllAssingnmentContainerSearchModel;
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
public class AllAssingnmentContainerSearchService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    public List getContainerSearchResult(String assignDate, String containerNo){
        List<AllAssingnmentContainerSearchModel> list=new ArrayList<>();
        AllAssingnmentContainerSearchModel resultModel;
        String sqlQuery="";
        sqlQuery="select BL_No from igm_details inner join igm_detail_container on igm_detail_container.igm_detail_id=igm_details.id where cont_number='"+containerNo+"' order by igm_detail_container.id desc";

        List<AllAssingnmentContainerSearchModel> blList=primaryDBTemplate.query(sqlQuery,new AllAssingnmentContainerSearchService.BLNO());
        if(blList.size()>0){
            String blNo="";
            String query1="";
            AllAssingnmentContainerSearchModel tempBlListModel=blList.get(0);
            blNo=tempBlListModel.getBL_No();


           query1="SELECT igm_details.id,igm_detail_container.cont_gross_weight,igm_details.mlocode,LEFT(igm_details.Description_of_Goods,20) AS Description_of_Goods,cont_number,igm_details.Import_Rotation_No,igm_details.BL_No,\n" +
                   "(SELECT Vessel_Name FROM igm_masters \n" +
                   "WHERE igm_masters.id=igm_details.IGM_id) AS vsl_name,\n" +
                   "igm_details.BL_No,\n" +
                   "cont_size,cont_height,off_dock_id,\n" +
                   "(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_detail_container.off_dock_id) AS offdock_name,\n" +
                   "cont_status,cont_seal_number,cont_iso_type \n" +
                   "FROM igm_detail_container \n" +
                   "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id \n" +
                   "WHERE igm_details.BL_No='"+blNo+"'\n" +
                   "UNION\n" +
                   "SELECT igm_details.id,igm_detail_container.cont_gross_weight,igm_details.mlocode,LEFT(igm_details.Description_of_Goods,20) AS Description_of_Goods,igm_sup_detail_container.cont_number,igm_details.Import_Rotation_No,igm_details.BL_No,\n" +
                   "(SELECT Vessel_Name FROM igm_masters \n" +
                   "WHERE igm_masters.id=igm_supplimentary_detail.igm_master_id) AS vsl_name,igm_details.BL_No,igm_sup_detail_container.cont_size,igm_sup_detail_container.cont_height,igm_sup_detail_container.off_dock_id,\n" +
                   "(SELECT Organization_Name \n" +
                   "FROM organization_profiles WHERE organization_profiles.id=igm_sup_detail_container.off_dock_id) AS offdock_name,igm_sup_detail_container.cont_status,igm_sup_detail_container.cont_seal_number,igm_sup_detail_container.cont_iso_type \n" +
                   "FROM igm_sup_detail_container \n" +
                   "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
                   "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id\n" +
                   "INNER JOIN igm_detail_container ON igm_detail_container.igm_detail_id=igm_details.id\n" +
                   "WHERE igm_supplimentary_detail.BL_No='"+blNo+"'";
            List<AllAssingnmentContainerSearchModel> returnContainerList=primaryDBTemplate.query(query1,new AllAssingnmentContainerSearchService.ReturnContainerList());
            for(int i=0;i<returnContainerList.size(); i++){
                String query2="";
                String query3="";
               resultModel=returnContainerList.get(i);
               AllAssingnmentContainerSearchModel tempContainerListModel=returnContainerList.get(i);
               Integer id=tempContainerListModel.getId();
               String contNumber=tempContainerListModel.getCont_number();
               resultModel.setCont_number(contNumber);
               resultModel.setImport_Rotation_No(tempContainerListModel.getImport_Rotation_No());
               resultModel.setCont_iso_type(tempContainerListModel.getCont_iso_type());
               resultModel.setMlocode(tempContainerListModel.getMlocode());
               resultModel.setCont_status(tempContainerListModel.getCont_status());
               resultModel.setDescription_of_Goods(tempContainerListModel.getDescription_of_Goods());
               resultModel.setBL_No(tempContainerListModel.getBL_No());
               resultModel.setCont_gross_weight(tempContainerListModel.getCont_gross_weight());

                query2="SELECT  GROUP_CONCAT(igm_supplimentary_detail.BL_No) AS sub_bl FROM igm_supplimentary_detail WHERE igm_detail_id='"+id+"'";
                List<AllAssingnmentContainerSearchModel> subBLList=primaryDBTemplate.query(query2,new AllAssingnmentContainerSearchService.SubBL());
                if(subBLList.size()>0){
                    AllAssingnmentContainerSearchModel subBlModel= subBLList.get(0);
                    resultModel.setSub_bl(subBlModel.getSub_bl());
                }
                query3="SELECT LEFT(k.name,20) AS cf,\n" +
                        "IFNULL((SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                        "FROM sparcsn4.srv_event\n" +
                        "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                        "WHERE sparcsn4.srv_event.applied_to_gkey=a.gkey  AND sparcsn4.srv_event.event_type_gkey IN(18,13) AND sparcsn4.srv_event_field_changes.new_value IS NOT NULL AND sparcsn4.srv_event_field_changes.new_value !='' AND sparcsn4.srv_event_field_changes.new_value !='Y-CGP-.' AND sparcsn4.srv_event.gkey<(SELECT sparcsn4.srv_event.gkey FROM sparcsn4.srv_event\n" +
                        "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                        "WHERE sparcsn4.srv_event.event_type_gkey=4 AND sparcsn4.srv_event.applied_to_gkey=a.gkey AND metafield_id='unitFlexString01' AND new_value IS NOT NULL ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1) ORDER BY sparcsn4.srv_event.gkey DESC LIMIT 1),(SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7)\n" +
                        "FROM sparcsn4.srv_event\n" +
                        "INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
                        "WHERE sparcsn4.srv_event.applied_to_gkey=a.gkey  AND sparcsn4.srv_event.event_type_gkey=18 ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1)) AS slot,\n" +
                        "DATE(b.flex_date01) AS assignDate,config_metafield_lov.mfdch_desc\n" +
                        "FROM sparcsn4.inv_unit a\n" +
                        "INNER JOIN sparcsn4.inv_unit_fcy_visit b ON b.unit_gkey=a.gkey\n" +
                        "INNER JOIN sparcsn4.ref_bizunit_scoped g ON a.line_op = g.gkey\n" +
                        "INNER JOIN sparcsn4.config_metafield_lov ON a.flex_string01=config_metafield_lov.mfdch_value\n" +
                        "INNER JOIN sparcsn4.inv_goods j ON j.gkey = a.goods\n" +
                        "LEFT JOIN sparcsn4.ref_bizunit_scoped k ON k.gkey = j.consignee_bzu\n" +
                        "WHERE a.id='"+contNumber+"' AND a.category='IMPRT' AND a.freight_kind='FCL' ORDER BY a.gkey DESC LIMIT 1";

                List<AllAssingnmentContainerSearchModel> cfInfoList=secondaryDBTemplate.query(query3,new AllAssingnmentContainerSearchService.CfInfo());
                if(cfInfoList.size()>0){
                    AllAssingnmentContainerSearchModel tempCfInfoModel=cfInfoList.get(0);
                    resultModel.setCf(tempCfInfoModel.getCf());
                    resultModel.setSlot(tempCfInfoModel.getSlot());
                    resultModel.setMfdch_desc(tempCfInfoModel.getMfdch_desc());
                    resultModel.setAssignDate(tempCfInfoModel.getAssignDate());
                }
              list.add(resultModel);

            }

        }
        return list;

    }
    class BLNO implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            AllAssingnmentContainerSearchModel allAssingnmentContainerSearchModel=new AllAssingnmentContainerSearchModel();
            allAssingnmentContainerSearchModel.setBL_No(rs.getString("BL_No"));

            return allAssingnmentContainerSearchModel;
        }
    }

    class ReturnContainerList implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            AllAssingnmentContainerSearchModel allAssingnmentContainerSearchModel=new AllAssingnmentContainerSearchModel();
            allAssingnmentContainerSearchModel.setId(rs.getInt("id"));
            allAssingnmentContainerSearchModel.setCont_gross_weight(rs.getFloat("cont_gross_weight"));
            allAssingnmentContainerSearchModel.setMlocode(rs.getString("mlocode"));
            allAssingnmentContainerSearchModel.setDescription_of_Goods(rs.getString("Description_of_Goods"));
            allAssingnmentContainerSearchModel.setBL_No(rs.getString("BL_No"));
            allAssingnmentContainerSearchModel.setCont_number(rs.getString("cont_number"));
            allAssingnmentContainerSearchModel.setImport_Rotation_No(rs.getString("Import_Rotation_No"));
            allAssingnmentContainerSearchModel.setVsl_name(rs.getString("vsl_name"));
            allAssingnmentContainerSearchModel.setCont_size(rs.getInt("cont_size"));
            allAssingnmentContainerSearchModel.setCont_height(rs.getFloat("cont_height"));
            allAssingnmentContainerSearchModel.setOff_dock_id(rs.getInt("off_dock_id"));
            allAssingnmentContainerSearchModel.setOffdock_name(rs.getString("offdock_name"));
            allAssingnmentContainerSearchModel.setCont_status(rs.getString("cont_status"));
            allAssingnmentContainerSearchModel.setCont_seal_number(rs.getString("cont_seal_number"));
            allAssingnmentContainerSearchModel.setCont_iso_type(rs.getString("cont_iso_type"));
            return allAssingnmentContainerSearchModel;
        }
    }
    class SubBL implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            AllAssingnmentContainerSearchModel allAssingnmentContainerSearchModel=new AllAssingnmentContainerSearchModel();
            allAssingnmentContainerSearchModel.setSub_bl(rs.getString("sub_bl"));
            return allAssingnmentContainerSearchModel;
        }
    }

    class CfInfo implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            AllAssingnmentContainerSearchModel allAssingnmentContainerSearchModel=new AllAssingnmentContainerSearchModel();
            allAssingnmentContainerSearchModel.setCf(rs.getString("cf"));
            allAssingnmentContainerSearchModel.setAssignDate(rs.getString("assignDate"));
            allAssingnmentContainerSearchModel.setMfdch_desc(rs.getString("mfdch_desc"));
            allAssingnmentContainerSearchModel.setSlot(rs.getString("slot"));
            return allAssingnmentContainerSearchModel;
        }
    }
}
