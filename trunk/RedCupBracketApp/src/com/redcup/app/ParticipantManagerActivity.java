package com.redcup.app;


import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ParticipantManagerActivity extends Activity {
	
	private static final String TAG = "ParticipantManagerActivity";
	
	private ListView participantList;
	private ArrayList<Participant> participants;
	private ArrayAdapter<Participant> participantAdapter;   
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.participantmanager);
		
		participants = new ArrayList<Participant>();
		participantList = (ListView)findViewById(R.id.listView1);
		participantAdapter = new ArrayAdapter<Participant>(this, android.R.layout.simple_list_item_1, participants);		
		participantList.setAdapter(participantAdapter);
		
	}
	
	public void createParticipant(View v){
		Log.v(TAG, getString(R.string.createParticipant));
	}
	
	public void updateList(){

    }
	
}
