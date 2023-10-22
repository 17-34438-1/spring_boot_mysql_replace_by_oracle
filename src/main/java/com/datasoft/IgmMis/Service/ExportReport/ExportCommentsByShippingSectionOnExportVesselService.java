package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportCommentsByShippingSectionOnExportVessel;
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

    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;


    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;


    public List CommentsByShippingSectionOnExportVessel(String fromdate, String todate){
        System.out.println("fromdate:"+fromdate);
        System.out.println("Todate:"+todate);

//        String sqlQuery="SELECT sparcsn4.vsl_vessel_visit_details.vvd_gkey,sparcsn4.vsl_vessels.name,sparcsn4.vsl_vessel_visit_details.ib_vyg,sparcsn4.vsl_vessel_visit_details.ob_vyg,\n" +
//                "LEFT(sparcsn4.argo_carrier_visit.phase,2) AS phase_num,SUBSTR(sparcsn4.argo_carrier_visit.phase,3) AS phase_str,sparcsn4.argo_visit_details.eta,sparcsn4.argo_visit_details.etd,sparcsn4.argo_carrier_visit.ata,sparcsn4.argo_carrier_visit.atd,ctmsmis.mis_exp_vvd.comments,ctmsmis.mis_exp_vvd.comments_by,ctmsmis.mis_exp_vvd.comments_time,ctmsmis.mis_exp_vvd.pre_comments,ctmsmis.mis_exp_vvd.pre_comments_time,\n" +
//                "sparcsn4.ref_bizunit_scoped.id AS agent,\n" +
//                "IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,sparcsn4.vsl_vessel_visit_details.flex_string03) AS berthop\n" +
//                "FROM sparcsn4.argo_carrier_visit\n" +
//                "INNER JOIN sparcsn4.argo_visit_details ON sparcsn4.argo_visit_details.gkey=sparcsn4.argo_carrier_visit.cvcvd_gkey\n" +
//                "INNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=sparcsn4.argo_visit_details.gkey\n" +
//                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
//                "INNER JOIN ctmsmis.mis_exp_vvd ON sparcsn4.vsl_vessel_visit_details.vvd_gkey=ctmsmis.mis_exp_vvd.vvd_gkey\n" +
//                "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.vsl_vessel_visit_details.bizu_gkey\n" +
//                "WHERE DATE(ctmsmis.mis_exp_vvd.comments_time) BETWEEN '"+fromdate+"' AND '"+todate+"'\n" +
//                "ORDER BY ctmsmis.mis_exp_vvd.comments_time DESC";


        String sqlQuery="\n" +
                "SELECT mis_exp_vvd.comments,mis_exp_vvd.comments_by,mis_exp_vvd.comments_time,mis_exp_vvd.pre_comments,mis_exp_vvd.pre_comments_time,mis_exp_vvd.vvd_gkey\n" +
                "FROM mis_exp_vvd\n" +
                "WHERE DATE(mis_exp_vvd.comments_time) BETWEEN '"+fromdate+"' AND '"+todate+"'\n" +
                "ORDER BY mis_exp_vvd.comments_time DESC\n";


        List resultList=primaryDBTemplate.query(sqlQuery,new CommentsByShippingSectionOnExportVessel());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class CommentsByShippingSectionOnExportVessel implements RowMapper {

        @Override
        public ExportCommentsByShippingSectionOnExportVessel mapRow(ResultSet rs, int rowNum) throws SQLException {

            ExportCommentsByShippingSectionOnExportVessel exportCommentsByShippingSectionOnExportVessel =new ExportCommentsByShippingSectionOnExportVessel();

            String vvd_gkey=rs.getString("vvd_gkey");

            exportCommentsByShippingSectionOnExportVessel.setVvd_gkey(rs.getString("vvd_gkey"));

            String Query="SELECT vsl_vessel_visit_details.vvd_gkey,vsl_vessels.name,vsl_vessel_visit_details.ib_vyg,vsl_vessel_visit_details.ob_vyg,\n" +
                    "SUBSTR(argo_carrier_visit.phase, 3, LENGTH( argo_carrier_visit.phase)) AS phase_num,SUBSTR(argo_carrier_visit.phase,3) AS phase_str,argo_visit_details.eta,argo_visit_details.etd,argo_carrier_visit.ata,argo_carrier_visit.atd,\n" +
                    "ref_bizunit_scoped.id AS agent,\n" +
                    "COALESCE(vsl_vessel_visit_details.flex_string02,vsl_vessel_visit_details.flex_string03) AS berthop\n" +
                    "FROM argo_carrier_visit\n" +
                    "INNER JOIN argo_visit_details ON argo_visit_details.gkey=argo_carrier_visit.cvcvd_gkey\n" +
                    "INNER JOIN vsl_vessel_visit_details ON vsl_vessel_visit_details.vvd_gkey=argo_visit_details.gkey\n" +
                    "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                    "INNER JOIN ref_bizunit_scoped ON ref_bizunit_scoped.gkey=vsl_vessel_visit_details.bizu_gkey\n" +
                    "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvd_gkey+"'";

                List<ExportCommentsByShippingSectionOnExportVessel> resultList=OracleDbTemplate.query(Query,new export_vessel_info());
                for(int i=0;i<resultList.size();i++){
                ExportCommentsByShippingSectionOnExportVessel exportCommentsByShippingSectionOnExportVessel1;
                exportCommentsByShippingSectionOnExportVessel1=resultList.get(i);
                String vsl_name=exportCommentsByShippingSectionOnExportVessel1.getName();
                exportCommentsByShippingSectionOnExportVessel.setName(exportCommentsByShippingSectionOnExportVessel1.getName());
                exportCommentsByShippingSectionOnExportVessel.setIb_vyg(exportCommentsByShippingSectionOnExportVessel1.getIb_vyg());
                exportCommentsByShippingSectionOnExportVessel.setOb_vyg(exportCommentsByShippingSectionOnExportVessel1.getOb_vyg());
                exportCommentsByShippingSectionOnExportVessel.setPhase_num(exportCommentsByShippingSectionOnExportVessel1.getPhase_num());
                exportCommentsByShippingSectionOnExportVessel.setPhase_str(exportCommentsByShippingSectionOnExportVessel1.getPhase_str());
                exportCommentsByShippingSectionOnExportVessel.setEta(exportCommentsByShippingSectionOnExportVessel1.getEta());
                exportCommentsByShippingSectionOnExportVessel.setEtd(exportCommentsByShippingSectionOnExportVessel1.getEtd());
                exportCommentsByShippingSectionOnExportVessel.setAta(exportCommentsByShippingSectionOnExportVessel1.getAta());
                exportCommentsByShippingSectionOnExportVessel.setAtd(exportCommentsByShippingSectionOnExportVessel1.getAtd());
                exportCommentsByShippingSectionOnExportVessel.setAgent(exportCommentsByShippingSectionOnExportVessel1.getAgent());
                exportCommentsByShippingSectionOnExportVessel.setBerthop(exportCommentsByShippingSectionOnExportVessel1.getBerthop());

            }

            exportCommentsByShippingSectionOnExportVessel.setComments(rs.getString("comments"));
            exportCommentsByShippingSectionOnExportVessel.setComments_by(rs.getString("comments_by"));
            exportCommentsByShippingSectionOnExportVessel.setComments_time(rs.getTimestamp("comments_time"));
            exportCommentsByShippingSectionOnExportVessel.setPre_comments(rs.getString("pre_comments"));
            exportCommentsByShippingSectionOnExportVessel.setPre_comments_time(rs.getTimestamp("pre_comments_time"));
            return exportCommentsByShippingSectionOnExportVessel;
        }

    }


    class export_vessel_info implements RowMapper {

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
            exportCommentsByShippingSectionOnExportVessel.setAgent(rs.getString("agent"));
            exportCommentsByShippingSectionOnExportVessel.setBerthop(rs.getString("berthop"));




            return exportCommentsByShippingSectionOnExportVessel;
        }

    }



}