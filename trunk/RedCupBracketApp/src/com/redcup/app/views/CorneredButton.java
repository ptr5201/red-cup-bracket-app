package com.redcup.app.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.redcup.app.R;

public class CorneredButton extends View {

	// Fill colors
	private static final int FILL_DEFAULT = Color.WHITE;
	private static final int FILL_CLICKED = Color.YELLOW;
	private static final int FILL_DISABLED = Color.LTGRAY;

	// Border colors
	private static final int BORDER_DEFAULT = Color.BLACK;

	// Border thickness
	private float borderThickness = 4;

	// Corner radii
	private float corner_topleft = 20;
	private float corner_topright = 20;
	private float corner_bottomright = 20;
	private float corner_bottomleft = 20;

	// Insets
	private float insets_top = 20;
	private float insets_left = 20;
	private float insets_bottom = 20;
	private float insets_right = 20;

	// Icon
	private Drawable icon = null;

	// Icon scaling
	private boolean symmetricIconScaling = true;

	public CorneredButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initialize();
	}

	public CorneredButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize();
	}

	public CorneredButton(Context context) {
		super(context);
		this.initialize();
	}

	private void initialize() {
		this.setClickable(true);

		this.icon = new BitmapDrawable(BitmapFactory.decodeResource(
				getResources(), R.drawable.remove_participant));
	}

	/**
	 * Used to enable or disable symmetric icon scaling.
	 * 
	 * @param symmetric
	 *            if {@code true}, icon will be scaled symmetrically.
	 */
	public void setSymmetricIconScaling(boolean symmetric) {
		this.symmetricIconScaling = symmetric;
		this.invalidate();
	}

	/**
	 * Returns {@code true} if this {@code CorneredButton} is using symmetric
	 * icon scaling.
	 * 
	 * @return {@code true} if this {@code CorneredButton} is using symmetric
	 *         icon scaling.
	 */
	public boolean getSymmetricScaling() {
		return this.symmetricIconScaling;
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
		} else {
			Log.v("onTouchEvent()", "MotionEvent = " + event.getActionMasked());
		}

		return result;
	}

	public void setDrawable(Drawable icon) {
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

	public void setBorderThickness(float borderThickness) {
		this.borderThickness = borderThickness;
		this.invalidate();
	}

	public float getBorderThickness() {
		return this.borderThickness;
	}

	/**
	 * Sets the radius of each corner.
	 * 
	 * @param tl
	 *            top left radius.
	 * @param tr
	 *            top right radius.
	 * @param br
	 *            bottom right radius.
	 * @param bl
	 *            bottom left radius.
	 */
	public void setCorners(float tl, float tr, float br, float bl) {
		this.corner_topleft = tl;
		this.corner_topright = tr;
		this.corner_bottomright = br;
		this.corner_bottomleft = bl;
		this.invalidate();
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

	/**
	 * Draws a rounded rectangle with the given corner radii.
	 * 
	 * @param canvas
	 *            the {@code Canvas} to drop on.
	 * @param paint
	 *            the {@code Paint} to draw with.
	 * @param rect
	 *            the rectangle to draw.
	 * @param rtl
	 *            the top left corner radius.
	 * @param rtr
	 *            the top right corner radius.
	 * @param rbl
	 *            the bottom left corner radius.
	 * @param rbr
	 *            the bottom right corner radius.
	 */
	protected static void drawRoundedRect(Canvas canvas, Paint paint,
			RectF rect, float rtl, float rtr, float rbr, float rbl) {
		// Apply bounds to radii
		rtl = Math.max(rtl, 0);
		rtr = Math.max(rtr, 0);
		rbr = Math.max(rbr, 0);
		rbl = Math.max(rbl, 0);

		// Build path; start at top left and move clockwise
		Path path = new Path();
		path.moveTo(rect.left + rtl, rect.top);
		path.lineTo(rect.right - rtr, rect.top);
		path.arcTo(new RectF(rect.right - rtr * 2, rect.top, rect.right,
				rect.top + rtr * 2), -90, 90);
		path.lineTo(rect.right, rect.bottom - rbr);
		path.arcTo(new RectF(rect.right - rbr * 2, rect.bottom - rbr * 2,
				rect.right, rect.bottom), 0, 90);
		path.lineTo(rect.left + rbl, rect.bottom);
		path.arcTo(new RectF(rect.left, rect.bottom - rbl * 2, rect.left + rbl
				* 2, rect.bottom), 90, 90);
		path.lineTo(rect.left, rect.top + rtl);
		path.arcTo(new RectF(rect.left, rect.top, rect.left + rtl * 2, rect.top
				+ rtl * 2), 180, 90);

		// Draw the path
		canvas.drawPath(path, paint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);

		// Set colors according to state
		int fillColor = FILL_DEFAULT;
		if (this.isPressed()) {
			fillColor = FILL_CLICKED;
		}
		if (!this.isEnabled()) {
			fillColor = FILL_DISABLED;
		}

		// Draw border
		paint.setColor(BORDER_DEFAULT);
		drawRoundedRect(canvas, paint,
				new RectF(0, 0, this.getWidth(), this.getHeight()),
				this.corner_topleft, this.corner_topright,
				this.corner_bottomright, this.corner_bottomleft);

		// Draw fill
		paint.setColor(fillColor);
		drawRoundedRect(canvas, paint,
				new RectF(borderThickness, borderThickness, this.getWidth()
						- borderThickness, this.getHeight() - borderThickness),
				this.corner_topleft - borderThickness, this.corner_topright
						- borderThickness, this.corner_bottomright
						- borderThickness, this.corner_bottomleft
						- borderThickness);

		// Draw the icon
		if (this.icon != null) {
			// Compute icon sizes
			float iconAvailableWidth = this.getWidth() - this.insets_left
					- this.insets_right;
			float iconAvailableHeight = this.getHeight() - this.insets_top
					- this.insets_bottom;
			float iconWidth = this.icon.getMinimumWidth();
			float iconHeight = this.icon.getMinimumHeight();

			// Compute the icon scaling factor
			float iconScaleX = iconAvailableWidth / iconWidth;
			float iconScaleY = iconAvailableHeight / iconHeight;
			if (this.symmetricIconScaling) {
				iconScaleX = Math.min(iconScaleX, iconScaleY);
				iconScaleY = iconScaleX;
			}
			iconWidth *= iconScaleX;
			iconHeight *= iconScaleY;
			float tx = this.insets_left + (iconAvailableWidth - iconWidth) / 2;
			float ty = this.insets_top + (iconAvailableHeight - iconHeight) / 2;

			// Draw the icon
			this.icon.setBounds((int) tx, (int) ty, (int) (tx + iconWidth),
					(int) (ty + iconHeight));
			this.icon.draw(canvas);
		}
	}
}
