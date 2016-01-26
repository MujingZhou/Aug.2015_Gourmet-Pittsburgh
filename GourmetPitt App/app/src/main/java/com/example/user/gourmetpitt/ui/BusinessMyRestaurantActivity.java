package com.example.user.gourmetpitt.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.DefaultSocketClient_DeleteRestaurant;
import com.example.user.gourmetpitt.client.DefaultSocketClient_RetrieveInfo;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import model.SingleRestaurant;
import model.StaticRestaurants;
import model.listViewAdapter;

public class BusinessMyRestaurantActivity extends BusinessSwipeDeleteActivity {

    public static final String KEY_SONG = "song"; // parent node
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_THUMB_URL = "thumb_url";
    private String userName;
    private Context context;
    public int swipePos=0;
    ListView list;
    listViewAdapter adapter;


    @Override
    public ListView getListView() {
        return list;
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
                View current = list.getChildAt(position);
                current.setAlpha(1f);
                RelativeLayout r1 = (RelativeLayout) findViewById(R.id.relative1);
                ImageView iv1 = (ImageView) findViewById(R.id.image_delete);
                ImageView iv2 = (ImageView) findViewById(R.id.image_edit);
                r1.removeView(iv1);
                r1.removeView(iv2);
                iv2.setVisibility(View.INVISIBLE);
                iv1.setVisibility(View.INVISIBLE);
                r1.addView(iv1);
                r1.addView(iv2);
                swipePos=0;
            }
            else if (swipePos == 0||swipePos==-1) {
                View current = list.getChildAt(position);

//                current.setVisibility(View.VISIBLE);
//                current.setVisibility(View.GONE);

                RelativeLayout r1 = (RelativeLayout) findViewById(R.id.relative1);
                ListView l1 = (ListView) findViewById(R.id.myRestaurantList);
                current.setAlpha(0.5f);
//                l1.setBackgroundResource(R.color.dim_foreground_material_light);
                ImageView iv1 = (ImageView) findViewById(R.id.image_delete);
                ImageView iv2 = (ImageView) findViewById(R.id.image_edit);


                r1.removeView(iv1);
                iv1.setImageResource(R.drawable.myrest_delete);



                iv2.setVisibility(View.INVISIBLE);
                iv1.setVisibility(View.VISIBLE);
                iv1.setX(current.getX() + 500);
                iv1.setY(current.getY() + l1.getY());

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
                                DefaultSocketClient_DeleteRestaurant clientSocket = new DefaultSocketClient_DeleteRestaurant(context);
//                                TextView restNameTextView=(TextView)findViewById(R.id.title);
//                                String restName= (String) restNameTextView.getText();
                                StaticRestaurants staticRestaurants=new StaticRestaurants();
                                ArrayList<SingleRestaurant> allRestaurants=staticRestaurants.getStaticRestaurants();
                                String restName=allRestaurants.get(position).restaurantName;
                                System.out.println("delete "+restName);
                                clientSocket.setDeleteRestName(restName,userName);

                                clientSocket.start();

                                context.wait();
                                clientSocket.closeSession();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }


