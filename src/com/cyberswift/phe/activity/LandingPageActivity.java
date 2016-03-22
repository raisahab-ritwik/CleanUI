package com.cyberswift.phe.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.cyberswift.phe.R;
import com.cyberswift.phe.dropdown.DropDownViewForXML;
import com.cyberswift.phe.dto.Nodes;
import com.cyberswift.phe.services.VolleyTaskManager;
import com.cyberswift.phe.staticconstants.Consts;
import com.cyberswift.phe.sub_surface.SubSurfaceLandAcquisitionActivity;
import com.cyberswift.phe.sub_surface.SubSurfaceLayingDistributionPipeline;
import com.cyberswift.phe.sub_surface.SubSurfacePowerConnectionActivity;
import com.cyberswift.phe.surface.SurfaceIntakeRawWaterActivity;
import com.cyberswift.phe.surface.SurfaceLandAcquisitionActivity;
import com.cyberswift.phe.surface.SurfaceLayingClearWaterActivity;
import com.cyberswift.phe.surface.SurfaceLayingDistributionPipeline;
import com.cyberswift.phe.surface.SurfacePowerConnectionActivity;
import com.cyberswift.phe.surface.SurfaceWaterTreatementActivity;
import com.cyberswift.phe.utility.ServerResponseCallback;

public class LandingPageActivity extends Activity implements ServerResponseCallback {

