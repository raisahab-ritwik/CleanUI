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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyberswift.phe.R;
import com.cyberswift.phe.activity.BaseActivity;
import com.cyberswift.phe.adapter.ImageAdapter;
import com.cyberswift.phe.dto.ImageClass;
import com.cyberswift.phe.dto.OfflineDataSet;
import com.cyberswift.phe.dto.UserClass;
import com.cyberswift.phe.dto.WaterTreatementClass;
import com.cyberswift.phe.services.SpeedTestService;
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

public class SurfaceWaterTreatementActivity extends BaseActivity implements OnClickListener, ServerResponseCallback, LocationListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private Context mContext;

	// ============ Construction of Intake Well
	private CheckBox cbConstructionInakeToBeStarted, cbConstructionInakeWorkOnProgress, cbConstructionInakeCompleted;
	private TextView tvConstructionIntakeStartDate, tvConstructionIntakeCompletiontDate;
	private RelativeLayout rlConstructionIntakeStartDate, rlConstructionIntakeCompletiontDate;
	private Button btn_ConstructionIntakePhoto;
	private ViewPager vp_ConstructionIntakeSelectedImages;
	private TextView tv_ConstructionIntakeWellImageProgress;
	private View v_ConstructionIntakeswipeLeft, v_ConstructionIntakeswipeRight;

	// ============ Construction of Flush Mixture
	private CheckBox cbConstructionFlushToBeStarted, cbConstructionFlushInProgress, cbConstructionFlushCompleted;
	private TextView tvConstructionFlushStartDate, tvConstructionFlushCompletiontDate;
	private RelativeLayout rlConstructionFlushStartDate, rlConstructionFlushCompletionDate;
	private Button btn_ConstructionFlushPhoto;
	private ViewPager vp_ConstructionFlushSelectedImages;
	private TextView tv_ConstructionFlushMixtureProgress;
	private View v_ConstructionFlushswipeLeft, v_ConstructionFlushswipeRight;

	// ============ Construction of Clariflocculator or plate settler or Tube
	// settler
	private CheckBox cbConstructionClariflocculatorToBeStarted, cbConstructionClariflocculatorInProgress, cbConstructionClariflocculatorCompleted;
	private TextView tvConstructionClariflocculatorStartDate, tvConstructionClariflocculatorCompletiontDate;
	private RelativeLayout rlConstructionClariflocculatorStartDate, rlConstructionClariflocculatorCompletedDate;
	private Button btn_ConstructionClariflocculatorPhoto;
	private ViewPager vp_ConstructionClariflocculatorSelectedImages;
	private TextView tv_ConstructionClariflocculatorProgress;
	private View v_ConstructionClariflocculatorswipeLeft, v_ConstructionClariflocculatorswipeRight;

	// ============ Construction of Rapid Sand Filer
	private CheckBox cbConstructionRapidSandFilterToBeStarted, cbConstructionRapidSandFilterInProgress, cbConstructionRapidSandFilterCompleted;
	private TextView tvConstructionRapidSandFilterStartDate, tvConstructionRapidSandFilterCompletedDate;
	private RelativeLayout rlConstructionRapidSandFilterStartDate, rlConstructionRapidSandFilterCompletedDate;
	private Button btn_ConstructionRapidSandFilterPhoto;
	private ViewPager vp_ConstructionRapidSandFilterSelectedImages;
	private TextView tv_ConstructionRapidSandFilterProgress;
	private View v_ConstructionRapidSandFilterswipeLeft, v_ConstructionRapidSandFilterswipeRight;

	// ============ Construction of Clear Water Pump House
	private CheckBox cbConstructionClearWaterPumpHouseToBeStarted, cbConstructionClearWaterPumpHouseInProgress, cbConstructionClearWaterPumpHouseCompleted;
	private TextView tvConstructionClearWaterPumpHouseStartDate, tvConstructionClearWaterPumpHouseCompletedDate;
	private RelativeLayout rlConstructionClearWaterPumpHouseStartDate, rlConstructionClearWaterPumpHouseCompletedDate;
	private Button btn_ConstructionClearWaterPumpHousePhoto;
	private ViewPager vp_ConstructionClearWaterPumpHouseSelectedImages;
	private TextView tv_ConstructionClearWaterPumpHouseProgress;
	private View v_ConstructionClearWaterPumpHouseswipeLeft, v_ConstructionClearWaterPumpHouseswipeRight;

	// ============ Installation of Electrical and Mechanical Machinery
	private CheckBox cbInstallationElectricalMechanicalMachineryToBeStarted, cbInstallationElectricalMechanicalMachineryInProgress,
			cbInstallationElectricalMechanicalMachineryCompleted;
	private TextView tvInstallationElectricalMechanicalMachineryStartDate, tvInstallationElectricalMechanicalMachineryCompletedDate;
	private RelativeLayout rlInstallationElectricalMechanicalMachineryStartDate, rlInstallationElectricalMechanicalMachineryCompletedDate;
	private Button btn_InstallationElectricalMechanicalMachineryPhoto;
	private ViewPager vp_InstallationElectricalMechanicalMachinerySelectedImages;
	private TextView tv_InstallationElectricalMechanicalMachineryProgress;
	private View v_InstallationElectricalMechanicalMachineryswipeLeft, v_InstallationElectricalMechanicalMachineryswipeRight;

	// ============ Construction of Chemical House
	private CheckBox cbConstructionChemicalHouseToBeStarted, cbConstructionChemicalHouseInProgress, cbConstructionChemicalHouseCompleted;
	private TextView tvConstructionChemicalHouseStartDate, tvConstructionChemicalHouseCompletedDate;
	private RelativeLayout rlConstructionChemicalHouseStartDate, rlConstructionChemicalHouseCompletedDate;
	private Button btn_ConstructionChemicalHousePhoto;
	private ViewPager vp_ConstructionChemicalHouseSelectedImages;
	private TextView tv_ConstructionChemicalHouseProgress;
	private View v_ConstructionChemicalHouseswipeLeft, v_ConstructionChemicalHouseswipeRight;

	// ============ Construction of Chlorine Room and Installation of
	// Chlorination Arrangement
	private CheckBox cbConstructionInstallationChlorineRoomToBeStarted, cbConstructionInstallationChlorineRoomInProgress,
			cbConstructionInstallationChlorineRoomCompleted;
	private TextView tvConstructionInstallationChlorineRoomStartDate, tvConstructionInstallationChlorineRoomCompletionDate;
	private RelativeLayout rlConstructionInstallationChlorineRoomStartDate, rlConstructionInstallationChlorineRoomCompletionDate;
	private Button btn_ConstructionInstallationChlorineRoomPhoto;
	private ViewPager vp_ConstructionInstallationChlorineRoomSelectedImages;
	private TextView tv_ConstructionInstallChlorineRoomProgress;
	private View v_ConstructionInstallationChlorineRoomswipeLeft, v_ConstructionInstallationChlorineRoomswipeRight;

	private TextView tv_latitude, tv_longitude;

	/*-----------------------------------------------*/

	private int districtId = 0;
	private String schemeId = "";
	private String constructionIntakeWellStatus = "";

	private DatePickerDialog.OnDateSetListener constructionIntakeWellStartDateListener;
	private DatePickerDialog.OnDateSetListener constructionIntakeWellStopDateListener;

	private String constructionFlushMixtureStatus = "";

	private DatePickerDialog.OnDateSetListener constructionFlushMixtureStartDateListener;
	private DatePickerDialog.OnDateSetListener constructionFlushMixtureStopDateListener;

	private String constructionClariflocculatorStatus = "";

	private DatePickerDialog.OnDateSetListener constructionClariflocculatorStartDateListener;
	private DatePickerDialog.OnDateSetListener constructionClariflocculatorStopDateListener;

	private String constructionRapidSandFilterStatus = "";

	private DatePickerDialog.OnDateSetListener constructionRapidSandFilterStartDateListener;
	private DatePickerDialog.OnDateSetListener constructionRapidSandFilterStopDateListener;

	private String constructionClearWaterPumpHouseStatus = "";

	private DatePickerDialog.OnDateSetListener constructionClearWaterPumpHouseStartDateListener;
	private DatePickerDialog.OnDateSetListener constructionClearWaterPumpHouseStopDateListener;

	private String installationElectricalMechanicalMachineryStatus = "";

	private DatePickerDialog.OnDateSetListener installationElectricalMechanicalMachineryStartDateListener;
	private DatePickerDialog.OnDateSetListener installationElectricalMechanicalMachineryStopDateListener;

	private String constructionChemicalHouseStatus = "";

	private DatePickerDialog.OnDateSetListener constructionChemicalHouseStartDateListener;
	private DatePickerDialog.OnDateSetListener constructionChemicalHouseStopDateListener;

	private String constructionChlorineRoomStatus = "";

	private DatePickerDialog.OnDateSetListener constructionChlorineRoomStartDateListener;
	private DatePickerDialog.OnDateSetListener constructionChlorineRoomStopDateListener;

	/*-----------------------------------------------*/
	private boolean isConstructionIntakeWell = false;
	private boolean isConstructionFlushMixture = false;
	private boolean isConstructionClariflocculator = false;
	private boolean isConstructionRapidSandFilter = false;
	private boolean isConstructionClearWaterPumpHouse = false;
	private boolean isInstallationElectricalMechanicalMachinery = false;
	private boolean isConstructionChemicalHouse = false;
	private boolean isConstructionChlorineRoom = false;
	private ProgressDialog mProgressDialog;
	private ArrayList<ImageClass> constructionIntakeWellImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> constructionFlushMixtureImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> constructionClariflocculatorImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> constructionRapidSandFilterImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> constructionClearWaterPumpHouseImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> installationElectricalMechanicalMachineryImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> constructionChemicalHouseImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> constructionChlorineRoomImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> allImagesList = new ArrayList<ImageClass>();

	@SuppressWarnings("unused")
	private static final int DATE_DIALOG_ID = 316, PICTURE_GALLERY_REQUEST = 2572, CAMERA_PIC_REQUEST = 1337;

	private Uri mCapturedImageURI;

	private int constructionIntakeWellImagesCount = 0;
	private int constructionFlushMixtureImagesCount = 0;
	private int constructionClariflocculatorImagesCount = 0;
	private int constructionRapidSandFilterImagesCount = 0;
	private int constructionClearWaterPumpHouseImagesCount = 0;
	private int installationElectricalMechanicalMachineryImagesCount = 0;
	private int constructionChemicalHouseImagesCount = 0;
	private int constructionChlorineRoomImagesCount = 0;
	private VolleyTaskManager volleyTaskManager;
	MyReceiver myReceiver;
	private boolean isDataPostingService = false;

	private boolean isInsertService = false;
	private boolean isUpdateService = false;

	private int imagePosition;
	private boolean isSurfaceWaterTreatementImageService = false;

	private boolean isFetchDataService = false;

	private boolean isInsert = false;
	private boolean isUpdate = false;

	// ============LOCATION

	private Location mCurrentLocation;
	private LocationRequest mLocationRequest;
	private AlertDialog systemAlertDialog;
	private FusedLocationProviderApi fusedLocationProviderApi;
	private GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.surface_water_treatment_plant);

		mContext = SurfaceWaterTreatementActivity.this;

		initView();
		fetchValuesFromLandingPage();
		fetchFormData();
	}

	private void initView() {

		// ============ Construction of Intake Well
		cbConstructionInakeToBeStarted = (CheckBox) findViewById(R.id.cbConstructionInakeToBeStarted);
		cbConstructionInakeWorkOnProgress = (CheckBox) findViewById(R.id.cbConstructionInakeWorkOnProgress);
		cbConstructionInakeCompleted = (CheckBox) findViewById(R.id.cbConstructionInakeCompleted);
		tvConstructionIntakeStartDate = (TextView) findViewById(R.id.tvConstructionIntakeStartDate);
		tvConstructionIntakeCompletiontDate = (TextView) findViewById(R.id.tvConstructionIntakeCompletiontDate);
		rlConstructionIntakeStartDate = (RelativeLayout) findViewById(R.id.rlConstructionIntakeStartDate);
		rlConstructionIntakeCompletiontDate = (RelativeLayout) findViewById(R.id.rlConstructionIntakeCompletiontDate);
		btn_ConstructionIntakePhoto = (Button) findViewById(R.id.btn_ConstructionIntakePhoto);
		vp_ConstructionIntakeSelectedImages = (ViewPager) findViewById(R.id.vp_ConstructionIntakeSelectedImages);

		tv_ConstructionIntakeWellImageProgress = (TextView) findViewById(R.id.tv_ConstructionIntakeWellImageProgress);
		v_ConstructionIntakeswipeLeft = (View) findViewById(R.id.v_ConstructionIntakeswipeLeft);
		v_ConstructionIntakeswipeRight = (View) findViewById(R.id.v_ConstructionIntakeswipeRight);

		cbConstructionInakeToBeStarted.setOnClickListener(this);
		cbConstructionInakeWorkOnProgress.setOnClickListener(this);
		cbConstructionInakeCompleted.setOnClickListener(this);
		rlConstructionIntakeStartDate.setOnClickListener(this);
		rlConstructionIntakeCompletiontDate.setOnClickListener(this);
		btn_ConstructionIntakePhoto.setOnClickListener(this);

		// ============ Construction of Flush Mixture
		cbConstructionFlushToBeStarted = (CheckBox) findViewById(R.id.cbConstructionFlushToBeStarted);
		cbConstructionFlushInProgress = (CheckBox) findViewById(R.id.cbConstructionFlushInProgress);
		cbConstructionFlushCompleted = (CheckBox) findViewById(R.id.cbConstructionFlushCompleted);
		tvConstructionFlushStartDate = (TextView) findViewById(R.id.tvConstructionFlushStartDate);
		tvConstructionFlushCompletiontDate = (TextView) findViewById(R.id.tvConstructionFlushCompletiontDate);
		rlConstructionFlushStartDate = (RelativeLayout) findViewById(R.id.rlConstructionFlushStartDate);
		rlConstructionFlushCompletionDate = (RelativeLayout) findViewById(R.id.rlConstructionFlushCompletionDate);
		btn_ConstructionFlushPhoto = (Button) findViewById(R.id.btn_ConstructionFlushPhoto);
		vp_ConstructionFlushSelectedImages = (ViewPager) findViewById(R.id.vp_ConstructionFlushSelectedImages);

		tv_ConstructionFlushMixtureProgress = (TextView) findViewById(R.id.tv_ConstructionFlushMixtureProgress);
		v_ConstructionFlushswipeLeft = (View) findViewById(R.id.v_ConstructionFlushswipeLeft);
		v_ConstructionFlushswipeRight = (View) findViewById(R.id.v_ConstructionFlushswipeRight);

		cbConstructionFlushToBeStarted.setOnClickListener(this);
		cbConstructionFlushInProgress.setOnClickListener(this);
		cbConstructionFlushCompleted.setOnClickListener(this);
		rlConstructionFlushStartDate.setOnClickListener(this);
		rlConstructionFlushCompletionDate.setOnClickListener(this);
		btn_ConstructionFlushPhoto.setOnClickListener(this);

		// ============ Construction of Clariflocculator or plate settler or
		// Tube settler
		cbConstructionClariflocculatorToBeStarted = (CheckBox) findViewById(R.id.cbConstructionClariflocculatorToBeStarted);
		cbConstructionClariflocculatorInProgress = (CheckBox) findViewById(R.id.cbConstructionClariflocculatorInProgress);
		cbConstructionClariflocculatorCompleted = (CheckBox) findViewById(R.id.cbConstructionClariflocculatorCompleted);
		tvConstructionClariflocculatorStartDate = (TextView) findViewById(R.id.tvConstructionClariflocculatorStartDate);
		tvConstructionClariflocculatorCompletiontDate = (TextView) findViewById(R.id.tvConstructionClariflocculatorCompletiontDate);
		rlConstructionClariflocculatorStartDate = (RelativeLayout) findViewById(R.id.rlConstructionClariflocculatorStartDate);
		rlConstructionClariflocculatorCompletedDate = (RelativeLayout) findViewById(R.id.rlConstructionClariflocculatorCompletedDate);
		btn_ConstructionClariflocculatorPhoto = (Button) findViewById(R.id.btn_ConstructionClariflocculatorPhoto);
		vp_ConstructionClariflocculatorSelectedImages = (ViewPager) findViewById(R.id.vp_ConstructionClariflocculatorSelectedImages);

		tv_ConstructionClariflocculatorProgress = (TextView) findViewById(R.id.tv_ConstructionClariflocculatorProgress);
		v_ConstructionClariflocculatorswipeLeft = (View) findViewById(R.id.v_ConstructionClariflocculatorswipeLeft);
		v_ConstructionClariflocculatorswipeRight = (View) findViewById(R.id.v_ConstructionClariflocculatorswipeRight);

		cbConstructionClariflocculatorToBeStarted.setOnClickListener(this);
		cbConstructionClariflocculatorInProgress.setOnClickListener(this);
		cbConstructionClariflocculatorCompleted.setOnClickListener(this);
		rlConstructionClariflocculatorStartDate.setOnClickListener(this);
		rlConstructionClariflocculatorCompletedDate.setOnClickListener(this);
		btn_ConstructionClariflocculatorPhoto.setOnClickListener(this);

		// ============ Construction of Rapid Sand Filer
		cbConstructionRapidSandFilterToBeStarted = (CheckBox) findViewById(R.id.cbConstructionRapidSandFilterToBeStarted);
		cbConstructionRapidSandFilterInProgress = (CheckBox) findViewById(R.id.cbConstructionRapidSandFilterInProgress);
		cbConstructionRapidSandFilterCompleted = (CheckBox) findViewById(R.id.cbConstructionRapidSandFilterCompleted);
		tvConstructionRapidSandFilterStartDate = (TextView) findViewById(R.id.tvConstructionRapidSandFilterStartDate);
		tvConstructionRapidSandFilterCompletedDate = (TextView) findViewById(R.id.tvConstructionRapidSandFilterCompletedDate);
		rlConstructionRapidSandFilterStartDate = (RelativeLayout) findViewById(R.id.rlConstructionRapidSandFilterStartDate);
		rlConstructionRapidSandFilterCompletedDate = (RelativeLayout) findViewById(R.id.rlConstructionRapidSandFilterCompletedDate);
		btn_ConstructionRapidSandFilterPhoto = (Button) findViewById(R.id.btn_ConstructionRapidSandFilterPhoto);
		vp_ConstructionRapidSandFilterSelectedImages = (ViewPager) findViewById(R.id.vp_ConstructionRapidSandFilterSelectedImages);

		tv_ConstructionRapidSandFilterProgress = (TextView) findViewById(R.id.tv_ConstructionRapidSandFilterProgress);
		v_ConstructionRapidSandFilterswipeLeft = (View) findViewById(R.id.v_ConstructionRapidSandFilterswipeLeft);
		v_ConstructionRapidSandFilterswipeRight = (View) findViewById(R.id.v_ConstructionRapidSandFilterswipeRight);

		cbConstructionRapidSandFilterToBeStarted.setOnClickListener(this);
		cbConstructionRapidSandFilterInProgress.setOnClickListener(this);
		cbConstructionRapidSandFilterCompleted.setOnClickListener(this);
		rlConstructionRapidSandFilterStartDate.setOnClickListener(this);
		rlConstructionRapidSandFilterCompletedDate.setOnClickListener(this);
		btn_ConstructionRapidSandFilterPhoto.setOnClickListener(this);

		// ============ Construction of Clear Water Pump House
		cbConstructionClearWaterPumpHouseToBeStarted = (CheckBox) findViewById(R.id.cbConstructionClearWaterPumpHouseToBeStarted);
		cbConstructionClearWaterPumpHouseInProgress = (CheckBox) findViewById(R.id.cbConstructionClearWaterPumpHouseInProgress);
		cbConstructionClearWaterPumpHouseCompleted = (CheckBox) findViewById(R.id.cbConstructionClearWaterPumpHouseCompleted);
		tvConstructionClearWaterPumpHouseStartDate = (TextView) findViewById(R.id.tvConstructionClearWaterPumpHouseStartDate);
		tvConstructionClearWaterPumpHouseCompletedDate = (TextView) findViewById(R.id.tvConstructionClearWaterPumpHouseCompletedDate);
		rlConstructionClearWaterPumpHouseStartDate = (RelativeLayout) findViewById(R.id.rlConstructionClearWaterPumpHouseStartDate);
		rlConstructionClearWaterPumpHouseCompletedDate = (RelativeLayout) findViewById(R.id.rlConstructionClearWaterPumpHouseCompletedDate);
		btn_ConstructionClearWaterPumpHousePhoto = (Button) findViewById(R.id.btn_ConstructionClearWaterPumpHousePhoto);
		vp_ConstructionClearWaterPumpHouseSelectedImages = (ViewPager) findViewById(R.id.vp_ConstructionClearWaterPumpHouseSelectedImages);

		tv_ConstructionClearWaterPumpHouseProgress = (TextView) findViewById(R.id.tv_ConstructionClearWaterPumpHouseProgress);
		v_ConstructionClearWaterPumpHouseswipeLeft = (View) findViewById(R.id.v_ConstructionClearWaterPumpHouseswipeLeft);
		v_ConstructionClearWaterPumpHouseswipeRight = (View) findViewById(R.id.v_ConstructionClearWaterPumpHouseswipeRight);

		cbConstructionClearWaterPumpHouseToBeStarted.setOnClickListener(this);
		cbConstructionClearWaterPumpHouseInProgress.setOnClickListener(this);
		cbConstructionClearWaterPumpHouseCompleted.setOnClickListener(this);
		rlConstructionClearWaterPumpHouseStartDate.setOnClickListener(this);
		rlConstructionClearWaterPumpHouseCompletedDate.setOnClickListener(this);
		btn_ConstructionClearWaterPumpHousePhoto.setOnClickListener(this);

		// ============ Installation of Electrical and Mechanical Machinery
		cbInstallationElectricalMechanicalMachineryToBeStarted = (CheckBox) findViewById(R.id.cbInstallationElectricalMechanicalMachineryToBeStarted);
		cbInstallationElectricalMechanicalMachineryInProgress = (CheckBox) findViewById(R.id.cbInstallationElectricalMechanicalMachineryInProgress);
		cbInstallationElectricalMechanicalMachineryCompleted = (CheckBox) findViewById(R.id.cbInstallationElectricalMechanicalMachineryCompleted);
		tvInstallationElectricalMechanicalMachineryStartDate = (TextView) findViewById(R.id.tvInstallationElectricalMechanicalMachineryStartDate);
		tvInstallationElectricalMechanicalMachineryCompletedDate = (TextView) findViewById(R.id.tvInstallationElectricalMechanicalMachineryCompletedDate);
		rlInstallationElectricalMechanicalMachineryStartDate = (RelativeLayout) findViewById(R.id.rlInstallationElectricalMechanicalMachineryStartDate);
		rlInstallationElectricalMechanicalMachineryCompletedDate = (RelativeLayout) findViewById(R.id.rlInstallationElectricalMechanicalMachineryCompletedDate);
		btn_InstallationElectricalMechanicalMachineryPhoto = (Button) findViewById(R.id.btn_InstallationElectricalMechanicalMachineryPhoto);
		vp_InstallationElectricalMechanicalMachinerySelectedImages = (ViewPager) findViewById(R.id.vp_InstallationElectricalMechanicalMachinerySelectedImages);

		tv_InstallationElectricalMechanicalMachineryProgress = (TextView) findViewById(R.id.tv_InstallationElectricalMechanicalMachineryProgress);
		v_InstallationElectricalMechanicalMachineryswipeLeft = (View) findViewById(R.id.v_InstallationElectricalMechanicalMachineryswipeLeft);
		v_InstallationElectricalMechanicalMachineryswipeRight = (View) findViewById(R.id.v_InstallationElectricalMechanicalMachineryswipeRight);

		cbInstallationElectricalMechanicalMachineryToBeStarted.setOnClickListener(this);
		cbInstallationElectricalMechanicalMachineryInProgress.setOnClickListener(this);
		cbInstallationElectricalMechanicalMachineryCompleted.setOnClickListener(this);
		rlInstallationElectricalMechanicalMachineryStartDate.setOnClickListener(this);
		rlInstallationElectricalMechanicalMachineryCompletedDate.setOnClickListener(this);
		btn_InstallationElectricalMechanicalMachineryPhoto.setOnClickListener(this);

		// ============ Construction of Chemical House
		cbConstructionChemicalHouseToBeStarted = (CheckBox) findViewById(R.id.cbConstructionChemicalHouseToBeStarted);
		cbConstructionChemicalHouseInProgress = (CheckBox) findViewById(R.id.cbConstructionChemicalHouseInProgress);
		cbConstructionChemicalHouseCompleted = (CheckBox) findViewById(R.id.cbConstructionChemicalHouseCompleted);
		tvConstructionChemicalHouseStartDate = (TextView) findViewById(R.id.tvConstructionChemicalHouseStartDate);
		tvConstructionChemicalHouseCompletedDate = (TextView) findViewById(R.id.tvConstructionChemicalHouseCompletedDate);
		rlConstructionChemicalHouseStartDate = (RelativeLayout) findViewById(R.id.rlConstructionChemicalHouseStartDate);
		rlConstructionChemicalHouseCompletedDate = (RelativeLayout) findViewById(R.id.rlConstructionChemicalHouseCompletedDate);
		btn_ConstructionChemicalHousePhoto = (Button) findViewById(R.id.btn_ConstructionChemicalHousePhoto);
		vp_ConstructionChemicalHouseSelectedImages = (ViewPager) findViewById(R.id.vp_ConstructionChemicalHouseSelectedImages);

		tv_ConstructionChemicalHouseProgress = (TextView) findViewById(R.id.tv_ConstructionChemicalHouseProgress);
		v_ConstructionChemicalHouseswipeLeft = (View) findViewById(R.id.v_ConstructionChemicalHouseswipeLeft);
		v_ConstructionChemicalHouseswipeRight = (View) findViewById(R.id.v_ConstructionChemicalHouseswipeRight);

		cbConstructionChemicalHouseToBeStarted.setOnClickListener(this);
		cbConstructionChemicalHouseInProgress.setOnClickListener(this);
		cbConstructionChemicalHouseCompleted.setOnClickListener(this);
		rlConstructionChemicalHouseStartDate.setOnClickListener(this);
		rlConstructionChemicalHouseCompletedDate.setOnClickListener(this);
		btn_ConstructionChemicalHousePhoto.setOnClickListener(this);

		// ============ Construction of Chlorine Room and Installation of
		// Chlorination Arrangement
		cbConstructionInstallationChlorineRoomToBeStarted = (CheckBox) findViewById(R.id.cbConstructionInstallationChemicalRoomToBeStarted);
		cbConstructionInstallationChlorineRoomInProgress = (CheckBox) findViewById(R.id.cbConstructionInstallationChemicalRoomInProgress);
		cbConstructionInstallationChlorineRoomCompleted = (CheckBox) findViewById(R.id.cbConstructionInstallationChemicalRoomCompleted);
		tvConstructionInstallationChlorineRoomStartDate = (TextView) findViewById(R.id.tvConstructionInstallationChemicalRoomStartDate);
		tvConstructionInstallationChlorineRoomCompletionDate = (TextView) findViewById(R.id.tvConstructionInstallationChemicalRoomCompletionDate);
		rlConstructionInstallationChlorineRoomStartDate = (RelativeLayout) findViewById(R.id.rlConstructionInstallationChemicalRoomStartDate);
		rlConstructionInstallationChlorineRoomCompletionDate = (RelativeLayout) findViewById(R.id.rlConstructionInstallationChemicalRoomCompletionDate);
		btn_ConstructionInstallationChlorineRoomPhoto = (Button) findViewById(R.id.btn_ConstructionInstallationChemicalRoomPhoto);
		vp_ConstructionInstallationChlorineRoomSelectedImages = (ViewPager) findViewById(R.id.vp_ConstructionInstallationChemicalRoomSelectedImages);

		tv_ConstructionInstallChlorineRoomProgress = (TextView) findViewById(R.id.tv_ConstructionInstallChemicalRoomProgress);
		v_ConstructionInstallationChlorineRoomswipeLeft = (View) findViewById(R.id.v_ConstructionInstallationChemicalRoomswipeLeft);
		v_ConstructionInstallationChlorineRoomswipeRight = (View) findViewById(R.id.v_ConstructionInstallationChemicalRoomswipeRight);

		cbConstructionInstallationChlorineRoomToBeStarted.setOnClickListener(this);
		cbConstructionInstallationChlorineRoomInProgress.setOnClickListener(this);
		cbConstructionInstallationChlorineRoomCompleted.setOnClickListener(this);
		rlConstructionInstallationChlorineRoomStartDate.setOnClickListener(this);
		rlConstructionInstallationChlorineRoomCompletionDate.setOnClickListener(this);
		btn_ConstructionInstallationChlorineRoomPhoto.setOnClickListener(this);

		// ============LOCATION

		tv_latitude = (TextView) findViewById(R.id.tv_latitude);
		tv_longitude = (TextView) findViewById(R.id.tv_longitude);

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

		constructionIntakeWellStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionIntakeStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionIntakeWellStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionIntakeCompletiontDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionFlushMixtureStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionFlushStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionFlushMixtureStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionFlushCompletiontDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionClariflocculatorStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionClariflocculatorStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionClariflocculatorStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionClariflocculatorCompletiontDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionRapidSandFilterStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionRapidSandFilterStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};

		constructionRapidSandFilterStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionRapidSandFilterCompletedDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionClearWaterPumpHouseStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionClearWaterPumpHouseStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionClearWaterPumpHouseStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionClearWaterPumpHouseCompletedDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		installationElectricalMechanicalMachineryStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvInstallationElectricalMechanicalMachineryStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		installationElectricalMechanicalMachineryStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvInstallationElectricalMechanicalMachineryCompletedDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionChemicalHouseStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionChemicalHouseStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionChemicalHouseStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionChemicalHouseCompletedDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionChlorineRoomStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionInstallationChlorineRoomStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionChlorineRoomStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionInstallationChlorineRoomCompletionDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
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

				allImagesList.addAll(constructionIntakeWellImagesList);
				allImagesList.addAll(constructionFlushMixtureImagesList);
				allImagesList.addAll(constructionClariflocculatorImagesList);
				allImagesList.addAll(constructionRapidSandFilterImagesList);
				allImagesList.addAll(constructionClearWaterPumpHouseImagesList);
				allImagesList.addAll(installationElectricalMechanicalMachineryImagesList);
				allImagesList.addAll(constructionChemicalHouseImagesList);
				allImagesList.addAll(constructionChlorineRoomImagesList);
				postFormWebserviceCalling();
			}

		} else if (Util.checkConnectivity(mContext)) {

			Toast.makeText(mContext, "No Network connectivity.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {

		Calendar currentDate = Util.getCurrentDate();

		switch (v.getId()) {

		// ============ Construction of Intake Well
		case R.id.cbConstructionInakeToBeStarted:

			constructionIntakeWellStatus = "Yet to be Started";
			cbConstructionInakeToBeStarted.setChecked(true);
			cbConstructionInakeWorkOnProgress.setChecked(false);
			cbConstructionInakeCompleted.setChecked(false);
			break;
		case R.id.cbConstructionInakeWorkOnProgress:

			constructionIntakeWellStatus = "Work in Progress";
			cbConstructionInakeToBeStarted.setChecked(false);
			cbConstructionInakeWorkOnProgress.setChecked(true);
			cbConstructionInakeCompleted.setChecked(false);

			break;
		case R.id.cbConstructionInakeCompleted:

			constructionIntakeWellStatus = "Completed";
			cbConstructionInakeToBeStarted.setChecked(false);
			cbConstructionInakeWorkOnProgress.setChecked(false);
			cbConstructionInakeCompleted.setChecked(true);
			break;
		case R.id.rlConstructionIntakeStartDate:

			new DatePickerDialog(this, constructionIntakeWellStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.rlConstructionIntakeCompletiontDate:

			new DatePickerDialog(this, constructionIntakeWellStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.btn_ConstructionIntakePhoto:

			isConstructionIntakeWell = true;
			imageDialog();
			break;
		// ============ Construction of Flush Mixture
		case R.id.cbConstructionFlushToBeStarted:

			constructionFlushMixtureStatus = "Yet to be Started";
			cbConstructionFlushToBeStarted.setChecked(true);
			cbConstructionFlushInProgress.setChecked(false);
			cbConstructionFlushCompleted.setChecked(false);
			break;
		case R.id.cbConstructionFlushInProgress:
			constructionFlushMixtureStatus = "Work in Progress";
			cbConstructionFlushToBeStarted.setChecked(false);
			cbConstructionFlushInProgress.setChecked(true);
			cbConstructionFlushCompleted.setChecked(false);

			break;
		case R.id.cbConstructionFlushCompleted:
			constructionFlushMixtureStatus = "Completed";
			cbConstructionFlushToBeStarted.setChecked(false);
			cbConstructionFlushInProgress.setChecked(false);
			cbConstructionFlushCompleted.setChecked(true);

			break;
		case R.id.rlConstructionFlushStartDate:

			new DatePickerDialog(this, constructionFlushMixtureStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.rlConstructionFlushCompletionDate:

			new DatePickerDialog(this, constructionFlushMixtureStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.btn_ConstructionFlushPhoto:

			isConstructionFlushMixture = true;
			imageDialog();
			break;
		// ============ Construction of Clariflocculator or plate settler or
		// Tube settler

		case R.id.cbConstructionClariflocculatorToBeStarted:

			constructionClariflocculatorStatus = "Yet to be Started";
			cbConstructionClariflocculatorToBeStarted.setChecked(true);
			cbConstructionClariflocculatorInProgress.setChecked(false);
			cbConstructionClariflocculatorCompleted.setChecked(false);
			break;
		case R.id.cbConstructionClariflocculatorInProgress:

			constructionClariflocculatorStatus = "Work in Progress";
			cbConstructionClariflocculatorToBeStarted.setChecked(false);
			cbConstructionClariflocculatorInProgress.setChecked(true);
			cbConstructionClariflocculatorCompleted.setChecked(false);
			break;
		case R.id.cbConstructionClariflocculatorCompleted:

			constructionClariflocculatorStatus = "Completed";
			cbConstructionClariflocculatorToBeStarted.setChecked(false);
			cbConstructionClariflocculatorInProgress.setChecked(false);
			cbConstructionClariflocculatorCompleted.setChecked(true);
			break;

		case R.id.rlConstructionClariflocculatorStartDate:

			new DatePickerDialog(this, constructionClariflocculatorStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.rlConstructionClariflocculatorCompletedDate:

			new DatePickerDialog(this, constructionClariflocculatorStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.btn_ConstructionClariflocculatorPhoto:

			isConstructionClariflocculator = true;
			imageDialog();
			break;

		// ============ Construction of Rapid Sand Filer
		case R.id.cbConstructionRapidSandFilterToBeStarted:

			constructionRapidSandFilterStatus = "Yet to be Started";
			cbConstructionRapidSandFilterToBeStarted.setChecked(true);
			cbConstructionRapidSandFilterInProgress.setChecked(false);
			cbConstructionRapidSandFilterCompleted.setChecked(false);

			break;
		case R.id.cbConstructionRapidSandFilterInProgress:

			constructionRapidSandFilterStatus = "Work in Progress";
			cbConstructionRapidSandFilterToBeStarted.setChecked(false);
			cbConstructionRapidSandFilterInProgress.setChecked(true);
			cbConstructionRapidSandFilterCompleted.setChecked(false);

			break;
		case R.id.cbConstructionRapidSandFilterCompleted:

			constructionRapidSandFilterStatus = "Completed";
			cbConstructionRapidSandFilterToBeStarted.setChecked(false);
			cbConstructionRapidSandFilterInProgress.setChecked(false);
			cbConstructionRapidSandFilterCompleted.setChecked(true);
			break;

		case R.id.rlConstructionRapidSandFilterStartDate:

			new DatePickerDialog(this, constructionRapidSandFilterStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.rlConstructionRapidSandFilterCompletedDate:

			new DatePickerDialog(this, constructionRapidSandFilterStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.btn_ConstructionRapidSandFilterPhoto:

			isConstructionRapidSandFilter = true;
			imageDialog();
			break;
		// ============ Construction of Clear Water Pump House
		case R.id.cbConstructionClearWaterPumpHouseToBeStarted:

			constructionClearWaterPumpHouseStatus = "Yet to be Started";
			cbConstructionClearWaterPumpHouseToBeStarted.setChecked(true);
			cbConstructionClearWaterPumpHouseInProgress.setChecked(false);
			cbConstructionClearWaterPumpHouseCompleted.setChecked(false);

			break;
		case R.id.cbConstructionClearWaterPumpHouseInProgress:

			constructionClearWaterPumpHouseStatus = "Work in Progress";
			cbConstructionClearWaterPumpHouseToBeStarted.setChecked(false);
			cbConstructionClearWaterPumpHouseInProgress.setChecked(true);
			cbConstructionClearWaterPumpHouseCompleted.setChecked(false);
			break;
		case R.id.cbConstructionClearWaterPumpHouseCompleted:

			constructionClearWaterPumpHouseStatus = "Completed";
			cbConstructionClearWaterPumpHouseToBeStarted.setChecked(false);
			cbConstructionClearWaterPumpHouseInProgress.setChecked(false);
			cbConstructionClearWaterPumpHouseCompleted.setChecked(true);
			break;

		case R.id.rlConstructionClearWaterPumpHouseStartDate:

			new DatePickerDialog(this, constructionClearWaterPumpHouseStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.rlConstructionClearWaterPumpHouseCompletedDate:

			new DatePickerDialog(this, constructionClearWaterPumpHouseStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.btn_ConstructionClearWaterPumpHousePhoto:

			isConstructionClearWaterPumpHouse = true;
			imageDialog();
			break;

		// ============ Installation of Electrical and Mechanical Machinery
		case R.id.cbInstallationElectricalMechanicalMachineryToBeStarted:

			installationElectricalMechanicalMachineryStatus = "Yet to be Started";
			cbInstallationElectricalMechanicalMachineryToBeStarted.setChecked(true);
			cbInstallationElectricalMechanicalMachineryInProgress.setChecked(false);
			cbInstallationElectricalMechanicalMachineryCompleted.setChecked(false);

			break;
		case R.id.cbInstallationElectricalMechanicalMachineryInProgress:

			installationElectricalMechanicalMachineryStatus = "Work in Progress";
			cbInstallationElectricalMechanicalMachineryToBeStarted.setChecked(false);
			cbInstallationElectricalMechanicalMachineryInProgress.setChecked(true);
			cbInstallationElectricalMechanicalMachineryCompleted.setChecked(false);

			break;
		case R.id.cbInstallationElectricalMechanicalMachineryCompleted:

			installationElectricalMechanicalMachineryStatus = "Completed";
			cbInstallationElectricalMechanicalMachineryToBeStarted.setChecked(false);
			cbInstallationElectricalMechanicalMachineryInProgress.setChecked(false);
			cbInstallationElectricalMechanicalMachineryCompleted.setChecked(true);

			break;

		case R.id.rlInstallationElectricalMechanicalMachineryStartDate:

			new DatePickerDialog(this, installationElectricalMechanicalMachineryStartDateListener, currentDate.get(Calendar.YEAR),
					currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.rlInstallationElectricalMechanicalMachineryCompletedDate:

			new DatePickerDialog(this, installationElectricalMechanicalMachineryStopDateListener, currentDate.get(Calendar.YEAR),
					currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.btn_InstallationElectricalMechanicalMachineryPhoto:

			isInstallationElectricalMechanicalMachinery = true;
			imageDialog();
			break;

		// ============ Construction of Chemical House
		case R.id.cbConstructionChemicalHouseToBeStarted:

			constructionChemicalHouseStatus = "Yet to be Started";
			cbConstructionChemicalHouseToBeStarted.setChecked(true);
			cbConstructionChemicalHouseInProgress.setChecked(false);
			cbConstructionChemicalHouseCompleted.setChecked(false);
			break;
		case R.id.cbConstructionChemicalHouseInProgress:

			constructionChemicalHouseStatus = "Work in Progress";
			cbConstructionChemicalHouseToBeStarted.setChecked(false);
			cbConstructionChemicalHouseInProgress.setChecked(true);
			cbConstructionChemicalHouseCompleted.setChecked(false);
			break;
		case R.id.cbConstructionChemicalHouseCompleted:

			constructionChemicalHouseStatus = "Completed";
			cbConstructionChemicalHouseToBeStarted.setChecked(false);
			cbConstructionChemicalHouseInProgress.setChecked(false);
			cbConstructionChemicalHouseCompleted.setChecked(true);

			break;
		case R.id.rlConstructionChemicalHouseStartDate:

			new DatePickerDialog(this, constructionChemicalHouseStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.rlConstructionChemicalHouseCompletedDate:

			new DatePickerDialog(this, constructionChemicalHouseStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.btn_ConstructionChemicalHousePhoto:

			isConstructionChemicalHouse = true;
			imageDialog();
			break;

		// ============ Construction of Chlorine Room and Installation of
		// Chlorination Arrangement
		case R.id.cbConstructionInstallationChemicalRoomToBeStarted:

			constructionChlorineRoomStatus = "Yet to be Started";
			cbConstructionInstallationChlorineRoomToBeStarted.setChecked(true);
			cbConstructionInstallationChlorineRoomInProgress.setChecked(false);
			cbConstructionInstallationChlorineRoomCompleted.setChecked(false);
			break;

		case R.id.cbConstructionInstallationChemicalRoomInProgress:

			constructionChlorineRoomStatus = "Work in Progress";
			cbConstructionInstallationChlorineRoomToBeStarted.setChecked(false);
			cbConstructionInstallationChlorineRoomInProgress.setChecked(true);
			cbConstructionInstallationChlorineRoomCompleted.setChecked(false);
			break;

		case R.id.cbConstructionInstallationChemicalRoomCompleted:

			constructionChlorineRoomStatus = "Completed";
			cbConstructionInstallationChlorineRoomToBeStarted.setChecked(false);
			cbConstructionInstallationChlorineRoomInProgress.setChecked(false);
			cbConstructionInstallationChlorineRoomCompleted.setChecked(true);
			break;

		case R.id.rlConstructionInstallationChemicalRoomStartDate:

			new DatePickerDialog(this, constructionChlorineRoomStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.rlConstructionInstallationChemicalRoomCompletionDate:

			new DatePickerDialog(this, constructionChlorineRoomStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.btn_ConstructionInstallationChemicalRoomPhoto:

			isConstructionChlorineRoom = true;
			imageDialog();
			break;

		default:
			break;
		}
	}

	private boolean checkInputValidity() {

		if (constructionIntakeWellStatus.isEmpty() || constructionIntakeWellStatus.equalsIgnoreCase("") || constructionFlushMixtureStatus.isEmpty()
				|| constructionFlushMixtureStatus.equalsIgnoreCase("") || constructionClariflocculatorStatus.isEmpty()
				|| constructionClariflocculatorStatus.equalsIgnoreCase("") || constructionRapidSandFilterStatus.isEmpty()
				|| constructionRapidSandFilterStatus.equalsIgnoreCase("") || constructionClearWaterPumpHouseStatus.isEmpty()
				|| constructionClearWaterPumpHouseStatus.equalsIgnoreCase("") || installationElectricalMechanicalMachineryStatus.isEmpty()
				|| installationElectricalMechanicalMachineryStatus.equalsIgnoreCase("") || constructionChemicalHouseStatus.isEmpty()
				|| constructionChemicalHouseStatus.equalsIgnoreCase("") || constructionChlorineRoomStatus.isEmpty()
				|| constructionChlorineRoomStatus.equalsIgnoreCase("") || tv_latitude.getText().toString().trim().equalsIgnoreCase("")
				|| tv_longitude.getText().toString().trim().equalsIgnoreCase("")) {

			Util.showMessageWithOk(mContext, "Please fill all the required fields.");
			return false;
		} else
			return true;
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
		if ((isConstructionIntakeWell && constructionIntakeWellImagesList.size() < 3)
				|| (isConstructionFlushMixture && constructionFlushMixtureImagesList.size() < 3)
				|| (isConstructionClariflocculator && constructionClariflocculatorImagesList.size() < 3)
				|| (isConstructionRapidSandFilter && constructionRapidSandFilterImagesList.size() < 3)
				|| (isConstructionClearWaterPumpHouse && constructionClearWaterPumpHouseImagesList.size() < 3)
				|| (isInstallationElectricalMechanicalMachinery && installationElectricalMechanicalMachineryImagesList.size() < 3)
				|| (isConstructionChemicalHouse && constructionChemicalHouseImagesList.size() < 3)
				|| (isConstructionChlorineRoom && constructionChlorineRoomImagesList.size() < 3)) {

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
					isConstructionIntakeWell = false;
					isConstructionFlushMixture = false;
					isConstructionClariflocculator = false;
					isConstructionRapidSandFilter = false;
					isConstructionClearWaterPumpHouse = false;
					isInstallationElectricalMechanicalMachinery = false;
					isConstructionChemicalHouse = false;
					isConstructionChlorineRoom = false;

					customDialog.dismiss();
				}
			});

			customDialog.setCancelable(false);
			customDialog.setContentView(view);
			customDialog.setCanceledOnTouchOutside(false);
			// Start AlertDialog
			customDialog.show();
		} else {
			if (isConstructionIntakeWell) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Construction of Intake Well\" images has already been selected!");
			} else if (isConstructionFlushMixture) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Construction of Flush Mixture\" images has already been selected!");
			} else if (isConstructionClariflocculator) {
				Util.showMessageWithOk(mContext,
						"Maximum number of \"Construction of Clariflocculator or plate settler or Tube settler\" images has already been selected!");

			} else if (isConstructionRapidSandFilter) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Construction of Rapid Sand Filter\" images has already been selected!");

			} else if (isConstructionClearWaterPumpHouse) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Construction of Clear Water Pump house\" images has already been selected!");

			} else if (isInstallationElectricalMechanicalMachinery) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Installation of Electrical and Mechanical Machinery\" images has already been selected!");
			} else if (isConstructionChemicalHouse) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Construction of Chemical house\" images has already been selected!");
			} else if (isConstructionChlorineRoom) {
				Util.showMessageWithOk(mContext,
						"Maximum number of \"Construction of Chlorine room and installation of Chlorination Arrangement\" images has already been selected!");
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

			isConstructionIntakeWell = false;
			isConstructionFlushMixture = false;
			isConstructionClariflocculator = false;
			isConstructionRapidSandFilter = false;
			isConstructionClearWaterPumpHouse = false;
			isInstallationElectricalMechanicalMachinery = false;
			isConstructionChemicalHouse = false;
			isConstructionChlorineRoom = false;

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
			if (isConstructionIntakeWell) {
				imageClass.setImageType("Construction_Of_Intake_Well");
				imageClass.setImageCount(constructionIntakeWellImagesCount + 1);
				constructionIntakeWellImagesList.add(imageClass);
				isConstructionIntakeWell = false;
				constructionIntakeWellImagesCount++;
			} else if (isConstructionFlushMixture) {
				imageClass.setImageType("Construction_Of_Flush_Mixture");
				imageClass.setImageCount(constructionFlushMixtureImagesCount + 1);
				constructionFlushMixtureImagesList.add(imageClass);
				isConstructionFlushMixture = false;
				constructionFlushMixtureImagesCount++;
			} else if (isConstructionClariflocculator) {
				imageClass.setImageType("Construction_Of_Clariflocculator");
				imageClass.setImageCount(constructionClariflocculatorImagesCount + 1);
				constructionClariflocculatorImagesList.add(imageClass);
				isConstructionClariflocculator = false;
				constructionClariflocculatorImagesCount++;
			} else if (isConstructionRapidSandFilter) {
				imageClass.setImageType("Construction_Of_Rapid_Sand_Filter");
				imageClass.setImageCount(constructionRapidSandFilterImagesCount + 1);
				constructionRapidSandFilterImagesList.add(imageClass);
				isConstructionRapidSandFilter = false;
				constructionRapidSandFilterImagesCount++;
			} else if (isConstructionClearWaterPumpHouse) {
				imageClass.setImageType("Construction_Of_Clear_Water_Pump_House");
				imageClass.setImageCount(constructionClearWaterPumpHouseImagesCount + 1);
				constructionClearWaterPumpHouseImagesList.add(imageClass);
				isConstructionClearWaterPumpHouse = false;
				constructionClearWaterPumpHouseImagesCount++;
			} else if (isInstallationElectricalMechanicalMachinery) {
				imageClass.setImageType("Installation_Of_Electrical_And_Mechanical_Machinery");
				imageClass.setImageCount(installationElectricalMechanicalMachineryImagesCount + 1);
				installationElectricalMechanicalMachineryImagesList.add(imageClass);
				isInstallationElectricalMechanicalMachinery = false;
				installationElectricalMechanicalMachineryImagesCount++;
			} else if (isConstructionChemicalHouse) {
				imageClass.setImageType("Construction_Of_Chemical_House");
				imageClass.setImageCount(constructionChemicalHouseImagesCount + 1);
				constructionChemicalHouseImagesList.add(imageClass);
				isConstructionChemicalHouse = false;
				constructionChemicalHouseImagesCount++;
			} else if (isConstructionChlorineRoom) {
				imageClass.setImageType("Construction_Of_Chlorine_Room");
				imageClass.setImageCount(constructionChlorineRoomImagesCount + 1);
				constructionChlorineRoomImagesList.add(imageClass);
				isConstructionChlorineRoom = false;
				constructionChlorineRoomImagesCount++;
			}
		} else {
			isConstructionIntakeWell = false;
			isConstructionFlushMixture = false;
			isConstructionClariflocculator = false;
			isConstructionRapidSandFilter = false;
			isConstructionClearWaterPumpHouse = false;
			isInstallationElectricalMechanicalMachinery = false;
			isConstructionChemicalHouse = false;
			isConstructionChlorineRoom = false;
			Toast.makeText(this, picturePath + "not found", Toast.LENGTH_LONG).show();
		}

		imageUpdateOnView();
	}

	private void imageUpdateOnView() {

		// ============ Construction of Intake Well
		ImageAdapter constructionIntakeAdapter = new ImageAdapter(this, constructionIntakeWellImagesList);
		vp_ConstructionIntakeSelectedImages.setAdapter(constructionIntakeAdapter);

		if (constructionIntakeWellImagesList.size() == 0) {
			vp_ConstructionIntakeSelectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstructionIntakeSelectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstructionIntakeSelectedImages.setCurrentItem(constructionIntakeWellImagesList.size() - 1);
		}

		if (constructionIntakeWellImagesList.size() <= 1) {
			tv_ConstructionIntakeWellImageProgress.setText("[Image added " + constructionIntakeWellImagesList.size() + "/3]");
			v_ConstructionIntakeswipeRight.setVisibility(View.INVISIBLE);
			v_ConstructionIntakeswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstructionIntakeWellImageProgress.setText("Slide to view other images\n[Images added " + constructionIntakeWellImagesList.size() + "/3]");
			v_ConstructionIntakeswipeRight.setVisibility(View.VISIBLE);
			v_ConstructionIntakeswipeLeft.setVisibility(View.VISIBLE);
		}

		// ============ Construction of Flush Mixture
		ImageAdapter constructionFlushMixtureAdapter = new ImageAdapter(this, constructionFlushMixtureImagesList);
		vp_ConstructionFlushSelectedImages.setAdapter(constructionFlushMixtureAdapter);

		if (constructionFlushMixtureImagesList.size() == 0) {
			vp_ConstructionFlushSelectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstructionFlushSelectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstructionFlushSelectedImages.setCurrentItem(constructionFlushMixtureImagesList.size() - 1);
		}

		if (constructionFlushMixtureImagesList.size() <= 1) {
			tv_ConstructionFlushMixtureProgress.setText("[Image added " + constructionFlushMixtureImagesList.size() + "/3]");
			v_ConstructionFlushswipeRight.setVisibility(View.INVISIBLE);
			v_ConstructionFlushswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstructionFlushMixtureProgress.setText("Slide to view other images\n[Images added " + constructionFlushMixtureImagesList.size() + "/3]");
			v_ConstructionFlushswipeRight.setVisibility(View.VISIBLE);
			v_ConstructionFlushswipeLeft.setVisibility(View.VISIBLE);
		}

		// ============ Construction of Clariflocculator or plate settler or
		// Tube settler
		ImageAdapter constructionClariflocculatorAdapter = new ImageAdapter(this, constructionClariflocculatorImagesList);
		vp_ConstructionClariflocculatorSelectedImages.setAdapter(constructionClariflocculatorAdapter);

		if (constructionClariflocculatorImagesList.size() == 0) {
			vp_ConstructionClariflocculatorSelectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstructionClariflocculatorSelectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstructionClariflocculatorSelectedImages.setCurrentItem(constructionClariflocculatorImagesList.size() - 1);
		}

		if (constructionClariflocculatorImagesList.size() <= 1) {
			tv_ConstructionClariflocculatorProgress.setText("[Image added " + constructionClariflocculatorImagesList.size() + "/3]");
			v_ConstructionClariflocculatorswipeRight.setVisibility(View.INVISIBLE);
			v_ConstructionClariflocculatorswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstructionClariflocculatorProgress.setText("Slide to view other images\n[Images added " + constructionClariflocculatorImagesList.size()
					+ "/3]");
			v_ConstructionClariflocculatorswipeRight.setVisibility(View.VISIBLE);
			v_ConstructionClariflocculatorswipeLeft.setVisibility(View.VISIBLE);
		}

		// ============ Construction of Rapid Sand Filer
		ImageAdapter constructionRapidSandFilterAdapter = new ImageAdapter(this, constructionRapidSandFilterImagesList);
		vp_ConstructionRapidSandFilterSelectedImages.setAdapter(constructionRapidSandFilterAdapter);

		if (constructionRapidSandFilterImagesList.size() == 0) {
			vp_ConstructionRapidSandFilterSelectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstructionRapidSandFilterSelectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstructionRapidSandFilterSelectedImages.setCurrentItem(constructionRapidSandFilterImagesList.size() - 1);
		}

		if (constructionRapidSandFilterImagesList.size() <= 1) {
			tv_ConstructionRapidSandFilterProgress.setText("[Image added " + constructionRapidSandFilterImagesList.size() + "/3]");
			v_ConstructionRapidSandFilterswipeRight.setVisibility(View.INVISIBLE);
			v_ConstructionRapidSandFilterswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstructionRapidSandFilterProgress.setText("Slide to view other images\n[Images added " + constructionRapidSandFilterImagesList.size() + "/3]");
			v_ConstructionRapidSandFilterswipeRight.setVisibility(View.VISIBLE);
			v_ConstructionRapidSandFilterswipeLeft.setVisibility(View.VISIBLE);
		}

		// ============ Construction of Clear Water Pump House
		ImageAdapter constructionClearWaterPumpHouseAdapter = new ImageAdapter(this, constructionClearWaterPumpHouseImagesList);
		vp_ConstructionClearWaterPumpHouseSelectedImages.setAdapter(constructionClearWaterPumpHouseAdapter);

		if (constructionClearWaterPumpHouseImagesList.size() == 0) {
			vp_ConstructionClearWaterPumpHouseSelectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstructionClearWaterPumpHouseSelectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstructionClearWaterPumpHouseSelectedImages.setCurrentItem(constructionClearWaterPumpHouseImagesList.size() - 1);
		}

		if (constructionClearWaterPumpHouseImagesList.size() <= 1) {
			tv_ConstructionClearWaterPumpHouseProgress.setText("[Image added " + constructionClearWaterPumpHouseImagesList.size() + "/3]");
			v_ConstructionClearWaterPumpHouseswipeRight.setVisibility(View.INVISIBLE);
			v_ConstructionClearWaterPumpHouseswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstructionClearWaterPumpHouseProgress.setText("Slide to view other images\n[Images added " + constructionClearWaterPumpHouseImagesList.size()
					+ "/3]");
			v_ConstructionClearWaterPumpHouseswipeRight.setVisibility(View.VISIBLE);
			v_ConstructionClearWaterPumpHouseswipeLeft.setVisibility(View.VISIBLE);
		}

		// ============ Installation of Electrical and Mechanical Machinery
		ImageAdapter installationElectricalMechanicalMachineryAdapter = new ImageAdapter(this, installationElectricalMechanicalMachineryImagesList);
		vp_InstallationElectricalMechanicalMachinerySelectedImages.setAdapter(installationElectricalMechanicalMachineryAdapter);

		if (installationElectricalMechanicalMachineryImagesList.size() == 0) {
			vp_InstallationElectricalMechanicalMachinerySelectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_InstallationElectricalMechanicalMachinerySelectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_InstallationElectricalMechanicalMachinerySelectedImages.setCurrentItem(installationElectricalMechanicalMachineryImagesList.size() - 1);
		}

		if (installationElectricalMechanicalMachineryImagesList.size() <= 1) {
			tv_InstallationElectricalMechanicalMachineryProgress.setText("[Image added " + installationElectricalMechanicalMachineryImagesList.size() + "/3]");
			v_InstallationElectricalMechanicalMachineryswipeRight.setVisibility(View.INVISIBLE);
			v_InstallationElectricalMechanicalMachineryswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_InstallationElectricalMechanicalMachineryProgress.setText("Slide to view other images\n[Images added "
					+ installationElectricalMechanicalMachineryImagesList.size() + "/3]");
			v_InstallationElectricalMechanicalMachineryswipeRight.setVisibility(View.VISIBLE);
			v_InstallationElectricalMechanicalMachineryswipeLeft.setVisibility(View.VISIBLE);
		}

		// ============ Construction of Chemical House
		ImageAdapter constructionChemicalHouseAdapter = new ImageAdapter(this, constructionChemicalHouseImagesList);
		vp_ConstructionChemicalHouseSelectedImages.setAdapter(constructionChemicalHouseAdapter);

		if (constructionChemicalHouseImagesList.size() == 0) {
			vp_ConstructionChemicalHouseSelectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstructionChemicalHouseSelectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstructionChemicalHouseSelectedImages.setCurrentItem(constructionChemicalHouseImagesList.size() - 1);
		}

		if (constructionChemicalHouseImagesList.size() <= 1) {
			tv_ConstructionChemicalHouseProgress.setText("[Image added " + constructionChemicalHouseImagesList.size() + "/3]");
			v_ConstructionChemicalHouseswipeRight.setVisibility(View.INVISIBLE);
			v_ConstructionChemicalHouseswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstructionChemicalHouseProgress.setText("Slide to view other images\n[Images added " + constructionChemicalHouseImagesList.size() + "/3]");
			v_ConstructionChemicalHouseswipeRight.setVisibility(View.VISIBLE);
			v_ConstructionChemicalHouseswipeLeft.setVisibility(View.VISIBLE);
		}

		// ============ Construction of Chlorine Room and Installation of
		// Chlorination Arrangement
		ImageAdapter constructionInstallChlorineRoomAdapter = new ImageAdapter(this, constructionChlorineRoomImagesList);
		vp_ConstructionInstallationChlorineRoomSelectedImages.setAdapter(constructionInstallChlorineRoomAdapter);

		if (constructionChlorineRoomImagesList.size() == 0) {
			vp_ConstructionInstallationChlorineRoomSelectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstructionInstallationChlorineRoomSelectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstructionInstallationChlorineRoomSelectedImages.setCurrentItem(constructionChlorineRoomImagesList.size() - 1);
		}

		if (constructionChlorineRoomImagesList.size() <= 1) {
			tv_ConstructionInstallChlorineRoomProgress.setText("[Image added " + constructionChlorineRoomImagesList.size() + "/3]");
			v_ConstructionInstallationChlorineRoomswipeRight.setVisibility(View.INVISIBLE);
			v_ConstructionInstallationChlorineRoomswipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstructionInstallChlorineRoomProgress
					.setText("Slide to view other images\n[Images added " + constructionChlorineRoomImagesList.size() + "/3]");
			v_ConstructionInstallationChlorineRoomswipeRight.setVisibility(View.VISIBLE);
			v_ConstructionInstallationChlorineRoomswipeLeft.setVisibility(View.VISIBLE);
		}
	}

	private void postFormWebserviceCalling() {

		isDataPostingService = true;
		speedCheck(); // TODO

	}

	private void sendingImages(final int position) {

		imagePosition = position;
		UserClass user = Util.fetchUserClass(mContext);
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("ID", user.getSurfaceWaterTreatmentPlantUpdateId());
		paramsMap.put("IMAGE_TYPE", allImagesList.get(position).getImageType());
		paramsMap.put("IMAGE_NO", String.valueOf(allImagesList.get(position).getImageCount()));
		paramsMap.put("IMAGE_DATA", allImagesList.get(position).getBase64value());

		isSurfaceWaterTreatementImageService = true;
		volleyTaskManager.doPostSurfaceWaterTreatementImage(paramsMap);
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

			if (isDataPostingService) {

				requestMap.put("DISTRICT_ID", "" + districtId);
				requestMap.put("SCHEME_ID", schemeId);

				requestMap.put("CONSTRUCTION_OF_INTAKE_WELL_STATUS", constructionIntakeWellStatus);
				requestMap.put("CONSTRUCTION_OF_INTAKE_WELL_DATE_OF_START", tvConstructionIntakeStartDate.getText().toString().trim());
				requestMap.put("CONSTRUCTION_OF_INTAKE_WELL_DATE_OF_COMPLETION", tvConstructionIntakeCompletiontDate.getText().toString().trim());

				requestMap.put("CONSTRUCTION_OF_FLUSH_MIXTURE_STATUS", constructionFlushMixtureStatus);
				requestMap.put("CONSTRUCTION_OF_FLUSH_MIXTURE_DATE_OF_START", tvConstructionFlushStartDate.getText().toString().trim());
				requestMap.put("CONSTRUCTION_OF_FLUSH_MIXTURE_DATE_OF_COMPLETION", tvConstructionFlushCompletiontDate.getText().toString().trim());

				requestMap.put("CONSTRUCTION_OF_CLARIFLOCCULATOR_OR_PLATE_SETTLER_OR_TUBE_SETTLER_STATUS", constructionClariflocculatorStatus);
				requestMap.put("CONSTRUCTION_OF_CLARIFLOCCULATOR_OR_PLATE_SETTLER_OR_TUBE_SETTLER_DATE_OF_START", tvConstructionClariflocculatorStartDate
						.getText().toString().trim());
				requestMap
						.put("CONSTRUCTION_OF_CLARIFLOCCULATOR_DATE_OF_COMPLETION", tvConstructionClariflocculatorCompletiontDate.getText().toString().trim());

				requestMap.put("CONSTRUCTION_OF_RAPID_SAND_FILTER_STATUS", constructionRapidSandFilterStatus);
				requestMap.put("CONSTRUCTION_OF_RAPID_SAND_FILTER_DATE_OF_START", tvConstructionRapidSandFilterStartDate.getText().toString().trim());
				requestMap.put("CONSTRUCTION_OF_RAPID_SAND_FILTER_DATE_OF_COMPLETION", tvConstructionRapidSandFilterCompletedDate.getText().toString().trim());

				requestMap.put("CONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_STATUS", constructionClearWaterPumpHouseStatus);
				requestMap.put("CONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_DATE_OF_START", tvConstructionClearWaterPumpHouseStartDate.getText().toString().trim());
				requestMap.put("CONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_DATE_OF_COMPLETION", tvConstructionClearWaterPumpHouseCompletedDate.getText().toString()
						.trim());

				requestMap.put("INSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_STATUS", installationElectricalMechanicalMachineryStatus);
				requestMap.put("INSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_DATE_OF_START", tvInstallationElectricalMechanicalMachineryStartDate
						.getText().toString().trim());
				requestMap.put("INSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_DATE_OF_COMPLETION",
						tvInstallationElectricalMechanicalMachineryCompletedDate.getText().toString().trim());

				requestMap.put("CONSTRUCTION_OF_CHEMICAL_HOUSE_STATUS", constructionChemicalHouseStatus);
				requestMap.put("CONSTRUCTION_OF_CHEMICAL_HOUSE_DATE_OF_START", tvConstructionChemicalHouseStartDate.getText().toString().trim());
				requestMap.put("CONSTRUCTION_OF_CHEMICAL_HOUSE_DATE_OF_COMPLETION", tvConstructionChemicalHouseCompletedDate.getText().toString().trim());

				requestMap.put("CONSTRUCTION_OF_CHLORINE_ROOM_AND_INSTALLATION_OF_CHLORINATION_ARRANGEMENT_STATUS", constructionChlorineRoomStatus);
				requestMap.put("CONSTRUCTION_OF_CHLORINE_ROOM_AND_INSTALLATION_OF_CHLORINATION_ARRANGEMENT_DATE_OF_START",
						tvConstructionInstallationChlorineRoomStartDate.getText().toString().trim());
				requestMap.put("CONSTRUCTION_OF_CHLORINE_ROOM_DATE_OF_COMPLETION", tvConstructionInstallationChlorineRoomCompletionDate.getText().toString()
						.trim());

				requestMap.put("LAT", tv_latitude.getText().toString().trim());
				requestMap.put("LON", tv_longitude.getText().toString().trim());

				requestMap.put("CREATED_BY_ID", user.getDatabaseId());
				Log.v("RequestMAp", "" + new JSONObject(requestMap).toString());

				// ===================================================================

				if (isStrongNetwork) {

					if (isInsert) {
						isInsertService = true;
						volleyTaskManager.doPostSurfaceWaterTreatementInsert(requestMap);
					} else if (isUpdate) {
						isUpdateService = true;
						requestMap.put("ID", user.getSurfaceWaterTreatmentPlantUpdateId());
						volleyTaskManager.doPostSurfaceWaterTreatementUpdate(requestMap);
					}

					// ===============================================

					// writeToFile(new JSONObject(requestMap).toString());
				} else {

					// =================SAVE OFFLINE HERE

					// TODO OFFLINE POSTING - add condition here -->
					OfflineDataSet offlineDataSet = new OfflineDataSet(); //
					offlineDataSet.setServiceType("Plantation");
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

				// ===================================================================
				// isInsertService = true;
				// volleyTaskManager.doPostSurfaceWaterTreatementInsert(requestMap);
			}
		}
	}

	@Override
	public void onSuccess(JSONObject resultJsonObject) {
		volleyTaskManager.hideProgressDialog();
		Log.i("On Success", "" + resultJsonObject.toString());
		if (isDataPostingService) {

			isDataPostingService = false;

			if (resultJsonObject.toString() != null && !resultJsonObject.toString().trim().isEmpty()) {

				try {
					UserClass user = Util.fetchUserClass(mContext);

					String result = "";

					if (isInsertService) {

						isInsertService = false;
						result = new JSONObject(resultJsonObject.optString("SaveSurfaceWaterTreatmentPlantWTPResult")).optString("RES");

					} else if (isUpdateService) {

						isUpdateService = false;
						result = new JSONObject(resultJsonObject.optString("UpdateSurfaceWaterTreatmentPlantWTPResult")).optString("RES");
					}
					Log.v("TAG", "" + result);

					if (!result.equalsIgnoreCase("0")) {
						user.setSurfaceWaterTreatmentPlantUpdateId(result);
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

		} else if (isSurfaceWaterTreatementImageService) {

			isSurfaceWaterTreatementImageService = false;

			Log.d("Sent position:", imagePosition + "");
			String FDSaveSchoolImageDataResult = resultJsonObject.optString("SaveSurfaceWaterTreatmentPlantWTPMainImageResult");
			UserClass user = Util.fetchUserClass(mContext);

			if (user.getSurfaceWaterTreatmentPlantUpdateId().equalsIgnoreCase(FDSaveSchoolImageDataResult)) {
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
		// TODO Auto-generated method stub

	}

	private void fetchFormData() {
		if (Util.checkConnectivity(mContext)) {

			fetchSurfaceWaterTreatementPlantWTP(schemeId);
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

	private void fetchSurfaceWaterTreatementPlantWTP(String schemeId) {

		isFetchDataService = true;

		String value = "?scheme_id=" + schemeId;

		volleyTaskManager.doGetSurfaceWaterTreatmentPlantWTP(value);
	}

	private void parseInitialFetchedData(JSONObject resultJsonObject) {

		try {

			JSONArray resultJSONArray = new JSONArray(resultJsonObject.optString("GetSurfaceWaterTreatmentPlantWTPResult"));

			if (resultJSONArray.length() > 0) {
				ArrayList<WaterTreatementClass> waterTreatmentPlantList = new ArrayList<WaterTreatementClass>();

				for (int i = 0; i < resultJSONArray.length(); i++) {

					JSONObject waterTreatmentJSONObject = resultJSONArray.getJSONObject(i);
					WaterTreatementClass waterTreatmentPlant = new WaterTreatementClass();
					waterTreatmentPlant.setCONSTRUCTION_OF_CHEMICAL_HOUSE_DATE_OF_COMPLETION(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_CHEMICAL_HOUSE_DATE_OF_COMPLETION"));
					waterTreatmentPlant.setCONSTRUCTION_OF_CHEMICAL_HOUSE_DATE_OF_START(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_CHEMICAL_HOUSE_DATE_OF_START"));
					waterTreatmentPlant.setCONSTRUCTION_OF_CHEMICAL_HOUSE_STATUS(waterTreatmentJSONObject.optString("CONSTRUCTION_OF_CHEMICAL_HOUSE_STATUS"));
					waterTreatmentPlant.setCONSTRUCTION_OF_CHLORINE_ROOM_AND_INSTALLATION_OF_CHLORINATION_ARRANGEMENT_DATE_OF_START(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_CHLORINE_ROOM_AND_INSTALLATION_OF_CHLORINATION_ARRANGEMENT_DATE_OF_START"));
					waterTreatmentPlant.setCONSTRUCTION_OF_CHLORINE_ROOM_AND_INSTALLATION_OF_CHLORINATION_ARRANGEMENT_STATUS(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_CHLORINE_ROOM_AND_INSTALLATION_OF_CHLORINATION_ARRANGEMENT_STATUS"));
					waterTreatmentPlant.setCONSTRUCTION_OF_CHLORINE_ROOM_DATE_OF_COMPLETION(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_CHLORINE_ROOM_DATE_OF_COMPLETION"));
					waterTreatmentPlant.setCONSTRUCTION_OF_CLARIFLOCCULATOR_DATE_OF_COMPLETION(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_CLARIFLOCCULATOR_DATE_OF_COMPLETION"));
					waterTreatmentPlant.setCONSTRUCTION_OF_CLARIFLOCCULATOR_OR_PLATE_SETTLER_OR_TUBE_SETTLER_DATE_OF_START(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_CLARIFLOCCULATOR_OR_PLATE_SETTLER_OR_TUBE_SETTLER_DATE_OF_START"));
					waterTreatmentPlant.setCONSTRUCTION_OF_CLARIFLOCCULATOR_OR_PLATE_SETTLER_OR_TUBE_SETTLER_STATUS(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_CLARIFLOCCULATOR_OR_PLATE_SETTLER_OR_TUBE_SETTLER_STATUS"));
					waterTreatmentPlant.setCREATED_BY_ID(waterTreatmentJSONObject.optString("CREATED_BY_ID"));
					waterTreatmentPlant.setDISTRICT_ID(waterTreatmentJSONObject.optString("DISTRICT_ID"));
					waterTreatmentPlant.setID(waterTreatmentJSONObject.optString("ID"));
					waterTreatmentPlant.setCONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_DATE_OF_COMPLETION(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_DATE_OF_COMPLETION"));
					waterTreatmentPlant.setCONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_DATE_OF_START(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_DATE_OF_START"));
					waterTreatmentPlant.setCONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_STATUS(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_STATUS"));
					waterTreatmentPlant.setCONSTRUCTION_OF_FLUSH_MIXTURE_DATE_OF_COMPLETION(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_FLUSH_MIXTURE_DATE_OF_COMPLETION"));
					waterTreatmentPlant.setCONSTRUCTION_OF_FLUSH_MIXTURE_DATE_OF_START(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_FLUSH_MIXTURE_DATE_OF_START"));
					waterTreatmentPlant.setCONSTRUCTION_OF_FLUSH_MIXTURE_STATUS(waterTreatmentJSONObject.optString("CONSTRUCTION_OF_FLUSH_MIXTURE_STATUS"));
					waterTreatmentPlant.setCONSTRUCTION_OF_INTAKE_WELL_DATE_OF_COMPLETION(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_INTAKE_WELL_DATE_OF_COMPLETION"));
					waterTreatmentPlant.setCONSTRUCTION_OF_INTAKE_WELL_DATE_OF_START(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_INTAKE_WELL_DATE_OF_START"));
					waterTreatmentPlant.setCONSTRUCTION_OF_INTAKE_WELL_STATUS(waterTreatmentJSONObject.optString("CONSTRUCTION_OF_INTAKE_WELL_STATUS"));
					waterTreatmentPlant.setSCHEME_ID(waterTreatmentJSONObject.optString("SCHEME_ID"));

					waterTreatmentPlant.setCONSTRUCTION_OF_RAPID_SAND_FILTER_DATE_OF_COMPLETION(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_RAPID_SAND_FILTER_DATE_OF_COMPLETION"));
					waterTreatmentPlant.setCONSTRUCTION_OF_RAPID_SAND_FILTER_DATE_OF_START(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_RAPID_SAND_FILTER_DATE_OF_START"));
					waterTreatmentPlant.setCONSTRUCTION_OF_RAPID_SAND_FILTER_STATUS(waterTreatmentJSONObject
							.optString("CONSTRUCTION_OF_RAPID_SAND_FILTER_STATUS"));

					waterTreatmentPlant.setINSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_DATE_OF_COMPLETION(waterTreatmentJSONObject
							.optString("INSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_DATE_OF_COMPLETION"));
					waterTreatmentPlant.setINSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_DATE_OF_START(waterTreatmentJSONObject
							.optString("INSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_DATE_OF_START"));
					waterTreatmentPlant.setINSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_STATUS(waterTreatmentJSONObject
							.optString("INSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_STATUS"));

					UserClass user = Util.fetchUserClass(mContext);
					user.setSurfaceWaterTreatmentPlantUpdateId(waterTreatmentPlant.getID());
					Util.saveUserClass(mContext, user);

					waterTreatmentPlantList.add(waterTreatmentPlant);

				}

				isInsert = false;

				isUpdate = true;

				populateForm(waterTreatmentPlantList);
			} else {

				isInsert = true;
				isUpdate = false;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void populateForm(ArrayList<WaterTreatementClass> waterTreatmentPlantList) {

		for (WaterTreatementClass waterTreatmentPlant : waterTreatmentPlantList) {

			if (waterTreatmentPlant.getCONSTRUCTION_OF_CHEMICAL_HOUSE_STATUS() != null) {
				if (waterTreatmentPlant.getCONSTRUCTION_OF_CHEMICAL_HOUSE_STATUS().equalsIgnoreCase("Yet to be Started")) {
					constructionChemicalHouseStatus = "Yet to be Started";
					cbConstructionChemicalHouseToBeStarted.setChecked(true);
					cbConstructionChemicalHouseInProgress.setChecked(false);
					cbConstructionChemicalHouseCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_CHEMICAL_HOUSE_STATUS().equalsIgnoreCase("Work in Progress")) {
					constructionChemicalHouseStatus = "Work in Progress";
					cbConstructionChemicalHouseToBeStarted.setChecked(false);
					cbConstructionChemicalHouseInProgress.setChecked(true);
					cbConstructionChemicalHouseCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_CHEMICAL_HOUSE_STATUS().equalsIgnoreCase("Completed")) {
					constructionChemicalHouseStatus = "Completed";
					cbConstructionChemicalHouseToBeStarted.setChecked(false);
					cbConstructionChemicalHouseInProgress.setChecked(false);
					cbConstructionChemicalHouseCompleted.setChecked(true);

				}

				tvConstructionChemicalHouseStartDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_CHEMICAL_HOUSE_DATE_OF_START());
				tvConstructionChemicalHouseCompletedDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_CHEMICAL_HOUSE_DATE_OF_COMPLETION());

			}
			if (waterTreatmentPlant.getCONSTRUCTION_OF_CHLORINE_ROOM_AND_INSTALLATION_OF_CHLORINATION_ARRANGEMENT_STATUS() != null) {
				if (waterTreatmentPlant.getCONSTRUCTION_OF_CHLORINE_ROOM_AND_INSTALLATION_OF_CHLORINATION_ARRANGEMENT_STATUS().equalsIgnoreCase(
						"Yet to be Started")) {
					constructionChlorineRoomStatus = "Yet to be Started";
					cbConstructionInstallationChlorineRoomToBeStarted.setChecked(true);
					cbConstructionInstallationChlorineRoomInProgress.setChecked(false);
					cbConstructionInstallationChlorineRoomCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_CHLORINE_ROOM_AND_INSTALLATION_OF_CHLORINATION_ARRANGEMENT_STATUS().equalsIgnoreCase(
						"Work in Progress")) {
					constructionChlorineRoomStatus = "Work in Progress";
					cbConstructionInstallationChlorineRoomToBeStarted.setChecked(false);
					cbConstructionInstallationChlorineRoomInProgress.setChecked(true);
					cbConstructionInstallationChlorineRoomCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_CHLORINE_ROOM_AND_INSTALLATION_OF_CHLORINATION_ARRANGEMENT_STATUS().equalsIgnoreCase(
						"Completed")) {
					constructionChlorineRoomStatus = "Completed";
					cbConstructionInstallationChlorineRoomToBeStarted.setChecked(false);
					cbConstructionInstallationChlorineRoomInProgress.setChecked(false);
					cbConstructionInstallationChlorineRoomCompleted.setChecked(true);

				}

				tvConstructionInstallationChlorineRoomStartDate.setText(waterTreatmentPlant
						.getCONSTRUCTION_OF_CHLORINE_ROOM_AND_INSTALLATION_OF_CHLORINATION_ARRANGEMENT_DATE_OF_START());
				tvConstructionInstallationChlorineRoomCompletionDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_CHLORINE_ROOM_DATE_OF_COMPLETION());
			}
			if (waterTreatmentPlant.getCONSTRUCTION_OF_CLARIFLOCCULATOR_OR_PLATE_SETTLER_OR_TUBE_SETTLER_STATUS() != null) {
				if (waterTreatmentPlant.getCONSTRUCTION_OF_CLARIFLOCCULATOR_OR_PLATE_SETTLER_OR_TUBE_SETTLER_STATUS().equalsIgnoreCase("Yet to be Started")) {
					constructionClariflocculatorStatus = "Yet to be Started";
					cbConstructionClariflocculatorToBeStarted.setChecked(true);
					cbConstructionClariflocculatorInProgress.setChecked(false);
					cbConstructionClariflocculatorCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_CLARIFLOCCULATOR_OR_PLATE_SETTLER_OR_TUBE_SETTLER_STATUS().equalsIgnoreCase(
						"Work in Progress")) {
					constructionClariflocculatorStatus = "Work in Progress";
					cbConstructionClariflocculatorToBeStarted.setChecked(false);
					cbConstructionClariflocculatorInProgress.setChecked(true);
					cbConstructionClariflocculatorCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_CLARIFLOCCULATOR_OR_PLATE_SETTLER_OR_TUBE_SETTLER_STATUS().equalsIgnoreCase("Completed")) {
					constructionClariflocculatorStatus = "Completed";
					cbConstructionClariflocculatorToBeStarted.setChecked(false);
					cbConstructionClariflocculatorInProgress.setChecked(false);
					cbConstructionClariflocculatorCompleted.setChecked(true);

				}
				tvConstructionClariflocculatorStartDate.setText(waterTreatmentPlant
						.getCONSTRUCTION_OF_CLARIFLOCCULATOR_OR_PLATE_SETTLER_OR_TUBE_SETTLER_DATE_OF_START());
				tvConstructionClariflocculatorCompletiontDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_CLARIFLOCCULATOR_DATE_OF_COMPLETION());

			}
			if (waterTreatmentPlant.getCONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_STATUS() != null) {
				if (waterTreatmentPlant.getCONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_STATUS().equalsIgnoreCase("Yet to be Started")) {
					constructionClearWaterPumpHouseStatus = "Yet to be Started";
					cbConstructionClearWaterPumpHouseToBeStarted.setChecked(true);
					cbConstructionClearWaterPumpHouseInProgress.setChecked(false);
					cbConstructionClearWaterPumpHouseCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_STATUS().equalsIgnoreCase("Work in Progress")) {
					constructionClearWaterPumpHouseStatus = "Work in Progress";
					cbConstructionClearWaterPumpHouseToBeStarted.setChecked(false);
					cbConstructionClearWaterPumpHouseInProgress.setChecked(true);
					cbConstructionClearWaterPumpHouseCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_STATUS().equalsIgnoreCase("Completed")) {
					constructionClearWaterPumpHouseStatus = "Completed";
					cbConstructionClearWaterPumpHouseToBeStarted.setChecked(false);
					cbConstructionClearWaterPumpHouseInProgress.setChecked(false);
					cbConstructionClearWaterPumpHouseCompleted.setChecked(true);

				}

				tvConstructionClearWaterPumpHouseStartDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_DATE_OF_START());
				tvConstructionClearWaterPumpHouseCompletedDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_CLEAR_WATER_PUMP_HOUSE_DATE_OF_COMPLETION());
			}
			if (waterTreatmentPlant.getCONSTRUCTION_OF_FLUSH_MIXTURE_STATUS() != null) {
				if (waterTreatmentPlant.getCONSTRUCTION_OF_FLUSH_MIXTURE_STATUS().equalsIgnoreCase("Yet to be Started")) {
					constructionFlushMixtureStatus = "Yet to be Started";
					cbConstructionFlushToBeStarted.setChecked(true);
					cbConstructionFlushInProgress.setChecked(false);
					cbConstructionFlushCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_FLUSH_MIXTURE_STATUS().equalsIgnoreCase("Work in Progress")) {
					constructionFlushMixtureStatus = "Work in Progress";
					cbConstructionFlushToBeStarted.setChecked(false);
					cbConstructionFlushInProgress.setChecked(true);
					cbConstructionFlushCompleted.setChecked(false);
				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_FLUSH_MIXTURE_STATUS().equalsIgnoreCase("Completed")) {
					constructionFlushMixtureStatus = "Completed";
					cbConstructionFlushToBeStarted.setChecked(false);
					cbConstructionFlushInProgress.setChecked(false);
					cbConstructionFlushCompleted.setChecked(true);

				}
				tvConstructionFlushStartDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_FLUSH_MIXTURE_DATE_OF_START());
				tvConstructionFlushCompletiontDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_FLUSH_MIXTURE_DATE_OF_COMPLETION());

			}
			if (waterTreatmentPlant.getCONSTRUCTION_OF_INTAKE_WELL_STATUS() != null) {
				if (waterTreatmentPlant.getCONSTRUCTION_OF_INTAKE_WELL_STATUS().equalsIgnoreCase("Yet to be Started")) {
					constructionIntakeWellStatus = "Yet to be Started";
					cbConstructionInakeToBeStarted.setChecked(true);
					cbConstructionInakeWorkOnProgress.setChecked(false);
					cbConstructionInakeCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_INTAKE_WELL_STATUS().equalsIgnoreCase("Work in Progress")) {
					constructionIntakeWellStatus = "Work in Progress";
					cbConstructionInakeToBeStarted.setChecked(false);
					cbConstructionInakeWorkOnProgress.setChecked(true);
					cbConstructionInakeCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_INTAKE_WELL_STATUS().equalsIgnoreCase("Completed")) {
					constructionIntakeWellStatus = "Completed";
					cbConstructionInakeToBeStarted.setChecked(false);
					cbConstructionInakeWorkOnProgress.setChecked(false);
					cbConstructionInakeCompleted.setChecked(true);

				}
				tvConstructionIntakeStartDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_INTAKE_WELL_DATE_OF_START());
				tvConstructionIntakeCompletiontDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_INTAKE_WELL_DATE_OF_COMPLETION());

			}

			if (waterTreatmentPlant.getCONSTRUCTION_OF_RAPID_SAND_FILTER_STATUS() != null) {
				if (waterTreatmentPlant.getCONSTRUCTION_OF_RAPID_SAND_FILTER_STATUS().equalsIgnoreCase("Yet to be Started")) {
					constructionRapidSandFilterStatus = "Yet to be Started";
					cbConstructionRapidSandFilterToBeStarted.setChecked(true);
					cbConstructionRapidSandFilterInProgress.setChecked(false);
					cbConstructionRapidSandFilterCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_RAPID_SAND_FILTER_STATUS().equalsIgnoreCase("Work in Progress")) {
					constructionRapidSandFilterStatus = "Work in Progress";
					cbConstructionRapidSandFilterToBeStarted.setChecked(false);
					cbConstructionRapidSandFilterInProgress.setChecked(true);
					cbConstructionRapidSandFilterCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getCONSTRUCTION_OF_RAPID_SAND_FILTER_STATUS().equalsIgnoreCase("Completed")) {
					constructionRapidSandFilterStatus = "Completed";
					cbConstructionRapidSandFilterToBeStarted.setChecked(false);
					cbConstructionRapidSandFilterInProgress.setChecked(false);
					cbConstructionRapidSandFilterCompleted.setChecked(true);

				}
				tvConstructionRapidSandFilterStartDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_RAPID_SAND_FILTER_DATE_OF_START());
				tvConstructionRapidSandFilterCompletedDate.setText(waterTreatmentPlant.getCONSTRUCTION_OF_RAPID_SAND_FILTER_DATE_OF_COMPLETION());

			}

			if (waterTreatmentPlant.getINSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_STATUS() != null) {
				if (waterTreatmentPlant.getINSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_STATUS().equalsIgnoreCase("Yet to be Started")) {
					installationElectricalMechanicalMachineryStatus = "Yet to be Started";
					cbInstallationElectricalMechanicalMachineryToBeStarted.setChecked(true);
					cbInstallationElectricalMechanicalMachineryInProgress.setChecked(false);
					cbInstallationElectricalMechanicalMachineryCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getINSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_STATUS().equalsIgnoreCase("Work in Progress")) {
					installationElectricalMechanicalMachineryStatus = "Work in Progress";
					cbInstallationElectricalMechanicalMachineryToBeStarted.setChecked(false);
					cbInstallationElectricalMechanicalMachineryInProgress.setChecked(true);
					cbInstallationElectricalMechanicalMachineryCompleted.setChecked(false);

				} else if (waterTreatmentPlant.getINSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_STATUS().equalsIgnoreCase("Completed")) {
					installationElectricalMechanicalMachineryStatus = "Completed";
					cbInstallationElectricalMechanicalMachineryToBeStarted.setChecked(false);
					cbInstallationElectricalMechanicalMachineryInProgress.setChecked(false);
					cbInstallationElectricalMechanicalMachineryCompleted.setChecked(true);

				}
				tvInstallationElectricalMechanicalMachineryStartDate.setText(waterTreatmentPlant
						.getINSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_DATE_OF_START());
				tvInstallationElectricalMechanicalMachineryCompletedDate.setText(waterTreatmentPlant
						.getINSTALLATION_OF_ELECTRICAL_AND_MECHANICAL_MACHINERY_DATE_OF_COMPLETION());

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
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, SurfaceWaterTreatementActivity.this, requestCode);
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
		Util.trimCache(SurfaceWaterTreatementActivity.this);
	}

	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		Log.d("EntryFormActivity", "Location update stopped .......................");
	}
}
