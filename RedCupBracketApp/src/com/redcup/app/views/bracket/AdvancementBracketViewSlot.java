package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.redcup.app.R;
import com.redcup.app.model.Bracket;

public class AdvancementBracketViewSlot extends BracketViewSlot {

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
			AdvancementBracketViewSlot.this.updateAdvanceDemoteButtons();
		}
	};

	private OnClickListener promoteButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bracket b = AdvancementBracketViewSlot.this.getBracket();
			if (b != null && b.getParent() != null) {
				b.getParent().setParticipant(b.getParticipant());
			}
			AdvancementBracketViewSlot.this.updateAdvanceDemoteButtons();
		}
	};

	private OnClickListener demoteButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bracket b = AdvancementBracketViewSlot.this.getBracket();
			if (b != null) {
				b.setParticipant(null);
			}
			AdvancementBracketViewSlot.this.updateAdvanceDemoteButtons();
		}
	};

	// Child components
	private BracketSlotButton slotButton;
	private ImageButton removeButton;
	private ImageButton demoteButton;
	private ImageButton promoteButton;
	private PaintDrawable background = new PaintDrawable(Color.LTGRAY);

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
	public AdvancementBracketViewSlot(Context context, AttributeSet attrs,
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
	public AdvancementBracketViewSlot(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize();
	}

	/**
	 * Creates a new {@code SetupBracketViewSlot}.
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
		this.removeButton.setImageResource(R.drawable.red_x);
		this.removeButton.setVisibility(INVISIBLE);
		this.addView(this.removeButton);

		// Create the "demote" button
		this.demoteButton = new ImageButton(context);
		this.demoteButton.setImageResource(R.drawable.orange_arrow);
		this.demoteButton.setEnabled(false);
		this.demoteButton.setVisibility(INVISIBLE);
		this.demoteButton.setOnClickListener(this.demoteButtonListener);
		this.addView(this.demoteButton);

		// Create the "promote" button
		this.promoteButton = new ImageButton(context);
		this.promoteButton.setImageResource(R.drawable.green_checkmark);
		this.promoteButton.setEnabled(false);
		this.promoteButton.setVisibility(INVISIBLE);
		this.promoteButton.setOnClickListener(this.promoteButtonListener);
		this.addView(this.promoteButton);

		// Background drawable
		this.background.setBounds(0, 0, this.getExpandedWidth(),
				this.getCollapsedHeight());
		this.background.setCornerRadius(20);
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

	@Override
	protected void updateInternalComponents() {
		this.updateSlotButton();
		this.updateAdvanceDemoteButtons();
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
		this.getBackground().setBounds(0, 0,
				this.applyScale(this.getCollapsedWidth()),
				this.applyScale(this.getCollapsedWidth()));
	}
}
