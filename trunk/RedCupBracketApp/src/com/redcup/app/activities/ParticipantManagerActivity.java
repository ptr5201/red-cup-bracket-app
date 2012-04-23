package com.redcup.app.activities;


import java.util.ArrayList;

import com.redcup.app.R;
import com.redcup.app.data.Constants;
import com.redcup.app.data.RedCupDB;
import com.redcup.app.model.Participant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ParticipantManagerActivity extends Activity {
	
	private static final String TAG = "ParticipantManagerActivity";
	
	private ListView participantList;	
	
	RedCupDB db;
	ParticipantAdapter participantAdapter;

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.participantmanager);
		
		db = new RedCupDB(this);
		db.open();

		participantList = (ListView)findViewById(R.id.listView1);
		
		participantAdapter = new ParticipantAdapter(this);
		participantList.setAdapter(participantAdapter);
		
		participantList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
		    	return onLongListItemClick(v,pos,id);
		    }
		});
		
		db.close();
	}
	
	protected boolean onLongListItemClick(View v, int pos, long id) {
	    Log.i(TAG, "onLongListItemClick id=" + pos);
	    db.open();
	    db.deleteParticipant(participantAdapter.participants.get(pos).getName());
	    participantAdapter.getdata();
	    participantAdapter.notifyDataSetChanged();
	    db.close();
	    return true;
	}

	
	public void createParticipant(View v){
		Log.v(TAG, getString(R.string.createParticipant));
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter Name");

		final EditText input = new EditText(this);

		input.setText("");
		builder.setView(input);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
				
			if(!input.getText().toString().equals("")){
				db.open();
				db.insertParticipant(input.getText().toString());
				participantAdapter.getdata();
				participantAdapter.notifyDataSetChanged();
				db.close();
			}			
		}
		});

		builder.create();
		builder.show();
	}
	
	public void updateList(){

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
			Cursor c = db.getParticipants();
			startManagingCursor(c);
			if(c.moveToFirst()){
				String name = c.getString(c.getColumnIndex(Constants.PARTICIPANT_NAME));
			
				Participant temp = new Participant(name);
				participants.add(0, temp);
			}
			while(c.moveToNext()){
				String name = c.getString(c.getColumnIndex(Constants.PARTICIPANT_NAME));
				
				Participant temp = new Participant(name);
				participants.add(0, temp);
			}
			db.close();
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
		TextView name;
	}
	
}
