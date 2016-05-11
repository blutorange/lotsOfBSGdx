package de.homelab.madgaksha.util;

import java.util.Locale;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.JsonValue;

public class LocaleSerializer implements Serializer<Locale> {

	@Override
	public void write(Json json, Locale locale, @SuppressWarnings("rawtypes") Class knownType) {
		json.writeObjectStart();
		json.writeValue("language", locale.getLanguage());
		json.writeValue("country", locale.getCountry());
		json.writeValue("variant", locale.getVariant());
		json.writeObjectEnd();
	}

	@Override
	public Locale read(Json json, JsonValue jsonData, @SuppressWarnings("rawtypes") Class type) {
		System.out.println(jsonData.get("language"));
		final String language = jsonData.get("language").asString();
		final String country = jsonData.get("country").asString();
		final String variant = jsonData.get("variant").asString();
		return new Locale(language, country, variant);
	}
}
