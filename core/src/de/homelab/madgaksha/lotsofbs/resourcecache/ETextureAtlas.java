package de.homelab.madgaksha.lotsofbs.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * For loading, caching and disposing {@link TextureAtlas} resources.
 * 
 * @author madgaksha
 *
 */
public enum ETextureAtlas implements IResource<ETextureAtlas, TextureAtlas> {
	NINE_PATCHES("9patch/packed/9patchPacked.atlas"),
	MISC("texture/misc/packed/miscPacked.atlas"),
	PARTICLE_EFFECTS("particle/packed/particleEffectPacked.atlas"),
	STATUS_SCREEN("statusscreen/packed/statusScreenPacked.atlas"),
	
	//======================================
	//            TESTING ONLY
	//======================================	
	TEST_POOLABLE_SPRITE_TEST("test/poolableSpriteTest/packed/poolableSpriteTest.atlas"),
	
	//======================================
	//            SPECIAL ATTACKS
	//======================================
	OUGI_OUKA_MUSOUGEKI("cutscene/fancyScene/ougiOukaMusougeki/packed/ougiPacked.atlas"),

	//======================================
	//                BULLETS
	//======================================
	BULLETS_BASIC("bullets/basic-packed/bulletsBasic.atlas"),
	BULLETS_LARGE("bullets/large-packed/bulletsLarge.atlas"),
	
	//======================================
	//                FACES
	//======================================
	FACES_ESTELLE("texture/face-packed/estellePacked.atlas"),
	FACES_JOSHUA("texture/face-packed/joshuaPacked.atlas"),
	
	//======================================
	//               SPRITES
	//======================================
	ESTELLE("sprite/packed/estelle.atlas"),
	JOSHUA("sprite/packed/joshua.atlas"),
	SOLDIER_RED("sprite/packed/soldierred.atlas"),
	SOLDIER_GREEN("sprite/packed/soldiergreen.atlas"),
	ICER("sprite/packed/icer.atlas"),
	;

	private final static Logger LOG = Logger.getLogger(ETextureAtlas.class);
	private final static EnumMap<ETextureAtlas, TextureAtlas> textureAtlasCache = new EnumMap<ETextureAtlas, TextureAtlas>(
			ETextureAtlas.class);

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
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
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

	@Override
	public void clearAllOfThisKind() {
		ETextureAtlas.clearAll();
	}
}