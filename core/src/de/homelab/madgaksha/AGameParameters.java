package de.homelab.madgaksha;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.logging.Level;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Json.Serializable;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.level.ALevel;

public abstract class AGameParameters implements Serializable {
	private final static Logger LOG = Logger.getLogger(AGameParameters.class);
	public AGameParameters(){}	
	public Locale requestedLocale;
	public int requestedWidth;
	public int requestedHeight;
	public boolean requestedFullscreen;
	public int requestedFps;
	public int requestedLogLevel;
	public ALevel requestedLevel;
	public String requestedWindowTitle;
	private boolean deserializedSuccessfully = false;
	@Override
	public void write(Json json) {
		json.setSerializer(Locale.class, new LocaleSerializer());
		json.writeValue("levelClass", requestedLevel.getClass().getCanonicalName());
		json.writeValue("requestedLocale", requestedLocale);
		json.writeValue("requestedWidth", requestedWidth);
		json.writeValue("requestedHeight", requestedHeight);
		json.writeValue("requestedFullscreen", requestedFullscreen);
		json.writeValue("requestedFps", requestedFps);
		json.writeValue("requestedLogLevel", requestedLogLevel);
		json.writeValue("requestedLevel", requestedLevel);
		json.writeValue("requestedWindowTitle", requestedWindowTitle);
	}
	
	@Override
	public void read(Json json, JsonValue jsonData) {
		final String levelClass = jsonData.get("levelClass").asString();
		final JsonValue levelValue = jsonData.get("requestedLevel");
		Class<ALevel> aLevelClass;
		ALevel level;
		try {
			aLevelClass = (Class<ALevel>)Class.forName(levelClass);
			level = aLevelClass.newInstance();
		} catch (Exception e) {
			LOG.error("could not load level: " + levelClass,e);
			return;
		}
		level.read(json, jsonData);
		
		requestedLevel = level;
		requestedLocale = new LocaleSerializer().read(json, jsonData, null);
		requestedFps = jsonData.get("requestedFps").asInt();
		requestedWidth = jsonData.get("requestedWidth").asInt();
		requestedFullscreen = jsonData.get("requestedFullscreen").asBoolean();
		requestedLogLevel = jsonData.get("requestedLogLevel").asInt();
		requestedWindowTitle = jsonData.get("requestedWindowTitle").asString();
		deserializedSuccessfully = true;		
	}
	public byte[] toByteArray() {
		final Json json = new Json();
		final String string = json.toJson(this);
		final byte[] data = string.getBytes(Charset.forName("UTF-8"));
		return data;
	}
	public static class Builder {
		private Locale requestedLocale = Locale.getDefault();
		private int requestedWidth = 320;
		private int requestedHeight = 240;
		private boolean requestedFullscreen = false;
		private int requestedFps = 30;
		private int requestedLogLevel = Application.LOG_ERROR;
		private ALevel requestedLevel = null;
		private String requestedWindowTitle = "";
		public Builder(ALevel level) {
			if (level == null) throw new NullPointerException("level cannot be null");
			requestedLevel = level;
		}
		public Builder requestedLocale(Locale x) {
			requestedLocale = x;
			return this;
		}
		public Builder requestedWidth(int x) {
			requestedWidth = x;
			return this;
		}
		public Builder requestedHeight(int x) {
			requestedHeight = x;
			return this;
		}		
		public Builder requestedFullscreen(boolean x) {
			requestedFullscreen = x;
			return this;
		}
		public Builder requestedFps(int x) {
			requestedFps = x;
			return this;
		}
		public Builder requestedLogLevel(int x) {
			requestedLogLevel = x;
			return this;
		}
		public Builder requestedWindowTitle(String x) {
			requestedWindowTitle = x;
			return this;
		}		
		//TODO
		// !!!Change the read/write methods when changing this!!!
		public AGameParameters build() {
			AGameParameters params = new AGameParameters() {};
			params.requestedFps = this.requestedFps;
			params.requestedFullscreen = this.requestedFullscreen;
			params.requestedHeight = this.requestedHeight;
			params.requestedLevel = this.requestedLevel;
			params.requestedLocale = this.requestedLocale;
			params.requestedLogLevel = this.requestedLogLevel;
			params.requestedWidth = this.requestedWidth;
			params.requestedWindowTitle = this.requestedWindowTitle;
			return params;
		}
	}
	public boolean isDeserializedSuccessfully() {
		return deserializedSuccessfully;
	}
}
