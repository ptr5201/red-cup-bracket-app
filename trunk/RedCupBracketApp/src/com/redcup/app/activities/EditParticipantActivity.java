package com.redcup.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.redcup.app.R;
import com.redcup.app.data.RedCupDB;

public class EditParticipantActivity extends Activity{
	
	private static final String TAG = "EditParticipantActivity";
	private RedCupDB db;
	private EditText nameField;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editparticipantmenu);
		nameField = (EditText) findViewById(R.id.editNameEditText);
		nameField.setText(this.getIntent().getStringExtra("name"));
		db = new RedCupDB(this);
	}
	
	public void editParticipant(View v){
		//EditText nameField = (EditText) findViewById(R.id.editNameEditText);
		
		if (nameField.getText() == null || 
				nameField.getText().toString().equals("")) {
			Log.e(TAG, "Participant must have a name");
			return;
		}
		else{
			String oldName = this.getIntent().getStringExtra("name");
			String newName = nameField.getText().toString();
			// TODO: change to a result activity
			
			db.open();
			db.editParticipant(newName, oldName);
			db.close();
			
			Log.v(TAG, getString(R.string.participants));
			Intent participantManager = new Intent(this, ParticipantManagerActivity.class);
			startActivity(participantManager);
		}
		
		
	}

}
