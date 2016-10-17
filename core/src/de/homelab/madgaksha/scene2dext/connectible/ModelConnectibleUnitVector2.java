package de.homelab.madgaksha.scene2dext.connectible;

import de.homelab.madgaksha.safemutable.SafeMutableVector2;

/**
 * A vector that will always be normed.
 * @author madgaksha
 *
 */
public class ModelConnectibleUnitVector2 extends ModelConnectibleVector2 {
	public ModelConnectibleUnitVector2(final SafeMutableVector2 value) {
		super(value);
	}

	@Override
	protected void sanitizeValue(final SafeMutableVector2 v) {
		super.sanitizeValue(v);
		if (v.isZero())
			v.set(1f, 0f);
		else
			v.nor();
	}
}
