package com.redcup.app.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.redcup.app.R;
import com.redcup.app.model.Bracket;
import com.redcup.app.model.Participant;
import com.redcup.app.model.Tournament;
import com.redcup.app.model.TournamentManager;

public class WinTournamentActivity extends Activity {

	private static final String TAG = "WinTournamentActivity";
	
	private Tournament tournament;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Check if we are getting a tournament to work with
		int tournamentID = getIntent().getIntExtra(getString(R.string.EXTRA_TOURNAMENT_ID), -1);
		Log.v(TAG, "Row ID retrieved from create tournament screen: " + tournamentID);
		
		this.tournament = TournamentManager.getTournament(tournamentID);
		setContentView(R.layout.wintournament);		
		((Button)findViewById(R.id.mainMenuButton)).setOnClickListener(mainMenuListener);
		Participant winner;
		try {
			winner = getWinner();
		} catch (NullPointerException e) {
			Log.v(TAG, "Error reading winner");
			winner = new Participant("Tebow");
		}
		((TextView)findViewById(R.id.tournamentWinnerName)).setText(winner.getName());
		((TextView)findViewById(R.id.tournamentName)).setText(tournament.getName());
	}
	
	private Participant getWinner() {
		List<List<Bracket>> struct = tournament.getStrategy().getRoundStructure();
		List<Bracket> round = struct.get(struct.size()-1);
		Participant winner = round.get(0).getParticipant(); 
		return winner;
	}
	
	private OnClickListener mainMenuListener = new OnClickListener() {
		public void onClick(View v) {
			startActivity(BracketAppActivity.getMainMenuIntent(WinTournamentActivity.this));			
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {	      
			startActivity(BracketAppActivity.getMainMenuIntent(this));
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
