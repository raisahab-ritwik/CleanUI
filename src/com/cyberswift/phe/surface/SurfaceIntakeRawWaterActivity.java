package com.cyberswift.phe.surface;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.cyberswift.phe.adapter.ImageAdapter;
import com.cyberswift.phe.dto.ImageClass;
import com.cyberswift.phe.dto.IntakeRawWaterMainClass;
import com.cyberswift.phe.dto.OfflineDataSet;
import com.cyberswift.phe.dto.UserClass;
import com.cyberswift.phe.services.SpeedTestService;
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

public class SurfaceIntakeRawWaterActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener, ServerResponseCallback, LocationListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	// ============ Sinking of Pile for Intake Jetty
	private Context mContext;
	private SegmentedGroup sgSinkingPileIntakeJetty;
	private RadioButton rbSinkingPileIntakeJetty_Yes, rbSinkingPileIntakeJetty_No;
	private EditText etSinkingPileIntakeJettyDesc;
	private LinearLayout llSinkingPileIntakeJettyDesc;

	// ============ Construction of Intake Jetty(Fixed or Floating)
	private CheckBox cbConstructionIntakeToBeStarted, cbConstructionIntakeWorkInProg, cbConstructionIntakeCompleted;
	private TextView tvConstIntakeDateOfStart, tvConstIntakeDateOfCompletion;
	private RelativeLayout rlConstIntakeDateOfStart, rlConstIntakeDateOfCompletion;
	private Button btnConstructionIntakePhoto;
	private ViewPager vp_ConstructionIntakeselectedImages;
	private TextView tv_ConstructionIntakeImageProgress;
	private View v_ConstructionIntakeswipeLeft, v_ConstructionIntakeswipeRight;

	// ============ Laying of Suction Main
	private CheckBox cbLayingSuctionToBeStarted, cbLayingSuctionWorkInProg, cbLayingSuctionCompleted;
	private TextView tvLayingSuctionDateOfStart, tvLayingSuctionDateOfCompletion;
	private RelativeLayout rlLayingSuctionDateOfStart, rlLayingSuctionDateOfCompletion;
	private Button btnLayinSuctionPhoto;
	private ViewPager vp_LayinSuctionselectedImages;
	private TextView tv_LayinSuctionImageProgress;
	private View v_LayinSuctionswipeLeft, v_LayinSuctionswipeRight;

	// ============ Construction of Raw water pump house
	private CheckBox cbConstPumpHouseToBeStarted, cbConstPumpHouseProgress, cbConstPumpHouseCompleted;
	private TextView tvConstWaterPumpDateOfStart, tvConstWaterPumpDateOfCompletion;
	private RelativeLayout rlConstWaterPumpDateOfStart, rlConstWaterPumpDateOfCompletion;
	private Button btnConstPumpHousePhoto;
	private ViewPager vp_ConstPumpHouseselectedImages;
	private TextView tv_ConstPumpHouseImageProgress;
	private View v_ConstPumpHouseswipeLeft, v_ConstPumpHouseswipeRight;

	// ============ Installation of pumping and electrical machinery
	private CheckBox cbInstallPumpToBeStarted, cbInstallPumpInProgress, cbInstallPumpCompleted;
	private TextView tvMachineryInstallDateOfStart, tvMachineryInstallDateOfCompletion;
	private RelativeLayout rlMachineryInstallDateOfStart, rlMachineryInstallDateOfCompletion;
	private Button btnInstallPumpPhoto;
	private ViewPager vp_InstallPumpselectedImages;
	private TextView tv_InstallPumpImageProgress;
	private View v_InstallPumpswipeLeft, v_InstallPumpswipeRight;

	// ============ Construction of Substation(Electrical)
	private CheckBox cbConstructionSubstationToBeStarted, cbConstructionSubstationInProgress, cbConstructionSubstationCompleted;
	private TextView tvConstSubStationDateOfStart, tvConstSubStationDateOfCompletion;
	private RelativeLayout rlConstSubStationDateOfStart, rlConstSubStationDateOfCompletion;
	private Button btnConstructionSubstationPhoto;
	private ViewPager vp_ConstructionSubstationselectedImages;
	private TextView tv_ConstructionSubstationImageProgress;
	private View v_ConstructionSubstationswipeLeft, v_ConstructionSubstationswipeRight;

	// ============ Laying of raw water delivery main
	private CheckBox cbWaterDeliveryToBeStarted, cbWaterDeliveryInProgress, cbWaterDeliveryCompleted;
	private TextView tvLayWaterDeliveryDateOfStart, tvLayWaterDeliveryDateOfCompletion;
	private RelativeLayout rlLayWaterDeliveryDateOfStart, rlLayWaterDeliveryDateOfCompletion;
	private Button btnWaterDeliveryPhoto;
	private ViewPager vp_WaterDeliverySelectedImages;
	private TextView tv_WaterDeliveryImageProgress;
	private View v_WaterDeliveryswipeLeft, v_WaterDeliveryswipeRight;

	private TextView tv_latitude, tv_longitude;

	/*-----------------------------------------------*/

	private int districtId = 0;
	private String schemeId = "";
	private String isSinkingPileIntakeJetty = "";
	private String constructionIntakeJettyStatus = "";

	private DatePickerDialog.OnDateSetListener constructionIntakeStartDateListener;
	private DatePickerDialog.OnDateSetListener constructionIntakeStopDateListener;

	private String layingSuctionMainStatus = "";

	private DatePickerDialog.OnDateSetListener layingSuctionStartDateListener;
	private DatePickerDialog.OnDateSetListener layingSuctionStopDateListener;

	private String constRawWaterPumpHouseStatus = "";

	private DatePickerDialog.OnDateSetListener constRawWaterPumphouseStartDateListener;
	private DatePickerDialog.OnDateSetListener constRawWaterPumphouseStoptDateListener;

	private String installPumpMachineryStatus = "";

	private DatePickerDialog.OnDateSetListener installPumpMachineryStartDateListener;
	private DatePickerDialog.OnDateSetListener installPumpMachineryStopDateListener;

	private String constructionSubstationStatus = "";

	private DatePickerDialog.OnDateSetListener constructionSubstationStartDateListener;
	private DatePickerDialog.OnDateSetListener constructionSubstationStopDateListener;

	private String waterDeliveryStatus = "";

	private DatePickerDialog.OnDateSetListener waterDeliveryStartDateListener;
	private DatePickerDialog.OnDateSetListener waterDeliveryStopDateListener;

	/*-----------------------------------------------*/
	private boolean isInsertService = false;
	private boolean isUpdateService = false;
	private boolean isInsert = false;
	private boolean isUpdate = false;
	private VolleyTaskManager volleyTaskManager;
	private ArrayList<ImageClass> constructionIntakeImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> layingSuctionImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> constructionRawWaterPumpHouseImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> installationPumpingElectricalMechImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> constructionSubStationImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> layingRawWaterDeliveryImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> allImagesList = new ArrayList<ImageClass>();
	private ProgressDialog mProgressDialog;
	private Uri mCapturedImageURI;
	private static final int DATE_DIALOG_ID = 316, PICTURE_GALLERY_REQUEST = 2572, CAMERA_PIC_REQUEST = 1337;

	private boolean isConstructionIntake = false;
	private boolean isLayingSuctionMain = false;
	private boolean isConstructionRawWaterPumpHouse = false;
	private boolean isInstallationPumpingElectricalMachine = false;
	private boolean isConstructionSubstation = false;
	private boolean isLayingRawWaterDeliveryMain = false;
	private String insertUpdateId = "";
	MyReceiver myReceiver;
	private boolean isDataPostingService = false;
	private int imagePosition;
	private boolean isSurfaceIntakeRawWaterMainImageService = false;

	private int constructionIntakeImagesCount = 0;
	private int layingSuctionImagesCount = 0;
	private int constructionRawWaterPumpHouseImagesCount = 0;
	private int installationPumpingElectricalMechImagesCount = 0;
	private int constructionSubStationImagesCount = 0;
	private int layingRawWaterDeliveryImagesCount = 0;
	private boolean isFetchDataService = false;

	// ============LOCATION

	private Location mCurrentLocation;
	private LocationRequest mLocationRequest;
	private AlertDialog systemAlertDialog;
	private FusedLocationProviderApi fusedLocationProviderApi;
	private GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surface_intake_raw_water_main);
		mContext = SurfaceIntakeRawWaterActivity.this;
		initView();
		fetchValuesFromLandingPage();
		fetchFormData();
	}

	private void initView() {

		// ============ Sinking of Pile for Intake Jetty
		sgSinkingPileIntakeJetty = (SegmentedGroup) findViewById(R.id.sgSinkingPileIntakeJetty);
		etSinkingPileIntakeJettyDesc = (EditText) findViewById(R.id.etSinkingPileIntakeJettyDesc);
		rbSinkingPileIntakeJetty_Yes = (RadioButton) findViewById(R.id.rbSinkingPileIntakeJetty_Yes);
		rbSinkingPileIntakeJetty_No = (RadioButton) findViewById(R.id.rbSinkingPileIntakeJetty_No);
		llSinkingPileIntakeJettyDesc = (LinearLayout) findViewById(R.id.llSinkingPileIntakeJettyDesc);

		// ============ Construction of Intake Jetty(Fixed or Floating)
		cbConstructionIntakeToBeStarted = (CheckBox) findViewById(R.id.cbConstructionIntakeToBeStarted);
		cbConstructionIntakeWorkInProg = (CheckBox) findViewById(R.id.cbConstructionIntakeWorkInProg);
		cbConstructionIntakeCompleted = (CheckBox) findViewById(R.id.cbConstructionIntakeCompleted);
		tvConstIntakeDateOfStart = (TextView) findViewById(R.id.tvConstIntakeDateOfStart);
		tvConstIntakeDateOfCompletion = (TextView) findViewById(R.id.tvConstIntakeDateOfCompletion);
		rlConstIntakeDateOfStart = (RelativeLayout) findViewById(R.id.rlConstIntakeDateOfStart);
		rlConstIntakeDateOfCompletion = (RelativeLayout) findViewById(R.id.rlConstIntakeDateOfCompletion);
		btnConstructionIntakePhoto = (Button) findViewById(R.id.btnConstructionIntakePhoto);
		vp_ConstructionIntakeselectedImages = (ViewPager) findViewById(R.id.vp_ConstructionIntakeselectedImages);
		tv_ConstructionIntakeImageProgress = (TextView) findViewById(R.id.tv_ConstructionIntakeImageProgress);
		v_ConstructionIntakeswipeLeft = (View) findViewById(R.id.v_ConstructionIntakeswipeLeft);
		v_ConstructionIntakeswipeRight = (View) findViewById(R.id.v_ConstructionIntakeswipeRight);

		cbConstructionIntakeToBeStarted.setOnClickListener(this);
		cbConstructionIntakeWorkInProg.setOnClickListener(this);
		cbConstructionIntakeCompleted.setOnClickListener(this);
		rlConstIntakeDateOfStart.setOnClickListener(this);
		rlConstIntakeDateOfCompletion.setOnClickListener(this);
		btnConstructionIntakePhoto.setOnClickListener(this);

		// ============ Laying of Suction Main
		cbLayingSuctionToBeStarted = (CheckBox) findViewById(R.id.cbLayingSuctionToBeStarted);
		cbLayingSuctionWorkInProg = (CheckBox) findViewById(R.id.cbLayingSuctionWorkInProg);
		cbLayingSuctionCompleted = (CheckBox) findViewById(R.id.cbLayingSuctionCompleted);
		tvLayingSuctionDateOfStart = (TextView) findViewById(R.id.tvLayingSuctionDateOfStart);
		tvLayingSuctionDateOfCompletion = (TextView) findViewById(R.id.tvLayingSuctionDateOfCompletion);
		rlLayingSuctionDateOfStart = (RelativeLayout) findViewById(R.id.rlLayingSuctionDateOfStart);
		rlLayingSuctionDateOfCompletion = (RelativeLayout) findViewById(R.id.rlLayingSuctionDateOfCompletion);
		btnLayinSuctionPhoto = (Button) findViewById(R.id.btnLayinSuctionPhoto);
		vp_LayinSuctionselectedImages = (ViewPager) findViewById(R.id.vp_LayinSuctionselectedImages);
		tv_LayinSuctionImageProgress = (TextView) findViewById(R.id.tv_LayinSuctionImageProgress);
		v_LayinSuctionswipeLeft = (View) findViewById(R.id.v_LayinSuctionswipeLeft);
		v_LayinSuctionswipeRight = (View) findViewById(R.id.v_LayinSuctionswipeRight);

		cbLayingSuctionToBeStarted.setOnClickListener(this);
		cbLayingSuctionWorkInProg.setOnClickListener(this);
		cbLayingSuctionCompleted.setOnClickListener(this);
		rlLayingSuctionDateOfStart.setOnClickListener(this);
		rlLayingSuctionDateOfCompletion.setOnClickListener(this);
		btnLayinSuctionPhoto.setOnClickListener(this);

		// ============ Construction of Raw water pump house
		cbConstPumpHouseToBeStarted = (CheckBox) findViewById(R.id.cbConstPumpHouseToBeStarted);
		cbConstPumpHouseProgress = (CheckBox) findViewById(R.id.cbConstPumpHouseProgress);
		cbConstPumpHouseCompleted = (CheckBox) findViewById(R.id.cbConstPumpHouseCompleted);
		tvConstWaterPumpDateOfStart = (TextView) findViewById(R.id.tvConstWaterPumpDateOfStart);
		tvConstWaterPumpDateOfCompletion = (TextView) findViewById(R.id.tvConstWaterPumpDateOfCompletion);
		rlConstWaterPumpDateOfStart = (RelativeLayout) findViewById(R.id.rlConstWaterPumpDateOfStart);
		rlConstWaterPumpDateOfCompletion = (RelativeLayout) findViewById(R.id.rlConstWaterPumpDateOfCompletion);
		btnConstPumpHousePhoto = (Button) findViewById(R.id.btnConstPumpHousePhoto);
		vp_ConstPumpHouseselectedImages = (ViewPager) findViewById(R.id.vp_ConstPumpHouseselectedImages);
		tv_ConstPumpHouseImageProgress = (TextView) findViewById(R.id.tv_ConstPumpHouseImageProgress);
		v_ConstPumpHouseswipeLeft = (View) findViewById(R.id.v_ConstPumpHouseswipeLeft);
		v_ConstPumpHouseswipeRight = (View) findViewById(R.id.v_ConstPumpHouseswipeRight);

		cbConstPumpHouseToBeStarted.setOnClickListener(this);
		cbConstPumpHouseProgress.setOnClickListener(this);
		cbConstPumpHouseCompleted.setOnClickListener(this);
		rlConstWaterPumpDateOfStart.setOnClickListener(this);
		rlConstWaterPumpDateOfCompletion.setOnClickListener(this);
		btnConstPumpHousePhoto.setOnClickListener(this);

		// ============ Installation of pumping and electrical machinery
		cbInstallPumpToBeStarted = (CheckBox) findViewById(R.id.cbInstallPumpToBeStarted);
		cbInstallPumpInProgress = (CheckBox) findViewById(R.id.cbInstallPumpInProgress);
		cbInstallPumpCompleted = (CheckBox) findViewById(R.id.cbInstallPumpCompleted);
		tvMachineryInstallDateOfStart = (TextView) findViewById(R.id.tvMachineryInstallDateOfStart);
		tvMachineryInstallDateOfCompletion = (TextView) findViewById(R.id.tvMachineryInstallDateOfCompletion);
		rlMachineryInstallDateOfStart = (RelativeLayout) findViewById(R.id.rlMachineryInstallDateOfStart);
		rlMachineryInstallDateOfCompletion = (RelativeLayout) findViewById(R.id.rlMachineryInstallDateOfCompletion);
		btnInstallPumpPhoto = (Button) findViewById(R.id.btnInstallPumpPhoto);
		vp_InstallPumpselectedImages = (ViewPager) findViewById(R.id.vp_InstallPumpselectedImages);
		tv_InstallPumpImageProgress = (TextView) findViewById(R.id.tv_InstallPumpImageProgress);
		v_InstallPumpswipeLeft = (View) findViewById(R.id.v_InstallPumpswipeLeft);
		v_InstallPumpswipeRight = (View) findViewById(R.id.v_InstallPumpswipeRight);

		cbInstallPumpToBeStarted.setOnClickListener(this);
		cbInstallPumpInProgress.setOnClickListener(this);
		cbInstallPumpCompleted.setOnClickListener(this);
		rlMachineryInstallDateOfStart.setOnClickListener(this);
		rlMachineryInstallDateOfCompletion.setOnClickListener(this);
		btnInstallPumpPhoto.setOnClickListener(this);

		// ============ Construction of Substation(Electrical)
		cbConstructionSubstationToBeStarted = (CheckBox) findViewById(R.id.cbConstructionSubstationToBeStarted);
		cbConstructionSubstationInProgress = (CheckBox) findViewById(R.id.cbConstructionSubstationInProgress);
		cbConstructionSubstationCompleted = (CheckBox) findViewById(R.id.cbConstructionSubstationCompleted);
		tvConstSubStationDateOfStart = (TextView) findViewById(R.id.tvConstSubStationDateOfStart);
		tvConstSubStationDateOfCompletion = (TextView) findViewById(R.id.tvConstSubStationDateOfCompletion);
		rlConstSubStationDateOfStart = (RelativeLayout) findViewById(R.id.rlConstSubStationDateOfStart);
		rlConstSubStationDateOfCompletion = (RelativeLayout) findViewById(R.id.rlConstSubStationDateOfCompletion);
		btnConstructionSubstationPhoto = (Button) findViewById(R.id.btnConstructionSubstationPhoto);
		vp_ConstructionSubstationselectedImages = (ViewPager) findViewById(R.id.vp_ConstructionSubstationselectedImages);
		tv_ConstructionSubstationImageProgress = (TextView) findViewById(R.id.tv_ConstructionSubstationImageProgress);
		v_ConstructionSubstationswipeLeft = (View) findViewById(R.id.v_ConstructionSubstationswipeLeft);
		v_ConstructionSubstationswipeRight = (View) findViewById(R.id.v_ConstructionSubstationswipeRight);

		cbConstructionSubstationToBeStarted.setOnClickListener(this);
		cbConstructionSubstationInProgress.setOnClickListener(this);
		cbConstructionSubstationCompleted.setOnClickListener(this);
		rlConstSubStationDateOfStart.setOnClickListener(this);
		rlConstSubStationDateOfCompletion.setOnClickListener(this);
		btnConstructionSubstationPhoto.setOnClickListener(this);

		// ============ Laying of raw water delivery main
		cbWaterDeliveryToBeStarted = (CheckBox) findViewById(R.id.cbWaterDeliveryToBeStarted);
		cbWaterDeliveryInProgress = (CheckBox) findViewById(R.id.cbWaterDeliveryInProgress);
		cbWaterDeliveryCompleted = (CheckBox) findViewById(R.id.cbWaterDeliveryCompleted);
		tvLayWaterDeliveryDateOfStart = (TextView) findViewById(R.id.tvLayWaterDeliveryDateOfStart);
		tvLayWaterDeliveryDateOfCompletion = (TextView) findViewById(R.id.tvLayWaterDeliveryDateOfCompletion);
		rlLayWaterDeliveryDateOfStart = (RelativeLayout) findViewById(R.id.rlLayWaterDeliveryDateOfStart);
		rlLayWaterDeliveryDateOfCompletion = (RelativeLayout) findViewById(R.id.rlLayWaterDeliveryDateOfCompletion);
		btnWaterDeliveryPhoto = (Button) findViewById(R.id.btnWaterDeliveryPhoto);
		vp_WaterDeliverySelectedImages = (ViewPager) findViewById(R.id.vp_WaterDeliverySelectedImages);
		tv_WaterDeliveryImageProgress = (TextView) findViewById(R.id.tv_WaterDeliveryImageProgress);
		v_WaterDeliveryswipeLeft = (View) findViewById(R.id.v_WaterDeliveryswipeLeft);
		v_WaterDeliveryswipeRight = (View) findViewById(R.id.v_WaterDeliveryswipeRight);

		cbWaterDeliveryToBeStarted.setOnClickListener(this);
		cbWaterDeliveryInProgress.setOnClickListener(this);
		cbWaterDeliveryCompleted.setOnClickListener(this);
		rlLayWaterDeliveryDateOfStart.setOnClickListener(this);
		rlLayWaterDeliveryDateOfCompletion.setOnClickListener(this);
		btnWaterDeliveryPhoto.setOnClickListener(this);

		// ============LOCATION

		tv_latitude = (TextView) findViewById(R.id.tv_latitude);
		tv_longitude = (TextView) findViewById(R.id.tv_longitude);

		// -------------------------------------------------------------

		sgSinkingPileIntakeJetty.setOnCheckedChangeListener(this);
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		volleyTaskManager = new VolleyTaskManager(mContext);

		initializeDatePickerListener();
	}

	private void fetchValuesFromLandingPage() {

		Intent passedIntent = getIntent();
		districtId = passedIntent.getIntExtra("districtId", 0);
		schemeId = passedIntent.getStringExtra("schemeId");

		System.out.println("Scheme ID: " + schemeId + " District ID: " + districtId);

	}

	private void initializeDatePickerListener() {
		constructionIntakeStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstIntakeDateOfStart.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionIntakeStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstIntakeDateOfCompletion.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		layingSuctionStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvLayingSuctionDateOfStart.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		layingSuctionStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvLayingSuctionDateOfCompletion.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constRawWaterPumphouseStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstWaterPumpDateOfStart.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constRawWaterPumphouseStoptDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstWaterPumpDateOfCompletion.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		installPumpMachineryStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvMachineryInstallDateOfStart.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};

		installPumpMachineryStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvMachineryInstallDateOfCompletion.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionSubstationStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstSubStationDateOfStart.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionSubstationStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstSubStationDateOfCompletion.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		waterDeliveryStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvLayWaterDeliveryDateOfStart.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		waterDeliveryStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvLayWaterDeliveryDateOfCompletion.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
	}

	public void onLogoutClicked(View v) {

	}

	public void onPostClicked(View mView) {

		if (Util.checkConnectivity(mContext)) {

			boolean isValid = checkInputValidity();
			Log.d("IS VALID", "" + isValid);
			if (isValid) {

				Util.hideSoftKeyboard(mContext, mView);
				allImagesList.addAll(layingSuctionImagesList);
				allImagesList.addAll(constructionIntakeImagesList);
				allImagesList.addAll(constructionRawWaterPumpHouseImagesList);
				allImagesList.addAll(installationPumpingElectricalMechImagesList);
				allImagesList.addAll(constructionSubStationImagesList);
				allImagesList.addAll(layingRawWaterDeliveryImagesList);
				postFormWebserviceCalling();
			}

		} else if (Util.checkConnectivity(mContext)) {

			Toast.makeText(mContext, "No Network connectivity.", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {

		case R.id.rbSinkingPileIntakeJetty_Yes:

			isSinkingPileIntakeJetty = "true";
			onSinkingPileIntakeJetty_YesClick();
			break;

		case R.id.rbSinkingPileIntakeJetty_No:

			isSinkingPileIntakeJetty = "false";
			onSinkingPileIntakeJetty_NoClick();
			break;

		default:
			break;
		}
	}

	private void onSinkingPileIntakeJetty_NoClick() {

		if (llSinkingPileIntakeJettyDesc.getVisibility() == View.GONE)
			llSinkingPileIntakeJettyDesc.setVisibility(View.VISIBLE);

	}

	private void onSinkingPileIntakeJetty_YesClick() {

		if (llSinkingPileIntakeJettyDesc.getVisibility() == View.VISIBLE)

			llSinkingPileIntakeJettyDesc.setVisibility(View.GONE);
		etSinkingPileIntakeJettyDesc.setText("");

	}

	@Override
	public void onClick(View v) {

		Calendar currentDate = Util.getCurrentDate();

		switch (v.getId()) {

		// ============ Construction of Intake Jetty(Fixed or Floating)
		case R.id.cbConstructionIntakeToBeStarted:

			constructionIntakeJettyStatus = "Yet to be Started";
			cbConstructionIntakeToBeStarted.setChecked(true);
			cbConstructionIntakeWorkInProg.setChecked(false);
			cbConstructionIntakeCompleted.setChecked(false);
			break;
		case R.id.cbConstructionIntakeWorkInProg:

			constructionIntakeJettyStatus = "Work in Progress";
			cbConstructionIntakeToBeStarted.setChecked(false);
			cbConstructionIntakeWorkInProg.setChecked(true);
			cbConstructionIntakeCompleted.setChecked(false);
			break;
		case R.id.cbConstructionIntakeCompleted:

			constructionIntakeJettyStatus = "Completed";
			cbConstructionIntakeToBeStarted.setChecked(false);
			cbConstructionIntakeWorkInProg.setChecked(false);
			cbConstructionIntakeCompleted.setChecked(true);
			break;

		case R.id.rlConstIntakeDateOfStart:

			new DatePickerDialog(this, constructionIntakeStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.rlConstIntakeDateOfCompletion:

			new DatePickerDialog(this, constructionIntakeStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.btnConstructionIntakePhoto:

			isConstructionIntake = true;
			imageDialog();

			break;

		// ============ Laying of Suction Main
		case R.id.cbLayingSuctionToBeStarted:

			layingSuctionMainStatus = "Yet to be Started";
			cbLayingSuctionToBeStarted.setChecked(true);
			cbLayingSuctionWorkInProg.setChecked(false);
			cbLayingSuctionCompleted.setChecked(false);
			break;
		case R.id.cbLayingSuctionWorkInProg:

			layingSuctionMainStatus = "Work in Progress";
			cbLayingSuctionToBeStarted.setChecked(false);
			cbLayingSuctionWorkInProg.setChecked(true);
			cbLayingSuctionCompleted.setChecked(false);

			break;
		case R.id.cbLayingSuctionCompleted:

			layingSuctionMainStatus = "Completed";
			cbLayingSuctionToBeStarted.setChecked(false);
			cbLayingSuctionWorkInProg.setChecked(false);
			cbLayingSuctionCompleted.setChecked(true);
			break;

		case R.id.rlLayingSuctionDateOfStart:

			new DatePickerDialog(this, layingSuctionStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.rlLayingSuctionDateOfCompletion:

			new DatePickerDialog(this, layingSuctionStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.btnLayinSuctionPhoto:

			isLayingSuctionMain = true;
			imageDialog();
			break;

		// ============ Construction of Raw water pump house
		case R.id.cbConstPumpHouseToBeStarted:

			constRawWaterPumpHouseStatus = "Yet to be Started";
			cbConstPumpHouseToBeStarted.setChecked(true);
			cbConstPumpHouseProgress.setChecked(false);
			cbConstPumpHouseCompleted.setChecked(false);
			break;
		case R.id.cbConstPumpHouseProgress:

			constRawWaterPumpHouseStatus = "Work in Progress";
			cbConstPumpHouseToBeStarted.setChecked(false);
			cbConstPumpHouseProgress.setChecked(true);
			cbConstPumpHouseCompleted.setChecked(false);

			break;
		case R.id.cbConstPumpHouseCompleted:

			constRawWaterPumpHouseStatus = "Completed";
			cbConstPumpHouseToBeStarted.setChecked(false);
			cbConstPumpHouseProgress.setChecked(false);
			cbConstPumpHouseCompleted.setChecked(true);
			break;

		case R.id.rlConstWaterPumpDateOfStart:

			new DatePickerDialog(this, constRawWaterPumphouseStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.rlConstWaterPumpDateOfCompletion:

			new DatePickerDialog(this, constRawWaterPumphouseStoptDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.btnConstPumpHousePhoto:

			isConstructionRawWaterPumpHouse = true;
			imageDialog();
			break;

		// ============ Installation of pumping and electrical machinery
		case R.id.cbInstallPumpToBeStarted:

			installPumpMachineryStatus = "Yet to be Started";
			cbInstallPumpToBeStarted.setChecked(true);
			cbInstallPumpInProgress.setChecked(false);
			cbInstallPumpCompleted.setChecked(false);
			break;
		case R.id.cbInstallPumpInProgress:

			installPumpMachineryStatus = "Work in Progress";
			cbInstallPumpToBeStarted.setChecked(false);
			cbInstallPumpInProgress.setChecked(true);
			cbInstallPumpCompleted.setChecked(false);

			break;
		case R.id.cbInstallPumpCompleted:

			installPumpMachineryStatus = "Completed";
			cbInstallPumpToBeStarted.setChecked(false);
			cbInstallPumpInProgress.setChecked(false);
			cbInstallPumpCompleted.setChecked(true);
			break;

		case R.id.rlMachineryInstallDateOfStart:

			new DatePickerDialog(this, installPumpMachineryStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.rlMachineryInstallDateOfCompletion:

			new DatePickerDialog(this, installPumpMachineryStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.btnInstallPumpPhoto:

			isInstallationPumpingElectricalMachine = true;
			imageDialog();
			break;

		// ============ Construction of Substation(Electrical)
		case R.id.cbConstructionSubstationToBeStarted:

			constructionSubstationStatus = "Yet to be Started";
			cbConstructionSubstationToBeStarted.setChecked(true);
			cbConstructionSubstationInProgress.setChecked(false);
			cbConstructionSubstationCompleted.setChecked(false);
			break;
		case R.id.cbConstructionSubstationInProgress:

			constructionSubstationStatus = "Work in Progress";
			cbConstructionSubstationToBeStarted.setChecked(false);
			cbConstructionSubstationInProgress.setChecked(true);
			cbConstructionSubstationCompleted.setChecked(false);

			break;
		case R.id.cbConstructionSubstationCompleted:

			constructionSubstationStatus = "Completed";
			cbConstructionSubstationToBeStarted.setChecked(false);
			cbConstructionSubstationInProgress.setChecked(false);
			cbConstructionSubstationCompleted.setChecked(true);
			break;

		case R.id.rlConstSubStationDateOfStart:

			new DatePickerDialog(this, constructionSubstationStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.rlConstSubStationDateOfCompletion:

			new DatePickerDialog(this, constructionSubstationStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.btnConstructionSubstationPhoto:

			isConstructionSubstation = true;
			imageDialog();
			break;
		// ============ Laying of raw water delivery main

		case R.id.cbWaterDeliveryToBeStarted:

			waterDeliveryStatus = "Yet to be Started";
			cbWaterDeliveryToBeStarted.setChecked(true);
			cbWaterDeliveryInProgress.setChecked(false);
			cbWaterDeliveryCompleted.setChecked(false);
			break;
		case R.id.cbWaterDeliveryInProgress:

			waterDeliveryStatus = "Work in Progress";
			cbWaterDeliveryToBeStarted.setChecked(false);
			cbWaterDeliveryInProgress.setChecked(true);
			cbWaterDeliveryCompleted.setChecked(false);

			break;
		case R.id.cbWaterDeliveryCompleted:

			waterDeliveryStatus = "Completed";
			cbWaterDeliveryToBeStarted.setChecked(false);
			cbWaterDeliveryInProgress.setChecked(false);
			cbWaterDeliveryCompleted.setChecked(true);
			break;

		case R.id.rlLayWaterDeliveryDateOfStart:

			new DatePickerDialog(this, waterDeliveryStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.rlLayWaterDeliveryDateOfCompletion:

			new DatePickerDialog(this, waterDeliveryStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.btnWaterDeliveryPhoto:

			isLayingRawWaterDeliveryMain = true;
			imageDialog();
			break;

		default:
			break;
		}

	}

	private boolean checkInputValidity() {

		if (isSinkingPileIntakeJetty.trim().equalsIgnoreCase("") || constructionIntakeJettyStatus.equalsIgnoreCase("")
				|| constRawWaterPumpHouseStatus.equalsIgnoreCase("") || waterDeliveryStatus.equalsIgnoreCase("")
				|| constructionSubstationStatus.equalsIgnoreCase("") || installPumpMachineryStatus.equalsIgnoreCase("")
				|| tvConstIntakeDateOfStart.getText().toString().trim().equalsIgnoreCase("")
				|| tvConstIntakeDateOfCompletion.getText().toString().trim().equalsIgnoreCase("")
				|| tvLayingSuctionDateOfStart.getText().toString().trim().equalsIgnoreCase("")
				|| tvLayingSuctionDateOfCompletion.getText().toString().trim().equalsIgnoreCase("")
				|| tvConstWaterPumpDateOfStart.getText().toString().trim().equalsIgnoreCase("")
				|| tvConstWaterPumpDateOfCompletion.getText().toString().trim().equalsIgnoreCase("")
				|| tvMachineryInstallDateOfStart.getText().toString().trim().equalsIgnoreCase("")
				|| tvMachineryInstallDateOfCompletion.getText().toString().trim().equalsIgnoreCase("")
				|| tvConstSubStationDateOfStart.getText().toString().trim().equalsIgnoreCase("")
				|| tvConstSubStationDateOfCompletion.getText().toString().trim().equalsIgnoreCase("")
				|| tvLayWaterDeliveryDateOfStart.getText().toString().trim().equalsIgnoreCase("")
				|| tvLayWaterDeliveryDateOfCompletion.getText().toString().trim().equalsIgnoreCase("")
				|| tv_latitude.getText().toString().trim().equalsIgnoreCase("") || tv_longitude.getText().toString().trim().equalsIgnoreCase("")) {

			Util.showCallBackMessageWithOk(mContext, "Please fill all the required fields.", new AlertDialogCallBack() {
				@Override
				public void onSubmit() {
				}

				@Override
				public void onCancel() {
				}
			});
			return false;

		} else {
			if (isSinkingPileIntakeJetty.trim().equalsIgnoreCase("false") && etSinkingPileIntakeJettyDesc.getText().toString().trim().equalsIgnoreCase("")) {
				Util.showCallBackMessageWithOk(mContext, "Please provide description.", new AlertDialogCallBack() {
					@Override
					public void onSubmit() {
					}

					@Override
					public void onCancel() {
					}
				});
				etSinkingPileIntakeJettyDesc.requestFocus();
				return false;

			} else {
				return true;
			}
		}
	}

	private void showProgressDialog() {
		if (!mProgressDialog.isShowing())
			mProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}

	private void imageDialog() {
		if ((isConstructionIntake && constructionIntakeImagesList.size() < 3) || (isLayingSuctionMain && layingSuctionImagesList.size() < 3)
				|| (isConstructionRawWaterPumpHouse && constructionRawWaterPumpHouseImagesList.size() < 3)
				|| (isInstallationPumpingElectricalMachine && installationPumpingElectricalMechImagesList.size() < 3)
				|| (isConstructionSubstation && constructionSubStationImagesList.size() < 3)
				|| (isLayingRawWaterDeliveryMain && layingRawWaterDeliveryImagesList.size() < 3)) {

			final Dialog customDialog = new Dialog(mContext);

			customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

			LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.image_select_dialog, null);

			// WindowManager.LayoutParams wmlp =
			// customDialog.getWindow().getAttributes();
			// wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
			// wmlp.x = screenWidth/3; //x position
			// wmlp.y = -44; //y position

			Button btn_album = (Button) view.findViewById(R.id.btn_album);
			btn_album.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					customDialog.dismiss();

					// --> For Album
					mProgressDialog.setMessage("Please wait...");
					mProgressDialog.setCancelable(false);
					showProgressDialog();
					populatingSelectedPic();
				}
			});

			Button btn_camera = (Button) view.findViewById(R.id.btn_camera);
			btn_camera.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					customDialog.dismiss();

					// --> For Album
					mProgressDialog.setMessage("Please wait...");
					mProgressDialog.setCancelable(false);
					showProgressDialog();
					cameraSelectedPic();
				}
			});

			Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
			btn_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isConstructionIntake = false;
					isLayingSuctionMain = false;
					isConstructionRawWaterPumpHouse = false;
					isInstallationPumpingElectricalMachine = false;
					isConstructionSubstation = false;
					isLayingRawWaterDeliveryMain = false;

					customDialog.dismiss();
				}
			});

			customDialog.setCancelable(false);
			customDialog.setContentView(view);
			customDialog.setCanceledOnTouchOutside(false);
			// Start AlertDialog
			customDialog.show();
		} else {
			if (isConstructionIntake) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Construction of Intake Jetty\" images has already been selected!");
			} else if (isLayingSuctionMain) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Laying of Suction Main\" images has already been selected!");
			} else if (isConstructionRawWaterPumpHouse) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Construction of Raw water Pump House\" images has already been selected!");

			} else if (isInstallationPumpingElectricalMachine) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Installation of Pumping and Electrical Machinery\" images has already been selected!");

			} else if (isConstructionSubstation) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Construction of Subsatation\" images has already been selected!");

			} else if (isLayingRawWaterDeliveryMain) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Laying of raw water delivery main\" images has already been selected!");
			}
		}
	}

	protected void cameraSelectedPic() {

		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "Image File name");
		mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
		startActivityForResult(intentPicture, CAMERA_PIC_REQUEST);

	}

	protected void populatingSelectedPic() {
		Intent albumIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		albumIntent.setType("image/*");
		startActivityForResult(albumIntent, PICTURE_GALLERY_REQUEST);
	}

	public synchronized void onActivityResult(final int requestCode, int resultCode, final Intent data) {
		hideProgressDialog();

		if (resultCode == RESULT_OK) {
			// Log.v("DialogChoosePicture", " " + data.getData());

			Uri selectedUri = null;

			switch (requestCode) {
			case CAMERA_PIC_REQUEST:
				selectedUri = mCapturedImageURI;
				break;

			case PICTURE_GALLERY_REQUEST:
				selectedUri = data.getData();
				break;

			}

			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedUri, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);

			cursor.close();

			Log.v("IMAGE PATH:", picturePath);

			processImagePath(picturePath);
		} else {
			Log.w("DialogChoosePicture", "Warning: activity result not ok");

			isConstructionIntake = false;
			isLayingSuctionMain = false;
			isConstructionRawWaterPumpHouse = false;
			isInstallationPumpingElectricalMachine = false;
			isConstructionSubstation = false;
			isLayingRawWaterDeliveryMain = false;

			// Camera Click action
			Toast.makeText(mContext, "No image selected", Toast.LENGTH_LONG).show();

		}
	}

	private void processImagePath(String picturePath) {
		Bitmap bitmap = null;

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inScaled = true;

		int bitWidth = BitmapFactory.decodeFile(picturePath).getWidth();
		int bitHeight = BitmapFactory.decodeFile(picturePath).getHeight();

		System.out.println("width : " + bitWidth + " bitHeight : " + bitHeight);

		if (bitWidth <= 2048 || bitHeight <= 1536) {
			opt.inSampleSize = 4;
		} else if ((bitHeight > 1536 && bitHeight <= 1944) || (bitWidth > 2048 && bitWidth <= 2592)) {
			opt.inSampleSize = 6;
		} else {
			opt.inSampleSize = 8;
		}

		bitmap = BitmapFactory.decodeFile(picturePath, opt);

		if (bitmap != null) {
			try {
				ExifInterface exif = new ExifInterface(picturePath);
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

				Log.e("orientation", orientation + "<<<");
				Matrix matrix = new Matrix();

				switch (orientation) {
				case 1:
					Log.v("Case:", "1");
					break;
				case 2:
					Log.v("Case:", "2");
					break;
				case 3:
					Log.v("Case:", "3");
					matrix.postRotate(180);
					break;
				case 4:
					Log.v("Case:", "4");
					break;
				case 5:
					Log.v("Case:", "5");
					break;
				case 6:
					Log.v("Case:", "6");
					matrix.postRotate(90);
					break;
				case 7:
					Log.v("Case:", "7");
					break;
				case 8:
					Log.v("Case:", "8");
					matrix.postRotate(-90);
					break;
				}

				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			} catch (IOException e) {
				e.printStackTrace();
			}
			ImageClass imageClass = new ImageClass();
			// imageClass.setBitmap(bitmap);
			imageClass.setBase64value(Util.getBase64StringFromBitmap(bitmap)); // <--
																				// TODO
																				// Use
																				// this
																				// bitmap
			if (isConstructionIntake) {
				imageClass.setImageType("Construction_Of_Intake_Jetty");
				imageClass.setImageCount(constructionIntakeImagesCount + 1);
				constructionIntakeImagesList.add(imageClass);
				isConstructionIntake = false;
				constructionIntakeImagesCount++;
			} else if (isLayingSuctionMain) {
				imageClass.setImageType("Laying_Of_Suction_Main");
				imageClass.setImageCount(layingSuctionImagesCount + 1);
				layingSuctionImagesList.add(imageClass);
				isLayingSuctionMain = false;
				layingSuctionImagesCount++;
			} else if (isConstructionRawWaterPumpHouse) {
				imageClass.setImageType("Construction_Of_Raw_Water_Pump_House");
				imageClass.setImageCount(constructionRawWaterPumpHouseImagesCount + 1);
				constructionRawWaterPumpHouseImagesList.add(imageClass);
				isConstructionRawWaterPumpHouse = false;
				constructionRawWaterPumpHouseImagesCount++;
			} else if (isInstallationPumpingElectricalMachine) {
				imageClass.setImageType("Installation_Of_Pumping_And_Electrical_Machinery");
				imageClass.setImageCount(installationPumpingElectricalMechImagesCount + 1);
				installationPumpingElectricalMechImagesList.add(imageClass);
				isInstallationPumpingElectricalMachine = false;
				installationPumpingElectricalMechImagesCount++;
			} else if (isConstructionSubstation) {
				imageClass.setImageType("Construction_Of_Substation_Electrical");
				imageClass.setImageCount(constructionSubStationImagesCount + 1);
				constructionSubStationImagesList.add(imageClass);
				isConstructionSubstation = false;
				constructionSubStationImagesCount++;
			} else if (isLayingRawWaterDeliveryMain) {
				imageClass.setImageType("Laying_Of_Raw_Water_Delivery_Main");
				imageClass.setImageCount(layingRawWaterDeliveryImagesCount + 1);
				layingRawWaterDeliveryImagesList.add(imageClass);
				isLayingRawWaterDeliveryMain = false;
				layingRawWaterDeliveryImagesCount++;
			}
		} else {
			isConstructionIntake = false;
			isLayingSuctionMain = false;
			isConstructionRawWaterPumpHouse = false;
			isInstallationPumpingElectricalMachine = false;
			isConstructionSubstation = false;
			isLayingRawWaterDeliveryMain = false;

			Toast.makeText(this, picturePath + "not found", Toast.LENGTH_LONG).show();
		}

		imageUpdateOnView();
	}

	private void imageUpdateOnView() {

		// for Construction of Intake Jetty
		ImageAdapter drinkingAdapter = new ImageAdapter(this, constructionIntakeImagesList);
		vp_ConstructionIntakeselectedImages.setAdapter(drinkingAdapter);

		if (constructionIntakeImagesList.size() == 0) {
			vp_ConstructionIntakeselectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstructionIntakeselectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstructionIntakeselectedImages.setCurrentItem(constructionIntakeImagesList.size() - 1);
		}

		if (constructionIntakeImagesList.size() <= 1) {
			tv_ConstructionIntakeImageProgress.setText("[Image added " + constructionIntakeImagesList.size() + "/3]");
			v_ConstructionIntakeswipeRight.setVisibility(View.INVISIBLE);
			v_ConstructionIntakeswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstructionIntakeImageProgress.setText("Slide to view other images\n[Images added " + constructionIntakeImagesList.size() + "/3]");
			v_ConstructionIntakeswipeRight.setVisibility(View.VISIBLE);
			v_ConstructionIntakeswipeLeft.setVisibility(View.VISIBLE);
		}

		// for Laying Of Suction main
		ImageAdapter sanitationAdapter = new ImageAdapter(this, layingSuctionImagesList);
		vp_LayinSuctionselectedImages.setAdapter(sanitationAdapter);

		if (layingSuctionImagesList.size() == 0) {
			vp_LayinSuctionselectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_LayinSuctionselectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_LayinSuctionselectedImages.setCurrentItem(layingSuctionImagesList.size() - 1);
		}

		if (layingSuctionImagesList.size() <= 1) {
			tv_LayinSuctionImageProgress.setText("[Image added " + layingSuctionImagesList.size() + "/3]");
			v_LayinSuctionswipeRight.setVisibility(View.INVISIBLE);
			v_LayinSuctionswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_LayinSuctionImageProgress.setText("Slide to view other images\n[Images added " + layingSuctionImagesList.size() + "/3]");
			v_LayinSuctionswipeRight.setVisibility(View.VISIBLE);
			v_LayinSuctionswipeLeft.setVisibility(View.VISIBLE);
		}

		// for Construction of Raw water Pump house
		ImageAdapter constructionRawWaterPumpHouseAdapter = new ImageAdapter(this, constructionRawWaterPumpHouseImagesList);
		vp_ConstPumpHouseselectedImages.setAdapter(constructionRawWaterPumpHouseAdapter);

		if (constructionRawWaterPumpHouseImagesList.size() == 0) {
			vp_ConstPumpHouseselectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstPumpHouseselectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstPumpHouseselectedImages.setCurrentItem(constructionRawWaterPumpHouseImagesList.size() - 1);
		}

		if (constructionRawWaterPumpHouseImagesList.size() <= 1) {
			tv_ConstPumpHouseImageProgress.setText("[Image added " + constructionRawWaterPumpHouseImagesList.size() + "/3]");
			v_ConstPumpHouseswipeRight.setVisibility(View.INVISIBLE);
			v_ConstPumpHouseswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstPumpHouseImageProgress.setText("Slide to view other images\n[Images added " + constructionRawWaterPumpHouseImagesList.size() + "/3]");
			v_ConstPumpHouseswipeRight.setVisibility(View.VISIBLE);
			v_ConstPumpHouseswipeLeft.setVisibility(View.VISIBLE);
		}

		// for Installation of Pumping and electrical machinery
		ImageAdapter installationPumpingElectricalMechAdapter = new ImageAdapter(this, installationPumpingElectricalMechImagesList);
		vp_InstallPumpselectedImages.setAdapter(installationPumpingElectricalMechAdapter);

		if (installationPumpingElectricalMechImagesList.size() == 0) {
			vp_InstallPumpselectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_InstallPumpselectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_InstallPumpselectedImages.setCurrentItem(installationPumpingElectricalMechImagesList.size() - 1);
		}

		if (installationPumpingElectricalMechImagesList.size() <= 1) {
			tv_InstallPumpImageProgress.setText("[Image added " + installationPumpingElectricalMechImagesList.size() + "/3]");
			v_InstallPumpswipeRight.setVisibility(View.INVISIBLE);
			v_InstallPumpswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_InstallPumpImageProgress.setText("Slide to view other images\n[Images added " + installationPumpingElectricalMechImagesList.size() + "/3]");
			v_InstallPumpswipeRight.setVisibility(View.VISIBLE);
			v_InstallPumpswipeLeft.setVisibility(View.VISIBLE);
		}

		// for Construction of substation
		ImageAdapter constructionSubStationAdapter = new ImageAdapter(this, constructionSubStationImagesList);
		vp_ConstructionSubstationselectedImages.setAdapter(constructionSubStationAdapter);

		if (constructionSubStationImagesList.size() == 0) {
			vp_ConstructionSubstationselectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstructionSubstationselectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstructionSubstationselectedImages.setCurrentItem(constructionSubStationImagesList.size() - 1);
		}

		if (constructionSubStationImagesList.size() <= 1) {
			tv_ConstructionSubstationImageProgress.setText("[Image added " + constructionSubStationImagesList.size() + "/3]");
			v_ConstructionSubstationswipeRight.setVisibility(View.INVISIBLE);
			v_ConstructionSubstationswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstructionSubstationImageProgress.setText("Slide to view other images\n[Images added " + constructionSubStationImagesList.size() + "/3]");
			v_ConstructionSubstationswipeRight.setVisibility(View.VISIBLE);
			v_ConstructionSubstationswipeLeft.setVisibility(View.VISIBLE);
		}

		// for Laying of raw water delivery main
		ImageAdapter layingRawWaterDeliveryAdapter = new ImageAdapter(this, layingRawWaterDeliveryImagesList);
		vp_WaterDeliverySelectedImages.setAdapter(layingRawWaterDeliveryAdapter);

		if (layingRawWaterDeliveryImagesList.size() == 0) {
			vp_WaterDeliverySelectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_WaterDeliverySelectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_WaterDeliverySelectedImages.setCurrentItem(layingRawWaterDeliveryImagesList.size() - 1);
		}

		if (layingRawWaterDeliveryImagesList.size() <= 1) {
			tv_WaterDeliveryImageProgress.setText("[Image added " + layingRawWaterDeliveryImagesList.size() + "/3]");
			v_WaterDeliveryswipeRight.setVisibility(View.INVISIBLE);
			v_WaterDeliveryswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_LayinSuctionImageProgress.setText("Slide to view other images\n[Images added " + layingRawWaterDeliveryImagesList.size() + "/3]");
			v_WaterDeliveryswipeRight.setVisibility(View.VISIBLE);
			v_WaterDeliveryswipeLeft.setVisibility(View.VISIBLE);
		}

	}

	private void postFormWebserviceCalling() {

		isDataPostingService = true;
		speedCheck();

	}

	private void sendingImages(final int position) {

		imagePosition = position;
		UserClass user = Util.fetchUserClass(mContext);
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("ID", user.getSurfaceIntakeRawWaterMainUpdateId());
		paramsMap.put("IMAGE_TYPE", allImagesList.get(position).getImageType());
		paramsMap.put("IMAGE_NO", String.valueOf(allImagesList.get(position).getImageCount()));
		paramsMap.put("IMAGE_DATA", allImagesList.get(position).getBase64value());

		isSurfaceIntakeRawWaterMainImageService = true;
		volleyTaskManager.doPostSurfaceIntakeWaterMainImage(paramsMap);
	}

	private void speedCheck() {
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SpeedTestService.MY_ACTION);
		registerReceiver(myReceiver, intentFilter);

		startService(new Intent(this, SpeedTestService.class));
		mProgressDialog.setMessage("Checking internet speed...");
		showProgressDialog();
	}

	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("MyReceiver", "MyReceiver");
			// TODO Receiving Data from SpeedTestService
			hideProgressDialog();
			boolean isStrongNetwork = intent.getBooleanExtra("NETWORK_CHECK", false);

			unregisterReceiver(myReceiver);

			UserClass user = Util.fetchUserClass(mContext);
			HashMap<String, String> requestMap = new HashMap<String, String>();
			Log.v("TAG", "Is Strong Network: " + isStrongNetwork);
			Log.v("TAG", "Is DAta  posting servicve: " + isDataPostingService);

			if (isDataPostingService) {

				requestMap.put("DISTRICT_ID", "" + districtId);
				requestMap.put("SCHEME_ID", schemeId);
				requestMap.put("SINKING_OF_PILE_FOR_INTAKE_JETTY_ISDONE", isSinkingPileIntakeJetty);
				requestMap.put("SINKING_OF_PILE_FOR_INTAKE_JETTY_IFNO_DESCRIPTION", etSinkingPileIntakeJettyDesc.getText().toString());
				requestMap.put("CONSTRUCTION_OF_INTAKE_JETTY_STATUS", constructionIntakeJettyStatus);
				requestMap.put("CONSTRUCTION_OF_INTAKE_JETTY_DATE_OF_START", tvConstIntakeDateOfStart.getText().toString().trim());
				requestMap.put("CONSTRUCTION_OF_INTAKE_JETTY_DATE_OF_COMPLETION", tvConstIntakeDateOfCompletion.getText().toString().trim());
				requestMap.put("LAYING_OF_SUCTION_MAIN_STATUS", layingSuctionMainStatus);
				requestMap.put("LAYING_OF_SUCTION_MAIN_DATE_OF_START", tvLayingSuctionDateOfStart.getText().toString().trim());
				requestMap.put("LAYING_OF_SUCTION_MAIN_DATE_OF_COMPLETION", tvLayingSuctionDateOfCompletion.getText().toString().trim());
				requestMap.put("CONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_STATUS", constRawWaterPumpHouseStatus);
				requestMap.put("CONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_DATE_OF_START", tvConstWaterPumpDateOfStart.getText().toString().trim());
				requestMap.put("CONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_DATE_OF_COMPLETION", tvConstWaterPumpDateOfCompletion.getText().toString().trim());
				requestMap.put("INSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_STATUS", installPumpMachineryStatus);
				requestMap.put("INSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_DATE_OF_START", tvMachineryInstallDateOfStart.getText().toString().trim());
				requestMap.put("INSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_DATE_OF_COMPLETION", tvMachineryInstallDateOfCompletion.getText().toString()
						.trim());
				requestMap.put("CONSTRUCTION_OF_SUBSTATION_ELECTRICAL_STATUS", constructionSubstationStatus);
				requestMap.put("CONSTRUCTION_OF_SUBSTATION_ELECTRICAL_DATE_OF_START", tvConstSubStationDateOfStart.getText().toString().trim());
				requestMap.put("CONSTRUCTION_OF_SUBSTATION_ELECTRICAL_DATE_OF_COMPLETION", tvConstSubStationDateOfCompletion.getText().toString().trim());
				requestMap.put("LAYING_OF_RAW_WATER_DELIVERY_MAIN_STATUS", waterDeliveryStatus);
				requestMap.put("LAYING_OF_RAW_WATER_DELIVERY_MAIN_DATE_OF_START", tvLayWaterDeliveryDateOfStart.getText().toString().trim());
				requestMap.put("LAYING_OF_RAW_WATER_DELIVERY_MAIN_DATE_OF_COMPLETION", tvLayWaterDeliveryDateOfCompletion.getText().toString().trim());
				requestMap.put("LAT", tv_latitude.getText().toString().trim());
				requestMap.put("LON", tv_longitude.getText().toString().trim());
				requestMap.put("CREATED_BY_ID", user.getDatabaseId());
				// Log.v("RequestMAp", "" + new
				// JSONObject(requestMap).toString());

				// for (FacilityClass facilityObj : facilityList) {
				// paramsMap.put(facilityObj.getTag(),
				// facilityObj.getIsSelected() + "");
				// }

				if (isStrongNetwork) {
					// isDataPostingService = true;
					if (isInsert) {
						isInsertService = true;
						volleyTaskManager.doPostSurfaceInsertIntakeRawWater(requestMap);
					} else if (isUpdate) {
						isUpdateService = true;
						requestMap.put("ID", user.getSurfaceIntakeRawWaterMainUpdateId());
						volleyTaskManager.doPostSurfaceUpdateIntakeAndRawWater(requestMap);
					}
					// writeToFile(new JSONObject(requestMap).toString());
				} else {

					// =================SAVE OFFLINE HERE

					// TODO OFFLINE POSTING - add condition here -->
					OfflineDataSet offlineDataSet = new OfflineDataSet();
					// offlineDataSet.setServiceType("Plantation");
					offlineDataSet.setParamsMap(requestMap);
					if (allImagesList.size() > 0) {
						offlineDataSet.setImagesList(allImagesList);
					}

					ArrayList<OfflineDataSet> offlineDataSetList = Util.fetchOfflineDataSetList(mContext);
					if (offlineDataSetList == null) {
						offlineDataSetList = new ArrayList<OfflineDataSet>();
					}
					offlineDataSetList.add(offlineDataSet);
					Util.saveOfflineDataSetList(mContext, offlineDataSetList);

					Util.showCallBackMessageWithOk(mContext, getString(R.string.offline_storage), new AlertDialogCallBack() {

						@Override
						public void onSubmit() {
							finish();
						}

						@Override
						public void onCancel() {

						}
					});
					Util.showMessageWithOk(mContext, "Slow Network Connection");
				}
			}

		}
	}

	@Override
	public void onSuccess(JSONObject resultJsonObject) {

		volleyTaskManager.hideProgressDialog();

		Log.d("On Success", "" + resultJsonObject.toString());

		if (isDataPostingService) {

			isDataPostingService = false;

			if (resultJsonObject.toString() != null && !resultJsonObject.toString().trim().isEmpty()) {

				try {
					UserClass user = Util.fetchUserClass(mContext);

					String result = "";

					if (isInsertService) {

						isInsertService = false;
						result = new JSONObject(resultJsonObject.optString("SaveSurfaceIntakeAndRawWaterMainResult")).optString("RES");

					} else if (isUpdateService) {

						isUpdateService = false;
						result = new JSONObject(resultJsonObject.optString("UpdateSurfaceIntakeAndRawWaterMainResult")).optString("RES");
					}
					Log.v("TAG", "" + result);

					if (!result.equalsIgnoreCase("0")) {
						user.setSurfaceIntakeRawWaterMainUpdateId(result);
						Util.saveUserClass(mContext, user);
						if (allImagesList.size() > 0) {
							sendingImages(0);
						} else {
							Util.showCallBackMessageWithOk(mContext, "Data Posted successfully.", new AlertDialogCallBack() {

								@Override
								public void onSubmit() {
									finish();
								}

								@Override
								public void onCancel() {

								}
							});
						}

					} else
						Toast.makeText(mContext, " Request failed. Please try again.", Toast.LENGTH_SHORT).show();

				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(mContext, " Request failed. Please try again.", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(mContext, " Request failed. Please try again.", Toast.LENGTH_SHORT).show();
			}

		} else if (isSurfaceIntakeRawWaterMainImageService) {

			isSurfaceIntakeRawWaterMainImageService = false;

			Log.d("Sent position:", imagePosition + "");

			String FDSaveSchoolImageDataResult = resultJsonObject.optString("UploadSurfaceIntakeAndRawWaterMainImageResult");
			UserClass user = Util.fetchUserClass(mContext);

			if (user.getSurfaceIntakeRawWaterMainUpdateId().equalsIgnoreCase(FDSaveSchoolImageDataResult)) {
				allImagesList.get(imagePosition).setIsPosted(true);
			}
			if (imagePosition < allImagesList.size() - 1) {

				sendingImages(imagePosition + 1);
			} else {

				Log.e("Image Sending", "All Completed");

				Util.showCallBackMessageWithOk(mContext, "Data Posted successfully.", new AlertDialogCallBack() {

					@Override
					public void onSubmit() {
						finish();
					}

					@Override
					public void onCancel() {

					}
				});
			}

		} else if (isFetchDataService) {
			isFetchDataService = false;
			parseInitialFetchedData(resultJsonObject);
		}

	}

	@Override
	public void onError() {

	}

	private void fetchFormData() {
		if (Util.checkConnectivity(mContext)) {

			fetchSurfaceIntakeRawWaterMain(schemeId);
		} else {
			Util.showCallBackMessageWithOk(mContext, "No Internet Connection.", new AlertDialogCallBack() {
				@Override
				public void onSubmit() {

				}

				@Override
				public void onCancel() {

				}
			});
		}

	}

	private void fetchSurfaceIntakeRawWaterMain(String schemeId) {

		isFetchDataService = true;

		String value = "?scheme_id=" + schemeId;

		volleyTaskManager.doGetSurfaceIntakeAndRawWaterMain(value);
	}

	private void parseInitialFetchedData(JSONObject resultJsonObject) {

		try {

			JSONArray resultJSONArray = new JSONArray(resultJsonObject.optString("GetSurfaceIntakeAndRawWaterMainResult"));

			if (resultJSONArray.length() > 0) {
				ArrayList<IntakeRawWaterMainClass> intakeRawWaterMainList = new ArrayList<IntakeRawWaterMainClass>();

				for (int i = 0; i < resultJSONArray.length(); i++) {

					JSONObject intakeRawWaterMainJSONObject = resultJSONArray.getJSONObject(i);
					IntakeRawWaterMainClass intakeRawWaterMain = new IntakeRawWaterMainClass();
					intakeRawWaterMain.setCONSTRUCTION_OF_INTAKE_JETTY_DATE_OF_COMPLETION(intakeRawWaterMainJSONObject
							.optString("CONSTRUCTION_OF_INTAKE_JETTY_DATE_OF_COMPLETION"));
					intakeRawWaterMain.setCONSTRUCTION_OF_INTAKE_JETTY_DATE_OF_START(intakeRawWaterMainJSONObject
							.optString("CONSTRUCTION_OF_INTAKE_JETTY_DATE_OF_START"));
					intakeRawWaterMain.setCONSTRUCTION_OF_INTAKE_JETTY_STATUS(intakeRawWaterMainJSONObject.optString("CONSTRUCTION_OF_INTAKE_JETTY_STATUS"));
					intakeRawWaterMain.setCONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_DATE_OF_COMPLETION(intakeRawWaterMainJSONObject
							.optString("CONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_DATE_OF_COMPLETION"));
					intakeRawWaterMain.setCONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_DATE_OF_START(intakeRawWaterMainJSONObject
							.optString("CONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_DATE_OF_START"));
					intakeRawWaterMain.setCONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_STATUS(intakeRawWaterMainJSONObject
							.optString("CONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_STATUS"));
					intakeRawWaterMain.setCONSTRUCTION_OF_SUBSTATION_ELECTRICAL_DATE_OF_COMPLETION(intakeRawWaterMainJSONObject
							.optString("CONSTRUCTION_OF_SUBSTATION_ELECTRICAL_DATE_OF_COMPLETION"));
					intakeRawWaterMain.setCONSTRUCTION_OF_SUBSTATION_ELECTRICAL_DATE_OF_START(intakeRawWaterMainJSONObject
							.optString("CONSTRUCTION_OF_SUBSTATION_ELECTRICAL_DATE_OF_START"));
					intakeRawWaterMain.setCONSTRUCTION_OF_SUBSTATION_ELECTRICAL_STATUS(intakeRawWaterMainJSONObject
							.optString("CONSTRUCTION_OF_SUBSTATION_ELECTRICAL_STATUS"));
					intakeRawWaterMain.setCREATED_BY_ID(intakeRawWaterMainJSONObject.optString("CREATED_BY_ID"));
					intakeRawWaterMain.setDISTRICT_ID(intakeRawWaterMainJSONObject.optString("DISTRICT_ID"));
					intakeRawWaterMain.setID(intakeRawWaterMainJSONObject.optString("ID"));
					intakeRawWaterMain.setINSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_DATE_OF_COMPLETION(intakeRawWaterMainJSONObject
							.optString("INSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_DATE_OF_COMPLETION"));
					intakeRawWaterMain.setINSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_DATE_OF_START(intakeRawWaterMainJSONObject
							.optString("INSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_DATE_OF_START"));
					intakeRawWaterMain.setINSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_STATUS(intakeRawWaterMainJSONObject
							.optString("INSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_STATUS"));
					intakeRawWaterMain.setLAYING_OF_RAW_WATER_DELIVERY_MAIN_DATE_OF_COMPLETION(intakeRawWaterMainJSONObject
							.optString("LAYING_OF_RAW_WATER_DELIVERY_MAIN_DATE_OF_COMPLETION"));
					intakeRawWaterMain.setLAYING_OF_RAW_WATER_DELIVERY_MAIN_DATE_OF_START(intakeRawWaterMainJSONObject
							.optString("LAYING_OF_RAW_WATER_DELIVERY_MAIN_DATE_OF_START"));
					intakeRawWaterMain.setLAYING_OF_RAW_WATER_DELIVERY_MAIN_STATUS(intakeRawWaterMainJSONObject
							.optString("LAYING_OF_RAW_WATER_DELIVERY_MAIN_STATUS"));
					intakeRawWaterMain.setLAYING_OF_SUCTION_MAIN_DATE_OF_COMPLETION(intakeRawWaterMainJSONObject
							.optString("LAYING_OF_SUCTION_MAIN_DATE_OF_COMPLETION"));
					intakeRawWaterMain.setLAYING_OF_SUCTION_MAIN_DATE_OF_START(intakeRawWaterMainJSONObject.optString("LAYING_OF_SUCTION_MAIN_DATE_OF_START"));
					intakeRawWaterMain.setLAYING_OF_SUCTION_MAIN_STATUS(intakeRawWaterMainJSONObject.optString("LAYING_OF_SUCTION_MAIN_STATUS"));
					intakeRawWaterMain.setSCHEME_ID(intakeRawWaterMainJSONObject.optString("SCHEME_ID"));
					intakeRawWaterMain.setSINKING_OF_PILE_FOR_INTAKE_JETTY_IFNO_DESCRIPTION(intakeRawWaterMainJSONObject
							.optString("SINKING_OF_PILE_FOR_INTAKE_JETTY_IFNO_DESCRIPTION"));
					intakeRawWaterMain.setSINKING_OF_PILE_FOR_INTAKE_JETTY_ISDONE(intakeRawWaterMainJSONObject
							.optString("SINKING_OF_PILE_FOR_INTAKE_JETTY_ISDONE"));

					UserClass user = Util.fetchUserClass(mContext);
					user.setSurfaceIntakeRawWaterMainUpdateId(intakeRawWaterMain.getID());
					Util.saveUserClass(mContext, user);

					intakeRawWaterMainList.add(intakeRawWaterMain);

				}

				isInsert = false;

				isUpdate = true;

				populateForm(intakeRawWaterMainList);
			} else {

				isInsert = true;
				isUpdate = false;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void populateForm(ArrayList<IntakeRawWaterMainClass> intakeRawWaterMainList) {

		for (IntakeRawWaterMainClass intakeRawWaterMain : intakeRawWaterMainList) {

			if (intakeRawWaterMain.getSINKING_OF_PILE_FOR_INTAKE_JETTY_ISDONE().trim().equalsIgnoreCase("False")) {

				isSinkingPileIntakeJetty = "false";
				rbSinkingPileIntakeJetty_No.setChecked(true);
				etSinkingPileIntakeJettyDesc.setText(intakeRawWaterMain.getSINKING_OF_PILE_FOR_INTAKE_JETTY_IFNO_DESCRIPTION());
				onSinkingPileIntakeJetty_NoClick();
			} else {

				isSinkingPileIntakeJetty = "true";
				rbSinkingPileIntakeJetty_Yes.setChecked(true);
				onSinkingPileIntakeJetty_YesClick();
			}

			if (intakeRawWaterMain.getCONSTRUCTION_OF_INTAKE_JETTY_STATUS() != null) {
				if (intakeRawWaterMain.getCONSTRUCTION_OF_INTAKE_JETTY_STATUS().equalsIgnoreCase("Yet to be Started")) {
					constructionIntakeJettyStatus = "Yet to be Started";
					cbConstructionIntakeToBeStarted.setChecked(true);
					cbConstructionIntakeWorkInProg.setChecked(false);
					cbConstructionIntakeCompleted.setChecked(false);

				} else if (intakeRawWaterMain.getCONSTRUCTION_OF_INTAKE_JETTY_STATUS().equalsIgnoreCase("Work in Progress")) {
					constructionIntakeJettyStatus = "Work in Progress";
					cbConstructionIntakeToBeStarted.setChecked(false);
					cbConstructionIntakeWorkInProg.setChecked(true);
					cbConstructionIntakeCompleted.setChecked(false);

				} else if (intakeRawWaterMain.getCONSTRUCTION_OF_INTAKE_JETTY_STATUS().equalsIgnoreCase("Completed")) {
					constructionIntakeJettyStatus = "Completed";
					cbConstructionIntakeToBeStarted.setChecked(false);
					cbConstructionIntakeWorkInProg.setChecked(false);
					cbConstructionIntakeCompleted.setChecked(true);

				}

				tvConstIntakeDateOfStart.setText(intakeRawWaterMain.getCONSTRUCTION_OF_INTAKE_JETTY_DATE_OF_START());
				tvConstIntakeDateOfCompletion.setText(intakeRawWaterMain.getCONSTRUCTION_OF_INTAKE_JETTY_DATE_OF_COMPLETION());

			}
			if (intakeRawWaterMain.getLAYING_OF_SUCTION_MAIN_STATUS() != null) {
				if (intakeRawWaterMain.getLAYING_OF_SUCTION_MAIN_STATUS().equalsIgnoreCase("Yet to be Started")) {
					layingSuctionMainStatus = "Yet to be Started";
					cbLayingSuctionToBeStarted.setChecked(true);
					cbLayingSuctionWorkInProg.setChecked(false);
					cbLayingSuctionCompleted.setChecked(false);

				} else if (intakeRawWaterMain.getLAYING_OF_SUCTION_MAIN_STATUS().equalsIgnoreCase("Work in Progress")) {
					layingSuctionMainStatus = "Work in Progress";
					cbLayingSuctionToBeStarted.setChecked(false);
					cbLayingSuctionWorkInProg.setChecked(true);
					cbLayingSuctionCompleted.setChecked(false);

				} else if (intakeRawWaterMain.getLAYING_OF_SUCTION_MAIN_STATUS().equalsIgnoreCase("Completed")) {
					layingSuctionMainStatus = "Completed";
					cbLayingSuctionToBeStarted.setChecked(false);
					cbLayingSuctionWorkInProg.setChecked(false);
					cbLayingSuctionCompleted.setChecked(true);

				}

				tvLayingSuctionDateOfStart.setText(intakeRawWaterMain.getLAYING_OF_SUCTION_MAIN_DATE_OF_START());
				tvLayingSuctionDateOfCompletion.setText(intakeRawWaterMain.getLAYING_OF_SUCTION_MAIN_DATE_OF_COMPLETION());
			}
			if (intakeRawWaterMain.getCONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_STATUS() != null) {
				if (intakeRawWaterMain.getCONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_STATUS().equalsIgnoreCase("Yet to be Started")) {
					constRawWaterPumpHouseStatus = "Yet to be Started";
					cbConstPumpHouseToBeStarted.setChecked(true);
					cbConstPumpHouseProgress.setChecked(false);
					cbConstPumpHouseCompleted.setChecked(false);

				} else if (intakeRawWaterMain.getCONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_STATUS().equalsIgnoreCase("Work in Progress")) {
					constRawWaterPumpHouseStatus = "Work in Progress";
					cbConstPumpHouseToBeStarted.setChecked(false);
					cbConstPumpHouseProgress.setChecked(true);
					cbConstPumpHouseCompleted.setChecked(false);

				} else if (intakeRawWaterMain.getCONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_STATUS().equalsIgnoreCase("Completed")) {
					constRawWaterPumpHouseStatus = "Completed";
					cbConstPumpHouseToBeStarted.setChecked(false);
					cbConstPumpHouseProgress.setChecked(false);
					cbConstPumpHouseCompleted.setChecked(true);

				}
				tvConstWaterPumpDateOfStart.setText(intakeRawWaterMain.getCONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_DATE_OF_START());
				tvConstWaterPumpDateOfCompletion.setText(intakeRawWaterMain.getCONSTRUCTION_OF_RAW_WATER_PUMP_HOUSE_DATE_OF_COMPLETION());

			}
			if (intakeRawWaterMain.getINSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_STATUS() != null) {
				if (intakeRawWaterMain.getINSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_STATUS().equalsIgnoreCase("Yet to be Started")) {
					installPumpMachineryStatus = "Yet to be Started";
					cbInstallPumpToBeStarted.setChecked(true);
					cbInstallPumpInProgress.setChecked(false);
					cbInstallPumpCompleted.setChecked(false);

				} else if (intakeRawWaterMain.getINSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_STATUS().equalsIgnoreCase("Work in Progress")) {
					installPumpMachineryStatus = "Work in Progress";
					cbInstallPumpToBeStarted.setChecked(false);
					cbInstallPumpInProgress.setChecked(true);
					cbInstallPumpCompleted.setChecked(false);

				} else if (intakeRawWaterMain.getINSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_STATUS().equalsIgnoreCase("Completed")) {
					installPumpMachineryStatus = "Completed";
					cbInstallPumpToBeStarted.setChecked(false);
					cbInstallPumpInProgress.setChecked(false);
					cbInstallPumpCompleted.setChecked(true);

				}

				tvMachineryInstallDateOfStart.setText(intakeRawWaterMain.getINSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_DATE_OF_START());
				tvMachineryInstallDateOfCompletion.setText(intakeRawWaterMain.getINSTALLATION_OF_PUMPING_AND_ELECTRICAL_MACHINERY_DATE_OF_COMPLETION());
			}
			if (intakeRawWaterMain.getCONSTRUCTION_OF_SUBSTATION_ELECTRICAL_STATUS() != null) {
				if (intakeRawWaterMain.getCONSTRUCTION_OF_SUBSTATION_ELECTRICAL_STATUS().equalsIgnoreCase("Yet to be Started")) {
					constructionSubstationStatus = "Yet to be Started";
					cbConstructionSubstationToBeStarted.setChecked(true);
					cbConstructionSubstationInProgress.setChecked(false);
					cbConstructionSubstationCompleted.setChecked(false);

				} else if (intakeRawWaterMain.getCONSTRUCTION_OF_SUBSTATION_ELECTRICAL_STATUS().equalsIgnoreCase("Work in Progress")) {
					constructionSubstationStatus = "Work in Progress";
					cbConstructionSubstationToBeStarted.setChecked(false);
					cbConstructionSubstationInProgress.setChecked(true);
					cbConstructionSubstationCompleted.setChecked(false);
				} else if (intakeRawWaterMain.getCONSTRUCTION_OF_SUBSTATION_ELECTRICAL_STATUS().equalsIgnoreCase("Completed")) {
					constructionSubstationStatus = "Completed";
					cbConstructionSubstationToBeStarted.setChecked(false);
					cbConstructionSubstationInProgress.setChecked(false);
					cbConstructionSubstationCompleted.setChecked(true);

				}
				tvConstSubStationDateOfStart.setText(intakeRawWaterMain.getCONSTRUCTION_OF_SUBSTATION_ELECTRICAL_DATE_OF_START());
				tvConstSubStationDateOfCompletion.setText(intakeRawWaterMain.getCONSTRUCTION_OF_SUBSTATION_ELECTRICAL_DATE_OF_COMPLETION());

			}
			if (intakeRawWaterMain.getLAYING_OF_RAW_WATER_DELIVERY_MAIN_STATUS() != null) {
				if (intakeRawWaterMain.getLAYING_OF_RAW_WATER_DELIVERY_MAIN_STATUS().equalsIgnoreCase("Yet to be Started")) {
					waterDeliveryStatus = "Yet to be Started";
					cbWaterDeliveryToBeStarted.setChecked(true);
					cbWaterDeliveryInProgress.setChecked(false);
					cbWaterDeliveryCompleted.setChecked(false);

				} else if (intakeRawWaterMain.getLAYING_OF_RAW_WATER_DELIVERY_MAIN_STATUS().equalsIgnoreCase("Work in Progress")) {
					waterDeliveryStatus = "Work in Progress";
					cbWaterDeliveryToBeStarted.setChecked(false);
					cbWaterDeliveryInProgress.setChecked(true);
					cbWaterDeliveryCompleted.setChecked(false);

				} else if (intakeRawWaterMain.getLAYING_OF_RAW_WATER_DELIVERY_MAIN_STATUS().equalsIgnoreCase("Completed")) {
					waterDeliveryStatus = "Completed";
					cbWaterDeliveryToBeStarted.setChecked(false);
					cbWaterDeliveryInProgress.setChecked(false);
					cbWaterDeliveryCompleted.setChecked(true);

				}
				tvLayWaterDeliveryDateOfStart.setText(intakeRawWaterMain.getCONSTRUCTION_OF_SUBSTATION_ELECTRICAL_DATE_OF_START());
				tvLayWaterDeliveryDateOfCompletion.setText(intakeRawWaterMain.getCONSTRUCTION_OF_SUBSTATION_ELECTRICAL_DATE_OF_COMPLETION());

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
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, SurfaceIntakeRawWaterActivity.this, requestCode);
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
		Util.trimCache(SurfaceIntakeRawWaterActivity.this);
	}

	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		Log.d("EntryFormActivity", "Location update stopped .......................");
	}
}
