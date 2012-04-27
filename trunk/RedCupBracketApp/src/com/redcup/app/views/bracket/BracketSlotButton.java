package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom control that acts as a simple multi-state button.
 * 
 * @author Jackson Lamp
 */
public class BracketSlotButton extends View {

	private static final int DEFAULT_COLOR = Color.argb(255, 255, 255, 255);
	private static final int SELECTED_COLOR = Color.argb(255, 255, 255, 0);

	private String text = null;

	/**
	 * Creates a new {@code BracketSlotButton}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 * @param attrs
	 *            the attributes assigned to this {@code View}.
	 * @param defStyle
	 *            the default style to use for this {@code View}.
	 */
	public BracketSlotButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initialize();
	}

	/**
	 * Creates a new {@code BracketSlotButton}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 * @param attrs
	 *            the attributes assigned to this {@code View}.
	 */
	public BracketSlotButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize();
	}

	/**
	 * Creates a new {@code BracketSlotButton}.
	 * 
	 * @param context
	 *            the {@code Context} that this {@code View} exists within.
	 */
	public BracketSlotButton(Context context) {
		super(context);
		this.initialize();
	}

	/**
	 * Performs general initialization services. Should be called by all
	 * constructors.
	 */
	private void initialize() {
		this.setClickable(true);
	}

	/**
	 * Assigns text to this button.
	 * 
	 * @param text
	 *            the text to display on this button.
	 */
	public void setText(String text) {
		this.text = text;
		this.invalidate();
	}

	/**
	 * Returns the text currently displayed on this button.
	 * 
	 * @return the text currently displayed on this button.
	 */
	public String getText() {
		return this.text;
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
		if (this.text != null) {
			canvas.save();
			canvas.clipRect(new RectF(10, 10, this.getWidth() - 10, this
					.getHeight() - 10));
			paint.setColor(Color.BLACK);
			paint.setTextSize(32);
			paint.setUnderlineText(this.isSelected());
			canvas.drawText(this.text, 10, 45, paint);
			canvas.restore();
		}
	}

}
