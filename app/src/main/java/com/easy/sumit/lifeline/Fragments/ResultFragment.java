package com.easy.sumit.lifeline.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.CustomAdapter;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.ListModel;

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
    private JSONArray jsonArray;
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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (OUTPUT_STATUS == 0) {
            try {
                Log.d("OUTPUT String", output);
                jsonArray = new JSONArray(output);
                Log.i("******OUTPUT******", output);
                JSONObject json;
                arrayList = new ArrayList<>();
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
            listResultView.setOnItemClickListener((adapterView, view1, i, l) -> {
                ListModel listModel = arrayList.get(i);
                Log.i("***Item Selected***", listModel.getName());
                DetailResult detailResult=DetailResult.newInstance(jsonArray,i);
                FragmentManager fragmentManager=getFragmentManager();
                fragmentManager.beginTransaction()
                               .replace(R.id.resultLayout,detailResult)
                               .addToBackStack("DetailResult")
                               .commit();
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listResultView.setVisibility(View.INVISIBLE);
        listResultView.setClickable(false);
    }
}