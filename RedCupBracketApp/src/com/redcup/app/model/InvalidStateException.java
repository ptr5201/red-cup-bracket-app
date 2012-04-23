package com.redcup.app.model;

import com.redcup.app.model.BracketStrategy.BracketEvent;

public class InvalidStateException extends Exception {
	protected BracketEvent event;
	
	public void setEventCause(BracketEvent event) {
		this.event = event;
	}
	
	public BracketEvent getEventCause() {
		return this.event;
	}
}
