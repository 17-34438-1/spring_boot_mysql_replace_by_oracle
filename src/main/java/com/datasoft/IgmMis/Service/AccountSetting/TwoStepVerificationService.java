package com.datasoft.IgmMis.Service.AccountSetting;

import com.datasoft.IgmMis.Model.AccountSetting.ChangePasswordModel;
import com.datasoft.IgmMis.Model.AccountSetting.SendSMSModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TwoStepVerificationService {
    @Autowired
    @Qualifier("jdbcTemplatePrimary")
    private JdbcTemplate primaryDBTemplate;
    public List getCellNumberAndUpdateOtp(String loginId){
        List<ChangePasswordModel> resultList=new ArrayList<>();
        ChangePasswordModel resultModel=new ChangePasswordModel();
        String phoneNumber="";
        String sqlQuery="";
        List<ChangePasswordModel> PhoneNumberList=new ArrayList<>();
        sqlQuery="SELECT mobile_no  AS rtnValue FROM users_info WHERE login_id='"+loginId+"'";
        PhoneNumberList=primaryDBTemplate.query(sqlQuery,new TwoStepVerificationService.PhoneNumber());
        phoneNumber=PhoneNumberList.get(0).getPhoneNumber();
        if(phoneNumber.equals("") || phoneNumber==null){
            resultModel.setMessage("notFound");
        }
        else {
            Random random = new Random();
            int otp = random.ints(1, 9999)
                    .findFirst()
                    .getAsInt();
            String upateQuery="";
            upateQuery="UPDATE users_info SET otp_code = '"+otp+"' WHERE mobile_no ='"+phoneNumber+"'";
            primaryDBTemplate.update(upateQuery);
            String text="Your%20verification%20code:%20"+otp;
            SendSMSModel sendSMSModel=new SendSMSModel();
            sendSMSModel.sendSMS(phoneNumber,text);
            resultModel.setPhoneNumber(phoneNumber);
        }
        resultList.add(resultModel);
        return resultList;
    }
    public List verifyOtp(String phoneNumber,String verifyCode,String loginId){
        List<ChangePasswordModel> resultList=new ArrayList<>();
        ChangePasswordModel resultModel=new ChangePasswordModel();
        String sqlQuery="";
        List<ChangePasswordModel> verifyList=new ArrayList<>();
        sqlQuery="SELECT COUNT(*) AS rtnValue FROM users_info WHERE mobile_no='"+phoneNumber+"' AND otp_code='"+verifyCode+"'";
        verifyList=primaryDBTemplate.query(sqlQuery,new TwoStepVerificationService.Count());
        Integer c=0;
        Integer twoStepState=0;

        if(verifyList.size()>0){
            c=verifyList.get(0).getCount();
        }
        if(c>0){
            resultModel.setMessage("verified");
            String twoStepStateQuery="";
             twoStepStateQuery="SELECT two_stp_st  FROM users_info WHERE login_id='"+loginId+"'";
            List<ChangePasswordModel>  twoStepStateList=new ArrayList<>();
            twoStepStateList=  primaryDBTemplate.query(twoStepStateQuery,new TwoStepVerificationService.TwoStepState());

            if(twoStepStateList.size()>0){
                twoStepState=twoStepStateList.get(0).getTwoStepState();
                System.out.println("twoStep : "+ twoStepState);
            }
            resultModel.setTwoStepState(twoStepState);



        }
        else{
            resultModel.setMessage("notverified");
        }
        resultList.add(resultModel);
        return resultList;
    }
    public List updateTwoStepSettingOrPhoneNumber(String phoneNumber,Integer twoStepState,String loginId){
        List<ChangePasswordModel> resultList=new ArrayList<>();
        ChangePasswordModel resultModel=new ChangePasswordModel();
        Integer yes=0;


        String updateQuery="";
        if(twoStepState==1){
            updateQuery="UPDATE users_info SET mobile_no = '"+phoneNumber+"', two_stp_st = 1 WHERE login_id = '"+loginId+"'";

        }
        else{
            updateQuery="UPDATE users_info SET mobile_no = '"+phoneNumber+"', two_stp_st = 0 WHERE login_id = '"+loginId+"'";

        }
       yes= primaryDBTemplate.update(updateQuery);

        if(yes>0){
            resultModel.setMessage("Successful");
            resultModel.setTwoStepState(twoStepState);
        }
        else{
            resultModel.setMessage("Failed");
        }
        resultList.add(resultModel);
        return resultList;

    }

    class PhoneNumber implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ChangePasswordModel changePasswordModel=new ChangePasswordModel();
            changePasswordModel.setPhoneNumber(rs.getString("rtnValue"));
            return changePasswordModel;
        }
    }
    class Count implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ChangePasswordModel changePasswordModel=new ChangePasswordModel();
            changePasswordModel.setCount(rs.getInt("rtnValue"));
            return changePasswordModel;
        }
    }
    class TwoStepState implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ChangePasswordModel changePasswordModel=new ChangePasswordModel();
            changePasswordModel.setTwoStepState(rs.getInt("two_stp_st"));
            return changePasswordModel;
        }
    }
}
