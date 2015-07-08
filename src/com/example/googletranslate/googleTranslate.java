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

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class googleTranslate {

	static Activity mActivity;
	static TextView mTextView;
	static String result = "";
	private String voiceLanguage;
	
	public googleTranslate(Activity activity, TextView textView){
		mActivity = activity;
		mTextView = textView;
	}
	
	
	public String callGoogleTranslate(final String fromLanguage,
			final String toLanguage, final String textToTranslate) {
		
		voiceLanguage = toLanguage;
		
		 new Thread(new Runnable() {
			    public void run() {
		
		String yourKey = "Your key";
		String result = null;
		String URL  = "https://www.googleapis.com/language/translate/v2";
		String key = "?key=" + yourKey;
		String sourceParam = "&source=" + fromLanguage;
		String toParam = "&target=" + toLanguage;
		String textParam = "&q=" + textToTranslate.replaceAll(" ", "%20");
		String fullURL = URL + key + sourceParam + toParam + textParam;
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
	
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mTextView.setText((String) msg.obj);
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	String proccesResult(String jsonStringData) {

		try {
			JSONObject data = new JSONObject(jsonStringData);
			JSONObject jsoObj2 = data.getJSONObject("data");
			JSONArray jArray = jsoObj2.getJSONArray("translations");
			JSONObject steps = jArray.getJSONObject(0);
			String txtTraducido = steps.getString("translatedText");

			// TRANSLATE TEXT
			//Log.i("Prueba", "Resultado-> " + txtTraducido);
			Message txtmsg = new Message();
			txtmsg = myHandler.obtainMessage(0,txtTraducido);
			myHandler.sendMessage(txtmsg);   
			playGoogleVoice(txtTraducido);

			return txtTraducido;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	
	private void playGoogleVoice(String mString){
		
		try {
		    MediaPlayer player = new MediaPlayer();
		    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		    
		    player.setDataSource("http://translate.google.com/translate_tts?ie=utf-8&tl=" + voiceLanguage + "&q=" + mString.replaceAll(" ", "%20"));
		    player.prepare();
		    player.start();

		} catch (Exception e) {
		    // TODO: handle exception
		}
		
	}
	
	
	
	
	
	
}
