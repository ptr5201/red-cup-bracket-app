package com.redcup.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.redcup.app.R;
import com.redcup.app.data.RedCupDB;
import com.redcup.app.model.Tournament;
import com.redcup.app.model.TournamentManager;
import com.redcup.app.views.bracket.BracketMode;
import com.redcup.app.views.bracket.BracketView;

public class StartTournamentActivity extends Activity {
	
	private static final String TAG = "StartTournamentActivity";
	private Tournament tournament;
	private RedCupDB db;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.runtournament);
		Log.v(TAG, "Start Tournament View entered");
		// Check if we are getting a tournament to work with
		int tournamentID = getIntent().getIntExtra(getString(R.string.EXTRA_TOURNAMENT_ID), -1);
		Log.v(TAG, "Row ID retrieved from create tournament screen: " + tournamentID);
		
		this.tournament = TournamentManager.getTournament(tournamentID);
		Log.v(TAG, "Tournament name retrieved: " + tournament.getName());

		// Assign tournament to BracketView
		BracketView bracketView = (BracketView) this.findViewById(R.id.bracketView);
		if (bracketView != null) {
			bracketView.setTournament(this.tournament);
			bracketView.setMode(BracketMode.ADVANCEMENT);
		}
		
		// Set text view to tournament name
		TextView tName = (TextView)this.findViewById(R.id.tournamentName);
		tName.setText(tournament.getName());
		
		db = new RedCupDB(this);
		db.open();
		db.close();
	}
}
