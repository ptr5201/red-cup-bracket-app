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

			// Get the round structure and ensure that it is populated
			List<List<Bracket>> roundStructure = this.model.getRoundStructure();
			if (!roundStructure.isEmpty()) {
				// Used to keep track of the positioning of components
				int vPos = this.getTopMargin();
				int hPos = this.getLeftMargin();

				// The first round consists of evenly spaced bracket slots. The
				// remaining rounds are positioned based on the matchups in the
				// previous rounds.
				for (int roundNum = 0; roundNum < roundStructure.size(); roundNum++) {
					List<Bracket> round = roundStructure.get(roundNum);
					int maxBracketWidth = 0;
					for (Bracket bracket : round) {
						// Get the view corresponding to this bracket, creating
						// it if necessary
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
									SingleEliminationLayout.this
											.getBracketView().clearSelection();
								}
							});

							// Add the BracketSlot to the map and BracketView
							this.views.put(bracket, slot);
							this.getBracketView().addView(slot);
						}

						// Update the positioning and sizing of this component
						if (bracket.getRight() == null
								&& bracket.getLeft() == null) {
							// Opening round; space evenly
							slot.layout(hPos, vPos,
									hPos + slot.getExpandedWidth(),
									vPos + slot.getExpandedHeight());
						} else if (bracket.getRight() == null
								|| bracket.getLeft() == null) {
							// Bye round; base off of position of preceding
							// bracket
							Bracket prevBracket = (bracket.getLeft() != null) ? bracket
									.getLeft() : bracket.getRight();
							BracketViewSlot prevSlot = this.views
									.get(prevBracket);

							// Update slot's positioning
							slot.layout(
									hPos,
									prevSlot.getTop(),
									hPos + slot.getExpandedWidth(),
									prevSlot.getTop()
											+ slot.getExpandedHeight());
						} else {
							// Matchup round; base off of position of preceding
							// two bracket slots
							Bracket left = bracket.getLeft();
							Bracket right = bracket.getRight();
							BracketViewSlot leftSlot = this.views.get(left);
							BracketViewSlot rightSlot = this.views.get(right);
							int avgTop = (leftSlot.getTop() + rightSlot
									.getTop()) / 2;

							// Update slot's positioning
							slot.layout(hPos, avgTop,
									hPos + slot.getExpandedWidth(), avgTop
											+ slot.getExpandedHeight());
						}

						// Update the positioning variables
						vPos = Math.max(
								slot.getBottom() + this.getVerticalSpacing(),
								vPos);
						maxBracketWidth = Math.max(maxBracketWidth,
								slot.getExpandedWidth());

						// Update vertical measurement variable
						requiredHeight = Math.max(vPos, requiredHeight);

						// Update usedBrackets
						usedBrackets.add(bracket);
					}

					// Update positioning variables
					vPos = this.getTopMargin();
					hPos += maxBracketWidth + this.getHorizontalSpacing();

					// Update horizontal measurement variable
					requiredWidth = Math.max(hPos, requiredWidth);
				}

				// Update and commit measurements
				requiredHeight += this.getBottomMargin()
						- this.getVerticalSpacing();
				requiredWidth += this.getRightMargin()
						- this.getHorizontalSpacing();
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
