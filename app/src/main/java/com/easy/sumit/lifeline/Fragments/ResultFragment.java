package com.easy.sumit.lifeline.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.CustomAdapter;
import com.easy.sumit.lifeline.utils.ListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultFragment extends Fragment {

    private ListView listResultView;
    private String output = "";
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<ListModel> arrayList = new ArrayList<>();
    private int OUTPUT_STATUS = 0;

    private CustomAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            output = getArguments().getString("output");
            if (output.equals("null")) {
                OUTPUT_STATUS = 1;
            } else {
                OUTPUT_STATUS = 0;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        listResultView = (ListView) activity.findViewById(R.id.list);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (OUTPUT_STATUS == 0) {
            try {
                Log.d("OUTPUT String", output);
                JSONArray jsonArray = new JSONArray(output);
                Log.i("******OUTPUT******", output);
                JSONObject json;
                arrayList = new ArrayList<ListModel>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    json = jsonArray.getJSONObject(i);
                    ListModel data = new ListModel();

                    data.setName("Name: " + json.getString("name"));
                    data.setGender("Gender: " + json.getString("gender"));
                    data.setHIVStatus("HIV Status: " + json.getString("hiv_status"));

                    arrayList.add(data);
                }
                adapter = new CustomAdapter(this, arrayList);
                for (int i = 0; i < arrayList.size(); i++) {
                    Log.i("***Data***", arrayList.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listResultView = (ListView) getActivity().findViewById(R.id.list);
            listResultView.setAdapter(adapter);
            listResultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ListModel listModel = arrayList.get(i);
                    Log.i("***Item Selected***", listModel.getName());
                }
            });
        }
    }
}