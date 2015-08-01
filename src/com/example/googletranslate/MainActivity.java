package com.example.googletranslate;

import java.util.ArrayList;
import java.util.Locale;

import android.speech.tts.TextToSpeech;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
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
	
	private EditText txtSearch;
	TextView outtxt;
	
	private int MY_DATA_CHECK_CODE = 0;
	private text2Speech mText2Speech;
	
	/* New function */
	private Button btnlanguage1, btnlanguage2;
	private String detectlanguage, transLanguage ="zh";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtSearch = (EditText) findViewById(R.id.InputText);
		outtxt = (TextView) findViewById(R.id.OutputText);
		
		speech = new speech2Text(this);
		mSpinner = new languageSpinner(this);
		mText2Speech = new text2Speech(this);
		translate = new googleTranslate(this, outtxt, mText2Speech);
		
		/* Method 1 */
		btnlanguage1 = (Button) findViewById(R.id.btnlanguage1);
		btnlanguage2 = (Button) findViewById(R.id.btnlanguage2);
		mSpinner.setBtn(btnlanguage1, btnlanguage2);
		btnlanguage1.setOnTouchListener(mOnTouchListener);
		btnlanguage2.setOnTouchListener(mOnTouchListener);
		
		
	} // end onCreate()
	
    /** Handle the results from the recognition activity.*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == speech.VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
        	
          // Fill the list view with the strings the recognizer thought it could have heard, there should be 5, based on the call
          ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

          //display results.
          //logthis("results: "+String.valueOf(matches.size())); 
          txtquery = matches.get(0);
         
          // Add Language Detection here , for two-way translate
          //sendHandlerMsg(mSpinner.getDetectlanguage(), mSpinner.getTranslanguage());
          sendHandlerMsg(detectlanguage, transLanguage);
         
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
	
	/* Button change color of backgound */
	private Button.OnTouchListener mOnTouchListener = new Button.OnTouchListener(){

		public boolean onTouch(View v, MotionEvent event) {
			
			int selectItor = v.getId();
			Log.i("shinhua", "touch id:"+ v.getId());
			
			switch (selectItor) {
			case R.id.btnlanguage1:
				detectlanguage = mSpinner.getSpinnerLanguage1();
				transLanguage = mSpinner.getSpinnerLanguage2();
				speech.setRecognition(mSpinner.chooseLocale(detectlanguage));
				translateButtonAction(event.getAction(), btnlanguage1);
				break;
			case R.id.btnlanguage2:
				detectlanguage = mSpinner.getSpinnerLanguage2();
				transLanguage = mSpinner.getSpinnerLanguage1();
				speech.setRecognition(mSpinner.chooseLocale(detectlanguage));
				translateButtonAction(event.getAction(), btnlanguage2);
				break;
			default:
				break;

			}
			
			return false;
		}
		
	};
	
	
	private void translateButtonAction(int mAction, Button mTransButton){
		
		if (mAction == MotionEvent.ACTION_DOWN) {
			mTransButton.setBackgroundColor(Color.BLUE);
			judgeLanguageIdentical();
		} else if (mAction == MotionEvent.ACTION_UP) {
			mTransButton.setBackgroundResource(android.R.drawable.btn_default);
		}
	}
	
	private void judgeLanguageIdentical(){
		
		if(mSpinner.getSpinnerLanguage1() != mSpinner.getSpinnerLanguage2()){
			speech.startVoiceRecognitionActivity();
			
		}else if(mSpinner.getSpinnerLanguage1() == mSpinner.getSpinnerLanguage2()){
			Toast.makeText(MainActivity.this, "Please reselect detect & translated language.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public String getTransLanguage() {
		return transLanguage;
	}

}
