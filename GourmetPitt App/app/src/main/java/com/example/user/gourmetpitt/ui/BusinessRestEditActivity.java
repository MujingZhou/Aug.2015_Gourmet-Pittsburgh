package com.example.user.gourmetpitt.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.DefaultSocketClient_DeleteDishes;
import com.example.user.gourmetpitt.client.DefaultSocketClient_EditRestaurant;
import com.example.user.gourmetpitt.client.DefaultSocketClient_RetrieveInfo;
import com.example.user.gourmetpitt.client.DefaultSocketClient_RetrieveReviewInfo;

import java.util.ArrayList;

import model.SingleRestaurant;
import model.StaticRestaurants;


public class BusinessRestEditActivity extends BusinessSwipeDeleteActivity {

    ListView lv1;
    ListView lv2;

    private int restaurantID;
    private String userName;
    private Context context;
    public int swipePos=0;
    private int restPosition=-1;
    @Override
    public ListView getListView() {
        return lv1;
    }

    @Override
    public void getSwipeItem(boolean isRight, final int position) {
        Toast.makeText(this,
                "Swipe to " + (isRight ? "right" : "left") + " direction",
                Toast.LENGTH_SHORT).show();
        context=this;
//        View current1 = list.getChildAt(position);
//        current1.setAlpha(1f);
//        list.setSelection(position);
        if (!isRight) {
//            --swipePos;
            if (swipePos==1) {
                View current = lv1.getChildAt(position);
                current.setAlpha(1f);
//                LinearLayout r1 = (LinearLayout) findViewById(R.id.linearlayout_edit);
                RelativeLayout r1= (RelativeLayout)findViewById(R.id.relative_edit);
                ImageView iv1 = (ImageView) findViewById(R.id.image_edit_delete);
                ImageView iv2 = (ImageView) findViewById(R.id.image_edit_edit);
                r1.removeView(iv1);
                r1.removeView(iv2);
                iv2.setVisibility(View.INVISIBLE);
                iv1.setVisibility(View.INVISIBLE);
                r1.addView(iv1);
                r1.addView(iv2);
                swipePos=0;
            }
            else if (swipePos == 0||swipePos==-1) {
                View current = lv1.getChildAt(position);

//                current.setVisibility(View.VISIBLE);
//                current.setVisibility(View.GONE);

//                LinearLayout r1 = (LinearLayout) findViewById(R.id.linearlayout_edit);
                RelativeLayout r1= (RelativeLayout)findViewById(R.id.relative_edit);
                ListView l1 = (ListView) findViewById(R.id.dishList);
                current.setAlpha(0.5f);
//                l1.setBackgroundResource(R.color.dim_foreground_material_light);
                ImageView iv1 = (ImageView) findViewById(R.id.image_edit_delete);
                ImageView iv2 = (ImageView) findViewById(R.id.image_edit_edit);


                r1.removeView(iv1);
                iv1.setImageResource(R.drawable.myrest_delete);



                iv2.setVisibility(View.INVISIBLE);
                iv1.setVisibility(View.VISIBLE);

                iv1.setX(current.getX() + 250);
                iv1.setY(current.getY() + l1.getY()+300);

//                context=this;


                //RelativeLayout.LayoutParams p=(RelativeLayout.LayoutParams) current.getLayoutParams();
//                System.out.println("ppp is " + current.getX() + "  " + current.getY());

                r1.addView(iv1);
                iv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("choose to delete");

                        try {
                            synchronized (context) {
                                DefaultSocketClient_DeleteDishes clientSocket = new DefaultSocketClient_DeleteDishes(context);

                                StaticRestaurants staticRestaurants=new StaticRestaurants();
                                ArrayList<SingleRestaurant> allRestaurants=staticRestaurants.getStaticRestaurants();
                                ArrayList<String> dishNameList=allRestaurants.get(restPosition).dishesName;
                                String dishName=dishNameList.get(position);
                                clientSocket.setDeleteDishes(dishName,restaurantID);
                                System.out.println("delete dishes" + dishName);
                                clientSocket.start();

                                context.wait();
                                clientSocket.closeSession();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

//                        return ;

                        Intent intent=new Intent(context,BusinessRestEditActivity.class);

                        intent.putExtra("BusinessUserName",userName);
                        intent.putExtra("position",restPosition);
                        startActivity(intent);
                    }
                });
                swipePos=-1;
            }

        }

