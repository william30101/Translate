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

public class MainActivity extends Activity implements TextToSpeech.OnInitListener{

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
	private TextToSpeech myTTS;
	private int MY_DATA_CHECK_CODE = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtSearch = (EditText) findViewById(R.id.InputText);
		outtxt = (TextView) findViewById(R.id.OutputText);
		btnSearch = (Button) findViewById(R.id.TranslateButton);
		
		speech = new speech2Text(this);
		translate = new googleTranslate(this, outtxt,this);
		mSpinner = new languageSpinner(this);

		
		/* Method 1 */
		btnSearch.setOnClickListener(mOnClickListener);
		
	    /*Add start*/
	    Intent checkTTSIntent = new Intent();
	    checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
	    startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
	    /*Add end*/

		
		
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
          sendHandlerMsg(mSpinner.detectlanguage, mSpinner.translanguage);
         
        }
        
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {      
                myTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
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
				    settingInitLanguage();
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
			
			if(mSpinner.detectlanguage != mSpinner.translanguage){
				speech.startVoiceRecognitionActivity();
			}else if(mSpinner.detectlanguage == mSpinner.translanguage){
				Toast.makeText(MainActivity.this, "Please reselect detect & translated language.", Toast.LENGTH_SHORT).show();
			}

		}
		
	};
	
	
	@Override
    public void onInit(int status) {
 
		Locale language = Locale.ENGLISH;
        if (status == TextToSpeech.SUCCESS) {
        	if (mSpinner.translanguage.toLowerCase().contains("zh"))
        		language = Locale.CHINESE;
        	else if (mSpinner.translanguage.toLowerCase().contains("en"))
        		language = Locale.ENGLISH;
        	else if (mSpinner.translanguage.toLowerCase().contains("jpn"))
        		language = Locale.JAPAN;
        	else if (mSpinner.translanguage.toLowerCase().contains("kor"))
        		language = Locale.KOREA;
        	else if (mSpinner.translanguage.toLowerCase().contains("fra"))
        		language = Locale.FRANCE;
        	
            int result =  myTTS.setLanguage(language);
 
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
               // speakOut();
            }
 
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
 
    }
	
	public void settingInitLanguage()
	{
		Locale language = Locale.ENGLISH;
		if (mSpinner.translanguage.toLowerCase().contains("zh"))
    		language = Locale.CHINESE;
    	else if (mSpinner.translanguage.toLowerCase().contains("en"))
    		language = Locale.ENGLISH;
    	else if (mSpinner.translanguage.toLowerCase().contains("ja"))
    		language = Locale.JAPAN;
    	else if (mSpinner.translanguage.toLowerCase().contains("ko"))
    		language = Locale.KOREA;
    	else if (mSpinner.translanguage.toLowerCase().contains("fr"))
    		language = Locale.FRANCE;
		
        int result =  myTTS.setLanguage(language);

        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "This Language is not supported");
        } else {
           // speakOut();
        }
	}
 
    public void speakOut(String text) {
 
      //  String text = txtText.getText().toString();
 
    	myTTS.speak(text, TextToSpeech.QUEUE_FLUSH,null);
    }
	
}
