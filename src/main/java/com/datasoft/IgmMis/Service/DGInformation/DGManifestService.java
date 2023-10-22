package com.datasoft.IgmMis.Service.DGInformation;
import com.datasoft.IgmMis.Model.DGInformation.DgConsignee;
import com.datasoft.IgmMis.Model.DGInformation.DgManifest;
import com.datasoft.IgmMis.Model.DGInformation.DgManifestReport;
import com.datasoft.IgmMis.Model.DGInformation.DgNotifyParty;
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
public class DGManifestService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate PrimaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    public List getDgManifest(String rotation){

        String sqlQuery="";
        sqlQuery="SELECT DISTINCT igms.id AS id,igms.mlocode,igms.IGM_id AS IGM_id,igms.Import_Rotation_No AS Import_Rotation_No,\n" +
                "cont_status,igms.imco,igms.Line_No AS Line_No,\n" +
                "igms.BL_No AS BL_No,igms.Pack_Number AS Pack_Number,igms.Pack_Description AS Pack_Description,igms.Pack_Marks_Number AS Pack_Marks_Number,\n" +
                "igms.Description_of_Goods AS Description_of_Goods,igms.Date_of_Entry_of_Goods AS Date_of_Entry_of_Goods,igms.weight AS weight,\n" +
                "igms.weight_unit,igms.net_weight,igms.net_weight_unit, igms.Bill_of_Entry_No AS Bill_of_Entry_No,\n" +
                "igms.Bill_of_Entry_Date AS Bill_of_Entry_Date,igms.No_of_Pack_Delivered AS No_of_Pack_Delivered, \n" +
                "igms.No_of_Pack_Discharged AS No_of_Pack_Discharged,igms.Remarks AS Remarks,igms.AFR AS AFR,igms.ConsigneeDesc,\n" +
                "igms.NotifyDesc,igms.navy_comments,igms.Submitee_Org_Id,igms.mlocode, (SELECT Organization_Name FROM organization_profiles orgs\n" +
                "WHERE orgs.id=igms.Submitee_Org_Id) AS Organization_Name, (SELECT Vessel_Name FROM igm_masters igm_Master WHERE igm_Master.id=igms.IGM_id)\n" +
                "AS vessel_Name, imco,un,extra_remarks,navyresponse.response_details1, navyresponse.response_details2,navyresponse.secondapprovaltime,\n" +
                "navyresponse.response_details3,navyresponse.thirdapprovaltime, navyresponse.hold_application,navyresponse.hold_date,navyresponse.rejected_application,\n" +
                "navyresponse.rejected_date, navyresponse.final_amendment\n" +
                "FROM  igm_detail_container \n" +
                "INNER JOIN igm_details igms ON igm_detail_container.igm_detail_id=igms.id \n" +
                "LEFT JOIN igm_navy_response navyresponse ON navyresponse.igm_details_id=igms.id WHERE igms.Import_Rotation_No='"+rotation+"' \n" +
                "AND (cont_imo <> '' OR igms.imco <> '' OR igms.un <> '') \n" +
                "UNION\n" +
                "SELECT DISTINCT igms_sup.id AS id,igms_sup.mlocode,igms_sup.IGM_id AS IGM_id,igms_sup.Import_Rotation_No AS Import_Rotation_No,\n" +
                "cont_status,igms_sup.imco ,igms_sup.Line_No AS Line_No,\n" +
                "igms_sup.BL_No AS BL_No,igms_sup.Pack_Number AS Pack_Number,igms_sup.Pack_Description AS Pack_Description,igms_sup.Pack_Marks_Number AS Pack_Marks_Number,\n" +
                "igms_sup.Description_of_Goods AS Description_of_Goods,igms_sup.Date_of_Entry_of_Goods AS Date_of_Entry_of_Goods,igms_sup.weight AS weight,\n" +
                "igms_sup.weight_unit,igms_sup.net_weight,igms_sup.net_weight_unit, igms_sup.Bill_of_Entry_No AS Bill_of_Entry_No,\n" +
                "igms_sup.Bill_of_Entry_Date AS Bill_of_Entry_Date,igms_sup.No_of_Pack_Delivered AS No_of_Pack_Delivered, \n" +
                "igms_sup.No_of_Pack_Discharged AS No_of_Pack_Discharged,igms_sup.Remarks AS Remarks,igms_sup.AFR AS AFR,igms_sup.ConsigneeDesc,\n" +
                "igms_sup.NotifyDesc,igms_sup.navy_comments,igms_sup.Submitee_Org_Id,igms_sup.mlocode, (SELECT Organization_Name FROM organization_profiles orgs\n" +
                "WHERE orgs.id=igms_sup.Submitee_Org_Id) AS Organization_Name, (SELECT Vessel_Name FROM igm_masters igm_Master WHERE igm_Master.id=igms_sup.IGM_id)\n" +
                "AS vessel_Name, imco,un,extra_remarks,navyresponse.response_details1, navyresponse.response_details2,navyresponse.secondapprovaltime,\n" +
                "navyresponse.response_details3,navyresponse.thirdapprovaltime, navyresponse.hold_application,navyresponse.hold_date,navyresponse.rejected_application,\n" +
                "navyresponse.rejected_date, navyresponse.final_amendment\n" +
                "FROM igm_sup_detail_container\n" +
                "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
                "INNER JOIN igm_details igms_sup ON igms_sup.id=igm_supplimentary_detail.igm_detail_id\n" +
                "LEFT JOIN igm_navy_response navyresponse ON navyresponse.igm_details_id=igms_sup.id WHERE igms_sup.Import_Rotation_No='"+rotation+"'  \n" +
                "AND (cont_imo <> '' OR igms_sup.imco <> '' OR igms_sup.un <> '');\n";
        List resultList=PrimaryDBTemplate.query(sqlQuery,new ExportContainerBlockReportList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class  ExportContainerBlockReportList implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum)throws SQLException {
            DgManifest dgManifest=new DgManifest();
            Long id=rs.getLong("id");
            dgManifest.setId(id);
            dgManifest.setOrganization_Name(rs.getString("Organization_Name"));
            dgManifest.setMlocode(rs.getString("mlocode"));
            dgManifest.setLine_No(rs.getString("line_No"));
            dgManifest.setBL_No(rs.getString("BL_No"));
            dgManifest.setPack_Number(rs.getString("Pack_Number"));
            dgManifest.setPack_Description(rs.getString("pack_Description"));
            dgManifest.setPack_Marks_Number(rs.getString("pack_Marks_Number"));
            dgManifest.setDescription_of_Goods(rs.getString("description_of_Goods"));
            dgManifest.setDate_of_Entry_of_Goods(rs.getString("Date_of_Entry_of_Goods"));
            dgManifest.setNet_weight(rs.getString("net_weight"));
            dgManifest.setWeight(rs.getString("weight"));
            dgManifest.setBill_of_Entry_No(rs.getString("Bill_of_Entry_No"));
            dgManifest.setBill_of_Entry_Date(rs.getTimestamp("Bill_of_Entry_Date"));
            dgManifest.setNo_of_Pack_Delivered(rs.getString("No_of_Pack_Delivered"));
            dgManifest.setNo_of_Pack_Discharged(rs.getString("No_of_Pack_Discharged"));
            String ConsigneeDesc=rs.getString("ConsigneeDesc");
            dgManifest.setConsigneeDesc(ConsigneeDesc);
            String NotifyDesc=rs.getString("NotifyDesc");
            dgManifest.setNotify_name(NotifyDesc);
            System.out.println("NotifyDesc:"+NotifyDesc);
            dgManifest.setRemarks(rs.getString("remarks"));
            dgManifest.setImco(rs.getString("imco"));
            dgManifest.setUn(rs.getString("un"));
            String Query="\n" +
                    "SELECT cnt.id AS id, cnt.cont_number AS cont_number, cnt.cont_size AS cont_size,cnt.cont_type AS cont_type,cnt.cont_height AS cont_height,\n" +
                    "cnt.cont_status AS cont_status,cnt.cont_weight AS cont_weight,cnt.cont_seal_number AS cont_seal_number,\n" +
                    "cnt.cont_description AS cont_description, cnt.cont_imo AS cont_imo, cnt.cont_un AS cont_un,off_dock_id,Organization_Name\n" +
                    "FROM igm_detail_container cnt INNER JOIN organization_profiles ON organization_profiles.id=cnt.off_dock_id\n" +
                    "WHERE cnt.igm_detail_id='"+id+"' AND cont_imo <> ''\n" +
                    "UNION \n" +
                    "SELECT cnt.id AS id, cnt.cont_number AS cont_number, cnt.cont_size AS cont_size,\n" +
                    "cnt.cont_type AS cont_type,cnt.cont_height AS cont_height,cnt.cont_status AS cont_status,cnt.cont_weight AS cont_weight,\n" +
                    "cnt.cont_seal_number AS cont_seal_number,cnt.cont_description AS cont_description, cnt.cont_imo AS cont_imo, cnt.cont_un AS cont_un,\n" +
                    "off_dock_id,Organization_Name FROM igm_sup_detail_container cnt \n" +
                    "INNER JOIN organization_profiles ON organization_profiles.id=cnt.off_dock_id\n" +
                    "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=cnt.igm_sup_detail_id \n" +
                    "WHERE igm_supplimentary_detail.igm_detail_id='"+id+"' AND cnt.cont_imo <> ''\n" +
                    "\n";
            List<DgManifestReport> resultList=PrimaryDBTemplate.query(Query,new DgManifest_Dalivery_Report());
            List<DgManifestReport> firstList=new ArrayList<>();
            for(int i=0;i<resultList.size();i++){
                DgManifestReport dgManifestReport;
                dgManifestReport=resultList.get(i);
                firstList.add(dgManifestReport);
            }

            dgManifest.setDgManifestReports((ArrayList<DgManifestReport>) firstList);

            String SqlQuery="SELECT cons.id,cons.igm_detail_id,cons.Consignee_ID,(SELECT org.Organization_Name FROM organization_profiles org WHERE org.id=cons.Consignee_ID) AS consignee_name,(SELECT org1.Address_1 FROM organization_profiles org1 WHERE org1.id=cons.Consignee_ID) AS Address_1,cons.ff_clearance AS ff_clearance FROM igm_detail_consigneetabs cons WHERE cons.igm_detail_id='"+id+"'\n";
            List<DgConsignee> resultListConsignee=PrimaryDBTemplate.query(SqlQuery,new DgManifest_Consignee());
            DgConsignee dgConsignee;
            for(int i=0;i<resultListConsignee.size();i++){
                dgConsignee=resultListConsignee.get(i);
                dgManifest.setConsignee_name(dgConsignee.getConsignee_name());
                dgManifest.setAddress_1(dgConsignee.getAddress_1());

            }

            String SqlQueryNotify="select notf.id,notf.igm_detail_id,notf.Notify_ID,(select org.Organization_Name from organization_profiles org where org.id=notf.Notify_ID) as notify_name,(select org1.Address_1 from organization_profiles org1 where org1.id=notf.Notify_ID) as Address_1,notf.ff_clearance as ff_clearance from igm_detail_notifytabs notf where notf.igm_detail_id='"+id+"'";

            List<DgNotifyParty> resultListNotifyParty=PrimaryDBTemplate.query(SqlQueryNotify,new DgManifest_Notify());
            DgNotifyParty dgNotifyParty;
            for(int i=0;i<resultListNotifyParty.size();i++){
                dgNotifyParty=resultListNotifyParty.get(i);
                dgManifest.setNotify_name(dgNotifyParty.getNotify_name());
                dgManifest.setAddress_1(dgNotifyParty.getAddress_1());
            }
            return dgManifest;
        }
    }


    class DgManifest_Dalivery_Report implements RowMapper {
        @Override
        public DgManifestReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            DgManifestReport dgManifestReport=new DgManifestReport();

            Long id=rs.getLong("id");
            dgManifestReport.setId(id);

            dgManifestReport.setOrganization_Name(rs.getString("Organization_Name"));
            dgManifestReport.setCont_number(rs.getString("Cont_number"));
            dgManifestReport.setCont_seal_number(rs.getString("Cont_seal_number"));
            dgManifestReport.setCont_size(rs.getString("Cont_size"));
            dgManifestReport.setCont_type(rs.getString("Cont_type"));
            dgManifestReport.setCont_height(rs.getString("Cont_height"));
            dgManifestReport.setCont_weight(rs.getString("Cont_weight"));
            dgManifestReport.setCont_status(rs.getString("Cont_status"));
            dgManifestReport.setCont_imo(rs.getString("Cont_imo"));
            dgManifestReport.setCont_un(rs.getString("Cont_un"));
            return dgManifestReport;
        }
    }

    class DgManifest_Consignee implements RowMapper {
        @Override
        public DgConsignee mapRow(ResultSet rs, int rowNum) throws SQLException {
            DgConsignee dgConsignee=new DgConsignee();
            dgConsignee.setConsignee_name(rs.getString("consignee_name"));
            dgConsignee.setAddress_1(rs.getString("Address_1"));

            return dgConsignee;
        }
    }

    class DgManifest_Notify implements RowMapper {
        @Override
        public DgNotifyParty mapRow(ResultSet rs, int rowNum) throws SQLException {
            DgNotifyParty dgNotifyParty=new DgNotifyParty();
            dgNotifyParty.setNotify_name(rs.getString("Notify_name"));
            dgNotifyParty.setAddress_1(rs.getString("Address_1"));
            return dgNotifyParty;
        }
    }


}
