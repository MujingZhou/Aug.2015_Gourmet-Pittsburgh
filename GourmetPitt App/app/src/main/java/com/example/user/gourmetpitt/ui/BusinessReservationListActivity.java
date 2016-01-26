package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.DefaultSocketClient_RetrieveReservationInfo;

import java.util.ArrayList;

public class BusinessReservationListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_reservation_list);
        Intent intent=getIntent();
        int restaurantID=intent.getIntExtra("rest_ID", -1);

        ListView lv1=(ListView)findViewById(R.id.listView4);

        ArrayList<String []> reserveList=new ArrayList<>();

        try {
            synchronized (this) {
                DefaultSocketClient_RetrieveReservationInfo clientSocket = new DefaultSocketClient_RetrieveReservationInfo(this);
                clientSocket.setReservationInfo(restaurantID);
                clientSocket.start();

                wait();
                reserveList=clientSocket.reserveList;
                clientSocket.closeSession();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        String []reserveUserName=new String[reserveList.size()];
        String []reserveTime=new String[reserveList.size()];
        String []completeReserve=new String[reserveList.size()];;
        for (int i=0;i<reserveList.size();i++){
            String []singleReserve=reserveList.get(i);
            reserveUserName[i]=singleReserve[0];
            reserveTime[i]=singleReserve[1];
            completeReserve[i]=reserveUserName[i]+" has reserved "+reserveTime[i];
        }



        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, completeReserve);
        lv1.setAdapter(adp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_business_reservation_list, menu);
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
