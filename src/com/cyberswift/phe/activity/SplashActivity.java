package com.cyberswift.phe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.cyberswift.phe.R;
import com.cyberswift.phe.utility.Util;

public class SplashActivity extends Activity {

	// private TextView tv_splash;
	// private Typeface custom_font;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		// Log.v(null, null);
		initview();
		new SplashTimerTask().execute();
	}

	private void initview() {
		// tv_splash = (TextView) findViewById(R.id.tv_splash);

		// custom_font = Typeface.createFromAsset(getAssets(),
		// "fonts/SCRIPTBL.TTF");
		//
		// tv_splash.setTypeface(custom_font);
	}

	private class SplashTimerTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(2345);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (Util.fetchUserClass(SplashActivity.this) == null) {
				openLoginActivity();
			} else {
				Log.e("User logged in:", Util.fetchUserClass(SplashActivity.this).getIsLoggedin() + "");
				if (Util.fetchUserClass(SplashActivity.this).getIsLoggedin()) {
					openHomeActivity();
				} else {
					openLoginActivity();
				}
			}

		}
	}

	public void openLoginActivity() {
		Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	// TODO Remove comments
	private void openHomeActivity() {
		// Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		// startActivity(intent);
		// finish();
	}
}
