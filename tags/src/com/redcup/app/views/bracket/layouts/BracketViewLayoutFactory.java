package com.redcup.app.views.bracket.layouts;

import com.redcup.app.model.BracketStrategy;
import com.redcup.app.model.SingleEliminationBracketStrategy;
import com.redcup.app.model.Tournament;
import com.redcup.app.views.bracket.BracketView;

public final class BracketViewLayoutFactory {
	private BracketViewLayoutFactory() {
	}

	public static BracketViewLayout createLayout(BracketView view, Tournament t) {
		BracketViewLayout layout = null;
		BracketStrategy strategy = t.getStrategy();

		// Make sure the strategy is not null
		if (strategy != null) {
			if (strategy instanceof SingleEliminationBracketStrategy) {
				// SingleEliminationBracketStrategy
				layout = new SingleEliminationLayout(view,
						(SingleEliminationBracketStrategy) strategy);
			}
		}

		return layout;
	}
}
