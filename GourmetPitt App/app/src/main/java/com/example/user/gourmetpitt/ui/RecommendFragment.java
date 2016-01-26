package com.example.user.gourmetpitt.ui;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.RetrieveRestClient;

import java.util.ArrayList;

import model.Customer;
import model.Rate;
import model.SingleRestaurant;

public class RecommendFragment extends Fragment {

    EditText editrecom;
    ListView recomlist;
    private ArrayList<String> recomitems;
    Customer customer;

    public RecommendFragment() {
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
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        Bundle bundle = getArguments();
        recomitems = (ArrayList<String>)bundle.getSerializable("recommends");
        customer = (Customer)bundle.getSerializable("customer");

        editrecom = (EditText)view.findViewById(R.id.recommendationkey);
        recomlist = (ListView)view.findViewById(R.id.recommendationlist);

        /*recomitems = new ArrayList<String>();

        recomitems.add("BurgerKing");
        recomitems.add("RoseTeaCoffee");
        recomitems.add("CheeseBurger");
        recomitems.add("KoreanGarden");
        recomitems.add("LittleAsian");
        recomitems.add("Skibo");
        recomitems.add("ItalianFamily");
        recomitems.add("Alibaba");*/

        recomlist.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, recomitems));

        editrecom.addTextChangedListener(
                new TextWatcher() {


                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        ArrayList<String> tem = new ArrayList<String>();

                        int textlength = editrecom.getText().length();
                        tem.clear();
                        for (int i = 0; i < recomitems.size(); i++) {
                            if (textlength <= recomitems.get(i).length()) {

                                if (editrecom.getText().toString().equalsIgnoreCase((String) recomitems.get(i).subSequence(0, textlength))) {

                                    tem.add(recomitems.get(i));

                                }


                            }


                        }
                        recomlist.setAdapter(new ArrayAdapter<String>(RecommendFragment.this.getActivity(), android.R.layout.simple_list_item_1, tem));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    public void afterTextChanged(Editable s) {

                    }

                }

        );

        recomlist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String restName = recomitems.get(position);
                        goToRest(restName);
                    }
                }
        );

        return view;
    }

    public void goToRest(String restName){
        try {
            synchronized (this) {
                RetrieveRestClient clientSocket = new RetrieveRestClient(this, restName);
                clientSocket.start();
                this.wait();

                SingleRestaurant rest = clientSocket.restDetail;
                ArrayList<Rate> ratings = clientSocket.ratings;
                Intent i = new Intent(getActivity(), CusRestDetailActivity.class);
                i.putExtra("restDetail", rest);
                i.putExtra("customer", customer);
                i.putExtra("ratings", ratings);
                startActivity(i);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
