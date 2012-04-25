package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BracketViewSlot extends View {

	private int collapsedHeight = 60;
	private int collapsedWidth = 240;

	private int expandedHeight = 120;
	private int expandedWidth = 320;

	int r = 255;
	int b = 0;
	int g = 0;
	boolean hasFocus = false;

	public BracketViewSlot(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initialize();
	}

	public BracketViewSlot(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize();
	}

	public BracketViewSlot(Context context) {
		super(context);
		this.initialize();
	}

	private void initialize() {
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				r = 0;
				g = 255;
				b = 0;
				invalidate();
			}
		});

		this.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				r = 0;
				g = 0;
				b = 255;
				invalidate();
				return true;
			}
		});

		this.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				BracketViewSlot.this.hasFocus = hasFocus;
				BracketViewSlot.this.invalidate();
			}
		});
	}
	
	public void reset() {
		r = 255;
		g = 0;
		b = 0;
		invalidate();
	}

	public void setCollapsedWidth(int width) {
		this.collapsedWidth = width;
		this.invalidate();
	}

	public int getCollapsedWidth() {
		return this.collapsedWidth;
	}

	public void setCollapsedHeight(int height) {
		this.collapsedHeight = height;
		this.invalidate();
	}

	public int getCollapsedHeight() {
		return this.collapsedHeight;
	}

	public void setExpandedWidth(int width) {
		this.expandedWidth = width;
		this.invalidate();
	}

	public int getExpandedWidth() {
		return this.expandedWidth;
	}

	public void setExpandedHeight(int height) {
		this.expandedHeight = height;
		this.invalidate();
	}

	public int getExpandedHeight() {
		return this.expandedHeight;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(4);

		// Draw expanded area
		paint.setARGB(127, r, g, b);
		paint.setStyle(Style.FILL);
		canvas.drawRoundRect(new RectF(0, 0, this.expandedWidth,
				this.expandedHeight), 20, 20, paint);

		// Draw border
		int borderShade = this.hasFocus ? 255 : 0;
		paint.setARGB(255, borderShade, borderShade, 0);
		paint.setStyle(Style.FILL);
		canvas.drawRoundRect(new RectF(0, 0, this.collapsedWidth,
				this.collapsedHeight), 20, 20, paint);

		// Draw background
		paint.setARGB(255, r, g, b);
		paint.setStyle(Style.FILL);
		canvas.drawRoundRect(new RectF(4, 4, this.collapsedWidth - 4,
				this.collapsedHeight - 4), 16, 16, paint);

		// Draw label
		canvas.save();
		canvas.clipRect(new RectF(4, 4, this.collapsedWidth - 4,
				this.collapsedHeight - 4));
		paint.setARGB(255, 0, 0, 0);
		paint.setTextSize(32);
		paint.setUnderlineText(true);
		canvas.drawText("Text Goes Here and Stuff", 10, 40, paint);
		canvas.restore();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		// int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		// int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		// int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		//
		// this.setMeasuredDimension(widthSize, heightSize);
	}
}
