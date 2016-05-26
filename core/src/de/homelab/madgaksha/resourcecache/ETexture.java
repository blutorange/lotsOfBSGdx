package de.homelab.madgaksha.resourcecache;

import de.homelab.madgaksha.i18n.i18n;

import java.io.File;
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
	SOLDIER_RED_0("sprite/soldierRed0.png"),
	SOLDIER_RED_0_MAIN("texture/statusicon/enemy/soldierRed-main.png"),
	SOLDIER_RED_0_SUB("texture/statusicon/enemy","soldierRed-sub.png"),
	
	SOLDIER_GREEN_0("sprite/soldierGreen0.png"),
	SOLDIER_GREEN_0_MAIN("texture/statusicon/enemy/soldierGreen-main.png"),
	SOLDIER_GREEN_0_SUB("texture/statusicon/enemy","soldierGreen-sub.png"),

	
	// ===================
	//    STATUSSCREEN
	// ===================
	STATUS_ICON_TIME(ETextureAtlas.STATUS_SCREEN, "iconTimeSmall"),
	STATUS_ICON_SCORE(ETextureAtlas.STATUS_SCREEN, "iconScoreSmall"),
	STATUS_ICON_PAINBAR(ETextureAtlas.STATUS_SCREEN, "iconPainBarSmall"),
	STATUS_ICON_WEAPON(ETextureAtlas.STATUS_SCREEN, "iconWeaponSmall"),
	STATUS_ICON_TOKUGI(ETextureAtlas.STATUS_SCREEN, "iconTokugiSmall"),
	STATUS_ICON_ENEMY(ETextureAtlas.STATUS_SCREEN, "iconEnemySmall"),
	
	STATUS_ICON_NUMERAL_0(ETextureAtlas.STATUS_SCREEN, "latin0"),
	STATUS_ICON_NUMERAL_1(ETextureAtlas.STATUS_SCREEN, "latin1"),
	STATUS_ICON_NUMERAL_2(ETextureAtlas.STATUS_SCREEN, "latin2"),
	STATUS_ICON_NUMERAL_3(ETextureAtlas.STATUS_SCREEN, "latin3"),
	STATUS_ICON_NUMERAL_4(ETextureAtlas.STATUS_SCREEN, "latin4"),
	STATUS_ICON_NUMERAL_5(ETextureAtlas.STATUS_SCREEN, "latin5"),
	STATUS_ICON_NUMERAL_6(ETextureAtlas.STATUS_SCREEN, "latin6"),
	STATUS_ICON_NUMERAL_7(ETextureAtlas.STATUS_SCREEN, "latin7"),
	STATUS_ICON_NUMERAL_8(ETextureAtlas.STATUS_SCREEN, "latin8"),
	STATUS_ICON_NUMERAL_9(ETextureAtlas.STATUS_SCREEN, "latin9"),
	STATUS_ICON_SEPARATOR_TIME(ETextureAtlas.STATUS_SCREEN, "slash"), 
	STATUS_ICON_NO_ENEMY_MAIN("texture/statusicon/enemy/none-main.png"),
	STATUS_ICON_NO_ENEMY_SUB("texture/statusicon/enemy","none-sub.png"),
	
	// =========================================================================
	//       WEAPONS
	// main icons must be 1:1 aspect ratio
	// sub icons must be 2.5:1 aspect ratio (horizontally) or 1:2.5 (vertically) 
	// =========================================================================
	WEAPON_NONE_ICON_MAIN(ETextureAtlas.STATUS_SCREEN, "iconWeaponNoneMain"),
	WEAPON_NONE_ICON_SUB("texture/statusicon/weapon","none-sub.png"),
	
	
	// =========================================================================
	//       TOKUGI
	// main icons must be 1:1 aspect ratio
	// sub icons must be 2.5:1 aspect ratio (horizontally) or 1:2.5 (vertically) 
	// =========================================================================
	TOKUGI_NONE_ICON_MAIN(ETextureAtlas.STATUS_SCREEN, "iconTokugiNoneMain"),
	TOKUGI_NONE_ICON_SUB("texture/statusicon/tokugi","none-sub.png"),
	
	// =========================================================================
	//     LEVEL
	// icons horizontal must be 3:1 aspect ratio
	// icons vertical must be 1:3 aspect ratio
	// =========================================================================
	LEVEL_01_ICON("map/level01-icon.png"),

	
	// =========================================================================
	//     BULLETS
	// =========================================================================
	BULLET_PACMAN_LIGHTYELLOW("bullets/basic/pPacman/pPacman12.png"),
	BULLET_FLOWER_RED("bullets/basic/pFlower/pFlower1.png"),
	
	;

	private final static Logger LOG = Logger.getLogger(ETexture.class);
	private final static EnumMap<ETexture, TextureRegion> textureCache = new EnumMap<ETexture, TextureRegion>(ETexture.class);

	private String filename;
	private String basename = null;
	private ETextureAtlas textureAtlas = null;	

	
	private ETexture(String f) {
		filename = f;
	}
	private ETexture(String dir, String name) {
		filename = dir;
		basename = name;
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
		TextureRegion tr = ResourceCache.getTexture(this);
		return tr != null ? new Sprite(tr) : null;
	}

	@Override
	public TextureRegion getObject() {
		if (textureAtlas != null) {
			TextureAtlas ta = ResourceCache.getTextureAtlas(textureAtlas);
			return ta == null ? null : ta.findRegion(filename);
		}
		else {
			String path;
			if (basename != null) {
				// localizable images
				path = filename + File.separator + i18n.getShortName() + File.separator + basename; 
			}
			else path = filename;
			final FileHandle fileHandle = Gdx.files.internal(path);
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