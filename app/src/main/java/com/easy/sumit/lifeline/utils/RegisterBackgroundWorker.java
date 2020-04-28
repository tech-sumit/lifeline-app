package com.easy.sumit.lifeline.utils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RegisterBackgroundWorker extends AsyncTask<String,Void,String> {

    private String result = "";

    private AsyncResponse asyncResponse;

    public RegisterBackgroundWorker(AsyncResponse asyncResponse) {
        this.asyncResponse=asyncResponse;
    }

    @Override
    protected String doInBackground(String... strings) {
        String name = strings[0];
        String blood_group = strings[1];
        String gender = strings[2];
        String age;
        age = strings[3];
        String hiv_status = strings[4];
        String address = strings[5];
        String contact_no = strings[6];
        String user_name = strings[7];
        String user_mail = strings[8];
        String user_pass = strings[9];
        String state = strings[10];
        String district = strings[11];
        String sub_district = strings[12];
        String IMEI_NO=strings[13];
        try {
            String check_username_url = "http://10.0.2.2:9090/lifeline_app/register.php";
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(check_username_url).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data =
                    URLEncoder.encode("user_name", "UTF-8") + "=" +
                            URLEncoder.encode(user_name, "UTF-8") + "&" +
                    URLEncoder.encode("user_mail", "UTF-8") + "=" +
                            URLEncoder.encode(user_mail, "UTF-8") + "&" +
                    URLEncoder.encode("user_pass", "UTF-8") + "=" +
                            URLEncoder.encode(user_pass, "UTF-8") + "&" +
                    URLEncoder.encode("name", "UTF-8") + "=" +
                            URLEncoder.encode(name, "UTF-8") + "&" +
                    URLEncoder.encode("blood_group", "UTF-8") + "=" +
                            URLEncoder.encode(blood_group, "UTF-8") + "&" +
                    URLEncoder.encode("gender", "UTF-8") + "=" +
                            URLEncoder.encode(gender, "UTF-8") + "&" +
                    URLEncoder.encode("age", "UTF-8") + "=" +
                            URLEncoder.encode(age, "UTF-8") + "&" +
                    URLEncoder.encode("hiv_status", "UTF-8") + "=" +
                            URLEncoder.encode(hiv_status, "UTF-8") + "&" +
                    URLEncoder.encode("address", "UTF-8") + "=" +
                            URLEncoder.encode(address, "UTF-8") + "&" +
                    URLEncoder.encode("contact_no", "UTF-8") + "=" +
                            URLEncoder.encode(contact_no, "UTF-8")+ "&" +
                    URLEncoder.encode("state", "UTF-8") + "=" +
                            URLEncoder.encode(state, "UTF-8")+ "&" +
                    URLEncoder.encode("district", "UTF-8") + "=" +
                            URLEncoder.encode(district, "UTF-8") + "&" +
                    URLEncoder.encode("sub_district", "UTF-8") + "=" +
                            URLEncoder.encode(sub_district, "UTF-8") + "&" +
                    URLEncoder.encode("IMEI_NO", "UTF-8") + "=" +
                            URLEncoder.encode(IMEI_NO, "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            inputStream.close();
            bufferedReader.close();
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        asyncResponse.processFinish(result);
    }
}
