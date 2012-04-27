package com.redcup.app.views.bracket.layouts;

import com.redcup.app.views.bracket.BracketView;

/**
 * Abstract class defining the interfaces required to implement a custom layout
 * based on a bracket tree. Subclasses define methods used to compute the size
 * and position of elements in the parent control.
 * 
 * @author Jackson Lamp
 */
public abstract class BracketViewLayout {
	// Margin settings
	private int margin_top = 10;
	private int margin_bottom = 10;
	private int margin_left = 10;
	private int margin_right = 10;

	// Vertical spacing and sizing allocation
	private int spacing_vertical = 10;
	private int sizing_vertical = 120;

	// Horizontal spacing and sizing allocation
	private int spacing_horizontal = 40;
	private int sizing_horizontal = 240;

	// Measured width and height; used to communicate measurements with
	// BracketView.
	private int measuredHeight;
	private int measuredWidth;

	/**
	 * The frozen/thawed status of this {@code BracketViewLayout}. If
	 * {@code true}, the layout will not be updated until this
	 * {@code BracketViewLayout} is thawed.
	 */
	private boolean isFrozen = false;

	/**
	 * Indicates that an internal change (margin/sizing/spacing/context) has
	 * been made.
	 */
	private boolean hasChangedInternally = false;

	/**
	 * The {@code BracketView} that this {@code BracketViewLayout} manages.
	 */
	private BracketView bracketView = null;

	/**
	 * Creates a {@code BracketViewLayout}.
	 * 
	 * @param bracketView
	 *            the {@code BracketView} to manage.
	 */
	public BracketViewLayout(BracketView bracketView) {
		this.bracketView = bracketView;
	}

	/**
	 * Freezes the layout manager. The layout will not update until the
	 * {@link thaw} is called.
	 */
	public void freeze() {
		this.isFrozen = true;
	}

	/**
	 * Thaws the layout manager. The layout will resume updating in once this is
	 * called.
	 */
	public void thaw() {
		if (this.isFrozen) {
			this.isFrozen = false;
			this.refresh();
		}
	}

	/**
	 * Returns {@code true} if the layout is frozen, otherwise returns
	 * {@code false} to indicate that this manager is thawed.
	 */
	public boolean isFrozen() {
		return this.isFrozen;
	}

	/**
	 * When called, sets the {@code BracketViewLayout}'s
	 * {@code hasChangedInternal} flag. This forces the layout to update on the
	 * next layout pass regardless of whether the allocated space of the
	 * {@code BracketView} has changed.
	 */
	protected void setInteralChanged() {
		this.hasChangedInternally = true;
	}

	/**
	 * When called, clears the {@code BracketViewLayout}'s
	 * {@code hasChangedInternal} flag. This forces the layout to update on the
	 * next layout pass regardless of whether the allocated space of the
	 * {@code BracketView} has changed.
	 */
	protected void clearInteralChanged() {
		this.hasChangedInternally = false;
	}

	/**
	 * Returns {@code true} if this {@code BracketViewLayout}'s state has
	 * changed.
	 * 
	 * @return {@code true} if this {@code BracketViewLayout}'s state has
	 *         changed.
	 */
	protected boolean hasChangedInternally() {
		return this.hasChangedInternally;
	}

	/**
	 * Indicates that a change has been made internally and that the layout
	 * needs to be updated.
	 */
	protected void refresh() {
		// Prevent update if frozen
		if (!this.isFrozen) {
			// Indicate that there is an internal change
			this.hasChangedInternally = true;

			// Notify the context (if any)
			if (this.bracketView != null) {
				this.bracketView.invalidate();
			}
		}
	}

	/**
	 * Sets the {@code BracketView} that this {@code BracketViewLayout} manages.
	 * 
	 * @param bracketView
	 *            the {@code BracketView} that this {@code BracketViewLayout}
	 *            manages.
	 */
	public void setBracketView(BracketView bracketView) {
		this.bracketView = bracketView;
		this.refresh();
	}

	/**
	 * Returns the {@code BracketView} that this {@code BracketViewLayout}
	 * manages.
	 * 
	 * @return the {@code BracketView} that this {@code BracketViewLayout}
	 *         manages.
	 */
	public BracketView getBracketView() {
		return this.bracketView;
	}

