package swindroid.suntime.ui;

import android.util.Log;

import java.util.ArrayList;
import java.util.TimeZone;

import static java.lang.System.in;


/**
 * Created by Simonor on 13/09/2016.
 */
public class Cities
{
    public ArrayList<City> Cities;
    public int n;
    public String line;

    public Cities(ArrayList<String> s)
    {
        n = s.size();
        Log.e("s.length", String.valueOf(n));
        Cities = new ArrayList<City>();
        getData(s);
    }

    public int Length()
    {
        return n;
    }

    void getData(ArrayList<String> s)
    {
        for(String l : s)
        {
            Log.e("line", l);
            String[] entry = l.split(",");
            for(int q = 0; q < entry.length; q++)
            {
                Log.e("ec", entry[q]);
            }
            Log.e("entry length", Integer.toString(entry.length));
            if(entry.length == 4)
            {
                City c = new City(entry[0], Double.parseDouble(entry[1]), Double.parseDouble(entry[2]), TimeZone.getTimeZone(entry[3]));
                Cities.add(c);
            }
        }

    }
}
