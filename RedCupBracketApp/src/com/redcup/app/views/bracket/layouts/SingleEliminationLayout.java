package com.redcup.app.views.bracket.layouts;

import com.redcup.app.model.SingleEliminationBracketStrategy;
import com.redcup.app.views.bracket.BracketView;

public class SingleEliminationLayout extends BracketViewLayout {

	private SingleEliminationBracketStrategy model;

	public SingleEliminationLayout(BracketView context,
			SingleEliminationBracketStrategy model) {
		super(context);
		this.model = model;
	}

	@Override
	public void onLayout(boolean changed, int l, int t, int r, int b) {
		// Check to make sure that we need to actually update anything
		if ((changed || this.hasChangedInternally()) && !this.isFrozen()) {
			// TODO: Implement
			int availableWidth = r - l;
			int availableHeigth = b - t;

			// First column will be evenly spaced according to the given sizing
			// and spacing constraints; remaining columns will be spaced based
			// on the matchups in the previous column
		}
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO: Implement
		// Width = NumRounds * HorizontalSizing +
		// (NumRounds - 1) * HorizontalSpacing +
		// LeftMargin + RightMargin
		// Height = NumEntrants * VerticalSizing +
		// (NumEntrants - 1) * VerticalSpacing +
		// TopMargin + BottomMargin
	}

}
