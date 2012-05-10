package com.redcup.app.views.bracket.events;

import com.redcup.app.model.Participant;

/**
 * Event raised when the user attempts to remove a participant from a
 * tournament.
 * 
 * @author Jackson Lamp
 */
public class OnParticipantRemovedEvent {
	private final Participant participant;

	public OnParticipantRemovedEvent(Participant participant) {
		this.participant = participant;
	}

	public Participant getParticipant() {
		return this.participant;
	}
}
