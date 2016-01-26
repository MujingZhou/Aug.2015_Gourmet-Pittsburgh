package com.example.user.gourmetpitt.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.user.gourmetpitt.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.SingleRestaurant;

public class NearbyFragment extends Fragment {


    private Button showmap;
    private Location mylocation = null;
    private Geocoder geo = null;

    //TextView mylocationshow;

    EditText editNearBy;
    ListView NearByList;
    ArrayList<String> nearbyitems;
    ArrayList<SingleRestaurant> reslist = new ArrayList<SingleRestaurant>();
    ArrayList<SingleRestaurant> restaurantstore = new ArrayList<SingleRestaurant>();
    ArrayList<LatLng> reslocationlist = new ArrayList<LatLng>();

    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);

        showmap = (Button)view.findViewById(R.id.mapbutton);
        editNearBy =(EditText) view.findViewById(R.id.editNearBy);
        NearByList = (ListView) view.findViewById(R.id.NearByList);

        //mylocationshow = (TextView)this.getActivity().findViewById(R.id.mylocationshow);

        nearbyitems = new ArrayList<String>();

        geo = new Geocoder(this.getActivity(), Locale.US);


//        //hard code a singlerestaurant list
//        SingleRestaurant oe = new SingleRestaurant(1,"OE","4609 Forbes Ave, Pittsburgh, PA 15213",12345,"always","west",120,100,null);
//        SingleRestaurant littleasia = new SingleRestaurant(2,"Little Asia","301 S Craig Street, Pittsburgh, PA 15213",12345,"always","west",120,100,null);
//        SingleRestaurant everydaynoodle = new SingleRestaurant(3,"Every Day Noodle","5875 Forbes Ave, Pittsburgh, PA 15217",12345,"always","west",120,100,null);
//        SingleRestaurant primantibros = new SingleRestaurant(4,"PrimantiBros","2 S Market Square, Pittsburgh, PA 15222",12345,"always","west",120,100,null);
//        SingleRestaurant joseph = new SingleRestaurant(5,"Joseph","1210 Mellon Street, Pittsburgh, PA 15206",12345,"always","east",120,100,null);
//
//        reslist.add(oe);
//        reslist.add(littleasia);
//        reslist.add(everydaynoodle);
//        reslist.add(primantibros);
//        reslist.add(joseph);


        LatLng mylocation1 = new LatLng(40.44,-79.94);//hard code of my own location

        LatLng thislocation = getOwnLocation();

        //used for debug
        Log.d(String.valueOf(thislocation.latitude), "latitude of my location");
        Log.d(String.valueOf(thislocation.longitude),"longitude of my location");



        restaurantstore = findNearBy(thislocation, reslist);


        for(SingleRestaurant iter:restaurantstore){
            nearbyitems.add(iter.restaurantName);
            LatLng tem = getLocationByName(iter);
            reslocationlist.add(tem);

        }

        nearbyitems.add("BurgerKing");


        NearByList.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, nearbyitems));

        editNearBy.addTextChangedListener(
                new TextWatcher() {


                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        ArrayList<String> tem = new ArrayList<String>();

                        int textlength = editNearBy.getText().length();
                        tem.clear();
                        for (int i = 0; i < nearbyitems.size(); i++) {
                            if (textlength <= nearbyitems.get(i).length()) {

                                if (editNearBy.getText().toString().equalsIgnoreCase((String) nearbyitems.get(i).subSequence(0, textlength))) {

                                    tem.add(nearbyitems.get(i));

                                }


                            }


                        }
                        NearByList.setAdapter(new ArrayAdapter<String>(NearbyFragment.this.getActivity(), android.R.layout.simple_list_item_1, tem));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    public void afterTextChanged(Editable s) {

                    }

                }

        );




        showmap.setOnClickListener(

                new View.OnClickListener() {
                    public void onClick(View view) {

                        showthemap(view);

                    }

                }

        );

        return view;
    }

    public void showthemap(View view){

        Intent intent = new Intent(this.getActivity(), MapsActivity.class );

        Bundle bundle = new Bundle();
        bundle.putSerializable("LIST",restaurantstore);
        intent.putExtras(bundle);

        bundle = new Bundle();
        bundle.putSerializable("LOCA",reslocationlist);
        intent.putExtras(bundle);


        this.startActivity(intent);

    }


    public ArrayList<SingleRestaurant> findNearBy( LatLng ownlocation, ArrayList<SingleRestaurant> totalreslist ){

        ArrayList<SingleRestaurant> resultres = new ArrayList<SingleRestaurant>();

        for(SingleRestaurant temres: totalreslist){

            LatLng tem = getLocationByName(temres);
            int computeresult = (int)(Math.pow(2000*(ownlocation.latitude-tem.latitude),2)+Math.pow(1000*(ownlocation.longitude-tem.longitude),2));


            //used for debug
            Log.d(String.valueOf(computeresult),"some problem");
            Log.d(String.valueOf(ownlocation.latitude),"some other problem");
            Log.d(String.valueOf(tem.latitude),"some other other problem");

            Log.d(String.valueOf(ownlocation.longitude),"mylongitude");
            Log.d(String.valueOf(tem.longitude),"res longitude");

            Log.d(String.valueOf(Math.pow(200*(ownlocation.latitude-tem.latitude),2)),"total");

            Log.d(String.valueOf(Math.pow(100*(ownlocation.longitude-tem.longitude),2)),"total2");


            if(computeresult<=1500){
                resultres.add(temres);
            }
        }

        return resultres;

    }


    public LatLng getLocationByName(SingleRestaurant res){

        List addresses = null;

        try{
            addresses = geo.getFromLocationName(res.location,1);

        }catch(IOException e){

            e.printStackTrace();
        }

        if(addresses.size()==0){

            return new LatLng(0,0);

        }else{

            Address address = (Address)addresses.get(0);

            return new LatLng(address.getLatitude(),address.getLongitude());

        }

    }


    public LatLng getOwnLocation(){

        double latitude = 0.0;
        double longitude = 0.0;

        LocationManager locationManager = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }else{
            LocationListener locationListener = new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }

                //when coordinate change call this method
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        Log.e("Map", "Location changed : Lat: "
                                + location.getLatitude() + " Lng: "
                                + location.getLongitude());
                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                latitude = location.getLatitude(); //latitude
                longitude = location.getLongitude(); //longitude
            }
        }


        return new LatLng(latitude,longitude);
    }



}
