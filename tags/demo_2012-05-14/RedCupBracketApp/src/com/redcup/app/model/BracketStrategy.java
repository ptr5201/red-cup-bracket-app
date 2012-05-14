package com.redcup.app.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.redcup.app.model.events.OnTournamentCompletedEvent;
import com.redcup.app.model.events.OnTournamentCompletedListener;

/**
 * Essentially a tree interface w/ added methods for tourneys
 *
 */
public abstract class BracketStrategy {
	private Collection<OnTournamentCompletedListener> tournamentCompletedListenerList = new ArrayList<OnTournamentCompletedListener>();
	
	/**
	 * Is the passed participant in this tournament?
	 * @param participant
	 * @return boolean indicating presence
	 */
	public abstract Bracket lookup(Participant participant);
	
	public abstract void relocateUp(Participant participant) throws InvalidStateException;
	public abstract void relocateDown(Participant participant) throws InvalidStateException;
	public abstract void unWin(Participant participant) throws InvalidStateException;
	public abstract void win(Participant participant) throws InvalidStateException;
	public abstract void promoteParticipantAt(Bracket bracket) throws InvalidStateException;
	public abstract void demoteParticipantAt(Bracket bracket) throws InvalidStateException;
	
	/**
	 * @return number of rounds in tournament
	 */
	public abstract int numRounds();
	
	public abstract List<List<Bracket>> getRoundStructure();
	
	public void addOnTournamentCompletedListener(OnTournamentCompletedListener listener) {
		if(!this.tournamentCompletedListenerList.contains(listener)) {
			this.tournamentCompletedListenerList.add(listener);
		}
	}
	
	public void removeOnTournamentCompletedListener(OnTournamentCompletedListener listener) {
		this.tournamentCompletedListenerList.remove(listener);
	}
	
	protected void raiseOnTournamentCompletedEvent(OnTournamentCompletedEvent event) {
		for(OnTournamentCompletedListener l : this.tournamentCompletedListenerList) {
			l.tournamentCompleted(event);
		}
	}
}
