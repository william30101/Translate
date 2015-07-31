package com.example.googletranslate;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;


public class text2Speech implements TextToSpeech.OnInitListener{
	
	private String TAG = "TTS";
	
	private MainActivity mMainActivity;
	private Context mContext;
	
	private languageSpinner mSpinner;
	
	private TextToSpeech myTTS;
	private Locale mlanguage = Locale.ENGLISH;
	private int MY_DATA_CHECK_CODE = 0;
	
	public text2Speech(MainActivity activity,languageSpinner spinner){
		mMainActivity = activity;
		mContext = mMainActivity.getApplicationContext();
		mSpinner = spinner;
		
		checkTTSEngine();
	}
	
	private void checkTTSEngine(){
		
	    Intent checkTTSIntent = new Intent();
	    checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
	    mMainActivity.startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
	}
	
	public void installTTSEngine(){
		
		Intent installTTSIntent = new Intent();
		installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
		mMainActivity.startActivity(installTTSIntent);
	}
	
	
	public void initializeTTSEngine(){
		myTTS = new TextToSpeech(mContext, this);
	}
	
    public void speakOut(String text) {
    	Log.i(TAG, "speakOut");
    	myTTS.speak(text, TextToSpeech.QUEUE_FLUSH,null);
    }
	

	@Override
	public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
        	Log.i(TAG, "initializeTTSLanguage");
        	initializeTTSLanguage();
 
        } else {
            Log.e(TAG, "Initilization Failed!");
        }
	}

	
	public void initializeTTSLanguage(){
		
		if (mSpinner.getTranslanguage().toLowerCase().contains("zh"))
			mlanguage = Locale.CHINESE;
    	else if (mSpinner.getTranslanguage().toLowerCase().contains("en"))
    		mlanguage = Locale.ENGLISH;
    	else if (mSpinner.getTranslanguage().toLowerCase().contains("ja"))
    		mlanguage = Locale.JAPAN;
    	else if (mSpinner.getTranslanguage().toLowerCase().contains("ko"))
    		mlanguage = Locale.KOREA;
    	else if (mSpinner.getTranslanguage().toLowerCase().contains("fr"))
    		mlanguage = Locale.FRANCE;
    	else if (mSpinner.getTranslanguage().toLowerCase().contains("th"))
    		mlanguage = new Locale("th_TH");
		
        int result =  myTTS.setLanguage(mlanguage);

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "This Language is not supported");
        } else {
           // speakOut();
        }
	}
	
	

	
	
}
