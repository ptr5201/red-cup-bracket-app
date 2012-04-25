package com.redcup.app.views.bracket;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.redcup.app.views.themes.FlatButtonTheme;

public class BracketViewSlot extends ViewGroup {

	private BracketSlotButton slotButton;
	private Button removeButton;
	private PaintDrawable background = new PaintDrawable(Color.LTGRAY);

	private int collapsedHeight = 60;
	private int collapsedWidth = 240;

	private int expandedHeight = 120;
	private int expandedWidth = 320;

	public BracketViewSlot(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initialize(context);
	}

	public BracketViewSlot(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize(context);
	}

	public BracketViewSlot(Context context) {
		super(context);
		this.initialize(context);
	}

	private void initialize(Context context) {
		// Create the slot expansion/editing button
		this.slotButton = new BracketSlotButton(context);
		this.slotButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setSelected(true);
				BracketViewSlot.this.removeButton.setVisibility(VISIBLE);
				BracketViewSlot.this.background.setAlpha(255);
				BracketViewSlot.this.invalidate();
			}
		});
		this.addView(slotButton);

		// Create the "remove" button
		this.removeButton = new Button(context);
		this.removeButton.setText("X");
		this.removeButton.setVisibility(INVISIBLE);
		this.addView(this.removeButton);

		// Background drawable
		this.background.setBounds(0, 0, this.expandedWidth,
				this.collapsedHeight);
		this.background.setCornerRadius(20);
		this.background.setAlpha(0);
		this.setBackgroundDrawable(this.background);
	}

	public void reset() {
		if (this.slotButton != null) {
			this.slotButton.reset();
		}
		if (this.removeButton != null) {
			this.removeButton.setVisibility(INVISIBLE);
			this.background.setAlpha(0);
		}
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
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		this.slotButton.layout(0, 0, this.collapsedWidth, this.collapsedHeight);
		this.removeButton.layout(this.collapsedWidth + 5, 5,
				this.expandedWidth - 5, this.collapsedHeight - 5);
	}
}
