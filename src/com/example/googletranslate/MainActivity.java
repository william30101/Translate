package com.example.googletranslate;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	static String result = "";
	final String tag = "Prueba";
	TextView outtxt;
	
	/* Speech parameter */
	private speech2Text speech;
	private googleTranslate translate;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final EditText txtSearch = (EditText) findViewById(R.id.InputText);
		outtxt = (TextView) findViewById(R.id.OutputText);

		speech = new speech2Text(this);
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
          String txtquery = matches.get(0);
          translate.callGoogleTranslate("en", "zh-TW", txtquery);
          
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
	/*
	 * simple method to add the log TextView.
	 */
	public void logthis (String newinfo) {
		if (newinfo != "") {
			outtxt.setText(outtxt.getText() + "\n" + newinfo);
		}
	}
    

}
