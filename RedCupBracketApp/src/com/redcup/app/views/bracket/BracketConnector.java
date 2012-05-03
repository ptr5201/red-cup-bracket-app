package com.redcup.app.views.bracket;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class BracketConnector extends View {

	private Collection<BracketViewSlot> leftBrackets = new ArrayList<BracketViewSlot>();
	private Collection<BracketViewSlot> rightBrackets = new ArrayList<BracketViewSlot>();

	public BracketConnector(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BracketConnector(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BracketConnector(Context context) {
		super(context);
	}

	public void addLeftBracket(BracketViewSlot bracket) {
		this.leftBrackets.add(bracket);
	}

	public boolean removeLeftBracket(BracketViewSlot bracket) {
		return this.leftBrackets.remove(bracket);
	}

	public int getLeftBracketCount() {
		return this.leftBrackets.size();
	}

	public void addRightBracket(BracketViewSlot bracket) {
		this.rightBrackets.add(bracket);
	}

	public boolean removeRightBracket(BracketViewSlot bracket) {
		return this.rightBrackets.remove(bracket);
	}

	public int getRightBracketCount() {
		return this.rightBrackets.size();
	}

	/**
	 * Computes the bounds required to properly display this
	 * {@code BracketConnector}.
	 * 
	 * @return the computed bounds.
	 */
	public Rect computeBounds() {
		Rect r = new Rect();
		this.computeBounds(r);
		return r;
	}

	/**
	 * Computes the bounds required to properly display this
	 * {@code BracketConnector}.
	 * 
	 * @param r
	 *            output variable used to return the computed bounds.
	 */
	public void computeBounds(Rect r) {
		if (r == null) {
			throw new NullPointerException();
		}

		int top = Integer.MAX_VALUE;
		int bottom = 0;
		int left = Integer.MAX_VALUE;
		int right = 0;

		for (BracketViewSlot b : this.leftBrackets) {
			top = Math.min(top, b.getTop());
			bottom = Math
					.max(bottom, b.getTop() + b.getScaledCollapsedHeight());
			left = Math.min(left, b.getLeft() + b.getScaledCollapsedWidth());
			right = Math.max(right, b.getLeft() + b.getScaledCollapsedWidth());
		}

		for (BracketViewSlot b : this.rightBrackets) {
			top = Math.min(top, b.getTop());
			bottom = Math
					.max(bottom, b.getTop() + b.getScaledCollapsedHeight());
			left = Math.min(left, b.getLeft());
			right = Math.max(right, b.getLeft());
		}

		r.bottom = bottom;
		r.top = top;
		r.left = left;
		r.right = right;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Configure the paint object
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(4);

		// TODO: Eliminate hard coded center computation
		// Compute the center of the view
		// int centerX = this.getWidth() / 2;
		// int centerY = this.getHeight() / 2;
		int centerX = this.getWidth() - 20;
		int centerY = this.getHeight() / 2;

		// Draw lines from each element on the left
		for (BracketViewSlot b : this.leftBrackets) {
			// Compute the start of this line
			int startX = b.getLeft() + b.getScaledCollapsedWidth()
					- this.getLeft();
			int startY = b.getTop() + b.getScaledCollapsedHeight() / 2
					- this.getTop();

			// Build the path object
			Path path = new Path();
			path.moveTo(startX, startY);
			path.lineTo(centerX, startY);
			path.lineTo(centerX, centerY);

			// Draw the path
			canvas.drawPath(path, paint);
		}

		// Draw lines to each element on the right
		for (BracketViewSlot b : this.rightBrackets) {
			// Compute the start of this line
			int startX = b.getLeft() - this.getLeft();
			int startY = b.getTop() + b.getCollapsedHeight() / 2
					- this.getTop();
			canvas.drawLine(startX, startY, centerX, startY, paint);

			// Build the path object
			Path path = new Path();
			path.moveTo(startX, startY);
			path.lineTo(centerX, startY);
			path.lineTo(centerX, centerY);

			// Draw the path
			canvas.drawPath(path, paint);
		}
	}
}
