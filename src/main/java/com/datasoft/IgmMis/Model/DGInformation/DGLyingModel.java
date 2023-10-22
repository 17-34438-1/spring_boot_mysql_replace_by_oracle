package com.datasoft.IgmMis.Model.DGInformation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DGLyingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String vessel_Name;
    public String Import_Rotation_No;
    public String arrival_dt ;
    public String cont_number;
    public String cont_size;
    public String cont_height;
    public String mlo;
    public String carrentPosition;
    public String discharge_dt;
    public String BL_No;
    public String cont_status;
    public String cont_imo;
    public String cont_un;
    public String Description_of_Goods;
    public String Notify_name;
    public String NotifyDesc;
    public String Delivery_Status_date;
    public String rl_no;
    public String rl_date;
    public String obpc_number;
    public String obpc_date;
    public String final_amendment;
    public String response_details1;
    public String response_details2;
    public String response_details3;
    public String navy_response_to_port;
    public String hold_application;
    public String rejected_application;

    public String getVessel_Name() {
        return vessel_Name;
    }

    public void setVessel_Name(String vessel_Name) {
        this.vessel_Name = vessel_Name;
    }

    public String getImport_Rotation_No() {
        return Import_Rotation_No;
    }

    public void setImport_Rotation_No(String import_Rotation_No) {
        Import_Rotation_No = import_Rotation_No;
    }

    public String getArrival_dt() {
        return arrival_dt;
    }

    public void setArrival_dt(String arrival_dt) {
        this.arrival_dt = arrival_dt;
    }

    public String getCont_number() {
        return cont_number;
    }

    public void setCont_number(String cont_number) {
        this.cont_number = cont_number;
    }

    public String getCont_size() {
        return cont_size;
    }

    public void setCont_size(String cont_size) {
        this.cont_size = cont_size;
    }

    public String getCont_height() {
        return cont_height;
    }

    public void setCont_height(String cont_height) {
        this.cont_height = cont_height;
    }

    public String getMlo() {
        return mlo;
    }

    public void setMlo(String mlo) {
        this.mlo = mlo;
    }

    public String getCarrentPosition() {
        return carrentPosition;
    }

    public void setCarrentPosition(String carrentPosition) {
        this.carrentPosition = carrentPosition;
    }

    public String getDischarge_dt() {
        return discharge_dt;
    }

    public void setDischarge_dt(String discharge_dt) {
        this.discharge_dt = discharge_dt;
    }

    public String getBL_No() {
        return BL_No;
    }

    public void setBL_No(String BL_No) {
        this.BL_No = BL_No;
    }

    public String getCont_status() {
        return cont_status;
    }

    public void setCont_status(String cont_status) {
        this.cont_status = cont_status;
    }

    public String getCont_imo() {
        return cont_imo;
    }

    public void setCont_imo(String cont_imo) {
        this.cont_imo = cont_imo;
    }

    public String getCont_un() {
        return cont_un;
    }

    public void setCont_un(String cont_un) {
        this.cont_un = cont_un;
    }

    public String getDescription_of_Goods() {
        return Description_of_Goods;
    }

    public void setDescription_of_Goods(String description_of_Goods) {
        Description_of_Goods = description_of_Goods;
    }

    public String getNotify_name() {
        return Notify_name;
    }

    public void setNotify_name(String notify_name) {
        Notify_name = notify_name;
    }

    public String getNotifyDesc() {
        return NotifyDesc;
    }

    public void setNotifyDesc(String notifyDesc) {
        NotifyDesc = notifyDesc;
    }

    public String getDelivery_Status_date() {
        return Delivery_Status_date;
    }

    public void setDelivery_Status_date(String delivery_Status_date) {
        Delivery_Status_date = delivery_Status_date;
    }

    public String getRl_no() {
        return rl_no;
    }

    public void setRl_no(String rl_no) {
        this.rl_no = rl_no;
    }

    public String getRl_date() {
        return rl_date;
    }

    public void setRl_date(String rl_date) {
        this.rl_date = rl_date;
    }

    public String getObpc_number() {
        return obpc_number;
    }

    public void setObpc_number(String obpc_number) {
        this.obpc_number = obpc_number;
    }

    public String getObpc_date() {
        return obpc_date;
    }

    public void setObpc_date(String obpc_date) {
        this.obpc_date = obpc_date;
    }

    public String getFinal_amendment() {
        return final_amendment;
    }

    public void setFinal_amendment(String final_amendment) {
        this.final_amendment = final_amendment;
    }

    public String getResponse_details1() {
        return response_details1;
    }

    public void setResponse_details1(String response_details1) {
        this.response_details1 = response_details1;
    }

    public String getResponse_details2() {
        return response_details2;
    }

    public void setResponse_details2(String response_details2) {
        this.response_details2 = response_details2;
    }

    public String getResponse_details3() {
        return response_details3;
    }

    public void setResponse_details3(String response_details3) {
        this.response_details3 = response_details3;
    }

    public String getNavy_response_to_port() {
        return navy_response_to_port;
    }

    public void setNavy_response_to_port(String navy_response_to_port) {
        this.navy_response_to_port = navy_response_to_port;
    }

    public String getHold_application() {
        return hold_application;
    }

    public void setHold_application(String hold_application) {
        this.hold_application = hold_application;
    }

    public String getRejected_application() {
        return rejected_application;
    }

    public void setRejected_application(String rejected_application) {
        this.rejected_application = rejected_application;
    }
}
