package de.homelab.madgaksha.safemutable;

import com.badlogic.gdx.math.Vector2;

public class SafeMutableVector2 extends Vector2 implements SafeMutable<SafeMutableVector2> {
	private static final long serialVersionUID = 1L;
	public SafeMutableVector2() {
		super();
	}
	public SafeMutableVector2(final Vector2 v) {
		super(v);
	}
	@Override
	public SafeMutableVector2 cloneValue() {
		return new SafeMutableVector2(this);
	}
	@Override
	public void setValue(final SafeMutableVector2 value) {
		set(value);
	}
}
