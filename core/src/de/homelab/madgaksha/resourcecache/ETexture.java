package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

/**
 * For loading, caching and disposing {@link Texture} resources.
 * 
 * @author madgaksha
 *
 */
public enum ETexture implements IResource {
	BADLOGIC("badlogic.jpg"), FOOLEVEL_BACKGROUND("texture/foolevelBackground.jpg"),

	ESTELLE_RUNNING("texture/ch00001.png"), ESTELLE_STANDING("texture/ch0010b.png"), ESTELLE_SWINGING(
			"texture/ch00107.png"),

	JOSHUA_RUNNING("texture/ch00011.png");

	private final static Logger LOG = Logger.getLogger(ETexture.class);
	private final static EnumMap<ETexture, Texture> textureCache = new EnumMap<ETexture, Texture>(ETexture.class);

	private String filename;

	private ETexture(String f) {
		filename = f;
	}

	public static void clearAll() {
		LOG.debug("clearing all textures");
		for (ETexture txt : textureCache.keySet()) {
			txt.clear();
		}
	}

	public void toSprite(Sprite sprite) {
		final Texture texture = ResourceCache.getTexture(this);
		sprite.setTexture(texture);
		sprite.setSize(texture.getWidth(), texture.getHeight());
		sprite.setRegion(0, 0, texture.getWidth(), texture.getHeight());
	}

	public Sprite asSprite() {
		final Sprite sprite = new Sprite();
		toSprite(sprite);
		return sprite;
	}

	@Override
	public Texture getObject() {
		final FileHandle fileHandle = Gdx.files.internal(filename);
		try {
			return new Texture(fileHandle);
		} catch (GdxRuntimeException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this));
			return null;
		}
	}

	@Override
	public Enum<?> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_TEXTURE;
	}

	@Override
	public void clear() {
		LOG.debug("disposing texture: " + String.valueOf(this));
		final Texture t = textureCache.get(this);
		if (t != null)
			t.dispose();
		textureCache.remove(this);
	}

	@Override
	public EnumMap<ETexture, Texture> getMap() {
		return textureCache;
	}
}