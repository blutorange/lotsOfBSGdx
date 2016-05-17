package de.homelab.madgaksha.resourcecache;

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
public enum ENinePatch implements IResource {
	TEXTBOX_BLUE(ETextureAtlas.NINE_PATCHES,"textbox-blue");

	private final static Logger LOG = Logger.getLogger(ENinePatch.class);
	private final static EnumMap<ENinePatch, NinePatch> ninePatchCache = new EnumMap<ENinePatch, NinePatch>(ENinePatch.class);

	private String patchName;
	private ETextureAtlas textureAtlas;

	private ENinePatch(ETextureAtlas ta, String p) {
		patchName = p;
		textureAtlas = ta;
	}

	public static void clearAll() {
		LOG.debug("clearing all nine patches");
		for (ENinePatch np : ninePatchCache.keySet()) {
			np.clear();
		}
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