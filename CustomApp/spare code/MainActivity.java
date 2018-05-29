package com.example.simonor.motorcycleapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
//import android.app.Fragment;
//import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import layout.AddBike;
import layout.AddFuelUp;
import layout.GoogleMapFragment;
import layout.Main;
import layout.Table;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //Fragments
    AddBike ab;
    AddFuelUp afu;
    Main m;
    Table t;
    Weather w;
    GoogleMapFragment mf;
    //MapFragment mf;
    //google maps
    GoogleMap mMap;
    //location
    public static Location mLastLocation;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        /*
        if (m == null) {
            AddFragment();
        }*/
        Initilize();
        new SetupGPSMaps().execute();
    }

    void Initilize()
    {
        TabLayout tl = (TabLayout) findViewById(R.id.tabLayout);

        tl.addTab(tl.newTab().setText("Home"));
        tl.addTab(tl.newTab().setText("Table"));
        tl.addTab(tl.newTab().setText("Maps"));
        tl.addTab(tl.newTab().setText("Weather"));

        // declared as final to indicate these do not change (needed for use in inner class)
        final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), 4);

        final ViewPager vp = (ViewPager) findViewById(R.id.viewPager);

        vp.setAdapter(adapter);

        tl.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int _numberOfTabs;

        public MyPagerAdapter(FragmentManager fm, int numberOfTabs)
        {
            super(fm);
            _numberOfTabs = numberOfTabs;
        }

        // dispatch appropriate fragment
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    //if (m == null)
                        m = new Main();
                    return m;
                case 1:
                    //if (t == null)
                        t = new Table();
                    return t;
                case 2:
                    //if (mf == null)
                        mf = new GoogleMapFragment();
                    return mf;
                case 3:
                    //if (w == null)
                        w = new Weather();
                    return w;

                /*case 1:
                    SupportMapFragment mapFrag = SupportMapFragment.newInstance();
                    if(_f2 == null)
                        _f2 = new Fragment2();
                    mapFrag.getMapAsync(_f2);
                    return mapFrag;*/

                default:
                    return null;
            }
        }

        public int getCount()
        {
            return _numberOfTabs;
        }

    }

    void AddFragment() {
        m = new Main();
        m.setArguments(getIntent().getExtras());
        FragmentManager fragmang = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmang.beginTransaction();
        transaction.add(R.id.viewPager, m);
        transaction.commit();
    }

    void replaceFragment(Fragment f) {
        FragmentManager fragmang = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmang.beginTransaction();
        transaction.replace(R.id.viewPager, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //tabs on the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem mSpinnerItem1 = menu.findItem(R.id.action_Location);
        View view1 = mSpinnerItem1.getActionView();
        if (view1 instanceof Spinner) {
            final Spinner spinner = (Spinner) view1;
            //spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    // On selecting a spinner item
                    String item = parent.getItemAtPosition(position).toString();

                    switch (item) {
                        case "Main":
                            if (m == null)
                                m = new Main();
                            replaceFragment(m);
                            break;
                        case "Table":
                            if (t == null)
                                t = new Table();
                            replaceFragment(t);
                            break;
                        case "Maps":
                            //replaceFragment(mf);
                            break;
                        case "Weather":
                            if (w == null)
                                w = new Weather();
                            replaceFragment(w);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    // sometimes you need nothing here
                }
            });

            // Spinner Drop down elements
            List<String> categories = new ArrayList<String>();

            //options for the tabs on action bar
            categories.add("Main");
            categories.add("Table");
            categories.add("Maps");
            categories.add("Weather");

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
        }
        return true;
    }

    //selection for context menu on the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addBike:
                if (ab == null)
                    ab = new AddBike();
                replaceFragment(ab);
                return true;
            case R.id.action_addFuelUp:
                if (afu == null)
                    afu = new AddFuelUp();
                replaceFragment(afu);
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Adding settings later!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.wipe_data:
                deleteFile("bikes.txt");
                deleteFile("fuelup.txt");
                deleteFile("gps.txt");
                Toast.makeText(this, "Files deleted!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    //reads text file with saved locations and outputs a list of the Locations
    public ArrayList<LatLng> getLocations() {
        ArrayList<LatLng> loc = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("gps.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String l;
            while ((l = br.readLine()) != null) {
                loc.add(StringToLoc(l));
                Log.e("reading", l);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loc;
    }

    //converts a String to a location value
    LatLng StringToLoc(String s) {
        String[] entry = s.split(",");
        return new LatLng(Double.parseDouble(entry[0]), Double.parseDouble(entry[1]));
    }

    //used for getting location
    public void createInstanceGoogleAPI()
    {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Toast.makeText(this, "Connected: " + mLastLocation.toString(), Toast.LENGTH_SHORT).show();
        /*if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }*/
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    private class SetupGPSMaps extends AsyncTask<Void, Void, Void> implements OnMapReadyCallback
    {

        @Override
        protected Void doInBackground(Void... voids) {
            createInstanceGoogleAPI();
            //if (mf == null)
                //mf = new GoogleMapFragment();
                //mf = new MapFragment();
            return null;
        }

        protected void onPostExecute() {
            Toast.makeText(getApplicationContext(), getString(R.string.gps), Toast.LENGTH_LONG).show();
            mGoogleApiClient.connect();
            //mf.getMapAsync(this);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            for (LatLng l : getLocations()) {
                mMap.addMarker(new MarkerOptions().position(l).title("Petrol Station"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(l));
            }
            if(mLastLocation != null)
            {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                LatLng loc = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(loc).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            }
            // Add a marker in Sydney, Australia, and move the camera.
            //LatLng sydney = new LatLng(-34, 151);
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        }
    }
}
