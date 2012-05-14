package com.redcup.app.model.events;

/**
 * Listener for {@code OnTournamentCompletedEvent}s.
 * 
 * @author Jackson Lamp
 */
public interface OnTournamentCompletedListener {
	public void tournamentCompleted(OnTournamentCompletedEvent event);
}
