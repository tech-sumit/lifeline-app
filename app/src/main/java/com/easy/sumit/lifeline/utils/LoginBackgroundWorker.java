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

public class LoginBackgroundWorker extends AsyncTask<String,Void,String> {

    private String result="";
    private AsyncResponse asyncResponse;

    public LoginBackgroundWorker(AsyncResponse asyncResponse){
        this.asyncResponse=asyncResponse;
    }
    @Override
    protected String doInBackground(String... strings) {
        String user_name = strings[0];
        String user_pass = strings[1];
        String IMEI_NO = strings[2];


        try {
            String login_url = "http://10.0.2.2:9090/lifeline_app/login.php";
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(login_url).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&" +
                    URLEncoder.encode("user_pass", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8") + "&" +
                    URLEncoder.encode("IMEI_NO", "UTF-8") + "=" + URLEncoder.encode(IMEI_NO, "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            String line;
            while ((line= bufferedReader.readLine())!=null){
                result+=line;
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
        asyncResponse.processFinish(s);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
