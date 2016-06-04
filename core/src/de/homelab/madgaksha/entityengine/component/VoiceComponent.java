package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.audiosystem.VoicePlayer;
import de.homelab.madgaksha.resourcecache.ESound;

/**
 * Component for entities that can speak.
 * 
 * @author mad_gaksha
 */
public class VoiceComponent implements Component, Poolable {
	public ESound onLightDamage = null;
	public ESound onHeavyDamage = null;
	public ESound onBattleModeStart = null;
	public ESound onBattleModeExit = null;
	public ESound onBattleModeFlee = null;
	public ESound onEnemyKilled = null;
	public ESound onSpawn = null;
	public ESound onDeath = null;
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
		if (voicePlayer != null) voicePlayer.dispose();
		voicePlayer = null;
	}

}
