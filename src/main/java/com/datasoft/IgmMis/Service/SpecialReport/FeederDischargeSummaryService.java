package com.datasoft.IgmMis.Service.SpecialReport;

import com.datasoft.IgmMis.DAO.FeederDischargeSummaryDAO;
import com.datasoft.IgmMis.Model.ResponseMessage;
import com.datasoft.IgmMis.Model.SpecialReport.FeederDischargeSummary;
import com.datasoft.IgmMis.Model.SpecialReport.MloAndTypeWiseContainer;
import com.datasoft.IgmMis.Model.SpecialReport.MloWiseContainerInfo;
import com.datasoft.IgmMis.Repository.SpecialReport.FeederDischargeSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeederDischargeSummaryService {
    @Autowired
    private FeederDischargeSummaryRepository feederDischargeSummaryRepository;

    @Autowired
    private FeederDischargeSummaryDAO feederDischargeSummaryDAO;


    ResponseMessage jsonMsg;

    public FeederDischargeSummary feederDischargeSummary(String rotation) {

        FeederDischargeSummary feederDischargeSummary = new FeederDischargeSummary();
        feederDischargeSummary.setRotation(rotation);

        String vessel_name = "";
        String total_bls = "";
        String total_pkg = "";
        String total_cont = "";
        String total_gross_mass = "";
        String voy_no = "";
        List igmDetails[] = feederDischargeSummaryRepository.cntIgmDetails(rotation);
        Integer testdetailRes2 = igmDetails.length;

        if(testdetailRes2 < 1){
            //If not found in igm_details...
            List row_igm_master[] = feederDischargeSummaryRepository.igmMaster(rotation);
            Integer testRes2 = row_igm_master.length;
            if(testRes2 > 0){
                for (int a = 0; a < row_igm_master.length; a++) {
                    vessel_name = row_igm_master[a].get(1).toString(); //Vessel_Name Column
                    total_bls = row_igm_master[a].get(2).toString(); //Total_number_of_bols Column
                    total_pkg = row_igm_master[a].get(3).toString(); //Total_number_of_packages Column
                    total_cont = row_igm_master[a].get(4).toString(); //Total_number_of_containers Column
                    total_gross_mass = row_igm_master[a].get(5).toString(); //Total_gross_mass Column
                }
                feederDischargeSummary.setVesselName(vessel_name);
                feederDischargeSummary.setTotalBls(total_bls);
                feederDischargeSummary.setTotalPackage(total_pkg);
                feederDischargeSummary.setTotalContainers(total_cont);
                feederDischargeSummary.setTotalGrossMass(total_gross_mass);
                feederDischargeSummary.setFormatType("general");
            } else {
                feederDischargeSummary.setFormatType("invalid");
                feederDischargeSummary.setMessage("No Record found for your given rotation. Please type correctly and try again.");
            }

        } else {
            //If found in igm_details...
            feederDischargeSummary.setFormatType("letter");
            List row_igm_master[] = feederDischargeSummaryRepository.result_igm_master1(rotation);
            for (int a = 0; a < row_igm_master.length; a++) {
                vessel_name = row_igm_master[a].get(1).toString(); //Vessel_Name Column
                voy_no = row_igm_master[a].get(2).toString(); //Voy_No Column
            }
            feederDischargeSummary.setVesselName(vessel_name);
            feederDischargeSummary.setVoyageNo(voy_no);

            Integer totalLaden20 = 0;
            Integer totalLaden40 = 0;
            Integer totalEmpty20 = 0;
            Integer totalEmpty40 = 0;
            Integer totalReffer20 = 0;
            Integer totalReffer40 = 0;
            Integer totalImdg20 = 0;
            Integer totalImdg40 = 0;
            Integer totalTrans20 = 0;
            Integer totalTrans40 = 0;
            Integer totalPangaon20 = 0;
            Integer totalPangaon40 = 0;
            Integer totalIcd20 = 0;
            Integer totalIcd40 = 0;
            Integer totalL45 = 0;
            Integer totalE45 = 0;
            Integer mloWiseTotalContainerQty = 0;

            ArrayList<MloWiseContainerInfo> mloWiseContainerInfos = new ArrayList<>();
            List mloInfo[] = feederDischargeSummaryRepository.mloInfo(rotation);
            for (int b = 0; b < mloInfo.length; b++) {
                Integer totalContainer = 0;
                MloWiseContainerInfo mloWiseContainerInfo = new MloWiseContainerInfo();
                String mloCode = mloInfo[b].get(3).toString(); //Column : mlocode
                String submitee_org_id = mloInfo[b].get(0).toString(); //Column : submitee_org_id

                Integer laden20FirstValue = feederDischargeSummaryRepository.laden20FirstQuery(mloCode,submitee_org_id,rotation);
                Integer laden20SecondValue = feederDischargeSummaryRepository.laden20SecondQuery(mloCode,submitee_org_id,rotation);
                Integer laden20Value = laden20FirstValue-laden20SecondValue;
                totalLaden20 = totalLaden20 + laden20Value;

                Integer laden40FirstValue = feederDischargeSummaryRepository.laden40FirstQuery(mloCode,submitee_org_id,rotation);
                Integer laden40SecondValue = feederDischargeSummaryRepository.laden40SecondQuery(mloCode,submitee_org_id,rotation);
                Integer laden40Value = laden40FirstValue-laden40SecondValue;
                totalLaden40 = totalLaden40 + laden40Value;

                Integer empty20Value = feederDischargeSummaryRepository.empty20Query(mloCode,submitee_org_id,rotation);
                Integer empty40Value = feederDischargeSummaryRepository.empty40Query(mloCode,submitee_org_id,rotation);
                Integer reefer20Value = feederDischargeSummaryRepository.reefer20Query(mloCode,submitee_org_id,rotation);
                Integer reefer40Value = feederDischargeSummaryRepository.reefer40Query(mloCode,submitee_org_id,rotation);
                Integer imdg20Value = feederDischargeSummaryRepository.imdg20Query(mloCode,submitee_org_id,rotation);
                Integer imdg40Value = feederDischargeSummaryRepository.imdg40Query(mloCode,submitee_org_id,rotation);
                Integer trans20Value = feederDischargeSummaryRepository.trans20Query(mloCode,submitee_org_id,rotation);
                Integer trans40Value = feederDischargeSummaryRepository.trans40Query(mloCode,submitee_org_id,rotation);
                Integer pangaon20Value = feederDischargeSummaryRepository.pangaon20Query(mloCode,submitee_org_id,rotation);
                Integer pangaon40Value = feederDischargeSummaryRepository.pangaon40Query(mloCode,submitee_org_id,rotation);
                Integer icd20Value = feederDischargeSummaryRepository.icd20Query(mloCode,submitee_org_id,rotation);
                Integer icd40Value = feederDischargeSummaryRepository.icd40Query(mloCode,submitee_org_id,rotation);
                Integer l45Value = feederDischargeSummaryRepository.l45Query(mloCode,submitee_org_id,rotation);
                Integer e45Value = feederDischargeSummaryRepository.e45Query(mloCode,submitee_org_id,rotation);

                totalEmpty20 = totalEmpty20 + empty20Value;
                totalEmpty40 = totalEmpty40 + empty40Value;
                totalReffer20 = totalReffer20 + reefer20Value;
                totalReffer40 = totalReffer40 + reefer40Value;
                totalImdg20 = totalImdg20 + imdg20Value;
                totalImdg40 = totalImdg40 + imdg40Value;
                totalTrans20 = totalTrans20 + trans20Value;
                totalTrans40 = totalTrans40 + trans40Value;
                totalPangaon20 = totalPangaon20 + pangaon20Value;
                totalPangaon40 = totalPangaon40 + pangaon40Value;
                totalIcd20 = totalIcd20 + icd20Value;
                totalIcd40 = totalIcd40 + icd40Value;
                totalL45 = totalL45 + l45Value;
                totalE45 = totalE45 + e45Value;


                totalContainer = laden20Value + laden40Value + empty20Value + empty40Value + reefer20Value + reefer40Value + imdg20Value + imdg40Value +
                        trans20Value + trans40Value + pangaon20Value + pangaon40Value + icd20Value + icd40Value + l45Value + e45Value;

                mloWiseContainerInfo.setMloCode(mloCode);
                mloWiseContainerInfo.setAgentCode(feederDischargeSummaryRepository.agentCode(mloCode));
                mloWiseContainerInfo.setLaden20(laden20Value);
                mloWiseContainerInfo.setLaden40(laden40Value);
                mloWiseContainerInfo.setEmpty20(empty20Value);
                mloWiseContainerInfo.setEmpty40(empty40Value);
                mloWiseContainerInfo.setReefer20(reefer20Value);
                mloWiseContainerInfo.setReefer40(reefer40Value);
                mloWiseContainerInfo.setImdg20(imdg20Value);
                mloWiseContainerInfo.setImdg40(imdg40Value);
                mloWiseContainerInfo.setTrans20(trans20Value);
                mloWiseContainerInfo.setTrans40(trans40Value);
                mloWiseContainerInfo.setPangaon20(pangaon20Value);
                mloWiseContainerInfo.setPangaon40(pangaon40Value);
                mloWiseContainerInfo.setIcd20(icd20Value);
                mloWiseContainerInfo.setIcd40(icd40Value);
                mloWiseContainerInfo.setL45(l45Value);
                mloWiseContainerInfo.setE45(e45Value);
                mloWiseContainerInfo.setMloWiseTotalContainer(totalContainer);

                mloWiseContainerInfos.add(mloWiseContainerInfo);
            }
            feederDischargeSummary.setMloWiseContainerInfos(mloWiseContainerInfos);
            feederDischargeSummary.setAllMlo(mloInfo.length);

            feederDischargeSummary.setTotalLaden20(totalLaden20);
            feederDischargeSummary.setTotalLaden40(totalLaden40);
            feederDischargeSummary.setTotalEmpty20(totalEmpty20);
            feederDischargeSummary.setTotalEmpty40(totalEmpty40);
            feederDischargeSummary.setTotalReffer20(totalReffer20);
            feederDischargeSummary.setTotalReffer40(totalReffer40);
            feederDischargeSummary.setTotalImdg20(totalImdg20);
            feederDischargeSummary.setTotalImdg40(totalImdg40);
            feederDischargeSummary.setTotalTrans20(totalTrans20);
            feederDischargeSummary.setTotalTrans40(totalTrans40);
            feederDischargeSummary.setTotalPangaon20(totalPangaon20);
            feederDischargeSummary.setTotalPangaon40(totalPangaon40);
            feederDischargeSummary.setTotalIcd20(totalIcd20);
            feederDischargeSummary.setTotalIcd40(totalIcd40);
            feederDischargeSummary.setTotalL45(totalL45);
            feederDischargeSummary.setTotalE45(totalE45);
            mloWiseTotalContainerQty = totalLaden20 + totalLaden40 + totalEmpty20 + totalEmpty40 + totalReffer20 + totalReffer40 + totalImdg20 + totalImdg40 +
                    totalTrans20 + totalTrans40 + totalPangaon20 + totalPangaon40 + totalIcd20 + totalIcd40 + totalL45 + totalE45;
            feederDischargeSummary.setMloWiseTotalContainerQty(mloWiseTotalContainerQty);

            List<FeederDischargeSummaryDAO.vesselInfo> myvesselInfos = new ArrayList<FeederDischargeSummaryDAO.vesselInfo>();
            myvesselInfos = feederDischargeSummaryDAO.getVesselVisitInfo(rotation);
            String agent_name = "";
            for(int t = 0; t < myvesselInfos.size(); t++) {
                FeederDischargeSummaryDAO.vesselInfo info = myvesselInfos.get(t);
                agent_name = info.getName();
            }
            feederDischargeSummary.setAgentName(agent_name);

            String fcl20 = "";
            String fcl40 = "";
            String fclTeus = "";
            String fclBox = "";

            String lcl20 = "";
            String lcl40 = "";
            String lclTeus = "";
            String lclBox = "";

            String icd20 = "";
            String icd40 = "";
            String icdTeus = "";
            String icdBox = "";

            String reefer20 = "";
            String reefer40 = "";
            String reeferTeus = "";
            String reeferBox = "";

            String png20 = "";
            String png40 = "";
            String pngTeus = "";
            String pngBox = "";

            String emp20 = "";
            String emp40 = "";
            String empTeus = "";
            String empBox = "";

            String depot20 = "";
            String depot40 = "";
            String depotTeus = "";
            String depotBox = "";

            Integer total20 = 0;
            Integer total40 = 0;
            Integer totalBox = 0;
            Integer totalTeus = 0;

            List contTypeWiseQuantity[] = feederDischargeSummaryRepository.contTypeWiseQuantity(rotation);
            for (int c = 0; c < contTypeWiseQuantity.length; c++) {
                fcl20 = contTypeWiseQuantity[c].get(0).toString(); //fcl_20 Column
                fcl40 = contTypeWiseQuantity[c].get(1).toString(); //fcl_40 Column
                fclTeus = contTypeWiseQuantity[c].get(2).toString(); //fcl_tues Column
                fclBox = contTypeWiseQuantity[c].get(3).toString(); //fcl_box Column

                lcl20 = contTypeWiseQuantity[c].get(4).toString(); //lcl_20 Column
                lcl40 = contTypeWiseQuantity[c].get(5).toString(); //lcl_40 Column
                lclTeus = contTypeWiseQuantity[c].get(6).toString(); //lcl_tues Column
                lclBox = contTypeWiseQuantity[c].get(7).toString(); //lcl_box Column

                icd20 = contTypeWiseQuantity[c].get(8).toString(); //icd_20 Column
                icd40 = contTypeWiseQuantity[c].get(9).toString(); //icd_40 Column
                icdTeus = contTypeWiseQuantity[c].get(10).toString(); //icd_tues Column
                icdBox = contTypeWiseQuantity[c].get(11).toString(); //icd_box Column

                reefer20 = contTypeWiseQuantity[c].get(12).toString(); //ref_20 Column
                reefer40 = contTypeWiseQuantity[c].get(13).toString(); //ref_40 Column
                reeferTeus = contTypeWiseQuantity[c].get(14).toString(); //ref_tues Column
                reeferBox = contTypeWiseQuantity[c].get(15).toString(); //ref_box Column

                png20 = contTypeWiseQuantity[c].get(16).toString(); //png_20 Column
                png40 = contTypeWiseQuantity[c].get(17).toString(); //png_40 Column
                pngTeus = contTypeWiseQuantity[c].get(18).toString(); //png_tues Column
                pngBox = contTypeWiseQuantity[c].get(19).toString(); //png_box Column

                emp20 = contTypeWiseQuantity[c].get(20).toString(); //emp_20 Column
                emp40 = contTypeWiseQuantity[c].get(21).toString(); //emp_40 Column
                empTeus = contTypeWiseQuantity[c].get(22).toString(); //emp_tues Column
                empBox = contTypeWiseQuantity[c].get(23).toString(); //emp_box Column

                depot20 = contTypeWiseQuantity[c].get(24).toString(); //dep_20 Column
                depot40 = contTypeWiseQuantity[c].get(25).toString(); //dep_40 Column
                depotTeus = contTypeWiseQuantity[c].get(26).toString(); //dep_tues Column
                depotBox = contTypeWiseQuantity[c].get(27).toString(); //dep_box Column
            }
            feederDischargeSummary.setFcl20(fcl20);
            feederDischargeSummary.setFcl40(fcl40);
            feederDischargeSummary.setFclTeus(fclTeus);
            feederDischargeSummary.setFclBox(fclBox);
            feederDischargeSummary.setLcl20(lcl20);
            feederDischargeSummary.setLcl40(lcl40);
            feederDischargeSummary.setLclTeus(lclTeus);
            feederDischargeSummary.setLclBox(lclBox);
            feederDischargeSummary.setIcd20(icd20);
            feederDischargeSummary.setIcd40(icd40);
            feederDischargeSummary.setIcdTeus(icdTeus);
            feederDischargeSummary.setIcdBox(icdBox);
            feederDischargeSummary.setReefer20(reefer20);
            feederDischargeSummary.setReefer40(reefer40);
            feederDischargeSummary.setReeferTeus(reeferTeus);
            feederDischargeSummary.setReeferBox(reeferBox);
            feederDischargeSummary.setPng20(png20);
            feederDischargeSummary.setPng40(png40);
            feederDischargeSummary.setPngTeus(pngTeus);
            feederDischargeSummary.setPngBox(pngBox);
            feederDischargeSummary.setEmpty20(emp20);
            feederDischargeSummary.setEmpty40(emp40);
            feederDischargeSummary.setEmptyTeus(empTeus);
            feederDischargeSummary.setEmptyBox(empBox);
            feederDischargeSummary.setDepot20(depot20);
            feederDischargeSummary.setDepot40(depot40);
            feederDischargeSummary.setDepotTeus(depotTeus);
            feederDischargeSummary.setDepotBox(depotBox);

            total20 = Integer.parseInt(fcl20) + Integer.parseInt(lcl20) + Integer.parseInt(icd20) + Integer.parseInt(reefer20) + Integer.parseInt(png20) +
                    Integer.parseInt(emp20) + Integer.parseInt(depot20);

            total40 = Integer.parseInt(fcl40) + Integer.parseInt(lcl40) + Integer.parseInt(icd40) + Integer.parseInt(reefer40) + Integer.parseInt(png40) +
                    Integer.parseInt(emp40) + Integer.parseInt(depot40);

            totalTeus = Integer.parseInt(fclTeus) + Integer.parseInt(lclTeus) + Integer.parseInt(icdTeus) + Integer.parseInt(reeferTeus) +
                    Integer.parseInt(pngTeus) + Integer.parseInt(empTeus) + Integer.parseInt(depotTeus);

            totalBox = Integer.parseInt(fclBox) + Integer.parseInt(lclBox) + Integer.parseInt(icdBox) + Integer.parseInt(reeferBox) +
                    Integer.parseInt(pngBox) + Integer.parseInt(empBox) + Integer.parseInt(depotBox);

            feederDischargeSummary.setTotal20(total20);
            feederDischargeSummary.setTotal40(total40);
            feederDischargeSummary.setTotalTeus(totalTeus);
            feederDischargeSummary.setTotalBox(totalBox);

        }

        Integer mloAndTypeWiseTotalFcl20 = 0;
        Integer mloAndTypeWiseTotalFcl40 = 0;
        Integer mloAndTypeWiseTotalLcl20 = 0;
        Integer mloAndTypeWiseTotalLcl40 = 0;
        Integer mloAndTypeWiseTotalIcd20 = 0;
        Integer mloAndTypeWiseTotalIcd40 = 0;
        Integer mloAndTypeWiseTotalEmpty20 = 0;
        Integer mloAndTypeWiseTotalEmpty40 = 0;
        Integer mloAndTypeWiseTotalBox = 0;
        Integer mloAndTypeWiseTotalTeus = 0;

        ArrayList<MloAndTypeWiseContainer> mloAndTypeWiseContainers = new ArrayList<>();
        List orgInfo[] = feederDischargeSummaryRepository.orgInfo(rotation);
        for (int d = 0; d < orgInfo.length; d++) {
            MloAndTypeWiseContainer mloAndTypeWiseContainer = new MloAndTypeWiseContainer();
            String submitee_org_id = orgInfo[d].get(0).toString(); //Column : submitee_org_id
            String orgMloCode = orgInfo[d].get(3).toString(); //Column : mlocode
            mloAndTypeWiseContainer.setMloCode(orgMloCode);

            Integer totalFcl20 = feederDischargeSummaryRepository.totalFcl20(orgMloCode,submitee_org_id,rotation);
            Integer totalFcl40 = feederDischargeSummaryRepository.totalFcl40(orgMloCode,submitee_org_id,rotation);
            Integer totalLcl20 = feederDischargeSummaryRepository.totalLcl20(orgMloCode,submitee_org_id,rotation);
            Integer totalLcl40 = feederDischargeSummaryRepository.totalLcl40(orgMloCode,submitee_org_id,rotation);
            Integer totalIcd20 = feederDischargeSummaryRepository.totalIcd20(orgMloCode,submitee_org_id,rotation);
            Integer totalIcd40 = feederDischargeSummaryRepository.totalIcd40(orgMloCode,submitee_org_id,rotation);
            Integer totalEmpty20 = feederDischargeSummaryRepository.totalEmpty20(orgMloCode,submitee_org_id,rotation);
            Integer totalEmpty40 = feederDischargeSummaryRepository.totalEmpty40(orgMloCode,submitee_org_id,rotation);

            Integer totalBox = totalFcl20 + totalFcl40 + totalLcl20 + totalLcl40 + totalIcd20 + totalIcd40 + totalEmpty20 + totalEmpty40;
            Integer totalTeus = (totalFcl20+totalLcl20+totalIcd20+totalEmpty20) + (totalFcl40+totalLcl40+totalIcd40+totalEmpty40)*2;


            mloAndTypeWiseContainer.setFcl20(totalFcl20);
            mloAndTypeWiseContainer.setFcl40(totalFcl40);
            mloAndTypeWiseContainer.setLcl20(totalLcl20);
            mloAndTypeWiseContainer.setLcl40(totalLcl40);
            mloAndTypeWiseContainer.setIcd20(totalIcd20);
            mloAndTypeWiseContainer.setIcd40(totalIcd40);
            mloAndTypeWiseContainer.setEmpty20(totalEmpty20);
            mloAndTypeWiseContainer.setEmpty40(totalEmpty40);
            mloAndTypeWiseContainer.setBox(totalBox);
            mloAndTypeWiseContainer.setTeus(totalTeus);

            mloAndTypeWiseContainers.add(mloAndTypeWiseContainer);

            mloAndTypeWiseTotalFcl20 = mloAndTypeWiseTotalFcl20 + totalFcl20;
            mloAndTypeWiseTotalFcl40 = mloAndTypeWiseTotalFcl40 + totalFcl40;
            mloAndTypeWiseTotalLcl20 = mloAndTypeWiseTotalLcl20 + totalLcl20;
            mloAndTypeWiseTotalLcl40 = mloAndTypeWiseTotalLcl40 + totalLcl40;
            mloAndTypeWiseTotalIcd20 = mloAndTypeWiseTotalIcd20 + totalIcd20;
            mloAndTypeWiseTotalIcd40 = mloAndTypeWiseTotalIcd40 + totalIcd40;
            mloAndTypeWiseTotalEmpty20 = mloAndTypeWiseTotalEmpty20 + totalEmpty20;
            mloAndTypeWiseTotalEmpty40 = mloAndTypeWiseTotalEmpty40 + totalEmpty40;
            mloAndTypeWiseTotalBox = mloAndTypeWiseTotalBox + totalBox;
            mloAndTypeWiseTotalTeus = mloAndTypeWiseTotalTeus + totalTeus;
        }

        feederDischargeSummary.setMloAndTypeWiseContainers(mloAndTypeWiseContainers);

        feederDischargeSummary.setMloAndTypeWiseTotalFcl20(mloAndTypeWiseTotalFcl20);
        feederDischargeSummary.setMloAndTypeWiseTotalFcl40(mloAndTypeWiseTotalFcl40);
        feederDischargeSummary.setMloAndTypeWiseTotalLcl20(mloAndTypeWiseTotalLcl20);
        feederDischargeSummary.setMloAndTypeWiseTotalLcl40(mloAndTypeWiseTotalLcl40);
        feederDischargeSummary.setMloAndTypeWiseTotalIcd20(mloAndTypeWiseTotalIcd20);
        feederDischargeSummary.setMloAndTypeWiseTotalIcd40(mloAndTypeWiseTotalIcd40);
        feederDischargeSummary.setMloAndTypeWiseTotalEmpty20(mloAndTypeWiseTotalEmpty20);
        feederDischargeSummary.setMloAndTypeWiseTotalEmpty40(mloAndTypeWiseTotalEmpty40);
        feederDischargeSummary.setMloAndTypeWiseTotalBox(mloAndTypeWiseTotalBox);
        feederDischargeSummary.setMloAndTypeWiseTotalTeus(mloAndTypeWiseTotalTeus);

        return feederDischargeSummary;
    }
}
