package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancySlide;

public class ISlide implements DrawableInterpolation<PCrop> {
	public String interpolation;

	@Override
	public AFancyEvent toFancyEvent(final String key, final int zIndex, final PCrop p1, final PCrop p2,
			final float deltaTime) {
		return new FancySlide(p1.time + deltaTime, zIndex, key, p2.time - p1.time, interpolation, p2.left,
				p2.bottom, p2.right, p2.top);
	}

	@Override
	public void write(final Json json) {
		json.writeValue("interpolation", interpolation);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		json.readValue(String.class, jsonData);
	}
}