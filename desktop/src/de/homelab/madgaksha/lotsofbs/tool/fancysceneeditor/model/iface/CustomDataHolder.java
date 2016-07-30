package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import java.util.NoSuchElementException;

/**
 * Interface for object for which custom data can be set.
 * Mainly used for listeners, eg. action listeners.
 * @author madgaksha
 *
 */
public interface CustomDataHolder {
	/**
	 * 
	 * @param key The key for the data.
	 * @param data The data to be associated with the key.
	 * @throws NullPointerException When either key or data is null.
	 */
	public void setData(String key, Object data) throws NullPointerException;
	
	/**
	 * @param key The key of the data to retrieve.
	 * @return The data for this key.
	 * @throws NullPointerException When the key is null.
	 * @throws NoSuchElementException When no data has been set for the key.
	 */
	public Object getData(String key) throws NoSuchElementException;
	
	/**
	 * @param key Key 
	 * @param type The expected data type.
	 * @return The data for this key.
	 * @throws NoSuchElementException When no data has been set for the key, or it is of the wrong type.
	 */
	@SuppressWarnings("unchecked")
	default <T> T getData(String key, Class<T> type) throws NoSuchElementException {
		final Object data = getData(key);
		if (type.isAssignableFrom(data.getClass())) {
			return (T)data;
		}
		throw new NoSuchElementException("Data not of the specified type.");
	}
	
	/**
	 * Returns the data associated with the key, or the default. Never throws.
	 * @param key The key of the data to be retrieved.
	 * @return The data associated with the key, or the default.
	 */
	default Object getDataOrDefault(String key, Object def) {
		try {
			return getData(key);
		}
		catch (NoSuchElementException e) {
			return def;
		}
	}
}
