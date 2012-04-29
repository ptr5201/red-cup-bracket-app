package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.redcup.app.model.Bracket;
import com.redcup.app.model.Participant;
import com.redcup.app.model.Bracket.OnParticipantChangedEvent;

/**
 * Handles the layout of the individual bracket components. Contains a central
 * button that expands this control or opens up a dialog when pressed.
 * 
 * @author Jackson Lamp
 */
public class BracketViewSlot extends ViewGroup {

	private Bracket.OnParticipantChangedListener participantChangedListener = new Bracket.OnParticipantChangedListener() {
		@Override
		public void onParticipantChanged(OnParticipantChangedEvent event) {
			BracketViewSlot.this.updateText_ParticipantName();
			BracketViewSlot.this.updateButtons_AdvanceDemote();
		}
	};

	private OnClickListener slotButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BracketViewSlot.this
					.raiseOnExpandedStateChangedEvent(new OnExpandedStateChangedEvent(
							BracketViewSlot.this, ExpandedState.EXPANDED_BOTH));
			v.setSelected(true);
			BracketViewSlot.this.removeButton.setVisibility(VISIBLE);
			BracketViewSlot.this.demoteButton.setVisibility(VISIBLE);
			BracketViewSlot.this.promoteButton.setVisibility(VISIBLE);
			BracketViewSlot.this.background.setAlpha(255);
			BracketViewSlot.this.updateButtons_AdvanceDemote();
		}
	};

	private OnClickListener promoteButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bracket b = BracketViewSlot.this.getBracket();
			if (b != null && b.getParent() != null) {
				b.getParent().setParticipant(b.getParticipant());
			}
			BracketViewSlot.this.updateButtons_AdvanceDemote();
		}
	};

	private OnClickListener demoteButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bracket b = BracketViewSlot.this.getBracket();
			if (b != null) {
				b.setParticipant(null);
			}
			BracketViewSlot.this.updateButtons_AdvanceDemote();
		}
	};

	/**
	 * The different expanded states this {@code BracketViewSlot} can be in.
	 * 
	 * @author Jackson Lamp
	 */
	public enum ExpandedState {
		COLLAPSED, EXPANDED_HORIZONTAL, EXPANDED_VERTICAL, EXPANDED_BOTH
	}

	/**
	 * Event that is sent to a listener when the expanded state of this control
	 * changes.
	 * 
	 * @author Jackson Lamp
	 */
	public class OnExpandedStateChangedEvent {
		private final BracketViewSlot view;
		private final ExpandedState newState;

		/**
		 * Creates a new {@code OnExpandedStateChangedEvent}.
		 * 
		 * @param view
		 *            the {@code BracketViewSlot} that generated this event.
		 * @param newState
		 *            the new state of the control.
		 */
		public OnExpandedStateChangedEvent(BracketViewSlot view,
				ExpandedState newState) {
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
	 * Interface used to receive {@code OnExpandedStateChangedEvent}s.
	 * 
	 * @author Jackson Lamp
	 */
	public interface OnExpandedStateChangedListener {
		public void onExpandedStateChanged(OnExpandedStateChangedEvent evt);
	}

	// Child components
	private BracketSlotButton slotButton;
	private Button removeButton;
	private Button demoteButton;
	private Button promoteButton;
	private PaintDrawable background = new PaintDrawable(Color.LTGRAY);

	// The collapsed dimensions of this control
	private int collapsedHeight = 70;
	private int collapsedWidth = 240;

	// The expanded dimensions of this control
	private int expandedHeight = 140;
	private int expandedWidth = 330;

	// The scale factor of this control
	private float scale = 1.0f;

	// Used to dispatch expansion/contraction events to listening objects
	private OnExpandedStateChangedListener onExpandedStateChangedListener = null;

	// The used as the model for this control
	private Bracket bracket = null;

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
		Context context = this.getContext();

		// Create the slot expansion/editing button
		this.slotButton = new BracketSlotButton(context);
		this.slotButton.setOnClickListener(this.slotButtonListener);
		this.addView(slotButton);

		// Create the "remove" button
		this.removeButton = new Button(context);
		this.removeButton.setText("X");
		this.removeButton.setVisibility(INVISIBLE);
		this.addView(this.removeButton);

		// Create the "demote" button
		this.demoteButton = new Button(context);
		this.demoteButton.setText("<-");
		this.demoteButton.setEnabled(false);
		this.demoteButton.setVisibility(INVISIBLE);
		this.demoteButton.setOnClickListener(this.demoteButtonListener);
		this.addView(this.demoteButton);

		// Create the "promote" button
		this.promoteButton = new Button(context);
		this.promoteButton.setText("---->");
		this.promoteButton.setEnabled(false);
		this.promoteButton.setVisibility(INVISIBLE);
		this.promoteButton.setOnClickListener(this.promoteButtonListener);
		this.addView(this.promoteButton);

		// Background drawable
		this.background.setBounds(0, 0, this.expandedWidth,
				this.collapsedHeight);
		this.background.setCornerRadius(20);
		this.background.setAlpha(0);
		this.setBackgroundDrawable(this.background);
	}

	/**
	 * Assigns a listener for {@code OnExpandedStateChangedEvent}s.
	 * 
	 * @param listener
	 *            the listener to register.
	 */
	public void setOnExpandedStateChangedListener(
			OnExpandedStateChangedListener listener) {
		this.onExpandedStateChangedListener = listener;
	}

	/**
	 * Returns the currently registered {@code OnExpandedStateChangedListener}.
	 * 
	 * @return the currently registered {@code OnExpandedStateChangedListener}.
	 */
	public OnExpandedStateChangedListener getOnExpandedStateChangedListener() {
		return this.onExpandedStateChangedListener;
	}

	/**
	 * Helper method used to dispatch {@code OnExpandedStateChangedEvent}s
	 * generated locally to the registered
	 * {@code OnExpandedStateChangedListener}
	 * 
	 * @param evt
	 *            the {@code OnExpandedStateChangedEvent} to dispatch.
	 */
	protected void raiseOnExpandedStateChangedEvent(
			OnExpandedStateChangedEvent evt) {
		if (this.onExpandedStateChangedListener != null) {
			this.onExpandedStateChangedListener.onExpandedStateChanged(evt);
		}
	}

	/**
	 * Returns this control to its default state (collapsed, deselected).
	 */
	public void reset() {
		if (this.slotButton != null) {
			this.slotButton.setSelected(false);
		}
		if (this.removeButton != null) {
			this.removeButton.setVisibility(INVISIBLE);
			this.demoteButton.setVisibility(INVISIBLE);
			this.promoteButton.setVisibility(INVISIBLE);
			this.background.setAlpha(0);
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
	protected int applyScale(int dimension) {
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

		// Update text
		this.updateText_ParticipantName();

		// Update button statuses
		this.updateButtons_AdvanceDemote();
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

	protected void updateText_ParticipantName() {
		if (this.bracket != null && this.bracket.getParticipant() != null) {
			this.slotButton.setText(this.bracket.getParticipant().getName());
		} else {
			this.slotButton.setText(null);
		}
	}

	protected void updateButtons_AdvanceDemote() {
		if (this.bracket == null) {
			this.promoteButton.setEnabled(false);
			this.demoteButton.setEnabled(false);
		} else {
			this.promoteButton.setEnabled(this.bracket.getParent() != null
					&& this.bracket.getParticipant() != null
					&& this.bracket.getParent().getParticipant() == null);
			this.demoteButton
					.setEnabled((this.bracket.getRight() != null || this.bracket
							.getLeft() != null)
							&& this.bracket.getParticipant() != null);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		this.slotButton.layout(0, 0, this.applyScale(this.collapsedWidth),
				this.applyScale(this.collapsedHeight));
		this.removeButton.layout(this.applyScale(this.collapsedWidth + 5),
				this.applyScale(5), this.applyScale(this.expandedWidth - 5),
				this.applyScale(this.collapsedHeight - 5));
		this.demoteButton.layout(this.applyScale(5),
				this.applyScale(this.collapsedHeight + 5),
				this.applyScale(this.expandedHeight - this.collapsedHeight),
				this.applyScale(this.expandedHeight - 5));
		this.promoteButton.layout(
				this.applyScale(this.expandedHeight - this.collapsedHeight),
				this.applyScale(this.collapsedHeight + 5),
				this.applyScale(this.collapsedWidth - 5),
				this.applyScale(this.expandedHeight - 5));
		this.getBackground().setBounds(0, 0,
				this.applyScale(this.collapsedWidth),
				this.applyScale(this.collapsedWidth));
	}
}
