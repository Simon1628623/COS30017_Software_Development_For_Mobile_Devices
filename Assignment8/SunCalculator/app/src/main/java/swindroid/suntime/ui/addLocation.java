package swindroid.suntime.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import swindroid.suntime.R;

import static android.os.ParcelFileDescriptor.MODE_APPEND;


public class addLocation extends Fragment {

    Button button1;
    Button button2;
    EditText name;
    EditText lat;
    EditText lon;
    EditText tz;

    public addLocation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_location, container, false);
        button1 = (Button) v.findViewById(R.id.submit);
        button2 = (Button) v.findViewById(R.id.getLocation);
        name = (EditText) v.findViewById(R.id.name);
        lat = (EditText) v.findViewById(R.id.latitude);
        lon = (EditText) v.findViewById(R.id.longitude);
        tz = (EditText) v.findViewById(R.id.tz);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submit();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getLocation();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    void submit() {
        String n = name.getText().toString();
        String la = lat.getText().toString();
        String lo = lon.getText().toString();
        String t = tz.getText().toString();

        String line = "\n" + n + "," + la + "," + lo + "," + t;
        byte[] b = line.getBytes();


        FileOutputStream out = null;
        try {
            out = getActivity().openFileOutput("Locations.txt", MODE_APPEND);
            out.write(b);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //send back to main
        Suntimes f = new Suntimes();
        android.app.FragmentManager fragmang = getFragmentManager();
        android.app.FragmentTransaction transaction = fragmang.beginTransaction();
        transaction.replace(R.id.fragment_container, f);
        transaction.addToBackStack(null);
        transaction.commit();


    }

    void getLocation()
    {
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        //get suburb
        Geocoder gc = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address a1 = addresses.get(0);
            String locality = a1.getLocality();
            name.setText(locality);
            tz.setText(TimeZone.getDefault().getDisplayName().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }


        lat.setText(Double.toString(latitude));
        lon.setText(Double.toString(longitude));
    }

}
