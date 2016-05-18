package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

public enum EParticleEffect implements IResource {
	FOO_EFFECT("particle/sparkleEffect.p", ETextureAtlas.PARTICLE_EFFECTS, null);

	private final static Logger LOG = Logger.getLogger(EParticleEffect.class);
	private final static EnumMap<EParticleEffect, ParticleEffect> particleEffectCache = new EnumMap<EParticleEffect, ParticleEffect>(EParticleEffect.class);

	private String fileName;
	private ETextureAtlas textureAtlas;
	private String atlasPrefix;

	/**
	 * 
	 * @param fn Path to the particle effect file (*.p).
	 * @param ta Texture atlas containing the emitter images. 
	 * @param ap Atlas prefix. See {@link ParticleEffect#load(FileHandle, TextureAtlas, String)}. Can be null.
	 */
	private EParticleEffect(String fn, ETextureAtlas ta, String ap) {
		fileName = fn;
		textureAtlas = ta;
		atlasPrefix = ap;
	}

	public static void clearAll() {
		LOG.debug("clearing all particle effects");
		for (EParticleEffect pe : particleEffectCache.keySet()) {
			pe.clear();
		}
	}

	@Override
	public Enum<EParticleEffect> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_PARTICLE_EFFECT;
	}

	@Override
	public ParticleEffect getObject() {
		final FileHandle handle = Gdx.files.internal(fileName);
		final TextureAtlas atlas = ResourceCache.getTextureAtlas(textureAtlas);
		if (atlas == null) return null;
		try {
			final ParticleEffect particleEffect = new ParticleEffect();
			particleEffect.load(handle, atlas, atlasPrefix);
			return particleEffect;
		} catch (GdxRuntimeException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
			return null;
		}
	}

	@Override
	public void clear() {
		LOG.debug("clearing particle effect: " + String.valueOf(this));
		final ParticleEffect pe = particleEffectCache.get(this);
		if (pe != null)
			pe.dispose();
		particleEffectCache.remove(this);
	}

	@Override
	public EnumMap<EParticleEffect, ParticleEffect> getMap() {
		return particleEffectCache;
	}

}