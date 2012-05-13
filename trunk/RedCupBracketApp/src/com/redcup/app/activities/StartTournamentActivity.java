package com.redcup.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.redcup.app.R;
import com.redcup.app.data.RedCupDB;
import com.redcup.app.model.Tournament;
import com.redcup.app.model.TournamentManager;
import com.redcup.app.views.bracket.BracketView;

public class StartTournamentActivity extends Activity {
	
	private static final String TAG = "StartTournamentActivity";
	private String tName;
	private Tournament tournament;
	private RedCupDB db;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set textview to display tournament name
		tName = getIntent().getStringExtra(getString(R.string.TOURNAMENT_NAME));
		System.out.format("The tournament name is %s", tName);		
		setContentView(R.layout.runtournament);
		Log.v(TAG, "Start Tournament View entered");
		
		// Check if we are getting a tournament to work with
		int tournamentID = getIntent().getIntExtra(getString(R.string.EXTRA_TOURNAMENT_ID), -1);
		Log.v(TAG, "Row ID retrieved from create tournament screen: " + tournamentID);
		this.tournament = TournamentManager.getTournament(tournamentID);

		// Assign tournament to BracketView
		BracketView bracketView = (BracketView) this.findViewById(R.id.bracketView);
		if (bracketView != null) {
			bracketView.setTournament(this.tournament);
		}
		
		db = new RedCupDB(this);
		db.open();
		db.close();
	}
}
