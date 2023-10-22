package com.datasoft.IgmMis.Service.DGInformation;


import com.datasoft.IgmMis.Model.DGInformation.DgContDischarge;
import com.datasoft.IgmMis.Repository.DGInformation.DgContDischargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

@Service
public class DgContDischargeService  {
    @Autowired
    private DgContDischargeRepository dgContDischargeRepository;




    List<DgContDischarge> dgContDischarge;


    public List<DgContDischarge> DgContDischargeList(String rot) {
        String Import_Rotation_No = rot.replace("_", "/");
        System.out.println(Import_Rotation_No);

        dgContDischarge =new ArrayList<>();
        DgContDischarge DischargeDg = new DgContDischarge();
        String import_Rotation_No="";
        String vessel_name="";
        String igm_masters_Total_number_of_bols="";
        String igm_masters_Total_number_of_packages="";
        String igm_masters_Total_number_of_containers="";
        String igm_masters_Total_gross_mass="";



        List igmdetailResult[] = dgContDischargeRepository.igmdetailList(Import_Rotation_No);
        System.out.println(igmdetailResult.length);
        List igmmasterResult1[] = dgContDischargeRepository.igmmasterlList(Import_Rotation_No);
        System.out.println(igmmasterResult1.length);

        for(int i=0;i<igmmasterResult1.length;i++){
            import_Rotation_No=igmmasterResult1[i].get(0).toString();
            vessel_name=igmmasterResult1[i].get(1).toString();
        }
        System.out.println("igm_masters_Vessel_Name:"+vessel_name );


        if(igmdetailResult.length<1){

            System.out.println("test");
            List igmmasterResult[] = dgContDischargeRepository.igmmasterlList(Import_Rotation_No);
            System.out.println(igmmasterResult.length);




            for (int i = 0; i < igmmasterResult.length; i++) {
                if(igmmasterResult.length>0)
                {
                    DischargeDg.setFlag("GENERAL_INFORMATION");
                    import_Rotation_No = igmmasterResult[i].get(0).toString();
                    DischargeDg.setVessel_Name(vessel_name);
                    System.out.println("Igm_Masters_Import_Rotation_No:"+import_Rotation_No );
                    DischargeDg.setImport_Rotation_No(Import_Rotation_No);
                    DischargeDg.setImport_Rotation_No(import_Rotation_No);

                    igm_masters_Total_number_of_bols = igmmasterResult[i].get(2).toString();
                    System.out.println("igm_masters_Total_number_of_bols:"+igm_masters_Total_number_of_bols);
                    DischargeDg.setTotal_number_of_bols(igm_masters_Total_number_of_bols);

                    igm_masters_Total_number_of_packages = igmmasterResult[i].get(3).toString();

                    System.out.println("igm_masters_Total_number_of_packages:"+igm_masters_Total_number_of_packages );
                    DischargeDg.setTotal_number_of_packages(igm_masters_Total_number_of_packages);
                    igm_masters_Total_number_of_containers = igmmasterResult[i].get(4).toString();
                    System.out.println("igm_masters_Total_number_of_containers:"+igm_masters_Total_number_of_containers);
                    DischargeDg.setTotal_number_of_containers(igm_masters_Total_number_of_containers);
                    igm_masters_Total_gross_mass = igmmasterResult[i].get(5).toString();
                    System.out.println("igm_masters_Total_gross_mass:"+igm_masters_Total_gross_mass );
                    DischargeDg.setTotal_gross_mass(igm_masters_Total_gross_mass);
                }

            }
        }
        else{


            Integer totalg1=0;
            Integer totalg2=0;
            Integer totalg3=0;
            Integer totalg4=0;
            Integer totalg5=0;
            Integer totalg6=0;
            Integer totalg7=0;
            Integer totalg8=0;
            Integer totalg9=0;
            Integer totalg10=0;
            Integer totalg11=0;
            Integer totalg12=0;
            Integer totalg13=0;
            Integer totalg14=0;


            Integer sum=0;
            Integer grand=0;
            String mlocode="";
            String row_submitee_org_id="";
            String Agent_Code="";
            String row_mlodescription="";
            String row_mlo_agent_code_ctms="";
            String Vessel_name="";
            String Rotation_No="";
            List result_igm_master[] = dgContDischargeRepository.result_igm_master(Import_Rotation_No);
            System.out.println(result_igm_master.length);
            System.out.println("Secound test");
            for(int i=0;i<result_igm_master.length;i++){
                Rotation_No=result_igm_master[i].get(0).toString();
                Vessel_name=result_igm_master[i].get(1).toString();

            }
            DischargeDg.setImport_Rotation_No(Rotation_No);

            List result_igm_details[] = dgContDischargeRepository.result_igm_details(Import_Rotation_No);
            System.out.println(result_igm_details.length);
            System.out.println("Third Test");

            for(int i=0;i<result_igm_details.length;i++){

                sum = sum + grand;

                System.out.println(sum);
                DgContDischarge Discharge = new DgContDischarge();
                row_submitee_org_id = result_igm_details[i].get(0).toString();
                Discharge.setFlag("DG_CONTAINER_DISCHARGE_SUMMARY");
                System.out.println("submitee_org_id:"+row_submitee_org_id );

                Discharge.setVessel_Name(Vessel_name);
                Discharge.setImport_Rotation_No(Rotation_No);




                mlocode = result_igm_details[i].get(3).toString();
                Discharge.setMLocode(mlocode);
                System.out.println("Mlocode:"+mlocode );

                List result_mlo_detail[] = dgContDischargeRepository.result_mlo_detail(mlocode);
                System.out.println(result_mlo_detail.length);

                for(int j=0;j<result_mlo_detail.length;j++) {


                    row_mlodescription = result_mlo_detail[j].get(0).toString();

                    System.out.println("mlodescription:" + row_mlodescription);

                    if(result_mlo_detail[j].get(1)!=null){
                        row_mlo_agent_code_ctms = result_mlo_detail[j].get(1).toString();

                    }
                    else{
                        row_mlo_agent_code_ctms="";
                    }


                    Discharge.setAGentCode(row_mlo_agent_code_ctms);
                    System.out.println("mlo_agent_code_ctms:" + row_mlo_agent_code_ctms);

                }


                Integer result_igm_detail_container_total = dgContDischargeRepository.result_igm_detail_container(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(result_igm_detail_container_total);


                System.out.println("Fourth test...................................................");
                Integer total1 = dgContDischargeRepository.total1(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total1);

                Discharge.setLADEN_20(total1);
                totalg1=totalg1+total1;
                DischargeDg.setTotal1(totalg1);
                System.out.println("Summation Total First Number:......................");


                System.out.println(totalg1);
                Integer result_igm_details_container = dgContDischargeRepository.result_igm_details_container(Import_Rotation_No,row_submitee_org_id,mlocode);



                Integer total2 = dgContDischargeRepository.total2(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total2);
                Discharge.setLADEN_40(total2);
                totalg2=totalg2+total2;
                System.out.println("Summation Total Secound Number:......................");
                DischargeDg.setTotal2(totalg2);
                System.out.println(totalg2);



                Integer total3 = dgContDischargeRepository.total3(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total3);
                Discharge.setEMPTY_20(total3);
                totalg3=totalg3+total3;
                DischargeDg.setTotal3(totalg3);
                System.out.println("Summation Total Third Number:......................");
                System.out.println(totalg3);


                Integer total4 = dgContDischargeRepository.total4(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total4);
                Discharge.setEMPTY_40(total4);
                System.out.println("Summation Total Fourth Number:......................");
                totalg4=totalg4+total4;
                DischargeDg.setTotal4(totalg4);
                System.out.println(totalg4);


                Integer total5 = dgContDischargeRepository.total5(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total5);
                Discharge.setREFFER_20(total5);
                totalg5=totalg5+total5;
                DischargeDg.setTotal5(totalg5);
                System.out.println("Summation Total Five Number:......................");
                System.out.println(totalg5);


                Integer total6 = dgContDischargeRepository.total6(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total6);
                Discharge.setREFFER_40(total6);
                totalg6=totalg6+total6;
                DischargeDg.setTotal6(totalg6);
                System.out.println("Summation Total Sex Number:......................");
                System.out.println(totalg6);



                Integer total7 = dgContDischargeRepository.total7(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total7);
                Discharge.setIMDG_20(total7);
                totalg7=totalg7+total7;
                DischargeDg.setTotal7(totalg7);
                System.out.println("Summation Total Seven Number:......................");
                System.out.println(totalg7);

                Integer total8 = dgContDischargeRepository.total8(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total8);
                Discharge.setIMDG_40(total8);
                totalg8=totalg8+total8;
                DischargeDg.setTotal8(totalg8);
                System.out.println("Summation Total Eighth Number:......................");
                System.out.println(totalg8);


                Integer total9 = dgContDischargeRepository.total9(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total9);
                Discharge.setTRANS_20(total9);
                totalg9=totalg9+total9;
                DischargeDg.setTotal9(totalg9);
                System.out.println("Summation Total Nine Number:......................");
                System.out.println(totalg9);




                Integer total10 = dgContDischargeRepository.total10(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total10);
                Discharge.setTRANS_40(total10);
                totalg10=totalg10+total10;
                DischargeDg.setTotal10(totalg10);
                System.out.println("Summation Total Ten Number:......................");
                System.out.println(totalg10);


                Integer total11 = dgContDischargeRepository.total11(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total11);
                Discharge.setICD_20(total11);
                totalg11=totalg11+total11;
                DischargeDg.setTotal11(totalg11);
                System.out.println("Summation Total Eleven Number:......................");
                System.out.println(totalg11);

                Integer total12 = dgContDischargeRepository.total12(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total12);
                Discharge.setICD_40(total12);

                totalg12=totalg12+total12;
                DischargeDg.setTotal12(totalg12);
                System.out.println("Summation Total Twleave Number:......................");
                System.out.println(totalg12);


                Integer total13 = dgContDischargeRepository.total13(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total13);
                Discharge.setLD_45(total13);
                totalg13=totalg13+total13;
                DischargeDg.setTotal13(totalg13);
                System.out.println("Summation Total Thirteen Number:......................");
                System.out.println(totalg13);


                Integer total14 = dgContDischargeRepository.total14(Import_Rotation_No,row_submitee_org_id,mlocode);
                System.out.println(total14);
                Discharge.setMT_45(total14);
                totalg14=totalg14+total14;
                DischargeDg.setTotal14(totalg14);
                System.out.println("Summation Total Fourteen Number:......................");
                System.out.println(totalg14);
                dgContDischarge.add(Discharge);

            }

        }
        dgContDischarge.add(DischargeDg);
        return dgContDischarge;
    }
}