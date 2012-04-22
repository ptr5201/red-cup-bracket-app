package com.redcup.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.redcup.app.R;

public class TournamentParticipantsActivity extends Activity {
	
	private static final int ACTIVITY_ADD_PARTICIPANT = 1;
	
	private static final String TAG = "TournamentParticipantsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tournamentparticipants);
	}
	
	public void addParticipant(View v) {
		Intent i = new Intent(this, ParticipantSelectorActivity.class);
		startActivityForResult(i, ACTIVITY_ADD_PARTICIPANT);
	}
	
	public void startTournament(View v) {
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_ADD_PARTICIPANT) {
			if (resultCode == RESULT_OK) {
				Log.v(TAG, "got result back from ParticipantSelectorActivity");
			}
		}
	}
}
