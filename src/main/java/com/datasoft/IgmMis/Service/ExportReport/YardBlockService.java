package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.YardBlock;
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
public class YardBlockService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;



    public List yardList() throws SQLException {

        String sqlContInfo = "SELECT * FROM yard_block ORDER BY terminal ASC";

        List yardList = primaryDBTemplate.query(sqlContInfo, new yardBlockList());
        List listAll = (List) yardList.stream().collect(Collectors.toList());

        return listAll;
    }

    class yardBlockList implements RowMapper {

        @Override
        public YardBlock mapRow(ResultSet rs, int rowNum) throws SQLException {
            YardBlock yardBlock = new YardBlock();

            yardBlock.setId(rs.getLong("id"));
            yardBlock.setTerminal(rs.getString("terminal"));
            yardBlock.setBlock_unit(rs.getString("block_unit"));
            yardBlock.setBlock(rs.getString("block"));
            yardBlock.setBlock_cpa(rs.getString("block_cpa"));
            yardBlock.setAmount(rs.getString("amount"));
            yardBlock.setCapacity_teus(rs.getString("capacity_teus"));
            yardBlock.setCapacity_feus(rs.getString("capacity_feus"));

            return yardBlock;
        }
    }
}
