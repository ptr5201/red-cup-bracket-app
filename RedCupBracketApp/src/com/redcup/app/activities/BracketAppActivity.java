package com.redcup.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.redcup.app.R;

public class BracketAppActivity extends Activity {

	private static final String TAG = "BracketAppActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.v(TAG, "we are up and running");
	}

	public void switchActivityScreen(View v) {
		Log.v(TAG, "something clicked: " + v.getId());
		
		switch(v.getId()) {
		case R.id.createTournament:
			Log.v(TAG, getString(R.string.createTournament));
			Intent createTournament = new Intent(this, CreateTournamentActivity.class);
			startActivity(createTournament);
			break;
		case R.id.loadTournament:
			Log.v(TAG, getString(R.string.loadTournament));
			Intent loadTournament = new Intent(this, LoadTournamentActivity.class);
			startActivity(loadTournament);
			break;
		case R.id.participants:
			Log.v(TAG, getString(R.string.participants));
			Intent participantManager = new Intent(this, ParticipantManagerActivity.class);
			startActivity(participantManager);
			break;
		}
	}
}