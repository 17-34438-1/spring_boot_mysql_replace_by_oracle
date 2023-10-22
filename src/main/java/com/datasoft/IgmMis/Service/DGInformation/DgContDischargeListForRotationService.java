package com.datasoft.IgmMis.Service.DGInformation;

import com.datasoft.IgmMis.Model.DGInformation.DgContDischargeListByRotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DgContDischargeListForRotationService<dgContDischargeListByRotations> {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate pirmaryDBTemplate;

    List<DgContDischargeListByRotation> dgContDischargeListByRotations;

    public List DgContDischargeForRotationList(String rotation) throws SQLException {
        String import_Rotation_No = rotation.replace("_", "/");
        System.out.println(import_Rotation_No);

        Connection con42 = DriverManager.getConnection("jdbc:mysql://172.16.10.21/cchaportdb","user1","user1test");
        Statement st42 = con42.createStatement();

        dgContDischargeListByRotations = new ArrayList<>();



        String sqlContInfo = "SELECT DISTINCT cont_number,cont_size,cont_height,cont_type,mlocode, off_dock_id, cont_imo,cont_un,\n" +
                "igm_details.Description_of_Goods,cont_gross_weight, cont_status FROM igm_detail_container \n" +
                "INNER JOIN igm_details ON igm_details.id=igm_detail_container.igm_detail_id WHERE Import_Rotation_No='"+import_Rotation_No+"'\n" +
                "AND (cont_imo <> '' OR imco <> '' OR un <> '') \n" +
                "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')\n" +
                "AND cont_status NOT IN ('EMT','EMPTY','MT','ETY') \n" +
                "AND igm_details.final_submit=1\n" +
                "UNION \n" +
                "SELECT DISTINCT cont_number,cont_size,cont_height,cont_type,mlocode, off_dock_id, cont_imo,\n" +
                "cont_un,igm_details.Description_of_Goods,cont_gross_weight,cont_status FROM igm_sup_detail_container \n" +
                "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id \n" +
                "INNER JOIN igm_details ON igm_details.id=igm_supplimentary_detail.igm_detail_id\n" +
                "WHERE igm_details.Import_Rotation_No='"+import_Rotation_No+"' AND (cont_imo <> '' OR imco <> '' OR un <> '') \n" +
                "AND cont_iso_type NOT IN('22R1','45R1','45R0','25R1','45R3','22R0','42R1','45R8','20R1','22R9','42R0','22R2','20R0','45R4','22R7','42R3')  \n" +
                "AND cont_status NOT IN ('EMT','EMPTY','MT','ETY')  AND igm_details.final_submit=1";
        System.out.println("sql_berthOpRpt : "+sqlContInfo);
        ResultSet rslt_berthOptRpt = st42.executeQuery(sqlContInfo);

        while(rslt_berthOptRpt.next()){


            String cont_number=rslt_berthOptRpt.getString("cont_number");
            String cont_size=rslt_berthOptRpt.getString("cont_size");
            String cont_height=rslt_berthOptRpt.getString("cont_height");
            String mlocode=rslt_berthOptRpt.getString("mlocode");
            String off_dock_id=rslt_berthOptRpt.getString("off_dock_id");
            String cont_imo=rslt_berthOptRpt.getString("cont_imo");

            String cont_un=rslt_berthOptRpt.getString("cont_un");
            String Description_of_Goods=rslt_berthOptRpt.getString("Description_of_Goods");
            String cont_gross_weight=rslt_berthOptRpt.getString("cont_gross_weight");
            String cont_status=rslt_berthOptRpt.getString("cont_status");
            DgContDischargeListByRotation bor = new DgContDischargeListByRotation();

            bor.setCont_number(cont_number);
            bor.setCont_size(cont_size);
            bor.setCont_height(cont_height);
            bor.setMlocode(mlocode);
            bor.setOff_dock_id(off_dock_id);
            bor.setCont_imo(cont_imo);
            bor.setCont_un(cont_un);
            bor.setDescription_of_Goods(Description_of_Goods);
            bor.setCont_gross_weight(cont_gross_weight);
            bor.setCont_status(cont_status);
            dgContDischargeListByRotations.add(bor);
        }
        return  dgContDischargeListByRotations;

    }
}
