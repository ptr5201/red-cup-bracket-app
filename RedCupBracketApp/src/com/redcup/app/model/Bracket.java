package com.redcup.app.model;

/**
 * Node in binary tree
 * (can probably be subclassed if you need an n-ary tree)
 */
public class Bracket {
	private Bracket parent;
	private Participant participant;
	private final Bracket left;
	private final Bracket right;	
	// Make bidirectional
	
	Bracket(Bracket left, Bracket right) {
		this.left = left;
		this.right = right;
		this.parent = null;
		this.participant = null;
	}
	
	Bracket(Bracket left, Bracket right, Participant participant) {
		this.left = left;
		this.right = right;
		this.parent = null;
		this.participant = participant;
	}

	public Participant getParticipant() { return participant; }
	public void setParticipant(Participant participant) { this.participant = participant; }

	public Bracket getLeft() { return left; }
	public Bracket getRight() { return right; }
	
	public Bracket getParent() { return this.parent; }
	public void setParent(Bracket parent) { this.parent = parent; }
	
	public String toString() {
		String ret = "";
		if (participant != null) ret += "{ Participant: " + this.participant;
		ret += "\n\tLeft: " + this.left;
		ret += "\n\tRight: " + this.right;
		if (parent != null) ret += "\n\tParent: " + this.parent + "}";
		return ret;
	}
}
