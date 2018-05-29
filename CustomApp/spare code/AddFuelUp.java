package layout;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.simonor.motorcycleapp.MainActivity;
import com.example.simonor.motorcycleapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_APPEND;
import static java.lang.System.in;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFuelUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFuelUp extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    public AddFuelUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFuelUp.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFuelUp newInstance(String param1, String param2) {
        AddFuelUp fragment = new AddFuelUp();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_fuel_up, container, false);

        bs = (Spinner) v.findViewById(R.id.bikespinner);
        o = (EditText) v.findViewById(R.id.odo);
        c = (EditText) v.findViewById(R.id.cpl);
        l = (EditText) v.findViewById(R.id.l);
        fs = (Spinner) v.findViewById(R.id.fuelspinner);
        g = (CheckBox) v.findViewById(R.id.checkBox);
        a = (Button) v.findViewById(R.id.addfu);

        InitializeUI();

        return v;
    }

    public void InitializeUI()
    {

        //Will need to add adapter for the bike
        ArrayList<String> values = getBikeNames();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values);

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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.fuel_octane, android.R.layout.simple_spinner_item);

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



        a.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                submit();
            }
        });
    }

    public void submit()
    {
        if(o.getText().toString().equalsIgnoreCase("") && c.getText().toString().equalsIgnoreCase("") && l.getText().toString().equalsIgnoreCase("") && Octane != null && BikeID == "")
        {
            Toast.makeText(getContext(), "Please enter all fields", Toast.LENGTH_LONG).show();
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
        if(success)
        {
            //if gps checked
            if(g.isChecked())
            {
                ActivateGPS();
            }

            Toast.makeText(getContext(), BikeID + " " + f.Odometer + " " + f.CostPerLitre + " " + f.Litres + " " + f.Octane, Toast.LENGTH_LONG).show();
            //do something
            String s = BikeID + "," + f.Odometer + "," + f.CostPerLitre + "," + f.Litres + "," + f.Octane + "\n";
            writeToFile(s);

            //wipe data
            o.setText(null);
            c.setText(null);
            l.setText(null);
        }
        else
        {
            Toast.makeText(getContext(), "Please enter correct fields", Toast.LENGTH_LONG).show();
        }
    }

    public void writeToFile(String s)
    {
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
            out = getActivity().openFileOutput(f.getName(), MODE_APPEND);
            out.write(b);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ActivateGPS()
    {
        getLocation();
        saveLocation(Double.toString(latitude)+","+Double.toString(longitude)+"\n");
    }

    public ArrayList<Bike> readBikes()
    {
        ArrayList<Bike> result = new ArrayList<>();
        try {
            FileInputStream fis = getActivity().openFileInput("bikes.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String l;
            while((l = br.readLine()) != null)
            {
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

    public Bike stringToBike(String s)
    {
        String[] entry = s.split(",");

        Integer y = Integer.parseInt(entry[1]);
        Double o = Double.parseDouble(entry[3]);

        Bike b = new Bike(entry[0], y, entry[2], o);

        return b;
    }

    public ArrayList<String> getBikeNames()
    {
        ArrayList<Bike> bs = readBikes();
        ArrayList<String> s = new ArrayList<String>();
        for(Bike b : bs)
        {
            String st = b.Make + " " + b.Model + " " + b.Year.toString();
            s.add(st);
        }
        return s;
    }

    public void getLocation()
    {
        if(MainActivity.mLastLocation != null)
        {
            latitude = MainActivity.mLastLocation.getLatitude();
            longitude = MainActivity.mLastLocation.getLongitude();
            Log.e("got loc", Double.toString(latitude));
        }
        else
        Toast.makeText(getContext(), "GPS feature unavailable...", Toast.LENGTH_LONG).show();

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
            out = getActivity().openFileOutput(f.getName(), MODE_APPEND);
            out.write(b);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
