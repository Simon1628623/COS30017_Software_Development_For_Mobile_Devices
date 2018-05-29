package com.example.simonor.motorcycleapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.example.simonor.motorcycleapp.MainActivity;
import com.example.simonor.motorcycleapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class GoogleMapFragment extends Fragment implements OnMapReadyCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap mMap;

    public GoogleMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoogleMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoogleMapFragment newInstance(String param1, String param2) {
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_google_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (LatLng l : getLocations()) {
            mMap.addMarker(new MarkerOptions().position(l).title("Petrol Station"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(l));
        }
        if(MainActivity.GPSlocation != null)
        {
            mMap.addMarker(new MarkerOptions().position(MainActivity.GPSlocation).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.GPSlocation, 16));
        }
    }

    public ArrayList<LatLng> getLocations() {
        ArrayList<LatLng> loc = new ArrayList<>();
        try {
            FileInputStream fis = getContext().openFileInput("gps.txt");
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

    /*
    private class SetupGPSMaps extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            createInstanceGoogleAPI();
            Log.d(TAG, "loading google api");
            return null;
        }

        protected void onPostExecute() {
            Toast.makeText(getApplicationContext(), getString(R.string.gps), Toast.LENGTH_LONG).show();
            mGoogleApiClient.connect();
            Log.d(TAG, "completed loading google api");
        }

    }*/
}
