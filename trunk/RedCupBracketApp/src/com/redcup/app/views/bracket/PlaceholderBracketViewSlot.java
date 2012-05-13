package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class PlaceholderBracketViewSlot extends BracketViewSlot {

	private static final int SIZE = 20;
	private static final int BORDER_THICKNESS = 4;

	public PlaceholderBracketViewSlot(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.initialize();
	}

	public PlaceholderBracketViewSlot(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize();
	}

	public PlaceholderBracketViewSlot(Context context) {
		super(context);
		this.initialize();
	}

	private void initialize() {
		// Configure size
		int size = this.applyScale(SIZE);
		this.setCollapsedWidth(size);
		this.setCollapsedHeight(size);
		this.setExpandedWidth(size);
		this.setExpandedHeight(size);

		// Configure background drawable
		this.setBackgroundDrawable(new Drawable() {

			@Override
			public void setColorFilter(ColorFilter cf) {
			}

			@Override
			public void setAlpha(int alpha) {
			}

			@Override
			public int getOpacity() {
				return 0;
			}

			@Override
			public void draw(Canvas canvas) {
				int width = getCollapsedWidth();
				int height = getCollapsedHeight();
				Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setColor(Color.BLACK);
				canvas.drawOval(new RectF(0, 0, width, height), paint);
				paint.setColor(Color.WHITE);
				canvas.drawOval(new RectF(BORDER_THICKNESS, BORDER_THICKNESS,
						width - BORDER_THICKNESS, height - BORDER_THICKNESS),
						paint);
			}
		});
	}

	@Override
	protected void updateInternalComponents() {
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// Configure size
		int size = this.applyScale(SIZE);
		this.setCollapsedWidth(size);
		this.setCollapsedHeight(size);
		this.setExpandedWidth(size);
		this.setExpandedHeight(size);
		this.invalidate();
	}
}
