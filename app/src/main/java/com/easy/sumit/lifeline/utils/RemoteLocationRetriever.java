package com.easy.sumit.lifeline.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;

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
import java.util.ArrayList;

public class RemoteLocationRetriever extends AsyncTask<String,Void,String> {

    private String result = "";
    private JSONArray jsonArray;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private Spinner spinner;
    private Activity activity;
    private String user_name;
    private String db_action;
    private String location_level;
    private String data;
    public RemoteLocationRetriever(Activity activity,Spinner spinner) {
        this.spinner=spinner;
        this.activity=activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        user_name = strings[0];
        db_action= strings[1];
        location_level= strings[2];
        data=strings[3];
        try {
            String url = "http://10.0.2.2:9090/lifeline_app/getLocation.php";
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
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
                            URLEncoder.encode("location_level", "UTF-8") + "=" +
                            URLEncoder.encode(location_level, "UTF-8")+ "&" +
                            URLEncoder.encode("data", "UTF-8") + "=" +
                            URLEncoder.encode(data, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            Log.i("***Info***","BufferedWriter flushed.");
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            Log.i("***Info***","Result gathered successfully.");
            bufferedWriter.close();
            outputStream.close();
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
        Log.i("***Info***","Inside onPostExecute().");
        initSpinner(spinner,result);
        Log.i("***Info***","Spinner Innicialised.");
    }
    void initSpinner(Spinner spinner, String string){
        try {
            String label="";
            if(location_level.equals("1")){
                label="state";
            }else if(location_level.equals("2")){
                label="district";
            }else if(location_level.equals("3")){
                label="sub_district";
            }
            Log.i("Locaion Level:",""+label);
            Log.i("JSON Input String:",""+string);
            arrayList = new ArrayList<String>();
            jsonArray=new JSONArray(string);
            for(int i=0;i<jsonArray.length();i++){
                Log.d("\n"+label+" "+i,jsonArray.getString(i));
                arrayList.add(jsonArray.getJSONObject(i).getString(label));
            }
            arrayAdapter=new ArrayAdapter<String>(activity,
                    android.R.layout.simple_dropdown_item_1line,
                    arrayList);
            spinner.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
