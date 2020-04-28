package com.easy.sumit.lifeline.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.easy.sumit.lifeline.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ResultFragment extends Fragment {

    ListView listResultView;
    String output="";
    String itemTitles[];
    ArrayAdapter<String> arrayAdapter;
    List<String> itemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            output=getArguments().getString("output");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Log.d("OUTPUT String",output);
            JSONArray jsonArray=new JSONArray(output);
            Log.i("******OUTPUT******",output);
            itemTitles=new String[jsonArray.length()];
            JSONObject json;

            for(int i=0;i<jsonArray.length();i++){
                json=jsonArray.getJSONObject(i);
                itemTitles[i]="";
                itemTitles[i]+="Name: "+json.getString("name");
                itemTitles[i]+=" Gender: "+json.getString("gender");
                itemTitles[i]+=" HIV Status: "+json.getString("hiv_status");
                itemTitles[i]+=" Address: "+json.getString("address");
                itemTitles[i]+=" Contact No. "+json.getString("contact_no");
            }
            arrayAdapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,itemTitles);
            for(int i=0;i<itemTitles.length;i++){
                Log.i("***Data***",itemTitles[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listResultView= (ListView) getActivity().findViewById(R.id.listResultView);
        listResultView.setAdapter(arrayAdapter);
        listResultView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("***Item Selected***",adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
