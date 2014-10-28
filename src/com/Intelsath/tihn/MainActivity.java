package com.Intelsath.tihn;

import com.Intelsath.controller.ConnectionDetector;
import com.Intelsath.tihn.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {

	private ConnectionDetector connectionDetector;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		connectionDetector = new ConnectionDetector(
				this.getApplicationContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// check for the connection availability
				if (connectionDetector.isConnectingToInternet()) {
					Intent i = new Intent(getApplicationContext(),
							DetailsActivity.class);
					startActivity(i);
					finish();
				}else{
					final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
					dialog.setMessage("Device has no active \n Internet Connection");
					dialog.show();
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog.dismiss();
							finish();
						}
					}, 2000);
					
				}
			}
		}, 3000);
	}

}
