package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.AddFavClient;
import com.example.user.gourmetpitt.client.CusRateClient;
import com.example.user.gourmetpitt.client.DeleteFavClient;

import java.util.ArrayList;

import model.Customer;
import model.Rate;
import model.SingleRestaurant;


public class CusRestDetailActivity extends Activity {

    ListView lv1;
    ListView lv2;
    ListView lv3;
    SingleRestaurant rest;
    Customer customer;
    EditText editRate;
    EditText editReview;
    ArrayList<Rate> ratings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_rest_detail);

        lv1 = (ListView)findViewById(R.id.listView1);
        lv2 = (ListView)findViewById(R.id.listView2);

        Bundle bundle = getIntent().getExtras();
        rest = (SingleRestaurant)bundle.getSerializable("restDetail");
        customer = (Customer)bundle.getSerializable("customer");
        ratings = (ArrayList<Rate>)bundle.getSerializable("ratings");

        editRate = (EditText)findViewById(R.id.editText);
        editReview = (EditText)findViewById(R.id.editReview);

        TextView textRestName = (TextView)findViewById(R.id.textView10);
        textRestName.setText(rest.restaurantName);
        TextView textRating = (TextView)findViewById(R.id.textView11);
        textRating.setText("Average Rating: " + String.format("%.2f", rest.averageRating));
        TextView textLocation = (TextView)findViewById(R.id.textView14);
        textLocation.setText("Location: " + rest.location);
        TextView textTele = (TextView)findViewById(R.id.textView13);
        textTele.setText("Tele: " + rest.telephone);

        ArrayList<String> dishes = rest.dishesName;
        String[] dishesArr = new String[rest.dishesName.size()];
        for(int i = 0; i < dishes.size(); i++)  dishesArr[i] = dishes.get(i);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dishesArr);
        lv1.setAdapter(adp);

        String[] rateArr = new String[ratings.size()];
        for(int i = 0; i < ratings.size(); i++){
            Rate rate = ratings.get(i);
            rateArr[i] = rate.customerName + ":" + rate.review + " " + rate.rating + "/10";
        }
        adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rateArr);
        lv2.setAdapter(adp);

        ImageView buttonRate = (ImageView)findViewById(R.id.imageView14);
        buttonRate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String rateStr = editRate.getText().toString();
                        String review = editReview.getText().toString();
                        try {
                            float rating = Float.parseFloat(rateStr);
                            if (rating < 0 || rating > 10)
                                Toast.makeText(getApplicationContext(), "Illegal rate", Toast.LENGTH_SHORT).show();
                            else {
                                Rate rate = new Rate(customer.userName, rest.restaurantName, rating, review);
                                addCusRate(rate);
                            }
                        }
                        catch(Exception e) {
                            Toast.makeText(getApplicationContext(), "Illegal rate", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public void onReserveClick(View view){
        Intent i = new Intent(this, MakeReservationActivity.class);
        i.putExtra("restaurant", rest);
        i.putExtra("customer", customer);
        startActivity(i);
    }

    public void addCusRate(Rate rate){
        try {
            synchronized (this) {
                CusRateClient socketClient = new CusRateClient(this, rate);
                socketClient.start();
                wait();
                //Thread.sleep(500);

                /*if (socketClient.restDetail == null) System.out.println("nothing!");
                else    System.out.println("Got restaurant!");
                i.putExtra("restDetail", socketClient.restDetail);
                i.putExtra("customer", customerInfo);
                startActivity(i);*/
                Toast.makeText(this, "Rate added!", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cus_rest_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFavClick(View view) {
        try {
            synchronized (this) {
                //Intent i = new Intent(this, CustomHomeActivity.class);
                AddFavClient addFavClient = new AddFavClient(this, customer, rest);
                addFavClient.start();

                wait();

                /*Fragment myFragment = new PersonalInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("customerInfo", customer);
                myFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, myFragment)
                        .commit();*/
                //startActivity(i);
                Toast.makeText(this, "Favorite added", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

}
