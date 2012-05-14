package com.redcup.app.model;

import java.util.ArrayList;
import java.util.Collection;

import com.redcup.app.model.Participant.OnNameChangedEvent;
import com.redcup.app.model.Participant.OnNameChangedListener;

/**
 * Node in binary tree (can probably be subclassed if you need an n-ary tree)
 */
public class Bracket {
	public class OnParticipantChangedEvent {
		private final Bracket bracket;

		public OnParticipantChangedEvent(Bracket bracket) {
			this.bracket = bracket;
		}

		public Bracket getBracket() {
			return bracket;
		}
	}

	public interface OnParticipantChangedListener {
		public void onParticipantChanged(OnParticipantChangedEvent event);
	}

	private OnNameChangedListener onNameChangedListener = new OnNameChangedListener() {
		@Override
		public void onNameChanged(OnNameChangedEvent event) {
			Bracket.this
					.raiseOnParticipantChangedEvent(new OnParticipantChangedEvent(
							Bracket.this));
		}
	};

	private Bracket parent;
	private Participant participant;
	private final Bracket left;
	private final Bracket right;

	// Event listener lists
	private Collection<OnParticipantChangedListener> participantChangedListeners = new ArrayList<OnParticipantChangedListener>();

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

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		if (this.participant != null) {
			this.participant
					.removeOnNameChangedListener(this.onNameChangedListener);
		}

		this.participant = participant;

		if (this.participant != null) {
			this.participant
					.addOnNameChangedListener(this.onNameChangedListener);
		}
		this.raiseOnParticipantChangedEvent(new OnParticipantChangedEvent(this));
	}

	public Bracket getLeft() {
		return left;
	}

	public Bracket getRight() {
		return right;
	}

	public Bracket getParent() {
		return this.parent;
	}

	public void setParent(Bracket parent) {
		this.parent = parent;
	}

	public void addOnParticipantChangedListener(
			OnParticipantChangedListener listener) {
		if (!this.participantChangedListeners.contains(listener)) {
			this.participantChangedListeners.add(listener);
		}
	}

	public void removeOnParticipantChangedListener(
			OnParticipantChangedListener listener) {
		this.participantChangedListeners.remove(listener);
	}

	private void raiseOnParticipantChangedEvent(OnParticipantChangedEvent event) {
		for (OnParticipantChangedListener l : this.participantChangedListeners) {
			l.onParticipantChanged(event);
		}
	}

	public String toString() {
		String ret = "";
		if (participant != null)
			ret += "{ Participant: " + this.participant;
		ret += "\n\tLeft: " + this.left;
		ret += "\n\tRight: " + this.right;
		if (parent != null)
			ret += "\n\tParent: " + this.parent + "}";
		return ret;
	}
}
