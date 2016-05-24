package de.homelab.madgaksha.resourcecache;

import java.util.Set;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;

import de.homelab.madgaksha.logging.Logger;

public final class ResourceCache {

	private final static Logger LOG = Logger.getLogger(ResourceCache.class);
	
	public final static int LIMIT_MUSIC = 5;
	public final static int LIMIT_TEXTURE = 25;
	public final static int LIMIT_ANIMATION = 50;
	public final static int LIMIT_ANIMATION_LIST = 50;
	public final static int LIMIT_SOUND = 100;
	public final static int LIMIT_TILED_MAP = 4;
	public final static int LIMIT_BITMAP_FONT = 20;
	public final static int LIMIT_NINE_PATCH = 20;
	public final static int LIMIT_TEXTURE_ATLAS = 20;
	
	private ResourceCache() {
	}

	/**
	 * Loads the resource into memory. Applies caching via a Map. Each resource
	 * defines limit on how many resources can be loaded at once. When there are
	 * too many resources loaded, it will return null. Resources must be
	 * disposed with a call to {@link ResourceCache#clearAll()} or
	 * {@link ResourceCache#clearMusic(EMusic)} etc.
	 * 
	 * @param res
	 *            The resource to load. An Enum implementing the
	 *            {@link IResource} interface.
	 * @param cached When false, does not look up the object in the cache and loads
	 *               it again.
	 * @return An object representing the resource <code>res</code>. Must be
	 *         typecast to the correct type.
	 */
	@SuppressWarnings("unchecked")
	private static Object getResource(IResource<? extends Enum<?>,?> res, boolean cached) {
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
			if (r != null && cached)
				res.getMap().put(res.getEnum(), r);
			return r;
		}
	}
	
	/**
	 * Fetches the resources and puts it in the cache for later use.
	 * @param res Resource to pre-load.
	 * @return Whether it was loaded sucessfully.
	 */
	public static boolean loadToRam(IResource<? extends Enum<?>,?> res) {
		return getResource(res, true) != null;
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
	 * Clears all sound objects from the cache.
	 */
	public static void clearAllTiledMap() {
		ETiledMap.clearAll();
	}
	
	/**
	 * Clears all animation objects from the cache.
	 */
	public static void clearAllAnimationLists() {
		EAnimationList.clearAll();
	}
	
	/**
	 * Clears all texture atlas objects from the cache.
	 */
	public static void clearAllTextureAtlas() {
		ETextureAtlas.clearAll();
	}
	
	/**
	 * Clears all animation objects from the cache.
	 */
	public static void clearAllNinePatch() {
		ENinePatch.clearAll();
	}
	
	/**
	 * Clears all bitmap font objects from the cache.
	 */
	public static void clearAllBitmapFont() {
		EBitmapFont.clearAll();
	}
	/**
	 * Reloads all bitmap fonts. Happens when the game window
	 * resizes.
	 */
	public static void reloadAllBitmapFont() {
		Set<EBitmapFont> set = EBitmapFont.getMapKeys();
		clearAllBitmapFont();
		for (EBitmapFont bf : set) bf.getObject();
	}
	
	
	/**
	 * Clears all resource objects.
	 */
	public static void clearAll() {		
		clearAllAnimation();
		clearAllAnimationLists();
		clearAllBitmapFont();
		clearAllMusic();
		clearAllNinePatch();
		clearAllSound();
		clearAllTexture();
		clearAllTextureAtlas();
		clearAllTiledMap();
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
	public static TextureRegion getTexture(ETexture texture) {
		return (TextureRegion) getResource(texture, true);
	}
	public static TextureRegion getTexture(ETexture texture, boolean cached) {
		return (TextureRegion) getResource(texture, cached);
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
	public static Sound getSound(ESound sound, boolean cached) {
		return (Sound) getResource(sound, cached);
	}

	/**
	 * Fetches the requested tiled map from the cache, or loads it.
	 * 
	 * @param tiledMap
	 *            Tile map to load.
	 * @return Loaded tiled map.
	 */
	public static TiledMap getTiledMap(ETiledMap tiledMap) {
		return (TiledMap) getResource(tiledMap, true);
	}
	public static TiledMap getTiledMap(ETiledMap tiledMap, boolean cached) {
		return (TiledMap) getResource(tiledMap, cached);
	}
	
	/**
	 * Fetches the requested texture atlas from the cache, or loads it.
	 * 
	 * @param textureAtlas
	 *            Texture atlas to load.
	 * @return Loaded texture atlas.
	 */
	public static TextureAtlas getTextureAtlas(ETextureAtlas textureAtlas) {
		return (TextureAtlas) getResource(textureAtlas, true);
	}
	public static TextureAtlas getTextureAtlas(ETextureAtlas textureAtlas, boolean cached) {
		return (TextureAtlas) getResource(textureAtlas, cached);
	}
	
	/**
	 * Fetches the requested nine patch from the cache, or loads it.
	 * 
	 * @param ninePatch
	 *            Nine patch to load.
	 * @return Loaded nine patch.
	 */
	public static NinePatch getNinePatch(ENinePatch ninePatch) {
		return (NinePatch) getResource(ninePatch, true);
	}
	public static NinePatch getNinePatch(ENinePatch ninePatch, boolean cached) {
		return (NinePatch) getResource(ninePatch, cached);
	}
	
	/**
	 * Fetches the requested bitmap font from the cache, or loads it.
	 * 
	 * @param bitmapFont
	 *            Bitmap font to load.
	 * @return Loaded bitmap font.
	 */
	public static BitmapFont getBitmapFont(EBitmapFont bitmapFont) {
		return (BitmapFont) getResource(bitmapFont, true);
	}
	public static BitmapFont getBitmapFont(ENinePatch bitmapFont, boolean cached) {
		return (BitmapFont) getResource(bitmapFont, cached);
	}

}
