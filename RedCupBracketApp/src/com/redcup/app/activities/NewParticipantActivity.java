package com.redcup.app.activities;

import com.redcup.app.R;
import com.redcup.app.model.Participant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class NewParticipantActivity extends Activity {

	private static final String TAG = "NewParticipantActivity";
	public static final String NEW_PARTICIPANT_CREATED = "New Participant Created";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newparticipant);
		
	}
	
	public void newParticipant(View v) {
		EditText nameField = (EditText) findViewById(R.id.nameField);
		
		if (nameField.getText() == null || 
				nameField.getText().toString().equals("")) {
			Log.e(TAG, "Participant must have a name");
			return;
		}
		
		Participant p = new Participant(nameField.getText().toString());
		Intent result = new Intent();
		result.putExtra(NEW_PARTICIPANT_CREATED, p);
		setResult(RESULT_OK, result);
		finish();
	}
}
