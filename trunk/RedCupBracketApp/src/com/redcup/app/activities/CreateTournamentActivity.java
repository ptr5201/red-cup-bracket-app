package com.redcup.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.redcup.app.R;
import com.redcup.app.model.BracketTypeEnum;
import com.redcup.app.model.SingleEliminationBracketStrategy;
import com.redcup.app.model.Tournament;

public class CreateTournamentActivity extends Activity {
	
	private static final String TAG = "CreateTournamentActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createtournament);
		
		Spinner bracketType = (Spinner) findViewById(R.id.bracketType);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.bracket_choices, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bracketType.setAdapter(adapter);
	}
	
	public void createTournament(View v) {
		EditText nameField = (EditText) findViewById(R.id.nameField);
		Spinner bracketType = (Spinner) findViewById(R.id.bracketType);
		EditText participantCountField = (EditText) findViewById(R.id.participantCountField);
		
		// TODO: enable the "Create Tournament" button iff the 
		// name field AND bracket type are not empty
		if (nameField.getText() == null || 
				nameField.getText().toString().equals("")) {
			Log.e(TAG, "Tournament Name must have a non-empty value");
			return;
		}
		
		if (bracketType.getSelectedItemId() == AdapterView.INVALID_ROW_ID) {
			Log.e(TAG, "Bracket Type must have a non-empty value");
			return;
		}
		BracketTypeEnum bracketTypes[] = BracketTypeEnum.values();
		BracketTypeEnum tournamentBracketType = bracketTypes[(int) bracketType.getSelectedItemId()];
		
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
	}

}