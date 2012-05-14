package com.redcup.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.util.Log;

public class SingleEliminationBracketStrategy implements BracketStrategy {
	protected Bracket head;

	public SingleEliminationBracketStrategy(Bracket head) {
		this.head = head;
	}

	public SingleEliminationBracketStrategy(List<Participant> participants) {
		this.head = SingleEliminationBracketFactory.createBracketStructure(participants);
	}
	
	public SingleEliminationBracketStrategy() {
		this.head = null;
	}

	@Override
	public Bracket lookup(Participant participant) {
		return lookupRecurse(head, participant);
	}

	private Bracket lookupRecurse(Bracket bracket, Participant element) {
		if (element == bracket.getParticipant())
			return bracket;

		Bracket ret;
		ret = lookupRecurse(bracket.getLeft(), element);
		if (ret != null) return ret;
		ret = lookupRecurse(bracket.getRight(), element);
		if (ret != null) return ret;
		return null;
	}

	@Override
	public int numRounds() {
		return maxDepth(this.head);
	}

	private int maxDepth(Bracket node) {
		if (node == null)
			return 0;
		int lDepth = maxDepth(node.getLeft());
		int rDepth = maxDepth(node.getRight());
		return (Math.max(lDepth, rDepth) + 1);
	}

	/**
	 * Prints a list of participants by round
	 */
	public String toString() {
		List<List<Bracket>> structure = getRoundStructure();
		ListIterator<List<Bracket>> li = structure.listIterator(structure
				.size());

		String ret = "";
		int roundNum = 1;
		while (li.hasPrevious()) {
			ret += String.format("-----Round Number %i-----", roundNum);
			for (Bracket bracket : (li.previous())) {
				ret += String.format("%s \t", bracket.getParticipant()
						.getName());
			}
			ret += "\n";
			roundNum++;
		}
		return ret;
	}

	/**
	 * Recursive helper function used to compose the generic bracket structure.
	 * 
	 * @param structure
	 *            the structure being modified. The outer list represents the
	 *            rounds while the inner list contains the rounds.
	 * @param bracket
	 *            the {@code Bracket} being handled on an individual iteration.
	 * @return the next round number to be computed.
	 */
	private int getRoundStructure_Recurse(List<List<Bracket>> structure,
			Bracket bracket) {
		int level = 0;

		// Check termination condition
		if (bracket.getRight() == null && bracket.getLeft() == null) {
			// Ensure that the level in question exists in the structure
			while (structure.size() <= level) {
				structure.add(new ArrayList<Bracket>());
			}

			// Add the current bracket and return
			structure.get(level).add(bracket);
			return level + 1;
		}

		// Go down the right branch
		if (bracket.getRight() != null) {
			int levelRight = getRoundStructure_Recurse(structure,
					bracket.getRight());
			level = Math.max(levelRight, level);
		}

		// Go down the left branch
		if (bracket.getLeft() != null) {
			int levelLeft = getRoundStructure_Recurse(structure,
					bracket.getLeft());
			level = Math.max(levelLeft, level);
		}

		// Ensure that the level in question exists in the structure
		while (structure.size() <= level) {
			structure.add(new ArrayList<Bracket>());
		}

		// Add the current bracket and return
		structure.get(level).add(bracket);
		return level + 1;
	}

	@Override
	public List<List<Bracket>> getRoundStructure() {
		List<List<Bracket>> roundStructure = null;
		if (this.head != null) {
			roundStructure = new ArrayList<List<Bracket>>();
			this.getRoundStructure_Recurse(roundStructure, this.head);
		}
		return roundStructure;
	}

	@Override
	public void relocateUp(Participant participant)	throws InvalidStateException {
		Log.v("Strategy", "RelocateUp event occured!");
		Bracket start = lookup(participant);
		if (start == null) throw new InvalidStateException();
		Bracket parent = lookup(participant).getParent();
		if (participant == parent.getLeft().getParticipant()) {
			start.setParticipant(parent.getRight().getParticipant());
			parent.getRight().setParticipant(participant);
		} else if (participant == parent.getRight().getParticipant()) {
			Bracket gParent = parent.getParent();
			start.setParticipant(gParent.getRight().getLeft().getParticipant());
			gParent.getRight().getLeft().setParticipant(participant);
		} 
	}

	@Override
	public void relocateDown(Participant participant) throws InvalidStateException {
		Log.v("Strategy", "RelocateDown event occured!");
		Bracket start = lookup(participant);
		if (start == null) throw new InvalidStateException();
		Bracket parent = lookup(participant).getParent();
		if (participant == parent.getLeft().getParticipant()) {
			Bracket gParent = parent.getParent();
			start.setParticipant(gParent.getLeft().getRight().getParticipant());
			gParent.getLeft().getRight().setParticipant(participant);
		} else if (participant == parent.getRight().getParticipant()) {
			start.setParticipant(parent.getLeft().getParticipant());
			parent.getLeft().setParticipant(participant);
		} 
	}

	@Override
	public void unWin(Participant participant) throws InvalidStateException {
		Log.v("Strategy", "unWin event occured!");
		Bracket bracket = this.lookup(participant);
		// Ensure participant is a player in the tournament & has played at least one game
		if (bracket == null || bracket.getLeft() == null || bracket.getRight() == null) throw new InvalidStateException();		
		if (bracket.getLeft().getParticipant() == participant
		 || bracket.getRight().getParticipant() == participant) {	
			bracket.setParticipant(null);
			return;
		}
		// Something wrong has occurred
		throw new InvalidStateException();
	}

	@Override
	public void win(Participant participant) throws InvalidStateException {
		Log.v("Strategy", "Win event occured!");
		//Bracket winBracket = parentLookupRecurse(this.head, participant);
		Bracket winBracket = lookup(participant).getParent();
		// Participant is at head or someone has already won the game 
		if (winBracket == null || winBracket.getParticipant() != null) throw new InvalidStateException();
		winBracket.setParticipant(participant);
	}
	
	@Override
	public void promoteParticipantAt(Bracket bracket) throws InvalidStateException {
		// Check for valid state
		if (bracket.getParent() == null || bracket.getParticipant() == null) {
			// Bracket either doesn't have a participant to advance or is in the
			// last round
			throw new InvalidStateException();
		}

		// Advance participant in given slot
		bracket.getParent().setParticipant(bracket.getParticipant());
	}

	@Override
	public void demoteParticipantAt(Bracket bracket) throws InvalidStateException {
		// Check for valid state
		if (bracket.getLeft() == null || bracket.getRight() == null
				|| bracket.getParticipant() == null) {
			// Bracket either doesn't have a participant to demote or is in the
			// first round
			throw new InvalidStateException();
		}

		// Demote participant in given slot
		bracket.setParticipant(null);
	}
	
}
