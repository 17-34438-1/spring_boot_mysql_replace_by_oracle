package com.datasoft.IgmMis.Service.ImportReport;

import com.datasoft.IgmMis.Model.ExportReport.YardBlock;
import com.datasoft.IgmMis.Model.ImportReport.ContainerEventHistory;
import com.datasoft.IgmMis.Service.ExportReport.YardBlockService;
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
public class ContainerEventHistoryService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

   /* public List<ContainerEventHistory> getContainerEventHistory(String cont_number){
        return containerEventHistoryRepository.getContainerEventHistory(cont_number);
    }

    */
   public List getContainerEventHistory( String cont_number) throws SQLException {

       String sqlContInfo = "SELECT sparcsn4.inv_unit.gkey,sparcsn4.inv_unit_fcy_visit.time_move,\n" +
               "sparcsn4.inv_unit_fcy_visit.time_in,sparcsn4.inv_unit_fcy_visit.time_out,\n" +
               "sparcsn4.inv_unit.category,sparcsn4.inv_unit.freight_kind,sparcsn4.inv_unit_fcy_visit.transit_state,\n" +
               "sparcsn4.inv_unit_fcy_visit.last_pos_name,sparcsn4.ref_bizunit_scoped.id AS mlo\n" +
               "FROM sparcsn4.inv_unit\n" +
               "INNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit_fcy_visit.unit_gkey=sparcsn4.inv_unit.gkey\n" +
               "INNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey=sparcsn4.inv_unit.line_op\n" +
               "WHERE sparcsn4.inv_unit.id='"+cont_number+"'\n"+ "ORDER BY 2 DESC";

       List containerEventHistoryList = secondaryDBTemplate.query(sqlContInfo, new ContainerEventHistoryList());
       List listAll = (List) containerEventHistoryList.stream().collect(Collectors.toList());

       return listAll;
   }

    class ContainerEventHistoryList implements RowMapper {

        @Override
        public ContainerEventHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
            ContainerEventHistory containerEventHistory = new ContainerEventHistory();

            containerEventHistory.setGkey(rs.getInt("gkey"));
            containerEventHistory.setTime_move(rs.getString("time_move"));
            //System.out.println(rs.getString("time_in"));
            containerEventHistory.setTime_in(rs.getString("time_in"));
            containerEventHistory.setTime_out(rs.getString("time_out"));
            containerEventHistory.setCategory(rs.getString("category"));
            containerEventHistory.setFreight_kind(rs.getString("freight_kind"));
            containerEventHistory.setTransit_state(rs.getString("transit_state"));
            containerEventHistory.setLast_pos_name(rs.getString("last_pos_name"));
            containerEventHistory.setMlo(rs.getString("mlo"));


            return containerEventHistory ;
        }
    }
}


