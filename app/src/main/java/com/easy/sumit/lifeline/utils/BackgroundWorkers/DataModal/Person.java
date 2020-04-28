package com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal;

import android.os.Bundle;

import com.easy.sumit.lifeline.utils.Constants;

public class Person {
    private String user_name=null;
    private String user_pass=null;
    private String user_mail=null;
    private String name=null;
    private String blood_group=null;
    private String gender=null;
    private String age=null;
    private String hiv_status=null;
    private String address=null;
    private String contact_no=null;
    private String country=null;
    private String state=null;
    private String district=null;
    private String sub_district=null;
    private String imei_no=null;

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setHiv_status(String hiv_status) {
        this.hiv_status = hiv_status;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setSub_district(String sub_district) {
        this.sub_district= sub_district;
    }

    public void setImei_no(String imei_no) {
        this.imei_no = imei_no;
    }
    public void setALL(Bundle bundle){
        user_name=bundle.getString(Constants.USER_NAME);
        user_pass=bundle.getString(Constants.USER_PASS);
        user_mail=bundle.getString(Constants.USER_MAIL);
        name=bundle.getString(Constants.NAME);
        blood_group=bundle.getString(Constants.BLOOD_GROUP);
        gender=bundle.getString(Constants.GENDER);
        age=bundle.getString(Constants.AGE);
        hiv_status=bundle.getString(Constants.HIV_STATUS);
        address=bundle.getString(Constants.ADDRESS);
        contact_no=bundle.getString(Constants.CONTACT_NO);
        country=bundle.getString(Constants.COUNTRY);
        state=bundle.getString(Constants.STATE);
        district=bundle.getString(Constants.DISTRICT);
        sub_district=bundle.getString(Constants.SUB_DISTRICT);
        imei_no=bundle.getString(Constants.IMEI_NO);
    }

    public String getUser_name(){
        return user_name;
    }

    public String getUser_pass(){
        return user_pass;
    }

    public String getUser_mail(){
        return user_mail;
    }

    public String getName(){
        return name;
    }

    public String getBlood_group(){
        return blood_group;
    }

    public String getGender(){
        return gender;
    }

    public String getAge(){
        return age;
    }

    public String getHiv_status(){
        return hiv_status;
    }

    public String getAddress(){
        return address;
    }

    public String getContact_no(){
        return contact_no;
    }

    public String getCountry(){
        return country;
    }

    public String getState(){
        return state;
    }

    public String getDistrict(){
        return district;
    }

    public String getSub_district(){
        return sub_district;
    }

    public String getImei_no(){
        return imei_no;
    }
    public Bundle getALL(){
        Bundle bundle=new Bundle();
        bundle.putString(Constants.USER_NAME,user_name);
        bundle.putString(Constants.USER_PASS,user_pass);
        bundle.putString(Constants.USER_MAIL,user_mail);
        bundle.putString(Constants.NAME,name);
        bundle.putString(Constants.BLOOD_GROUP,blood_group);
        bundle.putString(Constants.GENDER,gender);
        bundle.putString(Constants.AGE,age);
        bundle.putString(Constants.HIV_STATUS,hiv_status);
        bundle.putString(Constants.ADDRESS,address);
        bundle.putString(Constants.CONTACT_NO,contact_no);
        bundle.putString(Constants.COUNTRY,country);
        bundle.putString(Constants.STATE,state);
        bundle.putString(Constants.DISTRICT,district);
        bundle.putString(Constants.SUB_DISTRICT,sub_district);
        bundle.putString(Constants.IMEI_NO,imei_no);
        return bundle;
    }

}