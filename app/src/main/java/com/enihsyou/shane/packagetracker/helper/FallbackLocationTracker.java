package com.enihsyou.shane.packagetracker.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class FallbackLocationTracker
    implements LocationTracker, LocationTracker.LocationUpdateListener {
    Location lastLocation;
    long lastTime;
    private boolean isRunning;
    private ProviderLocationTracker gps;
    private ProviderLocationTracker net;
    private LocationTracker.LocationUpdateListener listener;

    public FallbackLocationTracker(Context context, ProviderLocationTracker.ProviderType type) {
        gps = new ProviderLocationTracker(context, ProviderLocationTracker.ProviderType.GPS);
        net = new ProviderLocationTracker(context, ProviderLocationTracker.ProviderType.NETWORK);
    }

    public void onUpdate(Location oldLocation, long oldTime, Location newLocation, long newTime) {
        boolean update = false;
        if (lastLocation == null || lastLocation.getProvider().equals(newLocation.getProvider())
            || newLocation.getProvider().equals(LocationManager.GPS_PROVIDER)
            || newTime - lastTime > 5 * 60 * 1000) { update = true; }

        if (update) {
            if (listener != null) {
                listener.onUpdate(lastLocation, lastTime, newLocation, newTime);
            }
            lastLocation = newLocation;
            lastTime = newTime;
        }
    }

    public void start() {
        if (isRunning) {
            return;
        }
        gps.start(this);
        net.start(this);
        isRunning = true;
    }

    public void start(LocationTracker.LocationUpdateListener update) {
        start();
        listener = update;
    }


    public void stop() {
        if (isRunning) {
            gps.stop();
            net.stop();
            isRunning = false;
            listener = null;
        }
    }

    public boolean hasLocation() {
        return gps.hasLocation() || net.hasLocation();
    }

    public boolean hasPossiblyStaleLocation() {
        return gps.hasPossiblyStaleLocation() || net.hasPossiblyStaleLocation();
    }

    public Location getLocation() {
        Location result = gps.getLocation();
        if (result == null) {
            result = net.getLocation();
        }
        return result;
    }

    @Override
    public Location getPossiblyStaleLocation() {
        Location result = gps.getPossiblyStaleLocation();
        if (result == null) {
            result = net.getPossiblyStaleLocation();
        }
        return result;
    }


}
