package com.redcup.app.views.bracket;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.redcup.app.model.Bracket;
import com.redcup.app.model.Bracket.OnParticipantChangedEvent;

/**
 * Handles the layout of the individual bracket components. Contains a central
 * button that expands this control or opens up a dialog when pressed.
 * 
 * @author Jackson Lamp
 */
public abstract class BracketViewSlot extends ViewGroup {

	private Bracket.OnParticipantChangedListener participantChangedListener = new Bracket.OnParticipantChangedListener() {
		@Override
		public void onParticipantChanged(OnParticipantChangedEvent event) {
			BracketViewSlot.this.updateInternalComponents();
		}
	};

	/**
	 * The different expanded states this {@code BracketViewSlot} can be in.
	 * 
	 * @author Jackson Lamp
	 */
	public enum ExpandedState {
		COLLAPSED, EXPANDED
	}

	/**
	 * Event that is sent to a listener when the expanded state of this control
	 * changes.
	 * 
	 * @author Jackson Lamp
	 */
	public class OnExpansionEvent {
		private final BracketViewSlot view;
		private final ExpandedState newState;

		/**
		 * Creates a new {@code OnExpansionEvent}.
		 * 
		 * @param view
		 *            the {@code BracketViewSlot} that generated this event.
		 * @param newState
		 *            the new state of the control.
		 */
		public OnExpansionEvent(BracketViewSlot view, ExpandedState newState) {
			this.view = view;
			this.newState = newState;
		}

		/**
		 * Returns the {@code BracketViewSlot} that generated this event.
		 * 
		 * @return the {@code BracketViewSlot} that generated this event.
		 */
		public BracketViewSlot getView() {
			return this.view;
		}

		/**
		 * Returns the new state of the control.
		 * 
		 * @return the new state of the control.
		 */
		public ExpandedState getNewState() {
			return this.newState;
		}
	}

	/**
	 * Interface used to receive {@code OnExpansionEvent}s.
	 * 
	 * @author Jackson Lamp
	 */
	public interface OnExpansionListener {
		public void onExpansion(OnExpansionEvent evt);
	}

	// The collapsed dimensions of this control
	private int collapsedHeight = 70;
	private int collapsedWidth = 260;

	// The expanded dimensions of this control
	private int expandedHeight = 140;
	private int expandedWidth = 330;

	// The scale factor of this control
	private float scale = 1.0f;

	// The used as the model for this control
	private Bracket bracket = null;

	// Used to dispatch expansion/contraction events to listening objects
	private OnExpansionListener onExpansionListener = null;

	/**
	 * Creates a new {@code BracketViewSlot}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 * @param attrs
	 *            the attributes assigned to this {@code View}.
	 * @param defStyle
	 *            the default style to use for this {@code View}.
	 */
	public BracketViewSlot(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initialize();
	}

	/**
	 * Creates a new {@code BracketViewSlot}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 * @param attrs
	 *            the attributes assigned to this {@code View}.
	 */
	public BracketViewSlot(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize();
	}

	/**
	 * Creates a new {@code BracketViewSlot}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 */
	public BracketViewSlot(Context context) {
		super(context);
		this.initialize();
	}

	/**
	 * Performs general initialization services. Should be called by all
	 * constructors.
	 */
	private void initialize() {
	}

	/**
	 * Assigns a listener for {@code OnExpansionEvent}s.
	 * 
	 * @param listener
	 *            the listener to register.
	 */
	public void setOnExpansionListener(OnExpansionListener listener) {
		this.onExpansionListener = listener;
	}

	/**
	 * Returns the currently registered {@code OnExpansionListener}.
	 * 
	 * @return the currently registered {@code OnExpansionListener}.
	 */
	public OnExpansionListener getOnExpansionListener() {
		return this.onExpansionListener;
	}

	/**
	 * Helper method used to dispatch {@code OnExpansionEvent}s generated
	 * locally to the registered {@code OnExpansionListener}
	 * 
	 * @param evt
	 *            the {@code OnExpansionEvent} to dispatch.
	 */
	protected void raiseOnExpansionEvent(OnExpansionEvent evt) {
		if (this.onExpansionListener != null) {
			this.onExpansionListener.onExpansion(evt);
		}
	}

	/**
	 * Sets the amount of horizontal space this control uses when collapsed.
	 * 
	 * @param width
	 *            the amount of horizontal space this control uses when
	 *            collapsed.
	 */
	public void setCollapsedWidth(int width) {
		this.collapsedWidth = width;
		this.invalidate();
	}

	/**
	 * Returns the amount of horizontal space this control uses when collapsed.
	 * 
	 * @return the amount of horizontal space this control uses when collapsed.
	 */
	public int getCollapsedWidth() {
		return this.collapsedWidth;
	}

