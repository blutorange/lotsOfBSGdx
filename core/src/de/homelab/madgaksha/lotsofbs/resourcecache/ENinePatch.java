package de.homelab.madgaksha.lotsofbs.resourcecache;

import java.awt.Dimension;
import java.util.EnumMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * For loading, caching and disposing {@link Texture} resources.
 * 
 * @author madgaksha
 *
 */
public enum ENinePatch implements IResource<ENinePatch, NinePatch> {
	DEFAULT(ETextureAtlas.NINE_PATCHES, "default"),

	OUGI_OUKA_MUSOUGEKI_BACKGROUND_1(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "background1"),
	OUGI_OUKA_MUSOUGEKI_BACKGROUND_2(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "background2"),

	TEXTBOX_BLUE_ALL(ETextureAtlas.NINE_PATCHES, "textbox-blue-fc-all"),
	TEXTBOX_BLUE_BOTTOM(ETextureAtlas.NINE_PATCHES, "textbox-blue-fc-bottom"),
	TEXTBOX_BLUE_TOP(ETextureAtlas.NINE_PATCHES, "textbox-blue-fc-top"),
	TEXTBOX_BLUE_LEFT(ETextureAtlas.NINE_PATCHES, "textbox-blue-fc-left"),
	TEXTBOX_BLUE_RIGHT(ETextureAtlas.NINE_PATCHES, "textbox-blue-fc-right"),
	TEXTBOX_BLUE_BOTTOM_LEFT(ETextureAtlas.NINE_PATCHES, "textbox-blue-fc-bottom-left"),
	TEXTBOX_BLUE_BOTTOM_RIGHT(ETextureAtlas.NINE_PATCHES, "textbox-blue-fc-bottom-right"),

	STATUS_SCREEN_MAIN_FRAME(ETextureAtlas.STATUS_SCREEN, "soraFcBrownBox"),
	STATUS_SCREEN_CELL_FRAME(ETextureAtlas.STATUS_SCREEN, "niceBrownBox"),
	STATUS_SCREEN_HP_BAR_PLAYER(ETextureAtlas.STATUS_SCREEN, "pointBarBrownEllipse"),
	STATUS_SCREEN_HP_BAR_FILL(ETextureAtlas.STATUS_SCREEN, "hpBarFill"),
	STATUS_SCREEN_DATA_FRAME(ETextureAtlas.STATUS_SCREEN, "bracketsLrtd"),

	WHITE_RECTANGLE(ETextureAtlas.NINE_PATCHES, "whiteRectangle"),
	;

	private final static Logger LOG = Logger.getLogger(ENinePatch.class);
	private final static EnumMap<ENinePatch, NinePatch> ninePatchCache = new EnumMap<ENinePatch, NinePatch>(
			ENinePatch.class);

	private final String patchName;
	private final ETextureAtlas textureAtlas;
	private final Dimension offsetFace;
	private final Dimension offsetSpeaker;
	private final Dimension sizeFace;

	/**
	 * @param ta
	 *            Texture atlas containing the nine patch.
	 * @param p
	 *            Name of the nine patch.
	 */
	private ENinePatch(ETextureAtlas ta, String p) {
		patchName = p;
		textureAtlas = ta;
		offsetSpeaker = null;
		offsetFace = null;
		sizeFace = null;
	}

	public static void clearAll() {
		LOG.debug("clearing all nine patches");
		for (ENinePatch np : ninePatchCache.keySet()) {
			np.clear();
		}
	}

	public int getOffsetSpeakerX() {
		return offsetSpeaker.width;
	}

	public int getOffsetSpeakerY() {
		return offsetSpeaker.height;
	}

	public int getOffsetFaceX() {
		return offsetFace.width;
	}

	public int getOffsetFaceY() {
		return offsetFace.height;
	}

	public int getFaceWidth() {
		return sizeFace.width;
	}

	public int getFaceHeight() {
		return sizeFace.height;
	}

	@Override
	public NinePatch getObject() {
		try {
			final TextureAtlas atlas = ResourceCache.getTextureAtlas(textureAtlas);
			final NinePatch ninePatch = atlas.createPatch(patchName);
			if (ninePatch == null) {
				LOG.error("could not locate or open resource: " + String.valueOf(this));
			}
			return ninePatch;
		} catch (GdxRuntimeException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
			return null;
		} catch (IllegalArgumentException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
			return null;
		}
	}

	@Override
	public Enum<ENinePatch> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_NINE_PATCH;
	}

	@Override
	public void clear() {
		LOG.debug("disposing nine patch: " + String.valueOf(this));
		ninePatchCache.remove(this);
	}

	@Override
	public EnumMap<ENinePatch, NinePatch> getMap() {
		return ninePatchCache;
	}

	@Override
	public void clearAllOfThisKind() {
		ENinePatch.clearAll();
	}

}