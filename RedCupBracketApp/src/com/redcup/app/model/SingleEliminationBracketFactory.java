package com.redcup.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Factory class; encapsulates logic of creating a bracket
 * @author Matt
 */
public class SingleEliminationBracketFactory {
	private SingleEliminationBracketFactory() {};
	private static Stack<Participant> participantStack;

	/**
	 * Factory method which creates bracket structures
	 * 
	 * @param participants
	 *            list of participants
	 * @return Bracket pointing to head
	 */
	public static Bracket createBracketStructure(List<Participant> participants) {
		Bracket head = null;
		
		// Prepare the entry round brackets
		List<Bracket> entryBrackets = new ArrayList<Bracket>();
		for(Participant p : participants) {
			entryBrackets.add(new Bracket(null, null, p));
		}
		
		// Build the bracket structure by recursively forming matchups
		head = createBracketStructure_Recurse_BiasRight(entryBrackets);
		
		return head;
	}
	
	/**
	 * Recursive helper function. Builds a list of brackets for a single round,
	 * with each bracket linked to a previous bracket. If a bye is required in
	 * this round, it is given to the bracket in the extreme left (hence this
	 * function is "right biased"). If this occurs, the bias is switched for the
	 * next round to prevent multiple byes being assigned to the same
	 * participant.
	 * 
	 * @param prevRound
	 *            the list of brackets in the previous round.
	 * @return a reference to the head element of the bracket structure.
	 */
	private static Bracket createBracketStructure_Recurse_BiasRight(List<Bracket> prevRound) {
		boolean roundHasByes = false;
		
		// Check termination conditions
		if(prevRound.size() == 0) {
			// No brackets in previous round
			return null;
		} else if(prevRound.size() == 1) {
			// Last round was the "head" round
			return prevRound.get(0);
		}
		
		// Compose the brackets in this round
		List<Bracket> round = new ArrayList<Bracket>();
		for(int i = 0; i + 1 < prevRound.size(); i += 2) {
			Bracket right = prevRound.get(i);
			Bracket left = prevRound.get(i + 1);
			round.add(new Bracket(left, right));
		}
		
		// Give remaining players (if any) a bye
		for(int i = round.size() * 2; i < prevRound.size(); i++) {
			roundHasByes = true;
			round.add(prevRound.get(i));
		}
		
		// Recurse, swapping bias if necessary
		if(roundHasByes) {
			return createBracketStructure_Recurse_BiasLeft(round);
		}
		return createBracketStructure_Recurse_BiasRight(round);
	}
	
	/**
	 * Recursive helper function. Builds a list of brackets for a single round,
	 * with each bracket linked to a previous bracket. If a bye is required in
	 * this round, it is given to the bracket in the extreme right (hence this
	 * function is "left biased"). If this occurs, the bias is switched for the
	 * next round to prevent multiple byes being assigned to the same
	 * participant.
	 * 
	 * @param prevRound
	 *            the list of brackets in the previous round.
	 * @return a reference to the head element of the bracket structure.
	 */
	private static Bracket createBracketStructure_Recurse_BiasLeft(List<Bracket> prevRound) {
		boolean roundHasByes = false;
		
		// Check termination conditions
		if(prevRound.size() == 0) {
			// No brackets in previous round
			return null;
		} else if(prevRound.size() == 1) {
			// Last round was the "head" round
			return prevRound.get(0);
		}
		
		// Compose the brackets in this round
		List<Bracket> round = new ArrayList<Bracket>();
		for(int i = prevRound.size() - 2; i >= 0; i -= 2) {
			Bracket right = prevRound.get(i);
			Bracket left = prevRound.get(i + 1);
			round.add(0, new Bracket(left, right));
		}
		
		// Give remaining players (if any) a bye
		for(int i = prevRound.size() - round.size() * 2 - 1; i >= 0; i--) {
			roundHasByes = true;
			round.add(0, prevRound.get(i));
		}

		// Recurse, swapping bias if necessary
		if(roundHasByes) {
			return createBracketStructure_Recurse_BiasRight(round);
		}
		return createBracketStructure_Recurse_BiasLeft(round);
	}
	
	public static void main(String args[]) {
		List<Participant> entrants = new ArrayList<Participant>();
		entrants.add(new Participant("P1"));
		entrants.add(new Participant("P2"));
		entrants.add(new Participant("P3"));
		entrants.add(new Participant("P4"));
//		entrants.add(new Participant("P5"));
//		entrants.add(new Participant("P6"));
		
		Bracket head = createBracketStructure(entrants);
		System.out.println(head);
	}
}
