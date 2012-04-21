package com.redcup.app.model;

/**
 * Node in binary tree
 * (can probably be subclassed if you need an n-ary tree)
 */
public class Bracket {
	private Participant participant = null;
	private Bracket left = null;
	private Bracket right = null;
	
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
	}

	public Bracket getLeft() {
		return left;
	}

	public void setLeft(Bracket left) {
		this.left = left;
	}

	public Bracket getRight() {
		return right;
	}

	public void setRight(Bracket right) {
		this.right = right;
	}
}
