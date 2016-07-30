package de.homelab.madgaksha.lotsofbs.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.Icon;

import de.homelab.madgaksha.lotsofbs.logging.LoggerFactory;
import de.homelab.madgaksha.lotsofbs.resources.Resource.EIcon;

public class ResourceLoader {
	private ResourceLoader() {
	}

	private static final Logger LOG = LoggerFactory.getLogger(ResourceLoader.class);

	private static Map<Integer, Object> iconMap = new HashMap<Integer, Object>();

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
	private static Object getResource(Map<Integer, Object> map, IResource res) {
		final int o = res.getEnum().ordinal();
		if (map.containsKey(o)) {
			return map.get(o);
		}
		if (map.size() > res.getLimit()) {
			LOG.info("Clearing cache for resources of type " + res.getEnum().toString());
			map.clear();
		}
		final Object r = res.getObject();
		if (r != null)
			map.put(o, r);
		return r;
	}

	/**
	 * Loads an icon with caching.
	 * 
	 * @param icon
	 *            The icon to be loaded.
	 * @return The loaded icon, or null if it could not be loaded.
	 */
	public static Icon icon(EIcon icon) {
		return (Icon) getResource(iconMap, icon);
	}
}