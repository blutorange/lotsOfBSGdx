package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.audiosystem.VoicePlayer;
import de.homelab.madgaksha.resourcecache.EMusic;

/**
 * Component for entities that can speak.
 * 
 * @author mad_gaksha
 */
public class VoiceComponent implements Component, Poolable {
	public EMusic onLightDamage = null;
	public EMusic onHeavyDamage = null;
	public EMusic onBattleModeStart = null;
	public EMusic onSpawn = null;
	public EMusic onDeath = null;
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
		onSpawn = null;
		onDeath = null;
		if (voicePlayer != null) voicePlayer.dispose();
		voicePlayer = null;
	}

}
