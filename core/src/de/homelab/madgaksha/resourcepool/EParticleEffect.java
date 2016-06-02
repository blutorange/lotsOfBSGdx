package de.homelab.madgaksha.resourcepool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETextureAtlas;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public enum EParticleEffect implements IPooledResource<ParticleEffect, PooledEffect> {
	FIRE_THROWER("particle/fireThrower.p", ETextureAtlas.PARTICLE_EFFECTS, null, 2, 20),
	SMALL_FIRE("particle/smallFire.p", ETextureAtlas.PARTICLE_EFFECTS, null, 2, 20),
	PLAYER_BATTLE_MODE_ENTER_BURST("particle/playerBattleModeEnterBurst.p", ETextureAtlas.PARTICLE_EFFECTS, null, 2, 20),
	BATTLE_MODE_ENTER_CUT_IN("particle/battleModeActivate.p", ETextureAtlas.PARTICLE_EFFECTS, null, 2, 20),
	DEFAULT_PLAYER_DEATH("particle/deathEffectRed.p", ETextureAtlas.PARTICLE_EFFECTS, null, 2, 20),
	ALL_MY_ITEM_ARE_BELONG_TO_ME("particle/itemGetFountain.p", ETextureAtlas.PARTICLE_EFFECTS, null, 2, 20)
	
	;

	private final static Logger LOG = Logger.getLogger(EParticleEffect.class);

	private final String fileName;
	private final ETextureAtlas textureAtlas;
	private final String atlasPrefix;
	private final int initialCapacity;
	private final int maximumCapacity;
		
	/**
	 * 
	 * @param fn Path to the particle effect file (*.p).
	 * @param ta Texture atlas containing the emitter images. 
	 * @param ap Atlas prefix. See {@link ParticleEffect#load(FileHandle, TextureAtlas, String)}. Can be null.
	 * @param init ParticleEffectPool initial.
	 * @param init ParticleEffectPool maximum.
	 */
	private EParticleEffect(String fn, ETextureAtlas ta, String ap, int init, int max) {
		fileName = fn;
		textureAtlas = ta;
		atlasPrefix = ap;
		initialCapacity = init;
		maximumCapacity = max;
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
		} catch (IllegalArgumentException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
			return null;
		}
	}

	@Override
	public int getInitialCapacity() {
		return initialCapacity;
	}

	@Override
	public int getMaximumCapacity() {
		return maximumCapacity;
	}

	@Override
	public ParticleEffectPool getPool() {
		return new ParticleEffectPool(getObject(), initialCapacity, maximumCapacity);
	}

}