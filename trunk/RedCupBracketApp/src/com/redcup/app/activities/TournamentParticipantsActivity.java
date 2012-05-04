package com.redcup.app.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.redcup.app.R;
import com.redcup.app.model.Participant;
import com.redcup.app.model.Tournament;
import com.redcup.app.model.TournamentManager;
import com.redcup.app.views.bracket.BracketView;

public class TournamentParticipantsActivity extends Activity {

	public static final String EXTRA_TOURNAMENT_ID = "EXTRA_TOURNAMENT_ID";

	private static final int ACTIVITY_ADD_PARTICIPANT = 1;

	private static final String TAG = "TournamentParticipantsActivity";

	private ArrayList<Participant> participantList;

	private Tournament tournament = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tournamentparticipants);

		// Check if we are getting a tournament to work with
		String tournamentID = getIntent().getStringExtra(EXTRA_TOURNAMENT_ID);
		this.tournament = TournamentManager.getTournament(tournamentID);

		// Assign tournament to BracketView
		BracketView bracketView = (BracketView) this
				.findViewById(R.id.bracketView);
		if (bracketView != null) {
			bracketView.setTournament(this.tournament);
		}
	}

	public void addParticipant(View v) {
		Intent i = new Intent(this, ParticipantSelectorActivity.class);
		startActivityForResult(i, ACTIVITY_ADD_PARTICIPANT);
	}

	public void startTournament(View v) {

	}

	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_ADD_PARTICIPANT) {
			if (resultCode == RESULT_OK) {
				Log.v(TAG, "got result back from ParticipantSelectorActivity");

				participantList = (ArrayList<Participant>) data
						.getSerializableExtra(null);

				for (Participant part : participantList) {
					Log.v(TAG, part.getName());
				}
				
				tournament.setParticipants(participantList);
			}
		}
	}
}
