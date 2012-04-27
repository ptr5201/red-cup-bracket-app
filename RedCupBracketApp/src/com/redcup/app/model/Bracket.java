package com.redcup.app.model;

/**
 * Node in binary tree
 * (can probably be subclassed if you need an n-ary tree)
 */
public class Bracket {
	private Participant participant = null;
	private final Bracket left;
	private final Bracket right;
	// Make bidirectional
	
	Bracket(Bracket left, Bracket right) {
		this.left = left;
		this.right = right;
	}
	
	Bracket(Bracket left, Bracket right, Participant participant) {
		this.left = left;
		this.right = right;
		this.participant = participant;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
		// TODO send up event indicating change
	}

	public Bracket getLeft() {
		return left;
	}

	public Bracket getRight() {
		return right;
	}
	
	public String toString() {
		String ret = "{ Participant: " + this.participant;
		ret += "\n\tLeft: " + this.left;
		ret += "\n\tRight: " + this.right + "}";
		return ret;
	}
}
