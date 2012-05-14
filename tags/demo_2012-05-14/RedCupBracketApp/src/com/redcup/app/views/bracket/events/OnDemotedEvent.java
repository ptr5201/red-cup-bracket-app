package com.redcup.app.views.bracket.events;

import com.redcup.app.model.Bracket;

/**
 * Event raised when the user attempts to demote a participant located at the
 * given {@code Bracket}.
 * 
 * @author Jackson Lamp
 */
public class OnDemotedEvent {
	private final Bracket bracket;

	public OnDemotedEvent(Bracket bracket) {
		this.bracket = bracket;
	}

	public Bracket getBracket() {
		return this.bracket;
	}
}
