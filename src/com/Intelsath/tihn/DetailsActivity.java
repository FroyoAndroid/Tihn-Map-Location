package com.Intelsath.tihn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DetailsActivity extends Activity {
	public static final String PREFS_NAME = "tihn_pref";
	EditText username, mobileNo;
	Button btn_goAhead;
	// shared preference object
	public SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		username = (EditText) findViewById(R.id.editText1);
		mobileNo = (EditText) findViewById(R.id.editText2);
		btn_goAhead = (Button) findViewById(R.id.button1);
		settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		username.setText(settings.getString("username", "").toString());
		mobileNo.setText(settings.getString("mobileNo", "").toString());
		Log.d("username", settings.getString("username", ""));
		Log.d("mobile", settings.getString("mobileNo", ""));
		btn_goAhead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (username.getText().toString().equals("")
						|| mobileNo.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(),
							"No field can't be left empty", Toast.LENGTH_LONG)
							.show();
				} else {
					// We need an Editor object to make preference changes.
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("username", username.getText().toString());
					editor.putString("mobileNo", mobileNo.getText().toString());
					Log.d("TIHN",username.getText()+ " "+mobileNo.getText());
					// Commit the edits!
					editor.clear();
					editor.commit();
					Intent i = new Intent(getApplicationContext(),MapActivity.class);
					startActivity(i);
					finish();
			}

			}
		});

	}
	
}
