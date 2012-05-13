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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.redcup.app.R;
import com.redcup.app.data.Constants;
import com.redcup.app.data.RedCupDB;
import com.redcup.app.model.Participant;

public class ParticipantManagerActivity extends Activity {
	
	public static final String EXTRA_PARTICIPANT_ID = "pos";
	public static final String EXTRA_PARTICIPANT_OLD_NAME = "name";
	private static final String TAG = "ParticipantManagerActivity";
	private ListView participantList;	
	
	RedCupDB db;
	ParticipantAdapter participantAdapter;

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.participantmanager);
		
		db = new RedCupDB(this);
		db.open();

		participantList = (ListView)findViewById(R.id.participantManagerListId);
		
		participantAdapter = new ParticipantAdapter(this);
		participantList.setAdapter(participantAdapter);
		
		participantList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
		    	return onLongListItemClick(v,pos,id);
		    }
		});
		
		participantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
		    	onListItemClick(v,pos,id);
		    }
		});
		
		db.close();
	}
	
	protected boolean onLongListItemClick(View v, int pos, long id) {
	    Log.i(TAG, "onLongListItemClick id=" + pos);
	    db.open();
	    
	    int keyid = participantAdapter.participants.get(pos).getId();
	    db.deleteParticipant(keyid);
    
	    participantAdapter.getdata();
	    participantAdapter.notifyDataSetChanged();
	    db.close();
	    return true;
	}
	
	protected void onListItemClick(View v, int pos, long id) {
	    Log.i(TAG, "onListItemClick id=" + pos);
	    String name = participantAdapter.participants.get(pos).getName();
	    Intent editParticipant = new Intent(this, EditParticipantActivity.class);
	    editParticipant.putExtra(EXTRA_PARTICIPANT_OLD_NAME, name);
	    db.open();
	    int keyid = participantAdapter.participants.get(pos).getId();
	    editParticipant.putExtra(EXTRA_PARTICIPANT_ID, keyid);
	    db.close();
		startActivity(editParticipant);
	}
	
	
	@Override
	public void onResume() {  
		super.onResume();
		db.open();
		participantAdapter.getdata();
	    participantAdapter.notifyDataSetChanged();
	    db.close();
	}
	
	public void createParticipant(View v) {
		Log.v(TAG, getString(R.string.createParticipant));

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
	
	private class ParticipantAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		public ArrayList<Participant> participants;
		
		public ParticipantAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			participants = new ArrayList<Participant>();
			getdata();
		}
		
		public void getdata() {
			participants.clear();
			Cursor c = db.getTableCursor(Constants.Participant.TABLE_NAME);
			startManagingCursor(c);
			if(c.moveToFirst()) {
				String name = c.getString(c.getColumnIndex(com.redcup.app.data.Constants.Participant.PARTICIPANT_NAME));
				
				if(c.getLong(c.getColumnIndex(com.redcup.app.data.Constants.Participant.DATE_ENDED)) == 0){
					Participant temp = new Participant(name);
					temp.setId(c.getInt(c.getColumnIndex(com.redcup.app.data.Constants.Participant.KEY_ID)));
					participants.add(0, temp);
				}
			}
			while(c.moveToNext()) {
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
				v = inflater.inflate(R.layout.participantlistitem, null);
				holder = new ViewHolder();				
				holder.name = (TextView)v.findViewById(R.id.textView1);
				v.setTag(holder);
			}
			else{
				holder = (ViewHolder) v.getTag();
			}
			
			holder.participant = (Participant) getItem(position);
			holder.name.setText(holder.participant.getName());
			
			return v;
		}
		
	}
	
	public class ViewHolder{
		Participant participant;
		int keyid;
		TextView name;
	}
	
}
