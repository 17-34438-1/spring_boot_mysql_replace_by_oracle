package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.PODList;
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
public class PODService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    public List PODList() throws SQLException {

        String sqlContInfo="SELECT DISTINCT ref_unloc_code.id,ref_unloc_code.place_code,ref_unloc_code.place_name\n" +
                "FROM ref_routing_point\n" +
                "INNER JOIN ref_unloc_code ON ref_unloc_code.gkey=ref_routing_point.unloc_gkey\n" +
                "ORDER BY ref_unloc_code.id";
        List podListdata=secondaryDBTemplate.query(sqlContInfo,new PODListData());

        List listAll=(List)podListdata.stream().collect(Collectors.toList());
        return listAll;

    }
    class PODListData implements RowMapper{
        @Override
        public PODList mapRow(ResultSet rs, int rowNum)throws SQLException{
            PODList podlist = new PODList();

            podlist.setPlace_code(rs.getString("place_code"));
            podlist.setPlace_name(rs.getString("place_name"));
            return podlist;
        }
    }

    public List podListByPlaceCode(String place_code) throws SQLException{
        String sql="SELECT DISTINCT ref_unloc_code.id,ref_unloc_code.place_code,ref_unloc_code.place_name\n" +
                "FROM ref_routing_point\n" +
                "INNER JOIN ref_unloc_code ON ref_unloc_code.gkey=ref_routing_point.unloc_gkey\n" +
                "WHERE place_code='"+place_code+"'\n" +
                "ORDER BY ref_unloc_code.id";
        List listByPlace=secondaryDBTemplate.query(sql,new podListByPlaceCode());
        List listAllData=(List) listByPlace.stream().collect(Collectors.toList());

        return listAllData;
    }
    class podListByPlaceCode implements RowMapper{
        @Override
        public PODList mapRow(ResultSet rs, int rowNum)throws SQLException{
            PODList podlist=new PODList();

            podlist.setPlace_code(rs.getString("place_code"));
            podlist.setPlace_name(rs.getString("place_name"));
            return podlist;
        }
    }
}
