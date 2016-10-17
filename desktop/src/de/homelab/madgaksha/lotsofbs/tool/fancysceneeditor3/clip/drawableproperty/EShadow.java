package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import java.util.Collection;

import javax.naming.InsufficientResourcesException;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.ClipDrawable;

public class EShadow implements DrawableEffect {
	private float delay;
	private float timeBefore, timeAfter;
	private int countBefore, countAfter;
	private String interpolationNameBefore, interpolationNameAfter;
	private Interpolation interpolationBefore, interpolationAfter;

	/** @deprecated Serialization */
	@Deprecated
	public EShadow() {
	}

	public EShadow(final float delay) {
		this.delay = delay;
	}

	@Override
	public void write(final Json json) {
		json.writeValue("delay", delay);
		json.writeValue("timeBefore", timeBefore);
		json.writeValue("timeAfter", timeAfter);
		json.writeValue("countBefore", countBefore);
		json.writeValue("countAfter", countAfter);
		json.writeValue("interpolationNameBefore", interpolationNameBefore);
		json.writeValue("interpolationNameAfter", interpolationNameAfter);
	}

	@Override
	public void read(final Json json, final JsonValue jsonData) {
		delay = json.readValue("delay", Float.class, jsonData);
		timeBefore = json.readValue("timeBefore", Float.class, jsonData);
		timeAfter = json.readValue("timeAfter", Float.class, jsonData);
		countBefore = json.readValue("countBefore", Integer.class, jsonData);
		countAfter = json.readValue("countAfter", Integer.class, jsonData);
		interpolationNameBefore = json.readValue("interpolationNameBefore", String.class, jsonData);
		interpolationNameAfter = json.readValue("interpolationNameAfter", String.class, jsonData);
	}

	@Override
	public Collection<AFancyEvent> toFancyEventList(final String key, final int zIndex, final ClipDrawable clipDrawable, float deltaTime) throws InsufficientResourcesException {
		deltaTime += delay;
		final float adjustedStartTime = clipDrawable.getStartTime() + deltaTime;
		return clipDrawable.compileJavaWithoutEffects(key, deltaTime, adjustedStartTime);
	}
}