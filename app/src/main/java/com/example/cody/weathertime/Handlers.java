package com.example.cody.weathertime;

import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by Cody on 12/29/2017.
 * Handler that deals with the refreshing
 */

public class Handlers
{
    public static int id = 1;

    public static Handler Refresher = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg)
        {
            WeatherMain.appMain.schedulerBox.setText("" + id);
            WeatherMain.appMain.timesCaughtBox.setText("" + RetrievedToDisplayInfo.timesCaught);
            id++;
            // this refresh is for the previous info that just was loaded

            RetrievedToDisplayInfo.onRefresh();
            // this starts the next request for info at whatever indicated time.
            WeatherMain.refreshThread = new Thread(new RefreshRunnable());
            WeatherMain.refreshThread.start();
        }
    };

    public static void handleNewLocation(Location location)
    {
        WeatherMain.appMain.mCurrentLocation = location;

        String date = new java.text.SimpleDateFormat("MMMM d H:mm:ss a").format(new java.util.Date(System.currentTimeMillis()));
        Log.i(WeatherMain.TAG, "Location last updated on: " + date);
        Log.i(WeatherMain.TAG, "Location found: Lat@" + location.getLatitude() +  ", Long@" + location.getLongitude() + ", Alt@" + location.getAltitude());
    }
}
