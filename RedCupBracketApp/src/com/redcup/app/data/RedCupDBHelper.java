package com.redcup.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RedCupDBHelper extends SQLiteOpenHelper{
	
	private static final String CREATE_PARTICIPANT_TABLE = "create table "+
		Constants.PARTICIPANT_TABLE+" ("+
		Constants.KEY_ID+" integer primary key autoincrement, "+
		Constants.PARTICIPANT_NAME+" text not null, "+
		Constants.DATE_NAME+" long);";
	
	public RedCupDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("Helper onCreate", "Creating the tables");
		try{
			db.execSQL(CREATE_PARTICIPANT_TABLE);
		}catch(Exception e){
			Log.v("Create Tables Error", e.getMessage());
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists "+Constants.PARTICIPANT_NAME);
		onCreate(db);		
	}

}
