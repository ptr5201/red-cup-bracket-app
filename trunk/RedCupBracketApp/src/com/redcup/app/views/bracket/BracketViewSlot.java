package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public abstract class BracketViewSlot extends View {
	public BracketViewSlot(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BracketViewSlot(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BracketViewSlot(Context context) {
		super(context);
	}

	@Override
	protected abstract void onDraw(Canvas canvas);
}
