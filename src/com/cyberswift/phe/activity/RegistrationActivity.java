package com.cyberswift.phe.activity;
/*package com.cyberswift.phe.imis;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cyberswift.phe.services.VolleyTaskManager;
import com.cyberswift.phe.utility.AlertDialogCallBack;
import com.cyberswift.phe.utility.ServerResponseCallback;
import com.cyberswift.phe.utility.Util;

public class RegistrationActivity extends Activity implements ServerResponseCallback {

	private TextView tv_header;
	private EditText et_name, et_userId, et_password, et_confirmPassword,
			et_phone, et_email;
	private Button btn_register, btn_cancel;
	private Typeface custom_font;
	
	private VolleyTaskManager volleyTaskManager;
	
	private boolean isRegistrationService = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_layout);

		initview();
	}

	private void initview() {
		tv_header = (TextView) findViewById(R.id.tv_header);

		et_name = (EditText) findViewById(R.id.et_name);
		et_userId = (EditText) findViewById(R.id.et_userId);
		et_password = (EditText) findViewById(R.id.et_password);
		et_confirmPassword = (EditText) findViewById(R.id.et_confirmPassword);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_email = (EditText) findViewById(R.id.et_email);

		btn_register = (Button) findViewById(R.id.btn_register);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		
		custom_font = Typeface.createFromAsset(getAssets(), "fonts/SCRIPTBL.TTF");
		
		tv_header.setTypeface(custom_font);
		volleyTaskManager = new VolleyTaskManager(RegistrationActivity.this);
	}

	
	public void onRegisterClicked(View mView) {
		if(et_name.getText().toString().length() == 0 ||
				et_userId.getText().toString().length() == 0 ||
				et_password.getText().toString().length() == 0 ||
				et_confirmPassword.getText().toString().length() == 0 ||
				et_phone.getText().toString().length() == 0 ||
				et_email.getText().toString().length() == 0) {
			Util.showMessageWithOk(RegistrationActivity.this, "Please fill all the required fields!");
		} else if (!et_password.getText().toString().equals(et_confirmPassword.getText().toString())){
			Util.showMessageWithOk(RegistrationActivity.this, "Password and Confirm Password aren't same!");
		} else if (et_password.getText().toString().length() < 4) {
			Util.showMessageWithOk(RegistrationActivity.this, "Password should be at least 4 characters long!");
		} else if (et_phone.getText().toString().length() < 10) {
			Util.showMessageWithOk(RegistrationActivity.this, "Please provide a valid mobile number!");
		} else if (!Util.isEmailValid(et_email.getText().toString())) {
			Util.showMessageWithOk(RegistrationActivity.this, "Please provide a valid email address!");
		} else {
			if (Util.checkConnectivity(RegistrationActivity.this)) {
				Util.hideSoftKeyboard(RegistrationActivity.this, mView);
				registrationWebserviceCalling();
			} else {
				Util.showMessageWithOk(RegistrationActivity.this, getString(R.string.no_internet));
			}
			
		}
	}

	private void registrationWebserviceCalling() {
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("FULL_NAME", et_name.getText().toString());
		paramsMap.put("USER_NAME", et_userId.getText().toString());
		paramsMap.put("PASSWORD", et_password.getText().toString());
		paramsMap.put("MOBILE", et_phone.getText().toString());
		paramsMap.put("EMAIL", et_email.getText().toString());
		paramsMap.put("ISACTIVE", "0");
		
		isRegistrationService = true;
		volleyTaskManager.doRegistration(paramsMap, true);
	}

	public void onCancelClicked(View mView) {
		finish();
	}

	@Override
	public void onSuccess(JSONObject resultJsonObject) {
		Log.v("resultJsonObject", resultJsonObject.toString());
		
		if(isRegistrationService){
			isRegistrationService = false;
			
			String id = resultJsonObject.optString("ID");
			String msg = resultJsonObject.optString("MESSAGE");
					
			if(id.equalsIgnoreCase("1")) {
				Util.showCallBackMessageWithOk(RegistrationActivity.this, et_userId.getText().toString() +
						" registered successfully.", new AlertDialogCallBack() {
					
					@Override
					public void onSubmit() {
						finish();
					}
					
					@Override
					public void onCancel() {
						// Do nothing
					}
				});
			} else if (id.equalsIgnoreCase("2")) {
				Util.showMessageWithOk(RegistrationActivity.this, "User Name already exists!\nPlease try with another user Id.");
			} else {
				Util.showMessageWithOk(RegistrationActivity.this, "Registration unsuccessful!\nPlease try again later.");
			}

//			if(resultJsonObject.equalsIgnoreCase("1")) {
//				UserClass userClass = new UserClass();
//				
//				userClass.setName(et_name.getText().toString());
//				userClass.setUserId(et_userId.getText().toString());
//				userClass.setPhone(et_phone.getText().toString());
//				userClass.setEmail(et_email.getText().toString());
//				userClass.setAdharVoterId(et_aadharVoterId.getText().toString());
//				
//				Util.saveUserClass(RegistrationActivity.this, userClass);
//				
//				Util.showCallBackMessageWithOk(RegistrationActivity.this, et_userId.getText().toString() +
//						" registered successfully.", new AlertDialogCallBack() {
//					
//					@Override
//					public void onSubmit() {
//						finish();
//					}
//					
//					@Override
//					public void onCancel() {
//						// Do nothing
//						
//					}
//				});
//			} else if (resultJsonObject.optString("FDUserRegistrationResult").equalsIgnoreCase("2")) {
//				Util.showMessageWithOk(RegistrationActivity.this, "User Id already exists!\nPlease try with another user Id.");
//			} else {
//				Util.showMessageWithOk(RegistrationActivity.this, "Registration unsuccessful!\nPlease try again later.");
//			}
		}
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}
	
}
*/