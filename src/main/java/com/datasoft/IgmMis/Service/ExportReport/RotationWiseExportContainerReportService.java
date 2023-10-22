package com.datasoft.IgmMis.Service.ExportReport;


import com.datasoft.IgmMis.Model.ExportReport.RotationWiseExportContainerReport;
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
public class RotationWiseExportContainerReportService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;


    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;



    public List getRotationWiseExportContainerReport(String fromdate, String todate){


//        String sqlQuery="SELECT sparcsn4.vsl_vessel_visit_details.ib_vyg AS vsl_visit_dtls_ib_vyg, \n" +
//                "sparcsn4.vsl_vessels.name AS v_name,\n" +
//                "\n" +
//                "sparcsn4.argo_carrier_visit.phase AS PHASE,\n" +
//                " sparcsn4.argo_carrier_visit.ata AS ata,\n" +
//                "  sparcsn4.argo_carrier_visit.atd AS atd,\n" +
//                "sparcsn4.vsl_vessel_visit_details.flex_string02 AS bop,\n" +
//                "COUNT(sparcsn4.inv_unit.gkey) AS exp_cont\n" +
//                "FROM sparcsn4.inv_unit \n" +
//                "INNER JOIN sparcsn4.inv_unit_fcy_visit  ON  sparcsn4.inv_unit_fcy_visit.unit_gkey = sparcsn4.inv_unit.gkey\n" +
//                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.inv_unit_fcy_visit.actual_ob_cv=sparcsn4.argo_carrier_visit.gkey \n" +
//                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
//                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
//                "WHERE sparcsn4.argo_carrier_visit.ata BETWEEN '"+fromdate+" 00:00:00' AND '"+todate+" 23:59:59'\n" +
//                "GROUP BY 1";



                String sqlQuery="SELECT vsl_vessel_visit_details.ib_vyg ,\n" +
                        "vsl_vessels.name ,\n" +
                        "argo_carrier_visit.phase ,\n" +
                        "argo_carrier_visit.ata,\n" +
                        "argo_carrier_visit.atd ,\n" +
                        "vsl_vessel_visit_details.flex_string02 ,\n" +
                        "COUNT(inv_unit.gkey) AS exp_cont\n" +
                        "FROM inv_unit \n" +
                        "INNER JOIN inv_unit_fcy_visit  ON  inv_unit_fcy_visit.unit_gkey = inv_unit.gkey\n" +
                        "INNER JOIN argo_carrier_visit ON inv_unit_fcy_visit.actual_ob_cv=argo_carrier_visit.gkey \n" +
                        "INNER JOIN vsl_vessel_visit_details ON vsl_vessel_visit_details.vvd_gkey=argo_carrier_visit.cvcvd_gkey\n" +
                        "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                        "WHERE to_char(argo_carrier_visit.ata,'YYYY-MM-DD')  BETWEEN '"+fromdate+"' and '"+todate+"'\n" +
                        "GROUP BY vsl_vessel_visit_details.ib_vyg,name,phase,ata,atd,vsl_vessel_visit_details.flex_string02   \n";

        System.out.println("result:"+sqlQuery);

        List resultList=OracleDbTemplate.query(sqlQuery,new get_rotation_WiseExport_ContainerReport());

        System.out.println("sql:"+resultList);

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class get_rotation_WiseExport_ContainerReport implements RowMapper {

        @Override
        public RotationWiseExportContainerReport mapRow(ResultSet rs, int rowNum) throws SQLException {

            RotationWiseExportContainerReport rotationWiseExportContainerReport =new RotationWiseExportContainerReport();
            rotationWiseExportContainerReport.setVsl_visit_dtls_ib_vyg(rs.getString("ib_vyg"));
            rotationWiseExportContainerReport.setV_name(rs.getString("name"));
            rotationWiseExportContainerReport.setPHASE(rs.getString("PHASE"));
            rotationWiseExportContainerReport.setAta(rs.getTimestamp("ata"));
            rotationWiseExportContainerReport.setAtd(rs.getTimestamp("atd"));
            rotationWiseExportContainerReport.setBop(rs.getString("bop"));
            rotationWiseExportContainerReport.setExp_cont(rs.getString("exp_cont"));

            return rotationWiseExportContainerReport;
        }
    }


}