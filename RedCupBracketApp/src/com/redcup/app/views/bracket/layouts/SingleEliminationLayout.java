package com.redcup.app.views.bracket.layouts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redcup.app.model.Bracket;
import com.redcup.app.model.SingleEliminationBracketStrategy;
import com.redcup.app.views.bracket.BracketView;
import com.redcup.app.views.bracket.BracketViewSlot;
import com.redcup.app.views.bracket.BracketViewSlot.OnExpandedStateChangedEvent;
import com.redcup.app.views.bracket.BracketViewSlot.OnExpandedStateChangedListener;

public class SingleEliminationLayout extends BracketViewLayout {

	private final SingleEliminationBracketStrategy model;
	private final Map<Bracket, BracketViewSlot> views = new HashMap<Bracket, BracketViewSlot>();

	private final List<BracketViewSlot> testViews = new ArrayList<BracketViewSlot>();

	/**
	 * Creates a new {@code SingleEliminationLayout}.
	 * 
	 * @param context
	 *            the {@code BracketView} that this object manages.
	 * @param model
	 *            the {@code SingleEliminationBracketStrategy} that acts as this
	 *            object's internal model.
	 */
	public SingleEliminationLayout(BracketView context,
			SingleEliminationBracketStrategy model) {
		super(context);
		this.model = model;
	}

	@Override
	public void onLayout(boolean changed, int l, int t, int r, int b) {
		// Check to make sure that we need to actually update anything
		if ((changed || this.hasChangedInternally()) && !this.isFrozen()
				&& model != null) {
			// Used to keep track of the bracket objects that have been used;
			// after brackets have been added to the system, they are removed if
			// they are not on this list
			final Set<Bracket> usedBrackets = new HashSet<Bracket>();

			// First column will be evenly spaced according to the given sizing
			// and spacing constraints; remaining columns will be spaced based
			// on the matchups in the previous column
			int vPos = this.getTopMargin();
			int hPos = this.getLeftMargin();

			for (final Bracket bracket : this.model.getRoundStructure().get(0)) {
				// Get the view corresponding to this bracket, creating it if
				// necessary
				BracketViewSlot slot = this.views.get(bracket);
				if (slot != null) {
					slot = new BracketViewSlot(this.getBracketView()
							.getContext());
					this.views.put(bracket, slot);
					this.getBracketView().addView(slot);
				}

				// Update the positioning and sizing of this component
				slot.layout(hPos, vPos, hPos + this.getHorizontalSizing(), vPos
						+ this.getVerticalSizing());

				// Update the vertical positioning variable
				vPos += this.getVerticalSizing() + this.getVerticalSpacing();
			}

			// After all the bracket controls have been added, eliminate any
			// extra controls
			for (final Bracket bracket : this.views.keySet()) {
				if (!usedBrackets.contains(bracket)) {
					BracketViewSlot slot = this.views.remove(bracket);
					if (slot != null) {
						this.getBracketView().removeView(slot);
					}
				}
			}
		}

		// TODO: Remove test code
		// Create slots if necessary
		if (this.getBracketView().getChildCount() == 0) {

			for (int i = 0; i < 8; i++) {
				// Create slot
				BracketViewSlot slot = new BracketViewSlot(this
						.getBracketView().getContext());
				slot.setOnExpandedStateChangedListener(new OnExpandedStateChangedListener() {
					@Override
					public void onExpandedStateChanged(
							OnExpandedStateChangedEvent evt) {
						SingleEliminationLayout.this.getBracketView()
								.clearSelection();
					}
				});
				this.testViews.add(slot);

				// Add to BracketView
				this.getBracketView().addView(slot);
			}
		}

		// Position slots
		int vPos = this.getTopMargin();
		int hPos = this.getLeftMargin();
		for (BracketViewSlot slot : this.testViews) {
			// Update the positioning and sizing of this component
			slot.layout(hPos, vPos, hPos + slot.getExpandedWidth(),
					vPos + slot.getExpandedHeight());

			// Update the vertical positioning variable
			vPos += slot.getExpandedHeight() + this.getVerticalSpacing();
		}
	}

	// @Override
	// public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	// int widthAvailable = MeasureSpec.getSize(widthMeasureSpec);
	// int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	// int heightAvailable = MeasureSpec.getSize(heightMeasureSpec);
	//
	// int width = widthAvailable;
	// int height = heightAvailable;
	//
	// // The number of entrants and rounds in the model
	// int numEntrants = 0;
	// int numRounds = 0;
	//
	// // Ensure model is valid, then get its metrics
	// if (this.model != null) {
	// numEntrants = this.model.getRoundStructure().get(0).size();
	// numRounds = this.model.numRounds();
	// }
	//
	// // Calculate the amount of horizontal space required for the layout
	// if (widthMode != MeasureSpec.EXACTLY) {
	// width = numRounds * this.getHorizontalSizing() + (numRounds - 1)
	// * this.getHorizontalSpacing() + this.getRightMargin()
	// + this.getLeftMargin();
	// }
	//
	// // Limit if in AT_MOST mode
	// if (widthMode == MeasureSpec.AT_MOST) {
	// width = Math.min(width, widthAvailable);
	// }
	//
	// // Calculate the amount of vertical space required for the layout
	// if (heightMode != MeasureSpec.EXACTLY) {
	// height = numEntrants * this.getVerticalSizing() + (numEntrants - 1)
	// * this.getVerticalSpacing() + this.getTopMargin()
	// + this.getBottomMargin();
	// }
	//
	// // Limit if in AT_MOST mode
	// if (heightMode == MeasureSpec.AT_MOST) {
	// height = Math.min(height, heightAvailable);
	// }
	//
	// // Request the measured space
	// this.setMeasuredDimension(width, height);
	// }

	@Override
	public void updateSizeRequirements() {
		// The number of entrants and rounds in the model
		int numEntrants = 8;
		int numRounds = 1;

		// Ensure model is valid, then get its metrics
		if (this.model != null) {
			numEntrants = this.model.getRoundStructure().get(0).size();
			numRounds = this.model.numRounds();
		}

		// Calculate the amount of horizontal space required for the layout
		int width = numRounds * this.getHorizontalSizing() + (numRounds - 1)
				* this.getHorizontalSpacing() + this.getRightMargin()
				+ this.getLeftMargin();

		int height = numEntrants * this.getVerticalSizing() + (numEntrants - 1)
				* this.getVerticalSpacing() + this.getTopMargin()
				+ this.getBottomMargin();

		// Request the measured space
		this.setMeasuredDimension(width, height);
	}

}
