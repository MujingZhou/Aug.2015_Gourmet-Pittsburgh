package com.example.user.gourmetpitt.ui;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.example.user.gourmetpitt.R;
import com.example.user.gourmetpitt.client.GetRecommendClient;
import com.example.user.gourmetpitt.client.RequestCusInfoClient;
import com.example.user.gourmetpitt.service.BackGroundMusicService;

import java.util.ArrayList;

import model.Customer;


public class CustomHomeActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private static Customer customerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_home);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null)  return;
        customerInfo = (Customer)bundle.getSerializable("customerInfo");

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment myFragment = null;

        switch(position){
            case 0:
                myFragment = new CustomerMainPageFragment();
                break;
            case 1:
                myFragment = new PersonalInfoFragment();

                try {
                    synchronized (this) {
                        RequestCusInfoClient clientSocket = new RequestCusInfoClient(this);
                        ArrayList<String> nameAndType = new ArrayList<String>();
                        String userName = customerInfo.userName;
                        nameAndType.add(userName);
                        nameAndType.add("Customer");
                        clientSocket.setNameAndType(nameAndType);
                        clientSocket.start();

                        wait();
                        customerInfo = clientSocket.customerInfo;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("customerInfo", customerInfo);
                        myFragment.setArguments(bundle);
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    synchronized(this) {
                        myFragment = new RecommendFragment();
                        GetRecommendClient clientSocket = new GetRecommendClient(this, customerInfo);
                        clientSocket.start();
                        this.wait();

                        ArrayList<String> recommends = clientSocket.recommends;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("recommends", recommends);
                        bundle.putSerializable("customer", customerInfo);
                        myFragment.setArguments(bundle);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                break;
            case 3:
                myFragment = new SearchFragment();
                try {
                    /*synchronized (this) {
                        RequestCusInfoClient clientSocket = new RequestCusInfoClient(this);
                        ArrayList<String> nameAndType = new ArrayList<String>();
                        String userName = customerInfo.userName;
                        nameAndType.add(userName);
                        nameAndType.add("Customer");
                        clientSocket.setNameAndType(nameAndType);
                        clientSocket.start();

                        wait();
                        customerInfo = clientSocket.customerInfo;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("customerInfo", customerInfo);
                        myFragment.setArguments(bundle);
                    }*/
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("customerInfo", customerInfo);
                    myFragment.setArguments(bundle);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }

                break;
            case 4:
                myFragment = new ContactFragment();
                break;
            case 5:
                myFragment = new NearbyFragment();
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, myFragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.custom_home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_custom_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((CustomHomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }



}
