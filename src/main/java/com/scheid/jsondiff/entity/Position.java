package com.scheid.jsondiff.entity;

/**
 * Enum used to determine the possible positions a single {@link Element} can assume in a {@link Comparison}.
 * Also determines the position's behavior under a {@link Comparison}:
 * fixedPosition=true: A new {@link Element} on the same Position and {@link Comparison} will replace the previous one.
 * fixedPosition=false: A new {@link Element} on the same Position and {@link Comparison} will add another {@link Element} to the {@link Comparison}. 
 */
public enum Position {
	
	LEFT(true),
	RIGHT(true),
	UNDEFINED(false);
	
	private boolean fixedPosition;
	
	Position(boolean fixedPosition) {
		this.fixedPosition = fixedPosition;
	}
	
	public boolean isFixedPosition() {
		return this.fixedPosition;
	}
	
}
