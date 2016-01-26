package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.MakeReserveClient;

import model.Customer;
import model.Reservation;
import model.SingleRestaurant;

public class MakeReservationActivity extends Activity {
    EditText editTime;
    EditText editDate;
    SingleRestaurant restaurant;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);

        editTime = (EditText)findViewById(R.id.editText2);
        editDate = (EditText)findViewById(R.id.editText3);

        Bundle bundle = getIntent().getExtras();
        restaurant = (SingleRestaurant)bundle.getSerializable("restaurant");
        customer = (Customer)bundle.getSerializable("customer");
    }

    public void onReserveClick(View view){

        try {
            synchronized (this) {
                try {
                    String time = editTime.getText().toString();

                    String[] check = time.split(":");
                    if(check.length != 2)   throw new Exception();
                    int hr = Integer.parseInt(check[0]);
                    if(hr < 0 || hr > 24)   throw new Exception();
                    int minute = Integer.parseInt(check[1]);
                    if(minute < 0 || minute > 60)   throw new Exception();

                    String date = editDate.getText().toString();
                    check = date.split("/");
                    if(check.length != 3)   throw new Exception();

                    String reserveTime = time + " " + date;
                    Reservation reserve = new Reservation(restaurant.restaurantName, reserveTime);
                    MakeReserveClient socketClient = new MakeReserveClient(this, customer, reserve);
                    socketClient.start();
                    wait();

                    Toast.makeText(this, "Reserved", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Toast.makeText(this, "Illegal input\nTime hr:min\nDate mm/dd/year", Toast.LENGTH_SHORT).show();
                }

                /*Intent i = new Intent(this, CusRestDetailActivity.class);
                i.putExtra("restDetail", restaurant);
                i.putExtra("customer", customer);
                startActivity(i);*/
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_reservation, menu);
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
}
