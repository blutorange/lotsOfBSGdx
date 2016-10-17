package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;

import javax.naming.InsufficientResourcesException;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.common.IdProvidable;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;

public abstract class AClip implements IClip {

	private float startTime;
	private float duration;
	private int zIndex;

	protected abstract void writeSubclass(Json json);
	protected abstract void readSubclass(Json json, JsonValue jsonData);
	protected abstract Collection<AFancyEvent> compileJava(IdProvidable id, float deltaTime, float adjustedStartTime) throws InsufficientResourcesException;

	@Override
	public void compileBinary(final OutputStream os, final IdProvidable id, final float deltaTime) throws IOException, InsufficientResourcesException {
		try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
			for (final AFancyEvent fe : compileJava(id, deltaTime))
				oos.writeObject(fe);
		}
	}
	@Override
	public byte[] compileBinary(final IdProvidable id, final float deltaTime) throws IOException, InsufficientResourcesException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			compileBinary(baos, id, deltaTime);
			return baos.toByteArray();
		}
	}

	@Override
	public final void write(final Json json) {
		json.writeValue("zIndex", zIndex);
		json.writeValue("startTime", startTime);
		json.writeValue("duration", duration);
		json.writeObjectStart("details");
		writeSubclass(json);
		json.writeObjectEnd();
	}

	@Override
	public final void read(final Json json, final JsonValue jsonData) {
		zIndex = json.readValue(Integer.class, jsonData);
		startTime = json.readValue(Float.class, jsonData);
		duration = json.readValue(Float.class, jsonData);
		readSubclass(json, jsonData);
	}

	@Override
	public final Collection<AFancyEvent> compileJava(final IdProvidable id, final float deltaTime) throws InsufficientResourcesException {
		return compileJava(id, deltaTime, startTime+deltaTime);
	}


	public void setStartTime(final float startTime) {
		this.startTime = Math.max(0f, startTime);
	}
	public void setDuration(final float duration) {
		this.duration = Math.max(0f, duration);
	}
	@Override
	public float getStartTime() {
		return startTime;
	}

	@Override
	public float getDuration() {
		return duration;
	}

	public int getZIndex() {
		return zIndex;
	}
}
