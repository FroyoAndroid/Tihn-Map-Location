package com.Intelsath.tihn;

import org.json.JSONObject;

import com.Intelsath.controller.Dispatcher;
import com.Intelsath.controller.GPSTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MapActivity extends FragmentActivity implements OnMarkerDragListener,LocationListener {
	private Context mContext;
	JSONObject json;
	LatLng dragPosition,currentLocation;
	// Google Map
	// https://developers.google.com/maps/documentation/android/ check for
	// google map API v2
	private GoogleMap googleMap;
/**
 * Testing purpose coding goes here for the Location Listener
 */
		// GPSTracker class
	    GPSTracker gps;
	    Location location;
/**
 * End Of the testing variables here		
 */
	// private Dispatcher dispatcher;
	private Button sendRequestBtn;
	// shared preference object
	public SharedPreferences settings;
	public static final String PREFS_NAME = "tihn_pref";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
//		setContentView(R.layout.activity_map_fancy);
		//Getting google
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		// showing Status
		if(status !=ConnectionResult.SUCCESS){
			int requestCode =10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();
		}else{ // Google PLay services are available
			//Getting reference to the Map
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			//Enabling Mylocation layer of Google Map
			//googleMap.setMyLocationEnabled(true);
			
			
			
			//Getting LocationManager 
			LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
			
			//Creating a criteria object to retrieve provider
			Criteria criteria =new Criteria();
			
			//Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);
			
			//Gettiig the current location
			location = locationManager.getLastKnownLocation(provider);
			
			if(location!=null){
				onLocationChanged(location);
			}
			locationManager.requestLocationUpdates(provider, 20000, 0, this);
		}
		mContext = this;
		sendRequestBtn = (Button) findViewById(R.id.btnRequest);
		try {
			currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
			Log.d("TIHN","Current Location"+currentLocation.toString());
			//initializeMap(currentLocation);
			settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		} catch (Exception e) {
			Log.d("TIHN", "Error from MapActivity 1:" + e.getMessage());
		}
		// Function : sendRequestBtn
		// this function binds the onclick listener to the sendrequest Button
			sendRequestBtn.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View arg0) {
					// Main handling code goes here
					try {
						sendRequestBtn.setClickable(false);
						new BackgroundTask().execute();
	
					} catch (Exception e) {
						Log.d("TIHN",
								"Error from MapActivity Task:" + e.getMessage());
					}
				}
		});

	}	

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		stopLocationService();
	}
	
	

	/**
	 * Private class for async task
	 * 
	 */
	private class BackgroundTask extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Void... params) {
			// call dispatcher sendrequest function here
			try {
				Dispatcher dispatcher = new Dispatcher();
				json = dispatcher.sendRequest(
						settings.getString("username", ""),
						settings.getString("mobileNo", ""), "" + currentLocation.latitude,
						"" +currentLocation.longitude);
				Log.d("TIHN", "" + currentLocation.latitude+
						 " : " +currentLocation.longitude);
				Log.d("TIHN", settings.getString("username", "").toString());
			} catch (Exception e) {
				Log.d("TIHN", "Error from MapActivity 3:" + e.getMessage());
				// Toast.makeText(mContext, "Error in application",
				// Toast.LENGTH_LONG).show();
			}
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(mContext, "JSON: " + json, Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		dragPosition = arg0.getPosition();
		//initializeMap(dragPosition);
		currentLocation =dragPosition;
		Log.d("TIHN", "Drag End at Latitude:" + dragPosition.latitude
				+ ",Longitude:" + dragPosition.longitude);
		sendRequestBtn.setClickable(true);
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		Log.d("TIHN","Drag Started");
	}
	@Override
	public void onLocationChanged(Location plocation) {
		try {
			Log.d("TIHN", "Latitude:" + plocation.getLatitude()+ ",Longitude:" + plocation.getLongitude());
			LatLng latlng = new LatLng(plocation.getLatitude(), plocation.getLongitude());
			//Showing the current location in google map
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
			//Zoom in the google map
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
			googleMap.addMarker(new MarkerOptions().title("TIHN")
					.position(latlng).snippet("I am here")
					.draggable(true)
					);
			currentLocation = latlng;
			//initializeMap(currentLocation);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
