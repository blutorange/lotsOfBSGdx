package de.homelab.madgaksha.resourcecache;

import java.io.File;
import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.i18n.I18n;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.PoolableAtlasSprite;
import de.homelab.madgaksha.resourcepool.SpritePool;

/**
 * For loading, caching and disposing {@link Texture} resources.
 * 
 * @author madgaksha
 *
 */
public enum ETexture implements IResource<ETexture, AtlasRegion> {
	MAIN_BACKGROUND("texture/mainBackground.jpg"),

	OVAL_SHADOW(ETextureAtlas.MISC, "ovalShadow"),

	DEFAULT(ETextureAtlas.MISC, "default"),
	
	// ==================
	// BATTLE
	// ==================
	HIT_CIRCLE_YELLOW(ETextureAtlas.MISC, "hitCircleYellow"),
	BATTLE_STIGMA_GREEN(ETextureAtlas.MISC, "battleStigmaGreen", 0.5f),
	TARGET_SELECT_CIRCLE_WHEEL(ETextureAtlas.MISC, "targetSelectCircleWheel"),
	CUTIN_BATTLE_MODE_ACTIVATE("texture/battleModeActivate.png"),

	// ==================
	// ESTELLE
	// ==================
	ESTELLE_RUNNING("sprite/estelle00001.png"),
	ESTELLE_STANDING("sprite/estelle00100.png"),
	ESTELLE_SWINGING("sprite/estelle00107.png"),
	ESTELLE_ON_KNEES("sprite/estelle00104.png"),

	FACE_ESTELLE_01(ETextureAtlas.FACES_ESTELLE, "estelle01"),
	FACE_ESTELLE_02(ETextureAtlas.FACES_ESTELLE, "estelle02"),
	FACE_ESTELLE_03(ETextureAtlas.FACES_ESTELLE, "estelle03"),
	FACE_ESTELLE_04(ETextureAtlas.FACES_ESTELLE, "estelle04"),
	FACE_ESTELLE_05(ETextureAtlas.FACES_ESTELLE, "estelle05"),
	FACE_ESTELLE_06(ETextureAtlas.FACES_ESTELLE, "estelle06"),
	FACE_ESTELLE_07(ETextureAtlas.FACES_ESTELLE, "estelle07"),
	FACE_ESTELLE_08(ETextureAtlas.FACES_ESTELLE, "estelle08"),
	FACE_ESTELLE_09(ETextureAtlas.FACES_ESTELLE, "estelle09"),
	FACE_ESTELLE_10(ETextureAtlas.FACES_ESTELLE, "estelle10"),

	// ==================
	// JOSHUA
	// ==================
	JOSHUA_STANDING("sprite/joshua00010.png"),
	FACE_JOSHUA_01(ETextureAtlas.FACES_JOSHUA, "joshua01"),
	FACE_JOSHUA_02(ETextureAtlas.FACES_JOSHUA, "joshua02"),
	FACE_JOSHUA_03(ETextureAtlas.FACES_JOSHUA, "joshua03"),
	FACE_JOSHUA_04(ETextureAtlas.FACES_JOSHUA, "joshua04"),
	FACE_JOSHUA_05(ETextureAtlas.FACES_JOSHUA, "joshua05"),
	FACE_JOSHUA_06(ETextureAtlas.FACES_JOSHUA, "joshua06"),
	FACE_JOSHUA_07(ETextureAtlas.FACES_JOSHUA, "joshua07"),
	FACE_JOSHUA_08(ETextureAtlas.FACES_JOSHUA, "joshua08"),
	FACE_JOSHUA_09(ETextureAtlas.FACES_JOSHUA, "joshua09"),
	FACE_JOSHUA_10(ETextureAtlas.FACES_JOSHUA, "joshua10"),
	FACE_JOSHUA_11(ETextureAtlas.FACES_JOSHUA, "joshua11"),

	// ===================
	// ENEMIES
	// ===================
	SOLDIER_RED_0("sprite/soldierRed0.png"),
	SOLDIER_RED_0_MAIN("texture/statusicon/enemy/soldierRed-main.png"),
	SOLDIER_RED_0_SUB("texture/statusicon/enemy", "soldierRed-sub.png"),

	SOLDIER_GREEN_0("sprite/soldierGreen0.png"),
	SOLDIER_GREEN_0_MAIN("texture/statusicon/enemy/soldierGreen-main.png"),
	SOLDIER_GREEN_0_SUB("texture/statusicon/enemy", "soldierGreen-sub.png"),

	// ===================
	// STATUSSCREEN
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
	STATUS_ICON_NO_ENEMY_SUB("texture/statusicon/enemy", "none-sub.png"),

