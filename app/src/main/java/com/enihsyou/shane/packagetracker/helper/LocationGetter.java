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
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private Context mContext;
    private Location current;

    public LocationGetter(Context context) {
        mContext = context;
        getLocation();
    }

    private Location getLocation() throws SecurityException {
        LocationManager locationManager =
            (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location == null) continue;
            if (current == null || location.getAccuracy() < current.getAccuracy()) {
                current = location;
            }
        }
        if (current == null) Toast.makeText(mContext, "未获得位置信息", Toast.LENGTH_SHORT).show();
        return current;
    }

    public Location getCurrent() {
        if (current == null) getLocation();
        return current;
    }

    public double getLatitude() {
        if (current == null) getLocation();
        return current.getLatitude();
    }

    public double getLongitude() {
        if (current == null) getLocation();
        return current.getLongitude();
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