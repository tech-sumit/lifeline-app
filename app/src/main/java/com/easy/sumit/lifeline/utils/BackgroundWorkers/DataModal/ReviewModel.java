package com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal;

/**
 * Created by Sumit on 15-06-2017.
 */

public class ReviewModel {
    String from_user="";
    String from_user_name="";
    String date="";
    String time="";

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getFrom_user() {
        return from_user;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}

