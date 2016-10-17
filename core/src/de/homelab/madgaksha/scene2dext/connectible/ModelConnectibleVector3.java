package de.homelab.madgaksha.scene2dext.connectible;

import de.homelab.madgaksha.safemutable.SafeMutableVector3;

public class ModelConnectibleVector3 extends ModelConnectibleSafeMutable<SafeMutableVector3>  {
	public ModelConnectibleVector3(final SafeMutableVector3 value) {
		super(value);
	}
	@Override
	protected void sanitizeValue(final SafeMutableVector3 v) {
	}
}
