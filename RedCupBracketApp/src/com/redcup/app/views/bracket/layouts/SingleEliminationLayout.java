package com.redcup.app.views.bracket.layouts;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.graphics.Rect;
import android.view.View;

import com.redcup.app.model.Bracket;
import com.redcup.app.model.InvalidStateException;
import com.redcup.app.model.Participant;
import com.redcup.app.model.SingleEliminationBracketStrategy;
import com.redcup.app.views.bracket.BracketConnector;
import com.redcup.app.views.bracket.BracketMode;
import com.redcup.app.views.bracket.BracketView;
import com.redcup.app.views.bracket.BracketViewSlot;
import com.redcup.app.views.bracket.BracketViewSlot.ExpandedState;
import com.redcup.app.views.bracket.BracketViewSlot.OnExpansionEvent;
import com.redcup.app.views.bracket.BracketViewSlot.OnExpansionListener;
import com.redcup.app.views.bracket.SetupBracketViewSlot;
import com.redcup.app.views.bracket.SetupBracketViewSlot.OnEditBracketClickedEvent;
import com.redcup.app.views.bracket.SetupBracketViewSlot.OnEditBracketClickedListener;
import com.redcup.app.views.bracket.events.OnDemotedEvent;
import com.redcup.app.views.bracket.events.OnDemotedListener;
import com.redcup.app.views.bracket.events.OnParticipantRemovedEvent;
import com.redcup.app.views.bracket.events.OnParticipantRemovedListener;
import com.redcup.app.views.bracket.events.OnPromotedEvent;
import com.redcup.app.views.bracket.events.OnPromotedListener;

/**
 * Implementation of {@code BracketViewLayout} that is used to visually
 * represent a single elimination tournament.
 * 
 * @author Jackson Lamp
 */
public class SingleEliminationLayout extends BracketViewLayout {

