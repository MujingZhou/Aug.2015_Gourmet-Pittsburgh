package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.ActivityRetrieveRestClient;

import java.util.ArrayList;

import model.Customer;
import model.SingleRestaurant;


public class SearchResultActivity extends Activity {

    Customer customer;
    EditText editsearch;
    ListView resultlist;
    //private ArrayList<String> resultitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        editsearch = (EditText)findViewById(R.id.searchkey);
        resultlist = (ListView)findViewById(R.id.listView);

        /*resultitems = new ArrayList<String>();

        resultitems.add("BurgerKing");
        resultitems.add("RoseTeaCoffee");
        resultitems.add("CheeseBurger");
        resultitems.add("KoreanGarden");
        resultitems.add("LittleAsian");
        resultitems.add("Skibo");
        resultitems.add("ItalianFamily");
        resultitems.add("Alibaba");*/

        Bundle bundle = getIntent().getExtras();
        customer = (Customer)bundle.getSerializable("customer");
        final ArrayList<String> resultitems = (ArrayList<String>)bundle.getSerializable("results");
        String[] resultArr = new String[resultitems.size()];
        for(int i = 0; i < resultitems.size(); i++)
            resultArr[i] = resultitems.get(i);

        resultlist.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultArr));

        editsearch.addTextChangedListener(
                new TextWatcher(){


                    public void onTextChanged(CharSequence s,int start, int before, int count){
                        ArrayList<String> tem = new ArrayList<String>();

                        int textlength = editsearch.getText().length();
                        tem.clear();
                        for(int i=0;i<resultitems.size();i++){
                            if(textlength<=resultitems.get(i).length()){

                                if(editsearch.getText().toString().equalsIgnoreCase((String)resultitems.get(i).subSequence(0,textlength))){

                                    tem.add(resultitems.get(i));

                                }



                            }


                        }
                        resultlist.setAdapter(new ArrayAdapter<String>(SearchResultActivity.this, android.R.layout.simple_list_item_1, tem));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after){

                    }

                    public void afterTextChanged(Editable s){

                    }

                }

        );


        //set the result search key
        /*Bundle dbData = getIntent().getExtras();
        if(dbData == null)
            return;
        String dbString = dbData.getString("searchPlace");


        final TextView dbText = (TextView)findViewById(R.id.textViewPlace);
        dbText.setText("Key Word: "+dbString);*/


//setlistitemlistener
        resultlist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String resname = String.valueOf(parent.getItemAtPosition(position));
                        sendInfo(resname);
                    }
                }

        );
    }

    public void sendInfo(String resname){
        try {
            synchronized (this) {
                ActivityRetrieveRestClient socketClient = new ActivityRetrieveRestClient(this, resname);
                socketClient.start();
                this.wait();
                SingleRestaurant rest = socketClient.restDetail;

                Intent intent = new Intent(this, CusRestDetailActivity.class);
                intent.putExtra("restDetail", rest);
                intent.putExtra("customer", customer);
                intent.putExtra("ratings", socketClient.ratings);

                startActivity(intent);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void onBackClick(View view){
        /*Fragment prevFragment = new SearchFragment();
        FragmentManager fragManager = getFragmentManager();
        fragManager.beginTransaction().replace(R.id.container, prevFragment).commit();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_result, menu);
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
