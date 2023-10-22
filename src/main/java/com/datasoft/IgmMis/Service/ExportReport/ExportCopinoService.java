package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ImportReport.ExportCopino;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportCopinoService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;


    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;


    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;


    public List CopinoData(String rotation)throws SQLException{
        String Rotation=rotation.replace("_","/");
        System.out.println(Rotation);
        String sql="SELECT NAME FROM vsl_vessels INNER JOIN\n" +
                "vsl_vessel_visit_details ON\n" +
                "vsl_vessel_visit_details.vessel_gkey=vsl_vessels.gkey\n" +
                "WHERE vsl_vessel_visit_details.ib_vyg='"+Rotation+"'";
        System.out.println("query :"+ sql);

        List Copino=OracleDbTemplate.query(sql,new CopinoData());
        List listAll=(List) Copino.stream().collect(Collectors.toList());
        System.out.println("length: "+listAll.size());
        return listAll;
    }
    class CopinoData implements RowMapper{

        @Override
        public ExportCopino mapRow(ResultSet rs,int rowNum)throws SQLException{

            ExportCopino exportCopino=new ExportCopino();
            exportCopino.setNAME(rs.getString("name"));

            return exportCopino;

        }
    }

    public List ExportCopinoData(String rotation)throws SQLException{
        String import_Rotation=rotation.replace("_","/");
        System.out.println(import_Rotation);
        String sql="SELECT cont_id,cont_size,cont_height,isoType,cont_mlo,cont_status,goods_and_ctr_wt_kg,seal_no \n" +
                "FROM mis_exp_unit_preadv_req WHERE rotation='"+import_Rotation+"' ORDER BY 1";
        List CopinoData=primaryDBTemplate.query(sql,new ExportCopinoData());
        List listAll=(List) CopinoData.stream().collect(Collectors.toList());
        return listAll;
    }
    class ExportCopinoData implements RowMapper{
        @Override
        public ExportCopino mapRow(ResultSet rs,int rowNum)throws SQLException{
            ExportCopino exportCopino=new ExportCopino();
            exportCopino.setCont_id(rs.getString("cont_id"));
            exportCopino.setCont_size(rs.getString("cont_size"));
            exportCopino.setCont_height(rs.getString("cont_height"));
            exportCopino.setIsoType(rs.getString("isoType"));
            exportCopino.setCont_mlo(rs.getString("cont_mlo"));
            exportCopino.setCont_status(rs.getString("cont_status"));
            exportCopino.setGoods_and_ctr_wt_kg(rs.getString("goods_and_ctr_wt_kg"));
            exportCopino.setSeal_no(rs.getString("seal_no"));
            return exportCopino;
        }
    }
}
