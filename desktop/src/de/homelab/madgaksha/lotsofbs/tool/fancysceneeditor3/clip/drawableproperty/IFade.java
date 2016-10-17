package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyFade;

public class IFade implements DrawableInterpolation<POpacity> {
	public String interpolation;

	@Override
	public AFancyEvent toFancyEvent(final String key, final int zIndex, final POpacity p1, final POpacity p2,
			final float deltaTime) {
		return new FancyFade(p1.time + deltaTime, zIndex, key, p2.opacity, interpolation, p2.time - p1.time);
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