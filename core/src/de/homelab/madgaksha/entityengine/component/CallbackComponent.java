package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.reflect.Method;

import de.homelab.madgaksha.entityengine.entity.CallbackMaker;

/**
 * Contains information for {@link CallbackMaker} entities.
 * @author madgaksha
 *
 */
public class CallbackComponent implements Component, Poolable {
	private static final Method DEFAULT_CALLBACK = null;
	private static final MapProperties DEFAULT_PROPERTIES = null;
	
	public Method callback = DEFAULT_CALLBACK;
	public MapProperties properties = DEFAULT_PROPERTIES;
	
	public CallbackComponent(){
	}
	
	public CallbackComponent(Method callback, MapProperties properties) {
		setup(callback, properties);
	}
	
	public void setup(Method callback, MapProperties properties) {
		this.callback = callback;
		this.properties = properties;
	}
	
	@Override
	public void reset() {
		callback = null;
		properties = null;
	}
}
