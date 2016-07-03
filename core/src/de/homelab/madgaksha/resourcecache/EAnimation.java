package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

public enum EAnimation implements IResource<EAnimation, AtlasAnimation> {
	DEFAULT(ETexture.DEFAULT, 1, 1, 1, 0.1f, AtlasAnimation.PlayMode.LOOP),

	// =================
	// ESTELLE
	// =================
	ESTELLE_RUNNING(ETexture.ESTELLE_RUNNING, 64, 128, 64, 0.1f, AtlasAnimation.PlayMode.LOOP),
	ESTELLE_STANDING(ETexture.ESTELLE_STANDING, 128, 128, 40, 0.2f, AtlasAnimation.PlayMode.LOOP),
	ESTELLE_SWINGING(ETexture.ESTELLE_SWINGING, 128, 128, 20, 0.1f, AtlasAnimation.PlayMode.LOOP),

	// =================
	// JOSHUA
	// =================
	JOSHUA_STANDING(ETexture.JOSHUA_STANDING, 64, 128, 64, 0.1f, AtlasAnimation.PlayMode.LOOP),

	// =================
	// ENEMIES
	// =================

	OUGI_OUKA_MUSOUGEKI_SAKURA(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "sakura", 0.075f, AtlasAnimation.PlayMode.LOOP),

	// =================
	// BULLETS
	// =================
	NOTE_RED(ETextureAtlas.BULLETS_BASIC, "pNote0", 0.15f, AtlasAnimation.PlayMode.LOOP),
	NOTE_BLUE(ETextureAtlas.BULLETS_BASIC, "pNote1", 0.15f, AtlasAnimation.PlayMode.LOOP),
	NOTE_YELLOW(ETextureAtlas.BULLETS_BASIC, "pNote2", 0.15f, AtlasAnimation.PlayMode.LOOP),
	NOTE_PINK(ETextureAtlas.BULLETS_BASIC, "pNote3", 0.15f, AtlasAnimation.PlayMode.LOOP),;

	private final static Logger LOG = Logger.getLogger(EAnimation.class);
	private final static EnumMap<EAnimation, AtlasAnimation> animationCache = new EnumMap<EAnimation, AtlasAnimation>(
			EAnimation.class);

	private ETextureAtlas textureAtlas;
	private String name;
	private ETexture texture;
	private int tileWidth;
	private int tileHeight;
	private int count;
	private final float frameDuration;
	private final AtlasAnimation.PlayMode playMode;

	private EAnimation(ETextureAtlas textureAtlas, String name, float frameDuration, AtlasAnimation.PlayMode playMode) {
		this.textureAtlas = textureAtlas;
		this.name = name;
		this.playMode = playMode;
		this.frameDuration = frameDuration;
	}

	private EAnimation(ETexture texture, int tileWidth, int tileHeight, int count, float frameDuration,
			AtlasAnimation.PlayMode playMode) {
		this.texture = texture;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.count = count;
		this.frameDuration = frameDuration;
		this.playMode = playMode;
	}

	public static void clearAll() {
		LOG.debug("clearing all animations");
		for (EAnimation anm : animationCache.keySet()) {
			anm.clear();
		}
	}

	@Override
	public Enum<EAnimation> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_ANIMATION;
	}

	@Override
	public AtlasAnimation getObject() {
		if (textureAtlas != null) {
			// Retrieve texture atlas and texture regions.
			TextureAtlas atlas = ResourceCache.getTextureAtlas(textureAtlas);
			if (atlas == null)
				return null;
			Array<AtlasRegion> atlasRegionArray = atlas.findRegions(name);
			if (atlasRegionArray.size == 0)
				return null;
			AtlasAnimation animation = new AtlasAnimation(frameDuration, atlasRegionArray);
			animation.setPlayMode(playMode);
			return animation;
		} else {
			// Retrieve texture and split as tiles.
			AtlasRegion atlasRegion = ResourceCache.getTexture(texture);
			if (atlasRegion == null)
				return null;
			AtlasRegion[] atlasRegionArray = splitAtlasRegion(atlasRegion, tileWidth, tileHeight, count);
			AtlasAnimation animation = new AtlasAnimation(frameDuration, atlasRegionArray);
			animation.setPlayMode(playMode);
			return animation;
		}
	}

	@Override
	public void clear() {
		LOG.debug("clearing animation: " + String.valueOf(this));
		animationCache.remove(this);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public EnumMap getMap() {
		return animationCache;
	}

	private static AtlasRegion[] splitAtlasRegion(AtlasRegion atlasRegion, int tileWidth, int tileHeight, int count) {
		int x = atlasRegion.getRegionX();
		int y = atlasRegion.getRegionY();
		int width = atlasRegion.getRegionWidth();
		int cols = width / tileWidth;

		int startX = x;
		AtlasRegion[] tiles = new AtlasRegion[count];
		int col = 0;
		for (int i = 0; i != count; ++i) {

			tiles[i] = new AtlasRegion(atlasRegion.getTexture(), x, y, tileWidth, tileHeight);
			++col;
			x += tileWidth;
			if (col == cols) {
				x = startX;
				col = 0;
				y += tileHeight;
			}
		}
		return tiles;
	}

	@Override
	public void clearAllOfThisKind() {
		EAnimation.clearAll();
	}

}
