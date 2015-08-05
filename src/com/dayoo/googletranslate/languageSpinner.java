package com.dayoo.googletranslate;

import java.util.Locale;

import com.dayoo.googletranslate.R;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class languageSpinner{

	private static Activity mActivity;
	private Context mContext;
	
	private Spinner detectInput = null;
	private Spinner transOutput = null;

	private String spinnerLanguage1;
	private String spinnerLanguage2;
	
	private String[] languageItem = new String[] { "ENG", "zh-TW", "JPN", "KOR", "FRA", "Thai"};
	private ArrayAdapter<String> listAdapter;
	
	private Button btn1, btn2;

	protected languageSpinner(Activity activity){
		mActivity = activity;
		mContext = mActivity.getApplicationContext();
		
		declareSpinner();
	}
	
	private void declareSpinner(){
		
		listAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, languageItem);
		listAdapter.setDropDownViewResource(R.layout.spinner_item);
		
		/* Detect input Language */
		detectInput = (Spinner) mActivity.findViewById(R.id.spinnerlanguage1);
		detectInput.setAdapter(listAdapter);
		//transOutput.setSelection(0); //Default value at 0 position
		detectInput.setOnItemSelectedListener(selectListener);
		
		
		/* Translate output Language */
		transOutput = (Spinner) mActivity.findViewById(R.id.spinnerlanguage2);
		transOutput.setAdapter(listAdapter);
		transOutput.setSelection(1);
		transOutput.setOnItemSelectedListener(selectListener);
	}
	
	private OnItemSelectedListener selectListener = new OnItemSelectedListener(){
		
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			
			if(parent.getId() == R.id.spinnerlanguage1){
				selectSpinnerRecognitionLanguage(parent.getSelectedItemPosition());
				//setRecognitionLanguage(parent.getSelectedItemPosition(), parent.getId(), 0);
			}
			else if(parent.getId() == R.id.spinnerlanguage2){
				selectSpinnerTranslateLanguage(parent.getSelectedItemPosition());
				//setRecognitionLanguage(parent.getSelectedItemPosition(), parent.getId(), 1);
				
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
		
	};
	
	
	private void setRecognitionLanguage(int itor, int handleMsgNum, String mSpinnerLanguage){
		
		switch(itor){
		case 0:
			mSpinnerLanguage = "en";
			setButtonhandleMessage(handleMsgNum, mSpinnerLanguage);
			break;
		case 1:
			mSpinnerLanguage = "zh-CN";
			setButtonhandleMessage(handleMsgNum, mSpinnerLanguage);
			break;
		case 2:
			mSpinnerLanguage = "ja";
			setButtonhandleMessage(handleMsgNum, mSpinnerLanguage);
			break;
		case 3:
			mSpinnerLanguage = "ko";
			setButtonhandleMessage(handleMsgNum, mSpinnerLanguage);
			break;
		case 4:
			mSpinnerLanguage = "fr";
			setButtonhandleMessage(handleMsgNum, mSpinnerLanguage);
			break;
		case 5:
			mSpinnerLanguage = "th";
			setButtonhandleMessage(handleMsgNum, mSpinnerLanguage);
			break;
		default:
			break;
		}
	}
	
	
	
	private void selectSpinnerRecognitionLanguage(int itor){
		
		switch(itor){
		case 0:
			spinnerLanguage1 = "en";
			setButtonhandleMessage(0, languageItem[0]);
			break;
		case 1:
			spinnerLanguage1 = "zh-CN";
			setButtonhandleMessage(0, languageItem[1]);
			break;
		case 2:
			spinnerLanguage1 = "ja";
			setButtonhandleMessage(0, languageItem[2]);
			break;
		case 3:
			spinnerLanguage1 = "ko";
			setButtonhandleMessage(0, languageItem[3]);
			break;
		case 4:
			spinnerLanguage1 = "fr";
			setButtonhandleMessage(0, languageItem[4]);
			break;
		case 5:
			spinnerLanguage1 = "th";
			setButtonhandleMessage(0, languageItem[5]);
			break;
		default:
			break;
		}
	}
	
	private void selectSpinnerTranslateLanguage(int itor){
		
		switch(itor){
		case 0:
			spinnerLanguage2 = "en";
			setButtonhandleMessage(1, languageItem[0]);
			break;
		case 1:
			spinnerLanguage2 = "zh-CN";
			setButtonhandleMessage(1, languageItem[1]);
			break;
		case 2:
			spinnerLanguage2 = "ja";
			setButtonhandleMessage(1, languageItem[2]);
			break;
		case 3:
			spinnerLanguage2 = "ko";
			setButtonhandleMessage(1, languageItem[3]);
			break;
		case 4:
			spinnerLanguage2 = "fr";
			setButtonhandleMessage(1, languageItem[4]);
			break;
		case 5:
			spinnerLanguage2 = "th";
			setButtonhandleMessage(1, languageItem[5]);
			break;
		default:
			break;
		}
	}
	
	public String chooseLocale(String mLocale){
		if (mLocale == "en") {
			return Locale.US.toString();
		} else if (mLocale == "zh-CN") {
			return Locale.TAIWAN.toString();
		} else if (mLocale == "ja") {
			return Locale.JAPAN.toString();
		} else if (mLocale == "ko") {
			return Locale.KOREA.toString();
		} else if (mLocale == "fr") {
			return Locale.FRENCH.toString();
		} else if (mLocale == "th") {
			return "th";
		} else {
			return Locale.US.toString();
		}
	}
	
	public String getSpinnerLanguage1() {
		return spinnerLanguage1;
	}
	
	public String getSpinnerLanguage2() {
		return spinnerLanguage2;
	}
	
	public void setBtn(Button btn1, Button btn2) {
		this.btn1 = btn1;
		this.btn2 = btn2;
	}
	
	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				btn1.setText((String) msg.obj);
				break;
			case 1:
				btn2.setText((String) msg.obj);
				break;
				
			}
			super.handleMessage(msg);
		}
	};
	
	private void setButtonhandleMessage(int itor, String language){
		Message txtmsg = new Message();
		txtmsg = myHandler.obtainMessage(itor,language);
		myHandler.sendMessage(txtmsg);   
	}
	
	
}

