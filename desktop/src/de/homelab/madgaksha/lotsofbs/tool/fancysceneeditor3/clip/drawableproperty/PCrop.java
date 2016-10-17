package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyCrop;

public class PCrop implements DrawableProperty {
	public float time;
	public float left, right, top, bottom;

	@Override
	public AFancyEvent toFancyEvent(final String key, final int zIndex, final float deltaTime) {
		return new FancyCrop(time + deltaTime, zIndex, key, left, bottom, right, top);
	}

	@Override
	public void write(final Json json) {
		json.writeValue("time", time);
		json.writeValue("left", left);
		json.writeValue("right", right);
		json.writeValue("bottom", bottom);
		json.writeValue("top", top);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		time = json.readValue(Float.class, jsonData);
		left = json.readValue(Float.class, jsonData);
		right = json.readValue(Float.class, jsonData);
		bottom = json.readValue(Float.class, jsonData);
		top = json.readValue(Float.class, jsonData);
	}
}