        else {
//            ++swipePos;

            if (swipePos==-1) {
                View current = lv1.getChildAt(position);
                current.setAlpha(1f);
//                LinearLayout r1 = (LinearLayout) findViewById(R.id.linearlayout_edit);
                RelativeLayout r1= (RelativeLayout)findViewById(R.id.relative_edit);
                ImageView iv1 = (ImageView) findViewById(R.id.image_edit_delete);
                ImageView iv2 = (ImageView) findViewById(R.id.image_edit_edit);
                r1.removeView(iv1);
                r1.removeView(iv2);
                iv2.setVisibility(View.INVISIBLE);
                iv1.setVisibility(View.INVISIBLE);
                r1.addView(iv1);
                r1.addView(iv2);
                swipePos=0;
            }

            else if (swipePos==0||swipePos==1) {

                View current = lv1.getChildAt(position);
//                current.setVisibility(View.VISIBLE);
//                current.setVisibility(View.GONE);
                current.setAlpha(0.5f);
                ListView l1 = (ListView) findViewById(R.id.dishList);
//                l1.setBackgroundResource(R.color.dim_foreground_material_dark);
//                LinearLayout r1 = (LinearLayout) findViewById(R.id.linearlayout_edit);
                RelativeLayout r1= (RelativeLayout)findViewById(R.id.relative_edit);
//            ListView l1=(ListView)findViewById(R.id.myRestaurantList);
                ImageView iv1 = (ImageView) findViewById(R.id.image_edit_delete);
                ImageView iv2 = (ImageView) findViewById(R.id.image_edit_edit);
                iv2.setImageResource(R.drawable.myrest_edit);
                r1.removeView(iv2);
//            r1.removeView(iv1);
                iv1.setVisibility(View.INVISIBLE);
                iv2.setVisibility(View.VISIBLE);

                iv2.setX(current.getX() + 250);
                iv2.setY(current.getY() + l1.getY()+300);
                r1.addView(iv2);

                iv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        System.out.println("choose to edit");
//                        Intent intent=new Intent(context,BusinessRestEditActivity.class);
//                        intent.putExtra("BusinessUserName",userName);
//                        intent.putExtra("position",position);
//                        startActivity(intent);
                    }
                });
                swipePos=1;
            }
        }
    }

    @Override
    public void onItemClickListener(ListAdapter adapter, int position) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_rest_edit);

        ImageView edit_dishes_add=(ImageView)findViewById(R.id.imageView33);
        context=this;

        Intent intent=getIntent();
        System.out.println("return from add dish");
        final String userName=intent.getStringExtra("BusinessUserName");
        final int position=intent.getIntExtra("position", -1);
        System.out.println("pos in editRest is "+ position+userName);
        restPosition=position;
        this.userName=userName;

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

        edit_dishes_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("choose to add dishes");
                Intent intent = new Intent(context, BusinessDishesAddActivity.class);
                intent.putExtra("BusinessUserName",userName);
                intent.putExtra("restID",restaurantID);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        float rating=allRestaurants.get(position).averageRating;
        String ratingString="Rating: "+rating+"/10";
        TextView textRating=(TextView)findViewById(R.id.textRating);
        textRating.setText(ratingString);

        {
            ImageView imageView1=(ImageView)findViewById(R.id.star1);
            ImageView imageView2=(ImageView)findViewById(R.id.star2);
            ImageView imageView3=(ImageView)findViewById(R.id.star3);
            ImageView imageView4=(ImageView)findViewById(R.id.star4);
            ImageView imageView5=(ImageView)findViewById(R.id.star5);

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


        EditText editRestName=(EditText)findViewById(R.id.business_edit_restName);
        editRestName.setText(defaultRestName);

        EditText editLocation=(EditText)findViewById(R.id.editableLocation);
        editLocation.setText(defaultLocation);

        EditText editTelephone=(EditText)findViewById(R.id.editableTel);
        editTelephone.setText(String.valueOf(defaultTelephone));

        EditText editOpenHour=(EditText)findViewById(R.id.editableOpenHour);
        editOpenHour.setText(defaultOpenHour);

        EditText editFlavor=(EditText)findViewById(R.id.editableFlavor);
        editFlavor.setText(defaultFlavor);

        lv1 = (ListView)findViewById(R.id.dishList);
        lv2 = (ListView)findViewById(R.id.listView3);

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




        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,completeReview );
        lv2.setAdapter(adp1);




//        String[] reviews = {"not bad(6.5/10) - Derek", "wonderful(9/10) - Zhou", "Dirty environment(3/10) - Run"};
//        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reviews);
//        lv2.setAdapter(adp1);

        int[] images = {R.drawable.rest1, R.drawable.rest2, R.drawable.rest3};
    }

    public void onClickEditOK(View view){
        EditText editRestName=(EditText)findViewById(R.id.business_edit_restName);
        String newRestName=editRestName.getText().toString();

        EditText editLocation=(EditText)findViewById(R.id.editableLocation);
        String newLocation=editLocation.getText().toString();

        EditText editTelephone=(EditText)findViewById(R.id.editableTel);
        int newTelephone=Integer.valueOf(editTelephone.getText().toString());

        EditText editOpenHour=(EditText)findViewById(R.id.editableOpenHour);
        String newOpenHour=editOpenHour.getText().toString();

        EditText editFlavor=(EditText)findViewById(R.id.editableFlavor);
        String newFlavor=editFlavor.getText().toString();


        try {
            synchronized (this) {
                DefaultSocketClient_EditRestaurant clientSocket = new DefaultSocketClient_EditRestaurant(this);

                clientSocket.setEditRestaurant(restaurantID,newRestName,newLocation,newTelephone,newOpenHour,newFlavor);

                clientSocket.start();

                wait();
                clientSocket.closeSession();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


        Intent intent=new Intent(this,BusinessMyRestaurantActivity.class);
        intent.putExtra("BusinessUserName",this.userName);
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
