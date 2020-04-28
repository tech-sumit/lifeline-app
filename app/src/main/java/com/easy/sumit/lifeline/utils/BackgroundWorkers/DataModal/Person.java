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
    private String sec_question=null;
    private String sec_answer=null;
    private String name=null;
    private String blood_group=null;
    private String gender=null;
    private String age=null;
    private String address=null;
    private String contact_no=null;
    private String country=null;
    private String state=null;
    private String district=null;
    private String sub_district=null;
    private String last_donated =null;

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public void setSec_question(String sec_question) {
        this.sec_question = sec_question;
    }

    public void setSec_answer(String sec_answer) {
        this.sec_answer = sec_answer;
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

    public void setLast_donated(String last_donated) {
        this.last_donated = last_donated;
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

    public void setALL(Bundle bundle){
        user_name=bundle.getString(Constants.USER_NAME);
        user_pass=bundle.getString(Constants.USER_PASS);
        user_mail=bundle.getString(Constants.USER_MAIL);
        name=bundle.getString(Constants.NAME);
        blood_group=bundle.getString(Constants.BLOOD_GROUP);
        gender=bundle.getString(Constants.GENDER);
        age=bundle.getString(Constants.AGE);
        address=bundle.getString(Constants.ADDRESS);
        contact_no=bundle.getString(Constants.CONTACT_NO);
        country=bundle.getString(Constants.COUNTRY);
        state=bundle.getString(Constants.STATE);
        district=bundle.getString(Constants.DISTRICT);
        sub_district=bundle.getString(Constants.SUB_DISTRICT);
        last_donated =bundle.getString(Constants.LAST_DONATED);
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
                address = jsonObject.getString("address");
                contact_no = jsonObject.getString("contact_no");
                country = jsonObject.getString("country");
                state = jsonObject.getString("state");
                district = jsonObject.getString("district");
                sub_district = jsonObject.getString("sub_district");
                last_donated= jsonObject.getString("last_donated");
                Log.i("***Data***","Person{\n"+getUser_name()+
                                            "\n"+getUser_pass()+
                                            "\n"+getUser_mail()+
                                            "\n"+getName()+
                                            "\n"+getBlood_group()+
                                            "\n"+getGender()+
                                            "\n"+getAge()+
                                            "\n"+getAddress()+
                                            "\n"+getContact_no()+
                                            "\n"+getCountry()+
                                            "\n"+getState()+
                                            "\n"+getDistrict()+
                                            "\n"+getSub_district()+
                                            "\n"+getLast_donated());
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
        address=pref.getString("address",null);
        contact_no=pref.getString("contact_no",null);
        country=pref.getString("country",null);
        state=pref.getString("state",null);
        district=pref.getString("district",null);
        sub_district=pref.getString("sub_district",null);
        last_donated=pref.getString("last_donated",null);
        Log.i("***Data by pref***","Person{\n"+getUser_name()+
                "\n"+getUser_pass()+
                "\n"+getUser_mail()+
                "\n"+getName()+
                "\n"+getBlood_group()+
                "\n"+getGender()+
                "\n"+getAge()+
                "\n"+getAddress()+
                "\n"+getContact_no()+
                "\n"+getCountry()+
                "\n"+getState()+
                "\n"+getDistrict()+
                "\n"+getSub_district()+
                "\n"+getLast_donated());
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

    public String getSec_question() {
        return sec_question;
    }

    public String getSec_answer() {
        return sec_answer;
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

    public String getLast_donated() {
        return last_donated;
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
        bundle.putString(Constants.ADDRESS,address);
        bundle.putString(Constants.CONTACT_NO,contact_no);
        bundle.putString(Constants.COUNTRY,country);
        bundle.putString(Constants.STATE,state);
        bundle.putString(Constants.DISTRICT,district);
        bundle.putString(Constants.SUB_DISTRICT,sub_district);
        bundle.putString(Constants.LAST_DONATED,last_donated);
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
        editor.putString("address",address);
        editor.putString("contact_no",contact_no);
        editor.putString("country",country);
        editor.putString("state",state);
        editor.putString("district",district);
        editor.putString("sub_district",sub_district);
        editor.putString("last_donated",last_donated);
        editor.apply();
        Log.i("***Data Update Pref.***","Person{"+
                "\n"+pref.getString("user_name",null)+
                "\n"+pref.getString("user_pass",null)+
                "\n"+pref.getString("user_mail",null)+
                "\n"+pref.getString("name",null)+
                "\n"+pref.getString("blood_group",null)+
                "\n"+pref.getString("gender",null)+
                "\n"+pref.getString("age",null)+
                "\n"+pref.getString("address",null)+
                "\n"+pref.getString("contact_no",null)+
                "\n"+pref.getString("country",null)+
                "\n"+pref.getString("state",null)+
                "\n"+pref.getString("district",null)+
                "\n"+pref.getString("sub_district",null)+
                "\n"+pref.getString("last_donated",null));
     }
}
