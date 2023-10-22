package com.datasoft.IgmMis.Service.ImportReport;


import com.datasoft.IgmMis.Model.ImportReport.BerthOperatorReport;
import com.datasoft.IgmMis.Model.ResponseMessage;
import com.datasoft.IgmMis.Repository.ImportReport.BerthOperatorReportRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.*;
import java.util.ArrayList;

import java.util.List;

@Service
public class BerthOperatorReportService {

    @Autowired
    private BerthOperatorReportRepository berthOperatorReportRepository;

    ResponseMessage responseMessage;

    public ResponseEntity mloCodeByRotation(@PathVariable String rotation) {
        List mloCodeList = berthOperatorReportRepository.mloCodeByRotation(rotation);
        mloCodeList.add("All");
        return new ResponseEntity(mloCodeList, HttpStatus.OK);
    }

    public ResponseEntity vslName(@PathVariable String rotation){
        List vslName = berthOperatorReportRepository.vslName(rotation);
        return new ResponseEntity(vslName, HttpStatus.OK);
    }

    List<BerthOperatorReport> bOptRptList;

    public List<BerthOperatorReport> getBerthOptReport(BerthOperatorReport brOptRpt) throws SQLException {
        System.out.println("Service : "+brOptRpt);
        bOptRptList = new ArrayList<>();

        Connection con42 = DriverManager.getConnection("jdbc:mysql://192.168.16.42/cchaportdb","user1","user1test");
        Statement st42 = con42.createStatement();

        String impRot = brOptRpt.getImport_Rotation_No();
        String mlo = brOptRpt.getMlocode();
        String CStatus = brOptRpt.getCStatus();

        //String format = brOptRpt.getFormat();

        System.out.println("impRot : "+impRot);
        System.out.println("mlo : "+mlo);
        System.out.println("CStatus : "+CStatus);

        //System.out.println("format : "+format);

        String sql_berthOpRpt = "";

        if(CStatus.equals("both") || CStatus.equals(""))
        {
            if(mlo.equals("All"))
            {
                sql_berthOpRpt = "SELECT cont_imo AS imco,cont_un AS un,igm_details.Pack_Number,igm_details.Pack_Description,\n" +
                        "(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_detail_container.off_dock_id) AS depu,\n" +
                        "igm_details.Pack_Marks_Number,igm_detail_container.id,\n" +
                        "\n" +
                        "igm_details.Import_Rotation_No,\n" +
                        "igm_details.Import_Rotation_No AS imp_rot,\n" +
                        "\n" +
                        "igm_details.Line_No,\n" +
                        "igm_details.BL_No,igm_details.Submitee_Org_Id,\n" +
                        "\n" +
                        "cont_number,\n" +
                        "cont_number AS cont_no,\n" +
                        "\n" +
                        "cont_size,cont_weight,cont_seal_number,igm_details.Description_of_Goods AS dg,cont_status,cont_height,cont_type,mlocode,\n" +
                        "Organization_Name,commudity_desc,final_amendment,response_details1,\n" +
                        "firstapprovaltime,response_details2,secondapprovaltime,response_details3,thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'D' AS TYPE,\n" +
                        "\n" +
                        "master_Line_No,\n" +
                        "master_Line_No AS mst_line,\n" +
                        "\n" +
                        "Cont_gross_weight\n" +
                        "FROM igm_details \n" +
                        "LEFT JOIN igm_detail_container ON igm_details.id=igm_detail_container.igm_detail_id \n" +
                        "LEFT JOIN commudity_detail ON igm_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_details.id=igm_navy_response.igm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id  \n" +
                        "LEFT JOIN igm_supplimentary_detail ON igm_details.id=igm_supplimentary_detail.igm_detail_id \n" +
                        "WHERE igm_details.Import_Rotation_No LIKE '%"+impRot+"%' \n" +
                        "AND igm_detail_container.port_status IN('0','5') AND igm_supplimentary_detail.id IS NULL  AND igm_details.final_submit=1\n" +
                        "\n" +
                        "UNION\n" +
                        "\n" +
                        "SELECT cont_imo AS imco,cont_un AS un,igm_supplimentary_detail.Pack_Number,igm_supplimentary_detail.Pack_Description,\n" +
                        "(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_sup_detail_container.off_dock_id) AS depu,igm_supplimentary_detail.Pack_Marks_Number,igm_sup_detail_container.id,\n" +
                        "\n" +
                        "igm_supplimentary_detail.Import_Rotation_No,\n" +
                        "igm_supplimentary_detail.Import_Rotation_No AS imp_rot,\n" +
                        "\n" +
                        "igm_supplimentary_detail.Line_No,\n" +
                        "\n" +
                        "igm_supplimentary_detail.BL_No,igm_supplimentary_detail.Submitee_Org_Id,\n" +
                        "\n" +
                        "cont_number,\n" +
                        "cont_number AS cont_no,\n" +
                        "\n" +
                        "cont_size,cont_weight,cont_seal_number,igm_supplimentary_detail.Description_of_Goods AS dg,cont_status,cont_height,cont_type,\n" +
                        "(SELECT mlocode FROM igm_details WHERE id=igm_supplimentary_detail.igm_detail_id LIMIT 1) AS mlocode,Organization_Name,commudity_desc,final_amendment,response_details1,firstapprovaltime,response_details2,secondapprovaltime,response_details3,\n" +
                        "thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'S' AS TYPE,\n" +
                        "\n" +
                        "master_Line_No,\n" +
                        "master_Line_No AS mst_line,\n" +
                        "\n" +
                        "IF(cont_status='LCL',\n" +
                        "(SELECT cont_gross_weight\n" +
                        "FROM igm_detail_container\n" +
                        "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id\n" +
                        "WHERE Import_Rotation_No=imp_rot AND Line_No=mst_line AND cont_number=cont_no LIMIT 1),igm_supplimentary_detail.weight) AS weight\n" +
                        "\n" +
                        "FROM igm_supplimentary_detail \n" +
                        "LEFT JOIN igm_sup_detail_container ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id\n" +
                        "LEFT JOIN commudity_detail ON igm_sup_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_supplimentary_detail.id=igm_navy_response.egm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_supplimentary_detail.Submitee_Org_Id=organization_profiles.id  \n" +
                        "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id\n" +
                        "WHERE igm_supplimentary_detail.Import_Rotation_No LIKE '%"+impRot+"%' \n" +
                        "AND igm_sup_detail_container.port_status IN('0','5')\n" +
                        "AND igm_supplimentary_detail.final_submit=1 ORDER BY cont_no,Organization_Name";
            }
            else
            {
                sql_berthOpRpt = "SELECT cont_imo AS imco,cont_un AS un,igm_details.Pack_Number,igm_details.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_detail_container.off_dock_id) AS depu,igm_details.Pack_Marks_Number,igm_detail_container.id,igm_details.Import_Rotation_No,igm_details.Line_No,REPLACE(igm_details.BL_No,'S','\\'') AS BL_No,igm_details.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_details.Description_of_Goods AS dg,cont_status,cont_height,cont_type,mlocode,cont_type,\n" +
                        "Cont_gross_weight,Organization_Name,commudity_desc,final_amendment,response_details1,\n" +
                        "firstapprovaltime,response_details2,secondapprovaltime,response_details3,thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'D' AS TYPE,master_Line_No\n" +
                        "FROM igm_details LEFT JOIN igm_detail_container ON igm_details.id=igm_detail_container.igm_detail_id \n" +
                        "LEFT JOIN commudity_detail ON igm_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_details.id=igm_navy_response.igm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id  \n" +
                        "LEFT JOIN igm_supplimentary_detail ON igm_details.id=igm_supplimentary_detail.igm_detail_id WHERE igm_details.Import_Rotation_No LIKE '%"+impRot+"%' \n" +
                        "AND igm_detail_container.port_status IN('0','5') AND igm_supplimentary_detail.id IS NULL AND mlocode='"+mlo+"' AND igm_details.final_submit=1\n" +
                        "UNION\n" +
                        "SELECT cont_imo AS imco,cont_un AS un,igm_supplimentary_detail.Pack_Number,igm_supplimentary_detail.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_sup_detail_container.off_dock_id) AS depu,igm_supplimentary_detail.Pack_Marks_Number,igm_sup_detail_container.id,igm_supplimentary_detail.Import_Rotation_No,igm_supplimentary_detail.Line_No,igm_supplimentary_detail.BL_No,igm_supplimentary_detail.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_supplimentary_detail.Description_of_Goods AS dg,cont_status,cont_height,cont_type,(SELECT mlocode FROM igm_details WHERE id=igm_supplimentary_detail.igm_detail_id LIMIT 1) AS mlocode,cont_type,\n" +
                        "igm_supplimentary_detail.weight,Organization_Name,commudity_desc,final_amendment,response_details1,firstapprovaltime,response_details2,secondapprovaltime,response_details3,\n" +
                        "thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'S' AS TYPE,master_Line_No\n" +
                        "FROM igm_supplimentary_detail LEFT JOIN igm_sup_detail_container ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id\n" +
                        "LEFT JOIN commudity_detail ON igm_sup_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_supplimentary_detail.id=igm_navy_response.egm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_supplimentary_detail.Submitee_Org_Id=organization_profiles.id  \n" +
                        "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id\n" +
                        "WHERE igm_supplimentary_detail.Import_Rotation_No LIKE '%"+impRot+"%' \n" +
                        "AND igm_sup_detail_container.port_status IN('0','5')\n" +
                        "AND mlocode='"+mlo+"' AND igm_supplimentary_detail.final_submit=1 ORDER BY cont_number,Organization_Name";
            }
        }
        else if(CStatus.equals("fcl"))
        {
            if(mlo.equals("All"))
            {
                sql_berthOpRpt = "SELECT cont_imo AS imco,cont_un AS un,igm_details.Pack_Number,igm_details.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_detail_container.off_dock_id) AS depu,REPLACE(igm_details.Pack_Marks_Number,'S','\\'') AS Pack_Marks_Number,igm_detail_container.id,igm_details.Import_Rotation_No,igm_details.Line_No,REPLACE(igm_details.BL_No,'S','\\'') AS BL_No,igm_details.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_details.Description_of_Goods AS dg,cont_status,cont_height,cont_type,mlocode,cont_type,\n" +
                        "Cont_gross_weight,Organization_Name,commudity_desc,final_amendment,response_details1,\n" +
                        "firstapprovaltime,response_details2,secondapprovaltime,response_details3,thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'D' AS TYPE,master_Line_No\n" +
                        "FROM igm_details LEFT JOIN igm_detail_container ON igm_details.id=igm_detail_container.igm_detail_id \n" +
                        "LEFT JOIN commudity_detail ON igm_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_details.id=igm_navy_response.igm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id  \n" +
                        "LEFT JOIN igm_supplimentary_detail ON igm_details.id=igm_supplimentary_detail.igm_detail_id WHERE igm_details.Import_Rotation_No LIKE '%"+impRot+"%' AND cont_status='FCL'\n" +
                        "AND igm_detail_container.port_status IN('0','5') AND igm_supplimentary_detail.id IS NULL  AND igm_details.final_submit=1\n" +
                        "UNION\n" +
                        "SELECT cont_imo AS imco,cont_un AS un,igm_supplimentary_detail.Pack_Number,igm_supplimentary_detail.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_sup_detail_container.off_dock_id) AS depu,igm_supplimentary_detail.Pack_Marks_Number,igm_sup_detail_container.id,igm_supplimentary_detail.Import_Rotation_No,igm_supplimentary_detail.Line_No,igm_supplimentary_detail.BL_No,igm_supplimentary_detail.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_supplimentary_detail.Description_of_Goods AS dg,cont_status,cont_height,cont_type,(SELECT mlocode FROM igm_details WHERE id=igm_supplimentary_detail.igm_detail_id LIMIT 1) AS mlocode,cont_type,\n" +
                        "igm_supplimentary_detail.weight,Organization_Name,commudity_desc,final_amendment,response_details1,firstapprovaltime,response_details2,secondapprovaltime,response_details3,\n" +
                        "thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'S' AS TYPE,master_Line_No\n" +
                        "FROM igm_supplimentary_detail LEFT JOIN igm_sup_detail_container ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id\n" +
                        "LEFT JOIN commudity_detail ON igm_sup_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_supplimentary_detail.id=igm_navy_response.egm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_supplimentary_detail.Submitee_Org_Id=organization_profiles.id  \n" +
                        "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id\n" +
                        "WHERE igm_supplimentary_detail.Import_Rotation_No LIKE '%"+impRot+"%' AND cont_status='FCL'\n" +
                        "AND igm_sup_detail_container.port_status IN('0','5')\n" +
                        "AND igm_supplimentary_detail.final_submit=1 ORDER BY cont_number,Organization_Name";
            }
            else
            {
                sql_berthOpRpt = "SELECT cont_imo AS imco,cont_un AS un,igm_details.Pack_Number,igm_details.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_detail_container.off_dock_id) AS depu,REPLACE(igm_details.Pack_Marks_Number,'S','\\'') AS Pack_Marks_Number,igm_detail_container.id,igm_details.Import_Rotation_No,igm_details.Line_No,REPLACE(igm_details.BL_No,'S','\\'') AS BL_No,igm_details.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_details.Description_of_Goods AS dg,cont_status,cont_height,cont_type,mlocode,cont_type,\n" +
                        "Cont_gross_weight,Organization_Name,commudity_desc,final_amendment,response_details1,\n" +
                        "firstapprovaltime,response_details2,secondapprovaltime,response_details3,thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'D' AS TYPE,master_Line_No\n" +
                        "FROM igm_details LEFT JOIN igm_detail_container ON igm_details.id=igm_detail_container.igm_detail_id \n" +
                        "LEFT JOIN commudity_detail ON igm_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_details.id=igm_navy_response.igm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id  \n" +
                        "LEFT JOIN igm_supplimentary_detail ON igm_details.id=igm_supplimentary_detail.igm_detail_id WHERE igm_details.Import_Rotation_No LIKE '%"+impRot+"%' AND cont_status='FCL'\n" +
                        "AND igm_detail_container.port_status IN('0','5') AND igm_supplimentary_detail.id IS NULL AND mlocode='"+mlo+"' AND igm_details.final_submit=1\n" +
                        "UNION\n" +
                        "SELECT cont_imo AS imco,cont_un AS un,igm_supplimentary_detail.Pack_Number,igm_supplimentary_detail.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_sup_detail_container.off_dock_id) AS depu,igm_supplimentary_detail.Pack_Marks_Number,igm_sup_detail_container.id,igm_supplimentary_detail.Import_Rotation_No,igm_supplimentary_detail.Line_No,igm_supplimentary_detail.BL_No,igm_supplimentary_detail.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_supplimentary_detail.Description_of_Goods AS dg,cont_status,cont_height,cont_type,(SELECT mlocode FROM igm_details WHERE id=igm_supplimentary_detail.igm_detail_id LIMIT 1) AS mlocode,cont_type,\n" +
                        "igm_supplimentary_detail.weight,Organization_Name,commudity_desc,final_amendment,response_details1,firstapprovaltime,response_details2,secondapprovaltime,response_details3,\n" +
                        "thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'S' AS TYPE,master_Line_No\n" +
                        "FROM igm_supplimentary_detail LEFT JOIN igm_sup_detail_container ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id\n" +
                        "LEFT JOIN commudity_detail ON igm_sup_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_supplimentary_detail.id=igm_navy_response.egm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_supplimentary_detail.Submitee_Org_Id=organization_profiles.id  \n" +
                        "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id\n" +
                        "WHERE igm_supplimentary_detail.Import_Rotation_No LIKE '%"+impRot+"%' AND cont_status='FCL'\n" +
                        "AND igm_sup_detail_container.port_status IN('0','5')\n" +
                        "AND mlocode='"+mlo+"' AND igm_supplimentary_detail.final_submit=1 ORDER BY cont_number,Organization_Name";
            }
        }
        else
        {
            if(mlo.equals("All"))
            {
                sql_berthOpRpt = "SELECT cont_imo AS imco,cont_un AS un,igm_details.Pack_Number,igm_details.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_detail_container.off_dock_id) AS depu,REPLACE(igm_details.Pack_Marks_Number,'S','\\'') AS Pack_Marks_Number,igm_detail_container.id,igm_details.Import_Rotation_No,igm_details.Line_No,REPLACE(igm_details.BL_No,'S','\\'') AS BL_No,igm_details.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_details.Description_of_Goods AS dg,cont_status,cont_height,cont_type,mlocode,cont_type,\n" +
                        "Cont_gross_weight,Organization_Name,commudity_desc,final_amendment,response_details1,\n" +
                        "firstapprovaltime,response_details2,secondapprovaltime,response_details3,thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'D' AS TYPE,master_Line_No\n" +
                        "FROM igm_details LEFT JOIN igm_detail_container ON igm_details.id=igm_detail_container.igm_detail_id \n" +
                        "LEFT JOIN commudity_detail ON igm_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_details.id=igm_navy_response.igm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id  \n" +
                        "LEFT JOIN igm_supplimentary_detail ON igm_details.id=igm_supplimentary_detail.igm_detail_id WHERE igm_details.Import_Rotation_No LIKE '%"+impRot+"%' AND cont_status='LCL'\n" +
                        "AND igm_detail_container.port_status IN('0','5') AND igm_supplimentary_detail.id IS NULL AND igm_details.final_submit=1\n" +
                        "UNION\n" +
                        "SELECT cont_imo AS imco,cont_un AS un,igm_supplimentary_detail.Pack_Number,igm_supplimentary_detail.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_sup_detail_container.off_dock_id) AS depu,igm_supplimentary_detail.Pack_Marks_Number,igm_sup_detail_container.id,igm_supplimentary_detail.Import_Rotation_No,igm_supplimentary_detail.Line_No,igm_supplimentary_detail.BL_No,igm_supplimentary_detail.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_supplimentary_detail.Description_of_Goods AS dg,cont_status,cont_height,cont_type,(SELECT mlocode FROM igm_details WHERE id=igm_supplimentary_detail.igm_detail_id LIMIT 1) AS mlocode,cont_type,\n" +
                        "igm_supplimentary_detail.weight,Organization_Name,commudity_desc,final_amendment,response_details1,firstapprovaltime,response_details2,secondapprovaltime,response_details3,\n" +
                        "thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'S' AS TYPE,master_Line_No\n" +
                        "FROM igm_supplimentary_detail LEFT JOIN igm_sup_detail_container ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id\n" +
                        "LEFT JOIN commudity_detail ON igm_sup_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_supplimentary_detail.id=igm_navy_response.egm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_supplimentary_detail.Submitee_Org_Id=organization_profiles.id  \n" +
                        "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id\n" +
                        "WHERE igm_supplimentary_detail.Import_Rotation_No LIKE '%"+impRot+"%' AND cont_status='LCL'\n" +
                        "AND igm_sup_detail_container.port_status IN('0','5')\n" +
                        "AND igm_supplimentary_detail.final_submit=1 ORDER BY cont_number,Organization_Name";
            }
            else
            {
                sql_berthOpRpt = "SELECT cont_imo AS imco,cont_un AS un,igm_details.Pack_Number,igm_details.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_detail_container.off_dock_id) AS depu,REPLACE(igm_details.Pack_Marks_Number,'S','\\'') AS Pack_Marks_Number,igm_detail_container.id,igm_details.Import_Rotation_No,igm_details.Line_No,REPLACE(igm_details.BL_No,'S','\\'') AS BL_No,igm_details.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_details.Description_of_Goods AS dg,cont_status,cont_height,cont_type,mlocode,cont_type,\n" +
                        "Cont_gross_weight,Organization_Name,commudity_desc,final_amendment,response_details1,\n" +
                        "firstapprovaltime,response_details2,secondapprovaltime,response_details3,thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'D' AS TYPE,master_Line_No\n" +
                        "FROM igm_details LEFT JOIN igm_detail_container ON igm_details.id=igm_detail_container.igm_detail_id \n" +
                        "LEFT JOIN commudity_detail ON igm_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_details.id=igm_navy_response.igm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_details.Submitee_Org_Id=organization_profiles.id  \n" +
                        "LEFT JOIN igm_supplimentary_detail ON igm_details.id=igm_supplimentary_detail.igm_detail_id WHERE igm_details.Import_Rotation_No LIKE '%"+impRot+"%' AND cont_status='LCL'\n" +
                        "AND igm_detail_container.port_status IN('0','5') AND igm_supplimentary_detail.id IS NULL AND mlocode='"+mlo+"' AND igm_details.final_submit=1\n" +
                        "UNION\n" +
                        "SELECT cont_imo AS imco,cont_un AS un,igm_supplimentary_detail.Pack_Number,igm_supplimentary_detail.Pack_Description,(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_sup_detail_container.off_dock_id) AS depu,igm_supplimentary_detail.Pack_Marks_Number,igm_sup_detail_container.id,igm_supplimentary_detail.Import_Rotation_No,igm_supplimentary_detail.Line_No,igm_supplimentary_detail.BL_No,igm_supplimentary_detail.Submitee_Org_Id,cont_number,cont_size,cont_weight,cont_seal_number,igm_supplimentary_detail.Description_of_Goods AS dg,cont_status,cont_height,cont_type,(SELECT mlocode FROM igm_details WHERE id=igm_supplimentary_detail.igm_detail_id LIMIT 1) AS mlocode,cont_type,\n" +
                        "igm_supplimentary_detail.weight,Organization_Name,commudity_desc,final_amendment,response_details1,firstapprovaltime,response_details2,secondapprovaltime,response_details3,\n" +
                        "thirdapprovaltime,hold_application,rejected_application,hold_date,rejected_date,'S' AS TYPE,master_Line_No\n" +
                        "FROM igm_supplimentary_detail LEFT JOIN igm_sup_detail_container ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id\n" +
                        "LEFT JOIN commudity_detail ON igm_sup_detail_container.commudity_code=commudity_detail.commudity_code\n" +
                        "LEFT JOIN igm_navy_response ON igm_supplimentary_detail.id=igm_navy_response.egm_details_id\n" +
                        "LEFT JOIN organization_profiles ON igm_supplimentary_detail.Submitee_Org_Id=organization_profiles.id  \n" +
                        "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id\n" +
                        "WHERE igm_supplimentary_detail.Import_Rotation_No LIKE '%"+impRot+"%' AND cont_status='LCL'\n" +
                        "AND igm_sup_detail_container.port_status IN('0','5')\n" +
                        "AND mlocode='"+mlo+"' AND igm_supplimentary_detail.final_submit=1 ORDER BY cont_number,Organization_Name";
            }
        }

        System.out.println("sql_berthOpRpt : "+sql_berthOpRpt);
        ResultSet rslt_berthOptRpt = st42.executeQuery(sql_berthOpRpt);

        Integer Serial_No = 0;
        String prevCont = "";

        while(rslt_berthOptRpt.next()){

            String imco = rslt_berthOptRpt.getString("imco");
            String un = rslt_berthOptRpt.getString("un");
            String Pack_Number = rslt_berthOptRpt.getString("Pack_Number");
            String Pack_Description = rslt_berthOptRpt.getString("Pack_Description");
            String depu = rslt_berthOptRpt.getString("depu");
            String Pack_Marks_Number = rslt_berthOptRpt.getString("Pack_Marks_Number");

            Long id = rslt_berthOptRpt.getLong("id");
            String Import_Rotation_No = rslt_berthOptRpt.getString("Import_Rotation_No");
            String Line_No = rslt_berthOptRpt.getString("Line_No");
            String BL_No = rslt_berthOptRpt.getString("BL_No");

            String cont_number = rslt_berthOptRpt.getString("cont_number");
            String cont_size = rslt_berthOptRpt.getString("cont_size");
            String cont_weight = rslt_berthOptRpt.getString("cont_weight");
            String cont_seal_number = rslt_berthOptRpt.getString("cont_seal_number");
            String dg = rslt_berthOptRpt.getString("dg");
            String cont_status = rslt_berthOptRpt.getString("cont_status");
            String cont_height = rslt_berthOptRpt.getString("cont_height");
            String cont_type = rslt_berthOptRpt.getString("cont_type");
            String mlocode = rslt_berthOptRpt.getString("mlocode");
            String Cont_gross_weight = rslt_berthOptRpt.getString("Cont_gross_weight");
            String Organization_Name = rslt_berthOptRpt.getString("Organization_Name");
            String commudity_desc = rslt_berthOptRpt.getString("commudity_desc");

            String final_amendment = rslt_berthOptRpt.getString("final_amendment");
            String response_details1 = rslt_berthOptRpt.getString("response_details1");
            String firstapprovaltime = rslt_berthOptRpt.getString("firstapprovaltime");
            String response_details2 = rslt_berthOptRpt.getString("response_details2");
            String secondapprovaltime = rslt_berthOptRpt.getString("secondapprovaltime");
            String response_details3 = rslt_berthOptRpt.getString("response_details3");
            String thirdapprovaltime = rslt_berthOptRpt.getString("thirdapprovaltime");
            String hold_application = rslt_berthOptRpt.getString("hold_application");
            String rejected_application = rslt_berthOptRpt.getString("rejected_application");
            String hold_date = rslt_berthOptRpt.getString("hold_date");
            String rejected_date = rslt_berthOptRpt.getString("rejected_date");
            String TYPE = rslt_berthOptRpt.getString("TYPE");
            String master_Line_No = rslt_berthOptRpt.getString("master_Line_No");

            BerthOperatorReport bor = new BerthOperatorReport();

            bor.setImco(imco);
            bor.setUn(un);
            bor.setPack_Number(Pack_Number);
            bor.setPack_Description(Pack_Description);
            bor.setDepu(depu);
            bor.setPack_Marks_Number(Pack_Marks_Number);

            bor.setId(id);
            bor.setImport_Rotation_No(Import_Rotation_No);
            bor.setLine_No(Line_No);
            bor.setBL_No(BL_No);

            bor.setCont_number(cont_number);
            bor.setCont_size(cont_size);
            bor.setCont_weight(cont_weight);
            bor.setCont_seal_number(cont_seal_number);
            bor.setDg(dg);
            bor.setCont_status(cont_status);
            bor.setCont_height(cont_height);
            bor.setCont_type(cont_type);
            bor.setMlocode(mlocode);
            bor.setCont_gross_weight(Cont_gross_weight);
            bor.setOrganization_Name(Organization_Name);
            bor.setCommudity_desc(commudity_desc);
            bor.setFinal_amendment(final_amendment);
            bor.setResponse_details1(response_details1);
            bor.setFirstapprovaltime(firstapprovaltime);
            bor.setResponse_details2(response_details2);
            bor.setSecondapprovaltime(secondapprovaltime);
            bor.setResponse_details3(response_details3);
            bor.setThirdapprovaltime(thirdapprovaltime);
            bor.setHold_application(hold_application);
            bor.setRejected_application(rejected_application);
            bor.setHold_date(hold_date);
            bor.setRejected_date(rejected_date);
            bor.setType(TYPE);
            bor.setMaster_Line_No(master_Line_No);

            // check container

            if(!prevCont.equals(cont_number))
            {
                Serial_No++;
                prevCont = cont_number;
            }
            bor.setSerial_No(Serial_No);

            bOptRptList.add(bor);
        }

        return bOptRptList;
    }


}
