package com.redcup.app.views.bracket;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.redcup.app.R;
import com.redcup.app.model.Bracket;
import com.redcup.app.model.Participant;
import com.redcup.app.views.CorneredButton;
import com.redcup.app.views.bracket.events.OnDemotedEvent;
import com.redcup.app.views.bracket.events.OnDemotedListener;
import com.redcup.app.views.bracket.events.OnParticipantRemovedEvent;
import com.redcup.app.views.bracket.events.OnParticipantRemovedListener;
import com.redcup.app.views.bracket.events.OnPromotedEvent;
import com.redcup.app.views.bracket.events.OnPromotedListener;

public class SetupBracketViewSlot extends BracketViewSlot {

	public class OnEditBracketClickedEvent {
		private final SetupBracketViewSlot source;

		public OnEditBracketClickedEvent(SetupBracketViewSlot source) {
			this.source = source;
		}

		public SetupBracketViewSlot getSource() {
			return this.source;
		}
	}

	public interface OnEditBracketClickedListener {
		public void onEditBracketClicked(OnEditBracketClickedEvent event);
	}

	// Listener Definitions
	private OnClickListener slotButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!SetupBracketViewSlot.this.isSelected()) {
				SetupBracketViewSlot.this
						.raiseOnExpansionEvent(new OnExpansionEvent(
								SetupBracketViewSlot.this,
								ExpandedState.EXPANDED));
			} else {
				SetupBracketViewSlot.this
						.raiseOnEditBracketClickedEvent(new OnEditBracketClickedEvent(
								SetupBracketViewSlot.this));
			}
		}
	};

	private final OnClickListener promoteButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bracket b = SetupBracketViewSlot.this.getBracket();
			OnPromotedEvent event = new OnPromotedEvent(b);
			SetupBracketViewSlot.this.raiseOnPromotedEvent(event);
			SetupBracketViewSlot.this.updateInternalComponents();
		}
	};

	private final OnClickListener demoteButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bracket b = SetupBracketViewSlot.this.getBracket();
			OnDemotedEvent event = new OnDemotedEvent(b);
			SetupBracketViewSlot.this.raiseOnDemotedEvent(event);
			SetupBracketViewSlot.this.updateInternalComponents();
		}
	};

	private final OnClickListener removeButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bracket b = SetupBracketViewSlot.this.getBracket();
			Participant p = b.getParticipant();
			OnParticipantRemovedEvent event = new OnParticipantRemovedEvent(p);
			SetupBracketViewSlot.this.raiseOnParticipantRemovedEvent(event);
		}
	};

	// Bracket mode
	private BracketMode mode = BracketMode.SETUP;

	// Event management
	private final Collection<OnPromotedListener> onPromotedListenerList = new ArrayList<OnPromotedListener>();
	private final Collection<OnDemotedListener> onDemotedListenerList = new ArrayList<OnDemotedListener>();
	private final Collection<OnParticipantRemovedListener> onParticipantRemovedListenerList = new ArrayList<OnParticipantRemovedListener>();
	private final Collection<OnEditBracketClickedListener> onEditBracketClickedListenerList = new ArrayList<OnEditBracketClickedListener>();

	// Child components
	private BracketSlotButton slotButton;
	private CorneredButton removeButton;
	private CorneredButton demoteButton;
	private CorneredButton promoteButton;

	/**
	 * Creates a new {@code SetupBracketViewSlot}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 * @param attrs
	 *            the attributes assigned to this {@code View}.
	 * @param defStyle
	 *            the default style to use for this {@code View}.
	 */
	public SetupBracketViewSlot(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.initialize();
	}

	/**
	 * Creates a new {@code SetupBracketViewSlot}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 * @param attrs
	 *            the attributes assigned to this {@code View}.
	 */
	public SetupBracketViewSlot(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize();
	}

	/**
	 * Creates a new {@code SetupBracketViewSlot}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 */
	public SetupBracketViewSlot(Context context) {
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
		this.removeButton = new CorneredButton(context);
		this.removeButton.setIcon(R.drawable.remove_participant);
		this.removeButton.setVisibility(INVISIBLE);
		this.removeButton.setOnClickListener(this.removeButtonListener);
		// this.removeButton.setScaleType(ScaleType.FIT_CENTER);
		this.addView(this.removeButton, 0);

		// Create the "demote" button
		this.demoteButton = new CorneredButton(context);
		this.demoteButton.setIcon(R.drawable.demote_participant);
		this.demoteButton.setEnabled(false);
		this.demoteButton.setVisibility(INVISIBLE);
		this.demoteButton.setOnClickListener(this.demoteButtonListener);
		// this.demoteButton.setScaleType(ScaleType.FIT_CENTER);
		this.addView(this.demoteButton, 0);

		// Create the "promote" button
		this.promoteButton = new CorneredButton(context);
		this.promoteButton.setIcon(R.drawable.promote_participant);
		this.promoteButton.setEnabled(false);
		this.promoteButton.setVisibility(INVISIBLE);
		this.promoteButton.setOnClickListener(this.promoteButtonListener);
		// this.promoteButton.setScaleType(ScaleType.FIT_CENTER);
		this.addView(this.promoteButton, 0);
	}

	@Override
	public void setSelected(boolean selected) {
		boolean changed = this.isSelected() != selected;
		super.setSelected(selected);
		if (changed) {
			this.slotButton.setSelected(selected);
			this.removeButton
					.setVisibility((this.mode == BracketMode.SETUP && selected) ? VISIBLE
							: INVISIBLE);
			this.promoteButton
					.setVisibility((this.mode == BracketMode.ADVANCEMENT && selected) ? VISIBLE
							: INVISIBLE);
			this.demoteButton
					.setVisibility((this.mode == BracketMode.ADVANCEMENT && selected) ? VISIBLE
							: INVISIBLE);
			this.updateInternalComponents();
			this.invalidate();
		}
	}

	/**
	 * Returns this control to its default state (collapsed, deselected).
	 */
	public void reset() {
		this.setSelected(false);
	}

	/**
	 * Registers the given {@code OnEditBracketClickedListener} with this
	 * {@code SetupBracketViewSlot}.
	 * 
	 * @param listener
	 *            the {@code OnEditBracketClickedListener} to register.
	 */
	public void addOnEditBracketClickedListener(
			OnEditBracketClickedListener listener) {
		if (!this.onEditBracketClickedListenerList.contains(listener)) {
			this.onEditBracketClickedListenerList.add(listener);
		}
	}

	/**
	 * Removes the given {@code OnEditBracketClickedListener} from this
	 * {@code SetupBracketViewSlot}'s list of registered listeners.
	 * 
	 * @param listener
	 *            the {@code OnEditBracketClickedListener} to remove.
	 */
	public void removeOnEditBracketClickedListener(
			OnEditBracketClickedListener listener) {
		this.onEditBracketClickedListenerList.remove(listener);
	}

	/**
	 * Dispatches the given {@code OnEditBracketClickedEvent} to all registered
	 * {@code OnEditBracketClickedListener}s.
	 * 
	 * @param event
	 *            the {@code OnEditBracketClickedEvent} to raise.
	 */
	protected void raiseOnEditBracketClickedEvent(
			OnEditBracketClickedEvent event) {
		for (OnEditBracketClickedListener l : this.onEditBracketClickedListenerList) {
			l.onEditBracketClicked(event);
		}
	}

	/**
	 * Registers the given {@code OnPromotedListener} with this
	 * {@code SetupBracketViewSlot}.
	 * 
	 * @param listener
	 *            the {@code OnPromotedListener} to register.
	 */
	public void addOnPromotedListener(OnPromotedListener listener) {
		if (!this.onPromotedListenerList.contains(listener)) {
			this.onPromotedListenerList.add(listener);
		}
	}

	/**
	 * Removes the given {@code OnPromotedListener} from this
	 * {@code SetupBracketViewSlot}'s list of registered listeners.
	 * 
	 * @param listener
	 *            the {@code OnPromotedListener} to remove.
	 */
	public void removeOnPromotedListener(OnPromotedListener listener) {
		this.onPromotedListenerList.remove(listener);
	}

	/**
	 * Dispatches the given {@code OnPromotedEvent} to all registered
	 * {@code OnPromotedListener}s.
	 * 
	 * @param event
	 *            the {@code OnPromotedEvent} to raise.
	 */
	protected void raiseOnPromotedEvent(OnPromotedEvent event) {
		for (OnPromotedListener l : this.onPromotedListenerList) {
			l.onPromoted(event);
		}
	}

	/**
	 * Registers the given {@code OnDemotedListener} with this
	 * {@code SetupBracketViewSlot}.
	 * 
	 * @param listener
	 *            the {@code OnDemotedListener} to register.
	 */
	public void addOnDemotedListener(OnDemotedListener listener) {
		if (!this.onDemotedListenerList.contains(listener)) {
			this.onDemotedListenerList.add(listener);
		}
	}

	/**
	 * Removes the given {@code OnDemotedListener} from this
	 * {@code SetupBracketViewSlot}'s list of registered listeners.
	 * 
	 * @param listener
	 *            the {@code OnDemotedListener} to remove.
	 */
	public void removeOnDemotedListener(OnDemotedListener listener) {
		this.onDemotedListenerList.remove(listener);
	}

	/**
	 * Dispatches the given {@code OnDemotedEvent} to all registered
	 * {@code OnDemotedListener}s.
	 * 
	 * @param event
	 *            the {@code OnDemotedEvent} to raise.
	 */
	protected void raiseOnDemotedEvent(OnDemotedEvent event) {
		for (OnDemotedListener l : this.onDemotedListenerList) {
			l.onDemoted(event);
		}
	}

	/**
	 * Registers the given {@code OnParticipantRemovedListener} with this
	 * {@code SetupBracketViewSlot}.
	 * 
	 * @param listener
	 *            the {@code OnParticipantRemovedListener} to register.
	 */
	public void addOnParticipantRemovedListener(
			OnParticipantRemovedListener listener) {
		if (!this.onParticipantRemovedListenerList.contains(listener)) {
			this.onParticipantRemovedListenerList.add(listener);
		}
	}

	/**
	 * Removes the given {@code OnParticipantRemovedListener} from this
	 * {@code SetupBracketViewSlot}'s list of registered listeners.
	 * 
	 * @param listener
	 *            the {@code OnParticipantRemovedListener} to remove.
	 */
	public void removeOnParticipantRemovedListener(
			OnParticipantRemovedListener listener) {
		this.onParticipantRemovedListenerList.remove(listener);
	}

	/**
	 * Sets the current mode of this {@code SetupBracketViewSlot}.
	 * 
	 * @param mode
	 *            the mode to set this {@code SetupBracketViewSlot} to.
	 */
	public void setMode(BracketMode mode) {
		this.mode = mode;
		this.updateInternalComponents();
		this.invalidate();
	}

	/**
	 * Returns the current mode of this {@code SetupBracketViewSlot}.
	 * 
	 * @return the current mode of this {@code SetupBracketViewSlot}.
	 */
	public BracketMode getMode() {
		return this.mode;
	}

	/**
	 * Dispatches the given {@code OnParticipantRemovedEvent} to all registered
	 * {@code OnParticipantRemovedListener}s.
	 * 
	 * @param event
	 *            the {@code OnParticipantRemovedEvent} to raise.
	 */
	protected void raiseOnParticipantRemovedEvent(
			OnParticipantRemovedEvent event) {
		for (OnParticipantRemovedListener l : this.onParticipantRemovedListenerList) {
			l.onParticipantRemoved(event);
		}
	}

	protected void updateSlotButton() {
		if (this.getBracket() != null
				&& this.getBracket().getParticipant() != null) {
			this.slotButton.setText(this.getBracket().getParticipant()
					.getName());
			this.slotButton.setEnabled(true);
		} else {
			this.slotButton.setText(null);
			this.slotButton.setEnabled(false);
			this.setSelected(false);
		}
	}

	protected void updateAdvanceDemoteButtons() {
		if (this.getBracket() == null) {
			this.promoteButton.setEnabled(false);
			this.demoteButton.setEnabled(false);
		} else {
			Bracket parent = this.getBracket().getParent();
			Bracket right = this.getBracket().getRight();
			Bracket left = this.getBracket().getLeft();
			Bracket siblingRight = null;
			Bracket siblingLeft = null;
			if (parent != null) {
				siblingRight = parent.getRight();
				siblingLeft = parent.getLeft();
			}

			this.promoteButton
					.setEnabled(parent != null
							&& getBracket().getParticipant() != null
							&& parent.getParticipant() == null
							&& (siblingRight != null && siblingRight
									.getParticipant() != null)
							&& (siblingLeft != null && siblingLeft
									.getParticipant() != null));
			this.demoteButton.setEnabled((right != null || left != null)
					&& getBracket().getParticipant() != null
					&& (parent == null || parent.getParticipant() == null));
		}
	}

	@Override
	protected void updateInternalComponents() {
		this.updateSlotButton();
		this.updateAdvanceDemoteButtons();
	}

	@Override
	public void setScale(float scale) {
		super.setScale(scale);
		this.slotButton.setScale(scale);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// Compute scaled dimensions
		int collapsedWidth = this.applyScale(this.getCollapsedWidth());
		int collapsedHeight = this.applyScale(this.getCollapsedHeight());
		int expandedWidth = this.applyScale(this.getExpandedWidth());
		int expandedHeight = this.applyScale(this.getExpandedHeight());
		int collapsedCenterX = collapsedWidth
				- this.applyScale(this.slotButton.getCornerRadius());
		int collapsedCenterY = collapsedHeight
				- this.applyScale(this.slotButton.getCornerRadius());

		// Apply layouts
		this.slotButton.layout(0, 0, collapsedWidth, collapsedHeight);
		this.removeButton.layout(collapsedCenterX, 0, expandedWidth,
				collapsedHeight);
		this.demoteButton.layout(0, collapsedCenterY,
				(expandedHeight - collapsedHeight), expandedHeight);
		this.promoteButton.layout((expandedHeight - collapsedHeight - Math
				.round(this.promoteButton.getBorderThickness())),
				collapsedCenterY, collapsedWidth, expandedHeight);

		// Apply corners to CornerButtons
		float cornerRadius = applyScale(20);
		this.removeButton.setCorners(0, cornerRadius, cornerRadius, 0);
		this.demoteButton.setCorners(0, 0, 0, cornerRadius);
		this.promoteButton.setCorners(0, 0, cornerRadius, 0);

		// Apply insets to CornerButtons
		float padding = applyScale(20);
		this.removeButton.setInsets(
				collapsedWidth - collapsedCenterX + padding, padding, padding,
				padding);
		this.demoteButton.setInsets(padding, collapsedHeight - collapsedCenterY
				+ padding, padding, padding);
		this.promoteButton.setInsets(padding, collapsedHeight
				- collapsedCenterY + padding, padding, padding);
	}
}
