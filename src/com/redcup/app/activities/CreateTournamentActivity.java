package com.redcup.app.activities;

import java.util.ArrayList;
import java.util.List;

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
import com.redcup.app.model.Participant;
import com.redcup.app.model.SingleEliminationBracketStrategy;
import com.redcup.app.model.Tournament;
import com.redcup.app.model.TournamentManager;

public class CreateTournamentActivity extends Activity {

	private static final String TAG = "CreateTournamentActivity";

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

		// TODO: Remove test code
		List<Participant> p = new ArrayList<Participant>();
		int numParticipants = 7;
		String countText = participantCountField.getText().toString();
		if (countText != null && countText.length() > 0) {
			numParticipants = Integer.parseInt(countText);
		}
		for (int i = 1; i <= numParticipants; i++) {
			p.add(new Participant("Player " + i));
		}
		t.setParticipants(p);
		TournamentManager.addTournament(t);

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
		if (participantCountField.getText() != null
				&& participantCountField.getText().toString() != null) {
			// TODO: set max participant capacity?
			// t.setParticipantCapacity(Integer.parseInt(participantCountField.getText().toString()));
		}

		// TODO: we have the tournament set up, now we need to persist it
		Log.v(TAG,
				"Tournament created. Persist tournament and proceed to Tournament View");

		Intent tournamentParticipants = new Intent(this,
				TournamentParticipantsActivity.class);
		tournamentParticipants
				.putExtra(TournamentParticipantsActivity.EXTRA_TOURNAMENT_ID,
						t.getName());
		startActivity(tournamentParticipants);
	}

}