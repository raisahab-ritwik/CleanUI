package com.cyberswift.phe.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cyberswift.phe.R;
import com.cyberswift.phe.app.AppController;
import com.cyberswift.phe.utility.ServerResponseCallback;
import com.cyberswift.phe.utility.Util;

@SuppressLint("ShowToast")
public class VolleyTaskManager extends ServiceConnector {
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private String TAG = "";
	private String tag_json_obj = "jobj_req";
	private boolean isToShowDialog = true, isToHideDialog = true;

	public VolleyTaskManager(Context context) {
		mContext = context;

		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Loading...");

		TAG = mContext.getClass().getSimpleName();
		Log.d("tag", TAG);
	}

	public void showProgressDialog() {
		if (!mProgressDialog.isShowing())
			mProgressDialog.show();
	}

	public void hideProgressDialog() {
		if (mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}

	/**
	 * 
	 * Making json object request
	 * */
	private void makeJsonObjReq(int method, String url, final Map<String, String> paramsMap) {
		if (isToShowDialog) {
			showProgressDialog();
		}

		Log.v("JSONObject", new JSONObject(paramsMap).toString());

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url, new JSONObject(paramsMap), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d(TAG, response.toString());
				// msgResponse.setText(response.toString());
				if (isToHideDialog) {
					hideProgressDialog();
				}
				// TODO On getting successful result:
				((ServerResponseCallback) mContext).onSuccess(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				hideProgressDialog();
				VolleyLog.d(TAG, "Error: " + error.getMessage());

				if (error instanceof TimeoutError || error instanceof NoConnectionError) {
					Log.d("error ocurred", "TimeoutError");
					Toast.makeText(mContext, mContext.getString(R.string.response_timeout), Toast.LENGTH_LONG).show();
				} else if (error instanceof AuthFailureError) {
					Log.d("error ocurred", "AuthFailureError");
					Toast.makeText(mContext, mContext.getString(R.string.auth_failure), Toast.LENGTH_LONG).show();
				} else if (error instanceof ServerError) {
					Log.d("error ocurred", "ServerError");
					Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_LONG).show();
				} else if (error instanceof NetworkError) {
					Log.d("error ocurred", "NetworkError");
					Toast.makeText(mContext, mContext.getString(R.string.network_error), Toast.LENGTH_LONG).show();
				} else if (error instanceof ParseError) {
					Log.d("error ocurred", "ParseError");
					Toast.makeText(mContext, mContext.getString(R.string.parse_error), Toast.LENGTH_LONG).show();
				}

				((ServerResponseCallback) mContext).onError();
			}
		}) {

			/**
			 * Passing some request headers
			 * */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json; charset=utf-8");
				return headers;
			}

			@Override
			protected Map<String, String> getParams() {
				/*
				 * Map<String, String> params = new HashMap<String, String>();
				 * params.put("name", "Androidhive"); params.put("email",
				 * "abc@androidhive.info"); params.put("pass", "password123");
				 */
				return paramsMap;
			}

		};

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

		// Cancelling request
		// ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
	}

	/**
	 * Service method calling for FDGetDistrict -->
	 **/

	public void doGetDistrict() {
		String url = getBaseURL() + "FDGetDistrict";
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	/**
	 * Service method calling for FDGetHabitation -->
	 **/

	public void doGetHabitation() {
		String url = getBaseURL() + "FDGetHabitation";
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	/**
	 * Service method calling for FDGetBlock -->
	 **/

	public void doGetBlock(String value) {
		String url = getBaseURL() + "FDGetBlock" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	/**
	 * Service method calling for FDGetCluster -->
	 **/

	public void doGetCluster(String value) {
		String url = getBaseURL() + "FDGetCluster" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	/**
	 * Service method calling for FDGetVillage -->
	 **/

	public void doGetVillage(String value) {
		String url = getBaseURL() + "FDGetVillage" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	/**
	 * Service method calling for FDSaveSchoolData -->
	 **/

	public void doSaveSchoolData(HashMap<String, String> paramsMap, boolean isToShowDialog) {
		this.isToShowDialog = isToShowDialog;
		String url = getBaseURL() + "FDSaveSchoolData";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(paramsMap);
		makeJsonObjReq(method, url, paramsMap);
	}

	/**
	 * Service method calling for Login -->
	 **/

	public void doLogin(HashMap<String, String> paramsMap, boolean isToHideDialog) {
		this.isToHideDialog = isToHideDialog;
		String url = getBaseURL() + "MobileUserLogIn";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(paramsMap);
		makeJsonObjReq(method, url, paramsMap);
	}

	/**
	 * Service method calling for FDGetSchool -->
	 **/

	public void doGetSchool(String value) {
		String url = getBaseURL() + "FDGetSchool" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	/**
	 * Service method calling for Registration -->
	 **/

	public void doRegistration(HashMap<String, String> paramsMap, boolean isToHideDialog) {
		this.isToHideDialog = isToHideDialog;
		String url = getBaseURL() + "ManageMobileUserSaveData";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(paramsMap);
		makeJsonObjReq(method, url, paramsMap);
	}

	/**
	 * Service method calling for FDGetBlockSerially -->
	 **/

	public void doFDGetBlockSerially(HashMap<String, String> paramsMap, boolean isToShowDialog, String loaderText) {
		this.isToShowDialog = isToShowDialog;
		String url = getBaseURL() + "FDGetBlockSerially";
		int method = Method.POST;

		mProgressDialog.setMessage(loaderText);

		Log.i("url", url);
		System.out.println(paramsMap);
		makeJsonObjReq(method, url, paramsMap);
	}

	/**
	 * Service method calling for FDGetClusterSerially -->
	 **/

	public void doFDGetClusterSerially(HashMap<String, String> paramsMap, boolean isToShowDialog, String loaderText) {
		this.isToShowDialog = isToShowDialog;
		String url = getBaseURL() + "FDGetClusterSerially";
		int method = Method.POST;

		mProgressDialog.setMessage(loaderText);

		Log.i("url", url);
		System.out.println(paramsMap);
		makeJsonObjReq(method, url, paramsMap);
	}

	/**
	 * Service method calling for FDGetVillageSerially -->
	 **/

	public void doFDGetVillageSerially(HashMap<String, String> paramsMap, boolean isToShowDialog, String loaderText) {
		this.isToShowDialog = isToShowDialog;
		String url = getBaseURL() + "FDGetVillageSerially";
		int method = Method.POST;

		mProgressDialog.setMessage(loaderText);

		Log.i("url", url);
		System.out.println(paramsMap);
		makeJsonObjReq(method, url, paramsMap);
	}

	/**
	 * Service method calling for FDGetSchoolSerially -->
	 **/

	public void doFDGetSchoolSerially(HashMap<String, String> paramsMap, boolean isToHideDialog, String loaderText) {
		this.isToHideDialog = isToHideDialog;
		String url = getBaseURL() + "FDGetSchoolSerially";
		int method = Method.POST;

		mProgressDialog.setMessage(loaderText);

		Log.i("url", url);
		System.out.println(paramsMap);
		makeJsonObjReq(method, url, paramsMap);
	}

	// ---------------------------------------------------->
	/**
	 * Service method calling for GetAllFormDataByUser -->
	 **/

	public void doGetAllFormDataByUser(String value, boolean isToShowDialog, boolean isToHideDialog) {
		this.isToShowDialog = isToShowDialog;
		this.isToHideDialog = isToHideDialog;
		String url = getBaseURL() + "GetAllFormDataByUser" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	public void doGetFormListBySchemeType(String value) {
		String url = getBaseURL() + "GetFormListBySchemeType" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	public void doGetSchemeName(String value) {
		System.out.println("---------------<< CAlled >>---------------");
		String url = getBaseURL() + "GetSchemeNameByDistrict" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	public void doGetSurfaceLandAcquisition(String value) {
		System.out.println("---------------<< CAlled >>---------------");
		String url = getBaseURL() + "GetSurfaceLandAcquisition" + value;
		int method = Method.GET;
		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}
	public void doGetSubSurfaceLandAcquisition(String value) {
		System.out.println("---------------<< CAlled >>---------------");
		String url = getBaseURL() + "GetSubSurfaceLandAcquisition" + value;
		int method = Method.GET;
		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}
//	public void doGetLandAcquisition(String value) {
//		System.out.println("---------------<< CAlled >>---------------");
//		String url = getBaseURL() + "GetSurfaceLandAcquisition" + value;
//		int method = Method.GET;
//
//		Log.i("url", url);
//		makeJsonObjReq(method, url, new HashMap<String, String>());
//	}

	public void doGetSurfacePowerConnection(String value) {
		System.out.println("---------------<< CAlled >>---------------");
		String url = getBaseURL() + "GetSurfacePowerConnection" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	public void doGetSurfaceIntakeAndRawWaterMain(String value) {
		System.out.println("---------------<< CAlled >>---------------");
		String url = getBaseURL() + "GetSurfaceIntakeAndRawWaterMain" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	public void doGetSurfaceWaterTreatmentPlantWTP(String value) {
		System.out.println("---------------<< CAlled >>---------------");
		String url = getBaseURL() + "GetSurfaceWaterTreatmentPlantWTP" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	public void doGetSurfaceLayingClearWaterMain(String value) {
		System.out.println("---------------<< CAlled >>---------------");
		String url = getBaseURL() + "GetSurfaceLayingOfClearWaterMain" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}
	// ========== Laying of Distribution System
	
	public void doGetSurfacePipelineFromNode(String value) {
		System.out.println("---------------<< CAlled >>---------------");
		String url = getBaseURL() + "GetSurfacePipelineFromNode" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}
	
	
	public void doGetSurfacePipelineToNode(String value) {
		System.out.println("---------------<< CAlled >>---------------");
		String url = getBaseURL() + "GetSurfacePipelineToNode" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}
	public void doGetSurfacePipeline(String value) {
		System.out.println("---------------<< CAlled >>---------------");
		String url = getBaseURL() + "GetSurfacePipeline" + value;
		int method = Method.GET;

		Log.i("url", url);
		makeJsonObjReq(method, url, new HashMap<String, String>());
	}

	// ==============================POST
	//   -------LAND ACQUISITION
	public void doPostSurfaceInsertLandAcquisition(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "SaveSurfaceLandAcquisition";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);
		makeJsonObjReq(method, url, requestMap);
	}

	public void doPostSurfaceUpdateLandAcquisition(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "UpdateSurfaceLandAcquisition";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);
		makeJsonObjReq(method, url, requestMap);
	}

	
	public void doPostSubSurfaceInsertLandAcquisition(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "SaveSubSurfaceLandAcquisition";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);
		makeJsonObjReq(method, url, requestMap);
	}
	
	public void doPostSubSurfaceUpdateLandAcquisition(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "UpdateSubSurfaceLandAcquisition";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);
		makeJsonObjReq(method, url, requestMap);
	}
	
	public void doPostInsertPowerConnection(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "SaveSurfacePowerConnection";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);
		makeJsonObjReq(method, url, requestMap);
	}

	public void doPostUpdatePowerConnection(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "UpdateSurfacePowerConnection";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);
		makeJsonObjReq(method, url, requestMap);
	}

	public void doPostSurfaceInsertIntakeRawWater(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "SaveSurfaceIntakeAndRawWaterMain";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);

		makeJsonObjReq(method, url, requestMap);

	}

	public void doPostSurfaceUpdateIntakeAndRawWater(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "UpdateSurfaceIntakeAndRawWaterMain";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);
		makeJsonObjReq(method, url, requestMap);
	}

	public void doPostSurfaceIntakeWaterMainImage(HashMap<String, String> paramsMap) {

		String url = getBaseURL() + "UploadSurfaceIntakeAndRawWaterMainImage";
		int method = Method.POST;

		Log.i("url", url);
		// System.out.println(paramsMap);

		makeJsonObjReq(method, url, paramsMap);
	}

	public void doPostSurfaceWaterTreatementInsert(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "SaveSurfaceWaterTreatmentPlantWTP";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);

		makeJsonObjReq(method, url, requestMap);

	}

	public void doPostSurfaceWaterTreatementUpdate(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "UpdateSurfaceWaterTreatmentPlantWTP";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);
		makeJsonObjReq(method, url, requestMap);
	}

	public void doPostSurfaceWaterTreatementImage(HashMap<String, String> paramsMap) {

		String url = getBaseURL() + "UploadSurfaceWaterTreatmentPlantWTPImage";
		int method = Method.POST;

		Log.i("url", url);
		// System.out.println(paramsMap);

		makeJsonObjReq(method, url, paramsMap);
	}

	// ========== Laying of Clear Water Main
	public void doPostSurfaceLayingClearWaterInsert(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "SaveSurfaceLayingOfClearWaterMain";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);

		makeJsonObjReq(method, url, requestMap);

	}

	public void doPostSurfaceLayingClearWaterUpdate(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "UpdateSurfaceLayingOfClearWaterMain";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);
		makeJsonObjReq(method, url, requestMap);
	}

	public void doPostSurfaceLayingClearWaterImage(HashMap<String, String> paramsMap) {

		String url = getBaseURL() + "UploadSurfaceLayingOfClearWaterMainImage";
		int method = Method.POST;

		Log.i("url", url);
		// System.out.println(paramsMap);
		JSONObject obj = new JSONObject(paramsMap);
		Util.writeToFile("Image", obj.toString());

		makeJsonObjReq(method, url, paramsMap);
	}

	// ========== Laying of Distribution System
	public void doPostSurfacePipelineInsert(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "SaveSurfacePipeline";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);

		makeJsonObjReq(method, url, requestMap);

	}

	public void doPostPipelineUpdate(HashMap<String, String> requestMap) {

		String url = getBaseURL() + "UpdateSurfacePipeline";
		int method = Method.POST;

		Log.i("url", url);
		System.out.println(requestMap);
		makeJsonObjReq(method, url, requestMap);
	}

}
