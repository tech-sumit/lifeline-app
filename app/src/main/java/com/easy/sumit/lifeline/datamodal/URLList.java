package com.easy.sumit.lifeline.datamodal;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.easy.sumit.lifeline.R;

/**
 * Created by Sumit on 27-07-2017.
 */

public class URLList {
    public static String getUrl(Context context,String url){
        SharedPreferences pref;
        String fetched_url="";
        pref = context.getSharedPreferences("lifeline_urls", Context.MODE_PRIVATE);
        fetched_url=pref.getString(""+url,null);
        return fetched_url;
    }

    public static void setDefaultPreferences(Context context){
        SharedPreferences pref = context.getSharedPreferences("lifeline_urls", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String flag=pref.getString("flag",null);
        if(flag==null){
            editor.putString("call_log",context.getString(R.string.call_log));
            editor.putString("getData",context.getString(R.string.getData));
            editor.putString("check_username",context.getString(R.string.check_username));
            editor.putString("eula",context.getString(R.string.eula));
            editor.putString("getLocation",context.getString(R.string.getLocation));
            editor.putString("login",context.getString(R.string.login));
            editor.putString("register",context.getString(R.string.register));
            editor.putString("webpage",context.getString(R.string.webpage));
            editor.apply();

            Log.i("***Data Update Pref.***","Urls{"+
                    "\n"+pref.getString("call_log",null)+
                    "\n"+pref.getString("getData",null)+
                    "\n"+pref.getString("check_username",null)+
                    "\n"+pref.getString("eula",null)+
                    "\n"+pref.getString("getLocation",null)+
                    "\n"+pref.getString("login",null)+
                    "\n"+pref.getString("register",null)+
                    "\n"+pref.getString("webpage",null)+
                    "\n}");

            Log.i("Default Preference","Setup Done");
        }else{
            Log.i("Default Preference","Setup Failed");
        }
    }
}