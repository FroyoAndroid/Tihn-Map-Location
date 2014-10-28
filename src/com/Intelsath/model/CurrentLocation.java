package com.Intelsath.model;

import com.google.android.gms.maps.model.LatLng;

public class CurrentLocation {
	
	
	private static double latitude;
	private static double longitude;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		//this.latitude = latitude;
		CurrentLocation.latitude = 22.5667;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		//this.longitude = longitude;
		CurrentLocation.longitude = 88.3667;
	}

	
	
	

}
