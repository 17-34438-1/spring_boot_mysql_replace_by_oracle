package com.datasoft.IgmMis.Service.DGInformation;


import com.datasoft.IgmMis.Model.DGInformation.DgContainerDeliveryReport;
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
public class DgContainerDeliveryReportService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate PrimaryDBTemplate;


    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;

    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;


    public List DgContainerDeliveryReport(String sCriteria, String srotation, String fromdate, String todate) {


        System.out.println("Shift:" + sCriteria);
        System.out.println("Fromdate:" + fromdate);
        System.out.println("Todate:" + todate);


        String strCrt="";
        String strSupCrt="";


        String eq = "";
        String arr = "";


        if(sCriteria.equals("rotation")){

            strCrt = "igms.Import_Rotation_No='" + srotation + "' AND igm_detail_container.Delivery_Status_date IS NOT NULL";
            strSupCrt = "igms_sup.Import_Rotation_No='" + srotation + "' AND igm_sup_detail_container.Delivery_Status_date IS NOT NULL";

            System.out.println("strCrt:" + strCrt);
            System.out.println("strCrt:" + strSupCrt);
        }

        else if(sCriteria.equals("date")){

            strCrt = "DATE(igm_detail_container.Delivery_Status_date) between '"+fromdate+"' and '"+todate+"'";
            strSupCrt = "DATE(igm_sup_detail_container.Delivery_Status_date) between '"+fromdate+"' and '"+todate+"'";
            System.out.println("strCrt:" + strCrt);
            System.out.println("strCrt:" + strSupCrt);
        }

        String Query="SELECT DISTINCT igms.id AS id,igms.mlocode,igms.IGM_id AS IGM_id,igms.Import_Rotation_No AS Import_Rotation_No,\n" +
                "cont_status,igms.imco,cont_imo,cont_un,igms.Line_No AS Line_No,\n" +
                "igms.BL_No AS BL_No,igms.Pack_Number AS Pack_Number,igms.Pack_Description AS Pack_Description,igms.Pack_Marks_Number AS Pack_Marks_Number,\n" +
                "igms.Description_of_Goods AS Description_of_Goods,igms.Date_of_Entry_of_Goods AS Date_of_Entry_of_Goods,igms.weight AS weight,\n" +
                "igms.weight_unit,igms.net_weight,igms.net_weight_unit, igms.Bill_of_Entry_No AS Bill_of_Entry_No,\n" +
                "igms.Bill_of_Entry_Date AS Bill_of_Entry_Date,igms.No_of_Pack_Delivered AS No_of_Pack_Delivered, \n" +
                "igms.No_of_Pack_Discharged AS No_of_Pack_Discharged,igms.Remarks AS Remarks,igms.AFR AS AFR,igms.ConsigneeDesc,igms.Notify_name,\n" +
                "igms.NotifyDesc,igms.navy_comments,igms.Submitee_Org_Id,igms.mlocode, (SELECT Organization_Name FROM organization_profiles orgs\n" +
                "WHERE orgs.id=igms.Submitee_Org_Id) AS Organization_Name, (SELECT Vessel_Name FROM igm_masters igm_Master WHERE igm_Master.id=igms.IGM_id)\n" +
                "AS vessel_Name, imco,un,extra_remarks,navyresponse.response_details1, navyresponse.response_details2,navyresponse.secondapprovaltime,\n" +
                "navyresponse.response_details3,navyresponse.thirdapprovaltime, navyresponse.hold_application,navyresponse.hold_date,navyresponse.rejected_application,\n" +
                "navyresponse.rejected_date,navyresponse.final_amendment,igm_detail_container.cont_number,igm_detail_container.cont_size,\n" +
                "igm_detail_container.Delivery_Status_date,igm_detail_container.Discharged_Status_date,\n" +
                "un,extra_remarks,navyresponse.response_details1, navyresponse.response_details2,navyresponse.secondapprovaltime,\n" +
                "navyresponse.response_details3,navyresponse.thirdapprovaltime, navyresponse.hold_application,navyresponse.hold_date,navyresponse.rejected_application,\n" +
                "navyresponse.rejected_date, navyresponse.final_amendment,navyresponse.navy_response_to_port\n" +
                "FROM  igm_detail_container \n" +
                "INNER JOIN igm_details igms ON igm_detail_container.igm_detail_id=igms.id \n" +
                "LEFT JOIN igm_navy_response navyresponse ON navyresponse.igm_details_id=igms.id \n" +
                "WHERE "+strCrt+" AND (cont_imo <> '' OR igms.imco <> '' OR igms.un <> '') \n" +
                "UNION\n" +
                "SELECT DISTINCT igms_sup.id AS id,igms_sup.mlocode,igms_sup.IGM_id AS IGM_id,igms_sup.Import_Rotation_No AS Import_Rotation_No,\n" +
                "cont_status,igms_sup.imco,cont_imo,cont_un,igms_sup.Line_No AS Line_No,\n" +
                "igms_sup.BL_No AS BL_No,igms_sup.Pack_Number AS Pack_Number,igms_sup.Pack_Description AS Pack_Description,igms_sup.Pack_Marks_Number AS Pack_Marks_Number,\n" +
                "igms_sup.Description_of_Goods AS Description_of_Goods,igms_sup.Date_of_Entry_of_Goods AS Date_of_Entry_of_Goods,igms_sup.weight AS weight,\n" +
                "igms_sup.weight_unit,igms_sup.net_weight,igms_sup.net_weight_unit, igms_sup.Bill_of_Entry_No AS Bill_of_Entry_No,\n" +
                "igms_sup.Bill_of_Entry_Date AS Bill_of_Entry_Date,igms_sup.No_of_Pack_Delivered AS No_of_Pack_Delivered, \n" +
                "igms_sup.No_of_Pack_Discharged AS No_of_Pack_Discharged,igms_sup.Remarks AS Remarks,igms_sup.AFR AS AFR,igms_sup.ConsigneeDesc,igms_sup.Notify_name,\n" +
                "igms_sup.NotifyDesc,igms_sup.navy_comments,igms_sup.Submitee_Org_Id,igms_sup.mlocode, (SELECT Organization_Name FROM organization_profiles orgs\n" +
                "WHERE orgs.id=igms_sup.Submitee_Org_Id) AS Organization_Name, (SELECT Vessel_Name FROM igm_masters igm_Master WHERE igm_Master.id=igms_sup.IGM_id)\n" +
                "AS vessel_Name, imco,un,extra_remarks,navyresponse.response_details1, navyresponse.response_details2,navyresponse.secondapprovaltime,\n" +
                "navyresponse.response_details3,navyresponse.thirdapprovaltime, navyresponse.hold_application,navyresponse.hold_date,navyresponse.rejected_application,\n" +
                "navyresponse.rejected_date, navyresponse.final_amendment,igm_sup_detail_container.cont_number,igm_sup_detail_container.cont_size,\n" +
                "igm_sup_detail_container.Delivery_Status_date,igm_sup_detail_container.Discharged_Status_date,\n" +
                "un,extra_remarks,navyresponse.response_details1, navyresponse.response_details2,navyresponse.secondapprovaltime,\n" +
                "navyresponse.response_details3,navyresponse.thirdapprovaltime, navyresponse.hold_application,navyresponse.hold_date,navyresponse.rejected_application,\n" +
                "navyresponse.rejected_date, navyresponse.final_amendment,navyresponse.navy_response_to_port\n" +
                "FROM igm_sup_detail_container\n" +
                "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
                "INNER JOIN igm_details igms_sup ON igms_sup.id=igm_supplimentary_detail.igm_detail_id\n" +
                "LEFT JOIN igm_navy_response navyresponse ON navyresponse.igm_details_id=igms_sup.id \n" +
                "WHERE "+strSupCrt+" AND (cont_imo <> '' OR igms_sup.imco <> '' OR igms_sup.un <> '')";
        System.out.println(Query);
        List<DgContainerDeliveryReport> resultList=PrimaryDBTemplate.query(Query,new DgContainer_Report());

        System.out.println(resultList);
        DgContainerDeliveryReport  dgContainerDeliveryReport;
        String import_Rotation_no="";
        String cont_number="";

        for(int i=0;i<resultList.size();i++){
            dgContainerDeliveryReport=resultList.get(i);
        }

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class DgContainer_Report implements RowMapper {
        @Override
        public DgContainerDeliveryReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            String sqlQuery="";
            String cont_number="";
            String import_Rotation_no="";

            DgContainerDeliveryReport dgContainerDeliveryReport=new DgContainerDeliveryReport();
            import_Rotation_no= rs.getString("Import_Rotation_No");
            dgContainerDeliveryReport.setImport_Rotation_No(rs.getString("Import_Rotation_No"));
            dgContainerDeliveryReport.setVessel_Name(rs.getString("vessel_name"));
            cont_number=rs.getString("cont_number");
            dgContainerDeliveryReport.setCont_number(rs.getString("cont_number"));
            dgContainerDeliveryReport.setCont_size(rs.getString("cont_size"));
            dgContainerDeliveryReport.setDischarged_Status_date(rs.getTimestamp("Discharged_Status_date"));
            dgContainerDeliveryReport.setDelivery_Status_date(rs.getTimestamp("Delivery_Status_date"));
            dgContainerDeliveryReport.setBL_No(rs.getString("BL_NO"));
            dgContainerDeliveryReport.setCont_status(rs.getString("cont_status"));
            dgContainerDeliveryReport.setCont_imo(rs.getString("cont_imo"));
            dgContainerDeliveryReport.setCont_un(rs.getString("cont_un"));
            dgContainerDeliveryReport.setDescription_of_Goods(rs.getString("Description_of_Goods"));
            dgContainerDeliveryReport.setNotify_name(rs.getString("Notify_name"));


            String Query="SELECT inv_unit_fcy_visit.flex_string04 AS rl_no,inv_unit_fcy_visit.flex_string04 AS rl_date,inv_unit_fcy_visit.flex_string07 AS obpc_number,\n" +
                    "inv_unit_fcy_visit.flex_string08 AS obpc_date,inv_unit_fcy_visit.time_in, inv_unit_fcy_visit.time_out,argo_carrier_visit.ata,r.id AS mlo\n" +
                    "FROM inv_unit \n" +
                    "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                    "INNER JOIN argo_carrier_visit ON inv_unit_fcy_visit.actual_ib_cv=argo_carrier_visit.gkey \n" +
                    "INNER JOIN vsl_vessel_visit_details ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                    "INNER JOIN ref_bizunit_scoped r ON r.gkey=inv_unit.line_op \n" +
                    "WHERE inv_unit.id ='"+cont_number+"' AND vsl_vessel_visit_details.ib_vyg='"+import_Rotation_no+"'";
            List<DgContainerReport> resultList=OracleDbTemplate.query(Query,new DgContainer_Dalivery_Report());
            DgContainerReport dgContainerReport;
            Integer rl_no=0;
            Integer arr=0;
            for(int i=0;i<resultList.size();i++){
                dgContainerReport=resultList.get(i);
                dgContainerDeliveryReport.setMlo(dgContainerReport.getMlo());
                dgContainerDeliveryReport.setAta(dgContainerReport.getAta());
                dgContainerDeliveryReport.setTime_in(dgContainerReport.getTime_in());
                dgContainerDeliveryReport.setObpc_date(dgContainerReport.getObpc_date());
                dgContainerDeliveryReport.setObpc_number(dgContainerReport.getObpc_number());
                dgContainerDeliveryReport.setTime_out(dgContainerReport.getTime_out());
                dgContainerDeliveryReport.setRl_no(dgContainerReport.getRl_no());
                dgContainerDeliveryReport.setRl_date(dgContainerReport.getRl_date());
            }


            return dgContainerDeliveryReport;
        }
    }
    class DgContainer_Dalivery_Report implements RowMapper {
        @Override
        public DgContainerReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            String sqlQuery="";
            String cont_number="";
            Integer rl_no=0;
            String rl_date="";
            String obpc_date="";
            String time_out="";
            Integer obpc_number=0;
            String import_Rotation_no="";
            DgContainerReport  dgContainerReport=new DgContainerReport();
            dgContainerReport.setRl_no(rs.getInt("rl_no"));
            dgContainerReport.setRl_date(rs.getTimestamp("rl_date"));
            dgContainerReport.setObpc_number(rs.getInt("obpc_number"));
            dgContainerReport.setObpc_date(rs.getString("obpc_date"));

            dgContainerReport.setTime_in(rs.getTimestamp("time_in"));


            dgContainerReport.setTime_out(rs.getTimestamp("time_out"));
            dgContainerReport.setAta(rs.getTimestamp("ata"));

            dgContainerReport.setMlo(rs.getString("mlo"));


            return dgContainerReport;
        }
    }

}