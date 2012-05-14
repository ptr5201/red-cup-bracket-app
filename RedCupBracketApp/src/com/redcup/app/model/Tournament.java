package com.redcup.app.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.redcup.app.model.events.OnTournamentCompletedEvent;
import com.redcup.app.model.events.OnTournamentCompletedListener;
import com.redcup.app.model.events.OnParticipantChangedEvent;
import com.redcup.app.model.events.OnParticipantChangedListener;

public class Tournament {
	private Collection<OnTournamentCompletedListener> tournamentCompletedListenerList = new ArrayList<OnTournamentCompletedListener>();
	
	private final OnTournamentCompletedListener tournamentCompletedListener = new OnTournamentCompletedListener() {
		@Override
		public void tournamentCompleted(OnTournamentCompletedEvent event) {
			Tournament.this.raiseOnTournamentCompletedEvent(event);
		}
	};

	private BracketStrategy strategy;
	private String name;
	private int id;
	private List<Participant> participants = new ArrayList<Participant>();
	private int participantLimit = -1;

	private List<OnParticipantChangedListener> participantChangedListeners = new ArrayList<OnParticipantChangedListener>();

	public BracketStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(BracketStrategy strategy) {
		// Remove listeners from old strategy
		if(this.strategy != null) {
			this.strategy.removeOnTournamentCompletedListener(this.tournamentCompletedListener);
		}
		
		this.strategy = strategy;
		
		// Add listeners to new strategy
		if(this.strategy != null) {
			this.strategy.addOnTournamentCompletedListener(this.tournamentCompletedListener);
		}
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
			OnParticipantChangedListener listener) {
		this.participantChangedListeners.add(listener);
	}

	public void removeOnParticipantListChangedListener(
			OnParticipantChangedListener listener) {
		this.participantChangedListeners.remove(listener);
	}

	private void raiseOnParticipantListChangedEvent(
			OnParticipantChangedEvent event) {
		for (OnParticipantChangedListener l : this.participantChangedListeners) {
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
				this.raiseOnParticipantListChangedEvent(new OnParticipantChangedEvent(
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
			this.raiseOnParticipantListChangedEvent(new OnParticipantChangedEvent(
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
				Participant pIter = this.participants.get(i);
				if (pIter != null && pIter.equals(p)) {
					this.participants.set(i, null);
					this.raiseOnParticipantListChangedEvent(new OnParticipantChangedEvent(
							this));
					return true;
				}
			}
			return false;
		} else {
			// Remove the given participant outright
			boolean result = this.participants.remove(p);
			this.raiseOnParticipantListChangedEvent(new OnParticipantChangedEvent(
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
			this.raiseOnParticipantListChangedEvent(new OnParticipantChangedEvent(
					this));
			return p;
		} else {
			// Remove the given participant outright
			Participant p = this.participants.remove(index);
			this.raiseOnParticipantListChangedEvent(new OnParticipantChangedEvent(
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
	
	public void addOnTournamentCompletedListener(OnTournamentCompletedListener listener) {
		if(!this.tournamentCompletedListenerList.contains(listener)) {
			this.tournamentCompletedListenerList.add(listener);
		}
	}
	
	public void removeOnTournamentCompletedListener(OnTournamentCompletedListener listener) {
		this.tournamentCompletedListenerList.remove(listener);
	}
	
	protected void raiseOnTournamentCompletedEvent(OnTournamentCompletedEvent event) {
		for(OnTournamentCompletedListener l : this.tournamentCompletedListenerList) {
			l.tournamentCompleted(event);
		}
	}

	// TODO

	// add(Participant)
	// replace(Participant, index)
	// remove(Participant)
	// remove(index)

	// handle / throw fwd events
}