                        Intent intent=new Intent(context,BusinessMyRestaurantActivity.class);
                        intent.putExtra("BusinessUserName",userName);
                        startActivity(intent);
                    }
                });
                swipePos=-1;
            }

        }

        else {
//            ++swipePos;

            if (swipePos==-1) {
                View current = list.getChildAt(position);
                current.setAlpha(1f);
                RelativeLayout r1 = (RelativeLayout) findViewById(R.id.relative1);
                ImageView iv1 = (ImageView) findViewById(R.id.image_delete);
                ImageView iv2 = (ImageView) findViewById(R.id.image_edit);
                r1.removeView(iv1);
                r1.removeView(iv2);
                iv2.setVisibility(View.INVISIBLE);
                iv1.setVisibility(View.INVISIBLE);
                r1.addView(iv1);
                r1.addView(iv2);
                swipePos=0;
            }

            else if (swipePos==0||swipePos==1) {

                View current = list.getChildAt(position);
//                current.setVisibility(View.VISIBLE);
//                current.setVisibility(View.GONE);
                current.setAlpha(0.5f);
                ListView l1 = (ListView) findViewById(R.id.myRestaurantList);
//                l1.setBackgroundResource(R.color.dim_foreground_material_dark);
                RelativeLayout r1 = (RelativeLayout) findViewById(R.id.relative1);

//            ListView l1=(ListView)findViewById(R.id.myRestaurantList);
                ImageView iv1 = (ImageView) findViewById(R.id.image_delete);
                ImageView iv2 = (ImageView) findViewById(R.id.image_edit);
                iv2.setImageResource(R.drawable.myrest_edit);
                r1.removeView(iv2);
//            r1.removeView(iv1);
                iv1.setVisibility(View.INVISIBLE);
                iv2.setVisibility(View.VISIBLE);

                iv2.setX(current.getX() + 500);
                iv2.setY(current.getY() + l1.getY());
                r1.addView(iv2);

                iv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("choose to edit");
                        Intent intent=new Intent(context,BusinessRestEditActivity.class);
                        intent.putExtra("BusinessUserName",userName);
                        intent.putExtra("position",position);
                        System.out.println("edit rest"+position);
                        startActivity(intent);
                    }
                });
                swipePos=1;
            }
        }
    }

    @Override
    public void onItemClickListener(ListAdapter adapter, int position) {
//        Toast.makeText(this, "Single tap on item position " + position,
//                Toast.LENGTH_SHORT).show();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_my_restaurant);
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
        final ArrayList<SingleRestaurant> allRestaurants=staticRestaurants.getStaticRestaurants();

        ArrayList<String> allRestName=staticRestaurants.getRestaurantName();
        setContentView(R.layout.activity_business_my_restaurant);
        Intent intent=getIntent();
        String userName=intent.getStringExtra("BusinessUserName");
        String url=intent.getStringExtra("url");
        this.userName=userName;
        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

        ArrayList<String> image=new ArrayList<>();

//        image.add(String.valueOf(R.drawable.burger_king_icon));
//        image.add(String.valueOf(R.drawable.mc_icon));
//        image.add(String.valueOf(R.drawable.subway_icon));
//        for (int i=0;i<allRestaurants.size();i++) {
//            if (allRestaurants.get(i).restaurantName.equals("BurgerKing")){
//                image.add(String.valueOf(R.drawable.burger_king_icon));
//            }
//            else if (allRestaurants.get(i).restaurantName.equals("McDonald")){
//                image.add(String.valueOf(R.drawable.mc_icon));
//            }
//            else if (allRestaurants.get(i).restaurantName.equals("Subway")){
//                image.add(String.valueOf(R.drawable.subway_icon));
//            }
//            else
//            image.add(String.valueOf(R.color.material_blue_grey_800));
//
//        }
        for (int i=0;i<allRestaurants.size();i++){
            HashMap<String,String> map1=new HashMap<String,String>();
            map1.put(KEY_TITLE,allRestaurants.get(i).restaurantName);
            map1.put(KEY_ARTIST,allRestaurants.get(i).location);
            map1.put(KEY_DURATION,allRestaurants.get(i).openHour);
//            map1.put(KEY_THUMB_URL,image.get(i));
            map1.put(KEY_THUMB_URL,allRestaurants.get(i).url);
            songsList.add(map1);
        }

        list=(ListView)findViewById(R.id.myRestaurantList);
        adapter=new listViewAdapter(this, songsList,getApplicationContext());
        list.setAdapter(adapter);

//        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//
//                int i= (int)list.getItemAtPosition(position);
//
//                System.out.println("You have choose "+i);
//
//                Intent intent=new Intent(getApplicationContext(),BusinessRestDetailActivity.class);
//                intent.putExtra("rest_name",allRestaurants.get(i).restaurantName);
//               // startActivity(intent);
//
//            }
//
//        });


        String [] arrayName=new String[allRestName.size()];
        for (int i=0;i<arrayName.length;i++){
            arrayName[i]=allRestName.get(i);
        }

    }

    private Drawable LoadImageFromWebOperations(String url)
    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "Jay2");
            return d;

        }catch (Exception e) {
            Log.d("Excep", "Exc=" + e);
            return null;
        }
    }

    public void clickCreateRestaurant(View view){
        Intent intent=new Intent(this,BusinessCreateRestaurant.class);
        intent.putExtra("BusinessUserName",userName);
        startActivity(intent);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_business_my_restaurant, menu);
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
