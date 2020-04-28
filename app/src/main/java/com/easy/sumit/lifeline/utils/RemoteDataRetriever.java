package com.easy.sumit.lifeline.utils;

import android.os.AsyncTask;
import android.util.Log;

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

public class RemoteDataRetriever extends AsyncTask<String,Void,String> {

    private String result = "";

    private AsyncResponse asyncResponse;

    public RemoteDataRetriever(AsyncResponse asyncResponse) {
        this.asyncResponse=asyncResponse;
    }

    @Override
    protected String doInBackground(String... strings) {
        String user_name = strings[0];
        String db_action= strings[1];
        String total_data=strings[2];
        try {
            String check_username_url = "http://10.0.2.2:9090/lifeline_app/getData.php";
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(check_username_url).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data =
                    URLEncoder.encode("user_name", "UTF-8") + "=" +
                            URLEncoder.encode(user_name, "UTF-8") + "&" +
                            URLEncoder.encode("db_action", "UTF-8") + "=" +
                            URLEncoder.encode(db_action, "UTF-8")+ "&" +
                            URLEncoder.encode("total_data", "UTF-8") + "=" +
                            URLEncoder.encode(total_data, "UTF-8");
            if(strings.length>3){
                for(int i=3;i<strings.length;i++){
                    Log.i("String["+i+"]:",strings[i]+"   AS:data"+(i-2));
                    post_data+="&"+
                            URLEncoder.encode("data"+(i-2),"UTF-8") + "=" +
                            URLEncoder.encode(strings[i],"UTF-8");
                }

            }
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
