package com.redcup.app.views.bracket;

import com.redcup.app.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Custom control that acts as a simple multi-state button.
 * 
 * @author Jackson Lamp
 */
public class BracketSlotButton extends View {

	// Fill colors
	private static final int FILL_DEFAULT = Color.argb(255, 255, 255, 255);
	private static final int FILL_SELECTED = Color.argb(255, 255, 255, 0);
	private static final int FILL_PRESSED = Color.argb(255, 255, 192, 0);
	private static final int FILL_EMPTY = Color.argb(255, 160, 160, 160);
	private static final int FILL_DISABLED = Color.argb(255, 160, 160, 160);

	// Border colors
	private static final int BORDER_DEFAULT = Color.BLACK;
	private static final int BORDER_SELECTED = Color.BLACK;
	private static final int BORDER_PRESSED = Color.BLACK;
	private static final int BORDER_EMPTY = Color.BLACK;
	private static final int BORDER_DISABLED = Color.BLACK;

	// Reference font size
	private static final int DEFAULT_TEXT_SIZE = 32;

	// Sample text used to compute text sizes
	private static final String SIZING_SAMPLE_TEXT = "Ty";

	// Button state
	private boolean isEmpty = false;

	// Border thickness
	private float borderThickness = 4;

	// Corner radius
	private float cornerRadius = 20;

	// Insets
	private float insets_top = 15;
	private float insets_left = 10;
	private float insets_bottom = 15;
	private float insets_right = 10;
	private float insets_topOffset = 5;
	private float insets_iconTextSpacing = 5;

	// Displayed text and icon
	private String text = null;
	private Drawable icon = null;
	private Drawable addIcon;

