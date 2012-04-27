package com.redcup.app.views.bracket;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.redcup.app.model.Participant;
import com.redcup.app.model.SingleEliminationBracketStrategy;
import com.redcup.app.views.bracket.layouts.BracketViewLayout;
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
		public boolean onSingleTapUp(MotionEvent e) {
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
		public boolean onDown(MotionEvent e) {
			// We have to return 'true' or advanced gestures won't work
			return true;
		}
	}

	private GestureDetector gestures;

	/**
	 * The layout algorithm to use
	 */
	private BracketViewLayout layout = null;

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
		this.setBackgroundColor(Color.WHITE);
		this.setScrollContainer(true);

		gestures = new GestureDetector(this.getContext(), new GestureListener());

		// TESTING CODE BEYOND THIS POINT

		List<Participant> entrants = new ArrayList<Participant>();
		entrants.add(new Participant("P1"));
		entrants.add(new Participant("P2"));
		entrants.add(new Participant("P3"));
		entrants.add(new Participant("P4"));
		entrants.add(new Participant("P5"));
		entrants.add(new Participant("P6"));
		SingleEliminationBracketStrategy model = new SingleEliminationBracketStrategy(
				entrants);
		SingleEliminationLayout layout = new SingleEliminationLayout(this,
				model);
		this.setLayoutAlgorithm(layout);
		// SingleEliminationLayout layout = new SingleEliminationLayout(this,
		// null);
		// this.setLayoutAlgorithm(layout);

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
		this.invalidate();
	}

	/**
	 * Sets the {@code BracketViewLayout} used to layout components in this
	 * {@code BracketView}.
	 * 
	 * @param layout
	 *            the {@code BracketViewLayout} used to layout components in
	 *            this {@code BracketView}.
	 */
	public void setLayoutAlgorithm(BracketViewLayout layout) {
		if (this.layout != null) {
			this.layout.setBracketView(null);
		}
		this.layout = layout;
		this.layout.setBracketView(this);
		this.removeAllViews();
		this.invalidate();
	}

	/**
	 * Returns the {@code BracketViewLayout} used to layout components in this
	 * {@code BracketView}.
	 * 
	 * @return the {@code BracketViewLayout} used to layout components in this
	 *         {@code BracketView}.
	 */
	public BracketViewLayout getLayoutAlgorithm() {
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
		if (this.layout != null) {
			this.layout.onLayout(changed, l, t, r, b);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Although the layout does not affect the amount of measured space
		// required for this view, we do need to update its internal
		// measurements
		if (this.layout != null) {
			this.layout.updateSizeRequirements();
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
