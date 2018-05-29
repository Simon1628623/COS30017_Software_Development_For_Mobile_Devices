package swindroid.suntime.ui;

import swindroid.suntime.MapsActivity;
import swindroid.suntime.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static swindroid.suntime.ui.Suntimes.selected;
import static swindroid.suntime.ui.Suntimes.sunrise;
import static swindroid.suntime.ui.Suntimes.sunset;

public class Main extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback {

    Suntimes frag;
    addLocation al;
    maps m;
    MapFragment mf;
    SunTable st;
    //MapsActivity m;
    GoogleMap mMap;

    private ShareActionProvider mShareActionProvider;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if(frag == null)
            AddFragment();
    }

    void AddFragment()
    {
        frag = new Suntimes();
        frag.setArguments(getIntent().getExtras());
        android.app.FragmentManager fragmang = getFragmentManager();
        android.app.FragmentTransaction transaction = fragmang.beginTransaction();
        transaction.add(R.id.fragment_container, frag);
        transaction.commit();
    }

    void replaceFragment(Fragment f)
    {
        android.app.FragmentManager fragmang = getFragmentManager();
        android.app.FragmentTransaction transaction = fragmang.beginTransaction();
        transaction.replace(R.id.fragment_container, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addLocation:
                if(al == null)
                    al = new swindroid.suntime.ui.addLocation();
                replaceFragment(al);
                return true;
            case R.id.action_addDefaultLocations:
                addDefaultLocations();
                return true;
            case R.id.menu_item_share:
                if(frag.isVisible())
                    sharepls();
                else
                    Toast.makeText(this, "Share on Suntimes tab only", Toast.LENGTH_LONG).show();
                //setShareIntent(share());
                Log.e("", "pressed share");
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem mSpinnerItem1 = menu.findItem( R.id.action_Location);
        View view1 = mSpinnerItem1.getActionView();
        if (view1 instanceof Spinner)
        {
            final Spinner spinner = (Spinner) view1;
            spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            // Spinner Drop down elements
            List<String> categories = new ArrayList<String>();

            categories.add("Sun Times");
            categories.add("Table");
            categories.add("Maps");


            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);

            // Locate MenuItem with ShareActionProvider
            MenuItem item = menu.findItem(R.id.menu_item_share);

            // Fetch and store ShareActionProvider
            //mShareActionProvider = (ShareActionProvider) item.getActionProvider();
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        switch(item)
        {
            case "Sun Times":
                replaceFragment(frag);
                break;
            case "Table":
                if(st == null)
                    st = new SunTable();
                replaceFragment(st);
                break;
            case "Maps":
                if(mf == null)
                    mf = new MapFragment();
                replaceFragment(mf);
                mf.getMapAsync(this);
                break;
            default:

                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        replaceFragment(frag);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(selected.getLatitude(), selected.getLongitude());
        Marker loc = mMap.addMarker(new MarkerOptions().position(location).title(selected.getName()).snippet("sunrise: " + sunrise + "am sunset" + sunset + "pm")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.common_full_open_on_phone)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
            startActivity(Intent.createChooser(shareIntent, "Shearing Option"));


        }
    }

    public void sharepls()
    {
        startActivity(Intent.createChooser(share(), "Shearing Option"));
    }


    public Intent share()
    {
        DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
        TextView sunriseTV = (TextView) findViewById(R.id.sunriseTimeTV);
        TextView sunsetTV = (TextView) findViewById(R.id.sunsetTimeTV);
        TextView location = (TextView) findViewById(R.id.locationTV);

        Calendar cal = Calendar.getInstance();
        String date = Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(cal.get(Calendar.MONTH)) + "/"+ Integer.toString(cal.get(Calendar.YEAR));
        String sr = sunriseTV.getText().toString();
        String ss = sunsetTV.getText().toString();
        String loc = location.getText().toString();

        String line = "In " + loc + " on the " + date + " the sun will rise at " + sr + " and set at " + ss;
        Log.e("", line);
        Intent result = new Intent();
        result.setAction(Intent.ACTION_SEND);
        result.setType("text/plain");
        result.putExtra(Intent.EXTRA_SUBJECT, "Sun Times");
        result.putExtra(Intent.EXTRA_TEXT, line);
        return result;
    }

    void addDefaultLocations()
    {

        try {
            InputStream fs = getAssets().open("au_locations.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            String l;
            while((l = br.readLine()) != null)
            {
                String ln = "\n" + l;

                byte[] b = ln.getBytes();


                FileOutputStream out = null;
                try {
                    out = openFileOutput("Locations.txt", MODE_APPEND);
                    out.write(b);
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            br.close();
            fs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}