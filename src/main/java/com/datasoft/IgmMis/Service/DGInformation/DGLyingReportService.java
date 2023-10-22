package com.datasoft.IgmMis.Service.DGInformation;

import com.datasoft.IgmMis.Model.DGInformation.DGLyingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

@Service
public class DGLyingReportService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    public List getDGResultList(String searchCriteria, String fromDate, String toDate, String yard, String block, String rotation) throws SQLException {
        // System.out.println("yard:" + yard);
        List<DGLyingModel> DGLyingList = new ArrayList<>();

        DGLyingModel DGLying_ResultModel;

        String strYard = "", strCrt = "", strSupCrt = "";

        if (searchCriteria.equals("rotation")) {
            strCrt = " yard_lying_info.rotation='" + rotation + "' AND";

            strSupCrt = " yard_lying_info.rotation='" + rotation + "' AND";
        } else if (searchCriteria.equals("date") ) {
            strYard = "";
            strCrt = " yard_lying_info.discharge_dt BETWEEN '"+fromDate+"' and '"+toDate+"' AND";
            strSupCrt = "yard_lying_info.discharge_dt BETWEEN '"+fromDate+"' and '"+toDate+"' AND";
        } else if (searchCriteria.equals("yard")) {
            strCrt = "";
            strSupCrt = "";
            strYard = "";
            if (block.equals( "ALL") || block.equals( "all")) {
                strYard = " WHERE Yard_No='" + yard + "'";
            } else {
                strYard = " WHERE Yard_No='" + yard + "' AND carrentPosition='" + block + "'";
            }
        } else if (searchCriteria.equals("all") ) {
            strYard = "";
            strCrt = "";
            strSupCrt = "";
        }

        String sqlQuery = "SELECT DISTINCT igms.id AS id,igm_detail_container.cont_number,igm_detail_container.cont_size,igms.mlocode,igms.IGM_id AS IGM_id,igms.Import_Rotation_No AS Import_Rotation_No,\n" +
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
                "igm_detail_container.Delivery_Status_date,igms.Notify_name\n" +
                "FROM  igm_detail_container \n" +
                "INNER JOIN igm_details igms ON igm_detail_container.igm_detail_id=igms.id \n" +
                "INNER JOIN yard_lying_info ON igm_detail_container.cont_number= yard_lying_info.id  AND igms.Import_Rotation_No = yard_lying_info.rotation\n" +
                "LEFT JOIN igm_navy_response navyresponse ON navyresponse.igm_details_id=igms.id\n" +
                "WHERE " + strCrt + "  igm_detail_container.Delivery_Status_date IS NULL\n" +
                "AND (cont_imo <> '' OR igms.imco <> '' OR igms.un <> '') \n" +
                "\n" +
                "\t\tUNION\n" +
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
                "igm_sup_detail_container.Delivery_Status_date,igms_sup.Notify_name\n" +
                "FROM igm_sup_detail_container\n" +
                "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
                "INNER JOIN igm_details igms_sup ON igms_sup.id=igm_supplimentary_detail.igm_detail_id\n" +
                "INNER JOIN yard_lying_info ON igm_sup_detail_container.cont_number= yard_lying_info.id  AND igms_sup.Import_Rotation_No = yard_lying_info.rotation\n" +
                "LEFT JOIN igm_navy_response navyresponse ON navyresponse.igm_details_id=igms_sup.id \n" +
                "WHERE " + strSupCrt + "   igm_sup_detail_container.Delivery_Status_date IS NULL\n" +
                "AND (cont_imo <> '' OR igms_sup.imco <> '' OR igms_sup.un <> '')";

        System.out.println("result:" + sqlQuery);
        List<DGLyingModel> DGLyingResultRowMapper = primaryDBTemplate.query(sqlQuery, new DGLyingRowMapper());

//        System.out.println("sql:" + resultList);
//        List listAll = (List) resultList.stream().collect(Collectors.toList());
//        return listAll;

