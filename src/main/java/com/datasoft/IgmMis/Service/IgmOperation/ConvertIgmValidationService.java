package com.datasoft.IgmMis.Service.IgmOperation;

import com.datasoft.IgmMis.Model.IgmOperation.ConvertIgmModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.lang.*;



@Service
public class ConvertIgmValidationService {

    @Autowired
    @Qualifier("jdbcTemplateSecondary")
    private JdbcTemplate secondaryDBTemplate;
    private final Path fileStorageLocation;

    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;


    @Autowired
    public ConvertIgmValidationService(Environment env){
        this.fileStorageLocation = Paths.get(env.getProperty("app.file.convert-pangaon-container", "./uploads/ConvertIgm/xml"))
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }



    public List convertIgmContainer(String Rotation) throws IOException {
        List<ConvertIgmModel> resultList = new ArrayList<>();
        ConvertIgmModel resultModel = new ConvertIgmModel();
        ConvertIgmModel subBlModel = new ConvertIgmModel();

        String strMainQuery = "";
        String igm_master_id = "";
        String igm_detail_id = "";
        String igm_sup_detail_id = "";
        Integer rowi = 0;
        String row_igm_detail_id = "";
        String igm_sub_detail_id = "";
        String vessel_name = "";
        String Import_rotation = "";
        String my = "";
        String myrot = "";


//        my = Rotation.substring(0, 1);
//        myrot = Rotation.substring(4, 1);


//        if(my=="R" || my=="r" || myrot=="/") {
//    }

        strMainQuery = "select id from igm_masters where Import_Rotation_No='" + Rotation + "'";
        resultList = primaryDBTemplate.query(strMainQuery, new IgmMasterId());
        List<ConvertIgmModel> resultSubList = new ArrayList<>();

        if (resultList.size() > 0) {
            igm_master_id = resultList.get(0).getId();
        }

        List<ConvertIgmModel> igmSupplimentary = new ArrayList<>();

        strMainQuery = "SELECT DISTINCT igm_details.id FROM igm_details INNER JOIN igm_detail_container ON igm_detail_container.igm_detail_id=igm_details.id \n" +
                "LEFT JOIN igm_supplimentary_detail ON igm_supplimentary_detail.igm_detail_id=igm_details.id \n" +
                "LEFT JOIN igm_sup_detail_container ON igm_sup_detail_container.igm_sup_detail_id=igm_supplimentary_detail.id \n" +
                "WHERE (igm_details.PFstatus=1 OR igm_details.PFstatus=10 OR igm_details.PFstatus=2) AND IGM_id='" + igm_master_id + "'  AND igm_detail_container.cont_number NOT IN \n" +
                "(SELECT cont_number FROM igm_sup_detail_container INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_sup_detail_container.igm_sup_detail_id  WHERE igm_master_id='" + igm_master_id + "')\t\t\n" +
                "\t\t\t";

        igmSupplimentary = primaryDBTemplate.query(strMainQuery, new IgmDetailId());

        for (int j = 0; j < igmSupplimentary.size(); j++) {
            igm_detail_id = igmSupplimentary.get(j).getId();


            String stri = "select igm_detail_id from igm_for_ctms where igm_detail_id='" + igm_detail_id + "' and data_type='igm'";
            resultList = primaryDBTemplate.query(stri, new row_igm_details_id());
            if (!(rowi.equals(resultList.size()))) {

                String igm_for_ctms_copy = "insert into igm_for_ctms_copy(igm_detail_id,update_datetime,data_type) values('" + igm_detail_id + "',now(),'igm')";
            }


        }


        strMainQuery = "select id from igm_supplimentary_detail where (PFstatus=1 or PFstatus=10 or PFstatus=2) and igm_master_id='" + igm_master_id + "'";

        igmSupplimentary = primaryDBTemplate.query(strMainQuery, new IgmMasterId());
        for (int j = 0; j < igmSupplimentary.size(); j++) {
            igm_sup_detail_id = igmSupplimentary.get(j).getId();
            String stri = "select igm_sub_detail_id from igm_for_ctms where igm_sub_detail_id='" + igm_sup_detail_id + "' and data_type='igm'";
            resultList = primaryDBTemplate.query(stri, new row_igm_sub_detail_id());


            if (!(rowi.equals(resultList.size()))) {

                String igm_for_ctms_copy = "insert into igm_for_ctms_copy(igm_sub_detail_id,update_datetime,data_type) values('" + igm_sub_detail_id + "',now(),'igm')";
            }


        }


//        Integer yes = 0;
//
//        String str22 = "UPDATE igm_for_ctms_copy \n" +
//                "INNER JOIN igm_details ON igm_details.id=igm_for_ctms_copy.igm_detail_id\n" +
//                "SET igm_for_ctms_copy.igm_master_id=igm_details.IGM_id\n" +
//                "WHERE igm_for_ctms_copy.igm_master_id IS NULL";
//
//        yes = primaryDBTemplate.update(str22);
//        System.out.println("Master id (IGM) Success<br> " + yes);
//
//
//        String str33 = "UPDATE igm_for_ctms_copy\n" +
//                "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_for_ctms_copy.igm_sub_detail_id\n" +
//                "SET igm_for_ctms_copy.igm_master_id=igm_supplimentary_detail.igm_master_id\n" +
//                "WHERE igm_for_ctms_copy.igm_master_id IS NULL";
//
//        yes = primaryDBTemplate.update(str33);
//        System.out.println("Master id (IGM Supplimentary) Success<br> " + yes);
//
//
//        String str = "UPDATE igm_for_ctms_copy\n" +
//                "INNER JOIN igm_details ON igm_details.id=igm_for_ctms_copy.igm_detail_id\n" +
//                "SET igm_for_ctms_copy.Rotation_no=igm_details.Import_Rotation_No,\n" +
//                "igm_for_ctms_copy.BL_No=igm_details.BL_No\n" +
//                "WHERE igm_for_ctms_copy.igm_master_id='" + igm_master_id + "'";
//
//        yes = primaryDBTemplate.update(str);
//        System.out.println("IGM Success<br> " + yes);
//
//
//        String str1 = "UPDATE igm_for_ctms_copy \n" +
//                "INNER JOIN igm_supplimentary_detail ON igm_supplimentary_detail.id=igm_for_ctms_copy.igm_sub_detail_id\n" +
//                "SET igm_for_ctms_copy.Rotation_no=igm_supplimentary_detail.Import_Rotation_No,\n" +
//                "igm_for_ctms_copy.BL_No=  REPLACE(REPLACE(SUBSTRING_INDEX(igm_supplimentary_detail.BL_No,_latin1'*',-(1)) ,' ',''),'\t','')\n" +
//                "WHERE igm_for_ctms_copy.igm_master_id='" + igm_master_id + "'";
//
//
//        yes = primaryDBTemplate.update(str1);
//        System.out.println("IGM Supplimentary Success " + yes);
//

        String myFile_old = "";

        String masterid = igm_master_id;

        String igm_master_name = "select Vessel_Name,Import_Rotation_No from igm_masters where id='" + masterid + "'";


        resultList = primaryDBTemplate.query(igm_master_name, new row_igm_master_for_vessel_name());
        if (resultList.size() > 0) {

            ConvertIgmModel convertIgmMaster = new ConvertIgmModel();
            ConvertIgmModel convertIgmMasterMainModel = new ConvertIgmModel();


            resultModel.setVessel_Name(convertIgmMasterMainModel.getVessel_Name());
            resultModel.setImport_Rotation_No(convertIgmMasterMainModel.getImport_Rotation_No());


            vessel_name = resultList.get(0).getVessel_Name();
            resultModel.setVessel_Name(vessel_name);
            Import_rotation = resultList.get(0).getImport_Rotation_No();
            resultModel.setImport_Rotation_No(Import_rotation);


            String[] rotation = Import_rotation.split("/");
            String rot1 = rotation[0];
            String rot2 = rotation[1];

            String rotno = rot1 + rot2;


            String file_old = rotno + "_" + vessel_name;
            myFile_old = file_old + ".xml";

        }

        Integer filegenerated = 0;
        Integer loopgnrt = 0;
        Integer newfile = 0;
        String igm_masters = "";
        String igm_master_id_old = "";
        InputStream tmpStrem;
        String Pack_Number = "";
        String Pack_Description = "";
        String Pack_Marks_Number = "";
        String Description_of_Goods = "";

        String GetigmInfo = "";
        Integer weight = 0;
        String weight_unit = "";
        String IGM_id = "";
        String mlocode = "";
        String portId = "";

        String cont_number = "";
        String cont_gross_weight = "";
        String cont_status = "";
        String cont_iso_type = "";
        String commudity_code = "";
        String off_dock_id = "";
        String cont_seal_number = "";
        String Igm_sub_detail_id = "";
        String ConsigneeDesc = "";
        String Submitee_Org_Id = "";
        String type_of_igm = "";
        String blCategory = "";
        String Voy_No = "";
        String Port_of_Shipment = "";
        String Port_Ship_ID = "";
        String offdock = "";
        String blno = "";

        double weight1 = 0;
        String getsubmitee = "";
        String getvesselInfo = "";
        Integer igm_gross_weight = 0;
        String cont_status_final = "";
        String stringData0 = "";
        String igmdetailid = "";
        String igmsubdetailid = "";

        String stringData = "";


        stringData = stringData + "<edi:blTransactions xmlns:edi='http://www.navis.com/argo' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>" + "\n";



        String sql2 = "select id,igm_master_id,igm_detail_id,igm_sub_detail_id,Rotation_no,BL_No from igm_for_ctms \n" +
                "\t\t\t\t\twhere  data_type='igm' and igm_master_id='" + masterid + "'";
        List<ConvertIgmModel> strMainList = primaryDBTemplate.query(sql2, new row_igm_master_details());

        ConvertIgmModel convertIgmMasterModel = new ConvertIgmModel();


        for (int j = 0; j < strMainList.size(); j++) {

            convertIgmMasterModel = strMainList.get(j);
            loopgnrt = loopgnrt + 1;
            Integer id1 = convertIgmMasterModel.getIds();
            resultModel.setIds(id1);


            igmdetailid = convertIgmMasterModel.getIgm_detail_id();


            igm_master_id = convertIgmMasterModel.getIgm_master_id();
            igmsubdetailid = convertIgmMasterModel.getIgm_sub_detail_id();


            String Rotation_no = convertIgmMasterModel.getRotation_no();
            String BL_No = convertIgmMasterModel.getBL_No();
            resultModel.setIgm_detail_id(igmdetailid);
            resultModel.setIgm_sub_detail_id(igmsubdetailid);
            resultModel.setRotation_no(Rotation_no);
            resultModel.setBL_No(BL_No);

            String[] rotation = Rotation_no.split("/");
            String rot1 = rotation[0];
            String rot2 = rotation[1];

            String rotno = rot1 + rot2;

            String file_old = rotno + "_" + vessel_name;
            myFile_old = file_old + ".xml";


            if (igmdetailid != null) {


                String igm_master = "SELECT Pack_Number,Pack_Description,Pack_Marks_Number,Description_of_Goods,\n" +
                        "weight,weight_unit,IGM_id,mlocode,ConsigneeDesc,Submitee_Org_Id,type_of_igm FROM igm_details \n" +
                        "WHERE id='" + convertIgmMasterModel.getIgm_detail_id() + "'";
                List<ConvertIgmModel> strList = primaryDBTemplate.query(igm_master, new row_igm_detail_Information());

                if (strList.size() > 0) {

                    subBlModel = strList.get(0);
                    Pack_Number = subBlModel.getPack_Number();
                    resultModel.setPack_Number(subBlModel.getPack_Number());
                    Pack_Description = subBlModel.getPack_Description();
                    resultModel.setPack_Description(subBlModel.getPack_Description());
                    Pack_Marks_Number = subBlModel.getPack_Marks_Number();
                    resultModel.setPack_Marks_Number(subBlModel.getPack_Marks_Number());
                    Description_of_Goods = subBlModel.getDescription_of_Goods();
                    resultModel.setDescription_of_Goods(Description_of_Goods);
                    weight = subBlModel.getWeight();
                    resultModel.setWeight(subBlModel.getWeight());
                    weight_unit = subBlModel.getWeight_unit();
                    resultModel.setWeight_unit(weight_unit);


                    type_of_igm = subBlModel.getType_of_igm();
                    resultModel.setType_of_igm(subBlModel.getType_of_igm());

                    ConsigneeDesc = subBlModel.getConsigneeDesc();
                    resultModel.setConsigneeDesc(subBlModel.getConsigneeDesc());

                    mlocode = subBlModel.getMlocode();
                    resultModel.setMlocode(subBlModel.getMlocode());
                    IGM_id = subBlModel.getIGM_id();
                    resultModel.setIGM_id(IGM_id);
                }


                String getvesselInf = "select Import_Rotation_No,Vessel_Name,Voy_No,Port_of_Shipment,Port_Ship_ID from igm_masters \n" +
                        "\t\t\t\t\t\t\twhere id='" + IGM_id + "'";
                List<ConvertIgmModel> strIgmMasterList = primaryDBTemplate.query(getvesselInf, new row_igm_master_Information());

                if (strIgmMasterList.size() > 0) {


                    ConvertIgmModel subModel = strIgmMasterList.get(0);
                    Import_rotation = subModel.getImport_Rotation_No();


                    Port_Ship_ID = subModel.getPort_Ship_ID();
                    resultModel.setPort_Ship_ID(subModel.getPort_Ship_ID());


                }


                if (type_of_igm == "TS") {
                    blCategory = "TRANSSHIP";
                    portId = "BDMGL";
                } else {
                    blCategory = "IMPORT";
                    portId = "BDCGP";
                }

                stringData = stringData + "<edi:blTransaction edi:msgClass=\"MANIFEST\" edi:msgFunction=\"9\" edi:msgReferenceNbr=\"000000001\" edi:msgTypeId=\"310\">\n";
                stringData = stringData + "<edi:Interchange edi:InterchangeReceipient=\"CPA\" edi:InterchangeSender=\"CPA\"/>\n";

                stringData = stringData + "<edi:ediBillOfLading edi:blCategory=\"" + blCategory + "\" edi:blNbr=\"" + BL_No + "\"/>\n";


                stringData = stringData + "<edi:ediVesselVisit edi:actualTimeArrival=\"\" edi:actualTimeDeparture=\"\" edi:estimatedTimeArrival=\"\" edi:estimatedTimeDeparture=\"\" edi:inVoyageNbr=\"" + Import_rotation + "\" edi:vesselId=\"" + vessel_name + "\" edi:vesselIdConvention=\"VESNAME\">\n";
                stringData = stringData + "<edi:shippingLine edi:shippingLineCode=\"" + mlocode + "\" edi:shippingLineCodeAgency=\"SCAC\"/>\n";
                stringData = stringData + "<edi:loadPort edi:portId=\"" + Port_Ship_ID + "\" edi:portIdConvention=\"UNLOCCODE\"/>\n";

                stringData = stringData + "</edi:ediVesselVisit>\n";
                stringData = stringData + "<edi:shipper edi:shipperName=\"\"/>\n";
                stringData = stringData + "<edi:consignee edi:consigneeName=\"\"/>\n";


                stringData = stringData + "<edi:dischargePort1 edi:portId=\"" + portId + "\" edi:portIdConvention=\"UNLOCCODE\"/>\n";

                stringData = stringData + "<edi:ediBlItemHolder>\n";


                String detailcont_offdid = "SELECT cont_number,cont_gross_weight,cont_status,cont_iso_type,commudity_code,off_dock_id,cont_seal_number\n" +
                        "FROM igm_detail_container WHERE igm_detail_id='" + igmdetailid + "'";
                List<ConvertIgmModel> strIgmDetailList = primaryDBTemplate.query(detailcont_offdid, new row_igm_detail_container_Information());
                for (int k = 0; k < strIgmDetailList.size(); k++) {


                    ConvertIgmModel convertIgmDetailMasterModel;
                    convertIgmDetailMasterModel = strIgmDetailList.get(k);
                    cont_gross_weight = convertIgmDetailMasterModel.getCont_gross_weight();
                    resultModel.setCont_gross_weight(convertIgmDetailMasterModel.getCont_gross_weight());

                    cont_iso_type = convertIgmDetailMasterModel.getCont_iso_type();
                    resultModel.setCont_iso_type(convertIgmDetailMasterModel.getCont_iso_type());


                    cont_number = convertIgmDetailMasterModel.getCont_number();
                    resultModel.setCont_number(convertIgmDetailMasterModel.getCont_number());

                    off_dock_id = convertIgmDetailMasterModel.getOff_dock_id();
                    resultModel.setOff_dock_id(convertIgmDetailMasterModel.getOff_dock_id());

                    cont_seal_number = convertIgmDetailMasterModel.getCont_seal_number();
                    resultModel.setCont_seal_number(convertIgmDetailMasterModel.getCont_seal_number());


                    commudity_code = convertIgmDetailMasterModel.getCommudity_code();
                    resultModel.setCommudity_code(convertIgmDetailMasterModel.getCommudity_code());


                    cont_status = convertIgmDetailMasterModel.getCont_status();
                    resultModel.setCont_status(convertIgmDetailMasterModel.getCont_status());

                    if (commudity_code == "") {
                        commudity_code = "35";
                    } else {
                        commudity_code = convertIgmDetailMasterModel.getCommudity_code();
                    }

                    if (cont_status == "EMPTY" || cont_status == "EMT" || cont_status == "MT") {
                        cont_status = "MTY";

                    } else {
                        cont_status = convertIgmDetailMasterModel.getCont_status();

                    }

                    cont_status_final = cont_status.trim().substring(0, 3);

                    String[] cont_seal = cont_seal_number.split(",");

                    String cont_seal_number_s = cont_seal[0];


                    if (type_of_igm == "TS") {
                        offdock = "BDMGL";
                    } else {
                        offdock = convertIgmDetailMasterModel.getOff_dock_id();

                    }


                    stringData = stringData + "<edi:ediBlEquipment>\n";

                    stringData = stringData + "<edi:ediContainer edi:containerGrossWt=\"" + cont_gross_weight + "\" edi:containerISOcode=\"" + cont_iso_type + "\" edi:containerNbr=\"" + cont_number + "\"  edi:containerSealNumber1=\"" + cont_seal_number_s + "\"   edi:containerStatus=\"" + cont_status_final + "\"   >\n";

                    stringData = stringData + "</edi:ediContainer>\n";

                    stringData = stringData + "<edi:ediCommodity edi:referenceNbr=\"\" edi:commodityCode=\"" + commudity_code + "\" edi:commodityShortName=\"\" edi:commodityDescription=\"\" edi:origin=\"\"  edi:destination=\"" + offdock + "\" edi:shipper=\"\" edi:consignee=\"\">\n";

                    stringData = stringData + "</edi:ediCommodity>\n";

                    stringData = stringData + "</edi:ediBlEquipment>\n";


                }


                if (!(weight.equals(0) && weight_unit.equals(""))) {

                    igm_gross_weight = weight;


                    weight_unit = weight_unit.toUpperCase();
                    weight_unit = weight_unit.replace('.', ' ');
                    String ex_mess = "";


                    if ((weight_unit == "MTON") || (weight_unit == "MTONS")) {
                        weight1 = igm_gross_weight * 1000;
                    } else if ((weight_unit == "TON") || (weight_unit == "TONS")) {
                        weight1 = igm_gross_weight * 1000;
                    } else if ((weight_unit == "HTON") || (weight_unit == "HTONS")) {
                        weight1 = igm_gross_weight * 1000 * 1.8;
                    } else if ((weight_unit == "STON") || (weight_unit == "STONS")) {
                        weight1 = igm_gross_weight * 907.185;
                    } else if ((weight_unit == "LTON") || (weight_unit == "LTONS")) {
                        weight1 = igm_gross_weight * 1016.05;
                    } else if (weight_unit == "LBS") {
                        weight1 = igm_gross_weight * 0.453592;
                    } else {
                        weight1 = igm_gross_weight;

                    }
                    weight_unit = "KG";

                } else {

                }


                stringData = stringData + "<edi:ediBlItem edi:quantity=\"" + Pack_Number + "\" edi:type=\"\" edi:weight=\"" + weight1 + "\" edi:weightUnit=\"" + weight_unit + "\" edi:markNumber=\"" + Pack_Marks_Number + "\" edi:description=\"" + subBlModel.getDescription_of_Goods() + "\">\n";
                stringData = stringData + "<edi:ediCommodity edi:referenceNbr=\"\" edi:commodityCode=\"" + commudity_code + "\" edi:commodityShortName=\"\" edi:commodityDescription=\"\" edi:origin=\"\" edi:destination=\"\" edi:shipper=\"\" edi:consignee=\"\">\n";
                stringData = stringData + "</edi:ediCommodity>\n";
                stringData = stringData + "</edi:ediBlItem>\n";
                stringData = stringData + "</edi:ediBlItemHolder>\n";
                stringData = stringData + "</edi:blTransaction>\n";


            } else {

                GetigmInfo = "SELECT igm_detail_id,Pack_Number,Pack_Description,Pack_Marks_Number,Description_of_Goods,\n" +
                        "weight,weight_unit,igm_master_id AS IGM_id,ConsigneeDesc,Submitee_Org_Id,type_of_igm FROM igm_supplimentary_detail WHERE id='" + igmsubdetailid + "' \n";

                List<ConvertIgmModel> strList = primaryDBTemplate.query(GetigmInfo, new row_igm_detail_Informations());
                if (strList.size() > 0) {


                    ConvertIgmModel subBlModels = strList.get(0);

                    Pack_Number = subBlModels.getPack_Number();
                    resultModel.setPack_Number(subBlModels.getPack_Number());

                    Pack_Description = subBlModels.getPack_Description();
                    resultModel.setPack_Description(subBlModels.getPack_Description());

                    Pack_Marks_Number = subBlModels.getPack_Marks_Number();
                    resultModel.setPack_Marks_Number(subBlModels.getPack_Marks_Number());

                    Description_of_Goods = subBlModels.getDescription_of_Goods();
                    resultModel.setDescription_of_Goods(Description_of_Goods);


                    weight = subBlModel.getWeight();
                    resultModel.setWeight(subBlModel.getWeight());

                    weight_unit = subBlModel.getWeight_unit();
                    resultModel.setWeight_unit(weight_unit);


                    type_of_igm = subBlModel.getType_of_igm();
                    resultModel.setType_of_igm(subBlModel.getType_of_igm());


                    ConsigneeDesc = subBlModel.getConsigneeDesc();
                    resultModel.setConsigneeDesc(subBlModel.getConsigneeDesc());


                }


                getsubmitee = "SELECT mlocode,Submitee_Org_Id FROM igm_details WHERE id='" + convertIgmMasterModel.getIgm_detail_id() + "'";
                List<ConvertIgmModel> strIgmList = primaryDBTemplate.query(getsubmitee, new row_igm_detail_Information_for_mlo());


                if (strIgmList.size() > 0) {


                    ConvertIgmModel subBlModels = strIgmList.get(0);

                    mlocode = subBlModels.getMlocode();
                    resultModel.setMlocode(subBlModels.getMlocode());

                }

                getvesselInfo = "SELECT Import_Rotation_No,Vessel_Name,Voy_No,Port_of_Shipment,Port_Ship_ID FROM igm_masters \n" +
                        "WHERE id='" + IGM_id + "'";
                List<ConvertIgmModel> strIgmMainList = primaryDBTemplate.query(getvesselInfo, new row_igm_master_Information());
                if (strIgmMainList.size() > 0) {

                    ConvertIgmModel subModel = strIgmMainList.get(0);
                    Import_rotation = subModel.getImport_Rotation_No();

                    resultModel.setVessel_Name(subModel.getVessel_Name());
                    Port_Ship_ID = subModel.getPort_Ship_ID();
                    resultModel.setPort_Ship_ID(subModel.getPort_Ship_ID());


                }


                if (type_of_igm == "TS") {
                    blCategory = "TRANSSHIP";
                    portId = "BDMGL";
                } else {
                    blCategory = "IMPORT";
                    portId = "BDCGP";

                }


                stringData = stringData + "<edi:blTransaction edi:msgClass=\"MANIFEST\" edi:msgFunction=\"9\" edi:msgReferenceNbr=\"000000001\" edi:msgTypeId=\"310\">\n";

                stringData = stringData + "<edi:Interchange edi:InterchangeReceipient=\"CPA\" edi:InterchangeSender=\"CPA\"/>\n";

                stringData = stringData + "<edi:ediBillOfLading edi:blCategory=\"" + blCategory + "\" edi:blNbr=\"" + blno + "\"/>\n";

                stringData = stringData + "<edi:ediVesselVisit edi:actualTimeArrival=\"\" edi:actualTimeDeparture=\"\" edi:estimatedTimeArrival=\"\" edi:estimatedTimeDeparture=\"\" edi:inVoyageNbr=\"" + Import_rotation + "\" edi:vesselId=\"" + vessel_name + "\" edi:vesselIdConvention=\"VESNAME\">\n";

                stringData = stringData + "<edi:shippingLine edi:shippingLineCode=\"" + mlocode + "\" edi:shippingLineCodeAgency=\"SCAC\"/>\n";

                stringData = stringData + "<edi:loadPort edi:portId=\"" + Port_Ship_ID + "\" edi:portIdConvention=\"UNLOCCODE\"/>\n";

                stringData = stringData + "</edi:ediVesselVisit>\n";

                stringData = stringData + "<edi:shipper edi:shipperName=\"\"/>\n";


                stringData = stringData + "<edi:consignee edi:consigneeName=\"\"/>\n";


                stringData = stringData + "<edi:dischargePort1 edi:portId=\"" + portId + "\" edi:portIdConvention=\"UNLOCCODE\"/>\n";

                stringData = stringData + "<edi:ediBlItemHolder>\n";


                String detailcont_offdoc = "SELECT cont_number,cont_gross_weight,cont_status,cont_iso_type,commudity_code,off_dock_id,cont_seal_number\n" +
                        "FROM igm_sup_detail_container WHERE igm_sup_detail_id='" + igmsubdetailid + "'";
                List<ConvertIgmModel> strIgmDetailContainerList = primaryDBTemplate.query(detailcont_offdoc, new row_igm_detail_container_Information());

                for (int m = 0; m < strIgmDetailContainerList.size(); m++) {

                    ConvertIgmModel convertIgmDetailMasterModel;
                    convertIgmDetailMasterModel = strIgmDetailContainerList.get(m);
                    cont_gross_weight = convertIgmDetailMasterModel.getCont_gross_weight();
                    resultModel.setCont_gross_weight(convertIgmDetailMasterModel.getCont_gross_weight());

                    cont_iso_type = convertIgmDetailMasterModel.getCont_iso_type();
                    resultModel.setCont_iso_type(convertIgmDetailMasterModel.getCont_iso_type());


                    cont_number = convertIgmDetailMasterModel.getCont_number();
                    resultModel.setCont_number(convertIgmDetailMasterModel.getCont_number());

                    off_dock_id = convertIgmDetailMasterModel.getOff_dock_id();
                    resultModel.setOff_dock_id(convertIgmDetailMasterModel.getOff_dock_id());

                    cont_seal_number = convertIgmDetailMasterModel.getCont_seal_number();
                    resultModel.setCont_seal_number(convertIgmDetailMasterModel.getCont_seal_number());


                    commudity_code = convertIgmDetailMasterModel.getCommudity_code();
                    resultModel.setCommudity_code(convertIgmDetailMasterModel.getCommudity_code());


                    cont_status = convertIgmDetailMasterModel.getCont_status();
                    resultModel.setCont_status(convertIgmDetailMasterModel.getCont_status());

                    if (commudity_code == "") {
                        commudity_code = "35";
                    } else {
                        commudity_code = convertIgmDetailMasterModel.getCommudity_code();
                    }

                    if (cont_status == "EMPTY" || cont_status == "EMT" || cont_status == "MT") {
                        cont_status = "MTY";

                    } else {
                        cont_status = convertIgmDetailMasterModel.getCont_status();

                    }

                    cont_status_final = cont_status.trim().substring(0, 3);

                    String[] cont_seal = cont_seal_number.split(",");

                    String cont_seal_number_s = cont_seal[0];


                    if (type_of_igm == "TS") {
                        offdock = "BDMGL";
                    } else {
                        offdock = convertIgmDetailMasterModel.getOff_dock_id();

                    }


                    stringData = stringData + "<edi:ediBlEquipment>\n";

                    stringData = stringData + "<edi:ediContainer edi:containerGrossWt=\"" + cont_gross_weight + "\" edi:containerISOcode=\"" + cont_iso_type + "\" edi:containerNbr=\"" + cont_number + "\"  edi:containerSealNumber1=\"" + cont_seal_number_s + "\"   edi:containerStatus=\"" + cont_status_final + "\"   >\n";

                    stringData = stringData + "</edi:ediContainer>\n";

                    stringData = stringData + "<edi:ediCommodity edi:referenceNbr=\"\" edi:commodityCode=\"" + commudity_code + "\" edi:commodityShortName=\"\" edi:commodityDescription=\"\" edi:origin=\"\"  edi:destination=\"" + offdock + "\" edi:shipper=\"\" edi:consignee=\"\">\n";

                    stringData = stringData + "</edi:ediCommodity>\n";

                    stringData = stringData + "</edi:ediBlEquipment>\n";


                }


                if (!(weight.equals(0) && weight_unit.equals(""))) {

                    igm_gross_weight = weight;
                    weight_unit = weight_unit.toUpperCase();
                    weight_unit = weight_unit.replace('.', ' ');
                    String ex_mess = "";


                    if ((weight_unit == "MTON") || (weight_unit == "MTONS")) {
                        weight1 = igm_gross_weight * 1000;
                    } else if ((weight_unit == "TON") || (weight_unit == "TONS")) {
                        weight1 = igm_gross_weight * 1000;
                    } else if ((weight_unit == "HTON") || (weight_unit == "HTONS")) {
                        weight1 = igm_gross_weight * 1000 * 1.8;
                    } else if ((weight_unit == "STON") || (weight_unit == "STONS")) {
                        weight1 = igm_gross_weight * 907.185;
                    } else if ((weight_unit == "LTON") || (weight_unit == "LTONS")) {
                        weight1 = igm_gross_weight * 1016.05;
                    } else if (weight_unit == "LBS") {
                        weight1 = igm_gross_weight * 0.453592;
                    } else {
                        weight1 = igm_gross_weight;

                    }
                    weight_unit = "KG";

                }


                stringData = stringData + "<edi:ediBlItem edi:quantity=\"" + Pack_Number + "\" edi:type=\"\" edi:weight=\"" + weight1 + "\" edi:weightUnit=\"" + weight_unit + "\" edi:markNumber=\"" + Pack_Marks_Number + "\" edi:description=\"" + Description_of_Goods + "\">\n";
                stringData = stringData + "<edi:ediCommodity edi:referenceNbr=\"\" edi:commodityCode=\"" + commudity_code + "\" edi:commodityShortName=\"\" edi:commodityDescription=\"\" edi:origin=\"\" edi:destination=\"\" edi:shipper=\"\" edi:consignee=\"\">\n";
                stringData = stringData + "</edi:ediCommodity>\n";
                stringData = stringData + "</edi:ediBlItem>\n";
                stringData = stringData + "</edi:ediBlItemHolder>\n";
                stringData = stringData + "</edi:blTransaction>\n";
            }


        }

        stringData = stringData+"</edi:blTransactions>\n";
        resultModel.setFilename(myFile_old);
        resultModel.setXmlData(stringData);
        File tmpFile = File.createTempFile("test", ".tmp");
        FileWriter writer = new FileWriter(tmpFile);
        writer.write(stringData);
        writer.close();
        tmpStrem = new FileInputStream(tmpFile);
        Path targetLocation=this.fileStorageLocation.resolve(myFile_old);
        Files.copy(tmpStrem,targetLocation, StandardCopyOption.REPLACE_EXISTING);



        resultList.add(resultModel);
        return resultList;

    }


