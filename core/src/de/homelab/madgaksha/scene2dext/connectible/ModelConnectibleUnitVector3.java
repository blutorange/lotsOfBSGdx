package de.homelab.madgaksha.scene2dext.connectible;

import de.homelab.madgaksha.safemutable.SafeMutableVector3;

/**
 * A vector that will always be normed.
 * @author madgaksha
 *
 */
public class ModelConnectibleUnitVector3 extends ModelConnectibleVector3 {
	public ModelConnectibleUnitVector3(final SafeMutableVector3 value) {
		super(value);
	}

	@Override
	protected void sanitizeValue(final SafeMutableVector3 v) {
		super.sanitizeValue(v);
		if (v.isZero())
			v.set(1f, 0f, 0f);
		else
			v.nor();
	}
}
