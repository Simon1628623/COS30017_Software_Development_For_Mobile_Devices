package swindroid.suntime.ui;

import android.content.Context;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TimeZone;

import swindroid.suntime.R;

/**
 * Created by Simonor on 13/09/2016.
 */
public class City
{
    public String suburb;
    public double latitude;
    public double longitude;
    public TimeZone timeZone;

    public City(String sub, double lat, double lon, TimeZone tz)
    {
        suburb = sub;
        latitude = lat;
        longitude = lon;
        timeZone = tz;
    }

    public City()
    {

    }

    public String getName() {
        return suburb;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }


}
