package com.redcup.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.redcup.app.R;
import com.redcup.app.data.RedCupDB;
import com.redcup.app.model.Tournament;
import com.redcup.app.model.TournamentManager;
import com.redcup.app.model.events.OnTournamentCompletedEvent;
import com.redcup.app.model.events.OnTournamentCompletedListener;
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
		this.tournament.addOnTournamentCompletedListener(new OnTournamentCompletedListener() {
			@Override
			public void tournamentCompleted(OnTournamentCompletedEvent event) {
				// Register completion in database
				db.open();
				db.completeTournament(tournament.getId());
				db.close();
				
				// Go to win screen
				Intent winTournament = new Intent(StartTournamentActivity.this, WinTournamentActivity.class);
				winTournament.putExtra(getString(R.string.EXTRA_TOURNAMENT_ID), tournament.getId());
				startActivity(winTournament);
			}
		});
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {	      
			startActivity(BracketAppActivity.getMainMenuIntent(this));
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