        for (int i = 0; i < DGLyingResultRowMapper.size(); i++) {

            DGLyingModel dgResltIgm = DGLyingResultRowMapper.get(i);

            DGLying_ResultModel = DGLyingResultRowMapper.get(i);
            String cont_no = "", rot_no = "", n4_query = "";

            cont_no = dgResltIgm.getCont_number();
            rot_no = dgResltIgm.getImport_Rotation_No();

//            DGLying_ResultModel.setCont_size(dgResltIgm.getCont_size());
//            DGLying_ResultModel.setCont_height(dgResltIgm.getCont_height());
//            DGLying_ResultModel.setImport_Rotation_No(dgResltIgm.getImport_Rotation_No());
//            DGLying_ResultModel.setNotify_name(dgResltIgm.getNotify_name());
//            DGLying_ResultModel.setCont_imo(dgResltIgm.getCont_imo());
//            DGLying_ResultModel.setCont_un(dgResltIgm.getCont_un());
//            DGLying_ResultModel.setCont_status(dgResltIgm.getCont_status());
//            DGLying_ResultModel.setDescription_of_Goods(dgResltIgm.getDescription_of_Goods());
//            DGLying_ResultModel.setBL_No(dgResltIgm.getBL_No());
//            DGLying_ResultModel.setVessel_Name(dgResltIgm.getVessel_Name());
//            DGLying_ResultModel.setDelivery_Status_date(dgResltIgm.getDelivery_Status_date());



//            n4_query = "SELECT * FROM (SELECT inv_unit_fcy_visit.flex_string04 AS rl_no,inv_unit_fcy_visit.flex_string04 AS rl_date,inv_unit_fcy_visit.flex_string07 AS obpc_number,\n" +
//                    " inv_unit_fcy_visit.flex_string08 AS obpc_date,inv_unit_fcy_visit.time_in, inv_unit_fcy_visit.time_out,\n" +
//                    " sparcsn4.argo_carrier_visit.ata,r.id as mlo,\n" +
//                    " IFNULL((SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7) \n" +
//                    " FROM sparcsn4.srv_event INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey \n" +
//                    " WHERE sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey AND sparcsn4.srv_event.event_type_gkey IN(18,13,16) AND \n" +
//                    " sparcsn4.srv_event_field_changes.new_value IS NOT NULL AND sparcsn4.srv_event_field_changes.new_value !='' AND \n" +
//                    " sparcsn4.srv_event_field_changes.new_value !='Y-CGP-.' AND sparcsn4.srv_event.gkey<(SELECT sparcsn4.srv_event.gkey \n" +
//                    " FROM sparcsn4.srv_event INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey\n" +
//                    " WHERE sparcsn4.srv_event.event_type_gkey=4 AND sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey AND metafield_id='unitFlexString01' AND\n" +
//                    " new_value IS NOT NULL ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1) ORDER BY sparcsn4.srv_event.gkey DESC LIMIT 1),\n" +
//                    "(SELECT SUBSTRING(sparcsn4.srv_event_field_changes.new_value,7) FROM sparcsn4.srv_event \n" +
//                    " INNER JOIN sparcsn4.srv_event_field_changes ON sparcsn4.srv_event_field_changes.event_gkey=sparcsn4.srv_event.gkey \n" +
//                    " WHERE sparcsn4.srv_event.applied_to_gkey=sparcsn4.inv_unit.gkey AND sparcsn4.srv_event.event_type_gkey IN(18,13,16) \n" +
//                    " ORDER BY sparcsn4.srv_event_field_changes.gkey DESC LIMIT 1)) AS carrentPosition, \n" +
//                    "\t\n" +
//                    " (SELECT ctmsmis.cont_yard(carrentPosition)) AS Yard_No, \n" +
//                    " (SELECT ctmsmis.cont_block(carrentPosition, Yard_No)) AS Block_No\n" +
//                    " FROM sparcsn4.inv_unit \n" +
//                    " INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
//                    " INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.inv_unit_fcy_visit.actual_ib_cv=sparcsn4.argo_carrier_visit.gkey \n" +
//                    " INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
//                    " INNER JOIN sparcsn4.ref_bizunit_scoped r ON r.gkey=inv_unit.line_op \n" +
//                    " INNER JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
//                    " WHERE inv_unit.id ='"+cont_no+"' AND vsl_vessel_visit_details.ib_vyg='"+rot_no+"'\n" +
//                    " AND sparcsn4.inv_goods.destination='2591'  AND sparcsn4.inv_unit_fcy_visit.transit_state='S40_YARD' AND  inv_unit.category='IMPRT')\n" +
//                    " AS tmp "+strYard ;

                    n4_query = "SELECT * FROM (SELECT inv_unit_fcy_visit.flex_string04 AS rl_no,inv_unit_fcy_visit.flex_string04 AS rl_date,\n" +
                            "inv_unit_fcy_visit.flex_string07 AS obpc_number,\n" +
                            "inv_unit_fcy_visit.flex_string08 AS obpc_date,inv_unit_fcy_visit.time_in, inv_unit_fcy_visit.time_out\n" +
                            ",argo_carrier_visit.ata,r.id as mlo,NVL((SELECT SUBSTR(srv_event_field_changes.new_value,7)\n" +
                            "FROM srv_event INNER JOIN srv_event_field_changes ON srv_event_field_changes.event_gkey=srv_event.gkey\n" +
                            "WHERE srv_event.applied_to_gkey=inv_unit.gkey AND srv_event.event_type_gkey IN(18,13,16) AND \n" +
                            "srv_event_field_changes.new_value IS NOT NULL AND srv_event_field_changes.new_value !=''\n" +
                            "AND srv_event_field_changes.new_value !='Y-CGP-.' AND srv_event.gkey<(SELECT srv_event.gkey \n" +
                            "FROM srv_event INNER JOIN srv_event_field_changes ON srv_event_field_changes.event_gkey=srv_event.gkey\n" +
                            "WHERE srv_event.event_type_gkey=4 AND srv_event.applied_to_gkey=inv_unit.gkey  AND metafield_id='unitIsCtrSealed' AND\n" +
                            "new_value IS NOT NULL ORDER BY srv_event_field_changes.gkey DESC FETCH FIRST 1 ROWS ONLY)ORDER BY srv_event.gkey DESC FETCH FIRST 1 ROWS ONLY),\n" +
                            "(SELECT SUBSTR(srv_event_field_changes.new_value,7) FROM srv_event \n" +
                            "INNER JOIN srv_event_field_changes ON srv_event_field_changes.event_gkey=srv_event.gkey \n" +
                            "WHERE srv_event.applied_to_gkey=inv_unit.gkey AND srv_event.event_type_gkey IN(18,13,16) \n" +
                            "ORDER BY srv_event_field_changes.gkey DESC FETCH FIRST 1 ROWS ONLY)\n" +
                            ") AS carrentPosition\n" +
                            "\n" +
                            "FROM inv_unit \n" +
                            "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                            "INNER JOIN argo_carrier_visit ON inv_unit_fcy_visit.actual_ib_cv=argo_carrier_visit.gkey \n" +
                            "INNER JOIN vsl_vessel_visit_details ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                            "INNER JOIN ref_bizunit_scoped r ON r.gkey=inv_unit.line_op \n" +
                            "INNER JOIN inv_goods ON inv_goods.gkey=inv_unit.goods\n" +
                            "WHERE inv_unit.id ='"+cont_no+"' AND vsl_vessel_visit_details.ib_vyg='"+rot_no+"'\n" +
                            "AND inv_goods.destination='2591' AND inv_unit_fcy_visit.transit_state='S40_YARD' AND inv_unit.category='IMPRT') tmp" ;


                             System.out.println("resultn4:" + n4_query);

            List<DGLyingModel> n4DataList = secondaryDBTemplate.query(n4_query, new DGLyingReportService.n4DataListRowMapper());
            if (n4DataList.size() > 0) {

                DGLyingModel n4DgInfo = n4DataList.get(0);
                if( n4DgInfo.getArrival_dt()!=null) {

                    DGLying_ResultModel.setCont_number(dgResltIgm.getCont_number());
                    DGLying_ResultModel.setCont_size(dgResltIgm.getCont_size());
                    DGLying_ResultModel.setCont_height(dgResltIgm.getCont_height());
                    DGLying_ResultModel.setImport_Rotation_No(dgResltIgm.getImport_Rotation_No());
                    DGLying_ResultModel.setNotify_name(dgResltIgm.getNotify_name());
                    DGLying_ResultModel.setCont_imo(dgResltIgm.getCont_imo());
                    DGLying_ResultModel.setCont_un(dgResltIgm.getCont_un());
                    DGLying_ResultModel.setCont_status(dgResltIgm.getCont_status());
                    DGLying_ResultModel.setDescription_of_Goods(dgResltIgm.getDescription_of_Goods());
                    DGLying_ResultModel.setBL_No(dgResltIgm.getBL_No());
                    DGLying_ResultModel.setVessel_Name(dgResltIgm.getVessel_Name());
                    DGLying_ResultModel.setDelivery_Status_date(dgResltIgm.getDelivery_Status_date());


                    DGLying_ResultModel.setArrival_dt(n4DgInfo.getArrival_dt());
                    DGLying_ResultModel.setMlo(n4DgInfo.getMlo());
                    DGLying_ResultModel.setObpc_number(n4DgInfo.getObpc_number());
                    DGLying_ResultModel.setObpc_date(n4DgInfo.getObpc_date());
                    DGLying_ResultModel.setRl_no(n4DgInfo.getRl_no());
                    DGLying_ResultModel.setRl_date(n4DgInfo.getRl_date());
                }
                DGLyingList.add(DGLying_ResultModel);
            }


        }

