package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.ETrigger;


public abstract class EnemyMaker extends AEntityMaker implements IBehaviour {

	public EnemyMaker(Shape2D shape, ETrigger trigger) {
		super();
		Component triggerComponent = MakerUtils.makeTrigger(trigger, shape);
		this.add(triggerComponent);
		if (triggerComponent != null) this.add(triggerComponent);
		initializeEnemy();
	}

	@Override
	public void reinitializeEntity() {
	}

	@Override
	public void behave() {
		// TODO Auto-generated method stub
	}
	
	protected abstract void initializeEnemy();
	
}
