package swindroid.suntime.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import swindroid.suntime.R;
import swindroid.suntime.addLocation;
import swindroid.suntime.calc.AstronomicalCalendar;
import swindroid.suntime.calc.GeoLocation;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements AdapterView.OnItemSelectedListener
{
    Cities c;
	City selected = new City("Melbourne",-37.814,144.96332,TimeZone.getTimeZone("Australia/Melbourne"));
    String s;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeUI();
    }

	private void initializeUI()
	{
		DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		dp.init(year,month,day,dateChangeHandler); // setup initial values and reg. handler
		updateTime(year, month, day);
		c = new Cities(ReadText());

		// Spinner element
		Spinner spinner = (Spinner) findViewById(R.id.spinner);

		// Spinner click listener
		spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

		// Spinner Drop down elements
		List<String> categories = new ArrayList<String>();
		for(int i = 0; c.Length()-1 > i; i++)
		{
			categories.add(c.Cities.get(i).suburb);
		}

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinner.setAdapter(dataAdapter);

		Button button1 = (Button) findViewById(R.id.add);

		button1.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				addLocation();
			}
		});

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b != null)
        {
            s = b.getString("suburb");
            Toast.makeText(this, "Saved: " + s, Toast.LENGTH_LONG).show();
            selected = c.Cities.get(c.Cities.size()-1);
        }
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// On selecting a spinner item
		String item = parent.getItemAtPosition(position).toString();
        selected = c.Cities.get(position);
        refreshTime();
        Log.d("position", String.valueOf(position));
		// Showing selected spinner item
		Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
	}
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
        selected = new City("Melbourne",-37.814,144.96332,TimeZone.getTimeZone("Australia/Melbourne"));
	}

	public ArrayList<String> ReadText()
	{
        ArrayList<String> result = new ArrayList<String>();

		try {
            FileInputStream fis = this.openFileInput("1.txt");
            InputStreamReader isr = new InputStreamReader(fis);

            BufferedReader br = new BufferedReader(isr);
			String l;
			while((l = br.readLine()) != null)
			{
				result.add(l.toString());
                Log.e("line", l.toString());
			}
            br.close();
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

		TextView sunriseTV = (TextView) findViewById(R.id.sunriseTimeTV);
		TextView sunsetTV = (TextView) findViewById(R.id.sunsetTimeTV);
		Log.d("SUNRISE Unformatted", srise+"");

		sunriseTV.setText(sdf.format(srise));
		sunsetTV.setText(sdf.format(sset));
	}

	OnDateChangedListener dateChangeHandler = new OnDateChangedListener()
	{
		public void onDateChanged(DatePicker dp, int year, int monthOfYear, int dayOfMonth)
		{
			updateTime(year, monthOfYear, dayOfMonth);
		}
	};

    public void refreshTime()
    {
        TextView location = (TextView) findViewById(R.id.locationTV);
        location.setText(selected.suburb);

        DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        dp.init(year,month,day,dateChangeHandler); // setup initial values and reg. handler
        updateTime(year, month, day);
    }

	public void addLocation()
	{
        Intent intent = new Intent(this, addLocation.class);
        startActivity(intent);
	}
}