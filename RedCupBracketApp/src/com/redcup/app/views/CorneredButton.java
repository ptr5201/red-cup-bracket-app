package com.redcup.app.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.redcup.app.R;

public class CorneredButton extends View {

	// Fill colors
	private static final int FILL_DEFAULT = Color.WHITE;
	private static final int FILL_CLICKED = Color.YELLOW;

	// Border colors
	private static final int BORDER_DEFAULT = Color.RED;

	// Border thickness
	private static final int BORDER_THICKNESS = 4;

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
	private Bitmap icon = null;

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
		this.icon = BitmapFactory.decodeResource(getResources(),
				R.drawable.remove_participant);
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
		this.invalidate();
	}

	public void setIcon(int resid) {
		this.icon = BitmapFactory.decodeResource(this.getResources(), resid);
		this.invalidate();
	}

	public Bitmap getIcon() {
		return this.icon;
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
	protected void drawRoundedRect(Canvas canvas, Paint paint, RectF rect,
			float rtl, float rtr, float rbr, float rbl) {
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
		paint.setStyle(Style.FILL);

		// Set colors according to state
		int fillColor = FILL_DEFAULT;
		if (this.isPressed()) {
			fillColor = FILL_CLICKED;
		}

		// Draw background
		paint.setColor(BORDER_DEFAULT);
		this.drawRoundedRect(canvas, paint, new RectF(0, 0, this.getWidth(),
				this.getHeight()), this.corner_topleft, this.corner_topright,
				this.corner_bottomright, this.corner_bottomleft);
		paint.setColor(fillColor);
		this.drawRoundedRect(
				canvas,
				paint,
				new RectF(BORDER_THICKNESS, BORDER_THICKNESS, this.getWidth()
						- BORDER_THICKNESS, this.getHeight() - BORDER_THICKNESS),
				this.corner_topleft - BORDER_THICKNESS, this.corner_topright
						- BORDER_THICKNESS, this.corner_bottomright
						- BORDER_THICKNESS, this.corner_bottomleft
						- BORDER_THICKNESS);

		// Draw the icon
		if (this.icon != null) {
			// Compute icon sizes
			float iconAvailableWidth = this.getWidth() - this.insets_left
					- this.insets_right;
			float iconAvailableHeight = this.getHeight() - this.insets_top
					- this.insets_bottom;
			float iconWidth = this.icon.getWidth();
			float iconHeight = this.icon.getHeight();

			// Compute the icon scaling factor
			float iconScaleX = iconAvailableWidth / iconWidth;
			float iconScaleY = iconAvailableHeight / iconHeight;
			iconScaleX = Math.min(iconScaleX, iconScaleY);
			iconScaleY = iconScaleX;

			// Build the transformation matrix
			Matrix matrix = new Matrix();
			float tx = this.insets_left
					+ (iconAvailableWidth - iconWidth * iconScaleX) / 2;
			float ty = this.insets_top
					+ (iconAvailableHeight - iconHeight * iconScaleY) / 2;
			matrix.preTranslate(tx, ty);
			matrix.preScale(iconScaleX, iconScaleY);

			// Draw the icon
			canvas.drawBitmap(this.icon, matrix, paint);
		}
	}
}
