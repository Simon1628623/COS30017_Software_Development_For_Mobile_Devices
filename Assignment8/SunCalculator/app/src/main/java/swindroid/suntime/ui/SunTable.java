package swindroid.suntime.ui;

import android.content.Context;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import swindroid.suntime.R;
import swindroid.suntime.calc.AstronomicalCalendar;
import swindroid.suntime.calc.GeoLocation;

import static com.google.android.gms.internal.zzs.TAG;
import static swindroid.suntime.R.id.endDate;
import static swindroid.suntime.ui.Suntimes.SpinPos;
import static swindroid.suntime.ui.Suntimes.c;
import static swindroid.suntime.ui.Suntimes.selected;


public class SunTable extends Fragment {

    Spinner s;
    EditText b;
    EditText e;
    Button button;
    ListView listView;
    ArrayList<String> times;

    public SunTable() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sun_table, container, false);

        s = (Spinner) view.findViewById(R.id.spin);
        b = (EditText) view.findViewById(R.id.beginDate);
        e = (EditText) view.findViewById(endDate);
        button = (Button) view.findViewById(R.id.button);
        listView = (ListView) view.findViewById(R.id.list);
        times = new ArrayList<String>();
        initializeUI();

        // Inflate the layout for this fragment
        return view;
    }

    private void initializeUI()
    {
        times.add("Table");

        setSpinner();
        if(times != null)
            setList();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GenerateTable();
            }
        });




    }

    public void GenerateTable()
    {
        Date start = getDate(b.getText().toString());
        Date end = getDate(e.getText().toString());
        Log.e("b is ", b.getText().toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        times.clear();
        while (!cal.getTime().after(end))
        {
            Date d = cal.getTime();
            System.out.println(d);
            cal.add(Calendar.DAY_OF_MONTH, 1);

            GeoLocation geolocation = new GeoLocation(selected.suburb, selected.latitude, selected.longitude, selected.timeZone);
            AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
            ac.getCalendar().set(cal.get(Calendar.YEAR), Calendar.MONTH, Calendar.DAY_OF_MONTH);
            Date srise = ac.getSunrise();
            Date sset = ac.getSunset();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            String r = format.format(d) + "| Sunrise: " + sdf.format(srise) + "| Sunset: " + sdf.format(sset);
            times.add(r);
        }
        setList();
    }

    public Date getDate(String date)
    {
        Date myDate = null;
        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            myDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e1) {
            e1.printStackTrace();
        }
        return myDate;
    }


    public void setSpinner()
    {


        // Spinner click listener
        s.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {@Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            // On selecting a spinner item
            String item = parent.getItemAtPosition(position).toString();
            selected = c.Cities.get(position);
            SpinPos = position;
            // Showing selected spinner item
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                selected = new City("Melbourne",-37.814,144.96332, TimeZone.getTimeZone("Australia/Melbourne"));
            }}));

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for(int i = 0; c.Length()-1 > i; i++)
        {
            categories.add(c.Cities.get(i).suburb);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        s.setAdapter(dataAdapter);

        s.setSelection(SpinPos);
    }

    public void setList()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, times);

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
                Toast.makeText(parent.getContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

            }

        });
    }

}
