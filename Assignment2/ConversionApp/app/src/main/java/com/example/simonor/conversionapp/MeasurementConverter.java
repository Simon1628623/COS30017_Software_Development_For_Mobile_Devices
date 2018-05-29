package com.example.simonor.conversionapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MeasurementConverter extends Activity {

    float miles;
    float feet;
    float inches;
    float result;
    String measurement;
    boolean mments;

    CheckBox mSet;
    TextView results;
    EditText Tmiles;
    EditText Tfeet;
    EditText Tinches;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_converter);
        Initialize();
    }

    //getting all variables for text boxes, check boxes and edit text boxes
    public void Initialize()
    {
        mSet = (CheckBox) findViewById(R.id.checkBox);
        Tmiles = (EditText)findViewById(R.id.miles);
        Tfeet = (EditText)findViewById(R.id.feet);
        Tinches = (EditText)findViewById(R.id.inches);
        results = (TextView)findViewById(R.id.result);
        //setting up button for converting
        Button convertButton = (Button)findViewById(R.id.convertButton);
        convertButton.setOnClickListener(convertBtnListener);
    }
    //when clicked do this
    private View.OnClickListener convertBtnListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            setResults();
        }
    };
    //assigning the result and setting the text box with the value
    public void setResults()
    {
        measurementCheck();

        setImperial();

        results.setText(String.valueOf(result) + measurement);
    }

    //checking to see if M or CM
    public void measurementCheck()
    {
        if(mSet.isChecked()) {
            measurement = "M";
            mments = true;
        }
        else{
            measurement = "CM";
            mments = false;
        }
    }
    //converting and checking if values not entered and assigning result
    public void setImperial(){
        if(!Tmiles.getText().toString().isEmpty())
        {miles =  Float.valueOf(Tmiles.getText().toString());}
        else
            miles = 0;
        if(!Tfeet.getText().toString().isEmpty())
        {feet =  Float.valueOf(Tfeet.getText().toString());}
        else
            feet = 0;
        if(!Tinches.getText().toString().isEmpty())
        {inches =  Float.valueOf(Tinches.getText().toString());}
        else
            inches = 0;

        result = getConvert(miles, feet, inches);
    }
    //getting the result value
    public float getConvert(float miles, float feet, float inches)
    {
        float result = 0;
        if(!mments)
            result = (float)(((((miles * 5280) + feet) * 12)+ inches) * 2.54);
        else
            result = (float)(((((miles * 5280) + feet) * 12)+ inches) * 2.54)/100;
        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle dataBundle) {
        super.onSaveInstanceState(dataBundle);

        //don't neccesarily need to save result and measurements can perform action at onCreate to set them
        //Bundle dataBundle = new Bundle();
        dataBundle.putFloat("Miles", miles);
        dataBundle.putFloat("Feet", feet);
        dataBundle.putFloat("Inches", inches);
        dataBundle.putFloat("Result", result);
        dataBundle.putString("measurement", measurement);
        dataBundle.putBoolean("mments", mments);


    }

    @Override
    protected void onRestoreInstanceState(Bundle dataBundle) {
        super.onRestoreInstanceState(dataBundle);

        miles = dataBundle.getFloat("Miles");
        feet = dataBundle.getFloat("Feet");
        inches = dataBundle.getFloat("Inches");
        result = dataBundle.getFloat("Result");
        measurement = dataBundle.getString("measurement");
        mments = dataBundle.getBoolean("mments");

        mSet = (CheckBox) findViewById(R.id.checkBox);
        mSet.setChecked(mments);

        Tmiles = (EditText)findViewById(R.id.miles);
        Tmiles.setText(String.valueOf(miles));
        Tfeet = (EditText)findViewById(R.id.feet);
        Tfeet.setText(String.valueOf(feet));
        Tinches = (EditText)findViewById(R.id.inches);
        Tinches.setText(String.valueOf(inches));

        results = (TextView)findViewById(R.id.result);
        results.setText(String.valueOf(result) + measurement);
    }

}
