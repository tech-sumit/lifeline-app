package com.easy.sumit.lifeline.utils;


public class ListModel {
    private String Name = "";
    private String Gender = "";
    private String HIVStatus = "";

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name= Name;
    }

    public String getGender() {
        return this.Gender;
    }

    public void setGender (String Gender ) {
        this.Gender = Gender ;
    }

    public String getHIVStatus() {
        return this.HIVStatus;
    }

    public void setHIVStatus(String HIVStatus) {
        this.HIVStatus = HIVStatus;
    }
}
