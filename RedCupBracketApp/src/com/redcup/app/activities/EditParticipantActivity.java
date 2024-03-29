package com.redcup.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.redcup.app.R;
import com.redcup.app.data.RedCupDB;

public class EditParticipantActivity extends Activity {
	
	private static final String TAG = "EditParticipantActivity";
	private RedCupDB db;
	private EditText nameField;
	private Button saveButton;
	private int pos;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editparticipantmenu);
		saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setEnabled(false);
		
		pos = this.getIntent().getIntExtra(ParticipantManagerActivity.EXTRA_PARTICIPANT_ID, 0);
		
		nameField = (EditText) findViewById(R.id.editNameEditText);
		nameField.setText(this.getIntent().getStringExtra(ParticipantManagerActivity.EXTRA_PARTICIPANT_OLD_NAME));
		nameField.setSelection(this.getIntent().getStringExtra(ParticipantManagerActivity.EXTRA_PARTICIPANT_OLD_NAME).length());
		nameField.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (nameField.getText().toString().equals(getIntent().getStringExtra(ParticipantManagerActivity.EXTRA_PARTICIPANT_ID)) 
						|| nameField.getText().toString().equals("")) {
					saveButton.setEnabled(false);
				} else {
					saveButton.setEnabled(true);
				}
			}
		});
		db = new RedCupDB(this);
	}
	
	public void editParticipant(View v){
	
		if (nameField.getText() == null || 
				nameField.getText().toString().equals("")) {
			Log.e(TAG, "Participant must have a name");
			return;
		}

		String newName = nameField.getText().toString();
		
		db.open();
		db.editParticipant(newName, pos);
		db.close();
		
		getIntent().putExtra(ParticipantManagerActivity.EXTRA_PARTICIPANT_ID, newName);
		
		AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
		adBuilder.setMessage("Name change successful")
			.setCancelable(false)
			.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					saveButton.setEnabled(false);
				}
			});
		AlertDialog alert = adBuilder.create();
		alert.show();
		
	}

}
