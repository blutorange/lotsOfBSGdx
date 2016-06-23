package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * A component for entities that leave or enter battle mode at a certain
 * distance to another entity.
 * 
 * @author mad_gaksha
 */
public class BattleDistanceComponent implements Component, Poolable {
	private final static float DEFAULT_BATTLE_IN_SQUARED = 1.0f;
	private final static float DEFAULT_BATTLE_OUT_SQUARED = 1.0f;

	public float battleInSquared = DEFAULT_BATTLE_IN_SQUARED;
	public float battleOutSquared = DEFAULT_BATTLE_OUT_SQUARED;
	public Entity relativeToEntity = null;

	public BattleDistanceComponent() {
	}

	public BattleDistanceComponent(Entity relativeToEntity, float battleIn, float battleOut) {
		setup(relativeToEntity, battleIn, battleOut);
	}

	public void setup(Entity relativeToEntity, float battleIn, float battleOut) {
		this.relativeToEntity = relativeToEntity;
		this.battleInSquared = battleIn * battleIn;
		this.battleOutSquared = battleOut * battleOut;
	}

	@Override
	public void reset() {
		battleInSquared = DEFAULT_BATTLE_IN_SQUARED;
		battleOutSquared = DEFAULT_BATTLE_OUT_SQUARED;
		relativeToEntity = null;
	}
}
