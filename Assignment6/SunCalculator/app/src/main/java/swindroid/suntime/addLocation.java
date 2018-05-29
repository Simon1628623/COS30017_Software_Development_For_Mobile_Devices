package swindroid.suntime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.util.TimeZone;

import swindroid.suntime.ui.Main;

public class addLocation extends Activity {

    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        Initilize();
    }

    void Initilize()
    {
        Button button1 = (Button) findViewById(R.id.submit);

        button1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                submit();
            }
        });

    }

    void submit()
    {
        EditText name = (EditText) findViewById(R.id.name);
        EditText lat = (EditText) findViewById(R.id.latitude);
        EditText lon = (EditText) findViewById(R.id.longitude);
        EditText tz = (EditText) findViewById(R.id.tz);

        String n = name.getText().toString();
        String la = lat.getText().toString();
        String lo = lon.getText().toString();
        String t = tz.getText().toString();

        String line = "\n" + n + "," + la + "," + lo + "," + t;
        byte[] b = line.getBytes();


        FileOutputStream out = null;
        try {
            out = openFileOutput("1.txt", MODE_APPEND);
            out.write(b);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, Main.class);
        intent.putExtra("suburb", n);
        startActivity(intent);

    }
}
