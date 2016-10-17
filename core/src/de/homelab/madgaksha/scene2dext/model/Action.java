package de.homelab.madgaksha.scene2dext.model;

import java.util.Locale;


public interface Action<T extends Model> {
	public abstract void actOnModel(T model);
	public String getName(Locale locale);
	public abstract static class ActionWithFixedName<T extends Model> implements Action<T> {
		protected final String name;
		public ActionWithFixedName(final String name) {
			this.name = name;
		}
		@Override
		public String getName(final Locale locale) {
			return name;
		}
	}
}