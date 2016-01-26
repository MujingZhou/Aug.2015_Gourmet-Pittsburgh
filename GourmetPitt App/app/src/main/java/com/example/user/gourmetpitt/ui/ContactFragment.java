package com.example.user.gourmetpitt.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.gourmetpitt.R;


public class ContactFragment extends Fragment {

    Button clearbutton;
    Button sendbutton;
    EditText editfeedback;
    Button buttonBack;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        clearbutton = (Button)view.findViewById(R.id.clearbutton);
        sendbutton = (Button)view.findViewById(R.id.sendbutton);
        editfeedback = (EditText)view.findViewById(R.id.editFeedBack);
        buttonBack = (Button) view.findViewById(R.id.buttonBack);

        clearbutton.setOnClickListener(

                new View.OnClickListener() {
                    public void onClick(View view) {

                        editfeedback.setText("");

                    }

                }

        );

        buttonBack.setOnClickListener(
          new View.OnClickListener(){
              public void onClick(View view){
                  goBack();
              }
          }
        );

        return view;
    }

    public void goBack(){
        Intent i = new Intent(getActivity(), CustomHomeActivity.class);
        startActivity(i);
    }
}
