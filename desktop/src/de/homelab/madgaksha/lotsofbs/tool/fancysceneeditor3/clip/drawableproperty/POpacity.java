package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyOpacity;

public class POpacity implements DrawableProperty {
	public float time;
	public float opacity;

	@Override
	public AFancyEvent toFancyEvent(final String key, final int zIndex, final float deltaTime) {
		return new FancyOpacity(time + deltaTime, zIndex, key, opacity);
	}

	@Override
	public void write(final Json json) {
		json.writeValue("time", time);
		json.writeValue("opacity", opacity);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		time = json.readValue(Float.class, jsonData);
		opacity = json.readValue(Float.class, jsonData);
	}
}