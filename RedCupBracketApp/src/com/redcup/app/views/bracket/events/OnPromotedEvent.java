package com.redcup.app.views.bracket.events;

import com.redcup.app.model.Bracket;

/**
 * Event raised when the user attempts to promote a participant located at the
 * given {@code Bracket}.
 * 
 * @author Jackson Lamp
 */
public class OnPromotedEvent {
	private final Bracket bracket;

	public OnPromotedEvent(Bracket bracket) {
		this.bracket = bracket;
	}

	public Bracket getBracket() {
		return this.bracket;
	}
}
