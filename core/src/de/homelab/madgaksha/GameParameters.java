package de.homelab.madgaksha;

import java.nio.charset.Charset;
import java.util.Locale;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.APlayer;
import de.homelab.madgaksha.util.LocaleSerializer;

public class GameParameters implements Serializable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(GameParameters.class);

	public GameParameters() {
	}

	public Locale requestedLocale;
	public int requestedWidth;
	public int requestedHeight;
	public boolean requestedFullscreen;
	public int requestedFps;
	public int requestedLogLevel;
	public ALevel requestedLevel;
	public APlayer requestedPlayer;
	public String requestedWindowTitle;
	private boolean deserializedSuccessfully = false;

	@Override
	public void write(Json json) {
		json.writeValue("requestedLevel", requestedLevel.getClass().getCanonicalName(), String.class);
		json.writeValue("requestedPlayer", requestedPlayer.getClass().getCanonicalName(), String.class);
		json.writeValue("requestedLocale", requestedLocale);
		json.writeValue("requestedWidth", requestedWidth);
		json.writeValue("requestedHeight", requestedHeight);
		json.writeValue("requestedFullscreen", requestedFullscreen);
		json.writeValue("requestedFps", requestedFps);
		json.writeValue("requestedLogLevel", requestedLogLevel);
		json.writeValue("requestedWindowTitle", requestedWindowTitle);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(Json json, JsonValue jsonData) {
		String levelClass = jsonData.get("requestedLevel").asString();
		String playerClass = jsonData.get("requestedPlayer").asString();
		try {
			Class<ALevel> level = (Class<ALevel>)Class.forName(levelClass);
			Class<APlayer> player = (Class<APlayer>)Class.forName(playerClass);
			requestedLevel = level.newInstance();
			requestedPlayer = player.newInstance();
		}
		catch (Exception e) {
			System.err.println("could not load level " + levelClass + " for player " + playerClass);
			e.printStackTrace(System.err);
			return;
		}
		JsonValue localeValue = jsonData.get("requestedLocale");
		requestedLocale = new LocaleSerializer().read(json, localeValue, null);
		requestedFps = jsonData.get("requestedFps").asInt();
		requestedWidth = jsonData.get("requestedWidth").asInt();
		requestedHeight = jsonData.get("requestedHeight").asInt();
		requestedFullscreen = jsonData.get("requestedFullscreen").asBoolean();
		requestedLogLevel = jsonData.get("requestedLogLevel").asInt();
		requestedWindowTitle = jsonData.get("requestedWindowTitle").asString();
		deserializedSuccessfully = true;
	}

	public byte[] toByteArray() {
		final Json json = new Json();
		json.setSerializer(Locale.class, new LocaleSerializer());
		json.setOutputType(JsonWriter.OutputType.json);
		final String string = json.toJson(this);
		System.out.println(string);
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
		private String requestedWindowTitle = "";
		private final APlayer requestedPlayer; 
		private final ALevel requestedLevel;
		
		public Builder(ALevel level, APlayer player) {
			if (level == null)
				throw new NullPointerException("level cannot be null");
			if (player == null)
				throw new NullPointerException("player cannot be null");
			requestedLevel = level;
			requestedPlayer = player;
		}

		public Builder requestedLocale(Locale x) {
			if (x != null) requestedLocale = x;
			return this;
		}

		public Builder requestedWidth(int x) {
			if (x>0) requestedWidth = x;
			return this;
		}

		public Builder requestedHeight(int x) {
			if (x>0) requestedHeight = x;
			return this;
		}

		public Builder requestedFullscreen(boolean x) {
			requestedFullscreen = x;
			return this;
		}

		public Builder requestedFps(int x) {
			if (x>0) requestedFps = x;
			return this;
		}

		public Builder requestedLogLevel(int x) {
			if (x>=0) requestedLogLevel = x;
			return this;
		}

		public Builder requestedWindowTitle(String x) {
			requestedWindowTitle = String.valueOf(x);
			return this;
		}
		
		// TODO
		// !!!Change the read/write methods when changing this!!!
		public GameParameters build() {
			if (requestedPlayer == null || requestedLevel == null)
				throw new NullPointerException("level and player must not be null");
			GameParameters params = new GameParameters();
			params.requestedFps = this.requestedFps;
			params.requestedFullscreen = this.requestedFullscreen;
			params.requestedHeight = this.requestedHeight;
			params.requestedLevel = this.requestedLevel;
			params.requestedLocale = this.requestedLocale;
			params.requestedLogLevel = this.requestedLogLevel;
			params.requestedWidth = this.requestedWidth;
			params.requestedWindowTitle = this.requestedWindowTitle;
			params.requestedPlayer = this.requestedPlayer;
			return params;
		}
	}

	public boolean isDeserializedSuccessfully() {
		return deserializedSuccessfully;
	}
}
