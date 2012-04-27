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

	private void getEntrantRound_Recuse(List<Bracket> entrants, Bracket bracket) {
		// Check termination condition
		if (bracket.getRight() == null && bracket.getLeft() == null) {
			entrants.add(bracket);
			return;
		}

		if (bracket.getRight() != null) {
			getEntrantRound_Recuse(entrants, bracket.getRight());
		}

		if (bracket.getLeft() != null) {
			getEntrantRound_Recuse(entrants, bracket.getLeft());
		}
	}

	public List<Bracket> getEntrantRound() {
		List<Bracket> entrantRound = null;
		if (this.head != null) {
			entrantRound = new ArrayList<Bracket>();
			getEntrantRound_Recuse(entrantRound, this.head);
		}
		return entrantRound;
	}

	@Override
	public List<List<Bracket>> getRoundStructure() {
		List<List<Bracket>> roundStructure = new ArrayList<List<Bracket>>();
		// TODO build list2 from tree
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
