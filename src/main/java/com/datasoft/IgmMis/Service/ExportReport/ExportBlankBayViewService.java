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
import java.util.stream.Collectors;
@Service
public class ExportBlankBayViewService {
    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;

    @Autowired
    @Qualifier("jdbcTemplateOracle")
    private JdbcTemplate OracleDbTemplate;
    

    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;

    public List getVesselList(){
        String sqlQuery="";
        sqlQuery="select distinct vsl_vessel_visit_details.vvd_gkey,CONCAT((vsl_vessels.name),CONCAT('-',(vsl_vessel_visit_details.ib_vyg)) ) AS vsl\n" +
                "from vsl_vessel_visit_details\n" +
                "inner join argo_carrier_visit on argo_carrier_visit.cvcvd_gkey=vsl_vessel_visit_details.vvd_gkey\n" +
                "inner join vsl_vessels on vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "where argo_carrier_visit.phase not in('80CANCELED','70CLOSED')";
        List resultList=OracleDbTemplate.query(sqlQuery,new ExportBlankBayViewService.VesselList());
        List listAll = (List) resultList.stream().collect(Collectors.toList());
        return listAll;
    }
    public List getBlankBayVesselInfo(Integer vvdgkey) {
        List<ImportCotainerBayViewModel> vesselInfoList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="select vsl_vessels.id,vsl_vessels.name\n" +
                "from vsl_vessel_visit_details\n" +
                "inner join vsl_vessels on vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "where vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";

        vesselInfoList=OracleDbTemplate.query(sqlQuery,new ExportBlankBayViewService.VesselInfo());
        return vesselInfoList;

    }
    public List getBlankBayViewList(Integer vvdgkey){
        List<ImportContainerBayViewMainModel> bayViewList=new ArrayList<>();
        List<ImportCotainerBayViewModel> resultList=new ArrayList<>();
        List<ImportCotainerBayViewModel> vesselInfoList=new ArrayList<>();
        String sqlQuery="";
        sqlQuery="select vsl_vessels.id,vsl_vessels.name\n" +
                "from vsl_vessel_visit_details\n" +
                "inner join vsl_vessels on vsl_vessels.gkey=vsl_vessel_visit_details.vessel_gkey\n" +
                "where vsl_vessel_visit_details.vvd_gkey='"+vvdgkey+"'";


        vesselInfoList=OracleDbTemplate.query(sqlQuery,new ExportBlankBayViewService.VesselInfo());
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
        resultList=primaryDBTemplate.query(sqlQuery,new ExportBlankBayViewService.MisBayView());
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
               // bay = title1+","+title2;
            }
            else{
                if(importCotainerBayViewModel.getBay()<10){
                    title = "0"+importCotainerBayViewModel.getBay();

                }
                else{
                    title = ""+importCotainerBayViewModel.getBay();

                }
               // bay=title;
            }


            String strMaxColQuery="";
            strMaxColQuery = "select max(maxColLimit) as maxCol from misBayViewBelow where vslId='"+vesselId+"' and bay='"+importCotainerBayViewModel.getBay()+"'";
            List<ImportCotainerBayViewModel> maxColList=primaryDBTemplate.query(strMaxColQuery,new ExportBlankBayViewService.MaxCol());
            Integer maxCol=0;

            if(maxColList.size()>0){
                maxCol=maxColList.get(0).getMaxCol();
            }

            resultModel.setMaxCol(maxCol);
            resultModel.setTitle(title);
            String  strUpDeckLblQuery="";
            strUpDeckLblQuery = "select minColLimit,maxColLimit from misBayViewBelow where vslId='"+vesselId+"' and bay='"+importCotainerBayViewModel.getBay()+"'";
            List<ImportCotainerBayViewModel> maxAndMInColList=primaryDBTemplate.query(strUpDeckLblQuery,new ExportBlankBayViewService.MaxAndMInCOlLImit());
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

                List<ImportCotainerBayViewModel> resUpDeckList=primaryDBTemplate.query(strUpDeck,new ExportBlankBayViewService.MaxAndMInCOlLImit());
                System.out.println(resUpDeckList.size());
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

