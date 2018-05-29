package com.example.simonor.metadataapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    public static final int PICTURE_CODE = 0x777;
    public static final String PICTURE = "AAA";

    Picture p1;
    Picture p2;
    Picture p3;
    Picture p4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initilize();
    }

    void Initilize()
    {
        CreatePictureData();
        Log.d("initilizing", "creating and setting");
        //getPictureIntent();

        setValues();

        //finding buttons
        ImageButton button1 = (ImageButton) findViewById(R.id.imageButton);
        ImageButton button2 = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton button3 = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton button4 = (ImageButton) findViewById(R.id.imageButton4);

        //assigning onclicklisteners to detect clicks
        button1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                sendIntent(p1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                sendIntent(p2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                sendIntent(p3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                sendIntent(p4);
            }
        });
    }

    public void getPictureIntent(Intent intent)
    {
        final Picture p = intent.getParcelableExtra(MetaDataEditor.RETURN_PICTURE);

        if(p != null)
        {
            switch(p.resID)
            {
                case 1:
                    p1 = p;
                    break;
                case 2:
                    p2 = p;
                    break;
                case 3:
                    p3 = p;
                    break;
                case 4:
                    p4 = p;
                    break;
                default:

                    break;
            }
        }
        else
        {
            Log.e("Picture is ", "NULL");
        }

    }

    public void sendIntent(Picture p)
    {
        final Intent intent = new Intent(this, MetaDataEditor.class);
        intent.putExtra(PICTURE, p);

        startActivityForResult(intent, PICTURE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if(intent == null)
            Log.i("MAIN IS RECEIVING", "IS NULL");
        if(requestCode == PICTURE_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                CreatePictureData();
                getPictureIntent(intent);
                setValues();
            }
        }
        else
        {
            Log.e("Intent", "Not recieved back");
        }

    }


    public void setValues()
    {
        TextView title1 = (TextView) findViewById(R.id.name1);
        TextView title2 = (TextView) findViewById(R.id.name2);
        TextView title3 = (TextView) findViewById(R.id.name3);
        TextView title4 = (TextView) findViewById(R.id.name4);

        TextView date1 = (TextView) findViewById(R.id.date1);
        TextView date2 = (TextView) findViewById(R.id.date2);
        TextView date3 = (TextView) findViewById(R.id.date3);
        TextView date4 = (TextView) findViewById(R.id.date4);

        if(p1 != null)
            title1.setText(p1.Name);
        if(p2 != null)
            title2.setText(p2.Name);
        if(p3 != null)
            title3.setText(p3.Name);
        if(p4 != null)
            title4.setText(p4.Name);


        if(!p1.Date.isEmpty())
            date1.setText(p1.Date);
        if(!p2.Date.isEmpty())
            date2.setText(p2.Date);
        if(!p3.Date.isEmpty())
            date3.setText(p3.Date);
        if(!p4.Date.isEmpty())
            date4.setText(p4.Date);

    }

    public void CreatePictureData()
    {
        p1 = new Picture("sea", "aaa@gmail.com", 1);
        p2 = new Picture("digger", "aba@gmail.com", 2);
        p3 = new Picture("grass", "acc@gmail.com", 3);
        p4 = new Picture("race", "asa@gmail.com", 4);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");
        String dt = sdf.format(new Date());

        p1.Date = dt;
        p2.Date = dt;
        p3.Date = dt;
        p4.Date = dt;
    }

}
