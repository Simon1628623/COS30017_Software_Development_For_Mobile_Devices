package swindroid.suntime.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import swindroid.suntime.R;
import swindroid.suntime.calc.AstronomicalCalendar;
import swindroid.suntime.calc.GeoLocation;

public class Suntimes extends Fragment implements AdapterView.OnItemSelectedListener{

    public static Cities c;
    public static City selected = new City("Melbourne",-37.814,144.96332,TimeZone.getTimeZone("Australia/Melbourne"));
    String s;
    public static String sunrise;
    public static String sunset;
    public static int SpinPos;

    DatePicker dp;
    Spinner spinner;
    TextView sunriseTV;
    TextView sunsetTV;
    TextView location;

    public Suntimes() {
        // Required empty public constructor
    }

    //containing activity calls this event handler when the display for fragment is to be formed
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suntimes, container, false);

        dp = (DatePicker) view.findViewById(R.id.datePicker);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        sunriseTV = (TextView) view.findViewById(R.id.sunriseTimeTV);
        sunsetTV = (TextView) view.findViewById(R.id.sunsetTimeTV);
        location = (TextView) view.findViewById(R.id.locationTV);

        initializeUI();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initializeUI()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        dp.init(year,month,day,dateChangeHandler); // setup initial values and reg. handler
        updateTime(year, month, day);
        c = new Cities(ReadText());

        // Spinner click listener
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

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
        spinner.setAdapter(dataAdapter);

        spinner.setSelection(SpinPos);

        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        if(b != null)
        {
            s = b.getString("suburb");
            Toast.makeText(getContext(), "Saved: " + s, Toast.LENGTH_LONG).show();
            selected = c.Cities.get(c.Cities.size()-1);
        }
    }

    public ArrayList<String> ReadText()
    {
        ArrayList<String> result = new ArrayList<String>();

        try {
            FileInputStream fis = getActivity().openFileInput("Locations.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String l;
            while((l = br.readLine()) != null)
            {
                result.add(l.toString());
                Log.e("line", l.toString());
            }
            br.close();
            isr.close();
            fis.close();
            Log.e("no more lines", "left to read");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void updateTime(int year, int monthOfYear, int dayOfMonth)
    {
        TimeZone tz = TimeZone.getDefault();
        GeoLocation geolocation = new GeoLocation(selected.suburb, selected.latitude, selected.longitude, selected.timeZone);
        AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
        ac.getCalendar().set(year, monthOfYear, dayOfMonth);
        Date srise = ac.getSunrise();
        Date sset = ac.getSunset();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        Log.d("SUNRISE Unformatted", srise+"");

        sunriseTV.setText(sdf.format(srise));
        sunsetTV.setText(sdf.format(sset));

        sunrise = sdf.format(srise);
        sunset = sdf.format(sset);
    }

    DatePicker.OnDateChangedListener dateChangeHandler = new DatePicker.OnDateChangedListener()
    {
        public void onDateChanged(DatePicker dp, int year, int monthOfYear, int dayOfMonth)
        {
            updateTime(year, monthOfYear, dayOfMonth);
        }
    };

    public void refreshTime()
    {
        location = (TextView) getView().findViewById(R.id.locationTV);
        location.setText(selected.suburb);

        dp = (DatePicker) getView().findViewById(R.id.datePicker);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        dp.init(year,month,day,dateChangeHandler); // setup initial values and reg. handler
        updateTime(year, month, day);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        selected = c.Cities.get(position);
        SpinPos = position;
        refreshTime();
        Log.d("position", String.valueOf(position));
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        selected = new City("Melbourne",-37.814,144.96332,TimeZone.getTimeZone("Australia/Melbourne"));
    }



}
