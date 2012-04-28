package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
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

	private static final int DEFAULT_TEXT_SIZE = 32;

	private static final int TOP_TEXT_MARGIN = 20;
	private static final int BOTTOM_TEXT_MARGIN = 15;
	private static final int LEFT_TEXT_MARGIN = 10;
	private static final int RIGHT_TEXT_MARGIN = 10;

	private static final int BORDER_THICKNESS = 4;
	private static final int CORNER_RADIUS = 20;

	private static final String SIZING_SAMPLE_TEXT = "Ty";

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
		paint.setStyle(Style.FILL);

		// Draw border
		paint.setColor(Color.BLACK);
		canvas.drawRoundRect(
				new RectF(0, 0, this.getWidth(), this.getHeight()),
				CORNER_RADIUS, CORNER_RADIUS, paint);

		// Draw background
		paint.setColor(this.isSelected() ? SELECTED_COLOR : DEFAULT_COLOR);
		canvas.drawRoundRect(
				new RectF(BORDER_THICKNESS, BORDER_THICKNESS, this.getWidth()
						- BORDER_THICKNESS, this.getHeight() - BORDER_THICKNESS),
				CORNER_RADIUS - BORDER_THICKNESS, CORNER_RADIUS
						- BORDER_THICKNESS, paint);

		// Draw label
		if (this.text != null) {
			canvas.save();
			// Configure paint
			RectF textAreaBounds = new RectF(LEFT_TEXT_MARGIN, TOP_TEXT_MARGIN,
					this.getWidth() - RIGHT_TEXT_MARGIN, this.getHeight()
							- BOTTOM_TEXT_MARGIN);
			Paint textPaint = new Paint(paint);
			canvas.clipRect(textAreaBounds);
			textPaint.setColor(Color.BLACK);
			textPaint.setTextSize(DEFAULT_TEXT_SIZE);
			textPaint.setUnderlineText(this.isSelected());

			// Size text to match height of button
			Rect measuredBounds = new Rect();
			textPaint.getTextBounds(SIZING_SAMPLE_TEXT, 0,
					SIZING_SAMPLE_TEXT.length(), measuredBounds);
			float textSize = textAreaBounds.height() / measuredBounds.height()
					* DEFAULT_TEXT_SIZE;
			textPaint.setTextSize(textSize);

			// Update measurements
			textPaint.getTextBounds(SIZING_SAMPLE_TEXT, 0,
					SIZING_SAMPLE_TEXT.length(), measuredBounds);

			// Draw text
			canvas.drawText(this.text, textAreaBounds.left,
					textAreaBounds.bottom - measuredBounds.bottom, textPaint);
			canvas.restore();
		}
	}
}
