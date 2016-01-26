package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.user.gourmetpitt.R;


public class CustomerMainPageFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_main_page, container, false);
        Button logoutButton = (Button) view.findViewById(R.id.logout);
        logoutButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                    }
                }
        );
        return view;
    }

}
