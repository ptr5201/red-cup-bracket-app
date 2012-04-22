package com.redcup.app.activities;

import java.util.ArrayList;

import com.redcup.app.R;
import com.redcup.app.model.Participant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ParticipantSelectorActivity extends Activity{
	
	private static final int ACTIVITY_NEW_PARTICIPANT = 1;
	
	private static final String TAG = "ParticipantSelectorActivity";
	
	private ListView participantList;
	private ArrayList<Participant> participants;
	private ArrayAdapter<Participant> participantAdapter;  
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.participantselector);
		
		participants = new ArrayList<Participant>();
		participantList = (ListView)findViewById(R.id.selectParticipantListView);
		participantAdapter = new ArrayAdapter<Participant>(this, android.R.layout.simple_list_item_1, participants);		
		participantList.setAdapter(participantAdapter);
		
	}

	public void createParticipant(View v){
		Log.v(TAG, getString(R.string.createParticipant) + " button clicked");
		Intent newParticipant = new Intent(this, NewParticipantActivity.class);
		startActivityForResult(newParticipant, ACTIVITY_NEW_PARTICIPANT);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_NEW_PARTICIPANT) {
			if (resultCode == RESULT_OK) {
				Participant p = (Participant) data.getSerializableExtra(
						NewParticipantActivity.NEW_PARTICIPANT_CREATED);
				participants.add(p);
				participantAdapter.notifyDataSetChanged();
				// TODO: list is updated; however, we need to persist the list
				// so that when the user leaves and returns to this screen,
				// the list will still exist
			}
		}
	}

}
