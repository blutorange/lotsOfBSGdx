package de.homelab.madgaksha.resourcecache;

import java.awt.Dimension;
import java.util.EnumMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

/**
 * For loading, caching and disposing {@link Texture} resources.
 * 
 * @author madgaksha
 *
 */
public enum ENinePatch implements IResource<ENinePatch,NinePatch> {
	TEXTBOX_BLUE(ETextureAtlas.NINE_PATCHES,"textbox-blue-fc",new Dimension(-3,29),new Dimension(-128,-10), new Dimension(128,128)),
	
	STATUS_SCREEN_MAIN_FRAME(ETextureAtlas.STATUS_SCREEN,"soraFcBrownBox"),
	STATUS_SCREEN_CELL_FRAME(ETextureAtlas.STATUS_SCREEN,"niceBrownBox"),
	STATUS_SCREEN_HP_BAR_PLAYER(ETextureAtlas.STATUS_SCREEN,"pointBarBrownEllipse"),
	STATUS_SCREEN_HP_BAR_FILL(ETextureAtlas.STATUS_SCREEN,"hpBarFill"),
	STATUS_SCREEN_DATA_FRAME(ETextureAtlas.STATUS_SCREEN,"bracketsLrtd"),
	
	PAUSE_LAYER_OVERLAY(ETextureAtlas.NINE_PATCHES,"whiteRectangle"),
	
	;

	private final static Logger LOG = Logger.getLogger(ENinePatch.class);
	private final static EnumMap<ENinePatch, NinePatch> ninePatchCache = new EnumMap<ENinePatch, NinePatch>(ENinePatch.class);

	private final String patchName;
	private final ETextureAtlas textureAtlas;
	private final Dimension offsetFace;
	private final Dimension offsetSpeaker;
	private final Dimension sizeFace;

	/**
	 * 
	 * @param ta Texture atlas containing the nine patch.
	 * @param p Name of the nine patch.
	 * @param os Speaker name offset to the left point where the text starts, and to the middle of the text line. From the top left of content area.
	 * @param of Face image offset to the image's bottom left point. In Pixels from the bottom right corner of the content area.
	 * @param sf Size of the face area (width x height) in pixels. 
	 * 
	 * os, of, and sf can be any value when nine patch is not used as a text box.
	 */
	private ENinePatch(ETextureAtlas ta, String p, Dimension os, Dimension of, Dimension sf) {
		patchName = p;
		textureAtlas = ta;
		offsetSpeaker = os;
		offsetFace = of;
		sizeFace = sf;
	}
	
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
			return atlas.createPatch(patchName);
		} catch (GdxRuntimeException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this));
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
		//final NinePatch np = ninePatchCache.get(this);
		ninePatchCache.remove(this);
	}

	@Override
	public EnumMap<ENinePatch, NinePatch> getMap() {
		return ninePatchCache;
	}

}