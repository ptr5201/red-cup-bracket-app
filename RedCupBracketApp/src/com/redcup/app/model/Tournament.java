package com.redcup.app.model;

import java.util.ArrayList;
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
	 *            the number of participatns that are allowed in this
	 *            tournament.
	 */
	public void setParticipantLimit(int limit) {
		this.participantLimit = limit;
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
		boolean result = this.participants.add(p);
		this.raiseOnParticipantListChangedEvent(new ParticipantChangedEvent(
				this));
		return result;
	}

	public boolean removeParticipant(Participant p) {
		boolean result = this.participants.remove(p);
		this.raiseOnParticipantListChangedEvent(new ParticipantChangedEvent(
				this));
		return result;
	}

	public Participant removeParticipant(int index) {
		Participant p = this.participants.remove(index);
		this.raiseOnParticipantListChangedEvent(new ParticipantChangedEvent(
				this));
		return p;
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
