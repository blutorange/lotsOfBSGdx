package de.homelab.madgaksha.lotsofbs.resourcepool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETextureAtlas;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public enum EParticleEffect implements IPooledResource<ParticleEffect, PooledEffect> {
	DEFAULT("particle/default.p", ETextureAtlas.PARTICLE_EFFECTS),
	FIRE_THROWER("particle/fireThrower.p", ETextureAtlas.PARTICLE_EFFECTS),
	SMALL_FIRE("particle/smallFire.p", ETextureAtlas.PARTICLE_EFFECTS),
	ENEMY_APPEAR_FLASH("particle/enemyAppearFlash.p", ETextureAtlas.PARTICLE_EFFECTS),
	ENEMY_DIE_SPLASH("particle/enemyDieSplash.p", ETextureAtlas.PARTICLE_EFFECTS),
	PLAYER_BATTLE_MODE_ENTER_BURST("particle/playerBattleModeEnterBurst.p", ETextureAtlas.PARTICLE_EFFECTS),
	PLAYER_BATTLE_MODE_EXIT_BURST("particle/playerBattleModeExitBurst.p", ETextureAtlas.PARTICLE_EFFECTS),
	BATTLE_MODE_ENTER_CUT_IN("particle/battleModeActivate.p", ETextureAtlas.PARTICLE_EFFECTS),
	DEFAULT_PLAYER_DEATH("particle/deathEffectRed.p", ETextureAtlas.PARTICLE_EFFECTS),
	ALL_MY_ITEM_ARE_BELONG_TO_ME("particle/itemGetFountain.p", ETextureAtlas.PARTICLE_EFFECTS),
	NPC_DISAPPEAR("particle/npcDisappear.p", ETextureAtlas.PARTICLE_EFFECTS),

	OUGI_OUKA_MUSOUGEKI_EXPLOSION(
			"cutscene/fancyScene/ougiOukaMusougeki/res/explosion.p",
			ETextureAtlas.OUGI_OUKA_MUSOUGEKI),
	OUGI_OUKA_MUSOUGEKI_SIMPLE_HIT(
			"cutscene/fancyScene/ougiOukaMusougeki/res/simpleHit.p",
			ETextureAtlas.OUGI_OUKA_MUSOUGEKI),
	OUGI_OUKA_MUSOUGEKI_SIMPLE_HIT2(
			"cutscene/fancyScene/ougiOukaMusougeki/res/simpleHit2.p",
			ETextureAtlas.OUGI_OUKA_MUSOUGEKI),
	OUGI_OUKA_MUSOUGEKI_JUMP_DUST(
			"cutscene/fancyScene/ougiOukaMusougeki/res/jumpDust.p",
			ETextureAtlas.OUGI_OUKA_MUSOUGEKI),

	;

	private final static Logger LOG = Logger.getLogger(EParticleEffect.class);

	private final String fileName;
	private final ETextureAtlas textureAtlas;
	private final String atlasPrefix;
	private final int initialCapacity;
	private final int maximumCapacity;

	private EParticleEffect(String fn, ETextureAtlas ta) {
		this(fn, ta, null, 2, 20);
	}

	/**
	 * 
	 * @param fn
	 *            Path to the particle effect file (*.p).
	 * @param ta
	 *            Texture atlas containing the emitter images.
	 * @param ap
	 *            Atlas prefix. See
	 *            {@link ParticleEffect#load(FileHandle, TextureAtlas, String)}.
	 *            Can be null.
	 * @param init
	 *            ParticleEffectPool initial.
	 * @param init
	 *            ParticleEffectPool maximum.
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
		if (atlas == null)
			return null;
		try {
			final ParticleEffect particleEffect = new ParticleEffect();
			particleEffect.load(handle, atlas, atlasPrefix);
			return particleEffect;
		} catch (Exception e) {
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

	public ETextureAtlas getTextureAtlas() {
		return textureAtlas;
	}

}