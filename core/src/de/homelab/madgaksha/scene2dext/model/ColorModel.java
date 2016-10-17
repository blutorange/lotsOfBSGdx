package de.homelab.madgaksha.scene2dext.model;

import com.badlogic.gdx.graphics.Color;

public class ColorModel implements SingleValueModel<Color> {

	private final Color color = new Color(Color.WHITE);
	private final Color publicColor = new Color();

	public ColorModel() {
		this(Color.WHITE);
	}

	public ColorModel(final Color color) {
		this.color.set(color);
	}

	@Override
	public void setValue(final Color value) {
		color.set(value);
	}

	@Override
	public Color getValue() {
		return publicColor.set(color);
	}

	public void setR(final float r) {
		color.r = r;
		color.clamp();
	}

	public void setG(final float g) {
		color.g = g;
		color.clamp();
	}

	public void setB(final float b) {
		color.b = b;
		color.clamp();
	}
	public void setA(final float a) {
		color.a = a;
		color.clamp();
	}
}
