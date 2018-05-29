package swindroid.suntime.ui;

import android.util.Log;

import java.util.TimeZone;


/**
 * Created by Simonor on 13/09/2016.
 */
public class Cities
{
    public City[] Cities;
    public int n;
    public String line;

    public Cities(String[] s)
    {
        Log.e("s.length", String.valueOf(s.length));
        n = s.length;
        Cities = new City[n];
        getData(s);
    }

    public int Length()
    {
        return n;
    }

    void getData(String[] s)
    {
        for(int i = 0; n > i; i++)
        {
            Cities[i] = new City();
            String currentLine = s[i];
            Log.d("recieving", currentLine);

            String[] entry = currentLine.split(",");

            Cities[i].suburb = entry[0];
            Cities[i].latitude = Double.parseDouble(entry[1]);
            Cities[i].longitude = Double.parseDouble(entry[2]);
            Cities[i].timeZone =  TimeZone.getTimeZone(entry[3]);

            Log.d("suburb is", Cities[i].suburb);
            Log.d("lat is", Double.toString(Cities[i].latitude));
            Log.d("long is", Double.toString(Cities[i].longitude));
            Log.d("timezone is", Cities[i].timeZone.toString());
        }
    }
}
