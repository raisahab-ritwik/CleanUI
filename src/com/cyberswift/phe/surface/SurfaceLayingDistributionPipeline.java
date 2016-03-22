package com.cyberswift.phe.surface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyberswift.phe.R;
import com.cyberswift.phe.activity.BaseActivity;
import com.cyberswift.phe.dropdown.DropDownViewForXML;
import com.cyberswift.phe.dto.Pipeline;
import com.cyberswift.phe.dto.UserClass;
import com.cyberswift.phe.services.VolleyTaskManager;
import com.cyberswift.phe.utility.AlertDialogCallBack;
import com.cyberswift.phe.utility.ServerResponseCallback;
import com.cyberswift.phe.utility.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class SurfaceLayingDistributionPipeline extends BaseActivity implements ServerResponseCallback, OnClickListener, LocationListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private Context mContext;
	private DropDownViewForXML dropDown_type, dropDown_diameter, dropDown_positionFromRoadCenter, dropDownLayingCompleted, dropDownLayingToBeStarted,
			dropDown_positionOfValve, dropDown_FromNode, dropDown_ToNode;
	private EditText etPipeNumber, etDepthOfCushion, etWorkProgress, etSummaryOfPipelineLaying;
	private EditText etTypeOfValve, etValveDiameter, etConcernedAE, etConcernedSAE;
	private RelativeLayout rlDateOfInstallation, rlDateOfCommencementOfTrialRun, rlDateOfCompletionOfTrialRun, rlExpectedDateOfCommisioningOfTheScheme,
			rlActualDateOfCommisioningOfTheScheme;
	private TextView tvDateOfInstallation, tvDateOfCommencementOfTrialRun, tvDateOfCompletionOfTrialRun, tvExpectedDateOfCommisioningOfTheScheme,
			tvActualDateOfCommisioningOfTheScheme;

	private TextView tv_latitude, tv_longitude;

	private DatePickerDialog.OnDateSetListener installationDateListener;
	private DatePickerDialog.OnDateSetListener commencementTrialRunDateListener;
	private DatePickerDialog.OnDateSetListener completionTrialRunDateListener;
	private DatePickerDialog.OnDateSetListener commissioningExpectedTrialRunDateListener;
	private DatePickerDialog.OnDateSetListener commissioningActualTrialRunDateListener;
	private VolleyTaskManager volleyTaskManager;
	private boolean isGetFromNodeService = false;
	private boolean isGetToNodeService = false;

	private int districtId = 0;
	private String schemeId = "";
	private int schemeTypeId = 0;
	private ArrayList<String> fromNodeNoList;
	private ArrayList<String> toNodeNoList;

	private ProgressDialog mProgressDialog;
	private boolean isInsertService = false;

	private Location mCurrentLocation;
	private LocationRequest mLocationRequest;
	private AlertDialog systemAlertDialog;
	private FusedLocationProviderApi fusedLocationProviderApi;
	private GoogleApiClient mGoogleApiClient;

	private boolean isGetSavedFormData = false;
	private Pipeline tempPipeline;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.surface_laying_distribution_pipeline);
		mContext = SurfaceLayingDistributionPipeline.this;
		initView();
		tempPipeline = new Pipeline();
		fetchValuesFromLandingPage();
		getFromNode();

	}

	private void initView() {

		dropDown_type = (DropDownViewForXML) findViewById(R.id.dropDown_type);
		dropDown_diameter = (DropDownViewForXML) findViewById(R.id.dropDown_diameter);
		dropDown_positionFromRoadCenter = (DropDownViewForXML) findViewById(R.id.dropDown_positionFromRoadCenter);
		dropDownLayingCompleted = (DropDownViewForXML) findViewById(R.id.dropDownLayingCompleted);
		dropDownLayingToBeStarted = (DropDownViewForXML) findViewById(R.id.dropDownLayingToBeStarted);
		dropDown_positionOfValve = (DropDownViewForXML) findViewById(R.id.dropDown_positionOfValve);
		dropDown_FromNode = (DropDownViewForXML) findViewById(R.id.dropDown_FromNode);
		dropDown_ToNode = (DropDownViewForXML) findViewById(R.id.dropDown_ToNode);

		etPipeNumber = (EditText) findViewById(R.id.etPipeNumber);
		etDepthOfCushion = (EditText) findViewById(R.id.etDepthOfCushion);
		etWorkProgress = (EditText) findViewById(R.id.etWorkProgress);
		etSummaryOfPipelineLaying = (EditText) findViewById(R.id.etSummaryOfPipelineLaying);
		etTypeOfValve = (EditText) findViewById(R.id.etTypeOfValve);
		etValveDiameter = (EditText) findViewById(R.id.etValveDiameter);
		etConcernedAE = (EditText) findViewById(R.id.etConcernedAE);
		etConcernedSAE = (EditText) findViewById(R.id.etConcernedSAE);

		rlDateOfInstallation = (RelativeLayout) findViewById(R.id.rlDateOfInstallation);
		rlDateOfCommencementOfTrialRun = (RelativeLayout) findViewById(R.id.rlDateOfCommencementOfTrialRun);
		rlDateOfCompletionOfTrialRun = (RelativeLayout) findViewById(R.id.rlDateOfCompletionOfTrialRun);
		rlExpectedDateOfCommisioningOfTheScheme = (RelativeLayout) findViewById(R.id.rlExpectedDateOfCommisioningOfTheScheme);
		rlActualDateOfCommisioningOfTheScheme = (RelativeLayout) findViewById(R.id.rlActualDateOfCommisioningOfTheScheme);

		tvDateOfInstallation = (TextView) findViewById(R.id.tvDateOfInstallation);
		tvDateOfCommencementOfTrialRun = (TextView) findViewById(R.id.tvDateOfCommencementOfTrialRun);
		tvDateOfCompletionOfTrialRun = (TextView) findViewById(R.id.tvDateOfCompletionOfTrialRun);
		tvExpectedDateOfCommisioningOfTheScheme = (TextView) findViewById(R.id.tvExpectedDateOfCommisioningOfTheScheme);
		tvActualDateOfCommisioningOfTheScheme = (TextView) findViewById(R.id.tvActualDateOfCommisioningOfTheScheme);

		tv_latitude = (TextView) findViewById(R.id.tv_latitude);
		tv_longitude = (TextView) findViewById(R.id.tv_longitude);

		dropDown_type.setItems(getResources().getStringArray(R.array.surface_pipeline_type_array));

		mProgressDialog = new ProgressDialog(SurfaceLayingDistributionPipeline.this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);

		volleyTaskManager = new VolleyTaskManager(mContext);

		dropDown_type.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (dropDown_type.getText().toString().trim().equalsIgnoreCase("DI"))
					dropDown_diameter.setItems(getResources().getStringArray(R.array.surface_pipeline_diameter_di_array));
				else if (dropDown_type.getText().toString().trim().equalsIgnoreCase("AC"))
					dropDown_diameter.setItems(getResources().getStringArray(R.array.surface_pipeline_diameter_ac_array));
				else if (dropDown_type.getText().toString().trim().equalsIgnoreCase("UPVC"))
					dropDown_diameter.setItems(getResources().getStringArray(R.array.surface_pipeline_diameter_upvc_array));
				else if (dropDown_type.getText().toString().trim().equalsIgnoreCase("HDPE"))
					dropDown_diameter.setItems(getResources().getStringArray(R.array.surface_pipeline_diameter_hdpe_array));
				else
					dropDown_diameter.setItems(getResources().getStringArray(R.array.surface_pipeline_diameter_array));
			}
		});

		dropDown_FromNode.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				getToNode(dropDown_FromNode.getText().toString());
			}

		});
		dropDown_ToNode.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				getSavedFormData();
			}
		});

		dropDown_positionFromRoadCenter.setItems(getResources().getStringArray(R.array.surface_pipeline_position_array));
		dropDown_positionOfValve.setItems(getResources().getStringArray(R.array.surface_pipeline_position_array));
		dropDownLayingCompleted.setItems(getResources().getStringArray(R.array.surface_pipeline_yesno_array));
		dropDownLayingToBeStarted.setItems(getResources().getStringArray(R.array.surface_pipeline_yesno_array));

		rlDateOfInstallation.setOnClickListener(this);
		rlDateOfCommencementOfTrialRun.setOnClickListener(this);
		rlDateOfCompletionOfTrialRun.setOnClickListener(this);
		rlExpectedDateOfCommisioningOfTheScheme.setOnClickListener(this);
		rlActualDateOfCommisioningOfTheScheme.setOnClickListener(this);

		initializeDatePickerListener();

	}

	private void fetchValuesFromLandingPage() {

		Intent passedIntent = getIntent();
		districtId = passedIntent.getIntExtra("districtId", 0);
		schemeId = passedIntent.getStringExtra("schemeId");
		schemeTypeId = passedIntent.getIntExtra("schemeTypeId", 0);

		System.out.println("Scheme ID: " + schemeId + " District ID: " + districtId + " Scheme Id: " + schemeTypeId);

	}

	private void initializeDatePickerListener() {

		installationDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvDateOfInstallation.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		commencementTrialRunDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvDateOfCommencementOfTrialRun.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		completionTrialRunDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvDateOfCompletionOfTrialRun.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		commissioningExpectedTrialRunDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvExpectedDateOfCommisioningOfTheScheme.setText(new SimpleDateFormat("MM/yyyy").format(newDate.getTime()));
			}
		};
		commissioningActualTrialRunDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvActualDateOfCommisioningOfTheScheme.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};

	}

	public void onPostClicked(View mView) {

		if (Util.checkConnectivity(mContext)) {

			boolean isValid = checkInputValidity();
			Log.d("IS VALID", "" + isValid);
			if (isValid) {

				Util.hideSoftKeyboard(mContext, mView);
				UserClass user = Util.fetchUserClass(mContext);
				HashMap<String, String> requestMap = new HashMap<String, String>();
				requestMap.put("DISTRICT_ID", String.valueOf(districtId));
				requestMap.put("SCHEME_ID", schemeId);
				requestMap.put("TYPE", dropDown_type.getText().toString().trim());
				requestMap.put("DIAMETER", dropDown_diameter.getText().toString().trim());
				requestMap.put("PIPE_NUMBER", etPipeNumber.getText().toString().trim());
				requestMap.put("FROM_NODE", dropDown_FromNode.getText().toString().trim());
				requestMap.put("TO_NODE", dropDown_ToNode.getText().toString().trim());
				requestMap.put("POSITION_FROM_ROAD_CENTER", dropDown_positionFromRoadCenter.getText().toString().trim());
				requestMap.put("DEPTH_OF_CUSHION_M", etDepthOfCushion.getText().toString().trim());
				requestMap.put("WORK_PROGRESS_STATUS", etWorkProgress.getText().toString().trim());

				String isLayingCompletedStatus = dropDownLayingCompleted.getText().toString().trim().equalsIgnoreCase("Y") ? "true" : "false";
				requestMap.put("LAYING_COMPLETED", isLayingCompletedStatus);

				String isLayingToBeStartedStatus = dropDownLayingToBeStarted.getText().toString().trim().equalsIgnoreCase("Y") ? "true" : "false";
				requestMap.put("LAYING_YET_TO_BE_STARTED", isLayingToBeStartedStatus);

				requestMap.put("SUMMARY_OF_PIPELINE_LAYING", etSummaryOfPipelineLaying.getText().toString().trim());
				requestMap.put("TYPE_NATURE_OF_VALVE", etTypeOfValve.getText().toString().trim());
				requestMap.put("DIAMETER_OF_VALVE", etValveDiameter.getText().toString().trim());
				requestMap.put("DATE_OF_INSTALLATION", tvDateOfInstallation.getText().toString().trim());
				requestMap.put("POSITION_OF_VALVE", dropDown_positionOfValve.getText().toString().trim());
				requestMap.put("CONCERNED_AE", etConcernedAE.getText().toString().trim());
				requestMap.put("CONCERNED_SAE", etConcernedSAE.getText().toString().trim());
				requestMap.put("DATE_OF_COMMENCEMENT_OF_TRIAL_RUN", tvDateOfCommencementOfTrialRun.getText().toString().trim());
				requestMap.put("DATE_OF_COMPLETION_OF_TRIAL_RUN", tvDateOfCompletionOfTrialRun.getText().toString().trim());
				requestMap.put("EXPECTED_DATE_OF_COMMISSIONING_OF_THE_SCHEME", tvExpectedDateOfCommisioningOfTheScheme.getText().toString().trim());
				requestMap.put("ACTUAL_DATE_OF_COMMISSIONING_OF_THE_SCHEME", tvActualDateOfCommisioningOfTheScheme.getText().toString().trim());
				requestMap.put("CREATED_BY_ID", user.getDatabaseId());
				requestMap.put("LAT", tv_latitude.getText().toString().trim());
				requestMap.put("LON", tv_longitude.getText().toString().trim());
				// if (isInsert) {
				//
				// isInsertService = true;
				// Log.v("SurfacePipeline",
				// ">>----------IS INSERT >>------------");
				// volleyTaskManager.doPostSurfacePipelineInsert(requestMap);
				//
				// } else if (isUpdate) {
				//
				// isUpdateService = true;
				// Log.v("SurfacePipeline",
				// ">>----------IS UPDATE >>------------");
				// requestMap.put("ID",
				// user.getSurfaceLandAcquisitionUpdateId());
				// volleyTaskManager.doPostSurfacePipelineInsert(requestMap);
				//
				// }

				isInsertService = true;
				Log.v("SurfacePipeline", ">>----------INSERT >>------------");
				volleyTaskManager.doPostSurfacePipelineInsert(requestMap);
			}

		} else if (Util.checkConnectivity(mContext)) {

			Toast.makeText(mContext, "No Network connectivity.", Toast.LENGTH_SHORT).show();
		}

	}

	public void onLogoutClicked(View v) {

	}

	private boolean checkInputValidity() {

		if (dropDown_type.getText().toString().trim().equalsIgnoreCase("") || dropDown_diameter.getText().toString().trim().equalsIgnoreCase("")
				|| etPipeNumber.getText().toString().trim().equalsIgnoreCase("") || dropDown_FromNode.getText().toString().trim().equalsIgnoreCase("")
				|| dropDown_ToNode.getText().toString().trim().equalsIgnoreCase("")
				|| dropDown_positionFromRoadCenter.getText().toString().trim().equalsIgnoreCase("")
				|| etDepthOfCushion.getText().toString().trim().equalsIgnoreCase("") || etWorkProgress.getText().toString().trim().equalsIgnoreCase("")
				|| dropDownLayingCompleted.getText().toString().trim().equalsIgnoreCase("")
				|| dropDownLayingToBeStarted.getText().toString().trim().equalsIgnoreCase("")
				|| etSummaryOfPipelineLaying.getText().toString().trim().equalsIgnoreCase("") || etTypeOfValve.getText().toString().equalsIgnoreCase("")
				|| etValveDiameter.getText().toString().trim().equalsIgnoreCase("") || tvDateOfInstallation.getText().toString().trim().equalsIgnoreCase("")
				|| dropDown_positionOfValve.getText().toString().trim().equalsIgnoreCase("") || etConcernedAE.getText().toString().trim().equalsIgnoreCase("")
				|| etConcernedSAE.getText().toString().trim().equalsIgnoreCase("")
				|| tvDateOfCommencementOfTrialRun.getText().toString().trim().equalsIgnoreCase("")
				|| tvDateOfCompletionOfTrialRun.getText().toString().trim().equalsIgnoreCase("")
				|| tvExpectedDateOfCommisioningOfTheScheme.getText().toString().trim().equalsIgnoreCase("")
				|| tv_latitude.getText().toString().trim().equalsIgnoreCase("") || tv_longitude.getText().toString().trim().equalsIgnoreCase("")) {

			Util.showMessageWithOk(mContext, "Please fill all the required fields.");
			return false;
		} else
			return true;

	}

	public void onLocationClicked(View v) {
		mCurrentLocation = null;
		// Setting present location null, so that new location value can be
		// fetched on each button click.
		checkingLocation();

	}

	@Override
	public void onSuccess(JSONObject resultJsonObject) {

		Log.v("OnSuccess", "" + resultJsonObject.toString());
		volleyTaskManager.hideProgressDialog();
		if (isGetFromNodeService) {
			isGetFromNodeService = false;
			fromNodeNoList = new ArrayList<String>();
			JSONArray fDGetFormNodeResultJsonArray = resultJsonObject.optJSONArray("GetSurfacePipelineFromNodeResult");
			try {
				for (int i = 0; i < fDGetFormNodeResultJsonArray.length(); i++) {
					JSONObject jObj = fDGetFormNodeResultJsonArray.getJSONObject(i);
					fromNodeNoList.add(jObj.optString("node_no"));
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
			populateFromNodeDropDown(fromNodeNoList);

		} else if (isGetToNodeService) {

			isGetToNodeService = false;
			toNodeNoList = new ArrayList<String>();
			JSONArray fDGetFormNodeResultJsonArray = resultJsonObject.optJSONArray("GetSurfacePipelineToNodeResult");
			try {
				for (int i = 0; i < fDGetFormNodeResultJsonArray.length(); i++) {
					JSONObject jObj = fDGetFormNodeResultJsonArray.getJSONObject(i);
					toNodeNoList.add(jObj.optString("node_no"));
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
			populateToNodeDropDown(toNodeNoList);

		} else if (isInsertService) {

			isInsertService = false;
			if (resultJsonObject.toString() != null && !resultJsonObject.toString().trim().isEmpty()) {

				try {
					UserClass user = Util.fetchUserClass(mContext);

					String result = new JSONObject(resultJsonObject.optString("SaveSurfacePipelineResult")).optString("RES");

					Log.v("TAG", "" + result);
					// user.setUpdateId(resultJsonObject.optString(""));
					if (!result.equalsIgnoreCase("0")) {
						user.setSurfaceLandAcquisitionUpdateId(result);
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

		} else if (isGetSavedFormData) {
			isGetSavedFormData = false;
			refreshView(resultJsonObject);

		}

	}

	private void refreshView(JSONObject resultJsonObject) {

		dropDown_type.setText("");
		dropDown_diameter.setText("");
		etPipeNumber.setText("");
		dropDown_positionFromRoadCenter.setText("");
		etDepthOfCushion.setText("");
		etWorkProgress.setText("");
		dropDownLayingCompleted.setText("");
		dropDownLayingToBeStarted.setText("");
		etSummaryOfPipelineLaying.setText("");
		etTypeOfValve.setText("");
		etValveDiameter.setText("");
		tvDateOfInstallation.setText("");
		dropDown_positionOfValve.setText("");
		etConcernedAE.setText("");
		etConcernedSAE.setText("");
		tvDateOfCommencementOfTrialRun.setText("");
		tvDateOfCompletionOfTrialRun.setText("");
		tvExpectedDateOfCommisioningOfTheScheme.setText("");
		tvActualDateOfCommisioningOfTheScheme.setText("");
		parseInitialFetchedData(resultJsonObject);
	}

	@Override
	public void onError() {

	}

	private void parseInitialFetchedData(JSONObject resultJsonObject) {

		try {

			JSONArray resultJSONArray = new JSONArray(resultJsonObject.optString("GetSurfacePipelineResult"));

			if (resultJSONArray.length() > 0) {
				ArrayList<Pipeline> pipelineList = new ArrayList<Pipeline>();

				for (int i = 0; i < resultJSONArray.length(); i++) {

					JSONObject pipelineJSONObject = resultJSONArray.getJSONObject(i);
					Pipeline pipeline = new Pipeline();
					pipeline.setCONCERNED_AE(pipelineJSONObject.optString("CONCERNED_AE"));
					pipeline.setCONCERNED_SAE(pipelineJSONObject.optString("CONCERNED_SAE"));
					pipeline.setCREATED_BY_ID(pipelineJSONObject.optString("CREATED_BY_ID"));
					pipeline.setDATE_OF_COMMENCEMENT_OF_TRIAL_RUN(pipelineJSONObject.optString("DATE_OF_COMMENCEMENT_OF_TRIAL_RUN"));
					pipeline.setEXPECTED_DATE_OF_COMMISSIONING_OF_THE_SCHEME(pipelineJSONObject.optString("EXPECTED_DATE_OF_COMMISSIONING_OF_THE_SCHEME"));
					pipeline.setACTUAL_DATE_OF_COMMISSIONING_OF_THE_SCHEME(pipelineJSONObject.optString("ACTUAL_DATE_OF_COMMISSIONING_OF_THE_SCHEME"));
					pipeline.setDATE_OF_COMPLETION_OF_TRIAL_RUN(pipelineJSONObject.optString("DATE_OF_COMPLETION_OF_TRIAL_RUN"));
					pipeline.setDATE_OF_INSTALLATION(pipelineJSONObject.optString("DATE_OF_INSTALLATION"));
					pipeline.setDEPTH_OF_CUSHION_M(pipelineJSONObject.optString("DEPTH_OF_CUSHION_M"));
					pipeline.setDIAMETER(pipelineJSONObject.optString("DIAMETER"));
					pipeline.setDIAMETER_OF_VALVE(pipelineJSONObject.optString("DIAMETER_OF_VALVE"));
					pipeline.setDISTRICT_ID(pipelineJSONObject.optString("DISTRICT_ID"));
					pipeline.setFROM_NODE(pipelineJSONObject.optString("FROM_NODE"));
					pipeline.setLAT(pipelineJSONObject.optString("LAT"));
					pipeline.setID(pipelineJSONObject.optString("ID"));
					pipeline.setLAYING_COMPLETED(pipelineJSONObject.optString("LAYING_COMPLETED"));
					pipeline.setLAYING_YET_TO_BE_STARTED(pipelineJSONObject.optString("LAYING_YET_TO_BE_STARTED"));

					pipeline.setLON(pipelineJSONObject.optString("LON"));
					pipeline.setMATERIAL(pipelineJSONObject.optString("MATERIAL"));
					pipeline.setPIPE_NUMBER(pipelineJSONObject.optString("PIPE_NUMBER"));
					pipeline.setPOSITION_FROM_ROAD_CENTER(pipelineJSONObject.optString("POSITION_FROM_ROAD_CENTER"));
					pipeline.setPOSITION_OF_VALVE(pipelineJSONObject.optString("POSITION_OF_VALVE"));

					pipeline.setSTATUS_OF_TRENCHING_M(pipelineJSONObject.optString("STATUS_OF_TRENCHING_M"));
					pipeline.setSUMMARY_OF_PIPELINE_LAYING(pipelineJSONObject.optString("SUMMARY_OF_PIPELINE_LAYING"));
					pipeline.setTO_NODE(pipelineJSONObject.optString("TO_NODE"));
					pipeline.setTYPE(pipelineJSONObject.optString("TYPE"));
					pipeline.setTYPE_NATURE_OF_VALVE(pipelineJSONObject.optString("TYPE_NATURE_OF_VALVE"));
					pipeline.setWORK_PROGRESS_STATUS(pipelineJSONObject.optString("WORK_PROGRESS_STATUS"));

					pipeline.setSCHEME_ID(pipelineJSONObject.optString("SCHEME_ID"));

					UserClass user = Util.fetchUserClass(mContext);
					user.setSurfaceLandAcquisitionUpdateId(pipeline.getID());
					Util.saveUserClass(mContext, user);

					pipelineList.add(pipeline);

				}

				populateForm(pipelineList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Populate the Form if data exists */
	private void populateForm(ArrayList<Pipeline> pipelineList) {

		for (Pipeline pipeline : pipelineList) {

			dropDown_type.setText(pipeline.getTYPE());
			dropDown_diameter.setText(pipeline.getDIAMETER());
			etPipeNumber.setText(pipeline.getPIPE_NUMBER());
			dropDown_FromNode.setText(pipeline.getFROM_NODE());
			dropDown_ToNode.setText(pipeline.getTO_NODE());
			dropDown_positionFromRoadCenter.setText(pipeline.getPOSITION_FROM_ROAD_CENTER());
			etDepthOfCushion.setText(pipeline.getDEPTH_OF_CUSHION_M());
			etWorkProgress.setText(pipeline.getWORK_PROGRESS_STATUS());

			if (pipeline.getLAYING_COMPLETED().trim().equalsIgnoreCase("True"))
				dropDownLayingCompleted.setText("Y");
			else
				dropDownLayingCompleted.setText("N");

			if (pipeline.getLAYING_YET_TO_BE_STARTED().trim().equalsIgnoreCase("True"))
				dropDownLayingToBeStarted.setText("Y");
			else
				dropDownLayingToBeStarted.setText("N");

			etSummaryOfPipelineLaying.setText(pipeline.getSUMMARY_OF_PIPELINE_LAYING());
			etTypeOfValve.setText(pipeline.getTYPE_NATURE_OF_VALVE());
			etValveDiameter.setText(pipeline.getDIAMETER_OF_VALVE());
			tvDateOfInstallation.setText(pipeline.getDATE_OF_INSTALLATION());
			dropDown_positionOfValve.setText(pipeline.getPOSITION_OF_VALVE());
			etConcernedAE.setText(pipeline.getCONCERNED_AE());
			etConcernedSAE.setText(pipeline.getCONCERNED_SAE());
			tvDateOfCommencementOfTrialRun.setText(pipeline.getDATE_OF_COMMENCEMENT_OF_TRIAL_RUN());
			tvDateOfCompletionOfTrialRun.setText(pipeline.getDATE_OF_COMPLETION_OF_TRIAL_RUN());
			tvExpectedDateOfCommisioningOfTheScheme.setText(pipeline.getEXPECTED_DATE_OF_COMMISSIONING_OF_THE_SCHEME());
			tvActualDateOfCommisioningOfTheScheme.setText(pipeline.getACTUAL_DATE_OF_COMMISSIONING_OF_THE_SCHEME());
			// tv_latitude.setText(pipeline.getLAT());
			// tv_longitude.setText(pipeline.getLON());

		}

	}

	private void onSuccessfulInsertUpdate(String message) {

		Util.showCallBackMessageWithOk(mContext, message, new AlertDialogCallBack() {

			@Override
			public void onSubmit() {
				finish();
			}

			@Override
			public void onCancel() {

			}
		});

	}

	private void getFromNode() {

		isGetFromNodeService = true;

		String value = "?scheme_id=" + schemeId + "&scheme_type_id=" + schemeTypeId;

		volleyTaskManager.doGetSurfacePipelineFromNode(value);
	}

	private void populateFromNodeDropDown(ArrayList<String> node_no_list) {

		dropDown_FromNode.setText("");
		dropDown_FromNode.setHint("");
		if (node_no_list.size() > 0) {

			dropDown_FromNode.setEnabled(true);
			dropDown_FromNode.setHint(" select ");
			String[] array = new String[node_no_list.size()];
			int index = 0;
			for (String value : node_no_list) {
				array[index] = node_no_list.get(index);
				index++;
			}
			dropDown_FromNode.setItems(array);
		} else {
			dropDown_FromNode.setEnabled(false);
		}
	}

	private void populateToNodeDropDown(ArrayList<String> node_no_list) {

		dropDown_ToNode.setText("");
		dropDown_ToNode.setHint("");
		if (node_no_list.size() > 0) {

			dropDown_ToNode.setEnabled(true);
			dropDown_ToNode.setHint(" select ");
			String[] array = new String[node_no_list.size()];
			int index = 0;
			for (String value : node_no_list) {
				array[index] = node_no_list.get(index);
				index++;
			}
			dropDown_ToNode.setItems(array);
		} else {
			dropDown_ToNode.setEnabled(false);
		}
	}

	private void getToNode(String nodeId) {

		isGetToNodeService = true;
		String value = "?scheme_id=" + schemeId + "&scheme_type_id=" + schemeTypeId + "&from_node=" + nodeId;
		volleyTaskManager.doGetSurfacePipelineToNode(value);

	}

	private void getSavedFormData() {

		isGetSavedFormData = true;
		String value = "?scheme_id=" + schemeId + "&from_node=" + dropDown_FromNode.getText().toString().trim() + "&to_node="
				+ dropDown_ToNode.getText().toString().trim();
		volleyTaskManager.doGetSurfacePipeline(value);
	}

	@Override
	public void onClick(View v) {

		Calendar currentDate = Util.getCurrentDate();
		switch (v.getId()) {

		case R.id.rlDateOfInstallation:
			new DatePickerDialog(this, installationDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.rlDateOfCommencementOfTrialRun:
			new DatePickerDialog(this, commencementTrialRunDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();

			break;
		case R.id.rlDateOfCompletionOfTrialRun:
			new DatePickerDialog(this, completionTrialRunDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();

			break;
		case R.id.rlExpectedDateOfCommisioningOfTheScheme:
			new DatePickerDialog(this, commissioningExpectedTrialRunDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();

			break;
		case R.id.rlActualDateOfCommisioningOfTheScheme:
			new DatePickerDialog(this, commissioningActualTrialRunDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();

			break;

		default:
			break;
		}

	}

	private void checkingLocation() {

		// if (Util.checkConnectivity(EntryFormActivity.this)) {

		if (!isGooglePlayServicesAvailable()) {
			int requestCode = 10;
			int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, SurfaceLayingDistributionPipeline.this, requestCode);
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
		Util.trimCache(SurfaceLayingDistributionPipeline.this);
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
