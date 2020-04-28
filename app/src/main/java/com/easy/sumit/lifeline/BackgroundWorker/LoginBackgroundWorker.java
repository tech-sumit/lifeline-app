package com.easy.sumit.lifeline.BackgroundWorker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Sumit on 15-08-2016.
 */
public class LoginBackgroundWorker extends AsyncTask<String,Void,String> {

    private Context context;
    private String user_name="",user_pass="";
    private String login_url="http://10.0.2.2:9090/dbproject/login.php";
    private HttpURLConnection httpURLConnection;
    private OutputStream outputStream;
    private BufferedWriter bufferedWriter;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private String post_data="",result="";
    private AsyncResponse asyncResponse;

    public LoginBackgroundWorker(Context context,AsyncResponse asyncResponse){
        this.context=context;
        this.asyncResponse=asyncResponse;
    }
    @Override
    protected String doInBackground(String... strings) {
        user_name=strings[0];
        user_pass=strings[1];

        try {
            httpURLConnection=(HttpURLConnection) new URL(login_url).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            outputStream=httpURLConnection.getOutputStream();
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            post_data= URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                    URLEncoder.encode("user_pass","UTF-8")+"="+URLEncoder.encode(user_pass,"UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream=httpURLConnection.getInputStream();
            bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

            String line="";
            while ((line=bufferedReader.readLine())!=null){
                result+=line;
            }
            inputStream.close();
            bufferedReader.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
