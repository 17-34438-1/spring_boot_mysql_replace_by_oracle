package com.datasoft.IgmMis.Service.ExportReport;


import com.datasoft.IgmMis.Model.ExportReport.RotationWiseExportContainerReport;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RotationWiseExportContainerReportService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;

    public List getRotationWiseExportContainerReport(String fromdate, String todate){


        String sqlQuery="SELECT sparcsn4.vsl_vessel_visit_details.ib_vyg AS vsl_visit_dtls_ib_vyg, \n" +
                "sparcsn4.vsl_vessels.name AS v_name,\n" +
                "\n" +
                "sparcsn4.argo_carrier_visit.phase AS PHASE,\n" +
                " sparcsn4.argo_carrier_visit.ata AS ata,\n" +
                "  sparcsn4.argo_carrier_visit.atd AS atd,\n" +
                "sparcsn4.vsl_vessel_visit_details.flex_string02 AS bop,\n" +
                "COUNT(sparcsn4.inv_unit.gkey) AS exp_cont\n" +
                "FROM sparcsn4.inv_unit \n" +
                "INNER JOIN sparcsn4.inv_unit_fcy_visit  ON  sparcsn4.inv_unit_fcy_visit.unit_gkey = sparcsn4.inv_unit.gkey\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.inv_unit_fcy_visit.actual_ob_cv=sparcsn4.argo_carrier_visit.gkey \n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "WHERE sparcsn4.argo_carrier_visit.ata BETWEEN '"+fromdate+" 00:00:00' AND '"+todate+" 23:59:59'\n" +
                "GROUP BY 1";

        System.out.println("result:"+sqlQuery);
        List resultList=SecondaryDBTemplate.query(sqlQuery,new get_rotation_WiseExport_ContainerReport());
        System.out.println("sql:"+resultList);
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class get_rotation_WiseExport_ContainerReport implements RowMapper {

        @Override
        public RotationWiseExportContainerReport mapRow(ResultSet rs, int rowNum) throws SQLException {

            RotationWiseExportContainerReport rotationWiseExportContainerReport =new RotationWiseExportContainerReport();
            rotationWiseExportContainerReport.setVsl_visit_dtls_ib_vyg(rs.getString("vsl_visit_dtls_ib_vyg"));
            rotationWiseExportContainerReport.setV_name(rs.getString("v_name"));
            rotationWiseExportContainerReport.setPHASE(rs.getString("PHASE"));
            rotationWiseExportContainerReport.setAta(rs.getTimestamp("ata"));
            rotationWiseExportContainerReport.setAtd(rs.getTimestamp("atd"));
            rotationWiseExportContainerReport.setBop(rs.getString("bop"));
            rotationWiseExportContainerReport.setExp_cont(rs.getString("exp_cont"));

            return rotationWiseExportContainerReport;
        }
    }


}