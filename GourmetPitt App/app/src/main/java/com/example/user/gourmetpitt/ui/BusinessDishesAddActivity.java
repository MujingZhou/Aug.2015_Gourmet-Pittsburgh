package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.AddDishesBusiness;


public class BusinessDishesAddActivity extends Activity {

    private int restID;
    private String userName;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_dishes_add);
        Intent intent=getIntent();
        userName=intent.getStringExtra("BusinessUserName");
        int restID=intent.getIntExtra("restID",-1);
        position=intent.getIntExtra("position",-1);
        this.restID=restID;
        System.out.println("dishes" + restID + userName);
    }

    public void onClickAddDish(View view){
        EditText dishNameText=(EditText)findViewById(R.id.editDish_Name);
        String dishName=dishNameText.getText().toString();




        try {
            synchronized (this) {
                AddDishesBusiness clientSocket = new AddDishesBusiness(this);

                clientSocket.setDishes(restID,dishName);

                clientSocket.start();

                wait();
                clientSocket.closeSession();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Intent intent=new Intent(this,BusinessRestEditActivity.class);
        intent.putExtra("BusinessUserName",userName);
        intent.putExtra("position",position);
        startActivity(intent);

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

}
