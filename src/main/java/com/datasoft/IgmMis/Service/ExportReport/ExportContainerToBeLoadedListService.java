package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportContainerToBeLoadedList;
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
public class ExportContainerToBeLoadedListService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;

    public Integer getExport_Container_vvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM sparcsn4.vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportContainerToBeLoadedList> resultList=SecondaryDBTemplate.query(sqlQuery,new ExportContainerToBeLoadedVvdGkey());

        ExportContainerToBeLoadedList exportContainerToBeLoadedList;
        for(int i=0;i<resultList.size();i++){
            exportContainerToBeLoadedList=resultList.get(i);
            vvdgkey=exportContainerToBeLoadedList.getVvd_gkey();
        }
        return vvdgkey;
    }

    public List getExport_Container_VoyNo(String rotation){
        String sqlQuery="";
        String voyNo="";
        sqlQuery="SELECT Voy_No FROM igm_masters WHERE Import_Rotation_No='"+rotation+"'";
        System.out.println(sqlQuery);
        List resultList=primaryDBTemplate.query(sqlQuery,new ExportContainerToBeLoadedVoyNo());
        List listAll = (List) resultList.stream().collect(Collectors.toList());

        return listAll;
    }

    class ExportContainerToBeLoadedVoyNo implements RowMapper {

        @Override
        public ExportContainerToBeLoadedList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerToBeLoadedList exportContainerToBeLoadedList =new ExportContainerToBeLoadedList();
            exportContainerToBeLoadedList.setVoy_No(rs.getString("Voy_No"));

            return exportContainerToBeLoadedList;
        }
    }

    class ExportContainerToBeLoadedVvdGkey implements RowMapper {

        @Override
        public ExportContainerToBeLoadedList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerToBeLoadedList exportContainerToBeLoadedList=new ExportContainerToBeLoadedList();
            exportContainerToBeLoadedList.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportContainerToBeLoadedList;
        }
    }



    public List getExportContainerVessleInformation(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="SELECT vsl_vessels.name AS vsl_name,IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,\n" +
                "IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string03,'')) AS berth_op,IFNULL(sparcsn4.argo_quay.id,'') AS berth,\n" +
                "DATE(sparcsn4.vsl_vessel_visit_details.published_eta) AS ata,\n" +
                "sparcsn4.argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessel_berthings ON sparcsn4.vsl_vessel_berthings.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN sparcsn4.argo_quay ON sparcsn4.argo_quay.gkey=sparcsn4.vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new ExportContainerToBeLoadedVesselInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class ExportContainerToBeLoadedVesselInfo implements RowMapper{

        @Override
        public ExportContainerToBeLoadedList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerToBeLoadedList exportContainerToBeLoadedList =new ExportContainerToBeLoadedList();
            exportContainerToBeLoadedList.setVsl_name(rs.getString("vsl_name"));
            exportContainerToBeLoadedList.setBerth_op(rs.getString("berth_op"));
            exportContainerToBeLoadedList.setBerth(rs.getString("berth"));
            exportContainerToBeLoadedList.setAta(rs.getDate("ata"));
            exportContainerToBeLoadedList.setAtd(rs.getTimestamp("atd"));

            return exportContainerToBeLoadedList;
        }
    }


    public List getExportContainerToBeLoadedInformation(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="SELECT CONCAT(SUBSTRING(sparcsn4.inv_unit.id,1,4),' ',SUBSTRING(sparcsn4.inv_unit.id,5)) AS id,sparcsn4.ref_equip_type.id AS iso, sparcsn4.ref_bizunit_scoped.id AS mlo, inv_unit.freight_kind, inv_unit.goods_and_ctr_wt_kg AS weight,\n" +
                "'' AS pod, inv_unit_fcy_visit.last_pos_slot, ref_commodity.short_name\n" +
                "FROM  sparcsn4.inv_unit\n" +
                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.inv_unit.line_op\n" +
                "\n" +
                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.inv_unit_fcy_visit.actual_ib_cv=sparcsn4.argo_carrier_visit.gkey \n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey \n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"' AND category='EXPRT' \n" +
                "AND inv_unit_fcy_visit.transit_state  NOT IN ('S60_LOADED','S70_DEPARTED','S99_RETIRED')\n" +
                "\n";

        System.out.println(sqlQuery);
        List resultList=SecondaryDBTemplate.query(sqlQuery,new getExportContainerToBeLoadedInformation());
        System.out.println(resultList.size());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    class getExportContainerToBeLoadedInformation implements RowMapper{

        @Override
        public ExportContainerToBeLoadedList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerToBeLoadedList exportContainerToBeLoadedList =new ExportContainerToBeLoadedList();
            exportContainerToBeLoadedList.setId(rs.getString("id"));
            exportContainerToBeLoadedList.setIso(rs.getString("iso"));
            exportContainerToBeLoadedList.setMlo(rs.getString("mlo"));
            exportContainerToBeLoadedList.setFreight_kind(rs.getString("freight_kind"));
            exportContainerToBeLoadedList.setWeight(rs.getString("weight"));
            exportContainerToBeLoadedList.setPod(rs.getString("pod"));
            exportContainerToBeLoadedList.setFcy_last_pos_slot(rs.getString("last_pos_slot"));
            exportContainerToBeLoadedList.setShort_name(rs.getString("short_name"));
            return exportContainerToBeLoadedList;
        }
    }


    public List getExportContainerToBeLoadedList(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="\n" +
                "\n" +
                "SELECT gkey,\n" +
                "IFNULL(SUM(balance_LD_20),0) AS balance_LD_20,\n" +
                "IFNULL(SUM(balance_LD_40),0) AS balance_LD_40,\n" +
                "IFNULL(SUM(balance_MT_20),0) AS balance_MT_20,\n" +
                "IFNULL(SUM(balance_MT_40),0) AS balance_MT_40,\n" +
                "IFNULL(SUM(balance_LD_tues),0) AS balance_LD_tues,\n" +
                "IFNULL(SUM(balance_MT_tues),0) AS balance_MT_tues\n" +
                "\n" +
                " FROM (\n" +
                "SELECT DISTINCT sparcsn4.inv_unit.gkey AS gkey,inv_unit.category,\n" +
                "\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind IN ('FCL','LCL')  THEN 1  \n" +
                "ELSE NULL END) AS balance_LD_40,\n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) = '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_20, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2) > '20' AND freight_kind ='MTY'  THEN 1  \n" +
                "ELSE NULL END) AS balance_MT_40, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind IN ('FCL','LCL') THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind IN ('FCL','LCL') THEN 2 ELSE NULL END) END) AS balance_LD_tues, \n" +
                "(CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20 AND freight_kind='MTY' THEN 1 \n" +
                "ELSE (CASE WHEN RIGHT(sparcsn4.ref_equip_type.nominal_length,2)>20 AND freight_kind='MTY' THEN 2 ELSE NULL END) END) AS balance_MT_tues\n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey \n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.inv_unit_fcy_visit.actual_ib_cv=sparcsn4.argo_carrier_visit.gkey \n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey \n" +
                "WHERE sparcsn4.vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"' AND sparcsn4.inv_unit.category='EXPRT' AND inv_unit_fcy_visit.transit_state NOT IN ('S60_LOADED','S70_DEPARTED','S99_RETIRED')\n" +
                ") AS tmp";
        System.out.println(sqlQuery);
        List resultList=SecondaryDBTemplate.query(sqlQuery,new getExportContainerToBeLoadedList());
        System.out.println(resultList.size());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    class getExportContainerToBeLoadedList implements RowMapper{

        @Override
        public ExportContainerToBeLoadedList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportContainerToBeLoadedList exportContainerToBeLoadedList =new ExportContainerToBeLoadedList();

            exportContainerToBeLoadedList.setBalance_LD_20(rs.getString("balance_LD_20"));
            exportContainerToBeLoadedList.setBalance_LD_40(rs.getString("balance_LD_40"));
            exportContainerToBeLoadedList.setBalance_MT_20(rs.getString("balance_MT_20"));
            exportContainerToBeLoadedList.setBalance_MT_40(rs.getString("balance_MT_40"));
            exportContainerToBeLoadedList.setBalance_LD_tues(rs.getString("balance_LD_tues"));
            exportContainerToBeLoadedList.setBalance_MT_tues(rs.getString("balance_MT_tues"));


            return exportContainerToBeLoadedList;
        }
    }


}