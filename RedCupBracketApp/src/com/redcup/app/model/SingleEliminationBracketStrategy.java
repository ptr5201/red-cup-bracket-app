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
		this.head = BracketFactory.createBracketStructure(participants);
	}

	@Override
	public boolean lookup(Participant participant) {
		return lookup(head, participant);
	}
	
	private boolean lookup(Bracket node, Participant element) {
		if (node == null) return false;
		if (element == node.getParticipant()) return true;
		if (lookup(node.getLeft(), element)) return true;
		if (lookup(node.getRight(), element)) return true;
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
		if (node == null) return 0;
		int lDepth = maxDepth(node.getLeft());
		int rDepth = maxDepth(node.getRight());
		return(Math.max(lDepth, rDepth) +1);
	}
	
	@Override
	public boolean occur(BracketEvent event, Participant participant)
			throws InvalidStateException {
		// TODO Auto-generated method stub
		System.out.println("Event occured!");
		return false;
	}
	
	/**
	 * Returns a list of participants by round
	 * Also an example of how to use roundStructure (should I encapsulate in a RoundStructure class?)
	 */
	public String toString() {
		List<List<Participant>> structure = getRoundStructure();
		ListIterator<List<Participant>> li = structure.listIterator(structure.size());
		
		String ret = "";
		int roundNum = 1;
		while (li.hasPrevious()) {
			ret += String.format("-----Round Number %i-----", roundNum);
			for (Participant participant : (li.previous())) {
				ret += String.format("%s \t", participant.getName());
			}
			ret += "\n";
			roundNum ++;
		}
		return ret;
	}
	
	public List<List<Participant>> getRoundStructure() {
		List<List<Participant>> roundStructure = new ArrayList<List<Participant>>();
		// TODO build list2 from tree
		return roundStructure;
	}

}
