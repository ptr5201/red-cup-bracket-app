package com.redcup.app.views.themes;

import android.R;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class FlatButtonTheme extends Drawable {

	@Override
	public void draw(Canvas canvas) {
		final int BORDER_SIZE = 2;
		final int CORNER_SIZE = 10;
		final int[] stateSet = this.getState();
		final Rect bounds = getBounds();

		boolean isEnabled = false;
		boolean isSelected = false;
		for (int state : stateSet) {
			isEnabled = isEnabled || (state == R.attr.state_enabled);
			isSelected = isSelected
					|| (state == R.attr.state_checked || state == R.attr.state_focused);
		}

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);

		int color = Color.WHITE;
		if (isSelected) {
			color = Color.YELLOW;
		}
		if (!isEnabled) {
			color = (color & 0x00FFFFFF) | 0x7F000000;
		}

		// Draw border
		paint.setColor(Color.BLACK);
		canvas.drawRoundRect(new RectF(bounds.left, bounds.top, bounds.right,
				bounds.bottom), CORNER_SIZE, CORNER_SIZE, paint);

		// Draw fill
		paint.setColor(color);
		canvas.drawRoundRect(new RectF(bounds.left + BORDER_SIZE, bounds.top
				+ BORDER_SIZE, bounds.right - BORDER_SIZE, bounds.bottom
				- BORDER_SIZE), CORNER_SIZE - BORDER_SIZE, CORNER_SIZE
				- BORDER_SIZE, paint);
	}

	@Override
	public boolean setState(int[] stateSet) {
		super.setState(stateSet);
		return true;
	}

	@Override
	public boolean isStateful() {
		return true;
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub

	}

}
