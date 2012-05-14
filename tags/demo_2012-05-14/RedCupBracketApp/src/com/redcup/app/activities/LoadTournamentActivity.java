package com.redcup.app.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.redcup.app.R;
import com.redcup.app.data.RedCupDB;
import com.redcup.app.model.Tournament;
import com.redcup.app.model.TournamentManager;

public class LoadTournamentActivity extends ListActivity {

	private static final String TAG = "LoadTournamentActivity";
	
	private RedCupDB db;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadtournament);
		db = new RedCupDB(this);
		db.open();
		List<String> tournaments = db.getTournaments();
		db.close();
		ListView tListView = (ListView)findViewById(android.R.id.list);
		
//		setListAdapter(new ArrayAdapter<String>(this, R.id.tournamentListItemId, tournaments));
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, tournaments);
		
		tListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
		    	// We can't save tournaments yet, so...
//		    	Intent loadTournament = new Intent(LoadTournamentActivity.this, StartTournamentActivity.class);
//			    loadTournament.putExtra(getString(R.string.EXTRA_TOURNAMENT_ID), pos);
//			    TournamentManager.addTournament(db.getTournament(pos));
//				startActivity(loadTournament);
		    }
		});
		
		tListView.setAdapter(adapter);
	}
	
	public void loadTournament(View v) {
		Log.v(TAG, getString(R.string.loadTournament));
		
		
	}	
}