	// Scale factor
	private float scale = 1.0f;

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
		this.addIcon = new BitmapDrawable(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.create_tournament));
	}

	/**
	 * Sets the empty state flag on this {@code BracketSlotButton}. If set, this
	 * flag overrides the appearance of this button but does not affect its
	 * functionality.
	 * 
	 * @param isEmpty
	 *            the empty state flag on this {@code BracketSlotButton}.
	 */
	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
		this.invalidate();
	}

	/**
	 * Returns the empty state flag on this {@code BracketSlotButton}. If set,
	 * this flag overrides the appearance of this button but does not affect its
	 * functionality.
	 * 
	 * @return the empty state flag on this {@code BracketSlotButton}.
	 */
	public boolean isEmpty() {
		return this.isEmpty;
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

	/**
	 * Sets this control's border thickness.
	 * 
	 * @param borderThickness
	 *            this control's border thickness.
	 */
	public void setBorderThickness(float borderThickness) {
		this.borderThickness = borderThickness;
		this.invalidate();
	}

	/**
	 * Returns this control's border thickness.
	 * 
	 * @return this control's border thickness.
	 */
	public float getBorderThickness() {
		return this.borderThickness;
	}

	/**
	 * Sets this control's corner radius.
	 * 
	 * @param radius
	 *            the new radius to use.
	 */
	public void setCornerRadius(float radius) {
		this.cornerRadius = radius;
		this.invalidate();
	}

	/**
	 * Returns this control's corner radius.
	 * 
	 * @return this control's corner radius.
	 */
	public float getCornerRadius() {
		return this.cornerRadius;
	}

	/**
	 * Sets the insets, which dictate content placement.
	 * 
	 * @param l
	 *            left padding.
	 * @param t
	 *            top padding.
	 * @param r
	 *            right padding.
	 * @param b
	 *            bottom padding.
	 */
	public void setInsets(float l, float t, float r, float b) {
		this.insets_left = l;
		this.insets_top = t;
		this.insets_right = r;
		this.insets_bottom = b;
		this.invalidate();
	}

	public void setScale(float scale) {
		this.scale = scale;
		this.invalidate();
	}

	public float getScale() {
		return this.scale;
	}

	protected float applyScale(float dimension) {
		return this.scale * dimension;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
		this.invalidate();
	}

	public void setIcon(Bitmap icon) {
		this.icon = new BitmapDrawable(icon);
		this.invalidate();
	}

	public void setIcon(int resid) {
		this.icon = new BitmapDrawable(BitmapFactory.decodeResource(
				this.getResources(), resid));
		this.invalidate();
	}

	public Drawable getIcon() {
		return this.icon;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = super.onTouchEvent(event);

		// Update pressed state
		if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			this.setPressed(true);
			this.invalidate();
		} else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
			this.setPressed(false);
			this.invalidate();
		} else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
			boolean isPointerOver = false;
			for (int i = 0; i < event.getPointerCount(); i++) {
				isPointerOver |= event.getX(i) >= 0
						&& event.getX(i) <= this.getWidth()
						&& event.getY(i) >= 0
						&& event.getY(i) <= this.getHeight();
			}
			this.setPressed(isPointerOver);
			this.invalidate();
		}

		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		int fillColor = FILL_DEFAULT;
		int borderColor = BORDER_DEFAULT;
		if (this.isSelected()) {
			fillColor = FILL_SELECTED;
			borderColor = BORDER_SELECTED;
		}
		if (this.isEmpty()) {
			fillColor = FILL_EMPTY;
			borderColor = BORDER_EMPTY;
		}
		if (this.isPressed()) {
			fillColor = FILL_PRESSED;
			borderColor = BORDER_PRESSED;
		}
		if (!this.isEnabled()) {
			fillColor = FILL_DISABLED;
			borderColor = BORDER_DISABLED;
		}
		int scaledCornerRadius = Math.round(this.applyScale(this.cornerRadius));
		int scaledTextMarginTop = Math.round(this.applyScale(this.insets_top));
		int scaledTextMarginLeft = Math
				.round(this.applyScale(this.insets_left));
		int scaledTextMarginRight = Math.round(this
				.applyScale(this.insets_right));
		int scaledTextMarginBottom = Math.round(this
				.applyScale(this.insets_bottom));

		// Draw border
		paint.setColor(borderColor);
		canvas.drawRoundRect(
				new RectF(0, 0, this.getWidth(), this.getHeight()),
				scaledCornerRadius, scaledCornerRadius, paint);

		// Draw background
		paint.setColor(fillColor);
		canvas.drawRoundRect(new RectF(this.borderThickness,
				this.borderThickness, this.getWidth() - this.borderThickness,
				this.getHeight() - this.borderThickness), scaledCornerRadius
				- this.borderThickness, scaledCornerRadius
				- this.borderThickness, paint);

		// Draw label
		String text = this.text;
		Drawable icon = this.icon;
		if (this.isEmpty()) {
			text = "Add";
			paint.setTextSkewX(-0.25f);
			icon = this.addIcon;
		}
		if (icon != null) {
			float iconScale = (this.getHeight() - scaledTextMarginBottom - scaledTextMarginTop)
					/ (float) icon.getMinimumHeight();
			float iconWidth = icon.getMinimumWidth() * iconScale;
			float iconHeight = icon.getMinimumHeight() * iconScale;
			icon.setBounds(scaledTextMarginLeft, scaledTextMarginTop,
					Math.round(scaledTextMarginLeft + iconWidth),
					Math.round(scaledTextMarginTop + iconHeight));
			icon.draw(canvas);
			scaledTextMarginLeft += iconWidth
					+ this.applyScale(this.insets_iconTextSpacing);
		}
		if (text != null) {
			canvas.save();
			// Configure paint
			RectF textAreaBounds = new RectF(scaledTextMarginLeft,
					scaledTextMarginTop
							+ this.applyScale(this.insets_topOffset),
					this.getWidth() - scaledTextMarginRight, this.getHeight()
							- scaledTextMarginBottom);
			Paint textPaint = new Paint(paint);
			canvas.clipRect(textAreaBounds);
			textPaint.setColor(Color.BLACK);
			textPaint.setTextSize(DEFAULT_TEXT_SIZE);
			textPaint.setUnderlineText(this.isSelected() || this.isEmpty());

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
			canvas.drawText(text, textAreaBounds.left, textAreaBounds.bottom
					- measuredBounds.bottom, textPaint);
			canvas.restore();
		}
	}
}
