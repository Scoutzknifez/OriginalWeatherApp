package com.example.cody.weathertime;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

/**
 * Created by Cody on 1/4/2018.
 * Location listener
 */

public class googleLocationListener implements LocationListener
{
    @Override
    public void onLocationChanged(Location location)
    {
        Log.i(WeatherMain.TAG, "Location has changed.");

        if(location != null)
        {
            Handlers.handleNewLocation(location);
        }
    }
}
