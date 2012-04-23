package com.redcup.app.views.bracket;

import com.redcup.app.views.bracket.layouts.BracketViewLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class BracketView extends ViewGroup {
	// The layout algorithm to use
	private BracketViewLayout layout = null;

	public BracketView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BracketView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BracketView(Context context) {
		super(context);
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
			this.layout.setContext(null);
		}
		this.layout = layout;
		this.layout.setContext(this);
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
		// Ensure that a layout manager is assigned
		if (this.layout != null) {
			// Obtain the amount of space required by the layout manager
			this.layout.onMeasure(widthMeasureSpec, heightMeasureSpec);
			this.setMeasuredDimension(this.layout.getMeasuredWidth(),
					this.layout.getMeasuredHeight());
		}
	}
}
