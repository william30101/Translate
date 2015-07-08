package com.example.googletranslate;

import java.util.ArrayList;

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

public class MainActivity extends Activity {

	private String TAG = "Translate";
	static String result = "";
	final String tag = "Prueba";
	TextView outtxt;
	
	/* Speech parameter */
	private speech2Text speech;
	private static googleTranslate translate;
	private languageDetection detection;
	private static String txtquery;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final EditText txtSearch = (EditText) findViewById(R.id.InputText);
		outtxt = (TextView) findViewById(R.id.OutputText);

		speech = new speech2Text(this);
		detection = new languageDetection();
		translate = new googleTranslate(this, outtxt);
		
		final Button btnSearch = (Button) findViewById(R.id.TranslateButton);
		btnSearch.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				speech.startVoiceRecognitionActivity();

			}
		});

	} // end onCreate()
	
    /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == speech.VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
        	
          // Fill the list view with the strings the recognizer thought it could have heard, there should be 5, based on the call
          ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

          //display results.
          logthis("results: "+String.valueOf(matches.size())); 
          txtquery = matches.get(0);
          // Add Language Detection here , for two-way translate
          detection.languageDetect(txtquery);
         
          //translate.callGoogleTranslate("en", "zh-TW", txtquery);
          
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     *  Register hanlder for language detect.
     * 
     * */
	public Handler languageHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String detectLanguage = ((String) msg.obj);
				Log.i(TAG, "detect language -> " + detectLanguage);
				
				/*
				 * Translate from source language to destination language
				 * When we detect language source.
				 * */
				
				if (detectLanguage != null)
				{
					if (detectLanguage.equals("en"))
						translate.callGoogleTranslate("en", "zh-CN", txtquery);
					else if (detectLanguage.equals("zh-CN"))
						translate.callGoogleTranslate("zh-CN", "en", txtquery);
					else if (detectLanguage.equals("ja"))
						translate.callGoogleTranslate("ja", "en", txtquery);
				}
				break;
			}
			super.handleMessage(msg);
		}
	};
	
    
	/*
	 * simple method to add the log TextView.
	 */
	public void logthis (String newinfo) {
		if (newinfo != "") {
			outtxt.setText(outtxt.getText() + "\n" + newinfo);
		}
	}
    

}
