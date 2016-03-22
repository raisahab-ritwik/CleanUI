package com.cyberswift.phe.sub_surface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.cyberswift.phe.dto.LandAcquisitionClass;
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

public class SubSurfaceLandAcquisitionActivity extends Activity implements OnCheckedChangeListener, OnClickListener, ServerResponseCallback, LocationListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private SegmentedGroup sgIdenticationLand, sgCommence, sgHinderances;
	private RadioButton rbIdentification_Yes, rbIdentification_No, rbCommence_Yes, rbCommence_No, rbHinderances_Yes, rbHinderances_No;
	private EditText etIdentificationDesc, etCommenceDesc, etHinderancesDesc;
	private TextView tvDateOfAcquisition;
	private RelativeLayout rlDateOfAcquisition;
	private LinearLayout llIdentificationDesc, llCommence, llHinderancesDesc;
	private TextView tv_latitude, tv_longitude;
	private Context mContext;
	private DatePickerDialog.OnDateSetListener acquisitionDateListener;
	private String isIdentification = "";
	private String isCommence = "";
	private String isHinderance = "";
	private int districtId = 0;
	private String schemeId = "";
	private VolleyTaskManager volleyTaskManager;
	private boolean isInsertService = false;
	private boolean isUpdateService = false;
	private boolean isFetchDataService = false;
	private boolean isInsert = false;
	private boolean isUpdate = false;

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
		setContentView(R.layout.surface_land_acquisition_layout);
		mContext = SubSurfaceLandAcquisitionActivity.this;
		initView();
		fetchValuesFromLandingPage();
		fetchPreviousData();
	}

	private void initView() {

		sgIdenticationLand = (SegmentedGroup) findViewById(R.id.sgIdenticationLand);
		sgCommence = (SegmentedGroup) findViewById(R.id.sgCommence);
		sgHinderances = (SegmentedGroup) findViewById(R.id.sgHinderances);
		rbIdentification_Yes = (RadioButton) findViewById(R.id.rbIdentification_Yes);
		rbIdentification_No = (RadioButton) findViewById(R.id.rbIdentification_No);
		rbCommence_Yes = (RadioButton) findViewById(R.id.rbCommence_Yes);
		rbCommence_No = (RadioButton) findViewById(R.id.rbCommence_No);
		rbHinderances_Yes = (RadioButton) findViewById(R.id.rbHinderances_Yes);
		rbHinderances_No = (RadioButton) findViewById(R.id.rbHinderances_No);
		etIdentificationDesc = (EditText) findViewById(R.id.etIdentificationDesc);
		etCommenceDesc = (EditText) findViewById(R.id.etCommenceDesc);
		etHinderancesDesc = (EditText) findViewById(R.id.etHinderancesDesc);
		llIdentificationDesc = (LinearLayout) findViewById(R.id.llIdentificationDesc);
		llCommence = (LinearLayout) findViewById(R.id.llCommence);
		llHinderancesDesc = (LinearLayout) findViewById(R.id.llHinderancesDesc);
		tvDateOfAcquisition = (TextView) findViewById(R.id.tvDateOfAcquisition);
		rlDateOfAcquisition = (RelativeLayout) findViewById(R.id.rlDateOfAcquisition);

		sgIdenticationLand.setOnCheckedChangeListener(this);
		sgCommence.setOnCheckedChangeListener(this);
		sgHinderances.setOnCheckedChangeListener(this);
		rlDateOfAcquisition.setOnClickListener(this);
		tvDateOfAcquisition.setText("");

		acquisitionDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvDateOfAcquisition.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
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

	private void fetchPreviousData() {

		if (Util.checkConnectivity(mContext)) {

			fetchSurfaceLandAcquisition(schemeId);
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

		System.out.println("Calander: " + tvDateOfAcquisition.getText().toString());
		Log.v("IS VALID", "" + isInputValid);

		if (Util.checkConnectivity(mContext) && isInputValid) {

			UserClass user = Util.fetchUserClass(mContext);

			HashMap<String, String> requestMap = new HashMap<String, String>();

			requestMap.put("DISTRICT_ID", String.valueOf(districtId));
			requestMap.put("SCHEME_ID", schemeId);
			requestMap.put("IDENTIFICATION_OF_LAND_ISDONE", isIdentification);
			requestMap.put("IDENTIFICATION_OF_LAND_IFNO_DESCRIPTION", etIdentificationDesc.getText().toString().trim());
			requestMap.put("COMMENCEMENT_OF_LAND_ACQUISITION_PROCESS_ISDONE", isCommence);
			requestMap.put("COMMENCEMENT_OF_LAND_ACQUISITION_PROCESS_IFNO_DESCRIPTION", etCommenceDesc.getText().toString().trim());
			requestMap.put("HINDRANCES", isHinderance);
			requestMap.put("HINDRANCES_IFYES_DESCRIPTION", etHinderancesDesc.getText().toString().trim());
			requestMap.put("DATE_OF_ACQUISITION", tvDateOfAcquisition.getText().toString().trim());
			requestMap.put("LAT", tv_latitude.getText().toString().trim());
			requestMap.put("LON", tv_longitude.getText().toString().trim());
			requestMap.put("CREATED_BY_ID", user.getDatabaseId());
			if (isInsert) {

				isInsertService = true;
				Log.v("SurfaceLand", ">>----------IS INSERT >>------------");
				volleyTaskManager.doPostSubSurfaceInsertLandAcquisition(requestMap);

			} else if (isUpdate) {

				isUpdateService = true;
				Log.v("SurfaceLand", ">>----------IS UPDATE >>------------");
				requestMap.put("ID", user.getSubSurfaceLandAcquisitionUpdateId());
				volleyTaskManager.doPostSubSurfaceUpdateLandAcquisition(requestMap);

			}
		} else if (!Util.checkConnectivity(mContext))

			Toast.makeText(mContext, "No Network Connection.", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {

		case R.id.rbIdentification_Yes:

			isIdentification = "true";
			onIdentification_YesClick();
			break;

		case R.id.rbIdentification_No:

			isIdentification = "false";
			onIdentification_NoClick();
			break;

		case R.id.rbCommence_Yes:

			isCommence = "true";
			onCommence_YesClick();
			break;

		case R.id.rbCommence_No:

			isCommence = "false";
			onCommence_NoClick();
			break;

		case R.id.rbHinderances_Yes:

			isHinderance = "true";
			onHinderance_YesClick();
			break;

		case R.id.rbHinderances_No:

			isHinderance = "false";
			onHinderance_NoClick();
			break;

		default:
			break;
		}
	}

	private void onIdentification_NoClick() {

		if (llIdentificationDesc.getVisibility() == View.GONE)
			llIdentificationDesc.setVisibility(View.VISIBLE);

	}

	private void onIdentification_YesClick() {

		if (llIdentificationDesc.getVisibility() == View.VISIBLE)
			llIdentificationDesc.setVisibility(View.GONE);
		etIdentificationDesc.setText("");

	}

	private void onCommence_NoClick() {

		if (llCommence.getVisibility() == View.GONE)
			llCommence.setVisibility(View.VISIBLE);

	}

	private void onCommence_YesClick() {

		if (llCommence.getVisibility() == View.VISIBLE)
			llCommence.setVisibility(View.GONE);
		etCommenceDesc.setText("");

	}

	private void onHinderance_NoClick() {
		if (llHinderancesDesc.getVisibility() == View.VISIBLE)
			llHinderancesDesc.setVisibility(View.GONE);
		etHinderancesDesc.setText("");

	}

	private void onHinderance_YesClick() {
		if (llHinderancesDesc.getVisibility() == View.GONE)
			llHinderancesDesc.setVisibility(View.VISIBLE);

	}

	@Override
	public void onClick(View v) {

		Calendar currentDate = Util.getCurrentDate();
		switch (v.getId()) {

		case R.id.rlDateOfAcquisition:

			new DatePickerDialog(this, acquisitionDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();

			break;

		default:
			break;
		}

	}

	@Override
	public void onSuccess(JSONObject resultJsonObject) {

		volleyTaskManager.hideProgressDialog();

		Log.d("On Success", "" + resultJsonObject.toString());

		if (isInsertService) {

			isInsertService = false;
			if (resultJsonObject.toString() != null && !resultJsonObject.toString().trim().isEmpty()) {

				try {
					UserClass user = Util.fetchUserClass(mContext);

					String result = new JSONObject(resultJsonObject.optString("SaveSubSurfaceLandAcquisitionResult")).optString("RES");

					Log.v("TAG", "" + result);
					// user.setUpdateId(resultJsonObject.optString(""));
					if (!result.equalsIgnoreCase("0")) {
						user.setSubSurfaceLandAcquisitionUpdateId(result);
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
		} else if (isUpdateService) {

			isUpdateService = false;

			if (resultJsonObject.toString() != null && !resultJsonObject.toString().trim().isEmpty()) {

				try {
					UserClass user = Util.fetchUserClass(mContext);

					String result = new JSONObject(resultJsonObject.optString("UpdateSubSurfaceLandAcquisitionResult")).optString("RES");

					if (!result.equalsIgnoreCase("0")) {

						user.setSubSurfaceLandAcquisitionUpdateId(result);
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

		else if (isFetchDataService) {
			isFetchDataService = false;

			parseInitialFetchedData(resultJsonObject);
		}
	}

	@Override
	public void onError() {

	}

	private void parseInitialFetchedData(JSONObject resultJsonObject) {

		try {

			JSONArray resultJSONArray = new JSONArray(resultJsonObject.optString("GetSubSurfaceLandAcquisitionResult"));

			if (resultJSONArray.length() > 0) {
				ArrayList<LandAcquisitionClass> landAcquisitionList = new ArrayList<LandAcquisitionClass>();

				for (int i = 0; i < resultJSONArray.length(); i++) {

					JSONObject landAcquisitionJSONObject = resultJSONArray.getJSONObject(i);
					LandAcquisitionClass landAcquisition = new LandAcquisitionClass();
					landAcquisition.setCOMMENCEMENT_DESCRIPTION(landAcquisitionJSONObject
							.optString("COMMENCEMENT_OF_LAND_ACQUISITION_PROCESS_IFNO_DESCRIPTION"));
					landAcquisition.setCOMMENCEMENT_ISDONE(landAcquisitionJSONObject.optString("COMMENCEMENT_OF_LAND_ACQUISITION_PROCESS_ISDONE"));
					landAcquisition.setCREATED_BY_ID(landAcquisitionJSONObject.optString("CREATED_BY_ID"));
					landAcquisition.setDATE_OF_ACQUISITION(landAcquisitionJSONObject.optString("DATE_OF_ACQUISITION"));
					landAcquisition.setDISTRICT_ID(landAcquisitionJSONObject.optString("DISTRICT_ID"));
					landAcquisition.setHINDRANCES_ISDONE(landAcquisitionJSONObject.optString("HINDRANCES"));
					landAcquisition.setHINDRANCES_DESCRIPTION(landAcquisitionJSONObject.optString("HINDRANCES_IFYES_DESCRIPTION"));
					landAcquisition.setID(landAcquisitionJSONObject.optString("ID"));
					landAcquisition.setIDENTIFICATION_DESCRIPTION(landAcquisitionJSONObject.optString("IDENTIFICATION_OF_LAND_IFNO_DESCRIPTION"));
					landAcquisition.setIDENTIFICATION_ISDONE(landAcquisitionJSONObject.optString("IDENTIFICATION_OF_LAND_ISDONE"));
					landAcquisition.setSCHEME_ID(landAcquisitionJSONObject.optString("SCHEME_ID"));

					UserClass user = Util.fetchUserClass(mContext);
					user.setSubSurfaceLandAcquisitionUpdateId(landAcquisition.getID());
					Util.saveUserClass(mContext, user);

					landAcquisitionList.add(landAcquisition);

				}

				isInsert = false;

				isUpdate = true;

				populateForm(landAcquisitionList);
			} else {

				isInsert = true;
				isUpdate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateForm(ArrayList<LandAcquisitionClass> landAcquisitionList) {

		for (LandAcquisitionClass landAcquisitionClass : landAcquisitionList) {

			if (landAcquisitionClass.getIDENTIFICATION_ISDONE().trim().equalsIgnoreCase("False")) {

				isIdentification = "false";
				rbIdentification_No.setChecked(true);
				etIdentificationDesc.setText(landAcquisitionClass.getIDENTIFICATION_DESCRIPTION());
				onIdentification_NoClick();
			} else {

				isIdentification = "true";
				rbIdentification_Yes.setChecked(true);
				onIdentification_YesClick();
			}

			if (landAcquisitionClass.getCOMMENCEMENT_ISDONE().trim().equalsIgnoreCase("False")) {

				isCommence = "false";
				rbCommence_No.setChecked(true);
				etCommenceDesc.setText(landAcquisitionClass.getCOMMENCEMENT_DESCRIPTION());
				onCommence_NoClick();
			} else {

				isCommence = "true";
				rbCommence_Yes.setChecked(true);
				onCommence_YesClick();
			}

			Log.v("TAG", "Is hinderance" + landAcquisitionClass.getHINDRANCES_ISDONE() + "*****");
			if (landAcquisitionClass.getHINDRANCES_ISDONE().trim().equalsIgnoreCase("True")) {

				isHinderance = "true";
				rbHinderances_Yes.setChecked(true);
				etHinderancesDesc.setText(landAcquisitionClass.getHINDRANCES_DESCRIPTION());
				onHinderance_YesClick();
			} else {

				isHinderance = "false";
				rbHinderances_No.setChecked(true);
				onHinderance_NoClick();
			}

			if (landAcquisitionClass.getDATE_OF_ACQUISITION() != null) {

				String[] dateArray = landAcquisitionClass.getDATE_OF_ACQUISITION().split(" ");
				tvDateOfAcquisition.setText(dateArray[0]);
			}

		}

	}

	private boolean checkFormDataInput() {

		boolean isValid = false;

		System.out.println("Hinderance: " + isHinderance);

		if (tvDateOfAcquisition.getText().toString() != null && !tvDateOfAcquisition.getText().toString().trim().isEmpty()
				&& !tvDateOfAcquisition.getText().toString().trim().equalsIgnoreCase("")) {

			System.out.println("--------->> Date of Acquisition is Present >>------------");

			if (isIdentification.equalsIgnoreCase("") || isIdentification.isEmpty() || isCommence.equalsIgnoreCase("") || isCommence.isEmpty()
					|| isHinderance.equalsIgnoreCase("") || isHinderance.isEmpty()) {

				System.out.println("--------->> isHinderance is Identification isCommence are empty >>------------");

				Toast.makeText(mContext, "Please select all fields.", Toast.LENGTH_SHORT).show();
				isValid = false;

			} else {

				System.out.println("--------->> isHinderance is Identification isCommence are Not empty  >>------------");

				if (isIdentification.equalsIgnoreCase("false")) {

					System.out.println("--------->> isIdentification is false  >>------------");

					if (etIdentificationDesc.getText().toString().trim().isEmpty() || etIdentificationDesc.getText().toString().trim().equalsIgnoreCase("")) {

						System.out.println("--------->> etIdentificationDesc Description is empty  >>------------");

						Toast.makeText(mContext, "Please enter the description.", Toast.LENGTH_SHORT).show();
						etIdentificationDesc.requestFocus();
						isValid = false;

					} else {
						isValid = true;
					}

				} else {
					isValid = true;
				}
				if (isCommence.equalsIgnoreCase("false")) {

					System.out.println("--------->> isCommence is false  >>------------");

					if (etCommenceDesc.getText().toString().trim().isEmpty() || etCommenceDesc.getText().toString().trim().equalsIgnoreCase("")) {

						System.out.println("--------->> etCommenceDesc Description is empty  >>------------");

						Toast.makeText(mContext, "Please enter the description.", Toast.LENGTH_SHORT).show();
						etCommenceDesc.requestFocus();
						isValid = false;
					} else {
						isValid = true;
					}
				} else {
					isValid = true;
				}
				if (isHinderance.equalsIgnoreCase("true")) {

					System.out.println("--------->> isHinderance is true  >>------------");

					if (etHinderancesDesc.getText().toString().trim().isEmpty() || etHinderancesDesc.getText().toString().trim().equalsIgnoreCase("")) {

						System.out.println("--------->> etHinderancesDesc Description is empty  >>------------");

						Toast.makeText(mContext, "Please enter the description.", Toast.LENGTH_SHORT).show();
						etHinderancesDesc.requestFocus();
						isValid = false;

					} else {
						isValid = true;
					}
				} else {
					isValid = true;
				}
			}

		} else {
			Toast.makeText(mContext, "Please provide the date of aacquisition.", Toast.LENGTH_SHORT).show();
			isValid = false;
		}
		return isValid;
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
				finish();
			}

			@Override
			public void onCancel() {

			}
		});

	}

	private void fetchSurfaceLandAcquisition(String schemeId) {

		isFetchDataService = true;

		String value = "?scheme_id=" + schemeId;

		volleyTaskManager.doGetSubSurfaceLandAcquisition(value);
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
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, SubSurfaceLandAcquisitionActivity.this, requestCode);
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
		Util.trimCache(SubSurfaceLandAcquisitionActivity.this);
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
