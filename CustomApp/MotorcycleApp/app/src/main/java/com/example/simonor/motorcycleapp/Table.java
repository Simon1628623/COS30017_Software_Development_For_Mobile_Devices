package com.example.simonor.motorcycleapp;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.simonor.motorcycleapp.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

import layout.Bike;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Table#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Table extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner s;
    ListView listView;
    ArrayList<String> mileage;
    String BikeID;

    public Table() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Table.
     */
    // TODO: Rename and change types and number of parameters
    public static Table newInstance(String param1, String param2) {
        Table fragment = new Table();
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
        View v = inflater.inflate(R.layout.fragment_table, container, false);
        s = (Spinner) v.findViewById(R.id.spinner6);
        listView = (ListView) v.findViewById(R.id.list);
        Initilize();

        return v;
    }

    public void Initilize()
    {
        mileage = new ArrayList<String>();

        //Will need to add adapter for the bike
        ArrayList<String> values = getBikeNames();
        if(values.size() != 0)
            BikeID = values.get(0);

        setList();
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values);

        // Specify the layout to use when the list of choices appears
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        s.setAdapter(ad);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                BikeID = parent.getItemAtPosition(position).toString();
                setList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
    }

    //Same as Addfuel up thingos
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
    //end of same

    public void setList()
    {
        mileage = convertData(readData());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, mileage);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getContext(), "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
            }
        });
    }

    public ArrayList<String> readData()
    {
        ArrayList<String> result = new ArrayList<>();
        try {
            FileInputStream fis = getActivity().openFileInput("fuelup.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String l;
            while((l = br.readLine()) != null)
            {
                if(isCorrectBike(l))
                {
                    result.add(l);
                }
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Boolean isCorrectBike(String s)
    {
        Boolean result;
        String[] entry = s.split(",");
        result = Objects.equals(BikeID, entry[0]);
        return result;
    }

    public ArrayList<String> convertData(ArrayList<String> s)
    {
        ArrayList<String> result = new ArrayList<>();
        result.add("L/100km | Cost/km | Octane | Litres | Distance ");
        String o = "";
        String c = "";
        String l = "";
        String oct = "";
        //String s = BikeID + "," + f.Odometer + "," + f.CostPerLitre + "," + f.Litres + "," + f.Octane + "\n";
        for(String a : s)
        {
            String[] entry = a.split(",");
            String st = "";

            if(!Objects.equals(o, "") && !Objects.equals(c, "") && !Objects.equals(l, "") && !Objects.equals(oct, ""))
            {
                Double d = round(Double.parseDouble(entry[1]) - Double.parseDouble(o), 1);
                Double lpk = round(Double.parseDouble(entry[3]) / d * 100, 1);
                Double cost = round(Double.parseDouble(entry[2]) * Double.parseDouble(entry[3]), 2);
                Double cpd = round(cost / d, 3);

                //st = lpk.toString() + "L/100km | $" + cpd.toString() + "/km | " + entry[4] + "o | " + entry[3] + "L | " + d.toString() + "km";
                st = lpk.toString() + "L/100km | $" + cpd.toString() + "/km | " + oct + "o | " + l + "L | " + d.toString() + "km";
                o = entry[1];
                c = entry[2];
                l = entry[3];
                oct = entry[4];
                result.add(st);
            }
            //needed
            else
            {
                o = entry[1];
                c = entry[2];
                l = entry[3];
                oct = entry[4];
            }
        }
        //mileage.add("L/100km | Cost/km | Octane | Litres | Distance ");
        return result;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
