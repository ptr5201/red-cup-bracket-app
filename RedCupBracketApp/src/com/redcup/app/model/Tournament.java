package com.redcup.app.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tournament {
	public class ParticipantChangedEvent {
		private final Tournament source;

		public ParticipantChangedEvent(Tournament source) {
			this.source = source;
		}

		public Tournament getSource() {
			return this.source;
		}
	}

	public interface ParticipantChangedListener {
		public void onParticipantListChanged(ParticipantChangedEvent event);
	}

	private BracketStrategy strategy;
	private String name;
	private int id;
	private List<Participant> participants = new ArrayList<Participant>();
	private int participantLimit = -1;

	private List<ParticipantChangedListener> participantChangedListeners = new ArrayList<Tournament.ParticipantChangedListener>();

	public BracketStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(BracketStrategy strategy) {
		this.strategy = strategy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the fixed number of participants that are allowed in a tournament.
	 * Negative numbers remove this restriction.
	 * 
	 * @param limit
	 *            the number of participants that are allowed in this
	 *            tournament.
	 */
	public void setParticipantLimit(int limit) {
		this.participantLimit = limit;
		if (this.participantLimit >= 0) {
			while (this.participants.size() < this.participantLimit) {
				this.participants.add(null);
			}
			while (this.participants.size() > this.participantLimit) {
				this.participants.remove(this.participants.size() - 1);
			}
		} else {
			Iterator<Participant> iter = this.participants.iterator();
			while (iter.hasNext()) {
				Participant p = iter.next();
				if (p == null) {
					iter.remove();
				}
			}
		}
	}

	/**
	 * Returns the fixed number of participants that are allowed in a
	 * tournament. Negative numbers remove this restriction.
	 * 
	 * @return the fixed number of participants that are allowed in a
	 *         tournament. Negative numbers remove this restriction.
	 */
	public int getParticipantLimit() {
		return this.participantLimit;
	}

	// Event listener management
	public void addOnParticipantListChangedListener(
			ParticipantChangedListener listener) {
		this.participantChangedListeners.add(listener);
	}

	public void removeOnParticipantListChangedListener(
			ParticipantChangedListener listener) {
		this.participantChangedListeners.remove(listener);
	}

	private void raiseOnParticipantListChangedEvent(
			ParticipantChangedEvent event) {
		for (ParticipantChangedListener l : this.participantChangedListeners) {
			l.onParticipantListChanged(event);
		}
	}

	// Direct access to participant list
	// TODO: Remove
	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	// Participant management
	public boolean addParticipant(Participant p) {
		// Replace any null elements before appending anything
		for (int i = 0; i < this.participants.size(); i++) {
			if (this.participants.get(i) == null) {
				this.participants.set(i, p);
				this.raiseOnParticipantListChangedEvent(new ParticipantChangedEvent(
						this));
				return true;
			}
		}

		// Check if we are allowed to change the number of participants
		if (this.participantLimit >= 0) {
			// We aren't allowed to add a new participant
			return false;
		} else {
			// Append the participant to the list
			boolean result = this.participants.add(p);
			this.raiseOnParticipantListChangedEvent(new ParticipantChangedEvent(
					this));
			return result;
		}
	}

	public boolean removeParticipant(Participant p) {
		// Check if we are allowed to change the number of participants
		if (this.participantLimit >= 0) {
			// Find the given participant and replace their slot with null
			for (int i = 0; i < this.participants.size(); i++) {
				// Replace with null if equal to p
				if (this.participants.get(i).equals(p)) {
					this.participants.set(i, null);
					this.raiseOnParticipantListChangedEvent(new ParticipantChangedEvent(
							this));
					return true;
				}
			}
			return false;
		} else {
			// Remove the given participant outright
			boolean result = this.participants.remove(p);
			this.raiseOnParticipantListChangedEvent(new ParticipantChangedEvent(
					this));
			return result;
		}
	}

	public Participant removeParticipant(int index) {
		// Don't allow out-of-bounds indices
		if (index >= this.participants.size() || index < 0) {
			return null;
		}

		// Check if we are allowed to change the number of participants
		if (this.participantLimit >= 0) {
			// Find the given participant and replace their slot with null
			Participant p = this.participants.set(index, null);
			this.raiseOnParticipantListChangedEvent(new ParticipantChangedEvent(
					this));
			return p;
		} else {
			// Remove the given participant outright
			Participant p = this.participants.remove(index);
			this.raiseOnParticipantListChangedEvent(new ParticipantChangedEvent(
					this));
			return p;
		}
	}

	public Participant getParticipant(int index) {
		return this.participants.get(index);
	}

	public int getParticipantCount() {
		return this.participants.size();
	}

	// TODO

	// add(Participant)
	// replace(Participant, index)
	// remove(Participant)
	// remove(index)

	// handle / throw fwd events
}
