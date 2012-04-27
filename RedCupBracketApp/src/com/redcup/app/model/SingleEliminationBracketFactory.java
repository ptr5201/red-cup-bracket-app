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
	 * @param participants list of participants
	 * @return Bracket pointing to head
	 */
	public static Bracket createBracketStructure(List<Participant> participants) {
		// Find number of rounds in tournament
		int roundCount = 1;
		int participantSize = participants.size(); 
		while ((participantSize >>= 1) > 0) {
			roundCount++;
		}

		participantStack = new Stack<Participant>();
		for (Participant participant : participants) {
			participantStack.push(participant);
		}

		System.out.println("Roundcount: " + roundCount);
		Bracket head = buildTree(new Bracket(null, null, null), roundCount);
		participantStack = null;
		return head;
	}

	/**
	 * Recursively builds node tree
	 * @param node
	 * @param roundCount
	 * @return new Bracket
	 */
	private static Bracket buildTree(Bracket node, int roundCount) {
		if (roundCount == 1) {
			return new Bracket(null, null, participantStack.pop());
		}
		node = new Bracket(
				buildTree(new Bracket(null, null, null), --roundCount),
				buildTree(new Bracket(null, null, null), roundCount),
				null
		);
		return node;
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
