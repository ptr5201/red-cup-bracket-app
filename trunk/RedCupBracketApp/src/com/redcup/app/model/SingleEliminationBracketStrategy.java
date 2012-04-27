package com.redcup.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SingleEliminationBracketStrategy implements BracketStrategy {
	protected Bracket head;

	public SingleEliminationBracketStrategy(Bracket head) {
		this.head = head;
	}

	public SingleEliminationBracketStrategy(List<Participant> participants) {
		this.head = SingleEliminationBracketFactory
				.createBracketStructure(participants);
	}

	@Override
	public boolean lookup(Participant participant) {
		return lookup(head, participant);
	}

	private boolean lookup(Bracket node, Participant element) {
		if (node == null)
			return false;
		if (element == node.getParticipant())
			return true;
		if (lookup(node.getLeft(), element))
			return true;
		if (lookup(node.getRight(), element))
			return true;
		return false;
	}

	@Override
	public void add(Participant participant) {
		// TODO Auto-generated method stub
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
	 * Returns a list of participants by round Also an example of how to use
	 * roundStructure (should I encapsulate in a RoundStructure class?)
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
		List<List<Bracket>> roundStructure = new ArrayList<List<Bracket>>();
		this.getRoundStructure_Recurse(roundStructure, this.head);
		return roundStructure;
	}

	@Override
	public boolean relocateUp(Participant participant)
			throws InvalidStateException {
		// TODO Auto-generated method stub
		System.out.println("Event occured!");
		return false;
	}

	@Override
	public boolean relocateDown(Participant participant)
			throws InvalidStateException {
		// TODO Auto-generated method stub
		System.out.println("Event occured!");
		return false;
	}

	@Override
	public boolean unWin(Participant participant) throws InvalidStateException {
		// TODO Auto-generated method stub
		System.out.println("Event occured!");
		return false;
	}

	@Override
	public boolean win(Participant participant) throws InvalidStateException {
		// TODO Auto-generated method stub
		System.out.println("Event occured!");
		return false;
	}

}
