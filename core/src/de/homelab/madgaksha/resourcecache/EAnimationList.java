package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

public enum EAnimationList implements IResource<EAnimationList, AtlasAnimation[]> {
	// =================
	// ESTELLE
	// =================
	ESTELLE_RUNNING(ETexture.ESTELLE_RUNNING, 64, 128, 64, 0.1f, AtlasAnimation.PlayMode.LOOP, 8),
	ESTELLE_STANDING(ETexture.ESTELLE_STANDING, 128, 128, 64, 0.1f, AtlasAnimation.PlayMode.LOOP, 8),

	// =================
	// JOSHUA
	// =================
	JOSHUA_STANDING(ETexture.JOSHUA_STANDING, 64, 128, 64, 0.1f, AtlasAnimation.PlayMode.LOOP, 8),

	// =================
	// ENEMIES
	// =================
	SOLDIER_RED_0(ETexture.SOLDIER_RED_0, 64, 128, 64, 0.1f, AtlasAnimation.PlayMode.LOOP, 8),
	SOLDIER_RED_1(ETexture.SOLDIER_RED_1, 64, 128, 8, 0.1f, AtlasAnimation.PlayMode.LOOP, 8),
	SOLDIER_GREEN_0(ETexture.SOLDIER_GREEN_0, 64, 128, 64, 0.1f, AtlasAnimation.PlayMode.LOOP, 8),
	SOLDIER_GREEN_1(ETexture.SOLDIER_GREEN_1, 64, 128, 8, 0.1f, AtlasAnimation.PlayMode.LOOP, 8),
	;

	private final static Logger LOG = Logger.getLogger(EAnimationList.class);
	private final static EnumMap<EAnimationList, AtlasAnimation[]> animationListCache = new EnumMap<EAnimationList, AtlasAnimation[]>(
			EAnimationList.class);

	private ETexture eTexture;
	private int tileWidth;
	private int tileHeight;
	private int count;
	private float frameDuration;
	private AtlasAnimation.PlayMode playMode;
	private int animationModeCount;

	private EAnimationList(ETexture et, int tw, int th, int c, float fd, AtlasAnimation.PlayMode pm, int amc) {
		eTexture = et;
		tileWidth = tw;
		tileHeight = th;
		count = c;
		frameDuration = fd;
		playMode = pm;
		animationModeCount = amc;
	}

	public static void clearAll() {
		LOG.debug("clearing all animation lists");
		for (EAnimationList al : animationListCache.keySet()) {
			al.clear();
		}
	}

	@Override
	public Enum<EAnimationList> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_ANIMATION_LIST;
	}

	@Override
	public AtlasAnimation[] getObject() {
		// Get texture
		final AtlasRegion atlasRegion = ResourceCache.getTexture(eTexture);
		if (atlasRegion == null)
			return null;
		final AtlasRegion[][] atlasRegionArray = splitAtlasRegion(atlasRegion, tileWidth, tileHeight);
		final AtlasAnimation[] animationList = new AtlasAnimation[animationModeCount];
		for (int i = 0; i != animationModeCount; ++i) {
			animationList[i] = new AtlasAnimation(frameDuration, atlasRegionArray[i]);
			animationList[i].setPlayMode(playMode);
		}
		return animationList;
	}

	@Override
	public void clear() {
		LOG.debug("clearing animation list: " + String.valueOf(this));
		animationListCache.remove(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public EnumMap getMap() {
		return animationListCache;
	}

	private AtlasRegion[][] splitAtlasRegion(AtlasRegion atlasRegion, int tileWidth, int tileHeight) {
		int x = atlasRegion.getRegionX();
		int y = atlasRegion.getRegionY();
		int width = atlasRegion.getRegionWidth();
		int height = atlasRegion.getRegionHeight();

		int rows = height / tileHeight;
		int cols = width / tileWidth;

		int startX = x;
		AtlasRegion[][] tiles = new AtlasRegion[rows][cols];
		for (int row = 0; row < rows; row++, y += tileHeight) {
			x = startX;
			for (int col = 0; col < cols; col++, x += tileWidth) {
				tiles[row][col] = new AtlasRegion(atlasRegion.getTexture(), x, y, tileWidth, tileHeight);
			}
		}

		return tiles;
	}

	@Override
	public void clearAllOfThisKind() {
		EAnimationList.clearAll();
	}
}
