package com.redcup.app.activities;

import com.redcup.app.R;
import com.redcup.app.R.id;
import com.redcup.app.R.layout;
import com.redcup.app.R.string;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

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
			// TODO: the following code is commented until representation
			// for ongoing tournaments is determined
			
			//Intent loadTournament = new Intent(this, LoadTournamentActivity.class);
			//startActivity(loadTournament);
			break;
		case R.id.pastTournaments:
			Log.v(TAG, getString(R.string.pastTournaments));
			break;
		case R.id.participants:
			Log.v(TAG, getString(R.string.participants));
			Intent participantManager = new Intent(this, ParticipantManagerActivity.class);
			startActivity(participantManager);
			break;
		case R.id.help:
			Log.v(TAG, getString(R.string.help));
			break;
		}
	}
}