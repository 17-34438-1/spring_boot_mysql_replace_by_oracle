package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportCommentsByShippingSectionOnExportVessel;
import com.datasoft.IgmMis.Model.ExportReport.ExportDateAndRotationWisePreAdvisedContainer;
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
public class ExportCommentsByShippingSectionOnExportVesselService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    public List CommentsByShippingSectionOnExportVessel(String fromdate, String todate){
        System.out.println("fromdate:"+fromdate);
        System.out.println("Todate:"+todate);

        String sqlQuery="SELECT sparcsn4.vsl_vessel_visit_details.vvd_gkey,sparcsn4.vsl_vessels.name,sparcsn4.vsl_vessel_visit_details.ib_vyg,sparcsn4.vsl_vessel_visit_details.ob_vyg,\n" +
                "LEFT(sparcsn4.argo_carrier_visit.phase,2) AS phase_num,SUBSTR(sparcsn4.argo_carrier_visit.phase,3) AS phase_str,sparcsn4.argo_visit_details.eta,sparcsn4.argo_visit_details.etd,sparcsn4.argo_carrier_visit.ata,sparcsn4.argo_carrier_visit.atd,ctmsmis.mis_exp_vvd.comments,ctmsmis.mis_exp_vvd.comments_by,ctmsmis.mis_exp_vvd.comments_time,ctmsmis.mis_exp_vvd.pre_comments,ctmsmis.mis_exp_vvd.pre_comments_time,\n" +
                "sparcsn4.ref_bizunit_scoped.id AS agent,\n" +
                "IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,sparcsn4.vsl_vessel_visit_details.flex_string03) AS berthop\n" +
                "FROM sparcsn4.argo_carrier_visit\n" +
                "INNER JOIN sparcsn4.argo_visit_details ON sparcsn4.argo_visit_details.gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_visit_details.gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN ctmsmis.mis_exp_vvd ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=ctmsmis.mis_exp_vvd.vvd_gkey\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.vsl_vessel_visit_details.bizu_gkey\n" +
                "WHERE DATE(ctmsmis.mis_exp_vvd.comments_time) BETWEEN '"+fromdate+"' AND '"+todate+"'\n" +
                "ORDER BY ctmsmis.mis_exp_vvd.comments_time DESC";

        System.out.println("result:"+sqlQuery);
        List resultList=SecondaryDBTemplate.query(sqlQuery,new CommentsByShippingSectionOnExportVessel());
        System.out.println("sql:"+resultList);
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class CommentsByShippingSectionOnExportVessel implements RowMapper {

        @Override
        public ExportCommentsByShippingSectionOnExportVessel mapRow(ResultSet rs, int rowNum) throws SQLException {

            ExportCommentsByShippingSectionOnExportVessel exportCommentsByShippingSectionOnExportVessel =new ExportCommentsByShippingSectionOnExportVessel();
            exportCommentsByShippingSectionOnExportVessel.setVvd_gkey(rs.getString("vvd_gkey"));
            exportCommentsByShippingSectionOnExportVessel.setName(rs.getString("name"));
            exportCommentsByShippingSectionOnExportVessel.setIb_vyg(rs.getString("ib_vyg"));
            exportCommentsByShippingSectionOnExportVessel.setOb_vyg(rs.getString("ob_vyg"));
            exportCommentsByShippingSectionOnExportVessel.setPhase_num(rs.getString("phase_num"));
            exportCommentsByShippingSectionOnExportVessel.setPhase_str(rs.getString("phase_str"));
            exportCommentsByShippingSectionOnExportVessel.setEta(rs.getTimestamp("eta"));
            exportCommentsByShippingSectionOnExportVessel.setEtd(rs.getTimestamp("etd"));

            exportCommentsByShippingSectionOnExportVessel.setAta(rs.getTimestamp("ata"));
            exportCommentsByShippingSectionOnExportVessel.setAtd(rs.getTimestamp("atd"));
            exportCommentsByShippingSectionOnExportVessel.setComments(rs.getString("comments"));
            exportCommentsByShippingSectionOnExportVessel.setComments_by(rs.getString("comments_by"));
            exportCommentsByShippingSectionOnExportVessel.setComments_time(rs.getTimestamp("comments_time"));
            exportCommentsByShippingSectionOnExportVessel.setPre_comments(rs.getString("pre_comments"));
            exportCommentsByShippingSectionOnExportVessel.setPre_comments_time(rs.getTimestamp("pre_comments_time"));
            exportCommentsByShippingSectionOnExportVessel.setAgent(rs.getString("agent"));
            exportCommentsByShippingSectionOnExportVessel.setBerthop(rs.getString("berthop"));
            return exportCommentsByShippingSectionOnExportVessel;
        }

    }

}