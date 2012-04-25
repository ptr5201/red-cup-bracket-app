package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.redcup.app.views.bracket.layouts.BracketViewLayout;
import com.redcup.app.views.bracket.layouts.SingleEliminationLayout;

public class BracketView extends ViewGroup {

	private int scrollY;

	private GestureDetector gestures;

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
		// SingleEliminationBracketStrategy(
		// entrants);
		// SingleEliminationLayout layout = new SingleEliminationLayout(this,
		// model);
		// this.setLayoutAlgorithm(layout);
		SingleEliminationLayout layout = new SingleEliminationLayout(this, null);
		this.setLayoutAlgorithm(layout);

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < BracketView.this.getChildCount(); i++) {
					View child = BracketView.this.getChildAt(i);
					if (child instanceof BracketViewSlot) {
						BracketViewSlot slot = (BracketViewSlot) child;
						slot.reset();
					}
				}
			}
		});

		gestures = new GestureDetector(this.getContext(),
				new OnGestureListener() {

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						return false;
					}

					@Override
					public void onShowPress(MotionEvent e) {
					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						int positionY = (int) Math.round(getScrollOffsetY()
								+ distanceY);
						setScrollOffsetY(positionY);
						return true;
					}

					@Override
					public void onLongPress(MotionEvent e) {
					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						return false;
					}

					@Override
					public boolean onDown(MotionEvent e) {
						// We have to "handle" this event to get other events to
						// work
						return true;
					}
				});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.gestures.onTouchEvent(event);
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

	private void setScrollOffsetY(int scrollY) {
		this.scrollY = scrollY;
		if (this.layout != null) {
			this.layout.setScrollOffsetY(scrollY);
			this.layout.updatePositions();
			this.invalidate();
		}
	}

	private int getScrollOffsetY() {
		return this.scrollY;
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
