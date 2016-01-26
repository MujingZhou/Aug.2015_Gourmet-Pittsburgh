package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.DefaultSocketClient_RetrieveInfo;
import com.example.user.gourmetpitt.client.DefaultSocketClient_RetrieveReviewInfo;

import java.util.ArrayList;

import model.SingleRestaurant;
import model.StaticRestaurants;


public class BusinessRestDetailActivity extends Activity {

    ListView lv1;
    ListView lv2;
    ListView lv3;
    ListView lv4;
    private String restName;
    private int restaurantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_rest_detail);

        Intent intent=getIntent();
        int position=intent.getIntExtra("position", -1);
        restName=intent.getStringExtra("rest_name");



        try {
            synchronized (this) {
                DefaultSocketClient_RetrieveInfo clientSocket = new DefaultSocketClient_RetrieveInfo(this);
                clientSocket.start();

                wait();
                clientSocket.closeSession();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        StaticRestaurants staticRestaurants=new StaticRestaurants();
        ArrayList<SingleRestaurant> allRestaurants=staticRestaurants.getStaticRestaurants();

        int restID=allRestaurants.get(position).restaurantID;
        this.restaurantID=restID;

//        edit_dishes_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("choose to add dishes");
//                Intent intent = new Intent(context, BusinessDishesAddActivity.class);
//                intent.putExtra("BusinessUserName",userName);
//                intent.putExtra("restID",restaurantID);
//                intent.putExtra("position", position);
//                startActivity(intent);
//            }
//        });

        float rating=allRestaurants.get(position).averageRating;
        String ratingString="Rating: "+rating+"/10";
        TextView textRating=(TextView)findViewById(R.id.textRating2);
        textRating.setText(ratingString);

        {
            ImageView imageView1=(ImageView)findViewById(R.id.star1_2);
            ImageView imageView2=(ImageView)findViewById(R.id.star2_2);
            ImageView imageView3=(ImageView)findViewById(R.id.star3_2);
            ImageView imageView4=(ImageView)findViewById(R.id.star4_2);
            ImageView imageView5=(ImageView)findViewById(R.id.star5_2);

            imageView1.setImageResource(R.drawable.edit_emptystar);
            imageView2.setImageResource(R.drawable.edit_emptystar);
            imageView3.setImageResource(R.drawable.edit_emptystar);
            imageView4.setImageResource(R.drawable.edit_emptystar);
            imageView5.setImageResource(R.drawable.edit_emptystar);

            if (rating>0&&rating<=1) imageView1.setImageResource(R.drawable.edit_halfstar);
            if (rating>1) {
                imageView1.setImageResource(R.drawable.edit_fullstar);
            }
            if (rating>2) {
                imageView2.setImageResource(R.drawable.edit_halfstar);
            }
            if (rating>3) {
                imageView2.setImageResource(R.drawable.edit_fullstar);
            }
            if (rating>4) {
                imageView3.setImageResource(R.drawable.edit_halfstar);
            }
            if (rating>5) {
                imageView3.setImageResource(R.drawable.edit_fullstar);
            }
            if (rating>6) {
                imageView4.setImageResource(R.drawable.edit_halfstar);
            }
            if (rating>7) {
                imageView4.setImageResource(R.drawable.edit_fullstar);
            }
            if (rating>8) {
                imageView5.setImageResource(R.drawable.edit_halfstar);
            }
            if (rating>9) {
                imageView5.setImageResource(R.drawable.edit_fullstar);
            }


        }





        String defaultRestName=allRestaurants.get(position).restaurantName;
        String defaultLocation=allRestaurants.get(position).location;
        int defaultTelephone=Integer.valueOf(allRestaurants.get(position).telephone);
        String defaultOpenHour=allRestaurants.get(position).openHour;
        String defaultFlavor=allRestaurants.get(position).flavor;


//        EditText editRestName=(EditText)findViewById(R.id.business_edit_restName);
//        editRestName.setText(defaultRestName);
//
//        EditText editLocation=(EditText)findViewById(R.id.editableLocation);
//        editLocation.setText(defaultLocation);
//
//        EditText editTelephone=(EditText)findViewById(R.id.editableTel);
//        editTelephone.setText(String.valueOf(defaultTelephone));
//
//        EditText editOpenHour=(EditText)findViewById(R.id.editableOpenHour);
//        editOpenHour.setText(defaultOpenHour);
//
//        EditText editFlavor=(EditText)findViewById(R.id.editableFlavor);
//        editFlavor.setText(defaultFlavor);


        TextView textDetailRestName=(TextView)findViewById(R.id.business_detail_restName);
        textDetailRestName.setText(defaultRestName);

        TextView textDetailTel=(TextView)findViewById(R.id.detail_tele);
        System.out.println("tele"+defaultTelephone);
        textDetailTel.setText(String.valueOf(defaultTelephone));

        TextView textDetailLocation=(TextView)findViewById(R.id.detailLocation);
        textDetailLocation.setText(defaultLocation);

        TextView textDetailOpenHour=(TextView)findViewById(R.id.detailOpenHour);
        textDetailOpenHour.setText(defaultOpenHour);

        TextView textDetailFlavor=(TextView)findViewById(R.id.detailFlavor);
        textDetailFlavor.setText(defaultFlavor);

        lv1 = (ListView)findViewById(R.id.dishList_detail);
        lv2 = (ListView)findViewById(R.id.listView_detail_review);

        ArrayList<String> dishNameList=allRestaurants.get(position).dishesName;
        String dishes[]=new String[dishNameList.size()];
        System.out.println("new dish list size" + dishNameList.size());
        dishes=dishNameList.toArray(dishes);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dishes);
        lv1.setAdapter(adp);

        ArrayList<String []> reviewList=new ArrayList<>();

        try {
            synchronized (this) {
                DefaultSocketClient_RetrieveReviewInfo clientSocket = new DefaultSocketClient_RetrieveReviewInfo(this);
                clientSocket.setReservationInfo(restaurantID);
                clientSocket.start();

                wait();
                reviewList=clientSocket.reviewList;
                clientSocket.closeSession();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

//        System.out.println("detail review"+reviewList.get(0).length);

        String []reviewUserName=new String[reviewList.size()];
        String []reviewContent=new String[reviewList.size()];
        String []reviewRating=new String[reviewList.size()];
        String []completeReview=new String[reviewList.size()];
        for (int i=0;i<reviewList.size();i++){
            String []singleReview=reviewList.get(i);
            reviewUserName[i]=singleReview[0];
            reviewContent[i]=singleReview[1];
            reviewRating[i]=singleReview[2];
            completeReview[i]=reviewContent[i]+"  ("+reviewRating[i]+"/10)"+" - "+reviewUserName[i];
            System.out.println(singleReview[1]);
        }




        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,completeReview);
        lv2.setAdapter(adp1);




//        String[] reviews = {"not bad(6.5/10) - Derek", "wonderful(9/10) - Zhou", "Dirty environment(3/10) - Run"};
//        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reviews);
//        lv2.setAdapter(adp1);

        int[] images = {R.drawable.rest1, R.drawable.rest2, R.drawable.rest3};
    }

    public void onClickReservation(View view){
        Intent intent=new Intent(this,BusinessReservationListActivity.class);
        intent.putExtra("rest_ID",restaurantID);
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