        return DGLyingList;
    }


    class DGLyingRowMapper implements RowMapper {

        @Override
        public DGLyingModel mapRow(ResultSet rs, int rowNum) throws SQLException {

            DGLyingModel dgByYard = new DGLyingModel();
            dgByYard.setVessel_Name(rs.getString("vessel_Name"));
            dgByYard.setCont_number(rs.getString("cont_number"));
            dgByYard.setCont_size(rs.getString("cont_size"));
            //dgByYard.setCont_height(rs.getString("height"));
            dgByYard.setImport_Rotation_No(rs.getString("Import_Rotation_No"));
            dgByYard.setDischarge_dt(rs.getString("discharge_dt"));
            dgByYard.setBL_No(rs.getString("BL_No"));
            dgByYard.setCont_status(rs.getString("cont_status"));
            dgByYard.setCont_imo(rs.getString("cont_imo"));
            dgByYard.setCont_un(rs.getString("cont_un"));
            dgByYard.setDescription_of_Goods(rs.getString("Description_of_Goods"));
            dgByYard.setNotify_name(rs.getString("Notify_name"));
            dgByYard.setNotifyDesc(rs.getString("NotifyDesc"));
            dgByYard.setDelivery_Status_date(rs.getString("Delivery_Status_date"));

            return dgByYard;
        }
    }


    class n4DataListRowMapper implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
            DGLyingModel N4DgData = new DGLyingModel();

            N4DgData.setArrival_dt(rs.getString("ata"));
            N4DgData.setMlo(rs.getString("mlo"));
            N4DgData.setCarrentPosition(rs.getString("carrentPosition"));
            N4DgData.setObpc_number(rs.getString("obpc_number"));
            N4DgData.setObpc_date(rs.getString("obpc_date"));
            N4DgData.setRl_no(rs.getString("rl_no"));
            N4DgData.setRl_date(rs.getString("rl_date"));
            return N4DgData;
        }
    }
}
