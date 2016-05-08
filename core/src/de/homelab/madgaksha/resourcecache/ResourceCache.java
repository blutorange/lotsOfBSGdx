package de.homelab.madgaksha.resourcecache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.audio.Music;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.Resources.Ebgm;

public final class ResourceCache {

	private ResourceCache(){}
	
	private final static Logger LOG = Logger.getLogger(ResourceCache.class);
	
	private final static List<Method> resourceClearerList;
	
	static {
		resourceClearerList = new ArrayList<Method>();
		for (Field f : Resources.class.getFields()) {
			if (f.isAnnotationPresent(AnnotationResource.class)) {
				Method m = null;
				try {
					m = f.getClass().getMethod("staticClear");
				} catch (Exception e) {
					LOG.error("could not get clear method for " + f.getClass().getCanonicalName());
				}
				if (m != null) resourceClearerList.add(m);
			}
		}
	}
	
	/**
	 * Loads the resource into memory. Applies caching via a Map. Each resource
	 * defines limit on how many resources can be loaded at once. When there are
	 * too many resources loaded, the map is cleared so that the garbage
	 * collector can then collect the resource once it ceases to be loaded.
	 * 
	 * @param map
	 *            The map used for caching the resources.
	 * @param res
	 *            The resource to load. An Enum implementing the
	 *            {@link IResource} interface.
	 * @return
	 */
	private static Object getResource(IResource res) {
		if (res.getMap().containsKey(res)) {
			return res.getMap().get(res);
		} else {
			if (res.getMap().size() > res.getLimit()) {
				LOG.debug("clearing cache for resources of type " + res.getEnum().toString());
				res.clear();
			}
			final Object r = res.getObject();
			if (r != null) res.getMap().put(res.getEnum(), r);
			else LOG.error("could not load resource: " + String.valueOf(res));
			return r;
		}
	}

	public static void clearBgm() {
		Ebgm.staticClear();
	}
	public static void clearAll() {
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
