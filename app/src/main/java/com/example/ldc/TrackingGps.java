package com.example.ldc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class TrackingGps implements LocationListener {

    public interface LocationChanged{
        void locationChanged(Location location);
    }

    Context context;
    private LocationChanged mListener;

    public TrackingGps(Context c){
        context = c;
        mListener = null;
    }

    public TrackingGps(Context c, LocationChanged listener){
        context = c;
        mListener = listener;
    }

    public Location getLocation(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context,"PLEASE GIVE THE PERMITION",Toast.LENGTH_LONG).show();
            return null;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean enableGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(enableGPS){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,4500,1,this);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;

        }else {
            Toast.makeText(context, "PLEASE TURN ON THE GPS", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {
       //Toast.makeText(context, "Location changed", Toast.LENGTH_SHORT).show();
        if(mListener != null)
            mListener.locationChanged(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
       // getLocation();

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
