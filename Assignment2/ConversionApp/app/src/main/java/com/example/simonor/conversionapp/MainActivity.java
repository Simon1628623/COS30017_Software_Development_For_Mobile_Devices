package com.example.simonor.conversionapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button t;
    Button m;

       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initilize();

    }

    public void Initilize()
    {
        /*t = (Button)findViewById(R.id.t);
        m = (Button)findViewById(R.id.m);

        t.setOnClickListener(onClickListener);
        m.setOnClickListener(onClickListener);*/
        //finding buttons
        Button b1=(Button)findViewById(R.id.t);
        Button b2=(Button)findViewById(R.id.m);
        //on click send user to other activities with intent
        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent myintent2 = new Intent(MainActivity.this,TemperatureConverter.class);
                startActivity(myintent2);

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent myintent2 = new Intent(MainActivity.this,MeasurementConverter.class);
                startActivity(myintent2);

            }
        });
    }
/*
    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
           if(R.id.m == v.getId())
               startActivity(new Intent(MainActivity.this, MeasurementConverter.class));
            else if(R.id.t == v.getId())
                startActivity(new Intent(MainActivity.this, TemperatureConverter.class));
        }
    };*/
}
