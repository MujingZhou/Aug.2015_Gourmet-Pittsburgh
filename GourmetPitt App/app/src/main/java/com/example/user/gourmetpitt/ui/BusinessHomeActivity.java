package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.user.gourmetpitt.R;

public class BusinessHomeActivity extends Activity {

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home);

        Intent intent=getIntent();
        String userName=intent.getStringExtra("BusinessUserName");
        this.userName=userName;
    }

    public void clickMyRestaurant(View view){
            Intent intent=new Intent(this,BusinessMyRestaurantActivity.class);
            intent.putExtra("BusinessUserName",userName);
            startActivity(intent);
    }


//    public void onClickContactUs(View view){
//           Intent intent=new Intent(this,BusinessContactUsActivity.class);
//           intent.putExtra("BusinessUserName",userName);
//           startActivity(intent);
//    }
public void onClickContactUs(View view){
//           Intent intent=new Intent(this,BusinessContactUsActivity.class);
//           intent.putExtra("BusinessUserName",userName);
//           startActivity(intent);
    Intent i = new Intent(Intent.ACTION_SEND);
    i.setType("message/rfc822");
    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"gourmetpitttour@gmail.com"});
    i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
    i.putExtra(Intent.EXTRA_TEXT, "Your feedback");
    try {
        startActivity(Intent.createChooser(i, "Send mail..."));
    } catch (android.content.ActivityNotFoundException ex) {
        Toast.makeText(BusinessHomeActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    }
}

    public void onClickBusinessLogout(View view){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_business_home, menu);
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
