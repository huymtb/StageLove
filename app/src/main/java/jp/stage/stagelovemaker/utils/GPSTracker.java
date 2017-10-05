package jp.stage.stagelovemaker.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by congn on 8/4/2017.
 */

public class GPSTracker implements LocationListener {

    public final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    // The minimum distance to change Updates in meters
    private final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 100 meters
    // The minimum time between updates in milliseconds
    private final long MIN_TIME_BW_UPDATES = 1000 * 60 * 60; // 60 minute
    private final Context mContext;
    // Declaring a Location Manager
    protected LocationManager locationManager;
    // flag for GPS status
    boolean isGPSEnabled = false;
    Location location; // location
    //0 Setting
    //1 Request permission
    double latitude; // latitude
    double longitude; // longitude
    int typeDenyLocation = 0;
    GPSTrackerDelegate delegate;

    public GPSTracker(Context context) {
        this.mContext = context;
        typeDenyLocation = 0;
        locationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);

    }

    public Location getLocation() {
        try {
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (isGPSEnabled) {
                // if GPS Enabled get lat/long using GPS Services
                if (location == null) {
                    if (!(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                        typeDenyLocation = 1;
                        return null;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        List<String> providers = locationManager.getProviders(true);
                        for (String provider : providers) {
                            Location l = locationManager.getLastKnownLocation(provider);
                            if (l == null) {
                                continue;
                            }
                            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                                // Found best last known location: %s", l);
                                location = l;
                            }
                        }
                        typeDenyLocation = 2;
                        if (location != null &&
                                (location.getLatitude() != 0 && location.getLongitude() != 0)) {
                            delegate = null;
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            if (locationManager != null) {
                                locationManager.removeUpdates(GPSTracker.this);
                            }
                            return location;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */

    public void setDelegate(GPSTrackerDelegate fragment) {
        delegate = fragment;
    }

    public int getTypeDenyLocation() {
        return typeDenyLocation;
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        if (location != null &&
                (location.getLatitude() != 0 && location.getLongitude() != 0)) {
            return true;
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (delegate != null) {
            this.location = location;
            delegate.setYourLocation(location);
            delegate = null;
            if (locationManager != null) {
                locationManager.removeUpdates(GPSTracker.this);
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public interface GPSTrackerDelegate {
        void setYourLocation(Location location);
    }

}
