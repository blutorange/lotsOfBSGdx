package de.homelab.madgaksha.safemutable;

import com.badlogic.gdx.graphics.Color;

public class SafeMutableColor extends Color implements SafeMutable<SafeMutableColor> {
	public SafeMutableColor() {
		super();
	}
	public SafeMutableColor(final Color c) {
		super(c);
	}
	@Override
	public SafeMutableColor cloneValue() {
		return new SafeMutableColor(this);
	}
	@Override
	public void setValue(final SafeMutableColor value) {
		set(value);
	}
}