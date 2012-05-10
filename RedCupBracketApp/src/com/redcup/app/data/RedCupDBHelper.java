package com.redcup.app.data;

import com.redcup.app.data.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RedCupDBHelper extends SQLiteOpenHelper {
	
	private static final String CREATE_PARTICIPANT_TABLE = "create table " +
		Constants.Participant.TABLE_NAME + " (" +
		Constants.Participant.KEY_ID + " integer primary key autoincrement, " +
		Constants.Participant.PARTICIPANT_NAME + " text not null, " +
		Constants.Participant.DATE_CREATED + " long" +
				");";
	
	private static final String CREATE_TOURNAMENT_TABLE = "create table " +
		Constants.Tournament.TABLE_NAME + " (" +
		Constants.Tournament.KEY_ID + " integer primary key autoincrement, " +
		Constants.Tournament.TOURNAMENT_NAME + " text not null, " +
		Constants.Tournament.DATE_CREATED + " long, " +
		Constants.Tournament.STARTED + " integer not null default (0), " +
		Constants.Tournament.COMPLETED + " integer not null default (0)" +
				");";
	
	public RedCupDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("Helper onCreate", "Creating the tables");
		try{
			db.execSQL(CREATE_PARTICIPANT_TABLE);
			db.execSQL(CREATE_TOURNAMENT_TABLE);
		}catch(Exception e){
			Log.v("Create Tables Error", e.getMessage());
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + Constants.Participant.TABLE_NAME);
		db.execSQL("drop table if exists " + Constants.Tournament.TABLE_NAME);
		onCreate(db);		
	}

}
