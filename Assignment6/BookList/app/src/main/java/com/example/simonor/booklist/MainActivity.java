package com.example.simonor.booklist;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
    }

    private void initializeUI()
    {
        //construct the data source
        ArrayList<Book> arrayofBooks = new ArrayList<Book>();
        //create the adapter to convert the array to views
        RowIconAdapter adapter = new RowIconAdapter(this, arrayofBooks);
        //adding books
        Book b1 = new Book("Mad Hats", 7);
        Book b2 = new Book("qwerty", 12);
        Book b3 = new Book("Zerker", 4);
        Book b4 = new Book("Fails Compilation", 7);
        Book b5 = new Book("Dying to Kill", 2);
        Book b6 = new Book("Android", 3);
        Book b7 = new Book("How to Noob", 10);
        Book b8 = new Book("Overmatch", 8);
        Book b9 = new Book("777", 777);
        Book b10 = new Book("The Happy Lemon", 77);
        adapter.add(b1);
        adapter.add(b2);
        adapter.add(b3);
        adapter.add(b4);
        adapter.add(b5);
        adapter.add(b6);
        adapter.add(b7);
        adapter.add(b8);
        adapter.add(b9);
        adapter.add(b10);
        //attach the adapter to a listview
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    public class RowIconAdapter extends ArrayAdapter<Book>
    {
        //view lookup cache
        private class ViewHolder
        {
            TextView name;
            TextView rating;
            ImageView icon;
        }
        public RowIconAdapter(Context context, ArrayList<Book> books)
        {
            super(context, R.layout.listrow, books);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Book b = getItem(position);
            ViewHolder viewHolder; //view lookup cache stored in tag
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null)
            {
                //if there is no view to reuse, inflate a brand new view for row
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listrow, parent, false);
                // Lookup view for data population
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.row_icon);
                viewHolder.name = (TextView) convertView.findViewById(R.id.row_label);
                viewHolder.rating = (TextView) convertView.findViewById(R.id.row_label2);
                //cache viewholder object inside fresh view
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // Populate the data into the template view using the data object
            viewHolder.name.setText(b.name);
            viewHolder.rating.setText(b.rating.toString() + "/10");
            //setting pictures
            switch(b.name)
            {
                case "Mad Hats":
                    viewHolder.icon.setImageResource(R.mipmap.ic_launcher);
                    break;
                case "qwerty":
                    viewHolder.icon.setImageResource(R.mipmap.ic_colour);
                    break;
                case "Zerker":
                    viewHolder.icon.setImageResource(R.mipmap.ic_movie);
                    break;
                case "Fails Compilation":
                    viewHolder.icon.setImageResource(R.mipmap.ic_photo);
                    break;
                case "Dying to Kill":
                    viewHolder.icon.setImageResource(R.mipmap.ic_launcher);
                    break;
                case "Android":
                    viewHolder.icon.setImageResource(R.mipmap.ic_brightness);
                    break;
                case "How to Noob":
                    viewHolder.icon.setImageResource(R.mipmap.ic_brightness);
                    break;
                case "Overmatch":
                    viewHolder.icon.setImageResource(R.mipmap.ic_launcher);
                    break;
                case "777":
                    viewHolder.icon.setImageResource(R.mipmap.ic_audio);
                    break;
                case "The Happy Lemon":
                    viewHolder.icon.setImageResource(R.mipmap.ic_launcher);
                    break;
                default:
                    viewHolder.icon.setImageResource(R.mipmap.ic_launcher);
                    break;
            }
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
