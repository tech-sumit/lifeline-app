package com.easy.sumit.lifeline.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;

public class ContactUs extends Fragment {
    private Person person;
    private EditText feedbackText;
    private Button sendFeedback;
    public ContactUs() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        person=new Person();
        person.setAllByPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        feedbackText= (EditText) view.findViewById(R.id.feedback_text);
        sendFeedback= (Button) view.findViewById(R.id.send_feedback);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,"17.sumitagrawal@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Lifeline User Report by, "+person.getUser_name());
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_TEXT,""+feedbackText.getText().toString());
                try{
                    startActivity(intent);
                }catch (ActivityNotFoundException exception){
                    exception.printStackTrace();
                    Toast.makeText(getContext(),"Email clients not found",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
