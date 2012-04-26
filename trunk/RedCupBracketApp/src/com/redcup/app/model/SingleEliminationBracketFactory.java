package com.redcup.app.model;

import java.util.List;
import java.util.Stack;

/**
 * Factory class; encapsulates logic of creating a bracket
 * @author Matt
 *
 * "You want your bitshifts? I got 'em right here."
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
		int roundCount = 0;
		int participantSize = participants.size(); 
		while ((participantSize >>= 1) > 0) {
			roundCount++;
		}

		participantStack = new Stack<Participant>();
		for (Participant participant : participants) {
			participantStack.push(participant);
		}

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
}
