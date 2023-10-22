package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportContainerBlockReport;
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
public class ExportDateAndRotationWisePreAdvisedContainerService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;

    public List getDateAndRotationWisePreAdvisedContainer(String rotation,String fromdate){


        String sqlQuery="SELECT ctmsmis.mis_exp_unit_preadv_req.gkey AS gkey,mis_exp_unit_preadv_req.cont_id AS cont_id,mis_exp_unit_preadv_req.cont_mlo,\n" +
                "mis_exp_unit_preadv_req.cont_size AS cont_size,mis_exp_unit_preadv_req.isoType,'' AS loc,'' AS emtyDate,\n" +
                "mis_exp_unit_preadv_req.rotation,mis_exp_unit_preadv_req.cont_status,mis_exp_unit_preadv_req.pod,\n" +
                "goods_and_ctr_wt_kg,seal_no,last_update,cont_mlo,\n" +
                "(select code  from ctmsmis.offdoc where id= ctmsmis.mis_exp_unit_preadv_req.transOp) as offdock,\n" +
                "(SELECT NAME FROM sparcsn4.vsl_vessel_visit_details\n" +
                " INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                " WHERE ib_vyg=ctmsmis.mis_exp_unit_preadv_req.rotation) AS vsl_name\n" +
                "FROM  ctmsmis.mis_exp_unit_preadv_req \n" +
                "WHERE  mis_exp_unit_preadv_req.rotation='"+rotation+"' AND date(last_update)='"+fromdate+"'order by cont_mlo";

        System.out.println("result:"+sqlQuery);
        List resultList=SecondaryDBTemplate.query(sqlQuery,new DateAndRotationWisePreAdvisedContainer());
        System.out.println("sql:"+resultList);
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }


    class DateAndRotationWisePreAdvisedContainer implements RowMapper {

        @Override
        public ExportDateAndRotationWisePreAdvisedContainer mapRow(ResultSet rs, int rowNum) throws SQLException {

            ExportDateAndRotationWisePreAdvisedContainer exportDateAndRotationWisePreAdvisedContainer=new ExportDateAndRotationWisePreAdvisedContainer();exportDateAndRotationWisePreAdvisedContainer.setCont_id(rs.getString("cont_id"));
            exportDateAndRotationWisePreAdvisedContainer.setCont_size(rs.getString("cont_size"));
            exportDateAndRotationWisePreAdvisedContainer.setGoods_and_ctr_wt_kg(rs.getString("goods_and_ctr_wt_kg"));
            exportDateAndRotationWisePreAdvisedContainer.setSeal_no(rs.getString("seal_no"));
            exportDateAndRotationWisePreAdvisedContainer.setCont_mlo(rs.getString("cont_mlo"));
            exportDateAndRotationWisePreAdvisedContainer.setIsoType(rs.getString("isoType"));
            exportDateAndRotationWisePreAdvisedContainer.setOffdock(rs.getString("offdock"));
            exportDateAndRotationWisePreAdvisedContainer.setVsl_name(rs.getString("vsl_name"));
            exportDateAndRotationWisePreAdvisedContainer.setRotation(rs.getString("rotation"));
            exportDateAndRotationWisePreAdvisedContainer.setLast_update(rs.getTimestamp("last_update"));
            exportDateAndRotationWisePreAdvisedContainer.setCont_status(rs.getString("cont_status"));
            exportDateAndRotationWisePreAdvisedContainer.setPod(rs.getString("pod"));
            return exportDateAndRotationWisePreAdvisedContainer;
        }
    }


}