package com.easy.sumit.lifeline.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.AsyncResponse;
import com.easy.sumit.lifeline.utils.RemoteDataRetriever;

public class PersonSearchFragment extends Fragment implements AsyncResponse{

    private String user_name;

    private Spinner personBloodGroup;
    private Button buttonSearch;
    private View view;
    private ArrayAdapter<CharSequence> arrayAdapterBloodGroup;
    private String bloodGroup="";

    public PersonSearchFragment(){

    }
    @SuppressLint("ValidFragment")
    public PersonSearchFragment(View view){
        this.view=view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_name = getArguments().getString("user_name");

        }
        else{
            Log.e("ERROR","NO user_name recived as intent argument");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonSearch= (Button) getActivity().findViewById(R.id.buttonSearch);

        personBloodGroup= (Spinner) getActivity().findViewById(R.id.spinnerBloodGroup);

        arrayAdapterBloodGroup = ArrayAdapter.
                createFromResource(getContext(),
                        R.array.blood_groups,
                        R.layout.support_simple_spinner_dropdown_item);
        personBloodGroup.setAdapter(arrayAdapterBloodGroup);
        personBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodGroup = personBloodGroup.getSelectedItem().toString();
                Log.i("Blood Group Selected",bloodGroup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        RemoteDataRetriever remoteDataRetriever=new RemoteDataRetriever(this);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bloodGroup.equals("")) {
                    remoteDataRetriever.execute(user_name, "2","1", bloodGroup);
                }
                else{
                    Toast.makeText(getActivity(),"Please select blood group",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        ResultFragment resultFragment=new ResultFragment();
        Bundle bundle=new Bundle();
        bundle.putString("output",output);
        resultFragment.setArguments(bundle);
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.personSearchFragment,resultFragment).commit();
    }

}
/*package com.easy.sumit.lifeline.Fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Log.d("OUTPUT String",output);
            JSONArray jsonArray=new JSONArray(output);
            Log.i("******OUTPUT******",output);
            itemTitles=new String[jsonArray.length()];
            JSONObject json;
            arrayAdapter=new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item);
            for(int i=0;i<jsonArray.length();i++){
                json=jsonArray.getJSONObject(i);
                itemTitles[i]="";
                itemTitles[i]+="Name: "+json.getString("name");
                itemTitles[i]+=" Gender: "+json.getString("gender");
                itemTitles[i]+=" HIV Status: "+json.getString("hiv_status");
                itemTitles[i]+=" Address: "+json.getString("address");
                itemTitles[i]+=" Contact No. "+json.getString("contact_no");
                arrayAdapter.add(itemTitles[i]);
            }
            for(int i=0;i<itemTitles.length;i++){
                arrayAdapter.add(itemTitles[i]);
                Log.i("***Data***",itemTitles[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listResultView= (ListView) getActivity().findViewById(R.id.listResultView);
        listResultView.setAdapter(arrayAdapter);

    }
}
*/