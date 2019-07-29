package com.example.cody.weathertime;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Cody on 1/3/2018.
 *
 */

 public class PermissionsFunctions
{
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 7171;
    public static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 7172;

    public static boolean hasFineLocationAccess()
    {
        if(ContextCompat.checkSelfPermission(WeatherMain.appMain, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(WeatherMain.appMain, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionsFunctions.MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            return false;
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionsFunctions.MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionsFunctions.MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }*/
        } else {
            System.out.println(">>IN DEV<< PRINT FOR HAVING PERMISSION TO ACCESS FINE LOCATION");
            return true;
        }
    }
    public static boolean hasCoarseLocationAccess()
    {
        if(ContextCompat.checkSelfPermission(WeatherMain.appMain, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(WeatherMain.appMain, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionsFunctions.MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
            return false;
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionsFunctions.MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionsFunctions.MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }*/
        } else {
            System.out.println(">>IN DEV<< PRINT FOR HAVING PERMISSION TO ACCESS COARSE LOCATION");
            return true;
        }
    }
}
