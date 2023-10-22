package com.datasoft.IgmMis.Service.ExportReport;

import com.datasoft.IgmMis.Model.ImportReport.ImportContainerBayViewMainModel;
import com.datasoft.IgmMis.Model.ImportReport.ImportContainerBayViewTempModel;
import com.datasoft.IgmMis.Model.ImportReport.ImportCotainerBayViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class ExportContainerBayViewService {


    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;

    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;



    public Integer getVvdGkey(String rotation){
        List<ImportCotainerBayViewModel> vvdGkeyList=new ArrayList<>();
        String sqlQuery="";
        Integer vvdGkey=0;
        sqlQuery="SELECT vsl_vessel_visit_details.vvd_gkey,vsl_vessel_visit_details.ib_vyg\n" +
                "FROM vsl_vessel_visit_details\n" +
                "INNER JOIN argo_carrier_visit ON argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "INNER JOIN vsl_vessels ON vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "WHERE ib_vyg='"+rotation+"'";
        vvdGkeyList=OracleDbTemplate.query(sqlQuery,new ExportContainerBayViewService.VvdGkey());
        for(int i=0; i<vvdGkeyList.size();i++){
            vvdGkey=vvdGkeyList.get(i).getVvd_gkey();

        }


        return vvdGkey;
    }

    public List getContainerBayVesselInfo(Integer vvdgkey) {
        List<ImportCotainerBayViewModel> vesselInfoList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="select vsl_vessels.id,vsl_vessels.name,vsl_vessel_visit_details.ib_vyg,\n" +
                "argo_carrier_visit.ata,argo_carrier_visit.atd\n" +
                "from vsl_vessel_visit_details\n" +
                "inner join argo_carrier_visit on argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "inner join vsl_vessels on vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "where vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";

        vesselInfoList=OracleDbTemplate.query(sqlQuery,new ExportContainerBayViewService.VesselInfo());
        return vesselInfoList;

    }
    public List getContainerBayViewList(Integer vvdgkey){
        List<ImportContainerBayViewMainModel> bayViewList=new ArrayList<>();
        List<ImportCotainerBayViewModel> resultList=new ArrayList<>();
        List<ImportCotainerBayViewModel> vesselInfoList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="select vsl_vessels.id,vsl_vessels.name,vsl_vessel_visit_details.ib_vyg,\n" +
                "argo_carrier_visit.ata,argo_carrier_visit.atd\n" +
                "from vsl_vessel_visit_details\n" +
                "inner join argo_carrier_visit on argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "inner join vsl_vessels on vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "where vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";

        vesselInfoList=OracleDbTemplate.query(sqlQuery,new ExportContainerBayViewService.VesselInfo());
        String vesselId="";
        String vesselName="";
        String rotation="";
        String ata="";
        String atd="";
        if(vesselInfoList.size()>0){
            vesselId=vesselInfoList.get(0).getVessel_id();
            vesselName=vesselInfoList.get(0).getName();
            ata=vesselInfoList.get(0).getAta();
            atd=vesselInfoList.get(0).getAtd();

        }

        sqlQuery="select * from misBayView where vslId='"+vesselId+"' order by bay asc";
        System.out.println(sqlQuery);
        resultList=primaryDBTemplate.query(sqlQuery,new ExportContainerBayViewService.MisBayView());
        Integer mystat = 0;
        for(int i=0; i< resultList.size(); i++ ){
            ImportContainerBayViewMainModel resultModel=new ImportContainerBayViewMainModel();
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel=resultList.get(i);

            resultModel.setId(importCotainerBayViewModel.getId());
            resultModel.setVslId(importCotainerBayViewModel.getVslId());
            resultModel.setBay(importCotainerBayViewModel.getBay());
            resultModel.setPaired(importCotainerBayViewModel.getPaired());
            resultModel.setPairedWith(importCotainerBayViewModel.getPairedWith());
            resultModel.setCenterLineA(importCotainerBayViewModel.getCenterLineA());
            resultModel.setGapLineA(importCotainerBayViewModel.getGapLineA());
            resultModel.setIsBelow(importCotainerBayViewModel.getIsBelow());
            resultModel.setCenterLineB(importCotainerBayViewModel.getCenterLineB());
            resultModel.setGapLineB(importCotainerBayViewModel.getGapLineB());
            resultModel.setMinRowLimAbv(importCotainerBayViewModel.getMinRowLimAbv());
            resultModel.setMinRowLimBlw(importCotainerBayViewModel.getMinRowLimBlw());
            resultModel.setMaxRowLimAbv(importCotainerBayViewModel.getMaxRowLimAbv());
            resultModel.setMaxRowLimBlw(importCotainerBayViewModel.getMaxRowLimBlw());
            resultModel.setGapUpperRow(importCotainerBayViewModel.getGapUpperRow());
            resultModel.setGapLowerRow(importCotainerBayViewModel.getGapLowerRow());

            String bay = "";
            String title = "";
            Integer prevBay1 =0;
            Integer prevBay2=0;
            if(importCotainerBayViewModel.getPaired()==1){
                String title1="";
                String title2="";
                if(importCotainerBayViewModel.getBay()<10){
                    title1 = "0"+importCotainerBayViewModel.getBay();

                }
                else{
                    title1 = ""+importCotainerBayViewModel.getBay();

                }
                if(importCotainerBayViewModel.getPairedWith()<10){
                    title2 = "0"+importCotainerBayViewModel.getPairedWith();
                }
                else {
                    title2 = ""+importCotainerBayViewModel.getPairedWith();

                }
                title=title1+"("+title2+")";
                bay = title1+","+title2;
            }
            else{
                if(importCotainerBayViewModel.getBay()<10){
                    title = "0"+importCotainerBayViewModel.getBay();

                }
                else{
                    title = ""+importCotainerBayViewModel.getBay();

                }
                bay=title;
            }

            prevBay1 = importCotainerBayViewModel.getBay()-1;
            prevBay2 =importCotainerBayViewModel.getBay()-2;
            String strChkBayQuery="";
            strChkBayQuery = "select count(bay) as cnt from misBayView where vslId='"+vesselId+"' and bay='"+prevBay1+"'";
            List<ImportCotainerBayViewModel> bayCountList=primaryDBTemplate.query(strChkBayQuery,new ExportContainerBayViewService.CheckBay());
            Integer bayCount=0;
            if(bayCountList.size()>0){
                bayCount=bayCountList.get(0).getCnt();
            }
            String strBayStateQuery = "";
            if(bayCount>0){
                strBayStateQuery="select paired from misBayView where vslId='"+vesselId+"' and bay='"+prevBay1+"'";
            }
            else{
                strBayStateQuery="select paired from misBayView where vslId='"+vesselId+"' and bay='"+prevBay2+"'";
            }

            List<ImportCotainerBayViewModel> bayStateList=primaryDBTemplate.query(strBayStateQuery,new ExportContainerBayViewService.BaySate());
            Integer bayState=0;
            if(bayStateList.size()>0){
                bayState=bayStateList.get(0).getPaired();
            }
            if(importCotainerBayViewModel.getBay()==1 && importCotainerBayViewModel.getPaired()==0){
                mystat=1;
            }
            String strMaxColQuery="";
            strMaxColQuery = "select max(maxColLimit) as maxCol from misBayViewBelow where vslId='"+vesselId+"' and bay='"+importCotainerBayViewModel.getBay()+"'";
            List<ImportCotainerBayViewModel> maxColList=primaryDBTemplate.query(strMaxColQuery,new ExportContainerBayViewService.MaxCol());
            Integer maxCol=0;

            if(maxColList.size()>0){
                maxCol=maxColList.get(0).getMaxCol();
            }

            resultModel.setMaxCol(maxCol);
            resultModel.setTitle(title);
            String  strUpDeckLblQuery="";
            strUpDeckLblQuery = "select minColLimit,maxColLimit from misBayViewBelow where vslId='"+vesselId+"' and bay='"+importCotainerBayViewModel.getBay()+"'";
            List<ImportCotainerBayViewModel> maxAndMInColList=primaryDBTemplate.query(strUpDeckLblQuery,new ExportContainerBayViewService.MaxAndMInCOlLImit());
            Integer minColLimit=0;
            Integer kl=0;
            if(maxAndMInColList.size()>0){
                minColLimit=maxAndMInColList.get(0).getMinColLimit();
            }

            if((maxCol%2==1)){
                kl = maxCol-1;

            }
            else{
                kl = maxCol;
            }
            List<ImportContainerBayViewTempModel> firstList=new ArrayList<>();
            while(kl >=minColLimit){
                ImportContainerBayViewTempModel firstModel =new ImportContainerBayViewTempModel();

                if(minColLimit!=0){
                    if(kl<10){
                        firstModel.setKl("0"+String.valueOf(kl));
                    }
                    else{
                        firstModel.setKl(String.valueOf(kl));
                    }
                    firstList.add(firstModel);
                }
                kl=kl-2;
            }
            resultModel.setFirstLimit(firstList);

            int l=0;
            int rLimit=0;
            l=minColLimit;

            if(maxCol%2==0){
                rLimit=maxCol-1;
            }
            else{
                rLimit=maxCol;
            }

            List<ImportContainerBayViewTempModel> lastList=new ArrayList<>();
            while(l<=rLimit)
            {
                ImportContainerBayViewTempModel lastModel =new ImportContainerBayViewTempModel();
                if(l<10){
                    lastModel.setL("0"+String.valueOf(l));
                }
                else{
                    lastModel.setL(String.valueOf(l));
                }

                l=l+2;
                lastList.add(lastModel);
            }
            resultModel.setLastLimit(lastList);

// Dynamic Row start
            Integer minRow = importCotainerBayViewModel.getMinRowLimAbv();
            Integer ivalue=0;
            Integer maxRowAbv = importCotainerBayViewModel.getMaxRowLimAbv();
            String upGapVal = importCotainerBayViewModel.getGapUpperRow();
            String upGapValArr[] = upGapVal.split(",");
            List<String> upGapValList= Arrays.asList(upGapValArr);
            ivalue=maxRowAbv;
            Map<String,Integer> totalTable1Ton= new HashMap<>();
            Map<String,Integer> totalTable2Ton= new HashMap<>();
            Integer tons12 =0;
            Integer tons11 =0;
            Integer tons10 =0;
            Integer tons9 =0;
            Integer tons8 =0;
            Integer tons7 =0;
            Integer tons6 =0;
            Integer tons5 =0;
            Integer tons4 =0;
            Integer tons3 =0;
            Integer tons2 =0;
            Integer tons1 =0;
            Integer tons0 =0;
            Integer minColLimitUp=0;
            Integer table1tonRow2=0;
            Integer table1tonRow1=0;


            List<ImportContainerBayViewTempModel> dynamicRowList=new ArrayList<>();
            while(ivalue >= minRow){

                ImportContainerBayViewTempModel daynamicRowModel= new ImportContainerBayViewTempModel();
                String strUpDeck="";
                strUpDeck = "select minColLimit,maxColLimit from misBayViewBelow where vslId='"+vesselId+"' and bay='"+importCotainerBayViewModel.getBay()+"' and bayRow='"+ivalue+"'";

                List<ImportCotainerBayViewModel> resUpDeckList=primaryDBTemplate.query(strUpDeck,new ExportContainerBayViewService.MaxAndMInCOlLImit());
                System.out.println(resUpDeckList.size());
                // Integer minColLimitUp=0;
                Integer maxColLimitUp=0;
                Integer k=0;

                if(resUpDeckList.size()>0){
                    minColLimitUp = resUpDeckList.get(0).getMinColLimit();
                    maxColLimitUp = resUpDeckList.get(0).getMaxColLimit();
                }

                daynamicRowModel.setI(ivalue);

                if(maxCol%2==1)
                {
                    k = maxCol-1;
                }
                else
                {
                    k = maxCol;
                }

                List<ImportCotainerBayViewModel> dynamicRowInnerList=new ArrayList<>();


                while(k>=minColLimitUp){
                    ImportCotainerBayViewModel dynamicInnerModel=new ImportCotainerBayViewModel();
                    String gapStr="";
                    String kval="";
                    String pos="";
                    if(k<10){
                        kval = "0"+k;
                    }
                    else{
                        kval = ""+k;
                    }
                    gapStr = kval+ivalue;
                    if(k<10){
                        pos = "0"+k+ivalue;
                    }

                    else{
                        pos = ""+k+ivalue;
                    }
                    // String strPrevCont = "";
                    List<ImportCotainerBayViewModel> dynamicSizeList=new ArrayList<>();
                    if(importCotainerBayViewModel.getPaired()==0) {
                        Integer rby1 = importCotainerBayViewModel.getBay() - 2;
                        Integer rby2 = importCotainerBayViewModel.getBay() - 1;
                        String rby12 = "";
                        String rby22 = "";
                        String slot1 = "";
                        String slot2 = "";
                        String strPrevCont = "";


                        if (rby1 < 10) {
                            rby12 = "0" + rby1;
                        } else {
                            rby12 = "" + rby1;
                        }


                        if (rby2 < 10) {
                            rby22 = "0" + rby2;
                        } else {
                            rby22 = "" + rby2;
                        }
                        slot1 = rby12 + pos;
                        slot2 = rby22 + pos;


//                        strPrevCont = "select right(sparcsn4.ref_equip_type.nominal_length,2) as size from ctmsmis.mis_exp_unit \n" +
//                                "\t\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
//                                "\t\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
//                                "\t\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
//                                "\t\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
//                                "\t\t\t\t\twhere (stowage_pos='"+slot1+"' or stowage_pos='"+slot2+"') and ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"'";
                strPrevCont = "select substr(ref_equip_type.nominal_length,-2) as cont_size, inv_unit_fcy_visit.last_pos_slot AS stowage_pos\n" +
                        "from inv_unit\n" +
                        "INNER JOIN inv_unit_fcy_visit ON inv_unit_fcy_visit.unit_gkey=inv_unit.gkey\n" +
                        "INNER JOIN ref_equipment ON ref_equipment.gkey=inv_unit.eq_gkey\n" +
                        "inner join ref_equip_type on ref_equip_type.gkey=ref_equipment.eqtyp_gkey\n" +
                        "where (inv_unit_fcy_visit.last_pos_slot='"+slot1+"' or inv_unit_fcy_visit.last_pos_slot='"+slot2+"')";
                        dynamicSizeList=OracleDbTemplate.query(strPrevCont,new ExportContainerBayViewService.DynamicSize());
                    }

                    if(dynamicSizeList.size()>0 && importCotainerBayViewModel.getPaired() == 0 && dynamicSizeList.get(0).getSize() > 20 && mystat == 0 && bayState > 0) {
                        Integer tempSize = 0;
                        tempSize = dynamicSizeList.get(0).getSize();
                        //if (importCotainerBayViewModel.getPaired() == 0 && tempSize > 20 && mystat == 0 && bayState > 0) {
                        dynamicInnerModel.setClassType("gridcolor");
                        dynamicInnerModel.setResultpodSize(String.valueOf(tempSize) + "'");


                        // }
                    }
                    else{
                        String strPos="";
                        strPos="select ctmsmis.mis_exp_unit.pod,sparcsn4.inv_unit.freight_kind,sparcsn4.inv_unit.id,cont_mlo as mlo,\n" +
                                "\t\t\t\t\tright(sparcsn4.ref_equip_type.nominal_length,2) as size, \n" +
                                "\t\t\t\t\tceil((ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg/1000)) as tons,sparcsn4.ref_equip_type.iso_group as rfr_connect \n" +
                                "\t\t\t\t\tfrom ctmsmis.mis_exp_unit \n" +
                                "\t\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                                "\t\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                                "\t\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                                "\t\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                                "\t\t\t\t\tinner JOIN sparcsn4.ref_bizunit_scoped r ON r.gkey=inv_unit.line_op \n" +
                                "\t\t\t\t\tLEFT JOIN sparcsn4.ref_agent_representation x on r.gkey=x.bzu_gkey \n" +
                                "\t\t\t\t\tLEFT JOIN sparcsn4.ref_bizunit_scoped y ON x.agent_gkey=y.gkey\n" +
                                "\t\t\t\t\twhere ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"' and left(stowage_pos,2) in("+bay+") and right(stowage_pos,4)='"+pos+"' and right(stowage_pos,2)>=80 order by stowage_pos";
                        List<ImportCotainerBayViewModel> resPosList=secondaryDBTemplate.query(strPos,new ExportContainerBayViewService.DynamicSize2());

                        if(minColLimitUp!=0){

                            if(k >maxColLimitUp){

                                dynamicInnerModel.setClassType("nogrid");
                            }
                            else if(upGapValList.contains(gapStr)){

                                dynamicInnerModel.setClassType("nogrid");

                            }
                            else  if(resPosList.size()>0){
                                dynamicInnerModel.setClassType("gridcolor");
                            }
                            else{
                                dynamicInnerModel.setClassType("grid");
                            }

                            if(resPosList.size()>0 && k<=maxColLimitUp)
                            {
                                String rfrAbvL="";
                                String freight_kindAbvL="";
                                String txtAbvL = "";
                                rfrAbvL = resPosList.get(0).getRfr_connect();
                                freight_kindAbvL = resPosList.get(0).getFreight_kind();

                                if(rfrAbvL.equals("RE")  || rfrAbvL.equals("RS")  || rfrAbvL.equals("RT") || rfrAbvL.equals("HR") ){
                                    txtAbvL = "R";
                                }

                                else if(freight_kindAbvL=="MTY"){
                                    txtAbvL = "E";
                                }

                                else if(freight_kindAbvL.equals("FCL") || freight_kindAbvL.equals("LCL")){
                                    txtAbvL = "D";
                                }


                                String s1 =resPosList.get(0).getPod()+" "+txtAbvL+resPosList.get(0).getSize()+"'\n";
                                dynamicInnerModel.setResultpodSize(s1);

                                dynamicInnerModel.setResultSId(resPosList.get(0).getsId()+"'\n");
                                dynamicInnerModel.setResultMloTons(resPosList.get(0).getMlo()+resPosList.get(0).getTons()+"Ts");
                                // ${'tons'.$k} += $rowPos->tons;
                                table1tonRow1=table1tonRow1+resPosList.get(0).getTons();

                                if(totalTable1Ton.get("tons"+k)!=null){
                                    totalTable1Ton.put("tons"+k,totalTable1Ton.get("tons"+k)+resPosList.get(0).getTons());
                                }
                                else{
                                    totalTable1Ton.put("tons"+k,resPosList.get(0).getTons());
                                }



                            }


                        }
                    }
                    dynamicRowInnerList.add(dynamicInnerModel);
                    k=k-2;
                }


                if(importCotainerBayViewModel.getCenterLineA()==1 && !(upGapValList.contains("00"+ivalue))){
                    List<ImportCotainerBayViewModel> centerLineList=new ArrayList<>();
                    daynamicRowModel.setState(1);
                    String  posCentre = "00"+ivalue;
                    if(importCotainerBayViewModel.getPaired()==0){
                        Integer rbyCntr1 = importCotainerBayViewModel.getBay()-2;
                        Integer rbyCntr2 = importCotainerBayViewModel.getBay()-1;
                        String rbyCntr12="";
                        String rbyCntr22="";
                        String slotCntr1="";
                        String slotCntr2="";

                        if(rbyCntr1<10){
                            rbyCntr12 = "0"+rbyCntr1;

                        }
                        else{
                            rbyCntr12 = ""+rbyCntr1;
                        }


                        if(rbyCntr2<10){
                            rbyCntr22 = "0"+rbyCntr2;
                        }

                        else{
                            rbyCntr22 = ""+rbyCntr2;
                        }

                        slotCntr1 = rbyCntr12+posCentre;
                        slotCntr2 = rbyCntr22+posCentre;
                        String strPrevContCntr="";
                        strPrevContCntr="select right(sparcsn4.ref_equip_type.nominal_length,2) as size from ctmsmis.mis_exp_unit \n" +
                                "\t\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                                "\t\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                                "\t\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                                "\t\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                                "\t\t\t\t\twhere (stowage_pos='"+slotCntr1+"' or stowage_pos='"+slotCntr2+"') and ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"'";
                        centerLineList=secondaryDBTemplate.query(strPrevContCntr,new ExportContainerBayViewService.DynamicSize());

                    }

                    if(centerLineList.size()>0 && importCotainerBayViewModel.getPaired() == 0 && centerLineList.get(0).getSize() > 20 && mystat == 0 && bayState > 0){
                        Integer tempSize2 = 0;
                        tempSize2 = centerLineList.get(0).getSize();
                        //  if (importCotainerBayViewModel.getPaired() == 0 && tempSize2 > 20 && mystat == 0 && bayState > 0) {
                        daynamicRowModel.setCenterClassType("gridcolor");
                        daynamicRowModel.setResultCenterPodSize(String.valueOf(tempSize2) + "'");


                        // }
                    }
                    else{
                        String strPosCentre="";
                        strPosCentre="select ctmsmis.mis_exp_unit.pod,sparcsn4.inv_unit.freight_kind,sparcsn4.inv_unit.id,cont_mlo as mlo,right(sparcsn4.ref_equip_type.nominal_length,2) as size, ceil((ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg/1000)) as tons,sparcsn4.ref_equip_type.iso_group as rfr_connect \n" +
                                "\t\t\t\t\tfrom ctmsmis.mis_exp_unit \n" +
                                "\t\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                                "\t\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                                "\t\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                                "\t\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                                "\t\t\t\t\tinner JOIN sparcsn4.ref_bizunit_scoped r ON r.gkey=inv_unit.line_op \n" +
                                "\t\t\t\t\tLEFT JOIN sparcsn4.ref_agent_representation x on r.gkey=x.bzu_gkey \n" +
                                "\t\t\t\t\tLEFT JOIN sparcsn4.ref_bizunit_scoped y ON x.agent_gkey=y.gkey\n" +
                                "\t\t\t\t\t\twhere ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"' and left(stowage_pos,2) in("+bay+") and right(stowage_pos,4)='"+posCentre+"' and right(stowage_pos,2)>=80 order by stowage_pos";
                        List<ImportCotainerBayViewModel> resCenterPosList=secondaryDBTemplate.query(strPosCentre,new ExportContainerBayViewService.DynamicSize2());
                        if(resCenterPosList.size()>0){
                            daynamicRowModel.setCenterClassType("gridcolor");
                            String rfrAbvC="";
                            String freight_kindAbvC="";
                            rfrAbvC = resCenterPosList.get(0).getRfr_connect();
                            freight_kindAbvC = resCenterPosList.get(0).getFreight_kind() ;
                            String  txtAbvC = "";
                            if(rfrAbvC.equals("RE")  || rfrAbvC.equals("RS") || rfrAbvC.equals("RT")  || rfrAbvC.equals("HR") ){
                                txtAbvC = "R";
                            }

                            else if(freight_kindAbvC.equals("MTY")){
                                txtAbvC = "E";

                            }

                            else if(freight_kindAbvC=="FCL" || freight_kindAbvC=="LCL"){
                                txtAbvC = "D";
                            }

                            String s =resCenterPosList.get(0).getPod()+" "+txtAbvC+resCenterPosList.get(0).getSize()+"'\n";
                            daynamicRowModel.setResultCenterPodSize(s);
                            daynamicRowModel.setResultCenterSId(resCenterPosList.get(0).getsId()+"'\n");
                            daynamicRowModel.setResultCenterMloTons(resCenterPosList.get(0).getMlo()+resCenterPosList.get(0).getTons());
                            tons0=tons0+resCenterPosList.get(0).getTons();


                        }
                        else{
                            daynamicRowModel.setCenterClassType("grid");
                        }


                    }




                }
                else if(upGapValList.contains("00"+ivalue)){
                    daynamicRowModel.setState(2);
                    daynamicRowModel.setCenterClassType("nogrid");

                }
                else if(importCotainerBayViewModel.getCenterLineA()==0 && importCotainerBayViewModel.getGapLineA()==1){
                    daynamicRowModel.setState(3);
                    daynamicRowModel.setCenterClassType("nogrid");

                }
                Integer lValue=0;
                Integer rcLimit=0;
                lValue = minColLimitUp;


                if(maxCol %2 ==0){
                    rcLimit = maxCol-1;
                }

                else{
                    rcLimit = maxCol;
                }
                List<ImportCotainerBayViewModel> dynamicListLast=new ArrayList<>();
                //  Integer table1tonRow2=0;
                while(lValue<= rcLimit){
                    ImportCotainerBayViewModel dynamicListLastModel=new ImportCotainerBayViewModel();
                    String lval="";
                    String gapStrRisht="";
                    String posRight="";
                    if(lValue < 10){
                        lval = "0"+lValue;
                    }
                    else{
                        lval = ""+lValue;
                    }

                    gapStrRisht = lval+ivalue;

                    if(lValue<10){
                        posRight = "0"+lValue+ivalue;
                    }

                    else{
                        posRight = ""+lValue+ivalue;
                    }


                    List<ImportCotainerBayViewModel> dynamicRowInnerList2=new ArrayList<>();
                    if(importCotainerBayViewModel.getPaired()==0){
                        Integer rbyRight1 = importCotainerBayViewModel.getBay()-2;
                        Integer rbyRight2 = importCotainerBayViewModel.getBay()-1;
                        String rbyRight12="";
                        String rbyRight22="";
                        String slotRight1="";
                        String slotRight2="";
                        String strPrevContRight="";
                        if(rbyRight1<10){
                            rbyRight12 = "0"+rbyRight1;
                        }

                        else{
                            rbyRight12 = ""+rbyRight1;
                        }


                        if(rbyRight2 < 10){
                            rbyRight22 = "0"+rbyRight2;
                        }

                        else{
                            rbyRight22 = ""+rbyRight2;
                        }



                        slotRight1 = rbyRight12+posRight;
                        slotRight2 = rbyRight22+posRight;
                        strPrevContRight="select right(sparcsn4.ref_equip_type.nominal_length,2) as size from ctmsmis.mis_exp_unit \n" +
                                "\t\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                                "\t\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                                "\t\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                                "\t\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                                "\t\t\t\t\twhere (stowage_pos='"+slotRight1+"' or stowage_pos='"+slotRight2+"') and ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"'";
                        dynamicRowInnerList2=secondaryDBTemplate.query(strPrevContRight,new ExportContainerBayViewService.DynamicSize());
                    }

                    if(dynamicRowInnerList2.size()>0 && importCotainerBayViewModel.getPaired() == 0 && dynamicRowInnerList2.get(0).getSize() > 20 && mystat == 0 && bayState > 0){
                        Integer tempSize3=dynamicRowInnerList2.get(0).getSize();
                        //if(importCotainerBayViewModel.getPaired() == 0 && tempSize3 > 20 && mystat == 0 && bayState > 0){
                        dynamicListLastModel.setClassType("gridcolor");
                        dynamicListLastModel.setResultpodSize(tempSize3+"'");

                        //  }


                    }
                    else{
                        String strPosRight="";
                        strPosRight="select ctmsmis.mis_exp_unit.pod,sparcsn4.inv_unit.freight_kind,sparcsn4.inv_unit.id,cont_mlo as mlo,right(sparcsn4.ref_equip_type.nominal_length,2) as size, ceil((ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg/1000)) as tons,sparcsn4.ref_equip_type.iso_group as rfr_connect \n" +
                                "\t\t\t\tfrom ctmsmis.mis_exp_unit \n" +
                                "\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                                "\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                                "\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                                "\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                                "\t\t\t\tinner JOIN sparcsn4.ref_bizunit_scoped r ON r.gkey=inv_unit.line_op \n" +
                                "\t\t\t\tLEFT JOIN sparcsn4.ref_agent_representation x on r.gkey=x.bzu_gkey \n" +
                                "\t\t\t\tLEFT JOIN sparcsn4.ref_bizunit_scoped y ON x.agent_gkey=y.gkey\n" +
                                "\t\t\t\twhere ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"' and left(stowage_pos,2) in("+bay+") and right(stowage_pos,4)='"+posRight+"' and right(stowage_pos,2)>=80 order by stowage_pos";
                        List<ImportCotainerBayViewModel> posRigtList=secondaryDBTemplate.query(strPosRight,new ExportContainerBayViewService.DynamicSize2());
                        if(lValue > maxColLimitUp){
                            dynamicListLastModel.setClassType("nogrid");

                        }
                        else if(upGapValList.contains(gapStrRisht)){
                            dynamicListLastModel.setClassType("nogrid");
                        }
                        else if(posRigtList.size()>0){
                            dynamicListLastModel.setClassType("gridcolor");

                        }
                        else {
                            dynamicListLastModel.setClassType("grid");

                        }
                        if(posRigtList.size()>0 && lValue<= maxColLimitUp){
                            String  rfrAbvR="";
                            String  freight_kindAbvR="";
                            String txtAbvR = "";
                            rfrAbvR = posRigtList.get(0).getRfr_connect();
                            freight_kindAbvR = posRigtList.get(0).getFreight_kind();
                            if(rfrAbvR.equals("RE")  || rfrAbvR.equals("RS")  || rfrAbvR.equals("RT")  || rfrAbvR.equals("HR") ){
                                txtAbvR = "R";
                            }

                            else if(freight_kindAbvR.equals("MTY")){
                                txtAbvR = "E";
                            }

                            else if(freight_kindAbvR.equals("FCL") || freight_kindAbvR.equals("LCL")){
                                txtAbvR = "D";
                            }

                            String s =posRigtList.get(0).getPod()+" "+txtAbvR+posRigtList.get(0).getSize()+"'\n";
                            dynamicListLastModel.setResultpodSize(s);
                            dynamicListLastModel.setResultSId(posRigtList.get(0).getsId()+"'\n");
                            dynamicListLastModel.setResultMloTons(posRigtList.get(0).getMlo()+posRigtList.get(0).getTons()+"Ts");
                            table1tonRow2=table1tonRow2+posRigtList.get(0).getTons();
                            if(totalTable1Ton.get("tons"+lValue)!=null){
                                totalTable1Ton.put("tons"+lValue,totalTable1Ton.get("tons"+lValue)+posRigtList.get(0).getTons());
                            }
                            else{
                                totalTable1Ton.put("tons"+lValue,posRigtList.get(0).getTons());
                            }

                        }

                    }
                    lValue = lValue+2;
                    dynamicListLast.add(dynamicListLastModel);

                }

                daynamicRowModel.setDynamicRightRowSl(ivalue+"");

                daynamicRowModel.setDynamicInnerList(dynamicRowInnerList);
                daynamicRowModel.setDynamicRightRowList(dynamicListLast);
                dynamicRowList.add(daynamicRowModel);

                ivalue=ivalue-2;
            }


            Integer tL=0;

            if(maxCol%2==1)
            {
                tL = maxCol-1;
            }
            else
            {
                tL = maxCol;
            }
            List<ImportCotainerBayViewModel> table1TonsList=new ArrayList<>();
            while (tL>=minColLimitUp){
                ImportCotainerBayViewModel table1TonsModel=new ImportCotainerBayViewModel();
                if(totalTable1Ton.get("tons"+tL)!=null){
                    table1TonsModel.setTable1TotalTons(totalTable1Ton.get("tons"+tL)+"Ts");
                }
                else{
                    table1TonsModel.setTable1TotalTons("0Ts");
                }
                tL = tL-2;
                table1TonsList.add(table1TonsModel);

            }

            resultModel.setTable1TonsList(table1TonsList);

            if(importCotainerBayViewModel.getCenterLineA()==1){
                resultModel.setTable1TotalSate(1);
                resultModel.setTable1Ton(tons0+"Ts");
            }
            else if(importCotainerBayViewModel.getCenterLineA()==0 && importCotainerBayViewModel.getGapLineA()==1) {
                resultModel.setTable1TotalSate(2);

            }

            Integer tR=0;
            Integer rcLimit=0;
            tR = minColLimitUp;
            if(maxCol%2==0){
                rcLimit = maxCol-1;
            }

            else{
                rcLimit = maxCol;
            }
            List<ImportCotainerBayViewModel> table1TonsList2=new ArrayList<>();
            while(tR<=rcLimit){
                ImportCotainerBayViewModel table1TonsModel2=new ImportCotainerBayViewModel();
                if(totalTable1Ton.get("tons"+tR)!=null){
                    table1TonsModel2.setTable1TotalTons(totalTable1Ton.get("tons"+tR)+"Ts");
                }
                else{
                    table1TonsModel2.setTable1TotalTons("0Ts");
                }
                tR = tR+2;
                table1TonsList2.add(table1TonsModel2);

            }
            resultModel.setTable1TonsList2(table1TonsList2);
            resultModel.setDynamicRowList(dynamicRowList);



            if(importCotainerBayViewModel.getIsBelow()==1){
                Integer b=0;
                Integer bMin=0;
                String upGapValBelow="";
                b =importCotainerBayViewModel.getMaxRowLimBlw();
                bMin = importCotainerBayViewModel.getMinRowLimBlw();
                upGapValBelow = importCotainerBayViewModel.getGapLowerRow();
                String upGapValArrBelow[] = upGapValBelow.split(",");
                List<String> upGapValArrBelowList= Arrays.asList(upGapValArrBelow);
                Integer tonsB12 =0;
                Integer tonsB11 =0;
                Integer tonsB10 =0;
                Integer tonsB9 =0;
                Integer tonsB8 =0;
                Integer tonsB7 =0;
                Integer tonsB6 =0;
                Integer tonsB5 =0;
                Integer tonsB4 =0;
                Integer tonsB3 =0;
                Integer tonsB2 =0;
                Integer tonsB1 =0;
                Integer tonsB0 =0;
                List<ImportContainerBayViewTempModel> dynamicBelowList=new ArrayList<>();
                while(b>=bMin){
                    ImportContainerBayViewTempModel dynamicBelowModel=new ImportContainerBayViewTempModel();

                    if(b<10){
                        dynamicBelowModel.setB("0"+b);

                    }
                    else{
                        dynamicBelowModel.setB(""+b);
                    }
                    List<ImportCotainerBayViewModel> resBlDeck=new ArrayList<>();
                    String strBlDeck="";
                    Integer minColLimitBelow=0;
                    Integer maxColLimitBelow=0;
                    Integer kbelow=0;
                    strBlDeck="select minColLimit,maxColLimit from ctmsmis.misBayViewBelow where vslId='"+vesselId+"' and bay='"+importCotainerBayViewModel.getBay()+"' and bayRow='"+b+"'";
                    resBlDeck=secondaryDBTemplate.query(strBlDeck,new ExportContainerBayViewService.MaxAndMInCOlLImit());
                    if(resBlDeck.size()> 0){
                        minColLimitBelow = resBlDeck.get(0).getMinColLimit();
                        maxColLimitBelow = resBlDeck.get(0).getMaxColLimit();
                    }
                    if(maxCol%2==1)
                    {
                        kbelow = maxCol-1;
                    }
                    else
                    {
                        kbelow = maxCol;
                    }
                    if(importCotainerBayViewModel.getCenterLineA()==0 && importCotainerBayViewModel.getCenterLineB()!=0){
                        kbelow = kbelow-2;
                    }

                    else if(importCotainerBayViewModel.getCenterLineA()==0 && importCotainerBayViewModel.getCenterLineB()==0){

                        kbelow = kbelow;

                    }
                    //String bb="";
                    //String cc="";
                    // String posbelow="";
                    List<ImportCotainerBayViewModel> daynamicLeftBelowList=new ArrayList<>();
                    while(kbelow>=2){
                        String bb="";
                        String cc="";
                        String posbelow="";
                        ImportCotainerBayViewModel daynamicLeftBelowModel=new ImportCotainerBayViewModel();
                        if(b<10){
                            bb = "0"+b;
                        }

                        else{
                            bb = ""+b;
                        }


                        if(kbelow<10){
                            cc = "0"+kbelow;
                        }

                        else{
                            cc = ""+kbelow;
                        }


                        posbelow = cc+bb;


                        List<ImportCotainerBayViewModel>resPrevContBelowList= new ArrayList<>();
                        if(importCotainerBayViewModel.getPaired()==0){

                            Integer rbyBelow1 =importCotainerBayViewModel.getBay()-2;
                            Integer rbyBelow2 = importCotainerBayViewModel.getBay()-1;
                            String rbyBelow12="";
                            String rbyBelow22="";
                            String slotBelow1="";
                            String slotBelow2="";
                            String strPrevContBelow="";
                            if(rbyBelow1<10){
                                rbyBelow12 = "0"+rbyBelow1;
                            }

                            else{
                                rbyBelow12 = ""+rbyBelow1;
                            }


                            if(rbyBelow2<10){
                                rbyBelow22 = "0"+rbyBelow2;
                            }

                            else{
                                rbyBelow22 = ""+rbyBelow2;
                            }
                            slotBelow1 = rbyBelow12+posbelow;
                            slotBelow2 = rbyBelow22+posbelow;

                            strPrevContBelow="select right(sparcsn4.ref_equip_type.nominal_length,2) as size from ctmsmis.mis_exp_unit \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                                    "\t\t\t\t\twhere (stowage_pos='"+slotBelow1+"' or stowage_pos='"+slotBelow2+"') and ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"'";
                            resPrevContBelowList=secondaryDBTemplate.query(strPrevContBelow, new ExportContainerBayViewService.DynamicSize());
                        }
                        if(resPrevContBelowList.size()>0 && importCotainerBayViewModel.getPaired()==0 && resPrevContBelowList.get(0).getSize()>20 && kbelow <= maxColLimitBelow && mystat==0 && bayState>0){
                            Integer size4=resPrevContBelowList.get(0).getSize();
                            daynamicLeftBelowModel.setClassType("gridcolor");
                            daynamicLeftBelowModel.setResultpodSize(size4+"'");
                        }
                        else{
                            String strPosbelow="";
                            strPosbelow="select ctmsmis.mis_exp_unit.pod,sparcsn4.inv_unit.freight_kind,sparcsn4.inv_unit.id,cont_mlo as mlo,right(sparcsn4.ref_equip_type.nominal_length,2) as size, ceil((ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg/1000)) as tons,sparcsn4.ref_equip_type.iso_group as rfr_connect \n" +
                                    "\t\t\t\t\tfrom ctmsmis.mis_exp_unit \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                                    "\t\t\t\t\tinner JOIN sparcsn4.ref_bizunit_scoped r ON r.gkey=inv_unit.line_op \n" +
                                    "\t\t\t\t\tLEFT JOIN sparcsn4.ref_agent_representation x on r.gkey=x.bzu_gkey \n" +
                                    "\t\t\t\t\tLEFT JOIN sparcsn4.ref_bizunit_scoped y ON x.agent_gkey=y.gkey\n" +
                                    "\t\t\t\t\twhere ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"' and left(stowage_pos,2) in("+bay+") and right(stowage_pos,4)='"+posbelow+"' and right(stowage_pos,2)<80 order by stowage_pos";
                            List<ImportCotainerBayViewModel> resPosbelowList=secondaryDBTemplate.query(strPosbelow,new ExportContainerBayViewService.DynamicSize2());
                            if(resPosbelowList.size()>0 && kbelow <=maxColLimitBelow) {
                                daynamicLeftBelowModel.setClassType("gridcolor");

                            }
                            else if(upGapValArrBelowList.contains(posbelow)){
                                daynamicLeftBelowModel.setClassType("nogrid");
                            }
                            else if(kbelow >maxColLimitBelow){
                                daynamicLeftBelowModel.setClassType("nogrid");
                            }
                            else{
                                daynamicLeftBelowModel.setClassType("grid");
                            }
                            if(resPosbelowList.size()>0 && kbelow <=maxColLimitBelow){
                                String rfr="";
                                String freight_kind="";
                                String   txt = "";
                                rfr = resPosbelowList.get(0).getRfr_connect();
                                freight_kind = resPosbelowList.get(0).getFreight_kind();

                                if(rfr.equals("RE") || rfr.equals("RS")  || rfr.equals("RT") || rfr.equals("HR") ){
                                    txt = "R";
                                }

                                else if(freight_kind.equals("MTY")){
                                    txt = "E";
                                }

                                else if(freight_kind.equals("FCL") || freight_kind.equals("LCL")){
                                    txt = "D";
                                }
                                String sBelowLeft =resPosbelowList.get(0).getPod()+" "+txt+resPosbelowList.get(0).getSize()+"'\n";
                                daynamicLeftBelowModel.setResultpodSize(sBelowLeft);
                                daynamicLeftBelowModel.setResultSId(resPosbelowList.get(0).getsId()+"'\n");
                                daynamicLeftBelowModel.setResultMloTons(resPosbelowList.get(0).getMlo()+resPosbelowList.get(0).getTons()+"Ts");

                                if(totalTable2Ton.get("tonsB"+kbelow)!=null){
                                    totalTable2Ton.put("tonsB"+kbelow,totalTable2Ton.get("tonsB"+kbelow)+resPosbelowList.get(0).getTons());
                                }
                                else{
                                    totalTable2Ton.put("tonsB"+kbelow,resPosbelowList.get(0).getTons());
                                }


                            }
                        }
                        kbelow = kbelow-2;
                        daynamicLeftBelowList.add(daynamicLeftBelowModel);

                    }


                    if(importCotainerBayViewModel.getCenterLineB()==1 && (!upGapValArrBelowList.contains("00"+b))){
                        dynamicBelowModel.setStateTable2(1);
                        String cb="";
                        String posCentreBelow="";
                        if(b<10){
                            cb = "0"+b;
                        }

                        else{
                            cb = ""+b;
                        }

                        posCentreBelow = "00"+cb;
                        List<ImportCotainerBayViewModel> belowCenterList=new ArrayList<>();

                        if(importCotainerBayViewModel.getPaired()==0){
                            dynamicBelowModel.setStateTable2(1);

                            Integer rbyBelowCntr1 = importCotainerBayViewModel.getBay()-2;
                            Integer rbyBelowCntr2 = importCotainerBayViewModel.getBay()-1;
                            String rbyBelowCntr12="";
                            String rbyBelowCntr22="";
                            String slotBelowCntr1="";
                            String slotBelowCntr2="";
                            String strPrevContBelowCntr="";
                            if(rbyBelowCntr1<10){
                                rbyBelowCntr12 = "0"+rbyBelowCntr1;
                            }

                            else{
                                rbyBelowCntr12 = ""+rbyBelowCntr1;
                            }


                            if(rbyBelowCntr2<10){
                                rbyBelowCntr22 = "0"+rbyBelowCntr2;

                            }

                            else{
                                rbyBelowCntr22 = ""+rbyBelowCntr2;
                            }


                            //echo $rby12.$pos."-".$rby22.$pos;
                            slotBelowCntr1 = rbyBelowCntr12+posCentreBelow;
                            slotBelowCntr2 = rbyBelowCntr22+posCentreBelow;
                            strPrevContBelowCntr="select right(sparcsn4.ref_equip_type.nominal_length,2) as size from ctmsmis.mis_exp_unit \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                                    "\t\t\t\t\twhere (stowage_pos='"+slotBelowCntr1+"' or stowage_pos='"+slotBelowCntr2+"') and ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"'";
                            belowCenterList=secondaryDBTemplate.query(strPrevContBelowCntr,new ExportContainerBayViewService.DynamicSize());

                        }
                        if(belowCenterList.size()>0 && importCotainerBayViewModel.getPaired()==0 && belowCenterList.get(0).getSize()>20  && mystat==0 && bayState>0){
                            Integer size4=belowCenterList.get(0).getSize();
                            dynamicBelowModel.setCenterClassTypeTable2("gridcolor");
                            dynamicBelowModel.setResultCenterPodSizeTable2(size4+"'");

                        }
                        else{
                            String strPosCentreBelow="";
                            strPosCentreBelow="select ctmsmis.mis_exp_unit.pod,sparcsn4.inv_unit.freight_kind,sparcsn4.inv_unit.id,cont_mlo as mlo,right(sparcsn4.ref_equip_type.nominal_length,2) as size, ceil((ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg/1000)) as tons,sparcsn4.ref_equip_type.iso_group as rfr_connect \n" +
                                    "\t\t\t\t\tfrom ctmsmis.mis_exp_unit \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                                    "\t\t\t\t\tinner JOIN sparcsn4.ref_bizunit_scoped r ON r.gkey=inv_unit.line_op \n" +
                                    "\t\t\t\t\tLEFT JOIN sparcsn4.ref_agent_representation x on r.gkey=x.bzu_gkey \n" +
                                    "\t\t\t\t\tLEFT JOIN sparcsn4.ref_bizunit_scoped y ON x.agent_gkey=y.gkey\n" +
                                    "\t\t\t\t\t\twhere ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"' and left(stowage_pos,2) in("+bay+") and right(stowage_pos,4)='"+posCentreBelow+"' and right(stowage_pos,2)<80 order by stowage_pos";
                            List<ImportCotainerBayViewModel>resPosCentreBelowList=secondaryDBTemplate.query(strPosCentreBelow,new ExportContainerBayViewService.DynamicSize2());
                            if(resPosCentreBelowList.size()>0){
                                dynamicBelowModel.setCenterClassTypeTable2("gridcolor");
                            }
                            else {
                                dynamicBelowModel.setCenterClassTypeTable2("grid");
                            }
                            if(resPosCentreBelowList.size()>0){
                                String rfrCtr="";
                                String freight_kindCtr="";
                                String txtCtr = "";
                                rfrCtr = resPosCentreBelowList.get(0).getRfr_connect();
                                freight_kindCtr =resPosCentreBelowList.get(0).getFreight_kind();
                                txtCtr = "";
                                if(rfrCtr.equals("RE")  || rfrCtr.equals("RS") || rfrCtr.equals("RT")  || rfrCtr.equals("HR") ){
                                    txtCtr = "R";
                                }

                                else if(freight_kindCtr.equals("MTY")){
                                    txtCtr = "E";
                                }

                                else if(freight_kindCtr.equals("FCL") || freight_kindCtr.equals("LCL")){
                                    txtCtr = "D";
                                }
                                String sBelowCenter =resPosCentreBelowList.get(0).getPod()+" "+txtCtr+resPosCentreBelowList.get(0).getSize()+"'\n";
                                dynamicBelowModel.setResultCenterPodSizeTable2(sBelowCenter);
                                dynamicBelowModel.setResultCenterSIdTable2(resPosCentreBelowList.get(0).getsId()+"'\n");
                                dynamicBelowModel.setResultCenterMloTonsTable2(resPosCentreBelowList.get(0).getMlo()+resPosCentreBelowList.get(0).getTons());
                                tonsB0=tonsB0+resPosCentreBelowList.get(0).getTons();

                            }
                        }

                    }
                    else if(importCotainerBayViewModel.getCenterLineB()==0 && importCotainerBayViewModel.getGapLineB()==1){
                        dynamicBelowModel.setStateTable2(2);

                    }


                    Integer lBelow = 1;
                    Integer rcLimitBelow=0;
                    if(maxCol%2==0){
                        rcLimitBelow = maxCol-1;
                    }

                    else{
                        rcLimitBelow = maxCol;
                    }
                    List<ImportCotainerBayViewModel> dynamicRightBelowList=new ArrayList<>();
                    while(lBelow<=rcLimitBelow){
                        ImportCotainerBayViewModel dynamicRightBelowModel=new ImportCotainerBayViewModel();
                        String posRightBelow="";
                        if(b<10){
                            posRightBelow = "0"+lBelow+"0"+b;
                        }

                        else{
                            posRightBelow = "0"+lBelow+b;
                        }
                        List<ImportCotainerBayViewModel> resPrevContBelowRightList=new ArrayList<>();

                        if(importCotainerBayViewModel.getPaired()==0){
                            Integer rbyBelowRight1 = importCotainerBayViewModel.getBay()-2;
                            Integer rbyBelowRight2 = importCotainerBayViewModel.getBay()-1;
                            String  rbyBelowRight12="";
                            String  rbyBelowRight22="";
                            String slotBelowRight1="";
                            String slotBelowRight2="";
                            String strPrevContBelowRight="";
                            if(rbyBelowRight1<10){
                                rbyBelowRight12 = "0"+rbyBelowRight1;
                            }

                            else{
                                rbyBelowRight12 = ""+rbyBelowRight1;
                            }


                            if(rbyBelowRight2<10){
                                rbyBelowRight22 = "0"+rbyBelowRight2;
                            }

                            else{
                                rbyBelowRight22 = ""+rbyBelowRight2;
                            }

                            slotBelowRight1 = rbyBelowRight12+posRightBelow;
                            slotBelowRight2 = rbyBelowRight22+posRightBelow;
                            strPrevContBelowRight="select right(sparcsn4.ref_equip_type.nominal_length,2) as size from ctmsmis.mis_exp_unit \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                                    "\t\t\t\t\twhere (stowage_pos='"+slotBelowRight1+"' or stowage_pos='"+slotBelowRight2+"') and ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"'";
                            resPrevContBelowRightList=secondaryDBTemplate.query(strPrevContBelowRight,new ExportContainerBayViewService.DynamicSize());
                        }
                        if(resPrevContBelowRightList.size()>0 && importCotainerBayViewModel.getPaired()==0 && resPrevContBelowRightList.get(0).getSize()>20 && lBelow <= maxColLimitBelow-1  && mystat==0 && bayState>0 ){
                            Integer size5=resPrevContBelowRightList.get(0).getSize();
                            dynamicRightBelowModel.setClassType("gridcolor");
                            dynamicRightBelowModel.setResultpodSize(size5+"'");



                        }
                        else{
                            String strPosRightBelow="";
                            strPosRightBelow="select ctmsmis.mis_exp_unit.pod,sparcsn4.inv_unit.freight_kind,sparcsn4.inv_unit.id,cont_mlo as mlo,right(sparcsn4.ref_equip_type.nominal_length,2) as size, ceil((ctmsmis.mis_exp_unit.goods_and_ctr_wt_kg/1000)) as tons,sparcsn4.ref_equip_type.iso_group as rfr_connect \n" +
                                    "\t\t\t\t\tfrom ctmsmis.mis_exp_unit \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit on sparcsn4.inv_unit.gkey=ctmsmis.mis_exp_unit.gkey \n" +
                                    "\t\t\t\t\tinner join sparcsn4.inv_unit_equip on sparcsn4.inv_unit_equip.unit_gkey=sparcsn4.inv_unit.gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equipment on sparcsn4.ref_equipment.gkey=sparcsn4.inv_unit_equip.eq_gkey\n" +
                                    "\t\t\t\t\tinner join sparcsn4.ref_equip_type on sparcsn4.ref_equip_type.gkey=sparcsn4.ref_equipment.eqtyp_gkey\n" +
                                    "\t\t\t\t\tinner JOIN sparcsn4.ref_bizunit_scoped r ON r.gkey=inv_unit.line_op \n" +
                                    "\t\t\t\t\tLEFT JOIN sparcsn4.ref_agent_representation x on r.gkey=x.bzu_gkey \n" +
                                    "\t\t\t\t\tLEFT JOIN sparcsn4.ref_bizunit_scoped y ON x.agent_gkey=y.gkey\n" +
                                    "\t\t\t\t\twhere ctmsmis.mis_exp_unit.vvd_gkey='"+vvdgkey+"' and left(stowage_pos,2) in("+bay+") and right(stowage_pos,4)=right('"+posRightBelow+"',4) and right(stowage_pos,2)<80 order by stowage_pos";
                            List<ImportCotainerBayViewModel> resPosRightBelowList=secondaryDBTemplate.query(strPosRightBelow,new ExportContainerBayViewService.DynamicSize2());
                            if(resPosRightBelowList.size()>0 && lBelow <= maxColLimitBelow-1) {
                                dynamicRightBelowModel.setClassType("gridcolor");
                            }
                            else if(upGapValArrBelowList.contains(posRightBelow)){
                                dynamicRightBelowModel.setClassType("nogrid");

                            }
                            else if(lBelow >maxColLimitBelow){
                                dynamicRightBelowModel.setClassType("nogrid");
                            }
                            else{
                                dynamicRightBelowModel.setClassType("grid");
                            }
                            if(resPosRightBelowList.size()>0 && lBelow <=maxColLimitBelow-1){
                                String rfrBlw="";
                                String freight_kindBlw="";
                                String   txtBlw = "";
                                rfrBlw = resPosRightBelowList.get(0).getRfr_connect();
                                freight_kindBlw = resPosRightBelowList.get(0).getFreight_kind();

                                if(rfrBlw.equals("RE")  || rfrBlw.equals("RS")  || rfrBlw.equals("RT")  || rfrBlw.equals("HR") ){
                                    txtBlw = "R";
                                }

                                else if(freight_kindBlw.equals("MTY")){
                                    txtBlw = "E";
                                }

                                else if(freight_kindBlw.equals("FCL") || freight_kindBlw.equals("LCL")){
                                    txtBlw = "D";
                                }

                                String sRinghtBelow =resPosRightBelowList.get(0).getPod()+" "+txtBlw+resPosRightBelowList.get(0).getSize()+"'\n";
                                dynamicRightBelowModel.setResultpodSize(sRinghtBelow);
                                dynamicRightBelowModel.setResultSId(resPosRightBelowList.get(0).getsId()+"'\n");
                                dynamicRightBelowModel.setResultMloTons(resPosRightBelowList.get(0).getMlo()+resPosRightBelowList.get(0).getTons()+"Ts");
                                table1tonRow2=table1tonRow2+resPosRightBelowList.get(0).getTons();
                                if(totalTable2Ton.get("tonsB"+lBelow)!=null){
                                    totalTable2Ton.put("tonsB"+lBelow,totalTable2Ton.get("tonsB"+lBelow)+resPosRightBelowList.get(0).getTons());
                                }
                                else{
                                    totalTable2Ton.put("tonsB"+lBelow,resPosRightBelowList.get(0).getTons());
                                }

                            }



                        }
                        lBelow = lBelow+2;
                        dynamicRightBelowList.add(dynamicRightBelowModel);
                    }
                    if(b<10){
                        dynamicBelowModel.setDynamicRightRowSl("0"+b);

                    }
                    else{
                        dynamicBelowModel.setDynamicRightRowSl(""+b);

                    }

                    dynamicBelowModel.setDynamicInnerBelowList(daynamicLeftBelowList);
                    dynamicBelowModel.setDynamicRightRowBelowList(dynamicRightBelowList);
                    dynamicBelowList.add(dynamicBelowModel);
                    b = b-2;
                }
                resultModel.setDynamicRowListBelow(dynamicBelowList);

                String strBlDeckMC="";
                Integer mc=0;
                Integer kbelowT=0;
                List<ImportCotainerBayViewModel> mcBelowList=new ArrayList<>();

                strBlDeckMC="select max(maxColLimit) as mc from ctmsmis.misBayViewBelow where vslId='"+vesselId+"' and bay='"+importCotainerBayViewModel.getBay()+"' and bayRow<80";
                mcBelowList=secondaryDBTemplate.query(strBlDeckMC,new ExportContainerBayViewService.Mc());
                if(mcBelowList.size()>0){
                    mc=mcBelowList.get(0).getMc();
                }
                if(maxCol%2==1)
                {
                    kbelowT = maxCol-1;
                }
                else
                {
                    kbelowT = maxCol;
                }
                if(importCotainerBayViewModel.getCenterLineA()==0 && importCotainerBayViewModel.getCenterLineB()!=0){
                    kbelowT = kbelowT-2;
                }

                else if(importCotainerBayViewModel.getCenterLineA()==0 && importCotainerBayViewModel.getCenterLineB()==0){
                    kbelowT = kbelowT;
                }

                List<ImportCotainerBayViewModel> table2TonsList=new ArrayList<>();
                while(kbelowT>=2){
                    ImportCotainerBayViewModel table2TonsModel1=new ImportCotainerBayViewModel();
                    if(kbelowT <=mc){
                        if(totalTable2Ton.get("tonsB"+kbelowT)!=null){
                            table2TonsModel1.setTable2TotalTons(totalTable2Ton.get("tonsB"+kbelowT)+"Ts");
                        }
                        else{
                            table2TonsModel1.setTable2TotalTons("0Ts");
                        }

                    }
                    else{
                        table2TonsModel1.setTable2TotalTons("");

                    }

                    kbelowT = kbelowT-2;
                    table2TonsList.add(table2TonsModel1);

                }
                resultModel.setTable2TonsList(table2TonsList);

                if(importCotainerBayViewModel.getCenterLineB()==1){
                    resultModel.setTable2TotalSate(1);
                    resultModel.setTable2Ton(tonsB0+"Ts");
                }
                else if(importCotainerBayViewModel.getCenterLineB()==0 && importCotainerBayViewModel.getGapLineB()==1){
                    resultModel.setTable2TotalSate(2);
                }


                Integer lBelowT = 1;
                Integer rcLimitBelowT=0;
                //echo $l;
                if(maxCol%2==0){
                    rcLimitBelowT = maxCol-1;
                }

                else{
                    rcLimitBelowT = maxCol;
                }

                List<ImportCotainerBayViewModel> table2TonsList2=new ArrayList<>();
                while(lBelowT<=rcLimitBelowT){
                    ImportCotainerBayViewModel table2TonsModel2=new ImportCotainerBayViewModel();
                    if(lBelowT <=mc-1){
                        if(totalTable2Ton.get("tonsB"+lBelowT)!=null){
                            table2TonsModel2.setTable2TotalTons(totalTable2Ton.get("tonsB"+lBelowT)+"Ts");
                        }
                        else{
                            table2TonsModel2.setTable2TotalTons("0Ts");
                        }

                    }
                    else{
                        table2TonsModel2.setTable2TotalTons("");

                    }
                    lBelowT = lBelowT+2;
                    table2TonsList2.add(table2TonsModel2);
                }
                resultModel.setTable2TonsList2(table2TonsList2);
                Integer klVal=0;
                if(maxCol%2==1)
                {
                    klVal = maxCol-1;
                }
                else
                {
                    klVal = maxCol;
                }
                if(importCotainerBayViewModel.getCenterLineA()==0 && importCotainerBayViewModel.getCenterLineB()!=0){
                    klVal = klVal-2;
                }
                if(importCotainerBayViewModel.getCenterLineA()==0 && importCotainerBayViewModel.getCenterLineB()==0){
                    klVal = klVal;

                }
                List<ImportContainerBayViewTempModel> belowFirstList= new ArrayList<>();
                while(klVal>=02){
                    ImportContainerBayViewTempModel belowFirstListModel=new ImportContainerBayViewTempModel();
                    if(klVal<10){
                        belowFirstListModel.setBelowKlValue("0"+klVal);

                    }
                    else{
                        belowFirstListModel.setBelowKlValue(""+klVal);
                    }
                    klVal = klVal-2;
                    belowFirstList.add(belowFirstListModel);

                }
                resultModel.setBelowFirstList(belowFirstList);

                Integer llValue = 1;
                Integer rBelowLimit=0;
                if(maxCol%2==0){
                    rBelowLimit = maxCol-1;
                }

                else{
                    rBelowLimit = maxCol;
                }
                if(importCotainerBayViewModel.getCenterLineA()==0 && importCotainerBayViewModel.getCenterLineB()!=0){
                    rBelowLimit = rBelowLimit-2;
                }

                if(importCotainerBayViewModel.getCenterLineA()==0 && importCotainerBayViewModel.getCenterLineB()==0){
                    rBelowLimit = rBelowLimit;
                }

                List<ImportContainerBayViewTempModel> belowLastList= new ArrayList<>();
                while(llValue<=rBelowLimit){
                    ImportContainerBayViewTempModel belowLastListModel=new ImportContainerBayViewTempModel();
                    if(llValue<10){
                        belowLastListModel.setBelowllValue("0"+llValue);

                    }
                    else{
                        belowLastListModel.setBelowllValue(""+llValue);

                    }
                    llValue = llValue+2;
                    belowLastList.add(belowLastListModel);

                }
                resultModel.setBelowLastList(belowLastList);



            }




            bayViewList.add(resultModel);
        }




        return bayViewList;
    }
    class VvdGkey implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel.setVvd_gkey(rs.getInt("vvd_gkey"));
            return importCotainerBayViewModel;
        }
    }
    class VesselList implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel.setVvd_gkey(rs.getInt("vvd_gkey"));
            importCotainerBayViewModel.setVsl(rs.getString("vsl"));
            return importCotainerBayViewModel;
        }
    }

    class VesselInfo implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel.setVessel_id(rs.getString("id"));
            importCotainerBayViewModel.setName(rs.getString("name"));
            importCotainerBayViewModel.setIb_vyg(rs.getString("ib_vyg"));
            importCotainerBayViewModel.setAta(rs.getString("ata"));
            importCotainerBayViewModel.setAtd(rs.getString("atd"));
            return importCotainerBayViewModel;
        }
    }
    class MisBayView implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel.setId(rs.getInt("id"));
            importCotainerBayViewModel.setVslId(rs.getString("vslId"));
            importCotainerBayViewModel.setBay(rs.getInt("bay"));
            importCotainerBayViewModel.setPaired(rs.getInt("paired"));
            importCotainerBayViewModel.setPairedWith(rs.getInt("pairedWith"));
            importCotainerBayViewModel.setCenterLineA(rs.getInt("centerLineA"));
            importCotainerBayViewModel.setGapLineA(rs.getInt("gapLineA"));
            importCotainerBayViewModel.setIsBelow(rs.getInt("isBelow"));
            importCotainerBayViewModel.setCenterLineB(rs.getInt("centerLineB"));
            importCotainerBayViewModel.setGapLineB(rs.getInt("gapLineB"));
            importCotainerBayViewModel.setMinRowLimAbv(rs.getInt("minRowLimAbv"));
            importCotainerBayViewModel.setMinRowLimBlw(rs.getInt("minRowLimBlw"));
            importCotainerBayViewModel.setMaxRowLimBlw(rs.getInt("maxRowLimBlw"));
            importCotainerBayViewModel.setMaxRowLimAbv(rs.getInt("maxRowLimAbv"));
            importCotainerBayViewModel.setGapUpperRow(rs.getString("gapUpperRow"));
            importCotainerBayViewModel.setGapLowerRow(rs.getString("gapLowerRow"));
            return importCotainerBayViewModel;
        }

    }

    class CheckBay implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel.setCnt(rs.getInt("cnt"));

            return importCotainerBayViewModel;
        }
    }
    class BaySate implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel.setPaired(rs.getInt("paired"));
            return importCotainerBayViewModel;
        }
    }
    class MaxCol implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel.setMaxCol(rs.getInt("maxCol"));
            return importCotainerBayViewModel;
        }
    }
    class MaxAndMInCOlLImit implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel.setMaxColLimit(rs.getInt("maxColLimit"));
            importCotainerBayViewModel.setMinColLimit(rs.getInt("minColLimit"));
            return importCotainerBayViewModel;
        }
    }
    class DynamicSize implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel.setSize(rs.getInt("size"));


            return importCotainerBayViewModel;
        }
    }
    class DynamicSize2 implements  RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel.setSize(rs.getInt("size"));
            importCotainerBayViewModel.setsId(rs.getString("id"));
            importCotainerBayViewModel.setPod(rs.getString("pod"));
            importCotainerBayViewModel.setFreight_kind(rs.getString("freight_kind"));
            importCotainerBayViewModel.setMlo(rs.getString("mlo"));
            importCotainerBayViewModel.setTons(rs.getInt("tons"));
            importCotainerBayViewModel.setRfr_connect(rs.getString("rfr_connect"));
            return importCotainerBayViewModel;
        }
    }
    class Mc implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImportCotainerBayViewModel importCotainerBayViewModel=new ImportCotainerBayViewModel();
            importCotainerBayViewModel.setMc(rs.getInt("mc"));
            return importCotainerBayViewModel;
        }
    }
}
