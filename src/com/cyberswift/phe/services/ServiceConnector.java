package com.cyberswift.phe.services;

import org.json.JSONObject;

abstract class ServiceConnector {

	//	192.168.1.78
	//	123.63.224.21
	protected static String baseURL = "http://123.63.224.21/PHEApolloService/Service1.svc/"; // <-- Local Server
	//protected static String baseURL = "http://192.168.1.155/PHEApolloService/Service1.svc/"; // <-- Local Server
	//protected static String baseURL = "http://csusazurevmtest.cloudapp.net/PHESchoolService/Service1.svc/"; // <-- Client Server
	
	protected JSONObject outputJson;

	public static String getBaseURL() {
		return baseURL;
	}

	//	public void setBaseURL(String baseURL) {
	//		this.baseURL = baseURL;
	//	}

	public JSONObject getOutputJson() {
		return outputJson;
	}

	public void setOutputJson(JSONObject outputJson) {
		this.outputJson = outputJson;
	}

}
