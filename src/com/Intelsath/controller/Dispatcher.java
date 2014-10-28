package com.Intelsath.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;



public class Dispatcher  {
	String APIUrl ="http://www.intelsath.com/tihn/webservice/index.php";
	JSON_Parser parser;
	public Dispatcher(){
		parser = new JSON_Parser();
	}
	
	
	public JSONObject sendRequest(String name,String mobile,String latitude,String longitude ){
		//Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("action", "INSERT_DATA"));
				params.add(new BasicNameValuePair("name", name));
				params.add(new BasicNameValuePair("mobile", mobile));
				params.add(new BasicNameValuePair("lat", latitude));
				params.add(new BasicNameValuePair("long", longitude));
				//getting JSON Object
				JSONObject json = parser.getJSONFromUrl(APIUrl, params);
				
				//return json
				return json;
	}



}
