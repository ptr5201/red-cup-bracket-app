package com.redcup.app.model;

/**
 * Essentially a tree interface w/ added methods for tourneys
 *
 */
public interface BracketStrategy {
	public enum BracketEvent {
		RelocateUp, RelocateDown, Win, UnWin, DQ
	}
	/**
	 * Is the passed participant in this tournament?
	 * @param participant
	 * @return boolean indicating presence
	 */
	public boolean lookup(Participant participant);
	
	/**
	 * Add the passed participant to the tournament.
	 * 
	 * Participant is added to the leftmost AVAILABLE position in the earliest round.
	 * @param participant
	 */
	public void add(Participant participant);
	
	/**
	 * Indicate to the BracketStrategy that an event has occured
	 * @param event
	 * @param participant
	 * @return boolean indicating success of event
	 * @throws InvalidStateException when the event cannot occur
	 */
	public boolean occur(BracketEvent event, Participant participant) throws InvalidStateException;

	
	/**
	 * @return number of rounds in tournament
	 */
	public int numRounds();
	
}
