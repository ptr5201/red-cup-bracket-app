package com.redcup.app.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.redcup.app.R;
import com.redcup.app.data.RedCupDB;
import com.redcup.app.model.Participant;
import com.redcup.app.model.Tournament;
import com.redcup.app.model.TournamentManager;
import com.redcup.app.model.events.OnParticipantChangedEvent;
import com.redcup.app.model.events.OnParticipantChangedListener;
import com.redcup.app.views.bracket.BracketView;

public class TournamentParticipantsActivity extends Activity {

	private static final int ACTIVITY_ADD_PARTICIPANT = 1;
	public static final int ACTIVITY_START_TOURNAMENT = 2;

	private static final String TAG = "TournamentParticipantsActivity";

	private ArrayList<Participant> participantList;

	private Tournament tournament = null;

	private RedCupDB db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tournamentparticipants);

		// Check if we are getting a tournament to work with
		int tournamentID = getIntent().getIntExtra(
				getString(R.string.EXTRA_TOURNAMENT_ID), -1);
		Log.v(TAG, "Row ID retrieved from create tournament screen: "
				+ tournamentID);
		this.tournament = TournamentManager.getTournament(tournamentID);

		// Assign tournament to BracketView
		BracketView bracketView = (BracketView) this
				.findViewById(R.id.bracketView);
		if (bracketView != null) {
			bracketView.setTournament(this.tournament);
		}

		// Initialize start tournament button
		Button startTournamentButton = (Button) findViewById(R.id.startTournamentButton);
		if (startTournamentButton != null) {
			startTournamentButton.setEnabled(tournament.canTournamentStart());
		}

		// Add listeners to the Tournament
		this.tournament
				.addOnParticipantListChangedListener(new OnParticipantChangedListener() {
					@Override
					public void onParticipantListChanged(
							OnParticipantChangedEvent event) {
						// Update start tournament button's state
						Button startTournamentButton = (Button) findViewById(R.id.startTournamentButton);
						if (startTournamentButton != null) {
							startTournamentButton.setEnabled(tournament
									.canTournamentStart());
						}
					}
				});

		db = new RedCupDB(this);
		db.open();
		db.close();
	}

	public void addParticipant(View v) {
		Intent i = new Intent(this, ParticipantSelectorActivity.class);
		startActivityForResult(i, ACTIVITY_ADD_PARTICIPANT);
	}

	public void startTournament(View v) {
		db.open();
		db.startTournament(tournament.getId());
		db.close();
		Intent startTournament = new Intent(this, StartTournamentActivity.class);
		startTournament.putExtra("TournamentName", tournament.getName());
		startTournament.putExtra(getString(R.string.EXTRA_TOURNAMENT_ID),
				tournament.getId());
		startActivityForResult(startTournament, ACTIVITY_START_TOURNAMENT);
	}

	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_ADD_PARTICIPANT) {
			if (resultCode == RESULT_OK) {
				Log.v(TAG, "got result back from ParticipantSelectorActivity");

				participantList = (ArrayList<Participant>) data
						.getSerializableExtra(null);

				db.open();
				for (Participant p : participantList) {
					Log.v(TAG, p.getName());
					this.tournament.addParticipant(p);
					db.insertTournamentParticipantLink(tournament.getId(),
							p.getId());
				}
				db.close();

				BracketView bracketView = (BracketView) this
						.findViewById(R.id.bracketView);
				if (bracketView != null) {
					bracketView.setTournament(this.tournament);
				}

			}
		}
		if (requestCode == ACTIVITY_START_TOURNAMENT){
			this.finish();
		}
	}
}