    class IgmMasterId implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertPanGaonContainerModel=new ConvertIgmModel();
            convertPanGaonContainerModel.setId(rs.getString("id"));
            return convertPanGaonContainerModel;
        }
    }


    class IgmDetailId implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();
            convertIgmModel.setId(rs.getString("id"));
            return convertIgmModel;
        }
    }
    class row_igm_mastername implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();
            convertIgmModel.setVessel_Name(rs.getString("vessel_Name"));
            convertIgmModel.setImport_Rotation_No(rs.getString("import_Rotation_No"));
            return convertIgmModel;
        }
    }

    class row_igm_master_for_vessel_name implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();
            convertIgmModel.setVessel_Name(rs.getString("Vessel_Name"));
            convertIgmModel.setImport_Rotation_No(rs.getString("import_Rotation_No"));




            return convertIgmModel;
        }
    }


    class row_igm_detail_Informations implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();

            convertIgmModel.setPack_Number(rs.getString("Pack_Number"));
            convertIgmModel.setType_of_igm(rs.getString("Type_of_igm"));
            convertIgmModel.setConsigneeDesc(rs.getString("ConsigneeDesc"));



            convertIgmModel.setPack_Marks_Number(rs.getString("Pack_Marks_Number"));
            convertIgmModel.setDescription_of_Goods(rs.getString("Description_of_Goods"));
            convertIgmModel.setWeight(rs.getInt("weight"));
            convertIgmModel.setWeight_unit(rs.getString("weight_unit"));


            return convertIgmModel;
        }
    }

    class row_igm_detail_Information_for_mlo implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();

            convertIgmModel.setMlocode(rs.getString("Mlocode"));


            return convertIgmModel;
        }
    }
    class row_igm_master_Information_for_off_dock implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();

            convertIgmModel.setOff_dock_id(rs.getString("off_dock_id"));


            return convertIgmModel;
        }
    }



    class row_igm_detail_Information implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();

            convertIgmModel.setPack_Number(rs.getString("Pack_Number"));
            convertIgmModel.setType_of_igm(rs.getString("Type_of_igm"));
            convertIgmModel.setConsigneeDesc(rs.getString("ConsigneeDesc"));



            convertIgmModel.setPack_Marks_Number(rs.getString("Pack_Marks_Number"));
            convertIgmModel.setDescription_of_Goods(rs.getString("Description_of_Goods"));
            convertIgmModel.setWeight(rs.getInt("weight"));
            convertIgmModel.setWeight_unit(rs.getString("weight_unit"));
            convertIgmModel.setIGM_id(rs.getString("IGM_id"));


            return convertIgmModel;
        }
    }


    class row_igm_detail_container_Information implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();

            convertIgmModel.setCont_number(rs.getString("cont_number"));
            convertIgmModel.setCont_iso_type(rs.getString("cont_iso_type"));
            convertIgmModel.setCommudity_code(rs.getString("commudity_code"));
            convertIgmModel.setCont_status(rs.getString("cont_status"));
            convertIgmModel.setCont_gross_weight(rs.getString("cont_gross_weight"));
            convertIgmModel.setCont_seal_number(rs.getString("cont_seal_number"));
            convertIgmModel.setOff_dock_id(rs.getString("off_dock_id"));



            return convertIgmModel;
        }
    }



    class row_igm_detail_container_gross_of_weight_Information implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();

            convertIgmModel.setCont_number(rs.getString("cont_number"));
            convertIgmModel.setCont_iso_type(rs.getString("cont_iso_type"));
            convertIgmModel.setCommudity_code(rs.getString("commudity_code"));
            convertIgmModel.setCont_status(rs.getString("cont_status"));
            convertIgmModel.setCont_gross_weight(rs.getString("cont_gross_weight"));
            convertIgmModel.setCont_seal_number(rs.getString("cont_seal_number"));
            convertIgmModel.setOff_dock_id(rs.getString("off_dock_id"));



            return convertIgmModel;
        }
    }


    class row_igm_master_Information implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();
            convertIgmModel.setImport_Rotation_No(rs.getString("import_Rotation_No"));

            convertIgmModel.setPort_Ship_ID(rs.getString("Port_Ship_ID"));



            return convertIgmModel;
        }
    }

    class row_igm_master_vessel_name implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();
            convertIgmModel.setVessel_Name(rs.getString("Vessel_Name"));

            return convertIgmModel;
        }
    }

    class row_igm_master_details implements RowMapper {
        @Override
        public ConvertIgmModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmMasterModel=new ConvertIgmModel();
            convertIgmMasterModel.setIgm_sub_detail_id(rs.getString("igm_sub_detail_id"));

            convertIgmMasterModel.setRotation_no(rs.getString("Rotation_no"));
            convertIgmMasterModel.setIgm_detail_id(rs.getString("igm_detail_id"));
            convertIgmMasterModel.setBL_No(rs.getString("BL_No"));
            return convertIgmMasterModel;
        }
    }


    class row_igm_details_id implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();
            convertIgmModel.setIgm_detail_id(rs.getString("igm_detail_id"));
            return convertIgmModel;
        }
    }

    class row_igm_supplimentary_detail implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();
            convertIgmModel.setIgm_detail_id(rs.getString("igm_detail_id"));
            return convertIgmModel;
        }
    }

    class row_igm_sub_detail_id implements RowMapper {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConvertIgmModel convertIgmModel=new ConvertIgmModel();
            convertIgmModel.setIgm_sub_detail_id(rs.getString("igm_sub_detail_id"));
            return convertIgmModel;
        }
    }


}

