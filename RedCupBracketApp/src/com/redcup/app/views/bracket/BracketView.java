package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.redcup.app.model.SingleEliminationBracketStrategy;
import com.redcup.app.model.Tournament;
import com.redcup.app.model.Tournament.ParticipantChangedEvent;
import com.redcup.app.views.bracket.layouts.BracketViewLayout;
import com.redcup.app.views.bracket.layouts.BracketViewLayoutFactory;
import com.redcup.app.views.bracket.layouts.SingleEliminationLayout;

/**
 * The control used for viewing and editing a bracket.
 * 
 * @author Jackson Lamp
 */
public class BracketView extends ViewGroup {

	/**
	 * Used to handle gesture events that are received by this {@code View}.
	 * 
	 * @author Jackson Lamp
	 */
	private class GestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// Reset selection
			BracketView.this.clearSelection();
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// Scroll, taking into account display boundaries
			awakenScrollBars();

			// Compute the position we want to move to
			int x = (int) Math.round(getScrollX() + distanceX);
			int y = (int) Math.round(getScrollY() + distanceY);

			// Apply movement bounds
			x = Math.min(x, getViewportWidth() - getWidth());
			x = Math.max(x, 0);
			y = Math.min(y, getViewportHeight() - getHeight());
			y = Math.max(y, 0);

