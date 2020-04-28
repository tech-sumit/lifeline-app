package com.easy.sumit.lifeline.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.datamodal.CustomAdapter;
import com.easy.sumit.lifeline.datamodal.ListModel;
import com.easy.sumit.lifeline.datamodal.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultFragment extends Fragment {

    private String output = "";
    private ArrayList<ListModel> arrayList = new ArrayList<>();
    private int OUTPUT_STATUS = 0;
    private JSONArray jsonArray;
    private CustomAdapter adapter;
    private Person person;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        person=new Person();
        person.setAllByPreferences(getContext());
        if (getArguments() != null) {
            output = getArguments().getString("output");
            if(savedInstanceState!=null){
                if(!savedInstanceState.isEmpty()){
                    output=savedInstanceState.getString("output");
                }
            }
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
                    data.setLastDonated("Last Donated: " + json.getString("last_donated"));

                    arrayList.add(data);
                }
                adapter = new CustomAdapter(this, arrayList);
                for (int i = 0; i < arrayList.size(); i++) {
                    Log.i("***Data***", arrayList.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ListView listResultView = (ListView) getActivity().findViewById(R.id.list);
            Log.i("**ResultFragment","ResultFragment Started");
            listResultView.setAdapter(adapter);
            Log.i("**ResultFragment","List adapter innicialised");
            listResultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListModel listModel = arrayList.get(position);
                    Log.i("***Item Selected***", listModel.getName());
                    DetailResult detailResult=DetailResult.newInstance(jsonArray,position,person.getUser_name(),person.getName());
                    FragmentManager fragmentManager=getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.resultLayout,detailResult)
                            .addToBackStack("DetailResult")
                            .commit();
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle=new Bundle();
        bundle.putString("output",output);
        outState.putAll(bundle);
    }
}