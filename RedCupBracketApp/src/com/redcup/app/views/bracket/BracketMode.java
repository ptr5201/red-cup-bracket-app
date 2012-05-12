package com.redcup.app.views.bracket;

/**
 * The modes supported by {@code BracketViewLayout}. The modes are as follows:
 * <p>
 * {@code SETUP}: Allows the user to add and remove players.<br>
 * {@code ADVANCEMENT}: Allows the user to declare winners and losers. No
 * participants can be added or removed.<br>
 * {@code READONLY}: Shows the state of a tournament, but does not allow the
 * user to make any changes to it.
 * 
 * @author Jackson Lamp
 */
public enum BracketMode {
	SETUP, ADVANCEMENT, READONLY
}