	/**
	 * Sets the amount of vertical space this control uses when collapsed.
	 * 
	 * @param height
	 *            the amount of vertical space this control uses when collapsed.
	 */
	public void setCollapsedHeight(int height) {
		this.collapsedHeight = height;
		this.invalidate();
	}

	/**
	 * Returns the amount of vertical space this control uses when collapsed.
	 * 
	 * @return the amount of vertical space this control uses when collapsed.
	 */
	public int getCollapsedHeight() {
		return this.collapsedHeight;
	}

	/**
	 * Sets the amount of horizontal space this control uses when expanded.
	 * 
	 * @param width
	 *            the amount of horizontal space this control uses when
	 *            expanded.
	 */
	public void setExpandedWidth(int width) {
		this.expandedWidth = width;
		this.invalidate();
	}

	/**
	 * Returns the amount of horizontal space this control uses when expanded.
	 * 
	 * @return the amount of horizontal space this control uses when expanded.
	 */
	public int getExpandedWidth() {
		return this.expandedWidth;
	}

	/**
	 * Sets the amount of vertical space this control uses when expanded.
	 * 
	 * @param height
	 *            the amount of vertical space this control uses when expanded.
	 */
	public void setExpandedHeight(int height) {
		this.expandedHeight = height;
		this.invalidate();
	}

	/**
	 * Returns the amount of vertical space this control uses when expanded.
	 * 
	 * @return the amount of vertical space this control uses when expanded.
	 */
	public int getExpandedHeight() {
		return this.expandedHeight;
	}

	/**
	 * Returns the collapsed height of this control, adjusted by the scale
	 * factor.
	 * 
	 * @return the collapsed height of this control, adjusted by the scale
	 *         factor.
	 */
	public int getScaledCollapsedHeight() {
		return this.applyScale(this.collapsedHeight);
	}

	/**
	 * Returns the collapsed width of this control, adjusted by the scale
	 * factor.
	 * 
	 * @return the collapsed width of this control, adjusted by the scale
	 *         factor.
	 */
	public int getScaledCollapsedWidth() {
		return this.applyScale(this.collapsedWidth);
	}

	/**
	 * Returns the expanded height of this control, adjusted by the scale
	 * factor.
	 * 
	 * @return the expanded height of this control, adjusted by the scale
	 *         factor.
	 */
	public int getScaledExpandedHeight() {
		return this.applyScale(this.expandedHeight);
	}

	/**
	 * Returns the expanded width of this control, adjusted by the scale factor.
	 * 
	 * @return the expanded width of this control, adjusted by the scale factor.
	 */
	public int getScaledExpandedWidth() {
		return this.applyScale(this.expandedWidth);
	}

	/**
	 * Sets the scale factor of this {@code BracketViewSlot}.
	 * 
	 * @param scale
	 *            the scale factor of this {@code BracketViewSlot}.
	 */
	public void setScale(float scale) {
		this.scale = scale;
		this.invalidate();
	}

	/**
	 * Returns the scale factor of this {@code BracketViewSlot}.
	 * 
	 * @return the scale factor of this {@code BracketViewSlot}.
	 */
	public float getScale() {
		return this.scale;
	}

	/**
	 * Applies the scale factor to the given dimension.
	 * 
	 * @param dimension
	 *            the dimension to scale.
	 * @return the given dimension adjusted by the scale factor.
	 */
	protected int applyScale(float dimension) {
		return Math.round(this.scale * dimension);
	}

	/**
	 * Sets the {@code Bracket} that acts as the model for this
	 * {@code BracketViewSlot}.
	 * 
	 * @param bracket
	 *            the {@code Bracket} that acts as the model for this
	 *            {@code BracketViewSlot}.
	 */
	public void setBracket(Bracket bracket) {
		// Remove listener from old bracket
		if (this.bracket != null) {
			this.bracket
					.removeOnParticipantChangedListener(this.participantChangedListener);
		}

		// Assign new bracket
		this.bracket = bracket;

		// Assign listener to new bracket
		if (this.bracket != null) {
			this.bracket
					.addOnParticipantChangedListener(this.participantChangedListener);
		}

		// Update components' states
		this.updateInternalComponents();
	}

	/**
	 * Returns the {@code Bracket} that acts as the model for this
	 * {@code BracketViewSlot}.
	 * 
	 * @return the {@code Bracket} that acts as the model for this
	 *         {@code BracketViewSlot}.
	 */
	public Bracket getBracket() {
		return this.bracket;
	}

	/**
	 * Refreshes the state of all internal components. Called whenever the state
	 * of the bracket is changed.
	 */
	protected abstract void updateInternalComponents();

	@Override
	protected abstract void onLayout(boolean changed, int l, int t, int r, int b);
}
