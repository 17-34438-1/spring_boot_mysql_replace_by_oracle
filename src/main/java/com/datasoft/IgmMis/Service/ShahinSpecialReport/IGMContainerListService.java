package com.datasoft.IgmMis.Service.ShahinSpecialReport;

import com.datasoft.IgmMis.Model.ShahinSpecialReport.IGMCotainerListMainModel;
import com.datasoft.IgmMis.Model.ShahinSpecialReport.IgmContainerListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class IGMContainerListService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    public List IGMContainerList(String rotation, Integer searchType){
        List<IGMCotainerListMainModel> resultList=new ArrayList<>();
       // IGMCotainerListMainModel resultModel=new IGMCotainerListMainModel();
        List<IgmContainerListModel> mainQueryList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="SELECT igm_detail_id2,mlocode,cont_number,id,Line_No,BL_No,Notify_name,Submitee_Org_Id,cont_size,place_of_unloading,cont_weight,\n" +
                "cont_seal_number,cont_status,cont_height,cont_iso_type,cont_type,commudity_desc,cont_vat,off_dock_id,cont_imo,cont_un,Cont_gross_weight,RType,\n" +
                "Organization_Name,Description_of_Goods,port_status,TYPE,off_dock_name,symbol FROM \n" +
                "(SELECT '' AS igm_detail_id2,mlocode,cont_number, igm_detail_container.id,igm_details.Line_No,igm_details.BL_No,igm_details.Notify_name,igm_details.Submitee_Org_Id,\n" +
                "cont_size,place_of_unloading,cont_weight, cont_seal_number,cont_status,cont_height,cont_iso_type,cont_type,commudity_desc, cont_vat,off_dock_id,\n" +
                "cont_imo,cont_un,Cont_gross_weight,'D' AS RType, Organization_Name,igm_details.Description_of_Goods,igm_detail_container.port_status,'D' AS TYPE, \n" +
                "(SELECT Organization_Name FROM organization_profiles WHERE organization_profiles.id=igm_detail_container.off_dock_id) AS off_dock_name,\n" +
                "IF((SELECT DISTINCT cont_number FROM igm_detail_container  abc WHERE abc.cont_number=igm_detail_container.cont_number  \n" +
                "AND igm_details.Import_Rotation_No='"+rotation+"' AND  igm_detail_consigneetabs.igm_detail_id IS NOT NULL AND igm_supplimentary_detail.igm_detail_id IS NULL )>0,'S','') AS symbol \n" +
                "FROM igm_details LEFT JOIN igm_detail_container ON igm_details.id=igm_detail_container.igm_detail_id LEFT JOIN organization_profiles \n" +
                "ON igm_details.Submitee_Org_Id=organization_profiles.id LEFT JOIN commudity_detail ON igm_detail_container.commudity_code=commudity_detail.commudity_code \n" +
                "LEFT JOIN igm_supplimentary_detail ON igm_supplimentary_detail.igm_detail_id=igm_details.id LEFT JOIN igm_detail_consigneetabs \n" +
                "ON igm_detail_consigneetabs.igm_detail_id=igm_details.id  WHERE igm_details.Import_Rotation_No='"+rotation+"'  )\n" +
                "AS tbl\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT igm_detail_id2,mlocode,cont_number,id,Line_No,BL_No,Notify_name,Submitee_Org_Id,cont_size,place_of_unloading,cont_weight,cont_seal_number,cont_status,\n" +
                "cont_height,cont_iso_type,cont_type,commudity_desc,cont_vat,off_dock_id,cont_imo,cont_un,Cont_gross_weight,RType,Organization_Name,\n" +
                "Description_of_Goods,port_status,TYPE,off_dock_name,symbol FROM \n" +
                "(SELECT igm_detail_id AS igm_detail_id2,(SELECT mlocode FROM igm_details WHERE igm_details.id=igm_supplimentary_detail.igm_detail_id) AS mlocode, \n" +
                "cont_number, igm_sup_detail_container.id,Line_No,igm_supplimentary_detail.BL_No,igm_supplimentary_detail.Notify_name, Submitee_Org_Id,(SELECT cont_size FROM igm_detail_container \n" +
                "WHERE igm_detail_container.igm_detail_id=igm_supplimentary_detail.igm_detail_id AND igm_detail_container.cont_number=igm_sup_detail_container.cont_number) \n" +
                "AS cont_size,'' AS place_of_unloading,cont_weight, cont_seal_number,cont_status,(SELECT cont_height FROM igm_detail_container WHERE \n" +
                "igm_detail_container.igm_detail_id=igm_supplimentary_detail.igm_detail_id AND igm_detail_container.cont_number=igm_sup_detail_container.cont_number) AS cont_height,\n" +
                "(SELECT cont_iso_type FROM igm_detail_container  WHERE igm_detail_container.igm_detail_id=igm_supplimentary_detail.igm_detail_id \n" +
                "AND igm_detail_container.cont_number=igm_sup_detail_container.cont_number) AS cont_iso_type,cont_type,commudity_desc, cont_vat,off_dock_id,\n" +
                "cont_imo,cont_un,Cont_gross_weight,'S' AS RType,Organization_Name,igm_supplimentary_detail.Description_of_Goods,\n" +
                "igm_sup_detail_container.port_status,'s' AS TYPE, (SELECT Organization_Name FROM organization_profiles WHERE \n" +
                "organization_profiles.id=igm_sup_detail_container.off_dock_id) AS off_dock_name,'' AS symbol FROM igm_supplimentary_detail \n" +
                "INNER JOIN igm_sup_detail_container ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id LEFT JOIN organization_profiles \n" +
                "ON igm_supplimentary_detail.Submitee_Org_Id=organization_profiles.id LEFT JOIN commudity_detail ON igm_sup_detail_container.commudity_code=commudity_detail.commudity_code \n" +
                "WHERE igm_supplimentary_detail.Import_Rotation_No='"+rotation+"' ) AS tbl";

        mainQueryList=primaryDBTemplate.query(sqlQuery,new IGMContainerListService.MainQuery());
        String con_no="";
        Integer sl=0;
        Integer sl2=0;
        Integer sl3=0;
        Integer sl4=0;
        Integer sl5=0;
        Integer sl6=0;
        for(int i=0;i<mainQueryList.size();i++){
            IGMCotainerListMainModel resultModel=new IGMCotainerListMainModel();
            String contno="";
            String igm_detail_id2="";
            contno=mainQueryList.get(i).getCont_number();
            igm_detail_id2=mainQueryList.get(i).getIgm_detail_id2();
            if (!(con_no.equals(mainQueryList.get(i).getCont_number())))
            {
                sl=sl + 1;
            }
            double container_height=0.0;
            if(mainQueryList.get(i).getCont_height()==8 || mainQueryList.get(i).getCont_height()==8.1 || mainQueryList.get(i).getCont_height()==8.2 || mainQueryList.get(i).getCont_height()==8.3 || mainQueryList.get(i).getCont_height()==8.4 || mainQueryList.get(i).getCont_height()==8.5)	{

                 container_height=8.6;
            }
            else
            {
                 container_height=mainQueryList.get(i).getCont_height();
            }

           String sql_str="";
            List<IgmContainerListModel> sql_strList=new ArrayList();
            sql_str="SELECT sparcsn4.inv_unit.id,sparcsn4.inv_unit.gkey, category, freight_kind,RIGHT(sparcsn4.ref_equip_type.nominal_length,2) AS size,\n" +
                    "\t\t\t\t\t\t\t\tRIGHT(sparcsn4.ref_equip_type.nominal_height,2) AS height, ref_bizunit_scoped.id as mlo\n" +
                    "\t\t\t\t\t\t\t\tFROM sparcsn4.inv_unit \n" +
                    "\t\t\t\t\t\t\t\tINNER JOIN sparcsn4.inv_unit_equip ON sparcsn4.inv_unit_equip.unit_gkey=inv_unit.gkey\n" +
                    "\t\t\t\t\t\t\t\tINNER JOIN sparcsn4.ref_equipment ON sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                    "\t\t\t\t\t\t\t\tINNER JOIN sparcsn4.ref_equip_type ON sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                    "\t\t\t\t\t\t\t\tINNER JOIN sparcsn4.inv_unit_fcy_visit ON sparcsn4.inv_unit.gkey=sparcsn4.inv_unit_fcy_visit.unit_gkey \n" +
                    "\t\t\t\t\t\t\t\tINNER JOIN sparcsn4.argo_carrier_visit ON sparcsn4.inv_unit_fcy_visit.actual_ib_cv=sparcsn4.argo_carrier_visit.gkey\n" +
                    "\t\t\t\t\t\t\t\tINNER JOIN sparcsn4.vsl_vessel_visit_details ON sparcsn4.argo_carrier_visit.cvcvd_gkey=sparcsn4.vsl_vessel_visit_details.vvd_gkey\n" +
                    "\t\t\t\t\t\t\t\tINNER JOIN sparcsn4.ref_bizunit_scoped ON sparcsn4.ref_bizunit_scoped.gkey= inv_unit.line_op\n" +
                    "\t\t\t\t\t\t\t\tWHERE sparcsn4.inv_unit.id='"+contno+"' AND sparcsn4.vsl_vessel_visit_details.ib_vyg='"+rotation+"'";
            sql_strList=secondaryDBTemplate.query(sql_str,new IGMContainerListService.SqlStr());
            String freight_kind="";
            //Integer height=0;
          //  Integer size=0;
            Double  height=0.0;
            Double size=0.0;
            String mlo="";
            String category="";
            String containerNo="";
            Integer tag=0;
            if(sql_strList.size()>0){
                if(sql_strList.get(0).getFreight_kind().equals("MTY")){
                    freight_kind="ETY";
                }

                else{
                    freight_kind=sql_strList.get(0).getFreight_kind();
                }

                height=sql_strList.get(0).getHeight();
                size=sql_strList.get(0).getSize();
                mlo=""+sql_strList.get(0).getMlo();
                category=""+sql_strList.get(0).getCategory();
                containerNo=""+sql_strList.get(0).getInv_unit_id();
                tag=0;
            }
            else {
                freight_kind="Container Not Found";
                freight_kind="Container Not Found";
                height=0.0;
                size=0.0;
                mlo="Container Not Found";
                category="Container Not Found";
                containerNo="Container Not Found";
                tag=1;
            }
            String portCategory="IMPRT";
            List<IgmContainerListModel> sql22List=new ArrayList<>();
            String sql22="";
            Integer isocode=0;
            sql22="select ref_equip_type.id from inv_unit\n" +
                    "\t\t\t\t\t\t\t\tINNER JOIN sparcsn4.inv_unit_equip ON inv_unit.gkey=inv_unit_equip.unit_gkey \n" +
                    "\t\t\t\t\t\t\t\tINNER JOIN sparcsn4.ref_equipment ON inv_unit_equip.eq_gkey=ref_equipment.gkey\n" +
                    "\t\t\t\t\t\t\t\tINNER JOIN sparcsn4.ref_equip_type ON ref_equipment.eqtyp_gkey=ref_equip_type.gkey \n" +
                    "\t\t\t\t\t\t\t\twhere inv_unit.id='"+contno+"' limit 1";
            sql22List=secondaryDBTemplate.query(sql22,new IGMContainerListService.Sql22());
            if(sql22List.size()>0){
                isocode=sql22List.get(0).getRef_equip_type_id();
            }

            if(searchType==1){
                if(mainQueryList.get(i).getSymbol().equals("S")) {
                    resultModel.setSymbolBgcolor("lightgreen");
                    resultModel.setSymblolClass("");
                }
                else {
                    resultModel.setSymbolBgcolor("");
                    resultModel.setSymblolClass("gridLight");
                }

                resultModel.setSl(sl);
                resultModel.setOrganization_Name(mainQueryList.get(i).getOrganization_Name());


                if(!mainQueryList.get(i).getMlocode().equals(mlo)){
                    resultModel.setMloBgcolor("pink");
                    resultModel.setMlocode(""+mainQueryList.get(i).getMlocode());
                    resultModel.setMlo(""+mlo);
                }
                else{
                    resultModel.setMloBgcolor("");
                    resultModel.setMlocode(mainQueryList.get(i).getMlocode());
                    resultModel.setMlo("");

                }
                resultModel.setLine_No(mainQueryList.get(i).getLine_No());
                resultModel.setBL_No(mainQueryList.get(i).getBL_No());
                resultModel.setNotify_name(mainQueryList.get(i).getNotify_name());
                resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                resultModel.setDescription_of_Goods(mainQueryList.get(i).getDescription_of_Goods());

                if(!(String.valueOf(mainQueryList.get(i).getCont_size()).equals(size.toString()))){
                    resultModel.setCont_size(""+mainQueryList.get(i).getCont_size());
                    resultModel.setSize(""+size);
                    resultModel.setSizeBgcolor("pink");

                }
                else{
                    resultModel.setCont_size(""+mainQueryList.get(i).getCont_size());
                    resultModel.setSizeBgcolor("");
                    resultModel.setSize("");
                }
                if(container_height!=(height/10)){
                    resultModel.setContainerHeight(""+container_height);
                    resultModel.setHeight(""+height);
                    resultModel.setContHeightBgcolor("pink");

                }
                else{
                    resultModel.setContainerHeight(""+container_height);
                    resultModel.setContHeightBgcolor("");
                    resultModel.setHeight("");

                }
                if(!(mainQueryList.get(i).getCont_iso_type().trim().equals(isocode.toString().trim()))){
                    resultModel.setCont_iso_type(""+mainQueryList.get(i).getCont_iso_type());
                    resultModel.setIsoCode(""+isocode);
                    resultModel.setContIsoTypeBgcolor("pink");

                }
                else{
                    resultModel.setCont_iso_type(mainQueryList.get(i).getCont_iso_type());
                    resultModel.setContIsoTypeBgcolor("");
                    resultModel.setIsoCode("");

                }
                if(!portCategory.equals(category)){
                    resultModel.setPortCategory(""+portCategory);
                    resultModel.setCategory(""+category);
                    resultModel.setContIsoTypeBgcolor("pink");

                }
                else{
                    resultModel.setPortCategory(portCategory);
                    resultModel.setContIsoTypeBgcolor("");
                    resultModel.setCategory("");

                }
                resultModel.setCont_gross_weight(mainQueryList.get(i).getCont_gross_weight());

                if(mainQueryList.get(i).getTYPE().equals("S") && mainQueryList.get(i).getCont_status().equals("LCL")){
                    resultModel.setCont_weight(mainQueryList.get(i).getCont_weight());
                }
                else{
                    resultModel.setCont_weight(mainQueryList.get(i).getCont_weight());
                }
                resultModel.setCont_seal_number(mainQueryList.get(i).getCont_seal_number());


                if(!(mainQueryList.get(i).getCont_status().trim().substring(0,3).equals(freight_kind))){
                    resultModel.setCont_status(""+mainQueryList.get(i).getCont_status().trim().substring(0,3));
                    resultModel.setFreight_kind(""+freight_kind);
                    resultModel.setContainerStatusBgColor("pink");

                }
                else{
                    resultModel.setCont_status(mainQueryList.get(i).getCont_status());
                    resultModel.setContainerStatusBgColor("");
                    resultModel.setFreight_kind("");
                }
                resultModel.setCont_type(mainQueryList.get(i).getCont_type());
                resultModel.setCommudity_desc(mainQueryList.get(i).getCommudity_desc());
                resultModel.setCont_vat(mainQueryList.get(i).getCont_vat());
                resultModel.setOff_dock_name(mainQueryList.get(i).getOff_dock_name());
                resultModel.setCont_imo(mainQueryList.get(i).getCont_imo());
                resultModel.setCont_un(mainQueryList.get(i).getCont_un());
                resultModel.setState(searchType);
                con_no=mainQueryList.get(i).getCont_number();

                resultList.add(resultModel);
            }

            if(searchType==2){
                if(!(mainQueryList.get(i).getCont_status().trim().substring(0,3).equals(freight_kind))){
                    sl2++;
                    resultModel.setSl(sl2);
                    resultModel.setOrganization_Name(mainQueryList.get(i).getOrganization_Name());
                    resultModel.setMlocode(""+mainQueryList.get(i).getMlocode());
                    resultModel.setLine_No(mainQueryList.get(i).getLine_No());
                    resultModel.setBL_No(mainQueryList.get(i).getBL_No());
                    resultModel.setNotify_name(mainQueryList.get(i).getNotify_name());
                    resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                    resultModel.setDescription_of_Goods(mainQueryList.get(i).getDescription_of_Goods());
                    resultModel.setCont_size(""+mainQueryList.get(i).getCont_size());
                    resultModel.setContainerHeight(""+container_height);
                    resultModel.setCont_iso_type(""+mainQueryList.get(i).getCont_iso_type());
                    resultModel.setPortCategory(""+portCategory);
                    resultModel.setCont_gross_weight(mainQueryList.get(i).getCont_gross_weight());
                    resultModel.setCont_seal_number(mainQueryList.get(i).getCont_seal_number());
                    if(!(mainQueryList.get(i).getCont_status().trim().substring(0,3).equals(freight_kind))){
                        resultModel.setCont_status(""+mainQueryList.get(i).getCont_status().trim().substring(0,3));
                        resultModel.setFreight_kind(""+freight_kind);
                        resultModel.setContainerStatusBgColor("pink");

                    }
                    else{
                        resultModel.setCont_status(mainQueryList.get(i).getCont_status());
                        resultModel.setContainerStatusBgColor("");
                        resultModel.setFreight_kind("");
                    }
                    resultModel.setCont_type(mainQueryList.get(i).getCont_type());
                    resultModel.setCommudity_desc(mainQueryList.get(i).getCommudity_desc());
                    resultModel.setCont_vat(mainQueryList.get(i).getCont_vat());
                    resultModel.setOff_dock_name(mainQueryList.get(i).getOff_dock_name());
                    resultModel.setCont_imo(mainQueryList.get(i).getCont_imo());
                    resultModel.setCont_un(mainQueryList.get(i).getCont_un());
                    resultModel.setState(searchType);
                    con_no=mainQueryList.get(i).getCont_number();

                    resultList.add(resultModel);

                }
            }

            if(searchType==3){
                if(!(String.valueOf(mainQueryList.get(i).getCont_size()).equals(size.toString()))){
                    sl2++;
                    resultModel.setSl(sl2);
                    resultModel.setOrganization_Name(mainQueryList.get(i).getOrganization_Name());
                    resultModel.setMlocode(""+mainQueryList.get(i).getMlocode());
                    resultModel.setLine_No(mainQueryList.get(i).getLine_No());
                    resultModel.setBL_No(mainQueryList.get(i).getBL_No());
                    resultModel.setNotify_name(mainQueryList.get(i).getNotify_name());
                    resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                    resultModel.setDescription_of_Goods(mainQueryList.get(i).getDescription_of_Goods());
                    resultModel.setCont_size(""+mainQueryList.get(i).getCont_size());
                    resultModel.setContainerHeight(""+container_height);
                    resultModel.setCont_iso_type(""+mainQueryList.get(i).getCont_iso_type());
                    if(!portCategory.equals(category)){
                        resultModel.setPortCategory(""+portCategory);
                        resultModel.setCategory(""+category);
                        resultModel.setContIsoTypeBgcolor("pink");

                    }
                    else{
                        resultModel.setPortCategory(""+portCategory);
                        resultModel.setCategory("");
                        resultModel.setContIsoTypeBgcolor("");

                    }
                    resultModel.setCont_gross_weight(mainQueryList.get(i).getCont_gross_weight());
                    resultModel.setCont_seal_number(mainQueryList.get(i).getCont_seal_number());
                    resultModel.setCont_status(mainQueryList.get(i).getCont_status());
                    resultModel.setCont_type(mainQueryList.get(i).getCont_type());
                    resultModel.setCommudity_desc(mainQueryList.get(i).getCommudity_desc());
                    resultModel.setCont_vat(mainQueryList.get(i).getCont_vat());
                    resultModel.setOff_dock_name(mainQueryList.get(i).getOff_dock_name());
                    resultModel.setCont_imo(mainQueryList.get(i).getCont_imo());
                    resultModel.setCont_un(mainQueryList.get(i).getCont_un());
                    resultModel.setState(searchType);
                    con_no=mainQueryList.get(i).getCont_number();

                    resultList.add(resultModel);


                }

            }
            if(searchType==4){

                if(mainQueryList.get(i).getCont_height()!=(height/10)){
                    sl4++;
                    resultModel.setSl(sl4);
                    resultModel.setOrganization_Name(mainQueryList.get(i).getOrganization_Name());
                    resultModel.setMlocode(""+mainQueryList.get(i).getMlocode());
                    resultModel.setLine_No(mainQueryList.get(i).getLine_No());
                    resultModel.setBL_No(mainQueryList.get(i).getBL_No());
                    resultModel.setNotify_name(mainQueryList.get(i).getNotify_name());
                    resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                    resultModel.setDescription_of_Goods(mainQueryList.get(i).getDescription_of_Goods());
                    resultModel.setCont_size(""+mainQueryList.get(i).getCont_size());
                    if(container_height!=(height/10)){
                        resultModel.setContainerHeight(""+container_height);
                        resultModel.setHeight(""+height);
                        resultModel.setContHeightBgcolor("pink");

                    }
                    else{
                        resultModel.setContainerHeight(""+container_height);
                        resultModel.setHeight("");
                        resultModel.setContHeightBgcolor("");

                    }
                    resultModel.setCont_iso_type(""+mainQueryList.get(i).getCont_iso_type());
                    resultModel.setPortCategory(""+portCategory);
                    resultModel.setCont_gross_weight(mainQueryList.get(i).getCont_gross_weight());
                    resultModel.setCont_seal_number(mainQueryList.get(i).getCont_seal_number());
                    resultModel.setCont_status(mainQueryList.get(i).getCont_status());
                    resultModel.setCont_type(mainQueryList.get(i).getCont_type());
                    resultModel.setCommudity_desc(mainQueryList.get(i).getCommudity_desc());
                    resultModel.setCont_vat(mainQueryList.get(i).getCont_vat());
                    resultModel.setOff_dock_name(mainQueryList.get(i).getOff_dock_name());
                    resultModel.setCont_imo(mainQueryList.get(i).getCont_imo());
                    resultModel.setCont_un(mainQueryList.get(i).getCont_un());
                    resultModel.setState(searchType);
                    con_no=mainQueryList.get(i).getCont_number();

                    resultList.add(resultModel);


                }

            }

            if(searchType==5){
                if(!mainQueryList.get(i).getMlocode().equals(mlo)){
                    sl5++;
                    resultModel.setSl(sl5);
                    resultModel.setOrganization_Name(mainQueryList.get(i).getOrganization_Name());
                    if(!mainQueryList.get(i).getMlocode().equals(mlo)){
                        resultModel.setMloBgcolor("pink");
                        resultModel.setMlocode(""+mainQueryList.get(i).getMlocode());
                        resultModel.setMlo(""+mlo);
                    }
                    else{
                        resultModel.setMloBgcolor("");
                        resultModel.setMlocode(mainQueryList.get(i).getMlocode());
                        resultModel.setMlo("");

                    }
                    resultModel.setLine_No(mainQueryList.get(i).getLine_No());
                    resultModel.setBL_No(mainQueryList.get(i).getBL_No());
                    resultModel.setNotify_name(mainQueryList.get(i).getNotify_name());
                    resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                    resultModel.setDescription_of_Goods(mainQueryList.get(i).getDescription_of_Goods());
                    resultModel.setCont_size(""+mainQueryList.get(i).getCont_size());
                    resultModel.setContainerHeight(""+container_height);
                    resultModel.setCont_iso_type(""+mainQueryList.get(i).getCont_iso_type());
                    resultModel.setPortCategory(""+portCategory);
                    resultModel.setCont_gross_weight(mainQueryList.get(i).getCont_gross_weight());
                    resultModel.setCont_seal_number(mainQueryList.get(i).getCont_seal_number());
                    resultModel.setCont_status(mainQueryList.get(i).getCont_status());
                    resultModel.setCont_type(mainQueryList.get(i).getCont_type());
                    resultModel.setCommudity_desc(mainQueryList.get(i).getCommudity_desc());
                    resultModel.setCont_vat(mainQueryList.get(i).getCont_vat());
                    resultModel.setOff_dock_name(mainQueryList.get(i).getOff_dock_name());
                    resultModel.setCont_imo(mainQueryList.get(i).getCont_imo());
                    resultModel.setCont_un(mainQueryList.get(i).getCont_un());
                    resultModel.setState(searchType);
                    con_no=mainQueryList.get(i).getCont_number();
                    resultList.add(resultModel);

                }



            }

            if(searchType==6){
                if(!portCategory.equals(category)){
                    sl5++;
                    resultModel.setSl(sl5);
                    resultModel.setOrganization_Name(mainQueryList.get(i).getOrganization_Name());
                    resultModel.setMlocode(""+mainQueryList.get(i).getMlocode());
                    resultModel.setLine_No(mainQueryList.get(i).getLine_No());
                    resultModel.setBL_No(mainQueryList.get(i).getBL_No());
                    resultModel.setNotify_name(mainQueryList.get(i).getNotify_name());
                    resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                    resultModel.setDescription_of_Goods(mainQueryList.get(i).getDescription_of_Goods());
                    resultModel.setCont_size(""+mainQueryList.get(i).getCont_size());
                    resultModel.setContainerHeight(""+container_height);
                    resultModel.setCont_iso_type(""+mainQueryList.get(i).getCont_iso_type());
                    if(!portCategory.equals(category)){
                        resultModel.setPortCategory(""+portCategory);
                        resultModel.setCategory(""+category);
                        resultModel.setContIsoTypeBgcolor("pink");

                    }
                    else{
                        resultModel.setPortCategory(""+portCategory);
                        resultModel.setCategory("");
                        resultModel.setContIsoTypeBgcolor("");

                    }
                    resultModel.setCont_gross_weight(mainQueryList.get(i).getCont_gross_weight());
                    resultModel.setCont_seal_number(mainQueryList.get(i).getCont_seal_number());
                    resultModel.setCont_status(mainQueryList.get(i).getCont_status());
                    resultModel.setCont_type(mainQueryList.get(i).getCont_type());
                    resultModel.setCommudity_desc(mainQueryList.get(i).getCommudity_desc());
                    resultModel.setCont_vat(mainQueryList.get(i).getCont_vat());
                    resultModel.setOff_dock_name(mainQueryList.get(i).getOff_dock_name());
                    resultModel.setCont_imo(mainQueryList.get(i).getCont_imo());
                    resultModel.setCont_un(mainQueryList.get(i).getCont_un());
                    resultModel.setState(searchType);
                    con_no=mainQueryList.get(i).getCont_number();

                    resultList.add(resultModel);


                }

            }

            if(searchType==8){
                if(!mainQueryList.get(i).getCont_number().equals(containerNo)){
                    sl6++;
                    resultModel.setSl(sl6);
                    resultModel.setOrganization_Name(mainQueryList.get(i).getOrganization_Name());
                    resultModel.setMlocode(""+mainQueryList.get(i).getMlocode());
                    resultModel.setLine_No(mainQueryList.get(i).getLine_No());
                    resultModel.setBL_No(mainQueryList.get(i).getBL_No());
                    resultModel.setNotify_name(mainQueryList.get(i).getNotify_name());
                    if(!mainQueryList.get(i).getCont_number().equals(containerNo)){
                        resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                        resultModel.setContainerNo(containerNo);
                        resultModel.setContainerNoBgcolor("pink");

                    }
                    else{
                        resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                        resultModel.setContainerNo("");
                        resultModel.setContainerNoBgcolor("");
                    }
                    resultModel.setDescription_of_Goods(mainQueryList.get(i).getDescription_of_Goods());
                    resultModel.setCont_size(""+mainQueryList.get(i).getCont_size());
                    resultModel.setContainerHeight(""+container_height);
                    resultModel.setCont_iso_type(""+mainQueryList.get(i).getCont_iso_type());
                    resultModel.setPortCategory(""+portCategory);
                    resultModel.setCont_gross_weight(mainQueryList.get(i).getCont_gross_weight());
                    resultModel.setCont_seal_number(mainQueryList.get(i).getCont_seal_number());
                    resultModel.setCont_status(mainQueryList.get(i).getCont_status());
                    resultModel.setCont_type(mainQueryList.get(i).getCont_type());
                    resultModel.setCommudity_desc(mainQueryList.get(i).getCommudity_desc());
                    resultModel.setCont_vat(mainQueryList.get(i).getCont_vat());
                    resultModel.setOff_dock_name(mainQueryList.get(i).getOff_dock_name());
                    resultModel.setCont_imo(mainQueryList.get(i).getCont_imo());
                    resultModel.setCont_un(mainQueryList.get(i).getCont_un());
                    resultModel.setState(searchType);
                    con_no=mainQueryList.get(i).getCont_number();

                    resultList.add(resultModel);

                }

            }

            if(searchType==9){
                if(!String.valueOf(isocode).equals(mainQueryList.get(i).getCont_iso_type())){
                    sl6++;
                    resultModel.setSl(sl6);
                    resultModel.setOrganization_Name(mainQueryList.get(i).getOrganization_Name());
                    resultModel.setMlocode(""+mainQueryList.get(i).getMlocode());
                    resultModel.setLine_No(mainQueryList.get(i).getLine_No());
                    resultModel.setBL_No(mainQueryList.get(i).getBL_No());
                    resultModel.setNotify_name(mainQueryList.get(i).getNotify_name());
                  if(!mainQueryList.get(i).getCont_number().equals(containerNo)){
                      resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                      resultModel.setContainerNo(containerNo);
                      resultModel.setContainerNoBgcolor("pink");


                  }
                  else{
                      resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                      resultModel.setContainerNo("");
                      resultModel.setContainerNoBgcolor("");

                  }
                    resultModel.setDescription_of_Goods(mainQueryList.get(i).getDescription_of_Goods());
                    resultModel.setCont_size(""+mainQueryList.get(i).getCont_size());
                    resultModel.setContainerHeight(""+container_height);

                    if(!(mainQueryList.get(i).getCont_iso_type().trim().equals(isocode.toString().trim()))){
                        resultModel.setCont_iso_type(""+mainQueryList.get(i).getCont_iso_type());
                        resultModel.setIsoCode(""+isocode);
                        resultModel.setContIsoTypeBgcolor("pink");

                    }
                    else{
                        resultModel.setCont_iso_type(mainQueryList.get(i).getCont_iso_type());
                        resultModel.setContIsoTypeBgcolor("");
                        resultModel.setIsoCode("");

                    }
                    resultModel.setPortCategory(""+portCategory);
                    resultModel.setCont_gross_weight(mainQueryList.get(i).getCont_gross_weight());
                    resultModel.setCont_seal_number(mainQueryList.get(i).getCont_seal_number());
                    resultModel.setCont_status(mainQueryList.get(i).getCont_status());
                    resultModel.setCont_type(mainQueryList.get(i).getCont_type());
                    resultModel.setCommudity_desc(mainQueryList.get(i).getCommudity_desc());
                    resultModel.setCont_vat(mainQueryList.get(i).getCont_vat());
                    resultModel.setOff_dock_name(mainQueryList.get(i).getOff_dock_name());
                    resultModel.setCont_imo(mainQueryList.get(i).getCont_imo());
                    resultModel.setCont_un(mainQueryList.get(i).getCont_un());
                    resultModel.setState(searchType);
                    con_no=mainQueryList.get(i).getCont_number();

                    resultList.add(resultModel);


                }

            }

            if(searchType==10){
                if(mainQueryList.get(i).getSymbol().equals("S")){
                    sl6++;
                    resultModel.setSl(sl6);
                    resultModel.setOrganization_Name(mainQueryList.get(i).getOrganization_Name());
                    resultModel.setMlocode(""+mainQueryList.get(i).getMlocode());
                    resultModel.setLine_No(mainQueryList.get(i).getLine_No());
                    resultModel.setBL_No(mainQueryList.get(i).getBL_No());
                    resultModel.setNotify_name(mainQueryList.get(i).getNotify_name());
                    if(!mainQueryList.get(i).getCont_number().equals(containerNo)){
                        resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                        resultModel.setContainerNo(containerNo);
                        resultModel.setContainerNoBgcolor("pink");

                    }
                    else{
                        resultModel.setCont_number(mainQueryList.get(i).getCont_number());
                        resultModel.setContainerNo("");
                        resultModel.setContainerNoBgcolor("");
                    }
                    resultModel.setDescription_of_Goods(mainQueryList.get(i).getDescription_of_Goods());
                    resultModel.setCont_size(""+mainQueryList.get(i).getCont_size());
                    resultModel.setContainerHeight(""+container_height);
                    resultModel.setCont_iso_type(""+mainQueryList.get(i).getCont_iso_type());
                    resultModel.setPortCategory(""+portCategory);
                    resultModel.setCont_gross_weight(mainQueryList.get(i).getCont_gross_weight());
                    resultModel.setCont_seal_number(mainQueryList.get(i).getCont_seal_number());
                    resultModel.setCont_status(mainQueryList.get(i).getCont_status());
                    resultModel.setCont_type(mainQueryList.get(i).getCont_type());
                    resultModel.setCommudity_desc(mainQueryList.get(i).getCommudity_desc());
                    resultModel.setCont_vat(mainQueryList.get(i).getCont_vat());
                    resultModel.setOff_dock_name(mainQueryList.get(i).getOff_dock_name());
                    resultModel.setCont_imo(mainQueryList.get(i).getCont_imo());
                    resultModel.setCont_un(mainQueryList.get(i).getCont_un());
                    resultModel.setState(searchType);
                    con_no=mainQueryList.get(i).getCont_number();

                    resultList.add(resultModel);

                }
            }
            if(searchType==7){
                if(tag==1){
                    sl5++;
                    resultModel.setSl(sl);
                    resultModel.setState7Message(mainQueryList.get(i).getCont_number()+" not found");
                    resultModel.setState(searchType);
                    resultList.add(resultModel);
                }


            }



            //resultList.add(resultModel);
        }





        return resultList;


    }

    class MainQuery implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            IgmContainerListModel igmContainerListModel= new IgmContainerListModel();
            igmContainerListModel.setIgm_detail_id2(rs.getString("igm_detail_id2"));
            igmContainerListModel.setMlocode(rs.getString("mlocode"));
            igmContainerListModel.setCont_number(rs.getString("cont_number"));
            igmContainerListModel.setId(rs.getInt("id"));
            igmContainerListModel.setLine_No(rs.getInt("Line_No"));
            igmContainerListModel.setBL_No(rs.getString("BL_No"));
            igmContainerListModel.setSubmitee_Org_Id(rs.getInt("Submitee_Org_Id"));
            igmContainerListModel.setCont_size(rs.getDouble("cont_size"));
            igmContainerListModel.setPlace_of_unloading(rs.getString("place_of_unloading"));
            igmContainerListModel.setCont_weight(rs.getInt("cont_weight"));
            igmContainerListModel.setCont_seal_number(rs.getString("cont_seal_number"));
            igmContainerListModel.setCont_status(rs.getString("cont_status"));
            igmContainerListModel.setCont_height(rs.getDouble("cont_height"));
            igmContainerListModel.setCont_iso_type(rs.getString("cont_iso_type"));
            igmContainerListModel.setCont_type(rs.getString("cont_type"));
            igmContainerListModel.setCommudity_desc(rs.getString("commudity_desc"));
            igmContainerListModel.setCont_vat(rs.getString("cont_vat"));
            igmContainerListModel.setOff_dock_id(rs.getInt("off_dock_id"));
            igmContainerListModel.setCont_imo(rs.getDouble("cont_imo"));
            igmContainerListModel.setCont_un(rs.getInt("cont_un"));
            igmContainerListModel.setCont_gross_weight(rs.getDouble("Cont_gross_weight"));
            igmContainerListModel.setRType(rs.getString("RType"));
            igmContainerListModel.setOrganization_Name(rs.getString("Organization_Name"));
            igmContainerListModel.setDescription_of_Goods(rs.getString("Description_of_Goods"));
            igmContainerListModel.setPort_status(rs.getInt("port_status"));
            igmContainerListModel.setTYPE(rs.getString("TYPE"));
            igmContainerListModel.setOff_dock_name(rs.getString("off_dock_name"));
            igmContainerListModel.setSymbol(rs.getString("symbol"));
            igmContainerListModel.setNotify_name(rs.getString("Notify_name"));
            return igmContainerListModel;
        }
    }

    class SqlStr implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            IgmContainerListModel igmContainerListModel= new IgmContainerListModel();
            igmContainerListModel.setInv_unit_id(rs.getString("id"));
            igmContainerListModel.setGkey(rs.getInt("gkey"));
            igmContainerListModel.setCategory(rs.getString("category"));
            igmContainerListModel.setFreight_kind(rs.getString("freight_kind"));
            igmContainerListModel.setSize(rs.getDouble("size"));
            igmContainerListModel.setHeight(rs.getDouble("height"));
            igmContainerListModel.setMlo(rs.getString("mlo"));
            return igmContainerListModel;
        }
    }

    class Sql22 implements RowMapper{
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            IgmContainerListModel igmContainerListModel= new IgmContainerListModel();
            igmContainerListModel.setRef_equip_type_id(rs.getInt("id"));
            return igmContainerListModel;
        }
    }

}
