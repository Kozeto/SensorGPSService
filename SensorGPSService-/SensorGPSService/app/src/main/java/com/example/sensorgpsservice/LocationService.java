package com.example.sensorgpsservice;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class LocationService extends Service implements LocationListener {
    private static final int TODO = 0;
    private static Location lastLocation;
    private LocationManager locationManager;
    private boolean gpsEnabled;
    private boolean networkEnabled;
    // we are making this variable global because we don't want to allocate memory every time
    // we call the function getLastLocationInfo()
    // same thing is implemented in other listeners also

    private String gpsLocationProvider = LocationManager.GPS_PROVIDER;
    private String networkLocationProvider = LocationManager.NETWORK_PROVIDER;

    public int onStartCommand(Intent intent, int flags, int startId) {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //checking which location providers are enabled and which are not
        gpsEnabled = locationManager.isProviderEnabled(gpsLocationProvider);
        networkEnabled = locationManager.isProviderEnabled(networkLocationProvider);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }
        locationManager.requestLocationUpdates(gpsLocationProvider, 0, 0, this);
        lastLocation = locationManager.getLastKnownLocation(gpsLocationProvider);
        // if gps has not given any location, then try the network Location provider
        if(lastLocation == null){
            // first, check whether the network is enabled or not.
            if(networkEnabled){
                lastLocation = locationManager.getLastKnownLocation(networkLocationProvider);
            }
        }
        return Service.START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.d("GPS", "location changed at "+ Calendar.getInstance().getTime());
        lastLocation = location;
        if(this.lastLocation != null ){
            Constants.latitude = location.getLatitude();
            Constants.longitude = location.getLongitude();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void unregisterListener() {
        if(locationManager != null)
            locationManager.removeUpdates(this);
    }

    public boolean isGpsEnabled() {
        return gpsEnabled;
    }

    public void setGpsEnabled(boolean gpsEnabled) {
        this.gpsEnabled = gpsEnabled;
    }

    public boolean isNetworkEnabled() {
        return networkEnabled;
    }

    public void setNetworkEnabled(boolean networkEnabled) {
        this.networkEnabled = networkEnabled;
    }

    public static Location getLastLocation(){
        return lastLocation;
    }

    public static void setLastLocation(Location newLoc){
        lastLocation = newLoc;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

