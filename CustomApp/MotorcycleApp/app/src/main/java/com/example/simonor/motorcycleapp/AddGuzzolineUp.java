package com.example.simonor.motorcycleapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import layout.Bike;
import layout.FuelUp;

public class AddGuzzolineUp extends AppCompatActivity {

    Spinner bs;
    EditText o;
    EditText c;
    EditText l;
    Spinner fs;
    CheckBox g;
    Button a;

    Integer Octane;
    String BikeID;

    double latitude = 0;
    double longitude = 0;

    LocationManager lm;
    Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guzzoline_up);
        Initilize();
    }

    void Initilize() {
        bs = (Spinner) findViewById(R.id.bikespinner);
        o = (EditText) findViewById(R.id.odo);
        c = (EditText) findViewById(R.id.cpl);
        l = (EditText) findViewById(R.id.l);
        fs = (Spinner) findViewById(R.id.fuelspinner);
        g = (CheckBox) findViewById(R.id.checkBox);
        a = (Button) findViewById(R.id.addfu);
        //Will need to add adapter for the bike
        ArrayList<String> values = getBikeNames();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);

        // Specify the layout to use when the list of choices appears
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        bs.setAdapter(ad);

        bs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                BikeID = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        //Octane spinner
        //
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.fuel_octane, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        fs.setAdapter(adapter);

        fs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                String o = parent.getItemAtPosition(position).toString();
                Octane = Integer.parseInt(o);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });


        a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void submit() {
        if (o.getText().toString().equalsIgnoreCase("") && c.getText().toString().equalsIgnoreCase("") && l.getText().toString().equalsIgnoreCase("") && Octane != null && BikeID == "") {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_LONG).show();
        }
        boolean success = true;
        FuelUp f = new FuelUp();
        try {
            f.BikeID = BikeID;
            f.Odometer = Integer.parseInt(o.getText().toString());
            f.CostPerLitre = Double.parseDouble(c.getText().toString());
            f.Litres = Double.parseDouble(l.getText().toString());
            f.Octane = Octane;
        } catch (NumberFormatException e) {
            success = false;
        }
        if (success) {
            //if gps checked
            if (g.isChecked()) {
                ActivateGPS();
            }

            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            //do something
            String s = BikeID + "," + f.Odometer + "," + f.CostPerLitre + "," + f.Litres + "," + f.Octane + "\n";
            writeToFile(s);

            //return to main activity
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Please enter correct fields", Toast.LENGTH_LONG).show();
        }
    }

    public void writeToFile(String s) {
        //creating file if it does not exist
        File f = new File("fuelup.txt");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] b = s.getBytes();
        FileOutputStream out = null;
        try {
            out = openFileOutput(f.getName(), MODE_APPEND);
            out.write(b);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ActivateGPS() {
        getLocation();
        if (latitude != 0 && longitude != 0)
            saveLocation(Double.toString(latitude) + "," + Double.toString(longitude) + "\n");
        Log.e("gps", Double.toString(latitude));
    }

    public ArrayList<Bike> readBikes() {
        ArrayList<Bike> result = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("bikes.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String l;
            while ((l = br.readLine()) != null) {
                result.add(stringToBike(l));
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Bike stringToBike(String s) {
        String[] entry = s.split(",");

        Integer y = Integer.parseInt(entry[1]);
        Double o = Double.parseDouble(entry[3]);

        Bike b = new Bike(entry[0], y, entry[2], o);

        return b;
    }

    public ArrayList<String> getBikeNames() {
        ArrayList<Bike> bs = readBikes();
        ArrayList<String> s = new ArrayList<String>();
        for (Bike b : bs) {
            String st = b.Make + " " + b.Model + " " + b.Year.toString();
            s.add(st);
        }
        return s;
    }

    public void getLocation() {
        if (MainActivity.GPSlocation != null) {
            latitude = MainActivity.GPSlocation.latitude;
            longitude = MainActivity.GPSlocation.longitude;
            Log.e("got loc", MainActivity.GPSlocation.toString());
        } /*else if (MainActivity.GPSlocation == null) {
            lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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
            loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
        } */else
            Toast.makeText(this, "GPS feature unavailable...", Toast.LENGTH_LONG).show();

    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateLocation();
        }

        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            if(status == LocationProvider.AVAILABLE)
            {
                updateLocation();
            }
        }
    };

    private void updateLocation() {
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
        loc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        longitude = loc.getLongitude();
        latitude = loc.getLatitude();
    }

    public void saveLocation(String s)
    {
        //creating file if it does not exist
        File f = new File("gps.txt");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] b = s.getBytes();
        FileOutputStream out = null;
        try {
            out = openFileOutput(f.getName(), MODE_APPEND);
            out.write(b);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
