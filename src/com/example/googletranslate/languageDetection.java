package com.example.googletranslate;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

public class languageDetection {


	protected static final String TAG = "languageDetection";
	
	static String result = "";
	public MainActivity mainActivity = new MainActivity();

	
	
	public String languageDetect(final String textToDetect) {
		
		 new Thread(new Runnable() {
			    public void run() {
		
		String yourKey = "Your key";
		String result = null;
		String URL  = "https://www.googleapis.com/language/translate/v2/detect";
		String key = "?key=" + yourKey;
		String textParam = "&q=" + textToDetect.replaceAll(" ", "%20");
		String fullURL = URL + key + textParam;
		System.out.println(fullURL);

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(fullURL);
		HttpResponse resp;

		
		try {
			resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());
			// Parse Result http
			proccesResult(respStr);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

			    }
		  }).start();
		
		return result;
	}
	
	
	
	String proccesResult(String jsonStringData) {

		try {
			JSONObject data = new JSONObject(jsonStringData);
			JSONObject jsoObj2 = data.getJSONObject("data");
			JSONArray jArray = jsoObj2.getJSONArray("detections");
			JSONArray detectArray = jArray.getJSONArray(0);
			JSONObject detectLanguage = detectArray.getJSONObject(0);
			String srcLanguage = detectLanguage.getString("language");

			// TRANSLATE TEXT
			//Log.i("william", "detect language -> " + srcLanguage);
			
			Message txtmsg = new Message();
			txtmsg = mainActivity.languageHandler.obtainMessage(0,srcLanguage);
			mainActivity.languageHandler.sendMessage(txtmsg);   
			

			return srcLanguage;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	
}
