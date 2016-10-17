package de.homelab.madgaksha.safemutable;

import com.badlogic.gdx.math.Vector3;

public class SafeMutableVector3 extends Vector3 implements SafeMutable<SafeMutableVector3> {
	private static final long serialVersionUID = 1L;
	public SafeMutableVector3() {
		super();
	}
	public SafeMutableVector3(final Vector3 v) {
		super(v);
	}
	@Override
	public SafeMutableVector3 cloneValue() {
		return new SafeMutableVector3(this);
	}
	@Override
	public void setValue(final SafeMutableVector3 value) {
		set(value);
	}
}