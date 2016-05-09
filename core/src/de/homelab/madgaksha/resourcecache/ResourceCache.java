package de.homelab.madgaksha.resourcecache;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.Resources.Ebgm;

public final class ResourceCache {

	private ResourceCache(){}
	
	private final static Logger LOG = Logger.getLogger(ResourceCache.class);
	
	private final static List<Method> resourceClearerList;
	
	static {
		resourceClearerList = new ArrayList<Method>();
		for (Class<?> c : Resources.class.getClasses()) {
			if (c.isAnnotationPresent(AnnotationResource.class)) {
				Method m = null;
				try {
					// For cross-platform support, we need to use libgdx reflection.
					m = ClassReflection.getMethod(c, "clearAll"); 
				} catch (Exception e) {
					LOG.error("could not get clearAll/clearAllBlock method for " + c.getCanonicalName());
				}
				if (m != null) resourceClearerList.add(m);
			}
		}
	}
	
	/**
	 * Loads the resource into memory. Applies caching via a Map. Each resource
	 * defines limit on how many resources can be loaded at once. When there are
	 * too many resources loaded, it will return null. Resources must be disposed
	 * with a call to {@link ResourceCache#clearAll()} or 
	 * {@link ResourceCache#clearBgm(Ebgm)} etc.
	 * 
	 * @param res
	 *            The resource to load. An Enum implementing the
	 *            {@link IResource} interface.
	 * @return An object representing the resource <code>res</code>. Must be typecast to the correct type.
	 */
	private static Object getResource(IResource res) {
		if (res.getMap().containsKey(res)) {
			return res.getMap().get(res);
		} else {
			if (res.getMap().size() > res.getLimit()) {
				LOG.debug("cannot load any more resources of type " + String.valueOf(res));
				return null;
			}
			final Object r = res.getObject();
			if (r != null) res.getMap().put(res.getEnum(), r);
			return r;
		}
	}

	public static void clearBgm(Ebgm bgm) {
		bgm.clear();
	}
	public static void clearAllBgm() {
		Ebgm.clearAll();
	}
	public static void clearAll() {
		LOG.debug("clearing cache");
		for (Method m : resourceClearerList)
			try {
				m.invoke(null);
			} catch (Exception e) {
				LOG.error("could not clear resources", e);
			}
	}
	
	
	public static Music getBgm(Ebgm bgm) {
		return (Music)getResource(bgm);
	}
	
}
