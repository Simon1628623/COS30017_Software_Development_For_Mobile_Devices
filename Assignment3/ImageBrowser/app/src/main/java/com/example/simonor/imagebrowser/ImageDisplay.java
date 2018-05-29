package com.example.simonor.imagebrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

public class ImageDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        //getting information sent by other activity
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        setPicture(message);
    }

    public void setPicture(String message)
    {
        //converting message to include drawable to be located in drawable folder
        message = "@drawable/" + message;
        //getting resource ID
        int resID = getResources().getIdentifier(message , "drawable", getPackageName());
        //getting imageview to put picture into
        ImageView img = (ImageView)findViewById(R.id.imageView);
        TextView txt = (TextView)findViewById(R.id.textView);
        txt.setText(message);
        //assigning picture to imageView
        img.setImageResource(resID);

    }
}
