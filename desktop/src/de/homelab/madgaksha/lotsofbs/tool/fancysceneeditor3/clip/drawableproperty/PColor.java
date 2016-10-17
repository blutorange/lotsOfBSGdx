package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyColor;

public class PColor implements DrawableProperty {
	public float time;
	public final Color color = new Color();

	@Override
	public AFancyEvent toFancyEvent(final String key, final int zIndex, final float deltaTime) {
		return new FancyColor(time + deltaTime, zIndex, key, color);
	}

	@Override
	public void write(final Json json) {
		json.writeValue("time", time);
		json.writeValue("color", Color.rgba8888(color));
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		time = json.readValue(Float.class, jsonData);
		color.set(json.readValue(Integer.class, jsonData));
	}
}