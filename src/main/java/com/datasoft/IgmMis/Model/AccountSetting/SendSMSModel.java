package com.datasoft.IgmMis.Model.AccountSetting;

import java.net.HttpURLConnection;
import java.net.URL;

public class SendSMSModel {
    public void sendSMS(String phoneNumber,String text){
        try {
            String baseUrl ="https://ej8nq1.api.infobip.com/sms/1/text/query?";
            String userName = "username=datasoft_ctms";
            String password = "&password=CTMStos45_@23$";
            String message = "&sender=8804445654290&to=88"+phoneNumber+"&text="+text+"&from=8804445654290";
            String url=baseUrl+userName+password+message;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            int code=connection.getResponseCode();

        }
        catch (Exception e){
            e.printStackTrace();

        }


    }
}
