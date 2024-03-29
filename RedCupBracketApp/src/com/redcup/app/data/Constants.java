package com.redcup.app.data;

public final class Constants {
	
	public static final String DATABASE_NAME = "RedCupData";
	public static final int DATABASE_VERSION = 1;
	
	public static final class Participant {

		public static final String TABLE_NAME = "participants";
		public static final String KEY_ID = "p_id";
		public static final String DATE_CREATED = "p_date";
		public static final String DATE_ENDED = "p_date_end";
		public static final String PARTICIPANT_NAME = "p_name";
		
	}
	
	public static final class Tournament {
		
		public static final String TABLE_NAME = "tournaments";
		public static final String KEY_ID = "t_id";
		public static final String DATE_CREATED = "t_date";
		public static final String TOURNAMENT_NAME = "t_name";
		public static final String STARTED = "t_started";
		public static final String COMPLETED = "t_completed";
		
	}
	
	public static final class Tournament_Participant {
		
		public static final String TABLE_NAME = "tournaments_participants";
		public static final String KEY_ID = "tp_id";
		public static final String DATE_CREATED = "tp_date";
		public static final String TOURNAMENT_ID = "tp_t_id";
		public static final String PARTICIPANT_ID = "tp_p_id";
		
	}
}
