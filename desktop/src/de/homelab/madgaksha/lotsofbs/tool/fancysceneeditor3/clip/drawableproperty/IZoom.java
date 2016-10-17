package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyZoom;

public class IZoom implements DrawableInterpolation<PScale> {
	public String interpolation;

	@Override
	public AFancyEvent toFancyEvent(final String key, final int zIndex, final PScale p1, final PScale p2,
			final float deltaTime) {
		return new FancyZoom(p1.time + deltaTime, zIndex, key, p2.scaleX, p2.scaleY, interpolation,
				p2.time - p1.time);
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