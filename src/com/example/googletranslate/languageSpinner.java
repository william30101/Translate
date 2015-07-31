package com.example.googletranslate;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class languageSpinner{

	private static Activity mActivity;
	private Context mContext;
	
	private Spinner detectInput = null;
	private Spinner transOutput = null;

	private String detectlanguage;
	private String translanguage;
	private String[] languageItem = new String[] { "ENG", "zh-TW", "JPN", "KOR", "FRA", "Thai"};
	private ArrayAdapter<String> listAdapter;
	
	private speech2Text speech;
	
	protected languageSpinner(Activity activity){
		mActivity = activity;
		mContext = mActivity.getApplicationContext();
		speech = speech2Text.getInstance();
		
		declareSpinner();
	}
	
	private void declareSpinner(){
		
		listAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, languageItem);
		listAdapter.setDropDownViewResource(R.layout.spinner_item);
		
		/* Detect input Language */
		detectInput = (Spinner) mActivity.findViewById(R.id.spinnerDetect);
		detectInput.setAdapter(listAdapter);
		//transOutput.setSelection(0); //Default value at 0 position
		detectInput.setOnItemSelectedListener(selectListener);
		
		
		/* Translate output Language */
		transOutput = (Spinner) mActivity.findViewById(R.id.spinnerTran);
		transOutput.setAdapter(listAdapter);
		transOutput.setSelection(1);
		transOutput.setOnItemSelectedListener(selectListener);
	}
	
	private OnItemSelectedListener selectListener = new OnItemSelectedListener(){
		
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			
			if(parent.getId() == R.id.spinnerDetect){
				selectSpinnerRecognitionLanguage(parent.getSelectedItemPosition());
			}
			else if(parent.getId() == R.id.spinnerTran){
				selectSpinnerTranslateLanguage(parent.getSelectedItemPosition());
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
		
	};
	
	
	private void selectSpinnerRecognitionLanguage(int itor){
		
		switch(itor){
		case 0:
			Toast.makeText(mContext, "Detect language: ENG", Toast.LENGTH_SHORT).show();
			speech.setRecognition(Locale.US.toString());
			detectlanguage = "en";
			break;
		case 1:
			Toast.makeText(mContext, "Detect language: zh-TW", Toast.LENGTH_SHORT).show();
			speech.setRecognition(Locale.TAIWAN.toString());
			detectlanguage = "zh-CN";
			break;
		case 2:
			Toast.makeText(mContext, "Detect language: JPN", Toast.LENGTH_SHORT).show();
			speech.setRecognition(Locale.JAPAN.toString());
			detectlanguage = "ja";
			break;
		case 3:
			Toast.makeText(mContext, "Detect language: KOR", Toast.LENGTH_SHORT).show();
			speech.setRecognition(Locale.KOREA.toString());
			detectlanguage = "ko";
			break;
		case 4:
			Toast.makeText(mContext, "Detect language: FRA", Toast.LENGTH_SHORT).show();
			speech.setRecognition(Locale.FRENCH.toString());
			detectlanguage = "fr";
			break;
		case 5:
			Toast.makeText(mContext, "Detect language: Thai", Toast.LENGTH_SHORT).show();
			speech.setRecognition("th");
			detectlanguage = "th";
			break;
		default:
			break;
		}
	}
	
	private void selectSpinnerTranslateLanguage(int itor){
		
		switch(itor){
		case 0:
			Toast.makeText(mContext, "Translate language: ENG", Toast.LENGTH_SHORT).show();
			translanguage = "en";
			break;
		case 1:
			Toast.makeText(mContext, "Translate language: zh-TW", Toast.LENGTH_SHORT).show();
			translanguage = "zh-CN";
			break;
		case 2:
			Toast.makeText(mContext, "Translate language: JPN", Toast.LENGTH_SHORT).show();
			translanguage = "ja";
			break;
		case 3:
			Toast.makeText(mContext, "Translate language: KOR", Toast.LENGTH_SHORT).show();
			translanguage = "ko";
			break;
		case 4:
			Toast.makeText(mContext, "Translate language: FRA", Toast.LENGTH_SHORT).show();
			translanguage = "fr";
			break;
		case 5:
			Toast.makeText(mContext, "Translate language: Thai", Toast.LENGTH_SHORT).show();
			translanguage = "th";
			break;
		default:
			break;
		}
	}
	
	public String getDetectlanguage() {
		return detectlanguage;
	}
	
	public String getTranslanguage() {
		return translanguage;
	}
	
}
