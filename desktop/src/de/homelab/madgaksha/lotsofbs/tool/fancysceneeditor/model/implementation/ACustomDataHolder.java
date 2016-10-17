package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.CustomDataHolder;

public abstract class ACustomDataHolder implements CustomDataHolder {
	
	private final Map<String,Object> dataMap = new HashMap<>(); 
	
	@Override
	public void setData(String key, Object data) throws NullPointerException {
		if (key == null || data == null) throw new NullPointerException("Key or data cannot be null.");
		dataMap.put(key, data);		
	}

	@Override
	public Object getData(String key) throws NoSuchElementException {
		if (key == null) throw new NullPointerException("Key cannot be null.");
		final Object data = dataMap.get(key);
		if (data == null) throw new NoSuchElementException("No data exists for the specified type.");
		return data;
	}
}
