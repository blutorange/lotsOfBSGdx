package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

/**
 * For loading, caching and disposing {@link Texture} resources.
 * 
 * @author madgaksha
 *
 */
public enum ETexture implements IResource<ETexture,TextureRegion> {
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
	
	
	// ===================
	//    STATUSSCREEN
	// ===================
	STATUS_ICON_TIME(ETextureAtlas.STATUS_SCREEN, "iconTimeSmall"),
	STATUS_ICON_SCORE(ETextureAtlas.STATUS_SCREEN, "iconScoreSmall"),
	STATUS_ICON_WEAPON(ETextureAtlas.STATUS_SCREEN, "iconWeaponSmall"),
	STATUS_ICON_TOKUGI(ETextureAtlas.STATUS_SCREEN, "iconTokugiSmall"),
	STATUS_ICON_ENEMY(ETextureAtlas.STATUS_SCREEN, "iconEnemySmall"),
	
	STATUS_ICON_NUMERAL_0(ETextureAtlas.STATUS_SCREEN, "javanese0c"),
	STATUS_ICON_NUMERAL_1(ETextureAtlas.STATUS_SCREEN, "javanese1c"),
	STATUS_ICON_NUMERAL_2(ETextureAtlas.STATUS_SCREEN, "javanese2c"),
	STATUS_ICON_NUMERAL_3(ETextureAtlas.STATUS_SCREEN, "javanese3c"),
	STATUS_ICON_NUMERAL_4(ETextureAtlas.STATUS_SCREEN, "javanese4c"),
	STATUS_ICON_NUMERAL_5(ETextureAtlas.STATUS_SCREEN, "javanese5c"),
	STATUS_ICON_NUMERAL_6(ETextureAtlas.STATUS_SCREEN, "javanese6c"),
	STATUS_ICON_NUMERAL_7(ETextureAtlas.STATUS_SCREEN, "javanese7c"),
	STATUS_ICON_NUMERAL_8(ETextureAtlas.STATUS_SCREEN, "javanese8c"),
	STATUS_ICON_NUMERAL_9(ETextureAtlas.STATUS_SCREEN, "javanese9c"),
	STATUS_ICON_SEPARATOR_TIME(ETextureAtlas.STATUS_SCREEN, "slash"),
	
	;

	private final static Logger LOG = Logger.getLogger(ETexture.class);
	private final static EnumMap<ETexture, TextureRegion> textureCache = new EnumMap<ETexture, TextureRegion>(ETexture.class);

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

	public Sprite asSprite() {
		if (textureAtlas != null) return ResourceCache.getTextureAtlas(textureAtlas).createSprite(filename);
		return new Sprite(ResourceCache.getTexture(this));
	}

	@Override
	public TextureRegion getObject() {
		if (textureAtlas != null) {
			TextureAtlas ta = ResourceCache.getTextureAtlas(textureAtlas);
			return ta == null ? null : ta.findRegion(filename);
		}
		else {
			final FileHandle fileHandle = Gdx.files.internal(filename);
			try {
				return new TextureRegion(new Texture(fileHandle));
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
		final TextureRegion tr = textureCache.get(this);
		if (tr != null)
			tr.getTexture().dispose();
		textureCache.remove(this);
	}

	@Override
	public EnumMap<ETexture, TextureRegion> getMap() {
		return textureCache;
	}
}