package de.homelab.madgaksha.resourcecache;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.Resources.EAnimation;
import de.homelab.madgaksha.resourcecache.Resources.EAnimationList;
import de.homelab.madgaksha.resourcecache.Resources.EMusic;
import de.homelab.madgaksha.resourcecache.Resources.ETexture;

public final class ResourceCache {

	private ResourceCache() {
	}

	private final static Logger LOG = Logger.getLogger(ResourceCache.class);

	private final static List<Method> resourceClearerList;

	static {
		resourceClearerList = new ArrayList<Method>();
		for (Class<?> c : Resources.class.getClasses()) {
			if (c.isAnnotationPresent(AnnotationResource.class)) {
				Method m = null;
				try {
					// For cross-platform support, we need to use libgdx
					// reflection.
					m = ClassReflection.getMethod(c, "clearAll");
				} catch (Exception e) {
					LOG.error("could not get clearAll/clearAllBlock method for " + c.getCanonicalName());
				}
				if (m != null)
					resourceClearerList.add(m);
			}
		}
	}

	/**
	 * Loads the resource into memory. Applies caching via a Map. Each resource
	 * defines limit on how many resources can be loaded at once. When there are
	 * too many resources loaded, it will return null. Resources must be
	 * disposed with a call to {@link ResourceCache#clearAll()} or
	 * {@link ResourceCache#clearBgm(Ebgm)} etc.
	 * 
	 * @param res
	 *            The resource to load. An Enum implementing the
	 *            {@link IResource} interface.
	 * @return An object representing the resource <code>res</code>. Must be
	 *         typecast to the correct type.
	 */
	@SuppressWarnings("unchecked")
	private static Object getResource(IResource res, boolean cached) {
		if (res == null)
			return null;
		if (cached && res.getMap().containsKey(res)) {
			// Fetch from cache.
			return res.getMap().get(res);
		} else {
			// Load the object from disk.
			if (res.getMap().size() > res.getLimit()) {
				LOG.debug("cannot load any more resources of type " + String.valueOf(res));
				return null;
			}
			final Object r = res.getObject();
			if (r != null)
				res.getMap().put(res.getEnum(), r);
			return r;
		}
	}

	/**
	 * Clears the given music object from the cache.
	 * 
	 * @param music
	 *            Music object to clear.
	 */
	public static void clearMusic(EMusic music) {
		music.clear();
	}

	/**
	 * Clears the given texture object from the cache.
	 * 
	 * @param texture
	 *            Texture object to clear.
	 */
	public static void clearTexture(ETexture texture) {
		texture.clear();
	}
	
	/**
	 * Clears the given texture object from the cache.
	 * 
	 * @param texture
	 *            Texture object to clear.
	 */
	public static void clearAnimation(EAnimation texture) {
		texture.clear();
	}
	
	/**
	 * Clears the given texture object from the cache.
	 * 
	 * @param texture
	 *            Texture object to clear.
	 */
	public static void clearAnimationList(EAnimationList animationList) {
		animationList.clear();
	}

	/**
	 * Clears all music objects from the cache.
	 */
	public static void clearAllMusic() {
		EMusic.clearAll();
	}

	/**
	 * Clears all texture objects from the cache.
	 */
	public static void clearAllTexture() {
		ETexture.clearAll();
	}

	/**
	 * Clears all animation objects from the cache.
	 */
	public static void clearAllAnimation() {
		EAnimation.clearAll();
	}
	
	/**
	 * Clears all animation objects from the cache.
	 */
	public static void clearAllAnimationLists() {
		EAnimationList.clearAll();
	}
	
	/**
	 * Clears all resource objects.
	 */
	public static void clearAll() {
		LOG.debug("clearing cache");
		for (Method m : resourceClearerList)
			try {
				m.invoke(null);
			} catch (Exception e) {
				LOG.error("could not clear resources", e);
			}
	}

	/**
	 * Fetches the requested music from the cache, or loads it.
	 * 
	 * @param bgm
	 *            Music to load.
	 * @return Loaded music.
	 */
	public static Music getMusic(EMusic bgm) {
		return (Music) getResource(bgm, true);
	}

	/**
	 * Fetches the requested texture from the cache, or loads it.
	 * 
	 * @param bgm
	 *            Texture to load.
	 * @return Loaded texture.
	 */
	public static Texture getTexture(ETexture texture) {
		return (Texture) getResource(texture, true);
	}
	public static Texture getTexture(ETexture texture, boolean cached) {
		return (Texture) getResource(texture, cached);
	}
	
	/**
	 * Fetches the requested animation from the cache, or loads it.
	 * 
	 * @param bgm
	 *            Animation to load.
	 * @return Loaded animation.
	 */
	public static Animation getAnimation(EAnimation animation) {
		return (Animation) getResource(animation, true);
	}
	
	/**
	 * Fetches the requested animation list from the cache, or loads it.
	 * 
	 * @param bgm
	 *            Texture to load.
	 * @return Loaded texture.
	 */
	public static Animation[] getAnimationList(EAnimationList animationList) {
		return (Animation[]) getResource(animationList, true);
	}
	public static Animation[] getAnimationList(EAnimationList animationList, boolean cached) {
		return (Animation[]) getResource(animationList, cached);
	}

}
