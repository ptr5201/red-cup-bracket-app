package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Handles the layout of the individual bracket components. Contains a central
 * button that expands this control or opens up a dialog when pressed.
 * 
 * @author Jackson Lamp
 */
public class BracketViewSlot extends ViewGroup {

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

	private BracketSlotButton slotButton;
	private Button removeButton;
	private Button demoteButton;
	private Button promoteButton;
	private PaintDrawable background = new PaintDrawable(Color.LTGRAY);

	private int collapsedHeight = 60;
	private int collapsedWidth = 240;

	private int expandedHeight = 120;
	private int expandedWidth = 320;

	private OnExpandedStateChangedListener onExpandedStateChangedListener = null;

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
		this.slotButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BracketViewSlot.this
						.raiseOnExpandedStateChangedEvent(new OnExpandedStateChangedEvent(
								BracketViewSlot.this,
								ExpandedState.EXPANDED_BOTH));
				v.setSelected(true);
				BracketViewSlot.this.removeButton.setVisibility(VISIBLE);
				BracketViewSlot.this.demoteButton.setVisibility(VISIBLE);
				BracketViewSlot.this.promoteButton.setVisibility(VISIBLE);
				BracketViewSlot.this.background.setAlpha(255);
				BracketViewSlot.this.invalidate();
			}
		});
		this.slotButton.setText("abcdefghijklmnopqrstuvwxyz");
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
		this.addView(this.demoteButton);

		// Create the "promote" button
		this.promoteButton = new Button(context);
		this.promoteButton.setText("---->");
		this.promoteButton.setVisibility(INVISIBLE);
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

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		this.slotButton.layout(0, 0, this.collapsedWidth, this.collapsedHeight);
		this.removeButton.layout(this.collapsedWidth + 5, 5,
				this.expandedWidth - 5, this.collapsedHeight - 5);
		this.demoteButton.layout(5, this.collapsedHeight + 5,
				this.expandedHeight - this.collapsedHeight,
				this.expandedHeight - 5);
		this.promoteButton.layout(this.expandedHeight - this.collapsedHeight,
				this.collapsedHeight + 5, this.collapsedWidth - 5,
				this.expandedHeight - 5);
	}
}
