package com.example.simonor.conversionapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TemperatureConverter extends Activity
{

    double c;
    double f;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_converter);
        //checking to see if any saves for data from previous instances
        if(savedInstanceState != null)
        {
            EditText inputTempText = (EditText) findViewById(R.id.inputTempEditText);
            TextView convertedText = (TextView) findViewById(R.id.convertedTempTextView);
            convertedText.setText(String.valueOf(f)+" F");
            inputTempText.setText(String.valueOf(c));
        }
        initializeUI();
    }

    //setup ui
    private void initializeUI()
    {
        Button convertButton = (Button)findViewById(R.id.convertButton);
        convertButton.setOnClickListener(convertBtnListener);
        convertButtonClicked();
    }

    /** Handle convert button click */
    private View.OnClickListener convertBtnListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            convertButtonClicked();
        }
    };

    //when button clicked set result
    private void convertButtonClicked()
    {
        EditText inputTempText = (EditText) findViewById(R.id.inputTempEditText);
        TextView convertedText = (TextView) findViewById(R.id.convertedTempTextView);
        String frStr = convertToFh(inputTempText.getText().toString());
        convertedText.setText(frStr+" F");
    }

    /** Convert celcius provided to farenheit
     *
     * @param celcius A valid float number
     * @return Fahrenheit numeric value or "ERR" if unable to
     */
    private String convertToFh(String pCelcius)
    {
        try
        {
            double c = Double.parseDouble(pCelcius);
            double f = c * (9.0/5.0) + 32.0;
            return String.format("%3.2f", f);
        }
        catch (NumberFormatException nfe)
        {
            return "ERR";
        }
    }

    //saving values for new instance when rotated
    @Override
    protected void onSaveInstanceState(Bundle dataBundle) {
        super.onSaveInstanceState(dataBundle);

        dataBundle.putDouble("Celsius", c);
        dataBundle.putDouble("Fahrenheit", f);
    }

    //getting data from when the instance is started again
    @Override
    protected void onRestoreInstanceState(Bundle dataBundle) {
        super.onRestoreInstanceState(dataBundle);

        c = dataBundle.getDouble("Celsius");
        f = dataBundle.getDouble("Fahrenheit");
    }
}