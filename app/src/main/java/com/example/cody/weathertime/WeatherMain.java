package com.example.cody.weathertime;

/**
 * Created by Cody on 12/26/2017.
 * main for weather app
 */
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class WeatherMain extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    public static WeatherMain appMain = null;
    public static final String TAG = WeatherMain.class.getSimpleName();

    public static int timeToRefreshInMinutes = 15;
    public static int timeToRefreshInSeconds = timeToRefreshInMinutes * 60;

    public static Thread refreshThread = null;

    public static ArrayList<WeatherForDay> displayedWeathers = new ArrayList<>();
    public static WeatherForDay displayedCurrent;


    public ArrayList<View> views = new ArrayList<>();
    public ImageView TodayImage, Day1Image, Day2Image, Day3Image, Day4Image, Day5Image, Day6Image,
                    TodayRain, DayRain1, DayRain2, DayRain3, DayRain4, DayRain5, DayRain6,
                    HLine, VLine1, VLine2, VLine3, VLine4, VLine5, VLine6;
    public TextView TODAY_TEXT, CurrentTemp, TodayDayOfWeek, TodayHigh, TodayLow, TodayPercipitationChance, TodayWind, TodayDate, lastUpdated, NotificationForLocationServicesBeingDisabled,
                    Day1, DayDate1, DayHigh1, DayLow1, DayWind1, DayPercipitationChance1,
                    Day2, DayDate2, DayHigh2, DayLow2, DayWind2, DayPercipitationChance2,
                    Day3, DayDate3, DayHigh3, DayLow3, DayWind3, DayPercipitationChance3,
                    Day4, DayDate4, DayHigh4, DayLow4, DayWind4, DayPercipitationChance4,
                    Day5, DayDate5, DayHigh5, DayLow5, DayWind5, DayPercipitationChance5,
                    Day6, DayDate6, DayHigh6, DayLow6, DayWind6, DayPercipitationChance6;
    public TextView schedulerBox, timesCaughtBox;

    public boolean shouldViewsGoVisible = true;

    public boolean hasGooglePlayServices = false;

    //private FusedLocationProviderClient mFusedLocationClient;
    //private LocationCallback mLocationCallBack;

    public static boolean mRequestingLocationUpdates;

    // these are all required for google play services.
    LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // this is required for android location services
    public LocationManager mLocationManager;
    public static final int TIME_IN_MILLISEC_TO_REFRESH_LOCATION = 2000;//(((timeToRefreshInSeconds * 1000) * 2) / 3);
    public static final int DISTANCE_TO_REFRESH_LOCATION_IN_METERS = 10;

    //this is listener for location changing
    public Object mLocationListener;
    public Location mCurrentLocation;

    public int connectedTime = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //set the screen to show

        //get info and then check if they are good sets of data, then set to the displayed set
        appMain = this;

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);
        if (code == ConnectionResult.SUCCESS)
        {
            Log.i(TAG, "This device has Google Play services...");
            hasGooglePlayServices = true;
            initializeGooglePlayLocationServiceEquipment();
        } else {
            Log.i(TAG, "This device does not have Google Play services...");
            hasGooglePlayServices = false;
            initializeAndroidLocationServiceEquipment();
        }

        //System.out.println(getLastLocation());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT > 16) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_weather_main);
        //initializers for text and images on screen
        NotificationForLocationServicesBeingDisabled = (TextView) findViewById(R.id.LocationServicesDisabledNotification);
        //init images
        TodayImage = (ImageView) findViewById(R.id.todayImage);
        Day1Image = (ImageView) findViewById(R.id.day1Image);
        Day2Image = (ImageView) findViewById(R.id.day2Image);
        Day3Image = (ImageView) findViewById(R.id.day3Image);
        Day4Image = (ImageView) findViewById(R.id.day4Image);
        Day5Image = (ImageView) findViewById(R.id.day5Image);
        Day6Image = (ImageView) findViewById(R.id.day6Image);
        // boxes for rain
        TodayRain = (ImageView) findViewById(R.id.raindrops);
        DayRain1 = (ImageView) findViewById(R.id.raindrops1);
        DayRain2 = (ImageView) findViewById(R.id.raindrops2);
        DayRain3 = (ImageView) findViewById(R.id.raindrops3);
        DayRain4 = (ImageView) findViewById(R.id.raindrops4);
        DayRain5 = (ImageView) findViewById(R.id.raindrops5);
        DayRain6 = (ImageView) findViewById(R.id.raindrops6);
        // splitters for layout
        HLine = (ImageView) findViewById(R.id.hLine);
        VLine1 = (ImageView) findViewById(R.id.vLine1);
        VLine2 = (ImageView) findViewById(R.id.vLine2);
        VLine3 = (ImageView) findViewById(R.id.vLine3);
        VLine4 = (ImageView) findViewById(R.id.vLine4);
        VLine5 = (ImageView) findViewById(R.id.vLine5);
        VLine6 = (ImageView) findViewById(R.id.vLine6);
        //init text
        //day 0 (today)
        CurrentTemp = (TextView) findViewById(R.id.currentTemp);
        TodayDayOfWeek = (TextView) findViewById(R.id.todayDay);
        TodayHigh = (TextView) findViewById(R.id.todayHigh);
        TodayLow = (TextView) findViewById(R.id.todayLow);
        TodayPercipitationChance = (TextView) findViewById(R.id.todayPercipitation);
        TodayWind = (TextView) findViewById(R.id.todayWind);
        TodayDate = (TextView) findViewById(R.id.todayDate);
        //day 1 (tomorrow)
        Day1 = (TextView) findViewById(R.id.day);
        DayDate1 = (TextView) findViewById(R.id.dayDate);
        DayHigh1 = (TextView) findViewById(R.id.dayHigh);
        DayLow1 = (TextView) findViewById(R.id.dayLow);
        DayWind1 = (TextView) findViewById(R.id.dayWind);
        DayPercipitationChance1 = (TextView) findViewById(R.id.dayPercipitation);
        //day 2
        Day2 = (TextView) findViewById(R.id.day2);
        DayDate2 = (TextView) findViewById(R.id.dayDate2);
        DayHigh2 = (TextView) findViewById(R.id.dayHigh2);
        DayLow2 = (TextView) findViewById(R.id.dayLow2);
        DayWind2 = (TextView) findViewById(R.id.dayWind2);
        DayPercipitationChance2 = (TextView) findViewById(R.id.dayPercipitation2);
        //day 3
        Day3 = (TextView) findViewById(R.id.day3);
        DayDate3 = (TextView) findViewById(R.id.dayDate3);
        DayHigh3 = (TextView) findViewById(R.id.dayHigh3);
        DayLow3 = (TextView) findViewById(R.id.dayLow3);
        DayWind3 = (TextView) findViewById(R.id.dayWind3);
        DayPercipitationChance3 = (TextView) findViewById(R.id.dayPercipitation3);
        //day 4
        Day4 = (TextView) findViewById(R.id.day4);
        DayDate4 = (TextView) findViewById(R.id.dayDate4);
        DayHigh4 = (TextView) findViewById(R.id.dayHigh4);
        DayLow4 = (TextView) findViewById(R.id.dayLow4);
        DayWind4 = (TextView) findViewById(R.id.dayWind4);
        DayPercipitationChance4 = (TextView) findViewById(R.id.dayPercipitation4);
        //day 5
        Day5 = (TextView) findViewById(R.id.day5);
        DayDate5 = (TextView) findViewById(R.id.dayDate5);
        DayHigh5 = (TextView) findViewById(R.id.dayHigh5);
        DayLow5 = (TextView) findViewById(R.id.dayLow5);
        DayWind5 = (TextView) findViewById(R.id.dayWind5);
        DayPercipitationChance5 = (TextView) findViewById(R.id.dayPercipitation5);
        //day 6
        Day6 = (TextView) findViewById(R.id.day6);
        DayDate6 = (TextView) findViewById(R.id.dayDate6);
        DayHigh6 = (TextView) findViewById(R.id.dayHigh6);
        DayLow6 = (TextView) findViewById(R.id.dayLow6);
        DayWind6 = (TextView) findViewById(R.id.dayWind6);
        DayPercipitationChance6 = (TextView) findViewById(R.id.dayPercipitation6);
        //text for time and last update
        schedulerBox = (TextView) findViewById(R.id.testingSchedulerTextBox);
        timesCaughtBox = (TextView) findViewById(R.id.timesCaughtBox);
        lastUpdated = (TextView) findViewById(R.id.lastUpdated);
        // text for debugging
        schedulerBox.setText("0");
        timesCaughtBox.setText("0");
        TODAY_TEXT = (TextView) findViewById(R.id.TODAY_TEXT);
        // last updated text
        lastUpdated.setText(getString(R.string.notUpdated));

        // adding to views list to change visibility of the whole page
        views.add(TodayImage);
        views.add(Day1Image);
        views.add(Day2Image);
        views.add(Day3Image);
        views.add(Day4Image);
        views.add(Day5Image);
        views.add(Day6Image);
        views.add(TodayRain);
        views.add(DayRain1);
        views.add(DayRain2);
        views.add(DayRain3);
        views.add(DayRain4);
        views.add(DayRain5);
        views.add(DayRain6);
        views.add(HLine);
        views.add(VLine1);
        views.add(VLine2);
        views.add(VLine3);
        views.add(VLine4);
        views.add(VLine5);
        views.add(VLine6);
        views.add(TODAY_TEXT);
        views.add(CurrentTemp);
        views.add(TodayDayOfWeek);
        views.add(TodayHigh);
        views.add(TodayLow);
        views.add(TodayPercipitationChance);
        views.add(TodayWind);
        views.add(TodayDate);
        views.add(lastUpdated);
        views.add(Day1);
        views.add(Day2);
        views.add(Day3);
        views.add(Day4);
        views.add(Day5);
        views.add(Day6);
        views.add(DayDate1);
        views.add(DayDate2);
        views.add(DayDate3);
        views.add(DayDate4);
        views.add(DayDate5);
        views.add(DayDate6);
        views.add(DayHigh1);
        views.add(DayHigh2);
        views.add(DayHigh3);
        views.add(DayHigh4);
        views.add(DayHigh5);
        views.add(DayHigh6);
        views.add(DayLow1);
        views.add(DayLow2);
        views.add(DayLow3);
        views.add(DayLow4);
        views.add(DayLow5);
        views.add(DayLow6);
        views.add(DayWind1);
        views.add(DayWind2);
        views.add(DayWind3);
        views.add(DayWind4);
        views.add(DayWind5);
        views.add(DayWind6);
        views.add(DayPercipitationChance1);
        views.add(DayPercipitationChance2);
        views.add(DayPercipitationChance3);
        views.add(DayPercipitationChance4);
        views.add(DayPercipitationChance5);
        views.add(DayPercipitationChance6);
        views.add(schedulerBox);
        views.add(timesCaughtBox);

        setViewVisibility(false);


        PermissionsFunctions.hasFineLocationAccess();
        PermissionsFunctions.hasCoarseLocationAccess();
        /*if (PermissionsFunctions.hasFineLocationAccess())
        {
            startLocationUpdates();
        }*/
    }
    public void setViewVisibility(boolean visible)
    {
        int visibility = View.VISIBLE;
        if(!visible)
        {
            visibility = View.INVISIBLE;
        }
        for(View view: views)
        {
            view.setVisibility(visibility);
        }
    }
    public void initializeAndroidLocationServiceEquipment()
    {
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new androidLocationListener();
    }
    public void initializeGooglePlayLocationServiceEquipment()
    {
        Log.i(TAG, "Building Google Play location services...");
        buildGoogleApiClient();

        buildLocationRequest();

        //buildLocationCallback();

        mLocationListener = new googleLocationListener();
    }
    private synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }
    private synchronized void buildLocationRequest()
    {
        int quickestRefresh = (((timeToRefreshInSeconds * 1000) * 2) / 3);
        int regularRefresh = ((timeToRefreshInSeconds * 1000) / 3);

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(regularRefresh)
                .setFastestInterval(quickestRefresh);
    }
    /*private synchronized void buildLocationCallback()
    {
        mLocationCallBack = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                for(Location location: locationResult.getLocations())
                {
                    System.out.println("callback says: " + location);
                }
            }
        };
    }*/
    public Location getLastLocation()
    {
        Log.i(TAG, "Getting last location...");
        if(hasGooglePlayServices)
        {
            try {
                int quickestRefresh = 1;
                int regularRefresh = 2;

               LocationRequest tempLocationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(regularRefresh)
                        .setFastestInterval(quickestRefresh);

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, tempLocationRequest, (googleLocationListener) mLocationListener);

                return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } catch(SecurityException e) {
                Log.i(TAG, "Does not have permission for locations...");
                return null;
            }
        } else {
            try {
                String bestProvider = mLocationManager.getBestProvider(new Criteria(), true);
                androidLocationListener tempListener = new androidLocationListener();
                mLocationManager.requestLocationUpdates(bestProvider, 0, 0,tempListener);
                /*while(mCurrentLocation == null)
                {

                }*/

                return mLocationManager.getLastKnownLocation(bestProvider);
            } catch(SecurityException e) {
                Log.i(TAG, "Does not have permission for locations...");
                return null;
            }
        }
    }
    public void startWeather()
    {
        Log.i(TAG, "Starting weather display...");
        Runnable startThread = new Runnable()
        {
            @Override
            public void run()
            {
                //get info and then check if they are good sets of data, then set to the displayed set
                if (Functions.updateWeather())
                {
                    if (RetrievedToDisplayInfo.WeatherListIsValid())
                    {
                        displayedWeathers = RetrievedToDisplayInfo.dailyForecast;
                    }
                    if (RetrievedToDisplayInfo.WeatherCurrentIsValid())
                    {
                        displayedCurrent = RetrievedToDisplayInfo.current;
                    }
                }
            }
        };
        refreshThread = new Thread(startThread);
        refreshThread.start();

        displayedCurrent = new WeatherForDay();
        for (int i = 0; i < 7; i++) {
            displayedWeathers.add(new WeatherForDay());
        }

        while (refreshThread.isAlive()) {

        }

        RetrievedToDisplayInfo.onRefresh();

        if(shouldViewsGoVisible)
        {
            setViewVisibility(true);
        }

        RefreshRunnable.timeInSeconds = timeToRefreshInSeconds;
        refreshThread = new Thread(new RefreshRunnable());
        refreshThread.start();
    }
    public boolean isGPSLocationsEnabled()
    {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public boolean isNetworkLocationsEnabled()
    {
        return mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    public boolean isPassiveLocationEnabled()
    {
        return mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
    }
    @Override
    protected void onResume()
    {
        Log.i(TAG, "Resuming...");
        super.onResume();
        if (hasGooglePlayServices)
        {
            if(mGoogleApiClient.isConnected())
            {
                if(mGoogleApiClient != null)
                {
                    mGoogleApiClient.connect();
                }
                if(mRequestingLocationUpdates)
                {
                    try {
                        startLocationUpdates();
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            if(connectedTime == -1)
            {
                connectedTime++;
                mCurrentLocation = getLastLocation();

                stopLocationUpdates();

                startLocationUpdates();

                startWeather();
            } else {
                startLocationUpdates();
            }
        }
        appMain = this;
    }
    @Override
    protected void onPause()
    {
        Log.i(TAG, "Pausing...");
        super.onPause();
        if (hasGooglePlayServices)
        {
            if(mGoogleApiClient != null)
            {
                if(mGoogleApiClient.isConnected())
                {
                    Log.i(TAG, "Disconnecting from Google API.");
                    stopLocationUpdates();
                    mGoogleApiClient.disconnect();
                }
            }
        } else {
            stopLocationUpdates();
        }
        appMain = null;
    }
    public void startLocationUpdates()
    {
        Log.i(TAG, "Starting location requests...");
        if(hasGooglePlayServices)
        {
            if(!mRequestingLocationUpdates)
            {
                try {
                    try {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (googleLocationListener) mLocationListener);
                        mRequestingLocationUpdates = true;
                    } catch(IllegalStateException e) {
                        Log.i(TAG, "Google API Client is not connected yet...");
                    }
                    //mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, getMainLooper());
                } catch(SecurityException e) {
                    e.printStackTrace();
                }
            }
        } else {
            String bestProvider = mLocationManager.getBestProvider(new Criteria(), true);
            if(bestProvider != null)
            {
                System.out.println(bestProvider);
                try {
                    mLocationManager.requestLocationUpdates(bestProvider, TIME_IN_MILLISEC_TO_REFRESH_LOCATION, DISTANCE_TO_REFRESH_LOCATION_IN_METERS, (androidLocationListener) mLocationListener);
                } catch(SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void stopLocationUpdates()
    {
        if (hasGooglePlayServices)
        {
            if(mRequestingLocationUpdates)
            {
                Log.i(TAG, "Stopping location requests...");
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (googleLocationListener) mLocationListener);
                mRequestingLocationUpdates = false;
            }
            //mFusedLocationClient.removeLocationUpdates(mLocationCallBack);
        } else {
            mLocationManager.removeUpdates((androidLocationListener) mLocationListener);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch(requestCode)
        {
            case PermissionsFunctions.MY_PERMISSIONS_REQUEST_FINE_LOCATION:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.i(TAG, "Received Permission to request fine location...");
                    Log.i(TAG, "Initializing Location services...");
                    if(hasGooglePlayServices)
                    {
                        // starts up the google play service locations
                        initializeGooglePlayLocationServiceEquipment();
                    } else {
                        // starts up android built in location services

                    }
                } else {
                    //use default values of long lat
                }
                return;
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        //outState.putBoolean();
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        Log.i(TAG, "Connected to google services...");
        if(hasGooglePlayServices)
        {
            if(connectedTime == -1)
            {
                connectedTime++;

                mCurrentLocation = getLastLocation();

                try {
                    if(mCurrentLocation == null)
                    {
                        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    }
                    startLocationUpdates();
                } catch(SecurityException e) {
                    e.printStackTrace();
                }

                startWeather();
            } else {
                startLocationUpdates();
                /*if(mRequestingLocationUpdates)
                {
                    startLocationUpdates();
                }*/
                Location location = null;
                try {
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                } catch(SecurityException e) {
                    e.printStackTrace();
                }
                if(location != null)
                {
                    Handlers.handleNewLocation(location);
                }
            }
        }
    }
    @Override
    public void onConnectionSuspended(int i)
    {
        // this is a google api function only
        Log.i(TAG, "Location services suspended. Attempting to reconnect...");
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        // this is a google api function only

        if (connectionResult.hasResolution())
        {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
}