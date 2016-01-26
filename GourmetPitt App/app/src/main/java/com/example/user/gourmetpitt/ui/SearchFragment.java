package com.example.user.gourmetpitt.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.AllRestClient;

import java.util.ArrayList;

import model.Customer;


public class SearchFragment extends Fragment {

    EditText sEditText;
    Button showSearch;
    Button voicecontrol;

    //Button showHistory;
    //Button returnBack;
    public Customer customer;

    public SearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        sEditText = (EditText)view.findViewById(R.id.editTextSearch);
        showSearch = (Button)view.findViewById(R.id.buttonSearch);
        voicecontrol = (Button)view.findViewById(R.id.voiceinputbutton);
        //showHistory = (Button)view.findViewById(R.id.buttonHistory);
        //returnBack = (Button)view.findViewById(R.id.buttonReturn);

        Bundle bundle = getArguments();
        customer = (Customer)bundle.getSerializable("customerInfo");


        voicecontrol.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        voiceword(v);

                    }
                }

        );




        showSearch.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        searchres(view);

                    }

                }

        );

        /*showHistory.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        reshistory(view);

                    }

                }

        );*/

        return view;
    }


    public void voiceword(View view){

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(intent, 5);

        }catch(ActivityNotFoundException e){
            Toast t = Toast.makeText(this.getActivity().getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==5&&resultCode==this.getActivity().RESULT_OK){

            ArrayList<String> text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String result = text.get(0);
            sEditText.setText(result.toCharArray(),0,result.length());

        }

    }





    public void searchres(View view){
        try {
            synchronized (this) {
                String searchPlace = sEditText.getText().toString();

                AllRestClient allRestClient = new AllRestClient(this);
                allRestClient.start();
                this.wait();

                ArrayList<String> allRests = allRestClient.allRests;
                ArrayList<String> results = new ArrayList<String>();

                for(String restName: allRests){
                    String name = restName.toLowerCase();
                    if(name.contains(searchPlace.toLowerCase())){
                        results.add(restName);
                    }
                }
                if(results.size() > 0) {
                    Intent intent = new Intent(this.getActivity(), SearchResultActivity.class);
                    intent.putExtra("results", results);
                    intent.putExtra("customer", customer);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity(), "No Results", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void reshistory(View view){
        Intent i = new Intent(this.getActivity(), HistoryActivity.class);
        startActivity(i);

    }

    public void returnback(View view){

        Intent i = new Intent(this.getActivity(), CustomHomeActivity.class);
        startActivity(i);

    }

}
