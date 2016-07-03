package de.homelab.madgaksha.resourcecache;

import java.util.Set;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.maps.tiled.TiledMap;

import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.textbox.FancyTextbox;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

public final class ResourceCache {

	private final static Logger LOG = Logger.getLogger(ResourceCache.class);

	public final static int LIMIT_MUSIC = 100;
	public final static int LIMIT_TEXTURE = 200;
	public final static int LIMIT_ANIMATION = 50;
	public final static int LIMIT_ANIMATION_LIST = 50;
	public final static int LIMIT_SOUND = 100;
	public final static int LIMIT_TILED_MAP = 4;
	public final static int LIMIT_BITMAP_FONT = 20;
	public final static int LIMIT_NINE_PATCH = 20;
	public final static int LIMIT_TEXTURE_ATLAS = 20;
	public final static int LIMIT_MODEL = 30;
	public final static int LIMIT_FREE_TYPE_FONT_GENERATOR = 5;
	public final static int LIMIT_FANCY_SCENE = 50;

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
	 * @param cached
	 *            When false, does not look up the object in the cache and loads
	 *            it again.
	 * @return An object representing the resource <code>res</code>. Must be
	 *         typecast to the correct type.
	 */
	@SuppressWarnings("unchecked")
	private static Object getResource(IResource<? extends Enum<?>, ?> res, boolean cached) {
		if (res == null)
			return null;
		if (cached && res.getMap().containsKey(res)) {
			// Fetch from cache.
			return res.getMap().get(res);
		} else {
			// Load the object from disk.
			if (res.getMap().size() > res.getLimit()) {
				LOG.error("cannot load any more resources of type " + String.valueOf(res.getEnum().getClass()));
				LOG.error("clearing cache");
				res.clearAllOfThisKind();
			}
			final Object r = res.getObject();
			if (r != null && cached)
				res.getMap().put(res.getEnum(), r);
			return r;
		}
	}

	/**
	 * Fetches the resources and puts it in the cache for later use.
	 * 
	 * @param res
	 *            Resource to pre-load.
	 * @return Whether it was loaded sucessfully.
	 */
	public static boolean loadToRam(IResource<? extends Enum<?>, ?> res) {
		return getResource(res, true) != null;
	}

	/**
	 * Loads all resources to ram.
	 * 
	 * @param requiredResources
	 *            Resources to load to RAM.
	 * @return Whether all resources could be loaded successfully.
	 * @see ResourceCache#loadToRam(IResource)
	 */
	public static boolean loadToRam(IResource<? extends Enum<?>, ?>[] requiredResources) {
		if (requiredResources == null)
			return true;
		for (IResource<? extends Enum<?>, ?> r : requiredResources) {
			LOG.debug("fetch " + r.getClass().getSimpleName() + ": " + r);
			if (!loadToRam(r))
				return false;
		}
		return true;
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
	 * Clears all fancy scene objects from the cache.
	 */
	public static void clearAllFancyScene() {
		EFancyScene.clearAll();
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
	 * Reloads all bitmap fonts. Happens when the game window resizes.
	 */
	public static void reloadAllBitmapFont() {
		Set<EBitmapFont> set = EBitmapFont.getMapKeys();
		clearAllBitmapFont();
		for (EBitmapFont bf : set)
			bf.getObject();
	}

	/**
	 * Clears all model objects from the cache.
	 */
	public static void clearAllModel() {
		EModel.clearAll();
	}

	/**
	 * Clears all free type font generator objects from the cache.
	 */
	public static void clearAllFreeTypeFontGenerator() {
		EFreeTypeFontGenerator.clearAll();
	}

	/**
	 * Clears all textbox objects from the cache.
	 */
	public static void clearAllTextbox() {
		ETextbox.clearAll();
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
		clearAllModel();
		clearAllFreeTypeFontGenerator();
		clearAllTextbox();
		clearAllFancyScene();
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
	public static AtlasRegion getTexture(ETexture texture) {
		return (AtlasRegion) getResource(texture, true);
	}

	public static AtlasRegion getTexture(ETexture texture, boolean cached) {
		return (AtlasRegion) getResource(texture, cached);
	}

	/**
	 * Fetches the requested animation from the cache, or loads it.
	 * 
	 * @param animation
	 *            Animation to load.
	 * @return Loaded animation.
	 */
	public static AtlasAnimation getAnimation(EAnimation animation) {
		return (AtlasAnimation) getResource(animation, true);
	}

	public static AtlasAnimation getAnimation(EAnimation animation, boolean cached) {
		return (AtlasAnimation) getResource(animation, cached);
	}

	/**
	 * Fetches the requested animation list from the cache, or loads it.
	 * 
	 * @param animationList
	 *            Animation list to load.
	 * @return Loaded animation list.
	 */
	public static AtlasAnimation[] getAnimationList(EAnimationList animationList) {
		return (AtlasAnimation[]) getResource(animationList, true);
	}

	public static AtlasAnimation[] getAnimationList(EAnimationList animationList, boolean cached) {
		return (AtlasAnimation[]) getResource(animationList, cached);
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

	public static BitmapFont getBitmapFont(EModel bitmapFont, boolean cached) {
		return (BitmapFont) getResource(bitmapFont, cached);
	}

	/**
	 * Fetches the requested model from the cache, or loads it.
	 * 
	 * @param model
	 *            Model font to load.
	 * @return Loaded model.
	 */
	public static Model getModel(EModel model) {
		return (Model) getResource(model, true);
	}

	public static Model getModel(EModel model, boolean cached) {
		return (Model) getResource(model, cached);
	}

	/**
	 * Fetches the requested freeTypeFontGenerator from the cache, or loads it.
	 * 
	 * @param freeTypeFontGenerator
	 *            FreeTypeFontGenerator font to load.
	 * @return Loaded freeTypeFontGenerator.
	 */
	public static FreeTypeFontGenerator getFreeTypeFontGenerator(EFreeTypeFontGenerator freeTypeFontGenerator) {
		return (FreeTypeFontGenerator) getResource(freeTypeFontGenerator, true);
	}

	public static FreeTypeFontGenerator getFreeTypeFontGenerator(EFreeTypeFontGenerator freeTypeFontGenerator,
			boolean cached) {
		return (FreeTypeFontGenerator) getResource(freeTypeFontGenerator, cached);
	}

	/**
	 * Fetches the requested textbox from the cache, or loads it.
	 * 
	 * @param textbox
	 *            Textbox font to load.
	 * @return Loaded textbox.
	 */
	public static FancyTextbox getTextbox(ETextbox textbox) {
		return (FancyTextbox) getResource(textbox, true);
	}

	public static FancyTextbox getTextbox(ETextbox textbox, boolean cached) {
		return (FancyTextbox) getResource(textbox, cached);
	}

	/**
	 * Fetches the requested fancy scene from the cache, or loads it.
	 * 
	 * @param textbox
	 *            Fancy scene font to load.
	 * @return Loaded fancy scene.
	 */
	public static EventFancyScene getFancyScene(EFancyScene eventFancyScene) {
		return (EventFancyScene) getResource(eventFancyScene, true);
	}

	public static EventFancyScene getFancyScene(EFancyScene eventFancyScene, boolean cached) {
		return (EventFancyScene) getResource(eventFancyScene, cached);
	}
}
