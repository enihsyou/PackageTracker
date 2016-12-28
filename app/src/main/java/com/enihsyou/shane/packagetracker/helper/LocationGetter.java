package com.enihsyou.shane.packagetracker.helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class LocationGetter extends Service implements LocationListener {
    private static final String TAG = "LocationGetter";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private Context mContext;
    private Location current;
    private boolean update;

    public LocationGetter(Context context, boolean update) {
        mContext = context;
        // this.update = update;
        this.update = false;
        getLocation();
    }

    private Location getLocation() {
        LocationManager locationManager =
            (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location == null || this.update) {
                Log.i(TAG, String.format("getLocation: %s %s, request update", provider, location));
                locationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            } else if (current == null || location.getAccuracy() < current.getAccuracy()) {
                current = location;
            }
        }
        if (current == null) {
            Toast.makeText(mContext, "未获得位置信息", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "getLocation: No location");
        }
        else locationManager.removeUpdates(this);
        return current;
    }

    public static boolean isPositionable(Context context) {
        LocationManager locationManager =
            (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d(TAG, String.format(Locale.getDefault(), "isPositionable: 当前设备服务状态 GPS: %b Network: %b", gps, network));
        return gps || network;
    }

    public double getLatitude() {
        return getCurrent().getLatitude();
    }

    public Location getCurrent() {
        if (current == null) getLocation();
        return current;
    }

    public double getLongitude() {
        return getCurrent().getLongitude();
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
        Log.d(TAG, "onProviderEnabled: 定位系统已启用");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(mContext, "定位系统已停用", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onProviderDisabled: 定位系统已停用");
    }

}
