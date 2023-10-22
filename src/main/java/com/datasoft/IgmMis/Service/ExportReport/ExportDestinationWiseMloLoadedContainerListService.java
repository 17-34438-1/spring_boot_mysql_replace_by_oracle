package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ExportReport.ExportDestinationWiseMloLoadedContainerList;

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
public class ExportDestinationWiseMloLoadedContainerListService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate SecondaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    public Integer get_Destination_Wise_Mlo_Loaded_Container_vvdGkey(String rotation){
        String sqlQuery="";
        Integer vvdgkey=0;
        sqlQuery="SELECT vvd_gkey FROM sparcsn4.vsl_vessel_visit_details WHERE ib_vyg='"+rotation+"'";


        List<ExportDestinationWiseMloLoadedContainerList> resultList=SecondaryDBTemplate.query(sqlQuery,new ExportDestinationWiseMloLoadedContainerVvdGkey());

        ExportDestinationWiseMloLoadedContainerList exportDestinationWiseMloLoadedContainerList;
        for(int i=0;i<resultList.size();i++){
            exportDestinationWiseMloLoadedContainerList=resultList.get(i);
            vvdgkey=exportDestinationWiseMloLoadedContainerList.getVvd_gkey();
        }
        return vvdgkey;
    }

    class ExportDestinationWiseMloLoadedContainerVvdGkey implements RowMapper {

        @Override
        public ExportDestinationWiseMloLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportDestinationWiseMloLoadedContainerList exportDestinationWiseMloLoadedContainerList=new ExportDestinationWiseMloLoadedContainerList();
            exportDestinationWiseMloLoadedContainerList.setVvd_gkey(rs.getInt("vvd_gkey"));

            return exportDestinationWiseMloLoadedContainerList;
        }
    }



    public List getDestinationWiseMloLoadedContainerVessleInformation(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="SELECT vsl_vessels.name AS vsl_name,IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string02,\n" +
                "IFNULL(sparcsn4.vsl_vessel_visit_details.flex_string03,'')) AS berth_op,IFNULL(sparcsn4.argo_quay.id,'') AS berth,\n" +
                "DATE(sparcsn4.vsl_vessel_visit_details.published_eta) AS ata,\n" +
                "sparcsn4.argo_carrier_visit.atd FROM vsl_vessel_visit_details\n" +
                "INNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessel_berthings ON sparcsn4.vsl_vessel_berthings.vvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN sparcsn4.vsl_vessels ON sparcsn4.vsl_vessels.gkey=sparcsn4.vsl_vessel_visit_details.vessel_gkey\n" +
                "INNER JOIN sparcsn4.argo_quay ON sparcsn4.argo_quay.gkey=sparcsn4.vsl_vessel_berthings.quay\n" +
                "WHERE vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new ExportDestinationWiseMloLoadedContainerVesselInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    public List getDestinationWiseMloLoadedContainerInfo(Integer vvdgkey){
        String sqlQuery="";
        sqlQuery="SELECT ctmsmis.mis_exp_unit.gkey,CONCAT(SUBSTRING(ctmsmis.mis_exp_unit.cont_id,1,4),SUBSTRING(ctmsmis.mis_exp_unit.cont_id,5)) AS id, \n" +
                "RIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size,RIGHT(sparcsn4.ref_equip_type.nominal_height,2) AS height, \n" +
                "mis_exp_unit.cont_mlo AS mlo,sparcsn4.ref_bizunit_scoped.name AS mlo_name,ctmsmis.mis_exp_unit.craine_id,ctmsmis.mis_exp_unit.seal_no,cont_status AS freight_kind,\n" +
                "ctmsmis.mis_exp_unit.coming_from AS coming_from,ctmsmis.mis_exp_unit.pod,ctmsmis.mis_exp_unit.stowage_pos, \n" +
                "ctmsmis.mis_exp_unit.user_id,ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg AS goods_and_ctr_wt_kg,ctmsmis.mis_exp_unit.truck_no\n" +
                "FROM ctmsmis.mis_exp_unit \n" +
                "LEFT JOIN sparcsn4.inv_unit ON sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey  \n" +
                "INNER JOIN sparcsn4.ref_bizunit_scoped  ON sparcsn4.inv_unit.line_op = sparcsn4.ref_bizunit_scoped.gkey \n" +
                " \n" +
                "LEFT JOIN sparcsn4.inv_goods ON sparcsn4.inv_goods.gkey=sparcsn4.inv_unit.goods \n" +
                "LEFT JOIN sparcsn4.ref_commodity ON sparcsn4.ref_commodity.gkey=sparcsn4.inv_goods.commodity_gkey \n" +
                "INNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey \n" +
                "INNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey \n" +
                "INNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey \n" +
                "WHERE mis_exp_unit.vvd_gkey='"+vvdgkey+"' AND mis_exp_unit.preAddStat='0' AND snx_type=0 AND mis_exp_unit.delete_flag='0' ORDER BY mis_exp_unit.cont_mlo,cont_status\n" +
                "\n";
        List resultList=SecondaryDBTemplate.query(sqlQuery,new ExportDestinationWiseMloContainerInfo());

        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }

    class ExportDestinationWiseMloLoadedContainerVesselInfo implements RowMapper{

        @Override
        public ExportDestinationWiseMloLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportDestinationWiseMloLoadedContainerList exportDestinationWiseMloLoadedContainerList =new ExportDestinationWiseMloLoadedContainerList();
            exportDestinationWiseMloLoadedContainerList.setVsl_name(rs.getString("vsl_name"));
            exportDestinationWiseMloLoadedContainerList.setBerth_op(rs.getString("berth_op"));
            exportDestinationWiseMloLoadedContainerList.setBerth(rs.getString("berth"));
            exportDestinationWiseMloLoadedContainerList.setAta(rs.getDate("ata"));
            exportDestinationWiseMloLoadedContainerList.setAtd(rs.getTimestamp("atd"));

            return exportDestinationWiseMloLoadedContainerList;
        }
    }

    class ExportDestinationWiseMloContainerInfo implements RowMapper{

        @Override
        public ExportDestinationWiseMloLoadedContainerList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportDestinationWiseMloLoadedContainerList exportDestinationWiseMloLoadedContainerList =new ExportDestinationWiseMloLoadedContainerList();
            exportDestinationWiseMloLoadedContainerList.setId(rs.getString("id"));
            exportDestinationWiseMloLoadedContainerList.setHeight(rs.getString("height"));
            exportDestinationWiseMloLoadedContainerList.setFreight_kind(rs.getString("freight_kind"));
            exportDestinationWiseMloLoadedContainerList.setStowage_pos(rs.getString("stowage_pos"));
            exportDestinationWiseMloLoadedContainerList.setSeal_no(rs.getString("seal_no"));
            exportDestinationWiseMloLoadedContainerList.setGoods_and_ctr_wt_kg(rs.getString("goods_and_ctr_wt_kg"));
            exportDestinationWiseMloLoadedContainerList.setPod(rs.getString("pod"));
            exportDestinationWiseMloLoadedContainerList.setTruck_no(rs.getString("truck_no"));
            exportDestinationWiseMloLoadedContainerList.setComing_from(rs.getString("coming_from"));
            exportDestinationWiseMloLoadedContainerList.setCraine_id(rs.getString("craine_id"));

            return exportDestinationWiseMloLoadedContainerList;
        }
    }
}