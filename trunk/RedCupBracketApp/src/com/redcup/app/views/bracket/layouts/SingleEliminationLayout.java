package com.redcup.app.views.bracket.layouts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redcup.app.model.Bracket;
import com.redcup.app.model.SingleEliminationBracketStrategy;
import com.redcup.app.views.bracket.BracketView;
import com.redcup.app.views.bracket.BracketViewSlot;
import com.redcup.app.views.bracket.BracketViewSlot.OnExpandedStateChangedEvent;
import com.redcup.app.views.bracket.BracketViewSlot.OnExpandedStateChangedListener;

/**
 * Implementation of {@code BracketViewLayout} that is used to visually
 * represent a single elimination tournament.
 * 
 * @author Jackson Lamp
 */
public class SingleEliminationLayout extends BracketViewLayout {

	private final SingleEliminationBracketStrategy model;
	private final Map<Bracket, BracketViewSlot> views = new HashMap<Bracket, BracketViewSlot>();

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
	public void onLayout(boolean changed) {
		// Check to make sure that we need to actually update anything
		if ((changed || this.hasChangedInternally()) && !this.isFrozen()
				&& model != null) {
			int requiredHeight = 0;
			int requiredWidth = 0;

			// Used to keep track of the bracket objects that have been used;
			// after brackets have been added to the system, they are removed if
			// they are not on this list
			final Set<Bracket> usedBrackets = new HashSet<Bracket>();

			// First column will be evenly spaced according to the given sizing
			// and spacing constraints; remaining columns will be spaced based
			// on the matchups in the previous column
			int vPos = this.getTopMargin();
			int hPos = this.getLeftMargin();

			List<List<Bracket>> roundStructure = this.model.getRoundStructure();
			if (!roundStructure.isEmpty()) {
				List<Bracket> entrantRound = roundStructure.get(0);

				for (final Bracket bracket : entrantRound) {
					// Get the view corresponding to this bracket, creating it
					// if necessary
					BracketViewSlot slot = this.views.get(bracket);
					if (slot == null) {
						// Create the BracketSlot
						slot = new BracketViewSlot(this.getBracketView()
								.getContext());

						// Assign the bracket to the BracketViewSlot
						slot.setBracket(bracket);

						// Assign listeners to the BracketSlot
						slot.setOnExpandedStateChangedListener(new OnExpandedStateChangedListener() {
							@Override
							public void onExpandedStateChanged(
									OnExpandedStateChangedEvent evt) {
								SingleEliminationLayout.this.getBracketView()
										.clearSelection();
							}
						});

						// Add the BracketSlot to the map and BracketView
						this.views.put(bracket, slot);
						this.getBracketView().addView(slot);
					}

					// Update the positioning and sizing of this component
					slot.layout(hPos, vPos, hPos + slot.getExpandedWidth(),
							vPos + slot.getExpandedHeight());

					// Update the vertical positioning variable
					vPos += slot.getExpandedHeight()
							+ this.getVerticalSpacing();
					requiredHeight = Math.max(vPos, requiredHeight);

					// Update usedBrackets
					usedBrackets.add(bracket);
				}

				// Update measurements
				requiredHeight += this.getBottomMargin()
						- this.getVerticalSpacing();
				this.setMeasuredDimension(requiredWidth, requiredHeight);
			}

			// After all the bracket controls have been added, eliminate any
			// extra controls
			Iterator<Bracket> iter = this.views.keySet().iterator();
			while (iter.hasNext()) {
				Bracket bracket = iter.next();
				if (!usedBrackets.contains(bracket)) {
					BracketViewSlot slot = this.views.get(bracket);
					if (slot != null) {
						this.getBracketView().removeView(slot);
					}
					iter.remove();
				}
			}
		}
	}

}
