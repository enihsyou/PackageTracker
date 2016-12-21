package com.enihsyou.shane.packagetracker.helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import java.util.List;

public class LocationGetter extends Service implements LocationListener {
    private static final String TAG = "LocationGetter";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;
    double latitude;
    double longitude;
    private Context mContext;
    private boolean gpsIsOn;
    private boolean networkIsOn;
    private boolean locationAvailable;
    private Location current;

    public LocationGetter(Context context) {
        mContext = context;
        getLocation();
    }

    private Location getLocation() throws SecurityException {
        LocationManager locationManager =
            (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        gpsIsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkIsOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsIsOn && !networkIsOn) {
            Toast.makeText(mContext, "推荐开启定位系统", Toast.LENGTH_SHORT).show();
        } else {
            locationAvailable = true;
            if (networkIsOn) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                current =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (current != null) {
                    latitude = current.getLatitude();
                    longitude = current.getLongitude();
                }
            }
            if (gpsIsOn) { //gpsIsOn = true
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                current =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (current != null) {
                    latitude = current.getLatitude();
                    longitude = current.getLongitude();
                }
            }
        }
        return current;
    }

    public boolean isGpsIsOn() {
        return gpsIsOn;
    }

    public boolean isNetworkIsOn() {
        return networkIsOn;
    }

    public boolean isLocationAvailable() {
        return locationAvailable;
    }

    public Location getCurrent() {
        if (current == null) getLocation();
        return current;
    }

    public double getLatitude() {
        if (current == null) getLocation();
        return latitude;
    }

    public double getLongitude() {
        if (current == null) getLocation();
        return longitude;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(mContext, "定位系统已启用", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(mContext, "定位系统已停用", Toast.LENGTH_SHORT).show();
    }
}
