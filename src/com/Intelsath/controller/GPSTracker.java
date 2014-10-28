package com.Intelsath.controller;
import com.Intelsath.model.CurrentLocation;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GPSTracker extends Service implements 
		android.location.LocationListener {
	//Application context object
	private final Context mContext;
	private CurrentLocation m_currentLocation;
	// flag for gps status
	boolean isGPSEnabled = false;
	// flag for network status
	boolean isNetworkEnabled = false;
	// flag for GPS status
	boolean canGetLocation = false;
	// Location
	Location location;
	// Latitude
	double latitude;
	// Longitude
	double longitude;

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute;

	// Declaring a Location Manager'`
	protected LocationManager locationManager;

	//Constructor
	public GPSTracker(Context context) {
		this.mContext = context;
		m_currentLocation = new CurrentLocation();
		//getLocation();
	}
	/**
     * Function to get the location from the available location providers
     * @return Location Object
     * */
	public Location getLocation() {
		// TODO Auto-generated method stub
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);
			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				Log.d("TIHN","No network provider is enabled");
				showSettingsAlert();
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network","Network");
					if(locationManager !=null){
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if(location != null ){
							m_currentLocation.setLatitude(location.getLatitude());
							m_currentLocation.setLongitude(location.getLongitude());
//							latitude =location.getLatitude();
//							longitude =location.getLongitude();
							Log.d("TIHN","Location:"+latitude+", "+latitude);
						}
					}
				}
				 // if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                            	m_currentLocation.setLatitude(location.getLatitude());
    							m_currentLocation.setLongitude(location.getLongitude());
                                Log.d("TIHN","Location:"+latitude+", "+latitude);
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            Log.d("TIHN","Error from GPS tracker:"+e.getMessage());
        }
 
        return location;
			
	}
	/**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
     
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will launch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
      
        // Setting Dialog Title
        alertDialog.setTitle("Enable GPS Settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		m_currentLocation.setLatitude(location.getLatitude());
		m_currentLocation.setLongitude(location.getLongitude());
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "GPS was disabled", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "GPS was enabled", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d("TIHN","Service Started");
		return null;
	}
	

}
