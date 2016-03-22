package com.cyberswift.phe.surface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyberswift.phe.R;
import com.cyberswift.phe.activity.BaseActivity;
import com.cyberswift.phe.dto.PowerConnectionClass;
import com.cyberswift.phe.dto.UserClass;
import com.cyberswift.phe.services.VolleyTaskManager;
import com.cyberswift.phe.utility.AlertDialogCallBack;
import com.cyberswift.phe.utility.SegmentedGroup;
import com.cyberswift.phe.utility.ServerResponseCallback;
import com.cyberswift.phe.utility.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class SurfacePowerConnectionActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener, ServerResponseCallback, LocationListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private TextView tvDateOfConnection;
	private EditText etApplicationDesc, etDepositDesc, etProblemDesc;
	private LinearLayout llApplicationDesc, llDeposit, llProblemDesc;
	private RelativeLayout rlDateOfConnection;
	private RadioButton rbApplication_Yes, rbApplication_No, rbDeposit_Yes, rbDeposit_No, rbProblem_Yes, rbProblem_No;
	private SegmentedGroup sgApplication, sgDeposit, sgProblem;
	private TextView tv_latitude, tv_longitude;
	private VolleyTaskManager volleyTaskManager;
	private Context mContext;
	private DatePickerDialog.OnDateSetListener connectionDateListener;
	private int districtId = 0;
	private String schemeId = "";
	private String isApplication = "";
	private String isDeposit = "";
	private String isProblem = "";
	private boolean isInsert = false;
	private boolean isUpdate = false;
	private boolean isInsertService = false;
	private boolean isUpdateService = false;
	private boolean isFetchDataService = false;
	// ============LOCATION

	private Location mCurrentLocation;
	private LocationRequest mLocationRequest;
	private AlertDialog systemAlertDialog;
	private FusedLocationProviderApi fusedLocationProviderApi;
	private GoogleApiClient mGoogleApiClient;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.surface_power_connection);
		mContext = SurfacePowerConnectionActivity.this;
		initView();
		fetchValuesFromLandingPage();
		fetchPreviousData();

	}

	private void initView() {

		tvDateOfConnection = (TextView) findViewById(R.id.tvDateOfConnection);
		etApplicationDesc = (EditText) findViewById(R.id.etApplicationDesc);
		etDepositDesc = (EditText) findViewById(R.id.etDepositDesc);
		etProblemDesc = (EditText) findViewById(R.id.etProblemDesc);
		llApplicationDesc = (LinearLayout) findViewById(R.id.llApplicationDesc);
		llDeposit = (LinearLayout) findViewById(R.id.llDeposit);
		llProblemDesc = (LinearLayout) findViewById(R.id.llProblemDesc);
		rlDateOfConnection = (RelativeLayout) findViewById(R.id.rlDateOfConnection);
		rbApplication_Yes = (RadioButton) findViewById(R.id.rbApplication_Yes);
		rbApplication_No = (RadioButton) findViewById(R.id.rbApplication_No);
		rbDeposit_Yes = (RadioButton) findViewById(R.id.rbDeposit_Yes);
		rbDeposit_No = (RadioButton) findViewById(R.id.rbDeposit_No);
		rbProblem_Yes = (RadioButton) findViewById(R.id.rbProblem_Yes);
		rbProblem_No = (RadioButton) findViewById(R.id.rbProblem_No);
		sgApplication = (SegmentedGroup) findViewById(R.id.sgApplication);
		sgDeposit = (SegmentedGroup) findViewById(R.id.sgDeposit);
		sgProblem = (SegmentedGroup) findViewById(R.id.sgProblem);

		sgApplication.setOnCheckedChangeListener(this);
		sgDeposit.setOnCheckedChangeListener(this);
		sgProblem.setOnCheckedChangeListener(this);
		rlDateOfConnection.setOnClickListener(this);
		tvDateOfConnection.setText("");

		connectionDateListener = new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvDateOfConnection.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};

		// ============LOCATION

		tv_latitude = (TextView) findViewById(R.id.tv_latitude);
		tv_longitude = (TextView) findViewById(R.id.tv_longitude);

		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);

		volleyTaskManager = new VolleyTaskManager(mContext);

	}

	private void fetchValuesFromLandingPage() {

		Intent passedIntent = getIntent();
		districtId = passedIntent.getIntExtra("districtId", 0);
		schemeId = passedIntent.getStringExtra("schemeId");

		System.out.println("Scheme ID: " + schemeId + " District ID: " + districtId);

	}

	public void onLogoutClicked(View v) {

	}

	public void onPostClicked(View v) {

		boolean isInputValid = checkFormDataInput();
		Log.v("TAG", "IS VALID " + isInputValid);
		if (Util.checkConnectivity(mContext) && isInputValid) {

			UserClass user = Util.fetchUserClass(mContext);

			HashMap<String, String> requestMap = new HashMap<String, String>();

			requestMap.put("DISTRICT_ID", String.valueOf(districtId));
			requestMap.put("SCHEME_ID", schemeId);
			requestMap.put("APPLICATION_TO_WBSEDCL_ISDONE", isApplication);
			requestMap.put("APPLICATION_TO_WBSEDCL_IFNO_DESCRIPTION", etApplicationDesc.getText().toString().trim());
			requestMap.put("DEPOSIT_OF_FEES_ISDONE", isDeposit);
			requestMap.put("DEPOSIT_OF_FEES_IFNO_DESCRIPTION", etDepositDesc.getText().toString().trim());
			requestMap.put("WAY_LEAVE_PROBLEM_IF_ANY_ISDONE", isProblem);
			requestMap.put("WAY_LEAVE_PROBLEM_IF_ANY_IFNO_DESCRIPTION", etProblemDesc.getText().toString().trim());
			requestMap.put("DATE_OF_CONNECTION", tvDateOfConnection.getText().toString().trim());
			requestMap.put("LAT", tv_latitude.getText().toString().trim());
			requestMap.put("LON", tv_longitude.getText().toString().trim());
			requestMap.put("CREATED_BY_ID", user.getDatabaseId());
			if (isInsert) {

				isInsertService = true;
				Log.v("SurfaceLand", ">>----------IS INSERT >>------------");
				volleyTaskManager.doPostInsertPowerConnection(requestMap);

			} else if (isUpdate) {

				isUpdateService = true;
				Log.v("SurfaceLand", ">>----------IS UPDATE >>------------");
				requestMap.put("ID", user.getSurfacePowerConnectionUpdateId());
				volleyTaskManager.doPostUpdatePowerConnection(requestMap);

			}

		} else if (!Util.checkConnectivity(mContext))

			Toast.makeText(mContext, "No Network Connection.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {

		case R.id.rbApplication_Yes:

			isApplication = "true";
			onApplication_YesClick();
			break;

		case R.id.rbApplication_No:

			isApplication = "false";
			onApplication_NoClick();
			break;

		case R.id.rbDeposit_Yes:

			isDeposit = "true";
			onDeposit_YesClick();
			break;

		case R.id.rbDeposit_No:

			isDeposit = "false";
			onDeposit_NoClick();
			break;

		case R.id.rbProblem_Yes:

			isProblem = "true";
			onProblem_YesClick();
			break;

		case R.id.rbProblem_No:

			isProblem = "false";
			onProblem_NoClick();
			break;

		default:
			break;
		}
	}

	private void onApplication_YesClick() {

		if (llApplicationDesc.getVisibility() == View.VISIBLE)
			llApplicationDesc.setVisibility(View.GONE);
		etApplicationDesc.setText("");
	}

	private void onApplication_NoClick() {

		if (llApplicationDesc.getVisibility() == View.GONE)
			llApplicationDesc.setVisibility(View.VISIBLE);
	}

	private void onDeposit_YesClick() {

		if (llDeposit.getVisibility() == View.VISIBLE)
			llDeposit.setVisibility(View.GONE);
		etDepositDesc.setText("");

	}

	private void onDeposit_NoClick() {

		if (llDeposit.getVisibility() == View.GONE)
			llDeposit.setVisibility(View.VISIBLE);

	}

	private void onProblem_YesClick() {

		if (llProblemDesc.getVisibility() == View.VISIBLE)
			llProblemDesc.setVisibility(View.GONE);
		etProblemDesc.setText("");

	}

	private void onProblem_NoClick() {

		if (llProblemDesc.getVisibility() == View.GONE)
			llProblemDesc.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {

		Calendar currentDate = Util.getCurrentDate();
		switch (v.getId()) {

		case R.id.rlDateOfConnection:

			new DatePickerDialog(this, connectionDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		default:
			break;
		}
	}

	private boolean checkFormDataInput() {

		boolean isValid = false;

		if (isApplication.trim().equalsIgnoreCase("") || isDeposit.trim().equalsIgnoreCase("") || isProblem.trim().equalsIgnoreCase("")
				|| tvDateOfConnection.getText().toString().trim().equalsIgnoreCase("")) {
			isValid = false;
			Toast.makeText(mContext, "Please select all fields.", Toast.LENGTH_SHORT).show();
		} else {

			if (isApplication.trim().equalsIgnoreCase("false")) {
				if (etApplicationDesc.getText().toString().trim().isEmpty() || etApplicationDesc.getText().toString().trim().equalsIgnoreCase("")) {

					System.out.println("--------->> etIdentificationDesc Description is empty  >>------------");

					Toast.makeText(mContext, "Please enter the description.", Toast.LENGTH_SHORT).show();
					etApplicationDesc.requestFocus();
					isValid = false;

				} else {
					isValid = true;
				}

			} else {
				isValid = true;
			}
			if (isDeposit.trim().equalsIgnoreCase("false")) {
				if (etDepositDesc.getText().toString().trim().isEmpty() || etDepositDesc.getText().toString().trim().equalsIgnoreCase("")) {

					System.out.println("--------->> etIdentificationDesc Description is empty  >>------------");

					Toast.makeText(mContext, "Please enter the description.", Toast.LENGTH_SHORT).show();
					etDepositDesc.requestFocus();
					isValid = false;

				} else {
					isValid = true;

				}
			} else {
				isValid = true;
			}
			if (isProblem.trim().equalsIgnoreCase("false")) {
				if (etProblemDesc.getText().toString().trim().isEmpty() || etProblemDesc.getText().toString().trim().equalsIgnoreCase("")) {

					System.out.println("--------->> etIdentificationDesc Description is empty  >>------------");

					Toast.makeText(mContext, "Please enter the description.", Toast.LENGTH_SHORT).show();
					etProblemDesc.requestFocus();
					isValid = false;

				} else {
					isValid = true;
				}
			} else {
				isValid = true;
			}
			if (tvDateOfConnection.getText().toString().trim().equalsIgnoreCase("")) {
				isValid = false;
				Toast.makeText(mContext, "Please provide the date of collection.", Toast.LENGTH_SHORT).show();
			} else {
				isValid = true;
			}
		}
		return isValid;

	}

	@Override
	public void onSuccess(JSONObject resultJsonObject) {

		volleyTaskManager.hideProgressDialog();

		Log.v("On Success", "" + resultJsonObject.toString());
		if (isInsertService) {

			isInsertService = false;
			if (resultJsonObject.toString() != null && !resultJsonObject.toString().trim().isEmpty()) {

				try {
					UserClass user = Util.fetchUserClass(mContext);

					String result = new JSONObject(resultJsonObject.optString("SaveSurfacePowerConnectionResult")).optString("RES");

					Log.v("TAG", "" + result);
					if (!result.equalsIgnoreCase("0")) {
						user.setSurfacePowerConnectionUpdateId(result);
						Util.saveUserClass(mContext, user);
						onSuccessfulInsertUpdate("Form Posted Successfully.");
					} else
						Toast.makeText(mContext, " Request failed. ", Toast.LENGTH_SHORT).show();

				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(mContext, " Request failed. ", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(mContext, " Request failed. ", Toast.LENGTH_SHORT).show();
			}
		}
		if (isUpdateService) {

			isUpdateService = false;

			if (resultJsonObject.toString() != null && !resultJsonObject.toString().trim().isEmpty()) {

				try {
					UserClass user = Util.fetchUserClass(mContext);

					String result = new JSONObject(resultJsonObject.optString("UpdateSurfacePowerConnectionResult")).optString("RES");

					if (!result.equalsIgnoreCase("0")) {

						user.setSurfacePowerConnectionUpdateId(result);
						Util.saveUserClass(mContext, user);
						onSuccessfulInsertUpdate("Form Updated Successfully.");

					} else

						Toast.makeText(mContext, " Request failed. ", Toast.LENGTH_SHORT).show();

				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(mContext, " Request failed. ", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(mContext, " Request failed. ", Toast.LENGTH_SHORT).show();
			}

		}
		if (isFetchDataService) {
			isFetchDataService = false;

			parseInitialFetchedData(resultJsonObject);
		}

	}

	@Override
	public void onError() {

	}

	private void onSuccessfulInsertUpdate(String message) {

		Util.showCallBackMessageWithOk(mContext, message, new AlertDialogCallBack() {

			@Override
			public void onSubmit() {
				// sgIdenticationLand.clearCheck();
				// sgCommence.clearCheck();
				// sgHinderances.clearCheck();
				// isIdentification = "";
				// isCommence = "";
				// isHinderance = "";
				// etIdentificationDesc.setText("");
				// etCommenceDesc.setText("");
				// etHinderancesDesc.setText("");
				// tvDateOfAcquisition.setText("");
				// llIdentificationDesc.setVisibility(View.GONE);
				// llCommence.setVisibility(View.GONE);
				// llHinderancesDesc.setVisibility(View.GONE);
			}

			@Override
			public void onCancel() {

			}
		});

	}

	private void fetchPreviousData() {

		if (Util.checkConnectivity(mContext)) {

			fetchSurfacePowerAcquisition(schemeId);
		} else {
			Util.showCallBackMessageWithOk(mContext, "No Intenet Connection.", new AlertDialogCallBack() {
				@Override
				public void onSubmit() {

				}

				@Override
				public void onCancel() {

				}
			});
		}

	}

	private void fetchSurfacePowerAcquisition(String schemeId) {

		isFetchDataService = true;

		String value = "?scheme_id=" + schemeId;

		volleyTaskManager.doGetSurfacePowerConnection(value);
	}

	private void parseInitialFetchedData(JSONObject resultJsonObject) {

		try {

			JSONArray resultJSONArray = new JSONArray(resultJsonObject.optString("GetSurfacePowerConnectionResult"));

			if (resultJSONArray.length() > 0) {
				ArrayList<PowerConnectionClass> powerConnectionList = new ArrayList<PowerConnectionClass>();

				for (int i = 0; i < resultJSONArray.length(); i++) {

					JSONObject powerConnectionJSONObject = resultJSONArray.getJSONObject(i);
					PowerConnectionClass powerConnection = new PowerConnectionClass();
					powerConnection.setAPPLICATION_DESCRIPTION(powerConnectionJSONObject.optString("APPLICATION_TO_WBSEDCL_IFNO_DESCRIPTION"));
					powerConnection.setAPPLICATION_ISDONE(powerConnectionJSONObject.optString("APPLICATION_TO_WBSEDCL_ISDONE"));
					powerConnection.setCREATED_BY_ID(powerConnectionJSONObject.optString("CREATED_BY_ID"));
					powerConnection.setDATE_OF_CONNECTION(powerConnectionJSONObject.optString("DATE_OF_CONNECTION"));
					powerConnection.setDISTRICT_ID(powerConnectionJSONObject.optString("DISTRICT_ID"));
					powerConnection.setDEPOSIT_ISDONE(powerConnectionJSONObject.optString("DEPOSIT_OF_FEES_ISDONE"));
					powerConnection.setDEPOSIT_DESCRIPTION(powerConnectionJSONObject.optString("DEPOSIT_OF_FEES_IFNO_DESCRIPTION"));
					powerConnection.setID(powerConnectionJSONObject.optString("ID"));
					Log.v("TAG", "Problem description " + powerConnectionJSONObject.optString("WAY_LEAVE_PROBLEM_IF_ANY_IFNO_DESCRIPTION"));
					powerConnection.setPROBLEM_DESCRIPTION(powerConnectionJSONObject.optString("WAY_LEAVE_PROBLEM_IF_ANY_IFNO_DESCRIPTION"));
					powerConnection.setPROBLEM_ISDONE(powerConnectionJSONObject.optString("WAY_LEAVE_PROBLEM_IF_ANY_ISDONE"));
					powerConnection.setSCHEME_ID(powerConnectionJSONObject.optString("SCHEME_ID"));

					UserClass user = Util.fetchUserClass(mContext);
					user.setSurfacePowerConnectionUpdateId(powerConnection.getID());
					Util.saveUserClass(mContext, user);

					powerConnectionList.add(powerConnection);

				}

				isInsert = false;

				isUpdate = true;

				populateForm(powerConnectionList);
			} else {

				isInsert = true;
				isUpdate = false;
			}

		} catch (Exception e) {

		}
	}

	private void populateForm(ArrayList<PowerConnectionClass> powerConnectionList) {

		for (PowerConnectionClass powerConnectionClass : powerConnectionList) {

			if (powerConnectionClass.getAPPLICATION_ISDONE().trim().equalsIgnoreCase("False")) {

				isApplication = "false";
				rbApplication_No.setChecked(true);
				etApplicationDesc.setText(powerConnectionClass.getAPPLICATION_DESCRIPTION());
				onApplication_NoClick();
			} else {

				isApplication = "true";
				rbApplication_Yes.setChecked(true);
				onApplication_YesClick();
			}

			if (powerConnectionClass.getDEPOSIT_ISDONE().trim().equalsIgnoreCase("False")) {

				isDeposit = "false";
				rbDeposit_No.setChecked(true);
				etDepositDesc.setText(powerConnectionClass.getDEPOSIT_DESCRIPTION());
				onDeposit_NoClick();
			} else {

				isDeposit = "true";
				rbDeposit_Yes.setChecked(true);
				onDeposit_YesClick();
			}

			if (powerConnectionClass.getPROBLEM_ISDONE().trim().equalsIgnoreCase("True")) {

				isProblem = "true";
				rbProblem_Yes.setChecked(true);
				onProblem_YesClick();
			} else {

				isProblem = "false";
				etProblemDesc.setText(powerConnectionClass.getPROBLEM_DESCRIPTION());
				rbProblem_No.setChecked(true);
				onProblem_NoClick();
			}

			if (powerConnectionClass.getDATE_OF_CONNECTION() != null) {

				String[] dateArray = powerConnectionClass.getDATE_OF_CONNECTION().split(" ");
				tvDateOfConnection.setText(dateArray[0]);
			}

		}
	}

	public void onLocationClicked(View v) {
		mCurrentLocation = null; // Setting present location null, so that new
		// location value can be fetched on each
		// button click.
		checkingLocation();

	}

	private void checkingLocation() {

		// if (Util.checkConnectivity(EntryFormActivity.this)) {

		if (!isGooglePlayServicesAvailable()) {
			int requestCode = 10;
			int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, SurfacePowerConnectionActivity.this, requestCode);
			dialog.show();
		} else {
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				Log.e("EntryFormActivity", "NO LOCATION FOUND!");
				// Util.buildAlertMessageNoGps( StartJourneyActivity.this );
				if (systemAlertDialog == null) {
					systemAlertDialog = Util.showSettingsAlert(getApplicationContext(), systemAlertDialog);
				} else if (!systemAlertDialog.isShowing()) {
					systemAlertDialog = Util.showSettingsAlert(getApplicationContext(), systemAlertDialog);
				}
			} else {
				Log.v("GPS Connection Found:", "true");
				if (mCurrentLocation == null) {
					mProgressDialog.setMessage("Fetching present location...");
					mProgressDialog.setCancelable(true);
					showProgressDialog();
					createLocationRequest();
				} else {
					// Toast.makeText(EntryFormActivity.this,
					// "Location already found.", Toast.LENGTH_SHORT).show();
				}
			}
		}
		// } else {
		// // Util.showMessageWithOk(EntryFormActivity.this,
		// getString(R.string.no_internet));
		// Log.v("Connection Status:", "No internet");
		// }

	}

	protected void createLocationRequest() {
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setNumUpdates(1); // TODO New line added
		mLocationRequest.setInterval(5000); // TODO New line added
		mLocationRequest.setFastestInterval(1000); // TODO New line added
		fusedLocationProviderApi = LocationServices.FusedLocationApi;
		mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
				.build();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		Log.d("EntryFormActivity", "Connection failed: " + connectionResult.toString());
		Toast.makeText(mContext, "Connection failed: " + connectionResult.toString(), Toast.LENGTH_LONG).show();
		hideProgressDialog();

	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.d("EntryFormActivity", "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
		startLocationUpdates();

	}

	protected void startLocationUpdates() {
		// PendingResult<Status> pendingResult =
		// LocationServices.FusedLocationApi.requestLocationUpdates(
		// mGoogleApiClient, mLocationRequest, this);
		fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		Log.d("EntryFormActivity", "Location update started ..............: ");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		hideProgressDialog();
		Log.d("EntryFormActivity", "Firing onLocationChanged..............................................");
		mCurrentLocation = location;
		// mLastUpdateTime = postFormater.format(new Date());
		updateUI();

	}

	private void updateUI() {

		Log.d("EntryFormActivity", "UI update initiated .............");

		SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		if (null != mCurrentLocation) {
			String lat = String.valueOf(mCurrentLocation.getLatitude());
			String lng = String.valueOf(mCurrentLocation.getLongitude());
			Log.v("LOCATION", "At Time: " + postFormater.format(new Date()) + "\n" + "Latitude: " + lat + "\n" + "Longitude: " + lng + "\n" + "Accuracy: "
					+ mCurrentLocation.getAccuracy() + "\n" + "Provider: " + mCurrentLocation.getProvider());

			tv_latitude.setText(lat);
			tv_longitude.setText(lng);
		} else {
			Log.d("EntryFormActivity", "location is null ...............");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCurrentLocation != null) {
			stopLocationUpdates();
		}
		Util.trimCache(SurfacePowerConnectionActivity.this);
	}

	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		Log.d("EntryFormActivity", "Location update stopped .......................");
	}

	private void showProgressDialog() {
		if (!mProgressDialog.isShowing())
			mProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}
}
