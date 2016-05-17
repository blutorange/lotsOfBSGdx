package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

/**
 * For loading, caching and disposing {@link Texture} resources.
 * 
 * @author madgaksha
 *
 */
public enum ETextureAtlas implements IResource {
	NINE_PATCHES("9patch/packed/9patchPacked.atlas");

	private final static Logger LOG = Logger.getLogger(ETextureAtlas.class);
	private final static EnumMap<ETextureAtlas, TextureAtlas> textureAtlasCache = new EnumMap<ETextureAtlas, TextureAtlas>(ETextureAtlas.class);

	private String filename;

	private ETextureAtlas(String f) {
		filename = f;
	}

	public static void clearAll() {
		LOG.debug("clearing all texture atlantes");
		for (ETextureAtlas ta : textureAtlasCache.keySet()) {
			ta.clear();
		}
	}
	
	@Override
	public TextureAtlas getObject() {
		final FileHandle fileHandle = Gdx.files.internal(filename);
		try {
			return new TextureAtlas(fileHandle);
		} catch (GdxRuntimeException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this));
			return null;
		}
	}

	@Override
	public Enum<ETextureAtlas> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_TEXTURE_ATLAS;
	}

	@Override
	public void clear() {
		LOG.debug("disposing texture atlas: " + String.valueOf(this));
		final TextureAtlas ta = textureAtlasCache.get(this);
		if (ta != null)
			ta.dispose();
		textureAtlasCache.remove(this);
	}

	@Override
	public EnumMap<ETextureAtlas, TextureAtlas> getMap() {
		return textureAtlasCache;
	}
}