			// Move and return
			scrollTo(x, y);
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO: Implement zooming using pinch mechanism
			// Temporary testing code
			if (layout != null) {
				// Update zoom factor and update the layout
				if (layout.getScale() > 0.9f) {
					layout.setScale(0.9f);
				} else if (layout.getScale() < 0.9f) {
					layout.setScale(1.0f);
				} else {
					layout.setScale(0.75f);
				}
				updateLayout(true);

				// Update scroll position
				int x = getScrollX();
				int y = getScrollY();

				// Apply movement bounds
				x = Math.min(x, getViewportWidth() - getWidth());
				x = Math.max(x, 0);
				y = Math.min(y, getViewportHeight() - getHeight());
				y = Math.max(y, 0);

				// Move and return
				scrollTo(x, y);
			}
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// We have to return 'true' or advanced gestures won't work
			return true;
		}
	}

	/**
	 * Detector for gesture events.
	 */
	private GestureDetector gestures;

	/**
	 * The layout algorithm to use
	 */
	private BracketViewLayout layout = null;

	/**
	 * The tournament to manage.
	 */
	private Tournament tournament = null;

	/**
	 * Used to handle changes in the list of participants in the Tournament.
	 */
	private Tournament.ParticipantChangedListener tournamentParticipantChangedListener = new Tournament.ParticipantChangedListener() {
		@Override
		public void onParticipantListChanged(ParticipantChangedEvent event) {
			// TODO: Remove once better system is in place
			Tournament source = event.getSource();
			SingleEliminationBracketStrategy bracket = new SingleEliminationBracketStrategy(
					source.getParticipants());
			source.setStrategy(bracket);

			// Create and configure the new layout instance
			BracketViewLayout oldLayout = BracketView.this.getLayoutAlgorithm();
			BracketViewLayout newLayout = new SingleEliminationLayout(
					BracketView.this, bracket);
			newLayout.setScale(oldLayout.getScale());
			BracketView.this.setLayoutAlgorithm(newLayout);
		}
	};

	/**
	 * Creates a new {@code BracketView}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 * @param attrs
	 *            the attributes assigned to this {@code View}.
	 * @param defStyle
	 *            the default style to use for this {@code View}.
	 */
	public BracketView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initialize();
	}

	/**
	 * Creates a new {@code BracketView}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 * @param attrs
	 *            the attributes assigned to this {@code View}.
	 */
	public BracketView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize();
	}

	/**
	 * Creates a new {@code BracketView}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 */
	public BracketView(Context context) {
		super(context);
		this.initialize();
	}

	/**
	 * Performs general initialization services. Should be called by all
	 * constructors.
	 */
	private void initialize() {
		// Configure background
		this.setBackgroundColor(Color.WHITE);

		// Configure scroll bars
		this.setScrollContainer(true);
		this.setScrollbarFadingEnabled(true);
		this.setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);

		// TODO: Figure out why enabling scroll bars results in crash
		// this.setVerticalScrollBarEnabled(true);
		// this.setHorizontalScrollBarEnabled(true);

		// Configure gesture listener
		gestures = new GestureDetector(this.getContext(), new GestureListener());
	}

	/**
	 * De-selects and collapses all {@code BracketViewSlot}s associated with
	 * this {@code BracketView}.
	 */
	public void clearSelection() {
		for (int i = 0; i < this.getChildCount(); i++) {
			View child = this.getChildAt(i);
			if (child instanceof BracketViewSlot) {
				BracketViewSlot slot = (BracketViewSlot) child;
				slot.reset();
			}
		}
	}

	/**
	 * Assigns a {@code Tournament} for this {@code BracketView} to manage.
	 * 
	 * @param tournament
	 *            the {@code Tournament} for this {@code BracketView} to manage.
	 */
	public void setTournament(Tournament tournament) {
		// Remove listeners from existing tournament
		if (this.tournament != null) {
			this.tournament
					.removeOnParticipantListChangedListener(this.tournamentParticipantChangedListener);
		}

		// Assign tournament
		this.tournament = tournament;

		// Register listeners with new tournament
		if (this.tournament != null) {
			this.tournament
					.addOnParticipantListChangedListener(this.tournamentParticipantChangedListener);
		}

		// Create and set the corresponding layout manaager
		BracketViewLayout layout = BracketViewLayoutFactory.createLayout(this,
				this.tournament);
		this.setLayoutAlgorithm(layout);
	}

	/**
	 * Returns this {@code BracketView}'s {@code Tournament}.
	 * 
	 * @return this {@code BracketView}'s {@code Tournament}.
	 */
	public Tournament getTournament() {
		return this.tournament;
	}

	/**
	 * Sets the {@code BracketViewLayout} used to layout components in this
	 * {@code BracketView}.
	 * 
	 * @param layout
	 *            the {@code BracketViewLayout} used to layout components in
	 *            this {@code BracketView}.
	 */
	protected void setLayoutAlgorithm(BracketViewLayout layout) {
		if (this.layout != null) {
			this.layout.setBracketView(null);
		}
		this.layout = layout;
		this.layout.setBracketView(this);
		this.removeAllViews();
		this.updateLayout(true);
		this.invalidate();
	}

	/**
	 * Returns the {@code BracketViewLayout} used to layout components in this
	 * {@code BracketView}.
	 * 
	 * @return the {@code BracketViewLayout} used to layout components in this
	 *         {@code BracketView}.
	 */
	protected BracketViewLayout getLayoutAlgorithm() {
		return this.layout;
	}

	/**
	 * Returns the height of the internal "viewport" of this scrollable
	 * component.
	 * 
	 * @return the height of the internal "viewport" of this scrollable
	 *         component.
	 */
	protected int getViewportHeight() {
		if (this.layout != null) {
			return this.layout.getMeasuredHeight();
		}
		return 0;
	}

	/**
	 * Returns the width of the internal "viewport" of this scrollable
	 * component.
	 * 
	 * @return the width of the internal "viewport" of this scrollable
	 *         component.
	 */
	protected int getViewportWidth() {
		if (this.layout != null) {
			return this.layout.getMeasuredWidth();
		}
		return 0;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.gestures.onTouchEvent(event);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		this.updateLayout(changed);
	}

	/**
	 * Runs the layout manager.
	 * 
	 * @param changed
	 *            whether an external change has occurred.
	 */
	protected void updateLayout(boolean changed) {
		if (this.layout != null) {
			this.layout.onLayout(changed);
		}
	}
}
