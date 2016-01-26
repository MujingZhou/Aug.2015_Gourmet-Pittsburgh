package com.example.user.gourmetpitt.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.DelMyRateClient;
import com.example.user.gourmetpitt.client.DelMyRateClient;
import com.example.user.gourmetpitt.client.DeleteFavClient;
import com.example.user.gourmetpitt.client.DeleteMyReserveClient;
import com.example.user.gourmetpitt.client.RetrieveRestClient;
import com.example.user.gourmetpitt.service.BackGroundMusicService;

import model.Customer;
import model.Rate;
import model.Reservation;


public class PersonalInfoFragment extends Fragment {

    ListView lv1;
    ListView lv2;
    ListView lv3;
    TextView textUserName;
    Customer customerInfo;

    boolean delete = false;
    boolean visit = false;
    boolean delReserve = false;
    boolean delRate = false;

    Button control;
    BackGroundMusicService myservice;
    boolean isBound = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);


        doBindService();
        control = (Button)view.findViewById(R.id.bgmbutton);
        control.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myservice.response();


                    }
                }

        );
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv1 = (ListView) getActivity().findViewById(R.id.listView1);
        lv2 = (ListView) getActivity().findViewById(R.id.listView2);
        lv3 = (ListView) getActivity().findViewById(R.id.listView3);
        textUserName = (TextView) getActivity().findViewById(R.id.textView);


        Bundle bundle = getArguments();
        customerInfo = (Customer) bundle.getSerializable("customerInfo");

        textUserName.setText("User: " + customerInfo.userName);

        final String[] favs = new String[customerInfo.favorites.size()];
        for (int i = 0; i < customerInfo.favorites.size(); i++)
            favs[i] = customerInfo.favorites.get(i);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, favs);
        lv1.setAdapter(adp);

        final String[] rates = new String[customerInfo.rates.size()];
        for (int i = 0; i < customerInfo.rates.size(); i++)
            rates[i] = customerInfo.rates.get(i).restaurantName + "/" + String.format("%.2f", customerInfo.rates.get(i).rating);

        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, rates);
        lv2.setAdapter(adp2);

        final String[] reserves = new String[customerInfo.reservations.size()];
        for (int i = 0; i < customerInfo.reservations.size(); i++)
            reserves[i] = customerInfo.reservations.get(i).restName + " " + customerInfo.reservations.get(i).reserveTime;
        ArrayAdapter<String> adp3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, reserves);
        lv3.setAdapter(adp3);

        lv1.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (visit == true && delete == false) {
                            visit = false;
                            delete = false;
                            String fav = favs[position];
                            getRestaurant(fav);
                        } else if (visit == false && delete == true) {
                            visit = false;
                            delete = false;
                            String fav = favs[position];
                            deleteFav(fav);
                            Toast.makeText(getActivity(), "Choose Personal Info to refresh", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Reset, choose a mode and choose a restaurant", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        lv2.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(delRate == false){
                            Toast.makeText(getActivity(), "choose DELETE MODE\nthen choose the rate to delete", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String rateStr = rates[position];
                            deleteRate(rateStr);
                            delRate = false;
                        }
                    }
                }
        );

        Button buttonDelete = (Button) getActivity().findViewById(R.id.buttonDelete);
        Button buttonVisit = (Button) getActivity().findViewById(R.id.buttonVisit);
        Button buttonReset = (Button) getActivity().findViewById(R.id.buttonReset);
        buttonDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete = true;
                        visit = false;
                    }
                }
        );

        buttonVisit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        visit = true;
                        delete = false;
                    }
                }
        );

        buttonReset.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete = false;
                        visit = false;
                    }
                }
        );

        Button buttonDelReserve = (Button)getActivity().findViewById(R.id.deleteReserve);

        buttonDelReserve.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delReserve = true;
                        Toast.makeText(getActivity(), "Now you can choose a reservation to delete", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        lv3.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(delReserve == false){
                            Toast.makeText(getActivity(), "enter delete mode, then choose a reservation to delete", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String reserveInfo = reserves[position];
                            String[] reserveInfoArr = reserveInfo.split("\\s");
                            Reservation reserve = new Reservation(reserveInfoArr[0], reserveInfoArr[1] + " " + reserveInfoArr[2]);
                            delReserve(position, reserve);
                        }
                    }
                }
        );

        Button buttonDelRate = (Button)getActivity().findViewById(R.id.buttonDeleteRate);

        buttonDelRate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delRate = true;
                    }
                }
        );
    }

    public void getRestaurant(String restName){
        Intent i = new Intent(getActivity(), CusRestDetailActivity.class);

        try {
            synchronized (this) {
                RetrieveRestClient socketClient = new RetrieveRestClient(this, restName);
                socketClient.start();
                wait();
                //Thread.sleep(500);

                if (socketClient.restDetail == null) System.out.println("nothing!");
                else    System.out.println("Got restaurant!");
                i.putExtra("restDetail", socketClient.restDetail);
                i.putExtra("customer", customerInfo);
                i.putExtra("ratings", socketClient.ratings);
                startActivity(i);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteFav(String restName){

        try {
            synchronized (this) {
                System.out.println("Trying to delete " + restName + " for " + customerInfo.userName + " ...");
                DeleteFavClient socketClient = new DeleteFavClient(this, customerInfo.userName, restName);
                socketClient.start();
                wait();
                //Thread.sleep(500);

                /*if (socketClient.restDetail == null) System.out.println("nothing!");
                else    System.out.println("Got restaurant!");
                i.putExtra("restDetail", socketClient.restDetail);
                i.putExtra("customer", customerInfo);
                startActivity(i);*/
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void delReserve(int position, Reservation reserve){
        try {
            synchronized (this) {
                DeleteMyReserveClient socketClient = new DeleteMyReserveClient(this, customerInfo, reserve);
                socketClient.start();
                wait();
                //Thread.sleep(500);

                /*if (socketClient.restDetail == null) System.out.println("nothing!");
                else    System.out.println("Got restaurant!");
                i.putExtra("restDetail", socketClient.restDetail);
                i.putExtra("customer", customerInfo);
                startActivity(i);*/
                Toast.makeText(getActivity(), "Reservation deleted\nClick PersonInfo to refresh page", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteRate(String rateStr){
        try{
            synchronized (this){
                String[] rateArr = rateStr.split("/");
                Rate rate = new Rate(customerInfo.userName, rateArr[0], Float.parseFloat(rateArr[1]), "");
                DelMyRateClient socketClient = new DelMyRateClient(this, rate);
                socketClient.start();
                this.wait();

                Toast.makeText(getActivity(), "Rating deleted\nClick PersonalInfo to refresh page", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    //server
    private ServiceConnection myconnection = new ServiceConnection(){
        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            BackGroundMusicService.MyMusicBind mybinder = (BackGroundMusicService.MyMusicBind)binder;

            myservice = mybinder.getService();

        }

        public void onServiceDisconnected(ComponentName name) {
            myservice = null;
        }

    };

    void doBindService(){
        this.getActivity().bindService(new Intent(this.getActivity(), BackGroundMusicService.class),
                myconnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    void doUnbindService()
    {
        if(isBound)
        {
            this.getActivity().unbindService(myconnection);
            isBound = false;
        }
    }

    @Override
    public void onDestroy() {

        doUnbindService();
        super.onDestroy();
    }
}
