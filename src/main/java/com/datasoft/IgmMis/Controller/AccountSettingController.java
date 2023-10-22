package com.datasoft.IgmMis.Controller;

import com.datasoft.IgmMis.Service.AccountSetting.ChangePasswordService;
import com.datasoft.IgmMis.Service.AccountSetting.TwoStepVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/AccountSetting")
public class AccountSettingController {
    @Autowired
    private ChangePasswordService changePasswordService;

    @Autowired
    private TwoStepVerificationService twoStepVerificationService;


//Change Password
    @RequestMapping(value = "/CellNumberAndUpdateOtpForChangePassword/{loginId}",method = RequestMethod.GET)
    @ResponseBody
    public List getCellNumberAndUpdateOtp(@PathVariable String loginId ){
        List list =new ArrayList<>();

        list=changePasswordService.getCellNumberAndUpdateOtp(loginId);

        return list;
    }
    @RequestMapping(value = "/verifyOtpForChangePassword/{phoneNumber}/{otp}",method = RequestMethod.GET)
    @ResponseBody
    public List verifyOtp(@PathVariable String phoneNumber,@PathVariable String otp ){
        List list =new ArrayList<>();

        list=changePasswordService.verifyOtp(phoneNumber,otp);

        return list;
    }
    @RequestMapping(value = "/UpdatePassword/{userName}/{currentPassword}/{newPassword}/{confirmPassword}/{ipAddress}/{password}",method = RequestMethod.GET)
    @ResponseBody
    public List updatePassword(@PathVariable String userName,@PathVariable String currentPassword,@PathVariable String newPassword,@PathVariable String confirmPassword,@PathVariable String ipAddress,@PathVariable String password ){
        List list =new ArrayList<>();

        list=changePasswordService.updatePassword(userName,currentPassword,newPassword,confirmPassword,ipAddress,password);

        return list;
    }


    //Two Step Verification

    @RequestMapping(value = "/CellNumberAndUpdateOtpForTwoStepVerification/{loginId}",method = RequestMethod.GET)
    @ResponseBody
    public List getCellNumberAndUpdateOtpTwoStepVerification(@PathVariable String loginId ){
        List list =new ArrayList<>();

        list=twoStepVerificationService.getCellNumberAndUpdateOtp(loginId);

        return list;
    }
    @RequestMapping(value = "/VerifyOtpForTwoStepVerification/{phoneNumber}/{otp}/{loginId}",method = RequestMethod.GET)
    @ResponseBody
    public List VerifyOtpForTwoStepVerification(@PathVariable String phoneNumber,@PathVariable String otp,@PathVariable String loginId ){
        List list =new ArrayList<>();

        list=twoStepVerificationService.verifyOtp(phoneNumber,otp,loginId);

        return list;
    }
    @RequestMapping(value = "/UpdateTwoStepSettingOrPhoneNumber/{phoneNumber}/{twoStepState}/{loginId}",method = RequestMethod.GET)
    @ResponseBody
    public List updateTwoStepSettingOrPhoneNumber(@PathVariable String phoneNumber,@PathVariable Integer twoStepState,@PathVariable String loginId ){
        List list =new ArrayList<>();

        list=twoStepVerificationService.updateTwoStepSettingOrPhoneNumber(phoneNumber,twoStepState,loginId);

        return list;
    }
}
