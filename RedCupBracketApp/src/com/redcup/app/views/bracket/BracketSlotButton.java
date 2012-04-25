package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BracketSlotButton extends View {

	private static final int DEFAULT_COLOR = Color.argb(255, 255, 255, 255);
	private static final int SELECTED_COLOR = Color.argb(255, 255, 255, 0);

	public BracketSlotButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initialize();
	}

	public BracketSlotButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize();
	}

	public BracketSlotButton(Context context) {
		super(context);
		this.initialize();
	}

	private void initialize() {
	}

	public void reset() {
		this.setSelected(false);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(4);

		// Draw border
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		canvas.drawRoundRect(
				new RectF(0, 0, this.getWidth(), this.getHeight()), 20, 20,
				paint);

		// Draw background
		paint.setColor(this.isSelected() ? SELECTED_COLOR : DEFAULT_COLOR);
		paint.setStyle(Style.FILL);
		canvas.drawRoundRect(
				new RectF(4, 4, this.getWidth() - 4, this.getHeight() - 4), 16,
				16, paint);

		// Draw label
		canvas.save();
		canvas.clipRect(new RectF(4, 4, this.getWidth() - 4,
				this.getHeight() - 4));
		paint.setColor(Color.BLACK);
		paint.setTextSize(32);
		paint.setUnderlineText(true);
		canvas.drawText("Text Goes Here and Stuff", 10, 40, paint);
		canvas.restore();
	}

}
