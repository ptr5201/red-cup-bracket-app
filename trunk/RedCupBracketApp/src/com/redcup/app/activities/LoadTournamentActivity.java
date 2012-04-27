package com.redcup.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.redcup.app.R;

public class LoadTournamentActivity extends Activity {

	private static final String TAG = "LoadTournamentActivity";
	
	private ListView ongoingTournamentsList;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadtournament);
		
		ongoingTournamentsList = (ListView) findViewById(R.id.listView1);
	}

	public void loadTournament(View v) {
		Log.v(TAG, getString(R.string.loadTournament));
	}
}
