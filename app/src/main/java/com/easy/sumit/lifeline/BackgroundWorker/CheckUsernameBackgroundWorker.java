package com.easy.sumit.lifeline.BackgroundWorker;

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
 * Created by Sumit on 16-08-2016.
 */
public class CheckUsernameBackgroundWorker extends AsyncTask<String,Void,String> {
    private String user_name = "";
    private String result = "";
    private String check_username_url = "http://10.0.2.2:9090/dbproject/check_username.php";
    private String post_data = "";

    private Context context;
    private HttpURLConnection httpURLConnection;

    private OutputStream outputStream;
    private BufferedWriter bufferedWriter;

    private InputStream inputStream;
    private BufferedReader bufferedReader;

    private AsyncResponse asyncResponse;

    public CheckUsernameBackgroundWorker(Context context,AsyncResponse asyncResponse) {
        this.context = context;
        this.asyncResponse=asyncResponse;
    }

    @Override
    protected String doInBackground(String... strings) {
        user_name = strings[0];
        try {
            httpURLConnection = (HttpURLConnection) new URL(check_username_url).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            outputStream = httpURLConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
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
        asyncResponse.processFinish(result);
    }
}
