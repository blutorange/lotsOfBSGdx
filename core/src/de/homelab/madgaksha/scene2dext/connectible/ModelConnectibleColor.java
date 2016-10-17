package de.homelab.madgaksha.scene2dext.connectible;

import de.homelab.madgaksha.safemutable.SafeMutableColor;

public class ModelConnectibleColor extends ModelConnectibleSafeMutable<SafeMutableColor> {
	public ModelConnectibleColor(final SafeMutableColor value) {
		super(value);
	}
	@Override
	protected void sanitizeValue(final SafeMutableColor v) {
	}
}
