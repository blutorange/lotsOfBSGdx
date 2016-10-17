package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyScale;

public class PScale implements DrawableProperty {
	public float time;
	public float scaleX, scaleY;

	@Override
	public AFancyEvent toFancyEvent(final String key, final int zIndex, final float deltaTime) {
		return new FancyScale(time + deltaTime, zIndex, key, scaleX, scaleY);
	}

	@Override
	public void write(final Json json) {
		json.writeValue("time", time);
		json.writeValue("scaleX", scaleX);
		json.writeValue("scaleY", scaleY);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		time = json.readValue(Float.class, jsonData);
		scaleX = json.readValue(Float.class, jsonData);
		scaleY = json.readValue(Float.class, jsonData);
	}
}