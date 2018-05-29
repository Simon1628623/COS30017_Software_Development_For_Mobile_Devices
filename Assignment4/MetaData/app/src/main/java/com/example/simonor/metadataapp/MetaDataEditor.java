package com.example.simonor.metadataapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MetaDataEditor extends AppCompatActivity {

    public static String RETURN_PICTURE = "BBB";

    Picture picture;

    EditText name;
    EditText url;
    EditText keywords;
    EditText date;
    ToggleButton share;
    EditText email;
    RatingBar star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_data_editor);
        Initilize();
    }

    void Initilize()
    {
        //getting information sent by other activity
        Intent intent = getIntent();
        picture = intent.getParcelableExtra(MainActivity.PICTURE);
        getTextBoxes();
        postValues();
        Button b1 = (Button) findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener()
    {
        public void onClick(View v)
        {
            if(name.getText().toString().trim().equals("") && email.getText().toString().trim().equals(""))
            {
                Context context = getApplicationContext();
                CharSequence text = "Please Enter Name and Email!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            else
            {
                collectChanges();
                sendChanges();
            }
        }
    });
    }

    void getTextBoxes()
    {
        name = (EditText)findViewById(R.id.name);
        url = (EditText) findViewById(R.id.url);
        keywords = (EditText)findViewById(R.id.keywords);
        date = (EditText)findViewById(R.id.date);
        share = (ToggleButton)findViewById(R.id.toggleButton);
        email = (EditText)findViewById(R.id.email);
        star = (RatingBar)findViewById(R.id.ratingBar);
    }

    void postValues()
    {
        name.setText(picture.Name);
        url.setText(picture.Url);
        keywords.setText(picture.Keywords);
        date.setText(picture.Date);
        share.setChecked(picture.Share);
        email.setText(picture.Email);
        star.setRating(picture.Star);
    }

    void collectChanges()
    {
        picture.Name = name.getText().toString();
        picture.Url = url.getText().toString();
        picture.Keywords = keywords.getText().toString();
        picture.Date = date.getText().toString();
        picture.Share = share.isChecked();
        picture.Email = email.getText().toString();
        picture.Star = star.getNumStars();
    }

    void sendChanges()
    {
        //add parcelables saves values here
        final Intent returnIntent = new Intent();
        returnIntent.putExtra(RETURN_PICTURE, picture);
        String s = Integer.toString(picture.resID);
        //Log.d("resID b4 sending back", s);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    //SAME AS BUTTON
    @Override
    public void onBackPressed()
    {
        sendChanges();
        super.onBackPressed();
    }
}
