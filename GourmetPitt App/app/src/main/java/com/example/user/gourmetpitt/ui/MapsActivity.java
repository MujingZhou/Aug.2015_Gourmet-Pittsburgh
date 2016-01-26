package com.example.user.gourmetpitt.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.user.gourmetpitt.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import model.SingleRestaurant;

public class MapsActivity extends FragmentActivity {


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    ArrayList<SingleRestaurant> restlist;
    ArrayList<LatLng> localist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // setUpMapIfNeeded();
        Intent intent = getIntent();
        restlist = (ArrayList<SingleRestaurant>)intent.getSerializableExtra("LIST");
        localist = (ArrayList<LatLng>)intent.getSerializableExtra("LOCA");

        LocationManager mlmanager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mllistener = new MyLocationListener();
        mlmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mllistener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Mid"));
    }


    //used to adjust map location and show it. update screen to show current location and add a marker
    public void changeMap(Location loc){
        //setUpMapIfNeeded();
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();


        setUpMap();

        mMap.clear();

        mMap.setMyLocationEnabled(true);

        LatLng currentLoc = new LatLng(loc.getLatitude(),loc.getLongitude());

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 16));

        mMap.addMarker(new MarkerOptions().position(currentLoc).title("myself"));


        for(int i=0;i<localist.size();i++){


            mMap.addMarker(new MarkerOptions().position(localist.get(i)).title(restlist.get(i).restaurantName));

        }

    }


    public class MyLocationListener implements LocationListener {

        @Override

        public void onLocationChanged(Location loc)

        {
            String Text = "My Location : "+String.valueOf(loc.getLatitude() )+" , "+String.valueOf(loc.getLongitude());

            changeMap(loc);

            Toast.makeText(getApplicationContext(),

                    Text,

                    Toast.LENGTH_SHORT).show();

        }


        @Override

        public void onProviderDisabled(String provider)

        {

        }


        @Override

        public void onProviderEnabled(String provider)

        {

        }


        @Override

        public void onStatusChanged(String provider, int status, Bundle extras)

        {

        }

    }
}
