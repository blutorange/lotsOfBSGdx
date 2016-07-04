package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

/**
 * Different animations for each mode, eg. different looking directions.
 * Can be initialized with a sprite sheet, in which case it must start
 * at the top-left.
 * <br><br>
 * Alternatively, it can be initialized with a list of {@link EAnimation}.
 * @author madgaksha
 *
 */
public enum EAnimationList implements IResource<EAnimationList, AtlasAnimation[]> {
	// =================
	// ESTELLE
	// =================
	ESTELLE_RUNNING(ETexture.ESTELLE_RUNNING, 64, 128, 0.1f, AtlasAnimation.PlayMode.LOOP, 8, 8),
	ESTELLE_STANDING(ETexture.ESTELLE_STANDING, 128, 128, 0.1f, AtlasAnimation.PlayMode.LOOP, 8, 8),

	// =================
	// JOSHUA
	// =================
	JOSHUA_STANDING(ETexture.JOSHUA_STANDING, 64, 128, 0.1f, AtlasAnimation.PlayMode.LOOP, 8, 8),

	// =================
	// ENEMIES
	// =================
	SOLDIER_RED_0(ETextureAtlas.SOLDIER_RED, "SOLDIER_RED_0", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),
	SOLDIER_RED_1(ETextureAtlas.SOLDIER_RED, "SOLDIER_RED_1", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),
	SOLDIER_GREEN_0(ETextureAtlas.SOLDIER_GREEN, "SOLDIER_GREEN_0", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),
	SOLDIER_GREEN_1(ETextureAtlas.SOLDIER_GREEN, "SOLDIER_GREEN_1", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),
	ICER_0(ETextureAtlas.ICER, "ICER_0", 8, 0.2f, AtlasAnimation.PlayMode.LOOP),
	ICER_1(ETextureAtlas.ICER, "ICER_1", 8, 0.2f, AtlasAnimation.PlayMode.LOOP),
	;

	private final static Logger LOG = Logger.getLogger(EAnimationList.class);
	private final static EnumMap<EAnimationList, AtlasAnimation[]> animationListCache = new EnumMap<EAnimationList, AtlasAnimation[]>(
			EAnimationList.class);

	private final int mode;
	private final Object data;
	private String name;
	private int tileWidth;
	private int tileHeight;
	private int frameCount;
	private float frameDuration;
	private AtlasAnimation.PlayMode playMode;
	private int animationModeCount;

	private EAnimationList(EAnimation... animationList) {
		this.data = animationList;
		this.mode = 0;
	}
	
	private EAnimationList(ETextureAtlas textureAtlas, String name, int animationModeCount, float frameDuration, AtlasAnimation.PlayMode playMode) {
		this.playMode = playMode;
		this.animationModeCount = animationModeCount;
		this.frameDuration = frameDuration;
		this.name = name;
		this.data = textureAtlas;
		this.mode = 1;
	}

	
	private EAnimationList(ETexture et, int tw, int th, float fd, AtlasAnimation.PlayMode pm, int amc, int fc) {
		data = et;
		mode = 2;
		tileWidth = tw;
		tileHeight = th;
		frameCount = fc;
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
		AtlasAnimation[] atlasAnimationList;
		switch (mode) {
		// EAnimation[]
		case 0:
			EAnimation[] animationList = (EAnimation[])data;
			atlasAnimationList = new AtlasAnimation[animationList.length];
			for (int i = 0; i != animationList.length; ++i) {
				atlasAnimationList[i] = ResourceCache.getAnimation(animationList[i]);
				if (animationList[i] == null)
					return null;
			}			
			break;
		// ETextureAtlas
		case 1:
			TextureAtlas textureAtlas = ResourceCache.getTextureAtlas((ETextureAtlas)data);
			if (textureAtlas == null) return null;
			atlasAnimationList = new AtlasAnimation[animationModeCount];
			for (int i = 0; i != animationModeCount; ++i) {
				Array<AtlasRegion> atlasRegionArray = textureAtlas.findRegions(name + "_" + i);
				if (atlasRegionArray == null || atlasRegionArray.size == 0)
					return null;
				AtlasAnimation animation = new AtlasAnimation(frameDuration, atlasRegionArray);
				animation.setPlayMode(playMode);
				atlasAnimationList[i] = animation;
			}
			break;
		// ETexture
		case 2:
			// Retrieve sprite sheet.
			AtlasRegion atlasRegion = ResourceCache.getTexture((ETexture)data);
			if (atlasRegion == null)
				return null;
			// Split sprite sheet.
			AtlasRegion[][] atlasRegionArray = splitAtlasRegion(atlasRegion);
			atlasAnimationList = new AtlasAnimation[animationModeCount];
			for (int i = 0; i != animationModeCount; ++i) {
				atlasAnimationList[i] = new AtlasAnimation(frameDuration, atlasRegionArray[i]);
				atlasAnimationList[i].setPlayMode(playMode);
			}			
			break;
		default:
			return null;
		}
		return atlasAnimationList;
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

	private AtlasRegion[][] splitAtlasRegion(AtlasRegion atlasRegion) {
		int width = atlasRegion.getRegionWidth();
		int height = atlasRegion.getRegionHeight();
		// Origin of the coordinate system is to the top left.
		// Sprite sheets (should) should start on the top-left as well.
		int x = atlasRegion.getRegionX();
		int y = atlasRegion.getRegionY();

		int rows = Math.min(animationModeCount, height / tileHeight);
		int cols = Math.min(frameCount, width / tileWidth);

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