	// Event handlers
	private final OnPromotedListener onPromotedListener = new OnPromotedListener() {
		@Override
		public void onPromoted(OnPromotedEvent event) {
			try {
				SingleEliminationLayout.this.model.promoteParticipantAt(event
						.getBracket());
			} catch (InvalidStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private final OnDemotedListener onDemotedListener = new OnDemotedListener() {
		@Override
		public void onDemoted(OnDemotedEvent event) {
			try {
				SingleEliminationLayout.this.model.demoteParticipantAt(event
						.getBracket());
			} catch (InvalidStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private final OnParticipantRemovedListener onParticipantRemovedListener = new OnParticipantRemovedListener() {
		@Override
		public void onParticipantRemoved(OnParticipantRemovedEvent event) {
			Participant p = event.getParticipant();
			SingleEliminationLayout.this.getBracketView().getTournament()
					.removeParticipant(p);
		}
	};

	// Internal state
	private final SingleEliminationBracketStrategy model;
	private final Map<Bracket, BracketViewSlot> bracketViewSlots = new HashMap<Bracket, BracketViewSlot>();

	/**
	 * Used to keep track of the bracket objects that have been used; after
	 * brackets have been added to the system, they are removed if they are not
	 * on this list
	 */
	private final Collection<Bracket> usedBrackets = new HashSet<Bracket>();

	private final OnExpansionListener expandedStateChangedListener = new OnExpansionListener() {
		@Override
		public void onExpansion(OnExpansionEvent evt) {
			SingleEliminationLayout.this.getBracketView().clearSelection();
			if (evt.getNewState() == ExpandedState.EXPANDED) {
				evt.getView().setSelected(true);
			}
		}
	};

	/**
	 * Creates a new {@code SingleEliminationLayout}.
	 * 
	 * @param bracketView
	 *            the {@code BracketView} that this object manages.
	 * @param model
	 *            the {@code SingleEliminationBracketStrategy} that acts as this
	 *            object's internal model.
	 */
	public SingleEliminationLayout(BracketView bracketView,
			SingleEliminationBracketStrategy model) {
		super(bracketView);
		this.model = model;
	}

	/**
	 * Returns an existing {@code BracketViewSlot} which corresponds with the
	 * given {@code Bracket} object. If this is not possible, creates and
	 * registers a new {@code BracketViewSlot}, which is returned.
	 * 
	 * @param bracket
	 *            the {@code Bracket} associated with a new or existing
	 *            {@code BracketViewSlot}.
	 * @return the {@code BracketViewSlot} associated with the given
	 *         {@code Bracket}.
	 */
	private BracketViewSlot getBracketViewSlot(Bracket bracket) {
		BracketViewSlot slot = this.bracketViewSlots.get(bracket);
		if (slot == null) {
			// Create the SetupBracketViewSlot
			final SetupBracketViewSlot setupSlot = new SetupBracketViewSlot(
					this.getBracketView().getContext());
			setupSlot.addOnPromotedListener(this.onPromotedListener);
			setupSlot.addOnDemotedListener(this.onDemotedListener);
			setupSlot
					.addOnParticipantRemovedListener(this.onParticipantRemovedListener);
			setupSlot
					.addOnEditBracketClickedListener(new OnEditBracketClickedListener() {
						@Override
						public void onEditBracketClicked(
								OnEditBracketClickedEvent event) {
							SetupBracketViewSlot source = event.getSource();
							switch (source.getMode()) {
							case ADVANCEMENT:
								source.setMode(BracketMode.READONLY);
								break;
							case READONLY:
								source.setMode(BracketMode.SETUP);
								break;
							case SETUP:
								source.setMode(BracketMode.ADVANCEMENT);
								break;
							}
						}
					});
			slot = setupSlot;

			// Assign the bracket to the BracketViewSlot
			slot.setBracket(bracket);

			// Assign listeners to the BracketSlot
			slot.setOnExpansionListener(this.expandedStateChangedListener);

			// Add the BracketSlot to the map and
			// BracketView
			this.bracketViewSlots.put(bracket, slot);
			this.getBracketView().addView(slot);
		}

		return slot;
	}

	/**
	 * Removes any {@code BracketViewSlot}s from the map and managed
	 * {@code BracketView} that do not correspond with {@code Brackets} found in
	 * the given {@code Collection}.
	 * 
	 * @param usedBrackets
	 *            the list of {@code Bracket} objects that correspond with
	 *            {@code BracketViewSlot} objects which need to be kept.
	 */
	private void postLayoutCleanup(Collection<Bracket> usedBrackets) {
		Iterator<Bracket> iter = this.bracketViewSlots.keySet().iterator();
		while (iter.hasNext()) {
			Bracket bracket = iter.next();
			if (!usedBrackets.contains(bracket)) {
				BracketViewSlot slot = this.bracketViewSlots.get(bracket);
				if (slot != null) {
					this.getBracketView().removeView(slot);
				}
				iter.remove();
			}
		}
	}

	/**
	 * Layout helper method. Positions the given {@code BrackeViewSlot} at the
	 * given location.
	 * 
	 * @param slot
	 *            the {@code BracketLayoutSlot} to lay out.
	 * @param vPos
	 *            the vertical position to place {@code slot} at.
	 * @param hPos
	 *            the horizontal position to place {@code slot} at.
	 */
	private void layoutOpeningRound(BracketViewSlot slot, int vPos, int hPos) {
		// Opening round; space evenly
		slot.layout(hPos, vPos, hPos + slot.getScaledExpandedWidth(), vPos
				+ slot.getScaledExpandedHeight());
	}

	/**
	 * Layout helper method. Positions the given {@code BracketViewSlot}
	 * associated with the given {@code Bracket} next to bracket preceding it in
	 * the tournament.
	 * 
	 * @param slot
	 *            the {@code BracketLayoutSlot} to lay out.
	 * @param bracket
	 *            the {@code BracketLayoutSlot} to corresponding to {@code slot}
	 *            .
	 * @param hPos
	 *            the horizontal position to place {@code slot} at.
	 */
	private void layoutByeRound(BracketViewSlot slot, Bracket bracket, int hPos) {
		// Bye round; base off of position of preceding
		// bracket
		Bracket prevBracket = (bracket.getLeft() != null) ? bracket.getLeft()
				: bracket.getRight();
		BracketViewSlot prevSlot = this.bracketViewSlots.get(prevBracket);

		// Update slot's positioning
		slot.layout(hPos, prevSlot.getTop(),
				hPos + slot.getScaledExpandedWidth(),
				prevSlot.getTop() + slot.getScaledExpandedHeight());
	}

	/**
	 * Layout helper method. Positions the given {@code BracketViewSlot}
	 * associated with the given {@code Bracket} next to two brackets preceding
	 * it in the tournament.
	 * 
	 * @param slot
	 *            the {@code BracketLayoutSlot} to lay out.
	 * @param bracket
	 *            the {@code BracketLayoutSlot} to corresponding to {@code slot}
	 *            .
	 * @param hPos
	 *            the horizontal position to place {@code slot} at.
	 */
	private void layoutMatchupRound(BracketViewSlot slot, Bracket bracket,
			int hPos) {
		// Matchup round; base off of position of preceding
		// two bracket slots
		Bracket left = bracket.getLeft();
		Bracket right = bracket.getRight();
		BracketViewSlot leftSlot = this.bracketViewSlots.get(left);
		BracketViewSlot rightSlot = this.bracketViewSlots.get(right);
		int avgTop = (leftSlot.getTop() + rightSlot.getTop()) / 2;

		// Update slot's positioning
		slot.layout(hPos, avgTop, hPos + slot.getScaledExpandedWidth(), avgTop
				+ slot.getScaledExpandedHeight());

		// TODO: Replace kludgey connector code
		// Create and add a BracketConnector
		BracketConnector connector = new BracketConnector(this.getBracketView()
				.getContext());
		connector.addLeftBracket(leftSlot);
		connector.addLeftBracket(rightSlot);
		connector.addRightBracket(slot);
		this.getBracketView().addView(connector, 0);
		Rect connectorBounds = connector.computeBounds();
		connector.layout(connectorBounds.left, connectorBounds.top,
				connectorBounds.right, connectorBounds.bottom);
	}

	@Override
	public void onLayout(boolean changed) {
		// Check to make sure that we need to actually update anything
		if (this.isLayoutRequired(changed)) {
			int requiredHeight = 0;
			int requiredWidth = 0;

			// TODO: Remove kludgey connector code
			// Remove all connectors from BracketView
			for (int i = 0; i < this.getBracketView().getChildCount(); i++) {
				View child = this.getBracketView().getChildAt(i);
				if (child instanceof BracketConnector) {
					this.getBracketView().removeViewAt(i);
					i--;
				}
			}

			// Get the round structure, if possible
			List<List<Bracket>> roundStructure = null;
			if (model != null) {
				roundStructure = this.model.getRoundStructure();
			}

			// Check if we have a valid round structure before attempting to
			// render it
			if (roundStructure != null && !roundStructure.isEmpty()) {
				// Used to keep track of the positioning of components
				int vPos = this.getTopMargin();
				int hPos = this.getLeftMargin();

				// The first round consists of evenly spaced bracket slots.
				// The remaining rounds are positioned based on the matchups
				// in the previous rounds.
				for (int roundNum = 0; roundNum < roundStructure.size(); roundNum++) {
					List<Bracket> round = roundStructure.get(roundNum);
					int maxBracketWidth = 0;
					for (Bracket bracket : round) {
						// Get the view corresponding to this bracket,
						// creating it if necessary
						BracketViewSlot slot = this.getBracketViewSlot(bracket);

						// Update scale of slot
						slot.setScale(this.getScale());

						// Update the positioning and sizing of this
						// component
						if (bracket.getRight() == null
								&& bracket.getLeft() == null) {
							// Opening round
							this.layoutOpeningRound(slot, vPos, hPos);
						} else if (bracket.getRight() == null
								|| bracket.getLeft() == null) {
							// Bye round
							this.layoutByeRound(slot, bracket, hPos);
						} else {
							// Matchup round
							this.layoutMatchupRound(slot, bracket, hPos);
						}

						// Update the positioning variables
						vPos = Math
								.max(slot.getBottom()
										+ this.getScaledVerticalSpacing(), vPos);
						maxBracketWidth = Math.max(maxBracketWidth,
								slot.getScaledExpandedWidth());

						// Update vertical measurement variable
						requiredHeight = Math.max(vPos, requiredHeight);

						// Update usedBrackets
						this.usedBrackets.add(bracket);
					}

					// Update positioning variables
					vPos = this.getTopMargin();
					hPos += maxBracketWidth + this.getScaledHorizontalSpacing();

					// Update horizontal measurement variable
					requiredWidth = Math.max(hPos, requiredWidth);
				}

				// Update measurements
				requiredHeight += this.getBottomMargin()
						- this.getScaledVerticalSpacing();
				requiredWidth += this.getRightMargin()
						- this.getScaledHorizontalSpacing();
			}

			// Commit measurements
			this.setMeasuredDimension(requiredWidth, requiredHeight);

			// Clean up unused resources
			this.postLayoutCleanup(this.usedBrackets);
			this.usedBrackets.clear();
		}
	}

	// @Override
	// public void setScale(float scale) {
	// super.setScale(scale);
	// Collection<BracketViewSlot> slots = this.bracketViewSlots.values();
	// for (BracketViewSlot slot : slots) {
	// slot.setScale(scale);
	// }
	// }
}
