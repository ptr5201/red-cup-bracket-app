package com.redcup.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.redcup.app.R;
import com.redcup.app.model.BracketTypeEnum;
import com.redcup.app.model.Tournament;

public class CreateTournamentActivity extends Activity {
	
	private static final String TAG = "CreateTournamentActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createtournament);
		
		Spinner bracketType = (Spinner) findViewById(R.id.bracketTypeSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.bracket_choices, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bracketType.setAdapter(adapter);
		
		EditText nameField = (EditText) findViewById(R.id.tournamentNameEditField);
		nameField.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				Button createTournamentButton = (Button) findViewById(R.id.createTournamentButton);
				if (s == null || s.length() == 0) {
					createTournamentButton.setEnabled(false);
				} else {
					createTournamentButton.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			
		});
	}
	
	public void createTournament(View v) {
		EditText nameField = (EditText) findViewById(R.id.tournamentNameEditField);
		Spinner bracketType = (Spinner) findViewById(R.id.bracketTypeSpinner);
		EditText participantCountField = (EditText) findViewById(R.id.participantCountEditField);
		
		// Disallow empty tournament names
		if (nameField.getText() == null || 
				nameField.getText().toString().equals("")) {
			return;
		}
		
		// Disallow empty bracket types
		if (bracketType.getSelectedItemId() == AdapterView.INVALID_ROW_ID) {
			return;
		}
		
		// Determine the bracket type that was selected
		BracketTypeEnum bracketTypes[] = BracketTypeEnum.values();
		BracketTypeEnum tournamentBracketType = bracketTypes[(int) bracketType.getSelectedItemId()];
		
		// Start to set up a new tournament
		Tournament t = new Tournament();
		t.setName(nameField.getText().toString());
		
		//TODO: determine how to set bracket strategy based on bracket type
		switch (tournamentBracketType) {
		case SINGLE_ELIMINATION:
			//t.setStrategy(new SingleEliminationBracketStrategy(null)); --> ambiguous constructor
			break;
		case DOUBLE_ELIMINATION:
			//
			break;
		}
		
		//TODO: do we want a bracket type?
		//t.setBracketType(tournamentBracketType);
		if (participantCountField.getText() != null &&
				participantCountField.getText().toString() != null) {
			//TODO: set max participant capacity?
			//t.setParticipantCapacity(Integer.parseInt(participantCountField.getText().toString()));
		}
		
		// TODO: we have the tournament set up, now we need to persist it
		Log.v(TAG, "Tournament created. Persist tournament and proceed to Tournament View");
		
		Intent tournamentParticipants = new Intent(this, TournamentParticipantsActivity.class);
		startActivity(tournamentParticipants);
	}

}