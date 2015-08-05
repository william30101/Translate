package com.dayoo.googletranslate;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

public class speech2Text implements RecognitionListener{

	private static speech2Text mClass;
	private static Activity mActivity;
	
	protected static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	protected static final String TAG = "VoiceRecognition";
	private String detectLanguage;
	
	private Context mContext;
	private SpeechRecognizer mSpeechRecognizer;
	
	public speech2Text(Activity activity){
		mActivity = activity;
		mClass = this;
		
		mContext = mActivity.getApplicationContext();
		mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
		mSpeechRecognizer.setRecognitionListener(mClass);
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
     
        // Given an hint to the recognizer about what the user is going to say
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        
        // Octocat 0713 add: Set detect language
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, detectLanguage);
        Log.i(TAG, "trans -->" + detectLanguage);
        
        // Display an hint to the user about what he should say.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something!");
        
        // Specify how many results you want to receive. The results will be sorted
        // where the first result is the one with higher confidence.
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        
        Log.i(TAG,"Calling the Voice Intenet");
        /* Method 1 */
        mActivity.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        
        /* Method 2 */
        //mSpeechRecognizer.startListening(intent);
    }
	
	
	/** RecognitionListener */
	protected void setRecognition(String language) {
		detectLanguage = language;
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBeginningOfSpeech() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRmsChanged(float rmsdB) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEndOfSpeech() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(int error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResults(Bundle results) {
	    ArrayList<String> recData = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
	 
	    String getData = new String();
	    for (String s : recData) {
	        getData += s + ",";
	    }
	    
	    Toast.makeText(mContext, getData, Toast.LENGTH_SHORT).show();		
	}

	@Override
	public void onPartialResults(Bundle partialResults) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEvent(int eventType, Bundle params) {
		// TODO Auto-generated method stub
		
	}
	
	
}
