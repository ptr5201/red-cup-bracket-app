package com.redcup.app.model.events;

/**
 * Listener for {@code OnParticipantChangedEvent}s.
 * 
 * @author Jackson Lamp
 */
public interface OnParticipantChangedListener {
	public void onParticipantListChanged(OnParticipantChangedEvent event);
}
