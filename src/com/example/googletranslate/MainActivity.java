package com.example.googletranslate;

import java.util.ArrayList;
import java.util.Locale;

import android.speech.tts.TextToSpeech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{

	private String TAG = "Translate";
	static String result = "";
	
	/* Speech parameter */
	private speech2Text speech;
	private static googleTranslate translate;
	private static String txtquery;
	private languageSpinner mSpinner;
	
	private Button btnSearch;
	private EditText txtSearch;
	TextView outtxt;
	
	private int MY_DATA_CHECK_CODE = 0;
	private text2Speech mText2Speech;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtSearch = (EditText) findViewById(R.id.InputText);
		outtxt = (TextView) findViewById(R.id.OutputText);
		btnSearch = (Button) findViewById(R.id.TranslateButton);
		
		speech = new speech2Text(this);
		mSpinner = new languageSpinner(this);
		mText2Speech = new text2Speech(this, mSpinner);
		translate = new googleTranslate(this, outtxt, mText2Speech);
		
		/* Method 1 */
		btnSearch.setOnClickListener(mOnClickListener);
		
	} // end onCreate()
	
    /** Handle the results from the recognition activity.*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == speech.VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
        	
          // Fill the list view with the strings the recognizer thought it could have heard, there should be 5, based on the call
          ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

          //display results.
          logthis("results: "+String.valueOf(matches.size())); 
          txtquery = matches.get(0);
         
          // Add Language Detection here , for two-way translate
          sendHandlerMsg(mSpinner.getDetectlanguage(), mSpinner.getTranslanguage());
         
        }
        
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {     
            	mText2Speech.initializeTTSEngine();
            }
            else {
            	mText2Speech.installTTSEngine();
            }
          }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**  Register hanlder for language detect. */
	public Handler languageHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String[] detectLanguage = ((String[]) msg.obj);
				
				String fromLanguage = detectLanguage[0];
				String toLanguage = detectLanguage[1];
				
				Log.i(TAG, "detect language -> " + fromLanguage + " toLanguage -> " +toLanguage);
				
				/*
				 * Translate from source language to destination language
				 * When we detect language source.
				 * */
				
				if (detectLanguage != null){
				    txtSearch.setText(txtquery);
				    mText2Speech.initializeTTSLanguage();
					translate.callGoogleTranslate(fromLanguage, toLanguage, txtquery);
				}
				
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	/** simple method to add the log TextView.*/
	public void logthis (String newinfo) {
		if (newinfo != "") {
			outtxt.setText(outtxt.getText() + "\n" + newinfo);
		}
	}
    
	private void sendHandlerMsg(String srcLanguage, String toLanguage){
		Message txtmsg = new Message();
		String[] temp = new String[10];
		temp[0] = srcLanguage;
		temp[1] = toLanguage;
		
		txtmsg = languageHandler.obtainMessage(0, temp);
		languageHandler.sendMessage(txtmsg);   
	}
	
	private Button.OnClickListener mOnClickListener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			
			if(mSpinner.getDetectlanguage() != mSpinner.getTranslanguage()){
				speech.startVoiceRecognitionActivity();
				
			}else if(mSpinner.getDetectlanguage() == mSpinner.getTranslanguage()){
				Toast.makeText(MainActivity.this, "Please reselect detect & translated language.", Toast.LENGTH_SHORT).show();
			}

		}
		
	};
	
}