	/**
	 * Set the amount of space requested by this layout.
	 * 
	 * @param measuredWidth
	 *            the amount of horizontal space requested by this layout.
	 * @param measuredHeight
	 *            the amount of vertical space requested by this layout.
	 */
	protected void setMeasuredDimension(int measuredWidth, int measuredHeight) {
		this.measuredWidth = measuredWidth;
		this.measuredHeight = measuredHeight;
	}

	/**
	 * Returns the amount of horizontal space requested by this layout.
	 * 
	 * @return the amount of horizontal space requested by this layout.
	 */
	public int getMeasuredWidth() {
		return this.measuredWidth;
	}

	/**
	 * Returns the amount of vertical space requested by this layout.
	 * 
	 * @return the amount of vertical space requested by this layout.
	 */
	public int getMeasuredHeight() {
		return this.measuredHeight;
	}

	/**
	 * Sets the internal margins of the {@code BracketViewLayout}.
	 * 
	 * @param left
	 *            the left margin.
	 * @param top
	 *            the top margin.
	 * @param right
	 *            the right margin.
	 * @param bottom
	 *            the bottom margin.
	 */
	public void setMargins(int left, int top, int right, int bottom) {
		this.margin_left = left;
		this.margin_top = top;
		this.margin_right = right;
		this.margin_bottom = bottom;
		this.refresh();
	}

	/**
	 * Returns the left internal margin.
	 * 
	 * @return the left internal margin.
	 */
	public int getLeftMargin() {
		return this.margin_left;
	}

	/**
	 * Returns the top internal margin.
	 * 
	 * @return the top internal margin.
	 */
	public int getTopMargin() {
		return this.margin_top;
	}

	/**
	 * Returns the right internal margin.
	 * 
	 * @return the right internal margin.
	 */
	public int getRightMargin() {
		return this.margin_right;
	}

	/**
	 * Returns the bottom internal margin.
	 * 
	 * @return the bottom internal margin.
	 */
	public int getBottomMargin() {
		return this.margin_bottom;
	}

	/**
	 * Sets the vertical spacing between elements.
	 * 
	 * @param vSpacing
	 *            the amount of spacing to place between elements.
	 */
	public void setVerticalSpacing(int vSpacing) {
		this.spacing_vertical = vSpacing;
		this.refresh();
	}

	/**
	 * Returns the amount of spacing between elements.
	 * 
	 * @return the amount of spacing between elements.
	 */
	public int getVerticalSpacing() {
		return this.spacing_vertical;
	}

	/**
	 * Sets the amount of vertical space allocated to each element.
	 * 
	 * @param vSizing
	 *            the amount of vertical space allocated to each element.
	 */
	public void setVerticalSizing(int vSizing) {
		this.sizing_vertical = vSizing;
		this.refresh();
	}

	/**
	 * Returns the amount of vertical space allocated to each element.
	 * 
	 * @return the amount of vertical space allocated to each element.
	 */
	public int getVerticalSizing() {
		return this.sizing_vertical;
	}

	/**
	 * Sets the horizontal spacing between columns.
	 * 
	 * @param hSpacing
	 *            the horizontal spacing between columns.
	 */
	public void setHorizontalSpacing(int hSpacing) {
		this.spacing_horizontal = hSpacing;
		this.refresh();
	}

	/**
	 * Returns the horizontal spacing between columns.
	 * 
	 * @return the horizontal spacing between columns.
	 */
	public int getHorizontalSpacing() {
		return this.spacing_horizontal;
	}

	/**
	 * Sets the amount of horizontal space allocated to each element.
	 * 
	 * @param hSizing
	 *            the amount of horizontal space allocated to each element.
	 */
	public void setHorizontalSizing(int hSizing) {
		this.sizing_horizontal = hSizing;
		this.refresh();
	}

	/**
	 * Returns the amount of horizontal space allocated to each element.
	 * 
	 * @return the amount of horizontal space allocated to each element.
	 */
	public int getHorizontalSizing() {
		return this.sizing_horizontal;
	}

	/**
	 * Performs the layout for the bracket.
	 * 
	 * @param changed
	 *            {@code true} if available size or position of this component
	 *            in question has changed.
	 * @param l
	 *            left position, relative to the parent.
	 * @param t
	 *            top position, relative to the parent.
	 * @param r
	 *            right position, relative to the parent.
	 * @param b
	 *            bottom position, relative to the parent.
	 */
	public abstract void onLayout(boolean changed, int l, int t, int r, int b);

	/**
	 * Measures the amount of space required for this layout.
	 */
	public abstract void updateSizeRequirements();
}
