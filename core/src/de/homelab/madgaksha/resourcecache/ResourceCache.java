package de.homelab.madgaksha.resourcecache;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;

import de.homelab.madgaksha.logging.Logger;

public final class ResourceCache {

	private final static Logger LOG = Logger.getLogger(ResourceCache.class);
	
	public final static int LIMIT_MUSIC = 5;
	public final static int LIMIT_TEXTURE = 25;
	public final static int LIMIT_ANIMATION = 50;
	public final static int LIMIT_ANIMATION_LIST = 50;
	public final static int LIMIT_SOUND = 100;
	
	private ResourceCache() {
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
	 * @param animationList
	 *            Animation list object to clear.
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
	 * Clears all sound objects from the cache.
	 */
	public static void clearAllSound() {
		ESound.clearAll();
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
		clearAllMusic();
		clearAllSound();
		clearAllTexture();
		clearAllAnimation();
		clearAllAnimationLists();
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
	public static Music getMusic(EMusic bgm, boolean cached) {
		return (Music) getResource(bgm, cached);
	}

	/**
	 * Fetches the requested texture from the cache, or loads it.
	 * 
	 * @param texture
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
	 * @param animation
	 *            Animation to load.
	 * @return Loaded animation.
	 */
	public static Animation getAnimation(EAnimation animation) {
		return (Animation) getResource(animation, true);
	}
	public static Animation getAnimation(EAnimation animation, boolean cached) {
		return (Animation) getResource(animation, cached);
	}
	
	/**
	 * Fetches the requested animation list from the cache, or loads it.
	 * 
	 * @param animationList
	 *            Animation list to load.
	 * @return Loaded animation list.
	 */
	public static Animation[] getAnimationList(EAnimationList animationList) {
		return (Animation[]) getResource(animationList, true);
	}
	public static Animation[] getAnimationList(EAnimationList animationList, boolean cached) {
		return (Animation[]) getResource(animationList, cached);
	}

	/**
	 * Fetches the requested sound from the cache, or loads it.
	 * 
	 * @param sound
	 *            Sound to load.
	 * @return Loaded sound.
	 */
	public static Sound getSound(ESound sound) {
		return (Sound) getResource(sound, true);
	}
	public static Sound getMusic(ESound sound, boolean cached) {
		return (Sound) getResource(sound, cached);
	}
	
}
