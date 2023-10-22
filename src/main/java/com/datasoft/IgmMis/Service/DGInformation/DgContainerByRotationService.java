package com.datasoft.IgmMis.Service.DGInformation;
import com.datasoft.IgmMis.Model.DGInformation.DgContainerByRotation;
import com.datasoft.IgmMis.Model.DGInformation.DgContainerReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class DgContainerByRotationService {

    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;




    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;

    public List DgContainerByRotation(String srotation) {

        String Query="\n" +
                "SELECT DISTINCT igms.id AS id,igm_detail_container.cont_number,igm_detail_container.cont_size,igms.mlocode,igms.IGM_id AS IGM_id,igms.Import_Rotation_No AS Import_Rotation_No,\n" +
                "cont_status,igms.imco,cont_imo,cont_un,igms.Line_No AS Line_No,\n" +
                "igms.BL_No AS BL_No,igms.Pack_Number AS Pack_Number,igms.Pack_Description AS Pack_Description,igms.Pack_Marks_Number AS Pack_Marks_Number,\n" +
                "igms.Description_of_Goods AS Description_of_Goods,igms.Date_of_Entry_of_Goods AS Date_of_Entry_of_Goods,igms.weight AS weight,\n" +
                "igms.weight_unit,igms.net_weight,igms.net_weight_unit, igms.Bill_of_Entry_No AS Bill_of_Entry_No,\n" +
                "igms.Bill_of_Entry_Date AS Bill_of_Entry_Date,igms.No_of_Pack_Delivered AS No_of_Pack_Delivered, \n" +
                "igms.No_of_Pack_Discharged AS No_of_Pack_Discharged,igms.Remarks AS Remarks,igms.AFR AS AFR,igms.ConsigneeDesc,\n" +
                "igms.NotifyDesc,igms.navy_comments,igms.Submitee_Org_Id,igms.mlocode, (SELECT Organization_Name FROM organization_profiles orgs\n" +
                "WHERE orgs.id=igms.Submitee_Org_Id) AS Organization_Name, (SELECT Vessel_Name FROM igm_masters igm_Master WHERE igm_Master.id=igms.IGM_id)\n" +
                "AS vessel_Name, imco,un,extra_remarks,navyresponse.response_details1, navyresponse.response_details2,navyresponse.secondapprovaltime,\n" +
                "navyresponse.response_details3,navyresponse.thirdapprovaltime, navyresponse.hold_application,navyresponse.hold_date,navyresponse.rejected_application,\n" +
                "navyresponse.rejected_date,  yard_lying_info.discharge_dt,yard_lying_info.location,\n" +
                "un,extra_remarks,navyresponse.response_details1, navyresponse.response_details2,navyresponse.secondapprovaltime,\n" +
                "navyresponse.response_details3,navyresponse.thirdapprovaltime, navyresponse.hold_application,navyresponse.hold_date,navyresponse.rejected_application,\n" +
                "navyresponse.rejected_date, navyresponse.final_amendment,navyresponse.navy_response_to_port,\n" +
                "igm_detail_container.Delivery_Status_date,igms.Notify_name,igm_detail_container.cont_seal_number\n" +
                "FROM  igm_detail_container \n" +
                "INNER JOIN igm_details igms ON igm_detail_container.igm_detail_id=igms.id \n" +
                "INNER JOIN yard_lying_info ON igm_detail_container.cont_number= yard_lying_info.id  AND igms.Import_Rotation_No = yard_lying_info.rotation\n" +
                "LEFT JOIN igm_navy_response navyresponse ON navyresponse.igm_details_id=igms.id\n" +
                "WHERE igms.Import_Rotation_No='"+srotation+"'\n" +
                "AND (cont_imo <> '' OR igms.imco <> '' OR igms.un <> '') \n" +
                "\n" +
                "UNION\n" +
                "\n" +
                "SELECT DISTINCT igms_sup.id AS id,igm_sup_detail_container.cont_number,igm_sup_detail_container.cont_size,igms_sup.mlocode,igms_sup.IGM_id AS IGM_id,igms_sup.Import_Rotation_No AS Import_Rotation_No,\n" +
                "cont_status,igms_sup.imco,cont_imo,cont_un,igms_sup.Line_No AS Line_No,\n" +
                "igms_sup.BL_No AS BL_No,igms_sup.Pack_Number AS Pack_Number,igms_sup.Pack_Description AS Pack_Description,igms_sup.Pack_Marks_Number AS Pack_Marks_Number,\n" +
                "igms_sup.Description_of_Goods AS Description_of_Goods,igms_sup.Date_of_Entry_of_Goods AS Date_of_Entry_of_Goods,igms_sup.weight AS weight,\n" +
                "igms_sup.weight_unit,igms_sup.net_weight,igms_sup.net_weight_unit, igms_sup.Bill_of_Entry_No AS Bill_of_Entry_No,\n" +
                "igms_sup.Bill_of_Entry_Date AS Bill_of_Entry_Date,igms_sup.No_of_Pack_Delivered AS No_of_Pack_Delivered, \n" +
                "igms_sup.No_of_Pack_Discharged AS No_of_Pack_Discharged,igms_sup.Remarks AS Remarks,igms_sup.AFR AS AFR,igms_sup.ConsigneeDesc,\n" +
                "igms_sup.NotifyDesc,igms_sup.navy_comments,igms_sup.Submitee_Org_Id,igms_sup.mlocode, (SELECT Organization_Name FROM organization_profiles orgs\n" +
                "WHERE orgs.id=igms_sup.Submitee_Org_Id) AS Organization_Name, (SELECT Vessel_Name FROM igm_masters igm_Master WHERE igm_Master.id=igms_sup.IGM_id)\n" +
                "AS vessel_Name, imco,un,extra_remarks,navyresponse.response_details1, navyresponse.response_details2,navyresponse.secondapprovaltime,\n" +
                "navyresponse.response_details3,navyresponse.thirdapprovaltime, navyresponse.hold_application,navyresponse.hold_date,navyresponse.rejected_application,\n" +
                "navyresponse.rejected_date, yard_lying_info.discharge_dt, yard_lying_info.location,\n" +
                "un,extra_remarks,navyresponse.response_details1, navyresponse.response_details2,navyresponse.secondapprovaltime,\n" +
                "navyresponse.response_details3,navyresponse.thirdapprovaltime, navyresponse.hold_application,navyresponse.hold_date,navyresponse.rejected_application,\n" +
                "navyresponse.rejected_date, navyresponse.final_amendment,navyresponse.navy_response_to_port,\n" +
                "igm_sup_detail_container.Delivery_Status_date,igms_sup.Notify_name,igm_sup_detail_container.cont_seal_number\n" +
                "FROM igm_sup_detail_container\n" +
                "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
                "INNER JOIN igm_details igms_sup ON igms_sup.id=igm_supplimentary_detail.igm_detail_id\n" +
                "INNER JOIN yard_lying_info ON igm_sup_detail_container.cont_number= yard_lying_info.id  AND igms_sup.Import_Rotation_No = yard_lying_info.rotation\n" +
                "LEFT JOIN igm_navy_response navyresponse ON navyresponse.igm_details_id=igms_sup.id \n" +
                "WHERE  igm_supplimentary_detail.Import_Rotation_No='"+srotation+"' \n" +
                "AND (cont_imo <> '' OR igms_sup.imco <> '' OR igms_sup.un <> '')\n" +
                "\n";
        System.out.println(Query);
        List<DgContainerByRotation> resultList=primaryDBTemplate.query(Query,new DgContainerByRotationReport());

        System.out.println(resultList);
        DgContainerByRotation dgContainerByRotation;
        String import_Rotation_no="";
        String cont_number="";

        for(int i=0;i<resultList.size();i++){
            dgContainerByRotation=resultList.get(i);
        }

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class DgContainerByRotationReport implements RowMapper {
        @Override
        public DgContainerByRotation mapRow(ResultSet rs, int rowNum) throws SQLException {
            String sqlQuery="";
            String cont_number="";
            String import_Rotation_no="";

            DgContainerByRotation dgContainerByRotation=new DgContainerByRotation();

            import_Rotation_no= rs.getString("Import_Rotation_No");
            dgContainerByRotation.setImport_Rotation_No(import_Rotation_no);
            dgContainerByRotation.setVessel_Name(rs.getString("vessel_name"));
            cont_number=rs.getString("cont_number");
            dgContainerByRotation.setCont_number(cont_number);
            dgContainerByRotation.setCont_size(rs.getString("cont_size"));
            dgContainerByRotation.setCont_seal_number(rs.getString("Cont_seal_number"));
            dgContainerByRotation.setCont_status(rs.getString("cont_status"));
            dgContainerByRotation.setCont_imo(rs.getString("cont_imo"));
            dgContainerByRotation.setCont_un(rs.getString("cont_un"));
            dgContainerByRotation.setDescription_of_Goods(rs.getString("Description_of_Goods"));
            dgContainerByRotation.setRemarks(rs.getString("Remarks"));


//            String Query="SELECT inv_unit_fcy_visit.time_out,sparcsn4.argo_carrier_visit.ata,r.id AS mlo, sparcsn4.argo_quay.id AS berth\n" +
//                    "FROM inv_unit \n" +
//                    "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
//                    "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.inv_unit_fcy_visit.actual_ib_cv=sparcsn4.argo_carrier_visit.gkey \n" +
//                    "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
//                    "INNER JOIN sparcsn4.ref_bizunit_scoped r ON r.gkey=inv_unit.line_op \n" +
//                    "LEFT JOIN sparcsn4.vsl_vessel_berthings ON sparcsn4.vsl_vessel_berthings.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
//                    "LEFT JOIN sparcsn4.argo_quay ON sparcsn4.argo_quay.gkey=sparcsn4.vsl_vessel_berthings.quay\n" +
//                    "WHERE inv_unit.id ='"+cont_number+"' AND vsl_vessel_visit_details.ib_vyg='"+import_Rotation_no+"'";

            String Query = "SELECT inv_unit_fcy_visit.time_out,argo_carrier_visit.ata,r.id AS mlo, argo_quay.id AS berth\n" +
                    "FROM inv_unit\n" +
                    "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                    "INNER JOIN argo_carrier_visit ON inv_unit_fcy_visit.actual_ib_cv=argo_carrier_visit.gkey\n" +
                    "INNER JOIN vsl_vessel_visit_details ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                    "INNER JOIN ref_bizunit_scoped r ON r.gkey=inv_unit.line_op\n" +
                    "LEFT JOIN vsl_vessel_berthings ON vsl_vessel_berthings.vvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                    "LEFT JOIN argo_quay ON argo_quay.gkey=vsl_vessel_berthings.quay\n" +
                    "WHERE inv_unit.id ='"+cont_number+"' AND vsl_vessel_visit_details.ib_vyg='"+import_Rotation_no+"'";


            List<DgContainerReport> resultList=OracleDbTemplate.query(Query,new DgContainer_Dalivery_Report());
            DgContainerReport dgContainerReport;
            Integer rl_no=0;
            Integer arr=0;
            for(int i=0;i<resultList.size();i++){
                dgContainerReport=resultList.get(i);
                dgContainerByRotation.setMlo(dgContainerReport.getMlo());
                dgContainerByRotation.setBerth(dgContainerReport.getBerth());
            }
            return dgContainerByRotation;
        }
    }
    class DgContainer_Dalivery_Report implements RowMapper {
        @Override
        public DgContainerReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            DgContainerReport dgContainerReport=new DgContainerReport();
            dgContainerReport.setMlo(rs.getString("mlo"));
            dgContainerReport.setBerth(rs.getString("berth"));
            return dgContainerReport;
        }
    }

}
