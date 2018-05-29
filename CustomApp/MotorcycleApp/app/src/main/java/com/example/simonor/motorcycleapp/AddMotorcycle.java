package com.example.simonor.motorcycleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import layout.Bike;

public class AddMotorcycle extends AppCompatActivity {

    Spinner spinner;
    Button b;
    EditText y;
    EditText m;
    EditText o;

    String Make;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_motorcycle);
        Initilize();
    }

    void Initilize()
    {
        spinner = (Spinner) findViewById(R.id.make);
        b = (Button) findViewById(R.id.button);
        y = (EditText) findViewById(R.id.year);
        m = (EditText) findViewById(R.id.model);
        o = (EditText) findViewById(R.id.Odo);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bike_makes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                Make = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        b.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                submit();
            }
        });
    }

    public void submit()
    {
        if(y.getText().toString().equalsIgnoreCase("") && m.getText().toString().equalsIgnoreCase("") && o.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_LONG).show();
        }
        else
        {
            boolean success = true;
            Bike b = new Bike();
            try {

                b.Make = Make;
                String yr = y.getText().toString();
                b.Year = Integer.parseInt(yr);
                b.Model = m.getText().toString();
                String od = o.getText().toString();
                b.Odometer = Double.parseDouble(od);
            } catch (NumberFormatException e) {
                success = false;
            }
            if(success)
            {
                Toast.makeText(this,"Success", Toast.LENGTH_LONG).show();

                String s = b.Make + "," + b.Year + "," + b.Model + "," + b.Odometer + "\n";
                writeToFile(s);

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(this, "Please enter correct fields", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void writeToFile(String s)
    {
        //creating file if it does not exist
        File f = new File("bikes.txt");
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
