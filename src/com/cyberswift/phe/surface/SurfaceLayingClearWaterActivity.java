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
import com.cyberswift.phe.dto.LayingClearWaterClass;
import com.cyberswift.phe.dto.OfflineDataSet;
import com.cyberswift.phe.dto.UserClass;
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

public class SurfaceLayingClearWaterActivity extends BaseActivity implements ServerResponseCallback, OnClickListener, LocationListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private Context mContext;

	// ============ Construction of Zonal OH reservoir/Zonal Head Work Site
	private CheckBox cbConstructionOfZonalToBeStarted, cbConstructionOfZonalInProgress, cbConstructionOfZonalCompleted;
	private TextView tvConstructionOfZonalStartDate, tvConstructionOfZonalCompletionDate;
	private RelativeLayout rlConstructionOfZonalStartDate, rlConstructionOfZonalCompletionDate;
	private Button btn_ConstructionOfZonalPhoto;
	private ViewPager vp_ConstructionOfZonalSelectedImages;
	private TextView tv_ConstructionOfZonalImageProgress;
	private View v_ConstructionOfZonalSwipeLeft, v_ConstructionOfZonalSwipeRight;

	// ============ Construction of Boosting Station
	private CheckBox cbConstructionOfBoostingToBeStarted, cbConstructionOfBoostingInProgress, cbConstructionOfBoostingCompleted;
	private TextView tvConstructionOfBoostingStartDate, tvConstructionOfBoostingCompletionDate;
	private RelativeLayout rlConstructionOfBoostingStartDate, rlConstructionOfBoostingCompletionDate;
	private Button btn_ConstructionOfBoostingPhoto;
	private ViewPager vp_ConstructionOfBoostingSelectedImages;
	private TextView tv_ConstructionOfBoostingImageProgress;
	private View v_ConstructionOfBoostingSwipeLeft, v_ConstructionOfBoostingSwipeRight;

	private TextView tv_latitude, tv_longitude;

	/*-----------------------------------------------*/
	private int districtId = 0;
	private String schemeId = "";

	private String constructionOfZonalStatus = "";

	private DatePickerDialog.OnDateSetListener constructionOfZonalStartDateListener;
	private DatePickerDialog.OnDateSetListener constructionOfZonalStopDateListener;

	private String constructionOfBoostingStatus = "";

	private DatePickerDialog.OnDateSetListener constructionOfBoostingStartDateListener;
	private DatePickerDialog.OnDateSetListener constructionOfBoostingStopDateListener;

	/*-----------------------------------------------*/

	private VolleyTaskManager volleyTaskManager;
	private ProgressDialog mProgressDialog;
	private boolean isFetchDataService = false;
	private boolean isConstructionZonal = false;
	private boolean isConstructionBoosting = false;
	private ArrayList<ImageClass> constructionZonalImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> constructionBoostingImagesList = new ArrayList<ImageClass>();
	private ArrayList<ImageClass> allImagesList = new ArrayList<ImageClass>();
	private int constructionZonalImagesCount = 0;
	private int constructionBoostingImagesCount = 0;

	@SuppressWarnings("unused")
	private static final int DATE_DIALOG_ID = 316, PICTURE_GALLERY_REQUEST = 2572, CAMERA_PIC_REQUEST = 1337;

	private Uri mCapturedImageURI;
	private boolean isDataPostingService = false;

	private boolean isInsertService = false;
	private boolean isUpdateService = false;

	private boolean isInsert = true;
	private boolean isUpdate = false;

	MyReceiver myReceiver;
	private int imagePosition;
	private boolean isSurfaceWaterTreatementImageService = false;

	// ============LOCATION

	private Location mCurrentLocation;
	private LocationRequest mLocationRequest;
	private AlertDialog systemAlertDialog;
	private FusedLocationProviderApi fusedLocationProviderApi;
	private GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surface_laying_clear_water_main);
		mContext = SurfaceLayingClearWaterActivity.this;

		initView();
		fetchValuesFromLandingPage();
		fetchFormData();
	}

	private void initView() {

		// ============ Construction of Zonal OH reservoir/Zonal Head Work Site
		cbConstructionOfZonalToBeStarted = (CheckBox) findViewById(R.id.cbConstructionOfZonalToBeStarted);
		cbConstructionOfZonalInProgress = (CheckBox) findViewById(R.id.cbConstructionOfZonalInProgress);
		cbConstructionOfZonalCompleted = (CheckBox) findViewById(R.id.cbConstructionOfZonalCompleted);
		tvConstructionOfZonalStartDate = (TextView) findViewById(R.id.tvConstructionOfZonalStartDate);
		tvConstructionOfZonalCompletionDate = (TextView) findViewById(R.id.tvConstructionOfZonalCompletionDate);
		rlConstructionOfZonalStartDate = (RelativeLayout) findViewById(R.id.rlConstructionOfZonalStartDate);
		rlConstructionOfZonalCompletionDate = (RelativeLayout) findViewById(R.id.rlConstructionOfZonalCompletionDate);
		btn_ConstructionOfZonalPhoto = (Button) findViewById(R.id.btn_ConstructionOfZonalPhoto);
		vp_ConstructionOfZonalSelectedImages = (ViewPager) findViewById(R.id.vp_ConstructionOfZonalSelectedImages);
		tv_ConstructionOfZonalImageProgress = (TextView) findViewById(R.id.tv_ConstructionOfZonalImageProgress);
		v_ConstructionOfZonalSwipeLeft = (View) findViewById(R.id.v_ConstructionOfZonalSwipeLeft);
		v_ConstructionOfZonalSwipeRight = (View) findViewById(R.id.v_ConstructionOfZonalSwipeRight);

		cbConstructionOfZonalToBeStarted.setOnClickListener(this);
		cbConstructionOfZonalInProgress.setOnClickListener(this);
		cbConstructionOfZonalCompleted.setOnClickListener(this);
		rlConstructionOfZonalStartDate.setOnClickListener(this);
		rlConstructionOfZonalCompletionDate.setOnClickListener(this);
		btn_ConstructionOfZonalPhoto.setOnClickListener(this);

		// ============ Construction of Boosting Station
		cbConstructionOfBoostingToBeStarted = (CheckBox) findViewById(R.id.cbConstructionOfBoostingToBeStarted);
		cbConstructionOfBoostingInProgress = (CheckBox) findViewById(R.id.cbConstructionOfBoostingInProgress);
		cbConstructionOfBoostingCompleted = (CheckBox) findViewById(R.id.cbConstructionOfBoostingCompleted);
		tvConstructionOfBoostingStartDate = (TextView) findViewById(R.id.tvConstructionOfBoostingStartDate);
		tvConstructionOfBoostingCompletionDate = (TextView) findViewById(R.id.tvConstructionOfBoostingCompletionDate);
		rlConstructionOfBoostingStartDate = (RelativeLayout) findViewById(R.id.rlConstructionOfBoostingStartDate);
		rlConstructionOfBoostingCompletionDate = (RelativeLayout) findViewById(R.id.rlConstructionOfBoostingCompletionDate);
		btn_ConstructionOfBoostingPhoto = (Button) findViewById(R.id.btn_ConstructionOfBoostingPhoto);
		vp_ConstructionOfBoostingSelectedImages = (ViewPager) findViewById(R.id.vp_ConstructionOfBoostingSelectedImages);
		tv_ConstructionOfBoostingImageProgress = (TextView) findViewById(R.id.tv_ConstructionOfBoostingImageProgress);
		v_ConstructionOfBoostingSwipeLeft = (View) findViewById(R.id.v_ConstructionOfBoostingSwipeLeft);
		v_ConstructionOfBoostingSwipeRight = (View) findViewById(R.id.v_ConstructionOfBoostingSwipeRight);

		cbConstructionOfBoostingToBeStarted.setOnClickListener(this);
		cbConstructionOfBoostingInProgress.setOnClickListener(this);
		cbConstructionOfBoostingCompleted.setOnClickListener(this);
		rlConstructionOfBoostingStartDate.setOnClickListener(this);
		rlConstructionOfBoostingCompletionDate.setOnClickListener(this);
		btn_ConstructionOfBoostingPhoto.setOnClickListener(this);

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

		constructionOfZonalStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionOfZonalStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionOfZonalStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionOfZonalCompletionDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionOfBoostingStartDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionOfBoostingStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};
		constructionOfBoostingStopDateListener = new OnDateSetListener() {
			@SuppressLint("SimpleDateFormat")
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				tvConstructionOfBoostingCompletionDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
			}
		};

	}

	private void fetchFormData() {
		if (Util.checkConnectivity(mContext)) {

			fetchSurfaceLayingClearWaterMain(schemeId);
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

	private void fetchSurfaceLayingClearWaterMain(String schemeId) {

		isFetchDataService = true;

		String value = "?scheme_id=" + schemeId;

		volleyTaskManager.doGetSurfaceLayingClearWaterMain(value);
	}

	public void onLogoutClicked(View v) {

	}

	public void onPostClicked(View mView) {
		if (Util.checkConnectivity(mContext)) {

			boolean isValid = checkInputValidity();
			Log.d("IS VALID", "" + isValid);
			if (isValid) {

				Util.hideSoftKeyboard(mContext, mView);

				allImagesList.addAll(constructionZonalImagesList);
				allImagesList.addAll(constructionBoostingImagesList);

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

		// ============ Construction of Zonal OH reservoir/Zonal Head Work Site
		case R.id.cbConstructionOfZonalToBeStarted:

			constructionOfZonalStatus = "Yet to be Started";
			cbConstructionOfZonalToBeStarted.setChecked(true);
			cbConstructionOfZonalInProgress.setChecked(false);
			cbConstructionOfZonalCompleted.setChecked(false);
			break;
		case R.id.cbConstructionOfZonalInProgress:

			constructionOfZonalStatus = "Work in Progress";
			cbConstructionOfZonalToBeStarted.setChecked(false);
			cbConstructionOfZonalInProgress.setChecked(true);
			cbConstructionOfZonalCompleted.setChecked(false);

			break;
		case R.id.cbConstructionOfZonalCompleted:

			constructionOfZonalStatus = "Completed";
			cbConstructionOfZonalToBeStarted.setChecked(false);
			cbConstructionOfZonalInProgress.setChecked(false);
			cbConstructionOfZonalCompleted.setChecked(true);

			break;
		case R.id.rlConstructionOfZonalStartDate:
			new DatePickerDialog(this, constructionOfZonalStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.rlConstructionOfZonalCompletionDate:
			new DatePickerDialog(this, constructionOfZonalStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.btn_ConstructionOfZonalPhoto:
			isConstructionZonal = true;
			imageDialog();

			break;
		// ============ Construction of Boosting Station
		case R.id.cbConstructionOfBoostingToBeStarted:

			constructionOfBoostingStatus = "Yet to be Started";
			cbConstructionOfBoostingToBeStarted.setChecked(true);
			cbConstructionOfBoostingInProgress.setChecked(false);
			cbConstructionOfBoostingCompleted.setChecked(false);
			break;
		case R.id.cbConstructionOfBoostingInProgress:

			constructionOfBoostingStatus = "Work in Progress";
			cbConstructionOfBoostingToBeStarted.setChecked(false);
			cbConstructionOfBoostingInProgress.setChecked(true);
			cbConstructionOfBoostingCompleted.setChecked(false);

			break;
		case R.id.cbConstructionOfBoostingCompleted:

			constructionOfBoostingStatus = "Completed";
			cbConstructionOfBoostingToBeStarted.setChecked(false);
			cbConstructionOfBoostingInProgress.setChecked(false);
			cbConstructionOfBoostingCompleted.setChecked(true);

			break;
		case R.id.rlConstructionOfBoostingStartDate:
			new DatePickerDialog(this, constructionOfBoostingStartDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.rlConstructionOfBoostingCompletionDate:
			new DatePickerDialog(this, constructionOfBoostingStopDateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
					currentDate.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.btn_ConstructionOfBoostingPhoto:

			isConstructionBoosting = true;
			imageDialog();
			break;
		default:
			break;
		}

	}

	private boolean checkInputValidity() {
		if (constructionOfZonalStatus.isEmpty() || constructionOfZonalStatus.equalsIgnoreCase("") || constructionOfBoostingStatus.isEmpty()
				|| constructionOfBoostingStatus.equalsIgnoreCase("") || tvConstructionOfZonalStartDate.getText().toString().trim().equalsIgnoreCase("")
				|| tvConstructionOfZonalCompletionDate.getText().toString().trim().equalsIgnoreCase("")
				|| tvConstructionOfBoostingStartDate.getText().toString().trim().equalsIgnoreCase("")
				|| tvConstructionOfBoostingCompletionDate.getText().toString().trim().equalsIgnoreCase("")
				|| tv_latitude.getText().toString().trim().equalsIgnoreCase("") || tv_longitude.getText().toString().trim().equalsIgnoreCase("")) {

			Util.showMessageWithOk(mContext, "Please fill all the required fields.");
			return false;
		} else
			return true;
	}

	private void imageDialog() {
		if ((isConstructionZonal && constructionZonalImagesList.size() < 3) || (isConstructionBoosting && constructionBoostingImagesList.size() < 3)) {

			final Dialog customDialog = new Dialog(mContext);

			customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

			LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.image_select_dialog, null);

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
					isConstructionZonal = false;
					isConstructionBoosting = false;

					customDialog.dismiss();
				}
			});

			customDialog.setCancelable(false);
			customDialog.setContentView(view);
			customDialog.setCanceledOnTouchOutside(false);
			// Start AlertDialog
			customDialog.show();
		} else {
			if (isConstructionZonal) {
				Util.showMessageWithOk(mContext,
						"Maximum number of \"Construction of Zonal OH reservoir/Zonal Head Work Site\" images has already been selected!");
			} else if (isConstructionBoosting) {
				Util.showMessageWithOk(mContext, "Maximum number of \"Construction of Boosting Station\" images has already been selected!");
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

			isConstructionZonal = false;
			isConstructionBoosting = false;

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
			if (isConstructionZonal) {
				imageClass.setImageType("Construction_Of_Zonal_OH_Reservoir_Zonal_Head_Work_Site");
				imageClass.setImageCount(constructionZonalImagesCount + 1);
				constructionZonalImagesList.add(imageClass);
				isConstructionZonal = false;
				constructionZonalImagesCount++;
			} else if (isConstructionBoosting) {
				imageClass.setImageType("Construction_Of_Boosting_Station");
				imageClass.setImageCount(constructionBoostingImagesCount + 1);
				constructionBoostingImagesList.add(imageClass);
				isConstructionBoosting = false;
				constructionBoostingImagesCount++;
			}
		} else {
			isConstructionZonal = false;
			isConstructionBoosting = false;
			Toast.makeText(this, picturePath + "not found", Toast.LENGTH_LONG).show();
		}

		imageUpdateOnView();
	}

	private void imageUpdateOnView() {

		// ============ Construction of Zonal OH reservoir / Zonal Head Work
		// Site
		ImageAdapter constructionZonalAdapter = new ImageAdapter(this, constructionZonalImagesList);
		vp_ConstructionOfZonalSelectedImages.setAdapter(constructionZonalAdapter);

		if (constructionZonalImagesList.size() == 0) {
			vp_ConstructionOfZonalSelectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstructionOfZonalSelectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstructionOfZonalSelectedImages.setCurrentItem(constructionZonalImagesList.size() - 1);
		}

		if (constructionZonalImagesList.size() <= 1) {
			tv_ConstructionOfZonalImageProgress.setText("[Image added " + constructionZonalImagesList.size() + "/3]");
			v_ConstructionOfZonalSwipeRight.setVisibility(View.INVISIBLE);
			v_ConstructionOfZonalSwipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstructionOfZonalImageProgress.setText("Slide to view other images\n[Images added " + constructionZonalImagesList.size() + "/3]");
			v_ConstructionOfZonalSwipeRight.setVisibility(View.VISIBLE);
			v_ConstructionOfZonalSwipeLeft.setVisibility(View.VISIBLE);
		}

		// ============ Construction of Boosting Station
		ImageAdapter constructionBoostingAdapter = new ImageAdapter(this, constructionBoostingImagesList);
		vp_ConstructionOfBoostingSelectedImages.setAdapter(constructionBoostingAdapter);

		if (constructionBoostingImagesList.size() == 0) {
			vp_ConstructionOfBoostingSelectedImages.setBackgroundResource(R.drawable.default_empty);
		} else {
			vp_ConstructionOfBoostingSelectedImages.setBackgroundColor(Color.parseColor("#D7D7D7"));

			vp_ConstructionOfBoostingSelectedImages.setCurrentItem(constructionBoostingImagesList.size() - 1);
		}

		if (constructionBoostingImagesList.size() <= 1) {
			tv_ConstructionOfBoostingImageProgress.setText("[Image added " + constructionBoostingImagesList.size() + "/3]");
			v_ConstructionOfZonalSwipeRight.setVisibility(View.INVISIBLE);
			v_ConstructionOfZonalSwipeLeft.setVisibility(View.INVISIBLE);
		} else {
			tv_ConstructionOfBoostingImageProgress.setText("Slide to view other images\n[Images added " + constructionBoostingImagesList.size() + "/3]");
			v_ConstructionOfZonalSwipeRight.setVisibility(View.VISIBLE);
			v_ConstructionOfZonalSwipeLeft.setVisibility(View.VISIBLE);
		}

	}

	private void postFormWebserviceCalling() {

		isDataPostingService = true;
		speedCheck(); // TODO

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
			Log.v("TAG", "Update ID: " + user.getSurfaceLayingOfClearWaterMainUpdateId());
			HashMap<String, String> requestMap = new HashMap<String, String>();

			if (isDataPostingService) {

				requestMap.put("DISTRICT_ID", "" + districtId);
				requestMap.put("SCHEME_ID", schemeId);

				requestMap.put("CONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_STATUS", constructionOfZonalStatus);
				requestMap.put("CONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_DATE_OF_START", tvConstructionOfZonalStartDate.getText().toString()
						.trim());
				requestMap.put("CONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_DATE_OF_COMPLETION", tvConstructionOfZonalCompletionDate.getText()
						.toString().trim());

				requestMap.put("CONSTRUCTION_OF_BOOSTING_STATION_STATUS", constructionOfBoostingStatus);
				requestMap.put("CONSTRUCTION_OF_BOOSTING_STATION_DATE_OF_START", tvConstructionOfBoostingStartDate.getText().toString().trim());
				requestMap.put("CONSTRUCTION_OF_BOOSTING_STATION_DATE_OF_COMPLETION", tvConstructionOfBoostingCompletionDate.getText().toString().trim());
				requestMap.put("LAT", tv_latitude.getText().toString().trim());
				requestMap.put("LON", tv_longitude.getText().toString().trim());
				requestMap.put("CREATED_BY_ID", user.getDatabaseId());
				Log.v("RequestMAp", "" + new JSONObject(requestMap).toString());

				// ===================================================================

				if (isStrongNetwork) {

					if (isInsert) {
						isInsertService = true;
						volleyTaskManager.doPostSurfaceLayingClearWaterInsert(requestMap);
					} else if (isUpdate) {
						isUpdateService = true;
						requestMap.put("ID", user.getSurfaceLayingOfClearWaterMainUpdateId());
						volleyTaskManager.doPostSurfaceLayingClearWaterUpdate(requestMap);
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
						result = new JSONObject(resultJsonObject.optString("SaveSurfaceLayingOfClearWaterMainResult")).optString("RES");

					} else if (isUpdateService) {

						isUpdateService = false;
						result = new JSONObject(resultJsonObject.optString("UpdateSurfaceLayingOfClearWaterMainResult")).optString("RES");
					}
					Log.v("TAG", "" + result);

					if (!result.equalsIgnoreCase("0")) {
						user.setSurfaceLayingOfClearWaterMainUpdateId(result);
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

	}

	private void sendingImages(final int position) {

		imagePosition = position;
		UserClass user = Util.fetchUserClass(mContext);
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("ID", user.getSurfaceLayingOfClearWaterMainUpdateId());
		paramsMap.put("IMAGE_TYPE", allImagesList.get(position).getImageType());
		paramsMap.put("IMAGE_NO", String.valueOf(allImagesList.get(position).getImageCount()));
		paramsMap.put("IMAGE_DATA", allImagesList.get(position).getBase64value());

		isSurfaceWaterTreatementImageService = true;
		volleyTaskManager.doPostSurfaceLayingClearWaterImage(paramsMap);
	}

	private void parseInitialFetchedData(JSONObject resultJsonObject) {

		try {

			JSONArray resultJSONArray = new JSONArray(resultJsonObject.optString("GetSurfaceLayingOfClearWaterMainResult"));

			if (resultJSONArray.length() > 0) {
				ArrayList<LayingClearWaterClass> layingClearWaterMainList = new ArrayList<LayingClearWaterClass>();

				for (int i = 0; i < resultJSONArray.length(); i++) {

					JSONObject layingClearWaterMainJSONObject = resultJSONArray.getJSONObject(i);
					LayingClearWaterClass layingClearWaterMain = new LayingClearWaterClass();
					layingClearWaterMain.setCONSTRUCTION_OF_BOOSTING_STATION_DATE_OF_COMPLETION(layingClearWaterMainJSONObject
							.optString("CONSTRUCTION_OF_BOOSTING_STATION_DATE_OF_COMPLETION"));
					layingClearWaterMain.setCONSTRUCTION_OF_BOOSTING_STATION_DATE_OF_START(layingClearWaterMainJSONObject
							.optString("CONSTRUCTION_OF_BOOSTING_STATION_DATE_OF_START"));
					layingClearWaterMain.setCONSTRUCTION_OF_BOOSTING_STATION_STATUS(layingClearWaterMainJSONObject
							.optString("CONSTRUCTION_OF_BOOSTING_STATION_STATUS"));
					layingClearWaterMain.setCONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_DATE_OF_COMPLETION(layingClearWaterMainJSONObject
							.optString("CONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_DATE_OF_COMPLETION"));
					layingClearWaterMain.setCONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_DATE_OF_START(layingClearWaterMainJSONObject
							.optString("CONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_DATE_OF_START"));
					layingClearWaterMain.setCONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_STATUS(layingClearWaterMainJSONObject
							.optString("CONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_STATUS"));
					layingClearWaterMain.setCREATED_BY_ID(layingClearWaterMainJSONObject.optString("CREATED_BY_ID"));
					layingClearWaterMain.setDISTRICT_ID(layingClearWaterMainJSONObject.optString("DISTRICT_ID"));
					layingClearWaterMain.setID(layingClearWaterMainJSONObject.optString("ID"));
					layingClearWaterMain.setSCHEME_ID(layingClearWaterMainJSONObject.optString("SCHEME_ID"));

					UserClass user = Util.fetchUserClass(mContext);
					user.setSurfaceLayingOfClearWaterMainUpdateId(layingClearWaterMain.getID());
					Util.saveUserClass(mContext, user);

					layingClearWaterMainList.add(layingClearWaterMain);

				}

				isInsert = false;

				isUpdate = true;

				populateForm(layingClearWaterMainList);
			} else {

				isInsert = true;
				isUpdate = false;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void populateForm(ArrayList<LayingClearWaterClass> layingClearWaterMainList) {

		for (LayingClearWaterClass layingClearWaterMain : layingClearWaterMainList) {

			if (layingClearWaterMain.getCONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_STATUS() != null) {
				if (layingClearWaterMain.getCONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_STATUS().equalsIgnoreCase("Yet to be Started")) {
					constructionOfZonalStatus = "Yet to be Started";
					cbConstructionOfZonalToBeStarted.setChecked(true);
					cbConstructionOfZonalInProgress.setChecked(false);
					cbConstructionOfZonalCompleted.setChecked(false);

				} else if (layingClearWaterMain.getCONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_STATUS().equalsIgnoreCase("Work in Progress")) {
					constructionOfZonalStatus = "Work in Progress";
					cbConstructionOfZonalToBeStarted.setChecked(false);
					cbConstructionOfZonalInProgress.setChecked(true);
					cbConstructionOfZonalCompleted.setChecked(false);

				} else if (layingClearWaterMain.getCONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_STATUS().equalsIgnoreCase("Completed")) {
					constructionOfZonalStatus = "Completed";
					cbConstructionOfZonalToBeStarted.setChecked(false);
					cbConstructionOfZonalInProgress.setChecked(false);
					cbConstructionOfZonalCompleted.setChecked(true);

				}

				tvConstructionOfZonalStartDate.setText(layingClearWaterMain.getCONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_DATE_OF_START());
				tvConstructionOfZonalCompletionDate.setText(layingClearWaterMain
						.getCONSTRUCTION_OF_ZONAL_OH_RESERVOIR_ZONAL_HEAD_WORK_SITE_DATE_OF_COMPLETION());

			}
			if (layingClearWaterMain.getCONSTRUCTION_OF_BOOSTING_STATION_STATUS() != null) {
				if (layingClearWaterMain.getCONSTRUCTION_OF_BOOSTING_STATION_STATUS().equalsIgnoreCase("Yet to be Started")) {
					constructionOfBoostingStatus = "Yet to be Started";
					cbConstructionOfBoostingToBeStarted.setChecked(true);
					cbConstructionOfBoostingInProgress.setChecked(false);
					cbConstructionOfBoostingCompleted.setChecked(false);

				} else if (layingClearWaterMain.getCONSTRUCTION_OF_BOOSTING_STATION_STATUS().equalsIgnoreCase("Work in Progress")) {
					constructionOfBoostingStatus = "Work in Progress";
					cbConstructionOfBoostingToBeStarted.setChecked(false);
					cbConstructionOfBoostingInProgress.setChecked(true);
					cbConstructionOfBoostingCompleted.setChecked(false);

				} else if (layingClearWaterMain.getCONSTRUCTION_OF_BOOSTING_STATION_STATUS().equalsIgnoreCase("Completed")) {
					constructionOfBoostingStatus = "Completed";
					cbConstructionOfBoostingToBeStarted.setChecked(false);
					cbConstructionOfBoostingInProgress.setChecked(false);
					cbConstructionOfBoostingCompleted.setChecked(true);

				}

				tvConstructionOfBoostingStartDate.setText(layingClearWaterMain.getCONSTRUCTION_OF_BOOSTING_STATION_DATE_OF_START());
				tvConstructionOfBoostingCompletionDate.setText(layingClearWaterMain.getCONSTRUCTION_OF_BOOSTING_STATION_DATE_OF_COMPLETION());
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
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, SurfaceLayingClearWaterActivity.this, requestCode);
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
		Util.trimCache(SurfaceLayingClearWaterActivity.this);
	}

	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		Log.d("EntryFormActivity", "Location update stopped .......................");
	}
}
