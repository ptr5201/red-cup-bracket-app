package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.redcup.app.views.bracket.layouts.BracketViewLayout;
import com.redcup.app.views.bracket.layouts.SingleEliminationLayout;

public class BracketView extends ViewGroup {

	/**
	 * The layout algorithm to use
	 */
	private BracketViewLayout layout = null;

	public BracketView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initialize();
	}

	public BracketView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize();
	}

	public BracketView(Context context) {
		super(context);
		this.initialize();
	}

	private void initialize() {
		this.setBackgroundColor(Color.WHITE);

		// Testing code
		// List<Participant> entrants = new ArrayList<Participant>();
		// entrants.add(new Participant("P1"));
		// entrants.add(new Participant("P2"));
		// entrants.add(new Participant("P3"));
		// entrants.add(new Participant("P4"));
		// entrants.add(new Participant("P5"));
		// entrants.add(new Participant("P6"));
		// SingleEliminationBracketStrategy model = new
		// SingleEliminationBracketStrategy(entrants);
		// SingleEliminationLayout layout = new SingleEliminationLayout(this,
		// model);
		// this.setLayoutAlgorithm(layout);
		SingleEliminationLayout layout = new SingleEliminationLayout(this, null);
		this.setLayoutAlgorithm(layout);
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

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (this.layout != null) {
			this.layout.onLayout(changed, l, t, r, b);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// // Ensure that a layout manager is assigned
		// if (this.layout != null) {
		// // Obtain the amount of space required by the layout manager
		// this.layout.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// this.setMeasuredDimension(this.layout.getMeasuredWidth(),
		// this.layout.getMeasuredHeight());
		// } else {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// }
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
