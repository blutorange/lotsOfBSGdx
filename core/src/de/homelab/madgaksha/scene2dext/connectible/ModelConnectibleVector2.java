package de.homelab.madgaksha.scene2dext.connectible;

import de.homelab.madgaksha.safemutable.SafeMutableVector2;

public class ModelConnectibleVector2 extends ModelConnectibleSafeMutable<SafeMutableVector2>  {
	public ModelConnectibleVector2(final SafeMutableVector2 value) {
		super(value);
	}
	@Override
	protected void sanitizeValue(final SafeMutableVector2 v) {
	}
}
