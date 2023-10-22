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
import java.text.SimpleDateFormat;

import java.util.*;

@Service
public class ChangePasswordService {
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
        PhoneNumberList=primaryDBTemplate.query(sqlQuery,new ChangePasswordService.PhoneNumber());
        phoneNumber=PhoneNumberList.get(0).getPhoneNumber();
        if(phoneNumber.equals("") || phoneNumber==null){
            resultModel.setMessage("notFound");
        }
        else {
            Random random = new Random();
            int otp = random.ints(1, 9999)
                    .findFirst()
                    .getAsInt();
            System.out.println(otp);
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
    public List verifyOtp(String phoneNumber,String verifyCode){
        List<ChangePasswordModel> resultList=new ArrayList<>();
        ChangePasswordModel resultModel=new ChangePasswordModel();
        String sqlQuery="";
        List<ChangePasswordModel> verifyList=new ArrayList<>();
        sqlQuery="SELECT COUNT(*) AS rtnValue FROM users_info WHERE mobile_no='"+phoneNumber+"' AND otp_code='"+verifyCode+"'";
        verifyList=primaryDBTemplate.query(sqlQuery,new ChangePasswordService.Count());
        Integer c=0;

        System.out.println("size "+verifyList.size());
        if(verifyList.size()>0){
           c=verifyList.get(0).getCount();
        }
        if(c>0){
            resultModel.setMessage("verified");
        }
        else{
            resultModel.setMessage("notverified");
        }
        resultList.add(resultModel);
        return resultList;
    }
    public List updatePassword(String userName,String currentPassword,String newPassword,String cofirmPassword,String ipAddress,String password){
        List<ChangePasswordModel> resultList=new ArrayList<>();
        ChangePasswordModel resultModel=new ChangePasswordModel();
        String sqlQuery="";
        List<ChangePasswordModel> currentPasswordList=new ArrayList<>();
        sqlQuery="SELECT login_password as rtnValue FROM users_info WHERE login_id='"+userName+"'";
        currentPasswordList=primaryDBTemplate.query(sqlQuery,new ChangePasswordService.CurrentPassword());
        String oldPassword="";
        if(currentPasswordList.size()>0){
            oldPassword=currentPasswordList.get(0).getCurrent();
        }
        if(!currentPassword.equals(oldPassword)){
            resultModel.setMessage("Current password is not Match. Press back to try again.");

        }
        else if(newPassword.equals(cofirmPassword)){

            String updateQuery="";
            Integer yes=0;
            updateQuery="UPDATE users_info SET login_password='"+newPassword+"',ptext='"+password+"',update_by='"+userName+"',update_ip='"+ipAddress+"' WHERE login_id='"+userName+"'";
            primaryDBTemplate.update(updateQuery);

            String logQuery="";
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            cal.add(Calendar.YEAR, 1); // to get previous year add -1
            Date nextYear = cal.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime= formatter.format(nextYear);
            dateTime.replace("+","");
            logQuery="INSERT INTO user_update_log_backup_01_June(update_for,update_by,update_at,ptext,user_ip,expire_date) \n" +
                    "\t\t\t\t\tVALUES('"+userName+"','"+userName+"',NOW(),'"+password+"','"+ipAddress+"','"+dateTime+"')";
            yes=primaryDBTemplate.update(logQuery);

            resultModel.setMessage("Password Updated Successfully");
        }
        else{

            resultModel.setMessage("New Password did not match. Press back to try again.");

        }
        resultList.add(resultModel);
        return resultList;
    }
    class PhoneNumber implements RowMapper{

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
    class CurrentPassword implements RowMapper{

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ChangePasswordModel changePasswordModel=new ChangePasswordModel();
            changePasswordModel.setCurrent(rs.getString("rtnValue"));
            return changePasswordModel;
        }
    }

}
