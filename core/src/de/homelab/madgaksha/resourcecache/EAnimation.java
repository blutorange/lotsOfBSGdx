package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

public enum EAnimation implements IResource<EAnimation, AtlasAnimation> {
	DEFAULT(ETexture.DEFAULT,1,1,1,0.1f, AtlasAnimation.PlayMode.LOOP),
	
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

	;

	private final static Logger LOG = Logger.getLogger(EAnimation.class);
	private final static EnumMap<EAnimation, AtlasAnimation> animationCache = new EnumMap<EAnimation, AtlasAnimation>(
			EAnimation.class);

	private ETexture eTexture;
	private int tileWidth;
	private int tileHeight;
	private int count;
	private float frameDuration;
	private AtlasAnimation.PlayMode playMode;

	private EAnimation(ETexture et, int tw, int th, int c, float fd, AtlasAnimation.PlayMode pm) {
		eTexture = et;
		tileWidth = tw;
		tileHeight = th;
		count = c;
		frameDuration = fd;
		playMode = pm;
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
		// Get texture
		final AtlasRegion atlasRegion = ResourceCache.getTexture(eTexture);
		if (atlasRegion == null)
			return null;
		final AtlasRegion[] atlasRegionArray = splitAtlasRegion(atlasRegion, tileWidth, tileHeight, count);
		final AtlasAnimation animation = new AtlasAnimation(frameDuration, atlasRegionArray);
		animation.setPlayMode(playMode);
		return animation;
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
