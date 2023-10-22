package com.datasoft.IgmMis.Service.HandlingReport;

import com.datasoft.IgmMis.Model.HandlingReport.HandlingReport;
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
public class HandlingReportService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    public Integer vvd_gkey=0;
    public List getContainerHandlingReportByRotation(String rotation){
        List<HandlingReport> handlingReports=new ArrayList<>();
        String sqlQuery="SELECT sparcsn4.vsl_vessel_visit_details.ib_vyg,\n" +
                "sparcsn4.vsl_vessel_visit_details.ob_vyg,\n" +
                "sparcsn4.vsl_vessel_visit_details.vvd_gkey,\n" +
                "sparcsn4.ref_bizunit_scoped.id AS shipping_agent,\n" +
                "sparcsn4.vsl_vessels.name,\n" +
                "DATE(sparcsn4.argo_carrier_visit.ata) AS arrived_date,\n" +
                "DATE(sparcsn4.argo_carrier_visit.atd) AS departure_date,\n" +
                "(SELECT ctmsmis.berth_for_vessel(sparcsn4.vsl_vessel_visit_details.vvd_gkey)) AS berth\n" +
                "FROM sparcsn4.vsl_vessel_visit_details\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.vsl_vessel_visit_details.bizu_gkey\n" +
                "WHERE  sparcsn4.vsl_vessel_visit_details.ib_vyg='"+rotation+"'";

        handlingReports=SecondaryDBTemplate.query(sqlQuery,new ContainerHandlingReportByRotation());

        if(handlingReports.size()>0){
            vvd_gkey=handlingReports.get(0).getVvd_gkey();
        }


        List listAll = (List) handlingReports.stream().collect(Collectors.toList());

        return listAll;
    }



    public Integer getHandlingReport_vvdgkey(String rotation){
        List<HandlingReport> handlingReports=new ArrayList<>();
        String sqlQuery="SELECT sparcsn4.vsl_vessel_visit_details.ib_vyg,\n" +
                "sparcsn4.vsl_vessel_visit_details.ob_vyg,\n" +
                "sparcsn4.vsl_vessel_visit_details.vvd_gkey,\n" +
                "sparcsn4.ref_bizunit_scoped.id AS shipping_agent,\n" +
                "sparcsn4.vsl_vessels.name,\n" +
                "DATE(sparcsn4.argo_carrier_visit.ata) AS arrived_date,\n" +
                "DATE(sparcsn4.argo_carrier_visit.atd) AS departure_date,\n" +
                "(SELECT ctmsmis.berth_for_vessel(sparcsn4.vsl_vessel_visit_details.vvd_gkey)) AS berth\n" +
                "FROM sparcsn4.vsl_vessel_visit_details\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.vsl_vessel_visit_details.bizu_gkey\n" +
                "WHERE  sparcsn4.vsl_vessel_visit_details.ib_vyg='"+rotation+"'";

        handlingReports=SecondaryDBTemplate.query(sqlQuery,new ContainerHandlingReportByRotation());

        if(handlingReports.size()>0){
            vvd_gkey=handlingReports.get(0).getVvd_gkey();
        }

        return vvd_gkey;
    }



    public List ContainerHandlingImportReport(Integer vvd_gkey, String work_date){

        String sqlQuery= "SELECT \n" +
                "SUM(disch_load20) AS disch_load20, \n" +
                "SUM(disch_load40) AS disch_load40,\n" +
                "SUM(disch_mty20) AS disch_mty20,\n" +
                "SUM(disch_mty40) AS disch_mty40,\n" +
                "(SUM(disch_load20)+SUM(disch_load40)*2) AS load_teus,\n" +
                "(SUM(disch_mty20)+SUM(disch_mty40)*2) AS mty_teus,\n" +
                "SUM(tot_disch_load20) AS tot_disch_load20, \n" +
                "SUM(tot_disch_load40) AS tot_disch_load40,\n" +
                "SUM(tot_disch_mty20) AS tot_disch_mty20,\n" +
                "SUM(tot_disch_mty40) AS tot_disch_mty40,\n" +
                "(SUM(tot_disch_load20)+SUM(tot_disch_load40)*2) AS tot_disch_load_teus,\n" +
                "(SUM(tot_disch_mty20)+SUM(tot_disch_mty40)*2) AS tot_disch_mty_teus,\n" +
                "SUM(bal_load20) AS bal_load20, \n" +
                "SUM(bal_load40) AS bal_load40,\n" +
                "SUM(bal_mty20) AS bal_mty20,\n" +
                "SUM(bal_mty40) AS bal_mty40,\n" +
                "(SUM(bal_load20)+SUM(bal_load40)*2) AS bal_load_teus,\n" +
                "(SUM(bal_mty20)+SUM(bal_mty40)*2) AS bal_mty_teus    \n" +
                "FROM\n" +
                "(\n" +
                "SELECT sparcsn4.inv_unit.id,freight_kind,ctmsmis.mis_disch_cont.disch_dt,\n" +
                "RIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size,\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NOT NULL AND ctmsmis.mis_disch_cont.disch_dt>= CONCAT(DATE(SUBDATE('"+work_date+"',1)), ' 08:00:00')\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt<'"+work_date+" 08:00:00'\n" +
                "THEN 1 ELSE 0 END) AS disch_load20,\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20 \n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NOT NULL AND ctmsmis.mis_disch_cont.disch_dt>= CONCAT(DATE(SUBDATE('"+work_date+"',1)), ' 08:00:00')\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt<'"+work_date+" 08:00:00'\n" +
                "THEN 1 ELSE 0 END) AS disch_load40,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NOT NULL AND ctmsmis.mis_disch_cont.disch_dt>= CONCAT(DATE(SUBDATE('"+work_date+"',1)), ' 08:00:00')\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt<'"+work_date+" 08:00:00'\n" +
                "THEN 1 ELSE 0 END) AS disch_mty20,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NOT NULL AND ctmsmis.mis_disch_cont.disch_dt>= CONCAT(DATE(SUBDATE('"+work_date+"',1)), ' 08:00:00')\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt<'"+work_date+" 08:00:00'\n" +
                "THEN 1 ELSE 0 END) AS disch_mty40,\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NOT NULL\n" +
                "THEN 1 ELSE 0 END) AS tot_disch_load20,\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20 \n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NOT NULL\n" +
                "THEN 1 ELSE 0 END) AS tot_disch_load40,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NOT NULL\n" +
                "THEN 1 ELSE 0 END) AS tot_disch_mty20,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NOT NULL\n" +
                "THEN 1 ELSE 0 END) AS tot_disch_mty40,\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NULL\n" +
                "THEN 1 ELSE 0 END) AS bal_load20,\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20 \n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NULL\n" +
                "THEN 1 ELSE 0 END) AS bal_load40,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NULL\n" +
                "THEN 1 ELSE 0 END) AS bal_mty20,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20\n" +
                "AND ctmsmis.mis_disch_cont.disch_dt IS NULL\n" +
                "THEN 1 ELSE 0 END) AS bal_mty40\n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ib_cv\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "LEFT JOIN ctmsmis.mis_disch_cont ON sparcsn4.inv_unit.gkey=ctmsmis.mis_disch_cont.gkey\n" +
                "WHERE  sparcsn4.argo_carrier_visit.cvcvd_gkey='"+vvd_gkey+"') AS tmp";

        List resultList=SecondaryDBTemplate.query(sqlQuery,new ContainerHandlingReport());
        System.out.println("sql:"+sqlQuery);
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    public List ContainerHandlingExportReport(Integer vvd_gkey, String work_date){

        String sqlQuery= "\n" +
                "SELECT \n" +
                "SUM(disch_load20) AS disch_load20, \n" +
                "SUM(disch_load40) AS disch_load40,\n" +
                "SUM(disch_mty20) AS disch_mty20,\n" +
                "SUM(disch_mty40) AS disch_mty40,\n" +
                "(SUM(disch_load20)+SUM(disch_load40)*2) AS load_teus,\n" +
                "(SUM(disch_mty20)+SUM(disch_mty40)*2) AS mty_teus,\n" +
                "\n" +
                "SUM(tot_disch_load20) AS tot_disch_load20, \n" +
                "SUM(tot_disch_load40) AS tot_disch_load40,\n" +
                "SUM(tot_disch_mty20) AS tot_disch_mty20,\n" +
                "SUM(tot_disch_mty40) AS tot_disch_mty40,\n" +
                "(SUM(tot_disch_load20)+SUM(tot_disch_load40)*2) AS tot_disch_load_teus,\n" +
                "(SUM(tot_disch_mty20)+SUM(tot_disch_mty40)*2) AS tot_disch_mty_teus,\n" +
                "\n" +
                "SUM(bal_load20) AS bal_load20, \n" +
                "SUM(bal_load40) AS bal_load40,\n" +
                "SUM(bal_mty20) AS bal_mty20,\n" +
                "SUM(bal_mty40) AS bal_mty40,\n" +
                "(SUM(bal_load20)+SUM(bal_load40)*2) AS bal_load_teus,\n" +
                "(SUM(bal_mty20)+SUM(bal_mty40)*2) AS bal_mty_teus    \n" +
                "\n" +
                "FROM\n" +
                "(\n" +
                "SELECT sparcsn4.inv_unit.id,freight_kind,sparcsn4.inv_unit_fcy_visit.time_load,\n" +
                "RIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size,\n" +
                "\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NOT NULL AND sparcsn4.inv_unit_fcy_visit.time_load>= CONCAT(DATE(SUBDATE('"+work_date+"',1)), ' 08:00:00')\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load<'"+work_date+" 08:00:00'\n" +
                "THEN 1 ELSE 0 END) AS disch_load20,\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20 \n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NOT NULL AND sparcsn4.inv_unit_fcy_visit.time_load>= CONCAT(DATE(SUBDATE('"+work_date+"',1)), ' 08:00:00')\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load<'"+work_date+" 08:00:00'\n" +
                "THEN 1 ELSE 0 END) AS disch_load40,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NOT NULL AND sparcsn4.inv_unit_fcy_visit.time_load>= CONCAT(DATE(SUBDATE('"+work_date+"',1)), ' 08:00:00')\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load<'"+work_date+" 08:00:00'\n" +
                "THEN 1 ELSE 0 END) AS disch_mty20,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NOT NULL AND sparcsn4.inv_unit_fcy_visit.time_load>= CONCAT(DATE(SUBDATE('"+work_date+"',1)), ' 08:00:00')\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load<'"+work_date+" 08:00:00'\n" +
                "THEN 1 ELSE 0 END) AS disch_mty40,\n" +
                "\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NOT NULL\n" +
                "THEN 1 ELSE 0 END) AS tot_disch_load20,\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20 \n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NOT NULL\n" +
                "THEN 1 ELSE 0 END) AS tot_disch_load40,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NOT NULL\n" +
                "THEN 1 ELSE 0 END) AS tot_disch_mty20,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NOT NULL\n" +
                "THEN 1 ELSE 0 END) AS tot_disch_mty40,\n" +
                "\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NULL\n" +
                "THEN 1 ELSE 0 END) AS bal_load20,\n" +
                "(CASE WHEN freight_kind != 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20 \n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NULL\n" +
                "THEN 1 ELSE 0 END) AS bal_load40,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)=20\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NULL\n" +
                "THEN 1 ELSE 0 END) AS bal_mty20,\n" +
                "(CASE WHEN freight_kind = 'MTY' AND RIGHT(sparcsn4.ref_equip_type.nominal_length,2)!=20\n" +
                "AND sparcsn4.inv_unit_fcy_visit.time_load IS NULL\n" +
                "THEN 1 ELSE 0 END) AS bal_mty40\n" +
                "\n" +
                "\n" +
                "FROM sparcsn4.inv_unit\n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.gkey=sparcsn4.inv_unit_fcy_visit.actual_ob_cv\n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                "WHERE  sparcsn4.argo_carrier_visit.cvcvd_gkey='"+vvd_gkey+"') AS tmp";

        List resultList=SecondaryDBTemplate.query(sqlQuery,new ContainerHandlingReport());
        System.out.println("sql:"+sqlQuery);
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class ContainerHandlingReportByRotation implements RowMapper {

        @Override
        public HandlingReport mapRow(ResultSet rs, int rowNum) throws SQLException {

            HandlingReport handlingReport=new HandlingReport();
            handlingReport.setName(rs.getString("name"));
            handlingReport.setVvd_gkey(rs.getInt("vvd_gkey"));
            handlingReport.setIb_vyg(rs.getString("ib_vyg"));
            handlingReport.setOb_vyg(rs.getString("ob_vyg"));
            handlingReport.setArrived_date(rs.getTimestamp("arrived_date"));
            handlingReport.setDeparture_date(rs.getTimestamp("departure_date"));
            handlingReport.setShipping_agent(rs.getString("shipping_agent"));
            handlingReport.setBerth(rs.getString("berth"));
            return handlingReport;
        }
    }

    class ContainerHandlingReport implements RowMapper {

        @Override
        public HandlingReport mapRow(ResultSet rs, int rowNum) throws SQLException {

            HandlingReport handlingReport=new HandlingReport();
            handlingReport.setDisch_load20(rs.getInt("disch_load20"));
            handlingReport.setDisch_load40(rs.getInt("disch_load40"));
            handlingReport.setDisch_mty20(rs.getInt("disch_mty20"));
            handlingReport.setDisch_mty40(rs.getInt("disch_mty40"));
            handlingReport.setLoad_teus(rs.getInt("load_teus"));
            handlingReport.setMty_teus(rs.getInt("mty_teus"));
            handlingReport.setTot_disch_load20(rs.getInt("tot_disch_load20"));
            handlingReport.setTot_disch_load40(rs.getInt("tot_disch_load40"));
            handlingReport.setTot_disch_mty20(rs.getInt("tot_disch_mty40"));
            handlingReport.setTot_disch_mty40(rs.getInt("tot_disch_mty40"));
            handlingReport.setTot_disch_load_teus(rs.getInt("tot_disch_load_teus"));
            handlingReport.setTot_disch_mty_teus(rs.getInt("tot_disch_mty_teus"));
            handlingReport.setBal_load20(rs.getInt("bal_load20"));
            handlingReport.setBal_load40(rs.getInt("bal_load40"));

            handlingReport.setBal_mty20(rs.getInt("bal_mty20"));
            handlingReport.setBal_mty40(rs.getInt("bal_mty40"));
            handlingReport.setBal_load_teus(rs.getInt("bal_load_teus"));
            handlingReport.setBal_mty_teus(rs.getInt("bal_mty_teus"));
            return handlingReport;
        }
    }

}
