package com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal;


public class ListModel {
    private Person person;
    public ListModel(){
        person=new Person();
    }
    public String getName() {
        return person.getName();
    }

    public void setName(String name) {
        person.setName(name);
    }

    public String getGender() {
        return person.getGender();
    }

    public void setGender (String gender ) {
        person.setGender(gender);
    }

    public String getHIVStatus() {
        return person.getHiv_status();
    }

    public void setHIVStatus(String hivStatus) {
        person.setHiv_status(hivStatus);
    }
}
