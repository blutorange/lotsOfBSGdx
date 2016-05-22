package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

/**
 * For loading, caching and disposing {@link Texture} resources.
 * 
 * @author madgaksha
 *
 */
public enum ETexture implements IResource<ETexture,Texture> {
	MAIN_BACKGROUND("texture/mainBackground.jpg"),
	
	OVAL_SHADOW(ETextureAtlas.MISC, "ovalShadow"),
	
	// ==================
	//      ESTELLE
	// ==================
	ESTELLE_RUNNING("sprite/estelle00001.png"),
	ESTELLE_STANDING("sprite/estelle00100.png"),
	ESTELLE_SWINGING("sprite/estelle00107.png"),

	FACE_ESTELLE_01("texture/face/estelle01.png"),

	// ==================
	//      JOSHUA
	// ==================
	JOSHUA_RUNNING("sprite/joshua00011.png"),
	
	
	// ===================
	//      ENEMIES
	// ===================
	SOLIDER_RED_0("sprite/soldierRed0.png"),
	
	;

	private final static Logger LOG = Logger.getLogger(ETexture.class);
	private final static EnumMap<ETexture, Texture> textureCache = new EnumMap<ETexture, Texture>(ETexture.class);

	private String filename;
	private ETextureAtlas textureAtlas = null;	

	private ETexture(String f) {
		filename = f;
	}
	private ETexture(ETextureAtlas ta, String f) {
		textureAtlas = ta;
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
		if (textureAtlas != null) {
			TextureAtlas ta = ResourceCache.getTextureAtlas(textureAtlas); 
			return ta == null ? null : ta.findRegion(filename).getTexture();
		}
		else {
			final FileHandle fileHandle = Gdx.files.internal(filename);
			try {
				return new Texture(fileHandle);
			} catch (GdxRuntimeException e) {
				LOG.error("could not locate or open resource: " + String.valueOf(this), e);
				return null;
			}
		}
	}

	@Override
	public Enum<ETexture> getEnum() {
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