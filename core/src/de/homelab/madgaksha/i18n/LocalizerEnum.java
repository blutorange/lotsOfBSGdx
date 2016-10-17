package de.homelab.madgaksha.i18n;

import java.util.Locale;

public interface LocalizerEnum {
	public String getLocalizedString(Locale locale, Enum<?> key);
}