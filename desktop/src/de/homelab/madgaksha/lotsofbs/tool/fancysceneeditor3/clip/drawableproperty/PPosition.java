package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyPosition;

public class PPosition implements DrawableProperty {
	public float time;
	public float x;
	public float y;
	public float dx;
	public float dy;

	/** @deprecated Serialization. */
	@Deprecated
	public PPosition() {
	}

	public PPosition(final float time, final float x, final float y, final float dx, final float dy) {
		this.time = Math.max(0f, time);
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public AFancyEvent toFancyEvent(final String key, final int zIndex, final float deltaTime) {
		return new FancyPosition(time + deltaTime, zIndex, key, x, y, dx, dy);
	}

	@Override
	public void write(final Json json) {
		json.writeValue("time", time);
		json.writeValue("x", x);
		json.writeValue("y", y);
		json.writeValue("dx", dx);
		json.writeValue("dy", dy);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		time = json.readValue(Float.class, jsonData);
		x = json.readValue(Float.class, jsonData);
		y = json.readValue(Float.class, jsonData);
		dx = json.readValue(Float.class, jsonData);
		dy = json.readValue(Float.class, jsonData);
	}
}