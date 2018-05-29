package com.example.simonor.imagebrowser;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "AAA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //finding buttons in layout
        ImageButton b1 = (ImageButton)findViewById(R.id.imageView1);
        ImageButton b2 = (ImageButton)findViewById(R.id.imageView2);
        ImageButton b3 = (ImageButton)findViewById(R.id.imageView3);
        ImageButton b4 = (ImageButton)findViewById(R.id.imageView4);
        //setting listener to activate when clicked
        b1.setOnClickListener(onClickListener);
        b2.setOnClickListener(onClickListener);
        b3.setOnClickListener(onClickListener);
        b4.setOnClickListener(onClickListener);
    }
    //on click of any button send to other layout
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendIntent(v);

        }
    };

    //saving choice of picture into string then loading other activity
    public void sendIntent(View V)
    {
        Intent intent = new Intent(this, ImageDisplay.class);
        ImageButton clicked = (ImageButton) findViewById(V.getId());
        String message = clicked.getContentDescription().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
