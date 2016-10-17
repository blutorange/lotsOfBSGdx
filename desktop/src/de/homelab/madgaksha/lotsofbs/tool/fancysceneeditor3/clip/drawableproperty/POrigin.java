package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyOrigin;

public class POrigin implements DrawableProperty {
	public float time;
	public float x, y;

	@Override
	public AFancyEvent toFancyEvent(final String key, final int zIndex, final float deltaTime) {
		return new FancyOrigin(time + deltaTime, zIndex, key, x, y);
	}

	@Override
	public void write(final Json json) {
		json.writeValue("time", time);
		json.writeValue("x", x);
		json.writeValue("y", y);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		time = json.readValue(Float.class, jsonData);
		x = json.readValue(Float.class, jsonData);
		y = json.readValue(Float.class, jsonData);
	}
}