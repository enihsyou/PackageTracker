package com.enihsyou.shane.packagetracker.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class ProviderLocationTracker
    implements LocationListener, LocationTracker.LocationUpdateListener {
    private static final String TAG = "ProviderLocationTracker";
    private static final long MIN_DISTANCE_UPDATES = 0;
    private static final long MIN_TIME_UPDATES = 0;
    private LocationManager manager;
    private String provider;
    private Location lastLocation;
    private long lastTime;
    private boolean isRunning;
    private LocationTracker.LocationUpdateListener listener;
    public ProviderLocationTracker(Context context, ProviderType type) {
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (type == ProviderType.NETWORK) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (type == ProviderType.GPS) {
            provider = LocationManager.GPS_PROVIDER;
        } else { throw new IllegalArgumentException(type.toString()); }
    }

    public void start(LocationTracker.LocationUpdateListener update) {
        start();
        listener = update;
    }

    public void start() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        manager.requestLocationUpdates(provider, MIN_TIME_UPDATES, MIN_DISTANCE_UPDATES, this);
        lastLocation = null;
        lastTime = 0;
    }

    public void stop() {
        if (isRunning) {
            manager.removeUpdates(this);
            isRunning = false;
            listener = null;
        }
    }

    public boolean hasLocation() {
        if (lastLocation == null) {
            return false;
        }
        return System.currentTimeMillis() - lastTime <= 5 * MIN_TIME_UPDATES;
    }

    public boolean hasPossiblyStaleLocation() {
        return lastLocation != null || manager.getLastKnownLocation(provider) != null;
    }

    public Location getLocation() {
        if (lastLocation == null || System.currentTimeMillis() - lastTime > 5 * MIN_TIME_UPDATES) {
            return null;
        }
        return lastLocation;
    }

    public Location getPossiblyStaleLocation() {
        if (lastLocation != null) {
            return lastLocation;
        }
        return manager.getLastKnownLocation(provider);
    }

    public void onLocationChanged(Location newLoc) {
        long now = System.currentTimeMillis();
        if (listener != null) {
            listener.onUpdate(lastLocation, lastTime, newLoc, now);
        }
        lastLocation = newLoc;
        lastTime = now;
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    public void onProviderEnabled(String arg0) {

    }

    public void onProviderDisabled(String arg0) {

    }

    @Override
    public void onUpdate(Location oldLoc, long oldTime, Location newLoc, long newTime) {

    }

    public enum ProviderType {
        NETWORK, GPS
    }
}
