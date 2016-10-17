package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyMove;
import de.homelab.madgaksha.lotsofbs.path.APath;

public class IMove implements DrawableInterpolation<PPosition> {
	public String interpolation;
	public APath path;

	@Override
	public AFancyEvent toFancyEvent(final String key, final int zIndex, final PPosition p1, final PPosition p2,
			final float deltaTime) {
		path.setOrigin(p1.x, p1.y);
		path.setTMax(p2.time - p1.time);
		return new FancyMove(p1.time + deltaTime, zIndex, key, path, interpolation);
	}

	@Override
	public void write(final Json json) {
		json.writeValue("path", path);
		json.writeValue("interpolation", interpolation);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		path = json.readValue(null, jsonData);
		interpolation = json.readValue(String.class, jsonData);
	}
}