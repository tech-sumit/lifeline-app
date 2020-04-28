package com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.easy.sumit.lifeline.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String block_count=null;

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

    public void setBlock_count(String block_count) {
        this.block_count = block_count;
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
        block_count=bundle.getString(Constants.BLOCK_COUNT);
    }

    public void setAll(String jsonString){
        try {
            Log.i("Person.java","jsonString:"+jsonString);
            JSONObject jsonObject=new JSONObject(jsonString);
            if(!jsonObject.equals("")) {
                user_name = jsonObject.getString("user_name");
                user_pass = jsonObject.getString("user_pass");
                user_mail = jsonObject.getString("user_mail");
                name = jsonObject.getString("name");
                blood_group = jsonObject.getString("blood_group");
                gender = jsonObject.getString("gender");
                age = jsonObject.getString("age");
                hiv_status = jsonObject.getString("hiv_status");
                address = jsonObject.getString("address");
                contact_no = jsonObject.getString("contact_no");
                country = jsonObject.getString("country");
                state = jsonObject.getString("state");
                district = jsonObject.getString("district");
                sub_district = jsonObject.getString("sub_district");
                imei_no = jsonObject.getString("IMEI_NO");
                block_count= jsonObject.getString("block_count");
                Log.i("***Data***","Person{\n"+getUser_name()+
                                            "\n"+getUser_pass()+
                                            "\n"+getUser_mail()+
                                            "\n"+getName()+
                                            "\n"+getBlood_group()+
                                            "\n"+getGender()+
                                            "\n"+getAge()+
                                            "\n"+getHiv_status()+
                                            "\n"+getAddress()+
                                            "\n"+getContact_no()+
                                            "\n"+getCountry()+
                                            "\n"+getState()+
                                            "\n"+getDistrict()+
                                            "\n"+getSub_district()+
                                            "\n"+getImei_no()+
                                            "\n"+getBlock_count());
            }else{
                Log.e("ERROR","at Person.java empty jsonString in method public void setAll(String jsonString) ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setAllByPreferences(Context context){

        SharedPreferences pref = context.getSharedPreferences("lifeline", Context.MODE_PRIVATE);
        user_name=pref.getString("user_name",null);
        user_pass=pref.getString("user_pass",null);
        user_mail=pref.getString("user_mail",null);
        name=pref.getString("name",null);
        blood_group=pref.getString("blood_group",null);
        gender=pref.getString("gender",null);
        age=pref.getString("age",null);
        hiv_status=pref.getString("hiv_status",null);
        address=pref.getString("address",null);
        contact_no=pref.getString("contact_no",null);
        country=pref.getString("country",null);
        state=pref.getString("state",null);
        district=pref.getString("district",null);
        sub_district=pref.getString("sub_district",null);
        imei_no=pref.getString("imei_no",null);
        block_count=pref.getString("block_count",null);
        Log.i("***Data by Pref.***","Person{"+
                "\n"+getUser_name()+
                "\n"+getUser_pass()+
                "\n"+getUser_mail()+
                "\n"+getName()+
                "\n"+getBlood_group()+
                "\n"+getGender()+
                "\n"+getAge()+
                "\n"+getHiv_status()+
                "\n"+getAddress()+
                "\n"+getContact_no()+
                "\n"+getCountry()+
                "\n"+getState()+
                "\n"+getDistrict()+
                "\n"+getSub_district()+
                "\n"+getImei_no()+
                "\n"+getBlock_count());
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

    public String getBlock_count() {
        return block_count;
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
        bundle.putString(Constants.BLOCK_COUNT,block_count);
        return bundle;
    }
    public void updatePreferences(Context context){
        SharedPreferences pref = context.getSharedPreferences("lifeline", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_name",user_name);
        editor.putString("user_pass",user_pass);
        editor.putString("user_mail",user_mail);
        editor.putString("name",name);
        editor.putString("blood_group",blood_group);
        editor.putString("gender",gender);
        editor.putString("age",age);
        editor.putString("hiv_status",hiv_status);
        editor.putString("address",address);
        editor.putString("contact_no",contact_no);
        editor.putString("country",country);
        editor.putString("state",state);
        editor.putString("district",district);
        editor.putString("sub_district",sub_district);
        editor.putString("imei_no",imei_no);
        editor.putString("block_count",block_count);
        editor.putString("login_status","true");
        editor.apply();
        Log.i("***Data Update Pref.***","Person{"+
                "\n"+pref.getString("user_name",null)+
                "\n"+pref.getString("user_pass",null)+
                "\n"+pref.getString("user_mail",null)+
                "\n"+pref.getString("name",null)+
                "\n"+pref.getString("blood_group",null)+
                "\n"+pref.getString("gender",null)+
                "\n"+pref.getString("age",null)+
                "\n"+pref.getString("hiv_status",null)+
                "\n"+pref.getString("address",null)+
                "\n"+pref.getString("contact_no",null)+
                "\n"+pref.getString("country",null)+
                "\n"+pref.getString("state",null)+
                "\n"+pref.getString("district",null)+
                "\n"+pref.getString("sub_district",null)+
                "\n"+pref.getString("imei_no",null)+
                "\n"+pref.getString("block_count",null));
    }
}