package com.example.cody.weathertime;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Cody on 1/5/2018.
 * Listener for androids built in location services
 */

public class androidLocationListener implements LocationListener
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
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Log.i(WeatherMain.TAG, provider + ":" + status + ":" + extras);
    }
    @Override
    public void onProviderEnabled(String provider)
    {
        Log.i(WeatherMain.TAG, provider);
    }
    @Override
    public void onProviderDisabled(String provider)
    {
        Log.i(WeatherMain.TAG, provider);
    }
}