	// =========================================================================
	// WEAPONS
	// main icons must be 1:1 aspect ratio
	// sub icons must be 2.5:1 aspect ratio (horizontally) or 1:2.5 (vertically)
	// =========================================================================
	WEAPON_NONE_ICON_MAIN(ETextureAtlas.STATUS_SCREEN, "iconWeaponNoneMain"),
	WEAPON_NONE_ICON_SUB("texture/statusicon/weapon", "none-sub.png"),
	WEAPON_BASIC_ICON_MAIN("texture/statusicon/weapon/basic-main.png"),
	WEAPON_BASIC_ICON_SUB("texture/statusicon/weapon", "basic-sub.png"),
	WEAPON_MULTI_ICON_MAIN("texture/statusicon/weapon/multi-main.png"),
	WEAPON_MULTI_ICON_SUB("texture/statusicon/weapon", "multi-sub.png"),

	// =========================================================================
	// TOKUGI
	// main icons must be 1:1 aspect ratio
	// sub icons must be 2.5:1 aspect ratio (horizontally) or 1:2.5 (vertically)
	// =========================================================================
	TOKUGI_NONE_ICON_MAIN(ETextureAtlas.STATUS_SCREEN, "iconTokugiNoneMain"),
	TOKUGI_NONE_ICON_SUB("texture/statusicon/tokugi", "none-sub.png"),

	
	// =========================================================================
	// OUGI
	// =========================================================================
	OUGI_OUKA_MUSOUGEKI_RAY14(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "pRay14"),
	OUGI_OUKA_MUSOUGEKI_CUTIN_BODY(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "cutinBody"),
	OUGI_OUKA_MUSOUGEKI_STAND0(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "stand0"),
	OUGI_OUKA_MUSOUGEKI_KNEEL0(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "kneel0"),
	OUGI_OUKA_MUSOUGEKI_RUN0(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "run0"),
	OUGI_OUKA_MUSOUGEKI_RUN1(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "run1"),
	OUGI_OUKA_MUSOUGEKI_RUN2(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "run2"),
	OUGI_OUKA_MUSOUGEKI_RUN3(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "run3"),
	OUGI_OUKA_MUSOUGEKI_RUN4(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "run4"),
	OUGI_OUKA_MUSOUGEKI_RUN5(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "run5"),
	OUGI_OUKA_MUSOUGEKI_RUN6(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "run6"),
	OUGI_OUKA_MUSOUGEKI_RUN7(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "run7"),
	OUGI_OUKA_MUSOUGEKI_JUMP0(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "jump0"),
	OUGI_OUKA_MUSOUGEKI_JUMP1(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "jump1"),
	OUGI_OUKA_MUSOUGEKI_JUMP2(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "jump2"),
	OUGI_OUKA_MUSOUGEKI_JUMP3(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "jump3"),
	OUGI_OUKA_MUSOUGEKI_JUMP4(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "jump4"),
	OUGI_OUKA_MUSOUGEKI_JUMP5(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "jump5"),
	OUGI_OUKA_MUSOUGEKI_JUMP6(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "jump6"),
	OUGI_OUKA_MUSOUGEKI_JUMP7(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "jump7"),
	OUGI_OUKA_MUSOUGEKI_SPIN1(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "spin1"),
	OUGI_OUKA_MUSOUGEKI_SPIN2(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "spin2"),
	OUGI_OUKA_MUSOUGEKI_SPIN3(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "spin3"),
	OUGI_OUKA_MUSOUGEKI_SPIN4(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "spin4"),
	OUGI_OUKA_MUSOUGEKI_SPIN5(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "spin5"),
	OUGI_OUKA_MUSOUGEKI_SPIN6(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "spin6"),
	OUGI_OUKA_MUSOUGEKI_SPIN7(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "spin7"),
	OUGI_OUKA_MUSOUGEKI_SPIN8(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "spin8"),
	OUGI_OUKA_MUSOUGEKI_SPIN9(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "spin9"),
	OUGI_OUKA_MUSOUGEKI_SPIN10(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "spin10"),
	OUGI_OUKA_MUSOUGEKI_FINALHIT0(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "finalhit0"),
	OUGI_OUKA_MUSOUGEKI_FINALHIT1(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "finalhit1"),
	OUGI_OUKA_MUSOUGEKI_FINALHIT2(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "finalhit2"),
	OUGI_OUKA_MUSOUGEKI_FINALHIT3(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "finalhit3"),
	OUGI_OUKA_MUSOUGEKI_FINALHIT4(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "finalhit4"),
	OUGI_OUKA_MUSOUGEKI_FINALHIT5(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "finalhit5"),
	OUGI_OUKA_MUSOUGEKI_SWORDRAIN0(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "swordrain0"),
	OUGI_OUKA_MUSOUGEKI_SWORDRAIN1(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "swordrain1"),
	OUGI_OUKA_MUSOUGEKI_SWORDRAIN2(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "swordrain2"),
	OUGI_OUKA_MUSOUGEKI_SWORDRAIN3(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "swordrain3"),
	OUGI_OUKA_MUSOUGEKI_ESTELLE_FACE(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "estelleFace"),
	OUGI_OUKA_MUSOUGEKI_ESTELLE_FACE_WHITE(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "estelleFaceWhite"),
	OUGI_OUKA_MUSOUGEKI_ESTELLE_FACE_BG(ETextureAtlas.OUGI_OUKA_MUSOUGEKI, "estelleFaceBg"),
	
	
	// =========================================================================
	// LEVEL
	// icons horizontal must be 3:1 aspect ratio
	// icons vertical must be 1:3 aspect ratio
	// =========================================================================
	LEVEL_01_ICON("map/level01-icon.png"),

