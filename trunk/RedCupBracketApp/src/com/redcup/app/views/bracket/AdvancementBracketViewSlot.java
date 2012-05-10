package com.redcup.app.views.bracket;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;

import com.redcup.app.R;
import com.redcup.app.model.Bracket;
import com.redcup.app.model.Participant;
import com.redcup.app.views.bracket.events.OnDemotedEvent;
import com.redcup.app.views.bracket.events.OnDemotedListener;
import com.redcup.app.views.bracket.events.OnParticipantRemovedEvent;
import com.redcup.app.views.bracket.events.OnParticipantRemovedListener;
import com.redcup.app.views.bracket.events.OnPromotedEvent;
import com.redcup.app.views.bracket.events.OnPromotedListener;

public class AdvancementBracketViewSlot extends BracketViewSlot {

	// Listener Definitions
	private OnClickListener slotButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			AdvancementBracketViewSlot.this
					.raiseOnExpandedStateChangedEvent(new OnExpandedStateChangedEvent(
							AdvancementBracketViewSlot.this,
							ExpandedState.EXPANDED_BOTH));
			v.setSelected(true);
			AdvancementBracketViewSlot.this.removeButton.setVisibility(VISIBLE);
			AdvancementBracketViewSlot.this.demoteButton.setVisibility(VISIBLE);
			AdvancementBracketViewSlot.this.promoteButton
					.setVisibility(VISIBLE);
			AdvancementBracketViewSlot.this.background.setAlpha(255);
			AdvancementBracketViewSlot.this.updateInternalComponents();
		}
	};

	private final OnClickListener promoteButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bracket b = AdvancementBracketViewSlot.this.getBracket();
			OnPromotedEvent event = new OnPromotedEvent(b);
			AdvancementBracketViewSlot.this.raiseOnPromotedEvent(event);
			AdvancementBracketViewSlot.this.updateInternalComponents();
		}
	};

	private final OnClickListener demoteButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bracket b = AdvancementBracketViewSlot.this.getBracket();
			OnDemotedEvent event = new OnDemotedEvent(b);
			AdvancementBracketViewSlot.this.raiseOnDemotedEvent(event);
			AdvancementBracketViewSlot.this.updateInternalComponents();
		}
	};

	private final OnClickListener removeButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bracket b = AdvancementBracketViewSlot.this.getBracket();
			Participant p = b.getParticipant();
			OnParticipantRemovedEvent event = new OnParticipantRemovedEvent(p);
			AdvancementBracketViewSlot.this
					.raiseOnParticipantRemovedEvent(event);
		}
	};

	// Event management
	private final Collection<OnPromotedListener> onPromotedListenerList = new ArrayList<OnPromotedListener>();
	private final Collection<OnDemotedListener> onDemotedListenerList = new ArrayList<OnDemotedListener>();
	private final Collection<OnParticipantRemovedListener> onParticipantRemovedListenerList = new ArrayList<OnParticipantRemovedListener>();

	// Child components
	private BracketSlotButton slotButton;
	private ImageButton removeButton;
	private ImageButton demoteButton;
	private ImageButton promoteButton;
	private PaintDrawable background = new PaintDrawable(Color.LTGRAY);

	/**
	 * Creates a new {@code AdvancementBracketViewSlot}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 * @param attrs
	 *            the attributes assigned to this {@code View}.
	 * @param defStyle
	 *            the default style to use for this {@code View}.
	 */
	public AdvancementBracketViewSlot(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.initialize();
	}

	/**
	 * Creates a new {@code AdvancementBracketViewSlot}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 * @param attrs
	 *            the attributes assigned to this {@code View}.
	 */
	public AdvancementBracketViewSlot(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize();
	}

	/**
	 * Creates a new {@code AdvancementBracketViewSlot}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 */
	public AdvancementBracketViewSlot(Context context) {
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
		this.removeButton = new ImageButton(context);
		this.removeButton.setImageResource(R.drawable.remove_participant);
		this.removeButton.setVisibility(INVISIBLE);
		this.removeButton.setOnClickListener(this.removeButtonListener);
		this.removeButton.setScaleType(ScaleType.FIT_CENTER);
		this.addView(this.removeButton);

		// Create the "demote" button
		this.demoteButton = new ImageButton(context);
		this.demoteButton.setImageResource(R.drawable.demote_participant);
		this.demoteButton.setEnabled(false);
		this.demoteButton.setVisibility(INVISIBLE);
		this.demoteButton.setOnClickListener(this.demoteButtonListener);
		this.demoteButton.setScaleType(ScaleType.FIT_CENTER);
		this.addView(this.demoteButton);

		// Create the "promote" button
		this.promoteButton = new ImageButton(context);
		this.promoteButton.setImageResource(R.drawable.promote_participant);
		this.promoteButton.setEnabled(false);
		this.promoteButton.setVisibility(INVISIBLE);
		this.promoteButton.setOnClickListener(this.promoteButtonListener);
		this.promoteButton.setScaleType(ScaleType.FIT_CENTER);
		this.addView(this.promoteButton);

		// Background drawable
		this.background.setBounds(0, 0, this.getExpandedWidth(),
				this.getCollapsedHeight());
		this.background.setCornerRadius(this.applyScale(20));
		this.background.setAlpha(0);
		this.setBackgroundDrawable(this.background);
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
		this.invalidate();
	}

	/**
	 * Registers the given {@code OnPromotedListener} with this
	 * {@code AdvancementBracketViewSlot}.
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
	 * {@code AdvancementBracketViewSlot}'s list of registered listeners.
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
	 * {@code AdvancementBracketViewSlot}.
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
	 * {@code AdvancementBracketViewSlot}'s list of registered listeners.
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
	 * {@code AdvancementBracketViewSlot}.
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
	 * {@code AdvancementBracketViewSlot}'s list of registered listeners.
	 * 
	 * @param listener
	 *            the {@code OnParticipantRemovedListener} to remove.
	 */
	public void removeOnParticipantRemovedListener(
			OnParticipantRemovedListener listener) {
		this.onParticipantRemovedListenerList.remove(listener);
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
			this.reset();
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
		this.slotButton.layout(0, 0, this.applyScale(this.getCollapsedWidth()),
				this.applyScale(this.getCollapsedHeight()));
		this.removeButton.layout(this.applyScale(this.getCollapsedWidth() + 5),
				this.applyScale(5),
				this.applyScale(this.getExpandedWidth() - 5),
				this.applyScale(this.getCollapsedHeight() - 5));
		this.demoteButton.layout(
				this.applyScale(5),
				this.applyScale(this.getCollapsedHeight() + 5),
				this.applyScale(this.getExpandedHeight()
						- this.getCollapsedHeight()),
				this.applyScale(this.getExpandedHeight() - 5));
		this.promoteButton.layout(
				this.applyScale(this.getExpandedHeight()
						- this.getCollapsedHeight()),
				this.applyScale(this.getCollapsedHeight() + 5),
				this.applyScale(this.getCollapsedWidth() - 5),
				this.applyScale(this.getExpandedHeight() - 5));

		this.background.setBounds(0, 0,
				this.applyScale(this.getCollapsedWidth()),
				this.applyScale(this.getCollapsedWidth()));
		this.background.setCornerRadius(this.applyScale(20));
	}
}