                    List<ImportCotainerBayViewModel> dynamicSizeList=new ArrayList<>();
                    if(minColLimitUp!=0){

                            if(k >maxColLimitUp){

                                dynamicInnerModel.setClassType("nogrid");
                            }
                            else if(upGapValList.contains(gapStr)){

                                dynamicInnerModel.setClassType("nogrid");

                            }

                            else{
                                dynamicInnerModel.setClassType("grid");
                            }
                    }

                    dynamicRowInnerList.add(dynamicInnerModel);
                    k=k-2;
                }


                if(importCotainerBayViewModel.getCenterLineA()==1 && !(upGapValList.contains("00"+ivalue))){
                    daynamicRowModel.setState(1);
                    daynamicRowModel.setCenterClassType("grid");

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

                    if(lValue > maxColLimitUp){
                            dynamicListLastModel.setClassType("nogrid");

                        }
                        else if(upGapValList.contains(gapStrRisht)){
                            dynamicListLastModel.setClassType("nogrid");
                        }
                        else {
                            dynamicListLastModel.setClassType("grid");

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
                    strBlDeck="select minColLimit,maxColLimit from misBayViewBelow where vslId='"+vesselId+"' and bay='"+importCotainerBayViewModel.getBay()+"' and bayRow='"+b+"'";
                    resBlDeck=primaryDBTemplate.query(strBlDeck,new ExportBlankBayViewService.MaxAndMInCOlLImit());
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

                    List<ImportCotainerBayViewModel> daynamicLeftBelowList=new ArrayList<>();
                    String bb="";
                    while(kbelow>=02){
                        //bb="";

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


                        if(kbelow >maxColLimitBelow){
                            daynamicLeftBelowModel.setClassType("nogrid");
                        }
                        else if(upGapValArrBelowList.contains(posbelow)){
                                daynamicLeftBelowModel.setClassType("nogrid");
                            }
                        else{
                                daynamicLeftBelowModel.setClassType("grid");
                            }


                        kbelow = kbelow-2;
                        daynamicLeftBelowList.add(daynamicLeftBelowModel);

                    }


                    if(importCotainerBayViewModel.getCenterLineB()==1){
                        dynamicBelowModel.setStateTable2(1);
                        if(b<10){
                            if(upGapValArrBelowList.contains("000"+b)){


                                dynamicBelowModel.setCenterClassTypeTable2("nogrid");

                            }
                            else {

                                dynamicBelowModel.setCenterClassTypeTable2("grid");
                            }

                        }
                        else{
                            if(upGapValArrBelowList.contains("00"+b)){


                                dynamicBelowModel.setCenterClassTypeTable2("nogrid");

                            }
                            else {

                                dynamicBelowModel.setCenterClassTypeTable2("grid");
                            }

                        }

                     /*   if(upGapValArrBelowList.contains("00"+b)){


                            dynamicBelowModel.setCenterClassTypeTable2("nogrid");

                        }
                        else {

                            dynamicBelowModel.setCenterClassTypeTable2("grid");
                        }*/



                    }
                    else if(importCotainerBayViewModel.getCenterLineB()==0 && importCotainerBayViewModel.getGapLineB()==1){
                        dynamicBelowModel.setStateTable2(2);
                        dynamicBelowModel.setCenterClassTypeTable2("nogrid");

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
                        String gapStrBR="";
                        String posRightBelow="";
                        ImportCotainerBayViewModel dynamicRightBelowModel=new ImportCotainerBayViewModel();

                        if(lBelow<10){
                            posRightBelow = "0"+lBelow;
                        }

                        else{
                            posRightBelow = "0"+lBelow;
                        }
                        gapStrBR = posRightBelow+bb;
                        List<ImportCotainerBayViewModel> resPrevContBelowRightList=new ArrayList<>();

                            if(lBelow >maxColLimitBelow){
                                dynamicRightBelowModel.setClassType("nogrid");
                            }

                            else if(upGapValArrBelowList.contains(gapStrBR)){
                                dynamicRightBelowModel.setClassType("nogrid");

                            }

                            else{
                                dynamicRightBelowModel.setClassType("grid");
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
    class VvdGkey implements RowMapper {
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
