package de.homelab.madgaksha.enums;

import de.homelab.madgaksha.entityengine.entityutils.ComponentUtils;

/**
 * Earthquake parameters for {@link ComponentUtils#enableScreenQuake(de.homelab.madgaksha.entityengine.entityutils.EQuake)}
 * @author madgaksha
 *
 */
public enum RichterScale {
	M1(0.5f,8.0f),
	M2(0.75f,15.0f),
	M3(2.0f,15.0f),
	M4(5.0f,12.0f),
	M5(7.5f,15.0f),
	M6(10.0f,30.0f),
	M7(10.0f,60.0f),
	M8(18.0f,60.0f),
	M9(36.0f,120.0f),
	M10(100.0f,120.0f);
	
	public final float amplitude;
	public final float frequency;
	public final static RichterScale[] numeric = new RichterScale[]{
			RichterScale.M1,
			RichterScale.M2,
			RichterScale.M3,
			RichterScale.M4,
			RichterScale.M5,
			RichterScale.M6,
			RichterScale.M7,
			RichterScale.M8,
			RichterScale.M9,
			RichterScale.M10
	};
	
	private RichterScale(float amplitude, float frequency) {
		this.amplitude = amplitude;
		this.frequency = frequency;
	}
}
