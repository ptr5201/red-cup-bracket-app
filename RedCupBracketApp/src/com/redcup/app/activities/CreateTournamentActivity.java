package com.redcup.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.redcup.app.R;
import com.redcup.app.data.RedCupDB;
import com.redcup.app.model.BracketTypeEnum;
import com.redcup.app.model.SingleEliminationBracketStrategy;
import com.redcup.app.model.Tournament;
import com.redcup.app.model.TournamentManager;

public class CreateTournamentActivity extends Activity {

	private static final String TAG = "CreateTournamentActivity";
	
	public static final int MAX_PARTICIPANTS = 128;
	public static final int MIN_PARTICIPANTS = 2;
	
	RedCupDB db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createtournament);

		Spinner bracketType = (Spinner) findViewById(R.id.bracketTypeSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.bracket_choices,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bracketType.setAdapter(adapter);

		EditText nameField = (EditText) findViewById(R.id.tournamentNameEditField);
		nameField.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				CreateTournamentActivity.this.updateStartButtonState();
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
		
		EditText participantCountField = (EditText) findViewById(R.id.participantCountEditField);
		participantCountField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				CreateTournamentActivity.this.updateStartButtonState();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		db = new RedCupDB(this);
		db.open();
		db.close();
	}
	
	private void updateStartButtonState() {
		Button createTournamentButton = (Button) findViewById(R.id.createTournamentButton);
		if (createTournamentButton != null) {
			boolean nameValid = true;
			boolean limitValid = true;
			EditText nameField = (EditText) findViewById(R.id.tournamentNameEditField);
			EditText limitField = (EditText) findViewById(R.id.participantCountEditField);

			if (nameField != null) {
				String name = nameField.getText().toString();
				nameValid = name.trim().length() > 0;
			}

			if (limitField != null) {
				String limit = limitField.getText().toString().trim();
				if(limit.length() > 0) {
					int l = Integer.parseInt(limit);
					limitValid = l >= MIN_PARTICIPANTS && l <= MAX_PARTICIPANTS;
				} else {
					limitValid = true;
				}
			}
			
			createTournamentButton.setEnabled(nameValid && limitValid);
		}
	}

	public void createTournament(View v) {
		EditText nameField = (EditText) findViewById(R.id.tournamentNameEditField);
		Spinner bracketType = (Spinner) findViewById(R.id.bracketTypeSpinner);
		EditText participantCountField = (EditText) findViewById(R.id.participantCountEditField);

		// Disallow empty tournament names
		if (nameField.getText() == null
				|| nameField.getText().toString().equals("")) {
			return;
		}

		// Disallow empty bracket types
		if (bracketType.getSelectedItemId() == AdapterView.INVALID_ROW_ID) {
			return;
		}

		// Determine the bracket type that was selected
		BracketTypeEnum bracketTypes[] = BracketTypeEnum.values();
		BracketTypeEnum tournamentBracketType = bracketTypes[(int) bracketType
				.getSelectedItemId()];

		// Start to set up a new tournament
		Tournament t = new Tournament();
		t.setName(nameField.getText().toString());

		// Prepare the initial participant list
		if (participantCountField.getText() != null) {
			String value = participantCountField.getText().toString();
			if (value != null && value.trim().length() > 0) {
				// TODO: set max participant capacity?
				int participantLimit = Integer.parseInt(participantCountField
						.getText().toString());
				t.setParticipantLimit(participantLimit);
			}
		}

		// TODO: determine how to set bracket strategy based on bracket type
		switch (tournamentBracketType) {
		case SINGLE_ELIMINATION:
			t.setStrategy(new SingleEliminationBracketStrategy(t
					.getParticipants()));
			break;
		case DOUBLE_ELIMINATION:
			// 2 * 0 = 0
			// 1 * 0 = 0
			// 2 * 0 = 1 * 0
			// 2 = 1
			// Therefore, single elimination = double elimination, right?
			// TODO: Fix this once we implement a model for double elimination
			t.setStrategy(new SingleEliminationBracketStrategy(t
					.getParticipants()));
			break;
		}

		// TODO: do we want a bracket type?
		// t.setBracketType(tournamentBracketType);

		// persist the tournament after it has been created
		Log.v(TAG, "Inserting new tournament in table");
		db.open();
		int t_id = (int) db.insertTournament(t.getName());
		db.close();
		
		Log.v(TAG, "Row ID of new tournament: " + t_id);
		t.setId(t_id);
		TournamentManager.addTournament(t);

		Intent tournamentParticipants = new Intent(this,
				TournamentParticipantsActivity.class);
		tournamentParticipants.putExtra(getString(R.string.EXTRA_TOURNAMENT_ID), t.getId());
		startActivity(tournamentParticipants);
	}

}