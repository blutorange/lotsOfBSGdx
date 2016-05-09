package de.homelab.madgaksha;

import java.util.Locale;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.JsonValue;

public class LocaleSerializer implements Serializer<Locale> {

	@Override
	public void write(Json json, Locale locale, Class knownType) {
		json.writeValue("language", locale.getLanguage());
		json.writeValue("country", locale.getCountry());
		json.writeValue("variant", locale.getVariant());
	}

	@Override
	public Locale read(Json json, JsonValue jsonData, Class type) {
		final String language = jsonData.get("language").asString();
		final String country = jsonData.get("country").asString();
		final String variant = jsonData.get("variant").asString();
		return new Locale(language, country, variant);
	}
}
