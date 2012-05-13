package com.redcup.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.redcup.app.R;

public class LoadTournamentActivity extends Activity {

	private static final String TAG = "LoadTournamentActivity";
	
	private ListView loadTournamentList;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadtournament);
		
		loadTournamentList = (ListView) findViewById(R.id.loadTournamentListId);
	}

	public void loadTournament(View v) {
		Log.v(TAG, getString(R.string.loadTournament));
	}
}
