package com.redcup.app.model;

import java.util.List;

/**
 * Essentially a tree interface w/ added methods for tourneys
 *
 */
public interface BracketStrategy {
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
	
	public boolean relocateUp(Participant participant) throws InvalidStateException;
	public boolean relocateDown(Participant participant) throws InvalidStateException;
	public boolean unWin(Participant participant) throws InvalidStateException;
	public boolean win(Participant participant) throws InvalidStateException;

	
	/**
	 * @return number of rounds in tournament
	 */
	public int numRounds();
	
	public List<List<Bracket>> getRoundStructure();
}