	// =========================================================================
	// BULLETS
	// =========================================================================
	BULLET_ORB_NOCOLOR(ETextureAtlas.BULLETS_BASIC, "pOrb0"),

	BULLET_PACMAN_BLACK(ETextureAtlas.BULLETS_BASIC, "pPacman0"),
	BULLET_PACMAN_RED(ETextureAtlas.BULLETS_BASIC, "pPacman1"),
	BULLET_PACMAN_LIGHTRED(ETextureAtlas.BULLETS_BASIC, "pPacman2"),
	BULLET_PACMAN_PINK(ETextureAtlas.BULLETS_BASIC, "pPacman3"),
	BULLET_PACMAN_LIGHTPINK(ETextureAtlas.BULLETS_BASIC, "pPacman4"),
	BULLET_PACMAN_BLUE(ETextureAtlas.BULLETS_BASIC, "pPacman5"),
	BULLET_PACMAN_LIGHTBLUE(ETextureAtlas.BULLETS_BASIC, "pPacman6"),
	BULLET_PACMAN_CYAN(ETextureAtlas.BULLETS_BASIC, "pPacman7"),
	BULLET_PACMAN_LIGHTCYAN(ETextureAtlas.BULLETS_BASIC, "pPacman8"),
	BULLET_PACMAN_GREEN(ETextureAtlas.BULLETS_BASIC, "pPacman9"),
	BULLET_PACMAN_LIGHTGREEN(ETextureAtlas.BULLETS_BASIC, "pPacman10"),
	BULLET_PACMAN_YELLOW(ETextureAtlas.BULLETS_BASIC, "pPacman11"),
	BULLET_PACMAN_LIGHTYELLOW(ETextureAtlas.BULLETS_BASIC, "pPacman12"),
	BULLET_PACMAN_ORANGE(ETextureAtlas.BULLETS_BASIC, "pPacman13"),
	BULLET_PACMAN_LIGHTORANGE(ETextureAtlas.BULLETS_BASIC, "pPacman14"),
	BULLET_PACMAN_WHITE(ETextureAtlas.BULLETS_BASIC, "pPacman15"),

	BULLET_STAR_BLACK(ETextureAtlas.BULLETS_BASIC, "pStar0"),
	BULLET_STAR_RED(ETextureAtlas.BULLETS_BASIC, "pStar1"),
	BULLET_STAR_LIGHTRED(ETextureAtlas.BULLETS_BASIC, "pStar2"),
	BULLET_STAR_PINK(ETextureAtlas.BULLETS_BASIC, "pStar3"),
	BULLET_STAR_LIGHTPINK(ETextureAtlas.BULLETS_BASIC, "pStar4"),
	BULLET_STAR_BLUE(ETextureAtlas.BULLETS_BASIC, "pStar5"),
	BULLET_STAR_LIGHTBLUE(ETextureAtlas.BULLETS_BASIC, "pStar6"),
	BULLET_STAR_CYAN(ETextureAtlas.BULLETS_BASIC, "pStar7"),
	BULLET_STAR_LIGHTCYAN(ETextureAtlas.BULLETS_BASIC, "pStar8"),
	BULLET_STAR_GREEN(ETextureAtlas.BULLETS_BASIC, "pStar9"),
	BULLET_STAR_LIGHTGREEN(ETextureAtlas.BULLETS_BASIC, "pStar10"),
	BULLET_STAR_YELLOW(ETextureAtlas.BULLETS_BASIC, "pStar11"),
	BULLET_STAR_LIGHTYELLOW(ETextureAtlas.BULLETS_BASIC, "pStar12"),
	BULLET_STAR_ORANGE(ETextureAtlas.BULLETS_BASIC, "pStar13"),
	BULLET_STAR_LIGHTORANGE(ETextureAtlas.BULLETS_BASIC, "pStar14"),
	BULLET_STAR_WHITE(ETextureAtlas.BULLETS_BASIC, "pStar15"),
	
