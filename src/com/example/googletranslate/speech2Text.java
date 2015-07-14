package com.example.googletranslate;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;

public class speech2Text {

	private static speech2Text mClass;
	private static Activity mActivity;
	
	protected static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	protected static final String TAG = "VoiceRecognition";
	
	public speech2Text(Activity activity){
		mActivity = activity;
		mClass = this;
	}
	
	public static speech2Text getInstance(){
		return mClass;
	}
	
    /**
     * Fire an intent to start the speech recognition activity.
     */
	protected void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify your application
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());

        // Display an hint to the user about what he should say.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something!");

        // Given an hint to the recognizer about what the user is going to say
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Specify how many results you want to receive. The results will be sorted
        // where the first result is the one with higher confidence.
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        Log.i(TAG,"Calling the Voice Intenet");
        mActivity.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }
	
	
}
