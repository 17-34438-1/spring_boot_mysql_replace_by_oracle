package com.datasoft.IgmMis.DAO;

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
public class FeederDischargeSummaryDAO {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    public List getVesselVisitInfo(String rotation) {
        String sqlVesselVisitInfo = "SELECT  r.name,DATE(vsl_vessel_visit_details.flex_date01) AS arr_dt\n" +
                "FROM sparcsn4.vsl_vessel_visit_details\n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped r  ON  sparcsn4.vsl_vessel_visit_details.bizu_gkey=r.gkey\n" +
                "WHERE sparcsn4.vsl_vessel_visit_details.ib_vyg='"+rotation+"'";
        List vesselVisitInfoList = secondaryDBTemplate.query(sqlVesselVisitInfo, new vesselVisitInfoMapper());
        List listAll = (List) vesselVisitInfoList.stream().collect(Collectors.toList());

        return listAll;
    }

    public class vesselInfo {
        private String name;
        private String arr_dt;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getArr_dt() { return arr_dt; }
        public void setArr_dt(String arr_dt) { this.arr_dt = arr_dt; }

    }
    class vesselVisitInfoMapper implements RowMapper {

        @Override
        public vesselInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            vesselInfo objVesselInfo = new vesselInfo();
            objVesselInfo.setName(rs.getString("name"));
            objVesselInfo.setArr_dt(rs.getString("arr_dt"));

            return objVesselInfo;
        }
    }





}