	BULLET_FLOWER_BLACK(ETextureAtlas.BULLETS_BASIC, "pFlower0"),
	BULLET_FLOWER_RED(ETextureAtlas.BULLETS_BASIC, "pFlower1"),
	BULLET_FLOWER_PINK(ETextureAtlas.BULLETS_BASIC, "pFlower2"),
	BULLET_FLOWER_BLUE(ETextureAtlas.BULLETS_BASIC, "pFlower3"),
	BULLET_FLOWER_CYAN(ETextureAtlas.BULLETS_BASIC, "pFlower4"),
	BULLET_FLOWER_GREEN(ETextureAtlas.BULLETS_BASIC, "pFlower5"),
	BULLET_FLOWER_YELLOW(ETextureAtlas.BULLETS_BASIC, "pFlower6"),
	BULLET_FLOWER_WHITE(ETextureAtlas.BULLETS_BASIC, "pFlower7"),

	BULLET_GEMLET_BROWN(ETextureAtlas.BULLETS_BASIC, "pGemlet0"),
	BULLET_GEMLET_BLUE(ETextureAtlas.BULLETS_BASIC, "pGemlet1"),
	BULLET_GEMLET_RED(ETextureAtlas.BULLETS_BASIC, "pGemlet2"),
	BULLET_GEMLET_GREEN(ETextureAtlas.BULLETS_BASIC, "pGemlet3"),;

	private final static Logger LOG = Logger.getLogger(ETexture.class);
	private final static EnumMap<ETexture, AtlasRegion> textureCache = new EnumMap<ETexture, AtlasRegion>(
			ETexture.class);

	private String filename;
	private String basename = null;
	private ETextureAtlas textureAtlas = null;
	private float scale = 1.0f;

	private ETexture(String f) {
		filename = f;
	}

	private ETexture(String f, float s) {
		filename = f;
		scale = s;
	}

	private ETexture(String dir, String name) {
		filename = dir;
		basename = name;
	}

	private ETexture(String dir, String name, float s) {
		filename = dir;
		basename = name;
		scale = s;
	}

	private ETexture(ETextureAtlas ta, String f) {
		textureAtlas = ta;
		filename = f;
	}

	private ETexture(ETextureAtlas ta, String f, float s) {
		textureAtlas = ta;
		filename = f;
		scale = s;
	}

	public static void clearAll() {
		LOG.debug("clearing all textures");
		for (ETexture txt : textureCache.keySet()) {
			txt.clear();
		}
	}

	public PoolableAtlasSprite asSprite() {
		final AtlasRegion ar = ResourceCache.getTexture(this);
		if (ar == null)
			return null;
		final PoolableAtlasSprite sprite = SpritePool.getInstance().obtain(ar);
		sprite.setScale(scale);
		return sprite;
	}

	@Override
	public AtlasRegion getObject() {
		if (textureAtlas != null) {
			TextureAtlas ta = ResourceCache.getTextureAtlas(textureAtlas);
			if (ta == null)
				return null;
			AtlasRegion ar = ta.findRegion(filename);
			if (ar == null)
				LOG.error("could not locate or open resource: " + String.valueOf(this));
			return ar;
		} else {
			String path;
			if (basename != null) {
				// localizable images
				path = filename + File.separator + I18n.getShortName() + File.separator + basename;
			} else
				path = filename;
			FileHandle fileHandle = Gdx.files.internal(path);
			try {
				Texture t = new Texture(fileHandle);
				TextureRegion tr = new TextureRegion(t);
				AtlasRegion ar = new AtlasRegion(tr.getTexture(), tr.getRegionX(), tr.getRegionY(), tr.getRegionWidth(),
						tr.getRegionHeight());
				return ar;
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
		AtlasRegion ar = textureCache.get(this);
		if (ar != null)
			ar.getTexture().dispose();
		textureCache.remove(this);
	}

	@Override
	public EnumMap<ETexture, AtlasRegion> getMap() {
		return textureCache;
	}

	public float getOriginalScale() {
		return scale;
	}

	@Override
	public void clearAllOfThisKind() {
		ETextbox.clearAll();
	}
}