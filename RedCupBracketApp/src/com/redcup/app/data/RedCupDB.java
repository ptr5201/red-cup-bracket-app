package com.redcup.app.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.redcup.app.model.Participant;
import com.redcup.app.model.Tournament;


public class RedCupDB {
	private SQLiteDatabase db;
	private final Context context;
	private final RedCupDBHelper helper;

	public RedCupDB(Context c) {
		context = c;
		helper = new RedCupDBHelper(context, 
				Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
	}

	public void close() {
		db.close();
	}

	public void open() {
		try {
			db = helper.getWritableDatabase();
		} catch(SQLiteException e) {
			Log.v("Open Database Exception", e.getMessage());
			db = helper.getReadableDatabase();
		}
	}

	public long insertParticipant(String name) {
		ContentValues taskValue = new ContentValues();
		taskValue.put(Constants.Participant.PARTICIPANT_NAME, name);
		taskValue.put(Constants.Participant.DATE_CREATED, 
				java.lang.System.currentTimeMillis());
		taskValue.put(Constants.Participant.DATE_ENDED, 0);
		return db.insert(Constants.Participant.TABLE_NAME, null, taskValue);
	}

	public void deleteParticipant(int participantId) {	
		/*db.delete(Constants.Participant.TABLE_NAME, 
				Constants.Participant.KEY_ID + "=?", 
				new String [] { Integer.toString(participantId) });*/

		ContentValues taskValue = new ContentValues();
		taskValue.put(Constants.Participant.DATE_ENDED, 
				java.lang.System.currentTimeMillis());
		db.update(Constants.Participant.TABLE_NAME, taskValue, 
				Constants.Participant.KEY_ID + "=?", 
				new String [] { Integer.toString(participantId) });
	}

	public void editParticipant(String newName, int participantId) {
		ContentValues taskValue = new ContentValues();
		taskValue.put(Constants.Participant.PARTICIPANT_NAME, newName);
		db.update(Constants.Participant.TABLE_NAME, taskValue, 
				Constants.Participant.KEY_ID + "=?", 
				new String [] { Integer.toString(participantId) });
	}

	public long insertTournament(String name) {
		ContentValues taskValue = new ContentValues();
		taskValue.put(Constants.Tournament.TOURNAMENT_NAME, name);
		taskValue.put(Constants.Tournament.DATE_CREATED, 
				java.lang.System.currentTimeMillis());
		return db.insert(Constants.Tournament.TABLE_NAME, null, taskValue);
	}

	public void startTournament(int tournamentId) {
		ContentValues taskValue = new ContentValues();
		taskValue.put(Constants.Tournament.STARTED, 1L);
		db.update(Constants.Tournament.TABLE_NAME, taskValue, 
				Constants.Tournament.KEY_ID + "=?",  
				new String [] { Integer.toString(tournamentId) });
	}

	public void completeTournament(int tournamentId) {
		ContentValues taskValue = new ContentValues();
		taskValue.put(Constants.Tournament.COMPLETED, 1L);
		db.update(Constants.Tournament.TABLE_NAME, taskValue, 
				Constants.Tournament.KEY_ID + "=?",  
				new String [] { Integer.toString(tournamentId) });
	}

	public List<String> getTournaments() {
		List<String> tournaments = new ArrayList<String>();

		Cursor c = db.query(true, Constants.Tournament.TABLE_NAME, new String[] {Constants.Tournament.TOURNAMENT_NAME}, 
				null, null, null, null, null, null);
	        if (c != null) {
	          c.moveToFirst();
	        }
	        while (c.isAfterLast() == false) {
	            tournaments.add(c.getString(0));
	            c.moveToNext();
	        }
	    c.close();
		db.close();
		return tournaments;
	}

	public long insertTournamentParticipantLink(int tournamentId, int participantId) {
		ContentValues taskValue = new ContentValues();
		taskValue.put(Constants.Tournament_Participant.TOURNAMENT_ID, tournamentId);
		taskValue.put(Constants.Tournament_Participant.PARTICIPANT_ID, participantId);
		taskValue.put(Constants.Tournament_Participant.DATE_CREATED, 
				java.lang.System.currentTimeMillis());
		return db.insert(Constants.Tournament_Participant.TABLE_NAME, null, taskValue);
	}

	public void deleteTournamentParticipantLink(int tournamentId, int participantId) {
		db.delete(Constants.Tournament_Participant.TABLE_NAME, 
				Constants.Tournament_Participant.TOURNAMENT_ID + "=?" + " and " + 
				Constants.Tournament_Participant.PARTICIPANT_ID + "=?", 
				new String [] { 
				Integer.toString(tournamentId),
				Integer.toString(participantId)
		});
	}

	public Cursor getTableCursor(String tableName) {
		Cursor c = db.query(tableName, null, null, null, null, null, null);
		return c;
	}
}