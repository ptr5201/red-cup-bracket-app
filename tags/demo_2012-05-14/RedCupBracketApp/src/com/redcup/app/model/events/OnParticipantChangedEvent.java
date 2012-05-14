package com.redcup.app.model.events;

import com.redcup.app.model.Tournament;

/**
 * Event dispatched whenever a participant is added, removed, or changed in a
 * {@code Tournament}.
 * 
 * @author Jackson Lamp
 */
public class OnParticipantChangedEvent {
	private final Tournament source;

	public OnParticipantChangedEvent(Tournament source) {
		this.source = source;
	}

	public Tournament getSource() {
		return this.source;
	}
}
