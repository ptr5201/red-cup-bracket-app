package com.redcup.app.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.redcup.app.R;
import com.redcup.app.data.Constants;
import com.redcup.app.data.RedCupDB;
import com.redcup.app.model.Participant;

public class ParticipantSelectorActivity extends Activity {
	
	private static final String TAG = "ParticipantSelectorActivity";
	
	private ListView participantListView;
	private ArrayList<Participant> participantList;
	
	RedCupDB db;
	ParticipantAdapter participantAdapter;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.participantselector);
		
		db = new RedCupDB(this);
		db.open();
		
		participantList = new ArrayList<Participant>();
		participantListView = (ListView)findViewById(R.id.selectParticipantListView);
		
		participantAdapter = new ParticipantAdapter(this);
		participantListView.setAdapter(participantAdapter);
		db.close();
	}

	public void createParticipant(View v){
		Log.v(TAG, getString(R.string.createParticipant) + " button clicked");
		Intent newParticipant = new Intent(this, NewParticipantActivity.class);
		startActivityForResult(newParticipant, NewParticipantActivity.ACTIVITY_NEW_PARTICIPANT);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NewParticipantActivity.ACTIVITY_NEW_PARTICIPANT) {
			if (resultCode == RESULT_OK) {
				Participant p = (Participant) data.getSerializableExtra(
						NewParticipantActivity.NEW_PARTICIPANT_CREATED);
				
				db.open();
				db.insertParticipant(p.getName());
				participantAdapter.getdata();
				db.close();
				participantAdapter.notifyDataSetChanged();
			}
		}
	}
	
	public void done(View v){
		Intent result = new Intent();
		result.putExtra(null, participantList);
		setResult(RESULT_OK, result);
		finish();
	}
		
	
	private class ParticipantAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		public ArrayList<Participant> participants;
		
		public ParticipantAdapter(Context context){
			inflater = LayoutInflater.from(context);
			participants = new ArrayList<Participant>();
			getdata();
		}
		
		public void getdata(){
			participants.clear();
			Cursor c = db.getTableCursor(Constants.Participant.TABLE_NAME);
			startManagingCursor(c);
			if (c.moveToFirst()) {
				String name = c.getString(c.getColumnIndex(com.redcup.app.data.Constants.Participant.PARTICIPANT_NAME));
			
				if(c.getLong(c.getColumnIndex(com.redcup.app.data.Constants.Participant.DATE_ENDED)) == 0){
					Participant temp = new Participant(name);
					temp.setId(c.getInt(c.getColumnIndex(com.redcup.app.data.Constants.Participant.KEY_ID)));
					participants.add(0, temp);
				}
			}
			while (c.moveToNext()) {
				String name = c.getString(c.getColumnIndex(com.redcup.app.data.Constants.Participant.PARTICIPANT_NAME));
				
				if(c.getLong(c.getColumnIndex(com.redcup.app.data.Constants.Participant.DATE_ENDED)) == 0){
					Participant temp = new Participant(name);
					temp.setId(c.getInt(c.getColumnIndex(com.redcup.app.data.Constants.Participant.KEY_ID)));
					participants.add(0, temp);
				}
			}
			db.close();
			
			Collections.sort(participants, new Comparator<Participant>() {

				@Override
				public int compare(Participant lhs, Participant rhs) {
					return lhs.getName().compareToIgnoreCase(rhs.getName());
				}
			});
		}
		
		@Override
		public int getCount() {
			return participants.size();
		}

		@Override
		public Object getItem(int arg0) {
			return participants.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View v = convertView;
			
			if((v == null) || (v.getTag() == null)){
				v = inflater.inflate(R.layout.participantselectoritem, null);
				holder = new ViewHolder();				
				holder.name = (TextView)v.findViewById(R.id.selectorParticipantNameId);
				holder.check = (CheckBox)v.findViewById(R.id.selectorParticipantCheckboxId);
				
				v.setTag(holder);
			}
			else{
				holder = (ViewHolder) v.getTag();
			}
			
			holder.participant = (Participant) getItem(position);
			holder.name.setText(holder.participant.getName());
			
			holder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() { 

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (buttonView.isChecked()) { 
						participantList.add(holder.participant);
				    } 
				    else 
				    { 
				    	participantList.remove(holder.participant); 
				    }
				} 
			  });
			
			return v;
		}
		
	}
	
	public class ViewHolder{
		Participant participant;
		TextView name;
		CheckBox check;
	}
}
