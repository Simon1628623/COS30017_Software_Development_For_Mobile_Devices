package com.example.simonor.motorcycleapp;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //Fragments
    Main m;
    Table t;
    Weather w;
    GoogleMapFragment mf;

    //space for the fragments
    ViewPager vp;

    //location
    public Location mLastLocation;
    public static LatLng GPSlocation;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MainActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private final int PERMISSION_ACCESS_COARSE_LOCATION = 7;
    public int fragNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        Initilize();
    }

    void Initilize()
    {
        //if permissions not already in action get permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        TabLayout tl = (TabLayout) findViewById(R.id.tabLayout);

        tl.addTab(tl.newTab().setText("Home"));
        tl.addTab(tl.newTab().setText("Table"));
        tl.addTab(tl.newTab().setText("Maps"));
        tl.addTab(tl.newTab().setText("Weather"));

        // declared as final to indicate these do not change (needed for use in inner class)
        final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), 4);

        vp = (ViewPager) findViewById(R.id.viewPager);

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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int _numberOfTabs;

        MyPagerAdapter(FragmentManager fm, int numberOfTabs)
        {
            super(fm);
            _numberOfTabs = numberOfTabs;
        }

        // dispatch appropriate fragment
        public Fragment getItem(int position)
        {
            fragNo = position;
            switch(position)
            {
                case 0:
                    if (m == null)
                        m = new Main();
                    return m;
                case 1:
                    if (t == null)
                        t = new Table();
                    return t;
                case 2:
                    if (mf == null)
                        mf = new GoogleMapFragment();
                    return mf;
                case 3:
                    if (w == null)
                        w = new Weather();
                    return w;
                default:
                    return null;
            }
        }

        public int getCount()
        {
            return _numberOfTabs;
        }
    }

    //tabs on the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //selection for context menu on the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addBike:
                Intent intent = new Intent(this, AddMotorcycle.class);
                startActivity(intent);
                return true;
            case R.id.action_addFuelUp:
                Intent intent2 = new Intent(this, AddGuzzolineUp.class);
                startActivity(intent2);
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Adding settings later!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.wipe_data:
                deleteFile("bikes.txt");
                deleteFile("fuelup.txt");
                Toast.makeText(this, "Files deleted!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.wipe_gps:
                deleteFile("gps.txt");
                Toast.makeText(this, "Files deleted!", Toast.LENGTH_SHORT).show();
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    //used for getting location
    public void createInstanceGoogleAPI()
    {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this, this, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                handleNewLocation(mLastLocation);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location)
    {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        GPSlocation = new LatLng(currentLatitude, currentLongitude);
        Log.d(TAG, location.toString());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        createInstanceGoogleAPI();
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
        else
            Log.e(TAG, "API CLIENT NULL");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
}
