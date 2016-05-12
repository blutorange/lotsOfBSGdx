package de.homelab.madgaksha.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class i18n {
	private static Map<String, String> main;
	private static Map<String, String> game;

	private static boolean initiated = false;

	// not instantiable
	private i18n() {
	};

	/**
	 * Initializes the localization module with the given locale and loads all
	 * messages for each namespace. Localized strings can then be accessed with
	 * <code>i18n.&lt;namespace&gt;(<KEY)</code>, eg.
	 * <code>i18n.main("helloWorld")</code>.
	 * 
	 * @param locale
	 *            The locale to use.
	 */
	public static void init(Locale locale) {
		ResourceBundle rbMain = ResourceBundle.getBundle("de.homelab.madgaksha.i18n.main", locale);
		ResourceBundle rbGame = ResourceBundle.getBundle("de.homelab.madgaksha.i18n.game", locale);
		main = rb2Map(rbMain);
		game = rb2Map(rbGame);
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

	private static String getValue(Map<String, String> map, String key) {
		return map.containsKey(key) ? map.get(key) : "??" + key + "??";
	}

	public static String main(String key) {
		return getValue(main, key);
	}

	public static String game(String key) {
		return getValue(game, key);
	}
}
