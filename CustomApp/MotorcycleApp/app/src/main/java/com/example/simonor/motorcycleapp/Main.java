package com.example.simonor.motorcycleapp;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.simonor.motorcycleapp.R;

import org.w3c.dom.Text;

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
 * Use the {@link Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner s;
    String BikeID;

    TextView avgmpg;
    TextView lmpg;
    TextView fu;
    TextView bmpg;
    TextView kmt;
    TextView cpk;
    TextView cpk91;
    TextView cpk95;
    TextView cpk98;
    TextView ds;

    public Main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main.
     */
    // TODO: Rename and change types and number of parameters
    public static Main newInstance(String param1, String param2) {
        Main fragment = new Main();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        s = (Spinner) v.findViewById(R.id.spinner);
        avgmpg = (TextView) v.findViewById(R.id.avgmilage);
        lmpg = (TextView) v.findViewById(R.id.lastmileage);
        fu = (TextView)  v.findViewById(R.id.fuelups);
        bmpg = (TextView) v.findViewById(R.id.bestmileage);
        kmt = (TextView) v.findViewById(R.id.kmtracked);
        cpk = (TextView) v.findViewById(R.id.costkm);
        cpk91 = (TextView) v.findViewById(R.id.costkm91);
        cpk95 = (TextView) v.findViewById(R.id.costkm95);
        cpk98 = (TextView) v.findViewById(R.id.costkm98);
        ds = (TextView) v.findViewById(R.id.spent);
        Initilize();

        return v;
    }

    public void Initilize()
    {
        //Will need to add adapter for the bike
        ArrayList<String> values = getBikeNames();
        if(values.size() != 0)
            BikeID = values.get(0);

        convertData(readData());

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
                convertData(readData());
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

    public void convertData(ArrayList<String> s)
    {
        //result.add("L/100km | Cost/km | Octane | Litres | Distance ");
        //litres per 100kms list
        ArrayList<Double> lpks = new ArrayList<>();
        //cost per km list
        ArrayList<String> cpks = new ArrayList<>();
        String o = "";
        String c = "";
        String l = "";
        String oct = "";
        double km = 0;
        double costTotal = 0;
        //String s = BikeID + "," + f.Odometer + "," + f.CostPerLitre + "," + f.Litres + "," + f.Octane + "\n";
        for(String a : s)
        {
            String[] entry = a.split(",");
            String st = "";

            //cost
            double cost = round(Double.parseDouble(entry[2]) * Double.parseDouble(entry[3]), 2);
            costTotal += cost;

            if(!Objects.equals(o, "") && !Objects.equals(c, "") && !Objects.equals(l, "") && !Objects.equals(oct, ""))
            {
                //calculating distance
                double d = round(Double.parseDouble(entry[1]) - Double.parseDouble(o), 1);
                //calculating litres per 100 kilometer
                double lpk = round(Double.parseDouble(entry[3]) / d * 100, 1);
                //calculating cost

                //calculating cost per km
                double cpd = round(cost / d, 3);
                lpks.add(lpk);
                cpks.add(Double.toString(cpd) + "," + oct);
                km += d;
                o = entry[1];
                c = entry[2];
                l = entry[3];
                oct = entry[4];
            }
            else
            {
                o = entry[1];
                c = entry[2];
                l = entry[3];
                oct = entry[4];
            }
        }
        //avg mileage
        double sum = 0;
        Integer i = 0;
        double llpk = 0;
        double blpk = 100;
        double sum91 = 0;
        double sum95 = 0;
        double sum98 = 0;
        Integer count91 = 0;
        Integer count95 = 0;
        Integer count98 = 0;
        for(double lpk: lpks)
        {
            sum += lpk;
            //last mileage value
            if(Objects.equals(lpk, lpks.get(lpks.size() - 1)))
            {
                llpk = lpk;
            }

            //best mileage
            if(blpk > lpk)
            {
                blpk = lpk;
            }

            //getting cost per Kilometre for different fuel ratings
            String[] entry = cpks.get(i).split(",");
            if(Objects.equals(entry[1], "91"))
            {
                sum91 += Double.parseDouble(entry[0]);
                sum91 = round(sum91, 3);
                count91++;
            }
            else if(Objects.equals(entry[1], "95"))
            {
                sum95 += Double.parseDouble(entry[0]);
                sum95 = round(sum95, 3);
                count95++;
            }
            else if(Objects.equals(entry[1], "98"))
            {
                sum98 += Double.parseDouble(entry[0]);
                sum98 = round(sum98, 3);
                count98++;
            }
            //counting total
            i++;
        }

        //Values to be put into textviews
        if(lpks.size() != 0)
        {
            double avgmlg = round((sum / lpks.size()), 1);
            avgmpg.setText(Double.toString(avgmlg));
        }
        double lastmlg = llpk;
        lmpg.setText(Double.toString(lastmlg));
        Integer fuelups = i;
        fu.setText(i.toString());
        double best = blpk;
        bmpg.setText(Double.toString(best));
        double kms = km;
        kmt.setText(Double.toString(kms));
        if(sum91 != 0)
        {
            double avgCostpKm91 = round((sum91 / count91), 3);
            cpk91.setText(Double.toString(avgCostpKm91));
        }
        if(sum95 != 0)
        {
            double avgCostpKm95 = round((sum95 / count95), 3);
            cpk95.setText(Double.toString(avgCostpKm95));
        }
        if(sum98 != 0)
        {
            double avgCostpKm98 = round((sum98 / count98), 3);
            cpk98.setText(Double.toString(avgCostpKm98));
        }

        double ct = costTotal;
        ds.setText(Double.toString(ct));
        if(count91 != 0 || count95 != 0 || count98 != 0)
        {
            double cpkk = round(((sum91 + sum95 + sum98) / (count91 + count95 + count98)), 3);
            cpk.setText(Double.toString(cpkk));
        }
        //mileage.add("L/100km | Cost/km | Octane | Litres | Distance ");
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
