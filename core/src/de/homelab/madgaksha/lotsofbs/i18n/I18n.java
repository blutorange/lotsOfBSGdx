package de.homelab.madgaksha.lotsofbs.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

public class I18n {
	private static Map<String, String> main;
	private static Map<String, String> game;
	private static Map<String, String> font;
	private static Map<String, String> character;
	private static Locale locale;
	private static boolean initiated = false;

	private final static Locale[] AVAILABLE_LOCALES = { Locale.ENGLISH, Locale.GERMAN, Locale.JAPANESE };
	private final static String BASE_PACKAGE = I18n.class.getPackage().getName() + ".";
	
	// not instantiable
	private I18n() {
	}

	/**
	 * Initializes the localization module with the given locale and loads all
	 * messages for each namespace. Localized strings can then be accessed with
	 * <code>i18n.&lt;namespace&gt;(<KEY)</code>, eg.
	 * <code>i18n.main("helloWorld")</code>.
	 * 
	 * @param locale
	 *            The locale to use.
	 */
	public static void init(Locale l) {
		if (l == null) l = AVAILABLE_LOCALES[0];
		if (!isLocaleAvailable(l)) l = AVAILABLE_LOCALES[0];
		locale = l;
		ResourceBundle rbMain = ResourceBundle.getBundle(BASE_PACKAGE + "main", locale);
		ResourceBundle rbGame = ResourceBundle.getBundle(BASE_PACKAGE + "game", locale);
		ResourceBundle rbFont = ResourceBundle.getBundle(BASE_PACKAGE + "font", locale);
		ResourceBundle rbCharacter = ResourceBundle.getBundle(BASE_PACKAGE + "character", locale);
		main = rb2Map(rbMain);
		game = rb2Map(rbGame);
		font = rb2Map(rbFont);
		character = rb2Map(rbCharacter);
		initiated = true;
	}

	public static boolean isInitiated() {
		return initiated;
	}

	/**
	 * Converts all key-value pairs from a {@link ResourceBundle} to a HashMap.
	 * 
	 * @param rb
	 *            The ResourceBundle to convert.
	 * @return The HashMap with all key-value pairs from the ResourceBundle.
	 */
	private static Map<String, String> rb2Map(ResourceBundle rb) {
		Map<String, String> m = new HashMap<String, String>();
		Set<String> s = rb.keySet();
		for (String k : s) {
			m.put(k, rb.getString(k));
		}
		return m;
	}

	/**
	 * @return The short name of the current locale (eg. de, en, ja).
	 */
	public static String getShortName() {
		return locale.getLanguage();
	}

	public static boolean isLocaleAvailable(Locale otherLocale) {
		for (Locale l : AVAILABLE_LOCALES)
			if (l.equals(otherLocale)) return true;
		return false;
	}
	
	public static Map<String, Locale> getAvailableLocales() {
		Map<String, Locale> map = new HashMap<String, Locale>(AVAILABLE_LOCALES.length);
		for (Locale l : AVAILABLE_LOCALES) {
			map.put(l.getDisplayLanguage(l), l);
		}
		return map;
	}

	private static String getValue(Map<String, String> map, String key) {
		return map.containsKey(key) ? map.get(key) : "??" + key + "??";
	}

	public static String main(String key) {
		return getValue(main, key);
	}

	public static String game(String key) {
		return getValue(game, key);
	}

	public static String gameE(String key) {
		return StringEscapeUtils.unescapeJava(game(key));
	}

	public static String font(String key) {
		return getValue(font, key);
	}

	public static String character(String key) {
		return getValue(character, key);
	}

	public static boolean hasFontKey(String key) {
		return font.containsKey(key);
	}

	public static boolean hasGameKey(String key) {
		return game.containsKey(key);
	}
}
