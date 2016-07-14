package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.audiosystem.VoicePlayer;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;

/**
 * Component for entities that can speak.
 * 
 * @author mad_gaksha
 */
public class VoiceComponent implements Component, Poolable {
	public ESound onLightDamage = null;
	public ESound onHeavyDamage = null;
	public ESound onLightHeal = null;
	public ESound onHeavyHeal = null;
	public ESound onBattleModeStart = null;
	public ESound onBattleModeExit = null;
	public ESound onBattleModeFlee = null;
	public ESound onEnemyKilled = null;
	public ESound onSpawn = null;
	public ESound onDeath = null;
	public ESound onConsumableUse = null;
	public VoicePlayer voicePlayer = null;

	public VoiceComponent() {
	}

	public VoiceComponent(VoicePlayer voicePlayer) {
		this.voicePlayer = voicePlayer;
	}

	@Override
	public void reset() {
		onLightDamage = null;
		onHeavyDamage = null;
		onBattleModeStart = null;
		onBattleModeExit = null;
		onBattleModeFlee = null;
		onEnemyKilled = null;
		onSpawn = null;
		onDeath = null;
		onConsumableUse = null;
		if (voicePlayer != null)
			voicePlayer.dispose();
		voicePlayer = null;
	}
	
	public static enum VoiceRetriever {
		onLightDamage {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onLightDamage;
			}
		},
		onHeavyDamage {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onHeavyDamage;
			}
		},
		onLightHeal {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onLightHeal;
			}
		},
		onHeavyHeal {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onHeavyHeal;
			}
		},
		onBattleModeExit {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onBattleModeExit;
			}
		},
		onBattleModeFlee {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onBattleModeFlee;
			}
		},
		onBattleModeStart {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onBattleModeStart;
			}
		},
		onDeath {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onDeath;
			}
		},
		onEnemyKilled {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onEnemyKilled;
			}
		},
		onSpawn {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onSpawn;
			}
		},
		onConsumableUse{
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onConsumableUse;
			}
		},
		;
		public abstract ESound fetch(VoiceComponent vc);
	}

}
