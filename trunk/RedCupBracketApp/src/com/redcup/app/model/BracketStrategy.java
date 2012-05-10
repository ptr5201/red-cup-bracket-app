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
	public Bracket lookup(Participant participant);
	
	public void relocateUp(Participant participant) throws InvalidStateException;
	public void relocateDown(Participant participant) throws InvalidStateException;
	public void unWin(Participant participant) throws InvalidStateException;
	public void win(Participant participant) throws InvalidStateException;
	public void promoteParticipantAt(Bracket bracket) throws InvalidStateException;
	public void demoteParticipantAt(Bracket bracket) throws InvalidStateException;
	
	/**
	 * @return number of rounds in tournament
	 */
	public int numRounds();
	
	public List<List<Bracket>> getRoundStructure();
}
