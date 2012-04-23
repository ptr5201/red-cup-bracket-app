package com.redcup.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


public class RedCupDB{
	private SQLiteDatabase db;
	private final Context context;
	private final RedCupDBHelper helper;
	
	public RedCupDB(Context c){
		context = c;
		helper = new RedCupDBHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
	}
	
	public void close(){
		db.close();
	}
	
	public void open(){
		try{
			db = helper.getWritableDatabase();
		}catch(SQLiteException e){
			Log.v("Open Database Exception", e.getMessage());
			db = helper.getReadableDatabase();
		}
	}
	
	public long insertParticipant(String name){
		try{
			ContentValues taskValue = new ContentValues();
			taskValue.put(Constants.PARTICIPANT_NAME, name);
			taskValue.put(Constants.DATE_NAME, 
							java.lang.System.currentTimeMillis());
			return db.insert(Constants.PARTICIPANT_TABLE, null, taskValue);
		}catch(Exception e){
			return -1;
		}
	}
	
	public void deleteParticipant(String name){
		
		db.delete(Constants.PARTICIPANT_TABLE, Constants.PARTICIPANT_NAME+"=?", new String [] { name });
	}
	
	public Cursor getParticipants(){
		Cursor c = db.query(Constants.PARTICIPANT_TABLE, null, null, null, null, null, null);
		return c;
	}
}