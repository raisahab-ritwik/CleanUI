package com.cyberswift.phe.activity;

import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cyberswift.phe.R;
import com.cyberswift.phe.dto.UserClass;
import com.cyberswift.phe.services.SpeedTestService;
import com.cyberswift.phe.services.VolleyTaskManager;
import com.cyberswift.phe.utility.ServerResponseCallback;
import com.cyberswift.phe.utility.Util;

public class LoginActivity extends Activity implements ServerResponseCallback {

	private EditText et_userName, et_password;
	private CheckBox chkbx_rememberMe;
	private UserClass user = new UserClass();
	private ProgressDialog mProgressDialog;

	private VolleyTaskManager volleyTaskManager;

	MyReceiver myReceiver;
	private WakeLock mWakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "com.schoolentry.imis.LoginActivity");
		mWakeLock.acquire();

		initview();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v("called", "onResume");
	}

	private void initview() {
		et_userName = (EditText) findViewById(R.id.et_userName);
		et_password = (EditText) findViewById(R.id.et_password);

		chkbx_rememberMe = (CheckBox) findViewById(R.id.chkbx_rememberMe);

		if (Util.fetchUserClass(LoginActivity.this) != null) {
			user = Util.fetchUserClass(LoginActivity.this);
		}

		if (user.getIsRemember()) {
			et_userName.setText(user.getUserId());
			et_password.setText(user.getPassword());
			chkbx_rememberMe.setChecked(user.getIsRemember());
		}

		volleyTaskManager = new VolleyTaskManager(LoginActivity.this);

		mProgressDialog = new ProgressDialog(LoginActivity.this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Please wait...");
	}

	// TODO Remove Comments
	public void onRegistrationClicked(View mView) {
		// Intent intent = new Intent(LoginActivity.this,
		// RegistrationActivity.class);
		// startActivity(intent);
	}

	public void onSignInClicked(View mView) {

		if (Util.checkConnectivity(LoginActivity.this)) {

			if (et_userName.getText().toString().length() == 0 || et_password.getText().toString().length() == 0) {
				Util.showMessageWithOk(LoginActivity.this, "Please fill all the required fields!");
			} else if (et_password.getText().toString().length() < 4) {
				Util.showMessageWithOk(LoginActivity.this, "Password should be at least 4 characters long!");
			} else {
				myReceiver = new MyReceiver();
				IntentFilter intentFilter = new IntentFilter();
				intentFilter.addAction(SpeedTestService.MY_ACTION);
				registerReceiver(myReceiver, intentFilter);

				startService(new Intent(this, SpeedTestService.class));
				mProgressDialog.setMessage("Checking internet speed...");
				showProgressDialog();

			}
		} else {
			Toast.makeText(LoginActivity.this, "No Internet Access.", Toast.LENGTH_SHORT).show();
		}

	}

	// --> For checking speed
	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("MyReceiver", "MyReceiver");
			// TODO Receiving Data from DataSyncService
			// hideProgressDialog();
			@SuppressWarnings("unused")
			boolean isStrongNetwork = intent.getBooleanExtra("NETWORK_CHECK", false);

			unregisterReceiver(myReceiver);
			hideProgressDialog();
			loginWebserviceCalling();

		}
	}

	private void loginWebserviceCalling() {
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("User_Id", et_userName.getText().toString());
		paramsMap.put("Password", et_password.getText().toString());

		volleyTaskManager.doLogin(paramsMap, false);
	}

	private void showProgressDialog() {
		if (!mProgressDialog.isShowing())
			mProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}

	@Override
	public void onSuccess(JSONObject resultJsonObject) {

		Log.v("resultJsonObject", resultJsonObject.toString());

		if (resultJsonObject.optString("Status").equalsIgnoreCase("1")) {
			user.setName(resultJsonObject.optString("Name"));
			user.setUserId(resultJsonObject.optString("User_Id"));
			user.setDatabaseId(resultJsonObject.optString("Id"));
			user.setPassword(et_password.getText().toString());
			user.setIsRemember(chkbx_rememberMe.isChecked());
			// userClass.setIsLoggedin(true);
			Util.saveUserClass(LoginActivity.this, user);
			openHomeActivity();
		} else if (resultJsonObject.optString("Status").equalsIgnoreCase("0") || resultJsonObject.optString("Status").equalsIgnoreCase("-1")) {
			Toast.makeText(LoginActivity.this, resultJsonObject.optString("Message"), Toast.LENGTH_SHORT).show();
		}
		volleyTaskManager.hideProgressDialog();
	}

	// TODO Remove Comments
	private void openHomeActivity() {
		Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub

	}

	@SuppressLint("Wakelock")
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWakeLock.release();
	}
}