	private Context mContext;
	private DropDownViewForXML dropDown_district, dropDown_schemeType, dropDown_schemeName, dropDown_formName;
	private VolleyTaskManager volleyTaskManager;
	private ArrayList<Nodes> formNameList;
	private ArrayList<Nodes> schemeNameList;
	private boolean isGetSchemeNameService = false;
	private boolean isGetFormNameService = false;
	private boolean isDistrictSelected = false;
	private boolean isSchemeTypeSelected = false;
	private int districtId = 0;
	private int schemeTypeId = 0;
	private String schemeID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.landing_layout);
		mContext = LandingPageActivity.this;
		initView();

	}

	/** InitView **/
	private void initView() {

		dropDown_district = (DropDownViewForXML) findViewById(R.id.dropDown_district);
		dropDown_schemeType = (DropDownViewForXML) findViewById(R.id.dropDown_schemeType);
		dropDown_schemeName = (DropDownViewForXML) findViewById(R.id.dropDown_schemeName);
		dropDown_formName = (DropDownViewForXML) findViewById(R.id.dropDown_formName);

		dropDown_district.setItems(getResources().getStringArray(R.array.district_array));
		dropDown_schemeType.setItems(getResources().getStringArray(R.array.scheme_type_array));

		volleyTaskManager = new VolleyTaskManager(mContext);

		dropDown_district.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				isDistrictSelected = true;
				districtId = position + 1;

				if (isSchemeTypeSelected)

					getSchemeName(districtId, schemeTypeId);

			}
		});

		dropDown_schemeType.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				isSchemeTypeSelected = true;
				schemeTypeId = position + 1;
				getFormName(position + 1);

			}
		});
		dropDown_schemeName.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				schemeID = schemeNameList.get(position).getNodeId();
			}
		});

	}

	public void onPostClicked(View mView) {

		navigateToForm(schemeTypeId, dropDown_formName.getText().toString().trim());
	}

	private void getSchemeName(int districtId, int schemeTypeId) {

		isGetSchemeNameService = true;

		String value = "?district=" + districtId + "&scheme_type=" + schemeTypeId;

		volleyTaskManager.doGetSchemeName(value);

	}

	private void getFormName(int position) {

		isGetFormNameService = true;

		String value = "?scheme_type=" + position;

		volleyTaskManager.doGetFormListBySchemeType(value);
	}

	@Override
	public void onSuccess(JSONObject resultJsonObject) {

		Log.v("OnSuccess", "" + resultJsonObject.toString());

		volleyTaskManager.hideProgressDialog();

		if (isGetFormNameService) {

			isGetFormNameService = false;
			formNameList = new ArrayList<Nodes>();
			JSONArray fDGetFormListResultJsonArray = resultJsonObject.optJSONArray("GetFormListBySchemeTypeResult");

			for (int i = 0; i < fDGetFormListResultJsonArray.length(); i++) {
				JSONObject districtJsonObject = fDGetFormListResultJsonArray.optJSONObject(i);

				Nodes formName = new Nodes();
				formName.setNodeId(districtJsonObject.optString("Id"));
				formName.setNodeName(districtJsonObject.optString("Name"));

				formNameList.add(formName);
				populateFormNameDropDown(formNameList);
				if (isDistrictSelected)
					getSchemeName(districtId, schemeTypeId);
			}

		}

		else if (isGetSchemeNameService) {

			Log.v("Populate Scheme NameList", "------------------------------------------");
			isGetSchemeNameService = false;

			schemeNameList = new ArrayList<Nodes>();
			JSONArray fDGetNameListResultJsonArray = resultJsonObject.optJSONArray("GetSchemeNameByDistrictResult");
			if (fDGetNameListResultJsonArray.length() > 0) {
				for (int i = 0; i < fDGetNameListResultJsonArray.length(); i++) {
					JSONObject districtJsonObject = fDGetNameListResultJsonArray.optJSONObject(i);

					Nodes formName = new Nodes();
					formName.setNodeId(districtJsonObject.optString("Id"));
					formName.setNodeName(districtJsonObject.optString("Name"));

					schemeNameList.add(formName);
					populateSchemeNameDropDown(schemeNameList);
				}
			} else {
				schemeNameList = new ArrayList<Nodes>();
				populateSchemeNameDropDown(schemeNameList);
			}

		}

	}

	@Override
	public void onError() {

	}

	private void populateFormNameDropDown(ArrayList<Nodes> formNameList) {

		dropDown_formName.setText("");
		dropDown_formName.setHint("");

		if (formNameList.size() > 0) {

			dropDown_formName.setEnabled(true);
			dropDown_formName.setHint(" select ");
			String[] array = new String[formNameList.size()];
			int index = 0;
			for (Nodes value : formNameList) {
				array[index] = (String) value.getNodeName();
				index++;
			}
			dropDown_formName.setItems(array);

		} else {
			dropDown_formName.setEnabled(false);
		}
	}

	private void populateSchemeNameDropDown(ArrayList<Nodes> schemeNameList) {

		dropDown_schemeName.setText("");
		dropDown_schemeName.setHint("");
		schemeID = "";
		if (schemeNameList.size() > 0) {

			dropDown_schemeName.setEnabled(true);
			dropDown_schemeName.setHint(" select ");
			String[] array = new String[schemeNameList.size()];
			int index = 0;
			for (Nodes value : schemeNameList) {
				array[index] = (String) value.getNodeName();
				index++;
			}
			dropDown_schemeName.setItems(array);
		} else {
			dropDown_schemeName.setEnabled(false);
		}
	}

	public void onLogoutClicked(View v) {

	}

	public void onOfflineDataSyncClicked(View v) {

		Toast.makeText(mContext, "Offline sync!!", Toast.LENGTH_SHORT).show();

	}

	private void navigateToForm(int schemeType, String formName) {

		Log.v("Navigation", "Scheme type: " + schemeType + " Form  Name: " + formName + " Scheme Id: " + schemeID + " District ID: " + districtId);

		if (schemeType == 1) {

			if (formName.trim().equalsIgnoreCase(Consts.LAND_ACQUISITION))
				startActivity(new Intent(mContext, SurfaceLandAcquisitionActivity.class).putExtra("districtId", districtId).putExtra("schemeId", schemeID));

			else if (formName.trim().equalsIgnoreCase(Consts.POWER_CONNECTION))
				startActivity(new Intent(mContext, SurfacePowerConnectionActivity.class).putExtra("districtId", districtId).putExtra("schemeId", schemeID));

			else if (formName.trim().equalsIgnoreCase(Consts.INTAKE_RAW_WATER))
				startActivity(new Intent(mContext, SurfaceIntakeRawWaterActivity.class).putExtra("districtId", districtId).putExtra("schemeId", schemeID));

			else if (formName.trim().equalsIgnoreCase(Consts.WATER_TREATMENT))
				startActivity(new Intent(mContext, SurfaceWaterTreatementActivity.class).putExtra("districtId", districtId).putExtra("schemeId", schemeID));

			else if (formName.trim().equalsIgnoreCase(Consts.LAYING_CLEAR_WATER))
				startActivity(new Intent(mContext, SurfaceLayingClearWaterActivity.class).putExtra("districtId", districtId).putExtra("schemeId", schemeID));

			else if (formName.trim().equalsIgnoreCase(Consts.DISTRIBUTION_SYSTEM_PIPELINE))
				startActivity(new Intent(mContext, SurfaceLayingDistributionPipeline.class).putExtra("districtId", districtId).putExtra("schemeId", schemeID)
						.putExtra("schemeTypeId", schemeTypeId));

		}
		/*else if (schemeType == 2) {
			if (formName.trim().equalsIgnoreCase(Consts.LAND_ACQUISITION))
				startActivity(new Intent(mContext, SubSurfaceLandAcquisitionActivity.class).putExtra("districtId", districtId).putExtra("schemeId", schemeID));
			else if (formName.trim().equalsIgnoreCase(Consts.POWER_CONNECTION))
				startActivity(new Intent(mContext, SubSurfacePowerConnectionActivity.class).putExtra("districtId", districtId).putExtra("schemeId", schemeID));
			else if (formName.trim().equalsIgnoreCase(Consts.DISTRIBUTION_SYSTEM_PIPELINE))
				startActivity(new Intent(mContext, SubSurfaceLayingDistributionPipeline.class).putExtra("districtId", districtId)
						.putExtra("schemeId", schemeID).putExtra("schemeTypeId", schemeTypeId));
		} else if (schemeType == 3) {

		}*/
	}
}
