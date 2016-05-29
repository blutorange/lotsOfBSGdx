package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Contains status values for player and enemy entities.
 * @author madgaksha
 *
 */
public class StatusValuesComponent implements Component, Poolable {
	private final static long DENOMINATOR = 1000L;
	private final static long DEFAULT_BULLET_RESISTANCE_NUM = 1;
	private final static long DEFAULT_BULLET_ATTACK_NUM = 1;
	
	public long bulletResistanceNum = DEFAULT_BULLET_RESISTANCE_NUM;
	public long bulletAttackNum = DEFAULT_BULLET_ATTACK_NUM;
	
	public StatusValuesComponent(){
	}
	
	public StatusValuesComponent(float bulletAttack, float bulletResistance) {
		setup(bulletAttack, bulletResistance);
	}
	
	public void setup(float bulletAttack, float bulletResistance) {
		// Maximum hit points anybody can have are 10^12.
		// Attack and resistance scale won't be larger than 10^3.
		// Largest number during damage calculation is thus 10^18.
		// Largest number long can support is 2^63-1 ~ 9*10^18
		bulletAttackNum = (int)(bulletAttack*DENOMINATOR);
		bulletResistanceNum = (int)(bulletResistance*DENOMINATOR);
	}
	
	@Override
	public void reset() {
		bulletAttackNum = DEFAULT_BULLET_ATTACK_NUM;
		bulletResistanceNum = DEFAULT_BULLET_RESISTANCE_NUM;
	}
}
